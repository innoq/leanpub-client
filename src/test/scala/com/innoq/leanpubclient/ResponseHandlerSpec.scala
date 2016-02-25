package com.innoq.leanpubclient

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import org.scalatest.WordSpec
import org.scalatest.concurrent.ScalaFutures
import play.api.libs.json.JsObject

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by tina on 15.02.16.
  */
class ResponseHandlerSpec extends WordSpec with ScalaFutures {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()


  "The ResponseHandler" when {
    "handling a POST response" which {
      "has a status code 200" should {
        "return a successful Future[Unit]" in {
          val response = HttpResponse(status = StatusCodes.OK)
          val uri = Uri("http://example.com")
          val resultF = ResponseHandler.handleResponseToPost(uri, response)
          assert(resultF.isCompleted)
        }
      }
    }
  }

  "The Response Handler" when {
    "handling a POST response" which {
      "has a status code 404" should {
        "return a failed Future containing a UnexpectedStatusException" in {
          val response = HttpResponse(status = StatusCodes.NotFound)
          val uri = Uri("http://example.com")
          val resultF = ResponseHandler.handleResponseToPost(uri, response)
          assert(resultF.failed.futureValue == UnexpectedStatusException(uri, StatusCodes.NotFound))
        }
      }
    }
  }

  "The Response Handler" when {
    "handling a GET response" which {
      "has a status code 200" should {
        "return a successful Future[JsValue]" in {
          val entityValue = JsObject(Seq.empty)
          val response = HttpResponse(status = StatusCodes.OK, entity = HttpEntity(ContentTypes.`application/json`, entityValue.toString()))
          val uri = Uri("http://example.com")
          val resultF = ResponseHandler.handleResponseToGet(uri, response)
          assert(resultF.futureValue == JsObject(Seq.empty))
        }
      }
    }
  }

  "The Response Handler" when {
    "handling a GET response" which {
      "has a status code 404" should {
        "return a failed Future containing a UnexpectedStatusException" in {
          val response = HttpResponse(status = StatusCodes.NotFound)
          val uri = Uri("http://example.com")
          val resultF = ResponseHandler.handleResponseToGet(uri, response)
          assert(resultF.failed.futureValue == UnexpectedStatusException(uri, StatusCodes.NotFound))
        }
      }
    }
  }
}