package cs.ses

import cs.ses.model.MarketOrder
import cs.ses.model.Entry
import cs.ses.model.Direction._
import scala.collection.mutable.HashMap
import cs.ses.model.BuyOrderBook
import cs.ses.model.SellOrderBook
import cs.ses.model.OrderBook
import cs.ses.model.Order

trait ExchangeSystem {
  
  /**
   * Add an order
   */
  def add(order: MarketOrder) : Unit
  
  /**
   * Returns open interest for a given RIC and direction
   */  
  def openInt(ric: String, direction: Direction) : List[(Entry)]
  
  /*
   * Returns the average execution price for a given RIC
   */
  def avgExPrice(ric: String) : BigDecimal
  
  /**
   * Returns the executied quantity for a given RIC and user
   */
  def exQty(ric: String, user: String) : Int
  
}

class SimpleExchangeSystem extends ExchangeSystem {
  
  val buyBooks = new HashMap[String, OrderBook]
  val sellBooks = new HashMap[String, OrderBook]
  
  def add(order: MarketOrder) : Unit = {
    val (sourceBook, targetBookOption) = order.direction match {
      case Buy => (buyBooks.getOrElseUpdate(order.ric, OrderBook(Buy)), sellBooks.get(order.ric))
      case Sell => (sellBooks.getOrElseUpdate(order.ric, OrderBook(Sell)), buyBooks.get(order.ric))
      case _ => throw new IllegalArgumentException()
    }
    Console.println(targetBookOption)
    if(targetBookOption.isEmpty) {
      // source book save order
      sourceBook.add(Order(order.quantity, order.price, System.currentTimeMillis(), order.user))
      return
    }
    val targetBook = targetBookOption.get
  }
  
  def avgExPrice(ric: String): BigDecimal = {
    ???
  }

  def exQty(ric: String, user: String): Int = {
    ???
  }

  def openInt(ric: String, direction: Direction): List[Entry] = {
    val bookOption = direction match {
      case Buy => buyBooks.get(ric)
      case Sell => sellBooks.get(ric)
      case _ => throw new IllegalArgumentException()      
    }
    bookOption match {
      case Some(book) => ??? // book.entries
      case None => List.empty
    }
  }
  
}