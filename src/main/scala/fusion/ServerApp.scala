package fusion

//import fusion.services.{Configuration, DbConnection, StockDAO}
import fusion.services.HTTP
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli._
import zio.clock.Clock
import zio.interop.catz._
import zio.{ExitCode, RIO, URIO, ZIO, ZLayer}

object ServerApp extends CatsApp {

  /**
   * Runtime will execute IO unsafe calls
   * (i.e. all the side effects)
   * and manage threading
   *
   * http://localhost:8080
   */
  val program =
    ZIO.runtime
      .flatMap { implicit runtime: zio.Runtime[AllExtServices] =>
        BlazeServerBuilder[STask]
          .bindHttp(8080, "0.0.0.0")
          .withHttpApp(HTTP.endpoints[STask].orNotFound)
          .serve
          .compile
          .drain
      }

  /**
   * - dependency StockDAO from PGConnection is hidden in constructor: TODO: FIX IT
   */
//  val deps =
//    StockDAO.liveHard ++
//    Configuration.stub >>> StockDAO.liveConf ++
//    Configuration.live ++ DbConnection.liveConf ++ StockDAO.liveConf ++ Clock.live

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    program
//      .provideLayer(deps)
      .exitCode
}
