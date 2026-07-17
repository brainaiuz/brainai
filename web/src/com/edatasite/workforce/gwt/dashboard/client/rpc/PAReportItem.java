package com.edatasite.workforce.gwt.dashboard.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: DELL
 * Date: 23-May-2009
 * Time: 07:08:52
 * To change this template use File | Settings | File Templates.
 */
public class PAReportItem implements IsSerializable {

    private String employeeName;
    private String departmentName;
    private Date dateName;
    private Integer groupById;

    private String assessmentName;
    private String initiatorName;
    private String reviewTemplate;
    private Double overallRate;

    public PAReportItem() {
    }

    public PAReportItem(String assName, String initiatorFirstName, String initiatorLastName, String reviewTemplate, Double overallRate, String empleFirstName, String empleLastName, String depName, Date dateName, Integer groupById) {
        this.assessmentName = assName;
        this.initiatorName = initiatorFirstName + " " + initiatorLastName;
        this.reviewTemplate = reviewTemplate;
        this.overallRate = overallRate;
        this.employeeName = empleFirstName + " " + empleLastName;
        this.departmentName = depName;
        this.dateName = new Date(dateName.getTime());
        this.groupById = groupById;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Date getDateName() {
        return dateName;
    }

    public void setDateName(Date dateName) {
        this.dateName = dateName;
    }

    public Integer getGroupById() {
        return groupById;
    }

    public void setGroupById(Integer groupById) {
        this.groupById = groupById;
    }

    public String getAssessmentName() {
        return assessmentName;
    }

    public void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName;
    }

    public String getInitiatorName() {
        return initiatorName;
    }

    public void setInitiatorName(String initiatorName) {
        this.initiatorName = initiatorName;
    }

    public String getReviewTemplate() {
        return reviewTemplate;
    }

    public void setReviewTemplate(String reviewTemplate) {
        this.reviewTemplate = reviewTemplate;
    }

    public Double getOverallRate() {
        return overallRate;
    }

    public void setOverallRate(Double overallRate) {
        this.overallRate = overallRate;
    }

}
