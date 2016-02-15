name := "leanpub-client"
organization := "com.innoq"
scalaVersion := "2.11.7"

val akkaHttpVersion = "2.0.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-core-experimental" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-stream-experimental" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaHttpVersion,
  "commons-codec" % "commons-codec" % "1.10",
  "de.heikoseeberger" %% "akka-http-play-json" % "1.4.2"
)

resolvers += Resolver.bintrayRepo("hseeberger", "maven")

cancelable in Global := true
