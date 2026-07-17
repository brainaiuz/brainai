package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.ArrayList;

public class RotationTableItem extends SelectItem {
    private Integer itemId;
    private Integer employeeId;
    private SelectItem employee;
    private SelectItem currentLocation;
    private SelectItem newLocation;
    private SelectItem currentDepartment;
    private SelectItem currentPosition;
    private SelectItem newDepartment;
    private SelectItem newPosition;
    private ArrayList<CompanyCustomFieldItem> itemCustomFields;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
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

    public SelectItem getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(SelectItem currentLocation) {
        this.currentLocation = currentLocation;
    }

    public SelectItem getNewLocation() {
        return newLocation;
    }

    public void setNewLocation(SelectItem newLocation) {
        this.newLocation = newLocation;
    }

    public SelectItem getCurrentDepartment() {
        return currentDepartment;
    }

    public void setCurrentDepartment(SelectItem currentDepartment) {
        this.currentDepartment = currentDepartment;
    }

    public SelectItem getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(SelectItem currentPosition) {
        this.currentPosition = currentPosition;
    }

    public SelectItem getNewDepartment() {
        return newDepartment;
    }

    public void setNewDepartment(SelectItem newDepartment) {
        this.newDepartment = newDepartment;
    }

    public SelectItem getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(SelectItem newPosition) {
        this.newPosition = newPosition;
    }

    public ArrayList<CompanyCustomFieldItem> getItemCustomFields() {
        return itemCustomFields;
    }

    public void setItemCustomFields(ArrayList<CompanyCustomFieldItem> itemCustomFields) {
        this.itemCustomFields = itemCustomFields;
    }
}
