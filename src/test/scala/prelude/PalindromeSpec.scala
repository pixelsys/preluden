package prelude

import org.scalatest.FlatSpec

class PalindromeSpec extends FlatSpec {
  
  "Palindrome" should "find longest unique paldinromes in a string" in {
    val res = Palindrome.find("swqebabexcfcarracmnkiju", 2)
    assert(res.map{_._1}.toSet == Set("ebabe", "carrac"))
  }
  
  "Palindrome" should "return start index and length, in descending order of length" in {
    val res = Palindrome.find("sqrrqabccbatudefggfedvwhijkllkjihxymnnmzpop", 3)
    assert(res(0) === List("hijkllkjih", 23, 10))
    assert(res(1) === List("defggfed", 13, 8))
    assert(res(2) === List("abccba", 5, 6))
  }
  
}