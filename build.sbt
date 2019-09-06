import sbt.Keys.mainClass

name := "my_first_spark_streaming_app"

version := "0.1"

scalaVersion := "2.11.12"

 val circeVersion = "0.11.1"

libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.0.8",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test",
  "com.typesafe" % "config" % "1.3.4",
  "org.apache.kafka" %% "kafka" % "2.3.0",
  "org.slf4j" % "log4j-over-slf4j" % "1.7.6"

) ++ circeDeps ++ sparkDeps

val sparkDeps = Seq(
  "org.apache.spark" %% "spark-core" % "2.4.4" %  Provided,
  "org.apache.spark" %% "spark-streaming" % "2.4.4" % Provided,
  "org.apache.spark" %% "spark-sql" % "2.4.4" % Provided
).map( sth =>
  sth excludeAll (
    ExclusionRule("log4j"),
    ExclusionRule("slf4j-log4j12")
  )
)


val circeDeps = Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

lazy val commonSettings = Seq(
  version := "0.1-SNAPSHOT",
  organization := "jimmy.cheong",
  scalaVersion := "2.11.12",
  test in assembly := {},
  mainClass in assembly := Some("jimmy.learning.StreamingApp")
)

lazy val root = (project in file("app"))
  .settings(commonSettings: _*)

addCommandAlias("buildSparkJar", ";clean; assembly")
