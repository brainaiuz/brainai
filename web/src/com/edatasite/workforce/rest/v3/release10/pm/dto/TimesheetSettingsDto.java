package com.edatasite.workforce.rest.v3.release10.pm.dto;

//hozircha APPROVE TIMESHEET HOURS: typelari un kere kn chali kereli settingslar yana qushiladi
public class TimesheetSettingsDto {
    private Boolean automatically;
    private Boolean submitForApproval;
    private Boolean manually;
    private Integer timesheetWeekStart;
    private Boolean sortTimesheetByTaskName;
    private String timesheetDateFormat;
    private Boolean automaticPercent;
    private Boolean manualPercent;
    private Boolean showCompletedTasks;
    private Boolean timesheetCommentRequired;
    private Boolean timesheetApprovalCommentRequired;
    private Boolean showTaskRelated;
    private Boolean showTimesheetHourTypes;
    private Boolean showToDoListTasks;
    private Boolean enableMultipleTimerInstances;
    private Boolean saveTimerIntoTimesheetAutomatically;
    private Boolean dailyFillTimesheetFromResUtilRequired;
    private Boolean validateTaskStart;
    private Boolean validateTaskEnd;
    private Boolean validateHoliday;
    private Boolean validateLeaveRequest;
    private Boolean validateMaximumHours;
    private Boolean validateTimeslot;
    private Boolean validateHours;
    private Integer maximumHours;
    private Boolean validateDayOff;
    private boolean validateTimesheetEstimate;
    private Boolean validatePastTimesheet;
    private Integer pastTimesheetDays;
    private Boolean validateFutureTimesheet;
    private Integer futureTimesheetDays;

    public Boolean getAutomatically() {
        return automatically;
    }

    public void setAutomatically(Boolean automatically) {
        this.automatically = automatically;
    }

    public Boolean getSubmitForApproval() {
        return submitForApproval;
    }

    public void setSubmitForApproval(Boolean submitForApproval) {
        this.submitForApproval = submitForApproval;
    }

    public Boolean getManually() {
        return manually;
    }

