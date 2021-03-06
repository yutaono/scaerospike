import sbt._
import Keys._
import sbtrelease.ReleasePlugin._
import sbtrelease._
import ReleaseStateTransformations._

object Scaerospike extends Build {

  lazy val scaerospike: Project = Project("scaerospike", file("."),
    settings = Config.buildSettings ++
      Seq(
      	organization := "com.tapad.scaerospike"
      ) ++ Seq(libraryDependencies ++=
        Seq(
          "com.aerospike" % "aerospike-client" % "3.0.30",
          "io.netty" % "netty-buffer" % "4.0.23.Final",
          "org.scalatest" %% "scalatest" % (if (scalaVersion.value.startsWith("2.9.")) "1.9.2" else "2.2.0") % "test"
        )
      )
  )
}


object Config {
  val tapadNexus = "Scala Tools Nexus" at "http://nexus.tapad.com:8080/nexus/content/groups/aggregate/"
  lazy val publishToNexus = Seq(
    publishTo <<= (version) { version: String =>
      val nexus = "http://nexus.tapad.com:8080/nexus/content/repositories/"
      if (version.trim.endsWith("SNAPSHOT") || version.trim.endsWith("TAPAD"))
        Some("snapshots" at (nexus + "snapshots/"))
      else
        Some("releases" at (nexus + "releases/"))
    },
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false }
  )

  val buildSettings = Defaults.defaultSettings ++ releaseSettings ++ publishToNexus ++ Seq(
    organization := "com.tapad",
    scalaVersion := "2.10.4",
    crossScalaVersions := Seq("2.9.3", "2.10.4", "2.11.1"),
    resolvers += tapadNexus,
    publishArtifact in(Compile, packageDoc) := false
  )
}

