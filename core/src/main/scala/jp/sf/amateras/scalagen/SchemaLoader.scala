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
    Class.forName(settings.driver)

    using(DriverManager.getConnection(url, username, password)){ conn =>
      using(conn.getMetaData().getTables(catalog, schemaPattern, tablePattern, null)){ rs =>
        rs.process { rs =>
          rs.getString("TABLE_TYPE") match {
            case "TABLE" => Some(rs.getString("TABLE_NAME"))
            case _ => None
          }
        }.flatten.filterNot { tableName => 
          (includeTablePattern.nonEmpty && !tableName.matches(includeTablePattern)) ||
          (excludeTablePattern.nonEmpty && tableName.matches(excludeTablePattern)) 
        }.map { name => loadTable(conn, name) }.toList
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
          val dataType = rs.getInt("DATA_TYPE")

          Some(Column(
            columnName,
            rs.getString("TYPE_NAME"),
            typeMappings.getOrElse(dataType,{
              DataTypes.toSqlType(dataType) match {
                case "UNKNOWN" => throw new IllegalArgumentException("%i is unknown type.".format(dataType))
                case sqlType   => throw new IllegalArgumentException("%s is not supported.".format(sqlType))
              }
            }),
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