scalagen
========

The source code generator for Scala ORMs.

The current version of Scalagen support [ScalaQuery](http://scalaquery.org/) and [Anorm](http://www.playframework.org/modules/scala-0.9.1/anorm).
It's possible to generate Table objects and case classes for ScalaQuery by the following code.

```scala
import jp.sf.amateras.scalagen._

Scalagen.generate(Settings(
  generator = new ScalaQueryGenerator(),
  driver = "org.hsqldb.jdbcDriver",
  url = "jdbc:hsqldb:hsql://localhost/",
  username = "sa",
  password = "",
  catalog = null,
  schemaPattern = null,
  tablePattern = null))
```

Source files would be generated into ```/src/main/scala/models```.

Scalagen is still under development. So it has never been published to the public repository.
You can get source code and build it from this git repository.

##Configuration

You can configure Scalagen via ```jp.sf.amateras.scalagen.Settings```.

property           | type            | description
-------------------|-----------------|------------------------------------------------
generator          | Generator       | generator instance (required)
driver             | String          | JDBC driver classname (required)
url                | String          | JDBC connection url (required)
username           | String          | JDBC connection username (required)
password           | String          | JDBC connection password (required)
catalog            | String          | catalog (default is "%")
schemaPattern      | String          | schema pattern (default is "%")
tablePattern       | String          | table pattern (default is "%")
includeTablePattern| String          | regular expression which matches included tables (default is "")
excludeTablePattern| String          | regular expression which matches excluded tables (default is "")
packageName        | String          | package name of generated source (default is "models")
targetDir          | File            | output directory of generated source (default is new File("src/main/scala"))
charset            | String          | chaarset of generated source (default is "UTF-8")
typeMappings       | Map[Int, String]| mappings of SQL type to Scala type (default is DataTypes.defaultMappings)

###Generators

Scalagen supports following ORMs. Specify a generator which corresponds to your ORM at ```Settings#generator```.

framework    | artifact            | generator classname
-------------|---------------------|------------------------------------------------
ScalaQuery   | scalagen-scalaquery | jp.sf.amateras.scalagen.ScalaQueryGenerator
Anorm        | scalagen-anorm      | jp.sf.amateras.scalagen.AnormGenerator
Squeryl      | TBD                 | TBD
mirage-scala | TBD                 | TBD

Scalagen also provides ```ScalateGenerator``` to generate source code by Scalate template which is written by users.

You can generate source code using your own Scalate template without Generator implementing by using this class as Generator.
This is a configuration example of ```ScalateGenerator```:

```scala
import jp.sf.amateras.scalagen._

Scalagen.generate(Settings(
  generator = new ScalateGenerator(new java.io.File("template/my.ssp")),
  ...
)
```

Following variables are available in the specified Scalate template file:

variable name | type                        
--------------|--------------------------------
table         |jp.sf.amateras.scalagen.Table
settings      |jp.sf.amateras.scalagen.Settings

##sbt-plugin

Scalagen could be used as sbt-plugin. In ```project/plugin.sbt```, add:

```scala
resolvers += ("amateras snapshot" at "http://amateras.sourceforge.jp/mvn-snapshot/")

addSbtPlugin("jp.sf.amateras.scalagen" % "scalagen-sbtplugin" % "0.1-SNAPSHOT")

libraryDependencies ++= Seq(
  // for ScalaQuery
  "jp.sf.amateras.scalagen" %% "scalagen-scalaquery" % "0.1-SNAPSHOT",
  // for Anorm
  //"jp.sf.amateras.scalagen" %% "scalagen-anorm" % "0.1-SNAPSHOT",
  // JDBC driver for your database
  "org.hsqldb" % "hsqldb" % "2.2.8"
)
```

In ```build.sbt```, add following configurations:

```scala
seq(jp.sf.amateras.scalagen.ScalagenPlugin.scalagenSettings: _*)

scalagenConfiguration := jp.sf.amateras.scalagen.Settings(
  // for ScalaQuery
  generator = new jp.sf.amateras.scalagen.ScalaQueryGenerator(),
  // for Anorm
  //generator = new jp.sf.amateras.scalagen.ScalaQueryGenerator(),
  driver = "org.hsqldb.jdbcDriver",
  url = "jdbc:hsqldb:hsql://localhost/",
  username = "sa",
  password = "",
  catalog = null,
  schemaPattern = null,
  tablePattern = null
)
```

Execute ```sbt scalagen```. Source files for ScalaQuery are generated into ```src/main/scala/models```.

## Customization

You can write your own Generator implementations based on ```jp.sf.amateras.scalagen.GeneratorBase```.
Scalagen provides some base classes to help implementing Generator.

### jp.sf.amateras.scalagen.GeneratorBase

```GeneratorBase``` is a most basic base class for Generators. Implements ```generate(settings: Settings, table: Table): String``` method 
which returns the source code for the given table.

See [ScalaQueryGenerator](https://github.com/takezoe/scalagen/blob/master/scalaquery/src/main/scala/jp/sf/amateras/scalagen/ScalaQueryGenerator.scala)
as an example of Generator implementation which is based on ```GeneratorBase```.

### jp.sf.amateras.scalagen.ScalateGeneratorBase

```ScalateGeneratorBase``` is a base class for Generators which generate source code by Scalate template.

See [AnormGeenrator](https://github.com/takezoe/scalagen/blob/master/anorm/src/main/scala/jp/sf/amateras/scalagen/AnormGenerator.scala) and
[its template](https://github.com/takezoe/scalagen/blob/master/anorm/src/main/resources/jp/sf/amateras/scalagen/AnormGenerator.ssp) 
as an example of Generator implementation which is base on ```ScalateGenerator```.
