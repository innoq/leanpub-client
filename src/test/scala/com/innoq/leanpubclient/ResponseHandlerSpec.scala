package com.innoq.leanpubclient

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.scalatest.WordSpec
import org.scalatest.concurrent.ScalaFutures
import play.api.libs.json.{JsBoolean, JsObject, Json}

class ResponseHandlerSpec extends WordSpec with ScalaFutures {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()


  "The ResponseHandler" when {
    "handling a POST response" which {
      "has a status code 200 and returns Json with a success message" should {
        "return a successful Future[Result.Success]" in {
          val response = StubResponse(200, Json.obj("success" -> JsBoolean(true)))
          val url = "http://example.com"
          val result = ResponseHandler.handleResponseToPost(url, response)
          assert(result == Result.Success)
        }
      }
    }
  }

  "The Response Handler" when {
    "handling a POST response" which {
      "has a status code 404" should {
        "return a successful Future containing a NotFoundError" in {
          val response = StubResponse(404, JsObject(Seq.empty))
          val url = "http://example.com"
          val result = ResponseHandler.handleResponseToPost(url, response)
          assert(result == Result.NotFoundError(url, 404))
        }
      }
    }
  }

  "The Response Handler" when {
    "handling a GET response" which {
      "has a status code 200" should {
        "return a successful Future[Option[JsValue]]" in {
          val response = StubResponse(200, JsObject(Seq.empty))
          val url = "http://example.com"
          val result = ResponseHandler.handleResponseToGet(url, response)
          assert(result == Option(JsObject(Seq.empty)))
        }
      }
    }
  }

  "The Response Handler" when {
    "handling a GET response" which {
      "has a status code 404" should {
        "return a Future.successful(None)" in {
          val response = StubResponse(404, JsObject(Seq.empty))
          val url = "http://example.com"
          val result = ResponseHandler.handleResponseToGet(url, response)
          assert(result.isEmpty)
        }
      }
    }
  }
}
