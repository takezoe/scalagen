package jp.sf.amateras.scalagen

import java.sql._
import jp.sf.amateras.scala.util.Resources._

case class Settings(url: String, username: String, password: String,
    catalog: String = "%", schemaPattern: String = "%", tablePattern: String = "%")

class SchemaLoader(settings: Settings) {

  def loadSchema(): List[Table] = {
    using(DriverManager.getConnection(
        settings.url, settings.username, settings.password)){ conn =>
      using(conn.getMetaData().getTables(
          settings.catalog, settings.schemaPattern, settings.tablePattern, null)){ rs =>
        toStream(rs){ rs =>
          rs.getString("TABLE_TYPE") match {
            case "TABLE" => Some(rs.getString("TABLE_NAME"))
            case _ => None
          }
        }.flatMap { x => x }.map { case name =>
          loadTable(conn, name)
        } toList
      }
    }
  }

  protected def loadTable(conn: Connection, name: String): Table = {
    val columns = {using(conn.getMetaData().getColumns(
        settings.catalog, settings.schemaPattern, settings.tablePattern, "%")){ rs =>
      toStream(rs){ rs =>
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
      }.toList}.flatMap { x => x}
    }

    Table(name, columns)
  }

  /**
   * Converts ResultSet to Stream.
   *
   * It might have to be moved to scala-utils.
   */
  private def toStream[T](rs: ResultSet)(handler: (ResultSet) => T): Stream[T] = rs.next match {
    case false => Stream.empty
    case true  => handler(rs) #:: toStream(rs)(handler)
  }

}