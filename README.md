# my_first_spark_streaming_app

The purpose of this project to is learn how to use Spark Streaming and read and process messages from Kafka. 

Technology stack: 
- Spark Streaming 
- Kafka 
- Typesafe Config 
- Circe for JSON encoding/decoding
- Scalatest
- sbt-assembly for packaging fat .jars

## How to run via command line

Run the following command using the spark-submit executable:

```
./bin/spark-submit \
    --master local[2] \
    --driver-java-options "-Dlogback.configurationFile=/logback.xml"
    --packages org.apache.spark:spark-sql-kafka-0-10_2.12:2.4.3 \
    <path to jar>
     100
```

## Kafka Command line 

To create the topic:

```bash
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic someTopic
``` 

See https://spark.apache.org/docs/latest/structured-streaming-kafka-integration.html#deploying for more