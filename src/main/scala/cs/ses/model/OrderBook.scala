package cs.ses.model

import scala.collection.immutable.TreeMap


/**
 * @author dpetyus
 */

object Direction extends Enumeration {
  type Direction = Value
  val Buy, Sell = Value  
}

case class Entry(quantity: Int, price: BigDecimal)
  
case class Interest(timestamp: Long, user: String)

object OrderBook {
  
  import Direction._
  
  def apply(direction: Direction) = direction match {
    case Buy => BuyOrderBook()
    case Sell => SellOrderBook()
    case _ => throw new IllegalArgumentException()
  }
  
}

abstract class OrderBook (entries : TreeMap[Entry, List[Interest]]) {
  
  
  
  
}

object BuyOrderBook {
  
  implicit val ordering : Ordering[Entry] = {
    ???
  }
  
  def apply() = {
      new BuyOrderBook(new TreeMap[Entry, List[Interest]])
  }
  
}

class BuyOrderBook private (entries : TreeMap[Entry, List[Interest]]) extends OrderBook (entries) {

}

object SellOrderBook {
  
  implicit val ordering : Ordering[Entry] = {
    ???
  }
  
  def apply() = {
      new SellOrderBook(new TreeMap[Entry, List[Interest]])
  }
  
}


class SellOrderBook private (entries : TreeMap[Entry, List[Interest]]) extends OrderBook (entries) {

}
