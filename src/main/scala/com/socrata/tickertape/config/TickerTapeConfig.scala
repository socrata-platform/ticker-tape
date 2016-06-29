package com.socrata.tickertape.config

import com.socrata.metrics.MetricIdPart
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.duration.{Duration, FiniteDuration}

class TickerTapeConfig(config: Config) {

  private def asFiniteDuration(d: java.time.Duration): FiniteDuration = Duration.fromNanos(d.toNanos)

  config.checkValid(ConfigFactory.defaultReference(),
    TickerTapeConfig.SleepTimeName,
    TickerTapeConfig.BatchSizeName,
    TickerTapeConfig.MetricsEntityIdName,
    TickerTapeConfig.BalboaName
  )

  def this() = this(ConfigFactory.load())

  /**
   * The duration to sleep between batch writes/
   */
  def sleepTime: Duration = asFiniteDuration(config.getDuration(TickerTapeConfig.SleepTimeName))

  /**
    * @return The number of metrics to emit in one attempt.
    */
  def batchSize: Int = config.getInt(TickerTapeConfig.BatchSizeName)

  /**
    * @return The Metrics Entity id
    */
  def metricsEntityId: MetricIdPart = new MetricIdPart(config.getString(TickerTapeConfig.MetricsEntityIdName))

  def balboa: Config = config.getConfig(TickerTapeConfig.BalboaName)

  override def toString: String = config.root().render()
}

object TickerTapeConfig {

  val SleepTimeName = "sleep-time"
  val BatchSizeName = "batch-size"
  val MetricsEntityIdName = "metric-entity-id"
  val BalboaName = "balboa"

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
  def apply(config: Config): TickerTapeConfig = new TickerTapeConfig(config)

}
