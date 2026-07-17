package com.edatasite.workforce.rest.v3.release10.hrms.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GTLTripDemoDto {
    private TripDemoRequestDto json;
    private String OrdDocumentId;
    private String kpiDocumentId;
    private String kpiDocumentType;

    public GTLTripDemoDto() {
    }

    public GTLTripDemoDto(TripDemoRequestDto json, String ordDocumentId) {
        this.json = json;
        OrdDocumentId = ordDocumentId;
    }

    public TripDemoRequestDto getJson() {
        return json;
    }

    public void setJson(TripDemoRequestDto json) {
        this.json = json;
    }

    public String getOrdDocumentId() {
        return OrdDocumentId;
    }

    public void setOrdDocumentId(String ordDocumentId) {
        OrdDocumentId = ordDocumentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GTLTripDemoDto)) return false;

        GTLTripDemoDto that = (GTLTripDemoDto) o;

        if (json != null ? !json.equals(that.json) : that.json != null) return false;
        if (OrdDocumentId != null ? !OrdDocumentId.equals(that.OrdDocumentId) : that.OrdDocumentId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = json != null ? json.hashCode() : 0;
        result = 31 * result + (OrdDocumentId != null ? OrdDocumentId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GTLTripDemoDto{" +
                "json=" + json +
                ", OrdDocumentId='" + OrdDocumentId + '\'' +
                '}';
    }

    public String getKpiDocumentId() {
        return kpiDocumentId;
    }

    public void setKpiDocumentId(String kpiDocumentId) {
        this.kpiDocumentId = kpiDocumentId;
    }

    public String getKpiDocumentType() {
        return kpiDocumentType;
    }

    public void setKpiDocumentType(String kpiDocumentType) {
        this.kpiDocumentType = kpiDocumentType;
    }
}
