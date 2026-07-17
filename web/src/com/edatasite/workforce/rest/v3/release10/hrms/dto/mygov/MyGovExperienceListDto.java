package com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyGovExperienceListDto {
    private List<MyGovExperienceDto> experiences;

    public List<MyGovExperienceDto> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<MyGovExperienceDto> experiences) {
        this.experiences = experiences;
    }
}
