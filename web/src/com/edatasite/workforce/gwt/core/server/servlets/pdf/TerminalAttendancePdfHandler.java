package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsBrigada;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeAttendanceReport;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeReport;
import com.edatasite.workforce.gwt.availability.client.rpc.FingerprintTimeDto;
import com.edatasite.workforce.gwt.core.client.ReasonItem;
import com.edatasite.workforce.gwt.core.client.enums.UnitType;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.BrigadaManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class TerminalAttendancePdfHandler extends AbstractITextPostPdfHandler implements PDFConstants {

    private final AvailabilityService availabilityService;
    private final DepartmentManager departmentManager;
    private final EmployeeManager employeeManager;
    private final ProjectManager projectManager;
    private final BrigadaManager brigadaManager;

    public TerminalAttendancePdfHandler(AvailabilityService availabilityService, DepartmentManager departmentManager, EmployeeManager employeeManager, ProjectManager projectManager, BrigadaManager brigadaManager) {
        this.availabilityService = availabilityService;
        this.departmentManager = departmentManager;
        this.employeeManager = employeeManager;
        this.projectManager = projectManager;
        this.brigadaManager = brigadaManager;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        pdfData.setBaseInvoice(baseInvoice);
        pdfData.setCustomData(customData);

        int maxMonthDays = Integer.parseInt(fp.getParams());
        getDate(fp);
        fp.setFromTerminal(true);
        fp.setStart(-1);
        fp.setVisableAll(true);
        EmployeeAttendanceReport report = availabilityService.getEmployeeAttendanceReport(fp, maxMonthDays);

        baseInvoice.setCustomProductTable(getTableData(fp, report, maxMonthDays));
        baseInvoice.setCustomNumberAndDatesTable(getCustomNumberAndDatesTable(fp, report));
        customData.put(DAYS, getHeaders(fp, maxMonthDays));
        customData.put(SUMMARY, getSummaryTable(fp, report));
        return pdfData;
    }

    private CustomisedITextTable getCustomNumberAndDatesTable(ListingFilterParameter fp, EmployeeAttendanceReport report) {
        CustomisedITextTable numAndDates = new CustomisedITextTable();
        numAndDates.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        numAndDates.addRowWithCode(MONTH, MONTH, fp.getMonthName());
        numAndDates.addRowWithCode(DATE, DATE, fp.getStartDateNC());

        if (fp.getProjectId() != null && projectManager.get(fp.getProjectId()) != null) {
            EdsProject project = projectManager.get(fp.getProjectId());
            EdsEmployee manager = project.getManager();

            numAndDates.addRowWithCode(PROJECT_NUMBER, PROJECT_NUMBER, project.getNumber());
            numAndDates.addRowWithCode(PROJECT_NAME, PROJECT_NAME, project.getName());
            numAndDates.addRowWithCode(MANAGER_NAME, MANAGER_NAME, manager != null ? manager.getFullName() : "");
        }

        if (fp.getBrigadaID() != null && brigadaManager.get(fp.getBrigadaID()) != null) {
            EdsBrigada brigada = brigadaManager.get(fp.getBrigadaID());
            numAndDates.addRowWithCode(BRIGADA_NAME, BRIGADA_NAME, brigada.getName() != null ? brigada.getName() : "");
            numAndDates.addRowWithCode(BRIGADA_NUMBER, BRIGADA_NUMBER, brigada.getNumber() != null ? brigada.getNumber() : "");
        }

        return numAndDates;
    }

    private CustomisedITextTable getSummaryTable(ListingFilterParameter fp, EmployeeAttendanceReport report) {
        CustomisedITextTable table = new CustomisedITextTable();
        table.addColumn("EMPLOYEE", "EMPLOYEE");
        table.addColumn("NORMAL_DAYS", "NORMAL_DAYS");
        table.addColumn("WORKED_DAYS", "WORKED_DAYS");
        table.addColumn("OVERTIME_DAYS", "OVERTIME_DAYS");
        table.addColumn("ACTUAL_DAYS", "ACTUAL_DAYS");

        SortedSet<String> sortedReasons = report.getLeaveTypes()
                .values()
                .stream()
                .filter(r -> r.getCode() != null)
                .sorted((r1, r2) -> {
                    if (r1.getUnitType() == null || r2.getUnitType() == null) {
                        return 0;
                    }
                    return r1.getUnitType().compareTo(r2.getUnitType());
                })
                .map(ReasonItem::getCode)
                .collect(Collectors.toCollection(TreeSet::new));

        sortedReasons.remove("LR_TYPE_DAY_OFF");
        sortedReasons.remove("LR_TYPE_HOLIDAY");
        sortedReasons.remove("LR_TYPE_RESIGNED");

        for (String code : sortedReasons) {
            ReasonItem ri = report.getLeaveTypes().get(code);
            String shortName = (ri != null && ri.getShortName() != null) ? ri.getShortName()
                    : (ri != null ? ri.getName() : code);
            table.addColumn(shortName, shortName);
        }

        table.addColumn("MONTHLY_PLANNED", "MONTHLY_PLANNED");
        table.addColumn("MONTHLY_ACTUAL", "MONTHLY_ACTUAL");
        table.addColumn(PDFConstants.ITEM_BASIC_SALARY, PDFConstants.ITEM_BASIC_SALARY);
        table.setRowsList(new ArrayList<>());

        Map<Integer, Map<String, Integer>> leaveMap = getLeaveTotalDataByEmployee(report);
        for (EmployeeReport emp : report.getEmployeeReports()) {
            Map<String, List<CellData>> row = new LinkedHashMap<>();
            row.put("EMPLOYEE", List.of(new CellData(emp.getName())));
            row.put("NORMAL_DAYS", List.of(new CellData(emp.getPlannedDays())));
            row.put("WORKED_DAYS", List.of(new CellData(emp.getWorkedDays())));
            row.put("OVERTIME_DAYS", List.of(new CellData(emp.getOvertimeDays())));
            row.put("ACTUAL_DAYS", List.of(new CellData(emp.getWorkedDays() + emp.getOvertimeDays())));

            Map<String, Integer> employeeLeaveTotals = leaveMap.get(emp.getId());
            for (String code : sortedReasons) {
                Integer value = employeeLeaveTotals != null ? employeeLeaveTotals.get(code) : null;
                ReasonItem ri = report.getLeaveTypes().get(code);
                String shortName = (ri != null && ri.getShortName() != null) ? ri.getShortName() : (ri != null ? ri.getName() : code);
                row.put(shortName, List.of(new CellData(value)));
            }

            row.put("MONTHLY_PLANNED", List.of(new CellData(String.valueOf(convertDateFormatToDouble((int) emp.getPlannedHours())))));
            row.put("MONTHLY_ACTUAL", List.of(new CellData(String.valueOf(convertDateFormatToDouble(emp.getInhour())))));
            row.put(PDFConstants.ITEM_BASIC_SALARY, List.of(new CellData(emp.getSalary() != null ? getMoneyFormat(emp.getSalary()) : "")));
            table.getRowsList().add(row);
        }
        return table;
    }

    private int calculateLeaveCount(ReasonItem reason, Integer minutes) {
        if (reason != null && reason.getUnitType() != null && reason.getUnitType().equals(UnitType.HOURLY)) {
            if (minutes == null) return 0;
            return minutes / 60;
        }
        return 1;
    }

    public Map<Integer, Map<String, Integer>> getLeaveTotalDataByEmployee(EmployeeAttendanceReport attendanceReport) {
        Map<Integer, Map<String, Integer>> result = new HashMap<>();
        if (attendanceReport == null || attendanceReport.getEmployeeReports() == null) {
            return result;
        }

        Map<String, ReasonItem> leaveTypes = attendanceReport.getLeaveTypes();
        for (EmployeeReport employee : attendanceReport.getEmployeeReports()) {
            Map<String, Integer> byCode = new HashMap<>();
            int[] al = employee.getAl();
            String[] leaveCodes = employee.getLeaveCodes();
            Integer[] inOutHour = employee.getInOutHour();

            if (al == null || leaveCodes == null) continue;
            for (int day = 1; day < al.length; day++) {
                int status = al[day];
                if (status == 1 || status == 4 || status == -1 || status == 2 || status == -2 || status == 3) {
                    String code = leaveCodes[day];
                    if (code == null) continue;
                    ReasonItem reason = leaveTypes.get(code);
                    int count = calculateLeaveCount(reason, inOutHour != null ? inOutHour[day] : null);
                    byCode.merge(code, count, Integer::sum);
                }
            }
            if (!byCode.isEmpty()) {
                result.put(employee.getId(), byCode);
            }
        }

        return result;
    }

    private CustomisedITextTable getHeaders(ListingFilterParameter fp, int maxMonthDays) {
        String userLocale = ServerUtils.getUserLocale().getLanguage();

        CustomisedITextTable daysTable = new CustomisedITextTable();
        daysTable.addColumn("DAY_NUM", "DAY_NUM");
        daysTable.addColumn("DAY_NAME", "DAY_NAME");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(fp.getStartDateNC()));

            for (int day = 1; day <= maxMonthDays; day++) {
                DayOfWeek dow = cal.getTime().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .getDayOfWeek();

                daysTable.addRow(new String[]{String.valueOf(day),  dow.getDisplayName(TextStyle.SHORT, new Locale(userLocale))});
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return daysTable;
    }

    private CustomisedITextTable getTableData(ListingFilterParameter fp, EmployeeAttendanceReport report, int maxMonthDays) {
        CustomisedITextTable table = new CustomisedITextTable();

        table.addColumn(PDFConstants.EMPLOYEE_TABLE, commonLocalizer.localize(PdfLocalizationName.employee));
        table.addColumn(PDFConstants.POSITION, commonLocalizer.localize(PdfLocalizationName.position));
        table.addColumn("IN_VALUES", commonLocalizer.localize(PdfLocalizationName.in));
        table.addColumn("OUT_VALUES", commonLocalizer.localize(PdfLocalizationName.out));
        table.addColumn("AT_VALUES", commonLocalizer.localize(PdfLocalizationName.actualShort));
        table.addColumn("LEAVE_REASON", commonLocalizer.localize(PdfLocalizationName.leaveReasons));
        table.addColumn("TIMESLOT", commonLocalizer.localize(PdfLocalizationName.timeslotCreated));
        table.addColumn("PAYABLE_TIME", commonLocalizer.localize(PdfLocalizationName.timeslotCreated));
        table.addColumn("TOTAL", commonLocalizer.localize(PdfLocalizationName.total));
        table.addColumn("LATE_RATE", commonLocalizer.localize(PdfLocalizationName.lateRate));
        table.addColumn("EARLY_RATE", commonLocalizer.localize(PdfLocalizationName.earlyRate));
        table.setRowsList(new ArrayList<>());

        Map<Integer, Map<Integer, FingerprintTimeDto>> data = report.getFingerprintTimeDtoMap();
        for (EmployeeReport employee : report.getEmployeeReports()) {
            Map<String, List<CellData>> row = new LinkedHashMap<>();

            row.put(PDFConstants.EMPLOYEE_TABLE, List.of(new CellData(employee.getName())));
            row.put(PDFConstants.POSITION, List.of(new CellData(employee.getPosition())));

            List<CellData> inList = new ArrayList<>();
            List<CellData> outList = new ArrayList<>();
            List<CellData> atList = new ArrayList<>();
            List<CellData> lvrList = new ArrayList<>();
            List<CellData> timeslotList = new ArrayList<>();
            List<CellData> payableTimeList = new ArrayList<>();

            int totalActualHours = 0;
            Map<Integer, FingerprintTimeDto> employeeMap = data.get(employee.getId());
            for (int day = 1; day <= maxMonthDays; day++) {
                FingerprintTimeDto dto = employeeMap != null ? employeeMap.get(day) : null;
                lvrList.add(getLeaveReason(fp, report, employee, day));

                int lateMins = employee.getTimeslotItem() != null ? employee.getTimeslotItem().getLateMinutes() : 0;
                timeslotList.add(new CellData(String.valueOf(employee.getTimeslotOverallMins()[day])));

                int payableMinutes = 0;
                if (dto != null && dto.getActualtime() != null) {
                    int actualMinutes = dto.getActualtime();
                    int lunchMinutes = employee.getTimeslotLunchMins()[day];
                    int actualWorkMinutes = Math.max(0, actualMinutes - lunchMinutes);
                    int plannedStart = employee.getTimeslotStartMins()[day];
                    int plannedEnd = employee.getTimeslotEndMins()[day];
                    int actualStart = dto.getIntime();
                    int actualEnd = dto.getOuttime() != null ? dto.getOuttime() : 0;

                    if (actualStart > plannedStart && actualStart - plannedStart > lateMins) {
                        actualWorkMinutes -= (actualStart - plannedStart);
                    } else if (actualStart > plannedStart) {
                        actualWorkMinutes += (actualStart - plannedStart);
                    }

                    int eveningMinutes = 0;
                    if (actualEnd > plannedEnd) {
                        eveningMinutes = actualEnd - plannedEnd;
                    }

                    payableMinutes = Math.max(0, actualWorkMinutes - eveningMinutes);
                }

                payableTimeList.add(new CellData(String.valueOf(payableMinutes)));

                if (dto != null) {
                    inList.add(new CellData(getTime(dto.getIntime())));
                    outList.add(new CellData(getTime(dto.getOuttime())));
                    atList.add(new CellData(getTime(dto.getActualtime())));

                    totalActualHours = totalActualHours + (dto.getActualtime() != null ? dto.getActualtime() : 0);
                } else {
                    inList.add(new CellData(""));
                    outList.add(new CellData(""));
                    atList.add(new CellData(""));
                }
            }

            row.put("IN_VALUES", inList);
            row.put("OUT_VALUES", outList);
            row.put("AT_VALUES", atList);
            row.put("LEAVE_REASON", lvrList);
            row.put("TIMESLOT", timeslotList);
            row.put("PAYABLE_TIME", payableTimeList);

            int hours = totalActualHours / 60;
            int minutes = totalActualHours % 60;
            row.put("TOTAL", List.of(new CellData(String.format("%d:%02d", hours, minutes))));

            Double[] lateEarlyPercent = employee.getLateEarlyPercent();
            if (lateEarlyPercent == null) {
                lateEarlyPercent = new Double[]{null, null};
            }
            String latePercent = lateEarlyPercent[0] != null ? (lateEarlyPercent[0] != 0 ? lateEarlyPercent[0].intValue() : "0") + "%" : "N/A";
            String earlyPercent = lateEarlyPercent[1] != null ? (lateEarlyPercent[1] != 0 ? lateEarlyPercent[1].intValue() : "0") + "%" : "N/A";
            row.put("LATE_RATE", List.of(new CellData(latePercent)));
            row.put("EARLY_RATE", List.of(new CellData(earlyPercent)));
            table.getRowsList().add(row);
        }

        return table;
    }

    private CellData getLeaveReason(ListingFilterParameter fp, EmployeeAttendanceReport report, EmployeeReport employee, int i) {
        int[] al = employee.getAl();
        int[] withHoliday = employee.getWithHoliday();
        int[] leaveRequestHoliday = employee.getLeaveRequestHolidays();
        CellData dayData = null;
        Date date = fp.getStartDate();

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(fp.getStartDate());
        boolean resignedMonth = employee.getResignationDay() != null && employee.getResignationDay().getMonth() == date.getMonth() && employee.getResignationDay().getYear() == date.getYear();

        HashMap<String, ReasonItem> leaveTypes = report.getLeaveTypes();

        if (!resignedMonth || employee.getResignationDay().getDate() >= i) {
            int tempHour = employee.getInOutHour()[i] != null ? employee.getInOutHour()[i] : 0;
            String code = employee.getLeaveCodes()[i];

            switch (al[i]) {
                case 1, -1, 2, -2, 3: {
                    dayData = getLeaveData(leaveTypes.get(code));
                    break;
                }

                case -3: {
                    dayData = getLeaveData(leaveTypes.get("LR_TYPE_HOLIDAY"));
                    break;
                }

                default: {
                    dayData = getDayData(report, employee, withHoliday, leaveRequestHoliday, tempHour, i);
                }
            }

            LocalDateTime dateTime = LocalDateTime.of(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH) + 1, i, 0, 0);
            Map<Date, Integer> exceptionalTimeSlotDates = employee.getExceptionalTimeSlotDates();
            Integer timeSlot = exceptionalTimeSlotDates.get(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));
        }

        return dayData;
    }

    private CellData getLeaveData(ReasonItem lt) {
        return new CellData(((lt != null && lt.getShortName() != null) ? lt.getShortName() : "LL"), "");
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
                dayData = new CellData(employee.getTimeSlotId()[index], "");
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
                dayData = new CellData(employee.getTimeSlotId()[index], "");
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

    private BigDecimal convertDateFormatToDouble(Integer number) {
        if (number == null) {
            return BigDecimal.ZERO;
        } else {
            String i = Math.abs(number) % 60 > 9 ? Math.round(Math.abs(number) % 60 / 0.6) + "" : "0" + Math.round(Math.abs(number) % 60 / 0.6);
            return BigDecimal.valueOf(Double.valueOf(Math.abs(number) / 60 + (Math.abs(number) % 60 > 0 ? "." + i : ""))).setScale(1, RoundingMode.HALF_UP);
        }
    }

    private String convertDateFormatToString(long number) {
        BigDecimal bigDecimal = convertDateFormatToDouble( (int) number);
        int i = bigDecimal.intValue();
        BigDecimal bigDecimal1 = BigDecimal.valueOf(Double.parseDouble(String.valueOf(i)));
        if (bigDecimal1.compareTo(bigDecimal) == 0) {
            return String.valueOf(i);
        } else {
            return bigDecimal.toString();
        }
    }

    private String getTime(Integer time) {
        return time != null
                ? time / 60 + ":" + (time % 60 < 10 ? "0" : "") + time % 60 : "";
    }

    private void getDate(ListingFilterParameter fp) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(fp.getStartDateNC()));

            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);

            Date startDate = cal.getTime();

            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);

            Date endDate = cal.getTime();

            String newStart = sdf.format(startDate);
            String newEnd = sdf.format(endDate);

            fp.setStartDateNC(newStart);
            fp.setEndDateNC(newEnd);

        } catch (ParseException e) {
            logger.error("Error parsing date", e);
        }
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName((user != null ? user.getName() + "_" + user.getLastName() : "") + dateFormat(new Date()));
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        return fp.getMonthName() + " - " + commonLocalizer.localize(PdfLocalizationName.terminalAttendance);
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.TERMINAL_ATTENDANCE;
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
        if (approverUniqueId != null && verifyUrl != null && !approverUniqueId.equals("")) {
            EdsEmployee approver = employeeManager.getEmployeeByNumber(approverUniqueId);
            fp.setApproverID(approver != null ? approver.getObjectID() : null);
            fp.setURL(verifyUrl);
        }
        return fp;
    }
}