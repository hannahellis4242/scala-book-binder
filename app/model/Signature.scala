package model

import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.{JsPath, Reads}

case class Signature(size: Int, count: Int) {
  lazy val toText = s"$size:$count"
}

object Signature {
  implicit val signatureReads: Reads[Signature] =
    ((JsPath \ "size").read[Int] and (JsPath \ "count").read[Int])(Signature.apply _)

  def fromText(encoded: String): Option[Signature] = {
    encoded.split(":") match {
      case Array(size, count) => {
        size.
          toIntOption
          .flatMap(x =>
            count.
              toIntOption
              .map(y => Signature(x, y)))
      }
      case _ => None
    }
  }
}
