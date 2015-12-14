import scala.collection.mutable.ListBuffer

object Tree {

	sealed trait Tree
	case class Branch(id: Int, children: Seq[Tree]) extends Tree
	case class Leaf(id: Int) extends Tree
	
	val res0 = Branch(1,List(Leaf(11),Leaf(12)))
                                                  //> res0  : Tree.Branch = Branch(1,List(Leaf(11), Leaf(12)))
	val res1 = Branch(2,List(Leaf(21),Leaf(22)))
                                                  //> res1  : Tree.Branch = Branch(2,List(Leaf(21), Leaf(22)))
  val res2 = Branch(0,List(res0,res1))            //> res2  : Tree.Branch = Branch(0,List(Branch(1,List(Leaf(11), Leaf(12))), Bran
                                                  //| ch(2,List(Leaf(21), Leaf(22)))))

	
	def leafNodes(node: Branch) : List[Leaf] = {
	  val acc = new ListBuffer[Leaf]()
		val sg = node.children.map{ c => c match {
			case l : Leaf => acc += l
			case b : Branch => acc ++= leafNodes(b)
		}}
		acc.toList
	}                                         //> leafNodes: (node: Tree.Branch)List[Tree.Leaf]
	
	leafNodes(res2)                           //> res0: List[Tree.Leaf] = List(Leaf(11), Leaf(12), Leaf(21), Leaf(22))
	
}