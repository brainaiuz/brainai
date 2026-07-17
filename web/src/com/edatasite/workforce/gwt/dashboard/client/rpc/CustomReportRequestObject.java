package com.edatasite.workforce.gwt.dashboard.client.rpc;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: administrator
 * Date: 11.03.2009
 * Time: 22:14:00
 * To change this template use File | Settings | File Templates.
 */
public class CustomReportRequestObject extends ListingFilterParameter {

    // TimeSheet Report columns
    private boolean showClient;
    private boolean showProject;
    private boolean showDepartment;
    private boolean showEmployee;
    private boolean showWorkstream;
    private boolean showTask;
    private boolean showDate;
    private boolean showComment;
    private boolean showDescription;
    private boolean showPercentCompleted;
    private boolean showApprovedHours;
    private boolean showNonApprovedHours;
    private boolean showStatus;
    private boolean showTimesheetStatus;
    private boolean showEstimatedTime;
    private boolean showManagerComment;
    private boolean showZeroHours;

    // Availability Report
    private boolean showReqReason;
    private boolean showReqType;
    private boolean showReqDuration;
    private boolean showReqStatus;
    private boolean showReqStartDate;
    private boolean showReqEndDate;

    //Staff in-out report
    private boolean showCheckedOut;
    private boolean showCheckedIn;
    private boolean showBudgetedHours;
    private boolean showTimesheetHours;
    private boolean showLaunchHours;
    private boolean showActualInHours;
    private boolean showLeaveRequest;
    private boolean showMissingHours;
    private boolean showFinImpact;

    //Performance Appraisals
    private boolean showAssessmentName;
    private boolean showInitiatorName;
    private boolean showReviewDate;
    private boolean showReviewTemplate;
    private boolean showOverallRate;
    private boolean showAppraisalsType;
    private boolean show360Appraisals;

    private ListingFilterParameter filterParametrs;
    private long fromDate;
    private String fromDateStr;
    private long toDate;
    private String toDateStr;

    public CustomReportRequestObject() {

    }

    // TimeSheet Report Transfer Object initialization

    public CustomReportRequestObject(ListingFilterParameter filterParametrs, boolean showClient,
                                     boolean showProject, boolean showDepartment, boolean showEmployee, boolean showTask, boolean showDate, boolean showDescription,
                                     boolean showPercentCompleted, boolean showApprovedHours, boolean showStatus, boolean showTimesheetStatus/*, boolean showMissingHours, boolean showFinImpact*/) {
        this.filterParametrs = filterParametrs;
        this.showClient = showClient;
        this.showDate = showDate;
        this.showDepartment = showDepartment;
        this.showEmployee = showEmployee;
        this.showPercentCompleted = showPercentCompleted;
        this.showApprovedHours = showApprovedHours;
        this.showProject = showProject;
        this.showTask = showTask;
        this.showDescription = showDescription;
        this.fromDate = filterParametrs.getStartDate().getTime();
        this.toDate = filterParametrs.getEndDate().getTime();
        this.toDate = filterParametrs.getEndDate().getTime();
        this.fromDateStr = DateUtils.formatToParse(filterParametrs.getStartDate());
        this.toDateStr = DateUtils.formatToParse(filterParametrs.getEndDate());
        this.showStatus = showStatus;
        this.showTimesheetStatus = showTimesheetStatus;
    }

    // TimeSheet Report Transfer Object initialization

