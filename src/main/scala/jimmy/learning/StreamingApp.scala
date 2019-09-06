package jimmy.learning

import io.circe.generic.auto._
import io.circe.parser._
import org.slf4j.LoggerFactory

object StreamingApp extends Configuration {

  def main(args: Array[String]): Unit = {

    val logger = LoggerFactory.getLogger(getClass)

    logger.info("Starting StreamingApp...")

    val sparkSession = buildSparkSession()

    import sparkSession.implicits._

    val kafkaConfig = buildKafkaConfig

    val streaming = sparkSession.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", kafkaConfig.serversAsString)
      .option("subscribe", kafkaConfig.topic)
      .option("startingOffsets", "latest")
      .option("fetchOffset.retryIntervalMs", 400)
      .load() // return DataFrame[Row] with cols: offset, value, topic, timestamp, timestampType, partition, key
      .selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
      .as[(String, String)]
      .map { record =>
        decode[PersonInfo](record._2) match {
          case Right(personInfo) => personInfo
          case Left(error) =>
            println(s"Error: $error")
            PersonInfo("unknown", 0)
        }
      }

    val applicantCount = streaming
      .filter(person => person.age >= 18)
      .groupBy("name")
      .sum("age")

    val writeTableName = "valid_applicants"

    val applicantQuery =
      applicantCount.writeStream
        .queryName(writeTableName)
        .format("memory")
        .outputMode("complete")
        .start()

    for (_ <- 1 to 1000) {
      sparkSession.sql(s"SELECT * FROM $writeTableName").show()
      Thread.sleep(1000)
    }
    applicantQuery.awaitTermination()

  }

}
