package com.edatasite.workforce.rest.v3.release10.crm.dto;

public class GymCrmAccountTO {
    private Integer crmAccountId;
    private Integer contactId;
    private String sessionId;
    private Integer studentId;

    public GymCrmAccountTO(Integer crmAccountId, Integer contactId, String sessionId, Integer studentId) {
        this.crmAccountId = crmAccountId;
        this.contactId = contactId;
        this.sessionId = sessionId;
        this.studentId = studentId;
    }

    public GymCrmAccountTO() {
    }

    public Integer getCrmAccountId() {
        return crmAccountId;
    }

    public Integer getContactId() {
        return contactId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Integer getStudentId() {
        return studentId;
    }
}
