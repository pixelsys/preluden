package ox.softeng.lookupservices;

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
 * Servlet implementation class PISLookupService
 * It returns consentForm lists from WEB-INF/consentForms/PIS.json
 */
@WebServlet("/lookupServices/PIS")
public class PISLookupService extends HttpServlet {
    private static final long serialVersionUID = 1L;

    static final Logger LOG = LoggerFactory.getLogger(PISLookupService.class);

    /**
     * @throws java.io.FileNotFoundException
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */


    public PISLookupService() {
        super();
    }

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext thisContext = request.getServletContext();
        LOG.debug("PIS Lookup requested...");

        
        
        
        
        String fileName = "/WEB-INF/consentForms/PIS.json";  
        //check for theme name and if not provided, by default Load RareDisease
        String themeType = (String) request.getParameter("theme");
    	if(themeType != null && "cancer".equals(themeType.toLowerCase())){
			 fileName =  "/WEB-INF/cancer/consentForms/PIS.json";
		}     
    	
        InputStream inStream = thisContext.getResourceAsStream(fileName);
        BufferedReader jsonFile = new BufferedReader(new InputStreamReader(inStream));
        JSONArray consentForms;

        try {
            JSONTokener tokens = new JSONTokener(jsonFile);
            consentForms = new JSONArray(tokens);
        } catch (JSONException e) {
            LOG.error("error: " + e.getMessage(), e);
            throw new ServletException("Cannot read the participant information definition file");
        }

        ServletOutputStream out = response.getOutputStream();
        JSONObject jResult = new JSONObject();
        try {
            jResult.put("result", consentForms);
        } catch (JSONException e) {
            LOG.warn(e.getMessage());
        }
        out.print(jResult.toString());
        LOG.debug("PIS Lookup responded");
        return;
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

}
