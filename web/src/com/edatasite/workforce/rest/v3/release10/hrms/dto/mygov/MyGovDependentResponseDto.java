package com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyGovDependentResponseDto {
    private List<MyGovDependentDto> items;

    public List<MyGovDependentDto> getItems() {
        return items;
    }

    public void setItems(List<MyGovDependentDto> items) {
        this.items = items;
    }
}
