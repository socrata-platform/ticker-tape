import Dependencies.Libraries._
import Dependencies.Resolvers._
import com.typesafe.sbt.packager.docker._

lazy val commonSettings = Seq(
  organization := "com.socrata",
  version := "0.1.0",
  scalaVersion := "2.11.7",
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

lazy val `ticker-tape` = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "ticker-tape",
    resolvers += socrata_release,
    libraryDependencies ++= Seq(balboa_client, balboa_client_jms)
  ).enablePlugins(JavaAppPackaging).
  enablePlugins(UniversalPlugin).
  enablePlugins(DockerPlugin).
  enablePlugins(DebianPlugin).
  settings(dockerSettings: _*)


//////////////////////////////////////////////////////
// Docker Plugin Settings
//////////////////////////////////////////////////////

lazy val dockerSettings = Seq(
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

