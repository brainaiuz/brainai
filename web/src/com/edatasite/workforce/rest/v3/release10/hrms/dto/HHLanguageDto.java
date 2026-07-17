package com.edatasite.workforce.rest.v3.release10.hrms.dto;

public class HHLanguageDto {
    private String id;
    private HHIdDto level;

    public HHLanguageDto() {
    }

    public HHLanguageDto(String id, HHIdDto level) {
        this.id = id;
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HHIdDto getLevel() {
        return level;
    }

    public void setLevel(HHIdDto level) {
        this.level = level;
    }
}
