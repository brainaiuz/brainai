package com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyGovAddressDto {
    private IdValueDto country;
    private IdValueDto region;
    private IdValueDto district;
    private String address;

    public IdValueDto getCountry() {
        return country;
    }

    public void setCountry(IdValueDto country) {
        this.country = country;
    }

    public IdValueDto getRegion() {
        return region;
    }

    public void setRegion(IdValueDto region) {
        this.region = region;
    }

    public IdValueDto getDistrict() {
        return district;
    }

    public void setDistrict(IdValueDto district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
