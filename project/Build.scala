import sbt._
import Keys._
import java.io.File
import com.typesafe.sbteclipse.plugin.EclipsePlugin._

object Build extends Build {

  val curDir = new File(".")

  lazy val root = Project(id = "scalagen",
    base = file("."),
    settings = commonSettings ++ Seq(
      publishArtifact := false)) aggregate (core, scalaquery, anorm, sbtplugin)

  lazy val core = Project(id = "scalagen-core",
    base = file("core"),
    settings = commonSettings ++ Seq(
      sbtPlugin := false,
      libraryDependencies ++= Seq(
        "org.fusesource.scalate" % "scalate-core" % "1.5.3",
        "jp.sf.amateras" %% "scala-utils" % "0.0.1-SNAPSHOT" changing()
      )
    )
  )

  lazy val scalaquery = Project(id = "scalagen-scalaquery",
    base = file("scalaquery"),
    settings = commonSettings ++ Seq(
      sbtPlugin := false
    )
  ) .dependsOn(core)

  lazy val anorm = Project(id = "scalagen-anorm",
    base = file("anorm"),
    settings = commonSettings ++ Seq(
      sbtPlugin := false
    )
  ) .dependsOn(core)

  lazy val sbtplugin = Project(id = "scalagen-sbtplugin",
    base = file("sbtplugin"),
    settings = commonSettings ++ Seq(
      sbtPlugin := true
    )
  ) .dependsOn(core)

  def commonSettings = Defaults.defaultSettings ++
    Seq(
      version := "0.1",
      organization := "jp.sf.amateras.scalagen",
      resolvers += ("amateras snapshot" at "http://amateras.sourceforge.jp/mvn-snapshot/"),

      publishTo <<= (version) { version: String =>
        val repoInfo =
          if (version.trim.endsWith("SNAPSHOT"))
            ("amateras snapshots" -> "/home/groups/a/am/amateras/htdocs/mvn-snapshot/")
          else
            ("amateras releases" -> "/home/groups/a/am/amateras/htdocs/mvn/")

        Some(Resolver.ssh(repoInfo._1, "shell.sourceforge.jp", repoInfo._2) as(
            System.getProperty("user.name"), (Path.userHome / ".ssh" / "id_rsa").asFile) withPermissions("0664"))
      },

      publishMavenStyle := true,
      EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource
    )
}
