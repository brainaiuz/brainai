package com.edatasite.workforce.gwt.availability.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;
import java.util.HashMap;

/**
 * @author Hurshid on 3/5/2019
 */
public class LeaveRequestLisItem implements IsSerializable, ListingCustomFields {

    public static final String EMPLOYEE_NAME = "employeeName";
    public static final String REASON = "reason";
    public static final String DESCRIPTION = "description";
    public static final String FROM_DATE = "from_date";
    public static final String TO_DATE = "to_date";
    public static final String STATUS = "status";
    public static final String LEAVE_DAYS = "leave_days";
    public static final String TYPE = "type";
    public static final String APPROVER = "approver";
    public static final String CREATED_DATE = "created_date";
    public static final String REGISTERED_BY = "registered_by";
    public static final String POSITION = "position";
    public static final String DEPARTMENT = "department";
    public static final String CODE = "code";

    private Integer objectId;
    private String description;
    private String reason;
    private String reasonCode;
    private String reasonID;
    private String status;
    private String statusID;
    private String statusCode;
    private String employeeName;
    private Integer employeeId;
    private String approverName;
    private Integer approverId;
    private DateNonConvertable startDate;
    private DateNonConvertable endDate;
    private Date createdDate;
    private String creator;
    private String paid;
    private String nonPaid;
    private String leaveDays;
    private String type;
    private boolean allDay;
    private Integer positionId;
    private String position;
    private Integer departmentId;
    private String department;
    private String leaveRequestCode;
    private HashMap<String, Object> customFields;
    private boolean userIsCurrentApprover;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getDescription() {
        if (description == null) {
            description = "";
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getNonPaid() {
        return nonPaid;
    }

    public void setNonPaid(String nonPaid) {
        this.nonPaid = nonPaid;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isPending() {
        return Constants.LR_STATUS_NOT_DEFINED.equals(getStatusCode());
    }

    public String getReasonID() {
        return reasonID;
    }

    public void setReasonID(String reasonID) {
        this.reasonID = reasonID;
    }

    public String getStatusID() {
        return statusID;
    }

    public void setStatusID(String statusID) {
        this.statusID = statusID;
    }

    public Integer getApproverId() {
        return approverId;
    }

    public void setApproverId(Integer approverId) {
        this.approverId = approverId;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(String leaveDays) {
        this.leaveDays = leaveDays;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public void setCustomFieldsValue(String key, Object value) {
        customFields.put(key, value);
    }

    @Override
    public Object getCustomFieldsValue(String columnKey) {
        return customFields.get(columnKey);
    }

    public void setCustomFields(HashMap<String, Object> customFields) {
        this.customFields = customFields;
    }

    public HashMap<String, Object> getCustomFields() {
        return customFields;
    }

    public String getLeaveRequestCode() {
        return leaveRequestCode;
    }

    public void setLeaveRequestCode(String leaveRequestCode) {
        this.leaveRequestCode = leaveRequestCode;
    }

    public boolean isUserIsCurrentApprover() {
        return userIsCurrentApprover;
    }

    public void setUserIsCurrentApprover(boolean userIsCurrentApprover) {
        this.userIsCurrentApprover = userIsCurrentApprover;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }
}
