package model

import play.api.libs.json.{Json, Writes}

case class BookOptionsProblem(minimum: Int,
                              maximum: Int,
                              sizes: Seq[Int],
                              format: String,
                              pageCount: Boolean)

object BookOptionsProblem {
  implicit val problemWrites: Writes[BookOptionsProblem] = (problem: BookOptionsProblem) =>
    Json.obj(
      "minimum" -> problem.minimum,
      "maximum" -> problem.maximum,
      "sizes" -> problem.sizes,
      "format" -> problem.format,
      "pageCount" -> problem.pageCount
    )
}

