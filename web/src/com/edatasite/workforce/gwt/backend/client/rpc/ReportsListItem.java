package com.edatasite.workforce.gwt.backend.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Virus
 * Date: 2/20/12
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportsListItem implements IsSerializable {
    private Integer id;
    private String name;
    private String exception;
    private Integer companyID;
    private Boolean isSuccess;
    private String viewName;
    private Boolean isDefault;
    private Integer reportID;

    public ReportsListItem() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public Boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public void setDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public Integer getReportID() {
        return reportID;
    }

    public void setReportID(Integer reportID) {
        this.reportID = reportID;
    }
}
