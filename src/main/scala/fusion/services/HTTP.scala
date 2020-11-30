package fusion.services

import cats.effect.Sync
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object HTTP {

  def endpoints[F[_]: Sync]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root => Ok("It works")
      case GET -> Root / "stock" / IntVar(id) => Ok(s"GET -> /stock/$id")
    }

  }

}
