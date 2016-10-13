package com.innoq.leanpubclient

import akka.http.scaladsl.model.{ResponseEntity, StatusCode, Uri}

sealed trait Result
object Result {
  case object Success extends Result
  case class ClientError(uri: Uri, entity: ResponseEntity) extends Result
  case class NotFoundError(uri: Uri, code: StatusCode) extends Result
}
