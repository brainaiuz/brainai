package com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyGovExperienceResultDto {
    private MyGovExperienceListDto data;

    public MyGovExperienceListDto getData() {
        return data;
    }

    public void setData(MyGovExperienceListDto data) {
        this.data = data;
    }
}
