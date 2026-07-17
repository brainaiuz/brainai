package com.edatasite.workforce.gwt.availability.client.ui.view.attendanceTable;

import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeReport;
import com.edatasite.workforce.gwt.availability.client.rpc.FingerprintTimeDto;
import com.edatasite.workforce.gwt.availability.client.ui.view.editFingerprint.FingerprintAdjustmentPopup;
import com.edatasite.workforce.gwt.core.client.ReasonItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AttendanceReportLogItem;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeePresentItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.UserFingerPrintAdjustment;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateTimePicker;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 08.09.2009
 * Time: 17:25:02
 * To change this template use File | Settings | File Templates.
 */
public class AttendanceTableDataBeta extends Widget implements AttendanceTableHtmlTags {


    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final DataListBox reasons;
    private int currentMonth;
    private final String monthYear;
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private KpiModal box;
    private DateTimePicker dateTimeInputs;
    private Date date;
    private Date startDate;
    private Date endDate;
    private final boolean holidayInclude;
    private final DateTimeFormat df = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm");
    private final DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd/MM/yyyy");
    public static NumberFormat numberFormat = NumberFormat.getFormat("#.00");
    private Integer id;
    private String dateItem;
    private boolean isBoxOpen = false;
    private boolean isNavBox = false;
    private Integer timeslotId;
    private boolean isFromTerminal = false;
    private HashMap<Integer, String> images = new HashMap<>();
    private HashMap<Integer, String> positions = new HashMap<>();
    private HashMap<Integer, String> employeeNames = new HashMap<>();
    private boolean hasActionAdjust = Utils.hasPermission(PermissionConstants.HRMS_ADD_ATTENDANCE_TABLE_DATA);
    private boolean enableActualInOut = Utils.hasGenericAccess(GenericSettingsEnum.TERMINAL_ACTUAL_IN_OUT_ENABLE);
    private boolean timeSLotActualEnabled = Utils.hasGenericAccess(GenericSettingsEnum.TERMINAL_TIME_SLOT_ACTUAL_ENABLE);

    AttendanceTableDataBeta(int monthDays, String monthYear, Date date, SelectItem[] reasonItems, boolean holidayInclude) {
        this.holidayInclude = holidayInclude;
        this.currentMonth = monthDays;
        this.monthYear = monthYear;
        this.date = date;
        reasons = new DataListBox();

        if (reasonItems != null && reasonItems.length > 0) {
            reasons.setItems(reasonItems);
        }

        createEditPopup();
        initLeaveRequestPopup();
    }

    AttendanceTableDataBeta(int monthDays, String monthYear, Date date, Date startDate, Date endDate, SelectItem[] reasonItems, boolean holidayInclude) {
        this.holidayInclude = holidayInclude;
        this.currentMonth = monthDays;
        this.monthYear = monthYear;
        this.date = date;
        this.startDate = startDate;
        this.endDate = endDate;
        reasons = new DataListBox();

        if (reasonItems != null && reasonItems.length > 0) {
            reasons.setItems(reasonItems);
        }
        createEditPopup();
        initLeaveRequestPopup();
    }

