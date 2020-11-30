package fusion.services

import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux
import fusion.domain.DbConnDetails
import fusion.{Configuration, DbConnection, IOTransactor}
import zio.{Task, UIO, URIO, ZIO, ZLayer}
import zio.interop.catz._

object DbConnection {

  trait Service {
    def transactor: UIO[IOTransactor]
  }

  /** this is PUBLIC POINT TO USE in the WHOLE APP */
//  def transactor: URIO[Configuration, IOTransactor] = ZIO.accessM((x: Configuration) => x.get.conf)

  val live: ZLayer[Configuration, Nothing, DbConnection] = ZLayer.fromFunction { conf => new Service {
    override def transactor: UIO[IOTransactor] = {
      for {
        ac <- conf.get.conf
        db = ac.db
        t = Transactor.fromDriverManager[Task](db.driver, db.url, db.user, db.password)
      } yield t
    }
  }}

}
