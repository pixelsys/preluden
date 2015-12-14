package ox.softeng.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import ox.softeng.pdfService.Barcode;

import com.google.zxing.EncodeHintType;
import com.google.zxing.NotFoundException;
 

@RunWith(Parameterized.class)
public class BarcodeDataMatrixTest {

	private String barcode;
	private String barcodeFileName;
	private boolean isGS1;
	
	//<GS> character that should be used as a separator in GS1 between elements 
	private static final char GS = (char)29; 

    @Parameters(name = "{index}: barcode={0} fileName={1} isGS1={2}")
	public static Collection<Object[]> data() {
		
		 return Arrays.asList(new Object[][] {     
	             {"01345678901234515171231", "datamatrix00-nonGS1.png",	false},
	             {"01345678901234515171231", "datamatrix00-GS1.png",	true},
	             {"0110614141543219103456789" + GS + "213456789012", "datamatrix01-GS1.png",true}
	       });
	}
	
	public BarcodeDataMatrixTest(String  barcode, String  barcodeFileName,boolean isGS1) {
		this.barcode = barcode;
		this.barcodeFileName = barcodeFileName;
		this.isGS1 = isGS1;
    }
	 	 
		 
	@Test
 	public void generateDataMatrix_creates_datamatrix_barcode() throws IOException, NotFoundException{
		
		Map<EncodeHintType,Object> hintTypes = new HashMap<EncodeHintType,Object>();
		hintTypes.put(EncodeHintType.DATA_MATRIX_SHAPE, com.google.zxing.datamatrix.encoder.SymbolShapeHint.FORCE_SQUARE);
		hintTypes.put(EncodeHintType.GS1_ENCODE, isGS1);
		
		File barcodeFile = File.createTempFile(UUID.randomUUID().toString(), ".png");
		new Barcode().generateDataMatrix(barcode, hintTypes, barcodeFile.getAbsolutePath());
		   
		URL expectedFileName = this.getClass().getClassLoader().getResource("resources/barcodes/" + barcodeFileName);
		File expectedFile = new File(expectedFileName.getPath());
		   
		assertEquals(FileUtils.readLines(barcodeFile), FileUtils.readLines(expectedFile));
		   
		//clean the template file
		barcodeFile.delete();
		assertEquals(barcodeFile.exists(), false);
	}
	
	
	@Test
 	public void readDataMatrix_reads_datamatrix_barcode() throws IOException, NotFoundException{

		URL inputFileName = this.getClass().getClassLoader().getResource("resources/barcodes/" + barcodeFileName);
		String result = new Barcode().readDataMatrix(inputFileName.getPath());
		String expect = barcode;
		
		//When bar-code scanner reads GS1 type bar-code, 
		//it adds char(29) at the beginning of the result
		if(isGS1)
			expect = (char)29 + expect;
		assertEquals(result,expect);
	}
	
}
