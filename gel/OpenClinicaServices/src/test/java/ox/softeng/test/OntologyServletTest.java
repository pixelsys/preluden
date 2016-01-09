package ox.softeng.test;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.biojava3.ontology.Ontology;
import org.junit.Test;

import ox.softeng.oboservice.OntologyServlet;

public class OntologyServletTest {


	@Test(expected=ServletException.class)
	public void doGet_will_thorw_exception_if_datasource_not_provided() throws Exception {	
		
 		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
		
		HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        when(request.getParameter("dataSourceName")).thenReturn(null);
 
        new OntologyServlet().doGet(request, response);
	}
	
	
	@Test
	public void doGet_will_add_termSet_and_Ontology_and_ontologyDB_into_session() throws Exception {	
		
		String dataSourceName = "hp.obo";
		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
		
	    //partial mock the servlet, as we need to mock the applicationContext
	    //which is part of the main servlet
	    OntologyServlet servlet = spy(new OntologyServlet());
		
	    ServletConfig config = mock(ServletConfig.class);
	    when(servlet.getServletConfig()).thenReturn(config);

		
		ServletContext application = mock(ServletContext.class);
		when(config.getServletContext()).thenReturn(application);


		HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        when(request.getParameter("dataSourceName")).thenReturn(dataSourceName);

        ServletContext thisContext = mock(ServletContext.class);
		when(request.getServletContext()).thenReturn(thisContext);

		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("resources/hp.obo");
        when(thisContext.getResourceAsStream("/WEB-INF/obo/" + dataSourceName)).thenReturn(inStream);

        servlet.doGet(request, response);

        verify(request, times(1)).getParameter("dataSourceName"); 
        verify(session, times(1)).setAttribute(eq("termSet_" + dataSourceName), anyObject());
		verify(session, times(1)).setAttribute(eq("ontology_" + dataSourceName), anyObject());
		verify(application, times(1)).setAttribute(eq("ontologyDB"), anyObject());
	}
	
	@Test
	public void doGet_will_use_session_cache() throws Exception {	
		
		String dataSourceName = "hp.obo";
		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
		
	    
	    //partial mock the servlet, as we need to mock the applicationContext
	    //which is part of the main servlet
	    OntologyServlet servlet = spy(new OntologyServlet());
		
	    ServletConfig config = mock(ServletConfig.class);
	    when(servlet.getServletConfig()).thenReturn(config);

		
		ServletContext application = mock(ServletContext.class);
		when(config.getServletContext()).thenReturn(application);
		
		
		HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        when(request.getParameter("dataSourceName")).thenReturn(dataSourceName);

        ServletContext thisContext = mock(ServletContext.class);
		when(request.getServletContext()).thenReturn(thisContext);

		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("resources/hp.obo");
        when(thisContext.getResourceAsStream("/WEB-INF/obo/" + dataSourceName)).thenReturn(inStream);
 
        //if the session cache already exists
        Ontology ontology = mock(Ontology.class);
        when(session.getAttribute("ontology_" + dataSourceName)).thenReturn(ontology);
        
		servlet.doGet(request, response);
        
        verify(request, times(1)).getParameter("dataSourceName");
        //it will not add into session cache
	    verify(session, times(0)).setAttribute(eq("termSet_" + dataSourceName), anyObject());
	    verify(session, times(0)).setAttribute(eq("ontology_" + dataSourceName), anyObject());
	    //this is application cache, as this is actually the first time that we run this test
	    //this will be added into Application cache
	    verify(application, times(1)).setAttribute(eq("ontologyDB"), anyObject());        
	}
	
	
	
	@Test
	public void doGet_will_use_application_cache() throws Exception {	
		
		String dataSourceName = "hp.obo";
		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
		
	    
	    //partial mock the servlet, as we need to mock the applicationContext
	    //which is part of the main servlet
	    OntologyServlet servlet = spy(new OntologyServlet());
		
	    ServletConfig config = mock(ServletConfig.class);
	    when(servlet.getServletConfig()).thenReturn(config);

		
		ServletContext application = mock(ServletContext.class);
		when(config.getServletContext()).thenReturn(application);
		//it doesn't exist
		when(application.getAttribute("ontologyDB")).thenReturn(null);
		
		
		HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        when(request.getParameter("dataSourceName")).thenReturn(dataSourceName);

        ServletContext thisContext = mock(ServletContext.class);
		when(request.getServletContext()).thenReturn(thisContext);

		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("resources/hp.obo");
        when(thisContext.getResourceAsStream("/WEB-INF/obo/" + dataSourceName)).thenReturn(inStream);
 
        //if the session cache already exists
        Ontology ontology = mock(Ontology.class);
        when(session.getAttribute("ontology_" + dataSourceName)).thenReturn(ontology);
        
		servlet.doGet(request, response);
        
        verify(request, times(1)).getParameter("dataSourceName");
        //it will not add into session cache
	    verify(session, times(0)).setAttribute(eq("termSet_" + dataSourceName), anyObject());
	    verify(session, times(0)).setAttribute(eq("ontology_" + dataSourceName), anyObject());
	    //this is application cache, as this is actually the first time that we run this test
	    //this will be added into Application cache
	    verify(application, times(1)).getAttribute(eq("ontologyDB"));    
	    verify(application, times(1)).setAttribute(eq("ontologyDB"), anyObject());    

	    
	    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	    
	    
	    request   = mock(HttpServletRequest.class);       
	    response = mock(HttpServletResponse.class);  
		
	    
	    //partial mock the servlet, as we need to mock the applicationContext
	    //which is part of the main servlet
	     servlet = spy(new OntologyServlet());
		
	  
	    when(servlet.getServletConfig()).thenReturn(config);
	    //this time, it exists
	    when(application.getAttribute("ontologyDB")).thenReturn(new Object());
		
		
		session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        when(request.getParameter("dataSourceName")).thenReturn(dataSourceName);

        thisContext = mock(ServletContext.class);
		when(request.getServletContext()).thenReturn(thisContext);

		 inStream = this.getClass().getClassLoader().getResourceAsStream("resources/hp.obo");
        when(thisContext.getResourceAsStream("/WEB-INF/obo/" + dataSourceName)).thenReturn(inStream);
 
        //if the session cache already exists
        when(session.getAttribute("ontology_" + dataSourceName)).thenReturn(mock(Ontology.class));
        
		servlet.doGet(request, response);
        
        verify(request, times(1)).getParameter("dataSourceName");
        //it will not add into session cache
	    verify(session, times(0)).setAttribute(eq("termSet_" + dataSourceName), anyObject());
	    verify(session, times(0)).setAttribute(eq("ontology_" + dataSourceName), anyObject());
	    
	    //this is application cache, as this is actually the second time that we run this(second session) it will call getAttribute again
	    verify(application, times(2)).getAttribute(eq("ontologyDB"));    
	    //but it will not call setAttribute again, and it is called just once for the first session
	    verify(application, times(1)).setAttribute(eq("ontologyDB"), anyObject());
	}
}
