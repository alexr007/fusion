package fusion

import fusion.Programs._
import fusion.services.Configuration
import zio._
import zio.clock.Clock
import zio.console.Console
import zio.interop.catz._

object Server2App extends CatsApp {

  /**
   * or provide deps to each part separately
   */
  val programNoDeps: ZIO[Any, Throwable, Unit] = program2.provideLayer(Clock.live)
  val prefixNoDeps: ZIO[Any, Nothing, Unit] = program1.provideLayer(Configuration.stub ++ Console.live)

  /**
   * combine them without requiring any dependencies
   */
  val app: ZIO[Any, Throwable, Unit] = prefixNoDeps *> programNoDeps

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = app.exitCode
}
