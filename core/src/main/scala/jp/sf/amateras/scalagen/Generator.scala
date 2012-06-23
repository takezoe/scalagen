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
  def generate(tables: List[Table]): Unit

}

/**
 * A base class for source code generators.
 */
abstract class GeneratorBase(settings: Settings) extends Generator {

  def generate(tables: List[Table]): Unit = {
    val templateEngine = new TemplateEngine()

    tables.foreach { table =>
      val writer = new StringWriter()

      val renderContext = new DefaultRenderContext(null, templateEngine, new PrintWriter(writer))
      renderContext.render(templatePath, Map(
        "packageName" -> settings.packageName,
        "table"       -> table))

      val outputDir = settings.packageName match {
        case "" => settings.targetDir
        case _  => new File(settings.targetDir, settings.packageName.replace(".", "/"))
      }

      val file = new File(outputDir, table.name + ".scala")
      file.write(writer.toString(), settings.charset)
    }
  }

  val templatePath: String

}