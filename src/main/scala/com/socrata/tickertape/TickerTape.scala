package com.socrata.tickertape

import com.socrata.metrics.Fluff
import com.socrata.tickertape.config.TickerTapeConfig
import com.typesafe.scalalogging.slf4j.LazyLogging

/**
 * Runnable class that emits metrics
 */
class TickerTape(config: TickerTapeConfig) extends Runnable with LazyLogging {

  private val queue = config.queue

  override def run(): Unit = {
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
