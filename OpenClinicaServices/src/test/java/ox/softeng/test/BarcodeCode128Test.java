package ox.softeng.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import ox.softeng.pdfService.Barcode;

import com.google.zxing.NotFoundException;

@RunWith(Parameterized.class)
public class BarcodeCode128Test {
	
	private String barcode;
	private String barcodeFileName;
	

    @Parameters(name = "{index}: barcode={0} fileName={1}")
	public static Collection<Object[]> data() {
		
		 return Arrays.asList(new Object[][] {     
	             {"This is a test data 0123456789",  "code128-00.png"},
	             {"ABCD 01345678901234515171231 EFG","code128-01.png"}
	       });
	}
	
	public BarcodeCode128Test(String  barcode, String  barcodeFileName) {
		this.barcode = barcode;
		this.barcodeFileName = barcodeFileName;
    }
	 	 
		 
	@Test
 	public void generateCode128_creates_code128_barcode() throws IOException, NotFoundException{
		
		File barcodeFile = File.createTempFile(UUID.randomUUID().toString(), ".png");
		new Barcode().generateCode128(barcode, barcodeFile.getAbsolutePath());
		   
		URL expectedFileName = this.getClass().getClassLoader().getResource("resources/barcodes/" + barcodeFileName);
		File expectedFile = new File(expectedFileName.getPath());
		   
		assertEquals(FileUtils.readLines(barcodeFile), FileUtils.readLines(expectedFile));
		   
		//clean the template file
		barcodeFile.delete();
		assertEquals(barcodeFile.exists(), false);
	}
	
	@Test
 	public void readCode128_reads_code128_barcode() throws IOException, NotFoundException{

		URL inputFileName = this.getClass().getClassLoader().getResource("resources/barcodes/" + barcodeFileName);
		String result = new Barcode().readCode128(inputFileName.getPath());
		assertEquals(result,barcode);
	}
	
}
