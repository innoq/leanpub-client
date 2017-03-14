package com.innoq.leanpubclient

/** An UnexpectedStatusException will be thrown on a HTTP status code different from 200 or 404
  * by [[ResponseHandler]] after a POST or GET request.
  *
  * @param url requested url
  * @param code response HTTP status code
  */
case class UnexpectedStatusException(url: String, code: Int) extends Exception {

  def message: String = s"Request to $url failed, Statuscode: $code"

}
