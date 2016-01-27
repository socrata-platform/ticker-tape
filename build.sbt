import Dependencies.Libraries._
import Dependencies.Resolvers._
import com.typesafe.sbt.packager.docker._

lazy val commonSettings = Seq(
  organization := "com.socrata",
  version := "0.1.0",
  scalaVersion := "2.10.5",
  libraryDependencies ++= Seq(
    scala_logging,
    slf4j_log4j,
    log4j,
    typesafe_config,
    joda_time,
    joda_convert
  )
) ++ Seq( // For now, ignore some socrata sbt plugin things that generally we should not.
  StylePlugin.StyleKeys.styleFailOnError in Compile := false,
  // TODO: enable coverage build failures
  scoverage.ScoverageSbtPlugin.ScoverageKeys.coverageFailOnMinimum := false,
  // TODO: enable findbugs build failures
  JavaFindBugsPlugin.JavaFindBugsKeys.findbugsFailOnError in Compile := false,
  JavaFindBugsPlugin.JavaFindBugsKeys.findbugsFailOnError in Test := false
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "ticker-tape",
    resolvers += socrata_release,
    libraryDependencies ++= Seq(balboa_client)
  ).enablePlugins(JavaAppPackaging).
  enablePlugins(UniversalPlugin).
  enablePlugins(DockerPlugin).
  settings(dockerSettings: _*)


//////////////////////////////////////////////////////
// Docker Plugin Settings
//////////////////////////////////////////////////////

lazy val dockerSettings = Seq(
  dockerBaseImage := "socrata/java8",
  mappings in Docker ++= Seq(
    file("docker/ship.d/run") -> "/etc/ship.d/run" // Use our favorite ship.d/run script
  ),
  dockerEntrypoint := Seq("/etc/ship.d"),
  dockerCmd := Seq("run"),
  daemonUser in Docker := "root",
  dockerCommands := dockerCommands.value.filterNot {
    case ExecCmd("ENTRYPOINT", _*) => true
    case ExecCmd("CMD", _*) => true
    case _ => false
  } ++ Seq(
    ExecCmd("ADD", "etc", "/etc"),
    ExecCmd("RUN", "chmod", "a+x", "/etc/ship.d/run"),
    ExecCmd("RUN", "chmod", "-R", "a+rX", "."),
    Cmd("LABEL", "com.socrata.ticker-tape=")
  )
)

