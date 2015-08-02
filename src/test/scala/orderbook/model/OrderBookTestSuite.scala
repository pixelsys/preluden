package orderbook.model

import org.scalatest.FunSuite

class OrderBookTestSuite extends FunSuite {
  
  test("A new orderbook should be initialised with zero values") {
    val ob = new BidListOrderBook(5, 1.0)
    ob.entries.foreach { e =>  
      assert(0 == e.price)
      assert(0 == e.quantity)
    } 
  }

  test("When invoking an action boundaries should be checked") {
    val ob = new BidListOrderBook(5, 1.0)
    val e = new Entry(10, 10)
    intercept[IllegalArgumentException] {
      ob.insert(e, -1)
    }
    intercept[IllegalArgumentException] {
      ob.insert(e, 5)
    }
    intercept[IllegalArgumentException] {
      ob.update(e, -1)
    }
    intercept[IllegalArgumentException] {
      ob.update(e, 5)
    }    
    intercept[IllegalArgumentException] {
      ob.delete(-1)
    }
    intercept[IllegalArgumentException] {
      ob.delete(5)
    }   
  }
  
  val testBookDepth = 5
  val testTickSize = 10.0
  
  def createTestBook = {
    var ob : OrderBook = new BidListOrderBook(5, testTickSize)
    for(i <- 1 to testBookDepth) {
      ob = ob.insert(new Entry((testBookDepth - i + 1) * 3 * testTickSize, 10), i - 1)
    } 
    // Entry(150.0,10), Entry(120.0,10), Entry(90.0,10), Entry(60.0,10), Entry(30.0,10)
    ob
  }
  
  test("When inserting a new entry existing price levels with a greater or equal index should be shifted up") {
    // fixture
    val ob = createTestBook
    val expectedOb = new BidListOrderBook(List(Entry(150.0,10), Entry(130.0,5), Entry(120.0,10), Entry(90.0,10), Entry(60.0,10)), testTickSize)
      
    // test
    val newEntry = new Entry(130.0, 5)
    val actualOb = ob.insert(newEntry, 1)
   
    // verify
    assert(expectedOb == actualOb)
  }

  test("When deleting a price level existing price levels with a higher index should be shifted down") {
    val ob = createTestBook
    
    // delete from the end
    val expectedOb = new BidListOrderBook(List(Entry(150.0,10), Entry(120.0,10), Entry(90.0,10), Entry(60.0,10), Entry(0,0)), testTickSize)
    val actualOb = ob.delete(4)
    assert(expectedOb == actualOb)
    
    // delete from the middle
    val expectedOb2 = new BidListOrderBook(List(Entry(150.0,10), Entry(120.0,10), Entry(90.0,10), Entry(30.0,10), Entry(0,0)), testTickSize)
    val actualOb2 = ob.delete(3)
    assert(expectedOb2 == actualOb2)
    
    // delete from the head
    val expectedOb3 = new BidListOrderBook(List(Entry(120.0,10), Entry(90.0,10), Entry(60.0,10), Entry(30.0,10), Entry(0,0)), testTickSize)
    val actualOb3 = ob.delete(0)
    assert(expectedOb3 == actualOb3)        
  }
 
  
  test("When updating a price level a new value should be there at the given price level index") {
    // fixture
    val ob = createTestBook
    val expectedOb = new BidListOrderBook(List(Entry(150.0,10), Entry(120.0,10), Entry(90.0,10), Entry(48.12,15), Entry(30.0,10)), testTickSize)
      
    // test
    val updateEntry = new Entry(48.12,15)
    val actualOb = ob.update(updateEntry, 3)
   
    // verify
    assert(expectedOb == actualOb)
  }
  
  test("Testing insert and update at the end of the order book") {
    val ob = createTestBook
    val expectedOb = new BidListOrderBook(List(Entry(150.0,10), Entry(120.0,10), Entry(90.0,10), Entry(60.0,10), Entry(45.0,5)), testTickSize)
    
    val newEntry = new Entry(45.0, 5)
    val actualInsertOb = ob.insert(newEntry, 4)
    assert(expectedOb == actualInsertOb)
    
    val actualUpdateOb = ob.update(newEntry, 4)
    assert(expectedOb == actualUpdateOb)    
  }
  
  test("When inserting or updating with new entries instructions only bigger than the tick size should take effect") {
    val ob = createTestBook
    assert(ob == ob.insert(new Entry(123.14, 5), 1))
    assert(ob == ob.insert(new Entry(115.0, 3), 1))
    assert(ob == ob.update(new Entry(123.14, 5), 1))
    assert(ob == ob.update(new Entry(115.0, 3), 1))    
  }

  test("Entries in the order book must be in descending order") {
    val ob = createTestBook
    for(i <- 1 to testBookDepth) {
      val level = i - 1 
      val bogusEntry = Entry(ob.entries(level).price - testTickSize - 1, 314)
      intercept[IllegalArgumentException] {
        ob.insert(bogusEntry, level)
      }
    }
    for(i <- 2 to testBookDepth) {
      val level = i - 1 
      val bogusEntry = Entry(ob.entries(level - 1).price + testTickSize + 1, 314)
      intercept[IllegalArgumentException] {
        ob.update(bogusEntry, level)
      }
    }    
  }
  
}  
  
