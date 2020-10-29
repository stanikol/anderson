package biddingAgent


import akka.actor.typed.scaladsl.AskPattern.{Askable, schedulerFromActorSystem}
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import biddingAgent.data.{BidRequest, BidResponse}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

import scala.concurrent.Future

class Routes(matchingActor: ActorRef[MatchingActor.MatchCommand])(implicit val system: ActorSystem[_]) {

  private implicit val timeout: Timeout = Timeout.create(system.settings.config.getDuration("bidding-agent.routes.ask-timeout"))

  def askForMatching(bidRequest: BidRequest): Future[Option[BidResponse]] =
    matchingActor.ask[Option[BidResponse]](MatchingActor.FindMatching(bidRequest, _))

  val routes: Route = pathPrefix("bid") {
      post {
        entity(as[BidRequest]) { bidRequest: BidRequest =>
          validate(BidRequest.isValid(bidRequest), "Got invalid bid request. Check if you have set correct dimensions for Impressions") {
              system.log.info("Got bidRequest {}", bidRequest)
              onSuccess(askForMatching(bidRequest)) {
                case Some(bidResponse) => complete(bidResponse)
                case None => complete(StatusCodes.NoContent)
              }
            }
          }
        }
      }

}
