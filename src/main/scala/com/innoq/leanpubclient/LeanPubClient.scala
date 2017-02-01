package com.innoq.leanpubclient

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.innoq.leanpubclient.ResponseHandler._
import play.api.libs.json._
import play.api.libs.ws.StandaloneWSRequest
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

class LeanPubClient(wsClient: StandaloneAhcWSClient, apiKey: String, requestTimeout: FiniteDuration)(implicit executionContext: ExecutionContext) {

  private val host = "https://leanpub.com"

  /** Sends a POST request to trigger a preview of the book.
    *
    * @param slug, usually book's title
    * @return Future of type [[Result]], which can be either a Success or an Error.
    */
  def triggerPreview(slug: String): Future[Result] = postFormParams(s"$host/$slug/preview.json")

  /** Sends a POST request to trigger the book's publishing.
    *
    * Triggers also the sending of an email to your book's readers if you provide an emailText.
    * Email sending will not be triggered if you omit the emailText param.
    *
    * @param slug, usually book's title
    * @param emailText is optional.
    * @return Future of type [[Result]], which can be either a Success or an Error.
    */
  def triggerPublish(slug: String, emailText: Option[String]): Future[Result] = {
    val formParams = emailText match {
      case Some(text) => Map("publish[email_readers]" -> Seq("true"), "publish[release_notes]" -> Seq(text))
      case None => Map.empty[String, Seq[String]]
    }
    postFormParams(s"$host/$slug/publish.json", formParams)
  }

  def createCoupon(slug: String, coupon: CreateCoupon): Future[Result] = {
    postJson(s"$host/$slug/coupons.json", coupon)
  }

  def updateCoupon(slug: String, couponCode: String, coupon: UpdateCoupon): Future[Result] = {
    putJson(s"$host/$slug/coupons/$couponCode.json", coupon)
  }

  def getCoupons(slug: String): Future[Option[List[Coupon]]] = {
    get(s"$host/$slug/coupons.json").map { response =>
      response.map { json => json.as[List[Coupon]] }
    }
  }

  def getSummary(slug: String): Future[Option[BookInfo]] = {
    get(s"$host/$slug.json").map { response =>
      response.map { json => json.as[BookInfo] }
    }
  }

  def getSales(slug: String): Future[Option[Sales]] = {
    get(s"$host/$slug/sales.json").map { response =>
      response.map { json => json.as[Sales] }
    }
  }

  def getIndividualPurchases(slug: String, page: Int = 1): Future[Option[List[IndividualPurchase]]] = {
    getWithPagination(s"$host/$slug/individual_purchases.json", page).map { response =>
      response.map {
        case a: JsArray => a.as[List[IndividualPurchase]]
        case _ => List.empty[IndividualPurchase]
      }
    }
  }

  def getIndividualPurchaseSource(slug: String): Source[IndividualPurchase, NotUsed] = {
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

  private def postFormParams(url: String, formParams: Map[String, Seq[String]] = Map.empty): Future[Result] = {
    val body = formParams.updated("api_key", Seq(apiKey))
    val request = buildBasicRequest(url)
      .post(body)
    request.map { response => handleResponseToPost(url, response) }
  }

  private def postJson[A](url: String, a: A)(implicit writes: Writes[A]): Future[Result] = {
    val query = "api_key" -> apiKey
    val data = Json.toJson(a)
    val request = buildBasicRequest(url)
      .withQueryString(query)
      .post(data)
    request.map { response => handleResponseToPost(url, response) }
  }

  private def putJson[A](url: String, a: A)(implicit writes: Writes[A]): Future[Result] = {
    val query = "api_key" -> apiKey
    val data = Json.toJson(a)
    val request = buildBasicRequest(url)
      .withQueryString(query)
      .put(data)
    request.map { response => handleResponseToPost(url, response) }
  }

  private def get(url: String): Future[Option[JsValue]] = {
    val query = "api_key" -> apiKey
    val request = buildBasicRequest(url)
      .withQueryString(query)
      .get()
    request.map { response => handleResponseToGet(url, response) }
  }

  private def getWithPagination(url: String, page: Int): Future[Option[JsValue]] = {
    val query1 = "api_key" -> apiKey
    val query2 = "page" -> page.toString
    val request = buildBasicRequest(url)
      .withQueryString(query1, query2)
      .get()
    request.map { response => handleResponseToGet(url, response) }
  }

  private def buildBasicRequest(url: String): StandaloneWSRequest = {
    wsClient.url(url).withRequestTimeout(requestTimeout)
  }
}
