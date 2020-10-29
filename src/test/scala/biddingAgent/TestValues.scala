package biddingAgent

import akka.stream.scaladsl.Source
import biddingAgent.data._
import shapeless.ops.nat.LT

object TestValues {

  val textBidRequest = """{
                         |  "id": "SGu1Jpq1IO",
                         |  "site": {
                         |	"id": "0006a522ce0f4bbbbaa6b3c38cafaa0f",
                         |	"domain": "fake.tld"
                         |  },
                         |  "device": {
                         |	"id": "440579f4b408831516ebd02f6e1c31b4",
                         |	"geo": {
                         |  	"country": "LT"
                         |	}
                         |  },
                         |  "imp": [
                         |	{
                         |  	"id": "1",
                         |  	"wmin": 50,
                         |  	"wmax": 300,
                         |  	"hmin": 100,
                         |  	"hmax": 300,
                         |  	"h": 250,
                         |  	"w": 300,
                         |  	"bidfloor": 3.12123
                         |	}
                         |  ],
                         |  "user": {
                         |	"geo": {
                         |  	"country": "LT"
                         |	},
                         |	"id": "USARIO1"
                         |  }
                         |}""".stripMargin
  val bidRequest = BidRequest("SGu1Jpq1IO",
    Some(List(Impression("1",Some(50),Some(300),Some(300),Some(100),Some(300),Some(250), Some(3.12123)))),
    Site("0006a522ce0f4bbbbaa6b3c38cafaa0f","fake.tld"),
    Some(User("USARIO1",Some(Geo(Some("LT"))))),
    Some(Device("440579f4b408831516ebd02f6e1c31b4",Some(Geo(Some("LT"))))))

  val bidRequest204 = BidRequest("SGu1Jpq1IO",
    Some(List(Impression("1",Some(50),Some(300),Some(300),Some(100),Some(300),Some(250), Some(333.12123)))),
    Site("0006a522ce0f4bbbbaa6b3c38cafaa0f","fake.tld"),
    Some(User("USARIO1",Some(Geo(Some("LT"))))),
    Some(Device("440579f4b408831516ebd02f6e1c31b4",Some(Geo(Some("LT"))))))

  val bidRequestInvalidImpression = BidRequest("SGu1Jpq1IO",
    Some(List(Impression("1",None,None,None,None,None,None, Some(333.12123)))),
    Site("0006a522ce0f4bbbbaa6b3c38cafaa0f","fake.tld"),
    Some(User("USARIO1",Some(Geo(Some("LT"))))),
    Some(Device("440579f4b408831516ebd02f6e1c31b4",Some(Geo(Some("LT"))))))
  val bidResponse = BidResponse("response1", "SGu1Jpq1IO", 3.12123, Some("1"), Some(Banner(1, "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph.jpeg", 300, 250)))
  val textBidResponse = """{
                          |  "id" : "response1",
                          |  "bidRequestId" : "SGu1Jpq1IO",
                          |  "price" : 3.12123,
                          |  "adid" : "1",
                          |  "banner" : {
                          |    "id" : 1,
                          |    "src" : "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph.jpeg",
                          |    "width" : 300,
                          |    "height" : 250
                          |  }
                          |}""".stripMargin


  val activeCampaigns = Seq(
    Campaign(
      id = 1,
      country = "LT",
      targeting = Targeting(
        targetedSiteIds = Source.single("0006a522ce0f4bbbbaa6b3c38cafaa0f") // Use collection of your choice
      ),
      banners = List(
        Banner(
          id = 1,
          src = "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph.jpeg",
          width = 300,
          height = 250
        )
      ),
      bid = 5d
    )
  )
}
