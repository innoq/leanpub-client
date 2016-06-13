package com.innoq.leanpubclient

import java.time.LocalDate
import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
  * Created by tina on 30.05.16.
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
