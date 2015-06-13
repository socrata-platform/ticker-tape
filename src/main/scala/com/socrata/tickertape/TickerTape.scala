package com.socrata.tickertape


import com.blist.metrics.impl.queue.MetricFileQueue
import com.socrata.metrics.{Fluff, MetricIdPart}
import com.socrata.tickertape.config.TickerTapeConfig
import org.slf4j.LoggerFactory

object TickerTape extends App {

  private val logger = LoggerFactory.getLogger(this.getClass)

  private val config = new TickerTapeConfig()
  private val queue = MetricFileQueue.getInstance(config.dataDirectory.getAbsolutePath, "")

  logger info s"${this.getClass.getSimpleName} configured to write metrics to ${config.dataDirectory}"
  logger info s"${this.getClass.getSimpleName} configured to write ${config.batchSize} metrics every ${config.sleepTime} milliseconds"
  logger info s"Starting ${this.getClass.getSimpleName}..."

  val idPart = new MetricIdPart("metrics-internal-test")
  while (true) {
    logger debug s"Writing ${config.batchSize} metrics"
    0 until config.batchSize foreach { i =>
      queue.create(idPart, Fluff(s"fake-metric-$i"), 1)
    }
    logger debug s"Sleeping ${config.sleepTime} milliseconds"
    Thread.sleep(config.sleepTime)
  }
}
