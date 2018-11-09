import java.io.File
import java.util.Calendar

import com.github.halfmatthalfcat.stringmetric.similarity.{JaroWinklerMetric, LevenshteinMetric, OverlapMetric, RatcliffObershelpMetric}
import com.github.tototoshi.csv.CSVReader

case class Record(val partNo: Int, val key: String, val description: String, val poValue:  Double, val poValNoDisc: Double)

object Record {

  def apply(partNo: String, description: String, poValue: String, poValNoDisc: String) = {
    val descLower = description.toLowerCase
    val key = descLower.split(",")(0)
    new Record(partNo.toInt, key, description, poValue.toDouble, poValNoDisc.toDouble)
  }

  def distance(record: Record, expKey: String, expression: String) : Option[Int] = {
    /*
    val penalty = if(record.key == expKey) {
      0
    } else { 10000 }
    val score = LevenshteinMetric.compare(record.description, expression)
    val scoreNormalised  = score match {
      case Some(x: Int) => penalty + x
      case None => Integer.MAX_VALUE
    }
    scoreNormalised*/
    if(record.key == expKey) {
      LevenshteinMetric.compare(record.description, expression)
    } else {
      None
    }
  }

}

object FuzzyMatching extends App {

  def getModePoVal(records: Map[Int, Record], searchDescription: String) : Double = {
    val searchKey = searchDescription.split(",")(0)
    //Console.println("searchkey: " + searchKey)
    val resss = records.flatMap{case(k, v) => {
      val distance = Record.distance(v, searchKey, searchDescription)
      distance match {
        case Some(x: Int) => Some(k, v.description, x, v.poValue, v.poValNoDisc)
        case _ => None
      }
    }}
    if(resss.size == 0) {
      -1.0
    } else {
      val poVals = resss.map(x => x._4)
      poVals.groupBy(i => i).mapValues(_.size).maxBy(_._2)._1
    }
  }


  val dir = "/Users/pixel/Downloads/OneDrive-2018-10-29/"
  val inventoryReportFile = "Inventory Report_30July2018.csv"
  val activePartsFile = "PDL Active Parts Positive QTY_On_Hand No Historical PO attached AUG 6.csv"

  val reader = CSVReader.open(new File(dir + inventoryReportFile))

  val inventoryMap = reader.toStream.map(x => {
    val record = Record(x(0), x(1), x(17), x(18))
    record.partNo -> record
    //x(0) -> (x(1).toLowerCase, x(2).toLowerCase(), x(17).toDouble, x(18).toDouble)
  }).toMap

  Console.println("Inventory map loaded")

  val invMapFiltered = inventoryMap.filter(x => x._2.poValue != 0.0)

  Console.println("Inventory map filtered")

  //val searchExpression = "SEAL, BEARING HOUSING".toLowerCase
  //val searchKey = "SEAL".toLowerCase
  //val searchDesc = "".toLowerCase

  //inventoryMap.take(10).foreach(Console.println(_))

  /*

  val resss = invMapFiltered.flatMap{case(k, v) => {
    val distance = Record.distance(v, searchKey, searchExpression)
    distance match {
      case Some(x: Int) => Some(k, v.description, x, v.poValue, v.poValNoDisc)
      case _ => None
    }
  }}


  //Console.println(resss.size)

  //resss.take(10).foreach(Console.println(_))

  //val rressses2 = resss.toSeq.sortBy(_._3)  //(Ordering[Int].reverse)

  //rressses2.take(100).foreach(x => Console.println(x))
  val poVals = resss.map(x => x._4)
  val mode = if(poVals.size > 0) {
    poVals.groupBy(i => i).mapValues(_.size).maxBy(_._2)._1
  } else {
    -1
  }*/

  val pdsReader =  CSVReader.open(new File(dir + activePartsFile))

  var total = 0
  var found = 0;

  pdsReader.foreach(line => {
    val expression = line(2)
    val res = FuzzyMatching.getModePoVal(invMapFiltered, expression.toLowerCase)
    total += 1
    if(res != -1.0) {
      found += 1
    }
    if(total % 100 == 0) {
      Console.println(Calendar.getInstance().getTime + " processed: " + total)
    }
    //Console.println("Searching for : " + expression)
    //Console.println(FuzzyMatching.getModePoVal(invMapFiltered, expression.toLowerCase))
  })

  //it.next // header

  // S.No., PART_NO, DESCRIPTION, NOTE_TEXT, PART_STATUS, ASSET_CLASS, ACCOUNTING_GROUP, PRIME_COMMODITY, SECOND_COMMODITY, UNIT_MEAS, MANUFACTURER_1, MANUFACTURER_2, MANUFACTURER_3, MANUFACTURER_4, MANUFACTURER_5, MANUFACTURER_6, PO_VALUE, PO_VALUE_NO_DISCOUNT, LAST_PO, DISCOUNT, PO_PRICE_NOK, INV_UNIT_COST_USD, GLOBAL_QTY_ON_HAND


  reader.close()
  pdsReader.close()

  Console.println("Total: " + total)
  Console.println("Found: " + found)

}

