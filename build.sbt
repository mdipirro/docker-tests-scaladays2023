import sbt.Keys._

ThisBuild / version := "1.0.0"

ThisBuild / scalaVersion := "3.3.0"

lazy val root = (project in file("."))
  .settings(
    name := "scala-days-2023",
    Docker / name := name.value,
    Docker / version := version.value,
    Docker / maintainer := organizationName.value
  )
  .enablePlugins(JavaAppPackaging, AshScriptPlugin, DockerPlugin)

lazy val sbtDockerCompose = (project in file("sbt-docker-compose"))
  .dependsOn(root)
  .settings(
    name := "sbt-docker-compose",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.16",
      "com.softwaremill.sttp.client3" %% "core" % "3.8.16"
    ).map(_ % Test)
  )
  .settings(
    dockerImageCreationTask := (root / Docker / publishLocal).value,
    root / dockerUpdateLatest := true
  )

lazy val testContainers = (project in file("test-containers"))
  .dependsOn(root)
  .settings(
    name := "test-containers",
    fork := true,
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.16",
      "com.softwaremill.sttp.client3" %% "core" % "3.8.16",
      "com.dimafeng" %% "testcontainers-scala-scalatest" % "0.40.17"
    ).map(_ % Test),
    Test / envVars := Map(
      "DOCKER_IMAGE_FULL_NAME" -> s"${(root / Docker / packageName).value}:${version.value}",
    ),
    (Test / test) := ((Test / test) dependsOn (root / Docker / publishLocal)).value
  )