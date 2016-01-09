package ox.softeng.oboservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.biojava3.ontology.Ontology;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ox.softeng.oboservice.GSonExclusion.ExclBroaderTerms;
import ox.softeng.oboservice.GSonExclusion.ExclDefAndCommentTerms;
import ox.softeng.oboservice.GSonExclusion.ExclNarrowTerms;
import ox.softeng.oboservice.OntologyHandler.LocalTerm;

/**
 * Servlet implementation class PredefinedTermsService
 */
@WebServlet("/OntologyService/PredefinedTermsService")
public class PredefinedTermsService extends OntologyServlet {
	private static final long serialVersionUID = 1L;
    
	static final Logger LOG = LoggerFactory.getLogger(PredefinedTermsService.class);
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PredefinedTermsService() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.debug("PredefinedTermService request started...");
		super.doGet(request, response);

		ServletContext thisContext = request.getServletContext();

		// Get the name of the datasource
		String predefinedTermsSourceName = (String) request.getParameter("predefinedTermsSourceName");

		// If we don't have it, barf
		if(predefinedTermsSourceName == null || "".equals(predefinedTermsSourceName))
		{
			throw new ServletException("Please pass the predefinedTermsSourceName");
		}


 		String diseaseGroupName = (String) request.getParameter("diseaseGroup");
		// If we don't have it
		if(diseaseGroupName == null || "".equals(diseaseGroupName))
		{
			throw new ServletException("Please pass the diseasegroup");
		}		
 		String subGroupName = (String) request.getParameter("subGroup");
		// If we don't have it
		if(subGroupName == null || "".equals(subGroupName))
		{
			throw new ServletException("Please pass the subGroup");
		}		
		String specificDisorderName = (String) request.getParameter("specificDisorder");
		// If we don't have it
		if(specificDisorderName == null || "".equals(specificDisorderName))
		{
			throw new ServletException("Please pass the specificDisorder");
		}

				
		InputStream inStream = thisContext.getResourceAsStream("/WEB-INF/rare-diseases/" + predefinedTermsSourceName);
		BufferedReader jsonFile = new BufferedReader ( new InputStreamReader ( inStream ) );
		JSONObject diseasegroup;
		JSONArray diseaseroupsArray;
		try {
			JSONTokener tokener = new JSONTokener(jsonFile);
			diseasegroup = new JSONObject(tokener);
			diseaseroupsArray = diseasegroup.getJSONArray("DiseaseGroups");
		} catch (JSONException e) {
			LOG.error("Error: " + e.getMessage(), e);
			throw new ServletException("Cannot read the definition file");
			
		}
		try {
				for(int groupIndex=0;groupIndex<diseaseroupsArray.length() ;groupIndex++){
					JSONObject group =  (JSONObject)(diseaseroupsArray.get(groupIndex));
					if(!group.getString("name").equals(diseaseGroupName)){
						continue;
					}
					
					JSONArray subGroupsArray = group.getJSONArray("subGroups");
					for(int subGroupIndex=0;subGroupIndex<subGroupsArray.length() ;subGroupIndex++){
						JSONObject subGroup =  (JSONObject)(subGroupsArray.get(subGroupIndex));
	
						if(!subGroup.getString("name").equals(subGroupName)){
							continue;
						}
						JSONArray specificDisorderArray = subGroup.getJSONArray("specificDisorders");
						for(int disorderIndex=0;disorderIndex<specificDisorderArray.length() ;disorderIndex++){
							JSONObject disorder =  (JSONObject)(specificDisorderArray.get(disorderIndex));
	
							if(!disorder.getString("name").equals(specificDisorderName)){
								continue;
							}	
							//NOW WE FOUND IF!
							JSONArray phenotypes = disorder.getJSONArray("shallowPhenotypes");
							for(int ptIndex=0;ptIndex<phenotypes.length();ptIndex++){
								
								String loadNarrow = request.getParameter("loadNarrowTermsTreeFlat");
								String dataSourceName = (String) request.getParameter("dataSourceName");
	
								//if loadNarrower is not passed, just return the terms
								if(loadNarrow == null || "".equals(loadNarrow) || 
								   dataSourceName == null || "".equals(dataSourceName)){
									response.getOutputStream().print(phenotypes.toString());
									return;
								}						
								
								HttpSession sess = request.getSession();
								Ontology ontology = (Ontology) sess.getAttribute("ontology_" + dataSourceName);
								//if ontology is not set to session						
								if(ontology == null){
									response.getOutputStream().print(phenotypes.toString());
									return;
								}	
								
								ArrayList<PredefinedTerm> result = new ArrayList<PredefinedTerm>();
								
								//get handler from main App cache
								OntologyHandler handler = getOntologyHandlerFromCache(ontology);
								
								for (int t = 0 ; t < phenotypes.length() ; t++){
									JSONObject term = (JSONObject) phenotypes.get(t);
									
									//if there are more than 1000 narrow terms for this element, then
									//just return 4 level of it (breadth-first)
									ArrayList<LocalTerm> narrowTerms = handler.getNarrowTermsTreeAsFlatList((String)(term.get("id")));
									if(narrowTerms.size() > 1000) {
										ArrayList<LocalTerm> narrowTerms4Level = new ArrayList<LocalTerm>();
										
										for(int l = 0; l < narrowTerms.size(); l++){
											LocalTerm lTerm = narrowTerms.get(l);
											if(lTerm.depthLevel  < 4 )
												narrowTerms4Level.add(lTerm);
											
										}
										narrowTerms = narrowTerms4Level;
									}
									
									PredefinedTerm pTerm = new PredefinedTerm((String)term.get("id"),(String)term.get("name"),narrowTerms);
									result.add(pTerm);													
								}
								
								ServletOutputStream out = response.getOutputStream();
								Gson gson = new GsonBuilder().setExclusionStrategies(new ExclBroaderTerms()).create();
								out.print(gson.toJson(result));	
								return;				
								
						}
					} 				
				}
			}		
				
		} catch (JSONException e) {
			LOG.error("Error: " + e.getMessage(), e);
			throw new ServletException("Cannot parse the definition file");
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	private OntologyHandler getOntologyHandlerFromCache(Ontology ontology){	
		
		OntologyHandler ontHandler = new OntologyHandler();

		//check global cache for ontologyDB
		ServletContext application = getServletConfig().getServletContext();
		HashMap<String,LocalTerm> db = (HashMap<String,LocalTerm>) application.getAttribute("ontologyDB");
		if(db != null){
			ontHandler = new OntologyHandler(db);
		}else{
			//if it is not in global cache, then load it again!
			ontHandler = new OntologyHandler(ontology);
		}			
		return ontHandler;		
	}


	public class PredefinedTerm {
		public String id;
		public String name;
		public ArrayList<LocalTerm> narrowTerms = new ArrayList<LocalTerm>();
		
		public PredefinedTerm(String id,String name,ArrayList<LocalTerm> narrowTerms){
			this.id = id;
			this.name = name;
			this.narrowTerms = narrowTerms;
		}
	}
	
}
