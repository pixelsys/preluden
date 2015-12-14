package ox.softeng.test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.biojava3.ontology.Ontology;
import org.biojava3.ontology.Term;
import org.biojava3.ontology.Triple;
import org.biojava3.ontology.io.OboParser;
import org.junit.Before;
import org.junit.Test;

import ox.softeng.oboservice.LookupService;


public class LookupServiceTest {

	
	String dataSourceName = "hp.obo";
	HttpServletRequest request   = mock(HttpServletRequest.class);       
    HttpServletResponse response = mock(HttpServletResponse.class);  
    
	private LookupService servlet;

    
	@Before
	public void setUp() throws Exception {
		
		

			//partial mock the servlet, as we need to mock the applicationContext
		    //which is part of the main servlet
		    servlet = spy(new LookupService());
		    ServletConfig config = mock(ServletConfig.class);
		    when(servlet.getServletConfig()).thenReturn(config);
		    
		    ServletContext application = mock(ServletContext.class);
			when(config.getServletContext()).thenReturn(application);
		
	
		   ServletContext thisContext = mock(ServletContext.class); 
			InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("resources/hp.obo");
	        when(request.getServletContext()).thenReturn(thisContext);
	        when(thisContext.getResourceAsStream("/WEB-INF/obo/" + dataSourceName)).thenReturn(inStream);
	        
	        HttpSession session = mock(HttpSession.class);
	        when(request.getSession()).thenReturn(session);
	        
	        
	        Set<Term> termSet = null;
	        Ontology ontology = null;
	    	try {
				OboParser parser = new OboParser();
				inStream = thisContext.getResourceAsStream("/WEB-INF/obo/" + dataSourceName);
				BufferedReader oboFile = new BufferedReader ( new InputStreamReader ( inStream ) );
				ontology  = parser.parseOBO(oboFile, "my Ontology name", "description of ontology");
				termSet = ontology.getTerms();
				System.out.println("Initialising the OBO Service");
				Object[] triples =  ontology.getTriples(null, null, null).toArray();
				for(int i=0;i<triples.length;i++)
				{
					Triple trip = (Triple) triples[i];
					if(!trip.getPredicate().getName().equals("is_a") && !trip.getPredicate().getName().equals("subset"))
						System.out.println(triples[i]);
				}
				
			} catch (Exception e){
				e.printStackTrace();
			}
	        when(session.getAttribute("termSet_" + dataSourceName)).thenReturn(termSet);
	        when(session.getAttribute("ontology_" + dataSourceName)).thenReturn(ontology);
	    	
	        
	}
	
	
	
	
	@Test
	public void LookupService_returns_matched_result() throws Exception {	
 		
        when(request.getParameter("dataSourceName")).thenReturn(dataSourceName);
        when(request.getParameter("term")).thenReturn("Open mouth");
 
        
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
		
		
        servlet.doGet(request, response);

        
        verify(request, atLeast(1)).getParameter("dataSourceName"); 
        verify(request, atLeast(1)).getParameter("term"); 

		assertNotNull(response.getOutputStream());

		String expected = "[{\"id\":[\"HP:0200096\"],\"value\":[\"Triangular-shaped open mouth\"],\"label\":[\"Triangular-shaped open mouth\"]},{\"id\":[\"HP:0000194\"],\"value\":[\"Open mouth\"],\"label\":[\"Open mouth\"]}]";
		verify(out).print(expected);
	}
	
	
	@Test
	public void LookupService_is_not_caseSensative() throws Exception {	
 		
        when(request.getParameter("dataSourceName")).thenReturn(dataSourceName);
        when(request.getParameter("term")).thenReturn("oPEn MoUtH");
 
        
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
		
		
        servlet.doGet(request, response);

        
        verify(request, atLeast(1)).getParameter("dataSourceName"); 
        verify(request, atLeast(1)).getParameter("term"); 

		assertNotNull(response.getOutputStream());

		String expected = "[{\"id\":[\"HP:0200096\"],\"value\":[\"Triangular-shaped open mouth\"],\"label\":[\"Triangular-shaped open mouth\"]},{\"id\":[\"HP:0000194\"],\"value\":[\"Open mouth\"],\"label\":[\"Open mouth\"]}]";
		verify(out).print(expected);
	}
	
	
	@Test
	public void LookupService_returns_empty_when_no_matched_found() throws Exception {	
 		
        when(request.getParameter("dataSourceName")).thenReturn(dataSourceName);
        when(request.getParameter("term")).thenReturn("XYZXYZ");
 
        
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
		
		
        servlet.doGet(request, response);

        
        verify(request, atLeast(1)).getParameter("dataSourceName"); 
        verify(request, atLeast(1)).getParameter("term"); 

		assertNotNull(response.getOutputStream());
		verify(out).print("[]");
	}
	
}
