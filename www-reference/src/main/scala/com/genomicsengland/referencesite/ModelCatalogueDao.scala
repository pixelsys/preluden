package com.genomicsengland.referencesite

import scalikejdbc._
import collection.JavaConverters._

/**
 * @author dpetyus
 */
object ModelCatalogueDao {

  def queryDestinationId(sourceId: Int, name: String): Option[Int] = DB.readOnly { implicit session =>
    sql"""SELECT t2.destination_id FROM catalogue_element t1, relationship t2
          WHERE t1.id=t2.destination_id AND t2.source_id=${sourceId} AND t2.relationship_type_id=4 
            AND LOWER(t1.name) LIKE ${name}
       """.map(rs => rs.int("destination_id")).first.apply()
  }

  def queryChildrenCatalogueElements(parentId: Int): List[CatalogueElement] = {
    val result = DB.readOnly { implicit session =>
      sql"""SELECT t2.destination_id FROM catalogue_element t1, relationship t2
              WHERE t1.id=t2.destination_id AND t2.source_id=${parentId} AND t2.relationship_type_id=4
           """.map(rs => rs.int("destination_id")).list.apply()
    }.flatMap { id => CatalogueElement(id) }
    result
  }

}