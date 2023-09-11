package mdipirro.scaladays

import java.io.PrintWriter
import java.net.ServerSocket

@main
def bootServer(): Unit =
  val text =
    """HTTP/1.0 200 OK
          Content-Type: text/html
          Content-Length: 200

          <html>
            <head>
              <title>Scala Days 2023</title>
             </head>
             <body lang="en-US">
              <p align="center">Hello, Madrid!</p>
            </body>
          </html>
    """.stripMargin
  val port = 8080
  val listener = ServerSocket(port)

  while (true) {
    val sock = listener.accept()
    PrintWriter(sock.getOutputStream, true).println(text)
    sock.shutdownOutput()
  }