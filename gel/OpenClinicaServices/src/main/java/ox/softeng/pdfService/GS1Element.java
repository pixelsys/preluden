package ox.softeng.pdfService;

public class GS1Element{		
	public String code; // GS1 Application Id(ai)
	public String name;	
	public String value;
	
	public GS1Element(String code, String name,String value) {
		this.code = code;
		this.name = name;
		this.value = value;
	}		
}