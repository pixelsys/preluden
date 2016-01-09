package ox.softeng.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ox.softeng.pdfService.SLFType;
import ox.softeng.pdfService.pdfService;


public class pdfServiceTest {
 	
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
	 
	}
	
	@Test
	public void doGet_will_return_pdf_when_called_by_correct_parameter_list_rare_diseases() throws Exception {	
		
 		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
		
		HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        String participantId = "1234567890";
        
        ServletContext thisContext = mock(ServletContext.class);
        InputStream pdfXSLInStream = this.getClass().getClassLoader().getResourceAsStream("WEB-INF/participantPDFXSL/participant_rare_diseases.xsl");
        String logoFilePath = this.getClass().getClassLoader().getResource("WEB-INF/participantPDFXSL/logo.jpg").getPath();
    	

        when(request.getServletContext()).thenReturn(thisContext);
        when(thisContext.getResourceAsStream("/WEB-INF/participantPDFXSL/participant_rare_diseases.xsl")).thenReturn(pdfXSLInStream);
        when(thisContext.getRealPath("/WEB-INF/participantPDFXSL/logo.jpg")).thenReturn(logoFilePath);

        String pdfFilePath = this.getClass().getClassLoader().getResource("resources/pdf/sample_slf_rd_for_ServiceTest.pdf").getPath();
        
        //Mock reponse.getOutputStream()
        ServletOutputStream oos = new ServletOutputStream() {			
			public void write(int b) throws IOException {}
			
			public void setWriteListener(Object writeListener){}
			
		};
        when(response.getOutputStream()).thenReturn(oos);
        
		
        String queryStr = "participantId="+ participantId +"&nhsNumber=1111111111&surname=SMITH&forenames=JOHN&dateOfBirth=26%2F01%2F1980&clinicId=123&diseaseType=Rare%20Diseases&familyId=12345&hospitalNumber=12345";
        when(request.getSession()).thenReturn(session);        
        when(request.getQueryString()).thenReturn(queryStr);
    	
        
        new pdfService().doGet(request, response);  
				
		verify(response,times(1)).getOutputStream();
		verify(response,times(1)).setContentType("application/pdf");
		verify(response,times(1)).addHeader("Content-Disposition", "attachment; filename=participant" + participantId + "_rare_diseases.pdf");
		//verify(response,times(1)).setContentLength((int) new File(pdfFilePath).length());
	}
	
	
	@Test
    public void doGet_will_return_pdf_when_called_by_correct_parameter_list_cancer_blood() throws Exception {  
        
        HttpServletRequest request   = mock(HttpServletRequest.class);       
        HttpServletResponse response = mock(HttpServletResponse.class);  
        
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        String participantId = "1234567890";
        
        ServletContext thisContext = mock(ServletContext.class);
        InputStream pdfXSLInStream = this.getClass().getClassLoader().getResourceAsStream("WEB-INF/participantPDFXSL/participant_cancer_blood.xsl");
        String logoFilePath = this.getClass().getClassLoader().getResource("WEB-INF/participantPDFXSL/logo.jpg").getPath();
            

        when(request.getServletContext()).thenReturn(thisContext);
        when(thisContext.getResourceAsStream("/WEB-INF/participantPDFXSL/participant_cancer_blood.xsl")).thenReturn(pdfXSLInStream);
        when(thisContext.getRealPath("/WEB-INF/participantPDFXSL/logo.jpg")).thenReturn(logoFilePath);

        String pdfFilePath = this.getClass().getClassLoader().getResource("resources/pdf/sample_slf_cancer_blood_for_ServiceTest.pdf").getPath();
        
        //Mock reponse.getOutputStream()
        ServletOutputStream oos = new ServletOutputStream() {           
            public void write(int b) throws IOException {}
            
            public void setWriteListener(Object writeListener){}
            
        };
        when(response.getOutputStream()).thenReturn(oos);
        
        
        String queryStr = "participantId="+ participantId +"&nhsNumber=1111111111&surname=SMITH&forenames=JOHN&dateOfBirth=26%2F01%2F1980&clinicId=123&diseaseType=Cancer&familyId=12345&hospitalNumber=12345&slfType="+SLFType.CANCER_BLOOD.name();
        when(request.getSession()).thenReturn(session);        
        when(request.getQueryString()).thenReturn(queryStr);
        
        
        new pdfService().doGet(request, response);  
                
        verify(response,times(1)).getOutputStream();
    
        verify(response,times(1)).setContentType("application/pdf");
        verify(response,times(1)).addHeader("Content-Disposition", "attachment; filename=participant" + participantId + "_cancer_blood.pdf");
        //verify(response,times(1)).setContentLength((int) new File(pdfFilePath).length());
    }
	
	
	@Test
    public void doGet_will_return_pdf_when_called_by_correct_parameter_list_cancer_tissue() throws Exception {  
        
        HttpServletRequest request   = mock(HttpServletRequest.class);       
        HttpServletResponse response = mock(HttpServletResponse.class);  
        
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        
        String participantId = "1234567890";
        
        ServletContext thisContext = mock(ServletContext.class);
        InputStream pdfXSLInStream = this.getClass().getClassLoader().getResourceAsStream("WEB-INF/participantPDFXSL/participant_cancer_tissue.xsl");
        String logoFilePath = this.getClass().getClassLoader().getResource("WEB-INF/participantPDFXSL/logo.jpg").getPath();
        

        when(request.getServletContext()).thenReturn(thisContext);
        when(thisContext.getResourceAsStream("/WEB-INF/participantPDFXSL/participant_cancer_tissue.xsl")).thenReturn(pdfXSLInStream);
        when(thisContext.getRealPath("/WEB-INF/participantPDFXSL/logo.jpg")).thenReturn(logoFilePath);

        String pdfFilePath = this.getClass().getClassLoader().getResource("resources/pdf/sample_slf_cancer_tissue_for_ServiceTest.pdf").getPath();
        
        //Mock reponse.getOutputStream()
        ServletOutputStream oos = new ServletOutputStream() {           
            public void write(int b) throws IOException {}
            
            public void setWriteListener(Object writeListener){}
            
        };
        when(response.getOutputStream()).thenReturn(oos);
        
        
        String queryStr = "participantId="+ participantId +"&nhsNumber=1111111111&surname=SMITH&forenames=JOHN&dateOfBirth=26%2F01%2F1980&clinicId=123&diseaseType=Cancer&familyId=12345&hospitalNumber=12345&hospitalSiteCode=SIC133&slfType="+SLFType.CANCER_TISSUE.name();
        when(request.getSession()).thenReturn(session);        
        when(request.getQueryString()).thenReturn(queryStr);
        
        
        new pdfService().doGet(request, response);  
                
        verify(response,times(1)).getOutputStream();
    
        verify(response,times(1)).setContentType("application/pdf");
        verify(response,times(1)).addHeader("Content-Disposition", "attachment; filename=participant" + participantId + "_cancer_tissue.pdf");
        //verify(response,times(1)).setContentLength((int) new File(pdfFilePath).length());
    }
	
	
	@Test
	public void decodeParameters_will_decode_the_parameters() throws Exception {	
		      
        String queryStr = 
        		"participantId="+ URLEncoder.encode("1234567890", "UTF-8") +
        		"&nhsNumber="+ URLEncoder.encode("1234567890", "UTF-8") +        		
        		"&surname="+ URLEncoder.encode("SMITH'Hi'=%", "UTF-8") +
        		"&forenames="+ URLEncoder.encode("JOHN", "UTF-8") +
        		"&dateOfBirth="+URLEncoder.encode("26/01/1980", "UTF-8")+
        		"&clinicId="+URLEncoder.encode("123%20=", "UTF-8") +
        		"&diseaseType="+URLEncoder.encode("Rare / / \\ \\ & & * * Diseases", "UTF-8") +
        		"&familyId="+URLEncoder.encode("12345", "UTF-8") +
        		"&hospitalNumber="+URLEncoder.encode("12345", "UTF-8");
        
        pdfService pdfService = new pdfService();
        HashMap<String,String> params = pdfService.decodeParameters(queryStr);
        
        assertEquals(params.get("participantId"), "1234567890");
        assertEquals(params.get("surname"), "SMITH'Hi'=%");
        assertEquals(params.get("diseaseType"), "Rare / / \\ \\ & & * * Diseases");
        assertEquals(params.get("clinicId"), "123%20=");        
        assertEquals(params.size(), 9);
	}
	
	
	
	@Test
	public void doGet_will_throw_exception_when_dateOfBirth_is_after_today() throws Exception {	
		
		
		expectedEx.expect(ServletException.class);
	    expectedEx.expectMessage("DateOfBirth is not valid!");
	    
	    
 		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
		
		HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
       
        //Mock reponse.getOutputStream()
        ServletOutputStream oos = new ServletOutputStream() {			
			public void write(int b) throws IOException {}
		};
        when(response.getOutputStream()).thenReturn(oos);
             
        Date tomorrow = new Date(new Date().getTime() + (1000 * 60 * 60 * 24));
        DateFormat df = new SimpleDateFormat(pdfService.DATE_OF_BIRTH_FORMAT);
 	    String tomorrowStr = df.format(tomorrow);
	    		 
        String queryStr = "participantId=1234567890&nhsNumber=1111111111&surname=SMITH&forenames=JOHN&dateOfBirth="+tomorrowStr+"&clinicId=123&diseaseType=Rare%20Diseases&familyId=12345&hospitalNumber=12345";
        when(request.getSession()).thenReturn(session);        
        when(request.getQueryString()).thenReturn(queryStr);
        
        new pdfService().doGet(request, response);  
				
		verify(response,times(0)).getOutputStream();
		verify(response,times(0)).setContentType("application/pdf");
	}
	
	@Test
	public void doGet_will_throw_exception_when_dateOfBirth_is_not_in_proper_format() throws Exception {	
		
		
		expectedEx.expect(ServletException.class);
	    expectedEx.expectMessage("Invalid format in dateOfBirth, we expect '" + pdfService.DATE_OF_BIRTH_FORMAT + "'.");
	    
	    
 		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
		
		HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
       
        //Mock reponse.getOutputStream()
        ServletOutputStream oos = new ServletOutputStream() {			
			public void write(int b) throws IOException {}
		};
        when(response.getOutputStream()).thenReturn(oos);
             

 	    String tomorrowStr = "22-dec-2015";
	    		 
        String queryStr = "participantId=123&nhsNumber=1111111111&surname=SMITH&forenames=JOHN&dateOfBirth="+tomorrowStr+"&clinicId=123&diseaseType=Rare%20Diseases&familyId=12345&hospitalNumber=12345";
        when(request.getSession()).thenReturn(session);        
        when(request.getQueryString()).thenReturn(queryStr);
    	        
        new pdfService().doGet(request, response);  
				
		verify(response,times(0)).getOutputStream();
		verify(response,times(0)).setContentType("application/pdf");
	}
	
	@Test
	public void doGet_will_throw_exception_when_dateOfBirth_is_empty() throws Exception {	
		

		expectedEx.expect(ServletException.class);
	    expectedEx.expectMessage("Please pass the dateOfBirth");
	    
 		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
		
		HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
       
        //Mock reponse.getOutputStream()
        ServletOutputStream oos = new ServletOutputStream() {			
			public void write(int b) throws IOException {}
		};
        when(response.getOutputStream()).thenReturn(oos);
             
      
	    		 
        String queryStr = "participantId=1234567890&nhsNumber=1111111111&surname=SMITH&forenames=JOHN&clinicId=123&diseaseType=Rare%20Diseases&familyId=12345&hospitalNumber=12345";
        when(request.getSession()).thenReturn(session);        
        when(request.getQueryString()).thenReturn(queryStr);
    	        
        new pdfService().doGet(request, response);  
				
		verify(response,times(0)).getOutputStream();
		verify(response,times(0)).setContentType("application/pdf");
	}
	
	
	
	@Test
	public void doGet_will_throw_exception_when_nhsNumber_is_not_valid() throws Exception {	
		


		expectedEx.expect(ServletException.class);
	    expectedEx.expectMessage("NHS number is not valid!");
	    
	    
 		HttpServletRequest request   = mock(HttpServletRequest.class);       
	    HttpServletResponse response = mock(HttpServletResponse.class);  
		
		HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
       
        //Mock reponse.getOutputStream()
        ServletOutputStream oos = new ServletOutputStream() {			
			public void write(int b) throws IOException {}
		};
        when(response.getOutputStream()).thenReturn(oos);
             

 	    String nhsString = "123";
	    		 
        String queryStr = "participantId=1234567890&nhsNumber="+ nhsString +"&surname=SMITH&forenames=JOHN&dateOfBirth=22/01/2015&clinicId=123&diseaseType=Rare%20Diseases&familyId=12345&hospitalNumber=12345";
        when(request.getSession()).thenReturn(session);        
        when(request.getQueryString()).thenReturn(queryStr);
    	        
        new pdfService().doGet(request, response);  
				
		verify(response,times(0)).getOutputStream();
		verify(response,times(0)).setContentType("application/pdf");
	}
	
	
}