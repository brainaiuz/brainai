package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;

public class TimeslotItem implements IsSerializable {

    public static final String ACTION = "action";
    public static final String NAME = "Name";
    public static final String DESCRIPTION = "Description";
    public static final String SHORT_NAME = "Short Name";
    public static final String DEPARTMENTS = "Departments";

    private Integer objectID;
    private String name;
    private String shortName;
    private ReferenceLocale localeItem;
    private ReferenceLocale referenceLocale;
    private String hexColor;
    private String description;
    private Departments[] departments;
    private SelectItem[] reasons;
    private int[] monday;
    private int[] lunchMo;
    private int[] coffeeMo;
    private int[] tuesday;
    private int[] lunchTu;
    private int[] coffeeTu;
    private int[] wednesday;
    private int[] lunchWe;
    private int[] coffeeWe;
    private int[] thursday;
    private int[] lunchTh;
    private int[] coffeeTh;
    private int[] friday;
    private int[] lunchFr;
    private int[] coffeeFr;
    private int[] saturday;
    private int[] lunchSa;
    private int[] coffeeSa;
    private int[] sunday;
    private int[] lunchSu;
    private int[] coffeeSu;
    private boolean isDefaultTimeSlot = false;
    private boolean isShift;

    private String departmentsAsString;

    private int[] weekDaysPlannedTime;
    private int[] actualWeekDaysPlannedTime;
    private int monthlyPlannedTime;
    private Integer[] selectedEmployeeIds;
    private boolean[] isHoliday;
    private boolean[] isLR;

    private ArrayList<ExceptionalTimeSlotItem> exceptionalCases;

    private ArrayList<TimeslotEmployeeItem> timeslotEmployeeItems;
    private ArrayList<ExceptionalTimeSlotItem> dailyItems;
    private Date effectiveDate;
    private Date endDate;
    private Integer weekStart;
    private ArrayList<Integer> selectedItemIds;
    private ArrayList<SelectItem> selectedLeaveReasons;
    private Integer lateMinutes;
    private Integer earlyMinutes;
    private Integer validInStart;
    private Integer validInEnd;
    private Integer validOutStart;
    private Integer validOutEnd;
    private Integer shiftStartTime;
    private Integer shiftEndTime;
    private boolean autoInOutEnabled;

    public String getDepartmentsAsString() {
        return departmentsAsString;
    }

    public void setDepartmentsAsString(Departments[] departments) {
        StringBuilder departmentsAsString = new StringBuilder();
        String pre = "";
        for (Departments department : departments) {
            if (!"".equals(department.getDeptName())) {
                departmentsAsString.append(pre).append(department.getDeptName());
                pre = ", ";
            }
        }
        this.departmentsAsString = departmentsAsString.toString();
    }

    public int[] getLunchMo() {
        return lunchMo;
    }

    public void setLunchMo(int[] lunchMo) {
        this.lunchMo = lunchMo;
    }

    public int[] getCoffeeMo() {
        return coffeeMo;
    }

    public void setCoffeeMo(int[] coffeeMo) {
        this.coffeeMo = coffeeMo;
    }

    public int[] getLunchTu() {
        return lunchTu;
    }

    public void setLunchTu(int[] lunchTu) {
        this.lunchTu = lunchTu;
    }

    public int[] getCoffeeTu() {
        return coffeeTu;
    }

    public void setCoffeeTu(int[] coffeeTu) {
        this.coffeeTu = coffeeTu;
    }

    public int[] getLunchWe() {
        return lunchWe;
    }

    public void setLunchWe(int[] lunchWe) {
        this.lunchWe = lunchWe;
    }

    public int[] getCoffeeWe() {
        return coffeeWe;
    }

    public void setCoffeeWe(int[] coffeeWe) {
        this.coffeeWe = coffeeWe;
    }

    public int[] getLunchTh() {
        return lunchTh;
    }

    public void setLunchTh(int[] lunchTh) {
        this.lunchTh = lunchTh;
    }

