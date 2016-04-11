package com.innoq.leanpubclient

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import play.api.libs.json.Json

import scala.concurrent.Await
import scala.concurrent.duration._


object Main extends App {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  val http = Http()

  val client = new LeanPubClient(http, sys.env("LEANPUB_API_KEY"))
  //Await.result(client.triggerPreview("notabook77"), 5.seconds)
  //Await.result(client.triggerPublish("notabook77", Some("hello World")), 5.seconds)
  val response = client.getIndividualPurchases("notabook77", 2)
  println(Await.result(response, 5.seconds))
  //println(response)
  http.shutdownAllConnectionPools() andThen { case _ => system.shutdown() }

}
