package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HasApprovers;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;

public class RotationItem extends HasApprovers implements IsSerializable, ListingCustomFields {
    public static final String APPROVER = "approver";
    public static String NUMBER = "rotationCode";
    public static String STATUS = "statusCode";
    public static String EMPLOYEE = "employeeName";
    public static String DATE = "date";
    public static String CREATOR = "creator";
    public static String UPDATER = "updater";
    public static String CREATED_DATE = "createdDate";
    public static String UPDATED_DATE = "updatedDate";
    public static String FROM_DEPARTMENT = "fromDepartment";
    public static String NEW_DEPARTMENT = "newDepartment";
    public static String FROM_POSITION = "fromPosition";
    private Integer id;
    private Integer employeeId;
    private Integer currentDepartmentId;
    private Integer newDepartmentId;
    private Integer currentPositionId;
    private Integer newPositionId;
    private String rotationCode;
    private String employeeName;
    private DateNonConvertable date;
    private SelectItem curLocation;
    private SelectItem curDepartment;
    private SelectItem curPosition;
    private SelectItem emplooyee;
    private SelectItem newLocation;
    private SelectItem newDepartment;
    private SelectItem newPosition;
    private NumberData numberData;
    private String statusCode;
    private Boolean isApprover;
    private DateNonConvertable approvedDate;
    private DateNonConvertable createdDate;
    private DateNonConvertable updatedDate;
    private SelectItem creator;
    private SelectItem updater;
    private SelectItem approverEmployee;
    private ColumnConfigs[] columnConfigs;
    private RotationTableItem[] rotationTableItems;

    private ArrayList<CompanyCustomFieldItem> customFieldItems;

    private HashMap<String, CompanyCustomFieldItem> employeeCustomFields;
    private HashMap<String, Object> customFieldValues;
    private SelectItem[] templates;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getCurrentDepartmentId() {
        return currentDepartmentId;
    }

    public void setCurrentDepartmentId(Integer currentDepartmentId) {
        this.currentDepartmentId = currentDepartmentId;
    }

    public Integer getNewDepartmentId() {
        return newDepartmentId;
    }

    public void setNewDepartmentId(Integer newDepartmentId) {
        this.newDepartmentId = newDepartmentId;
    }

    public Integer getCurrentPositionId() {
        return currentPositionId;
    }

    public void setCurrentPositionId(Integer currentPositionId) {
        this.currentPositionId = currentPositionId;
    }

    public Integer getNewPositionId() {
        return newPositionId;
    }

    public void setNewPositionId(Integer newPositionId) {
        this.newPositionId = newPositionId;
    }

    public String getRotationCode() {
        return rotationCode;
    }

    public void setRotationCode(String rotationCode) {
        this.rotationCode = rotationCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public SelectItem getCurLocation() {
        return curLocation;
    }

    public void setCurLocation(SelectItem curLocation) {
        this.curLocation = curLocation;
    }

    public SelectItem getCurDepartment() {
        return curDepartment;
    }

    public void setCurDepartment(SelectItem curDepartment) {
        this.curDepartment = curDepartment;
    }

    public SelectItem getCurPosition() {
        return curPosition;
    }

    public void setCurPosition(SelectItem curPosition) {
        this.curPosition = curPosition;
    }

    public SelectItem getEmplooyee() {
        return emplooyee;
    }

    public void setEmplooyee(SelectItem emplooyee) {
        this.emplooyee = emplooyee;
    }

    public SelectItem getNewLocation() {
        return newLocation;
    }

    public void setNewLocation(SelectItem newLocation) {
        this.newLocation = newLocation;
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

    public SelectItem getCurrentApproverAsSelectItem() {
        if (getCurrentApprover() != null) {
            return getCurrentApprover().getExactEmployee();
        }
        return null;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Boolean isApprover() {
        return isApprover;
    }

    public Boolean getApprover() {
        return isApprover;
    }

    public void setApprover(Boolean approver) {
        isApprover = approver;
    }

    public DateNonConvertable getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(DateNonConvertable approvedDate) {
        this.approvedDate = approvedDate;
    }

    public DateNonConvertable getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateNonConvertable createdDate) {
        this.createdDate = createdDate;
    }

    public DateNonConvertable getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(DateNonConvertable updatedDate) {
        this.updatedDate = updatedDate;
    }

    public SelectItem getCreator() {
        return creator;
    }

    public void setCreator(SelectItem creator) {
        this.creator = creator;
    }

    public SelectItem getUpdater() {
        return updater;
    }

    public void setUpdater(SelectItem updater) {
        this.updater = updater;
    }

    public SelectItem getApproverEmployee() {
        return approverEmployee;
    }

    public void setApproverEmployee(SelectItem approverEmployee) {
        this.approverEmployee = approverEmployee;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public HashMap<String, Object> getCustomFieldValuesItems() {
        return customFieldValues;
    }

    public void setCustomFieldValuesItems(HashMap<String, Object> customFieldValues) {
        this.customFieldValues = customFieldValues;
    }


    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return customFieldValues.get(columnCodeKey);
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        customFieldValues.put(columnCodeKey, cellValue);
    }

    public ColumnConfigs[] getColumnConfigs() {
        return columnConfigs;
    }

    public void setColumnConfigs(ColumnConfigs[] columnConfigs) {
        this.columnConfigs = columnConfigs;
    }

    public HashMap<String, Object> getCustomFieldValues() {
        return customFieldValues;
    }

    public void setCustomFieldValues(HashMap<String, Object> customFieldValues) {
        this.customFieldValues = customFieldValues;
    }

    public SelectItem[] getTemplates() {
        return templates;
    }

    public void setTemplates(SelectItem[] templates) {
        this.templates = templates;
    }

    public RotationTableItem[] getRotationTableItems() {
        return rotationTableItems;
    }

    public void setRotationTableItems(RotationTableItem[] rotationTableItems) {
        this.rotationTableItems = rotationTableItems;
    }

    public HashMap<String, CompanyCustomFieldItem> getEmployeeCustomFields() {
        return employeeCustomFields;
    }

    public void setEmployeeCustomFields(HashMap<String, CompanyCustomFieldItem> employeeCustomFields) {
        this.employeeCustomFields = employeeCustomFields;
    }
}
