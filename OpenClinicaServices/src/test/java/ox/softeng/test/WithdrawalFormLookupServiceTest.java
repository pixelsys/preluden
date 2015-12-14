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
import ox.softeng.consentservice.WithdrawalFormLookupService;


public class WithdrawalFormLookupServiceTest {
 	
	@Before
	public void setUp() throws Exception {
	 
	}
	
	@Test
	public void doGet_will_return_list_of_withdrawals_for_rare_disease_if_not_theme_is_provided() throws Exception {	
		
 		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
		
		HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        
        ServletContext thisContext = mock(ServletContext.class); 
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("resources/consentForms/withdrawalForms.json");
        when(request.getServletContext()).thenReturn(thisContext);
        when(thisContext.getResourceAsStream("/WEB-INF/consentForms/withdrawalForms.json")).thenReturn(inStream);
                
        
        when(request.getSession()).thenReturn(session);        
 
        
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
		
		
        new WithdrawalFormLookupService().doGet(request, response);
        
        JSONObject withdrawal1 = new JSONObject();
        withdrawal1.put("name","Version 1.0 dated 25.08.2014");
        withdrawal1.put("id","1");
        
          				    		
        JSONObject withdrawal2 = new JSONObject();
        withdrawal2.put("name","Version 2.0 dated 14.10.2014");
        withdrawal2.put("id","2");
         
        JSONArray withdrawals = new JSONArray();
        withdrawals.put(withdrawal1);
        withdrawals.put(withdrawal2);
        
        JSONObject result =new JSONObject();
        result.put("result", withdrawals);
        		    		
		verify(out,times(1)).print(result.toString());
		verify(thisContext,times(1)).getResourceAsStream("/WEB-INF/consentForms/withdrawalForms.json");
		verify(thisContext,times(0)).getResourceAsStream("/WEB-INF/cancer/consentForms/withdrawalForms.json");

	}
	
	@Test
	public void doGet_will_return_list_of_withdrawals_for_cancer_if_Cancer_is_passed_as_theme() throws Exception {	
		
 		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
		
		HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        
        ServletContext thisContext = mock(ServletContext.class); 
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("resources/cancer/consentForms/withdrawalForms.json");
        when(request.getServletContext()).thenReturn(thisContext);
        when(thisContext.getResourceAsStream("/WEB-INF/cancer/consentForms/withdrawalForms.json")).thenReturn(inStream);
        
        when(request.getSession()).thenReturn(session);        
        when(request.getParameter("theme")).thenReturn("cancer");
         
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
					
		
        new WithdrawalFormLookupService().doGet(request, response);
        
    	
    	 JSONObject withdrawal1 = new JSONObject();
         withdrawal1.put("name","Adult or child participants Version 2.0, dated 20.01.2015");
         withdrawal1.put("id","1");
         
           				    		
         JSONObject withdrawal2 = new JSONObject();
         withdrawal2.put("name","Colsultee Version 2.0, dated 20.01.2015");
         withdrawal2.put("id","2");
          
         JSONArray withdrawals = new JSONArray();
         withdrawals.put(withdrawal1);
         withdrawals.put(withdrawal2);
         
         JSONObject result =new JSONObject();
         result.put("result", withdrawals);
         		    		
 		verify(out,times(1)).print(result.toString());
 		
      
		verify(thisContext,times(0)).getResourceAsStream("/WEB-INF/consentForms/withdrawalForms.json");
		verify(thisContext,times(1)).getResourceAsStream("/WEB-INF/cancer/consentForms/withdrawalForms.json");

	}
	
}