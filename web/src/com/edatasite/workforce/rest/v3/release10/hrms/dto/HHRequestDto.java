package com.edatasite.workforce.rest.v3.release10.hrms.dto;

import java.util.List;

public class HHRequestDto {
    private String name;
    private String code;
    private String description;
    private HHSalaryDto salary;
    private List<HHIdDto> areas;
    private HHIdDto address;
    private HHIdDto experience;
    private List<HHLanguageDto> languages;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HHSalaryDto getSalary() {
        return salary;
    }

    public void setSalary(HHSalaryDto salary) {
        this.salary = salary;
    }

    public List<HHIdDto> getAreas() {
        return areas;
    }

    public void setAreas(List<HHIdDto> areas) {
        this.areas = areas;
    }

    public HHIdDto getAddress() {
        return address;
    }

    public void setAddress(HHIdDto address) {
        this.address = address;
    }

    public HHIdDto getExperience() {
        return experience;
    }

    public void setExperience(HHIdDto experience) {
        this.experience = experience;
    }

    public List<HHLanguageDto> getLanguages() {
        return languages;
    }

    public void setLanguages(List<HHLanguageDto> languages) {
        this.languages = languages;
    }
}
