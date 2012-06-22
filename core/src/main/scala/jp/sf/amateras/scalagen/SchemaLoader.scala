package jp.sf.amateras.scalagen

import java.sql._
import jp.sf.amateras.scala.util.Resources._

case class Settings(url: String, username: String, password: String)

class SchemaLoader(settings: Settings) {

  def loadSchema(): List[Table] = {
    using(DriverManager.getConnection(settings.url, settings.username, settings.password)){ conn =>
      using(conn.getMetaData().getTables(null, null, null, null)){ rs =>
        toStream(rs){ rs =>
          (rs.getString("TABLE_NAME"))
        }.map { case (name) =>
          loadTable(conn, name)
        } toList
      }
    }
  }

  protected def loadTable(conn: Connection, name: String): Table = {
    Table(name)
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