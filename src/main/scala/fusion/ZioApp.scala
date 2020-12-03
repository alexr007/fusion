package fusion

import fusion.domain.StockError
import fusion.services.{Configuration, DbConnection, StockDAO}
import zio._
import zio.clock.Clock
import zio.console.{Console, putStr}

object ZioApp extends App {

  val program: ZIO[Console with StockDAO with Clock, StockError, Unit] = for {
    a <- StockDAO.current(1)
    _ <- putStr(a.toString)
  } yield ()

  val dbLayer:   ZLayer[Any, Nothing, DbConnection]                     = Configuration.live >>> DbConnection.live
  val daoLayer:  ZLayer[Any, Nothing, StockDAO]                         = dbLayer >>> StockDAO.live
  val allLayers: ZLayer[Any, Nothing, Clock with Console with StockDAO] = Clock.live ++ (Console.live ++ daoLayer)

  val app: ZIO[Any, Any, Unit] = program
    .provideLayer(allLayers)

  def run(args: List[String]): URIO[ZEnv, ExitCode] = app.exitCode

}
