package com.edatasite.workforce.gwt.core.client.rpc.dashboard;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * User: DELL
 * Date: 19-May-2009
 * Time: 09:08:03
 */
public class InOutItem implements IsSerializable {

    private Integer employeeId;
    private String employeeName;

    private String departmentName;
    private String dateName;
    private Date dateFormat;
    private Integer groupById;
    private String[] inHours;
    private String[] outHours;
    private String[] duration;

    private HashMap<Date, Double> dailyInOutHour;

    private ArrayList<Integer> timeTrackList;
    private ArrayList<HashMap<String, String>> leaveRequest;

    private String launchHour;
    private String budgetHour;
    private String actualInHour;
    private String timeSheetHour;
    private String timeslotStartHour;
    private String timeslotEndHour;
    private String overtimeHour;
    private HashMap<String, Integer> missingHour;
    private HashMap<Double, Integer> finImpact;
    private boolean hasLeaveRequest = false;
    private Boolean holiday;


    public InOutItem() {
    }

    public ArrayList<HashMap<String, String>> getLeaveRequest() {
        return leaveRequest;
    }

    public void setLeaveRequest(ArrayList<HashMap<String, String>> leaveRequest) {
        this.leaveRequest = leaveRequest;
    }

    public String[] getInHours() {
        return inHours;
    }

    public void setInHours(String[] inHours) {
        this.inHours = inHours;
    }

    public String[] getOutHours() {
        return outHours;
    }

    public void setOutHours(String[] outHours) {
        this.outHours = outHours;
    }

    public String[] getDuration() {
        return duration;
    }

    public void setDuration(String[] duration) {
        this.duration = duration;
    }

    public HashMap<Date, Double> getDailyInOutHour() {
        return dailyInOutHour;
    }

    public void setDailyInOutHour(HashMap<Date, Double> dailyInOutHour) {
        this.dailyInOutHour = dailyInOutHour;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDateName() {
        return dateName;
    }

    public void setDateName(String dateName) {
        this.dateName = dateName;
    }

    public Date getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(Date dateFormat) {
        this.dateFormat = dateFormat;
    }

    public Integer getGroupById() {
        return groupById;
    }

    public void setGroupById(Integer groupById) {
        this.groupById = groupById;
    }


    public String getLaunchHour() {
        return launchHour;
    }

    public void setLaunchHour(String launchHour) {
        this.launchHour = launchHour;
    }

    public String getBudgetHour() {
        return budgetHour;
    }

    public void setBudgetHour(String budgetHour) {
        this.budgetHour = budgetHour;
    }

    public String getActualInHour() {
        return actualInHour;
    }

    public void setActualInHour(String actualInHour) {
        this.actualInHour = actualInHour;
    }

    public String getTimeSheetHour() {
        return timeSheetHour;
    }

    public void setTimeSheetHour(String timeSheetHour) {
        this.timeSheetHour = timeSheetHour;
    }


    public HashMap<String, Integer> getMissingHour() {
        return missingHour;

    }

    public void setMissingHour(HashMap<String, Integer> missingHour) {
        this.missingHour = missingHour;
    }

    public HashMap<Double, Integer> getFinImpact() {
        return finImpact;
    }

    public void setFinImpact(HashMap<Double, Integer> finImpact) {
        this.finImpact = finImpact;
    }

    public boolean isHasLeaveRequest() {
        return hasLeaveRequest;
    }

    public void setHasLeaveRequest(boolean hasLeaveRequest) {
        this.hasLeaveRequest = hasLeaveRequest;
    }

    public Boolean isHoliday() {
        return holiday;
    }

    public void setHoliday(Boolean holiday) {
        this.holiday = holiday;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public ArrayList<Integer> getTimeTrackList() {
        return timeTrackList;
    }

    public void setTimeTrackList(ArrayList<Integer> timeTrackList) {
        this.timeTrackList = timeTrackList;
    }

    public String getTimeslotStartHour() {
        return timeslotStartHour;
    }

    public void setTimeslotStartHour(String timeslotStartHour) {
        this.timeslotStartHour = timeslotStartHour;
    }

    public String getTimeslotEndHour() {
        return timeslotEndHour;
    }

    public void setTimeslotEndHour(String timeslotEndHour) {
        this.timeslotEndHour = timeslotEndHour;
    }

    public String getOvertimeHour() {
        return overtimeHour;
    }

    public void setOvertimeHour(String overtimeHour) {
        this.overtimeHour = overtimeHour;
    }
}