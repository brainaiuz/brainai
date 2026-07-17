package com.edatasite.workforce.gwt.availability.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.BackupEmployeeItem;
import com.edatasite.workforce.gwt.core.client.rpc.leaveRequest.LaborPeriodRequest;
import com.edatasite.workforce.gwt.core.client.ui.laborPeriod.MultiLeaveDTO;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class NewLeaveRequest extends HasApprovers implements IsSerializable {

    private Integer employee;
    private Integer objectID;
    private String employeeName;
    private Date startDate;
    private Date employeeStartDate;
    private Date endDate;
    private DateNonConvertable startNonConverable;
    private DateNonConvertable endNonConverable;
    private Integer reasonId;
    private String description;
    private int startHour, startMinut;
    private int endHour, endMinut;
    private String type;
    private Boolean typeBoolean;
    private String date;
    private String day;
    private String dayE;
    private String monthE;
    private String month;
    private String dateE;
    private NumberData numberData;
    private String leaveRequestCode;

    private TimeSlot timeSlot;
    private SelectItem[] reasons;
    private Integer year;
    private Boolean valid;
    private Boolean takeByMoney;
    private FileItem[] attachments;
    private ArrayList<Integer> employeeIds;
    private String policy;
    private String from = null;
    private boolean selfApprover;
    private boolean fromApi;
    private ArrayList<CompanyCustomFieldItem> customFields;
    private boolean allDay;
    private String errorMessage = "";
    private String statusCode;
    private Integer creatorId;
    private ArrayList<BackupEmployeeItem> backupEmployee = new ArrayList<>();
    private ArrayList<LaborPeriodRequest> periodList;
    private HashMap<String, ArrayList<MultiLeaveDTO>> multiLeaveValues = new HashMap<>();
    private MultiLeaveDTO multiLeaveDTO;
    private boolean recalculate;
    private Double usedExperienceDays;

    public ArrayList<LaborPeriodRequest> getPeriodList() {
        return periodList;
    }

    public void setPeriodList(ArrayList<LaborPeriodRequest> periodList) {
        this.periodList = periodList;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDayE() {
        return dayE;
    }

    public void setDayE(String dayE) {
        this.dayE = dayE;
    }

    public String getMonthE() {
        return monthE;
    }

    public void setMonthE(String monthe) {
        this.monthE = monthe;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateE() {
        return dateE;
    }

    public void setDateE(String dateE) {
        this.dateE = dateE;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean isTypeBoolean() {
        return typeBoolean;
    }

    public void setTypeBoolean(Boolean typeBoolean) {
        this.typeBoolean = typeBoolean;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public DateNonConvertable getStartNonConverable() {
        return startNonConverable;
    }

    public void setStartNonConverable(DateNonConvertable startNonConverable) {
        this.startNonConverable = startNonConverable;
    }

    public DateNonConvertable getEndNonConverable() {
        return endNonConverable;
    }

    public void setEndNonConverable(DateNonConvertable endNonConverable) {
        this.endNonConverable = endNonConverable;
    }

    public Integer getReasonId() {
        return reasonId;
    }

    public void setReasonId(Integer reasonId) {
        this.reasonId = reasonId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMinut() {
        return startMinut;
    }

    public void setStartMinut(int startMinut) {
        this.startMinut = startMinut;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinut() {
        return endMinut;
    }

    public void setEndMinut(int endMinut) {
        this.endMinut = endMinut;
    }

    public Integer getEmployee() {
        return employee;
    }

    public void setEmployee(Integer employee) {
        this.employee = employee;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public SelectItem[] getReasons() {
        return reasons;
    }

    public void setReasons(SelectItem[] reasons) {
        this.reasons = reasons;
    }

    public ArrayList<BackupEmployeeItem> getBackupEmployee() {
        return backupEmployee;
    }

    public void setBackupEmployee(ArrayList<BackupEmployeeItem> backupEmployees) {
        this.backupEmployee = backupEmployees;
    }


    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Boolean isTakeByMoney() {
        return takeByMoney;
    }

    public void setTakeByMoney(Boolean takeByMoney) {
        this.takeByMoney = takeByMoney;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public ArrayList<Integer> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(ArrayList<Integer> employeeIds) {
        this.employeeIds = employeeIds;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(ArrayList<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
    }

    public boolean isSelfApprover() {
        return selfApprover;
    }

    public void setSelfApprover(boolean selfApprover) {
        this.selfApprover = selfApprover;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public String getLeaveRequestCode() {
        return leaveRequestCode;
    }

    public void setLeaveRequestCode(String code) {
        this.leaveRequestCode = code;
    }

    public Date getEmployeeStartDate() {
        return employeeStartDate;
    }

    public void setEmployeeStartDate(Date employeeStartDate) {
        this.employeeStartDate = employeeStartDate;
    }

    public HashMap<String, ArrayList<MultiLeaveDTO>> getMultiLeaveValues() {
        return multiLeaveValues;
    }

    public void setMultiLeaveValues(HashMap<String, ArrayList<MultiLeaveDTO>> multiLeaveValues) {
        this.multiLeaveValues = multiLeaveValues;
    }

    public MultiLeaveDTO getMultiLeaveDTO() {
        return multiLeaveDTO;
    }

    public void setMultiLeaveDTO(MultiLeaveDTO multiLeaveDTO) {
        this.multiLeaveDTO = multiLeaveDTO;
    }

    public boolean isFromApi() {
        return fromApi;
    }

    public void setFromApi(boolean fromApi) {
        this.fromApi = fromApi;
    }

    public boolean isRecalculate() {
        return recalculate;
    }

    public void setRecalculate(boolean recalculate) {
        this.recalculate = recalculate;
    }

    public Double getUsedExperienceDays() {
        return usedExperienceDays;
    }

    public void setUsedExperienceDays(Double usedExperienceDays) {
        this.usedExperienceDays = usedExperienceDays;
    }
}
