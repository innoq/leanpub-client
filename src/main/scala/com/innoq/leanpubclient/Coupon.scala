package com.innoq.leanpubclient

import java.time.{LocalDate, ZonedDateTime}
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Coupon(couponCode: String,
                  createdAt: ZonedDateTime,
                  packageDiscounts: List[PackageDiscount],
                  endDate: Option[LocalDate],
                  maxUses: Option[Int],
                  note: Option[String],
                  numUses: Int,
                  startDate: LocalDate,
                  suspended: Boolean,
                  updatedAt: ZonedDateTime,
                  bookSlug: String
                 )

object Coupon {
  implicit val couponReads: Reads[Coupon] = (
      (JsPath \ "coupon_code").read[String] and
      (JsPath \ "created_at").read[ZonedDateTime] and
      (JsPath \ "package_discounts").read[List[PackageDiscount]] and
      (JsPath \ "end_date").readNullable[LocalDate] and
      (JsPath\ "max_uses").readNullable[Int] and
      (JsPath \ "note").readNullable[String] and
      (JsPath \ "num_uses").read[Int] and
      (JsPath \ "start_date").read[LocalDate] and
      (JsPath \ "suspended").readNullable[Boolean].map(_.getOrElse(false)) and
      (JsPath \ "updated_at").read[ZonedDateTime] and
      (JsPath \ "book_slug").read[String]
    ) (Coupon.apply _)
}
