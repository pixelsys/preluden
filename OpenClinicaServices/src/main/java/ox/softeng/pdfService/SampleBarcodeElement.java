package ox.softeng.pdfService;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class SampleBarcodeElement {

	public String tube;
	public String type;
	public String fileName;
	
	public SampleBarcodeElement(String tube,String type,String fileName){
		this.tube = tube;
		this.type = type;
		this.fileName = fileName;
	}
	
	
	public static SampleBarcodeElement createGELSampleBarcode(String tube,String type) throws IOException{
		
		File tempFile = File.createTempFile(UUID.randomUUID().toString(), ".png");		
		SampleBarcodeElement sampleBarcodeElement = new SampleBarcodeElement(tube, type, tempFile.getAbsolutePath());
		Barcode barcode = new Barcode();
		barcode.generateCode128(sampleBarcodeElement.type,sampleBarcodeElement.fileName);
		return sampleBarcodeElement;
	}	
}
