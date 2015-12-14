package com.genomicsengland.ocservice

import spark.SparkWrapper
import com.genomicsengland.ocservice.lookupservice.RareDiseaseLookupService

object WebApp extends SparkWrapper with App {  
   
  Console.println("Hello world!")
  
  get("/lookupServices/rarediseases/flat"){(_,_) => RareDiseaseLookupService.rareDiseasesJSONFlat}

}