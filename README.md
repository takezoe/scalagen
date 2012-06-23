scalagen
========

The source code generator for Scala ORMs.

The current version of Scalagen support [ScalaQuery](http://scalaquery.org/).
It possible to generate Table objects and case classes by the following code.

```scala
import jp.sf.amateras.scalagen._

Scalagen.generateFor[ScalaQuery](Settings(
  generator = new jp.sf.amateras.scalagen.ScalaQuery(),
  driver = "org.hsqldb.jdbcDriver",
  url = "jdbc:hsqldb:hsql://localhost/",
  username = "sa",
  password = "",
  catalog = null,
  schemaPattern = null,
  tablePattern = null,
  packageName = "models"))
```

Source files would be generated into ```/src/main/scala/models```.

Scalagen is still under development. So it has never been published to the public repository.
You can get source code and build it from this git repository.

##sbt-plugin

You can use Scalagen as sbt-plugin. In ```project/plugin.sbt```, add:

```scala
resolvers += ("amateras snapshot" at "http://amateras.sourceforge.jp/mvn-snapshot/")

addSbtPlugin("jp.sf.amateras.scalagen" % "scalagen-sbtplugin" % "0.1-SNAPSHOT")

libraryDependencies ++= Seq(
  // Framework module
  "jp.sf.amateras.scalagen" %% "scalagen-scalaquery" % "0.1-SNAPSHOT",
  // JDBC driver
  "org.hsqldb" % "hsqldb" % "2.2.8"
)
```

In ```build.sbt```, add following configurations:

```scala
seq(jp.sf.amateras.scalagen.ScalagenPlugin.scalagenSettings: _*)

scalagenConfiguration := jp.sf.amateras.scalagen.Settings(
  generator = new jp.sf.amateras.scalagen.ScalaQuery(),
  driver = "org.hsqldb.jdbcDriver",
  url = "jdbc:hsqldb:hsql://localhost/",
  username = "sa",
  password = "",
  catalog = null,
  schemaPattern = null,
  tablePattern = null,
  packageName = "models"
)
```

Then execute ```sbt scalagen``` at command line. Source files for ScalaQuery are generated into ```src/main/scala/models```.