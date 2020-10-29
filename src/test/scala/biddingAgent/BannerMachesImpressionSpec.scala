package biddingAgent

import biddingAgent.data.{Banner, Impression}
import org.scalatest.FlatSpec

class BannerMachesImpressionSpec extends FlatSpec {
  "Banner Impression matching by size" should "work" in {
    assert(MatchingLogic.matchesSize(
      Banner(0, "src", 100, 200),
      Impression("id", None, None, Some(100), None, None, Some(200), Some(1))))
    assert(!MatchingLogic.matchesSize(
      Banner(0, "src", 120, 200),
      Impression("id", None, None, Some(100), None, None, Some(200), Some(1))
    ))
    assert(MatchingLogic.matchesSize(
      Banner(0, "src", 100, 200),
      Impression("id", Some(100), None, None, Some(200), None, None, Some(1))
    ))
    assert(!MatchingLogic.matchesSize(
      Banner(0, "src", 100, 200),
      Impression("id", None, Some(101), None, None, Some(199), None, Some(1))
    ))
  }


}
