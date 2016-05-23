package com.innoq.leanpubclient

import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.WordSpec
import play.api.libs.json._
import java.time.{ZoneOffset, LocalDate, ZonedDateTime}

class CouponJsonSpec extends WordSpec with TypeCheckedTripleEquals {

  "The Coupon Class" when {
    "reading JSON" which {
      "is valid and contains all Coupon Data" should {
        "return a Coupon Object with all fields" in {
          val json = """{
            "coupon_code": "test",
            "created_at": "2012-01-01T12:00:00Z",
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
          val createdAt = ZonedDateTime.of(2012, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC)
          val updatedAt = ZonedDateTime.of(2013, 4, 17, 22, 12, 58, 0, ZoneOffset.UTC)
          val endDate = LocalDate.of(2016, 5, 17)
          val startDate = LocalDate.of(2013, 4, 17)

          assert(coupon === Coupon(couponCode = "test",
                                  createdAt = createdAt,
                                  packageDiscounts = List(PackageDiscount("book", 2.0)),
                                  endDate = Some(endDate),
                                  maxUses = Some(15),
                                  note = Some("This is not a real coupon"),
                                  numUses = 12,
                                  startDate = startDate,
                                  suspended = false,
                                  updatedAt = updatedAt,
                                  bookSlug = "yourbook"))
        }
      }
    }
  }

  "The Coupon Class" when {
    "reading JSON" which {
      "is valid and contains all non-optional Coupon Data" should {
        "return a Coupon Object with all required fields" in {
          val json = """{
            "coupon_code": "test",
            "created_at": "2012-01-01T12:00:00Z",
            "package_discounts": [
              {
                "package_slug": "book",
                "discounted_price": 2.0
              }
            ],
            "num_uses": 12,
            "start_date": "2013-04-17",
            "updated_at": "2013-04-17T22:12:58Z",
            "book_slug": "yourbook"
          }"""
          val coupon = Json.parse(json).as[Coupon]
          val createdAt = ZonedDateTime.of(2012, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC)
          val updatedAt = ZonedDateTime.of(2013, 4, 17, 22, 12, 58, 0, ZoneOffset.UTC)
          val startDate = LocalDate.of(2013, 4, 17)

          assert(coupon === Coupon(couponCode = "test",
                                  createdAt = createdAt,
                                  packageDiscounts = List(PackageDiscount("book", 2.0)),
                                  endDate = None,
                                  maxUses = None,
                                  note = None,
                                  numUses = 12,
                                  startDate = startDate,
                                  suspended = false,
                                  updatedAt = updatedAt,
                                  bookSlug = "yourbook"))
        }
      }
    }
  }

  "The Coupon Class" when {
    "reading JSON" which {
      "is valid but lacks parts of Coupon Data" should {
        "throw a JsResultException" in {
          val json = """{
            "coupon_code": "test",
            "created_at": "2012-01-01T12:00:00Z",
            "package_discounts": [
              {
                "package_slug": "book",
                "discounted_price": 2.0
              }
            ],
            "end_date": "2016-05-17"
          }"""
          intercept[JsResultException] { Json.parse(json).as[Coupon] }
        }
      }
    }
  }

}
