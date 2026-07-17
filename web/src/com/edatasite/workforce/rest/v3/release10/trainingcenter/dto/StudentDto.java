package com.edatasite.workforce.rest.v3.release10.trainingcenter.dto;

public class StudentDto {

    public StudentDto() {
    }

    public StudentDto(Integer crmAccountId, Integer contactId) {
        this.crmAccountId = crmAccountId;
        this.contactId = contactId;
    }

    private Integer crmAccountId;

    private Integer contactId;

    public Integer getCrmAccountId() {
        return crmAccountId;
    }

    public void setCrmAccountId(Integer crmAccountId) {
        this.crmAccountId = crmAccountId;
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }
}
