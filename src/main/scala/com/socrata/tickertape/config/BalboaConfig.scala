package com.socrata.tickertape.config

import java.io.File
import java.nio.file.Paths

import com.blist.metrics.impl.queue.{MetricJmsQueueNotSingleton, MetricFileQueue}
import com.socrata.metrics.MetricQueue
import com.typesafe.config.Config
import org.apache.activemq.ActiveMQConnection

/**
  * Config for Balboa MetricQueue
  *
  * Created by michaelhotan on 2/23/16.
  */
trait BalboaConfig {
  // TODO This would be a nice to have in Balboa

  def queue: MetricQueue

}

sealed case class BalboaConfigFromTypesafe(config: Config) extends BalboaConfig {

  private val queueType: String = config.getString("type")

  private lazy val fileQueueDirectory: File = {
    val dataDirPath = Paths.get(config.getString("file.dir"))
    val dataDir = dataDirPath.toFile
    require(dataDir.exists() && dataDir.isDirectory, s"$dataDirPath does not exist as an accessible directory")
    dataDir
  }

  private lazy val jmsUser: Option[String] = {
    val path = "jms.user"
    if (config.hasPath(path)) {
      Some(config.getString(path))
    } else {
      None
    }
  }

  private lazy val jmsPassword: Option[String] = {
    val path = "jms.password"
    if (config.hasPath(path)) {
      Some(config.getString(path))
    } else {
      None
    }
  }

  private lazy val jmsServer: String = config.getString("jms.server")

  private lazy val jmsConnection = (jmsUser, jmsPassword) match {
    case (Some(user), Some(password)) => ActiveMQConnection.makeConnection(user, password, jmsServer)
    case _ => ActiveMQConnection.makeConnection(jmsServer)
  }

  private lazy val jmsQueue: String = config.getString("jms.queue")


  override def queue: MetricQueue = queueType match {
    case "file" => new MetricFileQueue(fileQueueDirectory)
    case "jms" => new MetricJmsQueueNotSingleton(jmsConnection, jmsQueue)
    case _ => throw new UnsupportedOperationException(s"$queueType not supported")
  }

}

object BalboaConfig {

  def apply(config: Config): BalboaConfig = BalboaConfigFromTypesafe(config)

}