    public CustomReportRequestObject(ListingFilterParameter filterParametrs, boolean showClient,
                                     boolean showProject, boolean showDepartment, boolean showEmployee, boolean showWorkstream, boolean showTask,
                                     boolean showDate, boolean showComment, boolean showDescription,
                                     boolean showPercentCompleted, boolean showApprovedHours, boolean showNonApprovedHours,
                                     boolean showStatus, boolean showTimesheetStatus, boolean showEstimatedTime, boolean showManagerComment, boolean showZeroHours) {
        this.filterParametrs = filterParametrs;
        this.showClient = showClient;
        this.showDate = showDate;
        this.showComment = showComment;
        this.showDepartment = showDepartment;
        this.showEmployee = showEmployee;
        this.showPercentCompleted = showPercentCompleted;
        this.showApprovedHours = showApprovedHours;
        this.showNonApprovedHours = showNonApprovedHours;
        this.showProject = showProject;
        this.showWorkstream = showWorkstream;
        this.showTask = showTask;
        this.showDescription = showDescription;
        this.fromDate = filterParametrs.getNonConvertibleStartDate().getTime();
        this.toDate = filterParametrs.getNonConvertibleEndDate().getTime();
        this.fromDateStr = DateUtils.formatToParse(filterParametrs.getNonConvertibleStartDate());
        this.toDateStr = DateUtils.formatToParse(filterParametrs.getNonConvertibleEndDate());
        this.showStatus = showStatus;
        this.showTimesheetStatus = showTimesheetStatus;
        this.showEstimatedTime = showEstimatedTime;
        this.showManagerComment = showManagerComment;
        this.showZeroHours = showZeroHours;
    }

    // Appraisals Report Transfer Object initialization

    public CustomReportRequestObject(ListingFilterParameter filterParametrs, boolean showEmployee,
                                     boolean showDepartment, boolean showAssessmentName, boolean showInitiatorName, boolean showReviewDate, boolean showReviewTemplate, boolean showOverallRate,
                                     boolean showAppraisalsType) {
        this.filterParametrs = filterParametrs;
        this.showEmployee = showEmployee;
        this.showDepartment = showDepartment;
        this.showAssessmentName = showAssessmentName;
        this.showInitiatorName = showInitiatorName;
        this.showReviewDate = showReviewDate;
        this.showOverallRate = showOverallRate;
        this.showAppraisalsType = showAppraisalsType;
        this.showReviewTemplate = showReviewTemplate;
        this.fromDate = filterParametrs.getStartDate().getTime();
        this.toDate = filterParametrs.getEndDate().getTime();
        this.fromDateStr = DateUtils.formatToParse(filterParametrs.getStartDate());
        this.toDateStr = DateUtils.formatToParse(filterParametrs.getEndDate());
    }


    // Staff Availability Report Transfer Object initialization

    public CustomReportRequestObject(ListingFilterParameter filterParametrs, boolean showDescription,
                                     boolean showStatus, boolean showReason, boolean showType, boolean showStartDate, boolean showEndDate, boolean showDuration) {
        this.filterParametrs = filterParametrs;
        this.showDescription = showDescription;
        this.showReqStatus = showStatus;
        this.showReqReason = showReason;
        this.showReqType = showType;
        this.showReqStartDate = showStartDate;
        this.showReqDuration = showDuration;
        this.showReqEndDate = showEndDate;
        this.fromDate = filterParametrs.getStartDate().getTime();
        this.toDate = filterParametrs.getEndDate().getTime();
        this.fromDateStr = DateUtils.formatToParse(filterParametrs.getStartDate());
        this.toDateStr = DateUtils.formatToParse(filterParametrs.getEndDate());
    }

    // Staff In/Out Report Transfer Object initialization

    public CustomReportRequestObject(boolean showDate, boolean showCheckedOut, boolean showCheckedIn, boolean showBudgetedHours, boolean showTimesheetHours,
                                     boolean showLaunchHours, boolean showActualInHours, boolean showLeaveRequest,
                                     boolean showMissingHours, boolean showFinImpact, ListingFilterParameter filterParametrs) {
        this.filterParametrs = filterParametrs;
        this.showDate = showDate;
        this.showCheckedOut = showCheckedOut;
        this.showCheckedIn = showCheckedIn;
        this.showBudgetedHours = showBudgetedHours;
        this.showTimesheetHours = showTimesheetHours;
        this.showLaunchHours = showLaunchHours;
        this.showActualInHours = showActualInHours;
        this.showLeaveRequest = showLeaveRequest;
        this.showMissingHours = showMissingHours;
        this.showFinImpact = showFinImpact;
        this.fromDate = filterParametrs.getStartDate().getTime();
        this.toDate = filterParametrs.getEndDate().getTime();
        this.fromDateStr = DateUtils.formatToParse(filterParametrs.getStartDate());
        this.toDateStr = DateUtils.formatToParse(filterParametrs.getEndDate());
    }


