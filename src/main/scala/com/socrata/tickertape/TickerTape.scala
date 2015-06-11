package com.socrata.tickertape


import java.nio.file.Paths

import com.blist.metrics.impl.queue.MetricFileQueue
import com.socrata.metrics.{Fluff, MetricIdPart}
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

object ConfigKeys {

  lazy val prefix = "ticker-tape."
  lazy val dataDir = s"${prefix}data-directory"
  lazy val sleepTime = s"${prefix}sleep-time"
  lazy val batchSize = s"${prefix}batch-size"

}

object TickerTape extends App {

  private val logger = LoggerFactory.getLogger(this.getClass)

  private val config = ConfigFactory.load()
  private val dataDir = Paths.get(config.getString(ConfigKeys.dataDir)).toAbsolutePath
  private val sleepTime = config.getLong(ConfigKeys.sleepTime)
  private val batchSize = config.getInt(ConfigKeys.batchSize)
  private val queue = MetricFileQueue.getInstance(dataDir.toString, "")

  logger info s"${this.getClass.getSimpleName} configured to write metrics to $dataDir"
  logger info s"${this.getClass.getSimpleName} configured to write $batchSize metrics every $sleepTime milliseconds"
  logger info s"Starting ${this.getClass.getSimpleName}..."

  val idPart = new MetricIdPart("metrics-internal-test")
  while (true) {
    logger debug s"Writing $batchSize metrics"
    0 until batchSize foreach { i =>
      queue.create(idPart, Fluff(s"fake-metric-$i"), 1)
    }
    logger debug s"Sleeping $sleepTime milliseconds"
    Thread.sleep(sleepTime)
  }
}
