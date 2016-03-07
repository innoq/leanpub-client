package com.innoq.leanpubclient

import java.time.ZonedDateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._

import scala.collection.immutable.HashMap

case class Coupon(couponCode: String,
                  createdAt: ZonedDateTime,
                  //packageDiscounts: Array[HashMap],
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
        //(JsPath \ "package_discounts").read[]
        (JsPath \ "end_date").read[ZonedDateTime] and
        (JsPath\ "max_uses").readNullable[Int] and
        (JsPath \ "note").read[String] and
        (JsPath \ "num_uses").read[Int] and
        (JsPath \ "start_date").read[ZonedDateTime] and
        (JsPath \ "suspended").read[Boolean] and
        (JsPath \ "updated_at").read[ZonedDateTime] and
        (JsPath \ "book_slug").read[String]
    ) (Coupon.apply _)
}
