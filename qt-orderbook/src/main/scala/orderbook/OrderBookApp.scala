package orderbook

import Instruction._
import Side._
import scala.io.Source

object OrderBookApp extends App {
  
  if(3 != args.size) {
    printUsage()
    System.exit(-1)
  }
  
  val inputFile = args(0)
  val tickSize = BigDecimal(args(1))
  val bookDepth = Integer.valueOf(args(2))
  
  val engine = new OrderBookEngine(bookDepth, tickSize)
  
  try {  
    for(line <- Source.fromFile(inputFile).getLines()) {
      if(!line.isEmpty) {
        val mu = parseLine(line);
        engine.marketUpdate(mu._1, mu._2, mu._3, mu._4, mu._5)
      }
    }
  } catch {
    case e: Exception => {
      Console.err.println("Error while executing program: " + e.getMessage);
      e.printStackTrace(Console.err);
      System.exit(-1);
    }
  }
  
  printOutput()
  System.exit(0)
  // Ja mata!
  
  def printUsage() = {
    Console.println("Usage $0 inputfile tick-size book-depth")
  }
  
  def parseLine(line: String) : (Instruction, Side, Int, BigDecimal, Int) = {
    val row = line.split(" ")
    // instruction, side, price level index, price, quantity
    if(5 != row.length)
      throw new IllegalStateException("Invalid line, should have 5 items in line: " + line)
    val ins = row(0) match {
      case "N" => Instruction.New
      case "U" => Instruction.Update
      case "D" => Instruction.Delete
      case x => throw new IllegalArgumentException("Invalid instruction " + x + " in line " + line)
    }
    val side = row(1) match {
      case "B" => Side.Bid
      case "A" => Side.Ask
      case x => throw new IllegalArgumentException("Invalid side " + x + " in line " + line)      
    }
    val priceLevelIndex = Integer.valueOf(row(2)) - 1
    val price = BigDecimal(row(3)) * tickSize
    if(0 == price)
      throw new IllegalArgumentException("Price can't be zero in line " + line)
    val quantity = Integer.valueOf(row(4))
    if(0 == quantity)
      throw new IllegalArgumentException("Quantity can't be zero in line " + line)
    (ins, side, priceLevelIndex, price, quantity)
  }
  
  def printOutput() = {
    val bidEntries = engine.bidEntries
    val askEntries = engine.askEntries
    val bookDepth = if(bidEntries.size > askEntries.size) bidEntries.size else askEntries.size    
    (0 to bookDepth - 1).foreach{i =>
      if(bidEntries.length > i) {
        val e = bidEntries(i)
        Console.print(e.price + "," + e.quantity)        
      } else {
        Console.print(",")
      }
      Console.print(",")
      if(askEntries.length > i) {
        val e = askEntries(i)
        Console.print(e.price + "," + e.quantity)        
      } else {
        Console.print(",")
      }
      Console.println()
    }
  }
  
}
