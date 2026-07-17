package com.edatasite.workforce.rest.v3.release10.hrms.dto;

import java.util.List;

public class HHAreaDto {
    private String id;
    private List<HHIdDto> areas;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<HHIdDto> getAreas() {
        return areas;
    }

    public void setAreas(List<HHIdDto> areas) {
        this.areas = areas;
    }
}
