import doobie.util.transactor.Transactor.Aux

//import scala.language.higherKinds
//import fusion.services.{Configuration, DbConnection, StockDAO}
import org.http4s.Response
import zio.clock.Clock
import zio.{Has, RIO, Task, ZIO}

package object fusion {

  /** we need ONE-HOLE type, because of Http4s */
  type STask[A]       = RIO[AllExtServices, A]
  /** temporary, we define it as any */
  type AllExtServices = Clock


//  type ExtServices   = StockDAO with Clock
//  type SIO[E, A]     = ZIO[ExtServices, E, A]
//  type SResponse[A[_]]     = A[Response[A]]
//
//  type IOTransactor  = Has[Aux[Task, Unit]]
//  type StockDAO      = Has[StockDAO.Service]
//  type Configuration = Has[Configuration.Service]
//  type DbConnection  = Has[DbConnection.Service]

}
