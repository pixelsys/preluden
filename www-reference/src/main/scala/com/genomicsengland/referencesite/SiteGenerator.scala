package com.genomicsengland.referencesite

import scalikejdbc._
import scala.collection.mutable.ListBuffer
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files
import com.github.mustachejava.Mustache
import com.github.mustachejava.DefaultMustacheFactory
import scala.collection.mutable.HashMap
import java.io.FileWriter
import java.io.StringReader
import java.io.PrintWriter
import collection.JavaConverters._
import java.io.StringWriter
import ModelCatalogueDao._
import org.joda.time.DateTime

sealed trait Tree
case class Branch(id: Int, name: String, children: Seq[Tree]) extends Tree
case class Leaf(id: Int, name: String, latestVersionId: Int, versionNumber: Int) extends Tree

case class CatalogueElement(id: Int, name: String, description: String, latestVersionId: Int, versionNumber: Int, modelCatalogueId: String)

object CatalogueElement extends SQLSyntaxSupport[CatalogueElement] {
  
  def apply(rs: WrappedResultSet) : CatalogueElement = { 
    val versionId = rs.intOpt("latest_version_id") match {
       case Some(v) => v
       case None => rs.int("id")
    }
    val description  = rs.stringOpt("description") match {
      case Some(v) => v.replaceAll("\n", "<br />\n")      
      case None => ""
    }
    val modelCatalogueId  = rs.stringOpt("model_catalogue_id") match {
      case Some(v) => v
      case None => "#"
    }
    new CatalogueElement(id = rs.int("id"), name = rs.string("name"), description, latestVersionId = versionId, versionNumber = rs.int("version_number"), modelCatalogueId)
  }
  
  def apply(id: Int) : Option[CatalogueElement] = {
    DB.readOnly { implicit session =>
       sql"SELECT id, name, description, latest_version_id, version_number, model_catalogue_id FROM catalogue_element WHERE id=${id}"
         .map(rs => CatalogueElement(rs)).single.apply()
    }    
  }
  
}


object SiteGenerator {

  val mfFactory = new DefaultMustacheFactory()
  
