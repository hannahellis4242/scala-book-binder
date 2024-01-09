package model

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class SignatureTest extends AnyFunSuite with Matchers{
  test("can create a Signature from two integers"){
    val created = Signature(5,4)
    created.size should be(5)
    created.count should be(4)
  }
  test("can convert a Signature to text"){
    val signature = Signature(3,9)
    signature.toText should be("3:9")
  }
  test("can create a Signature from a valid string"){
    val signature = Signature.fromText("4:2")
    signature should not be(None)
    signature.get.count should be(2)
    signature.get.size should be(4)
  }
  test("cannot create a Signature from an empty string"){
    val signature = Signature.fromText("")
    signature should be(None)
  }
  test("cannot create a Signature from a string without numbers"){
    val signature = Signature.fromText("a:b")
    signature should be(None)
  }

  test("cannot create a Signature with an invalid string"){
    val signature = Signature.fromText("4:5:6")
    signature should be(None)
  }

  test("cannot create a Signature from string with non-integer values"){
    val signature = Signature.fromText( "4.5:6.3" )
    signature should be(None)
  }
}
