package com.innoq.leanpubclient

import java.time.ZonedDateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._

/** Representation of an individual book purchase.
  *
  * Instances of this class can be retrieved by performing a GET request with [[LeanPubClient.getIndividualPurchases]]
  * or via a source by using [[LeanPubClient.getIndividualPurchaseSource]].
  * @param causeRoyaltyPercentage cause royalties percentage
  * @param authorRoyaltyPercentage author royalties percentage
  * @param authorRoyalties author royalties
  * @param authorPaidOutAt date author will be paid
  * @param causePaidOutAt date cause will be paid
  * @param createdAt time and date entry was created
  * @param royaltyDaysHold days royalties will be hold
  * @param publisherRoyalties publisher royalties
  * @param publisherPaidOutAt date publisher will be paid
  * @param authorUsername author username
  * @param publisherSlug publisher slug
  * @param userEmail user email address
  * @param purchaseUuid purchase UUID
  * @param invoiceId invoice Id
  * @param datePurchased date of purchase
  */
case class IndividualPurchase(causeRoyaltyPercentage: String,
                             authorRoyaltyPercentage: String,
                             authorRoyalties: String,
                             authorPaidOutAt: Option[ZonedDateTime],
                             causePaidOutAt: Option[ZonedDateTime],
                             createdAt: ZonedDateTime,
                             royaltyDaysHold: Int,
                             publisherRoyalties: String,
                             publisherPaidOutAt: Option[ZonedDateTime],
                             authorUsername: String,
                             publisherSlug: String,
                             userEmail: String,
                             purchaseUuid: String,
                             invoiceId: String,
                             datePurchased: ZonedDateTime)

object IndividualPurchase {
  implicit val indvidualPurchaseReads: Reads[IndividualPurchase] = (
      (JsPath \ "cause_royalty_percentage").read[String] and
      (JsPath \ "author_royalty_percentage").read[String] and
      (JsPath \ "author_royalties").read[String] and
      (JsPath \ "author_paid_out_at").readNullable[ZonedDateTime] and
      (JsPath \ "cause_paid_out_at").readNullable[ZonedDateTime] and
      (JsPath \ "created_at").read[ZonedDateTime] and
      (JsPath \ "royalty_days_hold").read[Int] and
      (JsPath \ "publisher_royalties").read[String] and
      (JsPath \ "publisher_paid_out_at").readNullable[ZonedDateTime] and
      (JsPath \ "author_username").read[String] and
      (JsPath \ "publisher_slug").read[String] and
      (JsPath \ "user_email").read[String] and
      (JsPath \ "purchase_uuid").read[String] and
      (JsPath \ "invoice_id").read[String] and
      (JsPath \ "date_purchased").read[ZonedDateTime]
    ) (IndividualPurchase.apply _)
}

