import sbt._

object Dependencies {
  val Http4sVersion = "0.21.1"
  val CirceVersion = "0.13.0"
  val Specs2Version = "4.8.3"
  val LogbackVersion = "1.2.3"
  val doobieVersion = "0.8.8"

  lazy val http4s = Seq(
    "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
    "org.http4s" %% "http4s-blaze-client" % Http4sVersion,
    "org.http4s" %% "http4s-dsl" % Http4sVersion,
  )
  lazy val circe = Seq(
    "io.circe" %% "circe-generic" % CirceVersion,
    "org.http4s" %% "http4s-circe" % Http4sVersion,
  )
  lazy val specs2 = Seq(
    "org.specs2" %% "specs2-core" % Specs2Version % "test",
  )
  lazy val logback = Seq(
    "ch.qos.logback" % "logback-classic" % LogbackVersion
  )
  lazy val doobie = Seq(
    "org.tpolecat" %% "doobie-core" % doobieVersion,
    "org.tpolecat" %% "doobie-postgres" % doobieVersion,
    "org.tpolecat" %% "doobie-specs2" % doobieVersion % "test",
  )
  lazy val dependencies = http4s ++ circe ++ specs2 ++ logback ++ doobie
}
