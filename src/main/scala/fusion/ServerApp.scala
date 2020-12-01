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
   * http://localhost:8080/stock/457
   * http://localhost:8080/abracadabra
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

  /** combine TWO apps */
  val appReqDeps: ZIO[AllExtServices with Console with Configuration, Throwable, Unit] =
    prefix *> program

  /** combine all deps */
  val deps: ULayer[Configuration with Console with Clock] =
    Configuration.stub ++ (Console.live ++ Clock.live)

  /** provide all deps */
  val app: Task[Unit] = appReqDeps
    .provideLayer(deps)

  /**
   * or provide deps to each part separately
   */
  val programNoDeps: ZIO[Any, Throwable, Unit] = program.provideLayer(Clock.live)
  val prefixNoDeps: ZIO[Any, Nothing, Unit] = prefix.provideLayer(Configuration.stub ++ Console.live)
  /** combine them */
  val wholeApp: ZIO[Any, Throwable, Unit] = prefixNoDeps *> programNoDeps

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = app.exitCode
}
