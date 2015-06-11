import Dependencies.Libraries._
import Dependencies.Resolvers._
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport._
import com.typesafe.sbt.packager.docker.{Cmd, ExecCmd}

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
  enablePlugins(DockerPlugin).
  settings(dockerSettings: _*)


//////////////////////////////////////////////////////
// Explirmental Docker Plugin Settings
//////////////////////////////////////////////////////

lazy val dockerSettings = Seq(
  dockerBaseImage := "socrata/java",
  dockerCommands ++= Seq(
    // setting the run script executable
    ExecCmd("RUN", "chmod", "u+x",
      s"${(defaultLinuxInstallLocation in Docker).value}/bin/${executableScriptName.value}")
  )
)

