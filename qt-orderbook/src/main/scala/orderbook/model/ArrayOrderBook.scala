package orderbook.model

import scala.collection.mutable.ArraySeq
import scala.collection.mutable.WrappedArray

class ArrayOrderBook(val entries: WrappedArray[Entry], val tickSize: BigDecimal) extends OrderBook {

  override def insert(e: Entry, level: Int): OrderBook = {
    if(!checkLevel(level))
      throw new IllegalArgumentException("Level out of bounds: " + level + " should be [0.." + (depth - 1) + "]")
    if(0 != level && entries(level - 1).price < e.price) 
      throw new IllegalArgumentException("Order book must be ordered")
    if(depth - 1 > level && entries(level + 1).price > e.price)
      throw new IllegalArgumentException("Order book must be ordered")
    if(isTooSmall(entries(level).price, e.price))
      return this    
    ???
  }
  
  
  override def delete(level: Int): OrderBook = {
    //new ArrayOrderBook(Array[Entry](), 1.0)
    ???
  }

  override def update(e: Entry, level: Int): OrderBook = {
    ???
  }
}