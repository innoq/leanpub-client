package com.innoq.leanpubclient

import java.time.ZonedDateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Coupon(couponCode: String,
                  createdAt: ZonedDateTime,
                  packageDiscounts: List[PackageDiscount],
                  endDate: ZonedDateTime,
                  maxUses: Option[Int],
                  note: String,
                  numUses: Int,
                  startDate: ZonedDateTime,
                  suspended: Boolean,
                  updatedAt: ZonedDateTime,
                  bookSlug: String
                 )

object Coupon {
  implicit val couponReads: Reads[Coupon] = (
      (JsPath \ "coupon_code").read[String] and
      (JsPath \ "created_at").read[ZonedDateTime] and
      (JsPath \ "package_discounts").read[List[PackageDiscount]] and
      (JsPath \ "end_date").read[ZonedDateTime] and
      (JsPath\ "max_uses").readNullable[Int] and
      (JsPath \ "note").read[String] and
      (JsPath \ "num_uses").read[Int] and
      (JsPath \ "start_date").read[ZonedDateTime] and
      (JsPath \ "suspended").read[Boolean] and
      (JsPath \ "updated_at").read[ZonedDateTime] and
      (JsPath \ "book_slug").read[String]
    ) (Coupon.apply _)

  implicit val couponWrites: Writes[Coupon] = (
    (JsPath \ "coupon_code").write[String] and
      (JsPath \ "created_at").write[ZonedDateTime] and
      (JsPath \ "package_discounts").write[List[PackageDiscount]] and
      (JsPath \ "end_date").write[ZonedDateTime] and
      (JsPath\ "max_uses").writeNullable[Int] and
      (JsPath \ "note").write[String] and
      (JsPath \ "num_uses").write[Int] and
      (JsPath \ "start_date").write[ZonedDateTime] and
      (JsPath \ "suspended").write[Boolean] and
      (JsPath \ "updated_at").write[ZonedDateTime] and
      (JsPath \ "book_slug").write[String]
    ) (unlift(Coupon.unapply))
}
