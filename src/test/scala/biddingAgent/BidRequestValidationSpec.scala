package biddingAgent

import biddingAgent.data.{BidRequest, Impression, Site, User}
import org.scalatest.FlatSpec

class BidRequestValidationSpec extends FlatSpec {
  val invalidImpression = Impression("id", None, None, None, Some(1), Some(2), Some(3), Some(3d))
  val invalidImpression2 = Impression("id",  Some(1), Some(2), Some(3), None, None, None, Some(3d))
  val validImpression = Impression("id",  Some(1), None, None, Some(3), None, None, Some(3d))
  val validImpression2 = validImpression.copy(h = Some(10))
  "Validation on Impression" should "work" in {
      assert(!Impression.isValid(invalidImpression))
      assert(!Impression.isValid(invalidImpression2))
      assert(Impression.isValid(validImpression))
      assert(Impression.isValid(validImpression2))
  }

  val validBidRequest = BidRequest("id", None, Site("site-id", "www.no.such.site"), Some(User("user-id", None)), None)
  val validImpressions = List(validImpression, validImpression2)
  val validBidRequest2 = BidRequest("id", Some(validImpressions), Site("site-id", "www.no.such.site"), Some(User("user-id", None)), None)
  val validBidRequest3 = BidRequest("id", Some(List.empty), Site("site-id", "www.no.such.site"), Some(User("user-id", None)), None)
  val invalidBidRequest = BidRequest("id", Some(invalidImpression::validImpressions), Site("site-id", "www.no.such.site"), Some(User("user-id", None)), None)

  "Validation on BidRequest" should "work" in {
    assert(BidRequest.isValid(validBidRequest))
    assert(BidRequest.isValid(validBidRequest2))
    assert(BidRequest.isValid(validBidRequest3))
    assert(!BidRequest.isValid(invalidBidRequest))
  }

}
