package com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyGovAddressResponseDto {
    private MyGovAddressDto permanentRegistration;

    public MyGovAddressDto getPermanentRegistration() {
        return permanentRegistration;
    }

    public void setPermanentRegistration(MyGovAddressDto permanentRegistration) {
        this.permanentRegistration = permanentRegistration;
    }
}
