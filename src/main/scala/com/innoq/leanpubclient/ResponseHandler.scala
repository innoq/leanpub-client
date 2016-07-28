package com.innoq.leanpubclient

import akka.http.scaladsl.model.{HttpResponse, StatusCodes, Uri}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport._
import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by tina on 15.02.16.
  */
object ResponseHandler {
  private[leanpubclient] def handleResponseToPost(uri: Uri, response: HttpResponse)(implicit materializer: Materializer, ec: ExecutionContext): Future[Result] = {
    response.status match {
      case StatusCodes.OK => Unmarshal(response.entity).to[JsValue] match {
        case o: JsObject if o.value == Map("success" -> JsBoolean(true)) => Future.successful(Result.Success)
        case _ => Future.successful(Result.ClientError(uri, response.entity))
      }
      case StatusCodes.NotFound => Future.successful(Result.NotFoundError(uri, response.status))
      case code => Future.failed(UnexpectedStatusException(uri, code))
    }
  }

  private[leanpubclient] def handleResponseToGet(uri: Uri, response: HttpResponse)(implicit materializer: Materializer, ec: ExecutionContext): Future[Option[JsValue]] = {
    response.status match {
      case StatusCodes.OK => Unmarshal(response.entity).to[JsValue].map { jsvalue => Option(jsvalue) }
      case StatusCodes.NotFound => Future.successful(None)
      case code => Future.failed(UnexpectedStatusException(uri, code))
    }
  }
}
