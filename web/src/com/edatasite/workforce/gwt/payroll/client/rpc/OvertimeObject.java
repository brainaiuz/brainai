package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HasApprovers;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OvertimeObject extends HasApprovers implements Serializable {
    public static final String PERIOD = "period";
    public static final String STATUS = "status";
    public static final String REFERENCE = "reference";
    public static final String APPROVER = "approver";
    public static final String CREATOR = "creator";
    public static final String TOTAL = "total";
    public static final String CATEGORY = "category";

    private Integer id;
    private SelectItem selectedDepartment;
    private SelectItem category;
    private DateNonConvertable date;
    private List<OvertimeObjectData> items;
    private SelectItem selectedEmployee;
    private SelectItem creator;
    private SelectItem updater;
    private SelectItem approverEmployee;
    private SelectItem payrollBatch;
    private DateNonConvertable createdDate;
    private DateNonConvertable updatedDate;
    private String status;
    private String statusCode;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private Boolean isApprover;
    private DateNonConvertable approvedDate;
    private String overtimeType;
    private BigDecimal defaultHours;
    private Integer intNumber;
    private String code;
    private NumberData numberData;
    private Boolean applyForSubDepartment;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SelectItem getSelectedDepartment() {
        return selectedDepartment;
    }

    public void setSelectedDepartment(SelectItem selectedDepartment) {
        this.selectedDepartment = selectedDepartment;
    }

    public SelectItem getCategory() {
        return category;
    }

    public void setCategory(SelectItem category) {
        this.category = category;
    }

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public List<OvertimeObjectData> getItems() {
        return items;
    }

    public void setItems(List<OvertimeObjectData> items) {
        this.items = items;
    }

    public SelectItem getSelectedEmployee() {
        return selectedEmployee;
    }

    public void setSelectedEmployee(SelectItem selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
    }

    public SelectItem getCreator() {
        return this.creator;
    }

    public void setCreator(SelectItem creator) {
        this.creator = creator;
    }

    public DateNonConvertable getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateNonConvertable createdDate) {
        this.createdDate = createdDate;
    }

    public SelectItem getUpdater() {
        return updater;
    }

    public void setUpdater(SelectItem updater) {
        this.updater = updater;
    }

    public DateNonConvertable getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(DateNonConvertable updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public SelectItem getCurrentApproverAsSelectItem() {
        if (getCurrentApprover() != null) {
            return getCurrentApprover().getExactEmployee();
        }
        return null;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public Boolean isApprover() {
        return isApprover;
    }

    public void setApprover(Boolean approver) {
        isApprover = approver;
    }

    public SelectItem getApproverEmployee() {
        return approverEmployee;
    }

    public void setApproverEmployee(SelectItem approverEmployee) {
        this.approverEmployee = approverEmployee;
    }

    public DateNonConvertable getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(DateNonConvertable approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getOvertimeType() {
        return overtimeType;
    }

    public void setOvertimeType(String overtimeType) {
        this.overtimeType = overtimeType;
    }

    public SelectItem getPayrollBatch() {
        return payrollBatch;
    }

    public void setPayrollBatch(SelectItem payrollBatch) {
        this.payrollBatch = payrollBatch;
    }

    public BigDecimal getDefaultHours() {
        return defaultHours;
    }

    public void setDefaultHours(BigDecimal defaultHours) {
        this.defaultHours = defaultHours;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public Boolean getApplyForSubDepartment() {
        return applyForSubDepartment;
    }

    public void setApplyForSubDepartment(Boolean applyForSubDepartment) {
        this.applyForSubDepartment = applyForSubDepartment;
    }
}
