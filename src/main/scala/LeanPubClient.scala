import akka.http.scaladsl.HttpExt
import akka.http.scaladsl.model._
import akka.stream.Materializer

import scala.concurrent.{ExecutionContext, Future}
import org.apache.commons.codec.net.URLCodec

/**
  * Created by tina on 25.01.16.
  */
class LeanPubClient(http: HttpExt, apiKey: String)(implicit materializer: Materializer, executionContext: ExecutionContext) {

  val host: String = "https://leanpub.com"
  val urlCodec: URLCodec = new URLCodec()

  def post(uri: Uri, formParams: Map[String, String] = Map.empty): Future[Unit] = {
    val formData = FormData(formParams + ("api_key" -> apiKey))
    val request = HttpRequest(uri = uri, method = HttpMethods.POST, entity = formData.toEntity)
    http.singleRequest(request).flatMap(handleResponseToPost)
  }

  def get(uri: Uri): Future[Unit] = {
    val request = HttpRequest(uri = uri, method = HttpMethods.POST, entity = FormData("api_key" -> apiKey).toEntity)
    http.singleRequest(request).flatMap(handleResponseToGet)
  }

  def handleResponseToPost(response: HttpResponse): Future[Unit] = {
    response.status match {
      case StatusCodes.OK => Future.successful(())
      case code => Future.failed(new RuntimeException(s"Request to $host failed, Statuscode: $code"))
    }
  }

  def handleResponseToGet(response: HttpResponse): Future[Unit] = {
    response.status match {
      case StatusCodes.OK => Future.successful(response.entity)
      case code => Future.failed(new RuntimeException(s"Request to $host failed, Statuscode: $code"))
    }
  }

  def triggerPreview(slug: String): Future[Unit] = post(Uri(s"$host/$slug/preview.json"))

  def triggerPublish(slug: String, emailText: Option[String]): Future[Unit] = {
    val formParams = emailText match {
      case Some(text) => Map("publish[email_readers]" -> "true", "publish[release_notes]" -> urlCodec.encode(text))
      case None => Map.empty[String, String]
    }
    post(Uri(s"$host/$slug/publish.json"), formParams)
  }

  def getCoupons(slug: String): Future[Unit] = {
    get(Uri(s"$host/$slug/coupons.json"))
  }

  def getSummary(slug: String): Future[Unit] = {
    get(Uri(s"$host/$slug.json"))
  }

  def getSales(slug: String): Future[Unit] = {
    get(Uri(s"$host/$slug/sales.json"))
  }

  def getIndividualPurchases(slug: String): Future[Unit] = {
    get(Uri(s"$host/$slug/individual_purchases.json"))
  }
}
