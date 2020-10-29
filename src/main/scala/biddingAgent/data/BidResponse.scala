package biddingAgent.data
import io.circe._
import io.circe.generic.semiauto._
case class BidResponse(
    id: String,
    bidRequestId: String,
    price: Double,
    adid: Option[String], //adid is the campaign ID of the campaign selected
    banner: Option[Banner]
)

object BidResponse {
  implicit val encodeBidResponse: Encoder.AsObject[BidResponse] =
    deriveEncoder[BidResponse]
}
