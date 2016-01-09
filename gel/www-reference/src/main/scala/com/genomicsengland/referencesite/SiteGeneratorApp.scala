package com.genomicsengland.referencesite

import scalikejdbc._
import SiteGenerator._
import java.io.File

object SiteGeneratorApp extends App {
  
  val rootNodeId = 35936     
  val targetFolder = new File("/Users/dpetyus/Development/sc/dev/www-reference/public_html")
  
  Class.forName("com.mysql.jdbc.Driver") 
  //ConnectionPool.singleton("jdbc:mysql://gelmc-internal.extge.co.uk:3306/GE", "dan", "aiph8thaeVie1we8")  
  ConnectionPool.singleton("jdbc:mysql://localhost:3306/GE", "root", "mysql")
  implicit val session = AutoSession
  
  val diseasesTree = loadTree(rootNodeId, 2) 
  
  diseasesTree.foreach { e => {
    val diseases = leafNodes(e) 
    diseases.foreach{d => {
      Console.println("generating pages for disease: " + d)
      generatePages(d, targetFolder) 
    }}     
  }}
  
  val diseaesList = diseasesTree.map{e => leafNodes(e)}.flatten.sortBy {x => x.name}
  Console.println("Total diseases: " + diseaesList.size)
  generateIndexPage(diseaesList, targetFolder)
  
}

