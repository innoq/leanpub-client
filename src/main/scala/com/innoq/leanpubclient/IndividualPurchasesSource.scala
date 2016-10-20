package com.innoq.leanpubclient

import java.time.ZonedDateTime

import akka.NotUsed
import akka.stream.scaladsl.Source
import akka.stream.{Attributes, Outlet, SourceShape}
import akka.stream.stage.{GraphStage, GraphStageLogic, OutHandler}

class IndividualPurchasesSource(client: LeanPubClient, slug: String) extends GraphStage[SourceShape[IndividualPurchase]] {

  val out: Outlet[IndividualPurchase] = Outlet("IndividualPurchasesSource")
  override def shape: SourceShape[IndividualPurchase] = SourceShape(out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) {
      val individualPurchase = IndividualPurchase("", "", "", None, None, ZonedDateTime.now, 0, "", None, "", "", "", "", "", ZonedDateTime.now)

      def makeIndividualPurchasesList(individualPurchase: IndividualPurchase): List[IndividualPurchase] = {
        var list: List[IndividualPurchase] = List.empty
        for (i <- 1 to 20) {
          list = individualPurchase :: list
        }
        list
      }

      var individualPurchaseList = makeIndividualPurchasesList(individualPurchase)

      setHandler(out, new OutHandler {
        override def onPull(): Unit = {
          individualPurchaseList match {
            case head :: tail =>
              push(out, head)
              individualPurchaseList = tail
            case Nil => complete(out)
          }
        }
      })
  }
}

object IndividualPurchasesSource {
  def apply(client: LeanPubClient, slug: String): Source[IndividualPurchase, NotUsed] =
    Source.fromGraph(new IndividualPurchasesSource(client, slug))
}
