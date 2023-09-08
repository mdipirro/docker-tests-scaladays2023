# Running Docker-Based Integration Tests in Scala: A Case Study

This repository contains the code used in the [Running Docker-Based Integration Tests in Scala: A Case Study](https://scaladays.org/madrid-2023/running-docker-based-integration-tests-in-scala-a-case-study) talk at ScalaDays Madrid 2023.

In particular, it defines three SBT projects:

* The main project contains the code of a simple HTTP application. Such an application simply opens a socket on the `8080` port, waiting for incoming connections. The webpage it returns is quite simple and only shows the message "_Hello, Madrid_"; 
* The `sbt-docker-compose` subproject contains the integration tests written using [SBT-docker-compose](https://github.com/Kynetics/sbt-docker-compose);
* The `test-containers` subproject contains the integration tests written using [TestContainers-scala](https://github.com/testcontainers/testcontainers-scala).

## SBT-docker-compose

To run the tests in the `sbt-docker-compose` subproject, open an SBT shell and use the following commands:

```commandline
project sbtDockerCompose
dockerComposeTest
``` 

SBT will build a new Docker image named `scala-days-2023` and tagged with `latest`. It will then use it to run the tests implemented in [SampleSbtDockerComposeSpec](./sbt-docker-compose/src/test/scala/mdipirro/scaladays/SampleSbtDockerComposeSpec.scala).

## Test Containers

To run the tests in the `test-containers` subproject, open an SBT shell and use the following command:

```commandline
testContainers / test
```

SBT will build a new Docker image named `scala-days-2023` and tagged with `1.0.0`. It will then use it to run the tests implemented in [SampleTestContainersSpec](./test-containers/src/test/scala/mdipirro/scaladays/SampleTestContainersSpec.scala).