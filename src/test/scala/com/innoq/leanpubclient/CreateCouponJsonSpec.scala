package com.innoq.leanpubclient

import java.time.LocalDate
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.WordSpec
import play.api.libs.json._

/**
  * Created by tina on 30.05.16.
  */
class CreateCouponJsonSpec extends WordSpec with TypeCheckedTripleEquals {

  "The CreateCoupon Class" when {
    "writing JSON from a CreateCoupon object" which {
      "contains all required CreateCoupon data" should {
        "return valid Json" in {

          val json: JsValue = Json.parse(
            """{
                "coupon_code": "test_coupon",
                "package_discounts_attributes": [
              {
                "package_slug": "book",
                "discounted_price": 2.0
              } ],
                "start_date": "2016-04-17",
                "suspended": false
                }""")
          val createCoupon = CreateCoupon(couponCode = "test_coupon",
                                          packageDiscounts = List(PackageDiscount("book", 2.0)),
                                          startDate = LocalDate.of(2016, 4, 17))

          assert(json === Json.toJson(createCoupon))

        }
      }
    }
  }

  "The CreateCoupon Class" when {
    "writing JSON from a CreateCoupon object" which {
      "contains all required and optional CreateCoupon data" should {
        "return valid Json" in {

          val json = Json.parse("""{
            "coupon_code": "test",
            "package_discounts_attributes": [
              {
                "package_slug": "book",
                "discounted_price": 2.0
              }
            ],
            "start_date": "2013-04-17",
            "end_date": "2016-05-17",
            "max_uses": 15,
            "note": "This is not a real coupon",
            "suspended": false
          }""")
          val endDate = LocalDate.of(2016, 5, 17)
          val startDate = LocalDate.of(2013, 4, 17)
          val createCoupon = CreateCoupon(couponCode = "test",
                                          packageDiscounts = List(PackageDiscount("book", 2.0)),
                                          startDate = startDate,
                                          endDate = Some(endDate),
                                          maxUses = Some(15),
                                          note = Some("This is not a real coupon"),
                                          suspended = false)
          assert(json === Json.toJson(createCoupon))
        }
      }
    }
  }
}
