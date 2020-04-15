import Dependencies._

enablePlugins(GraalVMNativeImagePlugin)

graalVMNativeImageOptions ++= Seq(
  "--report-unsupported-elements-at-runtime",
  "--no-fallback",
  "--allow-incomplete-classpath",
  "--initialize-at-build-time",
  "--verbose"
)

lazy val root = (project in file("."))
  .settings(
    organization := "io.github.frankbo",
    name := "vocabulary-extension-api",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.1",
    libraryDependencies ++= dependencies,
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.0")
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Xfatal-warnings",
)
