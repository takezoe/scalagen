package jp.sf.amateras.scalagen

import java.io.File
import jp.sf.amateras.scala.util.io._

/**
 * A trait for source code generators.
 */
trait Generator {

  /**
   * Generates source code.
   */
  def generate(tables: List[Table]): Unit

}

/**
 * A base class for source code generators.
 */
abstract class GeneratorBase(settings: Settings) extends Generator {

  def generate(tables: List[Table]): Unit = {
    tables.foreach { table =>
      val generateFileInfo = generate(table)
      val file = new File(generateFileInfo.outputDir, generateFileInfo.filename)
      file.write(generateFileInfo.content, settings.charset)
    }
  }

  /**
   * Returns information of the source file for the given table model.
   * Implement this method at the subclass.
   */
  def generate(table: Table): GenerateFileInfo

  case class GenerateFileInfo(outputDir: File, filename: String, content: String)

}