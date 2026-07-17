package com.edatasite.workforce.rest.v2.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

public class ProjectTasksTO extends ResponseData {

    private Integer projectId;
    private String projectNumber;
    private String projectName;
    private Integer customerId;
    private String customerName;
    private ArrayList<TaskEmployeesListTO> tasks;

    public ProjectTasksTO() {
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public ArrayList<TaskEmployeesListTO> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<TaskEmployeesListTO> tasks) {
        this.tasks = tasks;
    }
}
