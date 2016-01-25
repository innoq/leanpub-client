import akka.http.scaladsl.HttpExt
import akka.http.scaladsl.model._
import akka.stream.Materializer

import scala.concurrent.{ExecutionContext, Future}


/**
  * Created by tina on 25.01.16.
  */
class LeanPubClient(http: HttpExt, apiKey: String)(implicit materializer: Materializer, executionContext: ExecutionContext) {

  val host: String = "https://leanpub.com"

  def triggerPreview(slug: String): Future[Unit] = {
    http.singleRequest(HttpRequest(uri=s"$host/$slug/preview.json", method=HttpMethods.POST, entity=FormData("api_key" -> apiKey).toEntity)).flatMap { response =>
        response.status match {
          case StatusCodes.OK => Future.successful(())
          case code => Future.failed(new RuntimeException(s"Request to $host failed, Statuscode: $code"))
      }
    }
  }

  def triggerPublish(slug: String, emailText: Option[String]): Future[Unit] = {
    val request: HttpRequest = emailText match {
      case Some(text) => HttpRequest( uri=s"$host/$slug/publish.json",
                                      method=HttpMethods.POST,
                                      entity=FormData("api_key" -> apiKey, "publish[email_readers]" -> "true", "publish[release_notes]" -> text).toEntity )
      case None => HttpRequest( uri=s"$host/$slug/publish.json",
                                method=HttpMethods.POST,
                                entity=FormData("api_key" -> apiKey).toEntity )
    }
    http.singleRequest(request).flatMap { response =>
      response.status match {
        case StatusCodes.OK => Future.successful(())
        case code => Future.failed(new RuntimeException(s"Request to $host failed, Statuscode: $code"))
      }
    }
  }

}
