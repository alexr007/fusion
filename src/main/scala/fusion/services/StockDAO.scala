package fusion.services

import doobie.util.transactor.Transactor
import fusion.{Configuration, IOTransactor, StockDAO}
import fusion.dao.StockDAOLive
import fusion.domain.{Stock, StockError}
import zio.{IO, Task, ULayer, URLayer, ZLayer}
import zio.interop.catz._

object StockDAO {

  trait Service {
    def current(stockId: Int): IO[StockError, Stock]
    def update(stockId: Int, updateValue: Int): IO[StockError, Stock]
  }

  val liveHard: ULayer[StockDAO] =
    ZLayer.succeed {
      val xa: IOTransactor = Transactor.fromDriverManager[Task](
        "org.h2.Driver",
        "jdbc:h2:file:./localdb;INIT=RUNSCRIPT FROM 'src/main/resources/sql/create.sql'"
        , "sa", ""
      )
      new StockDAOLive(xa)
    }

  val liveConf: URLayer[Configuration, StockDAO] = ZLayer.fromFunctionM {
    _.get.conf
      .map(_.db)
      .map(db => Transactor.fromDriverManager[Task](db.driver, db.url, db.user, db.password))
      .map(xa => new StockDAOLive(xa))
  }
}
