package jp.sf.amateras.scalagen

/**
 * The table model.
 *
 * @param name the table name
 * @param columns the list of column
 */
case class Table(name: String, columns: List[Column]){

  val className: String = {
    val sb = new StringBuilder
    for(i <- 0 to name.length - 1){
      val c = name.charAt(i)
      if(i == 0){
        sb.append(String.valueOf(c).toUpperCase())
      } else if(c != '_'){
        if(i > 0 && name.charAt(i - 1) == '_'){
          sb.append(String.valueOf(c).toUpperCase())
        } else {
          sb.append(String.valueOf(c).toLowerCase())
        }
      }
    }
    sb.toString()
  }

}

/**
 * The column model.
 *
 * @param name the column name
 * @param dataType the date type of the column
 * @param nullable the nullable flag
 */
case class Column(name: String, dataType: Class[_], nullable: Boolean, primaryKey: Boolean){

  val propertyName: String = {
    val sb = new StringBuilder
    for(i <- 0 to name.length - 1){
      val c = name.charAt(i)
      if(c != '_'){
        if(i > 0 && name.charAt(i - 1) == '_'){
          sb.append(String.valueOf(c).toUpperCase())
        } else {
          sb.append(String.valueOf(c).toLowerCase())
        }
      }
    }
    sb.toString()
  }

  val typeName: String = {
    val className = dataType.getName()

    if(className == "java.lang.String"){
      "String"
    } else if(className.indexOf('.') >= 0){
      className
    } else {
      className.capitalize
    }
  }

}
