package com.edatasite.workforce.gwt.profile.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 20.11.2010
 * Time: 19:12:41
 * To change this template use File | Settings | File Templates.
 */
public class PMNumberingSettings implements IsSerializable {

    private Integer objectID;
    private String projectNumberingFormat;
    private String taskNumberingFormat;
    private String opportunityNumberingFormat;
    private String productNumberingFormat;
    private String rentalOrderNumberingFormat;
    private String productCategoryNumberingFormat;
    private String fixedAssetNumberingFormat;
    private String rfpNumberingFormat;
    private String expenseNumberingFormat;
    private String mtNumberingFormat;
    private String bpNumberingFormat;
    private String brNumberingFormat;
    private String cpNumberingFormat;
    private String crNumberingFormat;
    private String prNumberingFormat;
    private String scNumberingFormat;
    private String crfNumberingFormat; //customer Refund
    private String srfNumberingFormat; //supplier Refund
    private String caNumberingFormat;
    private String mcaNumberingFormat;
    private String saNumberingFormat;
    private String stNumberingFormat;
    private String rpNumberingFormat;
    private String pbNumberingFormat;
    private String trackerPrefix;
    private Boolean automatic = false;
    private Boolean automaticApproval = false;
    private Boolean waitingForApproval = false;
    private Boolean showTaskRelated = false;
    private Boolean validateTaskStart = false;
    private Boolean validateTaskEnd = false;
    private Boolean validateHoliday = false;
    private Boolean validateLeaveRequest = false;
    private Boolean validateMaximumHours = false;
    private Boolean validateDayOff = false;
    private Boolean validatePastTimesheet = false;
    private Boolean validateFutureTimesheet = false;
    private Boolean validateTimeslot = false;
    private Boolean showCompletedTasks = false;
    private Boolean timesheetCommentRequired = false;
    private Boolean timesheetApprovalCommentRequired = true;
    private Boolean dailyFillTimesheetFromResUtilRequired = false;
    private Boolean showToDoListTasks = true;
    private Boolean showTimesheetHourTypes = false;
    private Boolean enableMultipleTimerInstances = true;
    private Boolean saveTimerIntoTimesheetAutomatically = false;
    private Boolean sortTimesheetByTaskName = false;
    private Boolean barcodeNumbering = false;
    private Integer maximumHours = 0;
    private Integer pastTimesheetDays = 0;
    private Integer futureTimesheetDays = 0;
    private Date projectNumberRestartDate;
    private Date leaveRequestNumberRestartDate;
    private String delimetrProject;
    private String delimetrTask;
    private Integer projectIntNumber;
    private Integer projectLastIntNumber;
    private Integer leaveRequestIntNumber;
    private Integer leaveRequestLastIntNumber;
    private Integer timesheetWeekStart = 2;//2 is Monday
    private String workstreamNumberingFormat;
    private String delimetrWorkstream;
    private String employeeNumberingFormat;
    private String delimetrEmployeeNumbering;
    private String departmentNumberingFormat;
    private String delimetrDepartmentFormat;
    private String locationNumberingFormat;
    private String delimetrLocationNumbering;
    private String leaveRequestNumberingFormat;
    private String delimetrLeaveRequestNumbering;
    private String positionNumberingFormat;
    private String personalGoalNumberingFormat;
    private String projectGoalNumberingFormat;
    private String delimetrPositionNumbering;
    private String delimetrPersonalGoalNumbering;
    private String delimetrProjectGoalNumbering;
    private String placementNumberingFormat;
    private String delimetrPlacementNumbering;
    private String timesheetDateFormat;
    private String barcodeType;
    private boolean validateTimesheetEstimate;

    private String purchaseOrderNumberingFormat;
    private String piNumberingFormat;
    private String cnNumberingFormat;
    private String dnNumberingFormat;
    private String invoiceCreditNoteNumberingFormat;
    private String salesQuoteNumberingFormat;
    private String salesOrderNumberingFormat;
    private String invoiceNumberingFormat;

