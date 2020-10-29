package biddingAgent

import akka.http.scaladsl.testkit.ScalatestRouteTest
import biddingAgent.data.BidRequest
import io.circe.Decoder.Result
import org.scalatest.{FlatSpec, Matchers, WordSpec}
import org.scalatest.concurrent.ScalaFutures
import io.circe._
import io.circe.parser._
import io.circe.syntax._

class TestJsonSpec extends FlatSpec with Matchers  {
  "BidRequest" should "be decoded from JSON" in {
    val Right(parsedBidRequest) = decode[BidRequest](TestValues.textBidRequest)
    assert(parsedBidRequest == TestValues.bidRequest)
  }

  "BidResponse" should "be encoded to JSON" in {
    val bidResponseJson: Json = TestValues.bidResponse.asJson
    val text = bidResponseJson.printWith(Printer.spaces2)
    assert(text == TestValues.textBidResponse)
  }

}
