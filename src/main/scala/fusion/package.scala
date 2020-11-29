import doobie.util.transactor.Transactor.Aux
import fusion.Dependencies.ExtServices
import org.http4s.Response
import zio.{RIO, Task, ZIO}

package object fusion {

  type IOTransactor = Aux[Task, Unit]
  type SIO[E, A]    = ZIO[ExtServices, E, A]
  type STask[A]     = RIO[ExtServices, A]
  type SResponse    = STask[Response[STask]]

}
