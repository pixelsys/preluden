import scala.collection.mutable.ListBuffer

object PalindromeWS2 {

  def isPalindrome(str: String) : Boolean = str.reverse == str
                                                  //> isPalindrome: (str: String)Boolean

	def slidingWithInfo(s: String, size: Int) : Seq[(String, Int, Int)] = {
		if(s.length < size)
			return List.empty
		(0 to s.length - size).map{i => (s.substring(i, size + i), i, size + i)}
	}                                         //> slidingWithInfo: (s: String, size: Int)Seq[(String, Int, Int)]
  
	def palindromes(input: String) : Seq[(String, Int, Int)] = {
	    val x = for {
	      substrSize <- input.length to 1 by -1
	      (substr, start, end) <- slidingWithInfo(input, substrSize)
	    } yield (substr, isPalindrome(substr), start, end)
	    val y = x.filter(_._2 == true).map{x => (x._1, x._3, x._4 - x._3)}
	    val coverage = new Array[Boolean](input.length)
	    (0 to input.length - 1).foreach{i => coverage(i) = false}
	    val z = y.flatMap{p => {
	    	if(coverage.slice(p._2, p._2 + p._3 - 1).exists(_ == true)) {
	    		None
	    	} else {
	    		(p._2 to p._2 + p._3 - 1).foreach{i => coverage(i) = true}
	    		Some(p)
	    	}
	    }}
	    z
	  }                                       //> palindromes: (input: String)Seq[(String, Int, Int)]

	val input = "sqrrqabccbatudefggfedvwhijkllkjihxymnnmzpop"
                                                  //> input  : String = sqrrqabccbatudefggfedvwhijkllkjihxymnnmzpop

	palindromes(input).take(3)                //> res0: Seq[(String, Int, Int)] = Vector((hijkllkjih,23,10), (defggfed,13,8),
                                                  //|  (abccba,5,6))

}