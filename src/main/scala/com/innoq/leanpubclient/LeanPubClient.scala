package com.innoq.leanpubclient

import akka.http.scaladsl.Http.HostConnectionPool
import akka.http.scaladsl.HttpExt
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._
import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.innoq.leanpubclient.ResponseHandler._
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport._
import org.apache.commons.codec.net.URLCodec
import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

/**
  * Created by tina on 25.01.16.
  */
class LeanPubClient(http: HttpExt, apiKey: String)(implicit materializer: Materializer, executionContext: ExecutionContext) {

  val host: String = "leanpub.com"
  val urlCodec: URLCodec = new URLCodec()

  private def sendRequest(request: HttpRequest): Future[HttpResponse] = {
    val flow: Flow[(HttpRequest, Int), (Try[HttpResponse], Int), HostConnectionPool] = http.newHostConnectionPoolHttps(host)
    val responseFuture: Future[(Try[HttpResponse], Int)] =
      Source.single(request -> 42)
        .via(flow)
        .runWith(Sink.head)
    responseFuture.flatMap {
      case (Success(response), _) => Future.successful(response)
      case (Failure(error), _) => Future.failed(error)
    }
  }

  private def postFormParams(uri: Uri, formParams: Map[String, String] = Map.empty): Future[Result] = {
    val formData = FormData(formParams + ("api_key" -> apiKey))
    val request = HttpRequest(uri = uri, method = HttpMethods.POST, entity = formData.toEntity)
    sendRequest(request).flatMap { response => handleResponseToPost(uri, response) }
  }

  private def sendJson[A](method: HttpMethod)(uri: Uri, a: A)(implicit writes: Writes[A]): Future[Result] = {
    val query = Query("api_key" -> apiKey)
    Marshal(a).to[MessageEntity].flatMap { entity =>
      val request = HttpRequest(uri = uri.withQuery(query), method = method, entity = entity)
      sendRequest(request).flatMap { response => handleResponseToPost(uri, response) }
    }
  }

  private def get(uri: Uri): Future[JsValue] = {
    val query = Query("api_key" -> apiKey)
    val request = HttpRequest(uri = uri.withQuery(query), method = HttpMethods.GET)
    sendRequest(request).flatMap { response => handleResponseToGet(uri, response) }
  }

  private def getWithPagination(uri: Uri, page: Int): Future[JsValue] = {
    val query = Query("api_key" -> apiKey, "page" -> page.toString)
    val request = HttpRequest(uri = uri.withQuery(query), method = HttpMethods.GET)
    sendRequest(request).flatMap { response => handleResponseToGet(uri, response) }
  }

  def triggerPreview(slug: String): Future[Result] = postFormParams(Uri(s"/$slug/preview.json"))

  def triggerPublish(slug: String, emailText: Option[String]): Future[Result] = {
    val formParams = emailText match {
      case Some(text) => Map("publish[email_readers]" -> "true", "publish[release_notes]" -> urlCodec.encode(text))
      case None => Map.empty[String, String]
    }
    postFormParams(Uri(s"/$slug/publish.json"), formParams)
  }

  def createCoupon(slug: String, coupon: CreateCoupon): Future[Result] = {
    sendJson(HttpMethods.POST)(Uri(s"/$slug/coupons.json"), coupon)
  }

  def updateCoupon(slug: String, couponCode: String, coupon: UpdateCoupon): Future[Result] = {
    sendJson(HttpMethods.PUT)(Uri(s"/$slug/coupons/$couponCode.json"), coupon)
  }

  def getCoupons(slug: String): Future[List[Coupon]] = {
    get(Uri(s"/$slug/coupons.json")).map { json => json.as[List[Coupon]] }
  }

  def getSummary(slug: String): Future[BookInfo] = {
    get(Uri(s"/$slug.json")).map { json => json.as[BookInfo] }
  }

  def getSales(slug: String): Future[Sales] = {
    get(Uri(s"/$slug/sales.json")).map { json => json.as[Sales] }
  }

  def getIndividualPurchases(slug: String, page: Int = 1): Future[List[IndividualPurchase]] = {
    getWithPagination(Uri(s"/$slug/individual_purchases.json"), page).map { json => json.as[List[IndividualPurchase]]}
  }

  def getAllIndividualPurchases(slug: String): Future[List[IndividualPurchase]] = {
    val firstPage = getIndividualPurchases(slug, 1)
    def loop(future: Future[List[IndividualPurchase]], accu: List[IndividualPurchase], count: Int): Future[List[IndividualPurchase]] = {
      future.flatMap { response =>
        if(response.isEmpty) {
          Future(accu)
        }
        else {
          val incrementCount = count + 1
          loop(getIndividualPurchases(slug, incrementCount), response ::: accu, incrementCount)
        }
      }
    }
    loop(firstPage, List.empty, 1)
  }
}
