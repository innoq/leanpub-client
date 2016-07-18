name := "leanpub-client"
organization := "com.innoq"
scalaVersion := "2.11.8"
scalacOptions ++= Seq("-unchecked", "-deprecation")

val akkaHttpVersion = "2.4.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaHttpVersion,
  "commons-codec" % "commons-codec" % "1.10",
  "de.heikoseeberger" %% "akka-http-play-json" % "1.7.0",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)

resolvers += Resolver.bintrayRepo("hseeberger", "maven")

cancelable in Global := true
