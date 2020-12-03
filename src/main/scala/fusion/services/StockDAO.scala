package fusion.services

import doobie.implicits._
import fusion.domain.{Stock, StockDBAccessError, StockError, StockNotFound}
import fusion.{DbConnection, StockDAO}
import zio.clock.Clock
import zio.interop.catz._
import zio.{IO, URLayer, ZIO, ZLayer}

object StockDAO {

  trait Service {
    def current(stockId: Int): ZIO[Clock, StockError, Stock]
    def update(stockId: Int, updateValue: Int): ZIO[Clock, StockError, Stock]
  }

  def current(stockId: Int): ZIO[StockDAO with Clock, StockError, Stock] =
    ZIO.accessM(_.get.current(stockId))
  def update(stockId: Int, updateValue: Int): ZIO[StockDAO with Clock, StockError, Stock] =
    ZIO.accessM(_.get.update(stockId, updateValue))

  val live: URLayer[DbConnection, StockDAO] = ZLayer.fromFunction { conn =>

    val selectQ = (stockId: Int) =>
      sql"""SELECT * FROM stock where id=$stockId""".query[Stock]
    val updateQ = (stockId: Int, value: Int) =>
      sql""" UPDATE stock SET value = value + $value where id=$stockId""".update

    new Service {
      override def current(stockId: Int): ZIO[Clock, StockError, Stock] =
        conn.get.xa
          .flatMap { xa =>
            selectQ(stockId).option
              .transact(xa)
              .mapError(ex => StockDBAccessError(ex))
              .flatMap {
                case Some(stock) => IO.succeed(stock)
                case None        => IO.fail(StockNotFound)
              }
          }

      override def update(stockId: Int, updateValue: Int): ZIO[Clock, StockError, Stock] =
        conn.get.xa
          .flatMap { xa =>
            val dbQ = for {
              _       <- updateQ(stockId, updateValue).run
              updated <- selectQ(stockId).unique
            } yield updated
            dbQ
              .transact(xa)
              .mapError(ex => StockDBAccessError(ex))
          }
    }
  }

}
