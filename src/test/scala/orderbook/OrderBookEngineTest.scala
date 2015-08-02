package orderbook

import org.scalatest.FunSuite
import Instruction._
import Side._
import orderbook.model.Entry

class OrderBookEngineTest extends FunSuite {

  test("Running an order book engine") {
    val obe = new OrderBookEngine(2, 0.1)
    obe.marketUpdate(New, Bid, 0, 5, 30)
    obe.marketUpdate(New, Bid, 1, 3, 10)
    obe.marketUpdate(New, Bid, 0, 6, 20)
    obe.marketUpdate(Update, Bid, 1, 4, 10)
    obe.marketUpdate(New, Ask, 0, 10, 10)
    obe.marketUpdate(New, Ask, 1, 5, 5)
    obe.marketUpdate(Delete, Ask, 0, null, 0)
    assert(BigDecimal(0.1) == obe.tickSize)
    assert(Seq(Entry(5,5), Entry(0,0)) == obe.askBook.entries)
    assert(Seq(Entry(6,20), Entry(4,10)) == obe.bidBook.entries)
  }
  
}