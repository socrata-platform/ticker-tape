package com.socrata.tickertape.config

import java.io.File
import java.nio.file.Paths

import com.typesafe.config.{Config, ConfigFactory}

object ConfigKeys {

  lazy val prefix = "ticker-tape."
  lazy val dataDir = s"${prefix}data-directory"
  lazy val sleepTime = s"${prefix}sleep-time"
  lazy val batchSize = s"${prefix}batch-size"

}

class TickerTapeConfig(config: Config) {

  config.checkValid(ConfigFactory.defaultReference(), "ticker-tape")

  def this() = this(ConfigFactory.load())

  /**
   * The data directory.
   */
  val dataDirectory: File = Paths.get(config.getString(ConfigKeys.dataDir)).toFile match {
    case f if f.exists() && f.isDirectory => f
    case _ => throw new IllegalArgumentException(
      s"${config.getString(ConfigKeys.dataDir)} is not an existing valid directory.")
  }

  /**
   * Number of milliseconds to sleep inbetween metrics commits.
   */
  val sleepTime: Long = config.getLong(ConfigKeys.sleepTime)

  /**
   * The number of metrics to commit inbeween intervals.
   */
  val batchSize: Int = config.getInt(ConfigKeys.batchSize)
}