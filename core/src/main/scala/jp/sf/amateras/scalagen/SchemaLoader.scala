package jp.sf.amateras.scalagen

import java.sql._
import jp.sf.amateras.scala.util.Resources._

class SchemaLoader(settings: Settings) {
  import settings._

  /**
   * A utility method to process ResultSet.
   */
  implicit private def extendsResultSet(rs: ResultSet) = new {
    def process[T](handler: (ResultSet) => T): Seq[T] = rs.next match {
      case false => Stream.empty
      case true  => handler(rs) +: process(handler)
    }
  }

  def loadSchema(): List[Table] = {
    using(DriverManager.getConnection(url, username, password)){ conn =>
      using(conn.getMetaData().getTables(catalog, schemaPattern, tablePattern, null)){ rs =>
        rs.process { rs =>
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
    val primaryKeys = using(conn.getMetaData().getPrimaryKeys(catalog, schemaPattern, name)){ rs =>
      rs.process { _.getString("COLUMN_NAME") }
    }

    val columns = using(conn.getMetaData().getColumns(catalog, schemaPattern, name, "%")){ rs =>
      rs.process { rs =>
        try {
          val columnName = rs.getString("COLUMN_NAME")

          Some(Column(
            columnName,
            DataTypes.toClass(rs.getInt("DATA_TYPE")),
            rs.getInt("NULLABLE") match {
              case DatabaseMetaData.columnNullable => true
              case _ => false
            },
            primaryKeys.contains(columnName)
          ))

        } catch {
          case ex: IllegalArgumentException => None
        }
      }.flatten.toList
    }

    Table(name, columns)
  }

}