    public HashMap<String, String> getRequestParams() {
        HashMap<String, String> paramMap = filterParametrs.getRequestParams();

        paramMap.put("fromDate", getAsString(fromDate));
        paramMap.put("toDate", getAsString(toDate));
        paramMap.put("fromDateStr", fromDateStr);
        paramMap.put("toDateStr", toDateStr);
        paramMap.put("groupByName", filterParametrs.getGroupByName());
        paramMap.put("groupByName", filterParametrs.getGroupByName());


        paramMap.put("showClient", getAsString(showClient));
        paramMap.put("showDate", getAsString(showDate));
        paramMap.put("showComment", getAsString(showComment));
        paramMap.put("showDepartment", getAsString(showDepartment));
        paramMap.put("showEmployee", getAsString(showEmployee));
        paramMap.put("showPercentCompleted", getAsString(showPercentCompleted));
        paramMap.put("showApprovedHours", getAsString(showApprovedHours));
        paramMap.put("showNonApprovedHours", getAsString(showNonApprovedHours));
        paramMap.put("showProject", getAsString(showProject));
        paramMap.put("showWorkstream", getAsString(showWorkstream));
        paramMap.put("showTask", getAsString(showTask));
        paramMap.put("showDescription", getAsString(showDescription));
        paramMap.put("showReqReason", getAsString(showReqReason));
        paramMap.put("showReqType", getAsString(showReqType));
        paramMap.put("showCheckedOut", getAsString(showCheckedOut));
        paramMap.put("showCheckedIn", getAsString(showCheckedIn));
        paramMap.put("showBudgetedHours", getAsString(showBudgetedHours));
        paramMap.put("showTimesheetHours", getAsString(showTimesheetHours));
        paramMap.put("showLaunchHours", getAsString(showLaunchHours));
        paramMap.put("showActualInHours", getAsString(showActualInHours));
        paramMap.put("showLeaveRequest", getAsString(showLeaveRequest));
        paramMap.put("showMissingHours", getAsString(showMissingHours));
        paramMap.put("showFinImpact", getAsString(showFinImpact));
        paramMap.put("showAssessmentName", getAsString(showAssessmentName));
        paramMap.put("showInitiatorName", getAsString(showInitiatorName));
        paramMap.put("showReviewDate", getAsString(showReviewDate));
        paramMap.put("showReviewTemplate", getAsString(showReviewTemplate));
        paramMap.put("showOverallRate", getAsString(showOverallRate));
        paramMap.put("showAppraisalsType", getAsString(showAppraisalsType));
        paramMap.put("show360Appraisals", getAsString(show360Appraisals));
        paramMap.put("showReqStatus", getAsString(showReqStatus));
        paramMap.put("showReqStartDate", getAsString(showReqStartDate));
        paramMap.put("showReqEndDate", getAsString(showReqEndDate));
        paramMap.put("showReqDuration", getAsString(showReqDuration));
        paramMap.put("showStatus", getAsString(showStatus));
        paramMap.put("showTimesheetStatus", getAsString(showTimesheetStatus));
        paramMap.put("showEstimatedTime", getAsString(showEstimatedTime));
        paramMap.put("showManagerComment", getAsString(showManagerComment));
        paramMap.put("showZeroHours", getAsString(showZeroHours));

        return paramMap;
    }

    public boolean isShowClient() {
        return showClient;
    }

    public void setShowClient(boolean showClient) {
        this.showClient = showClient;
    }

    public boolean isShowProject() {
        return showProject;
    }

    public void setShowProject(boolean showProject) {
        this.showProject = showProject;
    }

