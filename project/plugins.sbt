ThisBuild / libraryDependencySchemes ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
)

addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.9.11")

lazy val `sbt-docker-compose-plugin` = RootProject(uri("https://github.com/Kynetics/sbt-docker-compose.git"))
lazy val root = project in file(".") dependsOn `sbt-docker-compose-plugin`