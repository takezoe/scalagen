package jp.sf.amateras.scalagen

import java.io._
import jp.sf.amateras.scala.util.io._
import org.fusesource.scalate._

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

    val templateEngine = new TemplateEngine()

    tables.foreach { table =>
      val writer = new StringWriter()

      val renderContext = new DefaultRenderContext(null, templateEngine, new PrintWriter(writer))
      renderContext.render(templatePath, Map(
        "packageName" -> packageName,
        "table"       -> table))

      val outputDir = packageName match {
        case "" => targetDir
        case _  => new File(targetDir, packageName.replace(".", "/"))
      }

      outputDir.mkdirs()

      val file = new File(outputDir, table.className + ".scala")
      file.write(writer.toString(), charset)
    }
  }

  val templatePath: String

}