package com.innoq.leanpubclient

import play.api.libs.json.JsValue

/** Result is a custom response to a POST request.
  *
  * [[Result.Success]] signifies a successful POST request.
  * A [[Result.ClientError]] can occur if you perform a request that is not valid by the terms
  * of the Leanpub API. An example of an invalid request is a second POST request with
  * the same coupon code via [[LeanPubClient.createCoupon]].
  * A [[Result.NotFoundError]] occurs when the client receives a 404 response on a POST request.
  */
sealed trait Result
object Result {
  case object Success extends Result
  case class ClientError(url: String, entity: JsValue) extends Result
  case class NotFoundError(url: String, code: Int) extends Result
}