    private Boolean numberingRestartEnabled;
    private Integer numberingRestartDate;
    private Integer numberingRestartMonth;
    private String grnNumberFormat;
    private String gdnNumberFormat;

    public PMNumberingSettings() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getProjectNumberingFormat() {
        return projectNumberingFormat;
    }

    public void setProjectNumberingFormat(String projectNumberingFormat) {
        this.projectNumberingFormat = projectNumberingFormat;
    }

    public String getTaskNumberingFormat() {
        return taskNumberingFormat;
    }

    public void setTaskNumberingFormat(String taskNumberingFormat) {
        this.taskNumberingFormat = taskNumberingFormat;
    }

    public String getOpportunityNumberingFormat() {
        return opportunityNumberingFormat;
    }

    public void setOpportunityNumberingFormat(String opportunityNumberingFormat) {
        this.opportunityNumberingFormat = opportunityNumberingFormat;
    }

    public String getProductNumberingFormat() {
        return productNumberingFormat;
    }

    public void setProductNumberingFormat(String productNumberingFormat) {
        this.productNumberingFormat = productNumberingFormat;
    }

    public String getRentalOrderNumberingFormat() {
        return this.rentalOrderNumberingFormat;
    }

    public void setRentalOrderNumberingFormat(final String rentalOrderNumberingFormat) {
        this.rentalOrderNumberingFormat = rentalOrderNumberingFormat;
    }

    public String getProductCategoryNumberingFormat() {
        return productCategoryNumberingFormat;
    }

    public void setProductCategoryNumberingFormat(String productCategoryNumberingFormat) {
        this.productCategoryNumberingFormat = productCategoryNumberingFormat;
    }

    public String getFixedAssetNumberingFormat() {
        return fixedAssetNumberingFormat;
    }

    public void setFixedAssetNumberingFormat(String fixedAssetNumberingFormat) {
        this.fixedAssetNumberingFormat = fixedAssetNumberingFormat;
    }

    public void setTrackerPrefix(String trackerPrefix) {
        this.trackerPrefix = trackerPrefix;
    }

    public String getTrackerPrefix() {
        return trackerPrefix;
    }

    public Boolean isAutomatic() {
        return automatic;
    }

    public void setAutomatic(Boolean automatic) {
        this.automatic = automatic;
    }

    public Boolean isAutomaticApproval() {
        return automaticApproval;
    }

    public void setAutomaticApproval(Boolean automaticApproval) {
        this.automaticApproval = automaticApproval;
    }

    public String getExpenseNumberingFormat() {
        return expenseNumberingFormat;
    }

    public void setExpenseNumberingFormat(String expenseNumberingFormat) {
        this.expenseNumberingFormat = expenseNumberingFormat;
    }

    public Boolean getShowTaskRelated() {
        return showTaskRelated;
    }

