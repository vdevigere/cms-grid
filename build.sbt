name := "cms-grid"

version := "1.0"

scalaVersion := "2.11.1"

libraryDependencies += "com.typesafe.scala-logging" % "scala-logging_2.11" % "3.1.0"

libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.12"

libraryDependencies += "com.typesafe" % "config" % "1.3.0"

libraryDependencies += "org.infinispan" % "infinispan-core" % "8.0.1.Final"

libraryDependencies += "io.undertow" % "undertow-core" % "1.3.0.CR2"

libraryDependencies += "io.undertow" % "undertow-servlet" % "1.3.0.CR2"

libraryDependencies += "com.fasterxml.uuid" % "java-uuid-generator" % "3.1.4"

libraryDependencies += "org.apache.lucene" % "lucene-core" % "5.2.1"

libraryDependencies += "org.apache.lucene" % "lucene-queryparser" % "5.2.1"

libraryDependencies += "org.apache.lucene" % "lucene-analyzers-common" % "5.2.1"

libraryDependencies += "org.apache.lucene" % "lucene-memory" % "5.2.1"

libraryDependencies += "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.6.0-1"

libraryDependencies += "io.gatling.uncommons.maths" % "uncommons-maths" % "1.2.3"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"

libraryDependencies += "org.mockito" % "mockito-core" % "1.10.19" % "test"