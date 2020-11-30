package fusion.services

import doobie.util.transactor.Transactor
import fusion.{Configuration, DbConnection, IOTransactor}
import zio.{Task, UIO, URLayer, ZLayer}
import zio.interop.catz._

object DbConnection {

  trait Service {
    def xa: UIO[IOTransactor]
  }

  /** this is PUBLIC POINT TO USE in the WHOLE APP */
//  def xa: URIO[Configuration, IOTransactor] = ZIO.accessM((x: Configuration) => x.get.conf)

  val liveConf: URLayer[Configuration, IOTransactor] = ZLayer.fromFunctionM {
    _.get.conf
      .map(_.db)
      .map(db => Transactor.fromDriverManager[Task](db.driver, db.url, db.user, db.password))
  }


}
