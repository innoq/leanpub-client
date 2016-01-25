import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import scala.concurrent.Await
import scala.concurrent.duration._


object Main extends App {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  val http = Http()

  val client = new LeanPubClient(http, sys.env.getOrElse("LEANPUB_API_KEY", ""))
  Await.result(client.triggerPreview("notabook77"), 5.seconds)
  http.shutdownAllConnectionPools() andThen { case _ => system.shutdown() }
}
