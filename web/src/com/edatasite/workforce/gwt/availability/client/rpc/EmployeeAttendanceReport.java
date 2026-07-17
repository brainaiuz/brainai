package com.edatasite.workforce.gwt.availability.client.rpc;

import com.edatasite.workforce.gwt.core.client.ReasonItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 09.09.2009
 * Time: 18:16:32
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeAttendanceReport implements IsSerializable {

    private Integer id;
    private String name;
    private int[] totalAbsent;
    private int[] withoutLR;
    private int[] waitingForAppRoval;
    private int[] withLR;
    private int[] monthHoliday;
    private int totalAbsentSum;
    private int withoutLRSum;
    private int waitingForApprovalSum;
    private int withLRSum;
    private int totalInHour;
    private int overTime;
    private HashMap<String, ArrayList<EmployeeReport>> emplReports;
    private Integer locationId;
    private Integer pdfTemplateID;
    private HashMap<String, ReasonItem> leaveTypes = new HashMap<>();
    private Integer totalCount;
    private SelectItem[] reasons;
    private boolean holidayIncluded;
    private EmployeeReport[] employeeReports;
    Map<Integer, Map<Integer, FingerprintTimeDto>> fingerprintTimeDtoMap;
    HashMap<DateNonConvertable, Integer> monthHolidaysByPeriod;

    public EmployeeAttendanceReport() {
    }

    public EmployeeAttendanceReport(Integer id, Integer locationId, String name) {
        this.id = id;
        this.locationId = locationId;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getTotalAbsent() {
        return totalAbsent;
    }

    public void setTotalAbsent(int[] totalAbsent) {
        this.totalAbsent = totalAbsent;
    }

    public int[] getWithoutLR() {
        return withoutLR;
    }

    public void setWithoutLR(int[] withoutLR) {
        this.withoutLR = withoutLR;
    }

    public int[] getWaitingForAppRoval() {
        return waitingForAppRoval;
    }

    public void setWaitingForAppRoval(int[] waitingForAppRoval) {
        this.waitingForAppRoval = waitingForAppRoval;
    }

    public int[] getWithLR() {
        return withLR;
    }

    public void setWithLR(int[] withLR) {
        this.withLR = withLR;
    }

    public int getTotalAbsentSum() {
        return totalAbsentSum;
    }

    public void setTotalAbsentSum(int totalAbsentSum) {
        this.totalAbsentSum = totalAbsentSum;
    }

    public int getWithoutLRSum() {
        return withoutLRSum;
    }

    public void setWithoutLRSum(int withoutLRSum) {
        this.withoutLRSum = withoutLRSum;
    }

    public int getWaitingForApprovalSum() {
        return waitingForApprovalSum;
    }

    public void setWaitingForApprovalSum(int waitingForApprovalSum) {
        this.waitingForApprovalSum = waitingForApprovalSum;
    }

    public int getWithLRSum() {
        return withLRSum;
    }

    public void setWithLRSum(int withLRSum) {
        this.withLRSum = withLRSum;
    }

    public int getTotalInHour() {
        return totalInHour;
    }

    public void setTotalInHour(int totalInHour) {
        this.totalInHour = totalInHour;
    }

    public int getOverTime() {
        return overTime;
    }

    public void setOverTime(int overTime) {
        this.overTime = overTime;
    }

    public HashMap<String, ArrayList<EmployeeReport>> getEmplReports() {
        return emplReports;
    }

    public void setEmplReports(HashMap<String, ArrayList<EmployeeReport>> emplReports) {
        this.emplReports = emplReports;
    }

    public int[] getMonthHoliday() {
        return monthHoliday;
    }

    public void setMonthHoliday(int[] monthHoliday) {
        this.monthHoliday = monthHoliday;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getPdfTemplateID() {
        return pdfTemplateID;
    }

    public void setPdfTemplateID(Integer pdfTemplateID) {
        this.pdfTemplateID = pdfTemplateID;
    }

    public HashMap<String, ReasonItem> getLeaveTypes() {
        return leaveTypes;
    }

    public void setLeaveTypes(HashMap<String, ReasonItem> leaveTypes) {
        this.leaveTypes = leaveTypes;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public SelectItem[] getReasons() {
        return reasons;
    }

    public void setReasons(SelectItem[] reasons) {
        this.reasons = reasons;
    }

    public boolean isHolidayIncluded() {
        return holidayIncluded;
    }

    public void setHolidayIncluded(boolean holidayIncluded) {
        this.holidayIncluded = holidayIncluded;
    }

    public EmployeeReport[] getEmployeeReports() {
        return employeeReports;
    }

    public void setEmployeeReports(EmployeeReport[] employeeReports) {
        this.employeeReports = employeeReports;
    }

    public Map<Integer, Map<Integer, FingerprintTimeDto>> getFingerprintTimeDtoMap() {
        return fingerprintTimeDtoMap;
    }

    public void setFingerprintTimeDtoMap(Map<Integer, Map<Integer, FingerprintTimeDto>> fingerprintTimeDtoMap) {
        this.fingerprintTimeDtoMap = fingerprintTimeDtoMap;
    }

    public HashMap<DateNonConvertable, Integer> getMonthHolidaysByPeriod() {
        return monthHolidaysByPeriod;
    }

    public void setMonthHolidaysByPeriod(HashMap<DateNonConvertable, Integer> monthHolidaysByPeriod) {
        this.monthHolidaysByPeriod = monthHolidaysByPeriod;
    }
}
