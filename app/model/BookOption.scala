package model

import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.{JsPath, Reads}
import utils.Utils.collectElements
case class BookOption(signatures: Seq[Signature]){
  val pages: Int = signatures.map(_.pages).sum
  lazy val toText: String =
    s"${signatures.sortBy(_.size).map(_.toText).mkString(",")};$pages"
}

object BookOption {
  implicit val bookOptionReads: Reads[BookOption] =
    (JsPath \ "signatures").read[Seq[Signature]].map(BookOption.apply)
  def fromText(encoded:String):Option[BookOption]= {
     encoded.split(";") match {
       case Array(signaturesStr,_) => collectElements(signaturesStr.split(",").map(Signature.fromText)).map(BookOption.apply)
       case _ => None
     }
  }
}
