package com.innoq.leanpubclient

import play.api.libs.functional.syntax._
import play.api.libs.json._

/** PackageDiscount specifies the discounted price and package name for [[Coupon]],
  * [[CreateCoupon]] and [[UpdateCoupon]].
  *
  * @param identifier package name
  * @param discountedPrice discounted price for a book
  */
case class PackageDiscount(identifier: String, discountedPrice: BigDecimal)
object PackageDiscount {
  implicit val packageDiscountReads: Reads[PackageDiscount] = (
      (JsPath \ "package_slug").read[String] and
      (JsPath \ "discounted_price").read[BigDecimal]
    ) (PackageDiscount.apply _)

  implicit val packageDiscountWrites: Writes[PackageDiscount] = (
      (JsPath \ "package_slug").write[String] and
      (JsPath \ "discounted_price").write[BigDecimal]
    ) (unlift(PackageDiscount.unapply))
}
