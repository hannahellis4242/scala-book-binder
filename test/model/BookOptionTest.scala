package model

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class BookOptionTest extends AnyFunSuite with Matchers{
  test("can create a BookOption"){
    val signatures = Vector(Signature(4,1),Signature(3,1))
    val option = BookOption(signatures)
    option.signatures should have length(2)
    option.pages should be(28)
  }

  test("can convert a BookOption to text"){
    val signatures = Vector(Signature(4,2),Signature(3,5))
    val option = BookOption(signatures)
    option.toText should be("3:5,4:2;92")
  }
  test("can convert a valid string into a BookOption"){
    val option = BookOption.fromText("3:2,4:1;40")
    option should not be(None)
    option.get.signatures should have length(2)
    option.get.signatures should contain(Signature(3,2))
    option.get.signatures should contain(Signature(4,1))
    option.get.pages should be(40)
  }
  test("cannot convert empty string into a BookOption"){
    val option = BookOption.fromText("")
    option should be(None)
  }
}
