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
                    totalCopiesSold: Int,
                    totalRevenue: BigDecimal,
                    wordCount: Int,
                    wordCountPublished: Int,
                    author: String,
                    minimumPrice: BigDecimal,
                    suggestedPrice: BigDecimal,
                    possibleReaderCount: Int,
                    links: BookInfo.Links
                   )

object BookInfo {
  case class Links(self: String,
                   titlePage: String,
                   image: String,
                   pdfPreview: String,
                   epubPreview: String,
                   mobiPreview: String,
                   pdfPublished: String,
                   epubPublished: String,
                   mobiPublished: String
                  )

  val fields1to16: Reads[(String, String, String, String, ZonedDateTime, Option[String], Int, Int, Int, BigDecimal,
    Int, Int, String, BigDecimal, BigDecimal, Int)] = (
      (JsPath \ "slug").read[String] and
      (JsPath \ "subtitle").read[String] and
      (JsPath \ "title").read[String] and
      (JsPath \ "about_the_book").read[String] and
      (JsPath \ "last_published_at").read[ZonedDateTime] and
      (JsPath \ "meta_description").readNullable[String] and
      (JsPath \ "page_count").read[Int] and
      (JsPath \ "page_count_published").read[Int] and
      (JsPath \ "total_copies_sold").read[Int] and
      (JsPath \ "total_revenue").read[BigDecimal] and
      (JsPath \ "word_count").read[Int] and
      (JsPath \ "word_count_published").read[Int] and
      (JsPath \ "author_string").read[String] and
      (JsPath \ "minimum_price").read[BigDecimal] and
      (JsPath \ "suggested_price").read[BigDecimal] and
      (JsPath \ "possible_reader_count").read[Int]
    ).tupled

  val linksReads: Reads[(String, String, String, String, String, String, String, String, String)] = (
    (JsPath \ "url").read[String] and
      (JsPath \ "title_page_url").read[String] and
      (JsPath \ "image").read[String] and
      (JsPath \ "pdf_preview_url").read[String] and
      (JsPath \ "epub_preview_url").read[String] and
      (JsPath \ "mobi_preview_url").read[String] and
      (JsPath \ "pdf_published_url").read[String] and
      (JsPath \ "epub_published_url").read[String] and
      (JsPath \ "mobi_published_url").read[String]
    ).tupled

  implicit val bookInfoReads: Reads[BookInfo] = new Reads[BookInfo] {
    override def reads(json: JsValue): JsResult[BookInfo] = for {
      slug <- (json \ "slug").validate[String]
      subtitle <- (json \ "subtitle").validate[String]
      title <- (json \ "title").validate[String]
      about <- (json \ "about_the_book").validate[String]
      lastPublishedAt <- (json \ "last_published_at").validate[ZonedDateTime]
      metaDescription <- (json \ "meta_description").validateOpt[String]
      pageCount <- (json \ "page_count").validate[Int]
      pageCountPublished <- (json \ "page_count_published").validate[Int]
      totalCopiesSold <- (json \ "total_copies_sold").validate[Int]
      totalRevenue <- (json \ "total_revenue").validate[BigDecimal]
      wordCount <- (json \ "word_count").validate[Int]
      wordCountPublished <- (json \ "word_count_published").validate[Int]
      author <- (json \ "author_string").validate[String]
      minimumPrice <- (json \ "minimum_price").validate[BigDecimal]
      suggestedPrice <- (json \ "suggested_price").validate[BigDecimal]
      links <- linksReads
    } yield BookInfo(slug = slug, subtitle = subtitle, title = title, about = about, lastPublishedAt = lastPublishedAt,
                    metaDescription = metaDescription, pageCount = pageCount, pageCountPublished = pageCountPublished,
                    totalCopiesSold = totalCopiesSold, totalRevenue = totalRevenue, wordCount = wordCount,
                    wordCountPublished = wordCountPublished, author = author, minimumPrice = minimumPrice,
                    suggestedPrice = suggestedPrice, links = links
                    )
  }
}

