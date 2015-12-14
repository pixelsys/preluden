package ox.softeng.consentservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Servlet implementation class ConsentFormLookupService
 * It returns consentForm lists from WEB-INF/consentForms/consentForms.json
 */
@WebServlet("/ConsentForm/Questions")
public class ConsentQuestionService extends HttpServlet {
    private static final long serialVersionUID = 1L;

    static final Logger LOG = LoggerFactory.getLogger(ConsentQuestionService.class);

    /**
     * @throws java.io.FileNotFoundException
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    public ConsentQuestionService() {
        super();
    }

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext thisContext = request.getServletContext();
        LOG.debug("ConsentForm Questions requested...");
        String fileName = "/WEB-INF/consentForms/consentForms.json";  
        //check for theme name and if not provided, by default Load RareDisease
        String themeType = (String) request.getParameter("theme");
    	if(themeType != null && "cancer".equals(themeType.toLowerCase())){
			 fileName =  "/WEB-INF/cancer/consentForms/consentForms.json";
		}     
        String consentFormName = (String) request.getParameter("consentFormName");
    	if(consentFormName == null || "".equals(consentFormName)){
			throw new ServletException("Please pass the consentFormName");
		}
    	
        InputStream inStream = thisContext.getResourceAsStream(fileName);
        BufferedReader jsonFile = new BufferedReader(new InputStreamReader(inStream));
       
        JSONArray questions = new JSONArray();

        try {
            JSONTokener tokens = new JSONTokener(jsonFile);
            JSONArray consentForms = new JSONArray(tokens);
            
           for(int index = 0; index < consentForms.length(); index++){
        	    JSONObject consentForm = (JSONObject) consentForms.get(index);
        	  
        	    String name =(String) consentForm.get("name"); 
        	    if(name.toLowerCase().equals(consentFormName.toLowerCase())){
        	    	questions =(JSONArray) consentForm.get("stratificationQuestions");
        	    	break;
        	   }
           }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new ServletException("Cannot read the consentForms definition file");
        }

        ServletOutputStream out = response.getOutputStream();
        out.print(questions.toString());
        LOG.debug("ConsentForm Questions responded...");
        return;
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

}
