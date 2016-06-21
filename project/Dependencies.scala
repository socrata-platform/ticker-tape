import sbt._

object Dependencies {

  object Libraries {

    private object versions {
      val balboa = "0.17.+"
      val typesafe_config = "1.3.0"
      val log4j = "1.2.17"
      val scala_logging = "2.1.2"
      val slf4j_log4j = "1.7.12"
      val joda_convert = "1.2"
      val joda_time = "2.1"
    }

    // Logging Abstraction Layer for Scala and Java.  Using SLF4J
    val scala_logging = "com.typesafe.scala-logging" %% "scala-logging-slf4j" % versions.scala_logging
    // Logging SLF4J binding to Log4J
    val slf4j_log4j = "org.slf4j" % "slf4j-log4j12" % versions.slf4j_log4j
    // Underlying Log4J library
    val log4j = "log4j" % "log4j" % versions.log4j

    val typesafe_config = "com.typesafe" % "config" % versions.typesafe_config
    val balboa_client_jms = "com.socrata" %% "balboa-client-jms" % versions.balboa
    val joda_convert = "org.joda" % "joda-convert" % versions.joda_convert
    val joda_time = "joda-time" % "joda-time" % versions.joda_time
  }

}
