package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityServiceAsync;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveRequestLisItem;
import com.edatasite.workforce.gwt.availability.client.ui.view.customTabs.EmployeeLeaveRequestChart;
import com.edatasite.workforce.gwt.availability.client.ui.view.customTabs.EmployeeLeaveRequestsListTab;
import com.edatasite.workforce.gwt.availability.client.ui.view.customTabs.InOutReportTableTab;
import com.edatasite.workforce.gwt.availability.client.ui.view.customTabs.TimeSlotTabWidget;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.view.CalendarTabWidget;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTab;
import gwt.material.design.client.ui.MaterialTabItem;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.Date;

/**
 * User: Ilhombek
 * Date: 11/29/11
 * Time: 5:26 PM
 */
public class GlobalEmployeeLeaveRequestView extends View implements FittedContent {

    public static final String FROM_IN_OUT_ATTENDANCE_TRACKING_VIEW = "FROM_IN_OUT_ATTENDANCE_TRACKING_VIEW";
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private final AvailabilityServiceAsync availabilityService = AvailabilityService.App.get();
    private final DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy");
    private MaterialPanel generalPanel;
    private MaterialPanel bodyPanel;
    private final Integer employeeID;
    private final Integer int_requestID;
    private final String fromSection;
    private Integer selectedYear;
    private TextBox yearTextBox;
    private WfmButton2 yearButton;
    private DatePicker datePicker;
    private EmployeeLeaveRequestChart leaveChart;
    private EmployeeLeaveRequestsListTab employeeLeaveRequestsListTab;
    private CalendarTabWidget calendarTabWidget;
    private final String nameForm;
    private final String currentYear = dateFormat.format(new Date());

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected Span statisticShortcut;

