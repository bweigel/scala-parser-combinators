name := "dslling"

version := "0.1"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq("org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",
  "com.typesafe.akka" %% "akka-actor" % "2.5.13",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.13" % Test)

libraryDependencies ++= Seq("org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test")

libraryDependencies ++= Seq("com.amazonaws" % "aws-java-sdk" % "1.11.357")