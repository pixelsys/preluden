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
 * Servlet implementation class ConsentFormLookupService
 */
@WebServlet("/lookupServices/GeneScopeList")
public class GeneScopeListService extends HttpServlet {
    private static final long serialVersionUID = 1L;

    static final Logger LOG = LoggerFactory.getLogger(GeneScopeListService.class);

    /**
     * @throws java.io.FileNotFoundException
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */


    public GeneScopeListService() {
        super();
    }

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext thisContext = request.getServletContext();
        LOG.debug("GeneScopeList requested...");

        
        String fileName = "/WEB-INF/rare-diseases/GeneScopeList.json";  
        //check for theme name and if not provided, by default Load RareDisease
        String themeType = (String) request.getParameter("theme");
    	if(themeType != null && "cancer".equals(themeType.toLowerCase())){
			 fileName =  "/WEB-INF/cancer/GeneScopeList.json";
		}     
    	
        InputStream inStream = thisContext.getResourceAsStream(fileName);
        BufferedReader jsonFile = new BufferedReader(new InputStreamReader(inStream));
        JSONArray geneScopeList;

        try {
            JSONTokener tokens = new JSONTokener(jsonFile);
            geneScopeList = new JSONArray(tokens);
        } catch (JSONException e) {
            LOG.error("Error: " + e.getMessage(), e);
            throw new ServletException("Cannot read Gene Scope List definition file");
        }

        ServletOutputStream out = response.getOutputStream();
        JSONObject jResult = new JSONObject();
        try {
            jResult.put("result", geneScopeList);
        } catch (JSONException e) {
            LOG.warn(e.getMessage(), e);
        }
        out.print(jResult.toString());
        LOG.debug("GeneScopeList responded");
        return;        
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

}
