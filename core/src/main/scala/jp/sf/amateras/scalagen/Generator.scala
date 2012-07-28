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
 * A base class for source code generators which generates source code using Scalate.
 */
abstract class ScalateGeneratorBase extends GeneratorBase {
  
  val templatePath: String
  
  def generate(settings: Settings, table: Table): String =
    render(settings, Map("table" -> Table))
    
  def render(settings: Settings, attributes: Map[String, Any]): String = {
    val engine = new TemplateEngine
    
    settings.options.get("scala.libraryJar.path").foreach { case (scalaLibraryJarPath: String) =>
      engine.combinedClassPath = true
      engine.classpath = scalaLibraryJarPath
    }
    
    val url = getClass().getResource(templatePath)
    val template = TemplateSource.fromURL(url)
    engine.layout(template, attributes)
  }    
  
}