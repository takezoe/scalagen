package jp.sf.amateras.scalagen

/**
 * The table model.
 *
 * @param name the table name
 * @param columns the list of column
 */
case class Table(name: String, columns: List[Column])

/**
 * The column model.
 *
 * @param name the column name
 * @param dataType the date type of the column
 * @param nullable the nullable flag
 */
case class Column(name: String, dataType: Class[_], nullable: Boolean, primaryKey: Boolean)
