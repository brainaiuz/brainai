package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.draw;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.ui.HTML;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Span;

public class CalendarCheckBoxSideNav extends KpiSideNavBox {


    private final GoogleCalendarView.AppointmentCheckBox events;
    private final GoogleCalendarView.AppointmentCheckBox calls;
    private final GoogleCalendarView.AppointmentCheckBox projects;
    private final GoogleCalendarView.AppointmentCheckBox tasks;
    private final GoogleCalendarView.AppointmentCheckBox issues;
    private final GoogleCalendarView.AppointmentCheckBox leaveRequests;
    private final GoogleCalendarView.AppointmentCheckBox holidays;
    private final GoogleCalendarView.AppointmentCheckBox courses;

    private WfmButton2 saveBtn;

    public CalendarCheckBoxSideNav(GoogleCalendarView.AppointmentCheckBox events,
                                   GoogleCalendarView.AppointmentCheckBox calls,
                                   GoogleCalendarView.AppointmentCheckBox projects,
                                   GoogleCalendarView.AppointmentCheckBox tasks,
                                   GoogleCalendarView.AppointmentCheckBox issues,
                                   GoogleCalendarView.AppointmentCheckBox leaveRequests,
                                   GoogleCalendarView.AppointmentCheckBox holidays,
                                   GoogleCalendarView.AppointmentCheckBox courses) {
        super(300);
        this.events = events;
        this.calls = calls;
        this.projects = projects;
        this.tasks = tasks;
        this.issues = issues;
        this.leaveRequests = leaveRequests;
        this.holidays = holidays;
        this.courses = courses;
        setStyleName(getElement(), "quick-add", true);

        MaterialPanel marksPanel = new MaterialPanel("pg_google__calendar-marks");
        MaterialPanel eventItem = new MaterialPanel("pg_google__calendar-mark-item");
        Span eventColor = new Span();
        eventColor.setStyleName("pg_google__calendar-mark google-event-check-style");
        Span eventTitle = new Span(Property.getPluralWithObjectCode(Constants.EVENT_LIST, wfmStrings.events()));
        eventTitle.setStyleName("pg_google__calendar-mark-title");
        eventItem.add(eventColor);
        eventItem.add(eventTitle);
        eventItem.add(events);

        MaterialPanel callItem = new MaterialPanel("pg_google__calendar-mark-item");
        Span callColor = new Span();
        callColor.setStyleName("pg_google__calendar-mark google-call-check-style");
        Span callTitle = new Span(Property.get(Constants.LOGACALL, wfmStrings.logCall()));
        callTitle.setStyleName("pg_google__calendar-mark-title");
        callItem.add(callColor);
        callItem.add(callTitle);
        callItem.add(calls);

        MaterialPanel projectItem = new MaterialPanel("pg_google__calendar-mark-item");
        Span projectColor = new Span();
        projectColor.setStyleName("pg_google__calendar-mark project-check-style");
        Span projectTitle = new Span(wfmStrings.projects());
        projectTitle.setStyleName("pg_google__calendar-mark-title");
        projectItem.add(projectColor);
        projectItem.add(projectTitle);
        projectItem.add(projects);

        MaterialPanel taskItem = new MaterialPanel("pg_google__calendar-mark-item");
        Span taskColor = new Span();
        taskColor.setStyleName("pg_google__calendar-mark task-check-style");
        Span taskTitle = new Span(Property.getPluralWithObjectCode(Constants.TASK, wfmStrings.tasks()));
        taskTitle.setStyleName("pg_google__calendar-mark-title");
        taskItem.add(taskColor);
        taskItem.add(taskTitle);
        taskItem.add(tasks);

        MaterialPanel issueItem = new MaterialPanel("pg_google__calendar-mark-item");
        Span issueColor = new Span();
        issueColor.setStyleName("pg_google__calendar-mark issue-check-style");
        Span issueTitle = new Span(wfmStrings.issues());
        issueTitle.setStyleName("pg_google__calendar-mark-title");
        issueItem.add(issueColor);
        issueItem.add(issueTitle);
        issueItem.add(issues);

        MaterialPanel leaveRequestItem = new MaterialPanel("pg_google__calendar-mark-item");
        Span leaveRequestColor = new Span();
        leaveRequestColor.setStyleName("pg_google__calendar-mark leave-request-check-style");
        Span leaveRequestTitle = new Span(wfmStrings.leaveRequests());
        leaveRequestTitle.setStyleName("pg_google__calendar-mark-title");
        leaveRequestItem.add(leaveRequestColor);
        leaveRequestItem.add(leaveRequestTitle);
        leaveRequestItem.add(leaveRequests);

        MaterialPanel holidayItem = new MaterialPanel("pg_google__calendar-mark-item");
        Span holidayColor = new Span();
        holidayColor.setStyleName("pg_google__calendar-mark holiday-check-style");
        Span holidayTitle = new Span(wfmStrings.holidays());
        holidayTitle.setStyleName("pg_google__calendar-mark-title");
        holidayItem.add(holidayColor);
        holidayItem.add(holidayTitle);
        holidayItem.add(holidays);

        MaterialPanel courseItem = new MaterialPanel("pg_google__calendar-mark-item");
        Span courseColor = new Span();
        courseColor.setStyleName("pg_google__calendar-mark holiday-check-style");
        Span courseTitle = new Span(wfmStrings.courseSchedule());
        courseTitle.setStyleName("pg_google__calendar-mark-title");
        courseItem.add(courseColor);
        courseItem.add(courseTitle);
        courseItem.add(courses);

        marksPanel.add(eventItem);
        marksPanel.add(callItem);
        marksPanel.add(taskItem);
        marksPanel.add(leaveRequestItem);
        marksPanel.add(holidayItem);
        marksPanel.add(projectItem);
        marksPanel.add(issueItem);
        marksPanel.add(courseItem);

        addHeader(new HTML(wfmStrings.myCalendars()));
        addBody(marksPanel);
        show();
    }
}
