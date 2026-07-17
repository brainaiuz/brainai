package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeAttendanceReport;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeReport;
import com.edatasite.workforce.gwt.availability.client.rpc.FingerprintTimeDto;
import com.edatasite.workforce.gwt.core.client.ReasonItem;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.UnitType;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class TerminalAttendanceExcelHandler extends BaseExcelHandler {

    private static final String PLANED_DAYS = "PLANED_DAYS";
    private static final String WORKED_DAYS = "WORKED_DAYS";
    private static final String OVERTIME_DAYS = "OVERTIME_DAYS";
    private static final String ACTUAL_DAYS = "ACTUAL_DAYS";
    private static final String MONTHLY_PLANNED_DAY = "MONTHLY_PLANNED";
    private static final String MONTHLY_ACTUAL_DAY = "MONTHLY_ACTUAL_DAY";

    private static final int EMP_INFO_COLS = 3; // Full Name, Position, Employee Code
    private static final int COLS_PER_DAY = 3;  // IN, OUT, AT

    @Autowired
    private AvailabilityService availabilityService;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private DepartmentManager departmentManager;

    private HashMap<Integer, HashMap<String, LREmployee>> leaveTotalData;
    private HashMap<Integer, HashMap<String, Integer>> leaveTotalDataByEmployee;
    private HashMap<Integer, List<ExcelData[]>> employeeReportByDepartmentId;
    private Map<HSSFWorkbook, Map<String, HSSFCellStyle>> styleCacheByWorkbook = new HashMap<>();
    private transient HSSFWorkbook currentWorkbookForColor;
    private String monthName;

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {

        EmployeeAttendanceReport attendanceReport = null;
        ListingFilterParameter fp = (ListingFilterParameter) object;

        employeeReportByDepartmentId = new LinkedHashMap<>();
        leaveTotalDataByEmployee = new HashMap<>();
        leaveTotalData = new HashMap<>();

        List<ExcelData[]> list = new LinkedList<>();

        Map<String, Integer> totalMap = new HashMap<>();
        Map<Integer, Map<String, Integer>> grandTotalByDepartment = new HashMap<>();

        int plTotal = 0;
        int workDayTotal = 0;
        int overTimeTotal = 0;
        int actualDTotal = 0;
        int monthPlTotal = 0;
        int monthActualTotal = 0;

        int maxMonthDay;
        int reasonSize = 0;

        SortedSet<String> sortedReasons = new TreeSet<>();
        boolean timeSLotActualEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.TERMINAL_TIME_SLOT_ACTUAL_ENABLE);

        try {
            LocalDate startLocal = fp.getStartDate().toInstant().atZone(userManager.getUser().getUserTimezone().toZoneId()).toLocalDate();
            LocalDate endLoca = fp.getEndDate().toInstant().atZone(userManager.getUser().getUserTimezone().toZoneId()).toLocalDate();
            fp.setFromTerminal(true);

            maxMonthDay = (int) ChronoUnit.DAYS.between(startLocal, endLoca)+1;

            fp.setVisableAll(true);
            fp.setFromExcelPDF(true);

            if (fp.getStartDateNC() != null && fp.getEndDateNC() != null && maxMonthDay != -1) {

                attendanceReport = availabilityService.getEmployeeAttendanceReport(fp, Integer.parseInt(fp.getParams()));


                sortedReasons = attendanceReport.getLeaveTypes().values()
                        .stream()
                        .sorted((o1, o2) -> (o1.getUnitType() != null && o2.getUnitType() != null)
                                ? o1.getUnitType().compareTo(o2.getUnitType())
                                : -1)
                        .map(ReasonItem::getCode)
                        .collect(Collectors.toCollection(TreeSet::new));

                sortedReasons.remove("LR_TYPE_DAY_OFF");
                sortedReasons.remove("LR_TYPE_HOLIDAY");
                sortedReasons.remove("LR_TYPE_RESIGNED");

                reasonSize = sortedReasons.size();

                final int dayCols = maxMonthDay * COLS_PER_DAY;
                final int workSummaryCols = 4;   // Normal/Worked/Overtime/Actual
                final int monthlyCols = 2;       // Monthly planned/actual

                final int totalCols = EMP_INFO_COLS + dayCols + workSummaryCols + reasonSize + monthlyCols;

                final int DAY_START = EMP_INFO_COLS;
                final int WORK_SUMMARY_START = DAY_START + dayCols;
                final int REASON_START = WORK_SUMMARY_START + workSummaryCols;
                final int MONTHLY_START = REASON_START + reasonSize;
                final int LAST_COL = MONTHLY_START + monthlyCols - 1;

                ExcelData[] row1 = new ExcelData[totalCols];

                row1[0] = getExcelDataHeader2(
                        commonLocalizer.localize(PdfLocalizationName.fullName),
                        17, ExcelData.STRING,
                        HSSFColor.BLACK.index,
                        HSSFColor.WHITE.index);

                row1[1] = getExcelDataHeader2(
                        commonLocalizer.localize(PdfLocalizationName.position),
                        16, ExcelData.STRING,
                        HSSFColor.BLACK.index,
                        HSSFColor.WHITE.index);

                row1[2] = getExcelDataHeader2(
                        commonLocalizer.localize(PdfLocalizationName.employeeCode),
                        10, ExcelData.STRING,
                        HSSFColor.BLACK.index,
                        HSSFColor.WHITE.index);

                row1[0].setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                row1[1].setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                row1[2].setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

                row1[0].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                row1[1].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                row1[2].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);

                Date tempDate = new Date(fp.getStartDate().getTime());
                Date endDate = fp.getEndDate();

                SimpleDateFormat monthDayFmt = new SimpleDateFormat("MMM-dd", Locale.ENGLISH);
                SimpleDateFormat weekDayFmt = new SimpleDateFormat("EEE", Locale.ENGLISH);
                SimpleDateFormat holidayFormat = new SimpleDateFormat( "dd/MM/yyyy");


                int dayIndex = 0;
                while (!tempDate.after(endDate) && dayIndex < maxMonthDay) {

                    String title = monthDayFmt.format(tempDate).toUpperCase() + "\n" + weekDayFmt.format(tempDate).toUpperCase();

                    int base = DAY_START + dayIndex * COLS_PER_DAY;

                    row1[base] = getExcelDataHeader2(title, 15, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                    row1[base].setWrapped(true);
                    row1[base].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    row1[base].setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

//                    row2[base + 1] = getExcelDataHeader2("", 15, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
//                    row2[base + 2] = getExcelDataHeader2("", 15, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);

                    tempDate.setDate(tempDate.getDate() + 1);
                    dayIndex++;
                }

                row1[WORK_SUMMARY_START] = getExcelDataHeader2(
                        commonLocalizer.localize(PdfLocalizationName.daysOfWork),
                        20, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index
                );

                row1[REASON_START] = getExcelDataHeader2(
                        commonLocalizer.localize(PdfLocalizationName.leaveReasons),
                        20, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index
                );

                row1[MONTHLY_START] = getExcelDataHeader2(
                        commonLocalizer.localize(PdfLocalizationName.workingHours),
                        20, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index
                );




                list.add(row1);

                ExcelData[] row2 = new ExcelData[totalCols];

                row2[0] = getExcelDataHeader2("", 17, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                row2[1] = getExcelDataHeader2("", 16, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                row2[2] = getExcelDataHeader2("", 10, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);

                Calendar cal = Calendar.getInstance();
                cal.setTime(fp.getStartDate());

                tempDate = new Date(fp.getStartDate().getTime());
                endDate = fp.getEndDate();

                int dayIndex2 = 0;
                while (!tempDate.after(endDate) && dayIndex2 < maxMonthDay) {

                    int base = DAY_START + dayIndex2 * COLS_PER_DAY;

                    row2[base] = getExcelDataHeader2(commonLocalizer.localize(PdfLocalizationName.in),  10, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                    row2[base + 1] = getExcelDataHeader2(commonLocalizer.localize(PdfLocalizationName.out), 12, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                    row2[base + 2] = getExcelDataHeader2(commonLocalizer.localize(PdfLocalizationName.actualShort),  10, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);

                    row2[base].setWrapped(false);
                    row2[base + 1].setWrapped(false);
                    row2[base + 2].setWrapped(false);

                    row2[base].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    row2[base + 1].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    row2[base + 2].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);

                    tempDate.setDate(tempDate.getDate() + 1);
                    dayIndex2++;
                }





                row2[WORK_SUMMARY_START + 0] = getExcelVerticalData(commonLocalizer.localize(PdfLocalizationName.normalDays), 3, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                row2[WORK_SUMMARY_START + 1] = getExcelVerticalData(commonLocalizer.localize(PdfLocalizationName.workedDaysBasedOnTheNorm), 3, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                row2[WORK_SUMMARY_START + 2] = getExcelVerticalData(commonLocalizer.localize(PdfLocalizationName.overtimeWorkedDays), 3, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                row2[WORK_SUMMARY_START + 3] = getExcelVerticalData(commonLocalizer.localize(PdfLocalizationName.actualDays), 3, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);

                int k = 0;
                for (String leaveCode : sortedReasons) {
                    ReasonItem ri = attendanceReport.getLeaveTypes().get(leaveCode);
                    String shortName = (ri != null && ri.getShortName() != null) ? ri.getShortName()
                            : (ri != null ? ri.getName() : leaveCode);
                    row2[REASON_START + k] = getExcelVerticalData(shortName, 2, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                    k++;
                }

                row2[MONTHLY_START + 0] = getExcelVerticalData(commonLocalizer.localize(PdfLocalizationName.monthlyPlanned), 3, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                row2[MONTHLY_START + 1] = getExcelVerticalData(commonLocalizer.localize(PdfLocalizationName.monthlyActual), 3, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);

                list.add(row2);

                if (fp.isOrderByDepartment()) {
                    employeeReportByDepartmentId.put(-1, Arrays.asList(
                            list.get(0).clone(),
                            list.get(1).clone(),
                            list.get(2).clone(),
                            list.get(3).clone()
                    ));
                }


                EmployeeReport[] employeeReportArray = attendanceReport.getEmployeeReports();
                Map<Integer, Map<Integer, FingerprintTimeDto>> fingerprintTimeDtoMap = attendanceReport.getFingerprintTimeDtoMap();
                Date now = new Date();
                ZonedDateTime zonedDateTime = now.toInstant().atZone(userManager.getUser().getUserTimezone().toZoneId());

                for (EmployeeReport employee : employeeReportArray) {

                    int[] al = employee.getAl();
                    HashMap<String, Integer> monthHolidaysByPeriod = employee.getMonthHolidaysByPeriod();

                    ExcelData[] row = new ExcelData[totalCols];
                    Map<Integer, FingerprintTimeDto> employeeInOut = fingerprintTimeDtoMap.get(employee.getId());

                    row[0] = getExcelDataHeader(employee.getName(), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                    row[1] = getExcelDataHeader(employee.getPosition() != null ? employee.getPosition() : "", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                    row[2] = getExcelDataHeader(employee.getCode(), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);


                    int nowDay = now.getDate();
                    Date startDate = fp.getStartDate();
                    boolean futurePeriod = startDate != null &&
                            (startDate.getYear() > now.getYear() ||
                                    (startDate.getYear() == now.getYear() && startDate.getMonth() > now.getMonth()));
                    boolean thisMonth = startDate != null &&
                            (startDate.getYear() == now.getYear() && startDate.getMonth() == now.getMonth());


                    Date tmp = new Date(fp.getStartDate().getTime());
                    Date end = fp.getEndDate();

                    dayIndex = 0;
                    while (!tmp.after(end) && dayIndex < maxMonthDay) {

                        int dayOfMonth = tmp.getDate(); // 1..31 (arraylar uchun)
                        int base = DAY_START + dayIndex * COLS_PER_DAY;
                        LocalDate localDate = tmp.toInstant().atZone(userManager.getUser().getUserTimezone().toZoneId()).toLocalDate();
                        Integer holidayType = monthHolidaysByPeriod != null ? monthHolidaysByPeriod.get(holidayFormat.format(tmp)) : null;


                        Integer tempHour = employee.getInOutHour()[dayOfMonth] == null ? 0 : employee.getInOutHour()[dayOfMonth];

                        boolean future = futurePeriod || (thisMonth && dayOfMonth > nowDay);

                        ExcelData atCell;


                        if (localDate.equals(LocalDate.now()) && (zonedDateTime.getHour() * 60 + zonedDateTime.getMinute()) < employee.getTimeSlotItems().get(tmp.getDay())[0]) {
                            atCell = getExcelLeaveData(true, attendanceReport.getLeaveTypes().get("TIMESLOT_NOT_STARTED"));
                        } else if (al[tmp.getDate()] == 1 || al[tmp.getDate()] == 4) {
                            String code = employee.getLeaveCodes()[tmp.getDay()];
                            int hour = tempHour / 60;

                            int hourCount = (attendanceReport.getLeaveTypes().get(code) != null
                                    && attendanceReport.getLeaveTypes().get(code).getUnitType() != null
                                    && attendanceReport.getLeaveTypes().get(code).getUnitType().equals(UnitType.HOURLY))
                                    ? hour : 1;

                            atCell = getExcelLeaveData(true, attendanceReport.getLeaveTypes().get(code));
                            fillLeaveTotalData(leaveTotalData, dayOfMonth, code, employee, leaveTotalDataByEmployee, hourCount);
                        } else if (al[tmp.getDate()] == -1) {
                            String code = employee.getLeaveCodes()[tmp.getDate()];
                            int hour = tempHour / 60;

                            int hourCount = (attendanceReport.getLeaveTypes().get(code) != null
                                    && attendanceReport.getLeaveTypes().get(code).getUnitType() != null
                                    && attendanceReport.getLeaveTypes().get(code).getUnitType().equals(UnitType.HOURLY))
                                    ? hour : 1;

                            atCell = getExcelLeaveData(false, attendanceReport.getLeaveTypes().get(code));
                            fillLeaveTotalData(leaveTotalData, dayOfMonth, code, employee, leaveTotalDataByEmployee, hourCount);
                        } else if (al[tmp.getDate()] == 2 || al[tmp.getDate()] == -2) {
                            String code = employee.getLeaveCodes()[tmp.getDate()];
                            int hour = tempHour / 60;

                            int hourCount = (attendanceReport.getLeaveTypes().get(code) != null
                                    && attendanceReport.getLeaveTypes().get(code).getUnitType() != null
                                    && attendanceReport.getLeaveTypes().get(code).getUnitType().equals(UnitType.HOURLY))
                                    ? hour : 1;

                            atCell = getExcelLeaveHourlyData(al[tmp.getDate()] == 2, attendanceReport.getLeaveTypes().get(code), String.valueOf(hour));
                            fillLeaveTotalData(leaveTotalData, dayOfMonth, code, employee, leaveTotalDataByEmployee, hourCount);
                        } else if (al[tmp.getDate()] == 3) {
                            String code = employee.getLeaveCodes()[tmp.getDate()];
                            int hour = tempHour / 60;
                            int hourCount = (attendanceReport.getLeaveTypes().get(code) != null
                                    && attendanceReport.getLeaveTypes().get(code).getUnitType() != null
                                    && attendanceReport.getLeaveTypes().get(code).getUnitType().equals(UnitType.HOURLY))
                                    ? hour : 1;
                            atCell = getExcelLeaveHourlyData(false, attendanceReport.getLeaveTypes().get(code), String.valueOf(hour));
                            fillLeaveTotalData(leaveTotalData, dayOfMonth, code, employee, leaveTotalDataByEmployee, hourCount);
                        } else if (al[tmp.getDate()] == -3) {
                            atCell = getExcelLeaveData(true, attendanceReport.getLeaveTypes().get("LR_TYPE_HOLIDAY"));
                        } else if (employee.isHasShift()) {
                            if (holidayType != null && holidayType == 2) {
                                atCell = getExcelLeaveData(true, attendanceReport.getLeaveTypes().get("LR_TYPE_HOLIDAY"));
                            } else if (employee.getInOutHour()[tmp.getDate()] == null) {
                                atCell = getExcelLeaveData(true, attendanceReport.getLeaveTypes().get("LR_TYPE_UNAUTHORIZED_LEAVE"));
                            } else if (tempHour == 0) {
                                if (holidayType != null && (holidayType == 1 || holidayType == 3)) {
                                    atCell = getExcelLeaveData(true, attendanceReport.getLeaveTypes().get("LR_TYPE_HOLIDAY"));
                                } else {
                                    atCell = getExcelLeaveData(true, attendanceReport.getLeaveTypes().get("LR_TYPE_DAY_OFF"));
                                }
                            } else if (future) {
                                atCell = getExcelDataNormal("", 2, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                            } else {
                                atCell = getExcelLeaveData(true, attendanceReport.getLeaveTypes().get("LR_TYPE_UNAUTHORIZED_LEAVE"));
                            }
                        } else if (holidayType != null && holidayType == 1) {
                            if (tempHour == 0) {
                                atCell = getExcelLeaveData(true, attendanceReport.getLeaveTypes().get("LR_TYPE_DAY_OFF"));
                            } else if (future) {
                                atCell = getExcelDataNormal("", 2, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                            } else {
                                atCell = getExcelLeaveData(true, attendanceReport.getLeaveTypes().get("LR_TYPE_UNAUTHORIZED_LEAVE"));
                            }
                        } else if (holidayType != null && holidayType == 2) {
                            if (tempHour == 0) {
                                if (!future) {
                                    atCell = getExcelLeaveData(true, attendanceReport.getLeaveTypes().get("LR_TYPE_HOLIDAY"));
                                } else {
                                    atCell = getExcelDataNormal("", 2, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                                }

                            } else if (future) {
                                atCell = getExcelDataNormal("", 2, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                            } else {
                                atCell = getExcelLeaveData(true, attendanceReport.getLeaveTypes().get("LR_TYPE_UNAUTHORIZED_LEAVE"));
                            }
                        } else if (holidayType != null && holidayType == 3) {
                            if (tempHour == 0) {
                                atCell = getExcelLeaveData(true, attendanceReport.getLeaveTypes().get("LR_TYPE_HOLIDAY"));
                            } else if (!future) {
                                atCell = getExcelLeaveData(true, attendanceReport.getLeaveTypes().get("LR_TYPE_UNAUTHORIZED_LEAVE"));
                            } else {
                                atCell = getExcelDataNormal("", 2, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                            }
                        } else if (holidayType != null && holidayType == 4) {
                            if (tempHour == 0) {
                                atCell = getExcelLeaveData(true, attendanceReport.getLeaveTypes().get("LR_TYPE_HOLIDAY"));
                            } else if (!future) {
                                atCell = getExcelLeaveData(true, attendanceReport.getLeaveTypes().get("LR_TYPE_UNAUTHORIZED_LEAVE"));
                            } else {
                                atCell = getExcelDataNormal("", 2, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                            }
                        } else if (!future) {
                            atCell = getExcelLeaveData(true, attendanceReport.getLeaveTypes().get("LR_TYPE_UNAUTHORIZED_LEAVE"));
                        } else {
                            atCell = getExcelDataNormal("", 2, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                        }


                        ExcelData atWithBorder = getExcelDataNormal(
                                atCell != null ? String.valueOf(atCell.getValue()) : "",
                                2, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index,
                                ExcelData.BOTTOM_BORDER, ExcelData.RIGHT_BORDER
                        );
                        if (atCell != null) {
                            atWithBorder.setBgcolor(atCell.getBgcolor());
                            atWithBorder.setFontColor(atCell.getFontColor());
                            atWithBorder.setStyle(true);
                        }
                        atWithBorder.setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);



                        boolean hasActual = false;
                        if (employeeInOut != null && employeeInOut.get(dayOfMonth) != null) {
                            FingerprintTimeDto fingerprintDto = employeeInOut.get(dayOfMonth);

                            if (fingerprintDto.getIntime() != null) {
                                row[base] = getExcelDataNormal(getFingerprintTime(fingerprintDto, true), 2, ExcelData.STRING,
                                        HSSFColor.BLACK.index, HSSFColor.WHITE.index,
                                        ExcelData.BOTTOM_BORDER, ExcelData.RIGHT_BORDER);
                            } else {
                                row[base] = getExcelDataNormal("--:--", 2, ExcelData.STRING,
                                        HSSFColor.BLACK.index, HSSFColor.WHITE.index,
                                        ExcelData.BOTTOM_BORDER, ExcelData.RIGHT_BORDER);
                            }

                            if (fingerprintDto.getOuttime() != null) {
                                row[base + 1] = getExcelDataNormal(getFingerprintTime(fingerprintDto, false), 2, ExcelData.STRING,
                                        HSSFColor.BLACK.index, HSSFColor.WHITE.index,
                                        ExcelData.BOTTOM_BORDER, ExcelData.RIGHT_BORDER);
                            } else {
                                row[base + 1] = getExcelDataNormal("--:--", 2, ExcelData.STRING,
                                        HSSFColor.BLACK.index, HSSFColor.WHITE.index,
                                        ExcelData.BOTTOM_BORDER, ExcelData.RIGHT_BORDER);
                            }

                            if (fingerprintDto.getActualtime() != null) {
                                hasActual=true;
                                Integer actualTime = 0;
                                if (timeSLotActualEnabled && fingerprintDto.getActualtime() >= tempHour) {
                                    actualTime = tempHour;
                                } else {
                                    actualTime = fingerprintDto.getActualtime();
                                }

                                row[base+2] = getExcelDataNormal((actualTime / 60 < 10 ? "0" : "") + (actualTime / 60) + ":" + (actualTime % 60 < 10 ? "0" : "") + (actualTime % 60), 2, ExcelData.STRING,
                                        HSSFColor.BLACK.index, HSSFColor.WHITE.index,
                                        ExcelData.BOTTOM_BORDER, ExcelData.RIGHT_BORDER);
                            } else {
                                row[base + 2] = getExcelLeaveData(true, attendanceReport.getLeaveTypes().get("NO_CHECK_IN"));
                            }

                        } else {
                            row[base] = atWithBorder;

                            row[base + 1] = getExcelDataNormal("", 2, ExcelData.STRING,
                                    HSSFColor.BLACK.index, HSSFColor.WHITE.index,
                                    ExcelData.BOTTOM_BORDER);

                            row[base + 2] = getExcelDataNormal("", 2, ExcelData.STRING,
                                    HSSFColor.BLACK.index, HSSFColor.WHITE.index,
                                    ExcelData.BOTTOM_BORDER);

//                            row[base].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                        }

                        tmp.setDate(tmp.getDate() + 1);
                        dayIndex++;
                    }

                    if (fp.isOrderByDepartment()) {
                        addValueToGrandTotalMap(grandTotalByDepartment, employee);
                    }

                    int plannedDays = employee.getPlannedDays();
                    int workedDays = employee.getWorkedDays();
                    int overtimeDays = employee.getOvertimeDays();

                    row[WORK_SUMMARY_START + 0] = getExcelDataNormal(plannedDays != 0 ? plannedDays : null, ExcelData.INTEGER, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.BOTTOM_BORDER, ExcelData.RIGHT_BORDER);
                    row[WORK_SUMMARY_START + 1] = getExcelDataNormal(workedDays != 0 ? workedDays : null, ExcelData.INTEGER, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.BOTTOM_BORDER, ExcelData.RIGHT_BORDER);
                    row[WORK_SUMMARY_START + 2] = getExcelDataNormal(overtimeDays != 0 ? overtimeDays : null, ExcelData.INTEGER, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.BOTTOM_BORDER, ExcelData.RIGHT_BORDER);
                    row[WORK_SUMMARY_START + 3] = getExcelDataNormal((workedDays + overtimeDays) != 0 ? (workedDays + overtimeDays) : null, ExcelData.INTEGER, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.BOTTOM_BORDER, ExcelData.RIGHT_BORDER);

                    for (int t = 0; t < 4; t++) row[WORK_SUMMARY_START + t].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);

                    Map<String, Integer> lrMap = leaveTotalDataByEmployee.get(employee.getId());
                    int idx = 0;
                    for (String reason : sortedReasons) {
                        row[REASON_START + idx] = getExcelDataNormal(lrMap != null ? lrMap.get(reason) : null, ExcelData.INTEGER,
                                HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.BOTTOM_BORDER, ExcelData.RIGHT_BORDER);
                        row[REASON_START + idx].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                        idx++;
                    }

                    row[MONTHLY_START + 0] = getExcelDataNormal(convertDateFormatToDoube((int) employee.getPlannedHours()), 6, ExcelData.BIG_DECIMAL,
                            HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.BOTTOM_BORDER, ExcelData.RIGHT_BORDER);
                    row[MONTHLY_START + 1] = getExcelDataNormal(convertDateFormatToDoube(employee.getInhour()), 6, ExcelData.BIG_DECIMAL,
                            HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.BOTTOM_BORDER, ExcelData.RIGHT_BORDER);

                    plTotal += plannedDays;
                    workDayTotal += workedDays;
                    overTimeTotal += overtimeDays;
                    actualDTotal += workedDays + overtimeDays;
                    monthPlTotal += convertDateFormatToDoube((int) employee.getPlannedHours()).intValue();
                    monthActualTotal += convertDateFormatToDoube(employee.getInhour()).intValue();

                    list.add(row);

                    if (fp.isOrderByDepartment()) {
                        List<ExcelData[]> empList = employeeReportByDepartmentId.get(employee.getDepartmentId());
                        if (empList == null) empList = new ArrayList<>();
                        empList.add(row);
                        employeeReportByDepartmentId.put(employee.getDepartmentId(), empList);
                    }
                }

                totalMap.put(PLANED_DAYS, plTotal);
                totalMap.put(WORKED_DAYS, workDayTotal);
                totalMap.put(ACTUAL_DAYS, actualDTotal);
                totalMap.put(OVERTIME_DAYS, overTimeTotal);
                totalMap.put(MONTHLY_PLANNED_DAY, monthPlTotal);
                totalMap.put(MONTHLY_ACTUAL_DAY, monthActualTotal);

                list.add(getGranDTOtal(totalMap, totalCols, WORK_SUMMARY_START, MONTHLY_START));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // build workbook
        HSSFWorkbook workBook;

        if (fp.isOrderByDepartment() && employeeReportByDepartmentId != null && employeeReportByDepartmentId.size() > 0) {

            LinkedList<ExcelData[]> allRowsList = new LinkedList<>();
            HashMap<Integer, Integer> rowCountByDepartment = new HashMap<>();
            HashMap<Integer, String> departmentNames = departmentManager.getDepartmentNamesMapByIds(fp.getDepartmentIds());
            LinkedList<Integer> nonEmptyDepartmentIds = new LinkedList<>();

            for (Integer depId : departmentNames.keySet()) {
                if (!depId.equals(-1)) {
                    List<ExcelData[]> depEmployees = employeeReportByDepartmentId.get(depId);
                    if (depEmployees != null && !depEmployees.isEmpty()) {

                        LinkedList<ExcelData[]> depRows = new LinkedList<>();
                        depRows.addAll(employeeReportByDepartmentId.get(-1)); // headers
                        depRows.addAll(depEmployees);
                        depRows.add(getGranDTOtal(grandTotalByDepartment.get(depId),
                                employeeReportByDepartmentId.get(-1).get(0).length,
                                0, 0
                        ));
                        depRows.add(getEmptyRow());

                        rowCountByDepartment.put(depId, depRows.size());
                        allRowsList.addAll(depRows);
                        nonEmptyDepartmentIds.add(depId);
                    }
                }
            }

            allRowsList.add(getEmptyRow());
            workBook = new WorkBook(allRowsList, false, 0, 0).getWorkBook(filename, 0, 0, 0, allRowsList.size());

            this.currentWorkbookForColor = workBook;

            int afterIndex = 0;
            for (Integer depId : nonEmptyDepartmentIds) {
                createWorkbook(workBook, fp, fp.getLimit(), reasonSize, departmentNames.get(depId),
                        employeeReportByDepartmentId.get(depId).size(), afterIndex);
                afterIndex += rowCountByDepartment.get(depId);
            }

        } else {
            workBook = new WorkBook(list, false, 0, 0)
                    .getWorkBook(filename, 0, 0, 0, list.size());
            this.currentWorkbookForColor = workBook;

            int employeeSize = 0;
            workBook = createWorkbook(workBook, fp, fp.getLimit(), reasonSize, "",
                    employeeSize, 0);
        }

        return workBook;
    }

    private HSSFWorkbook createWorkbook(HSSFWorkbook workBook, ListingFilterParameter fp, int maxMonthDay, int reasonSize, String depName, int attendanceReportEmpSize, int startRow) {

        final int dayCols = maxMonthDay * COLS_PER_DAY;
        final int DAY_START = EMP_INFO_COLS;
        final int WORK_SUMMARY_START = DAY_START + dayCols;
        final int REASON_START = WORK_SUMMARY_START + 4;
        final int MONTHLY_START = REASON_START + reasonSize;

        HSSFSheet sheet = workBook.getSheetAt(0);

        for (int c = DAY_START; c < WORK_SUMMARY_START; c++) {
            sheet.setColumnWidth(c, 4 * 256);
        }

        for (int c = 0; c < 3; c++) {
            CellRangeAddress empMerge = new CellRangeAddress(startRow + 0, startRow + 1, c, c);
            sheet.addMergedRegion(empMerge);

            RegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, empMerge, sheet, workBook);
            RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, empMerge, sheet, workBook);
            RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, empMerge, sheet, workBook);
            RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, empMerge, sheet, workBook);
        }

        int dayTitleRowIndex = startRow + 0;

        for (int d = 0; d < maxMonthDay; d++) {

            int cFrom = DAY_START + d * COLS_PER_DAY;
            int cTo = cFrom + 2;

            CellRangeAddress range = new CellRangeAddress(dayTitleRowIndex, dayTitleRowIndex, cFrom, cTo);
            sheet.addMergedRegion(range);

            RegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, range, sheet, workBook);
            RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, range, sheet, workBook);
            RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, range, sheet, workBook);
            RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, range, sheet, workBook);

            HSSFRow row = sheet.getRow(dayTitleRowIndex);
            if (row != null) {
                HSSFCell cell = row.getCell(cFrom);
                if (cell != null) {
                    HSSFCellStyle st = cell.getCellStyle();
                    st.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    st.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                }
            }
        }

        // =========================
        // 3) SECTION HEADER MERGE (row = startRow + 0)
        // =========================
        drawExcelColumns(commonLocalizer.localize(PdfLocalizationName.daysOfWork),
                startRow + 0,
                WORK_SUMMARY_START,
                startRow + 0,
                WORK_SUMMARY_START + 3,
                workBook,
                WORK_SUMMARY_START);

        if (reasonSize > 0) {
            drawExcelColumns(commonLocalizer.localize(PdfLocalizationName.leaveReasons),
                    startRow + 0,
                    REASON_START,
                    startRow + 0,
                    REASON_START + reasonSize - 1,
                    workBook,
                    REASON_START);
        }

        drawExcelColumns(commonLocalizer.localize(PdfLocalizationName.workingHours),
                startRow + 0,
                MONTHLY_START,
                startRow + 0,
                MONTHLY_START + 1,
                workBook,
                MONTHLY_START);

        // =====================================================
// 4) DATA ROWS: IN/OUT/AT ichida (DO, Holiday, Leave, ...) bo‘lsa => 3 col merge
// =====================================================
        int headerRows = 2; // row0=day title, row1=in/out/actual
        int firstEmpRow = startRow + headerRows;
        int lastRow = sheet.getLastRowNum();

        for (int r = firstEmpRow; r <= lastRow; r++) {

            HSSFRow rr = sheet.getRow(r);
            if (rr == null) continue;

            for (int d = 0; d < maxMonthDay; d++) {

                int cFrom = DAY_START + d * COLS_PER_DAY;
                int cTo   = cFrom + 2;

                HSSFCell inCell  = rr.getCell(cFrom);
                HSSFCell outCell = rr.getCell(cFrom + 1);
                HSSFCell atCell  = rr.getCell(cFrom + 2);

                if (inCell == null)  inCell = rr.createCell(cFrom);
                if (outCell == null) outCell = rr.createCell(cFrom + 1);
                if (atCell == null)  atCell = rr.createCell(cFrom + 2);

                String inTxt  = getCellText(inCell);
                String outTxt = getCellText(outCell);
                String atTxt  = getCellText(atCell);

                boolean inEmpty  = inTxt.isEmpty();
                boolean outEmpty = outTxt.isEmpty();
                boolean atEmpty  = atTxt.isEmpty();

                // 1) AGAR DO (yoki leave code) AT da turib qolgan bo‘lsa:
                //    IN/OUT bo‘sh, AT to‘la => AT ni IN ga ko‘chiramiz
                if (inEmpty && outEmpty && !atEmpty) {
                    inCell.setCellValue(atTxt);
                    outCell.setCellValue("");
                    atCell.setCellValue("");

                    inTxt = atTxt;
                    inEmpty = false;
                    outEmpty = true;
                    atEmpty = true;
                }

                // 2) Endi standart holat: IN da qiymat bor, OUT/AT bo‘sh => merge
                if (!inEmpty && outEmpty && atEmpty) {

                    CellRangeAddress region = new CellRangeAddress(r, r, cFrom, cTo);
                    sheet.addMergedRegion(region);

                    // Region border (tashqi)
                    RegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, region, sheet, workBook);
                    RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, region, sheet, workBook);
                    RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, region, sheet, workBook);
                    RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, region, sheet, workBook);

                    // 🔥 ENG MUHIM QISM — har 3 ta cellga bottom border berish
                    for (int c = cFrom; c <= cTo; c++) {

                        HSSFCell cell = rr.getCell(c);
                        if (cell == null) cell = rr.createCell(c);

                        boolean isLeft  = (c == cFrom);
                        boolean isRight = (c == cTo);

                        // 🎨 MUHIM: eski cell rangini olish
                        Short bgColor = null;
                        HSSFCellStyle oldStyle = cell.getCellStyle();
                        if (oldStyle != null) {
                            bgColor = oldStyle.getFillForegroundColor();
                        }

                        HSSFCellStyle style = getOrCreateStyle(
                                workBook,
                                bgColor,
                                HSSFCellStyle.ALIGN_CENTER,
                                isLeft,
                                isRight,
                                true
                        );

                        cell.setCellStyle(style);
                    }
                }


            }
        }

/** STRING "" ham bo‘lishi mumkin — shuni ham bo‘sh deb hisoblaymiz */
        sheet.createFreezePane(0, 2);
        sheet.createFreezePane(3, 2);
        return workBook;
    }

    // Workbook bilan bog‘langan style cache

    private HSSFCellStyle getOrCreateStyle(HSSFWorkbook workbook,
                                           Short bgColor,
                                           short alignment,
                                           boolean left,
                                           boolean right,
                                           boolean bottom) {

        Map<String, HSSFCellStyle> styleCache = styleCacheByWorkbook.computeIfAbsent(workbook, k -> new HashMap<>());

        String key = bgColor + "_" + alignment + "_" + left + "_" + right + "_" + bottom;

        if (styleCache.containsKey(key)) {
            return styleCache.get(key);
        }

        HSSFCellStyle style = workbook.createCellStyle();

        if (bgColor != null) {
            style.setFillForegroundColor(bgColor);
            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        }
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 6);
        style.setFont(font);

        style.setAlignment(alignment);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        if (bottom) style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        if (left)   style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        if (right)  style.setBorderRight(HSSFCellStyle.BORDER_THIN);

        styleCache.put(key, style);
        return style;
    }

    private String getCellText(HSSFCell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                return cell.getStringCellValue() == null ? "" : cell.getStringCellValue().trim();
            case HSSFCell.CELL_TYPE_NUMERIC:
                return String.valueOf(cell.getNumericCellValue()).trim();
            case HSSFCell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue()).trim();
            case HSSFCell.CELL_TYPE_FORMULA:
                try {
                    return cell.getStringCellValue() == null ? "" : cell.getStringCellValue().trim();
                } catch (Exception e) {
                    return "";
                }
            default:
                return "";
        }
    }

    private ExcelData[] getEmptyRow() {
        return new ExcelData[40];
    }

    private void addValueToGrandTotalMap(Map<Integer, Map<String, Integer>> grandTotalMap, EmployeeReport employee) {
        Map<String, Integer> totalByDepartmentMap = grandTotalMap.get(employee.getDepartmentId()) == null
                ? new HashMap<>()
                : grandTotalMap.get(employee.getDepartmentId());

        totalByDepartmentMap.put(PLANED_DAYS, calculateCount(totalByDepartmentMap.get(PLANED_DAYS), employee.getPlannedDays()));
        totalByDepartmentMap.put(WORKED_DAYS, calculateCount(totalByDepartmentMap.get(WORKED_DAYS), employee.getWorkedDays()));
        totalByDepartmentMap.put(OVERTIME_DAYS, calculateCount(totalByDepartmentMap.get(OVERTIME_DAYS), employee.getOvertimeDays()));
        totalByDepartmentMap.put(ACTUAL_DAYS, calculateCount(totalByDepartmentMap.get(ACTUAL_DAYS), employee.getWorkedDays() + employee.getOvertimeDays()));
        totalByDepartmentMap.put(MONTHLY_PLANNED_DAY, calculateCount(totalByDepartmentMap.get(MONTHLY_PLANNED_DAY), convertDateFormatToDoube((int) employee.getPlannedHours()).intValue()));
        totalByDepartmentMap.put(MONTHLY_ACTUAL_DAY, calculateCount(totalByDepartmentMap.get(MONTHLY_ACTUAL_DAY), convertDateFormatToDoube(employee.getInhour()).intValue()));

        grandTotalMap.put(employee.getDepartmentId(), totalByDepartmentMap);
    }

    private Integer calculateCount(Integer firstValue, Integer totalValue) {
        if (firstValue == null) return totalValue;
        return firstValue + totalValue;
    }

    private ExcelData[] getGranDTOtal(Map<String, Integer> total, int totalCols, int workSummaryStart, int monthlyStart) {
        ExcelData[] granDTotal = new ExcelData[totalCols];
        granDTotal[0] = getExcelDataHeader2(commonLocalizer.localize(PdfLocalizationName.grandTotal), 17, ExcelData.STRING, HSSFColor.WHITE.index, HSSFColor.WHITE.index);

        if (workSummaryStart > 0) {
            granDTotal[workSummaryStart + 0] = getExcelDataNormal(total.get(PLANED_DAYS), ExcelData.INTEGER, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.NO_BORDER);
            granDTotal[workSummaryStart + 1] = getExcelDataNormal(total.get(WORKED_DAYS), ExcelData.INTEGER, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.NO_BORDER);
            granDTotal[workSummaryStart + 2] = getExcelDataNormal(total.get(OVERTIME_DAYS), ExcelData.INTEGER, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.NO_BORDER);
            granDTotal[workSummaryStart + 3] = getExcelDataNormal(total.get(ACTUAL_DAYS), ExcelData.INTEGER, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.NO_BORDER);
        }

        if (monthlyStart > 0) {
            granDTotal[monthlyStart + 0] = getExcelDataNormal(total.get(MONTHLY_PLANNED_DAY), ExcelData.INTEGER, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.NO_BORDER);
            granDTotal[monthlyStart + 1] = getExcelDataNormal(total.get(MONTHLY_ACTUAL_DAY), ExcelData.INTEGER, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.NO_BORDER);
        }

        return granDTotal;
    }

    @Override
    public void setFileName() {
        filename = "Terminal Attendance Report";
    }


    private void drawExcelColumns(String value, int rowFrom, int colFrom, int rowTo, int colTo, HSSFWorkbook workBook, int cellNumber) {
        HSSFSheet sheet = workBook.getSheetAt(0);
        sheet.setZoom(5, 4);

        sheet.setMargin(Sheet.RightMargin, 0.05);
        sheet.setMargin(Sheet.LeftMargin, 0.05);
        sheet.setMargin(Sheet.BottomMargin, 0.05);
        sheet.setMargin(Sheet.TopMargin, 0.05);

        org.apache.poi.hssf.util.Region region = new org.apache.poi.hssf.util.Region(rowFrom, (short) colFrom, rowTo, (short) colTo);
        RegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, CellRangeAddress.valueOf(region.getRegionRef()), sheet, workBook);
        RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, CellRangeAddress.valueOf(region.getRegionRef()), sheet, workBook);
        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, CellRangeAddress.valueOf(region.getRegionRef()), sheet, workBook);
        sheet.addMergedRegion(region);

        HSSFCell cell = getRow(rowFrom, sheet).getCell((short) cellNumber);
        getRow(rowFrom, sheet).setHeight((short) 500);
        getRow(rowFrom+1, sheet).setHeight((short) 1300);

        cell.getCellStyle().setFillForegroundColor(HSSFColor.WHITE.index);
        cell.getCellStyle().setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cell.getCellStyle().setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cell.getCellStyle().setAlignment(HSSFCellStyle.ALIGN_CENTER);

        if (cellNumber == 0 || cellNumber == 1 || cellNumber == 2) {
            cell.getCellStyle().setAlignment(HSSFCellStyle.ALIGN_LEFT);
        }

        cell.getCellStyle().setBorderTop(HSSFCellStyle.BORDER_THIN);
        cell.getCellStyle().setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cell.getCellStyle().setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cell.getCellStyle().setBorderRight(HSSFCellStyle.BORDER_THIN);

        cell.setCellValue(value);
    }


    private HSSFRow getRow(int rowNumber, HSSFSheet sheet) {
        return sheet.getRow(rowNumber);
    }


    private BigDecimal convertDateFormatToDoube(Integer number) {
        if (number == null) {
            return BigDecimal.ZERO;
        } else {
            String i = Math.abs(number) % 60 > 9 ? Math.round(Math.abs(number) % 60 / 0.6) + "" : "0" + Math.round(Math.abs(number) % 60 / 0.6);
            return new BigDecimal(Double.valueOf(Math.abs(number) / 60 + (Math.abs(number) % 60 > 0 ? "." + i : ""))).setScale(1, BigDecimal.ROUND_HALF_UP);
        }
    }

    private ExcelData getExcelDataHeader(String value, int type, short fontcolor, short bgcolor) {
        ExcelData excel = new ExcelData(value, type, 10, true, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        excel.setHorizontalAlignment(HSSFCellStyle.ALIGN_LEFT);
        excel.setStyle(true);
        excel.setFontColor(fontcolor);
        excel.setBgcolor(bgcolor);
        excel.setFontSize(6);
        return excel;
    }

    private ExcelData getExcelDataHeader2(String value, double cellSize, int type, short fontcolor, short bgcolor) {
        ExcelData excel = new ExcelData(value, type, cellSize, true, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        excel.setStyle(true);
        excel.setFontColor(fontcolor);
        excel.setBgcolor(bgcolor);
        excel.setFontSize(6);
        return excel;
    }

    private ExcelData getExcelDataNormal(Integer value, int type, short fontcolor, short bgcolor, int... borderPositions) {
        ExcelData excel = new ExcelData(value, type, 2, true, true, borderPositions, ExcelData.NORMAL);
        excel.setStyle(true);
        excel.setFontColor(fontcolor);
        excel.setBgcolor(bgcolor);
        excel.setFontSize(6);
        return excel;
    }

    private ExcelData getExcelDataNormal(String value, int cellSize, int type, short fontcolor, short bgcolor, int... borderPositions) {
        ExcelData excel = new ExcelData(value, type, cellSize, true, true, borderPositions, ExcelData.NORMAL);
        excel.setStyle(true);
        excel.setFontColor(fontcolor);
        excel.setBgcolor(bgcolor);
        excel.setFontSize(6);
        return excel;
    }

    private ExcelData getExcelDataNormal(BigDecimal value, int cellSize, int type, short fontcolor, short bgcolor, int... borderPositions) {
        ExcelData excel = new ExcelData(value, type, cellSize, true, true, borderPositions, ExcelData.NORMAL);
        excel.setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
        excel.setStyle(true);
        excel.setFontColor(fontcolor);
        excel.setBgcolor(bgcolor);
        excel.setFontSize(6);
        return excel;
    }

    private ExcelData getExcelVerticalData(String value, int cellSize, int type, short fontcolor, short bgcolor) {
        ExcelData excel = new ExcelData(value, type, cellSize, true, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        excel.setRotation(90);
        excel.setStyle(true);
        excel.setFontColor(fontcolor);
        excel.setBgcolor(bgcolor);
        excel.setFontSize(6);
        excel.setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
        return excel;
    }

    private ExcelData getExcelLeaveData(boolean approved, ReasonItem lt) {
        String color = "fff922";
        String value = (lt != null && lt.getShortName() != null) ? lt.getShortName() : "LL";
        if (approved) {
            color = getLeaveTypeColor(lt);
        }
        ExcelData excel = new ExcelData(value, ExcelData.STRING, 2.2, true, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        short cb = getColor(color);
        short cf = getColor(Utils.invertColor(color));
        excel.setStyle(true);
        excel.setFontColor(cf);
        excel.setBgcolor(cb);
        excel.setFontSize(6);
        return excel;
    }

    private ExcelData getExcelLeaveHourlyData(boolean approved, ReasonItem lt, String hour) {
        String color = "fff922";
        String value = (lt != null && lt.getShortName() != null) ? lt.getShortName() + "/" + hour : "LL/" + hour;
        if (approved) {
            color = getLeaveTypeColor(lt);
        }
        ExcelData excel = new ExcelData(value, ExcelData.STRING, 2.2, true, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        short cb = getColor(color);
        short cf = getColor(Utils.invertColor(color));
        excel.setStyle(true);
        excel.setFontColor(cf);
        excel.setBgcolor(cb);
        excel.setFontSize(6);
        return excel;
    }


    // IMPORTANT: palette color must be resolved against the SAME workbook.
    private short getColor(String hexColor) {
        if (hexColor == null || hexColor.isEmpty()) return HSSFColor.WHITE.index;
        if (hexColor.startsWith("#")) hexColor = hexColor.substring(1);

        int[] rgb = Utils.convertHexToRGB(hexColor);

        HSSFWorkbook wb = (currentWorkbookForColor != null) ? currentWorkbookForColor : new HSSFWorkbook();
        HSSFPalette palette = wb.getCustomPalette();
        HSSFColor c = palette.findSimilarColor(rgb[0], rgb[1], rgb[2]);
        return c.getIndex();
    }

    private String getLeaveTypeColor(ReasonItem lt) {
        if (lt != null && lt.getHexColor() != null && !"".equals(lt.getHexColor())) {
            return lt.getHexColor();
        }
        return "ffffff";
    }

    private void fillLeaveTotalData(HashMap<Integer, HashMap<String, LREmployee>> data,
                                    int day,
                                    String code,
                                    EmployeeReport employee,
                                    HashMap<Integer, HashMap<String, Integer>> leaveByEmployee,
                                    Integer count) {
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
                Integer value = leaveByEmployee.get(employee.getId()).get(code);
                leaveByEmployee.get(employee.getId()).put(code, value + count);
            } else {
                leaveByEmployee.get(employee.getId()).put(code, count);
            }
        } else {
            HashMap<String, Integer> t = new HashMap<>();
            t.put(code, count);
            leaveByEmployee.put(employee.getId(), t);
        }
    }

    private static class LREmployee {
        private int total = 0;
        private SortedSet<EmployeeReport> emps = new TreeSet<>(Comparator.comparing(EmployeeReport::getId));

        public int getTotal() { return total; }
        public void setTotal(int total) { this.total = total; }
        public SortedSet<EmployeeReport> getEmps() { return emps; }
        public void setEmps(SortedSet<EmployeeReport> emps) { this.emps = emps; }
    }

    private String getFingerprintTime(FingerprintTimeDto dto, boolean isIn) {
        if (dto == null) return null;

        Integer minutes = isIn ? dto.getIntime() : dto.getOuttime();
        return minutes == null ? null : formatMinutes(minutes);
    }

    private String formatMinutes(int minutes) {
        int h = minutes / 60;
        int m = minutes % 60;

        return (h < 10 ? "0" : "") + h + ":" + (m < 10 ? "0" : "") + m;
    }
}