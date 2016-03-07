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
                    mobiPublishedUrl: String
                   )

object BookInfo {
  /*implicit val bookInfoReader: Reads[BookInfo] = (
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
      (JsPath \ "url").read[String] and
      (JsPath \ "title_page_url").read[String] and
      (JsPath \ "minimum_price").read[BigDecimal] and
      (JsPath \ "suggested_price").read[BigDecimal] and
      (JsPath \ "image").read[String] and
      (JsPath \ "possible_reader_count").read[Int] and
      (JsPath \ "pdf_preview_url").read[String] and
      (JsPath \ "epub_preview_url").read[String] and
      (JsPath \ "mobi_preview_url").read[String] and
      (JsPath \ "pdf_published_url").read[String] and
      (JsPath \ "epub_published_url").read[String] and
      (JsPath \ "mobi_published_url").read[String]
    ) (BookInfo.apply _)*/

  /*val fields1to19: Reads[(String, String, String, String, ZonedDateTime, Option[String], Int, Int, Int, BigDecimal,
    Int, Int, String, String, String, BigDecimal, BigDecimal, String, Int)] = (
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
      (JsPath \ "url").read[String] and
      (JsPath \ "title_page_url").read[String] and
      (JsPath \ "minimum_price").read[BigDecimal] and
      (JsPath \ "suggested_price").read[BigDecimal] and
      (JsPath \ "image").read[String] and
      (JsPath \ "possible_reader_count").read[Int]
    ).tupled

  val fields20to25: Reads[(String, String, String, String, String, String)] = (
      (JsPath \ "pdf_preview_url").read[String] and
      (JsPath \ "epub_preview_url").read[String] and
      (JsPath \ "mobi_preview_url").read[String] and
      (JsPath \ "pdf_published_url").read[String] and
      (JsPath \ "epub_published_url").read[String] and
      (JsPath \ "mobi_published_url").read[String]
    ).tupled

  implicit val bookInfoReads: Reads[BookInfo] = (
      fields1to19 and fields20to25
    ) (BookInfo.apply _)*/
}

