package com.innoq.leanpubclient

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class PackageDiscount(identifier: String, discountedPrice: BigDecimal)
object PackageDiscount {
  implicit val packageDiscountReads: Reads[PackageDiscount] = (
    (JsPath \ "package_slug").read[String] and
      (JsPath \ "discounted_price").read[BigDecimal]
    ) (PackageDiscount.apply _)
}
