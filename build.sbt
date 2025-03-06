name := """coffee_booking"""
organization := "booking"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.13.16"

libraryDependencies += guice

Compile / playEbeanModels := Seq("models.*")

playEbeanVersion := "8.2.0"

libraryDependencies += ehcache

libraryDependencies ++= Seq(
  jdbc,
  javaWs,
  javaJdbc,
  "com.mysql" % "mysql-connector-j" % "8.3.0",
  "ch.qos.logback" % "logback-classic" % "1.5.4",
  "ch.qos.logback" % "logback-core" % "1.5.4",
  "ch.qos.logback" % "logback-access" % "1.4.14"
)