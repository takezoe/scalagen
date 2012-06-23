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
    val c = m.erasure.getConstructor(classOf[Settings])
    c.newInstance(settings) match {
      case generator: Generator => generator.generate(new SchemaLoader(settings).loadSchema())
    }
  }

}