import scalikejdbc._
import scala.collection.mutable.ListBuffer
import java.io.File

object LoadDataWS {

	Class.forName("com.mysql.jdbc.Driver")    //> res0: Class[?0] = class com.mysql.jdbc.Driver
	ConnectionPool.singleton("jdbc:mysql://localhost:3306/GE", "root", "mysql")
                                                  //> 12:24:04.061 [main] DEBUG scalikejdbc.ConnectionPool$ - Registered connectio
                                                  //| n pool : ConnectionPool(url:jdbc:mysql://localhost:3306/GE, user:root) using
                                                  //|  factory : <default>
                                                  //| 12:24:04.064 [main] DEBUG scalikejdbc.ConnectionPool$ - Registered singleton
                                                  //|  connection pool : ConnectionPool(url:jdbc:mysql://localhost:3306/GE, user:r
                                                  //| oot)
  implicit val session = AutoSession              //> session  : scalikejdbc.AutoSession.type = AutoSession

	// 11144.8
	val id = 29310                            //> id  : Int = 29310
	
	sealed trait Tree
	case class Branch(id: Int, name: String, children: Seq[Tree]) extends Tree
	case class Leaf(id: Int, name: String) extends Tree
	
	//case class Node(id: Int, name: String, children: Option[Seq[Node]])

	def loadTree(id: Int, depth: Int) : List[Tree] = {
		DB.readOnly { implicit session =>
			sql"""SELECT t1.id, t1.name FROM catalogue_element t1, relationship t2
				  WHERE t1.id=t2.destination_id AND t2.source_id=${id} AND t2.relationship_type_id=4
				  ORDER BY outgoing_index ASC
	  		""".map(rs => if(depth > 0)
	  										Branch(rs.int("id"), rs.string("name"), loadTree(rs.int("id"), depth - 1))
	  								  else
	  								  	Leaf(rs.int("id"), rs.string("name")))
				.list
				.apply()
			}
	}                                         //> loadTree: (id: Int, depth: Int)List[LoadDataWS.Tree]
	
	/*
	def leafNodes(root: Node) : Seq[Node] = {
		root.children.flatMap{ t => t match {
			case Some(children) => if(n.children == Seq(EmptyTree)) leafNodes(n) else Some(n)
			
		} }
	}*/
	
	
	val res = loadTree(id, 2)                 //> 12:24:04.493 [main] DEBUG s.StatementExecutor$$anon$1 - SQL execution compl
                                                  //| eted
                                                  //| 
                                                  //|   [SQL Execution]
                                                  //|    SELECT t1.id, t1.name FROM catalogue_element t1, relationship t2 WHERE t
                                                  //| 1.id=t2.destination_id AND t2.source_id=29310 AND t2.relationship_type_id=4
                                                  //|  ORDER BY outgoing_index ASC; (4 ms)
                                                  //| 
                                                  //|   [Stack Trace]
                                                  //|     ...
                                                  //|     LoadDataWS$$anonfun$main$1$$anonfun$1.apply(LoadDataWS.scala:30)
                                                  //|     LoadDataWS$$anonfun$main$1$$anonfun$1.apply(LoadDataWS.scala:21)
                                                  //|     scalikejdbc.DBConnection$class.readOnly(DBConnection.scala:191)
                                                  //|     scalikejdbc.DB.readOnly(DB.scala:60)
                                                  //|     scalikejdbc.DB$$anonfun$readOnly$1.apply(DB.scala:173)
                                                  //|     scalikejdbc.DB$$anonfun$readOnly$1.apply(DB.scala:172)
                                                  //|     scalikejdbc.LoanPattern$class.using(LoanPattern.scala:18)
                                                  //|     scalikejdbc.DB$.using(DB.scala:138)
                                                  //|     scalikejdbc.DB$.readOnly(DB.scala:172)
                                                  //|     LoadDataWS$$anonfun$main$1.LoadDataWS$$anonfun$$loadTree$1(LoadDataWS.s
	Console.println(res)                      //> List(Branch(29311,Cardiovascular disorders,List(Branch(29312,Connective Tis
                                                  //| sues Disorders and Aortopathies,List(Leaf(29313,Familial Thoracic Aortic An
                                                  //| eurysm Disease))), Branch(29314,Cardiac arrhythmia,List(Leaf(29315,Brugada 
                                                  //| syndrome), Leaf(11023,Long QT syndrome), Leaf(11024,Catecholaminergic Polym
                                                  //| orphic Ventricular Tachycardia))), Branch(10953,Cardiomyopathy,List(Leaf(11
                                                  //| 025,Arrhythmogenic Right Ventricular Cardiomyopathy), Leaf(15044,Left Ventr
                                                  //| icular Noncompaction Cardiomyopathy), Leaf(11026,Dilated Cardiomyopathy (DC
                                                  //| M)), Leaf(11027,Dilated Cardiomyopathy and conduction defects), Leaf(11028,
                                                  //| Hypertrophic Cardiomyopathy))), Branch(10954,Congenital heart disease,List(
                                                  //| Leaf(11029,Fallots tetralogy), Leaf(11030,Hypoplastic Left Heart Syndrome),
                                                  //|  Leaf(11031,Pulmonary atresia), Leaf(11032,Transposition of the great vesse
                                                  //| ls), Leaf(11033,Left Ventricular Outflow Tract obstruction disorders), Leaf
                                                  //| (11034,Isomerism and la
                                                  //| Output exceeds cutoff limit.
	
	def leafNodes(node: Tree) : List[Leaf] = {
	  val acc = new ListBuffer[Leaf]()
	  val hmm = node match {
	  	case l : Leaf => acc += l
	  	case b : Branch => b.children.map{ c => c match {
				case l : Leaf => acc += l
				case b : Branch => acc ++= leafNodes(b)
			}}
	  }
		acc.toList
	}                                         //> leafNodes: (node: LoadDataWS.Tree)List[LoadDataWS.Leaf]
	
	leafNodes(res.head)                       //> res1: List[LoadDataWS.Leaf] = List(Leaf(29313,Familial Thoracic Aortic Aneu
                                                  //| rysm Disease), Leaf(29315,Brugada syndrome), Leaf(11023,Long QT syndrome), 
                                                  //| Leaf(11024,Catecholaminergic Polymorphic Ventricular Tachycardia), Leaf(110
                                                  //| 25,Arrhythmogenic Right Ventricular Cardiomyopathy), Leaf(15044,Left Ventri
                                                  //| cular Noncompaction Cardiomyopathy), Leaf(11026,Dilated Cardiomyopathy (DCM
                                                  //| )), Leaf(11027,Dilated Cardiomyopathy and conduction defects), Leaf(11028,H
                                                  //| ypertrophic Cardiomyopathy), Leaf(11029,Fallots tetralogy), Leaf(11030,Hypo
                                                  //| plastic Left Heart Syndrome), Leaf(11031,Pulmonary atresia), Leaf(11032,Tra
                                                  //| nsposition of the great vessels), Leaf(11033,Left Ventricular Outflow Tract
                                                  //|  obstruction disorders), Leaf(11034,Isomerism and laterality disorders))
                                                  
	 def generatePages(target: File) : Unit = {
	 		
	 }                                        //> generatePages: (target: java.io.File)Unit
                                                  
                                                  
	
}