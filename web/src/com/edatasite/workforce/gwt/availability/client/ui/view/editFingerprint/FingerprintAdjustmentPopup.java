package com.edatasite.workforce.gwt.availability.client.ui.view.editFingerprint;

import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeReport;
import com.edatasite.workforce.gwt.availability.client.rpc.UserFingerPrintDeviceItem;
import com.edatasite.workforce.gwt.availability.client.ui.view.attendanceTable.AttendanceTableBeta;
import com.edatasite.workforce.gwt.availability.client.ui.view.attendanceTable.AttendanceTableDataBeta;
import com.edatasite.workforce.gwt.availability.client.ui.view.attendanceTable.AttendanceTableHtmlTags;
import com.edatasite.workforce.gwt.core.client.ReasonItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.HMWidget;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.hrms.client.rpc.UserFingerPrintAdjustmentService;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class FingerprintAdjustmentPopup extends KpiModal implements AttendanceTableHtmlTags {
    private static final DateTimeFormat monthYearFormatter = DateTimeFormat.getFormat("MM/yyyy");

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final DataListBox reasons;
    private int currentMonth;
    private final String monthYear;
    private DateTimePicker dateTimeInputs;
    private DateNonConvertable date;
    private final boolean holidayInclude;
    private final DateTimeFormat df = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm");
    private final DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd/MM/yyyy");
    private final DateTimeFormat dfAm = DateTimeFormat.getFormat("dd/MM/yyyy hh:mm a");
    private Integer id;
    private String dateItem;
    private boolean isBoxOpen = false;
    private Integer timeslotId;
    private final Integer employeeId;
    private EmployeeLookUp employeeLookUp;
    private TextArea2 descriptionTextArea;
    private WfmDropdown terminals;
    private final UserFingerPrintAdjustment item;
    private HMWidget timeWidget;
    private AttendanceTableDataBeta attendanceTableDataBeta;

    public FingerprintAdjustmentPopup() {
        this(new Date().getDay(), monthYearFormatter.format(new Date()), new DateNonConvertable(new Date()), new SelectItem[]{}, true);
    }

    public FingerprintAdjustmentPopup(int monthDays, String monthYear, DateNonConvertable date, SelectItem[] reasonItems, boolean holidayInclude) {
        this(monthDays, monthYear, date, reasonItems, holidayInclude, Utils.getUserID(),null);
    }
    public FingerprintAdjustmentPopup(int monthDays, String monthYear, DateNonConvertable date, SelectItem[] reasonItems, boolean holidayInclude, int employeeId, AttendanceTableDataBeta attendanceTableDataBeta) {
        this.holidayInclude = holidayInclude;
        this.currentMonth = monthDays;
        this.monthYear = monthYear;
        this.date = date;
        this.employeeId = employeeId;
        this.item = new UserFingerPrintAdjustment();
        this.attendanceTableDataBeta = attendanceTableDataBeta;
        reasons = new DataListBox();

        if (reasonItems != null && reasonItems.length > 0) {
            reasons.setItems(reasonItems);
        }
        createEditPopup();
        initLeaveRequestPopup();
    }

    private void createEditPopup() {
        editAttendanceReport();
        setWidth(400);
        setDismissible(true);
        addStyleName("attendance_report_modal");
        FlexTable content = new FlexTable();
        content.addStyleName("tbl-form");

        HTML employee = new HTML("<b>" + wfmStrings.employee() + "</b>");
        content.setWidget(0, 0, employee);

        employeeLookUp = new EmployeeLookUp(true, false, false);
        employeeLookUp.setSelected(employeeId);
        employeeLookUp.setOnSelectListener(() -> {
            updateTerminalList();
            item.setEmployeeId(employeeLookUp.getSelectedItemID());
        });
        content.setWidget(0, 1, employeeLookUp);

        HTML terminal = new HTML("<b>" + wfmStrings.attendanceTerminal() + "</b>");
        content.setWidget(1, 0, terminal);

        terminals = new WfmDropdown();
        updateTerminalList();
        updateFingerprintId();
        content.setWidget(1, 1, terminals);


        HTML htmlCheckIn = new HTML("<b>" + wfmStrings.time() + "</b>");
        content.setWidget(2, 0, htmlCheckIn);


        dateTimeInputs = new DateTimePicker(false, true);
        dateTimeInputs.startDate.addValueChangeHandler(event -> dateItem = dateFormat.format(dateTimeInputs.startDate.getDate()));

        timeWidget = new HMWidget();
        content.setWidget(2, 1, new InputGroup( dateTimeInputs.getStartDatePicker(),timeWidget));

        WfmButton2 btnCheckedIn = new WfmButton2(wfmStrings.checkedIn(), WfmButton2.BTN_PRIMARY);
        btnCheckedIn.addClickHandler(x -> saveRecord("IN"));

        WfmButton2 btnCheckedOut = new WfmButton2(wfmStrings.checkedOut(), WfmButton2.BTN_SUCCESS);
        btnCheckedOut.addClickHandler(x -> saveRecord("OUT"));

        content.setWidget(3, 0, new HTML("<b>" + wfmStrings.markAs() + "<b/>"));
        content.setWidget(3, 1, reasons);
        add(content);
        addCloseHandler(clickEvent -> isBoxOpen = false);

        HTML description = new HTML("<b>" + wfmStrings.description() + "</b>");
        content.setWidget( 4, 0, description);

        descriptionTextArea = new TextArea2();
        content.setWidget( 4, 1, descriptionTextArea);

        WfmButton2 btnClose = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT);
        btnClose.addClickHandler(clickEvent -> close());
        addButton(btnClose);
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_ATTENDANCE_TABLE_DATA)) {
            addButton(btnCheckedOut);
            addButton(btnCheckedIn);
        } else if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_ATTENDANCE_TABLE_DATA)) {
            addButton(btnCheckedOut);
            addButton(btnCheckedIn);
        }
    }

    private void saveRecord(String status) {
        if (!validate()) {
            return;
        }
        Date startTime = null;
        try {
            startTime = dateFormat.parse(dateItem);
            startTime.setHours(timeWidget.getValueAsMinutes() / 60);
            startTime.setMinutes(timeWidget.getValueAsMinutes() % 60);
        } catch (IllegalArgumentException e) {
            Utils.log("time issue " + e);
        }
        item.setEmployeeId(employeeLookUp.getSelectedItemID());
        item.setStartDate(new DateNonConvertable(startTime));
        item.setDeviceUUID(terminals.getSelected().getCode());
        item.setDeviceName(terminals.getSelected().getName());
        item.setStatusString(status);
        item.setTimeSlotId(timeslotId);
        item.setDescription(descriptionTextArea.getText());
        item.setStartDateMillis(startTime.getTime());
        item.setSource("WEB");
        UserFingerPrintAdjustmentService.App.get().create(item,
                new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(Throwable throwable) {
                        Info.warn("Unable to save");
                    }

                    @Override
                    public void success(Void i) {
                        attendanceTableDataBeta.setToTableFromPopup(item);
                        Info.show("Successfully saved", Info.Type.INFO);
                    }
                }
        );
        close();
    }

    private void updateTerminalList() {
        UserFingerPrintAdjustmentService.App.get().getFingerprintSetup(new AbstractAsyncCallback<List<CompanyDomain>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(List<CompanyDomain> result) {
                super.onSuccess(result);
                List<SelectItem> devices = result.stream()
                        .map(cd -> {
                            SelectItem selectItem = new SelectItem();
                            selectItem.setId(cd.getObjectID());
                            selectItem.setName(cd.getCompanyBranchName());
                            selectItem.setCode(cd.getCompanyUniqueID());
                            return selectItem;
                        })
                        .collect(Collectors.toList());
                terminals.setItems(devices);
            }
        });
        terminals.addSelectionHandler(event -> updateFingerprintId());
    }

    public boolean validate() {
        if (employeeLookUp.getSelectedItemID() == null) {
            Info.warn(wfmStrings.fillRequiredField());
            return false;
        }
        if (!Validation.validateWfmDropdown(terminals)) {
            Info.warn(wfmStrings.fillRequiredField());
            return false;
        }
        if (dateTimeInputs.getStartDatePicker() == null) {
            Info.warn(wfmStrings.fillRequiredField());
            return false;
        }
        if (timeWidget.getValueAsMinutes() == null || timeWidget.getValueAsMinutes() == 0) {
            Info.warn(wfmStrings.fillRequiredField());
            return false;
        }
        if (descriptionTextArea.getText() == null || descriptionTextArea.getText().isEmpty()) {
            Info.warn(wfmStrings.fillRequiredField());
            return false;
        }
        return true;
    }

    private void updateFingerprintId() {
        if (employeeLookUp.getSelectedItem() == null || terminals.getSelectedItem() == null) return;
        UserFingerPrintAdjustmentService.App.get().getFingerprintSetupByUserIdAndDeviceId(employeeLookUp.getSelectedItemID(), terminals.getSelectedItem().getCode(), new AsyncCallback<UserFingerPrintDeviceItem>() {

            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(UserFingerPrintDeviceItem result) {
                item.setFingerprintId(result.getFingerPrintId());
            }
        });
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
            Date tempDate = date.getDate();
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
        boolean _future_period = date != null && (date.getDate().getYear() > _date.getYear() || (date.getDate().getYear() == _date.getYear() && date.getDate().getMonth() > _date.getMonth()));
        boolean this_month = date != null && (date.getDate().getYear() == _date.getYear() && date.getDate().getMonth() == _date.getMonth());

        for (int i = 1; i <= currentMonth; i++) {
            boolean currentDate = (i == (date != null ? date.getDate().getDate() : 0));
            boolean future = _future_period || (this_month && i > _day);
            String currentDayStyle = currentDate ? (" " + CURRENT_DAY_STALY) : "";
            int tempHour = employee.getInOutHour()[i] == null ? 0 : employee.getInOutHour()[i];
            String value = String.valueOf(tempHour / 60);
            String shortname = employee.getTimeSlotId()[i];

            if (al[i] == 1 || al[i] == 4) {
                String code = employee.getLeaveCodes()[i];
                html.append(getLeaveTypeCell(leaveTypes.get(code), WITH_LR_STYLE + currentDayStyle + " has-actions", employee, i, true, false));
                fillLeaveTotalData(leaveTotalData, i, code, employee, leaveTotalDataByEmployee);
            } else if (employeeResignation != null && date.getDate().getMonth() == employeeResignation.getMonth() && date.getDate().getYear() == employeeResignation.getYear() && employeeResignation.getDate() == i) {
                html.append(getLeaveTypeCell(leaveTypes.get("LR_TYPE_RESIGNED"), WITHOUT_LR_STYLE + currentDayStyle, employee, i, false, false));
                fillLeaveTotalData(leaveTotalData, i, "LR_TYPE_RESIGNED", employee, leaveTotalDataByEmployee);
            } else if (al[i] == -1) {
                String code = employee.getLeaveCodes()[i];
                html.append(getLeaveTypeCell(leaveTypes.get(code), WITHOUT_LR_STYLE + currentDayStyle + " has-actions", employee, i, true, false));
                fillLeaveTotalData(leaveTotalData, i, code, employee, leaveTotalDataByEmployee);   //// Let's keep it a while
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
            } else if (withHoliday[i] == 1 || withHoliday[i] == 2) {//holiday days
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
        html.append(TD_BEGIN.replaceAll(CLASS_NAME, IN_HOUR_OVERTIME_STYLE)).append(plannedDays < 0 ? "-" : "").append(plannedDays).append(TD_END);
        html.append(TD_BEGIN.replaceAll(CLASS_NAME, IN_HOUR_OVERTIME_STYLE)).append(overtimeDays + workedDays < 0 ? "-" : "").append((overtimeDays + workedDays)).append(TD_END);
        html.append(TD_BEGIN.replaceAll(CLASS_NAME, IN_HOUR_OVERTIME_STYLE)).append(plannedHours).append(TD_END);
        html.append(TD_BEGIN.replaceAll(CLASS_NAME, IN_HOUR_OVERTIME_STYLE)).append(actualHours).append(TD_END);
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
                leaveByEmployee.get(employee.getId()).compute(code, (k, value) -> value + 1);
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
                styleName = "class=\"" + style + "\"";
            }
            String title = "title=\"" + Utils.escapeHtml(lt.getName()) + "\"";
            String status = WITH_LR_STYLE.equals(style) ? "SS_APPROVED" : "NOT_DEFINED";
            String onclick = "";
            if (isDayOff) {
                onclick = "style=\"cursor: pointer\" onclick=\"showEditPopup('" + employeeReport.getId() + "##" + currentDay + "/" + monthYear + "');\"";
            } else {
                onclick = "onclick=\"redirectToLeaveRequest('" + status + "', '" + employeeReport.getIdsOfLeaveRequests().get(currentDay) + "', '" + employeeReport.getId() + "');\"";
            }
            return "<td " + styleName + " " + title + " " + (onClick_ ? onclick : "") + ">" + "<span style=\"border-bottom: 2px solid #" + color + "; cursor: " + (onClick_ ? "pointer" : "default") + "\">" + shorName + "</span></td>";
        }
        return "<td></td>";
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

    private String getWorkingDayTypeCell(int tempHour, String currentDayStyle, EmployeeReport employee, int i, String value, String timeSlotShortName) {
        if (employee != null) {
            String in_out = (tempHour / 60) + " h " + (tempHour % 60 + " " + "m");
            String color = employee.getTimeslotItem().getHexColor() != null ? employee.getTimeslotItem().getHexColor() : "#000000";
            String shortName = timeSlotShortName != null ? timeSlotShortName : employee.getTimeslotItem().getShortName();
            String onclickWithShortName = "<a id = " + employee.getId() + "_" + i + " style=\"cursor: pointer\" onclick=\"window.showEditPopup('" + employee.getId() + "##" + i + "/" + monthYear + "')\">";
            String styleName = "class=\"" + IN_OUT_DAY_OFF + currentDayStyle + "\" ";
            String title = "title=\"" + in_out + "\" ";
            if (shortName != null && !shortName.isEmpty()) {
                return "<td " + styleName + title + onclickWithShortName + "<span style=\"border-bottom: 2px solid " + color + "; cursor: " + ("pointer") + "\">"
                        + shortName + "</span>" + "<span>" + value + "</span></a></td>";
            } else {
                return "<td " + styleName + title + getClickableValue(employee, i, value) + "</td>";
            }
        }
        return "<td></td>";
    }

    private String getFutureWorkingDayTypeCell(int tempHour, String currentDayStyle, EmployeeReport employee, int i, String value, String timeSlotShortName) {
        if (employee != null) {
            String in_out = (tempHour / 60) + " h " + (tempHour % 60 + " " + "m");
            String color = employee.getTimeslotItem().getHexColor() != null ? employee.getTimeslotItem().getHexColor() : "#000000";
            String shortName = timeSlotShortName != null ? timeSlotShortName : employee.getTimeslotItem().getShortName();
            String onclickWithShortName = "<a id = " + employee.getId() + "_" + i + " style=\"cursor: pointer\" onclick=\"window.showEditPopup('" + employee.getId() + "##" + i + "/" + monthYear + "')\">";
            String styleName = "class=\"" + IN_OUT_FUTURE + currentDayStyle + "\" ";
            String title = "title=\"" + in_out + "\" ";
            if (shortName != null && !shortName.isEmpty()) {
                return "<td " + styleName + title + onclickWithShortName + "<span style=\"border-bottom: 2px solid " + color + "; cursor: " + ("pointer") + "; opacity:0.5\">"
                        + shortName + "</span>" + "<span style= \"opacity:0.5\"><i>" + value + "</i></span></a></td>";
            } else {
                return "<td " + styleName + title + getClickableFutureValue(employee, i, value) + "</td>";
            }
        }
        return "<td></td>";
    }

    private String getClickableValue(EmployeeReport employee, int i, String value) {
        return "<a id = " + employee.getId() + "_" + i + " style=\"cursor: pointer\" onclick=\"window.showEditPopup('" + employee.getId() + "##" + i + "/" + monthYear + "')\">" + value + "</a>";
    }

    private String getClickableFutureValue(EmployeeReport employee, int i, String value) {
        return "<a id = " + employee.getId() + "_" + i + " style=\"cursor: pointer\" onclick=\"window.showEditPopup('" + employee.getId() + "##" + i + "/" + monthYear + "')\"><i style= \"opacity:0.5\">" + value + "</i></a>";
    }

    private native void editAttendanceReport() /*-{
        var that = this;
        var timerID;
        $wnd.showEditPopup = $entry(function (dateitem) {
            clearTimeout(timerID);
            timerID = setTimeout(function () {
                that.@com.edatasite.workforce.gwt.availability.client.ui.view.editFingerprint.FingerprintAdjustmentPopup::showEditPopup(Ljava/lang/String;)(dateitem);
            }, 200)
        });
    }-*/;

    public void showEditPopup(String dateItems) {

        if (!isBoxOpen) {
            isBoxOpen = true;
            String[] temp = dateItems.split("##");
            id = Integer.parseInt(temp[0]);
            dateItem = temp[1];
            reasons.setSelectedNullLabel();
            timeWidget.setValueAsMinutes(null);
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
                        setTitle(item.getDate());
                        open();
                    }
                }
            });
        }
    }

private native void initLeaveRequestPopup() /*-{
    var that = this;
    $wnd.redirectToLeaveRequest = $entry(function (status, id, employeeId) {
        that.@com.edatasite.workforce.gwt.availability.client.ui.view.editFingerprint.FingerprintAdjustmentPopup::redirectToLeaveRequest(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(status, id, employeeId);
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

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

}
