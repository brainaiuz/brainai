package com.edatasite.workforce.gwt.core.client.ui.resourceUtil;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.CoreMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.resourceUtil.EmployeeResourceUtilItem;
import com.edatasite.workforce.gwt.core.client.rpc.resourceUtil.ProjectTaskItem;
import com.edatasite.workforce.gwt.core.client.rpc.resourceUtil.TaskItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Ilhombek
 * Date: 5/22/12
 * Time: 2:07 PM
 */
class ResourceUtilReportTableData implements ResourceUtilReportConstants {

    private static final CoreMessages coreMessages = CoreMessages.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private final int currentMonth;
    private final int[] monthHoliday;
    private final Date date;
    private final Map<Element, Set<Element>> parentChildEMP_PROJMap;
    private final Map<Element, Set<Element>> parentChildPROJ_TASKMap;
    private final Map<String, Element> parentEMPLMap;
    private final Map<String, Element> parentPROJMap;

    private final Map<Element, Set<Element>> parentChildEMP_TSLOTHoursMap;
    private final Map<Element, Set<Element>> parentChildEMP_INOUTHoursMap;
    private final Map<Element, Set<Element>> parentChildEMP_OVERALL_TSHETHoursMap;
    private final Map<Element, Set<Element>> parentChildEMP_LRHoursVSHolidayDaysMap;
    private final Map<Element, Set<Element>> parentChildEMP_TSHETHoursMap;

    private final Map<String, Element> empOVER_ALL_HOURS_TD;
    private final Map<String, Element> empTIMESHEET_HOURS_TD;

    private final Map<String, Map<Integer, Element>> empOVER_ALL_HOURS_TDMap;
    private final Map<String, Map<Integer, Element>> empTIMESHEET_TDMap;
    private final Map<String, Map<Integer, Integer>> empOVER_ALL_HOURS_TD_OLD_HOURS_Map;
    private final Map<String, Map<Integer, Integer>> empTIMESHEET_HOURS_TD_OLD_HOURS_Map;
    private final Map<String, Map<Integer, Element>> proOVER_ALL_HOURS_TD_SPANMap;
    private final Map<String, Map<Integer, Integer>> proOVER_ALL_HOURS_TD_SPAN_OLD_HOURS_Map;

    private final Map<String, Map<Element, Element>> totalEmployee_OVER_ALL_HOURS_TD_map;
    private final Map<String, Map<Element, Element>> totalEmployee_TIMESHEET_HOURS_TD_map;
    private final Map<String, Map<Element, Element>> totalProject_OVER_ALL_HOURS_TD_map;

    private final Map<String, Map<Integer, Element>> empProTaskOVER_ALL_HOURS_TDMap;

    private final Map<String, Element> empTIME_SLOT_HOURS_TD;
    private final Map<String, Element> empIN_OUT_HOURS_TD;

    private final ResourceUtilReportTable reportTable;

    ResourceUtilReportTableData(ResourceUtilReportTable reportTable, int currentMonth, Date date, int[] monthHoliday) {
        this.reportTable = reportTable;
        this.currentMonth = currentMonth;
        this.monthHoliday = monthHoliday;
        this.date = date;

        parentChildEMP_PROJMap = new LinkedHashMap<>();
        parentChildPROJ_TASKMap = new LinkedHashMap<>();
        parentEMPLMap = new LinkedHashMap<>();
        parentPROJMap = new LinkedHashMap<>();

        parentChildEMP_TSLOTHoursMap = new LinkedHashMap<>();
        parentChildEMP_INOUTHoursMap = new LinkedHashMap<>();
        parentChildEMP_OVERALL_TSHETHoursMap = new LinkedHashMap<>();
        parentChildEMP_LRHoursVSHolidayDaysMap = new LinkedHashMap<>();
        parentChildEMP_TSHETHoursMap = new LinkedHashMap<>();

        empProTaskOVER_ALL_HOURS_TDMap = new LinkedHashMap<>();
        //
        empOVER_ALL_HOURS_TD = new LinkedHashMap<>();
        empTIMESHEET_HOURS_TD = new LinkedHashMap<>();
        empOVER_ALL_HOURS_TDMap = new LinkedHashMap<>();
        empTIMESHEET_TDMap = new LinkedHashMap<>();
        empOVER_ALL_HOURS_TD_OLD_HOURS_Map = new LinkedHashMap<>();
        empTIMESHEET_HOURS_TD_OLD_HOURS_Map = new LinkedHashMap<>();
        proOVER_ALL_HOURS_TD_SPANMap = new LinkedHashMap<>();
        proOVER_ALL_HOURS_TD_SPAN_OLD_HOURS_Map = new LinkedHashMap<>();

        totalEmployee_OVER_ALL_HOURS_TD_map = new LinkedHashMap<>();
        totalEmployee_TIMESHEET_HOURS_TD_map = new LinkedHashMap<>();
        totalProject_OVER_ALL_HOURS_TD_map = new LinkedHashMap<>();

        empTIME_SLOT_HOURS_TD = new LinkedHashMap<>();
        empIN_OUT_HOURS_TD = new LinkedHashMap<>();
    }

    void generateMonthDays(Element parentTRElement, int currentDay, boolean isTop) {
        DateTimeFormat weekDayFormat = DateTimeFormat.getFormat("E");
        DateTimeFormat dayFormat = DateTimeFormat.getFormat("dd");
        DateTimeFormat monthFormat = DateTimeFormat.getFormat("MMM");


        for (int i = 1; i <= currentMonth; i++) {
            Date tempDate = date;
            tempDate.setDate(i);

            String weekDayText = weekDayFormat.format(tempDate);
            String dayText = dayFormat.format(tempDate);
            String monthText = monthFormat.format(tempDate);

            Element dayTDElement = DOM.createTD();
            String topTitleMessage = "";
            if (currentDay == i && isTop) {
                dayTDElement.addClassName(STYLE_CURRENT_DAY);
                topTitleMessage = wfmStrings.currentDayOnly();
            } else {
                if (monthHoliday[i] == 1) {
                    dayTDElement.addClassName(STYLE_SUNDAY_MONTH);
                    topTitleMessage = wfmStrings.sunday();
                } else {
                    if (monthHoliday[i] == 2) {
                        dayTDElement.addClassName(STYLE_WORK_MONTH_HOLIDAY);
                        topTitleMessage = wfmStrings.dayOff();
                    } else {
                        dayTDElement.addClassName(STYLE_MONTH_DAY);
                    }
                }
            }
            if (isTop) {
                String formattedDate = "<strong>" + monthText + "-" + dayText + "</strong>" +
                        "<br><small>" + weekDayText + "</small>";
                dayTDElement.setInnerHTML(formattedDate);
                dayTDElement.setTitle(topTitleMessage);
            }
            reportTable.addChildToParent(parentTRElement, dayTDElement);
        }
        //total data element
        Element totalDayTDElement = DOM.createTD();
        totalDayTDElement.addClassName(STYLE_TOTAL_DAY);
        if (isTop) {
            totalDayTDElement.setInnerHTML(wfmStrings.total());
        }
        reportTable.addChildToParent(parentTRElement, totalDayTDElement);
    }

    /**
     * Generate employee name REPORT
     *
     * @param parentElementTBody - parent tBody element
     * @param employeeRUItem     - employee item
     */
    void generateEmployeeReport(Element parentElementTBody, EmployeeResourceUtilItem employeeRUItem, boolean enableExpand) {
        //register employee name TR element
        Element employeeNameElementTR = DOM.createTR();
        String key_employee_id_S = employeeRUItem.getEmployee_id() + "";
        parentEMPLMap.put(key_employee_id_S, employeeNameElementTR);
        employeeNameElementTR.addClassName(CLASS_EMPLOYEE_NAME_TR);
        reportTable.addChildToParent(parentElementTBody, employeeNameElementTR);
        //register employee name TH element
        Element employeeNameElementTH = DOM.createTH();
        employeeNameElementTH.addClassName(CLASS_EMPLOYEE_NAME_TH);
        employeeNameElementTH.addClassName("firstColCell");
        reportTable.addChildToParent(employeeNameElementTR, employeeNameElementTH);


        //register employee time slot hours TR element
        Element employeeTimeSlotHoursTR = DOM.createTR();
        parentChildEMP_TSLOTHoursMap.computeIfAbsent(parentEMPLMap.get(key_employee_id_S), k -> new LinkedHashSet<>());
        if (parentChildEMP_TSLOTHoursMap.get(parentEMPLMap.get(key_employee_id_S)) != null) {
            parentChildEMP_TSLOTHoursMap.get(parentEMPLMap.get(key_employee_id_S)).add(employeeTimeSlotHoursTR);
        }
        employeeTimeSlotHoursTR.addClassName(CLASS_TIME_SLOT_HOURS_TR);
        reportTable.addChildToParent(parentElementTBody, employeeTimeSlotHoursTR);
        //register employee time slot hours TH element
        Element employeeTimeSlotHoursTH = DOM.createTH();
        employeeTimeSlotHoursTH.addClassName(CLASS_TIME_SLOT_HOURS_TH);
        reportTable.addChildToParent(employeeTimeSlotHoursTR, employeeTimeSlotHoursTH);
        Element employeeTimeSlotHoursSPAN = getSpanTTElement();
        employeeTimeSlotHoursSPAN.setInnerHTML(wfmStrings.timeSlotHoursOnly());
        reportTable.addChildToParent(employeeTimeSlotHoursTH, employeeTimeSlotHoursSPAN);

        //register employee in/out hours TR element
        Element employeeInOutHoursTR = DOM.createTR();
        parentChildEMP_INOUTHoursMap.computeIfAbsent(parentEMPLMap.get(key_employee_id_S), k -> new LinkedHashSet<>());
        if (parentChildEMP_INOUTHoursMap.get(parentEMPLMap.get(key_employee_id_S)) != null) {
            parentChildEMP_INOUTHoursMap.get(parentEMPLMap.get(key_employee_id_S)).add(employeeInOutHoursTR);
        }
        employeeInOutHoursTR.addClassName(CLASS_IN_OUT_HOURS_TR);
        reportTable.addChildToParent(parentElementTBody, employeeInOutHoursTR);
        //register employee in/out hours TH element
        Element employeeInOutHoursTH = DOM.createTH();
        employeeInOutHoursTH.addClassName(CLASS_IN_OUT_HOURS_TH);
        reportTable.addChildToParent(employeeInOutHoursTR, employeeInOutHoursTH);
        Element employeeInOutHoursSPAN = getSpanTTElement();
        employeeInOutHoursSPAN.setInnerHTML(wfmStrings.inHours());
        reportTable.addChildToParent(employeeInOutHoursTH, employeeInOutHoursSPAN);

        //register employee overall time sheet hours TR element
        Element employeeOverAllTimeSheetHoursTR = DOM.createTR();
        parentChildEMP_OVERALL_TSHETHoursMap.computeIfAbsent(parentEMPLMap.get(key_employee_id_S), k -> new LinkedHashSet<>());
        if (parentChildEMP_OVERALL_TSHETHoursMap.get(parentEMPLMap.get(key_employee_id_S)) != null) {
            parentChildEMP_OVERALL_TSHETHoursMap.get(parentEMPLMap.get(key_employee_id_S)).add(employeeOverAllTimeSheetHoursTR);
        }
        employeeOverAllTimeSheetHoursTR.addClassName(CLASS_OVERALL_TIME_SHEET_HOURS_TR);
        reportTable.addChildToParent(parentElementTBody, employeeOverAllTimeSheetHoursTR);
        //register employee overall time sheet hours TH element
        Element employeeOverAllTimeSheetHoursTH = DOM.createTH();
        employeeOverAllTimeSheetHoursTH.addClassName(CLASS_OVERALL_TIME_SHEET_HOURS_TH);
        reportTable.addChildToParent(employeeOverAllTimeSheetHoursTR, employeeOverAllTimeSheetHoursTH);
        Element employeeOverAllTimeSheetHoursSPAN = getSpanTTElement();
        employeeOverAllTimeSheetHoursSPAN.setInnerHTML(wfmStrings.timeSheetSummary());
        reportTable.addChildToParent(employeeOverAllTimeSheetHoursTH, employeeOverAllTimeSheetHoursSPAN);

        //register employee LR hours and Holiday days TR element
        Element LR_HoursAndHolidayDaysTR = DOM.createTR();
        parentChildEMP_LRHoursVSHolidayDaysMap.computeIfAbsent(parentEMPLMap.get(key_employee_id_S), k -> new LinkedHashSet<>());
        if (parentChildEMP_LRHoursVSHolidayDaysMap.get(parentEMPLMap.get(key_employee_id_S)) != null) {
            parentChildEMP_LRHoursVSHolidayDaysMap.get(parentEMPLMap.get(key_employee_id_S)).add(LR_HoursAndHolidayDaysTR);
        }
        LR_HoursAndHolidayDaysTR.addClassName(CLASS_LR_HOURS_VS_HOLIDAY_DAYS_TR);
        reportTable.addChildToParent(parentElementTBody, LR_HoursAndHolidayDaysTR);
        //register employee LR hours and Holiday days TH element
        Element LR_HoursAndHolidayDaysTH = DOM.createTH();
        LR_HoursAndHolidayDaysTH.addClassName(CLASS_LR_HOURS_VS_HOLIDAY_DAYS_TH);
        reportTable.addChildToParent(LR_HoursAndHolidayDaysTR, LR_HoursAndHolidayDaysTH);
        Element LR_HoursAndHolidayDaysSPAN = getSpanTTElement();
        LR_HoursAndHolidayDaysSPAN.setInnerHTML(wfmStrings.leaveHoursOnly());
        reportTable.addChildToParent(LR_HoursAndHolidayDaysTH, LR_HoursAndHolidayDaysSPAN);

        //register 'shown/hidden option employee projects/tasks' span element
        Element showHideElementSPAN = getShowHideOPTION_PROJECT(parentElementTBody, employeeNameElementTR, employeeRUItem.getEmployee_id(), enableExpand);
        String employee_name = employeeRUItem.getEmployee_name();
        if (employee_name != null && employee_name.length() > 50) {
            employee_name = employee_name.substring(0, 50) + "...";
        }
        String employee_code = employeeRUItem.getEmployee_code();
        String employee_photo = employeeRUItem.getEmployee_photo();
        String employee_position = employeeRUItem.getEmployee_position(); // Lavozimni olish

        StringBuilder employeeNameWidget = new StringBuilder();
        employeeNameWidget.append("<dl class='attRepTbl__emplNm'>");
        employeeNameWidget.append("<dt>").append(employee_code).append("</dt>").append("<dd>").append(employee_name).append("</dd>");
        employeeNameWidget.append("<dt>").append(wfmStrings.position()).append("</dt>").append("<dd>").append(employee_position).append("</dd>");
        employeeNameWidget.append("</dl>");
        Element employeePhotoSPAN = DOM.createSpan();
        employeePhotoSPAN.getStyle().setProperty("backgroundImage", "url('" + employee_photo + "')");
        employeePhotoSPAN.addClassName("tblUserImg");

        Element employeeInfoSPAN = DOM.createSpan();
        employeeInfoSPAN.setInnerHTML(employeeNameWidget.toString());
//        employeeInfoSPAN.getStyle().setProperty("display", "inline-table");
//        employeeInfoSPAN.getStyle().setProperty("maxWidth", "160px");
//        showHideElementSPAN.getStyle().setProperty("display", "flex");
//        showHideElementSPAN.getStyle().setProperty("alignItems", "center");

        reportTable.addChildToParent(showHideElementSPAN, employeePhotoSPAN);
        reportTable.addChildToParent(showHideElementSPAN, employeeInfoSPAN);
        showHideElementSPAN.addClassName("resUtilzLevel-1");
        reportTable.addChildToParent(employeeNameElementTH, showHideElementSPAN);

        generateEmployeeReportTD(employeeRUItem, employeeNameElementTR, employeeTimeSlotHoursTR, employeeInOutHoursTR, employeeOverAllTimeSheetHoursTR, LR_HoursAndHolidayDaysTR);
    }

