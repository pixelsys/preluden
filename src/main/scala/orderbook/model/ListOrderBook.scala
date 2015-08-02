package orderbook.model

abstract class ListOrderBook(val entries: List[Entry], val tickSize: BigDecimal) extends OrderBook  {  
  
  def inOrder(a: Option[BigDecimal], b: BigDecimal, c: Option[BigDecimal]) : Boolean
  
  def construct(entries: List[Entry]) : OrderBook
  
  private def ensureLevel(level: Int) = {
    if(!checkLevel(level))
      throw new IllegalArgumentException("Level out of bounds: " + level + " should be [0.." + (depth - 1) + "]")    
  }
  
  override def insert(e: Entry, level: Int) : OrderBook = {
    ensureLevel(level)
    if(0 == level) {
      if(isTooSmall(entries.head.price, e.price))
        return this      
      if(!inOrder(None, e.price, Some(entries.head.price)))
        throw new IllegalArgumentException("Order book must be ordered")
      construct(e :: entries.take(entries.length - 1))      
    } else {
      val chunks = entries.splitAt(level)
      val leftP = chunks._1.last.price
      val rightP = chunks._2.head.price
      if(isTooSmall(rightP, e.price))
        return this
      if(!inOrder(Some(leftP), e.price, Some(rightP)))
        throw new IllegalArgumentException("Order book must be ordered")
      construct(chunks._1 ::: List(e) ::: chunks._2.take(chunks._2.length - 1))
    }  
  }
  
  override def update(e: Entry, level: Int) : OrderBook = {
    ensureLevel(level)
    val chunks = entries.splitAt(level)
    val oldValue = chunks._2.head.price
    if(oldValue != e.price && (0 == oldValue || isTooSmall(oldValue, e.price)))
      return this
    val leftP = if(!chunks._1.isEmpty) Some(chunks._1.last.price) else None
    val rightP = if(!chunks._2.isEmpty && !chunks._2.tail.isEmpty) Some(chunks._2.tail.head.price) else None
    if(!inOrder(leftP, e.price, rightP))
      throw new IllegalArgumentException("Order book must be ordered")
    construct(chunks._1 ::: List(e) ::: chunks._2.tail)  
  }
  
  override def delete(level: Int) : OrderBook = {
    ensureLevel(level)
    val chunks = entries.splitAt(level)
    val oldValue = chunks._2.head.price
    if(0 == oldValue)
      return this
    construct(chunks._1 ::: chunks._2.tail ::: List(Entry(0, 0)))
  }
    
}

class BidListOrderBook (override val entries: List[Entry], override val tickSize: BigDecimal) extends ListOrderBook (entries, tickSize) { 
  
  def this(depth: Int, tickSize: BigDecimal) =  this(OrderBook.initEntries(depth), tickSize)

  override def inOrder(a: Option[BigDecimal], b: BigDecimal, c: Option[BigDecimal]) : Boolean = {
    a match {
      case Some(aVal) => if(aVal != 0 && aVal < b) return false
      case None =>
    } 
    c match {
      case Some(cVal) => if(cVal != 0 && cVal > b) return false
      case None => 
    }
    true
  }
  
  def construct(entries: List[Entry]): OrderBook = new BidListOrderBook(entries, tickSize)
  
}

class AskListOrderBook (override val entries: List[Entry], override val tickSize: BigDecimal) extends ListOrderBook (entries, tickSize) { 
  
  def this(depth: Int, tickSize: BigDecimal) =  this(OrderBook.initEntries(depth), tickSize)
  
  override def inOrder(a: Option[BigDecimal], b: BigDecimal, c: Option[BigDecimal]) : Boolean = {
    a match {
      case Some(aVal) => if(aVal != 0 && aVal > b) return false
      case None =>
    } 
    c match {
      case Some(cVal) => if(cVal != 0 && cVal < b) return false
      case None => 
    }
    true
  }

  def construct(entries: List[Entry]): OrderBook = new AskListOrderBook(entries, tickSize)
  
}

