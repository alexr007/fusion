package fusion.domain

import io.circe.Json

object msg {
  val isEmpty: Json = Json.obj("Error" -> Json.fromString("Stock is empty"))
  val notFound: Json = Json.obj("Error" -> Json.fromString("Stock not found"))
  val e500: String => Json = (s: String) => Json.obj("BOOM" -> Json.fromString(s))
}
