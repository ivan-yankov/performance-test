
val scalaTestVersion = "3.2.9"

val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion

lazy val root = (project in file("."))
  .settings(
    organization := "yankov",
    name := "performance-test",
    scalaVersion := "2.12.15",
    scalacOptions += "-deprecation",
    version := readVersion.value(),
    isSnapshot := true,

    Test / parallelExecution := false,

    fork := true,
    outputStrategy := Some(StdoutOutput),
    connectInput := true,

    libraryDependencies += scalaTest
  )
