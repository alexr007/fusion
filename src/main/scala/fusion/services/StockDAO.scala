package fusion.services

import doobie.util.transactor.Transactor
import fusion.{AllExtServices, Configuration, DbConnection, IOTransactor, STask, StockDAO}
import zio.ZIO
import fusion.domain.{Stock, StockDBAccessError, StockError, StockNotFound}
import zio.{IO, Task, ULayer, URLayer, ZLayer}
import zio.interop.catz._
import doobie.implicits._
import fusion.dao.StockDAOLive

object StockDAO {

  trait Service {
    def current(stockId: Int): IO[StockError, Stock]
    def update(stockId: Int, updateValue: Int): IO[StockError, Stock]
  }

  def current(stockId: Int): ZIO[StockDAO, StockError, Stock] =
    ZIO.accessM(_.get.current(stockId))
  def update(stockId: Int, updateValue: Int): ZIO[StockDAO, StockError, Stock] =
    ZIO.accessM(_.get.update(stockId, updateValue))

  val live: URLayer[DbConnection, StockDAO] = ZLayer.fromFunction { conn =>

    val selectQ = (stockId: Int) =>
      sql"""SELECT * FROM stock where id=$stockId""".query[Stock]
    val updateQ = (stockId: Int, value: Int) =>
      sql""" UPDATE stock SET value = value + $value where id=$stockId""".update

    new Service {
      override def current(stockId: Int): IO[StockError, Stock] = {
        conn.get.xa.flatMap { xa =>
          val r: ZIO[AllExtServices, StockError, Stock] =
            selectQ(stockId).option
              .transact(xa)
              .mapError(ex => StockDBAccessError(ex))
              .flatMap {
                case Some(stock) => IO.succeed(stock)
                case None        => IO.fail(StockNotFound)
              }
              r
//          ???
        }
      }

    override def update(stockId: Int, updateValue: Int): IO[StockError, Stock] = ???
    }
  }



//  val liveHard: ULayer[StockDAO] =
//    ZLayer.succeed {
//      val xa = Transactor.fromDriverManager[Task](
//        "org.h2.Driver",
//        "jdbc:h2:file:./localdb;INIT=RUNSCRIPT FROM 'src/main/resources/sql/create.sql'"
//        , "sa", ""
//      )
//      new StockDAOLive(xa)
//    }

//  val liveConf: URLayer[DbConnection, StockDAO] = ZLayer.fromFunctionM {
//    _.get
//      .xa
//      .map(xa => new StockDAOLive(xa))
//  }
}
