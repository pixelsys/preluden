# qt-orderbook
Data Structures and Algorithms Exercise
v1.0.0 &copy; Daniel Petyus

## Usage
To run from command line, after checking out
```
sbt clean
sbt package
scala target/scala-2.11/qt-orderbook_2.11-1.0.0.jar src/test/resources/updates.txt 10.0 2
```
To run unit-tests
```
sbt clean
sbt test
```

## Assumptions 
- In the Bid side book entries are in descending order per price
- In the Ask side book entries are in ascending order per price
- Tick size is taken into account when inserting a new entry or updating an entry with a different price. Hence 'quantity only' updates are allowed

## Design Decisions
- To represent numbers internally BigDecimal was used
- It is hard to choose a data structure for the proposed problem: 
  - For an input which contains more updates an Array based solution can be better  
  - For an input which contains more inserts a List based solution can be better 
  - Here an immutable List based solution was implemented

## Further Discussion
- For better performance input parsing and order book operations should be decoupled (in the engine)
  - A thread reading instructions to a bid and an ask queue
  - Consumer threads taking elements from their queue and executing instructions 
   