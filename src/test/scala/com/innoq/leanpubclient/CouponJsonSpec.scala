package com.innoq.leanpubclient

import org.scalatest.WordSpec
import play.api.libs.json._
import play.libs.Json

class CouponJsonSpec extends WordSpec {

  "The Coupon Class" when {
    "reading JSON" which {
      "is valid and contains all Coupon Data" should {
        "return a Coupon Object with all fields" in {
          val json = """{
            "coupon_code": "test",
            "created_at": "2016-01-01T12:00:00Z",
            "package_discounts": [
              {
                "package_slug": "book",
                "discounted_price": 2.0
              }
            ],
            "end_date": "2016-05-17",
            "max_uses": 15,
            "note": "This is not a real coupon",
            "num_uses": 12,
            "start_date": "2013-04-17",
            "suspended": false,
            "updated_at": "2013-04-17T22:12:58Z",
            "book_slug": "yourbook"
          }"""
          val coupon = Json.parse(json).as[Coupon]
        }
      }
    }
  }

}