    public boolean isShowDepartment() {
        return showDepartment;
    }

    public void setShowDepartment(boolean showDepartment) {
        this.showDepartment = showDepartment;
    }

    public boolean isShowEmployee() {
        return showEmployee;
    }

    public void setShowEmployee(boolean showEmployee) {
        this.showEmployee = showEmployee;
    }

    public boolean isShowWorkstream() {
        return showWorkstream;
    }

    public void setShowWorkstream(boolean showWorkstream) {
        this.showWorkstream = showWorkstream;
    }

    public boolean isShowTask() {
        return showTask;
    }

    public void setShowTask(boolean showTask) {
        this.showTask = showTask;
    }

    public boolean isShowDate() {
        return showDate;
    }

    public void setShowDate(boolean showDate) {
        this.showDate = showDate;
    }

    public boolean isShowComment() {
        return showComment;
    }

    public void setShowComment(boolean showComment) {
        this.showComment = showComment;
    }

    public boolean isShowDescription() {
        return showDescription;
    }

    public void setShowDescription(boolean showDescription) {
        this.showDescription = showDescription;
    }

    public boolean isShowPercentCompleted() {
        return showPercentCompleted;
    }

    public void setShowPercentCompleted(boolean showPercentCompleted) {
        this.showPercentCompleted = showPercentCompleted;
    }

    public ListingFilterParameter getFilterParametrs() {
        return filterParametrs;
    }

    public void setFilterParametrs(ListingFilterParameter filterParametrs) {
        this.filterParametrs = filterParametrs;
    }

    public long getFromDate() {
        return fromDate;
    }

    public void setFromDate(long fromDate) {
        this.fromDate = fromDate;
    }

    public long getToDate() {
        return toDate;
    }

    public void setToDate(long toDate) {
        this.toDate = toDate;
    }

    public boolean isShowApprovedHours() {
        return showApprovedHours;
    }

    public void setShowApprovedHours(boolean showApprovedHours) {
        this.showApprovedHours = showApprovedHours;
    }

    public boolean isShowNonApprovedHours() {
        return showNonApprovedHours;
    }

    public void setShowNonApprovedHours(boolean showNonApprovedHours) {
        this.showNonApprovedHours = showNonApprovedHours;
    }

    public boolean isShowReqReason() {
        return showReqReason;
    }

    public void setShowReqReason(boolean showReqReason) {
        this.showReqReason = showReqReason;
    }

    public boolean isShowReqType() {
        return showReqType;
    }

    public void setShowReqType(boolean showReqType) {
        this.showReqType = showReqType;
    }

    public boolean isShowReqDuration() {
        return showReqDuration;
    }

    public void setShowReqDuration(boolean showReqDuration) {
        this.showReqDuration = showReqDuration;
    }

    public boolean isShowCheckedOut() {
        return showCheckedOut;
    }

    public void setShowCheckedOut(boolean showCheckedOut) {
        this.showCheckedOut = showCheckedOut;
    }

    public boolean isShowCheckedIn() {
        return showCheckedIn;
    }

    public void setShowCheckedIn(boolean showCheckedIn) {
        this.showCheckedIn = showCheckedIn;
    }

    public boolean isShowBudgetedHours() {
        return showBudgetedHours;
    }

    public void setShowBudgetedHours(boolean showBudgetedHours) {
        this.showBudgetedHours = showBudgetedHours;
    }

    public boolean isShowTimesheetHours() {
        return showTimesheetHours;
    }

    public void setShowTimesheetHours(boolean showTimesheetHours) {
        this.showTimesheetHours = showTimesheetHours;
    }

    public boolean isShowLaunchHours() {
        return showLaunchHours;
    }

    public void setShowLaunchHours(boolean showLaunchHours) {
        this.showLaunchHours = showLaunchHours;
    }

    public boolean isShowActualInHours() {
        return showActualInHours;
    }

