package com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyGovPositionResponseDto {
    private List<MyGovPositionDto> positions;

    public List<MyGovPositionDto> getPositions() {
        return positions;
    }

    public void setPositions(List<MyGovPositionDto> positions) {
        this.positions = positions;
    }
}
