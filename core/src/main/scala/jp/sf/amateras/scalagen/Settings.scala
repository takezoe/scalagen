package jp.sf.amateras.scalagen

import java.io.File

case class Settings(
    url: String,
    username: String,
    password: String,
    catalog: String = "%",
    schemaPattern: String = "%",
    tablePattern: String = "%",
    targetDir: File = new File("src/main/scala"),
    charset: String = "UTF-8")