    public void setShowTaskRelated(Boolean showTaskRelated) {
        this.showTaskRelated = showTaskRelated;
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

    public Boolean getValidateDayOff() {
        return validateDayOff;
    }

    public void setValidateDayOff(Boolean validateDayOff) {
        this.validateDayOff = validateDayOff;
    }

    public Integer getPastTimesheetDays() {
        return pastTimesheetDays;
    }

    public void setPastTimesheetDays(Integer pastTimesheetDays) {
        this.pastTimesheetDays = pastTimesheetDays;
    }

    public Integer getFutureTimesheetDays() {
        return futureTimesheetDays;
    }

    public void setFutureTimesheetDays(Integer futureTimesheetDays) {
        this.futureTimesheetDays = futureTimesheetDays;
    }

    public Boolean getValidatePastTimesheet() {
        return validatePastTimesheet;
    }

    public void setValidatePastTimesheet(Boolean validatePastTimesheet) {
        this.validatePastTimesheet = validatePastTimesheet;
    }

    public Boolean getValidateFutureTimesheet() {
        return validateFutureTimesheet;
    }

    public void setValidateFutureTimesheet(Boolean validateFutureTimesheet) {
        this.validateFutureTimesheet = validateFutureTimesheet;
    }

    public Boolean getValidateTimeslot() {
        return validateTimeslot;
    }

    public void setValidateTimeslot(Boolean validateTimeslot) {
        this.validateTimeslot = validateTimeslot;
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

    public Boolean getDailyFillTimesheetFromResUtilRequired() {
        return dailyFillTimesheetFromResUtilRequired;
    }

    public void setDailyFillTimesheetFromResUtilRequired(Boolean dailyFillTimesheetFromResUtilRequired) {
        this.dailyFillTimesheetFromResUtilRequired = dailyFillTimesheetFromResUtilRequired;
    }

    public Boolean getShowToDoListTasks() {
        return showToDoListTasks;
    }

    public void setShowToDoListTasks(Boolean showToDoListTasks) {
        this.showToDoListTasks = showToDoListTasks;
    }

    public Boolean getShowTimesheetHourTypes() {
        return showTimesheetHourTypes;
    }

    public void setShowTimesheetHourTypes(Boolean showTimesheetHourTypes) {
        this.showTimesheetHourTypes = showTimesheetHourTypes;
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

    public Integer getMaximumHours() {
        return maximumHours;
    }

    public void setMaximumHours(Integer maximumHours) {
        this.maximumHours = maximumHours;
    }

    public Date getProjectNumberRestartDate() {
        return projectNumberRestartDate;
    }

    public void setProjectNumberRestartDate(Date projectNumberRestartDate) {
        this.projectNumberRestartDate = projectNumberRestartDate;
    }

    public Date getLeaveRequestNumberRestartDate() {
        return leaveRequestNumberRestartDate;
    }

    public void setLeaveRequestNumberRestartDate(Date leaveRequestNumberRestartDate) {
        this.leaveRequestNumberRestartDate = leaveRequestNumberRestartDate;
    }

    public String getDelimetrProject() {
        return delimetrProject;
    }

    public void setDelimetrProject(String delimetrProject) {
        this.delimetrProject = delimetrProject;
    }

    public String getDelimetrTask() {
        return delimetrTask;
    }

    public void setDelimetrTask(String delimetrTask) {
        this.delimetrTask = delimetrTask;
    }

    public Integer getProjectIntNumber() {
        return projectIntNumber;
    }

    public void setProjectIntNumber(Integer projectIntNumber) {
        this.projectIntNumber = projectIntNumber;
    }

    public Integer getProjectLastIntNumber() {
        return projectLastIntNumber;
    }

    public void setProjectLastIntNumber(Integer projectLastIntNumber) {
        this.projectLastIntNumber = projectLastIntNumber;
    }

    public Integer getTimesheetWeekStart() {
        return timesheetWeekStart;
    }

    public void setTimesheetWeekStart(Integer timesheetWeekStart) {
        this.timesheetWeekStart = timesheetWeekStart;
    }

    public String getWorkstreamNumberingFormat() {
        return workstreamNumberingFormat;
    }

    public void setWorkstreamNumberingFormat(String workstreamNumberingFormat) {
        this.workstreamNumberingFormat = workstreamNumberingFormat;
    }

    public String getDelimetrWorkstream() {
        return delimetrWorkstream;
    }

    public void setDelimetrWorkstream(String delimetrWorkstream) {
        this.delimetrWorkstream = delimetrWorkstream;
    }

    public String getEmployeeNumberingFormat() {
        return employeeNumberingFormat;
    }

    public void setEmployeeNumberingFormat(String employeeNumberingFormat) {
        this.employeeNumberingFormat = employeeNumberingFormat;
    }

    public String getDelimetrEmployeeNumbering() {
        return delimetrEmployeeNumbering;
    }

    public void setDelimetrEmployeeNumbering(String delimetrEmployeeNumbering) {
        this.delimetrEmployeeNumbering = delimetrEmployeeNumbering;
    }

    public String getMtNumberingFormat() {
        return mtNumberingFormat;
    }

    public void setMtNumberingFormat(String mtNumberingFormat) {
        this.mtNumberingFormat = mtNumberingFormat;
    }

    public String getBpNumberingFormat() {
        return bpNumberingFormat;
    }

    public void setBpNumberingFormat(String bpNumberingFormat) {
        this.bpNumberingFormat = bpNumberingFormat;
    }

    public String getBrNumberingFormat() {
        return brNumberingFormat;
    }

    public void setBrNumberingFormat(String brNumberingFormat) {
        this.brNumberingFormat = brNumberingFormat;
    }

    public String getCpNumberingFormat() {
        return cpNumberingFormat;
    }

    public void setCpNumberingFormat(String cpNumberingFormat) {
        this.cpNumberingFormat = cpNumberingFormat;
    }

    public String getCrNumberingFormat() {
        return crNumberingFormat;
    }

    public void setCrNumberingFormat(String crNumberingFormat) {
        this.crNumberingFormat = crNumberingFormat;
    }

    public String getPrNumberingFormat() {
        return prNumberingFormat;
    }

    public void setPrNumberingFormat(String prNumberingFormat) {
        this.prNumberingFormat = prNumberingFormat;
    }

    public String getScNumberingFormat() {
        return scNumberingFormat;
    }

    public void setScNumberingFormat(String scNumberingFormat) {
        this.scNumberingFormat = scNumberingFormat;
    }

    public String getRpNumberingFormat() {
        return rpNumberingFormat;
    }

    public void setRpNumberingFormat(String rpNumberingFormat) {
        this.rpNumberingFormat = rpNumberingFormat;
    }

    public String getPbNumberingFormat() {
        return pbNumberingFormat;
    }

    public void setPbNumberingFormat(String pbNumberingFormat) {
        this.pbNumberingFormat = pbNumberingFormat;
    }

    public Boolean getSortTimesheetByTaskName() {
        return sortTimesheetByTaskName;
    }

    public void setSortTimesheetByTaskName(Boolean sortTimesheetByTaskName) {
        this.sortTimesheetByTaskName = sortTimesheetByTaskName;
    }

    public Boolean getBarcodeNumbering() {
        return barcodeNumbering;
    }

    public void setBarcodeNumbering(Boolean barcodeNumbering) {
        this.barcodeNumbering = barcodeNumbering;
    }

    public String getTimesheetDateFormat() {
        return timesheetDateFormat;
    }

    public void setTimesheetDateFormat(String timesheetDateFormat) {
        this.timesheetDateFormat = timesheetDateFormat;
    }

    public String getBarcodeType() {
        return barcodeType;
    }

    public void setBarcodeType(String barcodeType) {
        this.barcodeType = barcodeType;
    }

    public void setValidateTimesheetEstimate(boolean validateTimesheetEstimate) {
        this.validateTimesheetEstimate = validateTimesheetEstimate;
    }

    public boolean isValidateTimesheetEstimate() {
        return validateTimesheetEstimate;
    }

    public String getPurchaseOrderNumberingFormat() {
        return purchaseOrderNumberingFormat;
    }

    public void setPurchaseOrderNumberingFormat(String purchaseOrderNumberingFormat) {
        this.purchaseOrderNumberingFormat = purchaseOrderNumberingFormat;
    }

    public String getPiNumberingFormat() {
        return piNumberingFormat;
    }

    public void setPiNumberingFormat(String piNumberingFormat) {
        this.piNumberingFormat = piNumberingFormat;
    }

    public String getCnNumberingFormat() {
        return cnNumberingFormat;
    }

    public void setCnNumberingFormat(String cnNumberingFormat) {
        this.cnNumberingFormat = cnNumberingFormat;
    }

    public String getDnNumberingFormat() {
        return dnNumberingFormat;
    }

    public void setDnNumberingFormat(String dnNumberingFormat) {
        this.dnNumberingFormat = dnNumberingFormat;
    }

    public String getSalesQuoteNumberingFormat() {
        return salesQuoteNumberingFormat;
    }

    public void setSalesQuoteNumberingFormat(String salesQuoteNumberingFormat) {
        this.salesQuoteNumberingFormat = salesQuoteNumberingFormat;
    }

    public String getSalesOrderNumberingFormat() {
        return salesOrderNumberingFormat;
    }

    public void setSalesOrderNumberingFormat(String salesOrderNumberingFormat) {
        this.salesOrderNumberingFormat = salesOrderNumberingFormat;
    }

    public String getInvoiceNumberingFormat() {
        return invoiceNumberingFormat;
    }

    public void setInvoiceNumberingFormat(String invoiceNumberingFormat) {
        this.invoiceNumberingFormat = invoiceNumberingFormat;
    }

    public Boolean isNumberingRestartEnabled() {
        return numberingRestartEnabled != null && numberingRestartEnabled;
    }

    public void setNumberingRestartEnabled(Boolean numberingRestartEnabled) {
        this.numberingRestartEnabled = numberingRestartEnabled;
    }

    public Integer getNumberingRestartDate() {
        return numberingRestartDate;
    }

    public void setNumberingRestartDate(Integer numberingRestartDate) {
        this.numberingRestartDate = numberingRestartDate;
    }

    public Integer getNumberingRestartMonth() {
        return numberingRestartMonth;
    }

    public void setNumberingRestartMonth(Integer numberingRestartMonth) {
        this.numberingRestartMonth = numberingRestartMonth;
    }

    public String getCaNumberingFormat() {
        return caNumberingFormat;
    }

    public void setCaNumberingFormat(String caNumberingFormat) {
        this.caNumberingFormat = caNumberingFormat;
    }

    public String getMcaNumberingFormat() {
        return this.mcaNumberingFormat;
    }

    public void setMcaNumberingFormat(final String mcaNumberingFormat) {
        this.mcaNumberingFormat = mcaNumberingFormat;
    }

    public String getSaNumberingFormat() {
        return saNumberingFormat;
    }

    public void setSaNumberingFormat(String saNumberingFormat) {
        this.saNumberingFormat = saNumberingFormat;
    }

    public String getInvoiceCreditNoteNumberingFormat() {
        return invoiceCreditNoteNumberingFormat;
    }

    public void setInvoiceCreditNoteNumberingFormat(String invoiceCreditNoteNumberingFormat) {
        this.invoiceCreditNoteNumberingFormat = invoiceCreditNoteNumberingFormat;
    }

    public void setGrnNumberFormat(String grnNumberFormat) {
        this.grnNumberFormat = grnNumberFormat;
    }

    public String getGrnNumberFormat() {
        return grnNumberFormat;
    }

    public String getGdnNumberFormat() {
        return gdnNumberFormat;
    }

    public void setGdnNumberFormat(String gdnNumberFormat) {
        this.gdnNumberFormat = gdnNumberFormat;
    }

    public String getStNumberingFormat() {
        return stNumberingFormat;
    }

    public void setStNumberingFormat(String stNumberingFormat) {
        this.stNumberingFormat = stNumberingFormat;
    }

    public String getRfpNumberingFormat() {
        return this.rfpNumberingFormat;
    }

    public void setRfpNumberingFormat(final String rfpNumberingFormat) {
        this.rfpNumberingFormat = rfpNumberingFormat;
    }

    public String getCrfNumberingFormat() {
        return this.crfNumberingFormat;
    }

    public void setCrfNumberingFormat(final String crfNumberingFormat) {
        this.crfNumberingFormat = crfNumberingFormat;
    }

    public String getSrfNumberingFormat() {
        return this.srfNumberingFormat;
    }

    public void setSrfNumberingFormat(final String srfNumberingFormat) {
        this.srfNumberingFormat = srfNumberingFormat;
    }

    public Boolean isWaitingForApproval() {
        return waitingForApproval;
    }

    public void setWaitingForApproval(Boolean waitingForApproval) {
        this.waitingForApproval = waitingForApproval;
    }

    public String getDepartmentNumberingFormat() {
        return departmentNumberingFormat;
    }

    public void setDepartmentNumberingFormat(String departmentNumberingFormat) {
        this.departmentNumberingFormat = departmentNumberingFormat;
    }

    public String getDelimetrDepartmentFormat() {
        return delimetrDepartmentFormat;
    }

    public void setDelimetrDepartmentFormat(String delimetrDepartmentFormat) {
        this.delimetrDepartmentFormat = delimetrDepartmentFormat;
    }

    public String getLeaveRequestNumberingFormat() {
        return leaveRequestNumberingFormat;
    }

    public void setLeaveRequestNumberingFormat(String leaveRequestNumberingFormat) {
        this.leaveRequestNumberingFormat = leaveRequestNumberingFormat;
    }

    public String getDelimetrLeaveRequestNumbering() {
        return delimetrLeaveRequestNumbering;
    }

    public void setDelimetrLeaveRequestNumbering(String delimetrLeaveRequestNumbering) {
        this.delimetrLeaveRequestNumbering = delimetrLeaveRequestNumbering;
    }

    public String getPositionNumberingFormat() {
        return positionNumberingFormat;
    }

    public void setPositionNumberingFormat(String positionNumberingFormat) {
        this.positionNumberingFormat = positionNumberingFormat;
    }

    public String getDelimetrPositionNumbering() {
        return delimetrPositionNumbering;
    }

    public void setDelimetrPositionNumbering(String delimetrPositionNumbering) {
        this.delimetrPositionNumbering = delimetrPositionNumbering;
    }

    public String getLocationNumberingFormat() {
        return locationNumberingFormat;
    }

    public void setLocationNumberingFormat(String locationNumberingFormat) {
        this.locationNumberingFormat = locationNumberingFormat;
    }

    public String getDelimetrLocationNumbering() {
        return delimetrLocationNumbering;
    }

    public void setDelimetrLocationNumbering(String delimetrLocationNumbering) {
        this.delimetrLocationNumbering = delimetrLocationNumbering;
    }

    public String getPersonalGoalNumberingFormat() {
        return personalGoalNumberingFormat;
    }

    public void setPersonalGoalNumberingFormat(String personalGoalNumberingFormat) {
        this.personalGoalNumberingFormat = personalGoalNumberingFormat;
    }

    public String getProjectGoalNumberingFormat() {
        return projectGoalNumberingFormat;
    }

    public void setProjectGoalNumberingFormat(String projectGoalNumberingFormat) {
        this.projectGoalNumberingFormat = projectGoalNumberingFormat;
    }

    public String getDelimetrPersonalGoalNumbering() {
        return delimetrPersonalGoalNumbering;
    }

    public void setDelimetrPersonalGoalNumbering(String delimetrPersonalGoalNumbering) {
        this.delimetrPersonalGoalNumbering = delimetrPersonalGoalNumbering;
    }

    public String getDelimetrProjectGoalNumbering() {
        return delimetrProjectGoalNumbering;
    }

    public void setDelimetrProjectGoalNumbering(String delimetrProjectGoalNumbering) {
        this.delimetrProjectGoalNumbering = delimetrProjectGoalNumbering;
    }

    public String getPlacementNumberingFormat() {
        return this.placementNumberingFormat;
    }

    public void setPlacementNumberingFormat(final String placementNumberingFormat) {
        this.placementNumberingFormat = placementNumberingFormat;
    }

    public String getDelimetrPlacementNumbering() {
        return this.delimetrPlacementNumbering;
    }

    public void setDelimetrPlacementNumbering(final String delimetrPlacementNumbering) {
        this.delimetrPlacementNumbering = delimetrPlacementNumbering;
    }

    public Integer getLeaveRequestIntNumber() {
        return leaveRequestIntNumber;
    }

    public void setLeaveRequestIntNumber(Integer leaveRequestIntNumber) {
        this.leaveRequestIntNumber = leaveRequestIntNumber;
    }

    public Integer getLeaveRequestLastIntNumber() {
        return leaveRequestLastIntNumber;
    }

    public void setLeaveRequestLastIntNumber(Integer leaveRequestLastIntNumber) {
        this.leaveRequestLastIntNumber = leaveRequestLastIntNumber;
    }
}
