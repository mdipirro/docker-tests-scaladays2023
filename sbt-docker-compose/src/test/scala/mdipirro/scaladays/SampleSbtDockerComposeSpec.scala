package mdipirro.scaladays

import org.scalactic.source.Position
import org.scalatest.*
import org.scalatest.concurrent.*
import org.scalatest.exceptions.*
import org.scalatest.funsuite.FixtureAnyFunSuite
import org.scalatest.matchers.should.Matchers
import sttp.client3.{SimpleHttpClient, UriContext, basicRequest}

import java.io.{ByteArrayOutputStream, PrintWriter}
import scala.Console.*
import scala.sys.process.*

class SampleSbtDockerComposeSpec extends FixtureAnyFunSuite
  with fixture.ConfigMapFixture
  with Eventually
  with IntegrationPatience
  with EitherValues
  with Informing
  with Matchers:

  // The configMap passed to each test case will contain the connection information for the running Docker Compose
  // services. The key into the map is "serviceName:containerPort" and it will return "host:hostPort" which is the
  // Docker Compose generated endpoint that can be connected to at runtime. You can use this to endpoint connect to
  // for testing. Each service will also inject a "serviceName:containerId" key with the value equal to the container id.
  // You can use this to emulate service failures by killing and restarting the container.
  val ServiceName = "scalaTest"
  val ServiceHostKey = s"$ServiceName:8080"
  val ServiceContainerIdKey = s"$ServiceName:containerId"

  test("The app returns a success code and the string 'Hello, Madrid!'") {
    configMap => {
      val hostInfo = getHostInfo(configMap)
      val containerId = getContainerId(configMap)

      info(s"Attempting to connect to: $hostInfo, container id is $containerId")

      val client = SimpleHttpClient()
      val req = basicRequest.get(uri"http://$hostInfo")

      eventually {
        val resp = client.send(req)
        resp.code.isSuccess shouldBe true
        resp.body.value should include("Hello, Madrid!")
      }
    }
  }

  def getHostInfo(configMap: ConfigMap): String = getContainerSetting(configMap, ServiceHostKey)

  def getContainerId(configMap: ConfigMap): String = getContainerSetting(configMap, ServiceContainerIdKey)

  def getContainerSetting(configMap: ConfigMap, key: String): String = {
    if (configMap.keySet.contains(key)) {
      configMap(key).toString
    }
    else {
      throw new TestFailedException(
        message = s"Cannot find the expected Docker Compose service key '$key' in the configMap",
        failedCodeStackDepth = 10
      )
    }
  }