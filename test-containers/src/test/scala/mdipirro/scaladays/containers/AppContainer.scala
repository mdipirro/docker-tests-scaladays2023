package mdipirro.scaladays.containers

import com.dimafeng.testcontainers.GenericContainer
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy

class AppContainer private(underlying: GenericContainer) extends GenericContainer(underlying):
  lazy val port: Int = mappedPort(AppContainer.Port)

object AppContainer:
  private val Port = 8080

  object Def extends GenericContainer.Def[AppContainer](
    AppContainer(
      GenericContainer(
        dockerImage = System.getenv("DOCKER_IMAGE_FULL_NAME"),
        exposedPorts = Seq(Port),
        waitStrategy = HostPortWaitStrategy()
      )
    )
  )