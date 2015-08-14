package cs.ses.model

import cs.ses.model.Direction._

/**
 * @author dpetyus
 */
case class MarketOrder(ric: String, direction: Direction, quantity: Int, price: BigDecimal, user: String) {
  
}