    /**
     * Generate employee REPORT TD
     *
     * @param employeeRUItem           - employee item
     * @param employeeNameElementTR    - employee name TR
     * @param employeeTimeSlotHoursTR  - employee time slot hours TR
     * @param employeeInOutHoursTR  - employee inOut hours TR
     * @param employeeOverAllTimeSheetHoursTR
     *                                 - employee overall time sheet hours TR
     * @param LR_HoursAndHolidayDaysTR - LR hours and Holiday days TR
     */
    private void generateEmployeeReportTD(EmployeeResourceUtilItem employeeRUItem, Element employeeNameElementTR,
                                          Element employeeTimeSlotHoursTR, Element employeeInOutHoursTR, Element employeeOverAllTimeSheetHoursTR, Element LR_HoursAndHolidayDaysTR) {

        String key_employee_id = employeeRUItem.getEmployee_id() + "";
        empOVER_ALL_HOURS_TDMap.computeIfAbsent(key_employee_id, k -> new LinkedHashMap<>());
        empTIMESHEET_TDMap.computeIfAbsent(key_employee_id, k -> new LinkedHashMap<>());
        empOVER_ALL_HOURS_TD_OLD_HOURS_Map.computeIfAbsent(key_employee_id, k -> new LinkedHashMap<>());
        empTIMESHEET_HOURS_TD_OLD_HOURS_Map.computeIfAbsent(key_employee_id, k -> new LinkedHashMap<>());
        int totalTimeSlotHoursINT = 0;
        int totalInOutHoursINT = 0;
        int totalOverAllTimeSheetHoursINT = 0;
        int totalLR_HoursAndHolidayDaysINT = 0;
        int totalEmployeeAllocatedHoursINT = 0;
        Element totalEmployeeAllocatedHoursTD = DOM.createTD();
        Element totalEmployeeTimesheetHoursTD = DOM.createTD();
        totalEmployee_OVER_ALL_HOURS_TD_map.computeIfAbsent(key_employee_id, k -> new LinkedHashMap<>());
        totalEmployee_TIMESHEET_HOURS_TD_map.computeIfAbsent(key_employee_id, k -> new LinkedHashMap<>());
        for (int i = 1; i <= currentMonth; i++) {
            //register timeSlot hours
            Element timSlotHoursTD = DOM.createTD();
            empTIME_SLOT_HOURS_TD.put(i + "", timSlotHoursTD);
            reportTable.addChildToParent(employeeTimeSlotHoursTR, timSlotHoursTD);

            //register inOut hours
            Element inOutHoursTD = DOM.createTD();
            empIN_OUT_HOURS_TD.put(i + "", inOutHoursTD);
            reportTable.addChildToParent(employeeInOutHoursTR, inOutHoursTD);

/*            //register overAll timeSheet hours
            Element overAllTimeSheetHoursTD = DOM.createTD();
            empTIME_SHEET_HOURS_TD.put(i + "", overAllTimeSheetHoursTD);
            reportTable.addChildToParent(employeeOverAllTimeSheetHoursTR, overAllTimeSheetHoursTD);*/

            //register LR hours and Holiday days
            Element LR_HoursAndHolidayDaysTD = DOM.createTD();
            reportTable.addChildToParent(LR_HoursAndHolidayDaysTR, LR_HoursAndHolidayDaysTD);

            //register daily employee total in hours
            Element employeeAllocatedHoursTD = DOM.createTD();
            Element employeeTimesheetHoursTD = DOM.createTD();

            if (empOVER_ALL_HOURS_TDMap.get(key_employee_id) != null) {
                empOVER_ALL_HOURS_TDMap.get(key_employee_id).putIfAbsent(i, employeeAllocatedHoursTD);
            }
            if (totalEmployee_OVER_ALL_HOURS_TD_map.get(key_employee_id) != null) {
                totalEmployee_OVER_ALL_HOURS_TD_map.get(key_employee_id).putIfAbsent(employeeAllocatedHoursTD, totalEmployeeAllocatedHoursTD);
            }
            if (empOVER_ALL_HOURS_TD_OLD_HOURS_Map.get(key_employee_id) != null) {
                empOVER_ALL_HOURS_TD_OLD_HOURS_Map.get(key_employee_id).computeIfAbsent(i, i1 -> employeeRUItem.getTotalHours()[i1]);
            }
            //register daily employee timesheet hours
            if (empTIMESHEET_TDMap.get(key_employee_id) != null) {
                empTIMESHEET_TDMap.get(key_employee_id).putIfAbsent(i, employeeTimesheetHoursTD);
            }
            if (totalEmployee_TIMESHEET_HOURS_TD_map.get(key_employee_id) != null) {
                totalEmployee_TIMESHEET_HOURS_TD_map.get(key_employee_id).putIfAbsent(employeeTimesheetHoursTD, totalEmployeeTimesheetHoursTD);
            }
            if (empTIMESHEET_HOURS_TD_OLD_HOURS_Map.get(key_employee_id) != null) {
                empTIMESHEET_HOURS_TD_OLD_HOURS_Map.get(key_employee_id).computeIfAbsent(i, i1 -> employeeRUItem.getTotalTimeSheetHours()[i1]);
            }

            empOVER_ALL_HOURS_TD.put(i + "", employeeAllocatedHoursTD);
            reportTable.addChildToParent(employeeNameElementTR, employeeAllocatedHoursTD);
            empTIMESHEET_HOURS_TD.put(i + "", employeeTimesheetHoursTD);
            reportTable.addChildToParent(employeeOverAllTimeSheetHoursTR, employeeTimesheetHoursTD);
            if (monthHoliday[i] == 1) {//SUNDAY
                //time slot hours------------------------------------------------------
                if (employeeRUItem.getTotalTimeSlotHours()[i] == 0) {
                    if (employeeRUItem.getDayOff()[i]) {//DAY OFF
                        timSlotHoursTD.addClassName(STYLE_SUNDAY_MONTH);
                        timSlotHoursTD.setInnerHTML("<span style=color: black; >DO</span>");
                    } else {
                        timSlotHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    }
                } else {
                    if (employeeRUItem.getDayOff()[i]) {//DAY OFF
                        timSlotHoursTD.addClassName(STYLE_SUNDAY_RESOURCE_MONTH);
                    } else {
                        timSlotHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    }
                    totalTimeSlotHoursINT += employeeRUItem.getTotalTimeSlotHours()[i];
                    String dailyTimeSlotHTitle = (employeeRUItem.getTotalTimeSlotHours()[i] / 60) + " h " + (employeeRUItem.getTotalTimeSlotHours()[i] % 60) + " min";
                    timSlotHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(employeeRUItem.getTotalTimeSlotHours()[i]));
                    timSlotHoursTD.setTitle(dailyTimeSlotHTitle);
                }
                // inOut hours------------------------------------------------------
                if (employeeRUItem.getTotalInOutHours()[i] == 0) {
                    if (employeeRUItem.getDayOff()[i]) {//DAY OFF
                        inOutHoursTD.addClassName(STYLE_SUNDAY_MONTH);
                        inOutHoursTD.setInnerHTML("<span style=color: black; >DO</span>");
                    } else {
                        inOutHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    }
                } else {
                    if (employeeRUItem.getDayOff()[i]) {//DAY OFF
                        inOutHoursTD.addClassName(STYLE_SUNDAY_RESOURCE_MONTH);
                    } else {
                        inOutHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    }
                    totalInOutHoursINT += employeeRUItem.getTotalInOutHours()[i];
                    String dailyInOutHTitle = (employeeRUItem.getTotalInOutHours()[i] / 60) + " h " + (employeeRUItem.getTotalInOutHours()[i] % 60) + " min";
                    inOutHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(employeeRUItem.getTotalInOutHours()[i]));
                    inOutHoursTD.setTitle(dailyInOutHTitle);
                }
                //time sheet hours (overall)------------------------------------------
                if (employeeRUItem.getTotalTimeSheetHours()[i] == 0) {
                    if (employeeRUItem.getDayOff()[i]) {//DAY OFF
                        employeeTimesheetHoursTD.addClassName(STYLE_SUNDAY_MONTH);
                        employeeTimesheetHoursTD.setInnerHTML("<span style=color: black; >DO</span>");
                    } else {
                        employeeTimesheetHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    }
                } else {
                    if (employeeRUItem.getDayOff()[i]) {//DAY OFF
                        employeeTimesheetHoursTD.addClassName(STYLE_SUNDAY_RESOURCE_MONTH);
                    } else {
                        employeeTimesheetHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    }
                    totalOverAllTimeSheetHoursINT += employeeRUItem.getTotalTimeSheetHours()[i];
                    String dailyTimeSheetHTitle = (employeeRUItem.getTotalTimeSheetHours()[i] / 60) + " h " + (employeeRUItem.getTotalTimeSheetHours()[i] % 60) + " min";
                    employeeTimesheetHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(employeeRUItem.getTotalTimeSheetHours()[i]));
                    employeeTimesheetHoursTD.setTitle(dailyTimeSheetHTitle);
                }
                //LR hours and Holiday days-------------------------------------------
                if (employeeRUItem.getDayOff()[i]) {//DAY OFF
                    LR_HoursAndHolidayDaysTD.addClassName(STYLE_SUNDAY_RESOURCE_MONTH);
                } else {
                    LR_HoursAndHolidayDaysTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                }
                //employee allocated hours--------------------------------------------
                if (employeeRUItem.getTotalHours()[i] == 0) {
                    if (employeeRUItem.getDayOff()[i]) {//DAY OFF
                        employeeAllocatedHoursTD.addClassName(STYLE_SUNDAY_MONTH);
                        employeeAllocatedHoursTD.setInnerHTML("<span style=color: black; >DO</span>");
                    } else {
                        employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    }
                } else {
                    if (employeeRUItem.getTotalTimeSlotHours()[i] == employeeRUItem.getTotalHours()[i]) {//optimally allocated day
                        employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_OPTIMALLY_ALLOCATED_DAY);
                    } else {
                        if (employeeRUItem.getTotalTimeSlotHours()[i] > employeeRUItem.getTotalHours()[i]) {//under allocated day
                            employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_UNDER_ALLOCATED_DAY);
                        } else {
                            if (employeeRUItem.getTotalTimeSlotHours()[i] < employeeRUItem.getTotalHours()[i]) {//over allocated day
                                employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_OVER_ALLOCATED_DAY);
                            } else {
                                if (employeeRUItem.getDayOff()[i]) {//DAY OFF
                                    employeeAllocatedHoursTD.addClassName(STYLE_SUNDAY_RESOURCE_MONTH);
                                } else {
                                    employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                                }
                            }
                        }
                    }
                    totalEmployeeAllocatedHoursINT += employeeRUItem.getTotalHours()[i];
                    String dailyHour = (employeeRUItem.getTotalHours()[i] / 60) + " h " + (employeeRUItem.getTotalHours()[i] % 60) + " min";
                    employeeAllocatedHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(employeeRUItem.getTotalHours()[i]));
                    employeeAllocatedHoursTD.setTitle(dailyHour);
                }
                //-------------------------------------------------------------------
            } else if (monthHoliday[i] == 2 && employeeRUItem.getWithHoliday_INT()[i] != 1) {//DAY OFF and NOT HOLIDAY
                //time slot hours----------------------------------------------------
                if (employeeRUItem.getTotalTimeSlotHours()[i] == 0) {
                    if (employeeRUItem.getDayOff()[i]) {//DAY OFF
                        timSlotHoursTD.addClassName(STYLE_WORK_MONTH_HOLIDAY);
                    } else {
                        timSlotHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    }
                    timSlotHoursTD.setInnerHTML("&nbsp;");
                } else {
                    if (employeeRUItem.getDayOff()[i]) {//DAY OFF
                        timSlotHoursTD.addClassName(STYLE_WORK_MONTH_RESOURCE_HOLIDAY);
                    } else {
                        timSlotHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    }
                    totalTimeSlotHoursINT += employeeRUItem.getTotalTimeSlotHours()[i];
                    String dailyTimeSlotHTitle = (employeeRUItem.getTotalTimeSlotHours()[i] / 60) + " h " + (employeeRUItem.getTotalTimeSlotHours()[i] % 60) + " min";
                    timSlotHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(employeeRUItem.getTotalTimeSlotHours()[i]));
                    timSlotHoursTD.setTitle(dailyTimeSlotHTitle);
                }
                //InOut hours----------------------------------------------------
                if (employeeRUItem.getTotalInOutHours()[i] == 0) {
                    if (employeeRUItem.getDayOff()[i]) {//DAY OFF
                        inOutHoursTD.addClassName(STYLE_WORK_MONTH_HOLIDAY);
                    } else {
                        inOutHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    }
                    inOutHoursTD.setInnerHTML("&nbsp;");
                } else {
                    if (employeeRUItem.getDayOff()[i]) {//DAY OFF
                        inOutHoursTD.addClassName(STYLE_WORK_MONTH_RESOURCE_HOLIDAY);
                    } else {
                        inOutHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    }
                    totalInOutHoursINT += employeeRUItem.getTotalInOutHours()[i];
                    String dailyInOutHTitle = (employeeRUItem.getTotalInOutHours()[i] / 60) + " h " + (employeeRUItem.getTotalInOutHours()[i] % 60) + " min";
                    inOutHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(employeeRUItem.getTotalInOutHours()[i]));
                    inOutHoursTD.setTitle(dailyInOutHTitle);
                }
                //time sheet hours (overall)----------------------------------------
                if (employeeRUItem.getTotalTimeSheetHours()[i] == 0) {
                    if (employeeRUItem.getDayOff()[i]) {//DAY OFF
                        employeeTimesheetHoursTD.addClassName(STYLE_WORK_MONTH_HOLIDAY);
                    } else {
                        employeeTimesheetHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    }
                    employeeTimesheetHoursTD.setInnerHTML("&nbsp;");
                } else {
                    if (employeeRUItem.getDayOff()[i]) {//DAY OFF
                        employeeTimesheetHoursTD.addClassName(STYLE_WORK_MONTH_RESOURCE_HOLIDAY);
                    } else {
                        employeeTimesheetHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    }
                    totalOverAllTimeSheetHoursINT += employeeRUItem.getTotalTimeSheetHours()[i];
                    String dailyTimeSheetHTitle = (employeeRUItem.getTotalTimeSheetHours()[i] / 60) + " h " + (employeeRUItem.getTotalTimeSheetHours()[i] % 60) + " min";
                    employeeTimesheetHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(employeeRUItem.getTotalTimeSheetHours()[i]));
                    employeeTimesheetHoursTD.setTitle(dailyTimeSheetHTitle);
                }
                //LR hours and Holiday days-------------------------------------------
                if (employeeRUItem.getDayOff()[i]) {//DAY OFF
                    LR_HoursAndHolidayDaysTD.addClassName(STYLE_WORK_MONTH_RESOURCE_HOLIDAY);
                } else {
                    LR_HoursAndHolidayDaysTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                }

                //employee allocated hours-------------------------------------------
                if (employeeRUItem.getTotalHours()[i] == 0) {
                    if (employeeRUItem.getDayOff()[i] && employeeRUItem.getWith_LR_INT()[i] == 0) {//DAY OFF
                        employeeAllocatedHoursTD.addClassName(STYLE_WORK_MONTH_HOLIDAY);
                        employeeAllocatedHoursTD.setInnerHTML("&nbsp;");
                    } else if (!employeeRUItem.getDayOff()[i] && employeeRUItem.getWith_LR_INT()[i] != 0) {
                        employeeAllocatedHoursTD.addClassName(STYLE_WITH_LR_DAY);
                        employeeAllocatedHoursTD.setInnerHTML("<span>"+employeeRUItem.getShortNameLR()[i]+"</span>");
                    } else {
                        employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    }
                } else {
                    if (employeeRUItem.getTotalTimeSlotHours()[i] == employeeRUItem.getTotalHours()[i]) {//optimally allocated day
                        employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_OPTIMALLY_ALLOCATED_DAY);
                    } else {
                        if (employeeRUItem.getTotalTimeSlotHours()[i] > employeeRUItem.getTotalHours()[i]) {//under allocated day
                            employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_UNDER_ALLOCATED_DAY);
                        } else {
                            if (employeeRUItem.getTotalTimeSlotHours()[i] < employeeRUItem.getTotalHours()[i]) {//over allocated day
                                employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_OVER_ALLOCATED_DAY);
                            } else {
                                if (employeeRUItem.getDayOff()[i]) {//DAY OFF
                                    employeeAllocatedHoursTD.addClassName(STYLE_WORK_MONTH_RESOURCE_HOLIDAY);
                                } else {
                                    employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                                }
                            }
                        }
                    }
                    totalEmployeeAllocatedHoursINT += employeeRUItem.getTotalHours()[i];
                    String dailyHour = (employeeRUItem.getTotalHours()[i] / 60) + " h " + (employeeRUItem.getTotalHours()[i] % 60) + " min";
                    employeeAllocatedHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(employeeRUItem.getTotalHours()[i]));
                    employeeAllocatedHoursTD.setTitle(dailyHour);
                }
                //-----------------------------------------------------------------
            } else if (monthHoliday[i] == 3) {//HOLIDAY
                //time slot hours----------------------------------------------------
                if (employeeRUItem.getTotalTimeSlotHours()[i] == 0) {
                    timSlotHoursTD.addClassName(/*STYLE_DEFAULT_MONTH_DAY*/STYLE_WORK_COMPANY_HOLIDAY);
                    timSlotHoursTD.setInnerHTML("&nbsp;");
                } else {
                    timSlotHoursTD.addClassName(/*STYLE_DEFAULT_MONTH_DAY*/STYLE_WORK_COMPANY_HOLIDAY);
                    totalTimeSlotHoursINT += employeeRUItem.getTotalTimeSlotHours()[i];
                    String dailyTimeSlotHTitle = (employeeRUItem.getTotalTimeSlotHours()[i] / 60) + " h " + (employeeRUItem.getTotalTimeSlotHours()[i] % 60) + " min";
                    timSlotHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(employeeRUItem.getTotalTimeSlotHours()[i]));
                    timSlotHoursTD.setTitle(dailyTimeSlotHTitle);
                }
                //InOut hours----------------------------------------------------
                if (employeeRUItem.getTotalInOutHours()[i] == 0) {
                    inOutHoursTD.addClassName(STYLE_WORK_COMPANY_HOLIDAY);
                    inOutHoursTD.setInnerHTML("&nbsp;");
                } else {
                    inOutHoursTD.addClassName(STYLE_WORK_COMPANY_HOLIDAY);
                    totalInOutHoursINT += employeeRUItem.getTotalInOutHours()[i];
                    String dailyInOutHTitle = (employeeRUItem.getTotalInOutHours()[i] / 60) + " h " + (employeeRUItem.getTotalInOutHours()[i] % 60) + " min";
                    inOutHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(employeeRUItem.getTotalInOutHours()[i]));
                    inOutHoursTD.setTitle(dailyInOutHTitle);
                }
                //time sheet hours (overall)----------------------------------------
                if (employeeRUItem.getTotalTimeSheetHours()[i] == 0) {
                    employeeTimesheetHoursTD.addClassName(STYLE_WORK_COMPANY_HOLIDAY);
                    employeeTimesheetHoursTD.setInnerHTML("&nbsp;");
                } else {
                    employeeTimesheetHoursTD.addClassName(STYLE_WORK_COMPANY_HOLIDAY);
                    totalOverAllTimeSheetHoursINT += employeeRUItem.getTotalTimeSheetHours()[i];
                    String dailyTimeSheetHTitle = (employeeRUItem.getTotalTimeSheetHours()[i] / 60) + " h " + (employeeRUItem.getTotalTimeSheetHours()[i] % 60) + " min";
                    employeeTimesheetHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(employeeRUItem.getTotalTimeSheetHours()[i]));
                    employeeTimesheetHoursTD.setTitle(dailyTimeSheetHTitle);
                }
                //Holiday days-------------------------------------------------------
                LR_HoursAndHolidayDaysTD.addClassName(STYLE_WORK_COMPANY_HOLIDAY);
                LR_HoursAndHolidayDaysTD.setInnerHTML("H");
                LR_HoursAndHolidayDaysTD.setTitle(wfmStrings.holiday());
                //employee allocated hours-------------------------------------------
                if (employeeRUItem.getTotalHours()[i] == 0) {
                    employeeAllocatedHoursTD.addClassName(/*STYLE_DEFAULT_MONTH_DAY*/STYLE_WORK_COMPANY_HOLIDAY);
                    employeeAllocatedHoursTD.setInnerHTML("H");
                } else {
                    if (employeeRUItem.getTotalTimeSlotHours()[i] == employeeRUItem.getTotalHours()[i]) {//optimally allocated day
                        employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_OPTIMALLY_ALLOCATED_DAY);
                    } else {
                        if (employeeRUItem.getTotalTimeSlotHours()[i] > employeeRUItem.getTotalHours()[i]) {//under allocated day
                            employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_UNDER_ALLOCATED_DAY);
                        } else {
                            if (employeeRUItem.getTotalTimeSlotHours()[i] < employeeRUItem.getTotalHours()[i]) {//over allocated day
                                employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_OVER_ALLOCATED_DAY);
                            } else {
                                employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                            }
                        }
                    }
                    employeeAllocatedHoursTD.addClassName(STYLE_WORK_COMPANY_HOLIDAY);
                    totalEmployeeAllocatedHoursINT += employeeRUItem.getTotalHours()[i];
                    String dailyHour = (employeeRUItem.getTotalHours()[i] / 60) + " h " + (employeeRUItem.getTotalHours()[i] % 60) + " min";
                    employeeAllocatedHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(employeeRUItem.getTotalHours()[i]));
                    employeeAllocatedHoursTD.setTitle(dailyHour);
                }
                //-------------------------------------------------------------------
            } else if (employeeRUItem.getWithHoliday_INT()[i] == 1) {
                //time slot hours----------------------------------------------------
                if (employeeRUItem.getTotalTimeSlotHours()[i] == 0) {
                    timSlotHoursTD.addClassName(/*STYLE_DEFAULT_MONTH_DAY*/STYLE_WORK_COMPANY_HOLIDAY);
                    timSlotHoursTD.setInnerHTML("&nbsp;");
                } else {
                    timSlotHoursTD.addClassName(/*STYLE_DEFAULT_MONTH_DAY*/STYLE_WORK_COMPANY_HOLIDAY);
                    totalTimeSlotHoursINT += employeeRUItem.getTotalTimeSlotHours()[i];
                    String dailyTimeSlotHTitle = (employeeRUItem.getTotalTimeSlotHours()[i] / 60) + " h " + (employeeRUItem.getTotalTimeSlotHours()[i] % 60) + " min";
                    timSlotHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(employeeRUItem.getTotalTimeSlotHours()[i]));
                    timSlotHoursTD.setTitle(dailyTimeSlotHTitle);
                }
                //InOut hours----------------------------------------------------
                if (employeeRUItem.getTotalInOutHours()[i] == 0) {
                    inOutHoursTD.addClassName(STYLE_WORK_COMPANY_HOLIDAY);
                    inOutHoursTD.setInnerHTML("&nbsp;");
                } else {
                    inOutHoursTD.addClassName(STYLE_WORK_COMPANY_HOLIDAY);
                    totalInOutHoursINT += employeeRUItem.getTotalInOutHours()[i];
                    String dailyTimeSlotHTitle = (employeeRUItem.getTotalInOutHours()[i] / 60) + " h " + (employeeRUItem.getTotalInOutHours()[i] % 60) + " min";
                    inOutHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(employeeRUItem.getTotalInOutHours()[i]));
                    inOutHoursTD.setTitle(dailyTimeSlotHTitle);
                }
                //time sheet hours (overall)----------------------------------------
                if (employeeRUItem.getTotalTimeSheetHours()[i] == 0) {
                    employeeTimesheetHoursTD.addClassName(STYLE_WORK_COMPANY_HOLIDAY);
                    employeeTimesheetHoursTD.setInnerHTML("&nbsp;");
                } else {
                    employeeTimesheetHoursTD.addClassName(STYLE_WORK_COMPANY_HOLIDAY);
                    totalOverAllTimeSheetHoursINT += employeeRUItem.getTotalTimeSheetHours()[i];
                    String dailyTimeSheetHTitle = (employeeRUItem.getTotalTimeSheetHours()[i] / 60) + " h " + (employeeRUItem.getTotalTimeSheetHours()[i] % 60) + " min";
                    employeeTimesheetHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(employeeRUItem.getTotalTimeSheetHours()[i]));
                    employeeTimesheetHoursTD.setTitle(dailyTimeSheetHTitle);
                }
                //Holiday days -----------------------------------------------------------
                LR_HoursAndHolidayDaysTD.addClassName(STYLE_WORK_COMPANY_HOLIDAY);
                LR_HoursAndHolidayDaysTD.setInnerHTML("H");
                LR_HoursAndHolidayDaysTD.setTitle(wfmStrings.holiday());
                //employee allocated hours-------------------------------------------------
                if (employeeRUItem.getTotalHours()[i] == 0) {
                    employeeAllocatedHoursTD.addClassName(/*STYLE_DEFAULT_MONTH_DAY*/STYLE_WORK_COMPANY_HOLIDAY);
                    employeeAllocatedHoursTD.setInnerHTML("H");
                } else {
                    if (employeeRUItem.getTotalTimeSlotHours()[i] == employeeRUItem.getTotalHours()[i]) {//optimally allocated day
                        employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_OPTIMALLY_ALLOCATED_DAY);
                    } else {
                        if (employeeRUItem.getTotalTimeSlotHours()[i] > employeeRUItem.getTotalHours()[i]) {//under allocated day
                            employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_UNDER_ALLOCATED_DAY);
                        } else {
                            if (employeeRUItem.getTotalTimeSlotHours()[i] < employeeRUItem.getTotalHours()[i]) {//over allocated day
                                employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_OVER_ALLOCATED_DAY);
                            } else {
                                employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                            }
                        }
                    }
                    employeeAllocatedHoursTD.addClassName(STYLE_WORK_COMPANY_HOLIDAY);
                    totalEmployeeAllocatedHoursINT += employeeRUItem.getTotalHours()[i];
                    String dailyHour = (employeeRUItem.getTotalHours()[i] / 60) + " h " + (employeeRUItem.getTotalHours()[i] % 60) + " min";
                    employeeAllocatedHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(employeeRUItem.getTotalHours()[i]));
                    employeeAllocatedHoursTD.setTitle(dailyHour);
                }
                //-------------------------------------------------------------------
            } else if (employeeRUItem.getWith_LR_INT()[i] != 0) {
                //time slot hours----------------------------------------------------
                if (employeeRUItem.getTotalTimeSlotHours()[i] == 0) {
                    timSlotHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    timSlotHoursTD.setInnerHTML("&nbsp;");
                } else {
                    timSlotHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    totalTimeSlotHoursINT += employeeRUItem.getTotalTimeSlotHours()[i];
                    String dailyTimeSlotHTitle = (employeeRUItem.getTotalTimeSlotHours()[i] / 60) + " h " + (employeeRUItem.getTotalTimeSlotHours()[i] % 60) + " min";
                    timSlotHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(employeeRUItem.getTotalTimeSlotHours()[i]));
                    timSlotHoursTD.setTitle(dailyTimeSlotHTitle);
                }
                //InOut hours----------------------------------------------------
                if (employeeRUItem.getTotalInOutHours()[i] == 0) {
                    inOutHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    inOutHoursTD.setInnerHTML("&nbsp;");
                } else {
                    inOutHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    totalInOutHoursINT += employeeRUItem.getTotalInOutHours()[i];
                    String dailyInOutHTitle = (employeeRUItem.getTotalInOutHours()[i] / 60) + " h " + (employeeRUItem.getTotalInOutHours()[i] % 60) + " min";
                    inOutHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(employeeRUItem.getTotalInOutHours()[i]));
                    inOutHoursTD.setTitle(dailyInOutHTitle);
                }
                //time sheet hours (overall)-----------------------------------------
                if (employeeRUItem.getTotalTimeSheetHours()[i] == 0) {
                    employeeTimesheetHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    employeeTimesheetHoursTD.setInnerHTML("&nbsp;");
                } else {
                    employeeTimesheetHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    totalOverAllTimeSheetHoursINT += employeeRUItem.getTotalTimeSheetHours()[i];
                    String dailyTimeSheetHTitle = (employeeRUItem.getTotalTimeSheetHours()[i] / 60) + " h " + (employeeRUItem.getTotalTimeSheetHours()[i] % 60) + " min";
                    employeeTimesheetHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(employeeRUItem.getTotalTimeSheetHours()[i]));
                    employeeTimesheetHoursTD.setTitle(dailyTimeSheetHTitle);
                }
                //LR hours-----------------------------------------------------------
                totalLR_HoursAndHolidayDaysINT += employeeRUItem.getWith_LR_INT()[i];
                boolean isUnauthorizedLR = false;
                if (employeeRUItem.getWith_LR_INT()[i] < 0) {
                    isUnauthorizedLR = true;
                    LR_HoursAndHolidayDaysTD.addClassName(STYLE_WITH__UNAUTHORIZED_LR_DAY);
                    employeeRUItem.getWith_LR_INT()[i] = -employeeRUItem.getWith_LR_INT()[i];
                } else {
                    LR_HoursAndHolidayDaysTD.addClassName(STYLE_WITH_LR_DAY);
                }
                String dailyLR_HoursTitle = (employeeRUItem.getWith_LR_INT()[i] / 60) + " h " + (employeeRUItem.getWith_LR_INT()[i] % 60) + " min";
                LR_HoursAndHolidayDaysTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(employeeRUItem.getWith_LR_INT()[i]));
                if (employeeRUItem.getWith_LR_INT()[i] == employeeRUItem.getTotalTimeSlotHours()[i]) {
                    LR_HoursAndHolidayDaysTD.setInnerHTML("LR");
                }
                LR_HoursAndHolidayDaysTD.setTitle(dailyLR_HoursTitle);
                //employee allocated hours-------------------------------------------------
                if (employeeRUItem.getTotalHours()[i] == 0) {
                    employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);

                    if (isUnauthorizedLR) {
                        employeeAllocatedHoursTD.addClassName(STYLE_WITH__UNAUTHORIZED_LR_DAY);
                        employeeAllocatedHoursTD.setInnerHTML("A");
                    } else {
                        employeeAllocatedHoursTD.addClassName(STYLE_WITH_LR_DAY);
                        employeeAllocatedHoursTD.setInnerHTML("<span></span>");
                    }
                } else {
                    if (employeeRUItem.getTotalTimeSlotHours()[i] == employeeRUItem.getTotalHours()[i]) {//optimally allocated day
                        employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_OPTIMALLY_ALLOCATED_DAY);
                    } else {
                        if (employeeRUItem.getTotalTimeSlotHours()[i] > employeeRUItem.getTotalHours()[i]) {//under allocated day
                            employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_UNDER_ALLOCATED_DAY);
                        } else {
                            if (employeeRUItem.getTotalTimeSlotHours()[i] < employeeRUItem.getTotalHours()[i]) {//over allocated day
                                employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_OVER_ALLOCATED_DAY);
                            } else {
                                employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                            }
                        }
                    }
                    employeeAllocatedHoursTD.addClassName(STYLE_WITH_LR_DAY);
                    totalEmployeeAllocatedHoursINT += employeeRUItem.getTotalHours()[i];
                    String dailyHour = (employeeRUItem.getTotalHours()[i] / 60) + " h " + (employeeRUItem.getTotalHours()[i] % 60) + " min";
                    employeeAllocatedHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(employeeRUItem.getTotalHours()[i]));
                    employeeAllocatedHoursTD.setTitle(dailyHour);
                }
                //-------------------------------------------------------------------
            } else {
                if (employeeRUItem.getTotalTimeSlotHours()[i] == 0) {
                    employeeAllocatedHoursTD.addClassName(STYLE_SUNDAY_MONTH);
                    employeeAllocatedHoursTD.setInnerHTML("DO");
                    employeeTimesheetHoursTD.addClassName(STYLE_SUNDAY_MONTH);
                    employeeTimesheetHoursTD.setInnerHTML("DO");
                    LR_HoursAndHolidayDaysTD.addClassName(STYLE_SUNDAY_MONTH);
                    LR_HoursAndHolidayDaysTD.setInnerHTML("DO");
                    timSlotHoursTD.addClassName(STYLE_SUNDAY_MONTH);
                    timSlotHoursTD.setInnerHTML("DO");
                    inOutHoursTD.addClassName(STYLE_SUNDAY_MONTH);
                    inOutHoursTD.setInnerHTML("DO");
                } else {
                    employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    employeeTimesheetHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    LR_HoursAndHolidayDaysTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    timSlotHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    inOutHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                }
                //time slot hours----------------------------------------------------
                if (employeeRUItem.getTotalTimeSlotHours()[i] != 0) {
                    totalTimeSlotHoursINT += employeeRUItem.getTotalTimeSlotHours()[i];
                    String dailyTimeSlotHTitle = (employeeRUItem.getTotalTimeSlotHours()[i] / 60) + " h " + (employeeRUItem.getTotalTimeSlotHours()[i] % 60) + " min";
//                    timSlotHoursTD.setInnerHTML((employeeRUItem.getTotalTimeSlotHours()[i] / 60) + "");
                    timSlotHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(employeeRUItem.getTotalTimeSlotHours()[i]));
                    timSlotHoursTD.setTitle(dailyTimeSlotHTitle);
                }
                //InOut hours-------------------------------
                if (employeeRUItem.getTotalInOutHours()[i] != 0) {
                    totalInOutHoursINT += employeeRUItem.getTotalInOutHours()[i];
                    String dailyInOutHTitle = (employeeRUItem.getTotalInOutHours()[i] / 60) + " h " + (employeeRUItem.getTotalInOutHours()[i] % 60) + " min";
                    inOutHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(employeeRUItem.getTotalInOutHours()[i]));
                    inOutHoursTD.setTitle(dailyInOutHTitle);
                }
                //time sheet hours (overall)----------------------------------------
                if (employeeRUItem.getTotalTimeSheetHours()[i] != 0) {
                    totalOverAllTimeSheetHoursINT += employeeRUItem.getTotalTimeSheetHours()[i];
                    String dailyTimeSheetHTitle = (employeeRUItem.getTotalTimeSheetHours()[i] / 60) + " h " + (employeeRUItem.getTotalTimeSheetHours()[i] % 60) + " min";
                    employeeTimesheetHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(employeeRUItem.getTotalTimeSheetHours()[i]));
                    employeeTimesheetHoursTD.setTitle(dailyTimeSheetHTitle);
                }
                //employee allocated hours-------------------------------------------
                if (employeeRUItem.getTotalHours()[i] == 0) {
                    employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    employeeAllocatedHoursTD.setInnerHTML("&nbsp;");
                } else {
                    if (employeeRUItem.getTotalTimeSlotHours()[i] == employeeRUItem.getTotalHours()[i]) {//optimally allocated day
                        employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_OPTIMALLY_ALLOCATED_DAY);
                    } else {
                        if (employeeRUItem.getTotalTimeSlotHours()[i] > employeeRUItem.getTotalHours()[i]) {//under allocated day
                            employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_UNDER_ALLOCATED_DAY);
                        } else {
                            if (employeeRUItem.getTotalTimeSlotHours()[i] < employeeRUItem.getTotalHours()[i]) {//over allocated day
                                employeeAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_OVER_ALLOCATED_DAY);
                            }
                        }
                    }
                    totalEmployeeAllocatedHoursINT += employeeRUItem.getTotalHours()[i];
                    String dailyHour = (employeeRUItem.getTotalHours()[i] / 60) + " h " + (employeeRUItem.getTotalHours()[i] % 60) + " min";
                    employeeAllocatedHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(employeeRUItem.getTotalHours()[i]));
                    employeeAllocatedHoursTD.setTitle(dailyHour);
                }
                //-----------------------------------------------------------------
            }
        }
        //total timeSlot hours TD element
        Element totalTimeSlotHoursTD = DOM.createTD();
        totalTimeSlotHoursTD.addClassName(STYLE_TOTAL_MIDDLE_DAY);
		totalTimeSlotHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(totalTimeSlotHoursINT));
		if (totalTimeSlotHoursINT > 0) {
			String totalTimeSlotHoursINTTitle = (totalTimeSlotHoursINT / 60) + " h " + (totalTimeSlotHoursINT % 60) + " min";
			totalTimeSlotHoursTD.setTitle(totalTimeSlotHoursINTTitle);
		}
		reportTable.addChildToParent(employeeTimeSlotHoursTR, totalTimeSlotHoursTD);
        //total InOut hours TD element
        Element totalInOutHoursTD = DOM.createTD();
        totalInOutHoursTD.addClassName(STYLE_TOTAL_MIDDLE_DAY);
		totalInOutHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(totalInOutHoursINT));
		if (totalInOutHoursINT > 0) {
			String totalInOutHoursINTTitle = (totalInOutHoursINT / 60) + " h " + (totalInOutHoursINT % 60) + " min";
			totalInOutHoursTD.setTitle(totalInOutHoursINTTitle);
		}
		reportTable.addChildToParent(employeeInOutHoursTR, totalInOutHoursTD);
        //total overall timeSheet hours TD element
        //Element totalOverAllTimeSheetHoursTD = DOM.createTD();
        totalEmployeeTimesheetHoursTD.addClassName(STYLE_TOTAL_MIDDLE_DAY);
        totalEmployeeTimesheetHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(totalOverAllTimeSheetHoursINT));
		if (totalOverAllTimeSheetHoursINT > 0) {
			String totalOverAllTimeSheetHoursINTTitle = (totalOverAllTimeSheetHoursINT / 60) + " h " + (totalOverAllTimeSheetHoursINT % 60) + " min";
            totalEmployeeTimesheetHoursTD.setTitle(totalOverAllTimeSheetHoursINTTitle);
		}
        totalEmployeeTimesheetHoursTD.setAttribute("name", totalOverAllTimeSheetHoursINT + "");
		reportTable.addChildToParent(employeeOverAllTimeSheetHoursTR, totalEmployeeTimesheetHoursTD);

        //total LR hours and holiday days TD element
        Element totalLR_HoursAndHolidayDaysTD = DOM.createTD();
        totalLR_HoursAndHolidayDaysTD.addClassName(STYLE_TOTAL_MIDDLE_DAY);
		totalLR_HoursAndHolidayDaysTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(totalLR_HoursAndHolidayDaysINT));
		if (totalLR_HoursAndHolidayDaysINT > 0) {
			String totalLR_HoursAndHolidayDaysINTTitle = (totalLR_HoursAndHolidayDaysINT / 60) + " h " + (totalLR_HoursAndHolidayDaysINT % 60) + " min";
			totalLR_HoursAndHolidayDaysTD.setTitle(totalLR_HoursAndHolidayDaysINTTitle);
		}
		reportTable.addChildToParent(LR_HoursAndHolidayDaysTR, totalLR_HoursAndHolidayDaysTD);
        //total employee allocated hours TD element
        totalEmployeeAllocatedHoursTD.addClassName(STYLE_TOTAL_MIDDLE_DAY);
		totalEmployeeAllocatedHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(totalEmployeeAllocatedHoursINT));
		if (totalEmployeeAllocatedHoursINT > 0) {
			String totalEmployeeAllocatedHoursINTTitle = (totalEmployeeAllocatedHoursINT / 60) + " h " + (totalEmployeeAllocatedHoursINT % 60) + " min";
			totalEmployeeAllocatedHoursTD.setTitle(totalEmployeeAllocatedHoursINTTitle);
		}
		totalEmployeeAllocatedHoursTD.setAttribute("name", totalEmployeeAllocatedHoursINT + "");
        reportTable.addChildToParent(employeeNameElementTR, totalEmployeeAllocatedHoursTD);
    }

    /**
     * Time Slot hours row visible
     *
     * @param show - shown
     */
    void showHideTimeSlotHours(boolean show, String employeeID) {
        if (employeeID == null) {
            for (String empID : parentEMPLMap.keySet()) {
                showHideEMP_TimeSlotHours(show, empID);
            }
        } else {
            showHideEMP_TimeSlotHours(show, employeeID);
        }
    }

    private void showHideEMP_TimeSlotHours(boolean show, String empID) {
        Element keyEmpNameElementTR = parentEMPLMap.get(empID);
        Set<Element> elements = parentChildEMP_TSLOTHoursMap.get(keyEmpNameElementTR);
        if (keyEmpNameElementTR.getClassName().contains(CLASS_EXPANDED_EMPLOYEE_NAME) && show) {
            if (elements != null && elements.size() > 0) {
                for (Element child : elements) {
                    if (!child.getClassName().contains(CLASS_TIME_SLOT_HOURS_TR_V)) {
                        child.addClassName(CLASS_TIME_SLOT_HOURS_TR_V);
                    }
                }
            }
        } else {
            if (elements != null && elements.size() > 0) {
                for (Element child : elements) {
                    if (child.getClassName().contains(CLASS_TIME_SLOT_HOURS_TR_V)) {
                        child.removeClassName(CLASS_TIME_SLOT_HOURS_TR_V);
                    }
                }
            }
        }
    }

    void showHideInOutHours(boolean show, String employeeID) {
        if (employeeID == null) {
            for (String empID : parentEMPLMap.keySet()) {
                showHideEMP_InOutHours(show, empID);
            }
        } else {
            showHideEMP_InOutHours(show, employeeID);
        }
    }

    private void showHideEMP_InOutHours(boolean show, String empID) {
        Element keyEmpNameElementTR = parentEMPLMap.get(empID);
        Set<Element> elements = parentChildEMP_INOUTHoursMap.get(keyEmpNameElementTR);
        if (keyEmpNameElementTR.getClassName().contains(CLASS_EXPANDED_EMPLOYEE_NAME) && show) {
            if (elements != null && elements.size() > 0) {
                for (Element child : elements) {
                    if (!child.getClassName().contains(CLASS_IN_OUT_HOURS_TR_V)) {
                        child.addClassName(CLASS_IN_OUT_HOURS_TR_V);
                    }
                }
            }
        } else {
            if (elements != null && elements.size() > 0) {
                for (Element child : elements) {
                    if (child.getClassName().contains(CLASS_IN_OUT_HOURS_TR_V)) {
                        child.removeClassName(CLASS_IN_OUT_HOURS_TR_V);
                    }
                }
            }
        }
    }

    /**
     * Overall Time Sheet hours row visible
     *
     * @param show       - shown
     * @param employeeID - selected employee ID
     */
    void showHideOverallTimeSheetHours(boolean show, String employeeID) {
        if (employeeID == null) {
            for (String empID : parentEMPLMap.keySet()) {
                showHideOVERALL_EMP_TimeSheetHours(show, empID);
            }
        } else {
            showHideOVERALL_EMP_TimeSheetHours(show, employeeID);
        }
    }

    private void showHideOVERALL_EMP_TimeSheetHours(boolean show, String empID) {
        Element keyEmpNameElementTR = parentEMPLMap.get(empID);
        Set<Element> elements = parentChildEMP_OVERALL_TSHETHoursMap.get(keyEmpNameElementTR);
        if (keyEmpNameElementTR.getClassName().contains(CLASS_EXPANDED_EMPLOYEE_NAME) && show) {
            if (elements != null && elements.size() > 0) {
                for (Element child : elements) {
                    if (!child.getClassName().contains(CLASS_OVERALL_TIME_SHEET_HOURS_TR_V)) {
                        child.addClassName(CLASS_OVERALL_TIME_SHEET_HOURS_TR_V);
                    }
                }
            }
        } else {
            if (elements != null && elements.size() > 0) {
                for (Element child : elements) {
                    if (child.getClassName().contains(CLASS_OVERALL_TIME_SHEET_HOURS_TR_V)) {
                        child.removeClassName(CLASS_OVERALL_TIME_SHEET_HOURS_TR_V);
                    }
                }
            }
        }
    }

    void showHideOverallLeaveRequestHours(boolean show, String employeeID) {
        if (employeeID == null) {
            for (String empID : parentEMPLMap.keySet()) {
                showHideOVERALL_EMP_LRHours(show, empID);
            }
        } else {
            showHideOVERALL_EMP_LRHours(show, employeeID);
        }
    }

    private void showHideOVERALL_EMP_LRHours(boolean show, String empID) {
        Element keyEmpNameElementTR = parentEMPLMap.get(empID);
        Set<Element> elements = parentChildEMP_LRHoursVSHolidayDaysMap.get(keyEmpNameElementTR);
        if (keyEmpNameElementTR.getClassName().contains(CLASS_EXPANDED_EMPLOYEE_NAME) && show) {
            if (elements != null && elements.size() > 0) {
                for (Element child : elements) {
                    if (!child.getClassName().contains(CLASS_LR_HOURS_VS_HOLIDAY_DAYS_TR_V)) {
                        child.addClassName(CLASS_LR_HOURS_VS_HOLIDAY_DAYS_TR_V);
                    }
                }
            }
        } else {
            if (elements != null && elements.size() > 0) {
                for (Element child : elements) {
                    if (child.getClassName().contains(CLASS_LR_HOURS_VS_HOLIDAY_DAYS_TR_V)) {
                        child.removeClassName(CLASS_LR_HOURS_VS_HOLIDAY_DAYS_TR_V);
                    }
                }
            }
        }
    }

    /**
     * Time Sheet hours row visible
     *
     * @param show - shown
     */
    void showHideTimeSheetHours(boolean show, String employeeID, String projectID) {
        if (employeeID == null) {
            for (String empID : parentEMPLMap.keySet()) {
                showHideEMP_TimeSheetHours(show, empID, null);
            }
        } else {
            showHideEMP_TimeSheetHours(show, employeeID, projectID);
        }
    }

    private void showHideEMP_TimeSheetHours(boolean show, String empID, String projectID) {
        Element keyEmpNameElementTR = parentEMPLMap.get(empID);
        if (projectID == null) {
            Set<Element> elementsPROJ = parentChildEMP_PROJMap.get(keyEmpNameElementTR);
            if (elementsPROJ != null && elementsPROJ.size() > 0) {
                for (Element keyProNameElementTR_ChildPROJ : elementsPROJ) {
                    if (keyEmpNameElementTR.getClassName().contains(CLASS_EXPANDED_EMPLOYEE_NAME) && keyProNameElementTR_ChildPROJ.getClassName().contains(CLASS_EXPANDED_PROJECT_NAME) && show) {
                        showEMP_TimeSheetHoursTASK(keyProNameElementTR_ChildPROJ);
                    } else {
                        hideEMP_TimeSheetHoursTASK(keyProNameElementTR_ChildPROJ);
                    }
                }
            }
        } else {
            String key_EMP_PRO_ID = empID + "_" + projectID;
            Element keyProNameElementTR = parentPROJMap.get(key_EMP_PRO_ID);
            if (keyEmpNameElementTR.getClassName().contains(CLASS_EXPANDED_EMPLOYEE_NAME) && keyProNameElementTR.getClassName().contains(CLASS_EXPANDED_PROJECT_NAME) && show) {
                showEMP_TimeSheetHoursTASK(keyProNameElementTR);
            } else {
                hideEMP_TimeSheetHoursTASK(keyProNameElementTR);
            }
        }
    }

    private void showEMP_TimeSheetHoursTASK(Element keyProNameElementTR) {
        Set<Element> elements = parentChildEMP_TSHETHoursMap.get(keyProNameElementTR);
        if (elements != null && elements.size() > 0) {
            for (Element child : elements) {
                if (!child.getClassName().contains(CLASS_TIME_SHEET_HOURS_TR_V)) {
                    child.addClassName(CLASS_TIME_SHEET_HOURS_TR_V);
                }
            }
        }
    }

    private void hideEMP_TimeSheetHoursTASK(Element keyProNameElementTR) {
        Set<Element> elements = parentChildEMP_TSHETHoursMap.get(keyProNameElementTR);
        if (elements != null && elements.size() > 0) {
            for (Element child : elements) {
                if (child.getClassName().contains(CLASS_TIME_SHEET_HOURS_TR_V)) {
                    child.removeClassName(CLASS_TIME_SHEET_HOURS_TR_V);
                }
            }
        }
    }

    private void addListenerToTaskName(final Element taskNameElement, final Integer taskID, final boolean issue) {
        DOM.sinkEvents(taskNameElement, Event.ONCLICK);
        DOM.setEventListener(taskNameElement, event -> {
            if (DOM.eventGetType(event) == Event.ONCLICK) {
                SinksContainerFactory.entryPoint.onHistoryChanged((issue ? "issue|summary/" : "task|summary/") + taskID);
            }
        });
    }

    private void addListenerToTimesheetTD(final String key_emp_pro_task_id, final TaskItem taskRUItem, final Integer employeeID, final Element taskTimeSheetHoursTD, final DateNonConvertable dateNonConvertable,
                                          final Element totalTimeSheetHoursTD, final Element timeSheetHoursTDSpan,
                                          final Element totalEmployeeOverAllTimesheetHoursTD, final Element employeeOveAllTimesheetElementTD) {
        DOM.sinkEvents(taskTimeSheetHoursTD, Event.ONCLICK);
        DOM.setEventListener(taskTimeSheetHoursTD, event -> {
            if (DOM.eventGetType(event) == Event.ONCLICK) {
                final int currentTimesheetTime = Integer.valueOf(timeSheetHoursTDSpan.getAttribute("name"));
                final int employeeTimesheetDailyHour = Integer.valueOf(employeeOveAllTimesheetElementTD.getAttribute("name"));
                ResourceUtilTimesheetEditPopupCell resourceUtil = new ResourceUtilTimesheetEditPopupCell(taskRUItem, dateNonConvertable, taskTimeSheetHoursTD, currentTimesheetTime, employeeID);
                resourceUtil.setChangeDailyTimesheetListener(changedTimesheetTime -> {
                    applyTimesheetUpdate(taskRUItem, key_emp_pro_task_id, employeeID, dateNonConvertable, totalTimeSheetHoursTD, currentTimesheetTime,
                            timeSheetHoursTDSpan, changedTimesheetTime, totalEmployeeOverAllTimesheetHoursTD, employeeOveAllTimesheetElementTD, employeeTimesheetDailyHour);
                });
                resourceUtil.setCloseCommand(() -> resourceUtil.hide());
            }
        });
    }

    private void applyTimesheetUpdate(final TaskItem taskRUItem, String key_emp_pro_task_id, Integer employeeID, DateNonConvertable dateNonConvertable,
                                      Element totalTimeSheetHoursTD, final int currentTImesheetTime, Element timeSheetHoursTDSpan, final int changedTimesheetTime,
                                      Element totalEmployeeOverAllTimesheetHoursTD, Element employeeOveAllTimesheetElementTD, Integer employeeTimesheetDailyHour) {

        //update current Timesheet SPAN element daily Timesheet time
        String dailyTaskTimesheetTitle = (changedTimesheetTime / 60) + " h " + (changedTimesheetTime % 60) + " min";
        timeSheetHoursTDSpan.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(changedTimesheetTime));
        timeSheetHoursTDSpan.setTitle(dailyTaskTimesheetTitle);
        timeSheetHoursTDSpan.setAttribute("name", changedTimesheetTime + "");

        //update total task Timesheet hours
        if (totalTimeSheetHoursTD != null) {
            int totalTaskTimesheetHoursINT = Integer.valueOf(totalTimeSheetHoursTD.getAttribute("name"));
            int newTotalTimesheet = totalTaskTimesheetHoursINT - currentTImesheetTime + changedTimesheetTime;
            String totalTaskTimesheetTitle = (newTotalTimesheet / 60) + " h " + (newTotalTimesheet % 60) + " min";

            totalTimeSheetHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(newTotalTimesheet));
            totalTimeSheetHoursTD.setTitle(totalTaskTimesheetTitle);
            totalTimeSheetHoursTD.setAttribute("name", newTotalTimesheet + "");
        }

        // update employee total timesheet TD
        if (employeeOveAllTimesheetElementTD != null && employeeTimesheetDailyHour != null) {
            int collEMP = employeeTimesheetDailyHour - currentTImesheetTime + changedTimesheetTime;
            String dailyEmpTimeSpentTitle = (collEMP / 60) + " h " + (collEMP % 60) + " min";
            employeeOveAllTimesheetElementTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(collEMP));
            employeeOveAllTimesheetElementTD.setTitle(dailyEmpTimeSpentTitle);
            employeeOveAllTimesheetElementTD.setAttribute("name", collEMP + "");
        }

        //update employee total overall timesheet TD
        if (totalEmployeeOverAllTimesheetHoursTD != null) {
            int totalEmployeeTimesheetHoursINT = Integer.valueOf(totalEmployeeOverAllTimesheetHoursTD.getAttribute("name"));
            int collTOTAL_EMPLOYEE_TSH = totalEmployeeTimesheetHoursINT - currentTImesheetTime + changedTimesheetTime;
            totalEmployeeOverAllTimesheetHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(collTOTAL_EMPLOYEE_TSH));
            totalEmployeeOverAllTimesheetHoursTD.setAttribute("name", collTOTAL_EMPLOYEE_TSH + "");
        }
    }

    private void addListenerTD(final String key_emp_pro_task_id, final int currentPosition, final Element linkableElement, final Element taskAllocatedHoursSPAN,
                               final Element totalTaskAllocatedHoursTD, final Element totalProjectAllocatedHoursTD, final Element totalEmployeeAllocatedHoursTD,
                               final Integer employeeID, final TaskItem taskRUItem,
                               final DateNonConvertable dateNonConvertable, final int currentEstimatedTime1, final int currentTotalTimeSlotHours,
                               final Element employeeOverAllHoursElementTD, final Integer employeeOverAllDailyHour1,
                               final Element projectOverAllHoursElementSPAN, final Integer projectOverAllDailyHour1) {
        DOM.sinkEvents(linkableElement, Event.ONDBLCLICK);
        DOM.setEventListener(linkableElement, event -> {
            if (DOM.eventGetType(event) == Event.ONDBLCLICK) {
                final Integer taskID = taskRUItem.getTask_id();
                final String task_name = taskRUItem.getTask_name();
                final Date task_start_date = taskRUItem.getTask_start_date();
                final Date task_due_date = taskRUItem.getTask_due_date();

                final int currentEstimatedTime = Integer.valueOf(taskAllocatedHoursSPAN.getAttribute("name"));
                final int employeeOverAllDailyHour = Integer.valueOf(employeeOverAllHoursElementTD.getAttribute("name"));
                final int projectOverAllDailyHour = Integer.valueOf(projectOverAllHoursElementSPAN.getAttribute("name"));
                ResourceUtil resourceUtil = new ResourceUtil(linkableElement, currentEstimatedTime);
                resourceUtil.setChangeDailyTimeListener(changedEstimatedTime -> {
                    //save task hour logic
                    final boolean[] isChangeTaskStartTime = {false};
                    final boolean[] isChangeTaskEndTime = {false};

                    if (DateUtil.compareByDate(task_start_date, dateNonConvertable.getNonConvertedDate(), true)) {//selectedDate <= taskStartDate
                        String doYouWantToChangeStartDateOfTask = coreMessages.doYouWantToChangeStartDateOfTask(task_name);
                        WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.YesNo, doYouWantToChangeStartDateOfTask, new CloseHandler() {
                            @Override
                            public void onCancel() {
                            }

                            @Override
                            public void onSubmit() {
                                isChangeTaskStartTime[0] = true;
                                Date convertableStartDate = DateUtil.addTime(dateNonConvertable.getNonConvertedDate(), task_start_date.getHours(), task_start_date.getMinutes(), task_start_date.getSeconds());
                                addDailyEstimatedTime(taskRUItem, key_emp_pro_task_id, currentPosition, taskAllocatedHoursSPAN,
                                        totalTaskAllocatedHoursTD, totalProjectAllocatedHoursTD, totalEmployeeAllocatedHoursTD,
                                        employeeID, taskID,
                                        task_start_date, task_due_date,
                                        isChangeTaskStartTime, isChangeTaskEndTime,
                                        dateNonConvertable, convertableStartDate,
                                        changedEstimatedTime, currentEstimatedTime, currentTotalTimeSlotHours,
                                        employeeOverAllHoursElementTD, employeeOverAllDailyHour, projectOverAllHoursElementSPAN, projectOverAllDailyHour);
                            }
                        });
                        wfmMessageBox.setTitle(wfmStrings.confirmation());
                        wfmMessageBox.open();

                    } else {
                        if (DateUtil.compareByDate(dateNonConvertable.getNonConvertedDate(), task_due_date, true)) {//taskDueDate <= selectedDate
                            String doYouWantToChangeEndDateOfTask = coreMessages.doYouWantToChangeEndDateOfTask(task_name);
                            WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.YesNo, doYouWantToChangeEndDateOfTask, new CloseHandler() {
                                @Override
                                public void onCancel() {
                                }

                                @Override
                                public void onSubmit() {
                                    isChangeTaskEndTime[0] = true;
                                    Date convertableDueDate = DateUtil.addTime(dateNonConvertable.getNonConvertedDate(), task_due_date.getHours(), task_due_date.getMinutes(), task_due_date.getSeconds());
                                    addDailyEstimatedTime(taskRUItem, key_emp_pro_task_id, currentPosition, taskAllocatedHoursSPAN,
                                            totalTaskAllocatedHoursTD, totalProjectAllocatedHoursTD, totalEmployeeAllocatedHoursTD,
                                            employeeID, taskID,
                                            task_start_date, task_due_date,
                                            isChangeTaskStartTime, isChangeTaskEndTime,

                                            dateNonConvertable, convertableDueDate,
                                            changedEstimatedTime, currentEstimatedTime, currentTotalTimeSlotHours,
                                            employeeOverAllHoursElementTD, employeeOverAllDailyHour, projectOverAllHoursElementSPAN, projectOverAllDailyHour);
                                }
                            });
                            wfmMessageBox.setTitle(wfmStrings.confirmation());
                            wfmMessageBox.open();
                        } else {
                            addDailyEstimatedTime(taskRUItem, key_emp_pro_task_id, currentPosition, taskAllocatedHoursSPAN,
                                    totalTaskAllocatedHoursTD, totalProjectAllocatedHoursTD, totalEmployeeAllocatedHoursTD,
                                    employeeID, taskID,
                                    task_start_date, task_due_date,
                                    isChangeTaskStartTime, isChangeTaskEndTime,
                                    dateNonConvertable, dateNonConvertable.getNonConvertedDate(),
                                    changedEstimatedTime, currentEstimatedTime, currentTotalTimeSlotHours,
                                    employeeOverAllHoursElementTD, employeeOverAllDailyHour, projectOverAllHoursElementSPAN, projectOverAllDailyHour);
                        }
                    }
                });
            }
        });
    }

    private void addDailyEstimatedTime(final TaskItem taskRUItem, String key_emp_pro_task_id, int currentPosition, Element taskAllocatedHoursSPAN,
                                       Element totalTaskAllocatedHoursTD, Element totalProjectAllocatedHoursTD, Element totalEmployeeAllocatedHoursTD,
                                       Integer employeeID, Integer taskID,
                                       final Date task_start_date, final Date task_due_date,
                                       boolean[] changeTaskStartTime, boolean[] changeTaskEndTime, DateNonConvertable dateNonConvertable, final Date convertableDate,
                                       final int changedEstimatedTime, final int currentEstimatedTime, final int currentTotalTimeSlotHours,
                                       Element employeeOverAllHoursElementTD, Integer employeeOverAllDailyHour,
                                       Element projectOverAllHoursElementSPAN, Integer projectOverAllDailyHour) {

        //update current task SPAN element daily estimated time
        String dailyTaskTimeSpentTitle = (changedEstimatedTime / 60) + " h " + (changedEstimatedTime % 60) + " min";
        taskAllocatedHoursSPAN.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(changedEstimatedTime));
        taskAllocatedHoursSPAN.setTitle(dailyTaskTimeSpentTitle);
        taskAllocatedHoursSPAN.setAttribute("name", changedEstimatedTime + "");

        int startDateDATE = task_start_date.getDate();
        int dueDateDATE = task_due_date.getDate();

        startDateDATE = startDateDATE > dueDateDATE ? 1 : startDateDATE;
        int start = startDateDATE, end = dueDateDATE;
        if (currentPosition <= startDateDATE) {
            start = currentPosition;
            end = dueDateDATE;
        } else if (dueDateDATE <= currentPosition) {
            start = startDateDATE;
            end = currentPosition;
        }

        if (changeTaskStartTime[0]) {
            taskRUItem.setTask_start_date(convertableDate);
        } else {
            if (changeTaskEndTime[0]) {
                taskRUItem.setTask_due_date(convertableDate);
            }
        }

        for (int i = start; i <= end; i++) {
            Element taskAllocatedHoursElementTD = empProTaskOVER_ALL_HOURS_TDMap.get(key_emp_pro_task_id).get(i);
            if (taskAllocatedHoursElementTD != null) {
                if (monthHoliday[i] == 1) {//SUNDAY
                    taskAllocatedHoursElementTD.setClassName(STYLE_SUNDAY_MONTH);
                } else {
                    if (monthHoliday[i] == 2) {//DAY OFF
                        taskAllocatedHoursElementTD.setClassName(STYLE_WORK_MONTH_HOLIDAY);
                    } else {
                        if (monthHoliday[i] == 3) {//HOLIDAY
                            taskAllocatedHoursElementTD.setClassName(STYLE_WORK_MONTH_HOLIDAY);
                        } else {//working day
                            taskAllocatedHoursElementTD.setClassName(STYLE_WORKING_DAY_TASK_NAME);
                        }
                    }
                }
            }
        }

        //update current project SPAN element daily overall estimated time
        if (projectOverAllHoursElementSPAN != null && projectOverAllDailyHour != null) {
            int collPRO = projectOverAllDailyHour - currentEstimatedTime + changedEstimatedTime;
            String dailyProTimeSpentTitle = (collPRO / 60) + " h " + (collPRO % 60) + " min";
            projectOverAllHoursElementSPAN.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(collPRO));
            projectOverAllHoursElementSPAN.setTitle(dailyProTimeSpentTitle);
            projectOverAllHoursElementSPAN.setAttribute("name", collPRO + "");
        }
        //update employee SPAN element daily overall estimated time
        if (employeeOverAllHoursElementTD != null && employeeOverAllDailyHour != null) {
            int collEMP = employeeOverAllDailyHour - currentEstimatedTime + changedEstimatedTime;
            String dailyEmpTimeSpentTitle = (collEMP / 60) + " h " + (collEMP % 60) + " min";
            employeeOverAllHoursElementTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(collEMP));
            employeeOverAllHoursElementTD.setTitle(dailyEmpTimeSpentTitle);
            employeeOverAllHoursElementTD.setAttribute("name", collEMP + "");

            if (currentTotalTimeSlotHours == collEMP) {//optimally allocated day
                employeeOverAllHoursElementTD.setClassName(STYLE_DEFAULT_MONTH_OPTIMALLY_ALLOCATED_DAY);
            } else {
                if (currentTotalTimeSlotHours > collEMP) {//under allocated day
                    employeeOverAllHoursElementTD.setClassName(STYLE_DEFAULT_MONTH_UNDER_ALLOCATED_DAY);
                } else {
                    if (currentTotalTimeSlotHours < collEMP) {//over allocated day
                        employeeOverAllHoursElementTD.setClassName(STYLE_DEFAULT_MONTH_OVER_ALLOCATED_DAY);
                    } else {
                        employeeOverAllHoursElementTD.setClassName(STYLE_DEFAULT_MONTH_DAY);
                    }
                }
            }
        }
        //update total task allocated hours
        if (totalTaskAllocatedHoursTD != null) {
            int totalTaskAllocatedHoursINT = Integer.valueOf(totalTaskAllocatedHoursTD.getAttribute("name"));
            int collTOTAL_TASK = totalTaskAllocatedHoursINT - currentEstimatedTime + changedEstimatedTime;
            totalTaskAllocatedHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(collTOTAL_TASK));
            totalTaskAllocatedHoursTD.setAttribute("name", collTOTAL_TASK + "");
        }
        //update total project allocated hours
        if (totalProjectAllocatedHoursTD != null) {
            int totalProjectAllocatedHoursINT = Integer.valueOf(totalProjectAllocatedHoursTD.getAttribute("name"));
            int collTOTAL_PROJECT = totalProjectAllocatedHoursINT - currentEstimatedTime + changedEstimatedTime;
            totalProjectAllocatedHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(collTOTAL_PROJECT));
            totalProjectAllocatedHoursTD.setAttribute("name", collTOTAL_PROJECT + "");
        }
        //update total employee allocated hours
        if (totalEmployeeAllocatedHoursTD != null) {
            int totalEmployeeAllocatedHoursINT = Integer.valueOf(totalEmployeeAllocatedHoursTD.getAttribute("name"));
            int collTOTAL_EMPLOYEE = totalEmployeeAllocatedHoursINT - currentEstimatedTime + changedEstimatedTime;
            totalEmployeeAllocatedHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(collTOTAL_EMPLOYEE));
            totalEmployeeAllocatedHoursTD.setAttribute("name", collTOTAL_EMPLOYEE + "");
        }

        //update daily estimated time
        AllInOneService.App.get().saveResourceUtilDailyEstimatedTime(employeeID, taskID,
                changeTaskStartTime[0], changeTaskEndTime[0], dateNonConvertable, convertableDate, changedEstimatedTime, new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(Throwable throwable) {
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }

                    @Override
                    public void success(Void result) {
                    }
                });
    }

    /**
     * Generate employee projects
     *
     * @param parentElementTBody - parent tBody element
     * @param employeeID         - employee ID
     * @param projectRUItem      - project item
     */
    private void generateEmployeeProjectsReport(Element parentElementTBody, Integer employeeID, ProjectTaskItem projectRUItem, boolean enableExpand) {
        //register employee project name TR element
        Element projectNameElementTR = DOM.createTR();
        String key_employeeID_S = employeeID + "";

        String key_EMP_PRO_ID = employeeID + "_" + projectRUItem.getProject_id();

        parentPROJMap.put(key_EMP_PRO_ID, projectNameElementTR);

        parentChildEMP_PROJMap.computeIfAbsent(parentEMPLMap.get(key_employeeID_S), k -> new LinkedHashSet<>());
        parentChildEMP_PROJMap.get(parentEMPLMap.get(key_employeeID_S)).add(projectNameElementTR);
        projectNameElementTR.addClassName(CLASS_EMPLOYEE_PROJECT_NAME_TR);
        reportTable.addChildToParent(parentElementTBody, projectNameElementTR);

        //register employee project name TH element
        Element projectNameElementTH = DOM.createTH();
        projectNameElementTH.addClassName(CLASS_EMPLOYEE_PROJECT_NAME_TH);
        reportTable.addChildToParent(projectNameElementTR, projectNameElementTH);
        //register 'shown/hidden option employee projects' span element
        Element showHideElementSPAN = getShowHideOPTION_TASK(parentElementTBody, projectNameElementTR, employeeID, projectRUItem.getProject_id(), enableExpand);
        String project_name = projectRUItem.getProject_name();
        if (project_name != null && project_name.length() > 50) {
            showHideElementSPAN.setTitle(project_name);
            project_name = project_name.substring(0, 50) + "...";
        }
        Element projectNameSpan = DOM.createSpan();
        projectNameSpan.setInnerHTML(project_name);
        showHideElementSPAN.appendChild(projectNameSpan);
//        showHideElementSPAN.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
//        showHideElementSPAN.getStyle().setPaddingLeft(20, Style.Unit.PX);
        showHideElementSPAN.addClassName("resUtilzLevel--2");
//        showHideElementSPAN.getStyle().setProperty("display", "inline-table");
        reportTable.addChildToParent(projectNameElementTH, showHideElementSPAN);

        generateEmployeeProjectsReportTD(key_EMP_PRO_ID, projectRUItem, projectNameElementTR);
    }

    /**
     * Generate employee project TD items
     *
     * @param key_EMP_PRO_ID       - key_EMP_PRO_ID
     * @param projectRUItem        - project item
     * @param projectNameElementTR - projectName TR element
     */
    private void generateEmployeeProjectsReportTD(String key_EMP_PRO_ID, ProjectTaskItem projectRUItem, Element projectNameElementTR) {
        proOVER_ALL_HOURS_TD_SPANMap.computeIfAbsent(key_EMP_PRO_ID, k -> new LinkedHashMap<>());
        proOVER_ALL_HOURS_TD_SPAN_OLD_HOURS_Map.computeIfAbsent(key_EMP_PRO_ID, k -> new LinkedHashMap<>());
        int totalProjectAllocatedHourINT = 0;
        Element totalProjectAllocatedHoursTD = DOM.createTD();
        totalProject_OVER_ALL_HOURS_TD_map.computeIfAbsent(key_EMP_PRO_ID, k -> new LinkedHashMap<>());
        for (int i = 1; i <= currentMonth; i++) {
            int thisDay = monthHoliday[i];
            //register daily employee project total hours
            Element projectAllocatedHoursTD = DOM.createTD();
            Element projectAllocatedHoursSPAN = getSpanTTElement();
            projectAllocatedHoursSPAN.getStyle().setFontSize(11, Style.Unit.PX);
            reportTable.addChildToParent(projectAllocatedHoursTD, projectAllocatedHoursSPAN);

            if (proOVER_ALL_HOURS_TD_SPANMap.get(key_EMP_PRO_ID) != null) {
                proOVER_ALL_HOURS_TD_SPANMap.get(key_EMP_PRO_ID).putIfAbsent(i, projectAllocatedHoursSPAN);
            }
            if (totalProject_OVER_ALL_HOURS_TD_map.get(key_EMP_PRO_ID) != null) {
                totalProject_OVER_ALL_HOURS_TD_map.get(key_EMP_PRO_ID).putIfAbsent(projectAllocatedHoursSPAN, totalProjectAllocatedHoursTD);
            }
            if (proOVER_ALL_HOURS_TD_SPAN_OLD_HOURS_Map.get(key_EMP_PRO_ID) != null) {
                proOVER_ALL_HOURS_TD_SPAN_OLD_HOURS_Map.get(key_EMP_PRO_ID).computeIfAbsent(i, i1 -> projectRUItem.getTotalEstimatedTime()[i1]);
            }

            if (monthHoliday[i] == 1) {//SUNDAY
                if (projectRUItem.getTotalEstimatedTime()[i] == 0) {
                    projectAllocatedHoursTD.addClassName(STYLE_SUNDAY_MONTH);
                    projectAllocatedHoursSPAN.setInnerHTML("DO");
                } else {
                    projectAllocatedHoursTD.addClassName(STYLE_SUNDAY_RESOURCE_MONTH);
                    totalProjectAllocatedHourINT += projectRUItem.getTotalEstimatedTime()[i];
                    String dailyTimeSpentTitle = (projectRUItem.getTotalEstimatedTime()[i] / 60) + " h " + (projectRUItem.getTotalEstimatedTime()[i] % 60) + " min";
                    projectAllocatedHoursSPAN.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(projectRUItem.getTotalEstimatedTime()[i]));
                    projectAllocatedHoursSPAN.setTitle(dailyTimeSpentTitle);
                }
            } else if (monthHoliday[i] == 2) {//DAY OFF
                if (projectRUItem.getTotalEstimatedTime()[i] == 0 && projectRUItem.getWith_LR_INT()[i] == 0) {
                    projectAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    projectAllocatedHoursSPAN.setInnerHTML("&nbsp;");
                } else if (projectRUItem.getTotalEstimatedTime()[i] == 0 && projectRUItem.getWith_LR_INT()[i] != 0) {
                    projectAllocatedHoursTD.addClassName(STYLE_WITH_LR_DAY);
                    projectAllocatedHoursSPAN.setInnerHTML("");
                } else {
                    projectAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    totalProjectAllocatedHourINT += projectRUItem.getTotalEstimatedTime()[i];
                    String dailyTimeSpentTitle = (projectRUItem.getTotalEstimatedTime()[i] / 60) + " h " + (projectRUItem.getTotalEstimatedTime()[i] % 60) + " min";
                    projectAllocatedHoursSPAN.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(projectRUItem.getTotalEstimatedTime()[i]));
                    projectAllocatedHoursSPAN.setTitle(dailyTimeSpentTitle);
                }
            } else if (monthHoliday[i] == 3) {//HOLIDAY
                if (projectRUItem.getTotalEstimatedTime()[i] == 0) {
                    projectAllocatedHoursTD.addClassName(STYLE_WORK_COMPANY_HOLIDAY);
                    projectAllocatedHoursSPAN.setInnerHTML("H");
                } else {
                    projectAllocatedHoursTD.addClassName(STYLE_WORK_COMPANY_RESOURCE_HOLIDAY);
                    totalProjectAllocatedHourINT += projectRUItem.getTotalEstimatedTime()[i];
                    String dailyTimeSpentTitle = (projectRUItem.getTotalEstimatedTime()[i] / 60) + " h " + (projectRUItem.getTotalEstimatedTime()[i] % 60) + " min";
                    projectAllocatedHoursSPAN.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(projectRUItem.getTotalEstimatedTime()[i]));
                    projectAllocatedHoursSPAN.setTitle(dailyTimeSpentTitle);
                }
            } else {
                if (projectRUItem.getTotalEstimatedTime()[i] == 0) {
                    projectAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY_PRO_NAME);
                    projectAllocatedHoursSPAN.setInnerHTML("&nbsp;");
                } else {
                    projectAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY_PRO_NAME);
                    totalProjectAllocatedHourINT += projectRUItem.getTotalEstimatedTime()[i];
                    String dailyTimeSpentTitle = (projectRUItem.getTotalEstimatedTime()[i] / 60) + " h " + (projectRUItem.getTotalEstimatedTime()[i] % 60) + " min";
                    projectAllocatedHoursSPAN.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(projectRUItem.getTotalEstimatedTime()[i]));
                    projectAllocatedHoursSPAN.setTitle(dailyTimeSpentTitle);
                }
            }
            reportTable.addChildToParent(projectNameElementTR, projectAllocatedHoursTD);
        }

        //total project allocated hours TD element
        totalProjectAllocatedHoursTD.addClassName(STYLE_TOTAL_MIDDLE_DAY);
		totalProjectAllocatedHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(totalProjectAllocatedHourINT));
		if (totalProjectAllocatedHourINT > 0) {
			String totalProjectAllocatedHourINTTitle = (totalProjectAllocatedHourINT / 60) + " h " + (totalProjectAllocatedHourINT % 60) + " min";
			totalProjectAllocatedHoursTD.setTitle(totalProjectAllocatedHourINTTitle);
		}
		totalProjectAllocatedHoursTD.setAttribute("name", totalProjectAllocatedHourINT + "");
        reportTable.addChildToParent(projectNameElementTR, totalProjectAllocatedHoursTD);
    }

    /**
     * Generate project tasks
     *
     * @param parentElementTBody - parent tBody element
     * @param employeeID         - employee ID
     * @param projectID          - project ID
     * @param taskRUItem         - task item
     */
    private void generateEmployeeTasksReport(Element parentElementTBody, Integer employeeID, Integer projectID, TaskItem taskRUItem) {
        //register employee task name TR element
        Element taskNameElementTR = DOM.createTR();
        String key_EMP_ID = employeeID + "";
        String key_EMP_PRO_ID = employeeID + "_" + projectID;
        parentChildPROJ_TASKMap.computeIfAbsent(parentPROJMap.get(key_EMP_PRO_ID), k -> new LinkedHashSet<>());
        parentChildPROJ_TASKMap.get(parentPROJMap.get(key_EMP_PRO_ID)).add(taskNameElementTR);
        taskNameElementTR.addClassName(CLASS_EMPLOYEE_TASK_NAME_TR);
        reportTable.insertChildToParent(parentElementTBody, taskNameElementTR, DOM.getChildIndex(parentElementTBody, parentPROJMap.get(key_EMP_PRO_ID)) + 1);

        //register employee task name TH element
        Element taskNameElementTH = DOM.createTH();
        taskNameElementTH.addClassName(CLASS_EMPLOYEE_TASK_NAME_TH);
        reportTable.addChildToParent(taskNameElementTR, taskNameElementTH);

// Register 'shown/hidden option employee tasks' span element
        Element taskNameElementSPAN = DOM.createAnchor();
        String task_name = taskRUItem.getTask_name();
        taskNameElementSPAN.addClassName("resUtilzLevel--3");
        addListenerToTaskName(taskNameElementSPAN, taskRUItem.getTask_id(), taskRUItem.isIssue());
        reportTable.addChildToParent(taskNameElementTH, taskNameElementSPAN);

        Element innerSpan = DOM.createSpan();
        innerSpan.setInnerHTML(task_name);
        taskNameElementSPAN.appendChild(innerSpan);

        //register employee task time sheet hours TR element
        Element taskTimeSheetHoursTR = DOM.createTR();
        parentChildEMP_TSHETHoursMap.computeIfAbsent(parentPROJMap.get(key_EMP_PRO_ID), k -> new LinkedHashSet<>());
        parentChildEMP_TSHETHoursMap.get(parentPROJMap.get(key_EMP_PRO_ID)).add(taskTimeSheetHoursTR);
        taskTimeSheetHoursTR.addClassName(CLASS_TIME_SHEET_HOURS_TR);
        if (reportTable.getResourceUtilizationView().getTimeSheetHours().getValue()) {
            taskTimeSheetHoursTR.addClassName(CLASS_TIME_SHEET_HOURS_TR_V);
        }
        reportTable.insertChildToParent(parentElementTBody, taskTimeSheetHoursTR, DOM.getChildIndex(parentElementTBody, parentPROJMap.get(key_EMP_PRO_ID)) + 2);

        //register employee task time sheet hours TH element
        Element taskTimeSheetHoursTH = DOM.createTH();
        taskTimeSheetHoursTH.addClassName(CLASS_TIME_SHEET_HOURS_TH);
        reportTable.addChildToParent(taskTimeSheetHoursTR, taskTimeSheetHoursTH);

        Element taskTimeSheetHoursSPAN = getSpanTTElement();
        taskTimeSheetHoursSPAN.setInnerHTML(wfmStrings.timesheetHours());
        reportTable.addChildToParent(taskTimeSheetHoursTH, taskTimeSheetHoursSPAN);

        generateEmployeeTasksReportTD(key_EMP_ID, key_EMP_PRO_ID, employeeID, taskRUItem, taskNameElementTR, taskTimeSheetHoursTR);
    }

    /**
     * Generate employee project task TD items
     *
     * @param key_EMP_ID           - key employee id
     * @param key_EMP_PRO_ID       - key employee vs project id
     * @param taskRUItem           - task item
     * @param taskNameElementTR    - task name TR element
     * @param taskTimeSheetHoursTR - task time sheet hours TR element
     */
    private void generateEmployeeTasksReportTD(String key_EMP_ID, String key_EMP_PRO_ID,
                                               Integer employeeID, TaskItem taskRUItem,
                                               Element taskNameElementTR, Element taskTimeSheetHoursTR) {

        String key_emp_pro_task_id = key_EMP_PRO_ID + "_" + taskRUItem.getTask_id();
        empProTaskOVER_ALL_HOURS_TDMap.computeIfAbsent(key_emp_pro_task_id, k -> new LinkedHashMap<>());
        int totalTimeSheetHoursINT = 0;
        int totalTaskAllocatedHoursINT = 0;
        Element totalTimeSheetHoursTD = DOM.createTD();
        Element totalTaskAllocatedHoursTD = DOM.createTD();

        for (int i = 1; i <= currentMonth; i++) {
            //register daily employee task timeSheet hours
            Element taskTimeSheetHoursTD = DOM.createTD();
            Element timeSheetHoursTDSpan = getSpanTTElement();
            reportTable.addChildToParent(taskTimeSheetHoursTD, timeSheetHoursTDSpan);

            //register daily employee task total hours
            Element taskAllocatedHoursTD = DOM.createTD();
            Element taskAllocatedHoursSPAN = getSpanTTElement();
            reportTable.addChildToParent(taskAllocatedHoursTD, taskAllocatedHoursSPAN);

            if (empProTaskOVER_ALL_HOURS_TDMap.get(key_emp_pro_task_id) != null) {
                empProTaskOVER_ALL_HOURS_TDMap.get(key_emp_pro_task_id).putIfAbsent(i, taskAllocatedHoursTD);
            }

            if (monthHoliday[i] == 1) {//SUNDAY
                //task time sheet hours--------------------------
                if (taskRUItem.getTotalTimeSheetHours()[i] == 0) {
                    timeSheetHoursTDSpan.addClassName(STYLE_SUNDAY_MONTH);
                } else {
                    timeSheetHoursTDSpan.addClassName(STYLE_SUNDAY_RESOURCE_MONTH);
                    totalTimeSheetHoursINT += taskRUItem.getTotalTimeSheetHours()[i];
                    String dailyTimeSheetTitle = (taskRUItem.getTotalTimeSheetHours()[i] / 60) + " h " + (taskRUItem.getTotalTimeSheetHours()[i] % 60) + " min";
                    taskTimeSheetHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(taskRUItem.getTotalTimeSheetHours()[i]));
                    timeSheetHoursTDSpan.setTitle(dailyTimeSheetTitle);
                }
                timeSheetHoursTDSpan.setAttribute("name", taskRUItem.getTotalTimeSheetHours()[i] + "");
                //task time spent hours----------------------------
                if (taskRUItem.getTotalEstimatedTime()[i] == 0) {
                    taskAllocatedHoursSPAN.setInnerHTML("&nbsp;");
                } else {
                    totalTaskAllocatedHoursINT += taskRUItem.getTotalEstimatedTime()[i];
                    String dailyTimeSpentTitle = (taskRUItem.getTotalEstimatedTime()[i] / 60) + " h " + (taskRUItem.getTotalEstimatedTime()[i] % 60) + " min";
                    taskAllocatedHoursSPAN.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(taskRUItem.getTotalEstimatedTime()[i]));
                    taskAllocatedHoursSPAN.setTitle(dailyTimeSpentTitle);
                }
                taskAllocatedHoursSPAN.setAttribute("name", taskRUItem.getTotalEstimatedTime()[i] + "");
                if (taskRUItem.getWorkingDay()[i] ||
                        (DateUtil.compareByDate(taskRUItem.getDailyDate()[i].getNonConvertedDate(), taskRUItem.getTask_start_date()) &&
                                DateUtil.compareByDate(taskRUItem.getTask_due_date(), taskRUItem.getDailyDate()[i].getNonConvertedDate()))) {
                    if (taskRUItem.getTotalEstimatedTime()[i] == 0) {
                        taskAllocatedHoursTD.addClassName(STYLE_SUNDAY_MONTH);
                        taskAllocatedHoursTD.setInnerHTML("<span style=color: black;>DO</span>");
                    } else {
                        taskAllocatedHoursTD.addClassName(STYLE_SUNDAY_RESOURCE_MONTH);
                    }

                    if (taskRUItem.getTotalTimeSheetHours()[i] == 0) {
                        taskTimeSheetHoursTD.addClassName(STYLE_SUNDAY_MONTH);
                        taskTimeSheetHoursTD.setInnerHTML("<span style=color: black;>DO</span>");
                    } else {
                        taskTimeSheetHoursTD.addClassName(STYLE_SUNDAY_RESOURCE_MONTH);
                    }
                } else {
                    taskTimeSheetHoursTD.addClassName(STYLE_SUNDAY_RESOURCE_MONTH);
                    taskAllocatedHoursTD.addClassName(STYLE_WORK_MONTH_RESOURCE_HOLIDAY);
                }
                //-------------------------------------------------
            } else if (monthHoliday[i] == 2) {//DAY OFF
                //task time sheet hours--------------------------
                if (taskRUItem.getTotalTimeSheetHours()[i] == 0) {
                    timeSheetHoursTDSpan.addClassName(STYLE_WORK_MONTH_HOLIDAY);
                    timeSheetHoursTDSpan.setInnerHTML("&nbsp;");
                } else {
                    timeSheetHoursTDSpan.addClassName(STYLE_WORK_MONTH_RESOURCE_HOLIDAY);
                    totalTimeSheetHoursINT += taskRUItem.getTotalTimeSheetHours()[i];
                    String dailyTimeSheetTitle = (taskRUItem.getTotalTimeSheetHours()[i] / 60) + " h " + (taskRUItem.getTotalTimeSheetHours()[i] % 60) + " min";
                    timeSheetHoursTDSpan.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(taskRUItem.getTotalTimeSheetHours()[i]));
                    timeSheetHoursTDSpan.setTitle(dailyTimeSheetTitle);
                }
                timeSheetHoursTDSpan.setAttribute("name", taskRUItem.getTotalTimeSheetHours()[i] + "");
                //task time spent hours----------------------------
                if (taskRUItem.getTotalEstimatedTime()[i] == 0) {
                    taskAllocatedHoursSPAN.setInnerHTML("&nbsp;");
                } else {
                    totalTaskAllocatedHoursINT += taskRUItem.getTotalEstimatedTime()[i];
                    String dailyTimeSpentTitle = (taskRUItem.getTotalEstimatedTime()[i] / 60) + " h " + (taskRUItem.getTotalEstimatedTime()[i] % 60) + " min";
                    taskAllocatedHoursSPAN.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(taskRUItem.getTotalEstimatedTime()[i]));
                    taskAllocatedHoursSPAN.setTitle(dailyTimeSpentTitle);
                }
                taskAllocatedHoursSPAN.setAttribute("name", taskRUItem.getTotalEstimatedTime()[i] + "");
                if (taskRUItem.getWorkingDay()[i] ||
                        (DateUtil.compareByDate(taskRUItem.getDailyDate()[i].getNonConvertedDate(), taskRUItem.getTask_start_date()) &&
                                DateUtil.compareByDate(taskRUItem.getTask_due_date(), taskRUItem.getDailyDate()[i].getNonConvertedDate()))) {
                    if (taskRUItem.getTotalEstimatedTime()[i] == 0 && taskRUItem.getWith_LR_INT()[i] == 0) {
                        taskAllocatedHoursTD.addClassName(STYLE_WORKING_DAY_TASK_NAME);
                    } else if (taskRUItem.getTotalEstimatedTime()[i] == 0 && taskRUItem.getWith_LR_INT()[i] != 0) {
                        taskAllocatedHoursTD.addClassName(STYLE_WITH_LR_DAY);
                        taskAllocatedHoursSPAN.setInnerHTML("");
                    } else {
                        taskAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    }

                    if (taskRUItem.getTotalTimeSheetHours()[i] == 0 && !taskRUItem.getWorkingDay()[i]) {
                        taskTimeSheetHoursTD.addClassName(STYLE_SUNDAY_MONTH);
                    } else {
                        taskTimeSheetHoursTD.addClassName(STYLE_SUNDAY_RESOURCE_MONTH);
                    }
                } else if (taskRUItem.getTotalEstimatedTime()[i] == 0 && taskRUItem.getWorkingDay()[i]) {
                    taskTimeSheetHoursTD.addClassName(STYLE_SUNDAY_RESOURCE_MONTH);
                    taskAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                } else {
                    taskTimeSheetHoursTD.addClassName(STYLE_SUNDAY_RESOURCE_MONTH);
                    taskAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                }
                //-------------------------------------------------
            } else if (monthHoliday[i] == 3) {//HOLIDAY
                //task time sheet hours--------------------------
                if (taskRUItem.getTotalTimeSheetHours()[i] == 0) {
                    timeSheetHoursTDSpan.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    timeSheetHoursTDSpan.setInnerHTML("&nbsp;");
                } else {
                    timeSheetHoursTDSpan.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    totalTimeSheetHoursINT += taskRUItem.getTotalTimeSheetHours()[i];
                    String dailyTimeSheetTitle = (taskRUItem.getTotalTimeSheetHours()[i] / 60) + " h " + (taskRUItem.getTotalTimeSheetHours()[i] % 60) + " min";
                    timeSheetHoursTDSpan.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(taskRUItem.getTotalTimeSheetHours()[i]));
                    timeSheetHoursTDSpan.setTitle(dailyTimeSheetTitle);
                }
                timeSheetHoursTDSpan.setAttribute("name", taskRUItem.getTotalTimeSheetHours()[i] + "");
                //task time spent hours----------------------------
                if (taskRUItem.getTotalEstimatedTime()[i] == 0) {
                    taskAllocatedHoursSPAN.setInnerHTML("&nbsp;");
                } else {
                    totalTaskAllocatedHoursINT += taskRUItem.getTotalEstimatedTime()[i];
                    String dailyTimeSpentTitle = (taskRUItem.getTotalEstimatedTime()[i] / 60) + " h " + (taskRUItem.getTotalEstimatedTime()[i] % 60) + " min";
                    taskAllocatedHoursSPAN.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(taskRUItem.getTotalEstimatedTime()[i]));
                    taskAllocatedHoursSPAN.setTitle(dailyTimeSpentTitle);
                }
                taskAllocatedHoursSPAN.setAttribute("name", taskRUItem.getTotalEstimatedTime()[i] + "");
                if (taskRUItem.getWorkingDay()[i] ||
                        (DateUtil.compareByDate(taskRUItem.getDailyDate()[i].getNonConvertedDate(), taskRUItem.getTask_start_date()) &&
                                DateUtil.compareByDate(taskRUItem.getTask_due_date(), taskRUItem.getDailyDate()[i].getNonConvertedDate()))) {
                    taskAllocatedHoursTD.addClassName(STYLE_WORKING_DAY_TASK_NAME);

                    if (taskRUItem.getTotalTimeSheetHours()[i] == 0) {
                        taskTimeSheetHoursTD.addClassName(STYLE_SUNDAY_MONTH);
                    } else {
                        taskTimeSheetHoursTD.addClassName(STYLE_SUNDAY_RESOURCE_MONTH);
                    }
                } else if (taskRUItem.getWithHoliday_INT()[i] == 1) {
                    taskTimeSheetHoursTD.addClassName(STYLE_WORK_COMPANY_HOLIDAY);
                    taskAllocatedHoursTD.addClassName(STYLE_WORK_COMPANY_HOLIDAY);
                    taskAllocatedHoursSPAN.setInnerHTML("H");
                } else {
                    taskTimeSheetHoursTD.addClassName(STYLE_SUNDAY_RESOURCE_MONTH);
                    taskAllocatedHoursTD.addClassName(STYLE_WORK_COMPANY_HOLIDAY);
                }
                //-------------------------------------------------
            } else {
                if (taskRUItem.getTotalTimeSlotHours()[i] == 0) {
                    taskAllocatedHoursTD.addClassName(STYLE_SUNDAY_MONTH);
                    taskTimeSheetHoursTD.addClassName(STYLE_SUNDAY_MONTH);
                } else {
                    taskTimeSheetHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                    taskAllocatedHoursTD.addClassName(STYLE_DEFAULT_MONTH_DAY);
                }
                //task time sheet hours----------------------------
                if (taskRUItem.getTotalTimeSheetHours()[i] != 0) {
                    totalTimeSheetHoursINT += taskRUItem.getTotalTimeSheetHours()[i];
                    String dailyTimeSheetTitle = (taskRUItem.getTotalTimeSheetHours()[i] / 60) + " h " + (taskRUItem.getTotalTimeSheetHours()[i] % 60) + " min";
                    timeSheetHoursTDSpan.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(taskRUItem.getTotalTimeSheetHours()[i]));
                    timeSheetHoursTDSpan.setTitle(dailyTimeSheetTitle);
                }
                timeSheetHoursTDSpan.setAttribute("name", taskRUItem.getTotalTimeSheetHours()[i] + "");

                //task time spent hours----------------------------
                if (taskRUItem.getTotalEstimatedTime()[i] != 0) {
                    totalTaskAllocatedHoursINT += taskRUItem.getTotalEstimatedTime()[i];
                    String dailyTimeSpentTitle = (taskRUItem.getTotalEstimatedTime()[i] / 60) + " h " + (taskRUItem.getTotalEstimatedTime()[i] % 60) + " min";
                    taskAllocatedHoursSPAN.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(taskRUItem.getTotalEstimatedTime()[i]));
                    taskAllocatedHoursSPAN.setTitle(dailyTimeSpentTitle);
                }
                taskAllocatedHoursSPAN.setAttribute("name", taskRUItem.getTotalEstimatedTime()[i] + "");
                if (taskRUItem.getWorkingDay()[i] ||
                        (DateUtil.compareByDate(taskRUItem.getDailyDate()[i].getNonConvertedDate(), taskRUItem.getTask_start_date()) &&
                                DateUtil.compareByDate(taskRUItem.getTask_due_date(), taskRUItem.getDailyDate()[i].getNonConvertedDate()))) {
                    taskAllocatedHoursTD.addClassName(STYLE_WORKING_DAY_TASK_NAME);
                }
                //-------------------------------------------------
            }

            //register editable LISTENER
            Element employeeOverAllHoursElementTD = empOVER_ALL_HOURS_TDMap.get(key_EMP_ID).get(i);
            Integer employeeOverAllDailyHour = empOVER_ALL_HOURS_TD_OLD_HOURS_Map.get(key_EMP_ID).get(i);
            employeeOverAllHoursElementTD.setAttribute("name", employeeOverAllDailyHour + "");

            Element employeeOveAllTimesheetElementTD = empTIMESHEET_TDMap.get(key_EMP_ID).get(i);
            Integer employeeTimesheetDailyHour = empTIMESHEET_HOURS_TD_OLD_HOURS_Map.get(key_EMP_ID).get(i);
            employeeOveAllTimesheetElementTD.setAttribute("name", employeeTimesheetDailyHour + "");

            Element projectOverAllHoursElementSPAN = proOVER_ALL_HOURS_TD_SPANMap.get(key_EMP_PRO_ID).get(i);
            Integer projectOverAllDailyHour = proOVER_ALL_HOURS_TD_SPAN_OLD_HOURS_Map.get(key_EMP_PRO_ID).get(i);
            projectOverAllHoursElementSPAN.setAttribute("name", projectOverAllDailyHour + "");

            Element totalProjectAllocatedHoursTD = totalProject_OVER_ALL_HOURS_TD_map.get(key_EMP_PRO_ID).get(projectOverAllHoursElementSPAN);
            Element totalEmployeeAllocatedHoursTD = totalEmployee_OVER_ALL_HOURS_TD_map.get(key_EMP_ID).get(employeeOverAllHoursElementTD);
            Element totalEmployeeOverAllTimesheetHoursTD = totalEmployee_TIMESHEET_HOURS_TD_map.get(key_EMP_ID).get(employeeOveAllTimesheetElementTD);

            //employee task timesheet td is fillable
            if (Utils.hasRole(Constants.TIMESHEET_EDITOR)) {
                addListenerToTimesheetTD(key_emp_pro_task_id, taskRUItem, employeeID, taskTimeSheetHoursTD, taskRUItem.getDailyDate()[i],
                        totalTimeSheetHoursTD, timeSheetHoursTDSpan, totalEmployeeOverAllTimesheetHoursTD, employeeOveAllTimesheetElementTD);
            }

            //estimated hours fillable
            if (taskRUItem.isEditable()) {//editable
                int currentEstimatedTime = Integer.valueOf(taskAllocatedHoursSPAN.getAttribute("name"));

                addListenerTD(key_emp_pro_task_id, i, taskAllocatedHoursTD, taskAllocatedHoursSPAN,
                        totalTaskAllocatedHoursTD, totalProjectAllocatedHoursTD, totalEmployeeAllocatedHoursTD,
                        employeeID, taskRUItem,
                        taskRUItem.getDailyDate()[i], currentEstimatedTime, taskRUItem.getTotalTimeSlotHours()[i],
                        employeeOverAllHoursElementTD, employeeOverAllDailyHour,
                        projectOverAllHoursElementSPAN, projectOverAllDailyHour);
            }

            reportTable.addChildToParent(taskTimeSheetHoursTR, taskTimeSheetHoursTD);
            reportTable.addChildToParent(taskNameElementTR, taskAllocatedHoursTD);
        }
        //total task timeSheet hours TD element
        totalTimeSheetHoursTD.addClassName(STYLE_TOTAL_MIDDLE_DAY);
        totalTimeSheetHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(totalTimeSheetHoursINT));
        if (totalTimeSheetHoursINT > 0) {
            String totalTimeSheetHoursINTTitle = (totalTimeSheetHoursINT / 60) + " h " + (totalTimeSheetHoursINT % 60) + " min";
            totalTimeSheetHoursTD.setTitle(totalTimeSheetHoursINTTitle);
        }
        totalTimeSheetHoursTD.setAttribute("name", totalTimeSheetHoursINT + "");
        reportTable.addChildToParent(taskTimeSheetHoursTR, totalTimeSheetHoursTD);
        //total task allocated hours TD element
        totalTaskAllocatedHoursTD.addClassName(STYLE_TOTAL_MIDDLE_DAY);
        totalTaskAllocatedHoursTD.setInnerHTML(getTotalHourMinuteWithTimeFORMAT(totalTaskAllocatedHoursINT));
        if (totalTaskAllocatedHoursINT > 0) {
            String totalTaskAllocatedHoursINTTitle = (totalTaskAllocatedHoursINT / 60) + " h " + (totalTaskAllocatedHoursINT % 60) + " min";
            totalTaskAllocatedHoursTD.setTitle(totalTaskAllocatedHoursINTTitle);
        }
        totalTaskAllocatedHoursTD.setAttribute("name", totalTaskAllocatedHoursINT + "");
        reportTable.addChildToParent(taskNameElementTR, totalTaskAllocatedHoursTD);
    }

    private void getEmployeeProjects(Element parentTBodyElement, Integer employeeID, int dayMonth, boolean enableExpand) {
        String startDateS = DateUtils.getDateAndTimeFormatWithDash(reportTable.getMonthDay().getStartDate());
        String endDateS = DateUtils.getDateAndTimeFormatWithDash(reportTable.getMonthDay().getEndDate());
        LoadingPanel.loading(true);
        Integer departmentID = reportTable.getResourceUtilizationView().getDepartmentID();
        Integer projectID = reportTable.getResourceUtilizationView().getProjectID();
        boolean showOnlyFilledCells = reportTable.getResourceUtilizationView().isShowOnlyFilledCells();
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setProjectId(projectID);
        filterParameter.setDepartmentId(departmentID);
        filterParameter.setEmployeeId(employeeID);
        filterParameter.setDay(dayMonth);
        filterParameter.setShowFilledCells(showOnlyFilledCells);
        loadEmployeeProjects(parentTBodyElement, 0, startDateS, endDateS, filterParameter, enableExpand);
    }

    private void loadEmployeeProjects(final Element parentTBodyElement, final int start, final String startDate, final String endDate, final ListingFilterParameter filterParameter, final boolean enableExpand) {
        AllInOneService.App.get().getEmployeeProjectsResourceUtil(start, startDate, endDate, filterParameter, new AbstractAsyncCallback<ProjectTaskItem[]>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(ProjectTaskItem[] result) {
                LoadingPanel.loading(false);
                for (ProjectTaskItem projectTaskItem : result) {
                    generateEmployeeProjectsReport(parentTBodyElement, filterParameter.getEmployeeId(), projectTaskItem, enableExpand);
                }
                if (result.length == 0) {
                    String key_employeeID_S = filterParameter.getEmployeeId() + "";
                    parentChildEMP_PROJMap.computeIfAbsent(parentEMPLMap.get(key_employeeID_S), k -> new LinkedHashSet<>());
                }

                if (result.length == PAGE_SIZE) {
                    loadEmployeeProjects(parentTBodyElement, start + PAGE_SIZE, startDate, endDate, filterParameter, enableExpand);
                }
            }
        });
    }

    private void getEmployeeProjectTasks(final Element parentTBodyElement, ListingFilterParameter filterParameter) {
        String startDateS = DateUtils.getDateAndTimeFormatWithDash(reportTable.getMonthDay().getStartDate());
        String endDateS = DateUtils.getDateAndTimeFormatWithDash(reportTable.getMonthDay().getEndDate());
        LoadingPanel.loading(true);
        Integer departmentID = reportTable.getResourceUtilizationView().getDepartmentID();
        filterParameter.setDepartmentId(departmentID);
        loadEmployeeProjectTasks(parentTBodyElement, startDateS, endDateS, 0, filterParameter);
    }

    private void loadEmployeeProjectTasks(final Element parentTBodyElement, final String startDate, final String endDate, final int start, final ListingFilterParameter filterParameter) {
        AllInOneService.App.get().getEmployeeProjectTasksResourceUtil(startDate, endDate, start, filterParameter, new AbstractAsyncCallback<TaskItem[]>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(TaskItem[] result) {
                LoadingPanel.loading(false);
                for (TaskItem taskItem : result) {
                    generateEmployeeTasksReport(parentTBodyElement, filterParameter.getEmployeeId(), filterParameter.getProjectId(), taskItem);
                }
                if (result.length == 0) {
                    String key_EMP_PRO_ID = filterParameter.getEmployeeId() + "_" + filterParameter.getProjectId();
                    parentChildPROJ_TASKMap.computeIfAbsent(parentPROJMap.get(key_EMP_PRO_ID), k -> new LinkedHashSet<>());
                }

                if (result.length == PAGE_SIZE) {
                    loadEmployeeProjectTasks(parentTBodyElement, startDate, endDate, start + PAGE_SIZE, filterParameter);
                }
            }
        });
    }

    private Element getShowHideOPTION_PROJECT(final Element parentTBodyElement, final Element parentEmployeeNameTRElement, final Integer employeeID, final boolean enableExpand) {
        parentEmployeeNameTRElement.addClassName(CLASS_COLLAPSED_EMPLOYEE_NAME);
        final Element spanElement = DOM.createSpan();
        final boolean[] isShowE = {true};
        if (enableExpand) {
            generateEmployeeProjects(parentTBodyElement, parentEmployeeNameTRElement, employeeID, spanElement, isShowE, enableExpand);
        }
        DOM.sinkEvents(spanElement, Event.ONCLICK);
        DOM.setEventListener(spanElement, event -> {
            if (DOM.eventGetType(event) == Event.ONCLICK) {
                generateEmployeeProjects(parentTBodyElement, parentEmployeeNameTRElement, employeeID, spanElement, isShowE, enableExpand);
            }
        });

        return spanElement;
    }

    private void generateEmployeeProjects(Element parentTBodyElement, Element parentEmployeeNameTRElement, Integer employeeID, Element spanElement, boolean[] isShowE, boolean enableExpand) {
        String key_employeeID_S = employeeID + "";
        if (isShowE[0]) {
            if (parentEmployeeNameTRElement.getClassName().contains(CLASS_COLLAPSED_EMPLOYEE_NAME)) {
                parentEmployeeNameTRElement.removeClassName(CLASS_COLLAPSED_EMPLOYEE_NAME);
            }
            if (!parentEmployeeNameTRElement.getClassName().contains(CLASS_EXPANDED_EMPLOYEE_NAME)) {
                parentEmployeeNameTRElement.addClassName(CLASS_EXPANDED_EMPLOYEE_NAME);
            }
        } else {
            if (parentEmployeeNameTRElement.getClassName().contains(CLASS_EXPANDED_EMPLOYEE_NAME)) {
                parentEmployeeNameTRElement.removeClassName(CLASS_EXPANDED_EMPLOYEE_NAME);
            }
            if (!parentEmployeeNameTRElement.getClassName().contains(CLASS_COLLAPSED_EMPLOYEE_NAME)) {
                parentEmployeeNameTRElement.addClassName(CLASS_COLLAPSED_EMPLOYEE_NAME);
            }
            //child projects
            Set<Element> elementsPROJ = parentChildEMP_PROJMap.get(parentEMPLMap.get(key_employeeID_S));
            if (elementsPROJ != null && elementsPROJ.size() > 0) {
                for (Element childPROJ : elementsPROJ) {
                    if (childPROJ.getClassName().contains(CLASS_EXPANDED_PROJECT_NAME)) {
                        childPROJ.removeClassName(CLASS_EXPANDED_PROJECT_NAME);
                    }
                    if (!childPROJ.getClassName().contains(CLASS_COLLAPSED_PROJECT_NAME)) {
                        childPROJ.addClassName(CLASS_COLLAPSED_PROJECT_NAME);
                    }
                }
            }
        }

        if (reportTable.getResourceUtilizationView().getTimeSlotHours().getValue()) {
            showHideTimeSlotHours(isShowE[0], key_employeeID_S);
        }
        if (reportTable.getResourceUtilizationView().getInOutHours().getValue()) {
            showHideInOutHours(isShowE[0], key_employeeID_S);
        }
        if (reportTable.getResourceUtilizationView().getTimeSheetHours().getValue()) {
            showHideOverallTimeSheetHours(isShowE[0], key_employeeID_S);
            showHideTimeSheetHours(isShowE[0], key_employeeID_S, null);
        }
        if (reportTable.getResourceUtilizationView().getLeaveRequestHours().getValue()) {
            showHideOverallLeaveRequestHours(isShowE[0], key_employeeID_S);
        }
        if (isShowE[0]) {//collapsed elements
            isShowE[0] = false;
            if (!spanElement.getClassName().contains(CLASS_EXPANDED_ELEMENT)) {
                spanElement.addClassName(CLASS_EXPANDED_ELEMENT);
            }
            Set<Element> elementsPROJ = parentChildEMP_PROJMap.get(parentEMPLMap.get(key_employeeID_S));
            if (elementsPROJ != null && elementsPROJ.size() > 0) {
                for (Element childPROJ : elementsPROJ) {
                    if (childPROJ.getClassName().contains(CLASS_EMPLOYEE_PROJECT_NAME_TR_V)) {
                        childPROJ.removeClassName(CLASS_EMPLOYEE_PROJECT_NAME_TR_V);
                    }
                }
            }
            //register getting employee projects logic
            if (parentChildEMP_PROJMap.isEmpty() || parentChildEMP_PROJMap.get(parentEMPLMap.get(key_employeeID_S)) == null) {
                getEmployeeProjects(parentTBodyElement, employeeID, reportTable.getMonthMaxDay(), enableExpand);
            }
        } else {//expanded elements
            if (spanElement.getClassName().contains(CLASS_EXPANDED_ELEMENT)) {
                spanElement.removeClassName(CLASS_EXPANDED_ELEMENT);
            }
            Set<Element> elementsPROJ = parentChildEMP_PROJMap.get(parentEMPLMap.get(key_employeeID_S));
            if (elementsPROJ != null && elementsPROJ.size() > 0) {
                for (Element childPROJ : elementsPROJ) {
                    Element childPRO_NAME_TH = DOM.getChild(childPROJ, 0);
                    if (childPRO_NAME_TH != null) {
                        Element childPRO_NAME_TH_SPAN = DOM.getChild(childPRO_NAME_TH, 0);
                        if (childPRO_NAME_TH_SPAN != null && childPRO_NAME_TH_SPAN.getClassName().contains(CLASS_EXPANDED_ELEMENT)) {
                            childPRO_NAME_TH_SPAN.removeClassName(CLASS_EXPANDED_ELEMENT);
                        }
                    }
                    if (!childPROJ.getClassName().contains(CLASS_EMPLOYEE_PROJECT_NAME_TR_V)) {
                        childPROJ.addClassName(CLASS_EMPLOYEE_PROJECT_NAME_TR_V);
                    }

                    Set<Element> elements = parentChildPROJ_TASKMap.get(childPROJ);
                    if (elements != null && elements.size() > 0) {
                        for (Element childTask : elements) {
                            if (!childTask.getClassName().contains(CLASS_EMPLOYEE_TASK_NAME_TR_V)) {
                                childTask.addClassName(CLASS_EMPLOYEE_TASK_NAME_TR_V);
                            }
                        }
                    }
                }
            }
            isShowE[0] = true;
        }
    }

    private Element getShowHideOPTION_TASK(final Element parentTBodyElement, final Element parentProjectNameTRElement, final Integer employeeID,
                                           final Integer projectID, final boolean enableExpand) {
        parentProjectNameTRElement.addClassName(CLASS_COLLAPSED_PROJECT_NAME);
        final boolean showOnlyFilledCells = reportTable.getResourceUtilizationView().isShowOnlyFilledCells();
        final ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setEmployeeId(employeeID);
        filterParameter.setProjectId(projectID);
        filterParameter.setShowFilledCells(showOnlyFilledCells);
        filterParameter.setDay(reportTable.getMonthMaxDay());//max monthday
        final Element spanElement = DOM.createSpan();
        final boolean[] isShowE = {true};
        if (enableExpand) {
            generateEmployeeProTasks(parentTBodyElement, parentProjectNameTRElement, employeeID, projectID, filterParameter, spanElement, isShowE);
        }
        DOM.sinkEvents(spanElement, Event.ONCLICK);
        DOM.setEventListener(spanElement, event -> {
            if (DOM.eventGetType(event) == Event.ONCLICK) {
                generateEmployeeProTasks(parentTBodyElement, parentProjectNameTRElement, employeeID, projectID, filterParameter, spanElement, isShowE);
            }
        });
        return spanElement;
    }

    private void generateEmployeeProTasks(Element parentTBodyElement, Element parentProjectNameTRElement, Integer employeeID, Integer projectID, ListingFilterParameter filterParameter, Element spanElement, boolean[] isShowE) {
        String key_employeeID_S = employeeID + "";
        String key_projectID_S = projectID + "";
        String key_EMP_PRO_ID = employeeID + "_" + projectID;

        if (isShowE[0]) {
            if (parentProjectNameTRElement.getClassName().contains(CLASS_COLLAPSED_PROJECT_NAME)) {
                parentProjectNameTRElement.removeClassName(CLASS_COLLAPSED_PROJECT_NAME);
            }
            if (!parentProjectNameTRElement.getClassName().contains(CLASS_EXPANDED_PROJECT_NAME)) {
                parentProjectNameTRElement.addClassName(CLASS_EXPANDED_PROJECT_NAME);
            }
        } else {
            if (parentProjectNameTRElement.getClassName().contains(CLASS_EXPANDED_PROJECT_NAME)) {
                parentProjectNameTRElement.removeClassName(CLASS_EXPANDED_PROJECT_NAME);
            }
            if (!parentProjectNameTRElement.getClassName().contains(CLASS_COLLAPSED_PROJECT_NAME)) {
                parentProjectNameTRElement.addClassName(CLASS_COLLAPSED_PROJECT_NAME);
            }
        }

        if (reportTable.getResourceUtilizationView().getTimeSheetHours().getValue()) {
            showHideTimeSheetHours(isShowE[0], key_employeeID_S, key_projectID_S);
        }
        if (isShowE[0]) {//collapsed elements
            isShowE[0] = false;
            if (!spanElement.getClassName().contains(CLASS_EXPANDED_ELEMENT)) {
                spanElement.addClassName(CLASS_EXPANDED_ELEMENT);
            }

            Set<Element> elements = parentChildPROJ_TASKMap.get(parentPROJMap.get(key_EMP_PRO_ID));
            if (elements != null && elements.size() > 0) {
                for (Element childTask : elements) {
                    if (childTask.getClassName().contains(CLASS_EMPLOYEE_TASK_NAME_TR_V)) {
                        childTask.removeClassName(CLASS_EMPLOYEE_TASK_NAME_TR_V);
                    }
                }
            }
            //register getting employee tasks logic
            if (parentChildPROJ_TASKMap.isEmpty() || elements == null) {
                getEmployeeProjectTasks(parentTBodyElement, filterParameter);
            }
        } else {//expanded elements
            if (spanElement.getClassName().contains(CLASS_EXPANDED_ELEMENT)) {
                spanElement.removeClassName(CLASS_EXPANDED_ELEMENT);
            }

            Set<Element> elements = parentChildPROJ_TASKMap.get(parentPROJMap.get(key_EMP_PRO_ID));
            if (elements != null && elements.size() > 0) {
                for (Element childTask : elements) {
                    if (!childTask.getClassName().contains(CLASS_EMPLOYEE_TASK_NAME_TR_V)) {
                        childTask.addClassName(CLASS_EMPLOYEE_TASK_NAME_TR_V);
                    }
                }
            }
            isShowE[0] = true;
        }
    }

    private Element getSpanTTElement() {
        return DOM.createSpan();
    }

    private String getTotalHourMinuteWithTimeFORMAT(int totalTime) {
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_LONG_TIME_FORMAT_RESOURCE_UTIL_REPORT_TABLE)) {
            if (totalTime > 0) {
                String hour = (totalTime / 60) > 9 ? ((totalTime / 60) + "") : ("0" + (totalTime / 60));
                String minute = (totalTime % 60) > 9 ? ((totalTime % 60) + "") : ("0" + (totalTime % 60));
                return "<em>" + hour + ":" + minute + "</em>";
            }
        } else {
            if (totalTime > 0) {
                String hour = (totalTime / 60) > 9 ? ((totalTime / 60) + "") : ("" + (totalTime / 60));
                String minute = getMinuteShortFormat(totalTime % 60);
                return "<em>" + hour + "." + minute + "</em>";
            }
        }
        return "&nbsp;";
    }

    private String getMinuteShortFormat(int minute) {
        NumberFormat numberFormat = NumberFormat.getFormat(",##0");
        return numberFormat.format((minute * 10) / 60);
    }
}
