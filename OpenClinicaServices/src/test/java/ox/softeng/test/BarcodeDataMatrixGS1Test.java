package ox.softeng.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import ox.softeng.pdfService.Barcode;

public class BarcodeDataMatrixGS1Test {

	
		 
	@Test
 	public void generateGS1DataMatrix_creates_GS1datamatrix_barcode() throws Exception{
		
		File barcodeFile = File.createTempFile(UUID.randomUUID().toString(), ".png");
		String input = "This is a very simple string which will be encoded as GS1 in the dataMatrix";
		
		new Barcode().generateGS1DataMatrix(input, barcodeFile.getAbsolutePath());
				   
		URL expectedFileName = this.getClass().getClassLoader().getResource("resources/barcodes/datamatrix-Sample-GS1.png" );
		File expectedFile = new File(expectedFileName.getPath());
		   
		assertEquals(FileUtils.readLines(barcodeFile), FileUtils.readLines(expectedFile));
		   
		//clean the template file
		barcodeFile.delete();
		assertEquals(barcodeFile.exists(), false);
	}
	
	
	 
	
}
