package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Optional;

/**
 * User: Sherzod
 * Date: 20.11.2010
 * Time: 19:28:36
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "pmnumberingsettings")
public class EdsNumberingSettings extends EdsObject implements Constants {
    public static final DecimalFormat decimalFormat = new DecimalFormat("0000");
    public static final DecimalFormat threeDecimalFormat = new DecimalFormat("000");
    public static final DecimalFormat twoDecimalFormat = new DecimalFormat("00");

    public static final String DEF_PROJ_PREFIX = "P";
    public static final String DEF_TASK_PREFIX = "T";
    public static final String DEF_ISSUE_PREFIX = "I";
    public static final String DEF_OPPORTUNITY_PREFIX = String.valueOf((new Date()).getYear()).substring(1, 3);
    public static final String DEF_ACCOUNT_PREFIX = "ACCOUNT_";
    public static final String DEF_CUSTOMER_PREFIX = "CUS_";
    public static final String DEF_SUPPLIER_PREFIX = "SUP_";
    public static final String DEF_PROD_PREFIX = "PD";
    public static final String DEF_RENTAL_ORDER_PREFIX = "BOOKING";
    public static final String DEF_PROD_CATEGOR_PREFIX = "PC";
    public static final String DEF_EX_PREFIX = "EX";
    public static final String DEF_FIXED_ASSET_PREFIX = "FA";
    public static final String DEF_RFP_PREFIX = "RFP";
    public static final String DEF_TRACKER_PREFIX = "TR";
    public static final String DEF_MEETING_PREFIX = "M";
    public static final String DEF_BOOKING_ITEM_PREFIX = "BI";
    public static final String DEF_VACANCY_PREFIX = "V";
    public static final String DEF_CANDIDATE_PREFIX = "C";
    public static final String DEF_COURSE_PREFIX = "CO";
    public static final String DEF_ENQUIRY_PREFIX = "EN";
    public static final String DEF_COURSE_BOOKING_PREFIX = "CB";
    public static final String DEF_WORKSTREAM_PREFIX = "W";
    public static final String DEF_EMPLOYEE_PREFIX = "EMP";
    public static final String DEF_DEPARTMENT_PREFIX = "DEP";
    public static final String DEF_LEAVE_REQUEST_PREFIX = "LVR";
    public static final String DEF_POSITION_PREFIX = "POS";
    public static final String DEF_LOCATION_PREFIX = "L";
    public static final String DEF_EXCHANGE_CARD_PREFIX = "EXC";
    public static final String DEF_MANUAL_TRANSACTION_PREFIX = "MT";
    public static final String DEF_BANK_PAYMENT_PREFIX = "SPM";
    public static final String DEF_BANK_RECEIPT_PREFIX = "REM";
    public static final String DEF_CASH_PAYMENT_PREFIX = "CP";
    public static final String DEF_CASH_RECEIPT_PREFIX = "CR";
    public static final String DEF_CONSIGNMENT_PREFIX = "C";
    public static final String DEF_CASH_ADVANCE_PREFIX = "CA";
    public static final String DEF_MULTI_CASH_ADVANCE_PREFIX = "MCA";
    public static final String DEF_STOCK_ADJUMENT = "SA";
    public static final String DEF_STOCK_TRANSFER = "ST";
    public static final String DEF_END_OF_SERVICE = "EOS";
    public static final String DEF_RECEIVE_PAYMENT_PREFIX = "RP";
    public static final String DEF_PAY_BILL_PREFIX = "PB";
    public static final String DEF_GRN_PREFIX = "GRN";
    public static final String DEF_GDN_PREFIX = "GDN";
    public static final String DEF_SHIFT_PREFIX = "SH";
    public static final String DEF_BRIGADA_PREFIX = "T";
    public static final String DEF_ROTATION_PREFIX = "R";
    public static final String DEF_ASSEMBLY_PREFIX = "AI";
    public static final String DEF_PROJ_GOAL_PREFIX = "Q";
    public static final String DEF_GROUP_PLACEMENT_PREFIX = "GP";
    public static final String DEF_PLACEMENT_PREFIX = "P";
    public static final String DEF_OVRTIME_PREFIX = "OVR";
    public static final String DEF_BACKUPS_EMPLOYEE_PREFIX = "BCE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String projectNumberingFormat;
    private String projectGoalNumberingFormat;
    private String delimetrProjectGoalNumbering;
    private String personalGoalNumberingFormat;
    private String delimetrPersonalGoalNumbering;
    private String taskNumberingFormat;
    private String issueNumberingFormat;
    private String opportunityNumberingFormat;
    private String accountFormat;
    private String clientFormat;
    private String supplierFormat;
    private String productNumberingFormat;
    private String rentalOrderNumberingFormat;
    private String productCategoryNumberingFormat;
    private String fixedAssetNumberingFormat;
    private String rfpNumberingFormat;
    private String expenseNumberingFormat;
    private String mtNumberingFormat;
    private String grnNumberFormat; //PO grn number
    private String gdnNumberFormat; //SO gdn number
    private String smNumberingFormat; //bank payment or pay bill
    private String rmNumberingFormat; // bank receipt or receive payment
    private String cpNumberingFormat; //cash payment
    private String crNumberingFormat; // cash receipt
    private String prNumberingFormat;
    private String scNumberingFormat;
    private String crfNumberingFormat; //customer Refund
    private String srfNumberingFormat; //supplier Refund
    private String stockAdjustmentNumberingFormat;
    private String stockTransferNumberingFormat;
    private String rpNumberingFormat; // receive payment
    private String pbNumberingFormat; // pay bill
    private String overtimeNumberingFormat; // overtime
    private String backupsEmployeeNumberingFormat;
    private String delimetrBackupsEmployeeNumberingFormat;

    private String trackerPrefix;
    private Boolean automatic;//calculation method of task, task and project percentage
    private Boolean automaticApproval;//approval method of timesheet hours
    private Boolean waitingForApproval;//approval method of timesheet hours
    private Boolean validateTaskStart;
    private Boolean validateTaskEnd;
    private Boolean validateHoliday;
    private Boolean validateLeaveRequest;
    private Boolean validateMaximumHours;
    private Boolean validateDayOff;
    private Boolean validatePastTimesheet;
    private Boolean validateFutureTimesheet;
    private Boolean validateTimeslot;
    private Integer maximumHours;
    private Integer pastTimesheetDays;
    private Integer futureTimesheetDays;
    private Date projectNumberRestartDate;
    private Date leaveRequestNumberRestartDate;
    private String delimetrProject;
    private String delimetrTask;
    private Integer projectIntNumber;
    private Integer projectLastIntNumber;
    private Integer leaveRequestIntNumber;
    private Integer leaveRequestLastIntNumber;
    private String vacancyNumberingFormat;
    private String candidateNumberingFormat;
    private String consignmentNumberingFormat;
    private String shiftNumberingFormat;
    private String delimetrShiftNumberingFormat;
    private String brigadaNumberingFormat;
    private String delimetrBrigadaNumberingFormat;
    private String rotationNumberingDate;
    private String delimetrRotationNumberingFormat;
    private String assemblyNumberingDate;
    private String delimetrAssemblyNumberingFormat;
    private String groupPlacementNumberingFormat;
    private String delimetrGroupPlacementNumberingFormat;
    private String delimetrOvertimeNumbering;

    @Column(nullable = false, columnDefinition = "int4 default 2")//1-Sunday, 2-Monday, ... 7-Saturday
    private Integer timesheetWeekStart = 2;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean showCompletedTasks = false;//during the completion week
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean timesheetCommentRequired = false;
    @Column(nullable = true, columnDefinition = "boolean default true")
    private Boolean timesheetApprovalCommentRequired = true;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean dailyFillTimesheetFromResUtilRequired = false;
    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean showToDoListTasks = true;//in the timesheet
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean showTimesheetHourTypes = false;//in the timesheet when filling timesheet above comment
    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean enableMultipleTimerInstances = true;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean saveTimerIntoTimesheetAutomatically = false;

    private String courseNumberingFormat;
    private String enquiryNumberingFormat;
    private String courseBoookingNumberingFormat;
    private String workstreamNumberingFormat;
    private String delimetrWorkstream;

    private Integer defaultReminderID;//
    private String employeeNumberingFormat;
    private String delimetrEmployeeNumbering;
    private String departmentNumberingFormat;
    private String delimetrDepartmentNumbering;
    private String leaveRequestNumberingFormat;
    private String delimetrLeaveRequestNumbering;
    private String positionNumberingFormat;
    private String delimetrPositionNumbering;
    private String locationNumberingFormat;
    private String delimetrLocationNumbering;
    private String placementNumberingFormat;
    private String delimetrPlacementNumbering;

    private String cashAdvanceNumberFormat;
    private String multiCashAdvanceNumberFormat;
    private String endOfServiceNumberingFormat;
    @Column(columnDefinition = "boolean default false")
    private Boolean barcodeNumbering;
    private String barcodeType;

    //exlibris
    private String exchangeCardNumberingFormat;
    private Boolean sortTimesheetByTaskName = false;
    @Column(nullable = false, columnDefinition = "varchar(255) default 'EEE, MM/d'")
    private String timesheetDateFormat = "EEE, MM/d";
    private Boolean validateTimesheetEstimate = false;

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

    public String getGoalNumberingFormat(String categoryType) {
        return switch (categoryType) {
            case "PERSONAL_GOAL" -> getPersonalGoalNumberingFormat();
            case "PROJECT_GOAL" -> getProjectGoalNumberingFormat();
            default -> null;
        };
    }

    public String getDelimetrGoalNumbering(String categoryType) {
        return switch (categoryType) {
            case "PERSONAL_GOAL" -> getDelimetrPersonalGoalNumbering();
            case "PROJECT_GOAL" -> getDelimetrProjectGoalNumbering();
            default -> null;
        };
    }

    public String getProjectGoalNumberingFormat() {
        return projectGoalNumberingFormat;
    }

    public void setProjectGoalNumberingFormat(String projectGoalNumberingFormat) {
        this.projectGoalNumberingFormat = projectGoalNumberingFormat;
    }

    public String getDelimetrProjectGoalNumbering() {
        return delimetrProjectGoalNumbering;
    }

    public void setDelimetrProjectGoalNumbering(String delimetrProjectGoalNumbering) {
        this.delimetrProjectGoalNumbering = delimetrProjectGoalNumbering;
    }

    public String getPersonalGoalNumberingFormat() {
        return personalGoalNumberingFormat;
    }

    public void setPersonalGoalNumberingFormat(String personalGoalNumberingFormat) {
        this.personalGoalNumberingFormat = personalGoalNumberingFormat;
    }

    public String getDelimetrPersonalGoalNumbering() {
        return delimetrPersonalGoalNumbering;
    }

    public void setDelimetrPersonalGoalNumbering(String delimetrPersonalGoalNumbering) {
        this.delimetrPersonalGoalNumbering = delimetrPersonalGoalNumbering;
    }

    public String getTaskNumberingFormat() {
        return taskNumberingFormat;
    }

    public void setTaskNumberingFormat(String taskNumberingFormat) {
        this.taskNumberingFormat = taskNumberingFormat;
    }

    public String getIssueNumberingFormat() {
        return issueNumberingFormat;
    }

    public void setIssueNumberingFormat(String issueNumberingFormat) {
        this.issueNumberingFormat = issueNumberingFormat;
    }

    public String getOpportunityNumberingFormat() {
        return opportunityNumberingFormat;
    }

    public void setOpportunityNumberingFormat(String opportunityNumberingFormat) {
        this.opportunityNumberingFormat = opportunityNumberingFormat;
    }

    public String getAccountFormat() {
        return accountFormat;
    }

    public void setAccountFormat(String accountFormat) {
        this.accountFormat = accountFormat;
    }

    public String getClientFormat() {
        if (clientFormat == null) {
            clientFormat = EdsNumberingSettings.DEF_CUSTOMER_PREFIX + "0000";
        }
        return clientFormat;
    }

    public void setClientFormat(String clientFormat) {
        this.clientFormat = clientFormat;
    }

    public String getSupplierFormat() {
        if (supplierFormat == null) {
            supplierFormat = EdsNumberingSettings.DEF_SUPPLIER_PREFIX + "0000";
        }
        return supplierFormat;
    }

    public void setSupplierFormat(String supplierFormat) {
        this.supplierFormat = supplierFormat;
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

    public String getRfpNumberingFormat() {
        return this.rfpNumberingFormat;
    }

    public void setRfpNumberingFormat(final String rfpNumberingFormat) {
        this.rfpNumberingFormat = rfpNumberingFormat;
    }

    public String getTrackerPrefix() {
        return trackerPrefix;
    }

    public void setTrackerPrefix(String trackerPrefix) {
        this.trackerPrefix = trackerPrefix;
    }

    public Boolean isAutomatic() {
        return automatic == null ? Boolean.FALSE : automatic;
    }

    public void setAutomatic(Boolean automatic) {
        this.automatic = automatic;
    }

    public Boolean isAutomaticApproval() {
        return automaticApproval == null ? Boolean.FALSE : automaticApproval;
    }

    public void setAutomaticApproval(Boolean automaticApproval) {
        this.automaticApproval = automaticApproval;
    }

    public Boolean isValidateTaskStart() {
        return validateTaskStart == null ? Boolean.FALSE : validateTaskStart;
    }

    public void setValidateTaskStart(Boolean validateTaskStart) {
        this.validateTaskStart = validateTaskStart;
    }

    public Boolean isValidateTaskEnd() {
        return validateTaskEnd == null ? Boolean.FALSE : validateTaskEnd;
    }

    public void setValidateTaskEnd(Boolean validateTaskEnd) {
        this.validateTaskEnd = validateTaskEnd;
    }

    public Boolean isValidateHoliday() {
        return validateHoliday == null ? Boolean.FALSE : validateHoliday;
    }

    public void setValidateHoliday(Boolean validateHoliday) {
        this.validateHoliday = validateHoliday;
    }

    public Boolean isValidateLeaveRequest() {
        return validateLeaveRequest == null ? Boolean.FALSE : validateLeaveRequest;
    }

    public void setValidateLeaveRequest(Boolean validateLeaveRequest) {
        this.validateLeaveRequest = validateLeaveRequest;
    }

    public Boolean isValidateMaximumHours() {
        return validateMaximumHours == null ? Boolean.FALSE : validateMaximumHours;
    }

    public void setValidateMaximumHours(Boolean validateTimeLimit) {
        this.validateMaximumHours = validateTimeLimit;
    }

    public Boolean isValidateDayOff() {
        return validateDayOff == null ? Boolean.FALSE : validateDayOff;
    }

    public void setValidateDayOff(Boolean validateWeekend) {
        this.validateDayOff = validateWeekend;
    }

    public Boolean isValidatePastTimesheet() {
        return validatePastTimesheet == null ? Boolean.FALSE : validatePastTimesheet;
    }

    public void setValidatePastTimesheet(Boolean validatePastTimesheet) {
        this.validatePastTimesheet = validatePastTimesheet;
    }

    public Boolean isValidateFutureTimesheet() {
        return validateFutureTimesheet == null ? Boolean.FALSE : validateFutureTimesheet;
    }

    public void setValidateFutureTimesheet(Boolean validateFutureTimesheet) {
        this.validateFutureTimesheet = validateFutureTimesheet;
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

    public Boolean isValidateTimeslot() {
        return validateTimeslot == null ? Boolean.FALSE : validateTimeslot;
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

    public NumberData parseNumberData(Integer intNumber, String numFormat) {
        return parseNumberData(intNumber, numFormat, null);
    }

    public String getExpenseNumberingFormat() {
        return expenseNumberingFormat;
    }

    public void setExpenseNumberingFormat(String expenseNumberingFormat) {
        this.expenseNumberingFormat = expenseNumberingFormat;
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

    public String getVacancyNumberingFormat() {
        return vacancyNumberingFormat;
    }

    public void setVacancyNumberingFormat(String vacancyNumberingFormat) {
        this.vacancyNumberingFormat = vacancyNumberingFormat;
    }

    public String getCandidateNumberingFormat() {
        return candidateNumberingFormat;
    }

    public void setCandidateNumberingFormat(String candidateNumberingFormat) {
        this.candidateNumberingFormat = candidateNumberingFormat;
    }

    public String getCourseNumberingFormat() {
        return courseNumberingFormat;
    }

    public void setCourseNumberingFormat(String courseNumberingFormat) {
        this.courseNumberingFormat = courseNumberingFormat;
    }

    public String getEnquiryNumberingFormat() {
        return enquiryNumberingFormat;
    }

    public void setEnquiryNumberingFormat(String enquiryNumberingFormat) {
        this.enquiryNumberingFormat = enquiryNumberingFormat;
    }

    public String getCourseBoookingNumberingFormat() {
        return courseBoookingNumberingFormat;
    }

    public void setCourseBoookingNumberingFormat(String courseBoookingNumberingFormat) {
        this.courseBoookingNumberingFormat = courseBoookingNumberingFormat;
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

    public Integer getDefaultReminderID() {
        return defaultReminderID;
    }

    public void setDefaultReminderID(Integer defaultReminderID) {
        this.defaultReminderID = defaultReminderID;
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

    public String getExchangeCardNumberingFormat() {
        return exchangeCardNumberingFormat;
    }

    public void setExchangeCardNumberingFormat(String exchangeCardNumberingFormat) {
        this.exchangeCardNumberingFormat = exchangeCardNumberingFormat;
    }

    public String getMtNumberingFormat() {
        return mtNumberingFormat;
    }

    public void setMtNumberingFormat(String mtNumberingFormat) {
        this.mtNumberingFormat = mtNumberingFormat;
    }

    public String getSmNumberingFormat() {
        return smNumberingFormat;
    }

    public void setSmNumberingFormat(String smNumberingFormat) {
        this.smNumberingFormat = smNumberingFormat;
    }

    public String getRmNumberingFormat() {
        return rmNumberingFormat;
    }

    public void setRmNumberingFormat(String rmNumberingFormat) {
        this.rmNumberingFormat = rmNumberingFormat;
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

    public String getGrnNumberFormat() {
        return grnNumberFormat;
    }

    public void setGrnNumberFormat(String grnNumberFormat) {
        this.grnNumberFormat = grnNumberFormat;
    }

    public String getGdnNumberFormat() {
        return gdnNumberFormat;
    }

    public void setGdnNumberFormat(String gdnNumberFormat) {
        this.gdnNumberFormat = gdnNumberFormat;
    }

    public Boolean getSortTimesheetByTaskName() {
        return Optional.ofNullable(sortTimesheetByTaskName).orElse(false);
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

    public boolean isValidateTimesheetEstimate() {
        return Optional.ofNullable(validateTimesheetEstimate).orElse(false);
    }

    public void setValidateTimesheetEstimate(boolean validateTimesheetEstimate) {
        this.validateTimesheetEstimate = validateTimesheetEstimate;
    }

    public String getConsignmentNumberingFormat() {
        return consignmentNumberingFormat;
    }

    public void setConsignmentNumberingFormat(String consignmentNumberingFormat) {
        this.consignmentNumberingFormat = consignmentNumberingFormat;
    }

    public Boolean getBarcodeNumbering() {
        return Optional.ofNullable(barcodeNumbering).orElse(false);
    }

    public void setBarcodeNumbering(Boolean barcodeNumbering) {
        this.barcodeNumbering = barcodeNumbering;
    }

    public String getBarcodeType() {
        return barcodeType;
    }

    public void setBarcodeType(String barcodeType) {
        this.barcodeType = barcodeType;
    }

    public String getCashAdvanceNumberFormat() {
        return cashAdvanceNumberFormat;
    }

    public void setCashAdvanceNumberFormat(String cashAdvanceNumberFormat) {
        this.cashAdvanceNumberFormat = cashAdvanceNumberFormat;
    }

    public String getMultiCashAdvanceNumberFormat() {
        return this.multiCashAdvanceNumberFormat;
    }

    public void setMultiCashAdvanceNumberFormat(final String multiCashAdvanceNumberFormat) {
        this.multiCashAdvanceNumberFormat = multiCashAdvanceNumberFormat;
    }

    public void setEndOfServiceNumberingFormat(String endOfServiceNumberingFormat) {
        this.endOfServiceNumberingFormat = endOfServiceNumberingFormat;
    }

    public String getEndOfServiceNumberingFormat() {
        return endOfServiceNumberingFormat;
    }

    public String getStockAdjustmentNumberingFormat() {
        return stockAdjustmentNumberingFormat;
    }

    public void setStockAdjustmentNumberingFormat(String stockAdjustmentNumberingFormat) {
        this.stockAdjustmentNumberingFormat = stockAdjustmentNumberingFormat;
    }

    public String getStockTransferNumberingFormat() {
        return stockTransferNumberingFormat;
    }

    public void setStockTransferNumberingFormat(String stockTransferNumberingFormat) {
        this.stockTransferNumberingFormat = stockTransferNumberingFormat;
    }

    public NumberData parseNumberData(Integer intNumber, String numFormat, DecimalFormat customFormat) {
        intNumber = (intNumber != null ? intNumber + 1 : 1);
        NumberData numberData = new NumberData();
        numberData.setNumberFormat(numFormat);
        numberData.setDelimiter(getDelimetrEmployeeNumbering());
        int splitterIndex = numFormat.lastIndexOf("_");
        String prefix = numFormat.substring(0, splitterIndex);
        Integer number = Integer.parseInt(numFormat.substring(splitterIndex + 1));

        numberData.setIntNumber(number > intNumber ? number : intNumber);
        numberData.setFirstNumberString(prefix);
        if (customFormat != null) {
            numberData.setNumberString(prefix + customFormat.format(numberData.getIntNumber()));
        } else {
            numberData.setNumberString(prefix + decimalFormat.format(numberData.getIntNumber()));
        }

        return numberData;
    }


    public static NumberData getDefaultData(Integer number, String defPrefix) {
        return getDefaultData(number, defPrefix, null);
    }

    public static NumberData getDefaultData(Integer number, String defPrefix, DecimalFormat customFormat) {
        NumberData numberData = new NumberData();
        numberData.setIntNumber(number != null ? number + 1 : 1);

        String numbering = customFormat != null ? customFormat.format(numberData.getIntNumber()) : decimalFormat.format(numberData.getIntNumber());

        if (defPrefix != null && !defPrefix.isEmpty()) {
            numberData.setNumberFormat(defPrefix + "_" + numbering);
            numberData.setFirstNumberString(defPrefix);
            numberData.setNumberString(defPrefix + numbering);
        } else {
            numberData.setNumberFormat(DEF_TASK_PREFIX + "_" + numbering);
            numberData.setFirstNumberString(DEF_TASK_PREFIX);
            numberData.setNumberString(DEF_TASK_PREFIX + numbering);
        }

        return numberData;
    }

    public static NumberData validateNumberData(NumberData numberData) {
        String sValue = numberData.getNumberString();
        if (sValue != null && sValue.matches("([a-z_A-Z]){1,}([0-9]){1,}")) {

            if (!"22240".equals(ServerSecurityContext.getInstance().getCompanyId())) {
                String st = sValue.replaceAll("\\d", "");
                String numValue = sValue.replace(st, "");
                numberData.setIntNumber(Integer.valueOf(numValue));
            }

            return numberData;
        } else if (sValue != null && sValue.matches("([0-9]){1,}")) {
            return numberData;
        } else {
            return null;
        }
    }


    public NumberData parseNumberDataForALL(Integer intNumber, String numberingFormat, String delimetr, Date startDate, String clientId, String projectNumber, String numberType) {
        if (!ServerUtils.isNullOrEmpty(numberingFormat) && !numberingFormat.contains(WIDGET_PREFIX)) {
            return parseNumberData(intNumber, numberingFormat);
        }
        String numberString = "";
        StringBuilder savedNumberFormula = new StringBuilder();
        StringBuilder fullString = new StringBuilder();
        boolean hasDay = false;
        int intNumb = (intNumber != null ? intNumber + 1 : 1);
        if ("project".equals(numberType) || "leaveRequest".equals(numberType)) {
            intNumb = (intNumber != null ? intNumber : 1);
        }
        NumberData numberData = new NumberData();
        numberData.setNumberFormat(numberingFormat);

        if (!ServerUtils.isNullOrEmpty(numberingFormat)) {
            String[] firstString = numberingFormat.split("/");
            label:
            for (String value : firstString) {
                String[] split = value.split(":");
                switch (split[0]) {
                    case WIDGET_PREFIX:
                        if (!"".equals(split[1]) && !"false".equals(split[1])) {
                            numberString += split[1];
                        } else {
                            continue;
                        }
                        break;
                    case WIDGET_PROJECT_NUMBER:
                        if (!"false".equals(split[1]) && projectNumber != null && !"".equals(projectNumber)) {
                            numberString += projectNumber;
                        } else {
                            continue;
                        }
                        break;
                    case WIDGET_DATE_YEAR:
                        if (!"".equals(split[1]) && !"false".equals(split[1])) {
                            numberString += 1900 + convertDate(split[1], startDate).getYear();
                            hasDay = true;
                        }
                        continue;
                    case WIDGET_DATE_MONTH:
                        if (!"".equals(split[1]) && !"false".equals(split[1])) {
                            numberString += refactorLessTen(1 + convertDate(split[1], startDate).getMonth());
                            hasDay = true;
                        }
                        continue;
                    case WIDGET_DATE_DAY:
                        if (!"".equals(split[1]) && !"false".equals(split[1])) {
                            numberString += refactorLessTen(convertDate(split[1], startDate).getDate());
                        } else if (!hasDay) {
                            continue;
                        }
                        break;
                    case WIDGET_CLIENT_CODE:
                        if (!"false".equals(split[1]) && clientId != null) {
                            numberString += clientId;
                        } else {
                            continue;
                        }
                        break;
                    case WIDGET_NUMBERS:
                        int number = Integer.parseInt(split[1]);
                        int num = number > intNumb ? number : intNumb;
                        String numString = "";
                        numberData.setIntNumber(num);
                        numberData.setFirstNumberString(numberString);
                        if (split[1].length() > 3) {
                            numString = decimalFormat.format(num);
                        } else if (split[1].length() > 2) {
                            numString = threeDecimalFormat.format(num);
                        } else if (split[1].length() > 1) {
                            numString = twoDecimalFormat.format(num);
                        } else {
                            numString = Integer.toString(num);
                        }
                        savedNumberFormula.append(!"".equals(numberString) ? numberString : "null");
                        savedNumberFormula.append(SAV_NUM_DEL);
                        savedNumberFormula.append(numString);
                        savedNumberFormula.append(SAV_NUM_DEL);
                        fullString.append(numberString);
                        fullString.append(numString);
                        numberString = "";
                        break;
                    case WIDGET_SUFFIX:
                        if (!"".equals(split[1]) && !"false".equals(split[1])) {
                            numberString += split[1];
                        } else {
                            continue;
                        }
                        break;
                    case WIDGET_RESTART_NUMBER_EACH_PROJECT:
                        continue;
                    case WIDGET_UNIQUE_NUMBER_ALL_PROJECT:
                        break label;
                }
                if (delimetr != null && !"".equals(delimetr)) {
                    numberString += delimetr;
                }
            }
        }
        if (numberString != null && numberString.length() > 0 && delimetr != null && !"".equals(delimetr)) {
            numberData.setLastNumberString(numberString.substring(0, numberString.length() - 1));
            fullString.append(numberString, 0, numberString.length() - 1);
            savedNumberFormula.append(numberString.equals(delimetr) ? "null" : numberString.substring(0, numberString.length() - 1));
        } else {
            numberData.setLastNumberString(numberString);
            fullString.append(numberString);
            savedNumberFormula.append(numberString.length() > 0 ? numberString : "null");

        }
        numberData.setSavedNumberFormula(savedNumberFormula.toString());
        numberData.setNumberString(fullString.toString());

        return numberData;
    }

    public NumberData parsNumberDataForEdit(Integer intNumber, String savingNumberFormat, String numberingFormat) {
        if (savingNumberFormat == null || "".equals(savingNumberFormat)) {
            System.out.println(savingNumberFormat);
            if (!numberingFormat.contains(WIDGET_PREFIX)) {
                return parseNumberData(intNumber, numberingFormat);
            }
            return parseNumberData(intNumber, "P_0001");
        }
        NumberData numberData = new NumberData();
        StringBuilder fullString = new StringBuilder();
        numberData.setNumberFormat(savingNumberFormat);
        String[] firstString = savingNumberFormat.split(SAV_NUM_DEL);
        numberData.setFirstNumberString(firstString[0] != null && !"null".equals(firstString[0]) ? firstString[0] : "");
        fullString.append(!"null".equals(firstString[0]) ? firstString[0] : "");
        if (firstString[1] != null && !"null".equals(firstString[1])) {
            numberData.setIntNumber(Integer.valueOf(firstString[1]));
            fullString.append(Integer.valueOf(firstString[1]));
        } else {
            numberData.setIntNumber(null);
        }
        numberData.setLastNumberString(firstString[2] != null && !"null".equals(firstString[2]) ? firstString[2] : "");
        fullString.append(!"null".equals(firstString[2]) ? firstString[2] : "");
        numberData.setNumberString(fullString.toString());
        numberData.setSavedNumberFormula(savingNumberFormat);
        return numberData;
    }

    private String refactorLessTen(int i) {
        String s = String.valueOf(i);
        if (i < 10) {
            s = "0" + i;
        }
        return s;
    }

    private Date convertDate(String s, Date startDate) {
        if (startDate != null) {
            return startDate;
        }
        return new Date();  //To change body of created methods use File | Settings | File Templates.
    }

    public boolean isUniqueNumber(String numberingFormat, String numberingFormatString, String uniqueForProject) {
        if (numberingFormat.contains(numberingFormatString)) {
            String[] firstString = numberingFormat.split("/");
            for (String value : firstString) {
                String[] split = value.split(":");
                if (split[0].equals(uniqueForProject)) {
                    return Boolean.valueOf(split[1]);
                }
            }
        }
        return false;
    }

    public static NumberData getDefaultDataForTraining(Integer number, String defPrefix) {
        NumberData numberData = new NumberData();
        numberData.setIntNumber(number);

        String numbering = null;
        if (number != null) {
            numbering = decimalFormat.format(numberData.getIntNumber());
        }

        if (defPrefix != null && !defPrefix.isEmpty()) {
            numberData.setNumberFormat(defPrefix + "_" + numbering);
            numberData.setNumberString(defPrefix + numbering);
        } else {
            numberData.setNumberFormat(DEF_TASK_PREFIX + "_" + numbering);
            numberData.setNumberString(DEF_TASK_PREFIX + numbering);
        }

        return numberData;
    }

    public Boolean isWaitingForApproval() {
        return waitingForApproval == null ? Boolean.FALSE : waitingForApproval;
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

    public String getDelimetrDepartmentNumbering() {
        return delimetrDepartmentNumbering;
    }

    public void setDelimetrDepartmentNumbering(String delimetrDepartmentNumbering) {
        this.delimetrDepartmentNumbering = delimetrDepartmentNumbering;
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

    public String getShiftNumberingFormat() {
        return shiftNumberingFormat;
    }

    public void setShiftNumberingFormat(String shiftNumberingFormat) {
        this.shiftNumberingFormat = shiftNumberingFormat;
    }

    public String getDelimetrShiftNumberingFormat() {
        return delimetrShiftNumberingFormat;
    }

    public void setDelimetrShiftNumberingFormat(String delimetrShiftNumberingFormat) {
        this.delimetrShiftNumberingFormat = delimetrShiftNumberingFormat;
    }

    public String getRotationNumberingDate() {
        return rotationNumberingDate;
    }

    public void setRotationNumberingDate(String rotationNumberingDate) {
        this.rotationNumberingDate = rotationNumberingDate;
    }

    public String getDelimetrRotationNumberingFormat() {
        return delimetrRotationNumberingFormat;
    }

    public void setDelimetrRotationNumberingFormat(String delimetrRotationNumberingFormat) {
        this.delimetrRotationNumberingFormat = delimetrRotationNumberingFormat;
    }

    public String getDelimetrAssemblyNumberingFormat() {
        return delimetrAssemblyNumberingFormat;
    }

    public void setDelimetrAssemblyNumberingFormat(String delimetrAssemblyNumberingFormat) {
        this.delimetrAssemblyNumberingFormat = delimetrAssemblyNumberingFormat;
    }

    public String getAssemblyNumberingDate() {
        return assemblyNumberingDate;
    }

    public void setAssemblyNumberingDate(String assemblyNumberingDate) {
        this.assemblyNumberingDate = assemblyNumberingDate;
    }

    public Boolean getAutomatic() {
        return automatic;
    }

    public String getBrigadaNumberingFormat() {
        return brigadaNumberingFormat;
    }

    public void setBrigadaNumberingFormat(String brigadaNumberingFormat) {
        this.brigadaNumberingFormat = brigadaNumberingFormat;
    }

    public String getDelimetrBrigadaNumberingFormat() {
        return delimetrBrigadaNumberingFormat;
    }

    public void setDelimetrBrigadaNumberingFormat(String delimetrBrigadaNumberingFormat) {
        this.delimetrBrigadaNumberingFormat = delimetrBrigadaNumberingFormat;
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

    public String getGroupPlacementNumberingFormat() {
        return groupPlacementNumberingFormat;
    }

    public void setGroupPlacementNumberingFormat(String groupPlacementNumberingFormat) {
        this.groupPlacementNumberingFormat = groupPlacementNumberingFormat;
    }

    public String getDelimetrGroupPlacementNumberingFormat() {
        return delimetrGroupPlacementNumberingFormat;
    }

    public void setDelimetrGroupPlacementNumberingFormat(String delimetrGroupPlacementNumberingFormat) {
        this.delimetrGroupPlacementNumberingFormat = delimetrGroupPlacementNumberingFormat;
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

    public String getOvertimeNumberingFormat() {
        return overtimeNumberingFormat;
    }

    public void setOvertimeNumberingFormat(String overtimeNumberingFormat) {
        this.overtimeNumberingFormat = overtimeNumberingFormat;
    }

    public String getDelimetrOvertimeNumbering() {
        return delimetrOvertimeNumbering;
    }

    public void setDelimetrOvertimeNumbering(String delimetrOvertimeNumbering) {
        this.delimetrOvertimeNumbering = delimetrOvertimeNumbering;
    }

    public String getBackupsEmployeeNumberingFormat() {
        return backupsEmployeeNumberingFormat;
    }

    public void setBackupsEmployeeNumberingFormat(String backupsEmployeeNumberingFormat) {
        this.backupsEmployeeNumberingFormat = backupsEmployeeNumberingFormat;
    }

    public String getDelimetrBackupsEmployeeNumberingFormat() {
        return delimetrBackupsEmployeeNumberingFormat;
    }

    public void setDelimetrBackupsEmployeeNumberingFormat(String delimetrBackupsEmployeeNumberingFormat) {
        this.delimetrBackupsEmployeeNumberingFormat = delimetrBackupsEmployeeNumberingFormat;
    }
}
