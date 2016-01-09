package orderbook.model

case class Entry(price: BigDecimal, quantity: Int) 

object OrderBook {
  
  def initEntries(depth: Int) = {
    List.fill(depth)(new Entry(0, 0))
  }
  
}

trait OrderBook {
  
  val entries: Seq[Entry]
  val tickSize: BigDecimal
  val depth : Int = entries.size
  
  def insert(e: Entry, level: Int) : OrderBook 

  def update(e: Entry, level: Int) : OrderBook 
  
  def delete(level: Int) : OrderBook 
  
  def checkLevel(l: Int) : Boolean = {
    if(l < 0) false 
    else if (l >= depth) false
    else true
  }
  
  def isTooSmall(a: BigDecimal, b: BigDecimal) : Boolean = {
    (a - b).abs < tickSize 
  }
  
  override def toString = {
    "OrderBook=" + entries.toString() + ", tick:" + tickSize
  }
  
  override def hashCode = 17 * (41 + entries.hashCode + tickSize.toInt)
  
  override def equals(other: Any) = other match { 
    case that: OrderBook => this.tickSize == that.tickSize && this.entries == that.entries 
    case _ => false 
  }  
  
}
