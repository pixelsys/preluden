package ox.softeng.pdfService;

import java.util.ArrayList;

public class GS1Formatter {
	
 	public final static char GS = (char)29;	
 	public final static char PIPE  = (char)124; //pipe character |
 	public final static char TILDE = (char)126; //tilde character ~
 	
 	public final static String GEL_GS1_ORGANISATION_PREFIX = "0000000";
	
	public static String getBarcodeString(ArrayList<GS1Element> elements){
		
		String barcode = "";
		for(int i=0; i< elements.size();i++){
			GS1Element element = elements.get(i);
			barcode += element.code + element.value +  GS;
		}
		//remove the last GS
		return barcode.substring(0 , barcode.lastIndexOf(GS));
	}
	
	
	//Calculate GSRN Check Digit based on:
	//http://www.isb.nhs.uk/documents/isb-1077/amd-144-2010/10771442010spec.pdf
	public static String GRSNCheckDigit(String input) throws Exception{
		if(input.length() > 17){
			throw new Exception("GSRN length is more than 18 digits!");
		}
 		int total = 0;
		for(int i = 0; i < input.length();i++){
			int multiply = 1;
			//odd
			if (i % 2 == 0)
				multiply = 3;
			int digit = Character.getNumericValue(input.charAt(i)) * multiply;
			total += digit;
		}
		int nearest = ((total / 10) * 10 ) + 10;
		
		int value = Math.abs(nearest - total);
		return value + "";
	}
	
	public static String removeChars(String input)
	{
		if(input == null)
			input = "";
		return (input +"").replace(GS1Formatter.PIPE+"", "").replace("|", "").replace(GS1Formatter.TILDE+"", "").replace("~", "");
	}
	
}