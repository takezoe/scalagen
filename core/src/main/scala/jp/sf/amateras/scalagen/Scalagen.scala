package jp.sf.amateras.scalagen

/**
 * The bootstrap class of Scalagen.
 *
 * {{{
 * Scalagen.generateFor[ScalaQuery](Settings(
 *       url = "jdbc:hsqldb:hsql://localhost/",
 *       username = "sa",
 *       password = "",
 *       catalog = null,
 *       schemaPattern = null,
 *       tablePattern = null,
 *       packageName = "entities"))
 * }}}
 */
object Scalagen {

  def generateFor[T <: Generator](settings: Settings)(implicit m: scala.reflect.Manifest[T]): Unit = {
    m.erasure.newInstance() match {
      case generator: Generator => generator.generate(settings, new SchemaLoader(settings).loadSchema())
    }
  }

}