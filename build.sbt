scalaVersion := "2.11.8"

sbtVersion := "0.13"

name := "time-series"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"
