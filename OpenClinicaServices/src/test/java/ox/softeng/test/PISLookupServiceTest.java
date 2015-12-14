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

import ox.softeng.lookupservices.PISLookupService;


public class PISLookupServiceTest {
 	
	@Before
	public void setUp() throws Exception {
	 
	}
	
	@Test
	public void doGet_will_return_list_of_PIS_for_rare_disease_if_no_theme_is_provided() throws Exception {	
		
 		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
		
		HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        
        ServletContext thisContext = mock(ServletContext.class); 
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("resources/consentForms/PIS.json");
        when(request.getServletContext()).thenReturn(thisContext);
        when(thisContext.getResourceAsStream("/WEB-INF/consentForms/PIS.json")).thenReturn(inStream);
                
        when(request.getSession()).thenReturn(session);        
         
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
		
		
        new PISLookupService().doGet(request, response);
        
        JSONObject PIS1 = new JSONObject();
        PIS1.put("name","A");
        PIS1.put("id","1");
        
          				    		
        JSONObject PIS2 = new JSONObject();
        PIS2.put("name","B");
        PIS2.put("id","2");
         
        JSONArray PISs = new JSONArray();
        PISs.put(PIS1);
        PISs.put(PIS2);
        
        JSONObject result =new JSONObject();
        result.put("result", PISs);
        		    		
		verify(out,times(1)).print(result.toString());
		verify(thisContext,times(1)).getResourceAsStream("/WEB-INF/consentForms/PIS.json");
		verify(thisContext,times(0)).getResourceAsStream("/WEB-INF/cancer/consentForms/PIS.json");
	}
	
	@Test
	public void doGet_will_return_list_of_PIS_for_cancer_if_Cancer_is_passed_as_theme() throws Exception {	
		
 		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
		
		HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        
        ServletContext thisContext = mock(ServletContext.class); 
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("resources/cancer/consentForms/PIS.json");
        when(request.getServletContext()).thenReturn(thisContext);
        when(thisContext.getResourceAsStream("/WEB-INF/cancer/consentForms/PIS.json")).thenReturn(inStream);
        
        when(request.getSession()).thenReturn(session);        
        when(request.getParameter("theme")).thenReturn("cancer");
         
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
					
		
        new PISLookupService().doGet(request, response);
        
    	
    	 JSONObject PIS1 = new JSONObject();
         PIS1.put("name","PIS for adult patient with cancer(or suspected cancer), Version 2.1 dated 20.03.2015");
         PIS1.put("id","1");
              	
         JSONObject PIS2 = new JSONObject();
         PIS2.put("name","PIS for personal consultee regarding a patient with cancer (or suspected cancer), Version 2.3 dated 20.03.2015");
         PIS2.put("id","2");
          
         JSONArray PISs = new JSONArray();
         PISs.put(PIS1);
         PISs.put(PIS2);
         
         JSONObject result =new JSONObject();
         result.put("result", PISs);
         		    		
 		verify(out,times(1)).print(result.toString());
 		
      
		verify(thisContext,times(0)).getResourceAsStream("/WEB-INF/consentForms/PIS.json");
		verify(thisContext,times(1)).getResourceAsStream("/WEB-INF/cancer/consentForms/PIS.json");

	}
	
}