import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Success, Failure}
import scala.concurrent.ExecutionContext.Implicits.global


object Main extends App {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

/*  val responseFuture: Future[HttpResponse] = 
    Http().singleRequest(HttpRequest(uri="http://headers.jsontest.com/"))
*/
  
  def responseFuture(url: String): Future[HttpResponse] = {
    Http().singleRequest(HttpRequest(uri=url))
  }
  
  Await.result(responseFuture("http://jsonplaceholder.typicode.com/posts/1"), 10.second)

  responseFuture("http://jsonplaceholder.typicode.com/posts/1").onComplete {
    case Success(response) => 
      response.status match {
        case StatusCodes.OK => 
          println(response.status)
          println(s"this is the response:" + Unmarshal(response.entity).to[String])
        case _ => 
          println("Error!!! " + response.status)
      }
      system.shutdown()
    case Failure(e) => 
      println(s"Error-message $e")
      system.shutdown()
  }

}
