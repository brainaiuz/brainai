package com.edatasite.workforce.gwt.timesheet.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Jul 13, 2010
 * Time: 11:18:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class TimesheetSettings implements IsSerializable {

    private int maxMinutesPerDay;
    private int minMinutesPerDay;
    private boolean validateDailyTimesheets;
    private boolean validateTimesheetApproval;
    private boolean isTimesheetCommentRequired;
    private Boolean isTimesheetApprovalCommentRequired;
    private ProjectMember[] members;

    public int getMaxMinutesPerDay() {
        return maxMinutesPerDay;
    }

    public void setMaxMinutesPerDay(int maxMinutesPerDay) {
        this.maxMinutesPerDay = maxMinutesPerDay;
    }

    public int getMinMinutesPerDay() {
        return minMinutesPerDay;
    }

    public void setMinMinutesPerDay(int minMinutesPerDay) {
        this.minMinutesPerDay = minMinutesPerDay;
    }

    public boolean isValidateDailyTimesheets() {
        return validateDailyTimesheets;
    }

    public void setValidateDailyTimesheets(boolean validateDailyTimesheets) {
        this.validateDailyTimesheets = validateDailyTimesheets;
    }

    public boolean isValidateTimesheetApproval() {
        return validateTimesheetApproval;
    }

    public void setValidateTimesheetApproval(boolean validateTimesheetApproval) {
        this.validateTimesheetApproval = validateTimesheetApproval;
    }

    public ProjectMember[] getMembers() {
        return members;
    }

    public void setMembers(ProjectMember[] members) {
        this.members = members;
    }

    public boolean isTimesheetCommentRequired() {
        return isTimesheetCommentRequired;
    }

    public void setTimesheetCommentRequired(boolean isTimesheetCommentRequired) {
        this.isTimesheetCommentRequired = isTimesheetCommentRequired;
    }

    public Boolean isTimesheetApprovalCommentRequired() {
        return isTimesheetApprovalCommentRequired != null && isTimesheetApprovalCommentRequired;
    }

    public void setTimesheetApprovalCommentRequired(Boolean timesheetApprovalCommentRequired) {
        isTimesheetApprovalCommentRequired = timesheetApprovalCommentRequired;
    }
}
