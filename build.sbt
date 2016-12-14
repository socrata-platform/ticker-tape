import Dependencies.Libraries._
import com.typesafe.sbt.packager.docker._

lazy val `ticker-tape` = (project in file("."))
  .settings(
    name := "ticker-tape",
    scalaVersion := "2.11.7",
    organization := "com.socrata",
    version := "0.1.0",
    scoverage.ScoverageSbtPlugin.ScoverageKeys.coverageMinimum := 0,
    scoverage.ScoverageSbtPlugin.ScoverageKeys.coverageHighlighting := true,
    resolvers += "Socrata Artifactory" at "https://repo.socrata.com/artifactory/libs-release",
    libraryDependencies ++= Seq(
      scala_logging,
      slf4j_log4j,
      log4j,
      typesafe_config,
      joda_time,
      joda_convert,
      balboa_client_jms
    )
  )
  .enablePlugins(JavaAppPackaging, UniversalPlugin, DockerPlugin, DebianPlugin)
  .settings(
    // The ticker-tape jar is frequently copied to destination machines and run
    // from there. While Java 7 is transitioning out of wide spread use, it is
    // useful to compile the project in the older byte-code so it can be run on
    // whatever JVM is present.
    // Disallow the Java 7 compiler. When Java 9 is available, this can be
    // expanded.
    initialize := {
      val _ = initialize.value
      if (sys.props("java.specification.version") != "1.8")
        sys.error("Java 8 is required for this project.")
    }
  )
  .settings(
    packageName in Docker := s"socrata/${name.value.toLowerCase}",
    maintainer in Docker := "mission-control-l@socrata.com",
    dockerBaseImage := "socrata/java8",
    daemonUser in Docker := "root",
    dockerUpdateLatest in Docker := true,
    mappings in Docker ++= Seq(
      file("docker/run.sh") -> "/etc/ship.d/run"
    ),
    dockerCommands := dockerCommands.value.filterNot {
      case ExecCmd("ENTRYPOINT", _*) => true
      case ExecCmd("CMD", _*) => true
      case _ => false
    } ++ Seq(
      ExecCmd("ADD", "etc", "/etc"),
      ExecCmd("RUN", "chmod", "a+x", "/etc/ship.d/run"),
      ExecCmd("RUN", "chmod", "-R", "a+rX", "."),
      Cmd("LABEL", s"com.socrata.${name.value.toLowerCase}=")
    )
  )
