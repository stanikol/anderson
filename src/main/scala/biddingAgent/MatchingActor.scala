package biddingAgent

import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import biddingAgent.data.{BidRequest, BidResponse, Campaign}

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

object MatchingActor {
  sealed trait MatchCommand
  case class FindMatching(bidRequest: BidRequest, sender: ActorRef[Option[BidResponse]]) extends MatchCommand
  case class Result(bidResponse: Option[BidResponse], sender: ActorRef[Option[BidResponse]]) extends MatchCommand

  final case class MatchingResult(bidResponse: Option[BidResponse])

  def apply(activeCampaigns: Seq[Campaign])(implicit actorSystem: ActorSystem[_]): Behavior[MatchCommand] =
    Behaviors.receive{ (content, msg) =>
      implicit val ec: ExecutionContextExecutor = content.executionContext
      msg match {
        case FindMatching(bidRequest, sender) =>
          content.pipeToSelf(MatchingLogic.matchCampaign(activeCampaigns, bidRequest)){
            case Success(bidResponse) => Result(bidResponse, sender)
            case Failure(exception) =>
              content.log.error("Error matching bid {}: {}", bidRequest, exception.getMessage)
              Result(None, sender)
          }
        case Result(response, sender) => sender ! response
      }
      Behaviors.same
    }

}
