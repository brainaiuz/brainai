package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsReferenceLocale;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeAttendanceReport;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeReport;
import com.edatasite.workforce.gwt.core.client.ReasonItem;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.UnitType;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class AttendanceReportPdfHandler extends AbstractITextPostPdfHandler implements PDFConstants {
    private static final String PLANED_DAYS = "PLANED_DAYS";
    private static final String WORKED_DAYS = "WORKED_DAYS";
    private static final String OVERTIME_DAYS = "OVERTIME_DAYS";
    private static final String ACTUAL_DAYS = "ACTUAL_DAYS";
    private static final String MONTHLY_PLANNED_DAY = "MONTHLY_PLANNED";
    private static final String MONTHLY_ACTUAL_DAY = "MONTHLY_ACTUAL_DAY";
    private static final String OVERTIME_HOURS = "OVERTIME_HOURS";
    private static final String DAYOFF_HOURS = "DAYOFF_HOURS";
    private static final String OTGULLEAVE_HOURS = "OTGULLEAVE_HOURS";
    private static final String TWICE_PAID = "TWICE_PAID";
    private static final String OTGUL = "ОТГУЛ";

    @Autowired
    private AvailabilityService availabilityService;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;

    private HashSet<String> reasons;
    private HashMap<String, Integer> leaveTotal;
    private HashMap<Integer, HashMap<String, Integer>> leaveTotalByDep;
    private HashMap<Integer, HashMap<String, LREmployee>> leaveTotalData;
    private HashMap<Integer, HashMap<String, Integer>> leaveTotalDataByEmployee;
    private HashMap<Integer, List<Map<String, List<CellData>>>> employeeReportByDepartmentId;

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        final HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        fp.setVisableAll(true);
        fp.setFromExcelPDF(true);

        getTableData(fp, customData);

        baseInvoice.setCustomNumberAndDatesTable(getCustomNumberAndDatesTable(fp));
        pdfData.setBaseInvoice(baseInvoice);
        pdfData.setCustomData(customData);
        pdfData.setTableName("report");
        pdfData.setLandscape(true);
        return pdfData;
    }

    private void getTableData(ListingFilterParameter fp, HashMap<String, CustomisedITextTable> customData) {
        CustomisedITextTable customisedITextTable = new CustomisedITextTable();
        List<Map<String, List<CellData>>> list = new LinkedList<>();
        List<Map<String, List<CellData>>> list2 = new LinkedList<>();

        reasons = new HashSet<>();
        leaveTotal = new HashMap<>();
        leaveTotalByDep = new HashMap<>();
        employeeReportByDepartmentId = new LinkedHashMap<>();
        leaveTotalDataByEmployee = new HashMap<>();
        leaveTotalData = new HashMap<>();

        Map<String, Integer> totalMap = new HashMap<>();
        Map<Integer, Map<String, Integer>> grandTotalByDepartment = new HashMap<>();

        int maxMonthDay = getMaxMonthDay(fp);

        int plTotal = 0;
        int workDayTotal = 0;
        int overTimeTotal = 0;
        int actualDTotal = 0;
        int monthPlTotal = 0;
        int overTimeHours = 0;
        int monthActualTotal = 0;

        int dayOffTotal = 0;
        int otgulLeaveTotal = 0;
        int twicePaidTotal = 0;
        boolean timeslotActualEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.TERMINAL_TIME_SLOT_ACTUAL_ENABLE);

        if (fp.getStartDateNC() != null && fp.getEndDateNC() != null && maxMonthDay != -1) {
            EmployeeAttendanceReport report = availabilityService.getEmployeeAttendanceReport(fp, maxMonthDay);

            CustomisedITextTable headers = getHeaders(fp, maxMonthDay);
            CustomisedITextTable headersFull = getHeaders(fp, maxMonthDay);

            customData.put("HEADERS", headers);
            customData.put("HEADERS_WITH_ALL_LEAVE_REASONS", headersFull);

            list = new ArrayList<>();
            list2 = new ArrayList<>();

            customisedITextTable = new CustomisedITextTable();
            EmployeeReport[] employeeReportArray = report.getEmployeeReports();

            for (EmployeeReport employee : employeeReportArray) {
                if (employee.getPositionType() != null && employee.getPositionType().equals("TYPE_EXTERNAL")) {
                    continue;
                }

                Map<String, List<CellData>> row = new HashMap<>();
                Map<String, List<CellData>> row2 = new HashMap<>();

                List<CellData> employeeInfo = getEmployeeInfo(employee);
                row.put(EMPLOYMENT_INFORMATION, employeeInfo);
                row2.put(EMPLOYMENT_INFORMATION, employeeInfo);

                row.put(MONTH_DATE, getDaysInfo(fp, report, employee, maxMonthDay));
                row2.put(MONTH_DATE, getCustomDaysInfo(fp, report, employee, maxMonthDay));

                if (fp.isOrderByDepartment()) {
                    addValueToGrandTotalMap(grandTotalByDepartment, employee);
                }

                int plannedDays = employee.getPlannedDays();
                int workedDays = employee.getWorkedDays();
                int overtimeDays = employee.getOvertimeDays();

                List<CellData> totalDaysInfo = getTotalDaysInfo(plannedDays, workedDays, timeslotActualEnabled ? 0 : overtimeDays);
                row.put(DAYS, totalDaysInfo);
                row2.put(DAYS, totalDaysInfo);

                Map<String, Integer> lrMap = leaveTotalDataByEmployee.get(employee.getId());
                row.put(LEAVE_TYPE, getLeaveTypesInfo(fp, report, employee, lrMap));
                row2.put(LEAVE_TYPE, getFullLeaveTypesInfo(report, lrMap));

                int overTime = convertDateFormatToDoube((int) employee.getOvertimeHours()).intValue();
                int dayOff = convertDateFormatToDoube((int) employee.getDayOffHours()).intValue();
                int otgulLeave = ((lrMap != null && lrMap.get(OTGUL) != null) ? lrMap.get(OTGUL) : 0);
                int twicePaid = Math.max((dayOff + overTime - otgulLeave), 0);

                List<CellData> lvrInfo = getLvrInfo(employee, overTime, dayOff, otgulLeave, twicePaid);
                row.put(WORK_HOURS, lvrInfo);
                row2.put(WORK_HOURS, lvrInfo);

                plTotal += plannedDays;
                workDayTotal += workedDays;
                overTimeTotal += overtimeDays;
                actualDTotal += workedDays + (timeslotActualEnabled ? 0 : overtimeDays);
                monthPlTotal += convertDateFormatToDoube(employee.getPlannedHours()).intValue();
                monthActualTotal += convertDateFormatToDoube(employee.getInhour()).intValue();
                overTimeHours += overTime;
                dayOffTotal += dayOff;
                otgulLeaveTotal += otgulLeave;
                twicePaidTotal += twicePaid;

                list.add(row);
                list2.add(row2);

                if (fp.isOrderByDepartment()) {
                    List<Map<String, List<CellData>>> empList = employeeReportByDepartmentId.get(employee.getDepartmentId());
                    if (empList == null) {
                        empList = new ArrayList<>();
                    }
                    empList.add(row);
                    employeeReportByDepartmentId.put(employee.getDepartmentId(), empList);
                }
            }

            SortedSet<String> sortedset = getLeaveTypes(report);
            if (reasons.isEmpty()) {
                reasons.add(sortedset.first());
            }

            List<CellData> part = new ArrayList<>();
            List<CellData> lrHourly = new ArrayList<>();
            List<CellData> lrDaily = new ArrayList<>();
            for (String leaveCode : sortedset) {
                if (reasons.contains(leaveCode)) {
                    ReasonItem ri = report.getLeaveTypes().get(leaveCode);
                    String shortName = ri.getShortName() != null ? ri.getShortName() : ri.getName();

                    if ((UnitType.HOURLY).equals(ri.getUnitType()))
                        lrHourly.add(new CellData(shortName));
                    else
                        lrDaily.add(new CellData(shortName));
                }
            }

            part.addAll(lrDaily);
            part.addAll(lrHourly);
            part.get(0).setColspan(lrHourly.size());
            headers.getRowsList().get(0).put(LEAVE_TYPE, part);


            List<CellData> lrAll = new ArrayList<>();
            for (String leaveCode : sortedset) {
                ReasonItem ri = report.getLeaveTypes().get(leaveCode);
                String shortName = ri.getShortName() != null ? ri.getShortName() : ri.getName();
                lrAll.add(new CellData(shortName));
            }
            headersFull.getRowsList().get(0).put(LEAVE_TYPE, lrAll);
        }
        totalMap.put(PLANED_DAYS, plTotal);
        totalMap.put(WORKED_DAYS, workDayTotal);
        totalMap.put(ACTUAL_DAYS, actualDTotal);
        totalMap.put(OVERTIME_DAYS, overTimeTotal);
        totalMap.put(MONTHLY_PLANNED_DAY, monthPlTotal);
        totalMap.put(MONTHLY_ACTUAL_DAY, monthActualTotal);
        totalMap.put(OVERTIME_HOURS, overTimeHours);
        totalMap.put(DAYOFF_HOURS, dayOffTotal);
        totalMap.put(OTGULLEAVE_HOURS, otgulLeaveTotal);
        totalMap.put(TWICE_PAID, twicePaidTotal);

        list.add(getGrandTotal(totalMap, maxMonthDay, null));

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(fp.getStartDate());
        if (fp.getDepartmentIds() != null && fp.getDepartmentIds() != "" && fp.isOrderByDepartment() && employeeReportByDepartmentId != null && !employeeReportByDepartmentId.isEmpty()) {
            HashMap<Integer, String> departmentNames = departmentManager.getDepartmentNamesMapByIds(fp.getDepartmentIds());
            for (Integer key : departmentNames.keySet()) {
                if (!key.equals(-1)) {
                    if (employeeReportByDepartmentId.get(key) != null && !employeeReportByDepartmentId.get(key).isEmpty()) {
                        List<Map<String, List<CellData>>> dataList = new LinkedList<>(employeeReportByDepartmentId.get(key));
                        dataList.add(getGrandTotal(grandTotalByDepartment.get(key), maxMonthDay, key));
                        customisedITextTable = new CustomisedITextTable();
                        EdsReferenceLocale deparmentLocalization = departmentManager.getDeparmentLocalization(key);
                        customisedITextTable.setName(commonLocalizer.localizeWithParam(PdfLocalizationName.attendanceReportForMonthYearToDepartment, fp.getMonthName(), String.valueOf(startCalendar.get(Calendar.YEAR)), departmentNames.get(key)));
                        customisedITextTable.setNameUz(commonLocalizer.getMessage(PdfLocalizationName.attendanceReportForMonthYearToDepartment, new Object[]{getMonthUzb(startCalendar.get(Calendar.MONTH)), String.valueOf(startCalendar.get(Calendar.YEAR)), deparmentLocalization.getUzbek()}, new Locale("uz")));
                        customisedITextTable.setRowsList(dataList);
                        customData.put(String.valueOf(key), customisedITextTable);
                    }
                }
            }
        } else {
            customisedITextTable.setRowsList(list);
            customisedITextTable.setName(commonLocalizer.localizeWithParam(PdfLocalizationName.attendanceReportForMonthYear, fp.getMonthName(), String.valueOf(startCalendar.get(Calendar.YEAR))));
            customisedITextTable.setNameUz(commonLocalizer.getMessage(PdfLocalizationName.attendanceReportForMonthYear, new Object[]{getMonthUzb(startCalendar.get(Calendar.MONTH)), String.valueOf(startCalendar.get(Calendar.YEAR))}, new Locale("uz")));
            customData.put("ALL_DATA", customisedITextTable);

            CustomisedITextTable customisedITextTableFull = new CustomisedITextTable();
            customisedITextTableFull.setName(customisedITextTable.getName());
            customisedITextTableFull.setNameUz(customisedITextTable.getNameUz());
            customisedITextTableFull.setRowsList(list2);
            customData.put("ALL_DATA_WITH_ALL_LEAVE_REASONS", customisedITextTableFull);
        }
    }

    private SortedSet<String> getLeaveTypes(EmployeeAttendanceReport report) {
        SortedSet<String> sortedset = new TreeSet<>(report.getLeaveTypes().keySet());
        sortedset.remove("LR_TYPE_DAY_OFF");
        sortedset.remove("LR_TYPE_HOLIDAY");
        sortedset.remove("LR_TYPE_RESIGNED");
        return sortedset;
    }

    private CustomisedITextTable getHeaders(ListingFilterParameter fp, int maxMonthDay) {
        CustomisedITextTable customisedITextTable = new CustomisedITextTable();

        Map<String, String> header = new HashMap<>();
        header.put(EMPLOYMENT_INFORMATION, commonLocalizer.localize(PdfLocalizationName.employeeInformation));
        header.put(MONTH_DATE, commonLocalizer.localize(PdfLocalizationName.datesOfTheMonth));
        header.put(DAYS, commonLocalizer.localize(PdfLocalizationName.daysOfWork));
        header.put(LEAVE_TYPE, commonLocalizer.localize(PdfLocalizationName.leaveReasons));
        header.put(WORK_HOURS, commonLocalizer.localize(PdfLocalizationName.workingHours));
        if (fp.getApproverID() != null) {
            EdsEmployee employee = employeeManager.get(fp.getApproverID());
            header.put(APPROVER_NAME, employee != null && employee.getFullName() != null ? employee.getFullName() : "");
            header.put(APPROVER_POSITION, employee != null && employee.getPosition() != null && employee.getPosition().getLocale() != null && employee.getPosition().getLocale().getUzbek() != null ? employee.getPosition().getLocale().getUzbek() : "");
        }
        header.put(VERIFY_URL, fp.getURL());


        List<Map<String, List<CellData>>> list = new LinkedList<>();

        Map<String, List<CellData>> row = new HashMap<>();
        List<CellData> part = new ArrayList<>();
        part.add(new CellData(commonLocalizer.localize(PdfLocalizationName.fullName)));
        part.add(new CellData(commonLocalizer.localize(PdfLocalizationName.position)));
        part.add(new CellData(commonLocalizer.localize(PdfLocalizationName.employeeCode)));
        row.put(EMPLOYMENT_INFORMATION, part);

        part = new ArrayList<>();
        for (int i = 1, j = 3; i <= maxMonthDay; i++, j++) {
            part.add(new CellData(String.valueOf(i)));
        }
        row.put(MONTH_DATE, part);

        part = new ArrayList<>();
        part.add(new CellData(commonLocalizer.localize(PdfLocalizationName.normalDays)));
        part.add(new CellData(commonLocalizer.localize(PdfLocalizationName.workedDaysBasedOnTheNorm)));
        part.add(new CellData(commonLocalizer.localize(PdfLocalizationName.overtimeWorkedDays)));
        part.add(new CellData(commonLocalizer.localize(PdfLocalizationName.actualDays)));
        row.put(DAYS, part);

        part = new ArrayList<>();
        part.add(new CellData(commonLocalizer.localize(PdfLocalizationName.monthlyPlanned)));
        part.add(new CellData(commonLocalizer.localize(PdfLocalizationName.monthlyActual)));
        row.put(WORK_HOURS, part);

        list.add(row);

        customisedITextTable.setHeader(header);
        customisedITextTable.setRowsList(list);
        return customisedITextTable;
    }

    private List<CellData> getEmployeeInfo(EmployeeReport employee) {
        List<CellData> part = new ArrayList<>();
        part.add(new CellData(employee.getName()));
        part.add(new CellData(employee.getPosition()));
        part.add(new CellData(employee.getCode()));
        part.add(new CellData(employee.getPositionUzbek()));
        return part;
    }

    private List<CellData> getDaysInfo(ListingFilterParameter fp, EmployeeAttendanceReport report, EmployeeReport employee, int maxMonthDay) {
        List<CellData> part = new ArrayList<>();
        Date _date = new Date();
        int _day = _date.getDate();
        Date date = fp.getStartDate();
        boolean _future_period = date != null && (date.getYear() > _date.getYear() || (date.getYear() == _date.getYear() && date.getMonth() > _date.getMonth()));
        boolean this_month = date != null && (date.getYear() == _date.getYear() && date.getMonth() == _date.getMonth());
        boolean resignedMonth = employee.getResignationDay() != null && employee.getResignationDay().getMonth() == date.getMonth() && employee.getResignationDay().getYear() == date.getYear();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(fp.getStartDate());

        int[] al = employee.getAl();
        int[] withHoliday = employee.getWithHoliday();
        int[] leaveRequestHoliday = employee.getLeaveRequestHolidays();

        addEmptyData(part, maxMonthDay);
        CellData dayData = null;
        for (int i = 1; i <= maxMonthDay; i++) {
            boolean future = _future_period || (this_month && i > _day);
            if (!resignedMonth || employee.getResignationDay().getDate() >= i) {
                int hourcount = 0;
                int tempHour = employee.getInOutHour()[i] != null ? employee.getInOutHour()[i] : 0;
                int hour = tempHour / 60;
                String code = employee.getLeaveCodes()[i];

                switch (al[i]) {
                    case 1, -1, 2, -2: {
                        hourcount = report.getLeaveTypes().get(code).getUnitType() != null && report.getLeaveTypes().get(code).getUnitType().equals(UnitType.HOURLY) ? hour : 1;
                        dayData = getLeaveData(report.getLeaveTypes().get(code));
                        fillLeaveTotalData(leaveTotalData, i, code, employee, leaveTotalDataByEmployee, hourcount);
                        break;
                    }

                    case 3: {
                        hourcount = report.getLeaveTypes().get(code).getUnitType() != null && report.getLeaveTypes().get(code).getUnitType().equals(UnitType.HOURLY) ? hour : 1;
                        if (future) {
                            dayData = getLeaveData(report.getLeaveTypes().get(code));
                        } else {
                            dayData = getLeaveHourlyData(report.getLeaveTypes().get(code), String.valueOf(tempHour / 60));
                        }
                        fillLeaveTotalData(leaveTotalData, i, code, employee, leaveTotalDataByEmployee, hourcount);
                        break;
                    }

                    case -3: {
                        dayData = getLeaveData(report.getLeaveTypes().get("LR_TYPE_HOLIDAY"));
                        break;
                    }

                    default: {
                        dayData = getDayData(report, employee, withHoliday, leaveRequestHoliday, tempHour, i);
                    }
                }

                dayData.setBold("");
                if (future) {
                    dayData.setBackgroundColor("C0C0C0");
                    dayData.setBold("italic");
                }

                LocalDateTime dateTime = LocalDateTime.of(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH) + 1, i, 0, 0);
                Map<Date, Integer> exceptionalTimeSlotDates = employee.getExceptionalTimeSlotDates();
                Integer timeSlot = exceptionalTimeSlotDates.get(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));

                if ((leaveRequestHoliday[i] == 1 || withHoliday[i] == 1) && (timeSlot == null || timeSlot == 0)) {
                    dayData.setBackgroundColor("499CB5");
                }
                part.set(i - 1, dayData);
            }
        }

        return part;
    }

    private List<CellData> getCustomDaysInfo(ListingFilterParameter fp, EmployeeAttendanceReport report, EmployeeReport employee, int maxMonthDay) {
        List<CellData> part = new ArrayList<>();
        Date _date = new Date();
        int _day = _date.getDate();
        Date date = fp.getStartDate();
        boolean _future_period = date != null && (date.getYear() > _date.getYear() || (date.getYear() == _date.getYear() && date.getMonth() > _date.getMonth()));
        boolean this_month = date != null && (date.getYear() == _date.getYear() && date.getMonth() == _date.getMonth());
        boolean resignedMonth = employee.getResignationDay() != null && employee.getResignationDay().getMonth() == date.getMonth() && employee.getResignationDay().getYear() == date.getYear();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(fp.getStartDate());

        int[] al = employee.getAl();
        int[] withHoliday = employee.getWithHoliday();
        int[] leaveRequestHoliday = employee.getLeaveRequestHolidays();

        addEmptyData(part, maxMonthDay);
        CellData dayData = null;
        for (int i = 1; i <= maxMonthDay; i++) {
            boolean future = _future_period || (this_month && i > _day);
            if (!resignedMonth || employee.getResignationDay().getDate() >= i) {
                int tempHour = employee.getInOutHour()[i] != null ? employee.getInOutHour()[i] : 0;
                int hour = tempHour / 60;
                String code = employee.getLeaveCodes()[i];

                switch (al[i]) {
                    case 1, -1, 2, -2: {
                        dayData = getLeaveData(report.getLeaveTypes().get(code));
                        break;
                    }

                    case 3: {
//                        if (future) {
                            dayData = getLeaveData(report.getLeaveTypes().get(code));
//                        } else {
//                            dayData = getLeaveHourlyData(report.getLeaveTypes().get(code), String.valueOf(tempHour / 60));
//                        }
                        break;
                    }

                    case -3: {
                        dayData = getLeaveData(report.getLeaveTypes().get("LR_TYPE_HOLIDAY"));
                        break;
                    }

                    default: {
                        dayData = getDayData(report, employee, withHoliday, leaveRequestHoliday, tempHour, i);
                    }
                }

//                dayData.setBold("");
                if (future) {
//                    dayData.setBackgroundColor("C0C0C0");
//                    dayData.setBold("italic");
                    dayData.setText("");
                }

//                LocalDateTime dateTime = LocalDateTime.of(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH) + 1, i, 0, 0);
//                Map<Date, Integer> exceptionalTimeSlotDates = employee.getExceptionalTimeSlotDates();
//                Integer timeSlot = exceptionalTimeSlotDates.get(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));

//                if ((leaveRequestHoliday[i] == 1 || withHoliday[i] == 1) && (timeSlot == null || timeSlot == 0)) {
//                    dayData.setBackgroundColor("499CB5");
//                }
                part.set(i - 1, dayData);
            }
        }

        return part;
    }

    private CellData getDayData(EmployeeAttendanceReport report, EmployeeReport employee, int[] withHoliday, int[] leaveRequestHoliday, int tempHour, int index) {
        CellData dayData = null;
        if (employee.isHasShift()) {

            if (withHoliday[index] == 2) {
                dayData = getLeaveData(report.getLeaveTypes().get("LR_TYPE_HOLIDAY"));
            } else if (tempHour == 0) {
                dayData = withHoliday[index] == 1 || employee.getLeaveRequestHolidays()[index] == 3 ? getLeaveData(report.getLeaveTypes().get("LR_TYPE_HOLIDAY"))
                        : getLeaveData(report.getLeaveTypes().get("LR_TYPE_DAY_OFF"));
            } else if (employee.getTimeSlotId()[index] != null && !employee.getTimeSlotId()[index].isEmpty()) {
                dayData = new CellData(employee.getTimeSlotId()[index] + " / " + convertDateFormatToString(tempHour), "");
            } else {
                dayData = new CellData(convertDateFormatToString(tempHour), "");
            }

        } else if (leaveRequestHoliday[index] == 1) {

            if (tempHour == 0) {
                dayData = getLeaveData(report.getLeaveTypes().get("LR_TYPE_DAY_OFF"));
            } else {
                dayData = new CellData(convertDateFormatToString(tempHour), "");
            }

        } else if (leaveRequestHoliday[index] == 2) {

            if (tempHour == 0) {
                dayData = new CellData(convertDateFormatToString(tempHour), "");
            } else if (employee.getTimeSlotId()[index] != null && !"".equals(employee.getTimeSlotId()[index])) {
                dayData = new CellData(employee.getTimeSlotId()[index] + " / " + convertDateFormatToString(tempHour), "");
            } else {
                dayData = new CellData(convertDateFormatToString(tempHour), "");
            }

        } else if (leaveRequestHoliday[index] == 3) {

            if (employee.getInOutHour() != null
                    && Integer.valueOf(0).equals(tempHour)) {
                dayData = getLeaveData(report.getLeaveTypes().get("LR_TYPE_HOLIDAY"));
            } else {
                dayData = new CellData(convertDateFormatToString(tempHour), "");
            }

        } else if (withHoliday[index] == 1) {

            if (tempHour == 0) {
                dayData = getLeaveData(report.getLeaveTypes().get("LR_TYPE_HOLIDAY"));
            } else {
                dayData = new CellData(convertDateFormatToString(tempHour), "");
            }

        } else {
            dayData = new CellData(convertDateFormatToString(tempHour), "");
        }

        return dayData;
    }

    private List<CellData> getTotalDaysInfo(int plannedDays, int workedDays, int overtimeDays) {
        List<CellData> part = new ArrayList<>();
        part.add(new CellData(String.valueOf(plannedDays)));
        part.add(new CellData(String.valueOf(workedDays)));
        part.add(new CellData(String.valueOf(overtimeDays)));
        part.add(new CellData(String.valueOf(workedDays + overtimeDays)));
        return part;
    }

    private List<CellData> getLeaveTypesInfo(ListingFilterParameter fp, EmployeeAttendanceReport report, EmployeeReport employee, Map<String, Integer> lrMap) {
        List<CellData> part = new ArrayList<>();
        HashMap<String, Integer> reasonVal = new HashMap<>();
        for (String reason : reasons) {
            String shortname = report.getLeaveTypes().get(reason).getShortName() != null ? report.getLeaveTypes().get(reason).getShortName() : report.getLeaveTypes().get(reason).getName();
            reasonVal.put(shortname, (lrMap != null && lrMap.get(reason) != null) ? lrMap.get(reason) : 0);

            leaveTotal.put(shortname, leaveTotal.get(shortname) == null ? reasonVal.get(shortname) : leaveTotal.get(shortname) + reasonVal.get(shortname));
            if (fp.isOrderByDepartment()) {
                if (leaveTotalByDep.get(employee.getDepartmentId()) == null) {
                    leaveTotalByDep.put(employee.getDepartmentId(), new HashMap<String, Integer>() {{
                        put(shortname, reasonVal.get(shortname));
                    }});
                } else {
                    leaveTotalByDep.get(employee.getDepartmentId()).put(shortname, leaveTotalByDep.get(employee.getDepartmentId()).get(shortname) != null ?
                            leaveTotalByDep.get(employee.getDepartmentId()).get(shortname) + reasonVal.get(shortname) : reasonVal.get(shortname));
                }
            }
        }
        part.add(new CellData(reasonVal));
        return part;
    }

    private List<CellData> getFullLeaveTypesInfo(EmployeeAttendanceReport report, Map<String, Integer> lrMap) {
        List<CellData> part = new ArrayList<>();
        HashMap<String, Integer> reasonVal = new HashMap<>();
        SortedSet<String> allTypes = getLeaveTypes(report);
        for (String reason : allTypes) {
            ReasonItem ri = report.getLeaveTypes().get(reason);
            String nameKey = ri.getShortName() != null ? ri.getShortName() : ri.getName();
            Integer val = (lrMap != null && lrMap.get(reason) != null) ? lrMap.get(reason) : 0;
            reasonVal.put(nameKey, val);
        }
        part.add(new CellData(reasonVal));
        return part;
    }

    private List<CellData> getLvrInfo(EmployeeReport employee, int overTime, int dayOff, int otgulLeave, int twicePaid) {
        List<CellData> part = new ArrayList<>();
        part.add(new CellData(String.valueOf(convertDateFormatToDoube(employee.getPlannedHours()).intValue()), ""));
        part.add(new CellData(String.valueOf(convertDateFormatToDoube(employee.getInhour()).intValue()), ""));
        part.add(new CellData(String.valueOf(overTime), ""));
        part.add(new CellData(String.valueOf(dayOff), ""));
        part.add(new CellData(String.valueOf(otgulLeave)));
        part.add(new CellData(String.valueOf(twicePaid)));
        return part;
    }

    private void addEmptyData(List<CellData> part, int maxMonthDay) {
        for (int i = 1; i <= maxMonthDay; i++) {
            part.add(new CellData("", ""));
        }
    }

    private Map<String, List<CellData>> getGrandTotal(Map<String, Integer> total, int maxMonthDay, Integer depId) {

        Map<String, List<CellData>> row = new HashMap<>();
        List<CellData> part;
        part = new ArrayList<>();
        part.add(new CellData(commonLocalizer.localize(PdfLocalizationName.grandTotal)));
        part.add(new CellData(""));
        part.add(new CellData(""));
        row.put(EMPLOYMENT_INFORMATION, part);
        part = new ArrayList<>();
        for (int i = 1; i <= maxMonthDay; i++) {
            part.add(new CellData("", ""));
        }
        row.put(MONTH_DATE, part);

        part = new ArrayList<>();
        part.add(new CellData(String.valueOf(total.get(PLANED_DAYS))));
        part.add(new CellData(String.valueOf(total.get(WORKED_DAYS))));
        part.add(new CellData(String.valueOf(total.get(OVERTIME_DAYS))));
        part.add(new CellData(String.valueOf(total.get(ACTUAL_DAYS))));

        row.put(DAYS, part);

        part = new ArrayList<>();
        if (depId != null) {
            part.add(new CellData(leaveTotalByDep.get(depId)));
        } else {
            part.add(new CellData(leaveTotal));
        }

        row.put(LEAVE_TYPE, part);
        part = new ArrayList<>();
        part.add(new CellData(String.valueOf(total.get(MONTHLY_PLANNED_DAY))));
        part.add(new CellData(String.valueOf(total.get(MONTHLY_ACTUAL_DAY))));
        part.add(new CellData(String.valueOf(total.get(OVERTIME_HOURS))));
        part.add(new CellData(String.valueOf(total.get(DAYOFF_HOURS))));
        part.add(new CellData(String.valueOf(total.get(OTGULLEAVE_HOURS))));
        part.add(new CellData(String.valueOf(total.get(TWICE_PAID))));
        row.put(WORK_HOURS, part);
        return row;
    }

    private void addValueToGrandTotalMap(Map<Integer, Map<String, Integer>> grandTotalMap, EmployeeReport employee) {
        Map<String, Integer> totalByDepartmentMap = grandTotalMap.get(employee.getDepartmentId()) == null ? new HashMap<>() : grandTotalMap.get(employee.getDepartmentId());
        Map<String, Integer> lrMap = leaveTotalDataByEmployee.get(employee.getId());
        int overTime = convertDateFormatToDoube((int) employee.getOvertimeHours()).intValue();
        int dayOff = convertDateFormatToDoube((int) employee.getDayOffHours()).intValue();
        int otgulLeave = ((lrMap != null && lrMap.get(OTGUL) != null) ? lrMap.get(OTGUL) : 0);
        int twicePaid = Math.max((dayOff + overTime - otgulLeave), 0);
        totalByDepartmentMap.put(PLANED_DAYS, calculateCount(totalByDepartmentMap.get(PLANED_DAYS), employee.getPlannedDays()));
        totalByDepartmentMap.put(WORKED_DAYS, calculateCount(totalByDepartmentMap.get(WORKED_DAYS), employee.getWorkedDays()));
        totalByDepartmentMap.put(OVERTIME_DAYS, calculateCount(totalByDepartmentMap.get(OVERTIME_DAYS), employee.getOvertimeDays()));
        totalByDepartmentMap.put(ACTUAL_DAYS, calculateCount(totalByDepartmentMap.get(ACTUAL_DAYS), employee.getWorkedDays() + employee.getOvertimeDays()));
        totalByDepartmentMap.put(MONTHLY_PLANNED_DAY, calculateCount(totalByDepartmentMap.get(MONTHLY_PLANNED_DAY), convertDateFormatToDoube(employee.getPlannedHours()).intValue()));
        totalByDepartmentMap.put(MONTHLY_ACTUAL_DAY, calculateCount(totalByDepartmentMap.get(MONTHLY_ACTUAL_DAY), convertDateFormatToDoube(employee.getInhour()).intValue()));
        totalByDepartmentMap.put(OVERTIME_HOURS, calculateCount(totalByDepartmentMap.get(OVERTIME_HOURS), overTime));
        totalByDepartmentMap.put(DAYOFF_HOURS, calculateCount(totalByDepartmentMap.get(DAYOFF_HOURS), dayOff));
        totalByDepartmentMap.put(OTGULLEAVE_HOURS, calculateCount(totalByDepartmentMap.get(OTGULLEAVE_HOURS), otgulLeave));
        totalByDepartmentMap.put(TWICE_PAID, calculateCount(totalByDepartmentMap.get(TWICE_PAID), twicePaid));
        grandTotalMap.put(employee.getDepartmentId(), totalByDepartmentMap);
    }

    private Integer calculateCount(Integer firstValue, Integer totalValue) {
        if (firstValue == null) {
            return totalValue;
        }
        return firstValue + totalValue;
    }

    private BigDecimal convertDateFormatToDoube(long number) {
        long num = Math.abs(number);
        String i = num % 60 > 9 ? String.valueOf(Math.round(num % 60 / 0.6)) : "0" + Math.round(num % 60 / 0.6);

        return BigDecimal.valueOf(Double.valueOf(num / 60 + (num % 60 > 0 ? "." + i : ""))).setScale(1, RoundingMode.HALF_UP);
    }

    private String convertDateFormatToString(long number) {
        BigDecimal bigDecimal = convertDateFormatToDoube(number);
        int i = bigDecimal.intValue();
        BigDecimal bigDecimal1 = BigDecimal.valueOf(Double.parseDouble(String.valueOf(i)));
        if (bigDecimal1.compareTo(bigDecimal) == 0) {
            return String.valueOf(i);
        } else {
            return bigDecimal.toString();
        }
    }

    private CellData getLeaveHourlyData(ReasonItem lt, String hour) {
        return new CellData(((lt != null && lt.getShortName() != null) ? lt.getShortName() + " / " + hour : "LL" + " / " + hour), "");
    }

    private CellData getLeaveData(ReasonItem lt) {
        CellData cellData = new CellData(((lt != null && lt.getShortName() != null) ? lt.getShortName() : "LL"), "");
        if (cellData.getText().equals("D")) {
            cellData.setBackgroundColor("499CB5");
        }
        return cellData;
    }

    private void fillLeaveTotalData(HashMap<Integer, HashMap<String, LREmployee>> data, int day, String code, EmployeeReport employee, HashMap<Integer, HashMap<String, Integer>> leaveByEmployee, Integer hourcount) {
        if (data.get(day) != null) {
            if (data.get(day).get(code) != null) {
                LREmployee value = data.get(day).get(code);
                value.setTotal(value.getTotal() + 1);
                value.getEmps().add(employee);
                data.get(day).put(code, value);
            } else {
                LREmployee lr = new LREmployee();
                lr.setTotal(1);
                lr.getEmps().add(employee);
                data.get(day).put(code, lr);
            }
        } else {
            HashMap<String, LREmployee> t = new HashMap<>();
            LREmployee lr = new LREmployee();
            lr.setTotal(1);
            lr.getEmps().add(employee);
            t.put(code, lr);
            data.put(day, t);
        }

        if (leaveByEmployee.get(employee.getId()) != null) {
            if (leaveByEmployee.get(employee.getId()).get(code) != null) {
                leaveByEmployee.get(employee.getId()).compute(code, (k, value) -> value + hourcount);
            } else {
                leaveByEmployee.get(employee.getId()).put(code, hourcount);
            }
        } else {
            HashMap<String, Integer> t = new HashMap<>();
            t.put(code, hourcount);
            leaveByEmployee.put(employee.getId(), t);
        }
        reasons.add(code);
    }


    private CustomisedITextTable getCustomNumberAndDatesTable(ListingFilterParameter fp) {
        CustomisedITextTable numberAndDatesTable = new CustomisedITextTable();
        numberAndDatesTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        numberAndDatesTable.addRowWithCode(CURRENT_DATE, format.format(new Date()));

        return numberAndDatesTable;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        ListingFilterParameter fp = (ListingFilterParameter) super.getDataClass(request);
        String sessionId = request.getParameter("sessionId");
        if (StringUtils.isNotBlank(sessionId)) {
            ServerSecurityContext.getInstance().setSessionId(sessionId);
        }
        String departmentUniqueId = request.getParameter("departmentUniqueId");
        if (StringUtils.isNotBlank(departmentUniqueId)) {
            String[] uniqueIds = departmentUniqueId.split(",");
            ArrayList<Integer> departmentIds = new ArrayList<>();
            for (String uniqueId : uniqueIds) {
                EdsDepartment department = departmentManager.getDepartmentByUniqueId(uniqueId);
                if (department != null) {
                    departmentIds.add(department.getObjectID());
                }
            }
            fp.setDepartmentIds(departmentIds.stream().map(Objects::toString).collect(Collectors.joining(",")));
            fp.setOrderByDepartment(true);
        }
        String period = request.getParameter("period");
        if (StringUtils.isNotBlank(period)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
            Date periodDate = null;
            try {
                periodDate = format.parse(period);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (periodDate != null) {
                format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                Date start = ServerUtils.getMonthStartDate(periodDate);
                Date end = ServerUtils.getMonthEndDate(periodDate);
                fp.setParams(String.valueOf(DateUtil.countDays(start)));
                fp.setStartDate(start);
                fp.setEndDate(end);
                fp.setStartDateNC(format.format(start));
                fp.setEndDateNC(format.format(end));
                SimpleDateFormat monthNameFormatter = new SimpleDateFormat("MMMM");
                fp.setMonthName(monthNameFormatter.format(periodDate));
            }
            fp.setDay(new Date().getDate());
        }
        String templateId = request.getParameter("templateId");
        if (StringUtils.isNotBlank(templateId)) {
            fp.setTemplateID(Integer.valueOf(templateId));
        }
        String approverUniqueId = request.getParameter("approverCode");
        String verifyUrl = request.getParameter("verifyUrl") != null ? "https://chart.googleapis.com/chart?chs=250x250&cht=qr&chco=063970&chl=" + request.getParameter("verifyUrl") : null;
        if (approverUniqueId != null && verifyUrl != null && !approverUniqueId.isEmpty()) {
            EdsEmployee approver = employeeManager.getEmployeeByNumber(approverUniqueId);
            fp.setApproverID(approver != null ? approver.getObjectID() : null);
            fp.setURL(verifyUrl);
        }
        return fp;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        return fp.getMonthName() + " - " + commonLocalizer.localize(PdfLocalizationName.attendanceReport);
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName((user != null ? user.getName() + "_" + user.getLastName() : "") + dateFormat(new Date()));
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.ATTENDANCE_REPORT;
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        return fp.getTemplateID();
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        return PdfParams.Orientation.landscape;
    }

    private static class LREmployee {
        private int total = 0;
        private final SortedSet<EmployeeReport> emps = new TreeSet<>(Comparator.comparing(EmployeeReport::getId));

        public LREmployee() {
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public SortedSet<EmployeeReport> getEmps() {
            return emps;
        }
    }

    private int getMaxMonthDay(ListingFilterParameter fp) {
        try {
            return fp.getParams() != null && !"".equals(fp.getParams()) ? Integer.parseInt(fp.getParams()) : -1;
        } catch (Exception ex) {
            return -1;
        }
    }

    private String getMonthUzb(int num) {
        return switch (num) {
            case 0 -> "Yanvar";
            case 1 -> "Fevral";
            case 2 -> "Mart";
            case 3 -> "Aprel";
            case 4 -> "May";
            case 5 -> "Iyun";
            case 6 -> "Iyul";
            case 7 -> "Avgust";
            case 8 -> "Sentyabr";
            case 9 -> "Oktabr";
            case 10 -> "Noyabr";
            case 11 -> "Dekabr";
            default -> String.valueOf(num);
        };
    }
}
