package ox.softeng.pdfService;

import java.util.ArrayList;

public class Participant {
	
	public Participant(String nhsNumber, String dateOfBirth, String surname,
			String forenames, String participantId, String diseaseType,
			String clinicId, String familyId, String hospitalNumber,String hospitalSiteCode) {
		this.nhsNumber = nhsNumber;
		this.dateOfBirth = dateOfBirth;
		this.surname = surname;
		this.forenames = forenames;
		this.participantId = participantId;
		this.diseaseType = diseaseType;
		this.clinicId = clinicId;
		this.familyId = familyId;
		this.hospitalNumber = hospitalNumber;
		this.hospitalSiteCode=hospitalSiteCode;
    	this.barcodeElements  = new ArrayList<BarcodeElement>();    	
	}
	
	    
	public String nhsNumber;
	public String dateOfBirth;
	public String surname;
	public String forenames;
	public String participantId;
	public String diseaseType;
	public String clinicId;
	public String familyId;
	
	public String hospitalNumber;
    public String hospitalSiteCode;
	
	public String dataMatrixBarcodeFileName;
	public String pdfFile;

	public ArrayList<BarcodeElement> barcodeElements = new ArrayList<BarcodeElement>();
	public ArrayList<SampleBarcodeElement> sampleBarcodeElements = new ArrayList<SampleBarcodeElement>();

	
	

}
