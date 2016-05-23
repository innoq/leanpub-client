package com.innoq.leanpubclient

import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.WordSpec
import play.api.libs.json._

class CouponUpdateJsonSpec extends WordSpec with TypeCheckedTripleEquals {

  "The CouponUpdate Class" when {
    "writing JSON from a CouponUpdate object" which {
      "contains some CouponUpdate data" should {
        "return valid Json" in {
          val json: JsValue = Json.parse(
            """{
            "max_uses": 50,
            "note": "I changed something",
            "suspended": true
            }""")

          val couponUpdate = CouponUpdate(maxUses = Some(50),
                                          note = Some("I changed something"),
                                          suspended = Some(true)
          )

          assert(json === Json.toJson(couponUpdate))
        }
      }
    }
  }
}
