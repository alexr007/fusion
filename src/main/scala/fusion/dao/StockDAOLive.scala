package fusion.dao


import doobie.implicits._
import fusion.Dependencies.StockDAO
import fusion.IOTransactor
import fusion.domain.{Stock, StockDBAccessError, StockError, StockNotFound}
import org.http4s._
import zio.IO
import zio.interop.catz._

class StockDAOLive(xa: IOTransactor) extends StockDAO.Service {

  val selectQ = (stockId: Int) =>
    sql"""SELECT * FROM stock where id=$stockId""".query[Stock]
  val updateQ = (stockId: Int, value: Int) =>
    sql""" UPDATE stock SET value = value + $value where id=$stockId""".update

  override def current(stockId: Int): IO[StockError, Stock] =
    selectQ(stockId).option
      .transact(xa)
      .mapError(ex => StockDBAccessError(ex))
      .flatMap {
        case Some(stock) => IO.succeed(stock)
        case None        => IO.fail(StockNotFound)
      }

  override  def update(stockId: Int, newValue: Int): IO[StockError, Stock] = {
    val dbQ = for {
      _       <- updateQ(stockId, newValue).run
      updated <- selectQ(stockId).unique
    } yield updated

    dbQ
      .transact(xa)
      .mapError(ex => StockDBAccessError(ex))
  }

}
