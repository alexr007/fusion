package fusion.services

//import doobie.util.transactor.Transactor
//import doobie.util.transactor.Transactor.Aux
//import fusion.{Configuration, DbConnection, IOTransactor}
//import zio.{Task, UIO, URIO, URLayer, ZIO, ZLayer}
//import zio.interop.catz._
//
//object DbConnection {
//
//  trait Service {
//    def xa: UIO[Aux[Task, Unit]]
//  }
//
//  /** this is PUBLIC POINT TO USE in the WHOLE APP */
//  def xa: URIO[DbConnection, Aux[Task, Unit]] = ZIO.accessM(_.get.xa)
//
//  val liveConf: URLayer[Configuration, IOTransactor] = ZLayer.fromFunctionM {
//    _.get.conf
//      .map(_.db)
//      .map(db => Transactor.fromDriverManager[Task](db.driver, db.url, db.user, db.password))
//  }
//
//}
