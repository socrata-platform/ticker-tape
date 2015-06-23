package com.socrata.tickertape.config

import java.io.File
import java.nio.file.Paths

import com.socrata.metrics.MetricIdPart
import com.typesafe.config.{Config, ConfigFactory}

/**
 * Ticker Tape configuration interface.
 */
trait TickerTapeConfig {

  /**
   * The data directory to write metrics to.
   *
   * @return The Directory instance.
   */
  def dataDirectory: File

  /**
   * The amount of time to sleep between batch writes.
   * @return time in ms.
   */
  def sleepTime: Long

  /**
   * @return The number of metrics to emit in one attempt.
   */
  def batchSize: Int

  /**
   * @return The Metrics Entity id
   */
  def metricsEntityId: MetricIdPart = new MetricIdPart(metricsEntityIdAsString)

  /**
   * @return The Metric Entity ID as a String.
   */
  def metricsEntityIdAsString: String

  // NOTE Because this is a trait we can implement forms of injection and inheritence.

}

/**
 * Keys for referencing [[java.util.Properties]] files.
 */
private object PropertyFileConfigKeys {

  lazy val prefix = "ticker-tape."
  lazy val dataDir = s"${prefix}data-directory"
  lazy val sleepTime = s"${prefix}sleep-time"
  lazy val batchSize = s"${prefix}batch-size"
  lazy val metricsEntityId = s"${prefix}metric-entity-id"

}

case class ConfigFromTypeSafe(config: Config) extends TickerTapeConfig {

  config.checkValid(ConfigFactory.defaultReference(), "ticker-tape")

  def this() = this(ConfigFactory.load())

  override val dataDirectory: File = Paths.get(config.getString(PropertyFileConfigKeys.dataDir)).toFile match {
    case f if f.exists() && f.isDirectory => f
    case _ => throw new IllegalArgumentException(
      s"${config.getString(PropertyFileConfigKeys.dataDir)} is not an existing valid directory.")
  }

  override val sleepTime: Long = config.getLong(PropertyFileConfigKeys.sleepTime)

  override val batchSize: Int = config.getInt(PropertyFileConfigKeys.batchSize)

  override def metricsEntityIdAsString: String = config.getString(PropertyFileConfigKeys.metricsEntityId)
}