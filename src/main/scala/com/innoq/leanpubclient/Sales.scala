package com.innoq.leanpubclient

import play.api.libs.functional.syntax._
import play.api.libs.json._

/** Sales is the response of the GET request [[LeanPubClient.getSales]]
  *
  * @param slug, usually book's title
  * @param url book's url
  * @param totalAuthorRoyalties total author royalties
  * @param totalBookRoyalties total book royalties
  * @param numHappyReaders number of readers without returned books
  * @param numHappyPaidPurchases number of paid purchases without returned books
  * @param numRefundedPurchases number of refunds
  * @param unpaidRoyalties unpaid royalties
  * @param royaltiesCurrentlyDue royalties currently due
  * @param royaltiesDueOnFirstNextMonth royalties due on first of next month
  * @param paidRoyalties paid royalties
  */
case class Sales(slug: String,
                 url: String,
                 totalAuthorRoyalties: String,
                 totalBookRoyalties: String,
                 numHappyReaders: Int,
                 numHappyPaidPurchases: Int,
                 numRefundedPurchases: Int,
                 unpaidRoyalties: String,
                 royaltiesCurrentlyDue: String,
                 royaltiesDueOnFirstNextMonth: String,
                 paidRoyalties: String
                )

object Sales {
  implicit val salesReads: Reads[Sales] = (
    (JsPath \ "book").read[String] and
      (JsPath \ "url").read[String] and
      (JsPath \ "total_author_royalties").read[String] and
      (JsPath \ "total_book_royalties").read[String] and
      (JsPath \ "num_happy_readers").read[Int] and
      (JsPath \ "num_happy_paid_purchases").read[Int] and
      (JsPath \ "num_refunded_purchases").read[Int] and
      (JsPath \ "unpaid_royalties").read[String] and
      (JsPath \ "royalties_currently_due").read[String] and
      (JsPath \ "royalties_due_on_first_of_next_month").read[String] and
      (JsPath \ "paid_royalties").read[String]
    ) (Sales.apply _)
}
