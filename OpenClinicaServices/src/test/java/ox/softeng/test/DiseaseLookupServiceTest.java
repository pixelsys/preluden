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

import ox.softeng.lookupservices.DiseaseLookupService;


public class DiseaseLookupServiceTest {
 	
	@Before
	public void setUp() throws Exception {
	 
	}
	
	
	
	@Test
	public void doGet_will_return_all_disease_hierarchy() throws Exception {	
		
 		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
		
		HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        
        ServletContext thisContext = mock(ServletContext.class); 
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("resources/rarediseases/DiseaseOntology.json");
        when(request.getServletContext()).thenReturn(thisContext);
        when(thisContext.getResourceAsStream("/WEB-INF/rare-diseases/DiseaseOntology.json")).thenReturn(inStream);
        
               
        ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);
		
		
        new DiseaseLookupService().doGet(request, response);
                
        
        //Build the expected result
        JSONObject shallowPhenotype = new JSONObject();
        shallowPhenotype.put("id","HP:0001634");
        shallowPhenotype.put("name","Mitral valve prolapse");        
        
        JSONObject eligibilityQuestion = new JSONObject();
        eligibilityQuestion.put("date","30/04/2015");
        eligibilityQuestion.put("version", "1");
                                
        JSONObject DiseaseDisorder = new JSONObject();
        DiseaseDisorder.put("id", "11021" );
        DiseaseDisorder.put("name", "Familial Thoracic Aortic Aneurysm Disease" );
        DiseaseDisorder.put("eligibilityQuestion", eligibilityQuestion);
                
        
        JSONObject subGroup = new JSONObject();
        subGroup.put("id", "10951");
        subGroup.put("name","Connective Tissues Disorders and Aortopathies");
        subGroup.put("specificDisorders",new JSONArray().put(DiseaseDisorder));
        
                
        JSONObject DiseaseGroup = new JSONObject();
        DiseaseGroup.put("id", "10950");
        DiseaseGroup.put("name", "Cardiovascular disorders");
        DiseaseGroup.put("subGroups",new JSONArray().put(subGroup));

        	
        JSONObject expected = new JSONObject();
        expected.put("result", new JSONArray().put(DiseaseGroup)); 
        
		verify(out,times(1)).print(expected.toString());
		verify(thisContext,times(1)).getResourceAsStream("/WEB-INF/rare-diseases/DiseaseOntology.json");
	}
	
	
}



