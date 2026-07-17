package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.Pattern;

public class AddressDto {
    @Pattern(regexp = "HOME|WORK|OTHER", message = "addressType must be one of HOME/WORK/OTHER")
    private String addressType;
    private String name;
    private String addressLine;
    private String addressLine2;
    private String city;
    private String country;
    private String state;
    private String postcode;
    private boolean primary;

    @JsonIgnore
    private String countryCode;
    @JsonIgnore
    private Integer stateId;
    @JsonIgnore
    private Integer countryId;
    @JsonIgnore
    private Integer entityId;
    @JsonIgnore
    private String entityType;

    public AddressDto() {
    }

    public AddressDto(String addressType, String name, String addressLine, String addressLine2, String city, String country, String state, String postcode, boolean primary, String countryCode, Integer stateId, Integer countryId, Integer entityId, String entityType) {
        this.addressType = addressType;
        this.name = name;
        this.addressLine = addressLine;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.country = country;
        this.state = state;
        this.postcode = postcode;
        this.primary = primary;
        this.countryCode = countryCode;
        this.stateId = stateId;
        this.countryId = countryId;
        this.entityId = entityId;
        this.entityType = entityType;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressDto)) return false;

        AddressDto that = (AddressDto) o;

        if (isPrimary() != that.isPrimary()) return false;
        if (getAddressType() != null ? !getAddressType().equals(that.getAddressType()) : that.getAddressType() != null)
            return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getAddressLine() != null ? !getAddressLine().equals(that.getAddressLine()) : that.getAddressLine() != null)
            return false;
        if (getAddressLine2() != null ? !getAddressLine2().equals(that.getAddressLine2()) : that.getAddressLine2() != null)
            return false;
        if (getCity() != null ? !getCity().equals(that.getCity()) : that.getCity() != null) return false;
        if (getCountry() != null ? !getCountry().equals(that.getCountry()) : that.getCountry() != null) return false;
        if (getState() != null ? !getState().equals(that.getState()) : that.getState() != null) return false;
        if (getPostcode() != null ? !getPostcode().equals(that.getPostcode()) : that.getPostcode() != null)
            return false;
        if (getCountryCode() != null ? !getCountryCode().equals(that.getCountryCode()) : that.getCountryCode() != null)
            return false;
        if (getStateId() != null ? !getStateId().equals(that.getStateId()) : that.getStateId() != null) return false;
        if (getCountryId() != null ? !getCountryId().equals(that.getCountryId()) : that.getCountryId() != null)
            return false;
        if (getEntityId() != null ? !getEntityId().equals(that.getEntityId()) : that.getEntityId() != null)
            return false;
        if (getEntityType() != null ? !getEntityType().equals(that.getEntityType()) : that.getEntityType() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getAddressType() != null ? getAddressType().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getAddressLine() != null ? getAddressLine().hashCode() : 0);
        result = 31 * result + (getAddressLine2() != null ? getAddressLine2().hashCode() : 0);
        result = 31 * result + (getCity() != null ? getCity().hashCode() : 0);
        result = 31 * result + (getCountry() != null ? getCountry().hashCode() : 0);
        result = 31 * result + (getState() != null ? getState().hashCode() : 0);
        result = 31 * result + (getPostcode() != null ? getPostcode().hashCode() : 0);
        result = 31 * result + (isPrimary() ? 1 : 0);
        result = 31 * result + (getCountryCode() != null ? getCountryCode().hashCode() : 0);
        result = 31 * result + (getStateId() != null ? getStateId().hashCode() : 0);
        result = 31 * result + (getCountryId() != null ? getCountryId().hashCode() : 0);
        result = 31 * result + (getEntityId() != null ? getEntityId().hashCode() : 0);
        result = 31 * result + (getEntityType() != null ? getEntityType().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AddressDto{" +
                "addressType='" + addressType + '\'' +
                ", name='" + name + '\'' +
                ", addressLine='" + addressLine + '\'' +
                ", addressLine2='" + addressLine2 + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", postcode='" + postcode + '\'' +
                ", primary=" + primary +
                ", countryCode='" + countryCode + '\'' +
                ", stateId=" + stateId +
                ", countryId=" + countryId +
                ", entityId=" + entityId +
                ", entityType='" + entityType + '\'' +
                '}';
    }
}
