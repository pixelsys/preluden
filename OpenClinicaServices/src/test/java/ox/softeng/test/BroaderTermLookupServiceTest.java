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

import ox.softeng.oboservice.BroaderTermLookupService;
import ox.softeng.oboservice.OntologyHandler;
import ox.softeng.oboservice.OntologyHandler.LocalTerm;
import ox.softeng.oboservice.GSonExclusion.ExclBroaderTerms;
import ox.softeng.oboservice.GSonExclusion.ExclNarrowTerms;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class BroaderTermLookupServiceTest {

	private Ontology ontology;
	private BroaderTermLookupService servlet;
	
	@Before
	public void setUp() throws Exception {
		
		
		//partial mock the servlet, as we need to mock the applicationContext
	    //which is part of the main servlet
	    servlet = spy(new BroaderTermLookupService());
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
	public void doGet_uses_global_cache() throws Exception {
	
		servlet = spy(new BroaderTermLookupService());
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
	    when(request.getParameter("dataSourceName")).thenReturn(dataSourceName);
        when(request.getParameter("termId")).thenReturn("HP:0000108");
        when(response.getOutputStream()).thenReturn(mock(ServletOutputStream.class));
        
        
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);                       
        //return a mock ontology        
        when(session.getAttribute("ontology_" + dataSourceName)).thenReturn(mock(Ontology.class));
        //we do NOT have broaderCache, so it should return null and THEN it will check the global cache
        when(session.getAttribute("broaderCache")).thenReturn(null);        
                     		
		servlet.doGet(request, response);
     
        //once it is called by the OntologyServlet & the other time it is called by BroaderTermLookupServlet
        verify(application, times(2)).getAttribute(eq("ontologyDB"));
	}
	
	
	@Test
	public void doGet_returns_correct_values() throws Exception {	
 		
		String dataSourceName = "hp.obo";
		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
	    
        
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        when(request.getParameter("dataSourceName")).thenReturn(dataSourceName);
        when(request.getParameter("termId")).thenReturn("HP:0000108");
        
        when(session.getAttribute("ontology_" + dataSourceName)).thenReturn(ontology);
        
        
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
        
        
        servlet.doGet(request, response);

        
		HashMap<String,ArrayList<LocalTerm>> broaderCache  = new HashMap<String,ArrayList<LocalTerm>>();
		ArrayList<LocalTerm> result = new ArrayList<LocalTerm>();
		OntologyHandler handler = new OntologyHandler();
		result.add(handler.new LocalTerm("HP:0000107","Renal cyst","A fluid filled sac in the kidney.",null));
		result.add(handler.new LocalTerm("HP:0100957","Abnormality of the renal medulla","An abnormality of the medulla of the kidney.","The renal medulla is composed mainly of renal tubules."));
		result.add(handler.new LocalTerm("HP:0011035","Abnormality of the renal cortex","An abnormality of the cortex of the kidney.",null));
		  
		Gson gson = new GsonBuilder().setExclusionStrategies(new ExclBroaderTerms()).setExclusionStrategies(new ExclNarrowTerms()).create();
		verify(out,times(1)).print(gson.toJson(result));
	}
	
	
	
	@Test
	public void adds_value_to_cache_if_it_doesnt_exit() throws Exception {	
 		
		String dataSourceName = "hp.obo";
		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
	    
        
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        when(request.getParameter("dataSourceName")).thenReturn(dataSourceName);
        when(request.getParameter("termId")).thenReturn("HP:0000108");
        
        //return a fake ontology
        Ontology ontology = mock(Ontology.class);
        when(session.getAttribute("ontology_" + dataSourceName)).thenReturn(ontology);

        //we do NOT have broaderCache, so it should be added into the cache
        when(session.getAttribute("broaderCache")).thenReturn(null);
        
        
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
        
        
		servlet.doGet(request, response);

        
		HashMap<String,ArrayList<LocalTerm>> broaderCache  = new HashMap<String,ArrayList<LocalTerm>>();
		broaderCache.put("HP:0000108",new ArrayList<LocalTerm>());
        verify(session, times(1)).setAttribute(eq("broaderCache"), eq(broaderCache));
	}
	
	
@Test
	public void use_values_if_it_exit_in_cache() throws Exception {	
 		
		String dataSourceName = "hp.obo";
		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
	    

        
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        when(request.getParameter("dataSourceName")).thenReturn(dataSourceName);
        when(request.getParameter("termId")).thenReturn("HP:1");
        
        //return a fake ontology
        Ontology ontology = mock(Ontology.class);
        when(session.getAttribute("ontology_" + dataSourceName)).thenReturn(ontology);

        //we HAVE broaderCache in session,
        //so it should not be added into the cache
		HashMap<String,ArrayList<LocalTerm>> broaderCache  = new HashMap<String,ArrayList<LocalTerm>>();
		broaderCache.put("HP:1",new ArrayList<LocalTerm>());
		broaderCache.put("HP:2",new ArrayList<LocalTerm>());
        when(session.getAttribute("broaderCache")).thenReturn(broaderCache);
        
        
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
        
        
        
		servlet.doGet(request, response);
        
       //so session value should not be set, as it is already there
        verify(session, times(0)).setAttribute(eq("broaderCache"), anyObject()); 		
	}
	



@Test
public void use_global_ontologyCache_if_it_exits() throws Exception {	
		
	String dataSourceName = "hp.obo";
	HttpServletRequest request   = mock(HttpServletRequest.class);       
    HttpServletResponse response = mock(HttpServletResponse.class);  
    

    
    HttpSession session = mock(HttpSession.class);
    when(request.getSession()).thenReturn(session);
    
    when(request.getParameter("dataSourceName")).thenReturn(dataSourceName);
    when(request.getParameter("termId")).thenReturn("HP:1");
    
    //return a fake ontology
    Ontology ontology = mock(Ontology.class);
    when(session.getAttribute("ontology_" + dataSourceName)).thenReturn(ontology);

    //we HAVE broaderCache in session,
    //so it should not be added into the cache
	HashMap<String,ArrayList<LocalTerm>> broaderCache  = new HashMap<String,ArrayList<LocalTerm>>();
	broaderCache.put("HP:1",new ArrayList<LocalTerm>());
	broaderCache.put("HP:2",new ArrayList<LocalTerm>());
    when(session.getAttribute("broaderCache")).thenReturn(broaderCache);
    
    
    ServletOutputStream out = mock(ServletOutputStream.class);
	when(response.getOutputStream()).thenReturn(out);
    
    
    
	servlet.doGet(request, response);
    
   //so session value should not be set, as it is already there
    verify(session, times(0)).setAttribute(eq("broaderCache"), anyObject()); 		
}
	
}
