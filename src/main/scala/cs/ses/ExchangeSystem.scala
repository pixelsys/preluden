package cs.ses

import cs.ses.model.MarketOrder
import cs.ses.model.Entry
import cs.ses.model.Direction._

trait ExchangeSystem {
  
  /**
   * Add an order
   */
  def add(order: MarketOrder) : Unit
  
  /**
   * Returns open interest for a given RIC and direction
   */  
  def openInt(ric: String, direction: Direction) : List[Entry]
  
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
  
  def add(order: MarketOrder) : Unit = {
    
  }

  def avgExPrice(ric: String): BigDecimal = {
    ???
  }

  def exQty(ric: String, user: String): Int = {
    ???
  }

  def openInt(ric: String, direction: Direction): List[Entry] = {
    ???
  }
  
}