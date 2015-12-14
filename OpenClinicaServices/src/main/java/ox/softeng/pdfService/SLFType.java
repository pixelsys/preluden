package ox.softeng.pdfService;

public enum SLFType {

    RARE_DISEASES("1.1.1", "03/03/2015"), 
    CANCER_BLOOD("1.0.1", "01/09/2015"), 
    CANCER_TISSUE("1.0.0", "01/07/2015");

    private String version;
    private String date;

    private SLFType(String version, String date) {
        this.date = date;
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public String getDate() {
        return date;
    }

}
