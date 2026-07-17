package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;

public class GroupPlacementTableItem implements IsSerializable {
    private Integer objectID;
    private Integer employeeId;
    private SelectItem employee;
    private SelectItem location;
    private Integer type;
    private SelectItem candidate;
    private SelectItem department;
    private SelectItem position;
    private SelectItem matchedVacancy;
    private Date effectiveDate;
    private ArrayList<CompanyCustomFieldItem> itemCustomFields;


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public SelectItem getEmployee() {
        return employee;
    }

    public void setEmployee(SelectItem employee) {
        this.employee = employee;
    }

    public SelectItem getLocation() {
        return location;
    }

    public void setLocation(SelectItem location) {
        this.location = location;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public SelectItem getCandidate() {
        return candidate;
    }

    public void setCandidate(SelectItem candidate) {
        this.candidate = candidate;
    }

    public SelectItem getDepartment() {
        return department;
    }

    public void setDepartment(SelectItem department) {
        this.department = department;
    }

    public SelectItem getPosition() {
        return position;
    }

    public void setPosition(SelectItem position) {
        this.position = position;
    }

    public SelectItem getMatchedVacancy() {
        return matchedVacancy;
    }

    public void setMatchedVacancy(SelectItem matchedVacancy) {
        this.matchedVacancy = matchedVacancy;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public ArrayList<CompanyCustomFieldItem> getItemCustomFields() {
        return itemCustomFields;
    }

    public void setItemCustomFields(ArrayList<CompanyCustomFieldItem> itemCustomFields) {
        this.itemCustomFields = itemCustomFields;
    }
}
