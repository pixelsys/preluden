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
@WebServlet("/lookupServices/WithdrawalForm")
public class WithdrawalFormLookupService extends HttpServlet {
    private static final long serialVersionUID = 1L;

    static final Logger LOG = LoggerFactory.getLogger(WithdrawalFormLookupService.class);
    
    public WithdrawalFormLookupService() {
        super();
    }

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext thisContext = request.getServletContext();
        LOG.debug("WithdrawalForm Lookup requested...");

        
       
        String fileName = "/WEB-INF/consentForms/withdrawalForms.json";
        //check for theme name and if not provided, by default Load RareDisease
        String themeType = (String) request.getParameter("theme");
    	if(themeType != null && "cancer".equals(themeType.toLowerCase())){
			 fileName =  "/WEB-INF/cancer/consentForms/withdrawalForms.json";
		}       
    	
        InputStream inStream = thisContext.getResourceAsStream(fileName);
        BufferedReader jsonFile = new BufferedReader(new InputStreamReader(inStream));
        JSONArray consentForms;

        try {
            JSONTokener tokens = new JSONTokener(jsonFile);
            consentForms = new JSONArray(tokens);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new ServletException("Cannot read the withdrawalForms definition file");
        }

        ServletOutputStream out = response.getOutputStream();
        JSONObject jResult = new JSONObject();
        try {
            jResult.put("result", consentForms);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        out.print(jResult.toString());
        LOG.debug("WithdrawalForm Lookup responded");
        return;
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

}
