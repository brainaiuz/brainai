package com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyGovExperienceResponseDto {
    private MyGovExperienceResultDto result;

    public MyGovExperienceResultDto getResult() {
        return result;
    }

    public void setResult(MyGovExperienceResultDto result) {
        this.result = result;
    }
}
