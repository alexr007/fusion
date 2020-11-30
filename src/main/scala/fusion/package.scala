import doobie.util.transactor.Transactor.Aux
import fusion.services.{Configuration, DbConnection, StockDAO}
import org.http4s.Response
import zio.clock.Clock
import zio.{Has, RIO, Task, ZIO}

package object fusion {

  type SIO[E, A]     = ZIO[ExtServices, E, A]
  type STask[A]      = RIO[ExtServices, A]
  type SResponse     = STask[Response[STask]]

  type IOTransactor  = Has[Aux[Task, Unit]]
  type StockDAO      = Has[StockDAO.Service]
  type ExtServices   = StockDAO with Clock
  type Configuration = Has[Configuration.Service]
  type DbConnection  = Has[DbConnection.Service]

}
