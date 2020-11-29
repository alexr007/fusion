package fusion

import fusion.Dependencies.ExtServices
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli._
import zio.interop.catz._
import zio.{ExitCode, RIO, URIO, ZIO}

object ServerApp extends CatsApp {

  /**
   * Runtime will execute IO unsafe calls
   * (i.e. all the side effects)
   * and manage threading
   */
  val program: RIO[ExtServices, Unit] =
    ZIO.runtime[ExtServices]
      .flatMap { implicit runtime: zio.Runtime[ExtServices] =>
        BlazeServerBuilder[STask]
          .bindHttp(8080, "0.0.0.0")
          .withHttpApp(HTTPService.routes.orNotFound)
          .serve
          .compile
          .drain
      }

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    program
      .provideLayer(Dependencies.extServicesLive)
      .exitCode
}
