package com.genomicsengland.ocservice.lookupservice

import org.json4s._
import org.json4s.native.JsonMethods._

case class EligibilityQuestion(date: String, version: String)
case class SpecificDisorder(id: String, name: String, eligibilityQuestion: EligibilityQuestion)
case class SubGroup(id: String, name: String, specificDisorders: List[SpecificDisorder])
case class DiseaseGroup(id: String, name: String, subGroups: List[SubGroup])
case class RareDiseases(DiseaseGroups: List[DiseaseGroup])

object RareDiseaseLookupService {
  
  val rareDiseasesJSONFlat = {
    val disaseOntologyJsonString = io.Source.fromInputStream(getClass.getResourceAsStream("/data/rare-diseases/DiseaseOntology.json")).mkString  
    val disaseOntologyJson = parse(disaseOntologyJsonString)
    implicit lazy val formats = DefaultFormats
    val rd = disaseOntologyJson.extract[RareDiseases] 
    import org.json4s.JsonDSL._
    val elements = for {
   	  disaseGroup <- rd.DiseaseGroups
 	    subGroup <- disaseGroup.subGroups
 	    specificDisorder <- subGroup.specificDisorders
 	    val id = specificDisorder.id + "." + specificDisorder.eligibilityQuestion.version
    } yield ("id" -> id) ~ ("diseaseGroup" -> disaseGroup.name) ~ ("diseaseSubgroup" -> subGroup.name) ~ ("diseaseName" -> specificDisorder.name)  
    compact(render(elements)) 
  }
  
}