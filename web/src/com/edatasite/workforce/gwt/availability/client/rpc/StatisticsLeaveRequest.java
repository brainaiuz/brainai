package com.edatasite.workforce.gwt.availability.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HasApprovers;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.BackupEmployeeItem;
import com.edatasite.workforce.gwt.core.client.ui.laborPeriod.MultiLeaveDTO;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class StatisticsLeaveRequest extends HasApprovers implements IsSerializable {

    private Integer objectID;
    private String approverName;
    private String annualAllowance;
    private String duration;
    private Double nonPaidDays;
    private String department;
    private String description;
    private String employee;
    private Integer employeeId;
    private Integer salaryPercentage;
    private BigDecimal maternityLeaveDayCount;

    private String reason;
    private Integer reasonId;
    private String reasonCode;
    private boolean includeDayOffs;

    private String type;
    private String typeCode;
    private Integer typeId;
    private DateNonConvertable startDDate;
    private DateNonConvertable endDDate;
    private DateNonConvertable recallDDate;
    private boolean hideTime;
    private boolean action;
    private Boolean takeByMoney;
    private FileResource fileResource;
    private boolean approveForAll = false;
    private String creator;
    private String creatorPosition;
    private String creatorDepartment;

    private LeaveRequestComment[] leaveRequestComment;
    private Date createdDate;
    private ArrayList<CompanyCustomFieldItem> customFields;
    private Boolean from;
    private SelectItem[] reasons;
    private NumberData numberData;
    private String leaveRrequestCode;
    private ArrayList<BackupEmployeeItem> backupEmployee;
    private Date employeeStartDate;
    private ArrayList<MultiLeaveDTO> multiLeaveList;

    private String fromDate;
    private String toDate;
    private Date startDate;
    private Date endDate;
    private Integer status;
    private Boolean markAsDraft;
    private Integer currectApproverId;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public boolean isIncludeDayOffs() {
        return includeDayOffs;
    }

    public void setIncludeDayOffs(boolean includeDayOffs) {
        this.includeDayOffs = includeDayOffs;
    }

    public boolean isAction() {
        return action;
    }

    public void setAction(boolean action) {
        this.action = action;
    }

    public String getAnnualAllowance() {
        return annualAllowance;
    }

    public void setAnnualAllowance(String annualAllowance) {
        this.annualAllowance = annualAllowance;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Double getNonPaidDays() {
        return nonPaidDays != null ? nonPaidDays : 0d;
    }

    public void setNonPaidDays(Double nonPaidDays) {
        this.nonPaidDays = nonPaidDays;
    }

    public DateNonConvertable getStartDDate() {
        return startDDate;
    }

    public void setStartDDate(DateNonConvertable startDDate) {
        this.startDDate = startDDate;
    }

    public DateNonConvertable getEndDDate() {
        return endDDate;
    }

    public void setEndDDate(DateNonConvertable endDDate) {
        this.endDDate = endDDate;
    }

    public DateNonConvertable getRecallDDate() {
        return recallDDate;
    }

    public void setRecallDDate(DateNonConvertable recallDDate) {
        this.recallDDate = recallDDate;
    }

    public FileResource getFileResource() {
        return fileResource;
    }

    public void setFileResource(FileResource fileResource) {
        this.fileResource = fileResource;
    }

    public boolean isApproveForAll() {
        return approveForAll;
    }

    public void setApproveForAll(boolean approveForAll) {
        this.approveForAll = approveForAll;
    }

    public LeaveRequestComment[] getLeaveRequestComment() {
        return leaveRequestComment;
    }

    public void setLeaveRequestComment(LeaveRequestComment[] leaveRequestComment) {
        this.leaveRequestComment = leaveRequestComment;
    }

    public Integer getReasonId() {
        return reasonId;
    }

    public void setReasonId(Integer reasonId) {
        this.reasonId = reasonId;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getTakeByMoney() {
        return takeByMoney;
    }

    public void setTakeByMoney(Boolean takeByMoney) {
        this.takeByMoney = takeByMoney;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(ArrayList<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
    }

    public Integer getCurrentApproverEmployeeID() {
        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            return getCurrentApprover().getExactEmployee().getId();
        }
        return null;
    }

    public boolean isHideTime() {
        return hideTime;
    }

    public void setHideTime(boolean hideTime) {
        this.hideTime = hideTime;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }

    public Boolean getFrom() {
        return from;
    }

    public void setFrom(Boolean from) {
        this.from = from;
    }

    public SelectItem[] getReasons() {
        return reasons;
    }

    public void setReasons(SelectItem[] reasons) {
        this.reasons = reasons;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreatorPosition() {
        return creatorPosition;
    }

    public void setCreatorPosition(String creatorPosition) {
        this.creatorPosition = creatorPosition;
    }

    public String getCreatorDepartment() {
        return creatorDepartment;
    }

    public void setCreatorDepartment(String creatorDepartment) {
        this.creatorDepartment = creatorDepartment;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public ArrayList<BackupEmployeeItem> getBackupEmployee() {
        return backupEmployee;
    }

    public void setBackupEmployee(ArrayList<BackupEmployeeItem> backupEmployee) {
        this.backupEmployee = backupEmployee;
    }

    public Date getEmployeeStartDate() {
        return employeeStartDate;
    }

    public void setEmployeeStartDate(Date employeeStartDate) {
        this.employeeStartDate = employeeStartDate;
    }

    public ArrayList<MultiLeaveDTO> getMultiLeaveList() {
        return multiLeaveList;
    }

    public void setMultiLeaveList(ArrayList<MultiLeaveDTO> multiLeaveList) {
        this.multiLeaveList = multiLeaveList;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getLeaveRrequestCode() {
        return leaveRrequestCode;
    }

    public void setLeaveRrequestCode(String leaveRrequestCode) {
        this.leaveRrequestCode = leaveRrequestCode;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean isMarkAsDraft() {
        return markAsDraft;
    }

    public void setMarkAsDraft(Boolean markAsDraft) {
        this.markAsDraft = markAsDraft;
    }
    public Integer getCurrectApproverId() {
        return currectApproverId;
    }

    public void setCurrectApproverId(Integer currectApproverId) {
        this.currectApproverId = currectApproverId;
    }

    public Integer getSalaryPercentage() {
        return salaryPercentage;
    }

    public void setSalaryPercentage(Integer salaryPercentage) {
        this.salaryPercentage = salaryPercentage;
    }

    public BigDecimal getMaternityLeaveDayCount() {
        return maternityLeaveDayCount;
    }

    public void setMaternityLeaveDayCount(BigDecimal maternityLeaveDayCount) {
        this.maternityLeaveDayCount = maternityLeaveDayCount;
    }
}