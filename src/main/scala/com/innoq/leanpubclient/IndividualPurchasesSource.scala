package com.innoq.leanpubclient

import akka.NotUsed
import akka.stream.scaladsl.Source
import akka.stream.{Attributes, Outlet, SourceShape}
import akka.stream.stage.{GraphStage, GraphStageLogic, OutHandler}

import scala.concurrent.ExecutionContext

class IndividualPurchasesSource(client: LeanPubClient, slug: String)(implicit ec: ExecutionContext) extends GraphStage[SourceShape[IndividualPurchase]] {

  val out: Outlet[IndividualPurchase] = Outlet("IndividualPurchasesSource")
  override def shape: SourceShape[IndividualPurchase] = SourceShape(out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) {
    /*  val individualPurchase = IndividualPurchase("", "", "", None, None, ZonedDateTime.now, 0, "", None, "", "", "", "", "", ZonedDateTime.now)

      def makeIndividualPurchasesList(individualPurchase: IndividualPurchase): List[IndividualPurchase] = {
        var list: List[IndividualPurchase] = List.empty
        for (i <- 1 to 20) {
          list = individualPurchase :: list
        }
        list
      }*/

      var individualPurchaseList: List[IndividualPurchase] = List.empty
      var nextPage: Int = 1

      override def preStart(): Unit = {
        val future = client.getIndividualPurchases(slug, nextPage)
        val callback = getAsyncCallback[Option[List[IndividualPurchase]]] {
          case None => completeStage()
          case Some(purchases) =>
            individualPurchaseList = purchases
            nextPage += 1
        }
        future.foreach(callback.invoke)
      }

      setHandler(out, new OutHandler {
        override def onPull(): Unit = {
          individualPurchaseList match {
            case head :: tail =>
              push(out, head)
              individualPurchaseList = tail
            case Nil =>
              val future = client.getIndividualPurchases(slug, nextPage)
              val callback = getAsyncCallback[Option[List[IndividualPurchase]]] {
                case None => complete(out)
                case Some(list) => { list: List[IndividualPurchase] =>
                  list match {
                    case Nil => complete(out)
                    case purchases =>
                      individualPurchaseList = purchases
                      nextPage += 1
                  }
                }
              }
              future.foreach(callback.invoke)
          }
        }
      })
  }
}

object IndividualPurchasesSource {
  def apply(client: LeanPubClient, slug: String)(implicit ec: ExecutionContext): Source[IndividualPurchase, NotUsed] =
    Source.fromGraph(new IndividualPurchasesSource(client, slug))
}
