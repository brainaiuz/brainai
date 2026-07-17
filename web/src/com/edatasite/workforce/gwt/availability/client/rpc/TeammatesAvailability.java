package com.edatasite.workforce.gwt.availability.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TeammatesAvailability implements IsSerializable {

	public static final String AT_EMPLOYEE_NAME = "AT_EMPLOYEE_NAME";
	public static final String AT_DEPARTMENT_NAME = "AT_DEPARTMENT_NAME";
	public static final String AT_TIME_SLOT_NAME = "AT_TIME_SLOT_NAME";
	public static final String AT_FROM_DURATION = "AT_FROM_DURATION";
	public static final String AT_TO_DURATION = "AT_TO_DURATION";
	public static final String AT_STATUS_NAME = "AT_STATUS_NAME";

    private String employee;
    private String department;
    private String status;
    private String from;
    private String duration;
    private String employeeId;
    private Integer departmentId;
    private String timeslot;
    private String to;

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        if (from.length() > 16) {
            return from.substring(0, 16);
        }
        return from;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDuration() {
        return duration;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(String timeslot) {
        this.timeslot = timeslot;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }


}
