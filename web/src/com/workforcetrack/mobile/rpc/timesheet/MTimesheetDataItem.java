package com.workforcetrack.mobile.rpc.timesheet;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetDataItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * User: Abdulaziz
 * Date: 8/18/11
 * Time: 5:15 PM
 */
@XmlRootElement
public class MTimesheetDataItem implements Serializable {

    private Integer id;
    private int employeeTaskID;
    private Integer oldEmployeeTaskID;
    private String quickbookTimesheetID;
    private String quickbookEditSequence;
    private Date date;
    private Date dateNonConvertable;
    private int minutes;
    private String comment;
    private boolean editable = true;
    private int status;
    private ArrayList<Integer> oldEmployeeTaskIDList = new ArrayList<>();

    public MTimesheetDataItem() {

    }

    public MTimesheetDataItem(TimesheetDataItem item) {
        this.id = item.getId();
        this.employeeTaskID = item.getEmployeeTaskID();
        this.oldEmployeeTaskID = item.getOldEmployeeTaskID();
        this.quickbookTimesheetID = item.getQuickbookTimesheetID();
        this.quickbookEditSequence = item.getQuickbookEditSequence();
        this.date = item.getDate();
        this.dateNonConvertable = (item.getDateNonConvertable() != null ? item.getDateNonConvertable().getNonConvertedDate() : null);
        this.minutes = item.getMinutes();
        this.comment = item.getComment();
        this.editable = item.isEditable();
        this.status = item.getStatus();
        this.oldEmployeeTaskIDList = item.getOldEmployeeTaskIDList();
    }

    public TimesheetDataItem convert(TimesheetDataItem item) {
        if (item == null) {
            item = new TimesheetDataItem();
        }
        item.setId(getId());
        item.setEmployeeTaskID(getEmployeeTaskID());
        item.setOldEmployeeTaskID(getOldEmployeeTaskID() != null ? getOldEmployeeTaskID() : 0);
        item.setOldEmployeeTaskIDList(getOldEmployeeTaskIDList());
        item.setMinutes(getMinutes());
        item.setComment(getComment());
        item.setDate(item.getDate());

        return item;
    }

    public static TimesheetDataItem convertFromMobile(MTimesheetDataItem mItem) {
        TimesheetDataItem item = new TimesheetDataItem();
        item.setId(mItem.getId());
        item.setEmployeeTaskID(mItem.getEmployeeTaskID());
        item.setOldEmployeeTaskID(mItem.getOldEmployeeTaskID() != null ? mItem.getOldEmployeeTaskID() : 0);
        item.setOldEmployeeTaskIDList(mItem.getOldEmployeeTaskIDList());
        item.setQuickbookTimesheetID(mItem.getQuickbookTimesheetID());
        item.setQuickbookEditSequence(mItem.getQuickbookEditSequence());
        item.setDate(mItem.getDate());
        item.setDateNonConvertable((mItem.getDate() != null ? new DateNonConvertable(mItem.getDate()) : null));
        item.setMinutes(mItem.getMinutes());
        item.setComment(mItem.getComment());
        item.setEditable(mItem.isEditable());
        item.setStatus(mItem.getStatus());
        return item;
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

    public void setEmployeeTaskID(int employeeTaskID) {
        this.employeeTaskID = employeeTaskID;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDateNonConvertable() {
        return dateNonConvertable;
    }

    public void setDateNonConvertable(Date dateNonConvertable) {
        this.dateNonConvertable = dateNonConvertable;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getOldEmployeeTaskID() {
        return oldEmployeeTaskID;
    }

    public void setOldEmployeeTaskID(Integer oldEmployeeTaskID) {
        this.oldEmployeeTaskID = oldEmployeeTaskID;
    }

    public ArrayList<Integer> getOldEmployeeTaskIDList() {
        return oldEmployeeTaskIDList;
    }

    public void setOldEmployeeTaskIDList(ArrayList<Integer> oldEmployeeTaskIDList) {
        this.oldEmployeeTaskIDList = oldEmployeeTaskIDList;
    }
}
