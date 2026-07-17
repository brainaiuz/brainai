package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeAttendanceReport;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeReport;
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
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.RegionUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Created by Omonullo on 3/26/2017.
 */
public class AttendanceReportBetaExportExcelHandler extends BaseExcelHandler {

    private static final String PLANED_DAYS = "PLANED_DAYS";
    private static final String WORKED_DAYS = "WORKED_DAYS";
    private static final String OVERTIME_DAYS = "OVERTIME_DAYS";
    private static final String ACTUAL_DAYS = "ACTUAL_DAYS";
    private static final String MONTHLY_PLANNED_DAY = "MONTHLY_PLANNED";
    private static final String MONTHLY_ACTUAL_DAY = "MONTHLY_ACTUAL_DAY";
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


    protected HSSFWorkbook getWorkBook(Object object) {
        EmployeeAttendanceReport attendanceReport = null;
        ListingFilterParameter fp = ((ListingFilterParameter) object);
        int maxMonthDay = 0;
        int reasonSize = 0;
        HSSFWorkbook workBook;
        employeeReportByDepartmentId = new LinkedHashMap<>();
        List<ExcelData[]> list = new LinkedList<>();
        leaveTotalDataByEmployee = new HashMap<>();
        leaveTotalData = new HashMap<>();
        Map<String, Integer> totalMap = new HashMap<>();
        Map<Integer, Map<String, Integer>> grandTotalByDepartment = new HashMap<>();
        int plTotal = 0;
        int workDayTotal = 0;
        int overTimeTotal = 0;
        int actualDTotal = 0;
        int monthPlTotal = 0;
        int monthActualTotal = 0;

        try {
            maxMonthDay = fp.getParams() != null && !"".equals(fp.getParams()) ? Integer.parseInt(fp.getParams()) : -1;
            int additionalDays = maxMonthDay + 9;
            fp.setVisableAll(true);
            fp.setFromExcelPDF(true);


            if (fp.getStartDateNC() != null && fp.getEndDateNC() != null && maxMonthDay != -1) {
                attendanceReport = availabilityService.getEmployeeAttendanceReport(fp, maxMonthDay);
                Set<String> sortedset = attendanceReport.getLeaveTypes().values().stream().sorted(new Comparator<ReasonItem>() {
                    @Override
                    public int compare(ReasonItem o1, ReasonItem o2) {
                        return o1.getUnitType() != null && o2.getUnitType() != null ? o1.getUnitType().compareTo(o2.getUnitType()) : -1;
                    }
                }).map(ReasonItem::getCode).collect(Collectors.toSet());
                sortedset.remove("LR_TYPE_DAY_OFF");
                sortedset.remove("LR_TYPE_HOLIDAY");
                sortedset.remove("LR_TYPE_RESIGNED");
                sortedset.remove("TIMESLOT_NOT_STARTED");
                sortedset.remove("NO_CHECK_IN");
                sortedset.remove("LATE");
                sortedset.remove("EARLY_LEAVE");
                additionalDays += sortedset.size();
                reasonSize = sortedset.size();

                ExcelData[] excelData = new ExcelData[additionalDays];
                excelData[0] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.GREY_25_PERCENT.index);
                excelData[0].setFontColor(0);
                excelData[0].setFontSize(12);
                for (int i = 1; i < additionalDays; i++) {
                    excelData[i] = getExcelDataHeader("", ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.GREY_25_PERCENT.index);
                    excelData[i].setFontSize((short) 16);
                    excelData[i].setFontColor(0);
                }

                list.add(excelData);
                int[] monthHoliday = attendanceReport.getMonthHoliday();


                // employee title
                excelData = new ExcelData[additionalDays];
                excelData[0] = getExcelDataHeader2("", 17, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                excelData[1] = getExcelDataHeader2("", 16, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                excelData[2] = getExcelDataHeader2("", 10, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                excelData[3] = getExcelDataHeader2(commonLocalizer.localize(PdfLocalizationName.datesOfTheMonth) + fp.getMonthName(), 10, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.GREY_25_PERCENT.index);

                excelData[maxMonthDay + 3] = getExcelDataHeader2(commonLocalizer.localize(PdfLocalizationName.daysOfWork), 20, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                excelData[maxMonthDay + 7] = getExcelDataHeader2(commonLocalizer.localize(PdfLocalizationName.leaveReasons), 20, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                excelData[maxMonthDay + 7 + reasonSize] = getExcelDataHeader2(commonLocalizer.localize(PdfLocalizationName.workingHours), 20, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);

                list.add(excelData);

                //  row1
//                excelData = new ExcelData[additionalDays];
//                excelData[maxMonthDay + 14] = getExcelVerticalData(commonLocalizer.localize(PdfLocalizationName.onStandardWorkingHours), 7, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.BROWN.index);
//                excelData[maxMonthDay + 15] = getExcelVerticalData(commonLocalizer.localize(PdfLocalizationName.actualWorkedHours), 7, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.BROWN.index);
//                excelData[maxMonthDay + 16] = getExcelDataHeader2(commonLocalizer.localize(PdfLocalizationName.inParticular), 10, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.BROWN.index);

//                list.add(excelData);

                // row2
                excelData = new ExcelData[additionalDays];

                String color = "D3D3D3";

                short cb = getColor(color);

                excelData[0] = getExcelDataHeader2(commonLocalizer.localize(PdfLocalizationName.fullName), 17, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                excelData[1] = getExcelDataHeader2(commonLocalizer.localize(PdfLocalizationName.position), 16, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                excelData[2] = getExcelDataHeader2(commonLocalizer.localize(PdfLocalizationName.employeeCode), 10, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);

                for (int i = 1, j = 3; i <= maxMonthDay; i++, j++) {
                    if (monthHoliday[i] == 1) {
                        excelData[j] = getExcelDataHeader2(i + "", 2, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                        excelData[j].setVerticalAlignment(HSSFCellStyle.ALIGN_RIGHT);
                        excelData[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    } else if (monthHoliday[i] == 2) {
                        excelData[j] = getExcelDataHeader2(i + "", 2, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                        excelData[j].setVerticalAlignment(HSSFCellStyle.ALIGN_RIGHT);
                        excelData[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    } else {
                        excelData[j] = getExcelDataHeader2(i + "", 2, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                        excelData[j].setVerticalAlignment(HSSFCellStyle.ALIGN_RIGHT);
                        excelData[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    }
                }

                excelData[maxMonthDay + 3] = getExcelVerticalData(commonLocalizer.localize(PdfLocalizationName.normalDays), 3, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                excelData[maxMonthDay + 4] = getExcelVerticalData(commonLocalizer.localize(PdfLocalizationName.workedDaysBasedOnTheNorm), 3, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                excelData[maxMonthDay + 5] = getExcelVerticalData(commonLocalizer.localize(PdfLocalizationName.overtimeWorkedDays), 3, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                excelData[maxMonthDay + 6] = getExcelVerticalData(commonLocalizer.localize(PdfLocalizationName.actualDays), 3, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);

                int k = 0;
                for (String leaveCode : sortedset) {
                    String shortName;
                    shortName = attendanceReport.getLeaveTypes().get(leaveCode).getShortName() != null ? attendanceReport.getLeaveTypes().get(leaveCode).getShortName() : attendanceReport.getLeaveTypes().get(leaveCode).getName();
                    excelData[maxMonthDay + 7 + k] = getExcelVerticalData(shortName, 2, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                    k++;
                }
                excelData[maxMonthDay + 7 + reasonSize] = getExcelVerticalData(commonLocalizer.localize(PdfLocalizationName.monthlyPlanned), 3, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                excelData[maxMonthDay + 8 + reasonSize] = getExcelVerticalData(commonLocalizer.localize(PdfLocalizationName.monthlyActual), 3, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);


//                excelData[maxMonthDay + 11] = getExcelVerticalData(commonLocalizer.localize(PdfLocalizationName.daysOff), 7, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.BROWN.index);
//                excelData[maxMonthDay + 12] = getExcelVerticalData(commonLocalizer.localize(PdfLocalizationName.absenceHoursDuringWorkingDays), 7, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.BROWN.index);
//                excelData[maxMonthDay + 13] = getExcelVerticalData(commonLocalizer.localize(PdfLocalizationName.unworkedHoursDueToBeingLate), 7, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.BROWN.index);

//                excelData[maxMonthDay + 8] = getExcelVerticalData(commonLocalizer.localize(PdfLocalizationName.unpaidLeave), 7, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.BROWN.index);
//                excelData[maxMonthDay + 9] = getExcelVerticalData(commonLocalizer.localize(PdfLocalizationName.paidMaternityLeave), 7, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.BROWN.index);
//                excelData[maxMonthDay + 10] = getExcelVerticalData(commonLocalizer.localize(PdfLocalizationName.temporaryDisability), 7, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.BROWN.index);


//                excelData[maxMonthDay + 11] = getExcelVerticalData(commonLocalizer.localize(PdfLocalizationName.daysOff), 7, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.BROWN.index);
//                excelData[maxMonthDay + 12] = getExcelVerticalData(commonLocalizer.localize(PdfLocalizationName.absenceHoursDuringWorkingDays), 7, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.BROWN.index);
//                excelData[maxMonthDay + 13] = getExcelVerticalData(commonLocalizer.localize(PdfLocalizationName.unworkedHoursDueToBeingLate), 7, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.BROWN.index);
//
//                excelData[maxMonthDay + 16] = getExcelVerticalData(commonLocalizer.localize(PdfLocalizationName.overtimeWorkedHours), 6, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.BROWN.index);
//                excelData[maxMonthDay + 17] = getExcelVerticalData(commonLocalizer.localize(PdfLocalizationName.noneWorkedHours), 6, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.BROWN.index);
//                excelData[maxMonthDay + 18] = getExcelVerticalData(commonLocalizer.localize(PdfLocalizationName.workedAtNight), 6, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.BROWN.index);

                list.add(excelData);
                if (fp.isOrderByDepartment()) {
                    employeeReportByDepartmentId.put(-1, Arrays.asList(list.get(0).clone(), list.get(1).clone(), list.get(2).clone()));
                }
                EmployeeReport[] employeeReportArray = attendanceReport.getEmployeeReports();
                for (EmployeeReport employee : employeeReportArray) {
                    int[] al = employee.getAl();
                    int[] withHoliday = employee.getWithHoliday();
                    int[] leaveRequestHoliday = employee.getLeaveRequestHolidays();
                    excelData = new ExcelData[additionalDays];
                    boolean positionEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.POSITION_SHOW_IN_ATTENDANCE_REPORT);
                    String employeePosition = "";
                    if (positionEnabled) {
                        employeePosition = "\n" + hrmsLocalizer.localize("position") + ": " + employee.getPosition();
                    }
                    excelData[0] = getExcelDataHeader(employee.getName(), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                    excelData[1] = getExcelDataHeader(employee.getPosition(), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                    excelData[2] = getExcelDataHeader(employee.getCode(), ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);

                    Date _date = new Date();
                    int _day = _date.getDate();
                    Date date = fp.getStartDate();
                    boolean _future_period = date != null && (date.getYear() > _date.getYear() || (date.getYear() == _date.getYear() && date.getMonth() > _date.getMonth()));
                    boolean this_month = date != null && (date.getYear() == _date.getYear() && date.getMonth() == _date.getMonth());

                    for (int i = 1, j = 3; i <= maxMonthDay; i++, j++) {
                        int hourcount;
                        int tempHour = employee.getInOutHour()[i] == null ? 0 : employee.getInOutHour()[i];
                        boolean future = _future_period || (this_month && i > _day);
                        if (al[i] == 1) {
                            String code = employee.getLeaveCodes()[i];
                            int hour = tempHour / 60;
                            hourcount = attendanceReport.getLeaveTypes().get(code) != null && attendanceReport.getLeaveTypes().get(code).getUnitType() != null && attendanceReport.getLeaveTypes().get(code).getUnitType().equals(UnitType.HOURLY) ? hour : 1;
//                            html = html + getLeaveTypeCell(leaveTypes.get(code));
                            fillLeaveTotalData(leaveTotalData, i, code, employee, leaveTotalDataByEmployee, hourcount);
                            excelData[j] = getExcelLeaveData(true, attendanceReport.getLeaveTypes().get(code));
                            excelData[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                        } else if (al[i] == -1) {
                            String code = employee.getLeaveCodes()[i];
                            int hour = tempHour / 60;
                            hourcount = attendanceReport.getLeaveTypes().get(code).getUnitType() != null && attendanceReport.getLeaveTypes().get(code).getUnitType().equals(UnitType.HOURLY) ? hour : 1;
                            excelData[j] = getExcelLeaveData(false, attendanceReport.getLeaveTypes().get(code));
                            excelData[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                            fillLeaveTotalData(leaveTotalData, i, code, employee, leaveTotalDataByEmployee, hourcount);
                        } else if (al[i] == -2 || al[i] == 2) {
                            String code = employee.getLeaveCodes()[i];
                            int hour = tempHour / 60;
                            hourcount = attendanceReport.getLeaveTypes().get(code).getUnitType() != null && attendanceReport.getLeaveTypes().get(code).getUnitType().equals(UnitType.HOURLY) ? hour : 1;
                            excelData[j] = getExcelLeaveData(false, attendanceReport.getLeaveTypes().get(code));
                            excelData[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                            fillLeaveTotalData(leaveTotalData, i, code, employee, leaveTotalDataByEmployee, hourcount);
                        } else if (al[i] == 3) {
                            String code = employee.getLeaveCodes()[i];
                            Integer hour = tempHour / 60;
                            hourcount = attendanceReport.getLeaveTypes().get(code).getUnitType() != null && attendanceReport.getLeaveTypes().get(code).getUnitType().equals(UnitType.HOURLY) ? hour : 1;
                            if (future) {
                                excelData[j] = getExcelLeaveData(false, attendanceReport.getLeaveTypes().get(code));
                                excelData[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                                fillLeaveTotalData(leaveTotalData, i, code, employee, leaveTotalDataByEmployee, hourcount);
                            } else {
                                excelData[j] = getExcelLeaveHourlyData(false, attendanceReport.getLeaveTypes().get(code), String.valueOf(hour));
                                excelData[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                                fillLeaveTotalData(leaveTotalData, i, code, employee, leaveTotalDataByEmployee, hourcount);
                            }
                        } else if (al[i] == -3) {
                            excelData[j] = getExcelLeaveData(true, attendanceReport.getLeaveTypes().get("LR_TYPE_HOLIDAY"));
                            excelData[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                        } else if (employee.isHasShift()) {
                            if (future) {
                                excelData[j] = getExcelDataNormal("", 2, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                            } else if (tempHour == 0) {
                                excelData[j] = getExcelLeaveData(true, attendanceReport.getLeaveTypes().get("LR_TYPE_DAY_OFF"));
                                excelData[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_LEFT);
                            } else {
                                excelData[j] = getExcelTimeSlotData(employee.getTimeSlotId()[i], employee.getTimeslotItem().getHexColor(), String.valueOf(tempHour / 60));
                            }
                        } else if (leaveRequestHoliday[i] == 1) {
                            if (tempHour == 0 || future) {
                                excelData[j] = getExcelLeaveData(true, attendanceReport.getLeaveTypes().get("LR_TYPE_DAY_OFF"));
                                excelData[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_LEFT);
                            } else {
                                excelData[j] = getExcelDataNormal(convertDateFormatToDoube(tempHour), 2, ExcelData.BIG_DECIMAL, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.BOTTOM_BORDER, ExcelData.RIGHT_BORDER);
                            }
                        } else if (leaveRequestHoliday[i] == 2) {
                            if (tempHour == 0 || future) {
                                excelData[j] = getExcelDataNormal("", 2, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                                excelData[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_LEFT);
                            } else if (employee.getTimeslotItem() != null && employee.getTimeslotItem().getShortName() != null && !"".equals(employee.getTimeslotItem().getShortName())) {
                                excelData[j] = getExcelDataNormal(employee.getTimeslotItem().getShortName() + "/" + convertDateFormatToDoube(tempHour), 4, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.BOTTOM_BORDER, ExcelData.RIGHT_BORDER);
                            } else {
                                excelData[j] = getExcelDataNormal(convertDateFormatToDoube(tempHour), 2, ExcelData.BIG_DECIMAL, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.BOTTOM_BORDER, ExcelData.RIGHT_BORDER);
                            }
                        } else if (leaveRequestHoliday[i] == 3) {
                            if (Integer.valueOf(0).equals(tempHour)) {
                                excelData[j] = getExcelLeaveData(true, attendanceReport.getLeaveTypes().get("LR_TYPE_HOLIDAY"));
                                excelData[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                            } else if (!future) {
                                excelData[j] = getExcelDataNormal(convertDateFormatToDoube(tempHour), 2, ExcelData.BIG_DECIMAL, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.BOTTOM_BORDER, ExcelData.RIGHT_BORDER);
                            } else {
                                excelData[j] = getExcelDataNormal("", 2, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                            }
                        } else if (withHoliday[i] == 1) {
                            if (tempHour == 0) {
                                excelData[j] = getExcelLeaveData(true, attendanceReport.getLeaveTypes().get("LR_TYPE_HOLIDAY"));
                                excelData[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                            } else if (!future) {
                                excelData[j] = getExcelDataNormal(convertDateFormatToDoube(tempHour), 2, ExcelData.BIG_DECIMAL, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.BOTTOM_BORDER, ExcelData.RIGHT_BORDER);
                            }
                        } else if (!future) {
                            excelData[j] = getExcelDataNormal(convertDateFormatToDoube(tempHour), 2, ExcelData.BIG_DECIMAL, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.BOTTOM_BORDER, ExcelData.RIGHT_BORDER);
                            excelData[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_LEFT);
                        } else {
                            excelData[j] = getExcelDataNormal("", 2, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index);
                        }
                    }


                    if (fp.isOrderByDepartment()) {
                        addValueToGrandTotalMap(grandTotalByDepartment, employee);
                    }
                    int plannedDays = employee.getPlannedDays();
                    int workedDays = employee.getWorkedDays();
                    int overtimeDays = employee.getOvertimeDays();
                    excelData[maxMonthDay + 3] = getExcelDataNormal(plannedDays != 0 ? plannedDays : null, ExcelData.INTEGER, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.BOTTOM_BORDER, ExcelData.RIGHT_BORDER);
                    excelData[maxMonthDay + 3].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    excelData[maxMonthDay + 4] = getExcelDataNormal(workedDays != 0 ? workedDays : null, ExcelData.INTEGER, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.BOTTOM_BORDER, ExcelData.RIGHT_BORDER);
                    excelData[maxMonthDay + 4].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    excelData[maxMonthDay + 5] = getExcelDataNormal(overtimeDays != 0 ? overtimeDays : null, ExcelData.INTEGER, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.BOTTOM_BORDER, ExcelData.RIGHT_BORDER);
                    excelData[maxMonthDay + 5].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);

                    excelData[maxMonthDay + 6] = getExcelDataNormal((workedDays + overtimeDays) != 0 ? (workedDays + overtimeDays) : null, ExcelData.INTEGER, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.BOTTOM_BORDER, ExcelData.RIGHT_BORDER);
                    excelData[maxMonthDay + 6].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);

                    int l = 0;
                    Map<String, Integer> lrMap = leaveTotalDataByEmployee.get(employee.getId());
                    for (String reason : sortedset) {
                        excelData[maxMonthDay + 7 + l] = getExcelDataNormal(lrMap != null && lrMap.get(reason) != null ? lrMap.get(reason) : null, ExcelData.INTEGER, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.BOTTOM_BORDER, ExcelData.RIGHT_BORDER);
                        excelData[maxMonthDay + 7 + l].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                        l++;
                    }

                    excelData[maxMonthDay + 7 + reasonSize] = getExcelDataNormal(convertDateFormatToDoube(employee.getPlannedHours()), 3, ExcelData.BIG_DECIMAL, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.BOTTOM_BORDER, ExcelData.RIGHT_BORDER);
                    excelData[maxMonthDay + 8 + reasonSize] = getExcelDataNormal(convertDateFormatToDoube(employee.getInhour()), 3, ExcelData.BIG_DECIMAL, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.BOTTOM_BORDER, ExcelData.RIGHT_BORDER);

                    plTotal += plannedDays;
                    workDayTotal += workedDays;
                    overTimeTotal += overtimeDays;
                    actualDTotal += workedDays + overtimeDays;
                    monthPlTotal += convertDateFormatToDoube(employee.getPlannedHours()).intValue();
                    monthActualTotal += convertDateFormatToDoube(employee.getInhour()).intValue();

                    list.add(excelData);
                    if (fp.isOrderByDepartment()) {
                        try {
                            List<ExcelData[]> empList = employeeReportByDepartmentId.get(employee.getDepartmentId());
                            if (empList == null) {
                                empList = new ArrayList<>();
                            }
                            empList.add(excelData);
                            employeeReportByDepartmentId.put(employee.getDepartmentId(), empList);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

            }
            totalMap.put(PLANED_DAYS, plTotal);
            totalMap.put(WORKED_DAYS, workDayTotal);
            totalMap.put(ACTUAL_DAYS, actualDTotal);
            totalMap.put(OVERTIME_DAYS, overTimeTotal);
            totalMap.put(MONTHLY_PLANNED_DAY, monthPlTotal);
            totalMap.put(MONTHLY_ACTUAL_DAY, monthActualTotal);

            list.add(getGranDTOtal(totalMap, maxMonthDay, reasonSize));

        } catch (
                NumberFormatException e) {
            e.printStackTrace();
        }

        if (fp.isOrderByDepartment() && employeeReportByDepartmentId != null && employeeReportByDepartmentId.size() > 0) {
            LinkedList<ExcelData[]> allRowsList = new LinkedList<>();
            HashMap<Integer, Integer> rowCountByDepartment = new HashMap<>();
            HashMap<Integer, String> departmentNames = departmentManager.getDepartmentNamesMapByIds(fp.getDepartmentIds());
            LinkedList<Integer> nonEmptyDepartmetnIds = new LinkedList<>();
            for (Integer key : departmentNames.keySet()) {
                if (!key.equals(-1)) {
                    if (employeeReportByDepartmentId.get(key) != null && employeeReportByDepartmentId.get(key).size() > 0) {
                        LinkedList<ExcelData[]> excelDataList = new LinkedList<>();
                        excelDataList.addAll(0, employeeReportByDepartmentId.get(-1));
                        excelDataList.addAll(excelDataList.size(), employeeReportByDepartmentId.get(key));
                        excelDataList.add(getGranDTOtal(grandTotalByDepartment.get(key), maxMonthDay, reasonSize));
                        excelDataList.add(getEmptyRow());
                        rowCountByDepartment.put(key, excelDataList.size());
                        allRowsList.addAll(allRowsList.size(), excelDataList);
                        nonEmptyDepartmetnIds.add(key);
                    }
                }
            }
            allRowsList.add(getEmptyRow());
            workBook = new WorkBook(allRowsList, false, 0, 0).getWorkBook(filename, 0, 0, 0, allRowsList.size());
            int afterIndex = 0, rowCount = 0;
            for (Integer key : nonEmptyDepartmetnIds) {
                createWorkbook(workBook, fp, maxMonthDay, reasonSize, departmentNames.get(key), employeeReportByDepartmentId.get(key).size(), afterIndex);
                afterIndex += rowCountByDepartment.get(key);
            }
        } else {
            workBook = new WorkBook(list, true, 3, 3).getWorkBook(filename, 0, 0, 0, list.size());
            workBook = createWorkbook(workBook, fp, maxMonthDay, reasonSize, "", attendanceReport.getEmployeeReports().length, 0);
        }
//        HSSFSheet sheet = workBook.getSheetAt(0);
//        sheet.removeMergedRegion(0);
//        sheet.shiftRows(1, sheet.getLastRowNum(), -1);


        return workBook;
    }


    private HSSFWorkbook createWorkbook(HSSFWorkbook workBook,
                                        ListingFilterParameter fp, int maxMonthDay, int reasonSize, String depName,
                                        int attendanceReportEmpSize, int startRow) {

        //  row 0
        drawExcelColumns(depName, startRow + 0, 0, startRow + 0, maxMonthDay + 8 + reasonSize, workBook, 0);
        drawExcelReport(workBook, maxMonthDay, fp.getLocationId(), reasonSize, startRow, fp.isOrderByDepartment());

        // row1
        drawExcelColumns("", startRow + 1, 0, startRow + 1, 2, workBook, 0);

        alignEmployeeInformationToLeft(workBook, startRow + attendanceReportEmpSize + 3, startRow);

        drawExcelColumns(commonLocalizer.localize(PdfLocalizationName.fullName), startRow + 2, 0, startRow + 2, 0, workBook, 0);
        drawExcelColumns(commonLocalizer.localize(PdfLocalizationName.position), startRow + 2, 1, startRow + 2, 1, workBook, 1);
        drawExcelColumns(commonLocalizer.localize(PdfLocalizationName.employeeCode), startRow + 2, 2, startRow + 2, 2, workBook, 2);
        drawExcelColumns(commonLocalizer.localize(PdfLocalizationName.datesOfTheMonth) + " " + fp.getMonthName(), startRow + 1, 3, startRow + 1, maxMonthDay + 2, workBook, 3);

        drawExcelColumns(commonLocalizer.localize(PdfLocalizationName.daysOfWork), startRow + 1, maxMonthDay + 3, startRow + 1, maxMonthDay + 6, workBook, maxMonthDay + 3);
        drawExcelColumns(commonLocalizer.localize(PdfLocalizationName.leaveReasons), startRow + 1, maxMonthDay + 7, startRow + 1, maxMonthDay + 6 + reasonSize, workBook, maxMonthDay + 7);
        drawExcelColumns(commonLocalizer.localize(PdfLocalizationName.workingHours), startRow + 1, maxMonthDay + 7 + reasonSize, startRow + 1, maxMonthDay + 8 + reasonSize, workBook, maxMonthDay + 7 + reasonSize);

        return workBook;
    }

    private HSSFWorkbook margingTwoWorkBook(HSSFWorkbook toWorkbook, HSSFWorkbook fromWorkbook) {
        HSSFSheet sheet = toWorkbook.getSheetAt(0);
        int lastRowIndex = sheet.getLastRowNum();
        sheet.getRow(0).getCell(0);
        //
        return toWorkbook;
    }

    private ExcelData[] getEmptyRow() {
        return new ExcelData[40];
    }

    private void addValueToGrandTotalMap(Map<Integer, Map<String, Integer>> grandTotalMap, EmployeeReport employee) {

        Map<String, Integer> totalByDepartmentMap = grandTotalMap.get(employee.getDepartmentId()) == null ? new HashMap<>() : grandTotalMap.get(employee.getDepartmentId());

        totalByDepartmentMap.put(PLANED_DAYS, calculateCount(totalByDepartmentMap.get(PLANED_DAYS), employee.getPlannedDays()));
        totalByDepartmentMap.put(WORKED_DAYS, calculateCount(totalByDepartmentMap.get(WORKED_DAYS), employee.getWorkedDays()));
        totalByDepartmentMap.put(OVERTIME_DAYS, calculateCount(totalByDepartmentMap.get(OVERTIME_DAYS), employee.getOvertimeDays()));
        totalByDepartmentMap.put(ACTUAL_DAYS, calculateCount(totalByDepartmentMap.get(ACTUAL_DAYS), employee.getWorkedDays() + employee.getOvertimeDays()));
        totalByDepartmentMap.put(MONTHLY_PLANNED_DAY, calculateCount(totalByDepartmentMap.get(MONTHLY_PLANNED_DAY), convertDateFormatToDoube(employee.getPlannedHours()).intValue()));
        totalByDepartmentMap.put(MONTHLY_ACTUAL_DAY, calculateCount(totalByDepartmentMap.get(MONTHLY_ACTUAL_DAY), convertDateFormatToDoube(employee.getInhour()).intValue()));
        grandTotalMap.put(employee.getDepartmentId(), totalByDepartmentMap);
    }

    private Integer calculateCount(Integer firstValue, Integer totalValue) {
        if (firstValue == null) {
            return totalValue;
        }
        return firstValue + totalValue;
    }

    private ExcelData[] getGranDTOtal(Map<String, Integer> total, int maxMonthDay, int reasonSize) {
        int additionalDays = maxMonthDay + 10 + reasonSize;
        ExcelData[] granDTotal = new ExcelData[additionalDays];
        granDTotal[0] = getExcelDataHeader2(commonLocalizer.localize(PdfLocalizationName.grandTotal), 17, ExcelData.STRING, HSSFColor.WHITE.index, HSSFColor.WHITE.index);
        granDTotal[maxMonthDay + 3] = getExcelDataNormal(total.get(PLANED_DAYS), ExcelData.INTEGER, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.NO_BORDER);
        granDTotal[maxMonthDay + 3].setCellSize(4);
        granDTotal[maxMonthDay + 4] = getExcelDataNormal(total.get(WORKED_DAYS), ExcelData.INTEGER, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.NO_BORDER);
        granDTotal[maxMonthDay + 4].setCellSize(4);
        granDTotal[maxMonthDay + 5] = getExcelDataNormal(total.get(OVERTIME_DAYS), ExcelData.INTEGER, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.NO_BORDER);
        granDTotal[maxMonthDay + 6] = getExcelDataNormal(total.get(ACTUAL_DAYS), ExcelData.INTEGER, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.NO_BORDER);
        granDTotal[maxMonthDay + 6].setCellSize(4);
        granDTotal[maxMonthDay + 7 + reasonSize] = getExcelDataNormal(total.get(MONTHLY_PLANNED_DAY), ExcelData.INTEGER, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.NO_BORDER);
        granDTotal[maxMonthDay + 7 + reasonSize].setCellSize(5);
        granDTotal[maxMonthDay + 8 + reasonSize] = getExcelDataNormal(total.get(MONTHLY_ACTUAL_DAY), ExcelData.INTEGER, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.NO_BORDER);
        granDTotal[maxMonthDay + 8 + reasonSize].setCellSize(5);
        return granDTotal;
    }

    private ExcelData[] getUnavilableTotalTable(EmployeeAttendanceReport employeeAttendance, int maxMonthDay) {
        int additionalDays = maxMonthDay + 10;
        ExcelData[] grandTotal = new ExcelData[additionalDays];
        grandTotal[0] = getExcelDataHeader2(commonLocalizer.localize(PdfLocalizationName.grandTotal), 17, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.GREY_50_PERCENT.index);
        grandTotal[0].setFontColor(0);
        grandTotal[1] = getExcelDataHeader2("", 16, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.GREY_50_PERCENT.index);
        grandTotal[2] = getExcelDataHeader2("", 10, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.GREY_50_PERCENT.index);
        grandTotal[2].setFontColor(0);
        int total = 0;
        for (int i = 1, j = 3; i <= maxMonthDay; i++, j++) {
            grandTotal[j] = getExcelDataNormal(employeeAttendance.getTotalAbsent()[i], ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.GREY_25_PERCENT.index, ExcelData.NO_BORDER);
            total += employeeAttendance.getTotalAbsent()[i];
            grandTotal[j].setFontColor(0);
            grandTotal[j].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
        }
        grandTotal[maxMonthDay + 3] = getExcelDataNormal(total, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.GREY_25_PERCENT.index, ExcelData.NO_BORDER);
        grandTotal[maxMonthDay + 3].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
        return grandTotal;
    }


    private void getLeaveDays(EmployeeAttendanceReport employeeAttendance, int maxMonthDay, SortedSet<String> sortedSet, List<ExcelData[]> list) {
        int leaveDays = maxMonthDay + 10;

        for (String key : sortedSet) {
            ExcelData[] leaveTotal = new ExcelData[leaveDays];
            leaveTotal[0] = getExcelDataHeader2(getLeaveTypeCell(employeeAttendance.getLeaveTypes().get(key)), 17, ExcelData.STRING, HSSFColor.BLACK.index, getColor(employeeAttendance.getLeaveTypes().get(key).getHexColor()));
            int total = 0;


            for (int i = 1, j = 3; i <= maxMonthDay; i++) {
                int temp = getTotalByType(key, i);
                total += temp;

                if (temp > 0) {
                    leaveTotal[j] = getExcelDataNormal(temp, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.NO_BORDER);
                } else {
                    leaveTotal[j] = getExcelDataNormal(temp, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.WHITE.index, ExcelData.NO_BORDER);
                }
                j++;
            }
            leaveTotal[maxMonthDay + 3] = getExcelDataNormal(total, ExcelData.STRING, HSSFColor.BLACK.index, HSSFColor.BLUE.index, ExcelData.NO_BORDER);
            leaveTotal[maxMonthDay + 3].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
            if (total > 0) {
                list.add(leaveTotal);
            }
        }

    }

    private String getLeaveTypeCell(ReasonItem lt) {
        String color = lt != null && lt.getHexColor() != null ? lt.getHexColor() : "ffffff";
        String shortName = (lt != null && lt.getShortName() != null) ? lt.getShortName() : "(L)";
        String name = (lt != null && lt.getName() != null) ? shortName.concat(" - ").concat(lt.getName()) : shortName;

        return name;
    }

    private int getTotalByType(String key, int day) {
        HashMap<String, LREmployee> data = leaveTotalData.get(day);
        if (data != null && data.get(key) != null) {
            return data.get(key).getTotal();
        }
        return 0;
    }

    private void fillLeaveTotalData(HashMap<Integer, HashMap<String, LREmployee>> data, int day, String code, EmployeeReport employee, HashMap<Integer, HashMap<String, Integer>> leaveByEmployee, Integer count) {
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

    @Override
    public void setFileName() {
        filename = "AttendaceReport";
    }

    private void drawExcelReport(HSSFWorkbook workBook, int maxMonthDay, Integer locationId, Integer reasonSize, int startRow, boolean isOrderByDepartment) {

        setCustomColors(workBook);

        HSSFSheet sheet = workBook.getSheetAt(0);

        sheet.addMergedRegion(new Region(startRow + 0, (short) 0, startRow + 0, (short) (maxMonthDay + reasonSize + 8)));

        HSSFCell cell = getRow(startRow + 0, sheet).getCell((short) 0);
        getRow(startRow + 0, sheet).setHeight((short) 500);
        cell.getCellStyle().setFillForegroundColor(HSSFColor.WHITE.index);
        cell.getCellStyle().setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cell.getCellStyle().setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cell.getCellStyle().setAlignment(HSSFCellStyle.ALIGN_CENTER);
        if (!isOrderByDepartment) {
            if (locationId == null) {
                cell.setCellValue("  " + locationManager.getUser().getCompany().getName() + " " + commonLocalizer.localize(PdfLocalizationName.employeeHoursReport));
            } else {
                cell.setCellValue("  " + locationManager.get(locationId).getName() + " " + commonLocalizer.localize(PdfLocalizationName.employeeHoursReport));
            }
        }
        setCellFont(cell.getCellStyle(), (short) 20, HSSFColor.BLACK.index, HSSFFont.BOLDWEIGHT_BOLD, workBook);


    }

    private void drawExcelColumns(String value, int rowFrom, int colFrom, int rowTo, int colTo, HSSFWorkbook workBook, int cellNumber) {

        HSSFSheet sheet = workBook.getSheetAt(0);
        sheet.setZoom(5, 4);

        sheet.setMargin(Sheet.RightMargin, 0.05);
        sheet.setMargin(Sheet.LeftMargin, 0.05);
        sheet.setMargin(Sheet.BottomMargin, 0.05);
        sheet.setMargin(Sheet.TopMargin, 0.05);

        Region region = new Region(rowFrom, (short) colFrom, rowTo, (short) colTo);
        RegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, CellRangeAddress.valueOf(region.getRegionRef()), sheet, workBook);
        RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, CellRangeAddress.valueOf(region.getRegionRef()), sheet, workBook);
        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, CellRangeAddress.valueOf(region.getRegionRef()), sheet, workBook);
        sheet.addMergedRegion(region);

        HSSFCell cell = getRow(rowFrom, sheet).getCell((short) cellNumber);
        getRow(1, sheet).setHeight((short) 500);
        getRow(2, sheet).setHeight((short) 1300);

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

    private void alignEmployeeInformationToLeft(HSSFWorkbook workBook, int rowTo, int startRow) {
        HSSFSheet sheet = workBook.getSheetAt(0);
        for (int i = startRow + 2; i < rowTo; i++) {
            for (int j = 0; j < 3; j++) {
                HSSFCell cell = getRow(i, sheet).getCell((short) j);
                cell.getCellStyle().setVerticalAlignment(HSSFCellStyle.ALIGN_LEFT);
            }
        }
    }

    private void setCustomColors(HSSFWorkbook workBook) {
        HSSFPalette palette = workBook.getCustomPalette();

        palette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 79, (byte) 129, (byte) 189);
        palette.setColorAtIndex(HSSFColor.YELLOW.index, (byte) 242, (byte) 213, (byte) 3);
        palette.setColorAtIndex(HSSFColor.GREEN.index, (byte) 146, (byte) 208, (byte) 81);
        palette.setColorAtIndex(HSSFColor.LIGHT_BLUE.index, (byte) 149, (byte) 179, (byte) 215);
        palette.setColorAtIndex(HSSFColor.RED.index, (byte) 193, (byte) 0, (byte) 1);
        palette.setColorAtIndex(HSSFColor.AQUA.index, (byte) 121, (byte) 167, (byte) 229);
        palette.setColorAtIndex(HSSFColor.LIGHT_GREEN.index, (byte) 194, (byte) 215, (byte) 155);
        palette.setColorAtIndex(HSSFColor.BROWN.index, (byte) 219, (byte) 229, (byte) 241);
        palette.setColorAtIndex(HSSFColor.DARK_BLUE.index, (byte) 31, (byte) 105, (byte) 218);
    }

    private HSSFRow getRow(int rowNumber, HSSFSheet sheet) {
        return sheet.getRow(rowNumber);
    }

    private void setCellFont(HSSFCellStyle style, short size, short color, short font_weight, HSSFWorkbook workBook) {
        HSSFFont font = workBook.createFont();
        font.setFontHeightInPoints(size);
        font.setColor(color);
        font.setBoldweight(font_weight);
        style.setFont(font);
    }

    private String getDateFormat(int number) {
        return (number < 0 ? "-" : "")
                + (Math.abs(number) / 60 > 9 ? (Math.abs(number) / 60) + "" : "0" + (Math.abs(number) / 60))
                + ":"
                + (Math.abs(number) % 60 > 9 ? (Math.abs(number) % 60) + "" : "0" + (Math.abs(number) % 60));
    }


    private BigDecimal convertDateFormatToDoube(Integer number) {
        if (number == null) {
            return BigDecimal.ZERO;
        } else {
            String i = Math.abs(number) % 60 > 9 ? Math.round(Math.abs(number) % 60 / 0.6) + "" : "0" + Math.round(Math.abs(number) % 60 / 0.6);

            return new BigDecimal(Double.valueOf(Math.abs(number) / 60 + (Math.abs(number) % 60 > 0 ? "." + i : ""))).setScale(1, BigDecimal.ROUND_HALF_UP);
        }

    }

    private BigDecimal convertDateFormatToDoube(long number) {
        String i = Math.abs(number) % 60 > 9 ? Math.round(Math.abs(number) % 60 / 0.6) + "" : "0" + Math.round(Math.abs(number) % 60 / 0.6);

        return new BigDecimal(Double.valueOf(Math.abs(number) / 60 + (Math.abs(number) % 60 > 0 ? "." + i : ""))).setScale(1, BigDecimal.ROUND_HALF_UP);
    }

    private String getInOutTotal(int number) {
        return (number < 0 ? "-" : "")
                + (Math.abs(number) / 60 > 9 ? (Math.abs(number) / 60) + "" : "0" + (Math.abs(number) / 60))
                + ":"
                + (Math.abs(number) % 60 > 9 ? (Math.abs(number) % 60) + "" : "0" + (Math.abs(number) % 60));
    }

    private String getInOutTotal(Long number) {
        return (number < 0 ? "-" : "")
                + (Math.abs(number) / 60 > 9 ? (Math.abs(number) / 60) + "" : "0" + (Math.abs(number) / 60))
                + ":"
                + (Math.abs(number) % 60 > 9 ? (Math.abs(number) % 60) + "" : "0" + (Math.abs(number) % 60));
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
        String value = (lt != null && lt.getShortName() != null) ? lt.getShortName() + "/" + hour : "LL" + "/" + hour;
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

    private ExcelData getExcelTimeSlotData(String shortname, String cl, String hour) {
        if (cl != null && cl.startsWith("#")) {
            cl = cl.substring(1);
        }

        String color = cl != null ? cl : "0000CC";
        String value = (shortname != null) ? shortname + "/" + hour : "LL" + "/" + hour;

        ExcelData excel = new ExcelData(value, ExcelData.STRING, 2.2, true, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        short cb = getColor(color);
        short cf = getColor(Utils.invertColor(color));
        excel.setStyle(true);
        excel.setFontColor(cf);
        excel.setBgcolor(cb);
        excel.setFontSize(6);
        return excel;
    }

    private short getColor(String hexColor) {
        if (hexColor != null && !"".equals(hexColor)) {
            int[] colors = Utils.convertHexToRGB(hexColor);
            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFPalette palette = hwb.getCustomPalette();
            HSSFColor myColor = palette.findSimilarColor(colors[0], colors[1], colors[2]);
            return myColor.getIndex();
        }
        return 0;
    }

    private String getLeaveTypeColor(ReasonItem lt) {
        if (lt != null && lt.getHexColor() != null && !"".equals(lt.getHexColor())) {
            return lt.getHexColor();
        }
        return "ffffff";
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

    private static class LREmployee {
        public LREmployee() {

        }

        private int total = 0;
        private SortedSet<EmployeeReport> emps = new TreeSet<>(Comparator.comparing(EmployeeReport::getId));

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public SortedSet<EmployeeReport> getEmps() {
            return emps;
        }

        public void setEmps(SortedSet<EmployeeReport> emps) {
            this.emps = emps;
        }
    }

}
