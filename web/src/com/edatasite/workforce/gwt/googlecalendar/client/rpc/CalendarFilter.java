package com.edatasite.workforce.gwt.googlecalendar.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 9/15/12
 * Time: 8:15 PM
 */
public class CalendarFilter implements IsSerializable {
    private ArrayList<Integer> employeeIDs;
    private Date start;
    private Date end;
    private boolean fromAgenda = false;
    private boolean visible = false;
    private boolean forPDF = false;
    private boolean isReadOnly = false;
    private boolean fromMobile = false;
    private boolean forUIOnly = false;
    private boolean isCall;
    private Integer locationID;

    public ArrayList<Integer> getEmployeeIDs() {
        return employeeIDs;
    }

    public void setEmployeeIDs(ArrayList<Integer> employeeIDs) {
        this.employeeIDs = employeeIDs;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public boolean isFromAgenda() {
        return fromAgenda;
    }

    public void setFromAgenda(boolean fromAgenda) {
        this.fromAgenda = fromAgenda;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isForPDF() {
        return forPDF;
    }

    public void setForPDF(boolean forPDF) {
        this.forPDF = forPDF;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public void setReadOnly(boolean readOnly) {
        isReadOnly = readOnly;
    }

    public boolean isFromMobile() {
        return fromMobile;
    }

    public void setFromMobile(boolean fromMobile) {
        this.fromMobile = fromMobile;
    }

    public Integer getLocationID() {
        return locationID;
    }

    public void setLocationID(Integer locationID) {
        this.locationID = locationID;
    }

    public boolean isForUIOnly() {
        return forUIOnly;
    }

    public void setForUIOnly(boolean forUIOnly) {
        this.forUIOnly = forUIOnly;
    }

    public boolean isCall() {
        return this.isCall;
    }

    public void setCall(final boolean call) {
        this.isCall = call;
    }
}
