package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.EmployeeStepItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * User: abror
 * Date: 2/24/15 2:42 PM
 */
public class RemindersDataRpc implements IsSerializable {
    private Integer ObjectId;
    private SelectItem[] customField;
    private ArrayList<SelectItem> emailTemplate;
    private ListingFilterParameter filterParameter;
    private ArrayList<SelectItem> role = new ArrayList<>();
    private ArrayList<SelectItem> workflowRule = new ArrayList<>();
    private String categoryCode;
    private ArrayList<EmployeeStepItem> employeeStepItem;

    public Integer getObjectId() {
        return ObjectId;
    }

    public void setObjectId(Integer objectId) {
        ObjectId = objectId;
    }

    public SelectItem[] getCustomField() {
        return customField;
    }

    public void setCustomField(SelectItem[] customField) {
        this.customField = customField;
    }

    public ArrayList<SelectItem> getEmailTemplate() {
        return emailTemplate;
    }

    public void setEmailTemplate(ArrayList<SelectItem> emailTemplate) {
        this.emailTemplate = emailTemplate;
    }

    public ListingFilterParameter getFilterParameter() {
        return filterParameter;
    }

    public void setFilterParameter(ListingFilterParameter filterParameter) {
        this.filterParameter = filterParameter;
    }

    public ArrayList<SelectItem> getRole() {
        return role;
    }

    public void setRole(ArrayList<SelectItem> role) {
        this.role = role;
    }

    public ArrayList<SelectItem> getWorkflowRule() {
        return workflowRule;
    }

    public void setWorkflowRule(ArrayList<SelectItem> workflowRule) {
        this.workflowRule = workflowRule;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public ArrayList<EmployeeStepItem> getEmployeeStepItem() {
        return employeeStepItem;
    }

    public void setEmployeeStepItem(ArrayList<EmployeeStepItem> employeeStepItem) {
        this.employeeStepItem = employeeStepItem;
    }
}