    public GlobalEmployeeLeaveRequestView(Integer requestID, Integer employeeID, String name, String description) {
        this(requestID, employeeID, null, name, description, "");
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_REQUEST)) {
            setAddNew("availability|add/add");
        }
    }

    public GlobalEmployeeLeaveRequestView(Integer requestID, Integer employeeID, String selectedYear, String name, String description, String fromSection) {
        super(name);
        setDescription(property.getPlural(description));
        nameForm = name;
        this.int_requestID = requestID;
        this.employeeID = employeeID == null ? Utils.getUserID() : employeeID;
        if (selectedYear != null && !"".equals(selectedYear)) {
            this.selectedYear = Integer.parseInt(selectedYear);
        } else {
            this.selectedYear = Integer.parseInt(currentYear);
        }
        this.fromSection = fromSection;
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_REQUEST)) {
            setAddNew("availability|add/add");
        }
    }

    @Override
    public String getIconStyle() {
        return "availability global-employee-leave-req";
    }

    @Override
    protected Widget onInitialize() {
        generalPanel = new MaterialPanel("pg_leave");
        bodyPanel = new MaterialPanel("pg_leave__body");

        //first sidebar
        if (FROM_IN_OUT_ATTENDANCE_TRACKING_VIEW.equals(fromSection)) {
            generalPanel.addStyleName("pg_leave--approval");
            MaterialPanel ioPanel = new MaterialPanel("pg_leave__first-sidebar box-bg--1 box-radius");
            ioPanel.add(new InOutReportTableTab(hrmsStrings.inOutReport(), employeeID));
            bodyPanel.add(ioPanel);
        }

        Div label = new Div("pg_leave__chart-title");
        Div yearListDiv = new Div("pg_leave__chart-year-list");
        Div yearButtonDiv = new Div("pg_leave__chart-year-button");

        datePicker = new DatePicker();
        datePicker.setDate(new Date());
        datePicker.addChangeHandler(ch -> reloadWidgets(null));

        yearTextBox = new TextBox();
        yearTextBox.setAlignment(ValueBoxBase.TextAlignment.CENTER);
        yearTextBox.addKeyPressHandler(event -> {
            if (event.getNativeEvent().getKeyCode() == 10 || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                if (yearTextBox.getText() != null && !"".equals(yearTextBox.getText().trim())) {
                    reloadWidgets(null);
                }
            }
        });

        Validation.addPhoneNumberKeyboardListener(yearTextBox);
        yearButton = new WfmButton2(wfmStrings.show(), WfmButton2.BTN_PRIMARY, clickEvent -> reloadWidgets(null));

        if (Utils.isEnableProrataBasedAnnualLeave()) {
            yearListDiv.add(datePicker);
        } else {
            availabilityService.getLRYear(employeeID, selectedYear, new AbstractAsyncCallback<Integer>() {
                public void success(final Integer year) {
                    Scheduler.get().scheduleDeferred(() -> {
                        selectedYear = year;
                        yearTextBox.setText(year.toString());
                    });
                }
            });
            yearListDiv.add(yearTextBox);
        }
        yearButtonDiv.add(yearButton);
        yearButtonDiv.setMarginLeft(5);

        label.add(new HTML(wfmStrings.leaveStatus()));


        MaterialPanel headerPanel = new MaterialPanel();
        headerPanel.addStyleName("pg_leave__chart-header");
        headerPanel.add(label);
        headerPanel.add(yearListDiv);
        headerPanel.add(yearButtonDiv);

        leaveChart = new EmployeeLeaveRequestChart(employeeID, selectedYear);

        MaterialPanel chartPanel = new MaterialPanel("pg_leave__chart box-bg--1 box-radius");
        chartPanel.add(headerPanel);
        chartPanel.add(leaveChart);
        bodyPanel.add(chartPanel);

        drawRightPanel();
        drawBottomPanel();

        generalPanel.add(employeeLeaveRequestsListTab);
        generalPanel.add(bodyPanel);

        add(generalPanel);
        return null;
    }

    private void drawRightPanel() {
        MaterialPanel rightPanelDiv = new MaterialPanel("pg_leave__second-sidebar box-bg--1 box-radius");
        MaterialTab materialTab = new MaterialTab();
        materialTab.setShadow(1);
        materialTab.setIndicatorColor(Color.GREEN);
        rightPanelDiv.add(materialTab);

        MaterialTabItem calendarTabItem = new MaterialTabItem();
        MaterialLink calendarLink = new MaterialLink(wfmStrings.calendar());
        calendarLink.setHref("#calendar_tab");
        calendarTabItem.add(calendarLink);

        MaterialTabItem timeSlotTabItem = new MaterialTabItem();
        MaterialLink timeSlotLink = new MaterialLink(wfmStrings.timeslot());
        timeSlotLink.setHref("#timeslot_tab");
        timeSlotTabItem.add(timeSlotLink);

        materialTab.add(calendarTabItem);
        materialTab.add(timeSlotTabItem);

        MaterialPanel panel = new MaterialPanel();
        panel.setId("calendar_tab");

        MaterialPanel calendarTabBar = new MaterialPanel("pg_leave__calendar box-bg--1 box-radius");
        calendarTabWidget = new CalendarTabWidget(employeeID, selectedYear, this::reloadWidgets);
        calendarTabBar.add(calendarTabWidget);
        panel.add(calendarTabBar);

        TimeSlotTabWidget timeSlot = new TimeSlotTabWidget(hrmsStrings.defaulttimeslot(), employeeID);
        rightPanelDiv.add(panel);

        calendarLink.addClickHandler(event -> {
            panel.clear();
            panel.setId("calendar_tab");
            panel.add(calendarTabBar);
        });
        timeSlotLink.addClickHandler(event -> {
            panel.clear();
            panel.setId("timeslot_tab");
            panel.add(timeSlot);
        });
        materialTab.selectTab("calendar_tab");
        bodyPanel.add(rightPanelDiv);
    }

    private void reloadWidgets(Object object) {
        if (object != null) {
            Integer year;
            Date currentDate = (Date) object;
            year = Integer.parseInt(dateFormat.format(currentDate));
            if (selectedYear.equals(year)) {
                return;
            }
            selectedYear = year;
            yearTextBox.setText(selectedYear.toString());
        } else {
            int year = selectedYear;
            if (Utils.isEnableProrataBasedAnnualLeave()) {
                selectedYear = Integer.parseInt(dateFormat.format(datePicker.getDate()));
            } else {
                selectedYear = Integer.parseInt(yearTextBox.getText());
            }
            if (year != selectedYear) {
                calendarTabWidget.setYear(selectedYear);
                calendarTabWidget.setCalendarOffAndAppDays();
            }
        }

        leaveChart.setSelectedYear(selectedYear);
        leaveChart.setStartDate(datePicker.getDate());
        leaveChart.drawTab();

        employeeLeaveRequestsListTab.setSelectedYear(selectedYear);
        employeeLeaveRequestsListTab.draw();
    }

    private void drawBottomPanel() {
        employeeLeaveRequestsListTab = new EmployeeLeaveRequestsListTab(employeeID, int_requestID, selectedYear);
        employeeLeaveRequestsListTab.setChart(leaveChart);
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    public String getPropertyCode() {
        return "my_attendance";
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setEmployeeId(employeeID);
        fp.setYear(selectedYear);
        fp.setObjectId(int_requestID);
        fp.setSortField(LeaveRequestLisItem.FROM_DATE);
        fp.setAscending(false);
        AvailabilityService.App.get().getLeaveRequestList(fp, new AsyncCallback<ListResult<LeaveRequestLisItem>>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ListResult<LeaveRequestLisItem> leaveRequestLisItemListResult) {
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (leaveRequestLisItemListResult.getTotal() != null && leaveRequestLisItemListResult.getTotal() > 0) {
                        statisticShortcut.setText(countFormat(leaveRequestLisItemListResult.getTotal()));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }
        });
    }
}