    public void setShowActualInHours(boolean showActualInHours) {
        this.showActualInHours = showActualInHours;
    }

    public boolean isShowLeaveRequest() {
        return showLeaveRequest;
    }

    public void setShowLeaveRequest(boolean showLeaveRequest) {
        this.showLeaveRequest = showLeaveRequest;
    }

    public void setShowMissingHours(boolean showMissingHours) {
        this.showMissingHours = showMissingHours;
    }

    public boolean isShowMissingHours() {
        return showMissingHours;
    }

    public void setShowFinImpact(boolean showFinImpact) {
        this.showFinImpact = showFinImpact;
    }

    public boolean isShowFinImpact() {
        return showFinImpact;
    }

    public boolean isShowAssessmentName() {
        return showAssessmentName;
    }

    public void setShowAssessmentName(boolean showAssessmentName) {
        this.showAssessmentName = showAssessmentName;
    }

    public boolean isShowInitiatorName() {
        return showInitiatorName;
    }

    public void setShowInitiatorName(boolean showInitiatorName) {
        this.showInitiatorName = showInitiatorName;
    }

    public boolean isShowReviewDate() {
        return showReviewDate;
    }

    public void setShowReviewDate(boolean showReviewDate) {
        this.showReviewDate = showReviewDate;
    }

    public boolean isShowReviewTemplate() {
        return showReviewTemplate;
    }

    public void setShowReviewTemplate(boolean showReviewTemplate) {
        this.showReviewTemplate = showReviewTemplate;
    }

    public boolean isShowOverallRate() {
        return showOverallRate;
    }

    public void setShowOverallRate(boolean showOverallRate) {
        this.showOverallRate = showOverallRate;
    }

    public boolean isShowAppraisalsType() {
        return showAppraisalsType;
    }

    public void setShowAppraisalsType(boolean showAppraisalsType) {
        this.showAppraisalsType = showAppraisalsType;
    }

    public boolean isShow360Appraisals() {
        return show360Appraisals;
    }

    public void setShow360Appraisals(boolean show360Appraisals) {
        this.show360Appraisals = show360Appraisals;
    }

    public boolean isShowReqStatus() {
        return showReqStatus;
    }

    public void setShowReqStatus(boolean showReqStatus) {
        this.showReqStatus = showReqStatus;
    }

    public boolean isShowReqStartDate() {
        return showReqStartDate;
    }

    public void setShowReqStartDate(boolean showReqStartDate) {
        this.showReqStartDate = showReqStartDate;
    }

    public boolean isShowReqEndDate() {
        return showReqEndDate;
    }

    public void setShowReqEndDate(boolean showReqEndDate) {
        this.showReqEndDate = showReqEndDate;
    }

    public boolean isShowStatus() {
        return showStatus;
    }

    public void setShowStatus(boolean showStatus) {
        this.showStatus = showStatus;
    }

    public boolean isShowTimesheetStatus() {
        return showTimesheetStatus;
    }

    public void setShowTimesheetStatus(boolean showTimesheetStatus) {
        this.showTimesheetStatus = showTimesheetStatus;
    }

    public boolean isShowEstimatedTime() {
        return showEstimatedTime;
    }

    public void setShowEstimatedTime(boolean showEstimatedTime) {
        this.showEstimatedTime = showEstimatedTime;
    }

    public boolean isShowManagerComment() {
        return showManagerComment;
    }

    public void setShowManagerComment(boolean showManagerComment) {
        this.showManagerComment = showManagerComment;
    }

    public boolean isShowZeroHours() {
        return showZeroHours;
    }

    public void setShowZeroHours(boolean showZeroHours) {
        this.showZeroHours = showZeroHours;
    }

    public String getFromDateStr() {
        return fromDateStr;
    }

    public void setFromDateStr(String fromDateStr) {
        this.fromDateStr = fromDateStr;
    }

    public String getToDateStr() {
        return toDateStr;
    }

    public void setToDateStr(String toDateStr) {
        this.toDateStr = toDateStr;
    }
}

