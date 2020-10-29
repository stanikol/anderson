package biddingAgent

import akka.stream.Materializer
import akka.stream.scaladsl.Sink
import biddingAgent.data._

import scala.concurrent.{ExecutionContext, Future}

object MatchingLogic {
  def matchCampaign(
      activeCampaigns: Seq[Campaign],
      bidRequest: BidRequest)(implicit
      materializer: Materializer,
      executionContext: ExecutionContext
  ): Future[Option[BidResponse]] = {
    Future
      .sequence(activeCampaigns.map(campaign => matches(campaign, bidRequest)))
      .map(_.flatMap(_.toList))
      .map(_.headOption)
  }

  def matches(campaign: Campaign, bidRequest: BidRequest)(implicit
      materializer: Materializer,
      executionContext: ExecutionContext
  ): Future[Option[BidResponse]] = {
    // matches bid floor
    lazy val matchedByPrice: List[Impression] =
      bidRequest.imp.toList.flatten.filter(
        _.bidfloor.forall(_ <= campaign.bid)
      ) // if bidFloor==None means take bid from campaign
    // matches country
    // NB: device.geo object has a higher priority than user.geo object
    lazy val geo: Option[String] = (bidRequest.device, bidRequest.user) match {
      case (Some(Device(_, Some(Geo(someCountry @ Some(_))))), _) => someCountry
      case (None, Some(User(_, Some(Geo(someCountry @ Some(_)))))) =>
        someCountry
      case _ => None
    }
    lazy val matchesCountry: Boolean = geo.contains(campaign.country)
    // matches width and height
    lazy val matchedImpressionBanners: List[(Impression, List[Banner])] =
      matchedByPrice
        .map { impression: Impression =>
          val matchedBanners: List[Banner] =
            campaign.banners.filter(matchesSize(_, impression))
          impression -> matchedBanners
        }
        .filter(_._2.nonEmpty)
    // matches siteId
    if (
      matchedByPrice.nonEmpty && matchesCountry &&
      matchedImpressionBanners.nonEmpty
    ) {
      val hasMatchingTargets: Future[Boolean] =
        campaign.targeting.targetedSiteIds
          .filter(_ == bidRequest.site.id)
          .runWith(Sink.headOption)
          .map(_.isDefined)
      hasMatchingTargets.map { hasMatchingTargets =>
        if (hasMatchingTargets) {
          val (impression, banners) = matchedImpressionBanners.head
          val price =
            impression.bidfloor.getOrElse(
              campaign.bid
            ) // in case if bidFloor==None means bid price
          Some(
            BidResponse(
              "response1",
              bidRequest.id,
              price,
              Some(campaign.id.toString),
              banners.headOption
            )
          )
        } else None
      }
    } else Future.successful(None)
  }

  def matchesSize(banner: Banner, impression: Impression): Boolean = {
    assert(Impression.isValid(impression), s"Invalid impression $impression!")
    import banner.{height, width}
    import impression._
    val matchesWidth: Boolean =
      w.contains(width) || wmin.exists(_ <= width) || wmax.exists(_ >= width)
    val matchesHeight: Boolean =
      h.contains(height) || hmin.exists(_ <= height) || hmax.exists(_ >= height)
    matchesWidth && matchesHeight
  }

//  def matches(banners: List[Banner], impressions: List[Impression]): Boolean = {
//    banners.exists(banner => impressions.exists(matches(banner)))
//  }

}
