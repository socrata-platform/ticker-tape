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

  private val logger = LoggerFactory.getLogger(classOf[this])

  private val config = ConfigFactory.load()
  private val dataDir = Paths.get(config.getString(ConfigKeys.dataDir)).toAbsolutePath
  private val sleepTime = config.getLong(ConfigKeys.dataDir)
  private val batchSize = config.getInt(ConfigKeys.dataDir)
  private val queue = MetricFileQueue.getInstance(dataDir.toString, "")

  override def main(args: Array[String]): Unit = {
    val idPart = new MetricIdPart("metrics-internal-test")
    while (true) {
      logger info s"Writing $batchSize metrics"
      0 until batchSize foreach { i =>
        queue.create(idPart, Fluff(s"fake-metric-$i"), 1)
      }
      Thread.sleep(sleepTime)
    }
  }

}
