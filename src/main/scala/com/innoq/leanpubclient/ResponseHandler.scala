package com.innoq.leanpubclient

import akka.http.scaladsl.model.{HttpResponse, StatusCodes, Uri}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport._
import play.api.libs.json.JsValue

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by tina on 15.02.16.
  */
object ResponseHandler {
  private[leanpubclient] def handleResponseToPost(uri: Uri, response: HttpResponse)(implicit materializer: Materializer, ec: ExecutionContext): Future[Unit] = {
    response.status match {
      case StatusCodes.OK => Future.successful(())
      case code => Future.failed(UnexpectedStatusException(uri, code))
    }
  }

  private[leanpubclient] def handleResponseToGet(uri: Uri, response: HttpResponse)(implicit materializer: Materializer, ec: ExecutionContext): Future[JsValue] = {
    response.status match {
      case StatusCodes.OK => Unmarshal(response.entity).to[JsValue]
      case code => Future.failed(UnexpectedStatusException(uri, code))
    }
  }
}
