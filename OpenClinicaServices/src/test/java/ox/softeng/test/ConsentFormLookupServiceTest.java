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
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import ox.softeng.consentservice.ConsentFormLookupService;
import ox.softeng.consentservice.ConsentQuestionService;


public class ConsentFormLookupServiceTest {
 	
	@Before
	public void setUp() throws Exception {
	 
	}
	
	@Test
	public void doGet_will_return_list_of_consents_for_rare_disease_if_not_theme_is_provided() throws Exception {	
		
 		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
		
		HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        
        ServletContext thisContext = mock(ServletContext.class); 
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("resources/consentForms/consentForms.json");
        when(request.getServletContext()).thenReturn(thisContext);
        when(thisContext.getResourceAsStream("/WEB-INF/consentForms/consentForms.json")).thenReturn(inStream);
                
        
        when(request.getSession()).thenReturn(session);        
 
        
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
		
		
        new ConsentFormLookupService().doGet(request, response);
        
        JSONObject consentForm1 = new JSONObject();
        consentForm1.put("name","Version 1.0 dated 25.08.2014");
        consentForm1.put("id","1");
        JSONArray questions1 = new JSONArray();
        questions1.put("I would want additional genetic findings to be looked for and fed back to my clinical team");
        questions1.put("I would want Pre-symptomatic Carrier Testing information to be looked for and fed back to my clinical team");
        consentForm1.put("stratificationQuestions", questions1);
        
		    		
        JSONObject consentForm2 = new JSONObject();
        consentForm2.put("name","Version 2.0 dated 14.10.2014");
        consentForm2.put("id","2");
        JSONArray questions2 = new JSONArray();
        questions2.put("I would want additional genetic findings to be looked for and fed back to my clinical team");
        questions2.put("I would want Pre-symptomatic Carrier Testing information to be looked for and fed back to my clinical team");
        consentForm2.put("stratificationQuestions", questions2);
        
        JSONArray consentForms = new JSONArray();
        consentForms.put(consentForm1);
        consentForms.put(consentForm2);
        
        JSONObject result =new JSONObject();
        result.put("result", consentForms);
        		    		
		verify(out,times(1)).print(result.toString());
		verify(thisContext,times(1)).getResourceAsStream("/WEB-INF/consentForms/consentForms.json");

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
         
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
		
			
		
        new ConsentFormLookupService().doGet(request, response);
        
        
        
        JSONObject consentForm1 = new JSONObject();
        consentForm1.put("name","Patient with cancer(or suspected cancer) Version 2.0, 20.01.2015");
        consentForm1.put("id","1");
        JSONArray questions1 = new JSONArray();
        questions1.put("I would want additional genetic findings to be looked for and fed back to my clinical team");
        consentForm1.put("stratificationQuestions", questions1);
        
		    		
        JSONObject consentForm2 = new JSONObject();
        consentForm2.put("name","Personal consultee of patients with cancer (or suspected cancer) Version 2.2, 11.02.2015");
        consentForm2.put("id","2");
        JSONArray questions2 = new JSONArray();
        questions2.put("I would want additional genetic findings to be looked for and fed back to my clinical team");
        consentForm2.put("stratificationQuestions", questions2);
        
        JSONArray consentForms = new JSONArray();
        consentForms.put(consentForm1);
        consentForms.put(consentForm2);
        
        JSONObject result =new JSONObject();
        result.put("result", consentForms);
        		    		
		verify(out,times(1)).print(result.toString());
		verify(thisContext,times(0)).getResourceAsStream("/WEB-INF/consentForms/consentForms.json");
		verify(thisContext,times(1)).getResourceAsStream("/WEB-INF/cancer/consentForms/consentForms.json");

	}
	
}