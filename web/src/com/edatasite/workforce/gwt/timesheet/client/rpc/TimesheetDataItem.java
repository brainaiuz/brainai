package com.edatasite.workforce.gwt.timesheet.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;


public class TimesheetDataItem implements IsSerializable, Serializable {
    private static final long serialVersionUID = -6182916410094709186L;

    private Integer id;
    private int employeeTaskID;
    private String quickbookTimesheetID;
    private String quickbookEditSequence;
    private DateNonConvertable dateNonConvertable;
    private Date currentServerDate;
    private int minutes;
    private String comment;
    private boolean editable = true;
    private int status;
    private boolean autoApproved = false;
    private int difference = 0;
    private int teamID;
    private int employeeID;
    private int projectID;
    private int taskID;
    private int oldEmployeeTaskID;
    private int timeslotMinutes = 0;
    private int leaveRequestMinutes = 0;
    private int timesheetMinutes = 0;
    private int hourTypeID = 0;
    private boolean holiday = false;
    private boolean dayOff = false;
    private Date taskStart;
    private Date taskEnd;
    private boolean fromQuickbooks;
    private SelectItem[] hourTypes;
    private ArrayList<Integer> oldEmployeeTaskIDList = new ArrayList<>();
    private FastTaskTransfer taskTransfer;
    private Integer oldMinutes;
    private String reference;
    private String oldComment;

    public int getDifference() {
        return difference;
    }

    public void setDifference(int difference) {
        this.difference = difference;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isAutoApproved() {
        return autoApproved;
    }

    public void setAutoApproved(boolean autoApproved) {
        this.autoApproved = autoApproved;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getEmployeeTaskID() {
        return employeeTaskID;
    }

    public void setEmployeeTaskID(int taskId) {
        this.employeeTaskID = taskId;
    }

    public Date getDate() {
        return getDateNonConvertable().getNonConvertedDate();
    }

    public void setDate(Date date) {
        dateNonConvertable = new DateNonConvertable();
        dateNonConvertable.setDate(date);
    }

    public DateNonConvertable getDateNonConvertable() {
        if (dateNonConvertable == null) dateNonConvertable = new DateNonConvertable();
        return dateNonConvertable;
    }

    public void setDateNonConvertable(DateNonConvertable dateNonConverted) {
        this.dateNonConvertable = dateNonConverted;
    }

    public Date getCurrentServerDate() {
        return currentServerDate;
    }

    public void setCurrentServerDate(Date currentServerDate) {
        this.currentServerDate = currentServerDate;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public boolean equals(Object o) {
        TimesheetDataItem item = (TimesheetDataItem) o;
        return (getEmployeeTaskID() == item.getEmployeeTaskID()) && getDate().equals(item.getDate());
    }

    public int hashCode() {
        int first = Integer.MAX_VALUE / 2;
        int second = first + (Integer.MAX_VALUE % 2);
        return (getEmployeeTaskID() % first) + ((int) getDate().getTime() % second);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getQuickbookTimesheetID() {
        return quickbookTimesheetID;
    }

    public void setQuickbookTimesheetID(String quickbookTimesheetID) {
        this.quickbookTimesheetID = quickbookTimesheetID;
    }

    public String getQuickbookEditSequence() {
        return quickbookEditSequence;
    }

    public void setQuickbookEditSequence(String quickbookEditSequence) {
        this.quickbookEditSequence = quickbookEditSequence;
    }

    public int getTeamID() {
        return teamID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getOldEmployeeTaskID() {
        return oldEmployeeTaskID;
    }

    public void setOldEmployeeTaskID(int oldEmployeeTaskID) {
        this.oldEmployeeTaskID = oldEmployeeTaskID;
    }

    public int getTimeslotMinutes() {
        return timeslotMinutes;
    }

    public void setTimeslotMinutes(int timeslotMinutes) {
        this.timeslotMinutes = timeslotMinutes;
    }

    public int getLeaveRequestMinutes() {
        return leaveRequestMinutes;
    }

    public void setLeaveRequestMinutes(int leaveRequestMinutes) {
        this.leaveRequestMinutes = leaveRequestMinutes;
    }

    public boolean isHoliday() {
        return holiday;
    }

    public void setHoliday(boolean holiday) {
        this.holiday = holiday;
    }

    public boolean isDayOff() {
        return dayOff;
    }

    public void setDayOff(boolean dayOff) {
        this.dayOff = dayOff;
    }

    public Date getTaskStart() {
        return taskStart;
    }

    public void setTaskStart(Date taskStart) {
        this.taskStart = taskStart;
    }

    public Date getTaskEnd() {
        return taskEnd;
    }

    public void setTaskEnd(Date taskEnd) {
        this.taskEnd = taskEnd;
    }

    public int getTimesheetMinutes() {
        return timesheetMinutes;
    }

    public void setTimesheetMinutes(int timesheetMinutes) {
        this.timesheetMinutes = timesheetMinutes;
    }

    public boolean isFromQuickbooks() {
        return fromQuickbooks;
    }

    public void setFromQuickbooks(boolean fromQuickbooks) {
        this.fromQuickbooks = fromQuickbooks;
    }

    public int getHourTypeID() {
        return hourTypeID;
    }

    public void setHourTypeID(int hourTypeID) {
        this.hourTypeID = hourTypeID;
    }

    public SelectItem[] getHourTypes() {
        return hourTypes;
    }

    public void setHourTypes(SelectItem[] hourTypes) {
        this.hourTypes = hourTypes;
    }

    public ArrayList<Integer> getOldEmployeeTaskIDList() {
        return oldEmployeeTaskIDList;
    }

    public void setOldEmployeeTaskIDList(ArrayList<Integer> oldEmployeeTaskIDList) {
        this.oldEmployeeTaskIDList = oldEmployeeTaskIDList;
    }

    public void setTaskTransfer(FastTaskTransfer taskTransfer) {
        this.taskTransfer = taskTransfer;
    }

    public FastTaskTransfer getTaskTransfer() {
        return taskTransfer;
    }

    public void setOldMinutes(Integer oldMinutes) {
        this.oldMinutes = oldMinutes;
    }

    public Integer getOldMinutes() {
        return oldMinutes;
    }

    public String getOldComment() {
        return oldComment;
    }

    public void setOldComment(String oldComment) {
        this.oldComment = oldComment;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}