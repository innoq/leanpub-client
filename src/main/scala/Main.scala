import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
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
  
  Await.result(responseFuture("http://headers.jsontest.com/"), 10.second)

  responseFuture("http://headers.jsontest.com/").onComplete {
    case Success(response) => 
      println(s"this is the response: ${response.entity}")
      system.shutdown()
    case Failure(e) => 
      println(s"Error-message $e")
      system.shutdown()
  }

}
