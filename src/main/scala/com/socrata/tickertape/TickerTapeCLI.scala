package com.socrata.tickertape

import com.socrata.metrics.{MetricQueue, Fluff}
import config.{BalboaConfig, TickerTapeConfig}
import com.typesafe.scalalogging.slf4j.StrictLogging

object TickerTapeCLI extends StrictLogging {
  private val config = TickerTapeConfig()

  def main(args: Array[String]): Unit = {
    logger info s"${this.getClass.getSimpleName} configured with $config"
    logger info s"Starting ${this.getClass.getSimpleName}..."

    var queue = BalboaConfig(config.balboa).queue

    Runtime.getRuntime.addShutdownHook(new Thread {
      override def run(): Unit = {
        logger info "Closing the metrics file."
        queue.close()
      }
    })

    while (true) {

      try {
        writeMetricsBatch(queue)
      } finally {
        queue.close
        queue = BalboaConfig(config.balboa).queue
      }

      Thread.sleep(config.sleepTime.toMillis)
    }
  }

  def writeMetricsBatch(queue: MetricQueue): Unit = {
    logger info s"Writing ${config.batchSize} metrics"
    val startTime = System.currentTimeMillis()
    0 until config.batchSize foreach { i =>
      val entityId = config.metricsEntityId
      val metricName = s"fake-metric-$i"
      val metricValue = 1
      logger debug s"Emitting metric with Entity ID: $entityId, Name: $metricName and Value: $metricValue"
      queue.create(entityId, Fluff(metricName), metricValue)
    }

    logger info s"Completed emitting ${config.batchSize} metrics in ${System.currentTimeMillis() - startTime} ms"
  }

}
