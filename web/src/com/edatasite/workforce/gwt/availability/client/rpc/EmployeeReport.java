package com.edatasite.workforce.gwt.availability.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TimeslotItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 09.09.2009
 * Time: 18:23:08
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeReport implements IsSerializable {
    private Integer id;
    private String name;
    private String code;
    private int[] al;
    private int[] timeslotOverallMins;
    private int[] timeslotStartMins;
    private int[] timeslotEndMins;
    private int[] timeslotLunchMins;
    private Date[] inHours;
    private Date[] outHours;
    private Date[] actualHours;
    private Integer[] inOutHour;
    private int[] overtimeHour;
    private String[] timeSlotId;
    private int[] shiftStartTime;
    private int[] shiftEndTime;
    private String[] shiftColor;
    private String[] shiftShortName;
    private Integer[] hourlyLRs;
    private int inhour;
    private long plannedHours;
    private int overtime;
    private int totalmonth;
    private int withoutLR;
    private int withLR;
    private int[] withHoliday;
    private int waitingForApproval;
    private int[] leaveRequestHolidays;
    private String[] leaveCodes;
    private LinkedHashMap<Integer, Integer> idsOfLeaveRequests = new LinkedHashMap<>();
    private int[] monthHoliday;
    private String status;
    private String position;
    private String positionType;
    private String positionUzbek;
    private Date resignationDay;
    private String departmentOrPosition;
    private BigDecimal salary;
    private int plannedDays;
    private int workedDays;
    private int overtimeDays;

    private double overtimeHours;
    private double dayOffHours;
    private String photoUrl;
    private Integer departmentId;
    private TimeslotItem timeslotItem;
    private String unit;
    private SelectItem team;
    private SelectItem employeeName;
    private boolean hasShift;
    private Integer leaveCount;
    private List<StatisticsLeaveRequest> leave;
    private Set<Integer> plannedDaysSet;
    private Set<Integer> workedDaysSet;
    private Map<Date, Integer> exceptionalTimeSlotDates;
    private Map<Integer, int[]> timeSlotItems;
    HashMap<String, Integer> monthHolidaysByPeriod;
    private Double[] lateEarlyPercent;
    private Integer lateMinutes;
    private Integer earlyMinutes;

    public EmployeeReport() {
    }

    public EmployeeReport(Integer id, String name, int maxMonthDay, LinkedHashMap<Integer, Integer> idsOfLeaveRequets, String code, String position, String departmentOrPosition, boolean hasShift) {
        this.id = id;
        this.name = name;
        this.idsOfLeaveRequests = idsOfLeaveRequets;
        this.inOutHour = new Integer[maxMonthDay];
        this.timeSlotId = new String[maxMonthDay];
        this.code = code;
        this.position = position;
        this.departmentOrPosition = departmentOrPosition;
        this.hasShift = hasShift;
    }

    public EmployeeReport(Integer id, String name, int maxMonthDay, LinkedHashMap<Integer, Integer> idsOfLeaveRequets, String code, String position, String positionType, String positionUzbek, String departmentOrPosition, boolean hasShift) {
        this.id = id;
        this.name = name;
        this.idsOfLeaveRequests = idsOfLeaveRequets;
        this.inOutHour = new Integer[maxMonthDay];
        this.timeSlotId = new String[maxMonthDay];
        this.code = code;
        this.position = position;
        this.positionType = positionType;
        this.positionUzbek = positionUzbek;
        this.departmentOrPosition = departmentOrPosition;
        this.hasShift = hasShift;
        this.overtimeHour = new int[maxMonthDay];
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

    public int[] getAl() {
        return al;
    }

    public void setAl(int[] al) {
        this.al = al;
    }

    public int[] getTimeslotOverallMins() {
        return timeslotOverallMins;
    }

    public void setTimeslotOverallMins(int[] timeslotOverallMins) {
        this.timeslotOverallMins = timeslotOverallMins;
    }

    public int[] getTimeslotEndMins() {
        return timeslotEndMins;
    }

    public void setTimeslotEndMins(int[] timeslotEndMins) {
        this.timeslotEndMins = timeslotEndMins;
    }

    public int[] getTimeslotStartMins() {
        return timeslotStartMins;
    }

    public void setTimeslotStartMins(int[] timeslotStartMins) {
        this.timeslotStartMins = timeslotStartMins;
    }

    public int[] getTimeslotLunchMins() {
        return timeslotLunchMins;
    }

    public void setTimeslotLunchMins(int[] timeslotLunchMins) {
        this.timeslotLunchMins = timeslotLunchMins;
    }

    public int getInhour() {
        return inhour;
    }

    public void setInhour(int inhour) {
        this.inhour = inhour;
    }

    public int getOvertime() {
        return overtime;
    }

    public void setOvertime(int overtime) {
        this.overtime = overtime;
    }

    public int getTotalmonth() {
        return totalmonth;
    }

    public void setTotalmonth(int totalmonth) {
        this.totalmonth = totalmonth;
    }

    public int getWithoutLR() {
        return withoutLR;
    }

    public void setWithoutLR(int withoutLR) {
        this.withoutLR = withoutLR;
    }

    public int getWithLR() {
        return withLR;
    }

    public void setWithLR(int withLR) {
        this.withLR = withLR;
    }

    public Integer[] getInOutHour() {
        return inOutHour;
    }

    public void setInOutHour(Integer[] inOutHour) {
        this.inOutHour = inOutHour;
    }

    public int[] getWithHoliday() {
        return withHoliday;
    }

    public void setWithHoliday(int[] withHoliday) {
        this.withHoliday = withHoliday;
    }

    public int getWaitingForApproval() {
        return waitingForApproval;
    }

    public void setWaitingForApproval(int waitingForApproval) {
        this.waitingForApproval = waitingForApproval;
    }

    public int[] getLeaveRequestHolidays() {
        return leaveRequestHolidays;
    }

    public void setLeaveRequestHolidays(int[] leaveRequestHolidays) {
        this.leaveRequestHolidays = leaveRequestHolidays;
    }

    public String[] getLeaveCodes() {
        return leaveCodes;
    }

    public void setLeaveCodes(String[] leaveCodes) {
        this.leaveCodes = leaveCodes;
    }

    public LinkedHashMap<Integer, Integer> getIdsOfLeaveRequests() {
        return this.idsOfLeaveRequests;
    }

    public void setIdsOfLeaveRequests(LinkedHashMap<Integer, Integer> idsOfLeaveRequests) {
        this.idsOfLeaveRequests = idsOfLeaveRequests;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int[] getMonthHoliday() {
        return monthHoliday;
    }

    public void setMonthHoliday(int[] monthHoliday) {
        this.monthHoliday = monthHoliday;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    public Date getResignationDay() {
        return this.resignationDay;
    }

    public void setResignationDay(Date resignationDay) {
        this.resignationDay = resignationDay;
    }

    public String getDepartmentOrPosition() {
        return departmentOrPosition;
    }

    public void setDepartmentOrPosition(String departmentOrPosition) {
        this.departmentOrPosition = departmentOrPosition;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public long getPlannedHours() {
        return plannedHours;
    }

    public void setPlannedHours(long plannedHours) {
        this.plannedHours = plannedHours;
    }

    public int getPlannedDays() {
        return plannedDays;
    }

    public void setPlannedDays(int plannedDays) {
        this.plannedDays = plannedDays;
    }

    public int getWorkedDays() {
        return workedDays;
    }

    public void setWorkedDays(int workedDays) {
        this.workedDays = workedDays;
    }

    public double getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public double getDayOffHours() {
        return dayOffHours;
    }

    public void setDayOffHours(double dayOffHours) {
        this.dayOffHours = dayOffHours;
    }

    public int getOvertimeDays() {
        return overtimeDays;
    }

    public void setOvertimeDays(int overtimeDays) {
        this.overtimeDays = overtimeDays;
    }

    public String getPhotoUrl() {
        return this.photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer[] getHourlyLRs() {
        return hourlyLRs;
    }

    public void setHourlyLRs(Integer[] hourlyLRs) {
        this.hourlyLRs = hourlyLRs;
    }

    public TimeslotItem getTimeslotItem() {
        return timeslotItem;
    }

    public void setTimeslotItem(TimeslotItem timeslotItem) {
        this.timeslotItem = timeslotItem;
    }

    public SelectItem getTeam() {
        return team;
    }

    public void setTeam(SelectItem team) {
        this.team = team;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public SelectItem getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(SelectItem employeeName) {
        this.employeeName = employeeName;
    }

    public String[] getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(String[] timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public boolean isHasShift() {
        return hasShift;
    }

    public void setHasShift(boolean hasShift) {
        this.hasShift = hasShift;
    }

    public String getPositionUzbek() {
        return positionUzbek;
    }

    public void setPositionUzbek(String positionUzbek) {
        this.positionUzbek = positionUzbek;
    }


    public Integer getLeaveCount() {
        return leaveCount;
    }

    public void setLeaveCount(Integer leaveCount) {
        this.leaveCount = leaveCount;
    }


    public List<StatisticsLeaveRequest> getLeave() {
        return leave;
    }

    public void setLeave(List<StatisticsLeaveRequest> leave) {
        this.leave = leave;
    }

    public Set<Integer> getPlannedDaysSet() {
        return plannedDaysSet;
    }

    public void setPlannedDaysSet(Set<Integer> plannedDaysSet) {
        this.plannedDaysSet = plannedDaysSet;
    }

    public Set<Integer> getWorkedDaysSet() {
        return workedDaysSet;
    }

    public void setWorkedDaysSet(Set<Integer> workedDaysSet) {
        this.workedDaysSet = workedDaysSet;
    }

    public void setExceptionalTimeSlotDates(Map<Date, Integer> exceptionalTimeSlotDates) {
        this.exceptionalTimeSlotDates = exceptionalTimeSlotDates;
    }

    public Map<Date, Integer> getExceptionalTimeSlotDates() {
        return exceptionalTimeSlotDates;
    }

    public int[] getOvertimeHour() {
        return overtimeHour;
    }

    public void setOvertimeHour(int[] overtimeHour) {
        this.overtimeHour = overtimeHour;
    }

    public Date[] getInHours() {
        return inHours;
    }

    public void setInHours(Date[] inHours) {
        this.inHours = inHours;
    }

    public Date[] getOutHours() {
        return outHours;
    }

    public void setOutHours(Date[] outHours) {
        this.outHours = outHours;
    }

    public Date[] getActualHours() {
        return actualHours;
    }

    public void setActualHours(Date[] actualHours) {
        this.actualHours = actualHours;
    }

    public Map<Integer, int[]> getTimeSlotItems() {
        return timeSlotItems;
    }

    public void setTimeSlotItems(Map<Integer, int[]> timeSlotItems) {
        this.timeSlotItems = timeSlotItems;
    }

    public Integer getLateMinutes() {
        return lateMinutes;
    }

    public void setLateMinutes(Integer lateMinutes) {
        this.lateMinutes = lateMinutes;
    }

    public Integer getEarlyMinutes() {
        return earlyMinutes;
    }

    public void setEarlyMinutes(Integer earlyMinutes) {
        this.earlyMinutes = earlyMinutes;
    }

    public HashMap<String, Integer> getMonthHolidaysByPeriod() {
        return monthHolidaysByPeriod;
    }

    public void setMonthHolidaysByPeriod(HashMap<String, Integer> monthHolidaysByPeriod) {
        this.monthHolidaysByPeriod = monthHolidaysByPeriod;
    }

    public Double[] getLateEarlyPercent() {
        return lateEarlyPercent;
    }

    public void setLateEarlyPercent(Double[] lateEarlyPercent) {
        this.lateEarlyPercent = lateEarlyPercent;
    }

    public int[] getShiftStartTime() {
        return shiftStartTime;
    }

    public void setShiftStartTime(int[] shiftStartTime) {
        this.shiftStartTime = shiftStartTime;
    }

    public int[] getShiftEndTime() {
        return shiftEndTime;
    }

    public void setShiftEndTime(int[] shiftEndTime) {
        this.shiftEndTime = shiftEndTime;
    }

    public String[] getShiftColor() {
        return shiftColor;
    }

    public void setShiftColor(String[] shiftColor) {
        this.shiftColor = shiftColor;
    }
}
