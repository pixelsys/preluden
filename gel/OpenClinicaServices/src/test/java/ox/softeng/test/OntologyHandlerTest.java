package ox.softeng.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.biojava3.ontology.Ontology;
import org.biojava3.ontology.io.OboParser;
import org.junit.Before;
import org.junit.Test;

import ox.softeng.oboservice.OntologyHandler;
import ox.softeng.oboservice.OntologyHandler.LocalTerm;



public class OntologyHandlerTest {


	private OntologyHandler ontologyHandler;
	
	@Before
	public void setUp() throws Exception {
				
		OboParser parser = new OboParser();
		
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("resources/hp.obo");
		assertNotNull(inStream);		
			
		BufferedReader oboFile = new BufferedReader ( new InputStreamReader ( inStream ) );
		assertNotNull(oboFile);
		
		Ontology ontology = null;
		try{
			ontology = parser.parseOBO(oboFile, "my Ontology name", "description of ontology");
		}
		catch(Exception ex){		
		}
		assertNotNull(ontology);

		ontologyHandler = new OntologyHandler(ontology);
		assertNotNull(ontologyHandler);
	}
	
	
	@Test
	public void getNarrowTerms_returns_narrowTerms() {		
		
		//HP:0000280 has 1 narrow term
		 //HP:0000339		
		ArrayList<LocalTerm> result =  ontologyHandler.getNarrowTerms("HP:0000280");
		assertNotNull(result);		
		assertEquals(result.size(),1);
		assertEquals(result.get(0).id,"HP:0000339");
		
				
		//HP:0000436 has 11 narrow terms
		  //HP:0011832
		  //HP:0000451
		  //....
		result =  ontologyHandler.getNarrowTerms("HP:0000436");
		assertNotNull(result);		
		assertEquals(result.size(),11);
		assertEquals(result.get(0).id,"HP:0011832");
		assertEquals(result.get(0).name,"Narrow nasal tip");
		assertEquals(result.get(0).def,"Decrease in width of the nasal tip.");
		assertEquals(result.get(0).comment,"Nasal tip width is assessed at the anterior junction of the alae and the tip. This is easier in persons with a somewhat squared shape of the nasal tip. This may be best viewed from the inferior aspect of the nose. No objective measures are available.");
	
		
		assertEquals(result.get(1).id,"HP:0000451");
		assertEquals(result.get(1).name,"Triangular nasal tip");
		assertEquals(result.get(1).def,null);
		assertEquals(result.get(1).comment,null);
	}
	
	
	
	
	@Test
	public void getBroaderTerms_returns_broaderTerms() {		
		
		//HP:0010612 has 1 broader term
			//is_a: HP:0100276 ! Skin pits
			//is_a: HP:0100872 ! Abnormality of the plantar skin of foot
		
		ArrayList<LocalTerm> result =  ontologyHandler.getBroaderTerms("HP:0010612");
		assertNotNull(result);		
		assertEquals(result.size(),2);
		
		
		assertEquals(result.get(0).id,"HP:0100276");
		assertEquals(result.get(0).name,"Skin pits");
		assertEquals(result.get(0).comment,null);
		assertEquals(result.get(0).def,null);
		
		assertEquals(result.get(1).id,"HP:0100872");
		assertEquals(result.get(1).name,"Abnormality of the plantar skin of foot");
		assertEquals(result.get(1).comment,null);
		assertEquals(result.get(1).def,"An abnormality of the plantar part of foot, that is of the soles of the feet.");
	}
	
	
	
	
	@Test
	public void getNarrowTermsTreeAsFlatList_return_list_as_Ident_and_flat() {			
//		 "HP:0001317",		
//		  "HP:0002542",  "  Olivopontocerebellar atrophy"
//		  "HP:0001272",  "  Cerebellar atrophy",
//		    "HP:0008278","   Cerebellar cortical atrophy",
//		    "HP:0100275",
//		    "HP:0012082",			
//		    "HP:0012080",			  
//		    "HP:0006879",			 
//		  "HP:0007263",
//		....		
				
		ArrayList<LocalTerm> result =  ontologyHandler.getNarrowTermsTreeAsFlatList("HP:0001317");
		assertNotNull(result);	
		
		assertEquals(result.size(),68);
		
		
		assertEquals(result.get(0).id,"HP:0001317");
		assertEquals(result.get(0).name,"Abnormality of the cerebellum");
		assertEquals(result.get(0).depthLevel,0);
 		assertEquals(result.get(0).def,"An abnormality of the cerebellum.");
		assertEquals(result.get(0).comment,null);
		
	
		
		assertEquals(result.get(1).id,"HP:0002542");
		assertEquals(result.get(1).name,"Olivopontocerebellar atrophy");
		assertEquals(result.get(1).depthLevel,1);
 		assertEquals(result.get(1).def,"Neuronal degeneration in the cerebellum, pontine nuclei, and inferior olivary nucleus.");			
		assertEquals(result.get(1).comment,null);	
		
	
		
		assertEquals(result.get(2).id,"HP:0001272");
		assertEquals(result.get(2).name,"Cerebellar atrophy");
		assertEquals(result.get(2).depthLevel,1);
		assertEquals(result.get(2).def,"Atrophy (wasting) of the cerebellum.");			
		assertEquals(result.get(2).comment,"Cerebellar atrophy can be diagnosed if the cerebellum is small with shrunken folia and large cerebellar fissures or if it has been shown to undergo progressive volume loss.");			
						
				
		
		assertEquals(result.get(3).id,"HP:0008278");
		assertEquals(result.get(3).name,"Cerebellar cortical atrophy");
		assertEquals(result.get(3).depthLevel,2);
	}
	
	
	@Test
	public void getNarrowTermsTree_returns_termsTree() {		
		
//		 "HP:0001317",		
//			  "HP:0002542",  "  Olivopontocerebellar atrophy"
//			  "HP:0001272",  "  Cerebellar atrophy",
//			    "HP:0008278","   Cerebellar cortical atrophy",
//			    "HP:0100275",
//			    "HP:0012082",			
//			    "HP:0012080",			  
//			    "HP:0006879",			 
//			  "HP:0007263",
//			....		
		
		  LocalTerm result = ontologyHandler.getNarrowTermsTree("HP:0001317");  
		  assertNotNull(result);
		  
		  assertEquals(result.id, "HP:0001317");
		  assertEquals(result.name, "Abnormality of the cerebellum");

		  assertEquals(result.narrowTerms.size(), 13);
		  
		  assertEquals(result.narrowTerms.get(0).id,"HP:0002542");		  
		  assertEquals(result.narrowTerms.get(0).narrowTerms.size(),0);
		  assertEquals(result.narrowTerms.get(0).name,"Olivopontocerebellar atrophy");
		  assertEquals(result.narrowTerms.get(0).def,"Neuronal degeneration in the cerebellum, pontine nuclei, and inferior olivary nucleus.");
		  assertEquals(result.narrowTerms.get(0).comment,null);
		  
		  assertEquals(result.narrowTerms.get(1).id,"HP:0001272");		  
		  assertEquals(result.narrowTerms.get(1).narrowTerms.size(),6);
	}	
	
	
	@Test
	public void buildInMemory() {	
	
		HashMap<String,LocalTerm> result = ontologyHandler.db;
		  
		assertEquals( result.get("HP:0100957").broaderTerms.size(),1);
		assertEquals( result.get("HP:0100957").broaderTerms.get(0).id,"HP:0012210");
		
		assertEquals( result.get("HP:0100957").narrowTerms.size(),4);
		
	}
	

}