organization := "com.wlangiewicz"

name := "bitcoinj-examples-scala"

version := "1.0"

scalaVersion := "2.11.2"

resolvers += (
  "Local Maven Repository" at "file:///"+Path.userHome.absolutePath+"/.m2/repository"
)

libraryDependencies += "org.scala-sbt" % "command" % "0.13.6"

libraryDependencies += "org.bitcoinj" % "bitcoinj-core" % "0.12-SNAPSHOT"

