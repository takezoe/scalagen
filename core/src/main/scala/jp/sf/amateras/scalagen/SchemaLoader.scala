package jp.sf.amateras.scalagen

import java.sql._
import jp.sf.amateras.scala.util.Resources._

class SchemaLoader(settings: Settings) {

  implicit private def extendsResultSet(rs: ResultSet) = new {
    def toStream[T](handler: (ResultSet) => T): Stream[T] = rs.next match {
      case false => Stream.empty
      case true  => handler(rs) #:: toStream(handler)
    }
  }

  def loadSchema(): List[Table] = {
    using(DriverManager.getConnection(
        settings.url, settings.username, settings.password)){ conn =>
      using(conn.getMetaData().getTables(
          settings.catalog, settings.schemaPattern, settings.tablePattern, null)){ rs =>
        rs.toStream { rs =>
          rs.getString("TABLE_TYPE") match {
            case "TABLE" => Some(rs.getString("TABLE_NAME"))
            case _ => None
          }
        }.flatten.map { case name =>
          loadTable(conn, name)
        }.toList
      }
    }
  }

  protected def loadTable(conn: Connection, name: String): Table = {
    val columns = using(conn.getMetaData().getColumns(
        settings.catalog, settings.schemaPattern, settings.tablePattern, "%")){ rs =>
      rs.toStream { rs =>
        try {
          Some(Column(
            rs.getString("COLUMN_NAME"),
            DataTypes.toClass(rs.getInt("DATA_TYPE")),
            rs.getInt("NULLABLE") match {
              case DatabaseMetaData.columnNullable => true
              case _ => false
            }
          ))
        } catch {
          case ex: IllegalArgumentException => None
        }
      }.flatten.toList
    }

    Table(name, columns)
  }

}