  def loadTree(id: Int, depth: Int) : List[Tree] = {
    DB.readOnly { implicit session =>
      sql"""SELECT t1.id, t1.name, t1.latest_version_id, t1.version_number FROM catalogue_element t1, relationship t2
          WHERE t1.id=t2.destination_id AND t2.source_id=${id} AND t2.relationship_type_id=4
          ORDER BY outgoing_index ASC
        """.map(rs => {
           val latestVersionId = rs.intOpt("latest_version_id") match {
             case Some(v) => v
             case None => rs.int("id")
           }
           if(depth > 0)
             Branch(rs.int("id"), rs.string("name"), loadTree(rs.int("id"), depth - 1))
           else
             Leaf(rs.int("id"), rs.string("name"), latestVersionId, rs.int("version_number"))
         })
        .list
        .apply()
      }
  }    
  
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
  }       
  
  def generatePages(disease: Leaf, targetFolder: File) : Unit = {
    val filename = disease.latestVersionId + "." + disease.versionNumber + ".html"
    val template = mfFactory.compile(new StringReader(io.Source.fromInputStream(getClass.getResourceAsStream("/page.html")).mkString), filename)
    val scopes = HashMap("title" -> disease.name,
                         "eligibility" -> generateElegibilityContent(disease.id),
                         "phenotypes" -> generatePhenotypesContent(disease.id),
                         "clinicalReports" -> generateClinicalReports(disease.id) ).asJava
                                       
    

    val writer = new PrintWriter(targetFolder + File.separator + filename, "utf-8")
                           
    //val writer = new PrintWriter(System.out)                         
    template.execute(writer, scopes)
    writer.flush()
    //val p = Paths.get(targetFolder + File.separator + filename)
    //Files.write(p, output.getBytes())
  }
  
  def generateElegibilityContent(eligibilityElementId: Int) : String = {
    val rootId = queryDestinationId(eligibilityElementId, "%eligibility%") match {
      case Some(id) => id
      case None => return "Eligibility criteria is not available"
    }
    val rootElement = CatalogueElement(rootId).get
    val sections = queryChildrenCatalogueElements(rootId)
    val inclusionCriteria = sections.filter { x => x.name.toLowerCase().contains("inclusion") }.map{ x => x.description}.headOption
    val exclusionCriteria = sections.filter { x => x.name.toLowerCase().contains("exclusion") }.map{ x => x.description}.headOption
    val pgc = sections.filter { x => x.name.toLowerCase().contains("prior genetic") }.map{ x => x.description}.headOption
    val genes = sections.filter { x => x.name.toLowerCase().contains("genes") }.map{ x => x.description}.headOption
    val closingStmt = sections.filter { x => x.name.toLowerCase().contains("closing") }.map{ x => x.description}.headOption
    val template = mfFactory.compile(new StringReader(io.Source.fromInputStream(getClass.getResourceAsStream("/eligibility.html")).mkString), "eligibility")
    val contentWriter = new StringWriter()
    val scopes = HashMap("title" -> rootElement.name,
                         "description" -> rootElement.description,
                         "inclusion_criteria" -> inclusionCriteria.getOrElse(" "),
                         "exclusion_criteria" -> exclusionCriteria.getOrElse(" "),
                         "pgt" -> pgc.getOrElse(" "),
                         "genes" -> genes.getOrElse(" "),
                         "cs" -> closingStmt.getOrElse(" ")).asJava    
    template.execute(contentWriter, scopes)
    contentWriter.flush()
    contentWriter.toString()
  }
  
  // FIXME code duplication
  def generatePhenotypesContent(phenotypesElementId: Int) : String = {
    val rootId = queryDestinationId(phenotypesElementId, "%phenotypes%") match {
      case Some(id) => id
      case None => return "Phenotypes are not available"
    }
    val rootElement = CatalogueElement(rootId).get
    val sections = queryChildrenCatalogueElements(rootId).asJava
    val template = mfFactory.compile(new StringReader(io.Source.fromInputStream(getClass.getResourceAsStream("/phenotypes.html")).mkString), "phenotypes")
    val contentWriter = new StringWriter()
    val scopes = HashMap("title" -> rootElement.name,
                         "description" -> rootElement.description,
                         "sections" -> sections).asJava    
    template.execute(contentWriter, scopes)
    contentWriter.flush()
    contentWriter.toString()    
  }
  
  // FIXME code duplication
  def generateClinicalReports(clinicalReportsElementId: Int) : String = {
    val rootId = queryDestinationId(clinicalReportsElementId, "%clinical%test%") match {
      case Some(id) => id
      case None => return "Clinical tests are not available"
    }
    val rootElement = CatalogueElement(rootId).get
    val sections = queryChildrenCatalogueElements(rootId).asJava
    //val inclusionCriteria = sections.filter{s => s}
    val template = mfFactory.compile(new StringReader(io.Source.fromInputStream(getClass.getResourceAsStream("/clinicalReports.html")).mkString), "clinicalReports")
    val contentWriter = new StringWriter()
    val scopes = HashMap("title" -> rootElement.name,
                         "description" -> rootElement.description,
                         "sections" -> sections).asJava    
    template.execute(contentWriter, scopes)
    contentWriter.flush()
    contentWriter.toString()
  }
    
  def generateIndexPage(diseases: List[Leaf], targetFolder: File) : Unit = {
    val generated = new DateTime
    val template = mfFactory.compile(new StringReader(io.Source.fromInputStream(getClass.getResourceAsStream("/index.html")).mkString), "index.html")
    val scopes = HashMap("diseases" -> diseases.asJava,
                         "generated" -> generated.toString()).asJava
    val writer = new FileWriter(targetFolder + File.separator + "index.html")
    template.execute(writer, scopes)
    writer.flush()                         
  }
  
}