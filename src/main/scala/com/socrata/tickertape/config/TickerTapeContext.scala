package com.socrata.tickertape.config

import com.typesafe.config.{ConfigFactory, Config}

/**
 * Default configuration library.
 */
class TickerTapeContext(config: Config) {

  config.checkValid(ConfigFactory.defaultReference(), "ticker-tape")

  def this() = this(ConfigFactory.load())

}
