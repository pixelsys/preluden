package cs.ses.model

import scala.collection.immutable.TreeMap
import scala.collection.immutable.TreeSet


/**
 * @author dpetyus
 */

object Direction extends Enumeration {
  type Direction = Value
  val Buy, Sell = Value  
}  

case class Order(quantity: Int, price: BigDecimal, timestamp: Long, user: String)

object OrderBook {
  
  import Direction._
  
  def apply(direction: Direction) = direction match {
    case Buy => BuyOrderBook()
    case Sell => SellOrderBook()
    case _ => throw new IllegalArgumentException()
  }
  
  
}

abstract class OrderBook (val entries : TreeSet[Order]) {
  
  def matchAndExtract(input: Order) : (Option[Order], OrderBook) = {
    val matches = entries.filter{ e => isMatching(input, e) }
    if(0 == matches.size)  
      (None, create(entries))
    else 
      (Some(matches.head), create(entries - matches.head))              
  }  
  
  def add(e: Order) = create(entries + e)
  
  def isMatching(a: Order, b: Order) : Boolean 
  
  protected def create(entries:TreeSet[Order]) : OrderBook
  
}

object BuyOrderBook {
  
  implicit val ordering : Ordering[Order] = new Ordering[Order] {
    def compare(a: Order, b: Order) = {
      if(a.price == b.price)
        if(a.quantity == b.quantity)
          a.timestamp compare b.timestamp
        else
          a.quantity compare b.quantity
      else
        a.price compare b.price
    }
  }
  
  def apply() = new BuyOrderBook(new TreeSet[Order])

  
}

class BuyOrderBook private (entries : TreeSet[Order]) extends OrderBook (entries) {  
  
  def create(entries: TreeSet[Order]): OrderBook = new BuyOrderBook(entries)

  def isMatching(a: Order, b: Order): Boolean = if((a.quantity == b.quantity) && (a.price > b.price)) true else false
  
}

object SellOrderBook {
  
  implicit val ordering : Ordering[Order] = new Ordering[Order] {
    
    def compare(a: Order, b: Order) = {
      if(a.price == b.price)
        if(a.quantity == b.quantity)
          a.timestamp compare b.timestamp
        else
          a.quantity compare b.quantity
      else
        -1 * (a.price compare b.price)
    }
  }
  
  def apply() = new SellOrderBook(new TreeSet[Order])
  
}


class SellOrderBook private (entries : TreeSet[Order]) extends OrderBook (entries) {  
  
  def create(entries: TreeSet[Order]): OrderBook = new SellOrderBook(entries) 

  def isMatching(a: Order, b: Order): Boolean = if((a.quantity == b.quantity) && (a.price < b.price)) true else false
  
}
