package fusion

import fusion.Programs._
import fusion.services.Configuration
import zio._
import zio.clock.Clock
import zio.console.Console
import zio.interop.catz._

object Server1App extends CatsApp {

  /**
   * we can combine as many apps as we want.
   * as a result we have NEW Application which requires
   * all dependencies from all apps:
   * now: `Clock`, `Console`, `Configuration`
   */
  val appReqDeps: ZIO[Clock with Console with Configuration, Throwable, Unit] =
    program1 *> program2

  /** combine all dependencies */
  val deps: ULayer[Configuration with Console with Clock] =
    Configuration.stub ++ (Console.live ++ Clock.live)

  /** provide all dependencies to our applications */
  val app: Task[Unit] = appReqDeps
    .provideLayer(deps)

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = app.exitCode
}
