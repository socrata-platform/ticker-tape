package com.socrata.tickertape.config

import com.socrata.metrics.{MetricIdPart, MetricQueue}
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.duration.{Duration, FiniteDuration}

/**
 * Ticker Tape configuration interface.
 */
sealed trait TickerTapeConfig {

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

  /**
    * @return Return the queue used for emitting metrics.
    */
  def queue: MetricQueue

}

/**
 * Keys for referencing [[java.util.Properties]] files.
 */
private object PropertyFileConfigKeys {

  val sleepTime = "sleep-time"
  val batchSize = "batch-size"
  val metricsEntityId = "metric-entity-id"
  val balboa = "balboa"

}

sealed case class TickerTapeConfigFromTypesafe(config: Config) extends TickerTapeConfig {

  private def asFiniteDuration(d: java.time.Duration): FiniteDuration = Duration.fromNanos(d.toNanos)

  config.checkValid(ConfigFactory.defaultReference(),
    PropertyFileConfigKeys.sleepTime,
    PropertyFileConfigKeys.batchSize,
    PropertyFileConfigKeys.metricsEntityId,
    PropertyFileConfigKeys.balboa
  )

  def this() = this(ConfigFactory.load())

  override val sleepTime: FiniteDuration = asFiniteDuration(config.getDuration(PropertyFileConfigKeys.sleepTime))

  override val batchSize: Int = config.getInt(PropertyFileConfigKeys.batchSize)

  override val metricsEntityIdAsString: String = config.getString(PropertyFileConfigKeys.metricsEntityId)

  override val queue: MetricQueue = BalboaConfig(config.getConfig(PropertyFileConfigKeys.balboa)).queue

  override def toString: String = config.root().render()
}

object TickerTapeConfig {

  /**
    * Utilizes the default ConfigFactory.load method that typesafe config provides.
    *
    * @return The TickerTape Config
    */
  def apply(): TickerTapeConfig = apply(ConfigFactory.load())

  /**
    * Allows external users to compose an external TypeSafe Config object.  TickerTapeConfig
    * will validate if the configuration object passed in will be valid.
    *
    * @param config Config object to be used for Typesafe
    * @return
    */
  def apply(config: Config): TickerTapeConfig = TickerTapeConfigFromTypesafe(config)

}
