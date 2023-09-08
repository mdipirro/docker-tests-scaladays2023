import sbt.Keys._
import java.io.File

enablePlugins(JavaAppPackaging, AshScriptPlugin, DockerPlugin)
enablePlugins(DockerComposePlugin)

testCasesPackageTask := (Test / packageBin).value
testCasesJar := (Test / packageBin / artifactPath).value.getAbsolutePath
testDependenciesClasspath := {
  val fullClasspathCompile = (Compile / fullClasspath).value
  val classpathTestManaged = (Test / managedClasspath).value
  val classpathTestUnmanaged = (Test / unmanagedClasspath).value
  val testResources = (Test / resources).value
  (fullClasspathCompile.files ++ classpathTestManaged.files ++ classpathTestUnmanaged.files ++ testResources).map(_.getAbsoluteFile).mkString(File.pathSeparator)
}