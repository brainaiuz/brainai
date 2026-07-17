package com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyGovSalaryDataDto {
    private List<MyGovSalaryDto> salaries;

    public List<MyGovSalaryDto> getSalaries() {
        return salaries;
    }

    public void setSalaries(List<MyGovSalaryDto> salaries) {
        this.salaries = salaries;
    }
}
