package orderbook

import orderbook.model.ListOrderBook
import orderbook.model.OrderBook
import orderbook.model.Entry
import orderbook.model.BidListOrderBook
import orderbook.model.AskListOrderBook

object Instruction extends Enumeration {
  type Instruction = Value
  val Update, Delete, New = Value
}

object Side extends Enumeration {
  type Side = Value
  val Bid, Ask = Value
}

class OrderBookEngine(val depth: Int, val tickSize: BigDecimal) {
  
  import Instruction._
  import Side._
  
  var bidBook : OrderBook = new BidListOrderBook(depth, tickSize)
  var askBook : OrderBook = new AskListOrderBook(depth, tickSize) 
  
  def bidEntries = bidBook.entries
  def askEntries = askBook.entries
  
  def marketUpdate(ins: Instruction, side: Side, priceLevel: Int, price: BigDecimal, quantity: Int) : Unit = {
    side match {
      case Bid => bidBook = executeInstruction(bidBook, ins, priceLevel, price, quantity)
      case Ask => askBook = executeInstruction(askBook, ins, priceLevel, price, quantity)
      case _ => throw new IllegalArgumentException("Invalid side")
    }
  }
  
  private def executeInstruction(book: OrderBook, ins: Instruction, priceLevel: Int, price: BigDecimal, quantity: Int) : OrderBook = {
    val e = Entry(price, quantity)
    ins match {   
      case New => book.insert(e, priceLevel)
      case Update => book.update(e, priceLevel)
      case Delete => book.delete(priceLevel)
      case _ => throw new IllegalArgumentException("Invalid instruction")
    }    
  }
  
}