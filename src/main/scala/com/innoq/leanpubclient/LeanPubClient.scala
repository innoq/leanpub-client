package com.innoq.leanpubclient

import java.time.{LocalDate, ZonedDateTime}

import akka.http.scaladsl.HttpExt
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._
import akka.stream.Materializer
import akka.util.ByteString
import org.apache.commons.codec.net.URLCodec
import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}
import ResponseHandler._

/**
  * Created by tina on 25.01.16.
  */
class LeanPubClient(http: HttpExt, apiKey: String)(implicit materializer: Materializer, executionContext: ExecutionContext) {

  val host: String = "https://leanpub.com"
  val urlCodec: URLCodec = new URLCodec()

  private def postFormParams(uri: Uri, formParams: Map[String, String] = Map.empty): Future[Unit] = {
    val formData = FormData(formParams + ("api_key" -> apiKey))
    val request = HttpRequest(uri = uri, method = HttpMethods.POST, entity = formData.toEntity)
    http.singleRequest(request).flatMap { response => handleResponseToPost(uri, response) }

  }

  private def postJson[A](uri: Uri, a: A)(implicit writes: Writes[A]): Future[Unit] = {
    val query = Query("api_key" -> apiKey)
    val jsonString = Json.toJson(a).toString
    val entity = HttpEntity.Strict(ContentTypes.`application/json`, ByteString(jsonString))
    val request = HttpRequest(uri = uri.withQuery(query), method = HttpMethods.POST, entity = entity)
    http.singleRequest(request).flatMap { response => handleResponseToPost(uri, response) }
  }

  private def get(uri: Uri): Future[JsValue] = {
    val query = Query("api_key" -> apiKey)
    val request = HttpRequest(uri = uri.withQuery(query), method = HttpMethods.GET)
    http.singleRequest(request).flatMap { response => handleResponseToGet(uri, response) }
  }

  def triggerPreview(slug: String): Future[Unit] = postFormParams(Uri(s"$host/$slug/preview.json"))

  def triggerPublish(slug: String, emailText: Option[String]): Future[Unit] = {
    val formParams = emailText match {
      case Some(text) => Map("publish[email_readers]" -> "true", "publish[release_notes]" -> urlCodec.encode(text))
      case None => Map.empty[String, String]
    }
    postFormParams(Uri(s"$host/$slug/publish.json"), formParams)
  }

  def createCoupon(coupon: Coupon): Future[Unit] = {
    postJson(Uri(s"$host/${coupon.bookSlug}/coupons.json"), coupon)
  }

  def getCoupons(slug: String): Future[List[Coupon]] = {
    get(Uri(s"$host/$slug/coupons.json")).map { json => json.as[List[Coupon]] }
  }

  def getSummary(slug: String): Future[BookInfo] = {
    get(Uri(s"$host/$slug.json")).map { json => json.as[BookInfo] }
  }

  def getSales(slug: String): Future[Sales] = {
    get(Uri(s"$host/$slug/sales.json")).map { json => json.as[Sales] }
  }

  def getIndividualPurchases(slug: String): Future[JsValue] = {
    get(Uri(s"$host/$slug/individual_purchases.json"))
  }
}
