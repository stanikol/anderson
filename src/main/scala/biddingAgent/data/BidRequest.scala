package biddingAgent.data
import io.circe._
import io.circe.generic.semiauto._


case class BidRequest(id: String, imp: Option[List[Impression]], site: Site, user: Option[User], device: Option[Device])
//case class Impression(id: String, wmin: Option[Int], wmax: Option[Int],
//                      w: Option[Int], hmin: Option[Int], hmax: Option[Int], h: Option[Int],
//                      bidFloor: Option[Double]) <- changed
case class Impression(id: String, wmin: Option[Int], wmax: Option[Int], w: Option[Int], hmin: Option[Int], hmax: Option[Int], h: Option[Int], bidfloor: Option[Double])
object Impression{
  def isValid(impression: Impression): Boolean = {
    import impression._
    List(wmin, wmax, w).flatMap(_.toList).nonEmpty &&
      List(hmin, hmax, h).flatMap(_.toList).nonEmpty
  }
}
//case class Site(id: Int, domain: String) <- changed
case class Site(id: String, domain: String)
case class User(id: String, geo: Option[Geo])
case class Device(id: String, geo: Option[Geo])
case class Geo(country: Option[String])

object BidRequest{
  implicit val decoderGeo: Decoder[Geo] = deriveDecoder[Geo]
  implicit val decoderDevice: Decoder[Device] = deriveDecoder[Device]
  implicit val decoderUser: Decoder[User] = deriveDecoder[User]
  implicit val decoderSite: Decoder[Site] = deriveDecoder[Site]
  implicit val decoderImpression: Decoder[Impression] = deriveDecoder[Impression]
  implicit val decoderBidRequest: Decoder[BidRequest] = deriveDecoder[BidRequest]

  def isValid(bidRequest: BidRequest): Boolean = {
      bidRequest.imp.forall(_.forall(Impression.isValid))
  }
}
