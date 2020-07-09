import sbt.Keys._
import sbt._

name := "KafkaUtil"

version := "0.1"

scalaVersion := "2.12.11"

libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka-clients" % "2.5.0"
)

lazy val manifestSettings = Seq(
  packageOptions in(Compile, packageBin) +=
    Package.ManifestAttributes(
      "git_last_commit" -> git.gitHeadCommit.value.toString,
      "git_last_message" -> git.gitHeadMessage.value.toString.replaceAll("\n", ""))
)

val root = Project(id = "root", base = file(".")).settings(manifestSettings: _*)