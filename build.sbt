import Dependencies.Libraries._
import Dependencies.Resolvers._
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport.Docker
import com.typesafe.sbt.packager.docker._

lazy val commonSettings = Seq(
  organization := "com.socrata",
  version := "0.1.0",
  scalaVersion := "2.10.5",
  libraryDependencies ++= Seq(scala_logging, slf4j_log4j, log4j, typesafe_config, joda_time, joda_convert)
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "ticker-tape",
    resolvers += socrata_release,
    libraryDependencies ++= Seq(balboa_client)
  ).
  enablePlugins(JavaAppPackaging).
  enablePlugins(DockerPlugin).
  settings(dockerSettings: _*)


//////////////////////////////////////////////////////
// Explirmental Docker Plugin Settings
//////////////////////////////////////////////////////

lazy val dockerSettings = Seq(
  dockerBaseImage := "socrata/java",
  daemonUser in Docker := "root",
  maintainer := "Sum Team <sum-team-l@socrata.com>",
  mappings in Docker += file("docker/ship.d/run") -> "/etc/ship.d/run",

  dockerCommands := dockerCommands.value.filterNot {
    case ExecCmd("ENTRYPOINT", _*) => true
    case ExecCmd("CMD", _*) => true
    case ExecCmd("USER", _*) => true
    case _ => false
  } ++ Seq(
    ExecCmd("ADD", "etc", "/etc"),
    ExecCmd("RUN", "chmod", "a+x", "/etc/ship.d/run"),
    ExecCmd("RUN", "chmod", "a+x", "/opt/docker/bin/ticker-tape"),
    ExecCmd("RUN", "chmod", "-R", "a+rX", ".")
  )
)

