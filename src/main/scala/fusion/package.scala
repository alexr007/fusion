import doobie.util.transactor.Transactor.Aux
import fusion.Dependencies.StockDAO
import org.http4s.Response
import zio.clock.Clock
import zio.{Has, RIO, Task, ZIO}

package object fusion {

  type IOTransactor = Aux[Task, Unit]
  type SIO[E, A]    = ZIO[ExtServices, E, A]
  type STask[A]     = RIO[ExtServices, A]
  type SResponse    = STask[Response[STask]]

  type StockDAO     = Has[StockDAO.Service]
  type ExtServices  = StockDAO with Clock

}
