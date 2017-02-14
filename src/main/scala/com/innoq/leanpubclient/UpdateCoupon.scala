package com.innoq.leanpubclient

import java.time.LocalDate
import play.api.libs.functional.syntax._
import play.api.libs.json._

/** Use UpdateCoupon to update a coupon for a book via [[LeanPubClient.updateCoupon]]
  *
  * Every parameter is optional for UpdateCoupons. See below what each parameter does
  * and use only those you want to update.
  * @param packageDiscounts List of [[PackageDiscount]] to specify discounted price and package names
  * @param startDate start date for coupon validity
  * @param endDate end date for coupon validity
  * @param maxUses maximum number of uses for a coupon
  * @param note a description of the coupon
  * @param suspended whether coupon is suspended
  */
case class UpdateCoupon(packageDiscounts: Option[List[PackageDiscount]] = None,
                        startDate: Option[LocalDate] = None,
                        endDate: Option[LocalDate] = None,
                        maxUses: Option[Int] = None,
                        note: Option[String] = None,
                        suspended: Option[Boolean] = None
                        )

object UpdateCoupon {
  implicit val updateCouponWrites: Writes[UpdateCoupon] = (
      (JsPath \ "package_discounts_attributes").writeNullable[List[PackageDiscount]] and
      (JsPath \ "start_date").writeNullable[LocalDate] and
      (JsPath \ "end_date").writeNullable[LocalDate] and
      (JsPath \ "max_uses").writeNullable[Int] and
      (JsPath \ "note").writeNullable[String] and
      (JsPath \ "suspended").writeNullable[Boolean]
    ) (unlift(UpdateCoupon.unapply))
}
