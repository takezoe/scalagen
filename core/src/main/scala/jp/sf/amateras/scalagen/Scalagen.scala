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

  def generate(settings: Settings): Unit = {
    settings.generator.generate(settings, new SchemaLoader(settings).loadSchema())
  }

}