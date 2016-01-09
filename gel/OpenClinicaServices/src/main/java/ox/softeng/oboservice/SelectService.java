package ox.softeng.oboservice;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.biojava3.ontology.Ontology;
import org.biojava3.ontology.Term;
import org.biojava3.ontology.Triple;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class SelectService
 */
@WebServlet("/OntologyService/SelectService")
public class SelectService extends OntologyServlet {
	private static final long serialVersionUID = 1L;
    
	static final Logger LOG = LoggerFactory.getLogger(SelectService.class);
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SelectService() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// The super method will ensure the ontology is loaded and cached in the 
		// servlet context
		// It will also set the response type and do some logging.
		LOG.debug("SelectService request started...");
		super.doGet(request, response);
		try{
			JSONObject job = new JSONObject();
			HttpSession sess = request.getSession();
			String dataSourceName = (String) request.getParameter("dataSourceName");
			
			String id = request.getParameter("id");
			if(id == null || id.equals(""))
			{
				return;
			}
			LOG.debug("Searching for id: {}", id);
			Ontology ontology = (Ontology) sess.getAttribute("ontology_" + dataSourceName);
			Term t = ontology.getTerm(id);
			System.out.println(t.getName());
			Set<Triple> broaderTriples = ontology.getTriples(t, null, null);
			Iterator <Triple> tripleIterator = broaderTriples.iterator();
			JSONArray broaderTerms = new JSONArray();
			while(tripleIterator.hasNext())
			{
				JSONObject jterm = new JSONObject();
				Term broaderTerm = tripleIterator.next().getObject();
				jterm.put("id", broaderTerm.getName());
				jterm.put("label", broaderTerm.getDescription());
				jterm.put("value", broaderTerm.getDescription());
				broaderTerms.put(jterm);
				//System.out.println(triple.getObject().getDescription());
				
			}
			job.put("broaderTerms", broaderTerms);
			Set<Triple> narrowerTriples = ontology.getTriples(null, t, null);
			tripleIterator = narrowerTriples.iterator();
			JSONArray narrowerTerms = new JSONArray();
			while(tripleIterator.hasNext())
			{
				
				Term narrowerTerm = tripleIterator.next().getSubject();
				if(!narrowerTerm.getName().equals(id))
				{
					JSONObject jterm = new JSONObject();
					jterm.put("id", narrowerTerm.getName());
					jterm.put("label", narrowerTerm.getDescription());
					jterm.put("value", narrowerTerm.getDescription());
					narrowerTerms.put(jterm);
				}
			}
			job.put("narrowerTerms", narrowerTerms);
			response.getOutputStream().print(job.toString());
			return;
			
		}catch(Exception e){
			LOG.error("Error: " + e.getMessage(), e);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
