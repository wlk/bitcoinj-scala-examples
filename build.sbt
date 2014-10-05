organization := "com.wlangiewicz"

name := "bitcoinj-examples-scala"

version := "1.0"

scalaVersion := "2.11.2"


libraryDependencies += "org.scala-sbt" % "command" % "0.13.6"

libraryDependencies += "org.bitcoinj" % "bitcoinj-core" % "0.12"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.7"

libraryDependencies += "org.slf4j" % "log4j-over-slf4j" % "1.7.7"  // for any java classes looking for this

//libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.2"