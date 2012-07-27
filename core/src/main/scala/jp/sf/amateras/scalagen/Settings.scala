package jp.sf.amateras.scalagen

import java.io.File

case class Settings(
  generator: Generator,
  url: String,
  driver: String,
  username: String,
  password: String,
  catalog: String = "%",
  schemaPattern: String = "%",
  tablePattern: String = "%",
  packageName: String = "models",
  targetDir: File = new File("src/main/scala"),
  charset: String = "UTF-8",
  typeMappings: Map[Int, String] = DataTypes.defaultMappings
)