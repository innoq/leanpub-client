package com.innoq.leanpubclient

import akka.NotUsed
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
import java.util.UUID

import scala.concurrent.{ExecutionContext, Future, TimeoutException}
import scala.util.{Failure, Success, Try}
import scala.concurrent.duration._

class LeanPubClient(http: HttpExt, apiKey: String, requestTimeout: FiniteDuration)(implicit materializer: Materializer, executionContext: ExecutionContext) {

  private val host: String = "leanpub.com"
  private val urlCodec: URLCodec = new URLCodec()

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

  def getCoupons(slug: String): Future[Option[List[Coupon]]] = {
    get(Uri(s"/$slug/coupons.json")).map { response =>
      response.map { json => json.as[List[Coupon]] }
    }
  }

  def getSummary(slug: String): Future[Option[BookInfo]] = {
    get(Uri(s"/$slug.json")).map { response =>
      response.map { json => json.as[BookInfo] }
    }
  }

  def getSales(slug: String): Future[Option[Sales]] = {
    get(Uri(s"/$slug/sales.json")).map { response =>
      response.map { json => json.as[Sales] }
    }
  }

  def getIndividualPurchases(slug: String, page: Int = 1): Future[Option[List[IndividualPurchase]]] = {
    getWithPagination(Uri(s"/$slug/individual_purchases.json"), page).map { response =>
      response.map {
        case a: JsArray => a.as[List[IndividualPurchase]]
        case _ => List.empty[IndividualPurchase]
      }
    }
  }

  def getAllIndividualPurchases(slug: String): Source[IndividualPurchase, NotUsed] = {
    val startPage = 1
    Source.unfoldAsync(startPage) { pageNum =>
      val futurePage: Future[Option[List[IndividualPurchase]]] = getIndividualPurchases(slug, pageNum)
      val next = futurePage.map {
        case Some(Nil) => None
        case Some(list) => Some((pageNum + 1, list))
        case _ => None
      }
      next
      }.mapConcat(identity)
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

  private def get(uri: Uri): Future[Option[JsValue]] = {
    val query = Query("api_key" -> apiKey)
    val request = HttpRequest(uri = uri.withQuery(query), method = HttpMethods.GET)
    sendRequest(request).flatMap { response => handleResponseToGet(uri, response) }
  }

  private def getWithPagination(uri: Uri, page: Int): Future[Option[JsValue]] = {
    val query = Query("api_key" -> apiKey, "page" -> page.toString)
    val request = HttpRequest(uri = uri.withQuery(query), method = HttpMethods.GET)
    sendRequest(request).flatMap { response => handleResponseToGet(uri, response) }
  }

  private def sendRequest(request: HttpRequest): Future[HttpResponse] = {
    val flow: Flow[(HttpRequest, UUID), (Try[HttpResponse], UUID), HostConnectionPool] = http
      .newHostConnectionPoolHttps(host)
      .completionTimeout(requestTimeout)
    val responseFuture: Future[(Try[HttpResponse], UUID)] =
      Source.single(request -> UUID.randomUUID())
        .via(flow)
        .runWith(Sink.head)
    responseFuture.flatMap {
      case (Success(response), _) => Future.successful(response)
      case (Failure(error), _) => Future.failed(error)
    } recoverWith {
      case error: TimeoutException =>
        val uri = request.uri.withQuery(Query.Empty)
        Future.failed(RequestTimeoutException(request, s"Request timed out after $requestTimeout for $uri"))
    }
  }
}
