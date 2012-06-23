package jp.sf.amateras.scalagen

import java.io._
import jp.sf.amateras.scala.util.io._

/**
 * A trait for source code generators.
 */
trait Generator {

  /**
   * Generates source code.
   */
  def generate(settings: Settings, tables: List[Table]): Unit

}

/**
 * A base class for source code generators.
 */
abstract class GeneratorBase extends Generator {

  def generate(settings: Settings, tables: List[Table]): Unit = {
    import settings._

    tables.foreach { table =>
      val source = generate(settings, table)

      val outputDir = packageName match {
        case "" => targetDir
        case _  => new File(targetDir, packageName.replace(".", "/"))
      }

      outputDir.mkdirs()

      val file = new File(outputDir, table.className + ".scala")
      file.write(source, charset)
    }
  }

  def generate(settings: Settings, table: Table): String

}