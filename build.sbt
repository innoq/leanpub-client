name := "leanpub-client"
organization := "com.innoq"
scalaVersion := "2.12.1"
scalacOptions ++= Seq("-unchecked", "-deprecation")
licenses += ("MIT",  url("https://opensource.org/licenses/MIT"))
bintrayPackageLabels := Seq("scala")
bintrayOrganization := Some("innoq")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.4.16",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "com.typesafe.play" %% "play-json" % "2.6.0-M1",
  "com.typesafe.play" %% "play-ahc-ws-standalone" % "1.0.0-M1"
)

cancelable in Global := true
