package fusion

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import Dependencies.{ExtServices, StockDAO}
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.slf4j.LoggerFactory._
import zio.interop.catz._
import zio.{IO, RIO, URIO, ZIO}

/**
 * HTTP routes definition
 */
object HTTPService extends Http4sDsl[STask] with Loggable {
  val PATH = Root / "stock"

  val stockDao: URIO[StockDAO, StockDAO.Service] = ZIO.access[StockDAO](_.get)

  val routes: HttpRoutes[STask] = HttpRoutes.of[STask] {

    case GET -> PATH / IntVar(stockId) =>
      val r: ZIO[ExtServices, StockError, Stock] = for {
        dao      <- stockDao
        stock    <- dao.current(stockId)
        validated = Stock.validate(stock)
        result   <- IO.fromEither(validated)
      } yield result
      represent(r)

    case PUT -> PATH / IntVar(stockId) / IntVar(updateValue) =>
      val r: ZIO[StockDAO, StockError, Stock] = for {
        dao     <- stockDao
        updated <- dao.update(stockId, updateValue)
      } yield updated
      represent(r)

  }

  val msg_isEmpty = Json.obj("Error" -> Json.fromString("Stock is empty"))
  val msg_notFound = Json.obj("Error" -> Json.fromString("Stock not found"))
  val msg_500 = (s: String) => Json.obj("BOOM" -> Json.fromString(s))

  def represent(response: ZIO[ExtServices, StockError, Stock]): STask[Response[STask]] =
    response.foldM({
      case StockIsEmpty           => Conflict(msg_isEmpty)
      case StockNotFound          => NotFound(msg_notFound)
      case StockDBAccessError(ex) =>
        val message = ex.getMessage
        IO(log.error(message)) *> InternalServerError(msg_500(message))
    },
      stock => Ok(stock.asJson)
    )

}


