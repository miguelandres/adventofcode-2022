ThisBuild / scalaVersion := "3.2.1"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.miguely"
ThisBuild / organizationName := "miguely"

lazy val root = (project in file("."))
  .settings(
    name := "adventofcode"
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
