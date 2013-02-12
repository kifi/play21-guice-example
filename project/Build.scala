import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "play21guicedemo"
  val appVersion      = "0.1-SNAPSHOT"

  val appDependencies = Seq(
    "com.google.inject" % "guice" % "3.0",
    "com.tzavellas" % "sse-guice" % "0.7.1"
    // Add your other project dependencies here:
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
  )

}