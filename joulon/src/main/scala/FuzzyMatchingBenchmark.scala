import com.github.halfmatthalfcat.stringmetric.similarity.{LevenshteinMetric, OverlapMetric}

object FuzzyMatchingBenchmark extends App {

  val searchTerm = "HEATER, WIRE FIBERGLASS, 99IN/115V/320DEG"

  val matchEs = List("fiberglass", "heater", "wire", "99in", "heater, fiberglass", "heater, wire", "apple", "aiculedssul", "requiescat in pacem", "", " ", "heat, wire fiberlass")


  matchEs.foreach{ m => {
    val score = LevenshteinMetric.compare(searchTerm.toLowerCase, m.toLowerCase)
    val scoreNormalised  = score match {
      case Some(x: Int) => 1000 + x
      case None => Integer.MAX_VALUE
    }
    Console.println(searchTerm + " <=> " + m  + " = " + scoreNormalised)
  }}

  //val score = LevenshteinMetric.compare("HEATER, WIRE FIBERGLASS, 99IN/115V/320DEG".toLowerCase, "fiberglass".toLowerCase())
  //Console.println(score)



  Console.println("overlap: " + OverlapMetric(8).compare("EXTERNAL", "EXTERNAL CIRCLIPS, THULE SHAKER VSM"))

}
