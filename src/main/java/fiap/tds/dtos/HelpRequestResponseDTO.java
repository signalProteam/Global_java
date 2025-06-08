package fiap.tds.dtos;

public class HelpRequestResponseDTO {
    private Long id;
    private String cep;
    private String notes;
    private String contactInfo;
    private double latitude;
    private double longitude;
    private String status;  // novo campo

    public HelpRequestResponseDTO() {}

    public HelpRequestResponseDTO(Long id, String cep, String notes, String contactInfo, double latitude, double longitude, String status) {
        this.id = id;
        this.cep = cep;
        this.notes = notes;
        this.contactInfo = contactInfo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
