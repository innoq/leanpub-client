package com.innoq.leanpubclient

import java.time.{LocalDate, ZonedDateTime}
import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
  * Created by tina on 23.05.16.
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
      (JsPath \ "package_discounts").writeNullable[List[PackageDiscount]] and
      (JsPath \ "start_date").writeNullable[LocalDate] and
      (JsPath \ "end_date").writeNullable[LocalDate] and
      (JsPath \ "max_uses").writeNullable[Int] and
      (JsPath \ "note").writeNullable[String] and
      (JsPath \ "suspended").writeNullable[Boolean]
    ) (unlift(UpdateCoupon.unapply))
}
