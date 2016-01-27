package com.socrata.tickertape.config

import java.io.File
import java.nio.file.Paths
import scala.concurrent.duration.{FiniteDuration, Duration}

import com.socrata.metrics.MetricIdPart
import com.typesafe.config.{Config, ConfigFactory}

/**
 * Ticker Tape configuration interface.
 */
sealed trait TickerTapeConfig {

  /**
   * The data directory to write metrics to.
   *
   * @return The Directory instance.
   */
  def dataDirectory: File

  /**
   * The duration to sleep between batch writes/
   */
  def sleepTime: Duration

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

sealed case class ConfigFromTypeSafe(config: Config) extends TickerTapeConfig {

  private def asFiniteDuration(d: java.time.Duration): FiniteDuration = Duration.fromNanos(d.toNanos)

  config.checkValid(ConfigFactory.defaultReference(), "ticker-tape")

  def this() = this(ConfigFactory.load())

  override val dataDirectory: File = Paths.get(config.getString(PropertyFileConfigKeys.dataDir)).toFile match {
    case f if f.exists() && f.isDirectory => f
    case _ => throw new IllegalArgumentException(
      s"${config.getString(PropertyFileConfigKeys.dataDir)} is not an existing valid directory.")
  }

  override val sleepTime: FiniteDuration = asFiniteDuration(config.getDuration(PropertyFileConfigKeys.sleepTime))

  override val batchSize: Int = config.getInt(PropertyFileConfigKeys.batchSize)

  override def metricsEntityIdAsString: String = config.getString(PropertyFileConfigKeys.metricsEntityId)

  override def toString: String = config.root().render()
}

object TickerTapeConfig {

  /**
    * Utilizes the default ConfigFactory.load method that typesafe config provides.
    *
    * @return The TickerTape Config
    */
  def apply() = ConfigFromTypeSafe(ConfigFactory.load())

  /**
    * Allows external users to compose an external TypeSafe Config object.  TickerTapeConfig
    * will validate if the configuration object passed in will be valid.
    *
    * @param config Config object to be used for Typesafe
    * @return
    */
  def apply(config: Config) = ConfigFromTypeSafe(config)

}