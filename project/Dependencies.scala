import sbt._

object Dependencies {

  object Libraries {
    private object versions {
      val akka = "2.3.+"
      val balboa_client = "0.16.+"
      val typesafe_config = "1.3.0"
      val jopt_simple = "4.8"
      val log4j = "1.2.17"
      val scala_logging = "2.1.2"
      val slf4j_log4j = "1.7.12"
      val joda_convert = "1.2"
      val joda_time = "2.1"
    }

    val akka = "com.typesafe.akka" %% "akka-actor" % versions.akka
    // Logging Abstraction Layer for Scala and Java.  Using SLF4J
    val scala_logging = "com.typesafe.scala-logging" %% "scala-logging-slf4j" % versions.scala_logging
    // Logging SLF4J binding to Log4J
    val slf4j_log4j = "org.slf4j" % "slf4j-log4j12" % versions.slf4j_log4j
    // Underlying Log4J library
    val log4j = "log4j" % "log4j" % versions.log4j

    val typesafe_config = "com.typesafe" % "config" % versions.typesafe_config
    val balboa_client = "com.socrata" %% "balboa-client" % versions.balboa_client
    val jopt_simple = "net.sf.jopt-simple" % "jopt-simple" % versions.jopt_simple
    val joda_convert = "org.joda" % "joda-convert" % versions.joda_convert
    val joda_time = "joda-time" % "joda-time" % versions.joda_time
  }

  object Resolvers {

    val socrata_release = "socrata maven" at "https://repo.socrata.com/artifactory/libs-release-local"

  }


}