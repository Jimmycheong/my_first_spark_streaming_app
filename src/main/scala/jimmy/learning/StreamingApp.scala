package jimmy.learning

import org.apache.spark.sql.Row
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

object StreamingApp extends Configuration {

  def main(args: Array[String]): Unit = {

    val sparkSession = buildSparkSession()

    val logbackPath = getClass.getResource("/logback.xml").getPath
    sparkSession.sparkContext.addFile(
      "/Users/cheong1/PersonalDev/my_first_spark_streaming_app/target/scala-2.11/classes/logback.xml")

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
