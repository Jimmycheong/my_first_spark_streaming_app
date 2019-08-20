# my_first_spark_streaming_app
Learning how to use Spark Streaming and reading off Kafka


How to run via command line

```
./bin/spark-submit \
    --master local[2] \
    --packages org.apache.spark:spark-sql-kafka-0-10_2.12:2.4.3 \
    <path to jar>
     100
```

See https://spark.apache.org/docs/latest/structured-streaming-kafka-integration.html#deploying for more