package ox.softeng.lookupservices;

import org.json.JSONException;
import org.json.JSONObject;
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
import java.util.ArrayList;

/**
 * Servlet implementation class ConsentFormLookupService
 * It returns consentForm lists from WEB-INF/consentForms/consentForms.json
 */
@WebServlet("/lookupServices/ICDLookupService")
public class ICDLookupService extends HttpServlet {
    private static final long serialVersionUID = 1L;

    static final Logger LOG = LoggerFactory.getLogger(ICDLookupService.class);

    /**
     * @throws java.io.FileNotFoundException
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */


    public ICDLookupService() {
        super();
    }

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext thisContext = request.getServletContext();
        LOG.debug("ICD Lookup requested...");

        
        String dataSourceName = (String) request.getParameter("searchInput");
		if(dataSourceName == null){
			return;
		}
		
        String searchInput 		= request.getParameter("searchInput").toUpperCase();
        String searchInputUpper = searchInput.toUpperCase();
        
        ArrayList<String> results = new ArrayList<String>();
        
        InputStream inStream = thisContext.getResourceAsStream("/WEB-INF/ICD10/ICD10_Edition4_GB_20120401.txt");
        BufferedReader csvFileBuffer = new BufferedReader(new InputStreamReader(inStream));
        String line;
        int lineNumber = -1;
		while ((line = csvFileBuffer.readLine()) != null) {
			lineNumber++;
			if(lineNumber == 0){
				continue;
			}
			String[] items = line.split("\t");
			
			if(items.length < 5){
				continue;
			}
			// if it is a code, search in 0,1
			if (searchInputUpper.matches("^[A-Z][0-9]+")) {
				if (items[0].startsWith(searchInputUpper)
						|| items[1].startsWith(searchInputUpper)) {
					String detail = " ";				
					//if it has description columns
					if (items.length >= 6) {
						
						if(items[5].trim().length() > 0){
							detail = ", " + items[5] + " ";
						}
						//if it has second description column
						if(items.length >= 7 && items[6].trim().length() > 0){
							detail = detail.trim() + ", " + items[6].trim() + " ";
						}					
					}
					results.add(items[4] + detail + "(" + items[1] + ")");
				}
			} else {
				if (items[4].toUpperCase().contains(searchInputUpper)) {
					String detail = " ";
					if (items.length >= 6) {
						
						if(items[5].trim().length() > 0){
							detail = ", " + items[5] + " ";
						}						
						if(items.length >= 7 && items[6].trim().length() > 0){
							detail = detail.trim() + ", " + items[6].trim() + " ";
						}					
					}
					results.add(items[4] + detail + "(" + items[1] + ")");
				}
			}
		}

		csvFileBuffer.close();
        ServletOutputStream out = response.getOutputStream();
        JSONObject jResult = new JSONObject();
        try {
        	jResult.put("result", results);
        } catch (JSONException e) {
            LOG.warn(e.getMessage(), e);
        }
        out.print(jResult.toString());
        LOG.debug("Disease Lookup responded");
        return;
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

}
