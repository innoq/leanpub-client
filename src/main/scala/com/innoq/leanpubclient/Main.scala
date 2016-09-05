package com.innoq.leanpubclient

import java.time.LocalDate

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

  try {
    val client = new LeanPubClient(http, sys.env("LEANPUB_API_KEY"), 5000.millis)
    //Await.result(client.triggerPreview("notabook77"), 5.seconds)
    //Await.result(client.triggerPublish("notabook77", Some("hello World")), 5.seconds)
    //val response = client.getSales("notabook77")
    //val coupon = CreateCoupon("bug10", List(PackageDiscount("book", 1.0)), LocalDate.of(2016, 7, 1))
    //val coupon = UpdateCoupon(Some(List(PackageDiscount("book", 3.0))), Some(LocalDate.of(2015, 1, 1)))
    //val response = client.updateCoupon("notabook77", "fail", coupon)
    //val response = client.updateCoupon("notabook77", "test123", coupon)
    //val response = client.getCoupons("notabook77")
    val response = client.getAllIndividualPurchases("notabook77")
    //val response = client.createCoupon("notabook77", coupon)
    //val response = client.triggerPreviewSingleFile("notabook12345","test.txt")
    println(Await.result(response, 5.seconds))
    //println(response)
  }
  finally {
    http.shutdownAllConnectionPools() andThen { case _ => system.terminate() }
  }

}
