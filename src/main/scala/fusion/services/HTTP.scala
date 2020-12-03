package fusion.services

import cats.effect.Sync
import fusion.Loggable
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object HTTP extends Loggable {

//  val dao1: URIO[Has[StockDAO], StockDAO] = ZIO.service[StockDAO]
//  val dao2: URIO[StockDAO, StockDAO.Service] = ZIO.access[StockDAO]((x: StockDAO) => x.get)

  def endpoints[F[_]: Sync]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root                                        => Ok("It works")
      case GET -> Root / "stock" / IntVar(id)                 => Ok(s"GET -> /stock/$id")
      case PUT -> Root / "stock" / IntVar(id) / IntVar(value) => Ok(s"PUT -> /stock/$id/$value")
    }

  }


}
