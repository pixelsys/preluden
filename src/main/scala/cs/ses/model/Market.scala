package cs.ses.model

import cs.ses.model.Direction._

/**
 * @author dpetyus
 */
object MarketOrder {

  def apply(direction: Direction, ric: String, quantity: Int, price: BigDecimal, user: String) = {
    new MarketOrder(direction, ric, Entry(quantity, price), user)
  }  
  
}

case class MarketOrder(direction: Direction, ric: String, order: Entry, user: String)