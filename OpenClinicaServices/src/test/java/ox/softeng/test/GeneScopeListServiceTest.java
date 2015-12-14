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

import ox.softeng.lookupservices.GeneScopeListService;

public class GeneScopeListServiceTest {
 	
	@Before
	public void setUp() throws Exception {
	 
	}
	
	@Test
	public void doGet_will_return_list_of_geneScope_list_for_rareDiseases_when_no_theme_is_passed() throws Exception {	
		
 		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
		
		HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        
        ServletContext thisContext = mock(ServletContext.class); 
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("resources/rarediseases/GeneScopeList.json");
        when(request.getServletContext()).thenReturn(thisContext);
        when(thisContext.getResourceAsStream("/WEB-INF/rare-diseases/GeneScopeList.json")).thenReturn(inStream);
        
        when(request.getSession()).thenReturn(session);        
        //when(request.getParameter("theme")).thenReturn("cancer");

         
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
		
		new GeneScopeListService().doGet(request, response);       
        
		
        JSONObject questions = new JSONObject();
        questions.put("result",new JSONArray());
		verify(out,times(1)).print(questions.toString());
		
		verify(thisContext,times(1)).getResourceAsStream("/WEB-INF/rare-diseases/GeneScopeList.json");
		verify(thisContext,times(0)).getResourceAsStream("/WEB-INF/cancer/GeneScopeList.json");
	}		
	
	
	@Test
	public void doGet_will_return_list_of_geneScope_for_cancer_if_Cancer_is_passed_as_theme() throws Exception {	
		
 		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
		
		HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        
        ServletContext thisContext = mock(ServletContext.class); 
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("resources/cancer/GeneScopeList.json");
        when(request.getServletContext()).thenReturn(thisContext);
        when(thisContext.getResourceAsStream("/WEB-INF/cancer/GeneScopeList.json")).thenReturn(inStream);
        
        when(request.getSession()).thenReturn(session);        
        when(request.getParameter("theme")).thenReturn("cancer");
         
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
		
					
        new GeneScopeListService().doGet(request, response);
                
                
        JSONArray geneScopeList = new JSONArray();
        geneScopeList.put("A");
        geneScopeList.put("B");
        geneScopeList.put("C");
        
        JSONObject result =new JSONObject();
        result.put("result", geneScopeList);
        		    		
		verify(out,times(1)).print(result.toString());
		verify(thisContext,times(0)).getResourceAsStream("/WEB-INF/rare-diseases/GeneScopeList.json");
		verify(thisContext,times(1)).getResourceAsStream("/WEB-INF/cancer/GeneScopeList.json");

	}
}