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
    // overrides settings by the generator
    val modifiedSettings = settings.generator.settings(settings)
    // removes existing source files (*.scala)
    settings.targetDir.listFiles().foreach { file =>
      if(file.isFile && file.getName.endsWith(".scala")){
        file.delete
      }
    }
    // generates source files
    modifiedSettings.generator.generate(
        modifiedSettings, new SchemaLoader(modifiedSettings).loadSchema())
  }

}