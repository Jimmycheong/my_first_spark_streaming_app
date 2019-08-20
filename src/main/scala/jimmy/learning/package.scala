package jimmy

import org.apache.spark.sql.SparkSession

package object learning {

  case class PersonInfo(name: String, age: Int)

  def buildSparkSession(): SparkSession = {
    SparkSession
      .builder()
      .config("spark.master", "local[1]")
      .config("spark.sql.shuffle.partitions", 5)
      .getOrCreate()
  }

}
