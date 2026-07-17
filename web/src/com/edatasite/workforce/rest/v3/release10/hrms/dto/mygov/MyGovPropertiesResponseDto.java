package com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyGovPropertiesResponseDto {
    private List<MyGovPropertyDto> data;
    private List<MyGovCarDto> data_car;

    public List<MyGovPropertyDto> getData() {
        return data;
    }

    public void setData(List<MyGovPropertyDto> data) {
        this.data = data;
    }

    public List<MyGovCarDto> getData_car() {
        return data_car;
    }

    public void setData_car(List<MyGovCarDto> data_car) {
        this.data_car = data_car;
    }
}
