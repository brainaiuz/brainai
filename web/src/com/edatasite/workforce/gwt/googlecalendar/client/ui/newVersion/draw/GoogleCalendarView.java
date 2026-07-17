package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.draw;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.calendardatepicker.CalendarDatePicker;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxRow;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.googlecalendar.client.localization.GoogleCalendarMessages;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.CalendarFilter;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.CalendarFilterParameters;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.GoogleCalendarService;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.GoogleCalendarServiceAsync;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.UsersCalendarSettingsItem;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.WorkforceEvents;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.Calendar;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.CalendarSettings;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.CalendarViews;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.DateUtils;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.SaveAppointmentHandler;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.dayview.DayView;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.event.DeleteHandler;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.event.SaveHandler;
import com.edatasite.workforce.gwt.task.client.ui.quickadd.TaskQuickAddView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedTabBar;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSwitch;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Jan 25, 2010
 * Time: 4:10:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class GoogleCalendarView extends View implements Colapse, Constants {

    private final GoogleCalendarServiceAsync calendarService = GoogleCalendarService.App.get();
    private final GoogleCalendarMessages calendarMessages = GoogleCalendarMessages.App.get();
    private final DateTimeFormat monthFormat = DateTimeFormat.getFormat("MMMM");
    private final DateTimeFormat monthYearFormat = DateTimeFormat.getFormat("MMMM yyyy");
    private final DateTimeFormat weekFormat = DateTimeFormat.getFormat("MMM d");
    private final DateTimeFormat dayFormat = DateTimeFormat.getFormat("EEEE, MMM d");
    private static SaveHandler<Appointment> saveHandler;
    private static SaveAppointmentHandler saveAppointmentHandler;
    private FlexTable layoutTable;
    private VerticalPanel linkPanel;
    private MaterialPanel topPanel;
    private DecoratorPanel calendarViewDecorator;
    private DecoratorPanel datePickerDecorator;
    private DecoratorPanel checkBoxesDecorator;
    private DecoratedTabBar daysTabBar;
    private CalendarDatePicker datePicker;
    private EmployeeLookUp employeeLookUp;
    private Span monthNameSpan;
    private MaterialLink previousDay;
    private MaterialLink nextDay;
    private MaterialLink exportPDF;
    private DataListBox sortByLocation;
    private SelectItem edsLocation;
    private FlexTable selectedEmployees;
    private List<SelectItem> selectUsers;

    private Calendar selectedTab;
    private WfmButton2 today;
    private MaterialLink refreshButton;
    private Date selectedDate = new Date();

    private Calendar day;
    private Calendar week;
    private Calendar month;
    private Calendar agenda;

    private AppointmentCheckBox events;
    private AppointmentCheckBox calls;
    private AppointmentCheckBox projects;
    private AppointmentCheckBox tasks;
    private AppointmentCheckBox issues;
    private AppointmentCheckBox leaveRequests;
    private AppointmentCheckBox holidays;
    private AppointmentCheckBox courses;

    private GBox linksPanel;
    private GBoxRow linksPanelRow;
    private GBoxItem addEventPanel;
    private GBoxItem addTaskPanel;
    private ActionButton configureBoxItem;
    private MaterialLink addEventLink;
    private MaterialLink addTaskLink;
    private SimpleLink combinedCalendar;
    private ArrayList<Integer> idList = new ArrayList<>();
    private Date startDate;
    private boolean eventsIsLoaded = false;
    private boolean callsIsLoaded = false;
    private boolean projectsIsLoaded = false;
    private boolean tasksIsLoaded = false;
    private boolean issuesIsLoaded = false;
    private boolean paIsLoaded = false;
    private boolean leavesIsLoaded = false;
    private boolean holidaysIsLoaded = false;
    private boolean coursesIsLoaded = false;

    private boolean isRefreshing = false;
    /**
     * When the user selects other employee we have to store all temporary events.
     * Because, if the user wants to delete all other events, we should delete them immediately.
     */
    private ArrayList<Appointment> tempAppointments = new ArrayList<>();

    private Integer selectedType = 0;

    /**
     * If the user selects other employee, we should disable all his appointments.
     * Therefore we are storing all his or her appointments and then disabling or enabling them.
     */
    private WorkforceEvents userSelfAppointments;
    private UsersCalendarSettingsItem calendarSettingsItem;

    private int daysForPDF;
    private WfmButton2 syncButton;
    private int weekFirstDay;
    private List<Boolean> isOffice;
    private SelectItem[] timeZones;
    private Integer selectedTimeZoneID;

    public GoogleCalendarView(String name, String description, Integer calendarType, String dateString) {
        super(name, description);
        selectedType = calendarType;
        if (dateString != null) {
            selectedDate = DateTimeFormat.getFormat("dd-MMM-yyyy").parse(dateString);
        }
    }

    protected Widget onInitialize() {
        weekFirstDay = Integer.valueOf(Utils.userSettings.get(OVERALL_DATE_PICKER_WEEK_START));
        isRefreshing = true;
        initialize();
        layoutTable.removeStyleName("data-in");
        layoutTable.addStyleName("data-out");
        setData();
        clear();

        day.addSaveHandler(getSaveHandler());
        week.addSaveHandler(getSaveHandler());
        month.addSaveHandler(getSaveHandler());
        agenda.addSaveHandler(getSaveHandler());

        day.addDeleteHandler(getDeleteHandler());
        week.addDeleteHandler(getDeleteHandler());
        month.addDeleteHandler(getDeleteHandler());
        agenda.addDeleteHandler(getDeleteHandler());

        drawCalendarExtraButtons();

        datePickerDecorator.add(datePicker);
        datePicker.addValueChangeHandler(dateValueChangeEvent -> {
            selectedDate = dateValueChangeEvent.getValue();
            if (selectedDate.getMonth() != startDate.getMonth()) {
                if (selectedTab.getView() != null && selectedTab.getView() == CalendarViews.MONTH) {
                    startDate = DateUtil.getMonthFirstDay(selectedDate);
                } else {
                    startDate = DateUtil.getWeekFirstDay(selectedDate, weekFirstDay - 1);
                }
                refresh();
            }
            setSelectedDate();
            datePicker.setValue(selectedDate, false);
            datePicker.setCurrentMonth(selectedDate);
        });

        layoutTable.setStyleName("cal-MonthView__wrapper");
        layoutTable.setCellPadding(0);
        layoutTable.setCellSpacing(0);
        MaterialPanel topPanelDiv = new MaterialPanel("operPanel__wrapper");
        topPanelDiv.add(topPanel);
        layoutTable.setWidget(0, 1, topPanelDiv);

        layoutTable.setWidget(1, 0, calendarViewDecorator);
        layoutTable.getCellFormatter().setVerticalAlignment(1, 1, HasVerticalAlignment.ALIGN_TOP);
        setEnabledToCheckBoxes(true);

        saveAppointmentHandler = new SaveAppointmentHandler() {
            public void onSaveOrUpdate(Appointment appointment) {
                saveUpdateShareAppointment(appointment);
            }

            public void onSaveOrUpdateTask(Appointment appointment) {
                appointment.setStyle(appointment.isTask() ? appointment.getStyle() != null
                        ? appointment.getStyle()
                        : Appointment.GREEN : Appointment.GREEN);
                calendarService.saveCalendarTask(appointment, getCallback(appointment));
            }
        };
        clear();
        add(layoutTable);
        Div div = new Div("popup-overlay");
        div.addClickHandler(event -> {
            RootPanel.get().removeStyleName("cal-popup--opened");
        });
        add(div);
        layoutTable.removeStyleName("data-out");
        layoutTable.addStyleName("data-in");
        return null;
    }

    private void initialize() {
        layoutTable = new FlexTable();
        linkPanel = new VerticalPanel();
        topPanel = new MaterialPanel("operPanel--header operPanel");
        calendarViewDecorator = new DecoratorPanel();
        datePickerDecorator = new DecoratorPanel();
        checkBoxesDecorator = new DecoratorPanel();
        selectedEmployees = new FlexTable();
        selectedEmployees.setWidth("95%");

        daysTabBar = new DecoratedTabBar();
        daysTabBar.addTab(wfmStrings.day());
        daysTabBar.addTab(wfmStrings.week());
        daysTabBar.addTab(wfmStrings.month());
        daysTabBar.addTab(wfmStrings.agenda());
        if (daysTabBar.getSelectedTab() == -1) {
            daysTabBar.selectTab(selectedType);
        }
        daysTabBar.addSelectionHandler(event -> {
            switch (event.getSelectedItem()) {
                case 0:
                    RootPanel.get().removeStyleName("has-agenda");
                    onOpenTab(day);
                    selectedTab.setView(CalendarViews.DAY);
                    selectedTab.scrollToHour();
                    break;
                case 1:
                    RootPanel.get().removeStyleName("has-agenda");
                    onOpenTab(week);
                    break;
                case 2:
                    RootPanel.get().removeStyleName("has-agenda");
                    onOpenTab(month);
                    selectedTab.setView(CalendarViews.MONTH);
                    break;
                case 3:
                    RootPanel.get().addStyleName("has-agenda");
                    onOpenTab(agenda);
                    selectedTab.setView(CalendarViews.AGENDA);
                    break;
            }
            calendarService.saveCalendarSettings(getCalendarSettings(), new AbstractAsyncCallback<UsersCalendarSettingsItem>() {
                public void failure(Throwable caught) {

                }

                public void success(UsersCalendarSettingsItem result) {
                    userSelfAppointments.setCalendarSettings(result);
                }
            });
        });

        datePicker = new CalendarDatePicker(false);
        if (selectedDate != null) {
            datePicker.setValue(selectedDate, false);
            datePicker.setCurrentMonth(selectedDate);
        }
        employeeLookUp = new EmployeeLookUp(true, false, false);
        employeeLookUp.addStyleName(Constants.DEFAULT_WIDTH);
        employeeLookUp.getSuggestBox().addSelectionHandler(event -> onChangeEmployeeDropdown(getEmployeeID()));

        combinedCalendar = new SimpleLink("View consolidated calendar");
        combinedCalendar.addClickHandler(clickEvent -> {
            EmployeesSelectorPopup popUp = new EmployeesSelectorPopup(GoogleCalendarView.this);
            popUp.show();
        });

        DayView dayView = new DayView();
        dayView.setDisplayedDays(1);
        day = new Calendar(dayView);

        DayView weekView = new DayView();
        weekView.setDisplayedDays(7);
        week = new Calendar(weekView);
        month = new Calendar(CalendarViews.MONTH);
        agenda = new Calendar(CalendarViews.AGENDA);

        switch (selectedType) {
            case 0:
                selectedTab = day;
                break;
            case 1:
                selectedTab = week;
                break;
            case 2:
                selectedTab = month;
                break;
        }

        events = new AppointmentCheckBox();
        calls = new AppointmentCheckBox();
        projects = new AppointmentCheckBox();
        tasks = new AppointmentCheckBox();
        issues = new AppointmentCheckBox();
        leaveRequests = new AppointmentCheckBox();
        holidays = new AppointmentCheckBox();
        courses = new AppointmentCheckBox();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CALENDAR_EVENT_ADD, GoogleCalendarView.this, (sender, args) -> refresh());
    }

    private void setData() {
//        LoadingPanel.loading(true);
        calendarSettingsItem = null;
        /**
         * We are getting events in [(3 months before); (ahead 342 from the first day)] interval.
         */
        calendarService.getWorkforceTrackEvents(startDate, new AbstractAsyncCallback<WorkforceEvents>() {
            @Override
            public void failure(Throwable throwable) {
                isRefreshing = false;
                LoadingPanel.loading(false);
                LoadingPanel.loading(false);
            }

            public void success(WorkforceEvents wftEvents) {
                userSelfAppointments = wftEvents;
                CalendarSettings settings = CalendarSettings.DEFAULT_SETTINGS;
                settings.setWorkingHourStart(wftEvents.getWorkingHourStart());
                settings.setWorkingHourEnd(wftEvents.getWorkingHourEnd());
                if (wftEvents.getScrollToHour() != null && wftEvents.getScrollToHour() != 0) {
                    settings.setScrollToHour(wftEvents.getScrollToHour());
                } else {
                    settings.setScrollToHour(wftEvents.getWorkingHourStart());
                }
                day.setSettings(settings);
                week.setSettings(settings);
                month.setSettings(settings);
                agenda.setSettings(settings);

                Date start = wftEvents.getStartDate();
                Date end = wftEvents.getEndDate();

                calendarSettingsItem = wftEvents.getCalendarSettings();
                selectUsers = wftEvents.getSelectedUsers();
                if (edsLocation != null) {
                    sortByLocation.setSelected(edsLocation);
                }
                eventsIsLoaded = true;
                callsIsLoaded = true;
                projectsIsLoaded = true;
                tasksIsLoaded = true;
                issuesIsLoaded = true;
                paIsLoaded = true;
                leavesIsLoaded = true;
                holidaysIsLoaded = true;
                coursesIsLoaded = true;
                boolean isNoChacked = true;
                if (calendarSettingsItem != null) {
                    if (calendarSettingsItem.isEventIsChecked() != null && calendarSettingsItem.isEventIsChecked()) {
                        events.setChecked(true);
                        eventsIsLoaded = false;
                        isNoChacked = false;
                        loadEvents(start, end, false);
                    }
                    if (calendarSettingsItem.isCallIsChecked() != null && calendarSettingsItem.isCallIsChecked()) {
                        calls.setChecked(true);
                        callsIsLoaded = false;
                        isNoChacked = false;
                        loadEvents(start, end, true);
                    }
                    if (calendarSettingsItem.isProjectIsChecked() != null && calendarSettingsItem.isProjectIsChecked()) {
                        projects.setChecked(true);
                        projectsIsLoaded = false;
                        isNoChacked = false;
                        loadProjects(start, end);
                    }
                    if (calendarSettingsItem.isTaskIsChecked() != null && calendarSettingsItem.isTaskIsChecked()) {
                        tasks.setChecked(true);
                        tasksIsLoaded = false;
                        isNoChacked = false;
                        loadTasks(start, end);
                    }
                    if (calendarSettingsItem.isIssueIsChecked() != null && calendarSettingsItem.isIssueIsChecked()) {
                        issues.setChecked(true);
                        issuesIsLoaded = false;
                        isNoChacked = false;
                        loadIssues(start, end);
                    }

                    if (calendarSettingsItem.isLeaveRequestIsChecked() != null && calendarSettingsItem.isLeaveRequestIsChecked()) {
                        leaveRequests.setChecked(true);
                        leavesIsLoaded = false;
                        isNoChacked = false;
                        loadLeaveRequests(start, end);
                    }
                    if (calendarSettingsItem.isHolidayIsChecked() != null && calendarSettingsItem.isHolidayIsChecked()) {
                        holidays.setChecked(true);
                        holidaysIsLoaded = false;
                        isNoChacked = false;
                        loadHolidays(start, end);
                    }
                    if (calendarSettingsItem.isCourseIsChecked()) {
                        courses.setChecked(true);
                        coursesIsLoaded = false;
                        isNoChacked = false;
                        loadCourses(start, end);
                    }
                    if (isNoChacked) {
                        asyncLoaded();
                    }
                }
            }
        });
    }

    private void setSelectedEmployeePanel() {
        if (selectUsers != null && !selectUsers.isEmpty()) {
            for (int i = 0; i < selectUsers.size(); i++) {
                selectedEmployees.setText(i, 0, selectUsers.get(i).getName());
                selectedEmployees.getFlexCellFormatter().getElement(i, 0).getStyle().setPaddingLeft(5, Style.Unit.PX);
                selectedEmployees.setWidget(i, 1, new HTML());
                selectedEmployees.getFlexCellFormatter().setWidth(i, 0, "90%");
                selectedEmployees.getFlexCellFormatter().setWidth(i, 1, "10%");
                selectedEmployees.getFlexCellFormatter().getElement(i, 1).getStyle().setPaddingLeft(5, Style.Unit.PX);
                selectedEmployees.getFlexCellFormatter().getElement(i, 1).getStyle().setBackgroundColor(selectUsers.get(i).getDescription());
            }
            linkPanel.add(selectedEmployees);
        }
    }

    private void asyncLoaded() {
        if (eventsIsLoaded && projectsIsLoaded && tasksIsLoaded && issuesIsLoaded && paIsLoaded && leavesIsLoaded && holidaysIsLoaded && coursesIsLoaded) {
            LoadingPanel.loading(false);
            daysTabBar.selectTab(selectedType);
            isRefreshing = false;
        }
    }

    private void loadHolidays(Date start, Date end) {
        calendarService.getCalendarHolidays(start, end, new AbstractAsyncCallback<ArrayList<Appointment>>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(ArrayList<Appointment> result) {
                userSelfAppointments.setHolidays(result);
                if (result != null) {
                    setAppointmentsToCalendar(result, holidays);
                    holidaysIsLoaded = true;
                }
                asyncLoaded();
            }
        });
    }

    private void loadLeaveRequests(Date start, Date end) {
        calendarService.getCalendarLeaveRequests(idList, start, end, new AbstractAsyncCallback<ArrayList<Appointment>>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(ArrayList<Appointment> result) {
                userSelfAppointments.setLeaveRequests(result);
                if (result != null) {
                    setAppointmentsToCalendar(result, leaveRequests);
                    leavesIsLoaded = true;
                }
                asyncLoaded();
            }
        });
    }

    private void loadIssues(Date start, Date end) {
        calendarService.getCalendarIssues(start, end, new AbstractAsyncCallback<ArrayList<Appointment>>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(ArrayList<Appointment> result) {
                userSelfAppointments.setIssues(result);
                if (result != null) {
                    setAppointmentsToCalendar(result, issues);
                    issuesIsLoaded = true;
                }
                asyncLoaded();
            }
        });
    }

    private void loadTasks(Date start, Date end) {
        calendarService.getCalendarTasks(idList, start, end, false, true, false, new AbstractAsyncCallback<ArrayList<Appointment>>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(ArrayList<Appointment> result) {
                userSelfAppointments.setTasks(result);
                if (result != null) {
                    setAppointmentsToCalendar(result, tasks);
                    tasksIsLoaded = true;
                }
                asyncLoaded();
            }
        });
    }

    private void loadProjects(Date start, Date end) {
        calendarService.getCalendarProjects(idList, start, end, new AbstractAsyncCallback<ArrayList<Appointment>>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(ArrayList<Appointment> result) {
                userSelfAppointments.setProjects(result);
                if (result != null) {
                    setAppointmentsToCalendar(result, projects);
                    projectsIsLoaded = true;
                }
                asyncLoaded();
            }
        });
    }

    private void loadEvents(Date start, Date end, boolean isCall) {
        CalendarFilter filter = new CalendarFilter();
        filter.setEmployeeIDs(idList);
        filter.setStart(start);
        filter.setEnd(end);
        filter.setFromAgenda(false);
        filter.setForPDF(false);
        filter.setForUIOnly(true);
        filter.setCall(isCall);
        filter.setLocationID(edsLocation != null ? edsLocation.getId() : null);
        calendarService.getCalendarEvents(filter, new AbstractAsyncCallback<ArrayList<Appointment>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ArrayList<Appointment> result) {
                userSelfAppointments.setEvents(result);
                if (result != null) {
                    if (isCall) {
                        setAppointmentsToCalendar(result, calls);
                        callsIsLoaded = true;
                    } else {
                        setAppointmentsToCalendar(result, events);
                        eventsIsLoaded = true;
                    }
                }
                asyncLoaded();
            }
        });
    }

    private void loadCourses(Date start, Date end) {
        LoadingPanel.loading(true);
        CalendarFilter filter = new CalendarFilter();
        filter.setStart(start);
        filter.setEnd(end);
        filter.setLocationID(edsLocation != null ? edsLocation.getId() : null);
        calendarService.getCalendarCourses(filter, new AbstractAsyncCallback<ArrayList<Appointment>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ArrayList<Appointment> result) {
                super.onSuccess(result);
                userSelfAppointments.setCourses(result);
                if (result != null) {
                    setAppointmentsToCalendar(result, courses);
                    coursesIsLoaded = true;
                }
                asyncLoaded();
            }
        });
    }

    private void setAppointmentsToCalendar(ArrayList<Appointment> appointments, AppointmentCheckBox checkbox) {
        addAppointmentsToCalendar(appointments);
        checkbox.setAppointments(appointments);
    }

    private void addAppointmentsToCalendar(ArrayList<Appointment> appointments) {
        day.addAppointmentsWithoutRefresh(appointments);
        week.addAppointmentsWithoutRefresh(appointments);
        month.addAppointmentsWithoutRefresh(appointments);
        agenda.addAppointmentsWithoutRefresh(appointments);
    }

    private void onOpenTab(Calendar calendar) {
        /**
         * When the calendar has been synchronized it refreshed all values (set new instances) and
         * thus calendarViewDecorator didn't contain widget, because the new instance has been initialized.
         * If you were the same tab before refreshing it didn't show the opened tab, because
         * 'selectedTab.equals(calendar)' returns true and it didn't add widget to the calendarViewDecorator.
         * Thus we added a condition that is 'calendarViewDecorator.getWidget() == null'.
         */
        if (!selectedTab.equals(calendar) || calendarViewDecorator.getWidget() == null) {
            calendarViewDecorator.remove(selectedTab);
            calendarViewDecorator.add(selectedTab = calendar);

            setSelectedDate();
            selectedTab.scrollToHour();//Puts the scroll shown in the settings.(CalendarSettings -> scroll hour)
        }
    }

    private void setSelectedDate() {
        if (selectedTab.getView() == CalendarViews.MONTH) {
            selectedTab.setDate(selectedDate);
        } else {
            if (selectedTab.getDays() == 7) {//If calendar is weekly
                selectedTab.setDate(DateUtil.getWeekFirstDay(selectedDate, weekFirstDay - 1));

            } else {
                selectedTab.setDate(selectedDate);
            }
        }
        monthNameSpan.setText(createDateRangePanel());
    }

    private SaveHandler<Appointment> getSaveHandler() {
        saveHandler = appointmentSaveEvent -> {
            Appointment appointment = appointmentSaveEvent.getTarget();
            if (Utils.hasGenericAccess(GenericSettingsEnum.CONSOLIDATED_CALENDAR_ENABLED)) {
                if (appointment.isTask()) {
                    calendarService.saveCalendarTask(appointment, getCallback(appointment));
                } else {
                    saveUpdateShareAppointment(appointment);
                }
            } else {
                if (Appointment.GREEN.equals(appointment.getStyle()) || appointment.isTask()) {
                    appointment.setStyle(appointment.isTask() ? appointment.getStyle() != null
                            ? appointment.getStyle()
                            : Appointment.GREEN : Appointment.GREEN);
                    calendarService.saveCalendarTask(appointment, getCallback(appointment));
                } else {
                    saveUpdateShareAppointment(appointment);
                }
            }
        };
        return saveHandler;
    }

    private DeleteHandler<Appointment> getDeleteHandler() {
        return appointmentDeleteEvent -> {
            final Appointment appointment = appointmentDeleteEvent.getTarget();
            boolean withRemoveFromUI = true;
            if (Appointment.GREEN.equals(appointment.getStyle()) || appointment.isTask()) {
                calendarService.deleteTask(appointment.getObjectID(), appointment.getAction(), new AbstractAsyncCallback<Boolean>() {
                    public void success(Boolean isFullRefreshNeeded) {
                        if (isFullRefreshNeeded) {
                            refresh();
                        } else {
                            selectedTab.refresh();
                        }
                    }
                });
            } else {  // If appointment is not task then appointment is event.
                if (appointment.getObjectID() != null && appointment.getGuests() != null && !appointment.getGuests().isEmpty()) {
                    String message = wfmStrings.sureYouWantToDelete();
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNoCancel, message, wfmStrings.deleteWithNotification(), wfmStrings.deleteWithoutNotification(), null);
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            boolean withNotify = false;
                            if ("YES".equals(messageBox.getPressedButtonName())) {
                                withNotify = true;
                            } else if ("NO".equals(messageBox.getPressedButtonName())) {
                                withNotify = false;
                            }
                            removeApprointmentsWithoutRefresh(appointment);
                            calendarService.deleteEvent(getEmployeeID(), appointment.getObjectID(), appointment.getAction(), withNotify, new AbstractAsyncCallback<Boolean>() {
                                public void success(Boolean isFullRefreshNeeded) {
                                    if (isFullRefreshNeeded) {
                                        refresh();
                                    } else {
                                        selectedTab.refresh();
                                    }
                                }
                            });
                        }
                    });
                    messageBox.setTitle(Property.get(Constants.EVENT_LIST, wfmStrings.deleteThe(), wfmStrings.event()));
                    messageBox.open();
                    withRemoveFromUI = false;
                    appointmentDeleteEvent.setCancelled(true);
                } else {
                    calendarService.deleteEvent(getEmployeeID(), appointment.getObjectID(), appointment.getAction(), false, new AbstractAsyncCallback<Boolean>() {
                        public void success(Boolean isFullRefreshNeeded) {
                            if (isFullRefreshNeeded) {
                                refresh();
                            } else {
                                selectedTab.refresh();
                            }
                        }
                    });
                }
            }
            if (withRemoveFromUI) {
                removeApprointmentsWithoutRefresh(appointment);
            }
        };
    }

    private void removeApprointmentsWithoutRefresh(Appointment appointment) {
        day.removeAppointmentWithoutRefresh(appointment);
        week.removeAppointmentWithoutRefresh(appointment);
        month.removeAppointmentWithoutRefresh(appointment);
        agenda.removeAppointmentWithoutRefresh(appointment);
        if (appointment.getRecurrenceId() != null) {
            ArrayList<Appointment> appointmentList = selectedTab.getAppointmentsByRecurring(appointment);
            if (appointmentList != null && appointmentList.size() > 0) {
                for (Appointment tempAppointment : appointmentList) {
                    day.removeAppointmentWithoutRefresh(tempAppointment);
                    week.removeAppointmentWithoutRefresh(tempAppointment);
                    month.removeAppointmentWithoutRefresh(tempAppointment);
                    agenda.removeAppointmentWithoutRefresh(tempAppointment);
                }
            }
        }
        selectedTab.refresh();
    }

    private void saveUpdateShareAppointment(final Appointment appointment) {
        if (appointment.getAttendees() != null && appointment.getAttendees().size() > 0) {
            calendarService.getConflictedEmployees(appointment.getAttendees(), appointment.getStartDate(), appointment.getEndDate(), appointment.getObjectID(), new AbstractAsyncCallback<ArrayList<SelectItem>>() {
                public void success(ArrayList<SelectItem> conflictedEmployees) {
                    drawConflictView(appointment, conflictedEmployees);
                }
            });

        } else {
            saveOrUpdateWithWarning(appointment);
        }
    }

    private void saveOrUpdateWithWarning(final Appointment appointment) {
        if (idList.size() == 1 && idList.get(0).equals(Utils.getUserID())) {
            if (Appointment.GREEN.equals(appointment.getStyle()) || appointment.isTask()) {
                if (appointment.getObjectID() != null) {
                    appointment.setStyle(appointment.isTask() ? appointment.getStyle() != null
                            ? appointment.getStyle()
                            : Appointment.GREEN : Appointment.GREEN);
                    calendarService.saveCalendarTask(appointment, getCallback(appointment));
                }
            } else {
                if (appointment.getObjectID() != null && appointment.getGuests() != null && !appointment.getGuests().isEmpty() && (appointment.getGuests().size() > appointment.getNewGuestsCount())) {
                    String message = wfmStrings.sendUpdatesToGuests();
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNoCancel, message, wfmStrings.send(), wfmStrings.doNotSend(), null);
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            boolean withNotify = false;
                            if ("YES".equals(messageBox.getPressedButtonName())) {
                                withNotify = true;
                            } else if ("NO".equals(messageBox.getPressedButtonName())) {
                                withNotify = false;
                            }
                            calendarService.saveCalendarEvent(getEmployeeID(), appointment, withNotify, getCallback(appointment));
                        }
                    });
                    messageBox.setTitle(Property.get(Constants.EVENT_LIST, wfmStrings.editEvent(), wfmStrings.event()));
                    messageBox.open();
                } else {
                    calendarService.saveCalendarEvent(idList.get(0), appointment, false, getCallback(appointment));
                }
            }
        } else {
            String message = wfmStrings.sendUpdatesToGuests();
            if (appointment.getObjectID() != null && appointment.getGuests() != null && !appointment.getGuests().isEmpty() && (appointment.getGuests().size() > appointment.getNewGuestsCount())) {
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNoCancel, message, wfmStrings.send(), wfmStrings.doNotSend(), null);
                messageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        boolean withNotify = false;
                        if ("YES".equals(messageBox.getPressedButtonName())) {
                            withNotify = true;
                        } else if ("NO".equals(messageBox.getPressedButtonName())) {
                            withNotify = false;
                        }
                        calendarService.saveCalendarEvent(getEmployeeID(), appointment, withNotify, getCallback(appointment));
                    }
                });
                messageBox.setTitle(Property.get(Constants.EVENT_LIST, wfmStrings.editEvent(), wfmStrings.event()));
                messageBox.open();
            } else {
                final WfmMessageBox messageBox = getWarningMessageBox(message);
                messageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        calendarService.saveCalendarEvent(getEmployeeID(), appointment, false, getCallback(appointment));
                    }
                });
                messageBox.open();
            }
        }
    }

    private Integer getEmployeeID() {
        if (employeeLookUp != null && employeeLookUp.getSelectedItem() != null) {
            return employeeLookUp.getSelectedItem().getId();
        }

        return Utils.getUserID();
    }

    private AbstractAsyncCallback<SelectItem> getCallback(final Appointment appointment) {
//        LoadingPanel.loading(true);

        return new AbstractAsyncCallback<SelectItem>() {
            /**
             * In SelectItem we are receiving event's id and googleID;
             *
             * @param event
             */
            public void success(SelectItem event) {
                /**
                 * If we are updating an existing appointment, there is no need to reload it then.
                 * We must reload all calendars when we create an appointment, otherwise no extra action is necessary.
                 * If appointment hasn't id, we have to set id, otherwise we cannot edit it,
                 * because if there is no id for an existing event, it creates new one.
                 */
                RelationItem.setFromID(event.getId(), appointment.getRelations());
                RelationItem.setFromName(event.getId(), RelationItem.TYPE_EVENT, event.getName(), appointment.getRelations());
                if (appointment.getObjectID() == null) {
                    appointment.setObjectID(event.getId());
                    if (event.getDescription() != null) {
                        try {
                            if (event.getDescription() != null && !"".equals(event.getDescription())) {
                                String[] components = event.getDescription().split("#");
                                appointment.setOwnerID(Integer.valueOf(components[0]));
                                if (components.length > 1) {
                                    appointment.setLinkURL(components[1]);
                                }
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    reloadAppointment(appointment);
                    appointment.setGoogleID(event.getName());
                }
                if (appointment.getRecurrenceId() != null || appointment.getRecurrenceJobItem() != null /*|| !appointment.getBookingReservationItemList().isEmpty()*/) {
                    refresh();
                }
                if (Appointment.BLUE.equals(appointment.getStyle())) {
                    appointment.setVisible(events.isChecked());
                } else if (Appointment.AQUA.equals(appointment.getStyle())) {
                    appointment.setVisible(calls.isChecked());
                } else if (Appointment.GREEN.equals(appointment.getStyle()) || appointment.isTask()) {
                    appointment.setVisible(tasks.isChecked());
                }

                if (appointment.getRecurrenceJobItem() == null) {
                    ArrayList<Appointment> appointments1 = (ArrayList<Appointment>) selectedTab.getAppointments().clone();
                    ArrayList<Appointment> appointments = (ArrayList<Appointment>) selectedTab.getAppointments().clone();
                    for (Appointment tmpAppointment : appointments1) {
                        if (tmpAppointment.getObjectID() != null && appointment.getObjectID() != null) {
                            //replace If only both comparables are TASKS
                            if (tmpAppointment.getObjectID().equals(appointment.getObjectID()) &&
                                    (Appointment.GREEN.equals(tmpAppointment.getStyle()) || appointment.isTask()) &&
                                    (Appointment.GREEN.equals(appointment.getStyle()) || appointment.isTask())) {
                                appointments.remove(tmpAppointment);
                                appointments.add(appointment);
                                break;
                            }
                            //replace If only both comparables are EVENTS
                            if (tmpAppointment.getObjectID().equals(appointment.getObjectID()) &&
                                    Appointment.BLUE.equals(tmpAppointment.getStyle()) &&
                                    Appointment.BLUE.equals(appointment.getStyle())) {
                                appointments.remove(tmpAppointment);
                                appointments.add(appointment);
                                break;
                            }
                            if (tmpAppointment.getObjectID().equals(appointment.getObjectID()) &&
                                    Appointment.AQUA.equals(tmpAppointment.getStyle()) &&
                                    Appointment.AQUA.equals(appointment.getStyle())) {
                                appointments.remove(tmpAppointment);
                                appointments.add(appointment);
                                break;
                            }
                        }
                    }
                    day.clearAppointments();
                    day.addAppointments(appointments);
                    week.clearAppointments();
                    week.addAppointments(appointments);
                    month.clearAppointments();
                    month.addAppointments(appointments);
                    agenda.clearAppointments();
                    agenda.addAppointments(appointments);
                    selectedTab.scrollToHour();
                }
                if (appointment.isClone()) {
                    isRefreshing = false;
                    refresh();
                } else {
                    selectedTab.refresh();
                }
                // this part code need for showing message about saving recurring task with postProcessor
                if ((Appointment.GREEN.equals(appointment.getStyle()) || appointment.isTask()) && appointment.getRecurrenceJobItem() != null) {
                    if (event.getName() != null && !"".equals(event.getName())) {
                        Integer instances = Integer.valueOf(event.getName());
                        if (instances > 10) {
                            String messageText = wfmStrings.addingYourRecurringTasks();
                            if (appointment.getAction() != null) {
                                if (appointment.getAction() != null && appointment.getAction().contains("edit_")) {
                                    messageText = wfmStrings.updatingYourRecurringTasks();
                                }
                            }
                            Info.show(messageText, Info.Type.INFO);
                        }
                    }
                }
//                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CALENDAR_EVENT_ADD, appointment.getObjectID(), GoogleCalendarView.this);
                LoadingPanel.loading(false);
            }
        };
    }

    private void reloadAppointment(Appointment appointment) {
        appointment.setAllDay(appointment.isAllDay());
        appointment.setMultiDay(appointment.isMultiDayAppointment());
        loadAppointment(appointment);
    }

    private void loadAppointment(Appointment appointment) {
        day.addAppointmentWithoutRefresh(appointment);
        week.addAppointmentWithoutRefresh(appointment);
        month.addAppointmentWithoutRefresh(appointment);
        agenda.addAppointmentWithoutRefresh(appointment);

        events.addAppointment(appointment);
        calls.addAppointment(appointment);
        tasks.addAppointment(appointment);

        if (!getEmployeeID().equals(Utils.getUserID())) {
            tempAppointments.add(appointment);
        }
    }

    private void drawConflictView(final Appointment appointment, ArrayList<SelectItem> conflictedEmployees) {
        if (conflictedEmployees.size() == 0) {//If there are no conflicted employees.
            saveOrUpdateAndShareWithWarning(appointment);
        } else {
            StringBuilder message = new StringBuilder();
            message.append(calendarMessages.conflictsWithTheseEmployees(String.valueOf(conflictedEmployees.size())));
            int counter = 1;
            for (SelectItem employee : conflictedEmployees) {
                message.append("<br><b style='font-size:11px;'>").append(counter).append(". ").append(employee.getName()).append("</b>");
                counter++;
            }

            if (counter > 10) {
                counter = 10;
            }
            int height = counter * 12 + 100;

            final WfmMessageBox messageBox = getWarningMessageBox(message.toString());
            messageBox.setSize(400, height);
            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    saveOrUpdateAndShareWithWarning(appointment);
                }
            });
            messageBox.open();
        }
    }

    private void saveOrUpdateAndShareWithWarning(final Appointment appointment) {
        if (getEmployeeID().equals(Utils.getUserID())) {//If the user begin to create an event for another employee.(Just warning to notice)
            if (appointment.getObjectID() != null && appointment.getGuests() != null && !appointment.getGuests().isEmpty() && (appointment.getGuests().size() > appointment.getNewGuestsCount())) {
                String message = wfmStrings.sendUpdatesToGuests();
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNoCancel, message, wfmStrings.send(), wfmStrings.doNotSend(), null);
                messageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        boolean withNotify = false;
                        if ("YES".equals(messageBox.getPressedButtonName())) {
                            withNotify = true;
                        } else if ("NO".equals(messageBox.getPressedButtonName())) {
                            withNotify = false;
                        }
                        calendarService.saveCalendarEvent(getEmployeeID(), appointment, withNotify, getCallback(appointment));
                    }
                });
                messageBox.setTitle(Property.get(Constants.EVENT_LIST, wfmStrings.editEvent(), wfmStrings.event()));
                messageBox.open();
            } else {
                calendarService.saveCalendarEvent(getEmployeeID(), appointment, false, getCallback(appointment));
            }
        } else {
            String message = wfmStrings.youAreGoingToCreateAndShare();
            final WfmMessageBox messageBox = getWarningMessageBox(message);
            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    calendarService.saveCalendarEvent(getEmployeeID(), appointment, false, getCallback(appointment));
                }
            });
            messageBox.open();
        }
    }

    private WfmMessageBox getWarningMessageBox(String message) {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OkCancel, message, wfmStrings.continueAnyway(), wfmStrings.cancel(), null);
        messageBox.setTitle(wfmStrings.warning());
        messageBox.setMessage(message);
        return messageBox;
    }

    private void drawCalendarExtraButtons() {
        today = new WfmButton2(wfmStrings.today(), WfmButton2.BTN_WHITE);
        today.removeStyleName("hasicon--left");
        today.setTooltip(wfmStrings.today());
        today.addClickHandler(clickEvent -> {
            setClickedDate(new Date());
            monthNameSpan.setText(createDateRangePanel());
        });
        previousDay = new MaterialLink();
        SvgIcon prevIcon = new SvgIcon(SvgEnum.chevronLeft);
        previousDay.add(prevIcon);
        previousDay.removeStyleName("hasicon--left");
        previousDay.addStyleName("btn--icon");
        previousDay.setTooltip(wfmStrings.previous());
        previousDay.addClickHandler(clickEvent -> {
            addDays(-1, -selectedTab.getDays());
            monthNameSpan.setText(createDateRangePanel());
            if (!CalendarViews.MONTH.equals(selectedTab.getView())) {
            }
        });
        nextDay = new MaterialLink();
        SvgIcon nextIcon = new SvgIcon(SvgEnum.chevronRight);
        nextDay.add(nextIcon);
        nextDay.removeStyleName("hasicon--left");
        nextDay.addStyleName("btn--icon");
        nextDay.setTooltip(wfmStrings.nextstr());
        nextDay.addClickHandler(clickEvent -> {
            addDays(1, selectedTab.getDays());
            monthNameSpan.setText(createDateRangePanel());
            if (!CalendarViews.MONTH.equals(selectedTab.getView())) {
            }
        });


        refreshButton = new MaterialLink();
        SvgIcon refreshIcon = new SvgIcon(SvgEnum.rotateCw);
        refreshButton.add(refreshIcon);
        refreshButton.removeStyleName("hasicon--left");
        refreshButton.addStyleName("btn--icon");
        refreshButton.setTooltip(wfmStrings.refresh());
        refreshButton.addClickHandler(event -> refresh());

        exportPDF = new MaterialLink();
        exportPDF.removeStyleName("hasicon--left");
        exportPDF.addStyleName("btn--icon");
        exportPDF.setTitle(wfmStrings.exportToPDF1());
        exportPDF.addClickHandler(clickEvent -> drawPDFView());

        MaterialPanel navigationPanel = new MaterialPanel("operPanel__actions");
        MaterialPanel btnGroup = new MaterialPanel("btn-group");
        btnGroup.add(previousDay);
        btnGroup.add(today);
        btnGroup.add(nextDay);
        MaterialPanel navigationButtonPanel = new MaterialPanel("operPanel__btn-groups");
        navigationButtonPanel.add(btnGroup);
        navigationPanel.add(getAddButtons());
        navigationPanel.add(navigationButtonPanel);
        monthNameSpan = new Span(createDateRangePanel());
        navigationPanel.add(monthNameSpan);
        navigationPanel.add(refreshButton);

        sortByLocation = new DataListBox();

        calendarService.getLocationAsSelectItem(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void success(SelectItem[] result) {
                sortByLocation.setItems(result);
                if (edsLocation != null) {
                    sortByLocation.setSelected(edsLocation);
                }
            }
        });
        sortByLocation.addValueChangeHandler(event -> {
            edsLocation = sortByLocation.getSelectedItem();
            refresh();
        });
        if (Utils.isTrainingCenterEnabled()) {
            navigationPanel.add(sortByLocation);
        }

        drawEmployeeDropdown();
        if (Utils.hasGenericAccess(GenericSettingsEnum.CONSOLIDATED_CALENDAR_ENABLED)) {
            navigationPanel.add(combinedCalendar);
        } else if (Utils.hasRole(Constants.CALENDAR_EDITOR) || Utils.hasRole(Constants.CALENDAR_VIEWER)) {
            navigationPanel.add(employeeLookUp);
        }

        topPanel.add(navigationPanel);

        MaterialPanel tabContainerPanel = new MaterialPanel("operPanel__settings ");
        Div daysDiv = new Div();
        daysDiv.setStyleName("pg_google__tabs");
        daysDiv.add(daysTabBar);
        tabContainerPanel.add(daysDiv);

        Date startDate = DateUtil.getDateTime();
        final Appointment newAppointment = new Appointment(startDate);

