import sbt._
import Keys._
import java.io.File

object Build extends Build {

  val curDir = new File(".")

  lazy val root = Project(id = "scalagen",
    base = file("."),
    settings = commonSettings ++ Seq(
      publishArtifact := false)) aggregate (core, scalaquery)

  lazy val core = Project(id = "scalagen-core",
    base = file("core"),
    settings = commonSettings ++ Seq(
      sbtPlugin := false,
      libraryDependencies ++= Seq(
        "com.typesafe" % "config" % "0.4.1",
        "jp.sf.amateras" %% "scala-utils" % "0.0.1-SNAPSHOT",
        "org.scala-tools.testing" % "specs_2.8.1" % "1.6.8" % "test",
        "org.mockito" % "mockito-core" % "1.8.5" % "test"
      )
    )
  )

  lazy val scalaquery = Project(id = "scalagen-scalaquery",
    base = file("scalaquery"),
    settings = commonSettings ++ Seq(
      sbtPlugin := false,
      libraryDependencies ++= Seq(
        "com.typesafe" % "config" % "0.4.1",
        "jp.sf.amateras" %% "scala-utils" % "0.0.1-SNAPSHOT",
        "org.scala-tools.testing" % "specs_2.8.1" % "1.6.8" % "test",
        "org.mockito" % "mockito-core" % "1.8.5" % "test"
      )
    )
  ) .dependsOn(core)

  def commonSettings = Defaults.defaultSettings ++
    Seq(
      organization := "jp.sf.amateras.scalagen",
      resolvers += ("amateras snapshot" at "http://amateras.sourceforge.jp/mvn-snapshot/"),

      //publishArtifact in (Compile, packageDoc) := false,
      //publishArtifact in Test := false,

      publishTo <<= (version) { version: String =>
        val repoInfo =
          if (version.trim.endsWith("SNAPSHOT"))
            ("amateras snapshots" -> "/home/groups/a/am/amateras/htdocs/mvn-snapshot/")
          else
            ("amateras releases" -> "/home/groups/a/am/amateras/htdocs/mvn/")

        Some(Resolver.ssh(repoInfo._1, "shell.sourceforge.jp", repoInfo._2) as(
            System.getProperty("user.name"), (Path.userHome / ".ssh" / "id_rsa").asFile) withPermissions("0664"))
      },

      publishMavenStyle := true
    )
}