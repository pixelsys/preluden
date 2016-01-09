object PalindromeWS {

	val input = "swqebabexcfcarracmnkiu"      //> input  : String = swqebabexcfcarracmnkiu
	val length = input.length                 //> length  : Int = 22
	val mid = input.length / 2                //> mid  : Int = 11
  val left = input.substring(0, mid)              //> left  : String = swqebabexcf
  val right = input.substring(mid, input.length - 1)
                                                  //> right  : String = carracmnki
	
	def findPalindrome(input: String, position: Int, maxLength: Int) : Option[(Int, Int)] = {
	  if(input.length - 1 >= position || 0 <= position || maxLength < 1)
	  	throw new IllegalArgumentException
		if(input.length <= 1)
			return None
		if(input.length == 2) {
			if(input(0) == input(1)) return Some((0, 2)) else None
		}
			
		 for(i <- 1 to maxLength) {
		 		val left = input.charAt(position - i)
		 		val right = input.charAt(position + i)
		 }
		 ???
	}                                         //> findPalindrome: (input: String, position: Int, maxLength: Int)Option[(Int, I
                                                  //| nt)]
	
}