package com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyGovSalaryResponseDto {
    private MyGovSalaryDataDto data;

    public MyGovSalaryDataDto getData() {
        return data;
    }

    public void setData(MyGovSalaryDataDto data) {
        this.data = data;
    }
}
