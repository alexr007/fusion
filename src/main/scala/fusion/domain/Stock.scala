package fusion.domain

case class Stock(id:Int, value: Int)

sealed abstract class StockError(cause: Throwable) extends Exception(cause)
case object StockIsEmpty extends StockError(new Exception)
case object StockNotFound extends StockError(new Exception)
case class StockDBAccessError(cause: Throwable) extends StockError(cause)

object Stock {
  def validate(stock: Stock): Either[StockError, Stock] =
    if (stock.value > 0) Right(stock) else Left(StockIsEmpty)
}
