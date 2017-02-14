package com.innoq.leanpubclient

import java.time.{LocalDate, ZonedDateTime}
import play.api.libs.functional.syntax._
import play.api.libs.json._

/** A list of Coupons is the response of the GET request [[LeanPubClient.getCoupons]]
  *
  * Coupons contain codes to get a discount on a certain book. They also contain information
  * on the period of time in which they are valid, the discounted price and other details.
  * To create or update Coupons please use the respective classes [[CreateCoupon]] and [[UpdateCoupon]].
  * @param couponCode the coupon's name
  * @param packageDiscounts List of [[PackageDiscount]], contains discounted price and package name
  * @param startDate start date for coupon validity
  * @param endDate end date for coupon validity
  * @param maxUses maximum number of uses for a coupon, None means unlimited
  * @param note a description of the coupon
  * @param suspended whether coupon is suspended
  * @param numUses how many times the coupon has been used
  * @param createdAt creation date
  * @param updatedAt date of update
  */
case class Coupon(couponCode: String,
                  packageDiscounts: List[PackageDiscount],
                  startDate: LocalDate,
                  endDate: Option[LocalDate],
                  maxUses: Option[Int],
                  note: Option[String],
                  suspended: Boolean,
                  numUses: Int,
                  createdAt: ZonedDateTime,
                  updatedAt: ZonedDateTime
                 )

object Coupon {
  implicit val couponReads: Reads[Coupon] = (
      (JsPath \ "coupon_code").read[String] and
      (JsPath \ "package_discounts").read[List[PackageDiscount]] and
      (JsPath \ "start_date").read[LocalDate] and
      (JsPath \ "end_date").readNullable[LocalDate] and
      (JsPath\ "max_uses").readNullable[Int] and
      (JsPath \ "note").readNullable[String] and
      (JsPath \ "suspended").readNullable[Boolean].map(_.getOrElse(false)) and
      (JsPath \ "num_uses").read[Int] and
      (JsPath \ "created_at").read[ZonedDateTime] and
      (JsPath \ "updated_at").read[ZonedDateTime] 
    ) (Coupon.apply _)
}
