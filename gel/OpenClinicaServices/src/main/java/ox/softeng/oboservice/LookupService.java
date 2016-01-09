package ox.softeng.oboservice;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.biojava3.ontology.Ontology;
import org.biojava3.ontology.Term;
import org.biojava3.ontology.io.OboParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ox.softeng.nhsnumservice.NHSNumService;

/**
 * Servlet implementation class OBOService
 */
@WebServlet("/OntologyService/LookupService")
public class LookupService extends OntologyServlet {
	private static final long serialVersionUID = 1L;
    
	static final Logger LOG = LoggerFactory.getLogger(LookupService.class);
	
    /**
     * @throws FileNotFoundException 
     * @see HttpServlet#HttpServlet()
     */
	
	
    public LookupService() throws FileNotFoundException {
        super();
        
        
      
    }

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();

	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// The super method will ensure the ontology is loaded and cached in the 
		// servlet context
		// It will also set the response type and do some logging.
		LOG.debug("LookupService request started...");
		super.doGet(request, response);
		
		HttpSession sess = request.getSession();
		String dataSourceName = (String) request.getParameter("dataSourceName");
		
		String q = request.getParameter("term");
		if(q == null || q.equals(""))
		{
			return;
		}
		Set<Term> termSet = (Set<Term>) sess.getAttribute("termSet_" + dataSourceName);
		Iterator<Term> termIterator = termSet.iterator();
		ServletOutputStream out = response.getOutputStream();
		JSONArray jarr = new JSONArray();
		
		String desc;
		while (termIterator.hasNext()){
			Term term = (Term) termIterator.next();
			desc = term.getDescription();
			
			if(desc != null && desc.toLowerCase().contains(q.toLowerCase()))
			{
				JSONObject job = new JSONObject();
				try {
					job.append("id", term.getName());
					job.append("label", term.getDescription());
					job.append("value", term.getDescription());
					jarr.put(job);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					LOG.warn(e.getMessage(), e);
				}
				//response.getOutputStream().println("term name: " + term.getName());
				//response.getOutputStream().println("term description: " + term.getDescription());
			}
			
			/*Object[] synonyms =  term.getSynonyms();
			for ( Object syn : synonyms ) {
				System.out.println(syn);
			}*/
		}
		out.print(jarr.toString());
		//System.out.println(jarr.toString());
		LOG.debug("OBO Service request completed");
		return;
		// response.getOutputStream().println("query: " + q);
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
