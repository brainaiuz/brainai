package com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyGovPassportResponseDto {
    private MyGovPassportDto currentDocument;
    private Integer sex;
    private String photo;
    private MyGovNameDto name;
    private IdValueDto nationality;
    private MyGovBirthDto birth;

    public MyGovPassportDto getCurrentDocument() {
        return currentDocument;
    }

    public void setCurrentDocument(MyGovPassportDto currentDocument) {
        this.currentDocument = currentDocument;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public MyGovNameDto getName() {
        return name;
    }

    public void setName(MyGovNameDto name) {
        this.name = name;
    }

    public IdValueDto getNationality() {
        return nationality;
    }

    public void setNationality(IdValueDto nationality) {
        this.nationality = nationality;
    }

    public MyGovBirthDto getBirth() {
        return birth;
    }

    public void setBirth(MyGovBirthDto birth) {
        this.birth = birth;
    }
}
