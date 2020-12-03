import doobie.util.transactor.Transactor.Aux
import fusion.services.{DbConnection, StockDAO}
import zio.{IO, UIO}

//import scala.language.higherKinds
import fusion.services.Configuration
import org.http4s.Response
import zio.clock.Clock
import zio.{Has, RIO, Task, ZIO}

package object fusion {

  /**
   * we need ONE-HOLE type,
   * because Http4s and Doobie require it
   * to wrap their results
   */
  type STask[A]       = RIO[AllExtServices, A]
  type DTask[A]       = UIO[A]
  /** temporary, at investigation stage */
  type AllExtServices = Clock

  type Configuration = Has[Configuration.Service]
  type DbConnection  = Has[DbConnection.Service]
  type StockDAO      = Has[StockDAO.Service]
  /** doobie alias */
  type IOTransactor  = Aux[STask, Unit]


//  type ExtServices     = StockDAO with Clock
//  type SIO[E, A]       = ZIO[ExtServices, E, A]
//  type SResponse[A[_]] = A[Response[A]]

}
