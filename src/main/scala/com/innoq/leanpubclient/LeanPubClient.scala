package com.innoq.leanpubclient

import akka.http.scaladsl.HttpExt
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._
import akka.stream.Materializer
import org.apache.commons.codec.net.URLCodec
import play.api.libs.json.JsValue

import scala.concurrent.{ExecutionContext, Future}
import ResponseHandler._

/**
  * Created by tina on 25.01.16.
  */
class LeanPubClient(http: HttpExt, apiKey: String)(implicit materializer: Materializer, executionContext: ExecutionContext) {

  val host: String = "https://leanpub.com"
  val urlCodec: URLCodec = new URLCodec()

  private def post(uri: Uri, formParams: Map[String, String] = Map.empty): Future[Unit] = {
    val formData = FormData(formParams + ("api_key" -> apiKey))
    val request = HttpRequest(uri = uri, method = HttpMethods.POST, entity = formData.toEntity)
    http.singleRequest(request).flatMap { response => handleResponseToPost(uri, response) }
  }

  private def get(uri: Uri): Future[JsValue] = {
    val query = Query("api_key" -> apiKey)
    val request = HttpRequest(uri = uri.withQuery(query), method = HttpMethods.GET)
    http.singleRequest(request).flatMap { response => handleResponseToGet(uri, response) }
  }



  def triggerPreview(slug: String): Future[Unit] = post(Uri(s"$host/$slug/preview.json"))

  def triggerPublish(slug: String, emailText: Option[String]): Future[Unit] = {
    val formParams = emailText match {
      case Some(text) => Map("publish[email_readers]" -> "true", "publish[release_notes]" -> urlCodec.encode(text))
      case None => Map.empty[String, String]
    }
    post(Uri(s"$host/$slug/publish.json"), formParams)
  }

  def createCoupon(slug: String,
                   couponCode: String,
                   discountedPrice: Float,
                   startDate: DateTime,
                   endDate: DateTime,
                   maxUses: Int = 0,
                   note: String = ""): Future[Unit] = {
    val formParams = Map("coupon[coupon_code]" -> urlCodec.encode(couponCode),
                         "coupon[package_discounts_attributes]" -> Array(s"{package_slug: $slug, discounted_price: $discountedPrice}").toString,
                         "coupon[start_date]" -> urlCodec.encode(startDate.toIsoDateString),
                         "coupon[end_date]" -> urlCodec.encode(endDate.toIsoDateString),
                         "coupn[max_uses]" -> urlCodec.encode(maxUses.toString),
                         "coupon[note]" -> urlCodec.encode(note))
    post(Uri(s"$host/$slug/coupons.json"), formParams)
  }

  def getCoupons(slug: String): Future[JsValue] = {
    get(Uri(s"$host/$slug/coupons.json"))
  }

  def getSummary(slug: String): Future[JsValue] = {
    get(Uri(s"$host/$slug.json"))
  }

  def getSales(slug: String): Future[JsValue] = {
    get(Uri(s"$host/$slug/sales.json"))
  }

  def getIndividualPurchases(slug: String): Future[JsValue] = {
    get(Uri(s"$host/$slug/individual_purchases.json"))
  }
}
