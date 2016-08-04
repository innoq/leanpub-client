package com.innoq.leanpubclient

import java.util.concurrent.Executors
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import play.api.libs.json.{JsBoolean, JsObject, JsValue}
import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration._

object Main3 extends App with PlayJsonSupport {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))

  def sendRequest(req: HttpRequest) =
    Source.single(req)
      .via(Http().outgoingConnection(host = "localhost", port = 8080))
      .runWith(Sink.head)

  val future = sendRequest(HttpRequest()).map(response =>
    response.status match {
      case StatusCodes.OK =>
        Unmarshal(response.entity).to[JsValue].map(entity => {
          println(entity)
          entity match {
            case o: JsObject if o.value == Map("sucess" -> JsBoolean(true)) => {
              println(true)
              true
            }

            case e => {
              println(false)
              false
            }
          }
        }
        )
    })

  val result = Await.result(future, 5.seconds)
  println(result)
  system.terminate()

}