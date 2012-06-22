package jp.sf.amateras.scalagen

case class Table(name: String, columns: List[Column])

case class Column(name: String, dataType: Class[_], nullable: Boolean)
