package fusion.services

import fusion.Configuration
import fusion.domain.{AppConfig, AppDetails, DbConnDetails}
import pureconfig._
import pureconfig.generic.auto._
import zio._

object Configuration {

  /** interface isn't aware of any kind of resource */
  trait Service {
    def conf: UIO[AppConfig]
  }

  /** implementation which will be able to access `Configuration` in run time */
  def conf: URIO[Configuration, AppConfig] = ZIO.accessM(_.get.conf)

  /** real implementation, reads configuration from file */
  val live: ULayer[Configuration] = ZLayer.succeed(new Service {
    private lazy val c = ConfigSource.default.loadOrThrow[AppConfig]
    override def conf: UIO[AppConfig] = UIO(c)
  })

  /** stub implementation, has hardcoded configuration */
  val stub: ULayer[Configuration] = ZLayer.succeed(new Service {
    private val c = AppConfig(
      AppDetails("test app"),
      DbConnDetails(
        "org.h2.Driver",
        "jdbc:h2:file:./localdb;INIT=RUNSCRIPT FROM 'src/main/resources/sql/create.sql'",
        "sa",""
      )
    )
    override def conf: UIO[AppConfig] = UIO(c)
  })

}
