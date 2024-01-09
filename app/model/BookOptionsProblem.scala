package model

import play.api.libs.json.{Json, Writes}

case class BookOptionProblem(minimum: Int,
                   maximum: Int,
                   sizes: Seq[Int],
                   format: String,
                   pageCount: Boolean)

object BookOptionProblem {
  implicit val problemWrites: Writes[BookOptionProblem] = (problem: BookOptionProblem) =>
    Json.obj(
      "minimum" -> problem.minimum,
      "maximum" -> problem.maximum,
      "sizes" -> problem.sizes,
      "format" -> problem.format,
      "pageCount" -> problem.pageCount
    )
}

