package ox.softeng.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;

import ox.softeng.consentservice.ConsentFormLookupService;
import ox.softeng.consentservice.ConsentQuestionService;


public class ConsentServiceTest {
 	
	@Before
	public void setUp() throws Exception {
	 
	}
	
	@Test
	public void doGet_will_return_questions() throws Exception {	
		
 		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
		
		HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        
        ServletContext thisContext = mock(ServletContext.class); 
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("resources/consentForms/consentForms.json");
        when(request.getServletContext()).thenReturn(thisContext);
        when(thisContext.getResourceAsStream("/WEB-INF/consentForms/consentForms.json")).thenReturn(inStream);
        
        when(request.getSession()).thenReturn(session);        
        when(request.getParameter("consentFormName")).thenReturn("Version 1.0 dated 25.08.2014");
 
        
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
		
		
        new ConsentQuestionService().doGet(request, response);
        
        
        JSONArray questions = new JSONArray();
        questions.put("I would want additional genetic findings to be looked for and fed back to my clinical team");
        questions.put("I would want Pre-symptomatic Carrier Testing information to be looked for and fed back to my clinical team");
		    		

		verify(out,times(1)).print(questions.toString());
	}
	
	@Test
	public void doGet_will_return_empty_questions_when_can_not_find_the_question() throws Exception {	
		
 		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
		
		HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        
        ServletContext thisContext = mock(ServletContext.class); 
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("resources/consentForms/consentForms.json");
        when(request.getServletContext()).thenReturn(thisContext);
        when(thisContext.getResourceAsStream("/WEB-INF/consentForms/consentForms.json")).thenReturn(inStream);
        
        when(request.getSession()).thenReturn(session);        
        when(request.getParameter("consentFormName")).thenReturn("NOT_AVAILABLE");
 
        
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
		
		
        new ConsentQuestionService().doGet(request, response);
        
        
        JSONArray questions = new JSONArray();
		verify(out,times(1)).print(questions.toString());
	}
	
	
	
	@Test
	public void doGet_will_return_list_of_consents_for_cancer_if_Cancer_is_passed_as_theme() throws Exception {	
		
 		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
		
		HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        
        ServletContext thisContext = mock(ServletContext.class); 
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("resources/cancer/consentForms/consentForms.json");
        when(request.getServletContext()).thenReturn(thisContext);
        when(thisContext.getResourceAsStream("/WEB-INF/cancer/consentForms/consentForms.json")).thenReturn(inStream);
        
        when(request.getSession()).thenReturn(session);        
        when(request.getParameter("theme")).thenReturn("cancer");
        when(request.getParameter("consentFormName")).thenReturn("Patient with cancer(or suspected cancer) Version 2.0, 20.01.2015");

         
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
		
				
        new ConsentQuestionService().doGet(request, response);       
                				
        		    		
        JSONArray questions = new JSONArray();
        questions.put("I would want additional genetic findings to be looked for and fed back to my clinical team");
		verify(out,times(1)).print(questions.toString());
		
		verify(thisContext,times(0)).getResourceAsStream("/WEB-INF/consentForms/consentForms.json");
		verify(thisContext,times(1)).getResourceAsStream("/WEB-INF/cancer/consentForms/consentForms.json");

	}
}