package fusion

import fusion.services.{Configuration, HTTP}
import org.http4s.server.blaze.BlazeServerBuilder
import zio.ZIO
import zio.clock.Clock
import zio.console.Console
import zio.interop.catz._
import org.http4s.syntax.kleisli._

object Programs {
  /**
   * First Program:
   * requires `Console` and `Configuration`
   */
  val program1: ZIO[Console with Configuration, Nothing, Unit] = for {
    c <- Configuration.conf
    _ <- zio.console.putStrLn(c.toString)
  } yield ()

  /**
   * Runtime will execute IO unsafe calls
   * (i.e. all the side effects)
   * and manage threading
   *
   * http://localhost:8080
   * http://localhost:8080/stock/457
   * http://localhost:8080/abracadabra
   *
   * Second Program:
   * requires `Clock` because of Async Operations
   */
  val program2: ZIO[Clock, Throwable, Unit] =
    ZIO.runtime[Clock] >>= { implicit runtime =>
      BlazeServerBuilder[STask]
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(HTTP.endpoints[STask].orNotFound)
        .serve
        .compile
        .drain
    }

}
