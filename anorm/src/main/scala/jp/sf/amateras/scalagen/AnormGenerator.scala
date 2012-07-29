package jp.sf.amateras.scalagen

import jp.sf.amateras.scalagen._

object AnormGenerator extends App {
  /**
   * Adds Column#properyType() to use in the SSP template.
   */
  implicit def extendsColumn(column: Column) = new {
    def propertyType(): String =
      if(column.primaryKey){
        "Pk[" + column.dataType + "]"
      } else if(column.nullable) {
        "Option[" + column.dataType + "]"
      } else {
        column.dataType
      }
  }
}

/**
 * A generator implementation for Anorm.
 */
class AnormGenerator extends ScalateGeneratorBase {
  val templatePath = "/jp/sf/amateras/scalagen/AnormGenerator.ssp"
}