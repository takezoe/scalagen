package jp.sf.amateras.scalagen

import jp.sf.amateras.scala.util.StringConversions._

/**
 * The table model.
 *
 * @param name the table name
 * @param columns the list of column
 */
case class Table(name: String, columns: List[Column]){

  val className: String = name.uppercamel()

}

/**
 * The column model.
 *
 * @param name the column name
 * @param typeName the column type
 * @param dataType the date type of the column
 * @param nullable the nullable flag
 * @param primaryKey true if it is a primary key
 */
case class Column(name: String, typeName: String, dataType: Class[_], nullable: Boolean, primaryKey: Boolean){

  val propertyName: String = name.lowercamel()

  val scalaType: String = {
    dataType.getName() match {
      case "java.lang.String" => "String"
      case x if(x.indexOf(".") >= 0) => x
      case x => x.capitalize
    }
  }

}
