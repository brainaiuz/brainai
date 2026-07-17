package com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyGovMarriageResponseDto {
    private List<MyGovMarriageDto> items;

    public List<MyGovMarriageDto> getItems() {
        return items;
    }

    public void setItems(List<MyGovMarriageDto> items) {
        this.items = items;
    }
}
