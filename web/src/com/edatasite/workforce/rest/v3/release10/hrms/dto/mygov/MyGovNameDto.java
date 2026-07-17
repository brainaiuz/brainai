package com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyGovNameDto {
    private String nameLatin;
    private String surnameLatin;
    private String patronymLatin;

    public String getNameLatin() {
        return nameLatin;
    }

    public void setNameLatin(String nameLatin) {
        this.nameLatin = nameLatin;
    }

    public String getSurnameLatin() {
        return surnameLatin;
    }

    public void setSurnameLatin(String surnameLatin) {
        this.surnameLatin = surnameLatin;
    }

    public String getPatronymLatin() {
        return patronymLatin;
    }

    public void setPatronymLatin(String patronymLatin) {
        this.patronymLatin = patronymLatin;
    }
}
