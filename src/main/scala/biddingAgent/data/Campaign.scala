package biddingAgent.data

import akka.stream.scaladsl.Source
import io.circe._, io.circe.generic.semiauto._

case class Campaign(id: Int, country: String, targeting: Targeting, banners: List[Banner], bid: Double)
case class Targeting(targetedSiteIds: Source[String, _]) // if targetedSiteIds is huge
case class Banner(id: Int, src: String, width: Int, height: Int)
object Banner {
  implicit val encodeBanner: Encoder.AsObject[Banner] = deriveEncoder[Banner]
}
