package fusion.services

import doobie.util.transactor.Transactor
import fusion.{Configuration, DbConnection, IOTransactor}
import zio._
import zio.interop.catz._

object DbConnection {

  /**
   * plain Service interface (wrapped in ZIO)
   */
  trait Service {
    def xa: UIO[IOTransactor]
  }

  /**
   * we just make it dependent on a kind of stuff
   * which will be given later
   * this is PUBLIC POINT TO USE in the WHOLE APP
   */
  def xa: URIO[DbConnection, IOTransactor] = ZIO.accessM(_.get.xa)

  /**
   * it requires Configuration
   * it creates live implementation
   */
  val live: URLayer[Configuration, DbConnection] = ZLayer.fromFunction { configuration: Configuration =>
    new Service {
      override def xa: UIO[IOTransactor] =
        configuration.get.conf
          .map(_.db)
          .map(db => Transactor.fromDriverManager(db.driver, db.url, db.user, db.password))
    }
  }

}
