import com.socrata.sbtplugins.StylePlugin.StyleKeys
import sbt.Keys._
import Dependencies._

lazy val commonSettings = Seq(
  organization := "com.socrata",
  version := "0.1.0",
  scalaVersion := "2.10.5",
  libraryDependencies ++= Seq(scala_logging, slf4j_log4j, log4j, typesafe_config)
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "ticker-tape",
    libraryDependencies ++= Seq(balboa_client),
    StyleKeys.styleCheck := {}
  )