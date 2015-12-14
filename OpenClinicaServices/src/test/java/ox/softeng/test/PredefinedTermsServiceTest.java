package ox.softeng.test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.biojava3.ontology.Ontology;
import org.biojava3.ontology.io.OboParser;
import org.junit.Before;
import org.junit.Test;

import ox.softeng.oboservice.PredefinedTermsService;

public class PredefinedTermsServiceTest {


	
	private Ontology ontology;
	private PredefinedTermsService  servlet;

	
	@Before
	public void setUp() throws Exception {
				
		servlet = spy(new PredefinedTermsService());
	    ServletConfig config = mock(ServletConfig.class);
	    when(servlet.getServletConfig()).thenReturn(config);
	    
		ServletContext application = mock(ServletContext.class);
		when(config.getServletContext()).thenReturn(application);
			
		OboParser parser = new OboParser();
		
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("resources/hp.obo");
		assertNotNull(inStream);		
			
		BufferedReader oboFile = new BufferedReader ( new InputStreamReader ( inStream ) );
		assertNotNull(oboFile);
	
		try{
			ontology = parser.parseOBO(oboFile, "my Ontology name", "description of ontology");
		}
		catch(Exception ex){		
		}
		assertNotNull(ontology);
	}
	
	@Test
	public void doGet_returns_correct_terms_for_specified_diseaseName() throws Exception {	
 		
 		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
	    
        String dataSourceName = "hp.obo";
        
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        
        when(request.getParameter("dataSourceName")).thenReturn("hp.obo");
        when(request.getParameter("predefinedTermsSourceName")).thenReturn("DiseaseOntology.json");
        when(session.getAttribute("ontology_" + dataSourceName)).thenReturn(ontology);

        when(request.getParameter("diseaseGroup")).thenReturn("Cardiovascular disorders");
        when(request.getParameter("subGroup")).thenReturn("Connective Tissues Disorders and Aortopathies");
        when(request.getParameter("specificDisorder")).thenReturn("Familial Thoracic Aortic Aneurysm Disease");
 
        when(request.getParameter("loadNarrowTermsTreeFlat")).thenReturn("true");
         
                
        ServletContext thisContext = mock(ServletContext.class); 
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("resources/rarediseases/DiseaseOntology.json");
        when(request.getServletContext()).thenReturn(thisContext);
        when(thisContext.getResourceAsStream("/WEB-INF/rare-diseases/DiseaseOntology.json")).thenReturn(inStream);
        
        
        
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
		
                
        servlet.doGet(request, response);
 
        String expectedJSON = "[{\"id\":\"HP:0001634\",\"name\":\"Mitral valve prolapse\",\"narrowTerms\":[{\"id\":\"HP:0001634\",\"name\":\"Mitral valve prolapse\",\"def\":\"One or both of the leaflets (cusps) of the mitral valve bulges back into the left atrium upon contraction of the left ventricle.\",\"comment\":\"Mitral valve prolapse can be associated with mitral regurgitation.\",\"depthLevel\":0,\"narrowTerms\":[]}]}]";
        		
		verify(out,times(1)).print(expectedJSON);
	}
	
	
	
//	@Test
//	public void doGet_returns_correct_terms_and_narrowerTermsTreeFlat_for_specified_diseaseName_if_loadNarrow_param_is_provided() throws Exception {	
// 		
//		HttpServletRequest request   = mock(HttpServletRequest.class);       
//	    HttpServletResponse response = mock(HttpServletResponse.class);  
//	    
//        String dataSourceName = "hp.obo";
//        
//        HttpSession session = mock(HttpSession.class);
//        when(request.getSession()).thenReturn(session);
//        
//        
//        when(request.getParameter("dataSourceName")).thenReturn("hp.obo");
//        when(request.getParameter("predefinedTermsSourceName")).thenReturn("GELRareDiseases.json");
//        when(request.getParameter("diseaseName")).thenReturn("Rare inherited haematological disorders");
//        when(request.getParameter("loadNarrowTermsTreeFlat")).thenReturn("true");
//        
//        
//        when(session.getAttribute("ontology_" + dataSourceName)).thenReturn(ontology);
//        
//                
//        ServletContext thisContext = mock(ServletContext.class); 
//		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("resources/predefTerms/GELRareDiseases.json");
//        when(request.getServletContext()).thenReturn(thisContext);
//        when(thisContext.getResourceAsStream("/WEB-INF/predefTerms/GELRareDiseases.json")).thenReturn(inStream);
//        
//        
//        
//        ServletOutputStream out = mock(ServletOutputStream.class);
//		when(response.getOutputStream()).thenReturn(out);
//		
//                
//		servlet.doGet(request, response);
// 
//
//		OntologyHandler ongHandler = new OntologyHandler(ontology);
//		
//		ArrayList<PredefinedTerm> expected = new ArrayList<PredefinedTerm>();
//		PredefinedTermsService s = new PredefinedTermsService();
//		expected.add(s.new PredefinedTerm("HP:0001508", "Failure to thrive",ongHandler.getNarrowTermsTreeAsFlatList("HP:0001508")));
//		expected.add(s.new PredefinedTerm("HP:0001513","Obesity",ongHandler.getNarrowTermsTreeAsFlatList("HP:0001513")));
//				
//					
//		
// 		Gson gson = new GsonBuilder().setExclusionStrategies(new ExclBroaderTerms()).create();
//		verify(out,times(1)).print(gson.toJson(expected));
//        verify(session, times(2)).getAttribute(eq("ontology_" + dataSourceName)); 	
//
//	}
//	
//	
//	
//	@Test
//	public void doGet_will_return_at_max_one_thousand_narrowerTerms() throws Exception {	
// 		
//		HttpServletRequest request   = mock(HttpServletRequest.class);       
//	    HttpServletResponse response = mock(HttpServletResponse.class);  
//	    
//        String dataSourceName = "hp.obo";
//        
//        HttpSession session = mock(HttpSession.class);
//        when(request.getSession()).thenReturn(session);
//        
//        
//        when(request.getParameter("dataSourceName")).thenReturn("hp.obo");
//        when(request.getParameter("predefinedTermsSourceName")).thenReturn("GELRareDiseases.json");
//        when(request.getParameter("diseaseName")).thenReturn("GSTT-RD-11-01-01 Alport syndrome");
//        when(request.getParameter("loadNarrowTermsTreeFlat")).thenReturn("true");
//        
//        
//        when(session.getAttribute("ontology_" + dataSourceName)).thenReturn(ontology);
//        
//                
//        ServletContext thisContext = mock(ServletContext.class); 
//		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("resources/predefTerms/GELRareDiseases.json");
//        when(request.getServletContext()).thenReturn(thisContext);
//        when(thisContext.getResourceAsStream("/WEB-INF/predefTerms/GELRareDiseases.json")).thenReturn(inStream);
//        
//        
//        
//        ServletOutputStream out = mock(ServletOutputStream.class);
//		when(response.getOutputStream()).thenReturn(out);
//		
//                
//		servlet.doGet(request, response);
// 
//
//		OntologyHandler ongHandler = new OntologyHandler(ontology);
//		
//		ArrayList<PredefinedTerm> expected = new ArrayList<PredefinedTerm>();
//		PredefinedTermsService s = new PredefinedTermsService();
//		
////		ArrayList<LocalTerm> narrowTerms = ongHandler.getNarrowTermsTreeAsFlatList("HP:0000924");
////		if(narrowTerms.size() > 1000)
////			narrowTerms.subList(999 , narrowTerms.size()-1).clear();
//		
//		ArrayList<LocalTerm> narrowTerms = ongHandler.getNarrowTermsTreeAsFlatList("HP:0000924");
//		if(narrowTerms.size()>1000) {
//			ArrayList<LocalTerm> narrowTerms4Level = new ArrayList<LocalTerm>();
//			
//			for(int l = 0; l < narrowTerms.size(); l++){
//				LocalTerm lTerm = narrowTerms.get(l);
//				if(lTerm.depthLevel  < 4 )
//					narrowTerms4Level.add(lTerm);
//				
//			}
//			narrowTerms = narrowTerms4Level;
//		}	
//		
//		
//		expected.add(s.new PredefinedTerm("HP:0000924","Abnormality of the skeletal system",narrowTerms));
//			
//		
// 		Gson gson = new GsonBuilder().setExclusionStrategies(new ExclBroaderTerms()).create();
//		verify(out,times(1)).print(gson.toJson(expected));
//        verify(session, times(2)).getAttribute(eq("ontology_" + dataSourceName)); 	
//
//	}
	
	
}