    private void createEditPopup() {
        editAttendanceReport();
        runFingerPrintPopup();
        runDailyReport();
        box = new KpiModal();
        box.setWidth(400);
        box.setDismissible(true);
        box.addStyleName("attendance_report_modal");
        FlexTable content = new FlexTable();
        content.addStyleName("tbl-form");

        HTML htmlCheckIn = new HTML("<b>" + wfmStrings.checkedIn() + "</b>");
        content.setWidget(0, 0, htmlCheckIn);

        HTML htmlCheckOut = new HTML("<b>" + wfmStrings.checkedOut() + "</b>");
        content.setWidget(1, 0, htmlCheckOut);


        dateTimeInputs = new DateTimePicker(false, true);

        content.setWidget(0, 1, new InputGroup(dateTimeInputs.getStartDatePicker(), dateTimeInputs.getStartTime()));
        dateTimeInputs.startTime.setEnabled(true);
        dateTimeInputs.startTime.setVisible(true);

        content.setWidget(1, 1, new InputGroup(dateTimeInputs.getDueDatePicker(), dateTimeInputs.getEndTime()));
        dateTimeInputs.endTime.setEnabled(true);
        dateTimeInputs.endTime.setVisible(true);

        dateTimeInputs.startDate.setEnabled(false);
        dateTimeInputs.startDate.setTextBoxEnabled(false);

        WfmButton2 btnSave = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        btnSave.addClickHandler(x -> {
            Date startTime = null;
            Date endTime = null;
            try {
                startTime = df.parse(dateItem + " " + dateTimeInputs.startTime.getText());
                endTime = df.parse(dateItem + " " + dateTimeInputs.endTime.getText());
                if (startTime.after(endTime)) {
                    Info.show("Start time should be greater than end time", Info.Type.WARNING);
                    return;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("time is null");
            }
            DateNonConvertable date = dateItem != null ? new DateNonConvertable(dateFormat.parse(dateItem)) : null;
            DateNonConvertable startDate = startTime != null ? new DateNonConvertable(startTime) : null;
            DateNonConvertable enddate = endTime != null ? new DateNonConvertable(endTime) : null;
            EmployeePresentItem item = new EmployeePresentItem(id, date, startDate, enddate, reasons.getSelectedId(), null, timeslotId);
            CommonService.App.get().saveAttendanceHour(item,
                    new AbstractAsyncCallback<Integer>() {
                        @Override
                        public void failure(Throwable throwable) {
                            Info.warn("Unable to save");
                        }

                        @Override
                        public void success(Integer i) {
                            if (i.equals(Constants.VALIDATION)) {
                                Info.show("User already has a request for this period", Info.Type.WARNING);
                            } else if (i.equals(Constants.WARNING)) {
                                Info.show("Unable to save", Info.Type.WARNING);
                            } else if (i.equals(Constants.INFO)) {
                                Info.show("You can't add LR for non working days", Info.Type.WARNING);
                            } else {
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LEAVE_REQUEST_STATUS_CHANGED, null, AttendanceTableDataBeta.this);
                                Info.show("Successfully saved", Info.Type.INFO);
                            }
                            setToTable(String.valueOf(id), String.valueOf(dateItem), String.valueOf(dateTimeInputs.startTime.getText()), String.valueOf(dateTimeInputs.endTime.getText()));
                        }
                    }
            );
            box.close();
        });
        content.setWidget(2, 0, new HTML("<b>" + wfmStrings.markAs() + "<b/>"));
        content.setWidget(2, 1, reasons);
        box.add(content);
        box.addCloseHandler(clickEvent -> isBoxOpen = false);

        WfmButton2 btnClose = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT);
        btnClose.addClickHandler(clickEvent -> {
            box.close();
        });
        box.addButton(btnClose);
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_ATTENDANCE_TABLE_DATA)) {
            box.addButton(btnSave);
        } else if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_ATTENDANCE_TABLE_DATA)) {
            box.addButton(btnSave);
        }
    }

    public void setCurrentMonth(int currentMonth) {
        this.currentMonth = currentMonth;
    }

    public int getCurrentMonth() {
        return this.currentMonth;
    }

    public String getEmployeeTitle() {
        StringBuilder html = new StringBuilder();
        for (int i = 1; i <= currentMonth; i++) {
            Date tempDate = date;
            tempDate.setDate(i);
            DateTimeFormat weekDayFormat = DateTimeFormat.getFormat("E");
            DateTimeFormat day = DateTimeFormat.getFormat("dd");
            DateTimeFormat month = DateTimeFormat.getFormat("MMM");
            String weekDayText = weekDayFormat.format(tempDate);
            String dayFormat = day.format(tempDate);
            String monthFormat = month.format(tempDate);


            String monthDaySpan = "<strong>" + monthFormat + "-" + dayFormat + "</strong>";
            String weekDaySpan = "<small>" + weekDayText + "</span>";
            String wrappedContent = "<span class=\"daySlot\">" + monthDaySpan + weekDaySpan + "</span>";
            html.append(TH_BEGIN.replaceAll(CLASS_NAME, EMPLOYEE_LAEBL_MONTH_DAY))
                    .append(wrappedContent)
                    .append(TH_END);
        }
        return html.toString();
    }

    public String getTerminalEmployeeTitle(Date startDate, Date endDate) {
        StringBuilder html = new StringBuilder();
        Date tempDate = new Date(startDate.getTime());

        // Получаем текущую дату для сравнения (без времени)
        Date today = new Date();

        DateTimeFormat weekDayFormat = DateTimeFormat.getFormat("E");
        DateTimeFormat day = DateTimeFormat.getFormat("dd");
        DateTimeFormat month = DateTimeFormat.getFormat("MMM");

        while (tempDate.before(endDate) || tempDate.equals(endDate)) {
            // Проверка на текущий день
            boolean isCurrentDay = tempDate.getDate() == today.getDate() &&
                    tempDate.getMonth() == today.getMonth() &&
                    tempDate.getYear() == today.getYear();

            String weekDayText = weekDayFormat.format(tempDate);
            String dayFormat = day.format(tempDate);
            String monthFormat = month.format(tempDate);

            String monthDaySpan = "<strong>" + monthFormat + "-" + dayFormat + "</strong>";
            String weekDaySpan = "<small>" + weekDayText + "</small>";
            String wrappedContent = "<span class=\"daySlot\">" + monthDaySpan + weekDaySpan + "</span>";

            // Формируем классы. Убираем style и добавляем dayCol-end.
            // Добавляем current-day если совпало.
            String classes = "stickerCell col-group-day dayCol-end" + (isCurrentDay ? " current-day" : "");

            String TH_BEGIN_BORDER = "<th class=\"" + classes + "\">";

            String thWithColspan = TH_BEGIN_BORDER
                    .replaceAll(CLASS_NAME, EMPLOYEE_LAEBL_MONTH_DAY)
                    .replace("<th", "<th colspan=\"3\""); // instead of replaceAll, for more specific selection

            html.append(thWithColspan)
                    .append(wrappedContent)
                    .append("</th>"); // Заменяем TH_END на обычный закрывающий тег th, так как в TH_END может быть закрывающий span frame-affix-top

            tempDate.setDate(tempDate.getDate() + 1);
        }

        return html.toString();
    }


    public String getSubTitleRow(Date startDate, Date endDate) {
        StringBuilder html = new StringBuilder();
        // Убрали TH_BEGIN_BORDER внутрь цикла, чтобы управлять классами

        Date tempDate = new Date(startDate.getTime());
        Date _now = new Date(); // Берем текущую дату один раз перед циклом

        while (tempDate.before(endDate) || tempDate.equals(endDate)) {
            // Проверка: является ли день в итерации сегодняшним
            boolean isTrulyToday = (tempDate.getDate() == _now.getDate()) &&
                    (tempDate.getMonth() == _now.getMonth()) &&
                    (tempDate.getYear() == _now.getYear());
            //String currentDayStyle = isTrulyToday ? (" " + CURRENT_DAY_STALY) : "";
            String currentDayStyle = isTrulyToday ? " current-day" : "";

            // Для "IN"
            html.append("<th class=\"stickerCell time-in-header" + currentDayStyle + "\">")
                    .append(wfmStrings.in())
                    .append(TH_END);

            // Для "OUT"
            html.append("<th class=\"stickerCell time-out-header" + currentDayStyle + "\">")
                    .append(wfmStrings.out())
                    .append(TH_END);

            // Для "Actual" (добавляем dayCol-end для границы)
            html.append("<th class=\"stickerCell dayCol-end time-actual-header" + currentDayStyle + "\"><span class=\"frame_affix_top\">")
                    .append(wfmStrings.actualShort())
                    .append(TH_END);

            tempDate.setDate(tempDate.getDate() + 1);
        }
        return "<tr>" + html.toString() + "</tr>";
    }


    public String getEmployeesAttendanceReport(EmployeeReport employee, HashMap<String, ReasonItem> leaveTypes, HashMap<Integer, HashMap<String, AttendanceTableBeta.LREmployee>> leaveTotalData, HashMap<Integer, HashMap<String, Integer>> leaveTotalDataByEmployee) {
        int[] al = employee.getAl();
        int[] withHoliday = employee.getWithHoliday();
        String employeeName = employee.getName();
        String employeeCode = employee.getCode() != null && !"".equals(employee.getCode()) ? (employee.getCode()) : "";
        Date employeeResignation = employee.getResignationDay();

        String employeeImage = employee.getPhotoUrl();

        StringBuilder positionHtml = new StringBuilder();
        if (Utils.hasGenericAccess(GenericSettingsEnum.POSITION_SHOW_IN_ATTENDANCE_REPORT)) {
            positionHtml.append("<dt>").append(wfmStrings.position()).append("</dt>").append("<dd>").append(employee.getPosition()).append("</dd>");
        }
        StringBuilder html;

        HashMap<String, Integer> tooltipMap = new HashMap<String, Integer>();

        for (int i = 1; i <= currentMonth; i++) {
            if (al[i] == 1) {
                String code = employee.getLeaveCodes()[i];
                int count = tooltipMap.getOrDefault(code, 0);
                tooltipMap.put(code, count + 1);
            }
        }
        int totalHour = employee.getInhour();
        long plannedHour = employee.getPlannedHours();
        int plannedDays = employee.getPlannedDays();
        int workedDays = employee.getWorkedDays();
        int overtimeDays = employee.getOvertimeDays();

        String plannedHours = (Math.abs(plannedHour) / 60 > 9 ? (Math.abs(plannedHour) / 60) + "" : "0" + (Math.abs(plannedHour) / 60));
        String actualHours = (Math.abs(totalHour) / 60 > 9 ? (Math.abs(totalHour) / 60) + "" : "0" + (Math.abs(totalHour) / 60));

        StringBuilder employeeNameWidget = new StringBuilder();
        employeeNameWidget.append("<dl class='attRepTbl__emplNm'>").append("<dt>").append(employeeCode).append("</dt>").append("<dd>").append(employeeName).append("</dd>").append(positionHtml).append("</dl>").append("<dl class='attRepTbl_emlTltp'>");
        employeeNameWidget.append("<dt>").append(wfmStrings.normalDays()).append("</dt> <dd>").append(plannedDays).append("</dd>");
        employeeNameWidget.append("<dt>").append(wfmStrings.actualDays()).append("</dt> <dd>").append(workedDays + overtimeDays).append("</dd>");
        employeeNameWidget.append("<dt>").append(wfmStrings.monthlyPlanned()).append("</dt> <dd>").append(plannedHours).append("</dd>");
        employeeNameWidget.append("<dt>").append(wfmStrings.monthlyActual()).append("</dt> <dd>").append(actualHours).append("</dd>");
        tooltipMap.forEach((key, value) -> {
            String shortCode = leaveTypes.get(key).getShortName();
            employeeNameWidget.append("<span>" + shortCode + " - " + value + "</span>");
        });
        employeeNameWidget.append("</dl></span>");

        if (Utils.hasPermission(PermissionConstants.HRMS_EMPLOYEE_PROFILE) || Utils.isSettings()) {
            // TODO: change the way the link is generated
            html = new StringBuilder("<td class='employeeNameCell th-y--lg'>" + "<a href=\"#employeeProfile%7CemployeeProfileView/" + employee.getId() + "\">" + "<span class='tblUserImg' style='background-image: url(\"" + employeeImage + "\")''></span>" + employeeNameWidget + "</a>" + TD_END);
        } else {
            html = new StringBuilder("<td class='employeeNameCell th-y--lg'>" + "<span class='tblUserImg' style='background-image: url(\"" + employeeImage + "\")''></span>" + employeeNameWidget + TD_END);
        }

        Date _date = new Date();
        int _day = _date.getDate();
        boolean _future_period = date != null && (date.getYear() > _date.getYear() || (date.getYear() == _date.getYear() && date.getMonth() > _date.getMonth()));
        boolean this_month = date != null && (date.getYear() == _date.getYear() && date.getMonth() == _date.getMonth());

// 1. Предварительные расчеты перед циклом (оптимизация)
        Date _now = new Date();
        int _todayReal = _now.getDate();
        int _currentMonthNow = _now.getMonth();
        int _currentYearNow = _now.getYear();

        for (int i = 1; i <= currentMonth; i++) {
            // 2. Ваша надежная проверка на "сегодня" (день + месяц + год)
            boolean isTrulyToday = (i == _todayReal) &&
                    (date != null && date.getMonth() == _currentMonthNow) &&
                    (date != null && date.getYear() == _currentYearNow);

            // 3. Формируем стиль, используя константу коллеги (с пробелом!)
            String currentDayStyle = isTrulyToday ? (" " + CURRENT_DAY_STALY) : "";

            // 4. Технические переменные (ваши + коллеги)
            boolean future = _future_period || (this_month && i > _day);
            boolean currentDate = (i == (date != null ? date.getDate() : 0));

            int tempHour = employee.getInOutHour()[i] == null ? 0 : employee.getInOutHour()[i];
            String value = String.valueOf(tempHour / 60);
            String shortname = employee.getTimeSlotId()[i];

            // 5. Логика отрисовки ячеек (использует объединенный currentDayStyle)
            if (al[i] == 1 || al[i] == 4) {
                String code = employee.getLeaveCodes()[i];
                html.append(getLeaveTypeCell(leaveTypes.get(code), WITH_LR_STYLE + currentDayStyle + " has-actions", employee, i, true, false));
                fillLeaveTotalData(leaveTotalData, i, code, employee, leaveTotalDataByEmployee);
            } else if (employeeResignation != null && date.getMonth() == employeeResignation.getMonth() && date.getYear() == employeeResignation.getYear() && employeeResignation.getDate() == i) {
                html.append(getLeaveTypeCell(leaveTypes.get("LR_TYPE_RESIGNED"), WITHOUT_LR_STYLE + currentDayStyle, employee, i, false, false));
                fillLeaveTotalData(leaveTotalData, i, "LR_TYPE_RESIGNED", employee, leaveTotalDataByEmployee);
            } else if (al[i] == -1) {
                String code = employee.getLeaveCodes()[i];
                html.append(getLeaveTypeCell(leaveTypes.get(code), WITHOUT_LR_STYLE + currentDayStyle + " has-actions", employee, i, true, false));
                fillLeaveTotalData(leaveTotalData, i, code, employee, leaveTotalDataByEmployee);
            } else if (al[i] == 2 || al[i] == -2) {
                String code = employee.getLeaveCodes()[i];
                html.append(getLeaveTypeCell(leaveTypes.get(code), WITH_LR_STYLE + currentDayStyle + " has-actions", employee, i, true, false));
                fillLeaveTotalData(leaveTotalData, i, code, employee, leaveTotalDataByEmployee);
            } else if (al[i] == 3) {
                String code = employee.getLeaveCodes()[i];
                if (future) {
                    html.append(getLeaveTypeCell(leaveTypes.get(code), WITH_LR_STYLE + currentDayStyle + " has-actions", employee, i, true, false));
                } else {
                    html.append(getLeaveTypeCellHourly(leaveTypes.get(code), WITH_LR_STYLE + currentDayStyle + " has-actions", employee, i, value));
                }
            } else if (employee.isHasShift()) {
                if (withHoliday[i] == 2) {
                    html.append(getLeaveTypeCell(leaveTypes.get("LR_TYPE_HOLIDAY"), WITHOUT_LR_STYLE + currentDayStyle, employee, i, true, true));
                } else if (employee.getInOutHour()[i] == null) {
                    html.append(getWorkingDayTypeCell(0, currentDayStyle, employee, i, value, shortname));
                } else if (tempHour == 0) {
                    if (withHoliday[i] == 1 || employee.getLeaveRequestHolidays()[i] == 3) {
                        html.append(getLeaveTypeCell(leaveTypes.get("LR_TYPE_HOLIDAY"), WITHOUT_LR_STYLE + currentDayStyle, employee, i, true, true));
                    } else {
                        html.append(getLeaveTypeCell(leaveTypes.get("LR_TYPE_DAY_OFF"), SUNDAY_MONTH_STYLE + currentDayStyle, employee, i, true, true));
                    }
                } else if (future) {
                    html.append(getFutureWorkingDayTypeCell(tempHour, currentDayStyle, employee, i, value, shortname));
                } else {
                    html.append(getWorkingDayTypeCell(tempHour, currentDayStyle, employee, i, value, shortname));
                }
            } else if (employee.getLeaveRequestHolidays()[i] == 1) {
                if (tempHour == 0) {
                    html.append(getLeaveTypeCell(leaveTypes.get("LR_TYPE_DAY_OFF"), SUNDAY_MONTH_STYLE + currentDayStyle, employee, i, true, true));
                } else if (future) {
                    html.append(getFutureWorkingDayTypeCell(tempHour, currentDayStyle, employee, i, value, shortname));
                } else {
                    html.append(getWorkingDayTypeCell(tempHour, currentDayStyle, employee, i, value, shortname));
                }
            } else if (employee.getLeaveRequestHolidays()[i] == 2) {
                if (tempHour == 0) {
                    if (holidayInclude && !future) {
                        html.append(TD_BEGIN.replaceAll(CLASS_NAME, WORK_MONTH_HOLIDAY_STYLE + currentDayStyle)).append(getClickableValue(employee, i, "0")).append(TD_END);
                    } else {
                        html.append(TD_BEGIN.replaceAll(CLASS_NAME, WORK_MONTH_HOLIDAY_STYLE + currentDayStyle)).append("&nbsp;").append(TD_END);
                    }
                } else if (future) {
                    html.append(getFutureWorkingDayTypeCell(tempHour, currentDayStyle, employee, i, value, shortname));
                } else {
                    html.append(getWorkingDayTypeCell(tempHour, currentDayStyle, employee, i, value, shortname));
                }
            } else if (employee.getLeaveRequestHolidays()[i] == 3) {
                if (tempHour == 0) {
                    html.append(getLeaveTypeCell(leaveTypes.get("LR_TYPE_HOLIDAY"), WITHOUT_LR_STYLE + currentDayStyle, employee, i, true, true));
                } else if (!future) {
                    html.append(getWorkingDayTypeCell(tempHour, currentDayStyle, employee, i, value, shortname));
                } else {
                    html.append(getFutureWorkingDayTypeCell(tempHour, currentDayStyle, employee, i, value, shortname));
                }
            } else if (withHoliday[i] == 1 || withHoliday[i] == 2) {
                if (tempHour == 0) {
                    html.append(getLeaveTypeCell(leaveTypes.get("LR_TYPE_HOLIDAY"), WITHOUT_LR_STYLE + currentDayStyle, employee, i, true, true));
                } else if (!future) {
                    html.append(getWorkingDayTypeCell(tempHour, currentDayStyle, employee, i, value, shortname));
                } else {
                    html.append(getFutureWorkingDayTypeCell(tempHour, currentDayStyle, employee, i, value, shortname));
                }
            } else if (!future) {
                html.append(getWorkingDayTypeCell(tempHour, currentDayStyle, employee, i, value, shortname));
            } else {
                html.append(getFutureWorkingDayTypeCell(tempHour, currentDayStyle, employee, i, value, shortname));
            }
        }
        //Xullas betta nima logica qilinganini bilmadim. Umumiy logika, UIda har bir kundagi hoours yig'indisi totalga teng bo'lishi kerak. Hozir excelda to'gri chiqyapti, lekin UIda noto'gri
        html.append(TD_BEGIN.replaceAll(CLASS_NAME, IN_HOUR_OVERTIME_STYLE)).append(plannedDays < 0 ? "-" : "").append(plannedDays).append(TD_END);
        html.append(TD_BEGIN.replaceAll(CLASS_NAME, IN_HOUR_OVERTIME_STYLE)).append(overtimeDays + workedDays < 0 ? "-" : "").append((overtimeDays + workedDays)).append(TD_END);
        html.append(TD_BEGIN.replaceAll(CLASS_NAME, IN_HOUR_OVERTIME_STYLE)).append(plannedHours).append(TD_END);
        html.append(TD_BEGIN.replaceAll(CLASS_NAME, IN_HOUR_OVERTIME_STYLE)).append(actualHours).append(TD_END);
        return html.toString();
    }


    public String getTerminalEmployeesAttendanceReport(EmployeeReport employee, HashMap<String, ReasonItem> leaveTypes, HashMap<Integer, HashMap<String, AttendanceTableBeta.LREmployee>> leaveTotalData, HashMap<Integer, HashMap<String, Integer>> leaveTotalDataByEmployee, Map<Integer, Map<Integer, FingerprintTimeDto>> fingerprintTimeDtoMap, Date startDate, Date endDate) {
        isFromTerminal = true;
        int[] al = employee.getAl();
        int[] withHoliday = employee.getWithHoliday();
        HashMap<String, Integer> monthHolidaysByPeriod = employee.getMonthHolidaysByPeriod();
        String employeeName = employee.getName();
        String employeeCode = employee.getCode() != null && !"".equals(employee.getCode()) ? (employee.getCode()) : "";
        Date employeeResignation = employee.getResignationDay();

        String employeeImage = employee.getPhotoUrl();
        images.put(employee.getId(), employee.getPhotoUrl());
        positions.put(employee.getId(), employee.getPosition());
        employeeNames.put(employee.getId(), employeeName);

        StringBuilder positionHtml = new StringBuilder();
        positionHtml.append("<dt>").append(wfmStrings.position()).append("</dt>").append("<dd>").append(employee.getPosition()).append("</dd>");

        StringBuilder html;

        HashMap<String, Integer> tooltipMap = new HashMap<String, Integer>();

        Date tempDate = new Date(startDate.getTime());

        while (!tempDate.after(endDate)) {
            int dayOfMonth = tempDate.getDate();
            if (al[dayOfMonth] == 1) {
                String code = employee.getLeaveCodes()[dayOfMonth];
                int count = tooltipMap.getOrDefault(code, 0);
                tooltipMap.put(code, count + 1);
            }
            tempDate.setDate(tempDate.getDate() + 1);
        }
        int totalHour = employee.getInhour();
        long plannedHour = employee.getPlannedHours();
        int plannedDays = employee.getPlannedDays();
        int workedDays = employee.getWorkedDays();
        int overtimeDays = employee.getOvertimeDays();
        Double[] lateEarlyPercent = employee.getLateEarlyPercent();
        String latePercent = lateEarlyPercent[0] != null ? (lateEarlyPercent[0] != 0 ? lateEarlyPercent[0].intValue() : "0") + " %" : "N/A";
        String earlyPercent = lateEarlyPercent[1] != null ? (lateEarlyPercent[1] != 0 ? lateEarlyPercent[1].intValue() : "0") + " %" : "N/A";


        String plannedHours = (Math.abs(plannedHour) / 60 > 9 ? (Math.abs(plannedHour) / 60) + "" : "0" + (Math.abs(plannedHour) / 60));
        String actualHours = (Math.abs(totalHour) / 60 > 9 ? (Math.abs(totalHour) / 60) + "" : "0" + (Math.abs(totalHour) / 60));

        StringBuilder employeeNameWidget = new StringBuilder();
        employeeNameWidget.append("<dl class='attRepTbl__emplNm'>").append("<dt>").append(employeeCode).append("</dt>").append("<dd>").append(employeeName).append("</dd>").append(positionHtml).append("</dl>").append("<dl class='attRepTbl_emlTltp'>");

        employeeNameWidget.append("<dt>").append(wfmStrings.normalDays()).append("</dt> <dd>").append(plannedDays).append("</dd>");
        employeeNameWidget.append("<dt>").append(wfmStrings.actualDays()).append("</dt> <dd>").append(workedDays + overtimeDays).append("</dd>");
        employeeNameWidget.append("<dt>").append(wfmStrings.monthlyPlanned()).append("</dt> <dd>").append(plannedHours).append("</dd>");
        employeeNameWidget.append("<dt>").append(wfmStrings.monthlyActual()).append("</dt> <dd>").append(actualHours).append("</dd>");
        employeeNameWidget.append("<dt>").append(wfmStrings.lateRate()).append("</dt> <dd>").append(latePercent).append("</dd>");
        employeeNameWidget.append("<dt>").append(wfmStrings.earlyRate()).append("</dt> <dd>").append(earlyPercent).append("</dd>");
        tooltipMap.forEach((key, value) -> {
            String shortCode = leaveTypes.get(key).getShortName();
            employeeNameWidget.append("<span>" + shortCode + " - " + value + "</span>");
        });
        employeeNameWidget.append("</dl></span>");

        if (Utils.hasPermission(PermissionConstants.HRMS_EMPLOYEE_PROFILE) || Utils.isSettings()) {
            html = new StringBuilder("<td class='employeeNameCell th-y--lg'>" + "<a href=\"#employeeProfile%7CemployeeProfileView/" + employee.getId() + "\">" + "<span class='tblUserImg' style='background-image: url(\"" + employeeImage + "\")''></span>" + employeeNameWidget + "</a>" + TD_END);
        } else {
            html = new StringBuilder("<td class='employeeNameCell th-y--lg'>" + "<a>" + "<span class='tblUserImg' style='background-image: url(\"" + employeeImage + "\")''></span>" + employeeNameWidget + "</a>" + TD_END);
        }

        Date _date = new Date();
        int _day = _date.getDate();
        boolean _future_period = date != null && (date.getYear() > _date.getYear() || (date.getYear() == _date.getYear() && date.getMonth() > _date.getMonth()));
        boolean this_month = date != null && (date.getYear() == _date.getYear() && date.getMonth() == _date.getMonth());

        tempDate = new Date(startDate.getTime());
        while (!tempDate.after(endDate)) {
            int i = tempDate.getDate();

            // 1. Оставляем currentDate — она важна для логики (например, проверка TIMESLOT_NOT_STARTED)
            boolean currentDate = i == _date.getDate() && tempDate.getMonth() == _date.getMonth() && tempDate.getYear() == _date.getYear();

            // 2. Оставляем future — она используется во многих if ниже
            boolean future = _future_period || (this_month && i > _day);

            // 3. Добавляем новую проверку для ПОДСВЕТКИ (стиля)
            Date _now = new Date();
            boolean isTrulyToday = (i == _now.getDate()) &&
                    (tempDate.getMonth() == _now.getMonth()) &&
                    (tempDate.getYear() == _now.getYear());

            // 4. Формируем стиль (с пробелом)
            String currentDayStyle = isTrulyToday ? " current-day" : "";

            int tempHour = employee.getInOutHour()[i] == null ? 0 : employee.getInOutHour()[i];
            String value = String.valueOf(tempHour / 60);
            String shortname = employee.getTimeSlotId()[i];
            int[] shiftStartTime = employee.getShiftStartTime();
            int[] shiftEndTime = employee.getShiftEndTime();
            String[] shiftColor = employee.getShiftColor();
            Map<Integer, FingerprintTimeDto> employeeInOut = fingerprintTimeDtoMap.get(employee.getId());


            if (employeeInOut != null && employeeInOut.get(i) != null) {
                FingerprintTimeDto fingerprintDto = employeeInOut.get(i);
                int day = tempDate.getDate();
                int month = tempDate.getMonth();
                int year = tempDate.getYear();
                Integer lateMinutes = employee.getLateMinutes();
                Integer earlyMinutes = employee.getEarlyMinutes();
                int[] timeslot = employee.getTimeSlotItems().get(tempDate.getDay());
                int allowStartTIme = (shiftStartTime != null && shiftStartTime[i] != 0) ? shiftStartTime[i] :  timeslot[0] + (lateMinutes != null ? lateMinutes : 0);
                int allowEndTime = timeslot[1] - (earlyMinutes != null ? earlyMinutes : 0);
                ReasonItem lateReason = leaveTypes.get("LATE");
                ReasonItem earlyLeaveReason = leaveTypes.get("EARLY_LEAVE");

                if (fingerprintDto.getIntime() != null) {
                    html.append(getTerminalInOutTime(currentDayStyle, employee, i, fingerprintDto, null, day, month, year, true, allowStartTIme < fingerprintDto.getIntime(), false, lateReason, earlyLeaveReason, currentDate));
                } else {
                    html.append(getTerminalInOutTime(currentDayStyle, employee, i, fingerprintDto, null, day, month, year, true, false, false, lateReason, earlyLeaveReason, currentDate));
                }
                if (fingerprintDto.getOuttime() != null) {
                    html.append(getTerminalInOutTime(currentDayStyle, employee, i, fingerprintDto, null, day, month, year, false, false, allowEndTime > fingerprintDto.getOuttime(), lateReason, earlyLeaveReason, currentDate));
                } else {
                    html.append(getTerminalInOutTime(currentDayStyle, employee, i, fingerprintDto, null, day, month, year, false, false, false, lateReason, earlyLeaveReason, currentDate));
                }

                if (al[tempDate.getDate()] == 1 || al[tempDate.getDate()] == 4 || al[tempDate.getDate()] == 3) {
                    String code = employee.getLeaveCodes()[tempDate.getDate()];
                    html.append(getTerminalLeaveTypeCell(leaveTypes.get(code), WITHOUT_LR_STYLE + currentDayStyle + " has-actions", employee, i, true, false, i, tempDate.getDate(), tempDate.getMonth(), tempDate.getYear(), true));
                } else if (fingerprintDto.getActualtime() != null) {
                    if (timeSLotActualEnabled) {
                        if (fingerprintDto.getActualtime() >= tempHour) {
                            html.append(getTerminalInOutTimeWithBorder(currentDayStyle, employee, i, formatMinutesToHHmm(tempHour), null, day, month, year));
                        } else if (enableActualInOut) {
                            html.append(getTerminalInOutTimeWithBorder(currentDayStyle, employee, i, formatMinutesToHHmm(fingerprintDto.getActualtime().intValue()), null, day, month, year));
                        } else {
                            html.append(getTerminalInOutTimeWithBorder(currentDayStyle, employee, i, (fingerprintDto.getActualtime() / 60 < 10 ? "0" : "") + (fingerprintDto.getActualtime() / 60) + ":" + (fingerprintDto.getActualtime() % 60 < 10 ? "0" : "") + (fingerprintDto.getActualtime() % 60), null, day, month, year));
                        }
                    } else if (enableActualInOut) {
                        html.append(getTerminalInOutTimeWithBorder(currentDayStyle, employee, i, formatMinutesToHHmm(fingerprintDto.getActualtime().intValue()), null, day, month, year));
                    } else {
                        html.append(getTerminalInOutTimeWithBorder(currentDayStyle, employee, i, (fingerprintDto.getActualtime() / 60 < 10 ? "0" : "") + (fingerprintDto.getActualtime() / 60) + ":" + (fingerprintDto.getActualtime() % 60 < 10 ? "0" : "") + (fingerprintDto.getActualtime() % 60), null, day, month, year));
                    }
                } else if (!currentDate && (fingerprintDto.getIntime() == null || fingerprintDto.getOuttime() == null)) {
                    html.append(getTerminalNoCheckIn(leaveTypes.get("NO_CHECK_IN"), WITHOUT_LR_STYLE + currentDayStyle, employee, i, true, false, i, tempDate.getDate(), tempDate.getMonth(), tempDate.getYear()));
                } else {
                    html.append(getTerminalInOutTimeWithBorder(currentDayStyle, employee, i, "00:00", null, day, month, year));
                }
            } else {
                Integer holidayType = monthHolidaysByPeriod != null ? monthHolidaysByPeriod.get(dateFormat.format(tempDate)) : null;
                if (shiftStartTime != null && shiftStartTime[i] != 0 && shiftEndTime != null && shiftEndTime[i] != 0) {
                    html.append(getTerminalShiftDayTypeCell(currentDayStyle,employee,i,shortname,shiftStartTime[i],shiftEndTime[i],shiftColor[i]));
                } else if (currentDate && (date.getHours() * 60 + date.getMinutes()) < employee.getTimeSlotItems().get(tempDate.getDay())[0] && !employee.isHasShift()) {
                    html.append(getTerminalLeaveTypeCell(leaveTypes.get("TIMESLOT_NOT_STARTED"), WITHOUT_LR_STYLE + currentDayStyle, employee, i, true, true, i, tempDate.getDate(), tempDate.getMonth(), tempDate.getYear(), false));
                } else if (al[tempDate.getDate()] == 1 || al[tempDate.getDate()] == 4) {
                    String code = employee.getLeaveCodes()[tempDate.getDate()];
                    html.append(getTerminalLeaveTypeCell(leaveTypes.get(code), WITHOUT_LR_STYLE + currentDayStyle + " has-actions", employee, i, true, false, i, tempDate.getDate(), tempDate.getMonth(), tempDate.getYear(), false));
                    fillLeaveTotalData(leaveTotalData, i, code, employee, leaveTotalDataByEmployee);
                } else if (employeeResignation != null && date.getMonth() == employeeResignation.getMonth() && date.getYear() == employeeResignation.getYear() && employeeResignation.getDate() == i) {
                    html.append(getTerminalLeaveTypeCell(leaveTypes.get("LR_TYPE_RESIGNED"), WITHOUT_LR_STYLE + currentDayStyle, employee, i, false, false, i, tempDate.getDate(), tempDate.getMonth(), tempDate.getYear(), false));
                    fillLeaveTotalData(leaveTotalData, i, "LR_TYPE_RESIGNED", employee, leaveTotalDataByEmployee);
                } else if (al[tempDate.getDate()] == -1) {
                    String code = employee.getLeaveCodes()[tempDate.getDate()];
                    html.append(getTerminalLeaveTypeCell(leaveTypes.get(code), WITHOUT_LR_STYLE + currentDayStyle + " has-actions", employee, i, true, false, i, tempDate.getDate(), tempDate.getMonth(), tempDate.getYear(), false));
                    fillLeaveTotalData(leaveTotalData, i, code, employee, leaveTotalDataByEmployee);   //// Let's keep it a while
                } else if (al[tempDate.getDate()] == 2 || al[tempDate.getDate()] == -2) {
                    String code = employee.getLeaveCodes()[tempDate.getDate()];
                    html.append(getTerminalLeaveTypeCell(leaveTypes.get(code), WITHOUT_LR_STYLE + currentDayStyle + " has-actions", employee, i, true, false, i, tempDate.getDate(), tempDate.getMonth(), tempDate.getYear(), false));
                    fillLeaveTotalData(leaveTotalData, i, code, employee, leaveTotalDataByEmployee);
                } else if (al[tempDate.getDate()] == 3) {
                    String code = employee.getLeaveCodes()[tempDate.getDate()];
                    if (future) {
                        html.append(getTerminalLeaveTypeCell(leaveTypes.get(code), WITHOUT_LR_STYLE + currentDayStyle + " has-actions", employee, i, true, false, i, tempDate.getDate(), tempDate.getMonth(), tempDate.getYear(), false));
                    } else {
                        html.append(getTerminalLeaveTypeCellHourly(leaveTypes.get(code), WITHOUT_LR_STYLE + currentDayStyle + " has-actions", employee, i, value, i, tempDate.getDate(), tempDate.getMonth(), tempDate.getYear()));
                    }
                } else if (al[tempDate.getDate()] == -3) {
                    html.append(getTerminalLeaveTypeCell(leaveTypes.get("LR_TYPE_HOLIDAY"), WITHOUT_LR_STYLE + currentDayStyle, employee, i, true, true, i, tempDate.getDate(), tempDate.getMonth(), tempDate.getYear(), false));
                } else if (employee.isHasShift()) {
                    if (currentDate && (date.getHours() * 60 + date.getMinutes()) < employee.getTimeSlotItems().get(tempDate.getDay())[0]) {
                        html.append(getTerminalFutureWorkingDayTypeCell(tempHour, currentDayStyle, employee, i, value, shortname));
                    } else if (employee.getInOutHour()[i] == null) {
                        html.append(getTerminalWorkingDayTypeCell(0, currentDayStyle, employee, i, value, shortname, tempDate.getDate(), tempDate.getMonth(), tempDate.getYear()));
                    } else if (holidayType != null && holidayType == 1) {
                        if (tempHour == 0) {
                            html.append(getTerminalLeaveTypeCell(leaveTypes.get("LR_TYPE_DAY_OFF"), SUNDAY_MONTH_STYLE + currentDayStyle, employee, i, true, true, i, tempDate.getDate(), tempDate.getMonth(), tempDate.getYear(), false));
                        } else if (future) {
                            html.append(getTerminalFutureWorkingDayTypeCell(tempHour, currentDayStyle, employee, i, value, shortname));
                        } else {
                            html.append(getTerminalLeaveTypeCell(leaveTypes.get("LR_TYPE_UNAUTHORIZED_LEAVE"), WITHOUT_LR_STYLE + currentDayStyle, employee, i, true, false, i, tempDate.getDate(), tempDate.getMonth(), tempDate.getYear(), false));
                        }
                    } else if (holidayType != null && holidayType == 3) {
                        if (!future) {
                            html.append(getTerminalLeaveTypeCell(leaveTypes.get("LR_TYPE_UNAUTHORIZED_LEAVE"), WITHOUT_LR_STYLE + currentDayStyle, employee, i, true, false, i, tempDate.getDate(), tempDate.getMonth(), tempDate.getYear(), false));
                        } else {
                            html.append(getTerminalFutureWorkingDayTypeCell(tempHour, currentDayStyle, employee, i, value, shortname));
                        }
                    } else if (holidayType != null && (holidayType == 1 || holidayType == 2)) {
                        if (!future) {
                            html.append(getTerminalLeaveTypeCell(leaveTypes.get("LR_TYPE_UNAUTHORIZED_LEAVE"), WITHOUT_LR_STYLE + currentDayStyle, employee, i, true, false, i, tempDate.getDate(), tempDate.getMonth(), tempDate.getYear(), false));
                        } else {
                            html.append(getTerminalFutureWorkingDayTypeCell(tempHour, currentDayStyle, employee, i, value, shortname));
                        }
                    } else {
                        html.append(getTerminalLeaveTypeCell(leaveTypes.get("LR_TYPE_UNAUTHORIZED_LEAVE"), WITHOUT_LR_STYLE + currentDayStyle, employee, i, true, false, i, tempDate.getDate(), tempDate.getMonth(), tempDate.getYear(), false));
                    }
                } else if (holidayType != null && holidayType == 1) {
                    if (tempHour == 0) {
                        html.append(getTerminalLeaveTypeCell(leaveTypes.get("LR_TYPE_DAY_OFF"), SUNDAY_MONTH_STYLE + currentDayStyle, employee, i, true, true, i, tempDate.getDate(), tempDate.getMonth(), tempDate.getYear(), false));
                    } else if (future) {
                        html.append(getTerminalFutureWorkingDayTypeCell(tempHour, currentDayStyle, employee, i, value, shortname));
                    } else {
                        html.append(getTerminalLeaveTypeCell(leaveTypes.get("LR_TYPE_UNAUTHORIZED_LEAVE"), WITHOUT_LR_STYLE + currentDayStyle, employee, i, true, false, i, tempDate.getDate(), tempDate.getMonth(), tempDate.getYear(), false));
                    }
                } else if (holidayType != null && holidayType == 2) {
                    if (tempHour == 0) {
                        if (holidayInclude && !future) {
                            String htmlCode = "";
                            // Первые две ячейки (In/Out) - без границы
                            htmlCode = TD_BEGIN.replaceAll(CLASS_NAME, WORK_MONTH_HOLIDAY_STYLE + currentDayStyle) + getClickableValue(employee, i, "") + TD_END;
                            htmlCode += TD_BEGIN.replaceAll(CLASS_NAME, WORK_MONTH_HOLIDAY_STYLE + currentDayStyle) + getClickableValue(employee, i, "") + TD_END;
                            // Третья ячейка (Actual) - добавляем dayCol-end и убираем border-right
                            htmlCode += "<td class=\"" + WORK_MONTH_HOLIDAY_STYLE + currentDayStyle + " dayCol-end\">" + getClickableValue(employee, i, "") + "</td>";
                            html.append(htmlCode);
                        } else {
                            String htmlCode = "";
                            htmlCode = TD_BEGIN.replaceAll(CLASS_NAME, WORK_MONTH_HOLIDAY_STYLE + currentDayStyle) + "&nbsp;" + TD_END;
                            htmlCode += TD_BEGIN.replaceAll(CLASS_NAME, WORK_MONTH_HOLIDAY_STYLE + currentDayStyle) + "&nbsp;" + TD_END;
                            // Здесь тоже чистим
                            htmlCode += "<td class=\"" + WORK_MONTH_HOLIDAY_STYLE + currentDayStyle + " dayCol-end\">" + "&nbsp;" + "</td>";
                            html.append(htmlCode);
                        }
                    } else if (future) {
                        html.append(getTerminalFutureWorkingDayTypeCell(tempHour, currentDayStyle, employee, i, value, shortname));
                    } else {
                        html.append(getTerminalLeaveTypeCell(leaveTypes.get("LR_TYPE_UNAUTHORIZED_LEAVE"), WITHOUT_LR_STYLE + currentDayStyle, employee, i, true, false, i, tempDate.getDate(), tempDate.getMonth(), tempDate.getYear(), false));

                    }
                } else if (holidayType != null && holidayType == 3) {
                    if (tempHour == 0) {
                        html.append(getTerminalLeaveTypeCell(leaveTypes.get("LR_TYPE_HOLIDAY"), WITHOUT_LR_STYLE + currentDayStyle, employee, i, true, true, i, tempDate.getDate(), tempDate.getMonth(), tempDate.getYear(), false));
                    } else if (!future) {
                        html.append(getTerminalLeaveTypeCell(leaveTypes.get("LR_TYPE_UNAUTHORIZED_LEAVE"), WITHOUT_LR_STYLE + currentDayStyle, employee, i, true, false, i, tempDate.getDate(), tempDate.getMonth(), tempDate.getYear(), false));
                    } else {
                        html.append(getTerminalFutureWorkingDayTypeCell(tempHour, currentDayStyle, employee, i, value, shortname));
                    }
                } else if (holidayType != null && (holidayType == 1 || holidayType == 2)) {
                    if (tempHour == 0) {
                        html.append(getTerminalLeaveTypeCell(leaveTypes.get("LR_TYPE_HOLIDAY"), WITHOUT_LR_STYLE + currentDayStyle, employee, i, true, true, i, tempDate.getDate(), tempDate.getMonth(), tempDate.getYear(), false));
                    } else if (!future) {
                        html.append(getTerminalLeaveTypeCell(leaveTypes.get("LR_TYPE_UNAUTHORIZED_LEAVE"), WITHOUT_LR_STYLE + currentDayStyle, employee, i, true, false, i, tempDate.getDate(), tempDate.getMonth(), tempDate.getYear(), false));
                    } else {
                        html.append(getTerminalFutureWorkingDayTypeCell(tempHour, currentDayStyle, employee, i, value, shortname));
                    }
                } else if (!future) {
                    html.append(getTerminalLeaveTypeCell(leaveTypes.get("LR_TYPE_UNAUTHORIZED_LEAVE"), WITHOUT_LR_STYLE + currentDayStyle, employee, i, true, false, i, tempDate.getDate(), tempDate.getMonth(), tempDate.getYear(), false));
                } else {
                    html.append(getTerminalFutureWorkingDayTypeCell(tempHour, currentDayStyle, employee, i, value, shortname));
                }

            }
            tempDate.setDate(tempDate.getDate() + 1);

        }
        html.append(TD_BEGIN.replaceAll(CLASS_NAME, IN_HOUR_OVERTIME_STYLE)).append(latePercent).append(TD_END);
        html.append(TD_BEGIN.replaceAll(CLASS_NAME, IN_HOUR_OVERTIME_STYLE)).append(earlyPercent).append(TD_END);
        return html.toString();
    }


    private void fillLeaveTotalData(HashMap<Integer, HashMap<String, AttendanceTableBeta.LREmployee>> data, int day, String code, EmployeeReport employee, HashMap<Integer, HashMap<String, Integer>> leaveByEmployee) {
        if (data.get(day) != null) {
            if (data.get(day).get(code) != null) {
                AttendanceTableBeta.LREmployee value = data.get(day).get(code);
                value.setTotal(value.getTotal() + 1);
                value.getEmps().add(employee);
                data.get(day).put(code, value);
            } else {
                AttendanceTableBeta.LREmployee lr = new AttendanceTableBeta.LREmployee();
                lr.setTotal(1);
                lr.getEmps().add(employee);
                data.get(day).put(code, lr);
            }
        } else {
            HashMap<String, AttendanceTableBeta.LREmployee> t = new HashMap<>();
            AttendanceTableBeta.LREmployee lr = new AttendanceTableBeta.LREmployee();
            lr.setTotal(1);
            lr.getEmps().add(employee);
            t.put(code, lr);
            data.put(day, t);
        }
        if (leaveByEmployee.get(employee.getId()) != null) {
            if (leaveByEmployee.get(employee.getId()).get(code) != null) {
                Integer value = leaveByEmployee.get(employee.getId()).get(code);
                leaveByEmployee.get(employee.getId()).put(code, value + 1);
            } else {
                leaveByEmployee.get(employee.getId()).put(code, 1);
            }
        } else {
            HashMap<String, Integer> t = new HashMap<>();
            t.put(code, 1);
            leaveByEmployee.put(employee.getId(), t);
        }
    }

    private String getLeaveTypeCell(ReasonItem lt, String style, EmployeeReport employeeReport, int currentDay, boolean onClick_, boolean isDayOff) {
        if (lt != null) {
            String color = lt.getHexColor() != null ? lt.getHexColor() : "ffffff";
            String shorName = lt.getShortName() != null ? lt.getShortName() : "(L)";
            String styleName = "";
            if (style != null) {
                styleName = "class=\"" + style + " dayCol-end\"";
            }
            String title = "title=\"" + Utils.escapeHtml(lt.getName()) + "\"";
            String status = WITH_LR_STYLE.equals(style) ? "SS_APPROVED" : "NOT_DEFINED";
            String onclick = "";
            if (isDayOff) {
                onclick = "style=\"cursor: pointer\" onclick=\"showEditDialog('" + employeeReport.getId() + "##" + currentDay + "/" + monthYear + "');\"";
            } else {
                onclick = "onclick=\"redirectToLeaveRequest('" + status + "', '" + employeeReport.getIdsOfLeaveRequests().get(currentDay) + "', '" + employeeReport.getId() + "');\"";
            }
            return "<td " + styleName + " " + title + " " + (onClick_ ? onclick : "") + ">" + "<span style=\"border-bottom: 2px solid #" + color + "; cursor: " + (onClick_ ? "pointer" : "default") + "\">" + shorName + "</span></td>";
        }
        return "<td></td>";
    }


    private String getTerminalLeaveTypeCell(ReasonItem lt, String style, EmployeeReport employeeReport, int currentDay, boolean onClick_, boolean isDayOff, int i, int day, int month, int year, boolean isActual) {
        String html = "";
        if (lt != null) {
            String[] split = monthYear.split("/");
            Date date = new Date(Integer.valueOf(split[1]) - 1900, Integer.valueOf(split[0]) - 1, i);
            int[] timeSlotItem = employeeReport.getTimeSlotItems().get(date.getDay());
            int[] shiftStartTime = employeeReport.getShiftStartTime();
            int[] shiftEndTime = employeeReport.getShiftEndTime();

            // --- ЛОГИКА ВРЕМЕНИ ---
            int start = shiftStartTime != null && shiftStartTime[i] != 0 ? shiftStartTime[i] : timeSlotItem[0];
            int end = shiftEndTime != null && shiftEndTime[i] != 0 ? shiftEndTime[i] : timeSlotItem[1];
            String startTime = (start / 60 < 10 ? "0" : "") + (start / 60) + ":" + (start % 60 < 10 ? "0" : "") + (start % 60);
            String endTime = (end / 60 < 10 ? "0" : "") + (end / 60) + ":" + (end % 60 < 10 ? "0" : "") + (end % 60);
//            String startTime = (timeSlotItem[0] / 60 < 10 ? "0" : "") + (timeSlotItem[0] / 60) + ":" + (timeSlotItem[0] % 60 < 10 ? "0" : "") + (timeSlotItem[0] % 60);
//            String endTime = (timeSlotItem[1] / 60 < 10 ? "0" : "") + (timeSlotItem[1] / 60) + ":" + (timeSlotItem[1] % 60 < 10 ? "0" : "") + (timeSlotItem[1] % 60);
            String color = lt.getHexColor() != null ? lt.getHexColor() : "ffffff";
            String shorName = lt.getShortName() != null ? lt.getShortName() : "(L)";

            // РАЗДЕЛЯЕМ СТИЛИ ДЛЯ ТАБЛИЦЫ
            String styleBase = "";
            String styleEnd = "";
            if (style != null) {
                styleBase = "class=\"" + style + " \"";
                styleEnd = "class=\"" + style + " dayCol-end \"";
            }

            String title = "title=\"" + Utils.escapeHtml(lt.getName()) + "\"";
            String status = WITH_LR_STYLE.equals(style) ? "SS_APPROVED" : "NOT_DEFINED";
            String onclick = "";
            if (isDayOff) {
                onclick = "style=\"cursor: pointer\" onclick=\"showEditDialog('" + employeeReport.getId() + "##" + currentDay + "/" + monthYear + "');\"";
            } else {
                onclick = "onclick=\"redirectToLeaveRequest('" + status + "', '" + employeeReport.getIdsOfLeaveRequests().get(currentDay) + "', '" + employeeReport.getId() + "');\"";
            }

            if (!isActual) {
                if (hasActionAdjust) {
                    String dateItems = employeeReport.getId() + "##" + currentMonth + "##" + i + "/" + monthYear + "##" + day + "##" + month + "##" + year;
                    String inOnclickWithShortName = "<a id = " + "in_" + employeeReport.getId() + "_" + i + " style=\"cursor: pointer\" onclick=\"window.showFingerprintAdjustmentPopup('" + dateItems + "')\">";
                    String outOnclickWithShortName = "<a id = " + "out_" + employeeReport.getId() + "_" + i + " style=\"cursor: pointer\" onclick=\"window.showFingerprintAdjustmentPopup('" + dateItems + "')\">";
                    // Используем styleBase для первых двух столбцов (In/Out)
                    html = "<td " + styleBase + ">" + inOnclickWithShortName + "<span style= \"opacity:0.5;\"><i>" + startTime + "</i></span>" + "</a>" + "</td>";
                    html += "<td " + styleBase + ">" + outOnclickWithShortName + "<span style= \"opacity:0.5;\"><i>" + endTime + "</i></span>" + "</a>" + "</td>";
                } else {
                    html = "<td " + styleBase + "><span style= \"opacity:0.5;\"><i>" + startTime + "</i></span>" + "</td>";
                    html += "<td " + styleBase + "><span style= \"opacity:0.5;\"><i>" + endTime + "</i></span>" + "</td>";
                }
            }

            html += "<td " + styleEnd + " " + title + " " + (onClick_ ? onclick : "") + ">" + "<span style=\"border-bottom: 2px solid #" + color + "; cursor: " + (onClick_ ? "pointer" : "default") + "\">" + shorName + "</span></td>";
            return html;
        }

        return "<td></td> <td></td> <td class=\"dayCol-end\"></td>";
    }

    private String getTerminalNoCheckIn(ReasonItem lt, String style, EmployeeReport employeeReport, int currentDay, boolean onClick_, boolean isDayOff, int i, int day, int month, int year) {
        String html = "";
        if (lt != null) {
            String color = lt.getHexColor() != null ? lt.getHexColor() : "ffffff";
            String shorName = lt.getShortName() != null ? lt.getShortName() : "(L)";

            String styleEnd = "";
            if (style != null) {
                // Обязательно добавляем dayCol-end, чтобы закрыть группу из 3-х ячеек
                styleEnd = "class=\"" + style.trim() + " dayCol-end\"";
            }

            String title = "title=\"" + Utils.escapeHtml(lt.getName()) + "\"";

            // Рисуем ТОЛЬКО ОДНУ ячейку (третью в группе)
            html = "<td " + styleEnd + " " + title + ">" +
                    "<span style=\"border-bottom: 2px solid #" + color + "; cursor: " + (onClick_ ? "pointer" : "default") + "\">" +
                    shorName + "</span></td>";

            return html;
        }

        // Если данных нет, просто закрываем день пустой ячейкой-заглушкой
        return "<td class=\"dayCol-end\"></td>";
    }

    private String getLeaveTypeCellHourly(ReasonItem lt, String style, EmployeeReport employeeReport, int currentDay, String hour) {
        if (lt != null) {
            String color = lt.getHexColor() != null ? lt.getHexColor() : "ffffff";
            String shorName = lt.getShortName() != null ? lt.getShortName() : "(L)";
            String styleName = "";
            if (style != null) {
                styleName = "class=\"" + style + "\"";
            }
            String title = "title=\"" + Utils.escapeHtml(lt.getName()) + "\"";
            String status = WITH_LR_STYLE.equals(style) ? "SS_APPROVED" : "NOT_DEFINED";
            String onclick = "onclick=\"redirectToLeaveRequest('" + status + "', '" + employeeReport.getIdsOfLeaveRequests().get(currentDay) + "', '" + employeeReport.getId() + "');\"";
            return "<td " + styleName + " " + title + " " + (onclick) + ">" + "<span style=\"border-bottom: 2px solid #" + color + "; cursor: " + ("pointer") + "\">" + shorName + "</span>" +
                    "<span>" + hour + "</span></td>";
        }
        return "<td></td>";
    }

    private String getTerminalLeaveTypeCellHourly(ReasonItem lt, String style, EmployeeReport employeeReport, int currentDay, String hour, int i, int day, int month, int year) {
        String html = "";
        if (lt != null) {
            String[] split = monthYear.split("/");
            Date date = new Date(Integer.valueOf(split[1]) - 1900, Integer.valueOf(split[0]) - 1, i);
            int[] timeSlotItem = employeeReport.getTimeSlotItems().get(date.getDay());

            // Расчет времени
            String startTime = (timeSlotItem[0] / 60 < 10 ? "0" : "") + (timeSlotItem[0] / 60) + ":" + (timeSlotItem[0] % 60 < 10 ? "0" : "") + (timeSlotItem[0] % 60);
            String endTime = (timeSlotItem[1] / 60 < 10 ? "0" : "") + (timeSlotItem[1] / 60) + ":" + (timeSlotItem[1] % 60 < 10 ? "0" : "") + (timeSlotItem[1] % 60);

            String color = lt.getHexColor() != null ? lt.getHexColor() : "ffffff";
            String shorName = lt.getShortName() != null ? lt.getShortName() : "(L)";

            // РАЗДЕЛЯЕМ СТИЛИ
            String styleBase = "";
            String styleEnd = "";
            if (style != null) {
                styleBase = "class=\"" + style + "\"";
                styleEnd = "class=\"" + style + " dayCol-end\"";
            }

            String title = "title=\"" + Utils.escapeHtml(lt.getName()) + "\"";
            String status = WITH_LR_STYLE.equals(style) ? "SS_APPROVED" : "NOT_DEFINED";
            String onclick = "onclick=\"redirectToLeaveRequest('" + status + "', '" + employeeReport.getIdsOfLeaveRequests().get(currentDay) + "', '" + employeeReport.getId() + "');\"";

            if (hasActionAdjust) {
                String dateItems = employeeReport.getId() + "##" + currentMonth + "##" + i + "/" + monthYear + "##" + day + "##" + month + "##" + year;
                String onclickWithShortName = "<a id = " + employeeReport.getId() + "_" + i + " style=\"cursor: pointer\" onclick=\"window.showFingerprintAdjustmentPopup('" + dateItems + "')\">";

                // Используем styleBase для первых двух ячеек
                html = "<td " + styleBase + ">" + onclickWithShortName + "<span style= \"opacity:0.5;\"><i>" + startTime + "</i></span>" + "</a>" + "</td>";
                html += "<td " + styleBase + ">" + onclickWithShortName + "<span style= \"opacity:0.5;\"><i>" + endTime + "</i></span>" + "</a>" + "</td>";
            } else {
                // Исправлен синтаксис (добавлена >) и использован styleBase
                html = "<td " + styleBase + "><span style= \"opacity:0.5;\"><i>" + startTime + "</i></span>" + "</td>";
                html += "<td " + styleBase + "><span style= \"opacity:0.5;\"><i>" + endTime + "</i></span>" + "</td>";
            }

            // ТРЕТЬЯ ЯЧЕЙКА (Actual): Убран border-right, добавлен styleEnd (dayCol-end)
            html += "<td " + styleEnd + " " + title + " " + (onclick) + ">" +
                    "<span style=\"border-bottom: 2px solid #" + color + "; cursor: " + ("pointer") + "\">" +
                    shorName + "</span>" + "</td>";

            return html;
        }

        // Пустой случай: 3 ячейки с маркером конца
        return "<td></td> <td></td> <td class=\"dayCol-end\"></td>";
    }

    private String getWorkingDayTypeCell(int tempHour, String currentDayStyle, EmployeeReport employee, int i, String value, String timeSlotShortName) {
        if (employee != null) {
            String in_out = (tempHour / 60) + " h " + (tempHour % 60 + " " + "m");
            String color = employee.getTimeslotItem().getHexColor() != null ? employee.getTimeslotItem().getHexColor() : "#000000";
            String shortName = timeSlotShortName != null ? timeSlotShortName : employee.getTimeslotItem().getShortName();
            String onclickWithShortName = "<a id = " + employee.getId() + "_" + i + " style=\"cursor: pointer\" onclick=\"window.showEditDialog('" + employee.getId() + "##" + i + "/" + monthYear + "')\">";
            String styleName = "class=\"" + IN_OUT_DAY_OFF + " cellSlashMarkerWrapper" + currentDayStyle + "\" ";
            String title = "title=\"" + in_out + "\" ";
            if (shortName != null && !shortName.isEmpty()) {
                return "<td " + styleName + title + onclickWithShortName + "<span class=\"cellSlashMarker\" style=\"border-bottom: 2px solid " + color + "; cursor: " + ("pointer") + "\">"
                        + shortName + "</span>" + "<span>" + value + "</span></a></td>";
            } else {
                return "<td " + styleName + title + getClickableValue(employee, i, value) + "</td>";
            }
        }
        return "<td></td>";
    }

    private String getTerminalShiftDayTypeCell(String currentDayStyle, EmployeeReport employee, int i, String timeSlotShortName,int shiftStart,int shiftEnd,String color) {
        String html = "";
        if (employee != null) {
            String startTime = (shiftStart / 60 < 10 ? "0" : "") + (shiftStart / 60) + ":" + (shiftStart % 60 < 10 ? "0" : "") + (shiftStart % 60);
            String endTime = (shiftEnd / 60 < 10 ? "0" : "") + (shiftEnd / 60) + ":" + (shiftEnd % 60 < 10 ? "0" : "") + (shiftEnd % 60);

            String styleBase = "class=\"" + IN_OUT_DAY_OFF + " " + currentDayStyle + "\" ";
            String styleEnd = "class=\"" + IN_OUT_DAY_OFF + " " + currentDayStyle + " dayCol-end\" ";


            html = "<td " + styleBase + "><span style=\"opacity:0.5;\"><i>" + startTime + "</i></span></td>";
            html += "<td " + styleBase + "><span style=\"opacity:0.5;\"><i>" + endTime + "</i></span></td>";

            html += "<td style=\"color:"+(color != null ? color : "blue;")+"\" id=\"at_" + employee.getId() + "_" + i + "\" " + styleEnd + "><i>" + timeSlotShortName + "</i></td>";

            return html;
        }
        return "<td></td><td></td><td class=\"dayCol-end\"></td>";
    }


    private String getTerminalWorkingDayTypeCell(int tempHour, String currentDayStyle, EmployeeReport employee, int i, String value, String timeSlotShortName, int day, int month, int year) {
        String html = "";
        if (employee != null) {
            String[] split = monthYear.split("/");
            Date date = new Date(Integer.valueOf(split[1]) - 1900, Integer.valueOf(split[0]) - 1, i);
            int[] timeSlotItem = employee.getTimeSlotItems().get(date.getDay());
            String startTime = (timeSlotItem[0] / 60 < 10 ? "0" : "") + (timeSlotItem[0] / 60) + ":" + (timeSlotItem[0] % 60 < 10 ? "0" : "") + (timeSlotItem[0] % 60);
            String endTime = (timeSlotItem[1] / 60 < 10 ? "0" : "") + (timeSlotItem[1] / 60) + ":" + (timeSlotItem[1] % 60 < 10 ? "0" : "") + (timeSlotItem[1] % 60);

            // РАЗДЕЛЯЕМ СТИЛИ:
            // Базовый стиль для In и Out (без границы справа)
            String styleBase = "class=\"" + IN_OUT_DAY_OFF + " " + currentDayStyle + "\" ";
            // Стиль для Actual (с границей dayCol-end)
            String styleEnd = "class=\"" + IN_OUT_DAY_OFF + " " + currentDayStyle + " dayCol-end\" ";

            String dateItems = employee.getId() + "##" + currentMonth + "##" + i + "/" + monthYear + "##" + day + "##" + month + "##" + year;

            if (hasActionAdjust) {
                String inOnclick = "<a id=\"in_" + employee.getId() + "_" + i + "\" style=\"cursor: pointer\" onclick=\"window.showFingerprintAdjustmentPopup('" + dateItems + "')\">";
                String outOnclick = "<a id=\"out_" + employee.getId() + "_" + i + "\" style=\"cursor: pointer\" onclick=\"window.showFingerprintAdjustmentPopup('" + dateItems + "')\">";

                // Используем styleBase и закрываем <td> правильно через ">"
                html = "<td " + styleBase + ">" + inOnclick + "<span style=\"opacity:0.5;\"><i>" + startTime + "</i></span></a></td>";
                html += "<td " + styleBase + ">" + outOnclick + "<span style=\"opacity:0.5;\"><i>" + endTime + "</i></span></a></td>";
            } else {
                html = "<td " + styleBase + "><span style=\"opacity:0.5;\"><i>" + startTime + "</i></span></td>";
                html += "<td " + styleBase + "><span style=\"opacity:0.5;\"><i>" + endTime + "</i></span></td>";
            }

            String formattedValue = (tempHour / 60 < 10 ? "0" : "") + (tempHour / 60) + ":" + (tempHour % 60 < 10 ? "0" : "") + (tempHour % 60);

            // Третья ячейка (AT) — красная и с границей (styleEnd)
            html += "<td style=\"color:red;\" id=\"at_" + employee.getId() + "_" + i + "\" " + styleEnd + "><i>" + formattedValue + "</i></td>";

            return html;
        }
        return "<td></td><td></td><td class=\"dayCol-end\"></td>";
    }


    private String getTerminalInOutTime(
            String currentDayStyle,
            EmployeeReport employee,
            int i,
            FingerprintTimeDto fingerprintDto,
            String timeSlotShortName,
            int day,
            int month,
            int year,
            boolean isIn,
            boolean isLate,
            boolean isEarlyLeave,
            ReasonItem lateReason,
            ReasonItem earlyLeaveReason,
            boolean currentDate

    ) {
        if (employee == null) return "<td></td>"; // Сохраняем проверку коллеги

        boolean isFromTimeSlot = false;
        String value = getFingerprintTime(fingerprintDto, isIn);

        if (value == null) {
            // Используем ваш надежный способ определения дня недели
            Date tempDateForDay = new Date(year - 1900, month, day);
            int dayOfWeek = tempDateForDay.getDay();
            int[] timeSlot = employee.getTimeSlotItems().get(dayOfWeek);

            if (timeSlot != null) {
                value = formatMinutes(isIn ? timeSlot[0] : timeSlot[1]);
            } else {
                value = "--:--"; // Ваша страховка от пустого расписания
            }
            isFromTimeSlot = true;
        }

        String lateColor = lateReason.getHexColor() != null ? lateReason.getHexColor() : "ffffff";
        String earlyCOlor = earlyLeaveReason.getHexColor() != null ? earlyLeaveReason.getHexColor() : "ffffff";
        String iconHtml = "";

        String content = value;
        String title = null;

        if (isIn && fingerprintDto.getInAdjustment() && ("MOBILE".equals(fingerprintDto.getInSource()) || "WEB".equals(fingerprintDto.getInSource()))) {
            iconHtml = "MOBILE".equals(fingerprintDto.getInSource()) ? getPhoneSvg() : getAdjustSvg();
            content = "<span> " + value + " </span>";
            title = fingerprintDto.getInDescription();
        } else if (!isIn && fingerprintDto.getOutAdjustment() && ("MOBILE".equals(fingerprintDto.getOutSource()) || "WEB".equals(fingerprintDto.getOutSource()))) {
            iconHtml = "MOBILE".equals(fingerprintDto.getOutSource()) ? getPhoneSvg() : getAdjustSvg();
            content = "<span> " + value + " </span>";
            title = fingerprintDto.getOutDescription();
        }

        String dateItems = employee.getId() + "##" + currentMonth + "##" + i + "/" + monthYear
                + "##" + day + "##" + month + "##" + year;

        String anchor = "<a id=\"" + (isIn ? "in_" : "out_") + employee.getId() + "_" + i +
                "\" style=\"cursor:pointer\" onclick=\"window.showFingerprintAdjustmentPopup('" + dateItems + "')\">";

        // ПРИМЕНЕНИЕ СТИЛЯ: теперь currentDayStyle точно попадет в класс
        String style = "class=\"" + IN_OUT_DAY_OFF + " " + currentDayStyle + "\"";

        if (isFromTimeSlot) {
            content = "<span style=\"opacity:0.5\"><i>" + "--:--" + "</i></span>";
        } else if (title != null) {
            if (isIn) {
                if (isLate) {
                    content = "<span style=\"border-bottom: 2px solid #" + lateColor + ";\" title=\"" + title + "\"><span style=\"display:inline-flex;align-items:center;\">" + content + "</span></span>";
                } else {
                    content = "<span title=\"" + title + "\">" + content + "</span>";
                }
            } else {
                if (isEarlyLeave && !currentDate) {
                    content = "<span style=\"border-bottom: 2px solid #" + earlyCOlor + ";\" title=\"" + title + "\">" + content + "</span>";
                } else {
                    content = "<span title=\"" + title + "\">" + content + "</span>";
                }
            }
        } else {
            if (isIn) {
                if (isLate) {
                    content = "<span style=\"border-bottom: 2px solid #" + lateColor + ";\" title=\"" + lateReason.getName() + "\"><span style=\"display:inline-flex;align-items:center;\">" + content + "</span></span>";
                } else {
                    content = "<span>" + content + "</span>";
                }
            } else {
                if (isEarlyLeave && !currentDate) {
                    content = "<span style=\"border-bottom: 2px solid #" + earlyCOlor + ";\" title=\"" + earlyLeaveReason.getName() + "\">" + content + "</span>";
                } else {
                    content = "<span>" + content + "</span>";
                }
            }
        }

        // ИСПРАВЛЕНИЕ: закрываем </a> только если мы его открыли (hasActionAdjust)
        // iconHtml теперь стоит сразу после <td> и не попадает в <a>
        return "<td " + style + ">" + iconHtml + (hasActionAdjust ? anchor : "") + content + (hasActionAdjust ? "</a>" : "") + "</td>";
    }

    private String getTerminalInOutTimeWithBorder(String currentDayStyle, EmployeeReport employee, int i, String value, String timeSlotShortName, int day, int month, int year) {
        if (employee != null) {
            String dateItems = employee.getId() + "##" + currentMonth + "##" + i + "/" + monthYear
                    + "##" + day + "##" + month + "##" + year;

            String styleName = "class=\"" + IN_OUT_DAY_OFF + " " + currentDayStyle + " dayCol-end\" ";

            // Исправлен синтаксис: добавлена закрывающая скобка > после styleName
            return "<td id=\"at_" + employee.getId() + "_" + i + "\" " + styleName + ">" +
                    "<span><a onclick=\"window.showDailyReportPopup('" + dateItems + "')\">" + value + "</a></span>" +
                    "</td>";
        }
        // Для соблюдения сетки Terminal (3 колонки) лучше возвращать пустую ячейку с классом
        return "<td class=\"dayCol-end\"></td>";
    }

    private String getFutureWorkingDayTypeCell(int tempHour, String currentDayStyle, EmployeeReport employee, int i, String value, String timeSlotShortName) {
        if (employee != null) {
            String in_out = (tempHour / 60) + " h " + (tempHour % 60 + " " + "m");
            String color = employee.getTimeslotItem().getHexColor() != null ? employee.getTimeslotItem().getHexColor() : "#000000";
            String shortName = timeSlotShortName != null ? timeSlotShortName : employee.getTimeslotItem().getShortName();
            String onclickWithShortName = "<a id = " + employee.getId() + "_" + i + " style=\"cursor: pointer\" onclick=\"window.showEditDialog('" + employee.getId() + "##" + i + "/" + monthYear + "')\">";

            // Исправлено: гарантируем пробелы между классами
            String styleName = "class=\"" + IN_OUT_FUTURE + " " + currentDayStyle + "\"";
            String title = "title=\"" + Utils.escapeHtml(in_out) + "\"";

            if (shortName != null && !shortName.isEmpty()) {
                // Исправлено: добавлен символ > после title
                return "<td " + styleName + " " + title + ">" + onclickWithShortName + "<span style=\"border-bottom: 2px solid " + color + "; cursor: pointer; opacity:0.5\">"
                        + shortName + "</span>" + "<span style=\"opacity:0.5\"><i>" + value + "</i></span></a></td>";
            } else {
                // Исправлено: добавлен символ > после title
                return "<td " + styleName + " " + title + ">" + getClickableFutureValue(employee, i, value) + "</td>";
            }
        }
        return "<td></td>";
    }


    private String getTerminalFutureWorkingDayTypeCell(int tempHour, String currentDayStyle, EmployeeReport employee, int i, String value, String timeSlotShortName) {
        String html = "";
        if (employee != null) {
            String[] split = monthYear.split("/");
            Date date = new Date(Integer.valueOf(split[1]) - 1900, Integer.valueOf(split[0]) - 1, i);
            int[] timeSlotItem = employee.getTimeSlotItems().get(date.getDay());
            int[] shiftStartTime = employee.getShiftStartTime();
            int[] shiftEndTime = employee.getShiftEndTime();

            // Расчет времени
            int start = shiftStartTime != null && shiftStartTime[i] != 0 ? shiftStartTime[i] : timeSlotItem[0];
            int end = shiftEndTime != null && shiftEndTime[i] != 0 ? shiftEndTime[i] : timeSlotItem[1];
            String startTime = (start / 60 < 10 ? "0" : "") + (start / 60) + ":" + (start % 60 < 10 ? "0" : "") + (start % 60);
            String endTime = (end / 60 < 10 ? "0" : "") + (end / 60) + ":" + (end % 60 < 10 ? "0" : "") + (end % 60);

            String in_out = (tempHour / 60) + " h " + (tempHour % 60 + " " + "m");
            String color = employee.getTimeslotItem().getHexColor() != null ? employee.getTimeslotItem().getHexColor() : "#000000";
            String shortName = timeSlotShortName != null ? timeSlotShortName : employee.getTimeslotItem().getShortName();

            // Стили: styleBase для In/Out, styleEnd для Actual (с границей)
            String styleBase = "class=\"" + IN_OUT_FUTURE + currentDayStyle + "\" ";
            // Стиль для последней ячейки дня (с границей через класс)
            String styleEnd = "class=\"" + IN_OUT_FUTURE + currentDayStyle + " dayCol-end\" ";
            String title = "title=\"" + in_out + "\" ";
            String formattedValue = (tempHour / 60 < 10 ? "0" : "") + (tempHour / 60) + ":" + (tempHour % 60 < 10 ? "0" : "") + (tempHour % 60);

            if (shortName != null && !shortName.isEmpty()) {
                // Первая ячейка (In)
                html = "<td style=\"font-size: 11px;\" " + styleBase + title + ">" +
                        "<span style=\"opacity:0.5\"><i>" + startTime + "</i></span></td>";

                // Вторая ячейка (Out)
                html += "<td style=\"font-size: 11px;\" " + styleBase + title + ">" +
                        "<span style=\"opacity:0.5\"><i>" + endTime + "</i></span></td>";

                // Третья ячейка (Actual)
                if (shiftStartTime != null && shiftStartTime[i] != 0) {
                    html += "<td style=\"font-size: 11px;\" " + styleEnd + title + ">" +
                            "<span style=\"border-bottom: 2px solid " + color + "; cursor: pointer;\">" +
                            shortName + "</span></td>";
                } else {
                    html += "<td style=\"font-size: 11px;\" " + styleEnd + title + ">" +
                            "<span style=\"border-bottom: 2px solid " + color + "; cursor: pointer; opacity:0.5\">" +
                            shortName + "</span>" +
                            "<span style=\"opacity:0.5\"><i>" + formattedValue + "</i></span></td>";
                }
                return html;
            } else {
                // Случай без shortName
                html = "<td style=\"font-size: 11px;\" " + styleBase + title + "><i style=\"opacity:0.5\">" + startTime + "</i></td>";
                html += "<td style=\"font-size: 11px;\" " + styleBase + title + "><i style=\"opacity:0.5\">" + endTime + "</i></td>";
                // Третья ячейка (Actual)
                if (shiftStartTime != null && shiftStartTime[i] != 0) {
                    // Добавляем только ">" после title
                    html += "<td style=\"font-size: 11px;\" " + styleEnd + title + ">" +
                            "<span style=\"border-bottom: 2px solid " + color + "; cursor: pointer;\">" + shortName + "</span></td>";
                } else {
                    html += "<td " + styleEnd + title + ">" + getTerminalClickableFutureValue(employee, i, formattedValue) + "</td>";
                }
                return html;
            }
        }
        return "<td></td><td></td><td class=\"dayCol-end\"></td>";
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


    private String getAdjustSvg() {
        return "<svg style='width:1em;height:1em;vertical-align:middle;fill:orange;' viewBox='0 0 24 24'>" +
                "<path d='M13,13H11V7H13M13,17H11V15H13M1,21H23L12,2'/></svg>";
    }

    private String getPhoneSvg() {
        return "<svg class='getPhone' viewBox='0 0 24 24'>" +
                "<path d='M17,1H7A2,2 0 0,0 5,3V21A2,2 0 0,0 7,23H17A2,2 0 0,0 19,21V3A2,2 0 0,0 17,1M17,19H7V5H17V19Z'/>" +
                "</svg>";
    }


    private String getClickableValue(EmployeeReport employee, int i, String value) {
        return "<a id = " + employee.getId() + "_" + i + " style=\"cursor: pointer\" onclick=\"window.showEditDialog('" + employee.getId() + "##" + i + "/" + monthYear + "')\">" + value + "</a>";
    }

    private String getTerminalClickableValue(EmployeeReport employee, int i, String value) {
        return "<a id = " + employee.getId() + "_" + i + " style=\"cursor: pointer\" onclick=\"window.showEditDialog('" + employee.getId() + "##" + i + "/" + monthYear + "')\"><span>" + value + "</span></a>";
    }

    private String getClickableFutureValue(EmployeeReport employee, int i, String value) {
        return "<a id = " + employee.getId() + "_" + i + " style=\"cursor: pointer\" onclick=\"window.showEditDialog('" + employee.getId() + "##" + i + "/" + monthYear + "')\"><i style= \"opacity:0.5\">" + value + "</i></a>";
    }

    private String getTerminalClickableFutureValue(EmployeeReport employee, int i, String value) {
        return "<a id = " + employee.getId() + "_" + i + " ><i style= \"opacity:0.5\">" + value + "</i></a>";
    }

    private native void editAttendanceReport() /*-{
        var that = this;
        var timerID;
        $wnd.showEditDialog = $entry(function (dateitem) {
            clearTimeout(timerID);
            timerID = setTimeout(function () {
                that.@com.edatasite.workforce.gwt.availability.client.ui.view.attendanceTable.AttendanceTableDataBeta::showEditDialog(Ljava/lang/String;)(dateitem);
            }, 200)
        });
    }-*/;

    private native void runFingerPrintPopup() /*-{
        var that = this;
        $wnd.showFingerprintAdjustmentPopup = $entry(function (dateItems) {
            that.@com.edatasite.workforce.gwt.availability.client.ui.view.attendanceTable.AttendanceTableDataBeta::showFingerprintAdjustmentPopup(Ljava/lang/String;)(dateItems);
        });
    }-*/;

    private native void runDailyReport() /*-{
        var that = this;
        $wnd.showDailyReportPopup = $entry(function (dateItems) {
            that.@com.edatasite.workforce.gwt.availability.client.ui.view.attendanceTable.AttendanceTableDataBeta::showDailyReportPopup(Ljava/lang/String;)(dateItems);
        });
    }-*/;

    private void showDailyReportPopup(String dateItems) {
        if (isNavBox) {
            return;
        }
        String[] split = dateItems.split("##");
        int employeeId = split[0] != null ? Integer.parseInt(split[0]) : 0;
        int day = split[3] != null ? Integer.parseInt(split[3]) : 0;
        int month = split[4] != null ? Integer.parseInt(split[4]) : 0;
        int year = split[5] != null ? Integer.parseInt(split[5]) : 0;
        Date date = new Date(year, month, day);
        DateNonConvertable dateNonConvertable = new DateNonConvertable(date);
        DetailedInOutView inOutView = new DetailedInOutView(employeeId, dateNonConvertable, images.get(employeeId), employeeNames.get(employeeId), positions.get(employeeId));
        inOutView.open();
        isNavBox = true;
        inOutView.addClosedHandler(event -> isNavBox = false);
    }


    private void showFingerprintAdjustmentPopup(String dateItems) {
        if (isBoxOpen) {
            return;
        }
        isBoxOpen = true;
        String[] split = dateItems.split("##");
        int employeeId = split[0] != null ? Integer.parseInt(split[0]) : 0;
        int monthDays = split[1] != null ? Integer.parseInt(split[1]) : 0;
        String monthYear = split[2];
        int day = split[3] != null ? Integer.parseInt(split[3]) : 0;
        int month = split[4] != null ? Integer.parseInt(split[4]) : 0;
        int year = split[5] != null ? Integer.parseInt(split[5]) : 0;
        Date date = new Date(year, month, day);
        DateNonConvertable dateNonConvertable = new DateNonConvertable(date);
        FingerprintAdjustmentPopup popup = new FingerprintAdjustmentPopup(monthDays, monthYear, dateNonConvertable, null, false, employeeId, this);
        popup.showEditPopup(employeeId + "##" + monthYear);
        popup.addCloseHandler(event -> isBoxOpen = false);
    }

    private void showEditDialog(String dateItems) {

        if (!isBoxOpen) {
            isBoxOpen = true;
            String[] temp = dateItems.split("##");
            id = Integer.parseInt(temp[0]);
            dateItem = temp[1];
            reasons.setSelectedNullLabel();
            dateTimeInputs.startTime.setValue("");
            dateTimeInputs.endTime.setValue("");
            CommonService.App.get().getEmployeePresentTime(String.valueOf(id), String.valueOf(dateItem), new AbstractAsyncCallback<AttendanceReportLogItem>() {
                @Override
                public void failure(Throwable throwable) {
                    Window.alert(throwable.getMessage());
                }

                @Override
                public void success(AttendanceReportLogItem item) {
                    if (item != null && item.getText() != null) {
                        dateTimeInputs.startTime.setValue(item.getText().split(";")[0]);
                        dateTimeInputs.endTime.setValue(item.getText().split(";")[1]);
                        Date selectedDate = DateTimeFormat.getFormat("dd.MM.yyyy").parse(item.getDate());
                        dateTimeInputs.startDate.setDate(selectedDate);
                        timeslotId = item.getTimeslotId();
                        box.setTitle(item.getDate());
                        box.open();
                    }
                }
            });
        }
    }

    private native void initLeaveRequestPopup() /*-{
        var that = this;
        $wnd.redirectToLeaveRequest = $entry(function (status, id, employeeId) {
            that.@com.edatasite.workforce.gwt.availability.client.ui.view.attendanceTable.AttendanceTableDataBeta::redirectToLeaveRequest(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(status, id, employeeId);
        });
    }-*/;

    private void redirectToLeaveRequest(String status, String id, String employeeId) {
        boolean isPending = "NOT_DEFINED".equals(status);
        if (isPending) {
            if (Utils.hasRole(Constants.ADMIN) || (employeeId != null && Utils.getUserID().equals(Integer.parseInt(employeeId)))) {
                String link = "leaverequest|/" + id + "/" + employeeId;
                SinksContainerFactory.entryPoint.onHistoryChanged(link);
            } else {
                SinksContainerFactory.entryPoint.onHistoryChanged("leaverequest/" + id);
            }
        } else {
            SinksContainerFactory.entryPoint.onHistoryChanged("leaverequest/" + id);
        }
    }

    private void setToTable(String id, String dateItem, String from, String to) {
        DateTimeFormat dateFormatWithSlash = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm");

        Element elem = DOM.getElementById(id + "_" + dateItem.split("/")[0]);
        Date fromDate = dateFormatWithSlash.parse(dateItem + " " + from);
        Date toDate = dateFormatWithSlash.parse(dateItem + " " + to);
        elem.getParentElement().setTitle((toDate.getTime() - fromDate.getTime()) / (60 * 60 * 1000) + " h");
        elem.setInnerHTML(String.valueOf((toDate.getTime() - fromDate.getTime()) / (60 * 60 * 1000)));

    }

    public void setToTableFromPopup(UserFingerPrintAdjustment item) {
        Date nonConvertedDate = item.getStartDate().getNonConvertedDate();
        Element inElem = DOM.getElementById("in_" + item.getEmployeeId() + "_" + nonConvertedDate.getDate());
        Element outElem = DOM.getElementById("out_" + item.getEmployeeId() + "_" + nonConvertedDate.getDate());
        if ("IN".equals(item.getStatusString())) {
            inElem.setInnerHTML("<span title='" + item.getDescription() + "'><svg style='width:1em;height:1em;vertical-align:middle;fill:orange;' viewBox='0 0 24 24'><path d='M13,13H11V7H13M13,17H11V15H13M1,21H23L12,2'/></svg> " + item.getStartDate().getNonConvertedDate().getHours() + ":" + item.getStartDate().getNonConvertedDate().getMinutes() + "</span>");
        } else if ("OUT".equals(item.getStatusString())) {
            outElem.setInnerHTML("<span title='" + item.getDescription() + "'><svg style='width:1em;height:1em;vertical-align:middle;fill:orange;' viewBox='0 0 24 24'><path d='M13,13H11V7H13M13,17H11V15H13M1,21H23L12,2'/></svg> " + item.getStartDate().getNonConvertedDate().getHours() + ":" + item.getStartDate().getNonConvertedDate().getMinutes() + "</span>");
        }
        DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm");
        Date inParse = dtf.parse(DateTimeFormat.getFormat("yyyy-MM-dd").format(nonConvertedDate) + " " + inElem.getInnerText());
        Date outParse = dtf.parse(DateTimeFormat.getFormat("yyyy-MM-dd").format(nonConvertedDate) + " " + outElem.getInnerText());
        long diffMillis = outParse.getTime() - inParse.getTime();
        if (diffMillis < 0) {
            diffMillis += 24L * 60 * 60 * 1000;
        }
        long totalMinutes = diffMillis / (60 * 1000);
        long hours = totalMinutes / 60;
        String minutes = (totalMinutes % 60 < 10 ? "0" : "") + (totalMinutes % 60);
        Element atElem = DOM.getElementById("at_" + item.getEmployeeId() + "_" + nonConvertedDate.getDate());
        atElem.getStyle().clearColor();
        atElem.setInnerHTML(hours + ":" + minutes);
    }

    private String formatMinutesToHHmm(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        String h = (hours < 10 ? "0" : "") + hours;
        String m = (mins < 10 ? "0" : "") + mins;
        return h + ":" + m;
    }

    public void setDate(Date date) {
        this.date = date;
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
}
