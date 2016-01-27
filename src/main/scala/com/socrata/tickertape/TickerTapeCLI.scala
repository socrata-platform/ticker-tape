package com.socrata.tickertape


import java.util.concurrent.{Executors, TimeUnit}

import com.socrata.tickertape.config.ConfigFromTypeSafe
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

/**
 * Small entry point
 */
object TickerTapeCLI extends App {

  private val logger = LoggerFactory.getLogger(this.getClass)

  // Abstract away configuration values from application functionality.
  private val typeSafeConfig = ConfigFactory.load()
  private val config = new ConfigFromTypeSafe(typeSafeConfig)

  // Good practice to log what the current configuration state is to catch any pesky config bugs.
  logger info s"${this.getClass.getSimpleName} configured with ${typeSafeConfig.root.render}"
  logger info s"Starting ${this.getClass.getSimpleName}..."

  val scheduler = Executors.newScheduledThreadPool(1)
  val future = scheduler.scheduleAtFixedRate(new TickerTape(config), 0, config.sleepTime, TimeUnit.MILLISECONDS)

  Runtime.getRuntime.addShutdownHook(new Thread("Shutdown thread") {
    override def run(): Unit = {
      logger info s"Stopping scheduled tasks that emits metrics"
      future.cancel(true)
      logger debug s"Successfully cancelled future tasks."
    } // Can cancel, who really cares.
  })
}
