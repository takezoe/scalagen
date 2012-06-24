package jp.sf.amateras.scalagen

import jp.sf.amateras.scalagen._

class AnormGenerator extends GeneratorBase {

  def generate(settings: Settings, table: Table): String = {
    import settings._

    if(packageName == ""){
      ""
    } else {
      "package " + packageName + "\n\n"
    } +
    "import import anorm._\n" +
    "import anorm.SqlParser._\n\n" +
    "case class " + table.className + "(\n" +
    table.columns.map { column => "  " + column.propertyName + ": " + propertyType(column) }.mkString(",\n") +
    "\n" +
    ")\n\n"
    "object " + table.className + "{\n" +
    "  val simple = {" +
    table.columns.map { column =>
    "    get[" + propertyType(column) + "](\"" + column.name + "\"),"
    }.mkString("\n") + "map {" +
    "      case " + table.columns.map(_.propertyName).mkString("~") + " => " + table.className + "(" + table.columns.map(_.propertyName) + ")\n" +
    "    }\n" +
    "  }\n" +
    "}\n\n"
  }

  private def propertyType(column: Column): String =
    if(column.primaryKey){
      "Pk[" + column.typeName + "]"
    } else if(column.nullable) {
      "Option[" + column.typeName + "]"
    } else {
      column.typeName
    }

}