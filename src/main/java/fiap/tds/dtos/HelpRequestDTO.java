package fiap.tds.dtos;

// This class will be used for the client to send help requests
public class HelpRequestDTO {
    private String cep;
    private String notes;
    private String contactInfo;

    public HelpRequestDTO() {
    }

    public HelpRequestDTO(String cep, String notes, String contactInfo) {
        this.cep = cep;
        this.notes = notes;
        this.contactInfo = contactInfo;
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
}
