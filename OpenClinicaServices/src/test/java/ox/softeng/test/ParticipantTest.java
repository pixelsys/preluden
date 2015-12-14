package ox.softeng.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import ox.softeng.pdfService.BarcodeBuilder;
import ox.softeng.pdfService.BarcodeElement;
import ox.softeng.pdfService.GS1Formatter;
import ox.softeng.pdfService.Participant;
import ox.softeng.pdfService.SLFType;

public class ParticipantTest {
	@Test
	public void getSLFDatamatrixBarcodeString_will_return_the_barcode_string() throws Exception {	
		
 		Participant participant = new Participant("1234567890","28/11/2015","John","SMITH","12345","Rare Disease","GRT123","100","HSP123","");
    	participant.barcodeElements  = new ArrayList<BarcodeElement>(); 		
 		BarcodeBuilder builder=new BarcodeBuilder(SLFType.RARE_DISEASES); 
 		builder.buildBarcodeElements(participant);		
 		
 		String checksum = "5";
 	
 		String expected = "801800000001234567890"+ checksum + GS1Formatter.GS + "9112345" + GS1Formatter.GS + "92100|GRT123|HSP123|SMITH|John|28/11/2015" + GS1Formatter.GS + "93Rare Disease" + GS1Formatter.GS + "941.1.1|03/03/2015";	
		assertEquals( expected , builder.getSLFDatamatrixBarcodeString(participant));
	}
	
	
	@Test
	public void getSLFDatamatrixBarcodeString_will_remove_certain_characters() throws Exception {	
	    BarcodeBuilder builder=new BarcodeBuilder(SLFType.RARE_DISEASES); 
 		Participant participant = new Participant("1234567890","28/11/2015","J~ohn","S|MI|TH","12345","Rare Disease","GRT123","100","HSP123","");
    	participant.barcodeElements  = new ArrayList<BarcodeElement>(); 		
        builder.buildBarcodeElements(participant);   		
 		
 		String checksum = "5";
 	
 		String expected = "801800000001234567890"+ checksum + GS1Formatter.GS + "9112345" + GS1Formatter.GS + "92100|GRT123|HSP123|SMITH|John|28/11/2015" + GS1Formatter.GS + "93Rare Disease" + GS1Formatter.GS + "941.1.1|03/03/2015";	
		assertEquals( expected , builder.getSLFDatamatrixBarcodeString(participant));
	}
	
	
	@Test
	public void getSLFDatamatrixBarcodeString_will_make_string_as_empty_value_if_it_is_null() throws Exception {	
	    BarcodeBuilder builder=new BarcodeBuilder(SLFType.RARE_DISEASES); 
 		Participant participant = new Participant("1234567890","28/11/2015","John","SMITH","12345","Rare Disease",null,"100",null,null);
    	participant.barcodeElements  = new ArrayList<BarcodeElement>(); 		
        builder.buildBarcodeElements(participant);   		
 		
 		String checksum = "5";
 	
 		String expected = "801800000001234567890"+ checksum + GS1Formatter.GS + "9112345" + GS1Formatter.GS + "92100|||SMITH|John|28/11/2015" + GS1Formatter.GS + "93Rare Disease" + GS1Formatter.GS + "941.1.1|03/03/2015";	
		assertEquals( expected , builder.getSLFDatamatrixBarcodeString(participant));
	}
	
	
	 
}
