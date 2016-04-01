package com.socrata.tickertape

import java.util.concurrent.{Executors, TimeUnit}

import com.socrata.tickertape.config.TickerTapeConfig
import com.typesafe.scalalogging.slf4j.LazyLogging

/**
 * Small entry point
 */
object TickerTapeCLI extends App with LazyLogging {

  // Abstract away configuration values from application functionality.
  private val config = TickerTapeConfig()

  // Good practice to log what the current configuration state is to catch any pesky config bugs.
  logger info s"${this.getClass.getSimpleName} configured with $config"
  logger info s"Starting ${this.getClass.getSimpleName}..."

  val scheduler = Executors.newScheduledThreadPool(1)
  val future = scheduler.scheduleAtFixedRate(new TickerTape(config),
    0, config.sleepTime.toMillis, TimeUnit.MILLISECONDS)

  Runtime.getRuntime.addShutdownHook(new Thread("Shutdown thread") {
    override def run(): Unit = {
      logger info s"Stopping scheduled tasks that emits metrics"
      future.cancel(true)
      logger debug s"Successfully cancelled future tasks."
    } // Can cancel, who really cares.
  })
}
