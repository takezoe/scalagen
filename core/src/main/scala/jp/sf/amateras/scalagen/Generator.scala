package jp.sf.amateras.scalagen

import java.io._
import org.fusesource.scalate._
import jp.sf.amateras.scala.util.io._

/**
 * A trait for source code generators.
 */
trait Generator {

  /**
   * Modifies settings.
   */
  def settings(settings: Settings): Settings = settings

  /**
   * Generates source code.
   */
  def generate(settings: Settings, tables: List[Table]): Unit

}

/**
 * A simplest base class for source code generators.
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

/**
 * Provides Scalate support for Generator implementations.
 *
 * Generators can render a Scalate template by render() method.
 */
trait ScalateSupport {

  protected def render(settings: Settings, template: TemplateSource, attributes: Map[String, Any]): String = {
    val engine = new TemplateEngine
    engine.allowCaching =  false

    settings.options.get("scala.libraryJar.path").foreach { case (scalaLibraryJarPath: String) =>
      engine.combinedClassPath = true
      engine.classpath = scalaLibraryJarPath
    }

    engine.layout(template, attributes)
  }

}

/**
 * A base class for source code generators which generates source code using Scalate.
 */
abstract class ScalateGeneratorBase extends GeneratorBase with ScalateSupport {

  val templatePath: String

  def generate(settings: Settings, table: Table): String =
    render(settings,
        TemplateSource.fromURL(getClass().getResource(templatePath)),
        Map("table" -> table, "settings" -> settings))

}

/**
 * A generic implementation of Generator which generates source code by the specified Scalate template.
 */
class ScalateGenerator(val template: java.io.File) extends GeneratorBase with ScalateSupport {

  def generate(settings: Settings, table: Table): String =
    render(settings,
        TemplateSource.fromFile(this.template.getAbsolutePath()),
        Map("table" -> table, "settings" -> settings))

}