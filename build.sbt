lazy val akkaHttpVersion = "10.2.1"
lazy val akkaVersion    = "2.6.10"
lazy val circeVersion    = "0.13.0"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "Anderson",
      scalaVersion    := "2.13.3"
    )),
    name := "BiddingAgent",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_ % circeVersion),
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"                % akkaHttpVersion,
      "de.heikoseeberger" %% "akka-http-circe" % "1.31.0",
      "com.typesafe.akka" %% "akka-http-spray-json"     % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
      "ch.qos.logback"    % "logback-classic"           % "1.2.3",

      "com.typesafe.akka" %% "akka-http-testkit"        % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"                % "3.0.8"         % Test
    )
  )
