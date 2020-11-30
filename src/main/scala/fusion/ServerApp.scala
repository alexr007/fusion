package fusion

import fusion.services.{Configuration, HTTP}
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli._
import zio.clock.Clock
import zio.console.Console
import zio.interop.catz._
import zio._

object ServerApp extends CatsApp {

  /**
   * Runtime will execute IO unsafe calls
   * (i.e. all the side effects)
   * and manage threading
   *
   * http://localhost:8080
   */
  val program: ZIO[AllExtServices, Throwable, Unit] =
    ZIO.runtime
      .flatMap { implicit runtime: zio.Runtime[AllExtServices] =>
        BlazeServerBuilder[STask]
          .bindHttp(8080, "0.0.0.0")
          .withHttpApp(HTTP.endpoints[STask].orNotFound)
          .serve
          .compile
          .drain
      }

  val prefix: ZIO[Console with Configuration, Nothing, Unit] = for {
    c <- Configuration.conf
    _ <- zio.console.putStrLn(c.toString)
  } yield ()

  val appReqDeps: ZIO[AllExtServices with Console with Configuration, Throwable, Unit] =
    prefix *> program

  val deps: ULayer[Configuration with Console with Clock] =
    Configuration.stub ++ (Console.live ++ Clock.live)

  val app: Task[Unit] = appReqDeps
    .provideLayer(deps)

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = app.exitCode
}
