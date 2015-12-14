package ox.softeng.pdfService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ox.softeng.nhsnumservice.NHSNumberValidator;

/**
 * Servlet implementation class ConsentFormLookupService It returns proband
 * lists from WEB-INF/proband
 */
@WebServlet("/pdfService/participant")
public class pdfService extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String DATE_OF_BIRTH_FORMAT = "dd/MM/yyyy";

    static final Logger LOG = LoggerFactory.getLogger(pdfService.class);
    
    /**
     * @throws java.io.FileNotFoundException
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */

    public pdfService() {
        super();

    }

    /**
     * @throws IOException
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
     *      request, javax.servlet.http.HttpServletResponse response)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Read the queryString and split it
        HashMap<String, String> paramsMap = decodeParameters(request.getQueryString());
        if (paramsMap == null) {
            throw new ServletException("Can not decode parameters!");
        }

        SLFType slfType = SLFType.RARE_DISEASES;
        if (paramsMap.get("slfType")==null){
            paramsMap.put("slfType",SLFType.RARE_DISEASES.name());
        }

        

        ServletContext thisContext = request.getServletContext();
        InputStream xslInStream = null;
                
        switch (SLFType.valueOf(paramsMap.get("slfType"))) {
            case CANCER_BLOOD:
                slfType = SLFType.CANCER_BLOOD;
                checkRequiredParametersCancerBlood(paramsMap);
                xslInStream=thisContext.getResourceAsStream("/WEB-INF/participantPDFXSL/participant_cancer_blood.xsl");
                break;
            case CANCER_TISSUE:
                slfType = SLFType.CANCER_TISSUE;
                checkRequiredParametersCancerTissue(paramsMap);
                xslInStream=thisContext.getResourceAsStream("/WEB-INF/participantPDFXSL/participant_cancer_tissue.xsl");
                break;
            default:
                checkRequiredParametersRareDiseases(paramsMap);
                xslInStream=thisContext.getResourceAsStream("/WEB-INF/participantPDFXSL/participant_rare_diseases.xsl");
                break;
        }



        String participantId = paramsMap.get("participantId");
        Participant  participant = createSLFParticipant(paramsMap,slfType);

        BarcodeBuilder builder = new BarcodeBuilder(slfType);

        try {
            builder.buildBarcodeElements(participant);
        } catch (Exception e1) {
            LOG.warn(e1.getMessage(), e1);
        }



        try {
            PDFCreator app = new PDFCreator(slfType);
            String logoFileName = thisContext.getRealPath("/WEB-INF/participantPDFXSL/logo.jpg");
            
            app.createPDF(participant, logoFileName, xslInStream );
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
        } finally {
            xslInStream.close();
        }

        response.setContentType("application/pdf");
        response.addHeader("Content-Disposition", "attachment; filename=participant" + participantId +"_"+slfType.name().toLowerCase()+".pdf");
        response.setContentLength((int) new File(participant.pdfFile).length());

        if (new File(participant.pdfFile).exists()) {
            FileInputStream fileInputStream = null;
            OutputStream responseOutputStream = null;
            //FileUtils.writeByteArrayToFile(new File("cancer_"+slfType.name()+".pdf"),FileUtils.readFileToByteArray(new File(participant.pdfFile)));
            try {
                fileInputStream = new FileInputStream(participant.pdfFile);
                responseOutputStream = response.getOutputStream();
                int bytes;
                while ((bytes = fileInputStream.read()) != -1) {
                    responseOutputStream.write(bytes);
                }
            } catch (IOException e) {
            	LOG.warn(e.getMessage(), e);
            } finally {

                if (fileInputStream != null)
                    fileInputStream.close();

                if (responseOutputStream != null)
                    responseOutputStream.close();
            }

        }

        builder.cleanTempFiles(participant);

    }


    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
     *      request, javax.servlet.http.HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public static HashMap<String, String> decodeParameters(String queryString) throws UnsupportedEncodingException {
        // Read the queryString and split it
        String[] paramsString = queryString.split("&");
        HashMap<String, String> paramsMap = new HashMap<String, String>();
        for (int i = 0; i < paramsString.length; i++) {
            String param = "";
            String value = "";
            String[] splited = paramsString[i].split("=");
            if (splited.length >= 2) {
                param = splited[0];
                value = splited[1];
                // decode each value as we encoded them in JS file
                value = URLDecoder.decode(value, "UTF-8");
                paramsMap.put(param, value);
            }
        }
        return paramsMap;
    }


    private static void checkRequiredParametersRareDiseases(Map<String, String> params) throws ServletException {

        checkCommonRequiredParameters(params);

        String clinicId = params.get("clinicId");
        if (clinicId == null)// || "".equals(clinicId)) ??TODO ask charlie why?
        {
            throw new ServletException("Please pass the clinicId");
        }

        String diseaseType = params.get("diseaseType");
        if (StringUtils.isBlank(diseaseType)) {
            throw new ServletException("Please pass the diseaseType");
        }

        String familyId = params.get("familyId");
        if (StringUtils.isBlank(familyId)) {
            throw new ServletException("Please pass the familyId");
        }

        String hospitalNumber = params.get("hospitalNumber");
        if (hospitalNumber == null)
            hospitalNumber = ""; // to have an empty string in the final 2D
        // barcode
    }

    private static void checkRequiredParametersCancerBlood(Map<String, String> params) throws ServletException {
        String exceptionDetails = "";
        checkCommonRequiredParameters(params);

        String clinicId = params.get("clinicId");
        if (StringUtils.isBlank(clinicId)) {
            exceptionDetails += "clinicId,";
        }

        String hospitalNumber = params.get("hospitalNumber");
        if (StringUtils.isBlank(hospitalNumber)) {
            exceptionDetails += "hospitalNumber,";
        }

        if (!StringUtils.isEmpty(exceptionDetails)) {
            throw new ServletException("Plase pass " + exceptionDetails);
        }

    }

    private static void checkRequiredParametersCancerTissue(Map<String, String> params) throws ServletException {
        checkCommonRequiredParameters(params);
        String exceptionMsg = "";
        String hospitalNumber = params.get("hospitalNumber");
        if (StringUtils.isBlank(hospitalNumber)) {
            exceptionMsg += "hospitalNumber,";
        }

        String hospitalSiteCode = params.get("hospitalSiteCode");
        if (hospitalSiteCode==null) {
            exceptionMsg += "hospitalSiteCode,";
            
        }

        if (!StringUtils.isEmpty(exceptionMsg)) {
            throw new ServletException("Please pass " + exceptionMsg);
        }
    }

    private static void checkCommonRequiredParameters(Map<String, String> params) throws ServletException {
        String exceptionDetails = "";
        String participantId = params.get("participantId");
        if (StringUtils.isBlank(participantId)) {
            exceptionDetails += "participantId,";
        }

        String nhsNumber = params.get("nhsNumber");
        if (StringUtils.isBlank(nhsNumber)) {
            exceptionDetails += "nhsNumber,";
        } else {
            // validate the NHS number
            if (!NHSNumberValidator.validNHSNum(nhsNumber)) {
                throw new ServletException("NHS number is not valid!");
            }
        }

        String surname = params.get("surname");
        if (StringUtils.isBlank(surname)) {
            exceptionDetails += "surname,";
        }

        String forenames = params.get("forenames");
        if (StringUtils.isBlank(forenames)) {
            exceptionDetails += "forenames,";
        }

        String dateOfBirth = params.get("dateOfBirth");
        if (StringUtils.isBlank(dateOfBirth)) {
            exceptionDetails += "dateOfBirth,";
        } else {

            DateFormat dateFormat = new SimpleDateFormat(DATE_OF_BIRTH_FORMAT);
            Date birthDate = null;
            Date today = null;
            try {
                birthDate = dateFormat.parse(dateOfBirth);
                today = dateFormat.parse(dateFormat.format(new Date()));
            } catch (ParseException e) {
                throw new ServletException("Invalid format in dateOfBirth, we expect '" + DATE_OF_BIRTH_FORMAT + "'.");
            }

            // dateOfBirth can NOT be after today
            if (birthDate.compareTo(today) > 0) {
                throw new ServletException("DateOfBirth is not valid!");
            }

        }

        if (!StringUtils.isEmpty(exceptionDetails)) {
            throw new ServletException("Please pass the " + exceptionDetails);
        }

    }


    private static Participant createSLFParticipant(HashMap<String, String> paramsMap, SLFType slfType) {
        String participantId = paramsMap.get("participantId");
        String nhsNumber = paramsMap.get("nhsNumber");
        String surname = paramsMap.get("surname");
        String forenames = paramsMap.get("forenames");
        String dateOfBirth = paramsMap.get("dateOfBirth");
        String diseaseType = paramsMap.get("diseaseType");
        String familyId = paramsMap.get("familyId");
        String clinicId = paramsMap.get("clinicId");
        String hospitalNumber = paramsMap.get("hospitalNumber");
        String hospitalSiteCode = paramsMap.get("hospitalSiteCode");

        return  new Participant(nhsNumber, dateOfBirth, surname, forenames, participantId, diseaseType, clinicId, familyId, hospitalNumber,
                hospitalSiteCode);
    }

}