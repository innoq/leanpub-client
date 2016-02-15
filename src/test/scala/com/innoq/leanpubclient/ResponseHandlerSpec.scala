package com.innoq.leanpubclient

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{Uri, StatusCodes, HttpResponse}
import akka.stream.ActorMaterializer
import org.scalatest.WordSpec
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

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
          Await.result(resultF, 1.second)
        }
      }
    }
  }
}
