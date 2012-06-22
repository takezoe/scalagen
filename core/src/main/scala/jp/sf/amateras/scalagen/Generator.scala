package jp.sf.amateras.scalagen

/**
 * A trait for source code generators.
 */
trait Generator {

  /**
   * Generates source code.
   */
  def generate(tables: List[Table]): Unit

}