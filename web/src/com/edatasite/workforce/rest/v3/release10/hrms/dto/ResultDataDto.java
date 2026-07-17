package com.edatasite.workforce.rest.v3.release10.hrms.dto;

import java.util.List;

public class ResultDataDto {
    private List<ExperienceDto> experiences;

    public List<ExperienceDto> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<ExperienceDto> experiences) {
        this.experiences = experiences;
    }
}
