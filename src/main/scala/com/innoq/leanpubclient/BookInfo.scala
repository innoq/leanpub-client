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

  implicit val linksReads: Reads[BookInfo.Links] = (
    (JsPath \ "url").read[String] and
      (JsPath \ "title_page_url").read[String] and
      (JsPath \ "image").read[String] and
      (JsPath \ "pdf_preview_url").read[String] and
      (JsPath \ "epub_preview_url").read[String] and
      (JsPath \ "mobi_preview_url").read[String] and
      (JsPath \ "pdf_published_url").read[String] and
      (JsPath \ "epub_published_url").read[String] and
      (JsPath \ "mobi_published_url").read[String]
    ) (BookInfo.Links.apply _)

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
      possibleReaderCount <- (json \ "possible_reader_count").validate[Int]
      links <- json.validate[BookInfo.Links]
    } yield BookInfo(slug = slug, subtitle = subtitle, title = title, about = about, lastPublishedAt = lastPublishedAt,
                    metaDescription = metaDescription, pageCount = pageCount, pageCountPublished = pageCountPublished,
                    totalCopiesSold = totalCopiesSold, totalRevenue = totalRevenue, wordCount = wordCount,
                    wordCountPublished = wordCountPublished, author = author, minimumPrice = minimumPrice,
                    suggestedPrice = suggestedPrice, possibleReaderCount = possibleReaderCount, links = links
                    )
  }
}

