package fusion

import doobie.util.transactor.Transactor
import fusion.dao.StockDAOLive
import fusion.domain.{Stock, StockError}
import zio._
import zio.interop.catz._

object Dependencies {

  object StockDAO {

    trait Service {
      def current(stockId: Int): IO[StockError, Stock]
      def update(stockId: Int, updateValue: Int): IO[StockError, Stock]
    }

    val live: ULayer[StockDAO] =
      ZLayer.succeed {
        val xa = Transactor.fromDriverManager[Task](
          "org.h2.Driver",
          "jdbc:h2:file:./localdb;INIT=RUNSCRIPT FROM 'src/main/resources/sql/create.sql'"
          , "sa", ""
        )
        new StockDAOLive(xa)
      }
  }

}
