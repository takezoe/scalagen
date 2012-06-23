scalagen
========

The source code generator for Scala ORMs.

The current version of Scalagen support [ScalaQuery](http://scalaquery.org/).
It possible to generate Table objects and case classes by the following code.

```scala
import jp.sf.amateras.scalagen._

Scalagen.generateFor[ScalaQuery](Settings(
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