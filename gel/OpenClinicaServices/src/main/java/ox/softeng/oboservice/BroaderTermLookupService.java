package ox.softeng.oboservice;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.biojava3.ontology.Ontology;
import org.biojava3.ontology.Term;
import org.biojava3.ontology.Triple;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ox.softeng.oboservice.GSonExclusion.ExclBroaderTerms;
import ox.softeng.oboservice.GSonExclusion.ExclNarrowTerms;
import ox.softeng.oboservice.OntologyHandler.LocalTerm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class OBOService
 */
@WebServlet("/OntologyService/BroaderTermLookupService")
public class BroaderTermLookupService extends OntologyServlet {
	private static final long serialVersionUID = 1L;
       
	
    /**
     * @throws FileNotFoundException 
     * @see HttpServlet#HttpServlet()
     */
	
	
    public BroaderTermLookupService() throws FileNotFoundException {
        super();
        
        
      
    }

	@Override
	public void init() throws ServletException {
		super.init();

	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// The super method will ensure the ontology is loaded and cached in the 
		// servlet context
		// It will also set the response type and do some logging.
		LOG.debug("BroaderTermLookupService request started...");
		super.doGet(request, response);
		HttpSession sess = request.getSession();
		
		String dataSourceName = (String) request.getParameter("dataSourceName");
		//if the dataSourceName is not provided
		if(dataSourceName == null){
			return;
		}
			
		String termId = request.getParameter("termId");
		//if termId parameter is not provided
		if(termId == null || termId.equals("")){
			return;
		}
				
		Ontology ontology = (Ontology) sess.getAttribute("ontology_" + dataSourceName);
		//if ontology is not set to session
		if(ontology == null){
			return;
		}
		
		ArrayList<LocalTerm> broaderTerms = null;
		if(isInSessionCache(sess,termId)){
			broaderTerms = getFromSession(sess,termId);
		}	
		else{
			OntologyHandler ontHandler = new OntologyHandler();
			
			//check global cache for ontologyDB
			ServletContext application = getServletConfig().getServletContext();
			HashMap<String,LocalTerm> db = (HashMap<String,LocalTerm>) application.getAttribute("ontologyDB");
			if(db != null){
				ontHandler = new OntologyHandler(db);
			}else{
				ontHandler = new OntologyHandler(ontology);
			}	
			
			broaderTerms = ontHandler.getBroaderTerms(termId);
			addToSessionCache(sess,termId,broaderTerms);
 		}
		
		Gson gson = new GsonBuilder().setExclusionStrategies(new ExclBroaderTerms()).setExclusionStrategies(new ExclNarrowTerms()).create();
		ServletOutputStream out = response.getOutputStream();
		out.print(gson.toJson(broaderTerms));
	}

	private boolean isInSessionCache(HttpSession sess,String termId){
		HashMap<String,ArrayList<LocalTerm>> broaderCache  = (HashMap<String,ArrayList<LocalTerm>>) sess.getAttribute("broaderCache");
		if(broaderCache == null)
			return false;
		return broaderCache.containsKey(termId);
	}
	
	private void addToSessionCache(HttpSession sess,String termId,ArrayList<LocalTerm> broaderTerms){
		HashMap<String,ArrayList<LocalTerm>> broaderCache  = (HashMap<String,ArrayList<LocalTerm>>) sess.getAttribute("broaderCache");
		if(broaderCache == null)
			broaderCache = new HashMap<String,ArrayList<LocalTerm>>();
		broaderCache.put(termId, broaderTerms);	
		sess.setAttribute("broaderCache",broaderCache);
	}
	
	private ArrayList<LocalTerm> getFromSession(HttpSession sess,String termId){
		HashMap<String,ArrayList<LocalTerm>> broaderCache  = (HashMap<String,ArrayList<LocalTerm>>) sess.getAttribute("broaderCache");
		return broaderCache.get(termId);
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}

