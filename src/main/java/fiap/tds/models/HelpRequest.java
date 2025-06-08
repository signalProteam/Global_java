package fiap.tds.models;

import java.time.LocalDateTime;
import java.util.Objects;


public class HelpRequest {

    private Long id;
    private String cep;
    private double latitude;
    private double longitude;
    private LocalDateTime requestTimestamp;
    private Status status; //
    private String notes;
    private String enderecoAproximado;
    private String contactInfo;
    private Long updateBy;
    private Long userId;


    public HelpRequest() {
    }


    public HelpRequest(Long id, String cep, double latitude, double longitude, LocalDateTime requestTimestamp, Status status, String notes, String enderecoAproximado, String contactInfo, Long updateBy, Long userId) {
        this.id = id;
        this.cep = cep;
        this.latitude = latitude;
        this.longitude = longitude;
        this.requestTimestamp = requestTimestamp;
        this.status = status;
        this.notes = notes;
        this.enderecoAproximado = enderecoAproximado;
        this.contactInfo = contactInfo;
        this.updateBy = updateBy;
        this.userId = userId;
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

    public LocalDateTime getRequestTimestamp() {
        return requestTimestamp;
    }

    public void setRequestTimestamp(LocalDateTime requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getEnderecoAproximado() {
        return enderecoAproximado;
    }

    public void setEnderecoAproximado(String enderecoAproximado) {
        this.enderecoAproximado = enderecoAproximado;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}