package jp.sf.amateras.scalagen

import sbt._
import Keys._
import java.util.jar.Attributes.Name._
import sbt.Defaults._
import sbt.Package.ManifestAttributes

object ScalagenPlugin extends Plugin {
  val scalagen = TaskKey[Unit]("scalagen", "Generates source code for ORMs")

  val scalagenConfiguration = SettingKey[Settings]("scalagen-settings")

  val scalagenSettings: Seq[Project.Setting[_]] = Seq(
    scalagen <<= (scalaInstance, scalagenConfiguration) map { (scala, settings) =>
      Scalagen.generate(settings.copy(options = settings.options 
          + ("scala.libraryJar.path" -> scala.libraryJar.getPath)))
    }
  )
}