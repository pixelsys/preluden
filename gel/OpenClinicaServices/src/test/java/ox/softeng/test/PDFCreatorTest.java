package ox.softeng.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import ox.softeng.pdfService.BarcodeBuilder;
import ox.softeng.pdfService.PDFCreator;
import ox.softeng.pdfService.Participant;
import ox.softeng.pdfService.SLFType;

public class PDFCreatorTest {


    @Test
    public void createPDF_creates_a_PDF_file() throws Exception {   

        PDFCreator pdfCreator = new PDFCreator(SLFType.RARE_DISEASES);


        BarcodeBuilder builder=new BarcodeBuilder(SLFType.RARE_DISEASES);

        InputStream xsltFile = this.getClass().getClassLoader().getResourceAsStream("WEB-INF/participantPDFXSL/participant_rare_diseases.xsl");
        String logoFile = this.getClass().getClassLoader().getResource("resources/pdfxsl/logo.jpg").getPath();

        Participant participant = new Participant("1111111111","26/01/1980","SMITH","JOHN","1234567890","Rare Diseases","123","12345","12345",null);
        builder.buildBarcodeElements(participant);      
        pdfCreator.createPDF(participant, logoFile, xsltFile);

        String expectedFileName = this.getClass().getClassLoader().getResource("resources/pdf/sample_slf_rd_for_ServiceTest.pdf").getPath();
        File expectedFile = new File(expectedFileName);
        File generatedPdfFile=new File(participant.pdfFile);
        assertEquals(generatedPdfFile.length(), expectedFile.length());
        assertEquals(FileUtils.readLines(generatedPdfFile).size(), FileUtils.readLines(expectedFile).size());

        List expectedLines = FileUtils.readLines(expectedFile);
        for(int i = 0; i < expectedLines.size(); i++){
            //do not check for these specific fields, as FOP generates different value each time it creates the PDF file
            if (     ((String) expectedLines.get(i)).contains("CreationDate") || 
                    ((String) expectedLines.get(i)).contains("<dc:date>")    ||
                    ((String) expectedLines.get(i)).contains("<xmp:CreateDate>") ||
                    ((String) expectedLines.get(i)).contains("<xmp:MetadataDate>") ||
                    ((String) expectedLines.get(i)).contains("/ID [<")                    
                    )                   
                continue;            
            assertEquals(FileUtils.readLines(generatedPdfFile).get(i),expectedLines.get(i));    
        }       
        //remove the temp file
        builder.cleanTempFiles(participant);
    }

    @Test
    public void createPDF_creates_a_PDF_file_cancer_blood() throws Exception {   

        PDFCreator pdfCreator = new PDFCreator(SLFType.CANCER_BLOOD);


        BarcodeBuilder builder=new BarcodeBuilder(SLFType.CANCER_BLOOD);

        InputStream xsltFile = this.getClass().getClassLoader().getResourceAsStream("WEB-INF/participantPDFXSL/participant_cancer_blood.xsl");
        String logoFile = this.getClass().getClassLoader().getResource("resources/pdfxsl/logo.jpg").getPath();

        Participant participant = new Participant("1111111111","26/01/1980","SMITH","JOHN","1234567890","Cancer","123","12345","12345",null);
        builder.buildBarcodeElements(participant);      
        pdfCreator.createPDF(participant, logoFile, xsltFile);

        String expectedFileName = this.getClass().getClassLoader().getResource("resources/pdf/sample_slf_cancer_blood_for_ServiceTest.pdf").getPath();
        File expectedFile = new File(expectedFileName);
        File generatedPdfFile=new File(participant.pdfFile);
        assertEquals(generatedPdfFile.length(), expectedFile.length());
        assertEquals(FileUtils.readLines(generatedPdfFile).size(), FileUtils.readLines(expectedFile).size());

        List expectedLines = FileUtils.readLines(expectedFile);
        for(int i = 0; i < expectedLines.size(); i++){
            //do not check for these specific fields, as FOP generates different value each time it creates the PDF file
            if (    ((String) expectedLines.get(i)).contains("CreationDate") || 
                    ((String) expectedLines.get(i)).contains("<dc:date>")    ||
                    ((String) expectedLines.get(i)).contains("<xmp:CreateDate>") ||
                    ((String) expectedLines.get(i)).contains("<xmp:MetadataDate>") ||
                    ((String) expectedLines.get(i)).contains("/ID [<")                       
                    )                  
                continue;           
            assertEquals(FileUtils.readLines(generatedPdfFile).get(i),expectedLines.get(i));    
        }       
        //remove the temp file
        builder.cleanTempFiles(participant);
    }


