package com.innoq.leanpubclient

import java.time.ZonedDateTime

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class BookInfo(slug: String,
                    subtitle: String,
                    title: String,
                    about: String,
                    lastPublishedAt: ZonedDateTime,
                    metaDescription: Option[String],
                    pageCount: Int,
                    pageCountPublished: Int,
                    totalCopiesSold: Int
/*                    totalRevenue: BigDecimal,
                    wordCount: Int,
                    wordCountPublished: Int,
                    author: String,
                    url: String,
                    titlePageUrl: String,
                    minimumPrice: BigDecimal,
                    suggestedPrice: BigDecimal,
                    imageUrl: String,
                    possibleReaderCount: Int,
                    pdfPreviewUrl: String,
                    epubPreviewUrl: String,
                    mobiPreviewUrl: String,
                    pdfPublishedUrl: String,
                    epubPublishedUrl: String,
                    mobiPublishedUrl: String*/
                   )

object BookInfo {
  implicit val bookInfoReader: Reads[BookInfo] = (
      (JsPath \ "slug").read[String] and
      (JsPath \ "subtitle").read[String] and
      (JsPath \ "title").read[String] and
      (JsPath \ "about_the_book").read[String] and
      (JsPath \ "last_published_at").read[ZonedDateTime] and
      (JsPath \ "meta_description").readNullable[String] and
      (JsPath \ "page_count").read[Int] and
      (JsPath \ "page_count_published").read[Int] and
      (JsPath \ "total_copies_sold").read[Int]
    ) (BookInfo.apply _)
}

