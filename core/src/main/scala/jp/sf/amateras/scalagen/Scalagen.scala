package jp.sf.amateras.scalagen

/**
 * The bootstrap class of Scalagen.
 */
object Scalagen {

  def runWith[T <: Generator](settings: Settings)(implicit m: scala.reflect.Manifest[T]): Unit = {
    val c = m.erasure.getConstructor(classOf[Settings])
    c.newInstance(settings) match {
      case generator: Generator => generator.generate(new SchemaLoader(settings).loadSchema())
    }
  }

}