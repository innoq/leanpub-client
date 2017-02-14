package com.innoq.leanpubclient

import java.time.LocalDate
import play.api.libs.functional.syntax._
import play.api.libs.json._

/** Use CreateCoupon to create a coupon for a book via [[LeanPubClient.createCoupon]]
  *
  * You can specify different discount packages in one CreateCoupon by providing a list
  * of [[PackageDiscount]]. Each PackageDiscount contains a package name and discounted price.
  * @param couponCode coupon name
  * @param packageDiscounts List of [[PackageDiscount]] to specify discounted price and package names
  * @param startDate start date for coupon validity
  * @param endDate end date for coupon validity, is optional
  * @param maxUses maximum number of uses for a coupon, None means unlimited
  * @param note a description of the coupon, is optional
  * @param suspended whether coupon is suspended
  */
case class CreateCoupon(couponCode: String,
                        packageDiscounts: List[PackageDiscount],
                        startDate: LocalDate,
                        endDate: Option[LocalDate] = None,
                        maxUses: Option[Int] = None,
                        note: Option[String] = None,
                        suspended: Boolean = false)

object CreateCoupon {
  implicit val couponWrites: Writes[CreateCoupon] = (
    (JsPath \ "coupon_code").write[String] and
      (JsPath \ "package_discounts_attributes").write[List[PackageDiscount]] and
      (JsPath \ "start_date").write[LocalDate] and
      (JsPath \ "end_date").writeNullable[LocalDate] and
      (JsPath \ "max_uses").writeNullable[Int] and
      (JsPath \ "note").writeNullable[String] and
      (JsPath \ "suspended").write[Boolean]
    ) (unlift(CreateCoupon.unapply))
}
