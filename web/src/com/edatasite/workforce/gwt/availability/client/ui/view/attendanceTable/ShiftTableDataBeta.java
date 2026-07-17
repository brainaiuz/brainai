package com.edatasite.workforce.gwt.availability.client.ui.view.attendanceTable;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;

public class ShiftTableDataBeta extends Widget implements AttendanceTableHtmlTags {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final DataListBox reasons;
    private int currentMonth;
    private final String monthYear;
    private KpiModal box;
    private TextBox tbxCheckIn;
    private TextBox tbxCheckOut;
    private Date date;
    private final boolean holidayInclude;
    private final DateTimeFormat df = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm");
    private final DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd/MM/yyyy");
    private Integer id;
    private String dateItem;

    ShiftTableDataBeta(int monthDays, String monthYear, Date date, SelectItem[] reasonItems, boolean holidayInclude) {
        this.holidayInclude = holidayInclude;
        this.currentMonth = monthDays;
        this.monthYear = monthYear;
        this.date = date;
        reasons = new DataListBox();
        reasons.setWidth("100%");
        reasons.getElement().getStyle().setMarginTop(8, Style.Unit.PX);

        if (reasonItems != null && reasonItems.length > 0) {
            reasons.setItems(reasonItems);
        }
        createEditPopup();
        initLeaveRequestPopup();
        createCrmLookUp();
    }

    private void createEditPopup() {
        editAttendanceReport();
        box = new KpiModal();
        box.setWidth(400);
        box.setDismissible(true);
        FlexTable content = new FlexTable();
        content.setWidth("100%");

        HTML htmlCheckIn = new HTML("<b>" + wfmStrings.checkedIn() + "</b>");
        content.setWidget(0, 0, htmlCheckIn);

        HTML htmlCheckOut = new HTML("<b>" + wfmStrings.checkedOut() + "</b>");
        content.setWidget(1, 0, htmlCheckOut);


        tbxCheckIn = new TextBox();
        tbxCheckIn.getElement().setPropertyString("placeholder", "HH:mm");
        content.setWidget(0, 1, tbxCheckIn);

        tbxCheckOut = new TextBox();
        tbxCheckOut.getElement().setPropertyString("placeholder", "HH:mm");
        content.setWidget(1, 1, tbxCheckOut);
        tbxCheckIn.setText("");
        tbxCheckOut.setText("");

        WfmButton2 btnSave = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        btnSave.addClickHandler(x -> {
            Date startTime = null;
            Date endTime = null;
            try {
                startTime = df.parse(dateItem + " " + tbxCheckIn.getText());
                endTime = df.parse(dateItem + " " + tbxCheckOut.getText());
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
            EmployeePresentItem item = new EmployeePresentItem(id, date, startDate, enddate, reasons.getSelectedId(), null, null);
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
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LEAVE_REQUEST_STATUS_CHANGED, null, ShiftTableDataBeta.this);
                                Info.show("Successfully saved", Info.Type.INFO);
                            }
                            setToTable(String.valueOf(id), String.valueOf(dateItem), String.valueOf(tbxCheckIn.getText()), String.valueOf(tbxCheckOut.getText()));
                        }
                    }
            );
            box.close();
        });
        content.setWidget(2, 0, new HTML("<b>" + wfmStrings.markAs() + "<b/>"));
        content.setWidget(2, 1, reasons);
        box.add(content);

        WfmButton2 btnClose = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT);
        btnClose.addClickHandler(clickEvent -> box.close());
        box.addButton(btnClose);
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_ATTENDANCE_TABLE_DATA)) {
            box.addButton(btnSave);
        } else if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_ATTENDANCE_TABLE_DATA)) {
            box.addButton(btnSave);
        }
    }


    private native void initLeaveRequestPopup() /*-{
        var that = this;
        $wnd.redirectToLeaveRequest = $entry(function (status, id, employeeId) {
            that.@com.edatasite.workforce.gwt.availability.client.ui.view.attendanceTable.AttendanceTableDataBeta::redirectToLeaveRequest(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(status, id, employeeId);
        });
    }-*/;

    private native void createCrmLookUp() /*-{
        var that = this;
        $wnd.createCrmLookUp = $entry(function (status, id, employeeId) {
            that.@com.edatasite.workforce.gwt.availability.client.ui.view.attendanceTable.ShiftTableDataBeta::createCrmLookUpT(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(status, id, employeeId);
        });
    }-*/;

    private native void editAttendanceReport() /*-{
        var that = this;
        var timerID;
        $wnd.showEditDialog = $entry(function (dateitem) {
            clearTimeout(timerID);
            timerID = setTimeout(function () {
                that.@com.edatasite.workforce.gwt.availability.client.ui.view.attendanceTable.ShiftTableDataBeta::showEditDialog(Ljava/lang/String;)(dateitem);
            }, 200)
        });
    }-*/;

    private void createCrmLookUpT(String a, String b, String c) {


    }


    private void setToTable(String id, String dateItem, String from, String to) {
        DateTimeFormat dateFormatWithSlash = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm");

        Element elem = DOM.getElementById(id + "_" + dateItem.split("/")[0]);
        Date fromDate = dateFormatWithSlash.parse(dateItem + " " + from);
        Date toDate = dateFormatWithSlash.parse(dateItem + " " + to);
        elem.getParentElement().setTitle((toDate.getTime() - fromDate.getTime()) / (60 * 60 * 1000) + " h");
        elem.setInnerHTML(String.valueOf((toDate.getTime() - fromDate.getTime()) / (60 * 60 * 1000)));

    }

    public void setCurrentMonth(int currentMonth) {
        this.currentMonth = currentMonth;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    private void showEditDialog(String dateItems) {

        String[] temp = dateItems.split("##");
        id = Integer.valueOf(temp[0]);
        dateItem = temp[1];
        reasons.setSelectedNullLabel();
        CommonService.App.get().getEmployeePresentTime(String.valueOf(id), String.valueOf(dateItem), new AbstractAsyncCallback<AttendanceReportLogItem>() {
            @Override
            public void failure(Throwable throwable) {
                Window.alert(throwable.getMessage());
            }

            @Override
            public void success(AttendanceReportLogItem item) {
                if (item != null && item.getText() != null) {
                    box.setTitle(item.getDate().toString());
                    box.open();
                }
            }
        });
    }
}
