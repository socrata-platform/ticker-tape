import sbt._

object Dependencies {

  private object versions {
    val balboa_client = "0.16.+"
    val typesafe_config = "1.3.+"
    val jopt_simple = "4.8"
    val log4j = "1.2.17"
    val scala_logging = "2.1.2"
    val slf4j_log4j = "1.7.12"
  }

  // Logging Abstraction Layer for Scala and Java.  Using SLF4J
  val scala_logging = "com.typesafe.scala-logging" %% "scala-logging-slf4j" % versions.scala_logging
  // Logging SLF4J binding to Log4J
  val slf4j_log4j = "org.slf4j" % "slf4j-log4j12" % versions.slf4j_log4j
  // Underlying Log4J library
  val log4j = "log4j" % "log4j" % versions.log4j

  val typesafe_config = "com.typesafe" % "config" % versions.typesafe_config
  val balboa_client = "com.socrata" %% "balboa-client-core" % versions.balboa_client
  val jopt_simple = "net.sf.jopt-simple" % "jopt-simple" % versions.jopt_simple

}