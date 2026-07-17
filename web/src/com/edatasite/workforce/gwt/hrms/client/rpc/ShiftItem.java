package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HasApprovers;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class ShiftItem extends HasApprovers implements Serializable {

    public static String STATUS = "status";
    public static String SHIFT_NAME = "shiftName";
    public static String START_DATE = "startDate";
    public static String END_DATE = "endDate";
    public static String CREATED_DATE = "createdDate";
    public static String UPDATED_DATE = "updatedDate";
    public static String CREATOR = "creator";
    public static String UPDATOR = "updator";
    public static String SUBMITTED_DATE = "submittedDate";
    public static String APPROVAL_DATE = "approvalDate";
    public static String PERIOD = "period";
    public static String NUMBER = "number";
    public static String MONTH = "month";
    public static String TYPE = "lookUpType";
    public static String DEPARTMENT = "department";

    public static final String WAITING = "_WAITING";
    public static final String APPROVED = "_APPROVED";
    public static final String REJECTED = "_REJECTED";

    private Integer id;
    private String status;
    private String statusCode;
    private String shiftName;
    private Boolean isApprover;
    private DateNonConvertable approvedDate;
    private ArrayList<ShiftItems> shiftItems = new ArrayList<>();

    private Date period;
    private DateNonConvertable startDate;
    private DateNonConvertable endDate;
    private DateNonConvertable createdDate;
    private DateNonConvertable updatedDate;
    private DateNonConvertable submittedDate;
    private DateNonConvertable approvalDate;
    private SelectItem creator;
    private SelectItem updater;
    private SelectItem approverEmployee;
    private SelectItem[] templates;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private NumberData numberData;
    private String code;
    private LinkedHashMap<Integer, List<ShiftTeamsItem>> shiftTeams = new LinkedHashMap<>();
    private String manager;
    private String backupManager;
    private Integer lookUpType;
    private String groupsId;
    private String ownersId;
    private List<SelectItem> ownersSelectItem = new ArrayList<>();
    private SelectItem department;
    private SelectItem creatorDepartment;
    private SelectItem birgada;
    private String periodType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatusName() {
        return status;
    }

    public void setStatusName(String status) {
        this.status = status;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public Date getPeriod() {
        return period;
    }

    public void setPeriod(Date period) {
        this.period = period;
    }

    public DateNonConvertable getStartDate() {
        return startDate;
    }

    public void setStartDate(DateNonConvertable startDate) {
        this.startDate = startDate;
    }

    public DateNonConvertable getEndDate() {
        return endDate;
    }

    public void setEndDate(DateNonConvertable endDate) {
        this.endDate = endDate;
    }

    public DateNonConvertable getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(DateNonConvertable submittedDate) {
        this.submittedDate = submittedDate;
    }

    public DateNonConvertable getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(DateNonConvertable approvalDate) {
        this.approvalDate = approvalDate;
    }

    public ArrayList<ShiftItems> getShiftItems() {
        return shiftItems;
    }

    public void setShiftItems(ArrayList<ShiftItems> shiftItems) {
        this.shiftItems = shiftItems;
    }

    public SelectItem getCurrentApproverAsSelectItem() {
        if (getCurrentApprover() != null) {
            return getCurrentApprover().getExactEmployee();
        }
        return null;
    }

    public static String getSTATUS() {
        return STATUS;
    }

    public static void setSTATUS(String STATUS) {
        ShiftItem.STATUS = STATUS;
    }

    public static void setStartDate(String startDate) {
        START_DATE = startDate;
    }

    public static void setEndDate(String endDate) {
        END_DATE = endDate;
    }

    public static void setSubmittedDate(String submittedDate) {
        SUBMITTED_DATE = submittedDate;
    }

    public static void setApprovalDate(String approvalDate) {
        APPROVAL_DATE = approvalDate;
    }

    public static String getPERIOD() {
        return PERIOD;
    }

    public static void setPERIOD(String PERIOD) {
        ShiftItem.PERIOD = PERIOD;
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

    public Boolean getApprover() {
        return isApprover;
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

    public SelectItem[] getTemplates() {
        return templates;
    }

    public void setTemplates(SelectItem[] templates) {
        this.templates = templates;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LinkedHashMap<Integer, List<ShiftTeamsItem>> getShiftTeams() {
        return shiftTeams;
    }

    public void setShiftTeams(LinkedHashMap<Integer, List<ShiftTeamsItem>> shiftTeams) {
        this.shiftTeams = shiftTeams;
    }

    public String getManager() {
        return this.manager;
    }

    public void setManager(final String manager) {
        this.manager = manager;
    }

    public String getBackupManager() {
        return this.backupManager;
    }

    public void setBackupManager(final String backupManager) {
        this.backupManager = backupManager;
    }

    public Integer getLookUpType() {
        return lookUpType;
    }

    public void setLookUpType(Integer lookUpType) {
        this.lookUpType = lookUpType;
    }

    public String getGroupsId() {
        return groupsId;
    }

    public void setGroupsId(String groupsId) {
        this.groupsId = groupsId;
    }

    public String getOwnersId() {
        return ownersId;
    }

    public void setOwnersId(String ownersId) {
        this.ownersId = ownersId;
    }

    public List<SelectItem> getOwnersSelectItem() {
        return ownersSelectItem;
    }

    public void setOwnersSelectItem(List<SelectItem> ownersSelectItem) {
        this.ownersSelectItem = ownersSelectItem;
    }

    public SelectItem getDepartment() {
        return department;
    }

    public void setDepartment(SelectItem department) {
        this.department = department;
    }

    public SelectItem getCreatorDepartment() {
        return creatorDepartment;
    }
    public void setCreatorDepartment(SelectItem creatorDepartment) {
        this.creatorDepartment = creatorDepartment;
    }

    public SelectItem getBirgada() {
        return birgada;
    }

    public void setBirgada(SelectItem birgada) {
        this.birgada = birgada;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }
}
