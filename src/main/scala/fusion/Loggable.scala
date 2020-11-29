package fusion

import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

trait Loggable {
  val log: Logger = getLogger(this.getClass)
}
