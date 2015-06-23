package com.socrata.tickertape

import com.blist.metrics.impl.queue.MetricFileQueue
import com.socrata.metrics.Fluff
import com.socrata.tickertape.config.TickerTapeConfig
import org.slf4j.LoggerFactory

/**
 * [[Runnable]] class that emits metrics
 */
class TickerTape(config: TickerTapeConfig) extends Runnable {

  private val logger = LoggerFactory.getLogger(this.getClass)

  private val queue = MetricFileQueue.getInstance(config.dataDirectory.getAbsolutePath, "")

  override def run(): Unit = 0 until config.batchSize foreach { i =>
    logger debug s"Writing ${config.batchSize} metrics"
    queue.create(config.metricsEntityId, Fluff(s"fake-metric-$i"), 1)
  }

}
