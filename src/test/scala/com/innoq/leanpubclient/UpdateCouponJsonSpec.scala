package com.innoq.leanpubclient

import com.innoq.leanpubclient.UpdateCoupon.MaxUses.LimitedUses
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
            "max_uses": 50,
            "note": "I changed something",
            "suspended": true
            }""")

          val updateCoupon = UpdateCoupon(maxUses = Some(LimitedUses(50)),
                                          note = Some("I changed something"),
                                          suspended = Some(true)
          )

          assert(json === Json.toJson(updateCoupon))
        }
      }
    }
  }
}
