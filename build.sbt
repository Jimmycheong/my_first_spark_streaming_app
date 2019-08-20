import sbt.Keys.mainClass

name := "my_first_spark_streaming_app"

version := "0.1"

scalaVersion := "2.11.12"

 val circeVersion = "0.11.1"

libraryDependencies ++= (Seq(
  "org.scalactic" %% "scalactic" % "3.0.8",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test",
  "org.apache.spark" %% "spark-core" % "2.4.3" %  Provided,
  "org.apache.spark" %% "spark-streaming" % "2.4.3" % Provided,
  "org.apache.spark" %% "spark-sql" % "2.4.3" % Provided,
  "com.typesafe" % "config" % "1.3.4",
  "org.apache.kafka" %% "kafka" % "2.3.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "ch.qos.logback" % "logback-core" % "1.2.3"
) ++ circeDeps)

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

addCommandAlias("sparkBuildJar", ";clean; assembly")
