package ox.softeng.test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

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

import ox.softeng.oboservice.NarrowerTermLookupService;
import ox.softeng.oboservice.OntologyHandler;
import ox.softeng.oboservice.OntologyHandler.LocalTerm;
import ox.softeng.oboservice.GSonExclusion.ExclBroaderTerms;
import ox.softeng.oboservice.GSonExclusion.ExclDefAndCommentTerms;
import ox.softeng.oboservice.GSonExclusion.ExclNarrowTerms;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class NarrowerTermLookupServiceTest {

	
	private Ontology ontology;
	private NarrowerTermLookupService servlet;
	
	@Before
	public void setUp() throws Exception {
		
		
		
		//partial mock the servlet, as we need to mock the applicationContext
	    //which is part of the main servlet
	    servlet = spy(new NarrowerTermLookupService());
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
	public void doGet_uses_global_cache_when_narrowList_requested() throws Exception {			
		
		servlet = spy(new NarrowerTermLookupService());
	    ServletConfig config = mock(ServletConfig.class);
	    when(servlet.getServletConfig()).thenReturn(config);
	    
	    ServletContext application = mock(ServletContext.class);
		when(config.getServletContext()).thenReturn(application);
		
		//add global cache into application
		HashMap<String,LocalTerm> gloablDB = new HashMap<String,LocalTerm>();
		when(application.getAttribute("ontologyDB")).thenReturn(gloablDB);
		
		
		String dataSourceName = "hp.obo";
		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
	    
        
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);        
        when(request.getParameter("dataSourceName")).thenReturn(dataSourceName);
        when(request.getParameter("termId")).thenReturn("HP:0011035");
        when(request.getParameter("tree")).thenReturn("false");
     
        
        when(session.getAttribute("ontology_" + dataSourceName)).thenReturn(mock(Ontology.class));

        //we do NOT have narrowerTreeCache, so when it's requested it will return null
        when(session.getAttribute("narrowerTreeCache")).thenReturn(null);
        
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
                
		servlet.doGet(request, response);
		
		//once it is called by the OntologyServlet & the other time it is called by NarrowerTermLookupService
        verify(application, times(2)).getAttribute(eq("ontologyDB"));    
	}
	
	
	
	@Test
	public void doGet_uses_global_cache_when_narrowTree_requested() throws Exception {			
		
		servlet = spy(new NarrowerTermLookupService());
	    ServletConfig config = mock(ServletConfig.class);
	    when(servlet.getServletConfig()).thenReturn(config);
	    
	    ServletContext application = mock(ServletContext.class);
		when(config.getServletContext()).thenReturn(application);
		
		//add global cache into application
		HashMap<String,LocalTerm> gloablDB = new HashMap<String,LocalTerm>();
		when(application.getAttribute("ontologyDB")).thenReturn(gloablDB);
		
		
		String dataSourceName = "hp.obo";
		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
	    
        
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);        
        when(request.getParameter("dataSourceName")).thenReturn(dataSourceName);
        when(request.getParameter("termId")).thenReturn("HP:0011035");
        when(request.getParameter("tree")).thenReturn("true");
        when(request.getParameter("flat")).thenReturn("false");

        
        when(session.getAttribute("ontology_" + dataSourceName)).thenReturn(mock(Ontology.class));

        //we do NOT have narrowerTreeCache, so when it's requested it will return null
        when(session.getAttribute("narrowerTreeCache")).thenReturn(null);
        
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
                
		servlet.doGet(request, response);
		
		//once it is called by the OntologyServlet & the other time it is called by NarrowerTermLookupService
        verify(application, times(2)).getAttribute(eq("ontologyDB"));    
	}
	
	
	
	@Test
	public void doGet_uses_global_cache_when_narrowTreeFlat_requested() throws Exception {			
		
		servlet = spy(new NarrowerTermLookupService());
	    ServletConfig config = mock(ServletConfig.class);
	    when(servlet.getServletConfig()).thenReturn(config);
	    
	    ServletContext application = mock(ServletContext.class);
		when(config.getServletContext()).thenReturn(application);
		
		//add global cache into application
		HashMap<String,LocalTerm> gloablDB = new HashMap<String,LocalTerm>();
		when(application.getAttribute("ontologyDB")).thenReturn(gloablDB);
		
		
		String dataSourceName = "hp.obo";
		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
	    
        
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);        
        when(request.getParameter("dataSourceName")).thenReturn(dataSourceName);
        when(request.getParameter("termId")).thenReturn("HP:0011035");
        when(request.getParameter("tree")).thenReturn("true");
        when(request.getParameter("flat")).thenReturn("true");

        
        when(session.getAttribute("ontology_" + dataSourceName)).thenReturn(mock(Ontology.class));

        //we do NOT have narrowerTreeCache, so when it's requested it will return null
        when(session.getAttribute("narrowerTreeCache")).thenReturn(null);
        
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
                
		servlet.doGet(request, response);
		
		//once it is called by the OntologyServlet & the other time it is called by NarrowerTermLookupService
        verify(application, times(2)).getAttribute(eq("ontologyDB"));    
	}
	
	@Test
	public void doGet_returns_correct_values_for_narrowTerms() throws Exception {	
 		
		String dataSourceName = "hp.obo";
		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
	    
        
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        when(request.getParameter("dataSourceName")).thenReturn(dataSourceName);
        when(request.getParameter("termId")).thenReturn("HP:0010101");
        when(request.getParameter("tree")).thenReturn("false");

        when(session.getAttribute("ontology_" + dataSourceName)).thenReturn(ontology);
        
        
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
        
        
		servlet.doGet(request, response);

 		ArrayList<LocalTerm> result = new ArrayList<LocalTerm>();
		OntologyHandler handler = new OntologyHandler();
		result.add(handler.new LocalTerm("HP:0010097","Partial duplication of the distal phalanx of the hallux",null,null));
		result.add(handler.new LocalTerm("HP:0010099","Partial duplication of the 1st metatarsal","A developmental defect consisting in the duplication of part of the first metatarsal bone.",null));
		result.add(handler.new LocalTerm("HP:0010095","Partial duplication of the proximal phalanx of the hallux","Partial duplication of the proximal phalanx of big toe.",null));
		
		Gson gson = new GsonBuilder().setExclusionStrategies(new ExclBroaderTerms()).setExclusionStrategies(new ExclNarrowTerms()).create();	
		verify(out,times(1)).print(gson.toJson(result));
	}

	
	
	
	@Test
	public void doGet_returns_correct_values_for_narrowTermsTree() throws Exception {	
 		
		String dataSourceName = "hp.obo";
		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
	    
        
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        when(request.getParameter("dataSourceName")).thenReturn(dataSourceName);
        when(request.getParameter("termId")).thenReturn("HP:0100957");
        when(request.getParameter("tree")).thenReturn("true");
        when(request.getParameter("flat")).thenReturn("false");


        when(session.getAttribute("ontology_" + dataSourceName)).thenReturn(ontology);
        
        
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
        
        
		servlet.doGet(request, response);

        
 		
		OntologyHandler handler = new OntologyHandler();
		LocalTerm result =  handler.new LocalTerm("HP:0100957","Abnormality of the renal medulla","An abnormality of the medulla of the kidney.","The renal medulla is composed mainly of renal tubules.");
		
		LocalTerm nterm1 =  handler.new LocalTerm("HP:0005932","Abnormal renal corticomedullary differentiation","An abnormality of corticomedullary differentiation (CMD) on diagnostic imaging such as magnetic resonance imaging, computer tomography, or sonography. CMD is a difference in the visualization of cortex and medulla.","On T1-weighted magnetic resonance imaging, the signal intensity of the normal renal cortex is typically higher than medulla, resulting in easily visualized corticomedullary differentiation (CMD). Loss of CMD can be seen in disorders such as glomerulonephritis, acute tubular necrosis, end-stage chronic renal failure, obstructive hydronephrosis, and acute allograft rejection.");
		nterm1.narrowTerms.add(handler.new LocalTerm("HP:0005564","Absence of renal corticomedullary differentiation","A lack of differentiation between renal cortex and medulla on diagnostic imaging.",null));
		nterm1.narrowTerms.add(handler.new LocalTerm("HP:0005565","Reduced renal corticomedullary differentiation","Reduced differentiation between renal cortex and medulla on diagnostic imaging.",null));
		result.narrowTerms.add(nterm1);
		
		result.narrowTerms.add(handler.new LocalTerm("HP:0008659","Multiple small medullary renal cysts","The presence of many cysts in the medulla of the kidney.","This feature is the cardinal sign of medullary cystic disease, also known as medullary sponge disease."));
		result.narrowTerms.add(handler.new LocalTerm("HP:0000108","Renal corticomedullary cysts","The presence of multiple cysts at the border between the renal cortex and medulla.",null));
		result.narrowTerms.add(handler.new LocalTerm("HP:0000090","Nephronophthisis","Presence of cysts at the corticomedullary junction of the kidney in combination with tubulointerstitial fibrosis.","Nephronophthisis is here regarded as a phenotypic feature. The disease of the same name results in progressive symmetrical destruction of the kidneys involving both the tubules and glomeruli."));
		
 		
 		Gson gson = new GsonBuilder().setExclusionStrategies(new ExclBroaderTerms()).setExclusionStrategies(new ExclDefAndCommentTerms()).create();	

		verify(out,times(1)).print(gson.toJson(result));
	}
	
	
	
	
	
	@Test
	public void doGet_returns_correct_values_for_narrowTermsTreeFlat() throws Exception {	
 		
		String dataSourceName = "hp.obo";
		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
	    
        
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        when(request.getParameter("dataSourceName")).thenReturn(dataSourceName);
        when(request.getParameter("termId")).thenReturn("HP:0100957");
        when(request.getParameter("tree")).thenReturn("true");
        when(request.getParameter("flat")).thenReturn("true");


        when(session.getAttribute("ontology_" + dataSourceName)).thenReturn(ontology);
        
        
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
        
        
		servlet.doGet(request, response);

        
 		
		OntologyHandler handler = new OntologyHandler();
		ArrayList<LocalTerm> result = new ArrayList<LocalTerm>();
				
				
		result.add(handler.new LocalTerm("HP:0100957","Abnormality of the renal medulla","An abnormality of the medulla of the kidney.","The renal medulla is composed mainly of renal tubules.",0));		
		result.add(handler.new LocalTerm("HP:0005932","Abnormal renal corticomedullary differentiation","An abnormality of corticomedullary differentiation (CMD) on diagnostic imaging such as magnetic resonance imaging, computer tomography, or sonography. CMD is a difference in the visualization of cortex and medulla.","On T1-weighted magnetic resonance imaging, the signal intensity of the normal renal cortex is typically higher than medulla, resulting in easily visualized corticomedullary differentiation (CMD). Loss of CMD can be seen in disorders such as glomerulonephritis, acute tubular necrosis, end-stage chronic renal failure, obstructive hydronephrosis, and acute allograft rejection.",1));
		result.add(handler.new LocalTerm("HP:0005564","Absence of renal corticomedullary differentiation","A lack of differentiation between renal cortex and medulla on diagnostic imaging.",null,2));
		result.add(handler.new LocalTerm("HP:0005565","Reduced renal corticomedullary differentiation","Reduced differentiation between renal cortex and medulla on diagnostic imaging.",null,2));
		result.add(handler.new LocalTerm("HP:0008659","Multiple small medullary renal cysts","The presence of many cysts in the medulla of the kidney.","This feature is the cardinal sign of medullary cystic disease, also known as medullary sponge disease.",1));
		result.add(handler.new LocalTerm("HP:0000108","Renal corticomedullary cysts","The presence of multiple cysts at the border between the renal cortex and medulla.",null,1));
		result.add(handler.new LocalTerm("HP:0000090","Nephronophthisis","Presence of cysts at the corticomedullary junction of the kidney in combination with tubulointerstitial fibrosis.","Nephronophthisis is here regarded as a phenotypic feature. The disease of the same name results in progressive symmetrical destruction of the kidneys involving both the tubules and glomeruli.",1));
		
 		
  		
		Gson gson = new GsonBuilder().setExclusionStrategies(new ExclBroaderTerms()).setExclusionStrategies(new ExclNarrowTerms()).setExclusionStrategies(new ExclDefAndCommentTerms()).create();	

		verify(out,times(1)).print(gson.toJson(result));
	}
	
	
	
	
	
	
	@Test
	public void adds_value_to_narrowerCache_if_it_doesnt_exit() throws Exception {	
 		
		String dataSourceName = "hp.obo";
		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
	    
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        when(request.getParameter("dataSourceName")).thenReturn(dataSourceName);
        when(request.getParameter("termId")).thenReturn("HP:0000108");
        when(request.getParameter("tree")).thenReturn("false");

        //return a fake ontology
        Ontology ontology = mock(Ontology.class);
        when(session.getAttribute("ontology_" + dataSourceName)).thenReturn(ontology);

        //we do NOT have narrowerCache, so when it's requested it will return null
        when(session.getAttribute("narrowerCache")).thenReturn(null);
        
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
        
		servlet.doGet(request, response);

		HashMap<String,ArrayList<LocalTerm>> narrowerCache  = new HashMap<String,ArrayList<LocalTerm>>();
		narrowerCache.put("HP:0000108",new ArrayList<LocalTerm>());
		//session setAttribute should be called to set the cache
        verify(session, times(1)).setAttribute(eq("narrowerCache"), eq(narrowerCache)); 		
	}
	
	@Test
	public void use_narrowerCache_if_has_the_value() throws Exception {	
	 		
			String dataSourceName = "hp.obo";
			HttpServletRequest request   = mock(HttpServletRequest.class);       
		    HttpServletResponse response = mock(HttpServletResponse.class);  
		    	        
	        HttpSession session = mock(HttpSession.class);
	        when(request.getSession()).thenReturn(session);
	        
	        when(request.getParameter("dataSourceName")).thenReturn(dataSourceName);
	        when(request.getParameter("termId")).thenReturn("HP:1");
	        when(request.getParameter("tree")).thenReturn("false");

	        
	        //return a fake ontology
	        Ontology ontology = mock(Ontology.class);
	        when(session.getAttribute("ontology_" + dataSourceName)).thenReturn(ontology);

	        //we HAVE broaderCache in session,
	        //so it should not be added into the cache
			HashMap<String,ArrayList<LocalTerm>> broaderCache  = new HashMap<String,ArrayList<LocalTerm>>();
			broaderCache.put("HP:1",new ArrayList<LocalTerm>());
			broaderCache.put("HP:2",new ArrayList<LocalTerm>());
	        when(session.getAttribute("narrowerCache")).thenReturn(broaderCache);
	        	        
	        ServletOutputStream out = mock(ServletOutputStream.class);
			when(response.getOutputStream()).thenReturn(out);
	        
       
			servlet.doGet(request, response);
	        
	       //so session value should not be set, as it is already there
	        verify(session, times(0)).setAttribute(eq("narrowerCache"), anyObject()); 		
	        //but session get attribute should be called two times to retrieve the saved value
	        verify(session, times(2)).getAttribute(eq("narrowerCache"));
	        verify(session, times(2)).getAttribute(eq("ontology_" + dataSourceName)); 
		}
		
	
	@Test
	public void adds_value_to_narrowerTreeCache_if_it_doesnt_exit() throws Exception {	
 		
		String dataSourceName = "hp.obo";
		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
	    
        
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        when(request.getParameter("dataSourceName")).thenReturn(dataSourceName);
        when(request.getParameter("termId")).thenReturn("HP:0011035");
        when(request.getParameter("tree")).thenReturn("true");
        when(request.getParameter("flat")).thenReturn("false");

        
        //return a fake ontology
        Ontology ontology = mock(Ontology.class);
        when(session.getAttribute("ontology_" + dataSourceName)).thenReturn(ontology);

        //we do NOT have narrowerCache, so when it's requested it will return null
        when(session.getAttribute("narrowerTreeCache")).thenReturn(null);
        
        
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
        
        
		servlet.doGet(request, response);

        
		HashMap<String,LocalTerm> narrowerCache  = new HashMap<String,LocalTerm>();
		OntologyHandler handler = new OntologyHandler();
		narrowerCache.put("HP:0011035",null);
        verify(session, times(1)).setAttribute(eq("narrowerTreeCache"), eq(narrowerCache)); 		
	}	
	
	@Test
	public void use_narrowerTreeCache_if_has_the_value() throws Exception {	
	 		
			String dataSourceName = "hp.obo";
			HttpServletRequest request   = mock(HttpServletRequest.class);       
		    HttpServletResponse response = mock(HttpServletResponse.class);  
		    	        
	        HttpSession session = mock(HttpSession.class);
	        when(request.getSession()).thenReturn(session);
	        
	        when(request.getParameter("dataSourceName")).thenReturn(dataSourceName);
	        when(request.getParameter("termId")).thenReturn("HP:1");
	        when(request.getParameter("tree")).thenReturn("true");
	        when(request.getParameter("flat")).thenReturn("false");

	        
	        //return a fake ontology
	        Ontology ontology = mock(Ontology.class);
	        when(session.getAttribute("ontology_" + dataSourceName)).thenReturn(ontology);

	        //we HAVE broaderCache in session,
	        //so it should not be added into the cache
			HashMap<String,LocalTerm> broaderCache  = new HashMap<String,LocalTerm>();
			OntologyHandler handler = new OntologyHandler(ontology);
			broaderCache.put("HP:1",handler.new LocalTerm("","","",""));
 	        when(session.getAttribute("narrowerTreeCache")).thenReturn(broaderCache);
	        
	        
	        ServletOutputStream out = mock(ServletOutputStream.class);
			when(response.getOutputStream()).thenReturn(out);
	       
			servlet.doGet(request, response);
	        
	       //so session value should not be set, as it is already there
	        verify(session, times(0)).setAttribute(eq("narrowerTreeCache"), anyObject()); 
	        //session get attribute should be called to retrieve the cache value
	        verify(session, times(2)).getAttribute(eq("narrowerTreeCache"));
	        verify(session, times(2)).getAttribute(eq("ontology_" + dataSourceName)); 	
	}
	
	
	
	
	
	@Test
	public void adds_value_to_narrowerTreeFlatCache_if_it_doesnt_exit() throws Exception {	
 		
		String dataSourceName = "hp.obo";
		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
	    
        
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        when(request.getParameter("dataSourceName")).thenReturn(dataSourceName);
        when(request.getParameter("termId")).thenReturn("HP:0011035");
        when(request.getParameter("tree")).thenReturn("true");
        when(request.getParameter("flat")).thenReturn("true");

        
        //return a fake ontology
        Ontology ontology = mock(Ontology.class);
        when(session.getAttribute("ontology_" + dataSourceName)).thenReturn(ontology);

        //we do NOT have narrowerCache, so when it's requested it will return null
        when(session.getAttribute("narrowerTreeFlatCache")).thenReturn(null);
        
        
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
        
        
		servlet.doGet(request, response);

        
		HashMap<String,ArrayList<LocalTerm>> narrowerCache  = new HashMap<String,ArrayList<LocalTerm>>();
		OntologyHandler handler = new OntologyHandler();
		narrowerCache.put("HP:0011035",new ArrayList<LocalTerm>() );
        verify(session, times(1)).setAttribute(eq("narrowerTreeFlatCache"), eq(narrowerCache)); 		
	}	
	
	@Test
	public void use_narrowerTreeFlatCache_if_has_the_value() throws Exception {	
	 		
			String dataSourceName = "hp.obo";
			HttpServletRequest request   = mock(HttpServletRequest.class);       
		    HttpServletResponse response = mock(HttpServletResponse.class);  
		    	        
	        HttpSession session = mock(HttpSession.class);
	        when(request.getSession()).thenReturn(session);
	        
	        when(request.getParameter("dataSourceName")).thenReturn(dataSourceName);
	        when(request.getParameter("termId")).thenReturn("HP:1");
	        when(request.getParameter("tree")).thenReturn("true");
	        when(request.getParameter("flat")).thenReturn("true");

	        
	        //return a fake ontology
	        Ontology ontology = mock(Ontology.class);
	        when(session.getAttribute("ontology_" + dataSourceName)).thenReturn(ontology);

	        //we HAVE broaderCache in session,
	        //so it should not be added into the cache
			HashMap<String,ArrayList<LocalTerm>> broaderCache  = new HashMap<String,ArrayList<LocalTerm>>();
			OntologyHandler handler = new OntologyHandler(ontology);
			broaderCache.put("HP:1",new ArrayList<LocalTerm>());
 	        when(session.getAttribute("narrowerTreeFlatCache")).thenReturn(broaderCache);
	        
	        
	        ServletOutputStream out = mock(ServletOutputStream.class);
			when(response.getOutputStream()).thenReturn(out);
	       
			servlet.doGet(request, response);
	        
	       //so session value should not be set, as it is already there
	        verify(session, times(0)).setAttribute(eq("narrowerTreeFlatCache"), anyObject()); 
	        verify(session, times(2)).getAttribute(eq("narrowerTreeFlatCache"));
	        verify(session, times(2)).getAttribute(eq("ontology_" + dataSourceName)); 	
	}
	
	
	
}