    public int[] getCoffeeTh() {
        return coffeeTh;
    }

    public void setCoffeeTh(int[] coffeeTh) {
        this.coffeeTh = coffeeTh;
    }

    public int[] getLunchFr() {
        return lunchFr;
    }

    public void setLunchFr(int[] lunchFr) {
        this.lunchFr = lunchFr;
    }

    public int[] getCoffeeFr() {
        return coffeeFr;
    }

    public void setCoffeeFr(int[] coffeeFr) {
        this.coffeeFr = coffeeFr;
    }

    public int[] getCoffeeSa() {
        return coffeeSa;
    }

    public void setCoffeeSa(int[] coffeeSa) {
        this.coffeeSa = coffeeSa;
    }

    public int[] getLunchSu() {
        return lunchSu;
    }

    public void setLunchSu(int[] lunchSu) {
        this.lunchSu = lunchSu;
    }

    public int[] getLunchSa() {
        return lunchSa;
    }

    public void setLunchSa(int[] lunchSa) {
        this.lunchSa = lunchSa;
    }

    public int[] getCoffeeSu() {
        return coffeeSu;
    }

    public void setCoffeeSu(int[] coffeeSu) {
        this.coffeeSu = coffeeSu;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int[] getMonday() {
        return monday;
    }

    public void setMonday(int[] monday) {
        this.monday = monday;
    }

    public int[] getTuesday() {
        return tuesday;
    }

    public void setTuesday(int[] tuesday) {
        this.tuesday = tuesday;
    }

    public int[] getWednesday() {
        return wednesday;
    }

    public void setWednesday(int[] wednesday) {
        this.wednesday = wednesday;
    }

    public int[] getThursday() {
        return thursday;
    }

    public void setThursday(int[] thursday) {
        this.thursday = thursday;
    }

    public int[] getFriday() {
        return friday;
    }

    public void setFriday(int[] friday) {
        this.friday = friday;
    }

    public int[] getSaturday() {
        return saturday;
    }

    public void setSaturday(int[] saturday) {
        this.saturday = saturday;
    }

    public int[] getSunday() {
        return sunday;
    }

    public void setSunday(int[] sunday) {
        this.sunday = sunday;
    }

    public int[] getWeekDaysPlannedTime() {
        return weekDaysPlannedTime;
    }

    public void setWeekDaysPlannedTime(int[] weekDaysPlannedTime) {
        this.weekDaysPlannedTime = weekDaysPlannedTime;
    }

    public int[] getActualWeekDaysPlannedTime() {
        return actualWeekDaysPlannedTime;
    }

    public void setActualWeekDaysPlannedTime(int[] actualWeekDaysPlannedTime) {
        this.actualWeekDaysPlannedTime = actualWeekDaysPlannedTime;
    }

    public int getMonthlyPlannedTime() {
        return monthlyPlannedTime;
    }

    public void setMonthlyPlannedTime(int monthlyPlannedTime) {
        this.monthlyPlannedTime = monthlyPlannedTime;
    }

    public Departments[] getDepartments() {
        return departments;
    }

    public void setDepartments(Departments[] departments) {
        this.departments = departments;
    }

    public boolean isDefaultTimeSlot() {
        return isDefaultTimeSlot;
    }

    public void setDefaultTimeSlot(boolean defaultTimeSlot) {
        isDefaultTimeSlot = defaultTimeSlot;
    }

    public Integer[] getSelectedEmployeeIds() {
        return selectedEmployeeIds;
    }

    public void setSelectedEmployees(Integer[] selectedEmployeeIds) {
        this.selectedEmployeeIds = selectedEmployeeIds;
    }

    public int[] getDayOfWeek(int day) {
        int[] d = new int[0];
        switch (day) {
            case 0:
                d = sunday;
                break;
            case 1:
                d = monday;
                break;
            case 2:
                d = tuesday;
                break;
            case 3:
                d = wednesday;
                break;
            case 4:
                d = thursday;
                break;
            case 5:
                d = friday;
                break;
            case 6:
                return saturday;

        }
        return d;
    }

    public boolean[] getHoliday() {
        return isHoliday;
    }

    public void setHoliday(boolean[] holiday) {
        isHoliday = holiday;
    }

    public boolean[] getLR() {
        return isLR;
    }

    public void setLR(boolean[] LR) {
        isLR = LR;
    }

    public ArrayList<ExceptionalTimeSlotItem> getExceptionalCases() {
        return exceptionalCases;
    }

    public void setExceptionalCases(ArrayList<ExceptionalTimeSlotItem> exceptionalCases) {
        this.exceptionalCases = exceptionalCases;
    }

    public ArrayList<TimeslotEmployeeItem> getTimeslotEmployeeItems() {
        return timeslotEmployeeItems;
    }

    public void setTimeslotEmployeeItems(ArrayList<TimeslotEmployeeItem> timeslotEmployeeItems) {
        this.timeslotEmployeeItems = timeslotEmployeeItems;
    }

    public void setDailyItems(ArrayList<ExceptionalTimeSlotItem> dailyItems) {
        this.dailyItems = dailyItems;
    }

    public ArrayList<ExceptionalTimeSlotItem> getDailyItems() {
        return dailyItems;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public Integer getWeekStart() {
        return weekStart;
    }

    public void setWeekStart(Integer weekStart) {
        this.weekStart = weekStart;
    }

    public ArrayList<Integer> getAdditionalLeaveDays() {
        return selectedItemIds;
    }

    public void setAdditionalLeaveDays(ArrayList<Integer> additionalLeaveDays) {
        this.selectedItemIds = additionalLeaveDays;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public ReferenceLocale getLocaleItem() {
        return localeItem;
    }

    public void setLocaleItem(ReferenceLocale localeItem) {
        this.localeItem = localeItem;
    }

    public ReferenceLocale getReferenceLocale() {
        return referenceLocale;
    }

    public void setReferenceLocale(ReferenceLocale referenceLocale) {
        this.referenceLocale = referenceLocale;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public boolean isShift() {
        return isShift;
    }

    public void setShift(boolean shift) {
        isShift = shift;
    }

    public ArrayList<SelectItem> getSelectedLeaveReasons() {
        return selectedLeaveReasons;
    }

    public void setSelectedLeaveReasons(ArrayList<SelectItem> selectedLeaveReasons) {
        this.selectedLeaveReasons = selectedLeaveReasons;
    }

    public SelectItem[] getReasons() {
        return reasons;
    }

    public void setReasons(SelectItem[] reasons) {
        this.reasons = reasons;
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

    public Integer getValidInStart() {
        return validInStart;
    }

    public void setValidInStart(Integer validInStart) {
        this.validInStart = validInStart;
    }

    public Integer getValidInEnd() {
        return validInEnd;
    }

    public void setValidInEnd(Integer validInEnd) {
        this.validInEnd = validInEnd;
    }

    public Integer getValidOutStart() {
        return validOutStart;
    }

    public void setValidOutStart(Integer validOutStart) {
        this.validOutStart = validOutStart;
    }

    public Integer getValidOutEnd() {
        return validOutEnd;
    }

    public void setValidOutEnd(Integer validOutEnd) {
        this.validOutEnd = validOutEnd;
    }

    public Integer getShiftStartTime() {
        return shiftStartTime;
    }

    public void setShiftStartTime(Integer shiftStartTime) {
        this.shiftStartTime = shiftStartTime;
    }

    public Integer getShiftEndTime() {
        return shiftEndTime;
    }

    public void setShiftEndTime(Integer shiftEndTime) {
        this.shiftEndTime = shiftEndTime;
    }

    public boolean isAutoInOutEnabled() {
        return autoInOutEnabled;
    }

    public void setAutoInOutEnabled(boolean autoInOutEnabled) {
        this.autoInOutEnabled = autoInOutEnabled;
    }
}