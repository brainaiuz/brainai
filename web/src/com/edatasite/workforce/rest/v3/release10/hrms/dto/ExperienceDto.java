package com.edatasite.workforce.rest.v3.release10.hrms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExperienceDto {
    @JsonProperty("company_name")
    private String companyName;
    @JsonProperty("contract_date")
    private String contractDate;
    @JsonProperty("end_date")
    private String endDate;
    @JsonProperty("position_name")
    private String positionName;


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContractDate() {
        return contractDate;
    }

    public void setContractDate(String contractDate) {
        this.contractDate = contractDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
}
