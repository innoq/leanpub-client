package com.innoq.leanpubclient

import play.api.libs.json._
import play.api.libs.ws.StandaloneWSResponse

private[leanpubclient] object ResponseHandler {
  private[leanpubclient] def handleResponseToPost(url: String, response: StandaloneWSResponse): Result = {
    response.status match {
      case 200 =>
        val entity = response.json
        entity match {
          case o if o == Json.obj("success" -> JsBoolean(true)) => Result.Success
          case _ => Result.ClientError(url, entity)
        }
      case 404 => Result.NotFoundError(url, response.status)
      case code => throw UnexpectedStatusException(url, code)
    }
  }

  private[leanpubclient] def handleResponseToGet(url: String, response: StandaloneWSResponse): Option[JsValue] = {
    response.status match {
      case 200 => Option(response.json)
      case 404 => None
      case code => throw UnexpectedStatusException(url, code)
    }
  }
}