//        configureBoxItem = new ActionButton("Configure..", ActionButton.Type.TOOLMENU);
//        configureBoxItem.setStyleName("btn btn--default hover");
//        LoginService.App.get().isValidUserOfficeAndGoogle(Constants.OFFICE_365, new AbstractAsyncCallback<ArrayList<Boolean>>() {
//            public void failure(Throwable throwable) {
//            }
//
//            public void success(ArrayList<Boolean> result) {
//                isOffice = result;
//                getGoogleSettings(newAppointment);
//            }
//        });
//        tabContainerPanel.add(configureBoxItem);

        Span configure = new Span("");
        configure.setStyleName("btn btn--icon");
        configure.setTooltip(wfmStrings.customize());
        configure.setTooltipPosition(Position.TOP);
        configure.addClickHandler(event -> {
            CalendarCheckBoxSideNav calendarCheckBoxSideNav = new CalendarCheckBoxSideNav(events, calls, projects, tasks, issues, leaveRequests, holidays, courses);
        });
        configure.getElement().setInnerHTML("<svg class=\"icon--sliders\"><use href=\"mainStyles/new-ui/icons/sprite__panels.svg?v=" + Utils.getUploadVersion() + "#sliders\"></use></svg>");
        Div action = new Div();
        action.add(configure);
        tabContainerPanel.add(action);

        topPanel.add(tabContainerPanel);
    }

    private ActionButton getAddButtons() {
        MenuBar menuBar = new MenuBar(true);
        ActionButton add = new ActionButton("<div class=\"btn btn--new btn--circle\"><svg class=\"icon--plus\"><use href=\"mainStyles/new-ui/icons/sprite__panels.svg?v=" + Utils.getUploadVersion() + "#plus\"></use></svg></div>", ActionButton.Type.TOOLMENU);
        //Event add
        Date startDate = DateUtil.getDateTime();
        final Appointment newAppointment = new Appointment(startDate);
        MenuPopItem eddEvent = new MenuPopItem(Property.get(Constants.EVENT_LIST, wfmStrings.event()), null, () -> {
            Appointment appointment = new Appointment(DateUtil.getDateTime());
            appointment.setHasGoogleAccount(newAppointment.hasGoogleAccount());
            appointment.setActivityType(Appointment.EVENT);
            ActivityQuickAddForm activityQuickAddView = new ActivityQuickAddForm(appointment);
            activityQuickAddView.setHandler(saveAppointmentHandler);
        });
        eddEvent.getElement().setId("addEvent");
        if (!Utils.hasRole(Constants.CALENDAR_VIEWER)) {
            if (idList != null && idList.size() == 1 && idList.get(0).equals(Utils.getUserID())) {
                menuBar.addItem(eddEvent);
            }
        } else {
            if (Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.DR) || Utils.hasRole(Constants.CALENDAR_EDITOR)) {
                menuBar.addItem(eddEvent);
            } else if (idList != null && idList.size() == 1 && idList.get(0).equals(Utils.getUserID())) { // if this is user's calendar
                menuBar.addItem(eddEvent);
            }
        }

        //Log a call add
        final Appointment newAppointmentLogaCall = new Appointment(startDate);
        MenuPopItem addLogCall = new MenuPopItem(Property.get(LOGACALL, wfmStrings.logCall()), null, () -> {
            Appointment appointment = new Appointment(DateUtil.getDateTime());
            appointment.setHasGoogleAccount(newAppointmentLogaCall.hasGoogleAccount());
            appointment.setActivityType(Appointment.CALL_LOG);
            ActivityQuickAddForm activityQuickAddView = new ActivityQuickAddForm(appointment);
            activityQuickAddView.setHandler(saveAppointmentHandler);
        });
        addLogCall.getElement().setId("addLLogCall");
        if (!Utils.hasRole(Constants.CALENDAR_VIEWER)) {
            if (idList != null && idList.size() == 1 && idList.get(0).equals(Utils.getUserID())) {
                menuBar.addItem(addLogCall);
            }
        } else {
            if (Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.DR) || Utils.hasRole(Constants.CALENDAR_EDITOR)) {
                menuBar.addItem(addLogCall);
            } else if (idList != null && idList.size() == 1 && idList.get(0).equals(Utils.getUserID())) { // if this is user's calendar
                menuBar.addItem(addLogCall);
            }
        }

        //Task add
        MenuPopItem addTask = new MenuPopItem(wfmStrings.task(), null, () -> new TaskQuickAddView());
        addTask.getElement().setId("addTask");
        if (!Utils.hasRole(Constants.CALENDAR_VIEWER)) {
            if (idList != null && idList.size() == 1 && idList.get(0).equals(Utils.getUserID())) {
                menuBar.addItem(addTask);
            }
        } else {
            if (Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.DR) || Utils.hasRole(Constants.CALENDAR_EDITOR)) {
                menuBar.addItem(addTask);
            } else if (idList != null && idList.size() == 1 && idList.get(0).equals(Utils.getUserID())) { // if this is user's calendar
                menuBar.addItem(addTask);
            }
        }

        if (MODULE_HRMS.equals(GWT.getModuleName()) && Utils.hasPermission(PermissionConstants.HRMS_ADD_REQUEST)) {
            //LR add
            MenuPopItem addLR = new MenuPopItem(wfmStrings.leaveRequest(), null, () -> SinksContainerFactory.entryPoint.onHistoryChanged("availability|add/add"));
            addLR.getElement().setId("addLR");
            menuBar.addItem(addLR);
        }
        add.setMenu(menuBar);

        return add;
    }

    private String createDateRangePanel() {
        String dateString = monthYearFormat.format(selectedDate);
        if (selectedTab.getDays() == 7) {//If calendar is weekly
            Date weekFirstDate = DateUtil.getWeekFirstDay(selectedDate, weekFirstDay - 1);
            int weekEndDay = 1;//by defaul week end date id Sunday
            if (weekFirstDay == 1) {//if Sunday
                weekEndDay = 7;//end day of week is Saturday
            } else if (weekFirstDay == 7) {//if Saturday
                weekEndDay = 6;//end day of the week is Friday
            }
            Date weekLastDate = DateUtil.getWeekLastDay(selectedDate, weekEndDay - 1);
            dateString = weekFormat.format(weekFirstDate) + " - " + weekFormat.format(weekLastDate);
        }
        return dateString;
    }

    private void drawPDFView() {
        final KpiModal shell = new KpiModal();
        shell.setTitle(wfmStrings.exportToPDF1());
        shell.setSize(350, 170);

        final DatePicker start = new DatePicker(true);
        start.setDate(new Date());
        final DatePicker end = new DatePicker(true);
        end.setDate(DateUtil.addDays(new Date(), 3));

        final RadioButton oneDay = initRadioButtons(wfmStrings.oneDay(), 1, false, start, end);
        oneDay.setValue(true);
        final RadioButton threeDays = initRadioButtons(wfmStrings.threeDays(), 3, false, start, end);
        final RadioButton oneWeek = initRadioButtons(wfmStrings.sevenDays(), 7, false, start, end);
        final RadioButton custom = initRadioButtons(wfmStrings.custom(), 0, true, start, end);

        FlexTable table = initDatePickers(start, end, 4);
        table.setWidget(0, 0, oneDay);
        table.setWidget(1, 0, threeDays);
        table.setWidget(2, 0, oneWeek);
        table.setWidget(3, 0, custom);

        final HorizontalPanel hp = new HorizontalPanel();
        hp.add(table);

        WfmButton2 exportButton = new WfmButton2(wfmStrings.exportToPDF1(), event -> {
            Date dueDate = selectedDate;
            if (custom.getValue()) {
                dueDate = DateUtils.resetTime(start.getDate());
                daysForPDF = DateUtil.countDays(start.getDate(), end.getDate());
            } else {
                if (oneDay.getValue()) {
                    daysForPDF = 1;
                } else if (threeDays.getValue()) {
                    daysForPDF = 3;
                } else if (oneWeek.getValue()) {
                    daysForPDF = 7;
                }
            }
            generatePdf(hp, daysForPDF, dueDate);
            shell.close();
        });

        table.setWidget(5, 1, exportButton);
        table.getCellFormatter().setHorizontalAlignment(5, 1, HasAlignment.ALIGN_RIGHT);

        shell.add(hp);
        shell.open();
    }

    private RadioButton initRadioButtons(String label, final int days, final boolean custom, final DatePicker start, final DatePicker end) {
        RadioButton radio = new KpiRadioButton("pdf", " " + label);
        radio.addValueChangeHandler(event -> {
            daysForPDF = days;

            boolean enable;
            if (custom) {
                enable = event.getValue();
                daysForPDF = DateUtil.countDays(start.getDate(), end.getDate());
            } else {
                enable = !event.getValue();
            }

            start.setEnabled(enable);
            end.setEnabled(enable);
        });
        return radio;
    }

    private FlexTable initDatePickers(final DatePicker start, final DatePicker end, int row) {
        start.setEnabled(false);
        start.setWidth("135px");

        end.setEnabled(false);
        end.setWidth("135px");

        start.addChangeHandler(dateValueChangeEvent -> start.setStyleName(""));

        end.addChangeHandler(dateValueChangeEvent -> end.setStyleName(""));

        FlexTable dateRange = new FlexTable();
        dateRange.setWidget(row, 0, start);
        dateRange.setWidget(row, 1, end);

        return dateRange;
    }

    private void generatePdf(ComplexPanel panel, Integer days, Date dueDate) {
        String pdfURL = CommandConstants.PDF_URL + "/calendarAgendaListPDFHanlder";
        CalendarFilterParameters parameters = new CalendarFilterParameters();
        parameters.setShowEvent(events.isChecked());
        parameters.setShowCall(calls.isChecked());
        parameters.setShowProject(projects.isChecked());
        parameters.setShowTasks(tasks.isChecked());
        parameters.setShowIssues(issues.isChecked());
        parameters.setShowLeaveRequest(leaveRequests.isChecked());
        parameters.setShowHolidays(holidays.isChecked());
        parameters.setDueDate(dueDate.getTime());
        parameters.setDays(days);
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters.getRequestParameters(), "_blank");
    }

    private void addDays(int months, int days) {
        Date date;
        if (selectedTab.getView() != null && selectedTab.getView() == CalendarViews.MONTH) {
            date = DateUtil.addMonths(selectedTab.getDate(), months);
        } else {
            date = DateUtil.addDays(selectedTab.getDate(), days);
        }

        setClickedDate(date);
    }

    private void setClickedDate(Date date) {
        selectedDate = date;
        if (date.getMonth() != startDate.getMonth()) {
            startDate = DateUtil.getMonthFirstDay(date);
            refresh();
        }
        datePicker.setValue(date, true);
        datePicker.setCurrentMonth(date);
    }

    private void refresh() {
        if (!isRefreshing) {
//            LoadingPanel.loading(true);
            int selectedTab = daysTabBar.getSelectedTab();
            selectedType = selectedTab;
            onInitialize();
        }
    }

    private void drawEmployeeDropdown() {
        if (Utils.hasGenericAccess(GenericSettingsEnum.CONSOLIDATED_CALENDAR_ENABLED)) {
            setSelectedEmployeePanel();
        }
    }

    private void getGoogleSettings(final Appointment newAppointment) {

        calendarService.validateCurrentUser(new AbstractAsyncCallback<Boolean>() {
            public void success(Boolean result) {
                newAppointment.setHasGoogleAccount(result);
                MenuBar menuBar = new MenuBar(true);
                if (isOffice.get(0)) {
                    if (isOffice.get(1) && isOffice.get(2)) {
                        calendarService.getTimeZones(new AbstractAsyncCallback<ArrayList<SelectItem>>() {
                            @Override
                            public void success(ArrayList<SelectItem> result) {
                                timeZones = result.toArray(new SelectItem[result.size()]);
                            }
                        });
                        calendarService.getSelectedTimeZone(new AbstractAsyncCallback<Integer>() {
                            @Override
                            public void failure(Throwable throwable) {
                                super.failure(throwable);
                            }

                            @Override
                            public void success(Integer timeZoneID) {
                                selectedTimeZoneID = timeZoneID;
                            }
                        });
                        MenuPopItem syncLink = new MenuPopItem(wfmStrings.synchronizeWithOffice(), null, () -> syncEventsView(true));
                        syncLink.getElement().setId("syncLink");
                        menuBar.addItem(syncLink);
                    } else {
                        if (!isOffice.get(1)) {
                            Info.show("Your token expired. Please configure it again", Info.Type.WARNING);
                        }
                        MenuPopItem authorizeOfficeLink = new MenuPopItem(wfmStrings.configureWithOffice365(), null, () -> new GoogleCalendarQuestionPopup(false));
                        authorizeOfficeLink.getElement().setId("authorizeOfficeLink");
                        menuBar.addItem(authorizeOfficeLink);
                    }

                } else if (result) {
                    MenuPopItem syncLink = new MenuPopItem(wfmStrings.configureWithOffice365(), null, () -> syncEventsView(false));
                    syncLink.getElement().setId("syncLink");
                    menuBar.addItem(syncLink);
                } else {
                    MenuPopItem authorizeOfficeLink = new MenuPopItem(wfmStrings.configureWithOffice365(), null, () -> new GoogleCalendarQuestionPopup(false));
                    authorizeOfficeLink.getElement().setId("authorizeOfficeLink");
                    menuBar.addItem(authorizeOfficeLink);

                    MenuPopItem authorizeLink = new MenuPopItem(wfmStrings.configureWithGoogle(), null, () -> new GoogleCalendarQuestionPopup(true));
                    authorizeLink.getElement().setId("authorizeLink");
                    menuBar.addItem(authorizeLink);
                }

                configureBoxItem.setMenu(menuBar);

                String message = Cookies.getCookie(CommandConstants.GOOGLE_CALENDAR_COOKIE);
                if (message != null) {
                    Info.show(message, Info.Type.WARNING);
                    Cookies.removeCookie(CommandConstants.GOOGLE_CALENDAR_COOKIE);
                }
            }
        });
    }

    private void onChangeEmployeeDropdown(Integer employeeID) {
        Cookies.setCookie(CommandConstants.GOOGLE_CALENDAR_OWNER_ID_COOKIE, employeeID.toString());
        if (idList != null) {
            idList.clear();
            idList.add(employeeID);
        }
        if (employeeID.equals(Utils.getUserID())) {//If selected employee is the same user.
            checkBoxesDecorator.setVisible(true);
            enableSelfAppointments(true);
            removeTempAppointments();
            selectedTab.refresh();
        } else {
            LoadingPanel.loading(true);
            UsersCalendarSettingsItem settingsItem = userSelfAppointments.getCalendarSettings();
            if (idList != null) {
                idList.clear();
                idList.add(employeeID);
            }
            getSelectedEmployeesCalendar(idList, settingsItem.isEventIsChecked(), settingsItem.isTaskIsChecked());
        }
    }


    private void getSelectedEmployeesCalendar(ArrayList<Integer> employeesIdList, Boolean isEventIsChecked, Boolean isTaskIsChecked) {
        idList = employeesIdList;
        if (idList != null && !idList.isEmpty()) {
            LoadingPanel.loading(true);
            calendarService.getCalendarTasksAndEvents(idList, DateUtil.getMonthFirstDay(new Date()), DateUtil.addMonths(new Date(), 12), isEventIsChecked, isTaskIsChecked, new AbstractAsyncCallback<ArrayList<Appointment>>() {
                public void success(ArrayList<Appointment> result) {
                    checkBoxesDecorator.setVisible(false);
                    enableSelfAppointments(false);
                    removeTempAppointments();
                    if (!(Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.DR) || Utils.hasRole(Constants.CALENDAR_EDITOR))) {
                        linksPanel.remove(addEventPanel);
                        linksPanel.remove(addTaskPanel);
                    }
                    addExtraAppointmentsToCalendar(tempAppointments = result);
                    if (Utils.hasGenericAccess(GenericSettingsEnum.CONSOLIDATED_CALENDAR_ENABLED)) {
                        refresh();
                    }
                    LoadingPanel.loading(false);
                }

                public void failure(Throwable throwable) {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }
            });
        }
    }

    private void enableSelfAppointments(boolean enable) {
        enableAppointments(userSelfAppointments.getEvents(), enable && events.isChecked());
        enableAppointments(userSelfAppointments.getCalls(), enable && calls.isChecked());
        enableAppointments(userSelfAppointments.getTasks(), enable && tasks.isChecked());
        if (userSelfAppointments.getProjects() != null) {
            enableAppointments(userSelfAppointments.getProjects(), enable && projects.isChecked());
        }
        if (userSelfAppointments.getIssues() != null) {
            enableAppointments(userSelfAppointments.getIssues(), enable && issues.isChecked());
        }
        if (userSelfAppointments.getLeaveRequests() != null) {
            enableAppointments(userSelfAppointments.getLeaveRequests(), enable && leaveRequests.isChecked());
        }
        if (userSelfAppointments.getHolidays() != null) {
            enableAppointments(userSelfAppointments.getHolidays(), enable && holidays.isChecked());
        }
        if (userSelfAppointments.getCourses() != null) {
            enableAppointments(userSelfAppointments.getCourses(), enable && courses.isChecked());
        }
    }

    private void enableAppointments(ArrayList<Appointment> appointments, boolean enable) {
        if (appointments != null && appointments.size() > 0) {
            for (Appointment appointment : appointments) {
                appointment.setVisible(enable);
            }
        }
    }

    private void removeTempAppointments() {
        day.removeAppointmentsWithoutRefresh(tempAppointments);
        week.removeAppointmentsWithoutRefresh(tempAppointments);
        month.removeAppointmentsWithoutRefresh(tempAppointments);
        agenda.removeAppointmentsWithoutRefresh(tempAppointments);
    }

    private void syncEventsView(final Boolean isOffice) {
        final KpiModal shell = new KpiModal();
        shell.setWidth(400);
        shell.setTitle(isOffice ? wfmStrings.synchronizationWithOffice() : wfmStrings.synchronization());

        final DatePicker start = new DatePicker(true);
        start.setEnabled(true);

        final DatePicker end = new DatePicker(true);
        end.setEnabled(true);

        final KpiCheckBox autoTransfer = new KpiCheckBox(" " + wfmStrings.transferAll());
        autoTransfer.setValue(false);
        autoTransfer.addValueChangeHandler(event -> {
            start.setEnabled(!event.getValue());
            end.setEnabled(!event.getValue());
        });

        final DataListBox timeZoneList = new DataListBox();
        timeZoneList.setItems(timeZones);
        if (selectedTimeZoneID != null) {
            timeZoneList.setSelected(selectedTimeZoneID);
        }
        timeZoneList.setWidth("150px");

        syncButton = new WfmButton2("Synchronize", new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (!autoTransfer.getValue() && (start.getDate() == null || end.getDate() == null)) {
                    start.setStyleName("x-form-invalid");
                    end.setStyleName("x-form-invalid");
                    return;
                }
                Date startDate, endDate;
                if (autoTransfer.getValue()) {
                    startDate = new Date();
                    startDate.setDate(1);
                    startDate.setHours(0);
                    startDate.setMinutes(0);
                    startDate.setSeconds(0);
                    endDate = DateUtil.addMonths(startDate, 3);
                } else {
                    startDate = start.getDate();
                    startDate.setHours(0);
                    startDate.setMinutes(0);
                    startDate.setSeconds(0);
                    endDate = end.getDate();
                    endDate = DateUtil.addDays(endDate, 1);
                    endDate.setHours(0);
                    endDate.setMinutes(0);
                    endDate.setSeconds(0);
                }

                if (!Validation.validateDateOrder(startDate, endDate, wfmStrings.enterCorrectDate(), true)) {
                    start.setStyleName(ERROR_FORM_STYLE);
                    end.setStyleName(ERROR_FORM_STYLE);
                } else if (isOffice && timeZoneList.getSelectedItem() == null) {
                    timeZoneList.setStyleName(ERROR_FORM_STYLE);
                } else {
                    if (isOffice && timeZoneList.getSelectedItem() != null) {
                        calendarService.saveOfficeCalendarTimeZone(timeZoneList.getSelectedId(), new AbstractAsyncCallback<Boolean>() {
                            @Override
                            public void success(Boolean result) {
                                super.success(result);
                            }
                        });
                    }
                    synchronize(startDate, endDate, shell, isOffice);
                }
            }
        });

        autoTransfer.getElement().getStyle().setPaddingLeft(30, Style.Unit.PX);

        HorizontalPanel funcPanel = new HorizontalPanel();
        funcPanel.setWidth("100%");
        if (isOffice) {
            funcPanel.add(timeZoneList);
        }
        funcPanel.add(syncButton);
        funcPanel.setCellHorizontalAlignment(timeZoneList, HasAlignment.ALIGN_CENTER);
        funcPanel.setCellHorizontalAlignment(syncButton, HasAlignment.ALIGN_RIGHT);

        HTML message = new HTML("Please select your Office365 Calendar timezone.");
        message.getElement().getStyle().setPaddingLeft(15, Style.Unit.PX);

        VerticalPanel syncPanel = new VerticalPanel();
        syncPanel.setWidth("100%");
        syncPanel.setHeight("100%");
        syncPanel.setSpacing(7);
        syncPanel.add(new Label((isOffice
                ? Property.getPluralWithObjectCodeWithReplace(Constants.EVENT_LIST, wfmStrings.choosePeriodToTransferEventsOffice(), wfmStrings.events())
                : Property.getPluralWithObjectCodeWithReplace(Constants.EVENT_LIST, wfmStrings.choosePeriodToTransferEvents(), wfmStrings.events())) + " " + Utils.getProductName() + " " + wfmStrings.choosePeriodToTransferEvents1(), true));
        syncPanel.add(initDatePickers(start, end, 0));
        syncPanel.add(autoTransfer);
        if (isOffice) {
            syncPanel.add(message);
        }
        syncPanel.add(funcPanel);

        shell.add(syncPanel);
        shell.open();
    }

    private void synchronize(Date start, Date end, final KpiModal shell, Boolean isOffice) {
        syncButton.setEnabled(false);
        LoadingPanel.loading(true);
        if (isOffice) {
            calendarService.syncEvents(null, null, start, end, new AbstractAsyncCallback<ArrayList<Appointment>>() {
                public void success(ArrayList<Appointment> appointments) {
                    syncButton.setEnabled(true);
                    addExtraAppointmentsToCalendar(appointments);
                    shell.close();
                    refresh();

                    final WfmMessageBox message = new WfmMessageBox(IconEnum.CONFIRM, Action.OK, true);
                    message.setMessage(wfmStrings.youHaveSuccessfullySynchronisedOffice() + " " + Utils.getProductName() + " " +
                            wfmStrings.youHaveSuccessfullySynchronisedYourEventsBetweenGoogleAndWorkforcetrack1());
                    message.addCloseHandler(popupPanelCloseEvent -> selectedType = daysTabBar.getSelectedTab());
                    message.open();
                }

            });
        } else {
            calendarService.synchronizeEvents(null, start, end, new AbstractAsyncCallback<ArrayList<Appointment>>() {
                public void success(ArrayList<Appointment> appointments) {
                    syncButton.setEnabled(true);
                    addExtraAppointmentsToCalendar(appointments);
                    shell.close();
                    refresh();

                    final WfmMessageBox message = new WfmMessageBox(IconEnum.CONFIRM, Action.OK, true);
                    message.setMessage(Property.getPluralWithObjectCodeWithReplace(Constants.EVENT_LIST, wfmStrings.youHaveSuccessfullySynchronisedYourEventsBetweenGoogleAndWorkforcetrack(), wfmStrings.events()) + " " + Utils.getProductName() + " " +
                            wfmStrings.youHaveSuccessfullySynchronisedYourEventsBetweenGoogleAndWorkforcetrack1());
                    message.addCloseHandler(popupPanelCloseEvent -> selectedType = daysTabBar.getSelectedTab());
                    message.open();
                }
            });
        }
    }

    private void addExtraAppointmentsToCalendar(ArrayList<Appointment> appointments) {
        addAppointmentsToCalendar(appointments);

        events.addAppointments(appointments);
        calls.addAppointments(appointments);
        tasks.addAppointments(appointments);
        selectedTab.refresh();

        LoadingPanel.loading(false);
    }

    public String getIconStyle() {
        return null;
    }


    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                idList.clear();
                idList.add(Utils.getUserID());
                startDate = DateUtil.getMonthFirstDay(new Date());
                Cookies.setCookie(CommandConstants.GOOGLE_CALENDAR_OWNER_ID_COOKIE, Utils.getUserID().toString());
                callback.onSuccess(onInitialize());
            }
        });
    }

    private UsersCalendarSettingsItem getCalendarSettings() {
        UsersCalendarSettingsItem settingsItem = new UsersCalendarSettingsItem();
        settingsItem.setEventIsChecked(events.isChecked());
        settingsItem.setCallIsChecked(calls.isChecked());
        settingsItem.setProjectIsChecked(projects.isChecked());
        settingsItem.setTaskIsChecked(tasks.isChecked());
        settingsItem.setIssueIsChecked(issues.isChecked());
        settingsItem.setLeaveRequestIsChecked(leaveRequests.isChecked());
        settingsItem.setHolidayIsChecked(holidays.isChecked());
        settingsItem.setCourseIsChecked(courses.isChecked());
        settingsItem.setDefaultView(daysTabBar.getSelectedTab());
        return settingsItem;
    }

    class AppointmentCheckBox extends Composite {

        private ArrayList<Appointment> appointments;

        private final MaterialSwitch checkbox;

        public AppointmentCheckBox() {
            checkbox = new MaterialSwitch();
            checkbox.addValueChangeHandler(event -> {
                setEnabledToCheckBoxes(false);
                calendarService.saveCalendarSettings(getCalendarSettings(), new AbstractAsyncCallback<UsersCalendarSettingsItem>() {
                    public void failure(Throwable caught) {
                        setEnabledToCheckBoxes(true);
                    }

                    public void success(UsersCalendarSettingsItem result) {
                        if (validateValue(eventsIsLoaded, result.isEventIsChecked())) {
                            refresh();
                        } else if (validateValue(callsIsLoaded, result.isCallIsChecked())) {
                            refresh();
                        } else if (validateValue(tasksIsLoaded, result.isTaskIsChecked())) {
                            refresh();
                        } else if (validateValue(projectsIsLoaded, result.isProjectIsChecked())) {
                            refresh();
                        } else if (validateValue(issuesIsLoaded, result.isIssueIsChecked())) {
                            refresh();
                        } else if (validateValue(paIsLoaded, result.isPaIsChecked())) {
                            refresh();
                        } else if (validateValue(leavesIsLoaded, result.isLeaveRequestIsChecked())) {
                            refresh();
                        } else if (validateValue(holidaysIsLoaded, result.isHolidayIsChecked())) {
                            refresh();
                        } else if (validateValue(coursesIsLoaded, result.isCourseIsChecked())) {
                            refresh();
                        }
                        if (userSelfAppointments != null) {
                            userSelfAppointments.setCalendarSettings(result);
                        }
                        setEnabledToCheckBoxes(true);

                        boolean value = event.getValue();
                        enableAppointments(appointments, value);
                        selectedTab.refresh();
                    }
                });
            });

            appointments = new ArrayList<>();

            initWidget(checkbox);
        }

        public void setEnabled(boolean enabled) {
            checkbox.setEnabled(enabled);
        }

        private boolean validateValue(boolean value1, boolean value2) {
            return !value1 && value2;
        }

        public boolean isChecked() {
            return checkbox.getValue();
        }

        public void setChecked(boolean checked) {
            checkbox.setValue(checked);
        }

        public void setAppointments(ArrayList<Appointment> appointments) {
            this.appointments = appointments;
        }

        public void addAppointments(ArrayList<Appointment> appointments) {
            if (appointments != null && appointments.size() > 0) {
                for (Appointment appointment : appointments) {
                    addAppointment(appointment);
                }
            }
        }

        public void addAppointment(Appointment appointment) {
            if (appointments != null && appointment != null) {
                appointments.add(appointment);
            }
        }
    }

    private void setEnabledToCheckBoxes(boolean isEnabled) {
        events.setEnabled(isEnabled);
        calls.setEnabled(isEnabled);
        projects.setEnabled(isEnabled);
        tasks.setEnabled(isEnabled);
        issues.setEnabled(isEnabled);
        leaveRequests.setEnabled(isEnabled);
        holidays.setEnabled(isEnabled);
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        DeferredCommand.addCommand(() -> {
            if (daysTabBar != null && daysTabBar.getSelectedTab() == -1) {
                daysTabBar.selectTab(selectedType);
            }
        });
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        MainLayout.get().considerBodyHasOperPanel(true);
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        MainLayout.get().considerBodyHasOperPanel(false);
    }

    public void setIdList(ArrayList<Integer> employeeIds) {
        this.idList = employeeIds;
        refresh();
    }
}
