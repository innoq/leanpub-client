name := "leanpub-client"
organization := "com.innoq"
scalaVersion := "2.11.8"
scalacOptions ++= Seq("-unchecked", "-deprecation")

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "com.typesafe.akka" %% "akka-http" % "10.0.1",
  "com.typesafe.akka" %% "akka-stream" % "2.4.16",
  "commons-codec" % "commons-codec" % "1.10",
  "de.heikoseeberger" %% "akka-http-play-json" % "1.10.1",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)

resolvers += Resolver.bintrayRepo("hseeberger", "maven")

cancelable in Global := true
