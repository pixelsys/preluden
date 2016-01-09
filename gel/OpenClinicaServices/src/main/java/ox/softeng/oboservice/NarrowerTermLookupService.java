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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ox.softeng.oboservice.GSonExclusion.ExclBroaderTerms;
import ox.softeng.oboservice.GSonExclusion.ExclDefAndCommentTerms;
import ox.softeng.oboservice.GSonExclusion.ExclNarrowTerms;
import ox.softeng.oboservice.OntologyHandler.LocalTerm;

import com.google.gson.*;

/**
 * Servlet implementation class OBOService
 */
@WebServlet("/OntologyService/NarrowerTermLookupService")
public class NarrowerTermLookupService extends OntologyServlet {
	private static final long serialVersionUID = 1L;
   
	
    /**
     * @throws FileNotFoundException 
     * @see HttpServlet#HttpServlet()
     */
	
	
    public NarrowerTermLookupService() throws FileNotFoundException {
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
		LOG.debug("NarrowerTermLookupService request started...");
		super.doGet(request, response);
		HttpSession sess = request.getSession();
		
		
		String dataSourceName = (String) request.getParameter("dataSourceName");
		//if the dataSourceName is not provided
		if(dataSourceName == null){
			return;
		}
			
		ServletOutputStream out = response.getOutputStream();
		String termId = request.getParameter("termId");
		//if termId parameter is not provided
		if(termId == null || termId.equals("")){
			return;
		}
		
		String narrowTree = request.getParameter("tree");
		String flat = request.getParameter("flat");
		Ontology ontology = (Ontology) sess.getAttribute("ontology_" + dataSourceName);
		//if ontology is not set to session						
		if(ontology == null){
			return;
		}	
			

		
		OntologyHandler ontHandler = new OntologyHandler();
		//if narrowTree is not provided or if it is false
		//just return the list of narrow terms
		//otherwise return the tree structure of the narrow terms
		if(narrowTree==null || narrowTree.equals("false")){		
			
			ArrayList<LocalTerm> narrowTerms = null;

			if(isInSessionCache(sess,termId,"narrowerCache")){
				narrowTerms =(ArrayList<LocalTerm>)	getFromSession(sess,termId,"narrowerCache");
			}else{
								
				//check global cache for ontologyDB
				ontHandler = getOntologyHandlerFromCache(ontology);	
				narrowTerms =(ArrayList<LocalTerm>) ontHandler.getNarrowTerms(termId).clone();
				addToSessionCache(sess,termId,narrowTerms,"narrowerCache");
			}
			//as we just need the list of narrowTerms, and we are not interested in narrow/broad terms related to each narow result
			//so we exclude them
			Gson gson = new GsonBuilder().setExclusionStrategies(new ExclBroaderTerms()).setExclusionStrategies(new ExclNarrowTerms()).create();
			out.print(gson.toJson(narrowTerms));				
			return;
		}else{			
			if(flat==null || flat.equals("false")){	
				
				LocalTerm localTerm = null;
				if(isInSessionCache(sess,termId,"narrowerTreeCache")){
					localTerm = (LocalTerm) getFromSession(sess,termId,"narrowerTreeCache");
				}else{
					//check global cache for ontologyDB
					ontHandler = getOntologyHandlerFromCache(ontology);					
					localTerm = ontHandler.getNarrowTermsTree(termId);
					addToSessionCache(sess,termId,localTerm,"narrowerTreeCache");
				}
 				Gson gson = new GsonBuilder().setExclusionStrategies(new ExclBroaderTerms()).setExclusionStrategies(new ExclDefAndCommentTerms()).create();	
				out.print(gson.toJson(localTerm));
				return;
			}else{
				ArrayList<LocalTerm> flatList = null;
				if(isInSessionCache(sess,termId,"narrowerTreeFlatCache")){
					flatList = (ArrayList<LocalTerm>) getFromSession(sess,termId,"narrowerTreeFlatCache");
				}else{					
					//check global cache for ontologyDB
					ontHandler = getOntologyHandlerFromCache(ontology);
					flatList = ontHandler.getNarrowTermsTreeAsFlatList(termId);
					addToSessionCache(sess,termId,flatList,"narrowerTreeFlatCache");					
				}
				
 				Gson gson = new GsonBuilder().setExclusionStrategies(new ExclBroaderTerms()).setExclusionStrategies(new ExclNarrowTerms()).setExclusionStrategies(new ExclDefAndCommentTerms()).create();	
				out.print(gson.toJson(flatList));	
				return;
			}
		}	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
	
	
	private boolean isInSessionCache(HttpSession sess,String termId,String cacheName){
		HashMap<String,Object> broaderCache  = (HashMap<String,Object>) sess.getAttribute(cacheName);
		if(broaderCache == null)
			return false;
		return broaderCache.containsKey(termId);
	}
	
	private void addToSessionCache(HttpSession sess,String termId,Object narrowTerms,String cacheName){
		HashMap<String,Object> narrowCache  = (HashMap<String,Object>) sess.getAttribute(cacheName);
		if(narrowCache == null)
			narrowCache = new HashMap<String,Object>();
		narrowCache.put(termId, narrowTerms);	
		sess.setAttribute(cacheName,narrowCache);
	}
	
	private Object getFromSession(HttpSession sess,String termId,String cacheName){
		HashMap<String,Object> broaderCache  = (HashMap<String,Object>) sess.getAttribute(cacheName);
		return broaderCache.get(termId);
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
}