    public void setManually(Boolean manually) {
        this.manually = manually;
    }
    public Integer getTimesheetWeekStart() {
        return timesheetWeekStart;
    }
    public void setTimesheetWeekStart(Integer timesheetWeekStart) {
        this.timesheetWeekStart = timesheetWeekStart;
    }
    public Boolean getSortTimesheetByTaskName() {
        return sortTimesheetByTaskName;
    }
    public void setSortTimesheetByTaskName(Boolean sortTimesheetByTaskName) {
        this.sortTimesheetByTaskName = sortTimesheetByTaskName;
    }
    public String getTimesheetDateFormat() {
        return timesheetDateFormat;
    }
    public void setTimesheetDateFormat(String timesheetDateFormat) {
        this.timesheetDateFormat = timesheetDateFormat;
    }
    public Boolean getAutomaticPercent() {
        return automaticPercent;
    }
    public void setAutomaticPercent(Boolean automaticPercent) {
        this.automaticPercent = automaticPercent;
    }
    public Boolean getManualPercent() {
        return manualPercent;
    }
    public void setManualPercent(Boolean manualPercent) {
        this.manualPercent = manualPercent;
    }
    public Boolean getShowCompletedTasks() {
        return showCompletedTasks;
    }
    public void setShowCompletedTasks(Boolean showCompletedTasks) {
        this.showCompletedTasks = showCompletedTasks;
    }
    public Boolean getTimesheetCommentRequired() {
        return timesheetCommentRequired;
    }
    public void setTimesheetCommentRequired(Boolean timesheetCommentRequired) {
        this.timesheetCommentRequired = timesheetCommentRequired;
    }
    public Boolean getTimesheetApprovalCommentRequired() {
        return timesheetApprovalCommentRequired;
    }
    public void setTimesheetApprovalCommentRequired(Boolean timesheetApprovalCommentRequired) {
        this.timesheetApprovalCommentRequired = timesheetApprovalCommentRequired;
    }
    public Boolean getShowTaskRelated() {
        return showTaskRelated;
    }
    public void setShowTaskRelated(Boolean showTaskRelated) {
        this.showTaskRelated = showTaskRelated;
    }
    public Boolean getShowTimesheetHourTypes() {
        return showTimesheetHourTypes;
    }
    public void setShowTimesheetHourTypes(Boolean showTimesheetHourTypes) {
        this.showTimesheetHourTypes = showTimesheetHourTypes;
    }
    public Boolean getShowToDoListTasks() {
        return showToDoListTasks;
    }
    public void setShowToDoListTasks(Boolean showToDoListTasks) {
        this.showToDoListTasks = showToDoListTasks;
    }
    public Boolean getEnableMultipleTimerInstances() {
        return enableMultipleTimerInstances;
    }
    public void setEnableMultipleTimerInstances(Boolean enableMultipleTimerInstances) {
        this.enableMultipleTimerInstances = enableMultipleTimerInstances;
    }
    public Boolean getSaveTimerIntoTimesheetAutomatically() {
        return saveTimerIntoTimesheetAutomatically;
    }
    public void setSaveTimerIntoTimesheetAutomatically(Boolean saveTimerIntoTimesheetAutomatically) {
        this.saveTimerIntoTimesheetAutomatically = saveTimerIntoTimesheetAutomatically;
    }
    public Boolean getDailyFillTimesheetFromResUtilRequired() {
        return dailyFillTimesheetFromResUtilRequired;
    }
    public void setDailyFillTimesheetFromResUtilRequired(Boolean dailyFillTimesheetFromResUtilRequired) {
        this.dailyFillTimesheetFromResUtilRequired = dailyFillTimesheetFromResUtilRequired;
    }
    public Boolean getValidateTaskStart() {
        return validateTaskStart;
    }
    public void setValidateTaskStart(Boolean validateTaskStart) {
        this.validateTaskStart = validateTaskStart;
    }
    public Boolean getValidateTaskEnd() {
        return validateTaskEnd;
    }
    public void setValidateTaskEnd(Boolean validateTaskEnd) {
        this.validateTaskEnd = validateTaskEnd;
    }
    public Boolean getValidateHoliday() {
        return validateHoliday;
    }
    public void setValidateHoliday(Boolean validateHoliday) {
        this.validateHoliday = validateHoliday;
    }
    public Boolean getValidateLeaveRequest() {
        return validateLeaveRequest;
    }
    public void setValidateLeaveRequest(Boolean validateLeaveRequest) {
        this.validateLeaveRequest = validateLeaveRequest;
    }
    public Boolean getValidateMaximumHours() {
        return validateMaximumHours;
    }
    public void setValidateMaximumHours(Boolean validateMaximumHours) {
        this.validateMaximumHours = validateMaximumHours;
    }
    public Boolean getValidateTimeslot() {
        return validateTimeslot;
    }
    public void setValidateTimeslot(Boolean validateTimeslot) {
        this.validateTimeslot = validateTimeslot;
    }
    public Boolean getValidateHours() {
        return validateHours;
    }
    public void setValidateHours(Boolean validateHours) {
        this.validateHours = validateHours;
    }
    public Integer getMaximumHours() {
        return maximumHours;
    }
    public void setMaximumHours(Integer maximumHours) {
        this.maximumHours = maximumHours;
    }
    public Boolean getValidateDayOff() {
        return validateDayOff;
    }
    public void setValidateDayOff(Boolean validateDayOff) {
        this.validateDayOff = validateDayOff;
    }
    public boolean isValidateTimesheetEstimate() {
        return validateTimesheetEstimate;
    }
    public void setValidateTimesheetEstimate(boolean validateTimesheetEstimate) {
        this.validateTimesheetEstimate = validateTimesheetEstimate;
    }
    public Boolean getValidatePastTimesheet() {
        return validatePastTimesheet;
    }
    public void setValidatePastTimesheet(Boolean validatePastTimesheet) {
        this.validatePastTimesheet = validatePastTimesheet;
    }
    public Integer getPastTimesheetDays() {
        return pastTimesheetDays;
    }
    public void setPastTimesheetDays(Integer pastTimesheetDays) {
        this.pastTimesheetDays = pastTimesheetDays;
    }
    public Boolean getValidateFutureTimesheet() {
        return validateFutureTimesheet;
    }
    public void setValidateFutureTimesheet(Boolean validateFutureTimesheet) {
        this.validateFutureTimesheet = validateFutureTimesheet;
    }
    public Integer getFutureTimesheetDays() {
        return futureTimesheetDays;
    }
    public void setFutureTimesheetDays(Integer futureTimesheetDays) {
        this.futureTimesheetDays = futureTimesheetDays;
    }
}
