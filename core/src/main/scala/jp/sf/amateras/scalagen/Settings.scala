package jp.sf.amateras.scalagen

case class Settings(
    url: String,
    username: String,
    password: String,
    catalog: String = "%",
    schemaPattern: String = "%",
    tablePattern: String = "%",
    generator: Generator)