package com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.LinkedHashMap;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyGovResponseDto {
    private List<LinkedHashMap<String, Object>> items;

    public List<LinkedHashMap<String, Object>> getItems() {
        return items;
    }

    public void setItems(List<LinkedHashMap<String, Object>> items) {
        this.items = items;
    }
}
