package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.rpc.ICommand;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Date;

/**
 * User: Ilhombek
 * Date: 1/2/12
 * Time: 12:40 PM
 */
public class CalendarTabWidget extends Composite {

    private Integer employeeID;
    private MaterialPanel mainPanel;
    private MaterialPanel middlePanel;
    private CalendarView calendarView;
    private MaterialPanel calendarPanel;
    private Integer year;
    private ICommand command;
    private Date currentDate;
    private String reasonCode;

    public CalendarTabWidget(Integer employeeID, Integer year, ICommand command) {
        this(employeeID, year, command, new Date(), null);
    }

    public CalendarTabWidget(Integer employeeID, Integer year, ICommand command, Date date, String reasonCode) {
        this.employeeID = employeeID;
        this.year = year;
        this.currentDate = date;
        this.reasonCode = reasonCode;
        initData();
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public void initData() {
        mainPanel = new MaterialPanel();
        middlePanel = new MaterialPanel("pg_leave__calendar-marks");
        viewShow();

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAVER_REQUEST_ADD, CalendarTabWidget.this, (sender, args) -> redrawCalendar());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAVER_REQUEST_APPROVED, CalendarTabWidget.this, (sender, args) -> redrawCalendar());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAVER_REQUEST_REJECTED, CalendarTabWidget.this, (sender, args) -> redrawCalendar());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAVER_REQUEST_DELETE, CalendarTabWidget.this, (sender, args) -> redrawCalendar());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CALENDAR_DRAW, CalendarTabWidget.this, (sender, args) -> addReasonsDescription());

        initWidget(mainPanel);
    }

    public void addReasonsDescription() {

        ArrayList<String> colours = calendarView.getReasonColours();
        ArrayList<String> names = calendarView.getReasonNames();

        middlePanel.clear();

        if (colours != null && names != null) {
            for (int i = 0; i < colours.size(); i++) {
                MaterialPanel panel = new MaterialPanel("pg_leave__calendar-mark-item");
                Span mark = new Span();
                mark.setStyleName("pg_leave__calendar-mark pg_leave__calendar-mark-approved");
                mark.addStyleName("leave-" + colours.get(i));
                Span title = new Span(names.get(i));
                title.setStyleName("pg_leave__calendar-mark-title");
                panel.add(mark);
                panel.add(title);
                middlePanel.add(panel);
            }
        }
    }

    public void viewShow() {
        setCalendarOffAndAppDays();
    }

    public void setCalendarOffAndAppDays() {
        mainPanel.clear();
        calendarView = new CalendarView(employeeID, reasonCode, year, command, getCurrentDate());
        calendarPanel = new MaterialPanel("pg_leave__calendar-datepicker");
        calendarPanel.add(calendarView);
        mainPanel.add(calendarPanel);
        mainPanel.add(middlePanel);
    }

    public void redrawCalendar() {
        Timer timer = new Timer() {
            @Override
            public void run() {
                calendarView.removeFromParent();
                calendarView = new CalendarView(employeeID, reasonCode, year, command);
                calendarPanel.add(calendarView);
            }
        };
        timer.schedule(2000);
    }

    public void setCommand(ICommand command) {
        this.command = command;
    }

    public CalendarView getCalendarView() {
        return calendarView;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }
}
