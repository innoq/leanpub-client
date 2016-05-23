package com.innoq.leanpubclient

import java.time.{LocalDate, ZonedDateTime}
import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
  * Created by tina on 23.05.16.
  */
case class CouponUpdate(createdAt: Option[ZonedDateTime] = None,
                        packageDiscounts: Option[List[PackageDiscount]] = None,
                        endDate: Option[LocalDate] = None,
                        maxUses: Option[Int] = None,
                        note: Option[String] = None,
                        numUses: Option[Int] = None,
                        startDate: Option[LocalDate] = None,
                        suspended: Option[Boolean] = None,
                        updatedAt: Option[ZonedDateTime] = None,
                        bookSlug: Option[String] = None)

object CouponUpdate {
  implicit val couponUpdateWrites: Writes[CouponUpdate] = (
    (JsPath \ "created_at").writeNullable[ZonedDateTime] and
      (JsPath \ "package_discounts").writeNullable[List[PackageDiscount]] and
      (JsPath \ "end_date").writeNullable[LocalDate] and
      (JsPath\ "max_uses").writeNullable[Int] and
      (JsPath \ "note").writeNullable[String] and
      (JsPath \ "num_uses").writeNullable[Int] and
      (JsPath \ "start_date").writeNullable[LocalDate] and
      (JsPath \ "suspended").writeNullable[Boolean] and
      (JsPath \ "updated_at").writeNullable[ZonedDateTime] and
      (JsPath \ "book_slug").writeNullable[String]
    ) (unlift(CouponUpdate.unapply))
}
