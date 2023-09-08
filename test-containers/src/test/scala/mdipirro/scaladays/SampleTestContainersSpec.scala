package mdipirro.scaladays

import com.dimafeng.testcontainers.scalatest.TestContainerForAll
import mdipirro.scaladays.containers.AppContainer
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.{EitherValues, Informing}
import sttp.client3.{SimpleHttpClient, UriContext, basicRequest}

class SampleTestContainersSpec extends AnyFunSuite
  with EitherValues
  with Informing
  with Matchers
  with TestContainerForAll:

  override val containerDef: AppContainer.Def.type = AppContainer.Def

  test("The app returns a success code and the string 'Hello, Madrid!'") {
    withContainers { app =>
      val host = app.host
      val port = app.port

      info(s"Attempting to connect to: $host at port $port, container id is ${app.containerId}")

      val client = SimpleHttpClient()
      val req = basicRequest.get(uri"http://$host:$port")

      val resp = client.send(req)
      resp.code.isSuccess shouldBe true
      resp.body.value should include("Hello, Madrid!")
    }
  }