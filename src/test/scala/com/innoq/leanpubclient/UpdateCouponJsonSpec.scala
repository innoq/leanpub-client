package com.innoq.leanpubclient

import java.time.LocalDate

import com.innoq.leanpubclient.UpdateCoupon.EndDate._
import com.innoq.leanpubclient.UpdateCoupon.MaxUses._
import com.innoq.leanpubclient.UpdateCoupon.Note._
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.WordSpec
import play.api.libs.json._

class UpdateCouponJsonSpec extends WordSpec with TypeCheckedTripleEquals {

  "The UpdateCoupon Class" when {
    "writing JSON from a UpdateCoupon object" which {
      "contains some UpdateCoupon data" should {
        "return valid Json" in {
          val json: JsValue = Json.parse(
            """{
            "end_date": "2017-01-01",
            "max_uses": 50,
            "note": "I changed something",
            "suspended": true
            }""")
          val endDate = SpecificEndDate(LocalDate.of(2017, 1, 1))
          val updateCoupon = UpdateCoupon(endDate = Some(endDate),
                                          maxUses = Some(LimitedUses(50)),
                                          note = Some(TextNote("I changed something")),
                                          suspended = Some(true))
          assert(json === Json.toJson(updateCoupon))
        }
      }
    }
  }

  "The UpdateCoupon Class" when {
    "writing JSON from a UpdateCoupon object" which {
      "contains UpdateCoupon data with null values" should {
        "return valid Json containing null values" in {
          val json: JsValue = Json.parse(
            """{
            "end_date": null,
            "max_uses": null,
            "note": null
            }""")
          val updateCoupon = UpdateCoupon(endDate = Some(NoEndDate),
                                          maxUses = Some(UnlimitedUses),
                                          note = Some(EmptyNote))
          assert(json === Json.toJson(updateCoupon))
        }
      }
    }
  }

  "The UpdateCoupon Class" when {
    "writing JSON from a UpdateCoupon object" which {
      "contains no data" should {
        "return valid empty Json" in {
          val json: JsValue = Json.parse("{}")
          val updateCoupon = UpdateCoupon()
          assert(json === Json.toJson(updateCoupon))
        }
      }
    }
  }
}


