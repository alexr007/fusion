import doobie.util.transactor.Transactor.Aux
import fusion.services.{DbConnection, StockDAO}

import fusion.services.Configuration
import zio.clock.Clock
import zio.{Has, RIO}

package object fusion {

  /**
   * we need ONE-HOLE type,
   * because Http4s and Doobie require it
   * to wrap their results
   */
  type STask[A]      = RIO[Clock, A]
  /** doobie alias */
  type IOTransactor  = Aux[STask, Unit]
  /** ZIO aliases */
  type Configuration = Has[Configuration.Service]
  type DbConnection  = Has[DbConnection.Service]
  type StockDAO      = Has[StockDAO.Service]

}