    @Test
    public void createPDF_creates_a_PDF_file_cancer_tissue() throws Exception {   

        PDFCreator pdfCreator = new PDFCreator(SLFType.CANCER_TISSUE);


        BarcodeBuilder builder=new BarcodeBuilder(SLFType.CANCER_TISSUE);

        InputStream xsltFile = this.getClass().getClassLoader().getResourceAsStream("WEB-INF/participantPDFXSL/participant_cancer_tissue.xsl");
        String logoFile = this.getClass().getClassLoader().getResource("resources/pdfxsl/logo.jpg").getPath();

        Participant participant = new Participant("1111111111","26/01/1980","SMITH","JOHN","1234567890","Cancer","123","12345","12345","SIC133");
        builder.buildBarcodeElements(participant);      
        pdfCreator.createPDF(participant, logoFile, xsltFile);

        String expectedFileName = this.getClass().getClassLoader().getResource("resources/pdf/sample_slf_cancer_tissue_for_ServiceTest.pdf").getPath();
        File expectedFile = new File(expectedFileName);
        File generatedPdfFile=new File(participant.pdfFile);
        assertEquals(generatedPdfFile.length(), expectedFile.length());
        assertEquals(FileUtils.readLines(generatedPdfFile).size(), FileUtils.readLines(expectedFile).size());

        List expectedLines = FileUtils.readLines(expectedFile);
        for(int i = 0; i < expectedLines.size(); i++){
            //do not check for these specific fields, as FOP generates different value each time it creates the PDF file
            if (    ((String) expectedLines.get(i)).contains("CreationDate") || 
                    ((String) expectedLines.get(i)).contains("<dc:date>")    ||
                    ((String) expectedLines.get(i)).contains("<xmp:CreateDate>") ||
                    ((String) expectedLines.get(i)).contains("<xmp:MetadataDate>") ||
                    ((String) expectedLines.get(i)).contains("/ID [<")                       
                    )                  
                continue;           
            assertEquals(FileUtils.readLines(generatedPdfFile).get(i),expectedLines.get(i));    
        }       
        //remove the temp file
        builder.cleanTempFiles(participant);
    }

    @Test 
    public void createDocument_creates_right_dom_object_for_partitipant() throws Exception {    
        BarcodeBuilder builder=new BarcodeBuilder(SLFType.RARE_DISEASES);
        PDFCreator pdfCreator = new PDFCreator(SLFType.RARE_DISEASES);  

        Participant participant = new Participant("1111111111","26/01/1980","SMITH","JOHN","1234567890","Rare Diseases","123","12345","12345",null);
        builder.buildBarcodeElements(participant);   

        //just mock the value of the main bar-code fileName as they are created in Temporary and we they will change in each run
        for(int i = 0 ;i< participant.barcodeElements.size();i++){
            participant.barcodeElements.get(i).fileName = "barcode"+i+".pdf";           
        }

        //just mock the value of the main sample-bar-code fileName as they are created in Temporary and we they will change in each run
        for(int i = 0 ;i< participant.sampleBarcodeElements.size();i++){
            participant.sampleBarcodeElements.get(i).fileName = "sample"+i+".pdf";          
        }

        participant.dataMatrixBarcodeFileName = "dataMatrixBarcode.png";

        String logoFile = "resources/pdfxsl/logo.jpg";//this.getClass().getClassLoader().getResource("resources/pdfxsl/logo.jpg").getPath();

        DOMSource createDocument = pdfCreator.createDocument(participant,logoFile);
        String domActual = getStringFromDOM(createDocument);

        String pdfDOMFile = this.getClass().getClassLoader().getResource("resources/pdfxsl/pdfDOM.xml").getPath();
        String domExpected = FileUtils.readFileToString(new File(pdfDOMFile), "utf-8");

        //compare them as String, we can also use other XML JUnit libraries like http://www.xmlunit.org/
        assertEquals(domExpected.replace("\n", "").replace(" ", ""), domActual.replace("\n", "").replace(" ", ""));

    }

    private String getStringFromDOM(DOMSource doc) throws TransformerException {
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(doc, result);
        return writer.toString();
    }
}
