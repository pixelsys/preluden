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
 * It returns consentForm lists from WEB-INF/consentForms/consentForms.json
 */
@WebServlet("/lookupServices/DiseaseLookup")
public class DiseaseLookupService extends HttpServlet {
    private static final long serialVersionUID = 1L;

    static final Logger LOG = LoggerFactory.getLogger(DiseaseLookupService.class);

    /**
     * @throws java.io.FileNotFoundException
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */


    public DiseaseLookupService() {
        super();
    }

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext thisContext = request.getServletContext();
        LOG.debug("Disease Lookup requested...");

        // We'll just return all the diseases in a big tree.
        // String diseaseGroup = request.getParameter("diseaseGroup");
        // String subGroup = request.getParameter("subGroup");
        // String specificDisease = request.getParameter("diseaseGroup");
        
        
        InputStream inStream = thisContext.getResourceAsStream("/WEB-INF/rare-diseases/DiseaseOntology.json");
        BufferedReader jsonFile = new BufferedReader(new InputStreamReader(inStream));
        JSONObject diseaseOntology;
        JSONArray ret = new JSONArray();
        try {
            JSONTokener tokens = new JSONTokener(jsonFile);
            diseaseOntology = new JSONObject(tokens);
            JSONArray diseaseGroups = diseaseOntology.getJSONArray("DiseaseGroups");
            for(int dgidx = 0; dgidx < diseaseGroups.length(); dgidx++)
            {
            	JSONObject thisDiseaseGroup = diseaseGroups.getJSONObject(dgidx);
            	JSONObject dg=new JSONObject();
            	dg.put("name", thisDiseaseGroup.getString("name"));
            	dg.put("id", thisDiseaseGroup.getString("id"));
                JSONArray subGroups = thisDiseaseGroup.getJSONArray("subGroups");
                JSONArray newSubGroupArray = new JSONArray();
                for(int sgidx = 0; sgidx < subGroups.length(); sgidx++)
                {
                	JSONObject thisSubGroup = subGroups.getJSONObject(sgidx);
                	JSONObject sg=new JSONObject();
                	sg.put("name", thisSubGroup.getString("name"));
                	sg.put("id", thisSubGroup.getString("id"));
                    JSONArray specificDisorders = thisSubGroup.getJSONArray("specificDisorders");
                    JSONArray newSpecificDisorderArray = new JSONArray();
                    for(int sdidx = 0; sdidx < specificDisorders.length(); sdidx++)
                    {
                    	JSONObject thisSpecificDisorder = specificDisorders.getJSONObject(sdidx);
                    	JSONObject sd=new JSONObject();
                    	sd.put("name", thisSpecificDisorder.getString("name"));
                    	sd.put("id", thisSpecificDisorder.getString("id"));
                    	sd.put("eligibilityQuestion", thisSpecificDisorder.getJSONObject("eligibilityQuestion"));
                    	newSpecificDisorderArray.put(sd);
                    }
                    sg.put("specificDisorders", newSpecificDisorderArray);
                    newSubGroupArray.put(sg);
                }
                dg.put("subGroups", newSubGroupArray);
                ret.put(dg);
            }
            
        } catch (JSONException e) {
            LOG.error("Error: " + e.getMessage(), e);
            throw new ServletException("Cannot read the disease ontology definition file");
        }

        ServletOutputStream out = response.getOutputStream();
        JSONObject jResult = new JSONObject();
        try {
        	jResult.put("result", ret);
        } catch (JSONException e) {
        	LOG.warn("Error: " + e.getMessage(), e);
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
