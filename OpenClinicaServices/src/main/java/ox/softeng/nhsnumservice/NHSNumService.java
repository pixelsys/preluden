package ox.softeng.nhsnumservice;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class NHSNumService
 */
@WebServlet("/NHSNumService")
public class NHSNumService extends HttpServlet {
	
	static final Logger LOG = LoggerFactory.getLogger(NHSNumService.class);

	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NHSNumService() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		ServletOutputStream out = response.getOutputStream();
		LOG.debug("NHS Number Validation Service requested...");
		String nhsNo = request.getParameter("NHSNumVal");
		if(nhsNo == null || nhsNo.equals(""))
		{
			out.print("{result: false}");
			return;
		}
		boolean result = NHSNumberValidator.validNHSNum(nhsNo);

		JSONObject json = new JSONObject();
		try {
			json.put("result", result);
		} catch (JSONException e) {
			out.print("{\"result\": false}");
			LOG.warn(e.getMessage(),e);
		}
		out.print(json.toString());
		LOG.debug("NHS Number Validation Service responded");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    doGet(request,response);
	}

	
}
