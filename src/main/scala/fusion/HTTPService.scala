package fusion

import io.circe._
import io.circe.syntax._
import io.circe.generic.auto._
import fusion.domain._
import fusion.services.StockDAO
import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpRoutes, Response}
import org.http4s.circe._
import zio.clock.Clock
import zio.{IO, URIO, ZIO}
import zio.interop.catz._

/** HTTP routes definition */
object HTTPService extends Http4sDsl[STask] with Loggable {
//  val PATH = Root / "stock"
//
//  val stockDao: URIO[StockDAO, StockDAO.Service] = ZIO.access[StockDAO]((x: StockDAO) => x.get)
//
//  val routes: HttpRoutes[STask] = HttpRoutes.of[STask] {
//
//    case GET -> PATH / IntVar(stockId) =>
//      val r = for {
//        dao <- stockDao
//        stock <- dao.current(stockId)
//        validated = Stock.validate(stock)
//        result <- IO.fromEither(validated)
//      } yield result
//      represent(r)
//
//    case PUT -> PATH / IntVar(stockId) / IntVar(updateValue) =>
//      val r = for {
//        dao <- stockDao
//        updated <- dao.update(stockId, updateValue)
//      } yield updated
//      represent(r)
//
//  }
//
//  def represent(response: ZIO[Clock with StockDAO, StockError, Stock]): ZIO[Clock with StockDAO, Throwable, Response[STask]] =
//    response.foldM({
//      case StockIsEmpty => Conflict(msg.isEmpty)
//      case StockNotFound => NotFound(msg.notFound)
//      case StockDBAccessError(ex) =>
//        val message = ex.getMessage
//        IO(log.error(message)) *> InternalServerError(msg.e500(message))
//    },
//      stock => Ok(stock.asJson)
//    )

}
