package jimmy.learning.producer

import java.util.{Properties, UUID}

import com.typesafe.config.ConfigFactory
import jimmy.learning.PersonInfo
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

object MessageProducer {

  def main(args: Array[String]): Unit = {
    println("producing messages")

    val config = ConfigFactory.load()

    val topic = config.getString("kafka.topic")
    val bootstrapServers = config.getString("kafka.bootstrapServers")

    println(s"""
               |Produce to topic: $topic
               |Produce to servers: $bootstrapServers
      """.stripMargin)

    val runId = UUID.randomUUID().toString

    val data = Seq(
      PersonInfo("Alice", 19),
      PersonInfo("Bob", 20),
      PersonInfo("Charlie", 17),
      PersonInfo("David", 26)
    )

    val messages: Seq[ProducerRecord[String, String]] = data.zipWithIndex.map {
      case (personInfo, index) =>
        new ProducerRecord(topic,
                           s"$runId-message-$index",
                           personInfo.asJson.noSpaces)
    }

    val properties = new Properties()
    properties.put("bootstrap.servers", bootstrapServers)
    properties.put("key.serializer", classOf[StringSerializer])
    properties.put("value.serializer", classOf[StringSerializer])

    val producer = new KafkaProducer[String, String](properties)

    messages.foreach(msg => {
      println(s"""
                 |Publishing message: ${msg.key} with value: ${msg.value}
        """.stripMargin)
      producer.send(msg)
    })

    println("Completed publishing")
    producer.close()
  }

}
