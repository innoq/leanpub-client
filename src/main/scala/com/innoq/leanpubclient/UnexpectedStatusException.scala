package com.innoq.leanpubclient

import akka.http.scaladsl.model.{Uri, StatusCode}

case class UnexpectedStatusException(uri: Uri, code: StatusCode) extends Exception {

  def message: String = s"Request to $uri failed, Statuscode: $code"

}
