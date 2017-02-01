package com.innoq.leanpubclient


case class UnexpectedStatusException(url: String, code: Int) extends Exception {

  def message: String = s"Request to $url failed, Statuscode: $code"

}
