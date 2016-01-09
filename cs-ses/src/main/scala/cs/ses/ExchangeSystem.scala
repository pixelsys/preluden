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
  def openInt(ric: String, direction: Direction) : Map[BigDecimal, Int]
  
  /*
   * Returns the average execution price for a given RIC
   */
  def avgExPrice(ric: String) : BigDecimal
  
  /**
   * Returns the executied quantity for a given RIC and user
   */
  def exQty(ric: String, user: String) : Option[Int]
  
}

class SimpleExchangeSystem extends ExchangeSystem {
  
  val buyBooks = new HashMap[String, OrderBook]
  val sellBooks = new HashMap[String, OrderBook]
  val execQty = new HashMap[String, HashMap[String, Int]]
  
  def add(order: MarketOrder) : Unit = {
    val (sourceBook, targetBookOption) = order.direction match {
      case Buy => (buyBooks.getOrElseUpdate(order.ric, OrderBook(Buy)), sellBooks.get(order.ric))
      case Sell => (sellBooks.getOrElseUpdate(order.ric, OrderBook(Sell)), buyBooks.get(order.ric))
      case _ => throw new IllegalArgumentException()
    }
    if(targetBookOption.isEmpty) {
      // source book save order
      val newBook = sourceBook.add(Order(order.quantity, order.price, System.currentTimeMillis(), order.user))
      sellBooks.update(order.ric, newBook)
      logExecuted(order.ric, order.user, 0)
      return
    }
    val targetBook = targetBookOption.get
  }
  
  private def logExecuted(ric: String, user: String, quantity: Int) : Unit = {
    val userMap = execQty.getOrElseUpdate(ric, new HashMap[String, Int])
    val previousQuantity = userMap.getOrElse(user, 0)
    userMap.update(user, previousQuantity + quantity)
  }
   
  def avgExPrice(ric: String): BigDecimal = {
    ???
  }

  def exQty(ric: String, user: String): Option[Int] = {
    execQty.get(ric) match {
      case Some(userMap) => {
        userMap.get(user) match {
          case Some(value) => Some(value)
          case _ => None
        }
      }
      case _ => None
    }
  }

  def openInt(ric: String, direction: Direction): Map[BigDecimal, Int] = {
    val bookOption = direction match {
      case Buy => buyBooks.get(ric)
      case Sell => sellBooks.get(ric)
      case _ => throw new IllegalArgumentException()      
    }
    bookOption match {
      case Some(book) => book.entries.groupBy(_.price).map{case(k,v) => (k,v.map{x => x.quantity})}.map{case(k,v) => (k,v.foldLeft(0){(a,b) => a + b})}
      case None => Map.empty
    }
  }
  
}