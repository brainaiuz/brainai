package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HasApprovers;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.BackupEmployeeItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BackupsEmployeeObject extends HasApprovers implements Serializable {

    private Integer id;
    private DateNonConvertable date;
    private SelectItem selectedEmployee;
    private SelectItem position;
    private SelectItem department;
    private SelectItem creator;
    private SelectItem updater;
    private SelectItem approverEmployee;
    private DateNonConvertable createdDate;
    private DateNonConvertable updatedDate;

    private String code;
    private String status;
    private String statusCode;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private Boolean isApprover;
    private DateNonConvertable approvedDate;
    private NumberData numberData;

    private Integer intNumber;

    private Integer customReasonId;
    private List<BackupEmployeeItem> backupsEmployees;

    private boolean deleted;
    private String description;
    private String percentage;
    private String isNeedSignature;
    private Integer reasonsId;
    private SelectItem selectedReason;
    private SelectItem[] reasons;
    private String backups;
    private SelectItem[] templates;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public SelectItem getSelectedEmployee() {
        return selectedEmployee;
    }

    public void setSelectedEmployee(SelectItem selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
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

    public DateNonConvertable getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(DateNonConvertable approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getCode() {
        return code;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public Integer getCustomReasonId() {
        return customReasonId;
    }

    public void setCustomReasonId(Integer customReasonId) {
        this.customReasonId = customReasonId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SelectItem getCurrentApproverAsSelectItem() {
        if (getCurrentApprover() != null) {
            return getCurrentApprover().getExactEmployee();
        }
        return null;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public List<BackupEmployeeItem> getBackupsEmployees() {
        return backupsEmployees;
    }

    public void setBackupsEmployees(List<BackupEmployeeItem> backupsEmployees) {
        this.backupsEmployees = backupsEmployees;
    }

    public SelectItem getPosition() {
        return position;
    }

    public void setPosition(SelectItem position) {
        this.position = position;
    }

    public SelectItem getDepartment() {
        return department;
    }

    public void setDepartment(SelectItem department) {
        this.department = department;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getIsNeedSignature() {
        return isNeedSignature;
    }

    public void setIsNeedSignature(String isNeedSignature) {
        this.isNeedSignature = isNeedSignature;
    }

    public SelectItem[] getReasons() {
        return reasons;
    }

    public void setReasons(SelectItem[] reasons) {
        this.reasons = reasons;
    }

    public Integer getReasonsId() {
        return reasonsId;
    }

    public void setReasonsId(Integer reasonsId) {
        this.reasonsId = reasonsId;
    }

    public SelectItem getSelectedReason() {
        return selectedReason;
    }

    public void setSelectedReason(SelectItem selectedReason) {
        this.selectedReason = selectedReason;
    }

    public SelectItem[] getTemplates() {
        return templates;
    }

    public void setTemplates(SelectItem[] templates) {
        this.templates = templates;
    }

    public Date getDueDate() {
        if (getBackupsEmployees() != null && !getBackupsEmployees().isEmpty()) {
            ApproverItemMini backupEmployee = getBackupsEmployees().stream().findFirst().get().getParentBackupEmployee();
            if (backupEmployee != null) {
                return backupEmployee.getDueBackupEmployeeDate() != null ? backupEmployee.getDueBackupEmployeeDate().getDate() : null;
            }
        }
        return null;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getBackups() {
        return this.backups;
    }

    public void setBackups(final String backups) {
        this.backups = backups;
    }
}