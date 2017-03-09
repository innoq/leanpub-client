package com.innoq.leanpubclient

import java.time.LocalDate

import com.innoq.leanpubclient.UpdateCoupon.{EndDate, MaxUses, Note}
import play.api.libs.functional.syntax._
import play.api.libs.json._

/** Use UpdateCoupon to update a coupon for a book via [[LeanPubClient.updateCoupon]]
  *
  * Every parameter is optional for UpdateCoupons. See below what each parameter does
  * and use only those you want to update.
  * @param packageDiscounts adds the [[PackageDiscount]] you listed to the discounts which are already there
  * @param startDate start date for coupon validity
  * @param endDate end date for coupon validity. Please use custom datatype [[EndDate]] to specify if you want to set or unset it
  * @param maxUses maximum number of uses for a coupon, use custom datatype [[MaxUses]] to specify limited or unlimited use
  * @param note a description of the coupon. It is just used to remind you of what it was for. Please use custom datatype [[Note]] to set or remove the note
  * @param suspended whether coupon is suspended
  */
case class UpdateCoupon(packageDiscounts: Option[List[PackageDiscount]] = None,
                        startDate: Option[LocalDate] = None,
                        endDate: Option[EndDate] = None,
                        maxUses: Option[MaxUses] = None,
                        note: Option[Note] = None,
                        suspended: Option[Boolean] = None
                        )

object UpdateCoupon {

  sealed trait MaxUses
  object MaxUses {
    case object UnlimitedUses extends MaxUses
    case class LimitedUses(numberMaxUses: Int) extends MaxUses

    implicit val maxUsesWrites: Writes[MaxUses] = Writes {
      case UnlimitedUses => JsNull
      case LimitedUses(numberMaxUses) => JsNumber(numberMaxUses)
    }
  }

  sealed trait EndDate
  object EndDate {
    case object NoEndDate extends EndDate
    case class SpecificEndDate(endDate: LocalDate) extends EndDate

    implicit val endDateWrites: Writes[EndDate] = Writes {
      case NoEndDate => JsNull
      case SpecificEndDate(endDate) =>
        val dateString = endDate.toString
        Json.toJson(dateString)
    }
  }

  sealed trait Note
  object Note {
    case object EmptyNote extends Note
    case class TextNote(note: String) extends Note

    implicit val noteWrites: Writes[Note] = Writes {
      case EmptyNote => JsNull
      case TextNote(note) => Json.toJson(note)
    }
  }

  implicit val updateCouponWrites: Writes[UpdateCoupon] = (
      (JsPath \ "package_discounts_attributes").writeNullable[List[PackageDiscount]] and
      (JsPath \ "start_date").writeNullable[LocalDate] and
      (JsPath \ "end_date").writeNullable[EndDate] and
      (JsPath \ "max_uses").writeNullable[MaxUses] and
      (JsPath \ "note").writeNullable[Note] and
      (JsPath \ "suspended").writeNullable[Boolean]
    ) (unlift(UpdateCoupon.unapply))
}
