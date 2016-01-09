package ox.softeng.pdfService;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PDFCreator {
    
    private SLFType slfType=SLFType.RARE_DISEASES;

	private FopFactory fopFactory = FopFactory.newInstance();
	
	/**
	 * 
	 * @param slfType based on this parameter 
	 */
	public PDFCreator(SLFType slfType){
	    this.slfType=slfType;
	}

	public void createPDF(Participant participant, String logoFileName, InputStream xslt) throws IOException, FOPException,
			TransformerException, ParserConfigurationException,FileNotFoundException {

		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        FileOutputStream pdfOutputStream = new java.io.FileOutputStream(participant.pdfFile);
		BufferedOutputStream out = new java.io.BufferedOutputStream(pdfOutputStream);

		try {
			
			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent,out);
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(new StreamSource(xslt));
			
			Source src = (Source) createDocument(participant,logoFileName);

			Result res = new SAXResult(fop.getDefaultHandler());

			transformer.transform(src, res);
		} finally {
			out.close();
			pdfOutputStream.close();
		}
	}

	public DOMSource createDocument(Participant participant,String logoFileName)
			throws ParserConfigurationException,
			TransformerConfigurationException {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		// root elements
		Document doc = docBuilder.newDocument();

		Element participantElement = doc.createElement("participant");
		doc.appendChild(participantElement);

		Element participantIdElement = doc.createElement("participantId");
		participantIdElement.setTextContent(participant.participantId);
		participantElement.appendChild(participantIdElement);
		
		
		

		Element nhsNumberElement = doc.createElement("nhsNumber");
		//change nhs from 1234567890 to 123 456 7890
		
		String nhsStr = participant.nhsNumber;		
		
		//if(nhsStr.length() == 10)
			//nhsStr = nhsStr.substring(0, 3) ;//+ " " + nhsStr.substring(4, 7)+ " " + nhsStr.substring(8, nhsStr.length());
		

		nhsNumberElement.setTextContent(nhsStr);
		participantElement.appendChild(nhsNumberElement);

		
		Element dateOfBirthElement = doc.createElement("dateOfBirth");
		dateOfBirthElement.setTextContent(participant.dateOfBirth);
		participantElement.appendChild(dateOfBirthElement);
		
		
		Element surnameElement = doc.createElement("surname");
		surnameElement.setTextContent(participant.surname.toUpperCase() +" ");
		participantElement.appendChild(surnameElement);
	
		
		
		Element fornamesElement = doc.createElement("fornames");
		fornamesElement.setTextContent(participant.forenames.toUpperCase()+"  ");
		participantElement.appendChild(fornamesElement);
		
		
		
		Element barCodeFileNameElement = doc.createElement("barCodeFileName");
		barCodeFileNameElement.setTextContent(participant.dataMatrixBarcodeFileName);
		participantElement.appendChild(barCodeFileNameElement);
		
		
		Element logoFileNameElement = doc.createElement("logoFileName");
		logoFileNameElement.setTextContent(logoFileName);
		participantElement.appendChild(logoFileNameElement);
		
		
		for(int i = 0; i< participant.barcodeElements.size(); i++){
			
			//Element member = doc.createElement("barcodes");
			  
			Element name = doc.createElement("barcodeName"+i);
			name.setTextContent(participant.barcodeElements.get(i).name);
			participantElement.appendChild(name);
			  	  
			 
			Element value = doc.createElement("barcodeValue"+i);
			value.setTextContent(participant.barcodeElements.get(i).value);
			participantElement.appendChild(value);
			
			Element file = doc.createElement("barcodeFile"+i);
			file.setTextContent(participant.barcodeElements.get(i).fileName);
			participantElement.appendChild(file);
			
			//participantElement.appendChild(member);
		}
		
		for(int i = 0; i< participant.sampleBarcodeElements.size(); i++){
			
			//Element member = doc.createElement("barcodes");
			  
			Element name = doc.createElement("sampleTube"+i);
			name.setTextContent(participant.sampleBarcodeElements.get(i).tube);
			participantElement.appendChild(name);
			  	  
			 
			Element value = doc.createElement("sampleType"+i);
			value.setTextContent(participant.sampleBarcodeElements.get(i).type);
			participantElement.appendChild(value);
			
			Element file = doc.createElement("sampleBarcodeFile"+i);
			file.setTextContent(participant.sampleBarcodeElements.get(i).fileName);
			participantElement.appendChild(file);
			
			//participantElement.appendChild(member);
		}
		 
   	 
		//add SLF version and date
		Element slfVersion = doc.createElement("slfVersion");
		slfVersion.setTextContent(slfType.getVersion());
		participantElement.appendChild(slfVersion);		
		
		Element slfDate = doc.createElement("slfDate");
		slfDate.setTextContent(slfType.getDate());
		participantElement.appendChild(slfDate);			
		
		DOMSource source = new DOMSource(doc);
		return source;
	}

}
