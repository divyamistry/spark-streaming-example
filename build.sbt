name := "spark-streaming-example"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies ++= Seq("org.apache.spark" %% "spark-streaming" % "2.0.2",
    "org.apache.spark" %% "spark-core" % "2.0.2",
    "org.scalaj" %% "scalaj-http" % "2.3.0",
    "org.jfarcand" % "wcs" % "1.5")
