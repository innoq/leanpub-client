package com.innoq.leanpubclient

import play.api.libs.json.JsValue

sealed trait Result
object Result {
  case object Success extends Result
  case class ClientError(url: String, entity: JsValue) extends Result
  case class NotFoundError(url: String, code: Int) extends Result
}
