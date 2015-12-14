package ox.softeng.pdfService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class BarcodeBuilder {
    
    public SLFType slfType;

    public String dataMatrixBarcodeFileName;
    
    /**
     * Instanciate a barcode builder specific for {@link SLFType}. 
     * @param slfType
     */
    public BarcodeBuilder(SLFType slfType){
        this.slfType=slfType;
    }

    private void buildBarcodeElementsRareDiseases(Participant participant) throws Exception  {

        Barcode barcode = new Barcode();
        ArrayList<BarcodeElement> barcodeElements =participant.barcodeElements;
        ArrayList<SampleBarcodeElement> sampleBarcodeElements =participant.sampleBarcodeElements;

        //clear if any 
        barcodeElements.clear();
        
        //build common attributes 
        buildCommonBarElements(participant, barcode, barcodeElements);

        //Barcode 3 Family ID
        File  tempFile = File.createTempFile(UUID.randomUUID().toString(), ".png");
        barcode.generateCode128(participant.familyId,tempFile.getAbsolutePath()) ;
        BarcodeElement element = new BarcodeElement("Family ID",participant.familyId,tempFile.getAbsolutePath());
        barcodeElements.add(3,element);
        
        //Barcode 4 Clinic ID (ODS Code)
        tempFile = File.createTempFile(UUID.randomUUID().toString(), ".png");
        barcode.generateCode128(participant.clinicId,tempFile.getAbsolutePath()) ;
        element = new BarcodeElement("Clinic ID (ODS Code)",participant.clinicId,tempFile.getAbsolutePath());
        barcodeElements.add(4,element);



        //build sampleBarcodes

        SampleBarcodeElement sampleBarcodeElement = SampleBarcodeElement.createGELSampleBarcode("EDTA#1", "DNA Blood Germline");            
        sampleBarcodeElements.add(sampleBarcodeElement);    

        sampleBarcodeElement = SampleBarcodeElement.createGELSampleBarcode("EDTA#2", "DNA Blood Germline");         
        sampleBarcodeElements.add(sampleBarcodeElement);

        sampleBarcodeElement = SampleBarcodeElement.createGELSampleBarcode("PST", "LiHep Plasma");          
        sampleBarcodeElements.add(sampleBarcodeElement);


        sampleBarcodeElement = SampleBarcodeElement.createGELSampleBarcode("PAXgene RNA", "RNA Blood");         
        sampleBarcodeElements.add(sampleBarcodeElement);


        sampleBarcodeElement = SampleBarcodeElement.createGELSampleBarcode("SST", "Serum");         
        sampleBarcodeElements.add(sampleBarcodeElement);


        //build 2dBarcode
        dataMatrixBarcodeFileName = File.createTempFile(UUID.randomUUID().toString(), ".png").getAbsolutePath();
        participant.dataMatrixBarcodeFileName=dataMatrixBarcodeFileName;

        String barcodeString = getSLFDatamatrixBarcodeString(participant);     
        barcode.generateGS1DataMatrix(barcodeString,dataMatrixBarcodeFileName);         
        //build pdf file
        String pdfFile = File.createTempFile(UUID.randomUUID().toString(), ".pdf").getAbsolutePath(); 
        participant.pdfFile=pdfFile;

    }

    private void buildCommonBarElements(Participant participant, Barcode barcode, ArrayList<BarcodeElement> barcodeElements) throws IOException {
        //Barcode 0 NHS Number
        File tempFile = File.createTempFile(UUID.randomUUID().toString(), ".png");
        barcode.generateCode128(participant.nhsNumber,tempFile.getAbsolutePath()) ;
        BarcodeElement element = new BarcodeElement("NHS Number",participant.nhsNumber,tempFile.getAbsolutePath());
        barcodeElements.add(element);

        //Barcode 1 Hospital Number
        tempFile = File.createTempFile(UUID.randomUUID().toString(), ".png");
        barcode.generateCode128(participant.hospitalNumber,tempFile.getAbsolutePath());
        element = new BarcodeElement("Hospital Number",participant.hospitalNumber,tempFile.getAbsolutePath());
        barcodeElements.add(element);


        //Barcode 2 Participant ID
        tempFile = File.createTempFile(UUID.randomUUID().toString(), ".png");
        barcode.generateCode128(participant.participantId,tempFile.getAbsolutePath());
        element = new BarcodeElement("Participant ID",participant.participantId,tempFile.getAbsolutePath());
        barcodeElements.add(element);

        //Barcode 3 Family ID will be added later for rare diseases

        //Barcode 4 Clinic ID (ODS Code) will be added for rare diseases

        //Barcode 5 Disease Type
        tempFile = File.createTempFile(UUID.randomUUID().toString(), ".png");
        barcode.generateCode128(participant.diseaseType,tempFile.getAbsolutePath()) ;
        element = new BarcodeElement("Disease Type",participant.diseaseType,tempFile.getAbsolutePath());
        barcodeElements.add(element);
    } 

    private  void buildBarcodeElementsCancerBlood(Participant participant) throws Exception  {

        Barcode barcode = new Barcode();
        ArrayList<BarcodeElement> barcodeElements =participant.barcodeElements;
        ArrayList<SampleBarcodeElement> sampleBarcodeElements =participant.sampleBarcodeElements;
        //clear if any 
        barcodeElements.clear();

        buildCommonBarElements(participant, barcode, barcodeElements);
        


        //Barcode 3 Clinic ID (ODS Code)
        File tempFile = File.createTempFile(UUID.randomUUID().toString(), ".png");
        barcode.generateCode128(participant.clinicId,tempFile.getAbsolutePath()) ;
        BarcodeElement element = new BarcodeElement("Clinic ID (ODS Code)",participant.clinicId,tempFile.getAbsolutePath());
        barcodeElements.add(2,element);



        //build sampleBarcodes
        SampleBarcodeElement sampleBarcodeElement = SampleBarcodeElement.createGELSampleBarcode("EDTA#1", "DNA Blood Germline");            
        sampleBarcodeElements.add(sampleBarcodeElement);    

        sampleBarcodeElement = SampleBarcodeElement.createGELSampleBarcode("EDTA#2", "DNA Blood Germline");         
        sampleBarcodeElements.add(sampleBarcodeElement);

        sampleBarcodeElement = SampleBarcodeElement.createGELSampleBarcode("PST", "LiHep Plasma");          
        sampleBarcodeElements.add(sampleBarcodeElement);
        
        sampleBarcodeElement = SampleBarcodeElement.createGELSampleBarcode("EDTA", "EDTA Plasma");          
        sampleBarcodeElements.add(sampleBarcodeElement);


        sampleBarcodeElement = SampleBarcodeElement.createGELSampleBarcode("PAXgene RNA", "RNA Blood");         
        sampleBarcodeElements.add(sampleBarcodeElement);


        sampleBarcodeElement = SampleBarcodeElement.createGELSampleBarcode("SST", "Serum");         
        sampleBarcodeElements.add(sampleBarcodeElement); 


        //build 2dBarcode
        dataMatrixBarcodeFileName = File.createTempFile(UUID.randomUUID().toString(), ".png").getAbsolutePath();
        participant.dataMatrixBarcodeFileName=dataMatrixBarcodeFileName;

        String barcodeString = getSLFDatamatrixBarcodeString(participant);     
        barcode.generateGS1DataMatrix(barcodeString,dataMatrixBarcodeFileName);         
        
        //build empty pdf file
        String pdfFile = File.createTempFile(UUID.randomUUID().toString(), ".pdf").getAbsolutePath(); 
        participant.pdfFile=pdfFile;

    } 

    private  void buildBarcodeElementsCancerTissue (Participant participant) throws Exception  {

        Barcode barcode = new Barcode();
        ArrayList<BarcodeElement> barcodeElements =participant.barcodeElements;
        ArrayList<SampleBarcodeElement> sampleBarcodeElements =participant.sampleBarcodeElements;


        buildCommonBarElements(participant, barcode, barcodeElements);
        //Barcode 3 Hospital Site Code of Multidisciplinary Team Meeting
        File tempFile = File.createTempFile(UUID.randomUUID().toString(), ".png");
        barcode.generateCode128(participant.hospitalSiteCode,tempFile.getAbsolutePath()) ;
        BarcodeElement element = new BarcodeElement("Hospital Site Code of Multidisciplinary Team Meeting",participant.hospitalSiteCode,tempFile.getAbsolutePath());
        barcodeElements.add(2,element);



        //build sampleBarcodes

        SampleBarcodeElement sampleBarcodeElement = SampleBarcodeElement.createGELSampleBarcode("EDTA#1", "DNA FFPE Tumour");            
        sampleBarcodeElements.add(sampleBarcodeElement);    

        sampleBarcodeElement = SampleBarcodeElement.createGELSampleBarcode("EDTA#1", "DNA FF Tumour");         
        sampleBarcodeElements.add(sampleBarcodeElement);
        
        



        //build 2dBarcode
        dataMatrixBarcodeFileName = File.createTempFile(UUID.randomUUID().toString(), ".png").getAbsolutePath();
        participant.dataMatrixBarcodeFileName=dataMatrixBarcodeFileName;

        String barcodeString = getSLFDatamatrixBarcodeString(participant);     
        barcode.generateGS1DataMatrix(barcodeString,dataMatrixBarcodeFileName);         
        //build pdf file
        String pdfFile = File.createTempFile(UUID.randomUUID().toString(), ".pdf").getAbsolutePath(); 
        participant.pdfFile=pdfFile;

    } 
    
    /**
     * Method will create different elements based on instantiation of the class. 
     * @param participant participant that will be populated with  elements needed for SFL
     * @throws Exception
     */
    public void buildBarcodeElements(Participant participant) throws Exception{
        switch (slfType) {
        case RARE_DISEASES:
            buildBarcodeElementsRareDiseases(participant);
            break;
        case CANCER_BLOOD:
            buildBarcodeElementsCancerBlood(participant);
            break;

        case CANCER_TISSUE:
            buildBarcodeElementsCancerTissue(participant);
            break;

        default:
            throw new Exception("Invalid choice for SLFTType");
        }
    }


    public void cleanTempFiles(Participant participant){
        ArrayList<BarcodeElement> barcodeElements =participant.barcodeElements;
        ArrayList<SampleBarcodeElement> sampleBarcodeElements =participant.sampleBarcodeElements;

        for(int i = 0; i< barcodeElements.size(); i++){
            File file = new File(barcodeElements.get(i).fileName);
            file.delete();
        }

        for(int i = 0; i< sampleBarcodeElements.size(); i++){
            File file = new File(sampleBarcodeElements.get(i).fileName);
            file.delete();
        }

        if(dataMatrixBarcodeFileName != null){
            File file = new File(dataMatrixBarcodeFileName);
            if(file.exists()){
                file.delete();
            }
        }

        if(participant.pdfFile != null){
            File file = new File(participant.pdfFile);
            if(file.exists()){
                file.delete();  
            }           
        }

    }



    public String getSLFDatamatrixBarcodeString(Participant participant) throws Exception{

        ArrayList<GS1Element> GS1Codes = new ArrayList<GS1Element>();

        String checkDigit = GS1Formatter.GRSNCheckDigit(GS1Formatter.GEL_GS1_ORGANISATION_PREFIX + participant.nhsNumber);
        //based on http://www.isb.nhs.uk/documents/isb-1077/amd-144-2010/10771442010spec.pdf
        //GSRN consists of GEL_GS1_ORGANISATION_PREFIX + nhsNumber + checkDigit
        GS1Element gsElement = new GS1Element("8018","GEL_GSRN_and_nhsNumber", GS1Formatter.GEL_GS1_ORGANISATION_PREFIX + participant.nhsNumber + checkDigit);
        GS1Codes.add(gsElement);

        GS1Codes.add(new GS1Element("91","participantId", GS1Formatter.removeChars(participant.participantId) ));
        String participantDetails="";
        switch (slfType){
        case RARE_DISEASES:{
            //consists of familyId|clinicId|hospitalNumber|forenames|surname|dob
            participantDetails = GS1Formatter.removeChars(participant.familyId)  + GS1Formatter.PIPE +""
                    +  GS1Formatter.removeChars(participant.clinicId)  + GS1Formatter.PIPE +""
                    +  GS1Formatter.removeChars(participant.hospitalNumber) + GS1Formatter.PIPE +""
                    +  GS1Formatter.removeChars(participant.forenames) + GS1Formatter.PIPE +""
                    +  GS1Formatter.removeChars(participant.surname)   + GS1Formatter.PIPE +""
                    +  GS1Formatter.removeChars(participant.dateOfBirth);
           
        } break;
        case CANCER_BLOOD:{
            //consists of clinicId|hospitalNumber|forenames|surname|dob
            participantDetails =  GS1Formatter.removeChars(participant.clinicId)  + GS1Formatter.PIPE +""
                    +  GS1Formatter.removeChars(participant.hospitalNumber) + GS1Formatter.PIPE +""
                    +  GS1Formatter.removeChars(participant.forenames) + GS1Formatter.PIPE +""
                    +  GS1Formatter.removeChars(participant.surname)   + GS1Formatter.PIPE +""
                    +  GS1Formatter.removeChars(participant.dateOfBirth);
           
        } break;
        case CANCER_TISSUE:{
            //consists of hospitalSiteCode|hospitalNumber|forenames|surname|dob
            participantDetails = GS1Formatter.removeChars(participant.hospitalSiteCode)  + GS1Formatter.PIPE +""
                    +  GS1Formatter.removeChars(participant.hospitalNumber) + GS1Formatter.PIPE +""
                    +  GS1Formatter.removeChars(participant.forenames) + GS1Formatter.PIPE +""
                    +  GS1Formatter.removeChars(participant.surname)   + GS1Formatter.PIPE +""
                    +  GS1Formatter.removeChars(participant.dateOfBirth);
        } break;
        }


        GS1Codes.add(new GS1Element("92","participantDetails",participantDetails));
        GS1Codes.add(new GS1Element("93","diseaseType",participant.diseaseType));
        GS1Codes.add(new GS1Element("94","slf",slfType.getVersion() + GS1Formatter.PIPE + slfType.getDate()));

        //For example, the following code means ( we do not add open/close paranthes in the string in the actual system):
        //(91)1234567890(92)12345(93)100|290|SMITH|John|28/02/2015(94)Rare Disease(95)DNA Blood Germline
        //5 is checkDigit
        //8018000000012345678905<GS>9112345<GS>92100|290|SMITH|John|28/02/2015<GS>93Rare Disease<GS>94DNA Blood Germline

        //nshNumber: 1234567890
        //surname: SMITH
        //forenames: John
        //dob: 01/02/2015
        //particpantId: 12345
        //familyId: 100
        //clinicId: 290
        //diseaseType: Rare Disease
        //sampleType: DNA Blood Germline        
        return GS1Formatter.getBarcodeString(GS1Codes);
    }
}
