name := "server"
 
version := "1.0" 
      
lazy val `server` = (project in file(".")).enablePlugins(PlayScala)

      
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
      
scalaVersion := "2.13.5"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice )

libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "2.2.1"

libraryDependencies += "org.json4s" %% "json4s-native" % "4.0.2"
libraryDependencies += "org.json4s" %% "json4s-jackson" % "4.0.2"


