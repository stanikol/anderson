package biddingAgent
import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.http.scaladsl.model.MediaTypes.`application/json`
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.ValidationRejection
import akka.http.scaladsl.testkit.ScalatestRouteTest
import biddingAgent.data.BidResponse
import io.circe.generic.auto._
import io.circe.jawn.decode
import io.circe.syntax.EncoderOps
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
class TestRoutesSpec extends WordSpec with Matchers with ScalaFutures with ScalatestRouteTest {

  // the Akka HTTP route testkit does not yet support a typed actor system (https://github.com/akka/akka-http/issues/2036)
  // so we have to adapt for now
  lazy val testKit = ActorTestKit()
  implicit def typedSystem = testKit.system
  override def createActorSystem(): akka.actor.ActorSystem =
    testKit.system.classicSystem

  val matchingActor = testKit.spawn(MatchingActor(TestValues.activeCampaigns))
  lazy val routes = new Routes(matchingActor).routes

  "Routes" should {
    "return correct bid response" in {
        val bidRequestEntity = HttpEntity(`application/json`, TestValues.bidRequest.asJson.noSpaces)
        val request = Post("/bid").withEntity(bidRequestEntity)
        request ~> routes ~> check{
            val Right(response) = decode[BidResponse](entityAs[String])
            response shouldBe TestValues.bidResponse
        }
    }

    "return HTTP 204 StatusCode when bidfloor doesn't match" in {
      val bidRequestEntity = HttpEntity(`application/json`,
        TestValues.bidRequest204.asJson.noSpaces)
      val request = Post("/bid").withEntity(bidRequestEntity)
      request ~> routes ~> check{
        status should ===(StatusCodes.NoContent)
      }
    }

    "reject invalid impression" in {
      val bidRequestEntity = HttpEntity(`application/json`,
        TestValues.bidRequestInvalidImpression.asJson.noSpaces)
      val request = Post("/bid").withEntity(bidRequestEntity)
      request ~> routes ~> check{
        val m = "Got invalid bid request. Check if you have set correct dimensions for Impressions"
        rejection shouldEqual ValidationRejection(m, None)
      }
    }

  }
}
