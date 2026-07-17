package com.edatasite.workforce.gwt.availability.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

public class AttendanceMarkListItem implements IsSerializable {

    public static final String EMPLOYEE_NAME = "employeeName";
    public static final String EMPLOYEE_CODE = "employeeCode";
    public static final String DATE = "date";
    public static final String DEPARTMENT = "department";
    public static final String POSITION = "position";
    public static final String LOCATION = "location";
    public static final String TIMESLOT = "timeslot";
    public static final String SUPERVISOR = "supervisor";
    public static final String DEVICE_ID = "deviceId";
    public static final String TERMINAL = "terminal";
    public static final String CREATED_DATE = "createdDate";
    public static final String SOURCE = "source";
    public static final String VALIDATED = "validated";
    public static final String EMPLOYEE_STATUS = "employeeStatus";
    public static final String ROLE = "role";
    public static final String MAP = "map";
    public static final String NOTE = "note";
    public static final String CREATED_BY_NAME = "createdByName";
    public static final String UPDATED_AT = "updatedAt";
    public static final String UPDATED_BY_NAME = "updatedByName";
    public static final String PROFILE_PICTURE_URL = "profilePictureUrl";
    public static final String APPROVAL_STATUS = "approvalStatus";
    public static final String IS_AUTO = "isAuto";

    private Integer objectId;
    private String employeeCode;
    private String employeeName;
    private Integer employeeId;
    private String pictureUrl;
    private DateNonConvertable date;
    private String location;
    private String department;
    private String position;
    private String timeslotName;
    private String supervisor;
    private String deviceId;
    private String terminal;
    private DateNonConvertable createdDate;
    private String url;
    private String employeeStatus;
    private String role;
    private String validated;
    private String source;
    private Integer departmentId;
    private Integer positionId;
    private Integer locationId;
    private Integer timeslotId;
    private Integer supervisorId;
    private Integer employeeStatusId;
    private Integer terminalId;
    private String roleIds;
    private Integer adjustmentId;
    private Double latitude;
    private Double longitude;
    private String note;
    private String createdByName;
    private DateNonConvertable updatedAt;
    private String updatedByName;
    private String profilePictureUrl;
    private Integer photoId;
    private String approvalStatus;
    private Boolean isAuto;

    public Integer getObjectId() { return objectId; }
    public void setObjectId(Integer objectId) { this.objectId = objectId; }

    public String getEmployeeCode() { return employeeCode; }
    public void setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public String getPictureUrl() { return pictureUrl; }
    public void setPictureUrl(String pictureUrl) { this.pictureUrl = pictureUrl; }

    public DateNonConvertable getDate() { return date; }
    public void setDate(DateNonConvertable date) { this.date = date; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getTimeslotName() { return timeslotName; }
    public void setTimeslotName(String timeslotName) { this.timeslotName = timeslotName; }

    public String getSupervisor() { return supervisor; }
    public void setSupervisor(String supervisor) { this.supervisor = supervisor; }

    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }

    public String getTerminal() { return terminal; }
    public void setTerminal(String terminal) { this.terminal = terminal; }

    public DateNonConvertable getCreatedDate() { return createdDate; }
    public void setCreatedDate(DateNonConvertable createdDate) { this.createdDate = createdDate; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getEmployeeStatus() { return employeeStatus; }
    public void setEmployeeStatus(String employeeStatus) { this.employeeStatus = employeeStatus; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getValidated() { return validated; }
    public void setValidated(String validated) { this.validated = validated; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }

    public Integer getPositionId() { return positionId; }
    public void setPositionId(Integer positionId) { this.positionId = positionId; }

    public Integer getLocationId() { return locationId; }
    public void setLocationId(Integer locationId) { this.locationId = locationId; }

    public Integer getTimeslotId() { return timeslotId; }
    public void setTimeslotId(Integer timeslotId) { this.timeslotId = timeslotId; }

    public Integer getSupervisorId() { return supervisorId; }
    public void setSupervisorId(Integer supervisorId) { this.supervisorId = supervisorId; }

    public Integer getEmployeeStatusId() { return employeeStatusId; }
    public void setEmployeeStatusId(Integer employeeStatusId) { this.employeeStatusId = employeeStatusId; }

    public Integer getTerminalId() { return terminalId; }
    public void setTerminalId(Integer terminalId) { this.terminalId = terminalId; }

    public String getRoleIds() { return roleIds; }
    public void setRoleIds(String roleIds) { this.roleIds = roleIds; }

    public Integer getAdjustmentId() { return adjustmentId; }
    public void setAdjustmentId(Integer adjustmentId) { this.adjustmentId = adjustmentId; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }

    public DateNonConvertable getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(DateNonConvertable updatedAt) { this.updatedAt = updatedAt; }

    public String getUpdatedByName() { return updatedByName; }
    public void setUpdatedByName(String updatedByName) { this.updatedByName = updatedByName; }

    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }

    public Integer getPhotoId() { return photoId; }
    public void setPhotoId(Integer photoId) { this.photoId = photoId; }

    public String getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }

    public Boolean getIsAuto() { return isAuto; }
    public void setIsAuto(Boolean isAuto) { this.isAuto = isAuto; }
}
