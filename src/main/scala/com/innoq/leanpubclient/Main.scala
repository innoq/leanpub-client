package com.innoq.leanpubclient

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._


object Main extends App {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  val wsClient = StandaloneAhcWSClient()

  val client = new LeanPubClient(wsClient, sys.env("LEANPUB_API_KEY"), 5000.millis)
  //Await.result(client.triggerPreview("notabook77"), 5.seconds)
  //Await.result(client.triggerPublish("notabook77", Some("hello World")), 5.seconds)
  //val response = client.getSales("notabook77")
  //val coupon = CreateCoupon("bug10", List(PackageDiscount("book", 1.0)), LocalDate.of(2016, 7, 1))
  //val coupon = UpdateCoupon(Some(List(PackageDiscount("book", 3.0))), Some(LocalDate.of(2015, 1, 1)))
  //val response = client.updateCoupon("notabook77", "fail", coupon)
  //val response = client.updateCoupon("notabook77", "test123", coupon)
  //val response = client.getCoupons("notabook77")
  //val response = client.getAllIndividualPurchases("notabook77")
  //val response = client.createCoupon("notabook77", coupon)
  //println(Await.result(response, 5.seconds))
  //println(response)

  val source = client.getIndividualPurchaseSource("notabook77")
  //val source: Source[IndividualPurchase, NotUsed] = IndividualPurchasesSource(client, "notabook77")
  val sink = Sink.foreach(println)
  val result: Future[Done] = source.runWith(sink)
  result.onComplete { _ =>
    wsClient.close()
  }
}
