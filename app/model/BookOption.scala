package model

import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.{JsPath, Reads}
case class BookOption(signatures: Seq[Signature],pages:Int){
  lazy val toText: String =
    s"${signatures.map(_.toText).mkString(",")};$pages"
}

object BookOption {
  implicit val bookOptionReads: Reads[BookOption] =
    ((JsPath \ "signatures").read[Seq[Signature]] and (JsPath \ "pages").read[Int])(BookOption.apply _)

  def fromText(encoded:String):Option[Signature]= {
    val parts = encoded.split(";")
    if(parts.length == 0 || parts.length > 2){
      None
    }else{
      None
      //swap(parts(0).split(",").map(Signature.fromText))
    }
  }
}
