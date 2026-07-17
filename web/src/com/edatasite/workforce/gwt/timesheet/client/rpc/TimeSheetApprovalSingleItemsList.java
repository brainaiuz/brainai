package com.edatasite.workforce.gwt.timesheet.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.UserGrant;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: 04.05.2009
 * Time: 18:10:01
 * To change this template use File | Settings | File Templates.
 */
public class TimeSheetApprovalSingleItemsList implements IsSerializable, UserGrant {
    private int permission;
    private SelectItem[] actions;
    private TimeSheetApprovalSingleItem[] items;
    private int totalCount;
    private Integer id;
    private String employeeName;
    private String statusCode;
    private boolean isTimesheetApprovalCommentRequired;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SelectItem[] getActions() {
        return actions;
    }

    public void setActions(SelectItem[] actions) {
        this.actions = actions;
    }

    public TimeSheetApprovalSingleItem[] getItems() {
        return items;
    }

    public void setItems(TimeSheetApprovalSingleItem[] items) {
        this.items = items;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public int getPermission() {
        return permission;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public boolean isTimesheetApprovalCommentRequired() {
        return isTimesheetApprovalCommentRequired;
    }

    public void setTimesheetApprovalCommentRequired(boolean timesheetApprovalCommentRequired) {
        isTimesheetApprovalCommentRequired = timesheetApprovalCommentRequired;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
