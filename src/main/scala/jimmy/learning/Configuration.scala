package jimmy.learning

import com.typesafe.config.{Config, ConfigFactory}

trait Configuration {

  val config: Config = ConfigFactory.load()

  case class KafkaConfig(bootstrapServers: Seq[String], topic: String) {
    def serversAsString: String = bootstrapServers.mkString(",")
  }

  def buildKafkaConfig: KafkaConfig = {
    KafkaConfig(
      config.getString("kafka.bootstrapServers").split(","),
      config.getString("kafka.topic")
    )
  }

}
