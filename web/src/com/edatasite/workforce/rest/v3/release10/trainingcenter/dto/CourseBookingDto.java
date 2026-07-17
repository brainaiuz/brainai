package com.edatasite.workforce.rest.v3.release10.trainingcenter.dto;

public class CourseBookingDto {
    private String statusCode;
    private Integer statusId;
    private Integer typeId;
    private Integer locaitionId;
    private Integer crmAccountId;
    private Integer scheduledCourseId;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getLocaitionId() {
        return locaitionId;
    }

    public void setLocaitionId(Integer locaition) {
        this.locaitionId = locaition;
    }

    public Integer getScheduledCourseId() {
        return scheduledCourseId;
    }

    public void setScheduledCourseId(Integer scheduledCourseId) {
        this.scheduledCourseId = scheduledCourseId;
    }

    public Integer getCrmAccountId() {
        return crmAccountId;
    }

    public void setCrmAccountId(Integer crmAccountId) {
        this.crmAccountId = crmAccountId;
    }
}
