package cs.ses

import cs.ses.model.Direction._
import org.scalatest.FunSuite
import cs.ses.model.MarketOrder

class ExchangeSystemTest extends FunSuite {

  val ric = "VOD.L"
  val user1 = "User1"
  val system = new SimpleExchangeSystem()  
  
  def order1 = {
    val newOrder = MarketOrder(Sell, ric, 1000, 100.2, user1)
    system.add(newOrder)
    val status1 = system.openInt(ric, Sell)
    assert(null != status1)
    assert(1 == status1.length)
    assert(100.2 == status1(0).price)
    assert(1000 == status1(0).quantity)
    assert(true == system.openInt(ric, Buy).isEmpty)
    assert(0 == system.exQty(ric, user1))
  }
  
  def order2 = {
    
  }
  
  def order3 = {
    
  }
  
  def order4 = {
    
  }
  
  def order5 = {
    
  }
  
  def order6 = {
    
  }
  
  def order7 = {
    
  }
  
  test("Example sequence of orders") {
    order1
    order2
    order3
    order4
    order5
    order6
    order7
  }
  
}