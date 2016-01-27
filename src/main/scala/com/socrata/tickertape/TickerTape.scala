package com.socrata.tickertape

import com.blist.metrics.impl.queue.MetricFileQueue
import com.socrata.metrics.Fluff
import com.socrata.tickertape.config.TickerTapeConfig
import com.typesafe.scalalogging.slf4j.LazyLogging

/**
 * Runnable class that emits metrics
 */
class TickerTape(config: TickerTapeConfig) extends Runnable with LazyLogging {

  private val queue = MetricFileQueue.getInstance(config.dataDirectory)

  override def run(): Unit = {
    logger info s"Writing ${config.batchSize} metrics"
    val startTime = System.currentTimeMillis()
    0 until config.batchSize foreach { i =>
      queue.create(config.metricsEntityId, Fluff(s"fake-metric-$i"), 1)
    }
    logger info s"Completed emitting ${config.batchSize} metrics in ${System.currentTimeMillis() - startTime} ms"
  }

}
