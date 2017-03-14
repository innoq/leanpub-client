package com.innoq.leanpubclient

import akka.util.ByteString
import play.api.libs.json.JsValue
import play.api.libs.ws.{StandaloneWSResponse, WSCookie}

import scala.xml.Elem

case class StubResponse(status: Int, json: JsValue) extends StandaloneWSResponse {
  override def allHeaders: Map[String, Seq[String]] = Map.empty

  override def underlying[T]: T = ???

  override def statusText: String = ""

  override def header(key: String): Option[String] = None

  override def cookies: Seq[WSCookie] = Seq.empty

  override def cookie(name: String): Option[WSCookie] = None

  override def body: String = ""

  override def xml: Elem = ???

  override def bodyAsBytes: ByteString = ByteString.empty
}
