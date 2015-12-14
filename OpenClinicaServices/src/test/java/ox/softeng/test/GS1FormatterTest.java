package ox.softeng.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import ox.softeng.pdfService.GS1Element;
import ox.softeng.pdfService.GS1Formatter;

public class GS1FormatterTest {

	@Before
	public void setUp() throws Exception {
	 
	}
	
	@Test
	public void getBarcodeString_generates_the_barcode_string() throws Exception {

		String nhsNumber = "1234567890";
    	String dateOfBirth = "28/11/2015";
    	String surname = "John";
    	String forenames = "SMITH";
    	String participantId = "12345"; 
    	String diseaseType = "Rare Disease";
    	String clinicId = "GRT123";
    	String sampleType = "DNA Blood Germline";
    	String familyId = "100";
 		
		ArrayList<GS1Element> GS1Codes = new ArrayList<GS1Element>();		
		GS1Codes.add(new GS1Element("91","nhsNumber",nhsNumber));		
		GS1Codes.add(new GS1Element("92","participantId", participantId.replace("|", "") ));
		//consists of familyId|clinicId|forenames|surname|dob
		String participantDetails = familyId.replace("|", "")  + "|" 
								 +  clinicId.replace("|", "")  + "|"
							 	 +  forenames.replace("|", "") + "|"
								 +  surname.replace("|", "")   + "|" 
								 +  dateOfBirth.replace("|", "") ;						
		GS1Codes.add(new GS1Element("93","participantDetails",participantDetails));
 		GS1Codes.add(new GS1Element("94","diseaseType",diseaseType));
		GS1Codes.add(new GS1Element("95","sampleType",sampleType));
		
		//For example, the following code means ( we do not add open/close paranthes in the string in the actual system):
		//(91)1234567890(92)12345(93)100|290|SMITH|John|28/02/2015(94)Rare Disease(95)DNA Blood Germline
		//911234567890<GS>9212345<GS>93100|290|SMITH|John|28/02/2015<GS>94Rare Disease<GS>95DNA Blood Germline
		
		//nshNumber: 1234567890
		//surname: SMITH
		//forenames: John
		//dob: 01/02/2015
		//particpantId: 12345
		//familyId: 100
		//clinicId:	290
		//diseaseType: Rare Disease
		//sampleType: DNA Blood Germline		
		
 		String expected = "911234567890" + GS1Formatter.GS + "9212345" + GS1Formatter.GS + "93100|GRT123|SMITH|John|28/11/2015" + GS1Formatter.GS + "94Rare Disease" + GS1Formatter.GS + "95DNA Blood Germline";	
		assertEquals( expected , GS1Formatter.getBarcodeString(GS1Codes));
	}
	
	@Test
	public void check_if_GRSNCheckDigit_creates_correct_checkSum() throws Exception{
		
		String checkDigit = GS1Formatter.GRSNCheckDigit("09090900090000890");
		assertEquals( checkDigit , "1");
		
		
		checkDigit = GS1Formatter.GRSNCheckDigit("09090901000007890");
		assertEquals( checkDigit , "2");
		
		
		checkDigit = GS1Formatter.GRSNCheckDigit("50508989990005451");
		assertEquals( checkDigit , "3");
		
		checkDigit = GS1Formatter.GRSNCheckDigit("09090901000067890");
		assertEquals( checkDigit , "4");
		
		checkDigit = GS1Formatter.GRSNCheckDigit("00000001234567890");
		assertEquals( checkDigit , "5");
		
		
		checkDigit = GS1Formatter.GRSNCheckDigit("00000901234567890");
		assertEquals( checkDigit , "6");
		
		
		checkDigit = GS1Formatter.GRSNCheckDigit("00090901234567890");
		assertEquals( checkDigit , "7");
		
		
		checkDigit = GS1Formatter.GRSNCheckDigit("09090901234567890");
		assertEquals( checkDigit , "8");
		
		checkDigit = GS1Formatter.GRSNCheckDigit("01208801234007890");
		assertEquals( checkDigit , "9");
		
		
		checkDigit = GS1Formatter.GRSNCheckDigit("01208801234");
		assertEquals( checkDigit , "9");
		
	}
	
	@Test(expected=Exception.class)
	public void GRSNCheckDigit_thows_exception_if_string_is_longer_than_18() throws Exception{
		GS1Formatter.GRSNCheckDigit("50508989990005451000");
	}
}
