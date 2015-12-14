package ox.softeng.test;

import static org.junit.Assert.assertNotNull;

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

import ox.softeng.lookupservices.ICDLookupService;
import ox.softeng.oboservice.LookupService;
import static org.mockito.Mockito.*;

public class ICDLookupServiceTest {

	String dataSourceName = "hp.obo";
	HttpServletRequest request = mock(HttpServletRequest.class);
	HttpServletResponse response = mock(HttpServletResponse.class);

	private ICDLookupService servlet;

	@Before
	public void setUp() throws Exception {

		// partial mock the servlet, as we need to mock the applicationContext
		// which is part of the main servlet
		servlet = spy(new ICDLookupService());
		ServletConfig config = mock(ServletConfig.class);
		when(servlet.getServletConfig()).thenReturn(config);

		ServletContext application = mock(ServletContext.class);
		when(config.getServletContext()).thenReturn(application);

		ServletContext thisContext = mock(ServletContext.class);
		InputStream inStream = this.getClass().getClassLoader()
				.getResourceAsStream("resources/ICD10_Test_Resource.txt");
		when(request.getServletContext()).thenReturn(thisContext);
		
		when(thisContext.getResourceAsStream("/WEB-INF/ICD10/ICD10_Edition4_GB_20120401.txt"))
				.thenReturn(inStream);

		HttpSession session = mock(HttpSession.class);
		when(request.getSession()).thenReturn(session);
	}

	@Test
	public void ICDLookupService_returns_matched_result_for_ICD_Code() throws Exception {

		when(request.getParameter("searchInput")).thenReturn("A01");
		ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);

		servlet.doGet(request, response);

		assertNotNull(response.getOutputStream());
		String expected = "{\"result\":[\"Typhoid and paratyphoid fevers (A01)\",\"Typhoid fever (A010)\",\"Paratyphoid fever A (A011)\",\"Paratyphoid fever B (A012)\",\"Paratyphoid fever C (A013)\",\"Paratyphoid fever, unspecified (A014)\"]}";
	    
		verify(out).print(expected);
	}
	
	@Test
	public void ICDLookupService_returns_matched_result_for_Diagnosis_Text() throws Exception {

		when(request.getParameter("searchInput")).thenReturn("Vibrio");
		ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);

		servlet.doGet(request, response);
		
		assertNotNull(response.getOutputStream());
		String expected = "{\"result\":[\"Cholera due to Vibrio cholerae 01, biovar cholerae (A000)\",\"Cholera due to Vibrio cholerae 01, biovar eltor (A001)\"]}";
		verify(out).print(expected);
	}
	
	@Test
	public void ICDLookupService_returns_empty_array_when_can_NOT_find_result() throws Exception {

		when(request.getParameter("searchInput")).thenReturn("NOT_AVAILABLE_VALUE");
		ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);

		servlet.doGet(request, response);
		
		assertNotNull(response.getOutputStream());
		String expected = "{\"result\":[]}";
		verify(out).print(expected);
	}
	
	@Test
	public void ICDLookupService_will_ignores_rows_having_less_than_four_items() throws Exception {

		when(request.getParameter("searchInput")).thenReturn("A09");
		ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);

		servlet.doGet(request, response);

		assertNotNull(response.getOutputStream());
			 
		String expected = "{\"result\":[\"A, Simple_Desc1, Simple_Desc2 (A09)\",\"A, Simple_Desc2 (A09)\"]}";
		
		verify(out).print(expected);
	}
	
	
	@Test
	public void ICDLookupService_returns_first_and_second_descriptions_of_the_item() throws Exception {

		when(request.getParameter("searchInput")).thenReturn("A01");
		ServletOutputStream out = mock(ServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(out);

		servlet.doGet(request, response);

		assertNotNull(response.getOutputStream());
		
				
		String expected = "{\"result\":[\"Typhoid and paratyphoid fevers (A01)\",\"Typhoid fever (A010)\",\"Paratyphoid fever A (A011)\",\"Paratyphoid fever B (A012)\",\"Paratyphoid fever C (A013)\",\"Paratyphoid fever, unspecified (A014)\"]}";
	    
		verify(out).print(expected);
	}
}
