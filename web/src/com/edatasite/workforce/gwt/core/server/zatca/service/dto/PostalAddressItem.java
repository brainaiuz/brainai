package com.edatasite.workforce.gwt.core.server.zatca.service.dto;

public class PostalAddressItem {
    private String streetName;
    private String buildingName;
    private String plotIdentification;
    private String citySubdivision;
    private String cityName;
    private String postalZone;
    private String countrySubentity;
    private String countryIdentificationCode;

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getPlotIdentification() {
        return plotIdentification;
    }

    public void setPlotIdentification(String plotIdentification) {
        this.plotIdentification = plotIdentification;
    }

    public String getCitySubdivision() {
        return citySubdivision;
    }

    public void setCitySubdivision(String citySubdivision) {
        this.citySubdivision = citySubdivision;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPostalZone() {
        return postalZone;
    }

    public void setPostalZone(String postalZone) {
        this.postalZone = postalZone;
    }

    public String getCountrySubentity() {
        return countrySubentity;
    }

    public void setCountrySubentity(String countrySubentity) {
        this.countrySubentity = countrySubentity;
    }

    public String getCountryIdentificationCode() {
        return countryIdentificationCode;
    }

    public void setCountryIdentificationCode(String countryIdentificationCode) {
        this.countryIdentificationCode = countryIdentificationCode;
    }
}
