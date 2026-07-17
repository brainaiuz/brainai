package com.edatasite.workforce.gwt.backend.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Statistics implements IsSerializable {
    private String companiesCount;
    private String usersCount;
    private String systemAccessCount;
    private String projectCount;
    private String taskCount;
    private String departmentCount;
    private String clientsCount;
    private String issuesCount;
    private String appraisalsCount;
    private String timesheetCount;
    private String leadCount;

    public String getCompaniesCount() {
        return companiesCount;
    }

    public void setCompaniesCount(String companiesCount) {
        this.companiesCount = companiesCount;
    }

    public String getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(String usersCount) {
        this.usersCount = usersCount;
    }

    public String getSystemAccessCount() {
        return systemAccessCount;
    }

    public void setSystemAccessCount(String systemAccessCount) {
        this.systemAccessCount = systemAccessCount;
    }

    public String getProjectCount() {
        return projectCount;
    }

    public void setProjectCount(String projectCount) {
        this.projectCount = projectCount;
    }

    public String getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(String taskCount) {
        this.taskCount = taskCount;
    }

    public String getDepartmentCount() {
        return departmentCount;
    }

    public void setDepartmentCount(String departmentCount) {
        this.departmentCount = departmentCount;
    }

    public String getClientsCount() {
        return clientsCount;
    }

    public void setClientsCount(String clientsCount) {
        this.clientsCount = clientsCount;
    }

    public String getIssuesCount() {
        return issuesCount;
    }

    public void setIssuesCount(String issuesCount) {
        this.issuesCount = issuesCount;
    }

    public String getAppraisalsCount() {
        return appraisalsCount;
    }

    public void setAppraisalsCount(String appraisalsCount) {
        this.appraisalsCount = appraisalsCount;
    }

    public String getTimesheetCount() {
        return timesheetCount;
    }

    public void setTimesheetCount(String timesheetCount) {
        this.timesheetCount = timesheetCount;
    }

    public String getLeadCount() {
        return leadCount;
    }

    public void setLeadCount(String leadCount) {
        this.leadCount = leadCount;
    }
}
