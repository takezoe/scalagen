package jp.sf.amateras.scalagen

import java.sql.Types._

object DataTypes {

  def toClass(i: Int): Class[_] = {
    i match {
      case ARRAY         => throw new IllegalArgumentException("ARRAY(%d) is not supported.".format(i))
      case BIGINT        => classOf[Int]
      case BINARY        => throw new IllegalArgumentException("BINARY(%d) is not supported.".format(i))
      case BIT           => classOf[Int]
      case BLOB          => throw new IllegalArgumentException("BLOB(%d) is not supported.".format(i))
      case BOOLEAN       => classOf[Boolean]
      case CHAR          => classOf[String]
      case CLOB          => throw new IllegalArgumentException("CLOB(%d) is not supported.".format(i))
      case DATALINK      => throw new IllegalArgumentException("DATALINK(%d) is not supported.".format(i))
      case DATE          => classOf[java.util.Date]
      case DECIMAL       => classOf[Int]
      case DISTINCT      => throw new IllegalArgumentException("DISTINCT(%d) is not supported.".format(i))
      case DOUBLE        => classOf[Double]
      case FLOAT         => classOf[Float]
      case INTEGER       => classOf[Int]
      case JAVA_OBJECT   => throw new IllegalArgumentException("JAVA_OBJECT(%d) is not supported.".format(i))
      case LONGNVARCHAR  => classOf[String]
      case LONGVARBINARY => throw new IllegalArgumentException("LONGVARBINARY(%d) is not supported.".format(i))
      case LONGVARCHAR   => classOf[String]
      case NCHAR         => classOf[String]
      case NCLOB         => throw new IllegalArgumentException("NCLOB(%d) is not supported.".format(i))
      case NULL          => throw new IllegalArgumentException("NULL(%d) is not supported.".format(i))
      case NUMERIC       => classOf[Double]
      case NVARCHAR      => classOf[String]
      case OTHER         => throw new IllegalArgumentException("OTHER(%d) is not supported.".format(i))
      case REAL          => classOf[Double]
      case REF           => throw new IllegalArgumentException("REF(%d) is not supported.".format(i))
      case ROWID         => throw new IllegalArgumentException("REFID(%d) is not supported.".format(i))
      case SMALLINT      => classOf[Int]
      case SQLXML        => classOf[scala.xml.NodeSeq]
      case STRUCT        => throw new IllegalArgumentException("STRUCT(%d) is not supported.".format(i))
      case TIME          => classOf[java.sql.Time]
      case TIMESTAMP     => classOf[java.sql.Timestamp]
      case TINYINT       => classOf[Int]
      case VARBINARY     => throw new IllegalArgumentException("VARBINARY(%d) is not supported.".format(i))
      case VARCHAR       => classOf[String]
    }
  }

}