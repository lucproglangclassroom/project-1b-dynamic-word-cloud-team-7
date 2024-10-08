name := "hello-scalatest-scala"

version := "0.3"

scalaVersion := "3.3.3"

scalacOptions += "@.scalacOptions.txt"

libraryDependencies ++= Seq(
  "com.lihaoyi" %% "mainargs" % "0.6.3",
  "org.scalatest"  %% "scalatest"  % "3.2.19"  % Test,
  "org.scalacheck" %% "scalacheck" % "1.18.0" % Test,
  "org.log4s" %% "log4s" % "1.10.0",
  "org.slf4j" % "slf4j-simple" % "1.7.30",
  "org.apache.commons" % "commons-collections4" % "4.4",
  "org.scalatest" %% "scalatest" % "3.2.10" % "test",
  "org.scalatestplus" %% "scalacheck-1-15" % "3.2.9.0" % "test",
  "org.scalatest" %% "scalatest-propspec" % "3.2.10" % "test"
)

enablePlugins(JavaAppPackaging)
