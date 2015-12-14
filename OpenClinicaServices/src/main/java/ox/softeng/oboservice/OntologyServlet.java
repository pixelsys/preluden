package ox.softeng.oboservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.biojava3.ontology.Ontology;
import org.biojava3.ontology.Term;
import org.biojava3.ontology.Triple;
import org.biojava3.ontology.io.OboParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ox.softeng.oboservice.OntologyHandler.LocalTerm;
/**
 * Servlet implementation class OntologyServlet
 */
@WebServlet("/OntologyServlet")
public  class OntologyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	static final Logger LOG = LoggerFactory.getLogger(OntologyServlet.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OntologyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		LOG.debug("OBO Service requested...");

		HttpSession sess = request.getSession();
		// We're going to get the ontology name, see if we've already cached it, 
		// and if not, we'll be re-loading it, and caching it. 

		String dataSourceName = (String) request.getParameter("dataSourceName");

		// If we don't have it, barf
		if(dataSourceName == null || "".equals(dataSourceName))
		{
			throw new ServletException("Please pass the obo file name");
		}
		
		ServletContext thisContext = request.getServletContext();
		Ontology ontology = (Ontology) sess.getAttribute("ontology_" + dataSourceName);
		if(ontology == null){
			try {
				OboParser parser = new OboParser();
				InputStream inStream = thisContext.getResourceAsStream("/WEB-INF/obo/" + dataSourceName);
				BufferedReader oboFile = new BufferedReader ( new InputStreamReader ( inStream ) );
				ontology = parser.parseOBO(oboFile, "my Ontology name", "description of ontology");
				Set<Term> termSet = ontology.getTerms();
				LOG.debug("Initialising the OBO Service");
				Object[] triples =  ontology.getTriples(null, null, null).toArray();
				for(int i=0;i<triples.length;i++)
				{
					Triple trip = (Triple) triples[i];
					if(!trip.getPredicate().getName().equals("is_a") && !trip.getPredicate().getName().equals("subset"))
						LOG.debug(triples[i].toString());
				}
				LOG.debug("");
				sess.setAttribute("termSet_" + dataSourceName, termSet);
				sess.setAttribute("ontology_" + dataSourceName, ontology);
			} catch (Exception e){
				LOG.warn(e.getMessage(), e);
			}
		}
		
		ServletContext application = getServletConfig().getServletContext();
		if(application.getAttribute("ontologyDB") == null){
			//build ontologyDB in memory in Application Cache
			HashMap<String,LocalTerm> db = OntologyHandler.buildInMemoryDB(ontology);
			application.setAttribute("ontologyDB", db);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
