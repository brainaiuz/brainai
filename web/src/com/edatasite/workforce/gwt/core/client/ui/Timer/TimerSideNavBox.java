package com.edatasite.workforce.gwt.core.client.ui.Timer;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AttendanceStats;
import com.edatasite.workforce.gwt.core.client.rpc.ClockItem;
import com.edatasite.workforce.gwt.core.client.rpc.ClockWidgetService;
import com.edatasite.workforce.gwt.core.client.rpc.ClockWidgetServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextArea;
import gwt.material.design.client.ui.html.DD;
import gwt.material.design.client.ui.html.DL;
import gwt.material.design.client.ui.html.DT;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Date;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.CANCELLED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.CRM_CASE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.ISAUTOMATICAPPROVAL;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.MAXIMUM_HOURS;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.ON_HOLD;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.PM_ISSUE_TIMER;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.PM_TASK;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SAVE_TIMER_INTO_TIMESHEET_AUTOMATICALLY;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.TIMESHEET_COMMENT_REQUIRED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.VALIDATE_DAY_OFF;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.VALIDATE_HOLIDAY;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.VALIDATE_MAXIMUM_HOURS;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.VALIDATE_TASK_END;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.VALIDATE_TASK_START;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.VALIDATE_TIMESLOT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.VALIDATE_lEAVE_REQUEST;
import static com.edatasite.workforce.gwt.core.client.ui.Constants._CLOSED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants._COMPLETED;

/**
 * Created by Anvar Akramov on 5/28/18.
 */
public class TimerSideNavBox extends KpiSideNavBox {


    public void clearForm() {
        clearLookUps(projects);
        clearLookUps(tasks);
        clearLookUps(cases);
        comments.setText("");
    }

    interface TimerSideNavBoxUiBinder extends UiBinder<Widget, TimerSideNavBox> {
    }

    private static final TimerSideNavBoxUiBinder ourUiBinder = GWT.create(TimerSideNavBoxUiBinder.class);

    private final ClockWidgetServiceAsync clockWidgetService = ClockWidgetService.App.get();

    private static final DateTimeFormat timerFormat = DateTimeFormat.getFormat("HH:mm:ss");

    @UiField
    HTMLPanel htmlPanel;
    @UiField
    MaterialPanel timerContainer;
    @UiField
    MaterialPanel timerFooter;
    @UiField
    MaterialLabel hoursLabel;
    @UiField
    DT projectName;
    @UiField
    DD taskName;
    @UiField
    DD caseName;
    @UiField
    MaterialButton logTimeBtn;
    @UiField
    MaterialLink resetBtn;
    @UiField
    MaterialButton startStopBtn;
    @UiField
    MaterialTextArea comments;
    @UiField
    DL timeEntries;
    @UiField
    Heading timeEntriesTitle;
    @UiField
    Div timeEntriesPanel;

    private CRMLookUp projects;
    private CRMLookUp cases;
    private CRMLookUp tasks;

    private Integer objectId;
    private Integer entityType = null;//PM_TASK;
    private Date timerDate = new Date();
    private Integer projectID;
    private ClockItem item;
    private com.google.gwt.user.client.Timer timer;
    private boolean timerIsStarted = false;
    private boolean pageIsLoaded = false;
    private final boolean anotherTimerIsRunning = true;
    private boolean haveTimer = false;
    private long diff = 0;
    MaterialPanel dailyTotal = new MaterialPanel("timer__counter");

    public TimerSideNavBox() {
        this(null, null, PM_TASK);
    }

    public TimerSideNavBox(Integer projectID, Integer busObjectId, Integer entityType) {
        super(true, KpiSideNavBox.DEFAULT_WIDTH);
        setStyleName(getElement(), "quick-add timers", true);
        ourUiBinder.createAndBindUi(this);
        this.objectId = busObjectId;
        this.entityType = entityType;
        this.projectID = projectID;
        initialize();
    }

    public void setBusObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public void setEntityType(Integer entityType) {
        this.entityType = entityType;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    private void initialize() {
        Heading header = new Heading(HeadingSize.H1);

        resetBtn.setTooltip(wfmStrings.reset() + " " + wfmStrings.timer());

        logTimeBtn.addClickHandler(event -> applyTime());
        startStopBtn.addClickHandler(event -> startTimer(false));
        resetBtn.addClickHandler(event -> reset());

        timeEntriesTitle.setText(wfmStrings.todaysTimeEntry());
        comments.setPlaceholder(wfmStrings.addNote());
        newTimer();

        Span headerText = new Span();
        headerText.setText(wfmStrings.timer());

        header.add(headerText);

        addHeader(header);

        addBody(htmlPanel);

        addOpeningHandler(event -> {
            loadActiveTimer();
            loadLoggedEntries();
        });

        addClosingHandler(event -> {
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TIMER_STARTED, null, this);
        });


        MaterialPanel timerTotal = new MaterialPanel("timer timer--total");
        MaterialPanel timerHeading = new MaterialPanel("timer__heading");
        MaterialPanel timerHeadingTitle = new MaterialPanel("timer__title");
        timerHeadingTitle.getElement().setInnerText(wfmStrings.dailyTotal());
        dailyTotal.getElement().setInnerText("00:00");
        timerHeading.add(timerHeadingTitle);
        timerHeading.add(dailyTotal);
        timerTotal.add(timerHeading);

        addFooter(timerTotal);
    }

    private void loadLoggedEntries() {

        clockWidgetService.getCurrentUserEntriesByDay(new DateNonConvertable(), new AsyncCallback<ArrayList<ClockItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                GWT.log(throwable.getMessage());
            }

            @Override
            public void onSuccess(ArrayList<ClockItem> timesheetDataItems) {
                if (timesheetDataItems != null) {

                    timeEntries.clear();
                    Integer todaysTotal = 0;

                    for (ClockItem item : timesheetDataItems) {

                        if (item.getActualTime() != null && item.getActualTime() > 0) {
                            todaysTotal += item.getActualTime();

                            timeEntries.add(new DT(getTimeSpentHM(item.getActualTime())));

                            if (item.getTaskName() != null && !item.getTaskName().equals("")) {
                                timeEntries.add(new DD(item.getTaskName()));
                            }
                            if (item.getComment() != null && !item.getComment().equals("")) {
                                timeEntries.add(new DD(item.getComment()));
                            }
                        }
                    }
                    if (timesheetDataItems.size() > 0) {
                        timeEntriesPanel.setVisible(true);
                        timeEntriesTitle.setVisible(true);
                    } else {
                        timeEntriesPanel.setVisible(false);
                        timeEntriesTitle.setVisible(false);
                    }
                    setTotalDailyTime(getTimeSpentHM(todaysTotal));
                }
            }
        });

    }

    public void setTotalDailyTime(String time) {
        dailyTotal.getElement().setInnerText(time);//ex. "00:00"
    }

    ClockItem clockItem;

    private void loadActiveTimer() {
        GWT.log("objectID " + objectId + " entityType " + entityType);
        clockWidgetService.getHistoryClockItem(new AbstractAsyncCallback<ClockItem>() {
            @Override
            public void success(ClockItem result) {
                if (result != null) {
                    clockItem = result;
                    entityType = (entityType != null ? entityType : (result.getRelation() != null ? result.getRelation() : PM_TASK));//PM_TASK;
                    objectId = entityType.equals(result.getRelation()) ? result.getBusObjectId() : objectId;
                    haveTimer = true;
                } else {
                    clockItem = null;
                    if (entityType == null) {
                        entityType = PM_TASK;
                    }
                    haveTimer = false;
                }
                onInitialize();
            }
        });
    }

    protected static Integer parseSecond(String minutes) throws NumberFormatException, StringIndexOutOfBoundsException {
        if (minutes == null || minutes.equals("")) {
            return 0;
        }
        String[] parts = minutes.split(":");
        int h = 0;
        int m = 0;
        int s = 0;
        h = Integer.parseInt(parts[0]);
        m = Integer.parseInt(parts[1]);
        s = Integer.parseInt(parts[2]);

        return h * 3600 + m * 60 + s;
    }

    private void showHideLookups(Integer entityType) {
        if(!"".equalsIgnoreCase(projects.getSuggestBox().getText().trim()) && (projects.isSelected() || entityType!=CRM_CASE) ) {
            taskName.getElement().getStyle().setDisplay(Style.Display.BLOCK);
            caseName.getElement().getStyle().setDisplay(Style.Display.NONE);
        } else {
            taskName.getElement().getStyle().setDisplay(Style.Display.NONE);
            caseName.getElement().getStyle().setDisplay(Style.Display.BLOCK);
        }
    }
    private void showHideLookups() {
        showHideLookups(entityType);
    }

    private void clearLookUps(LookUp lookUp) {
        if (lookUp != null) {
            lookUp.clearAndClearItems();
            lookUp.refreshOracle(true);
        }
        objectId = null;
    }

    private void setAllData(final Integer busObjID) {

        Integer entityType = clockItem != null ? clockItem.getRelation() : (this.entityType != null ? this.entityType:PM_TASK);
        Integer objectID = (busObjID != null) ? busObjID : (clockItem != null ? clockItem.getBusObjectId() : null);
        showHideLookups(entityType);
        GWT.log("objectID " + objectID + " entityType " + entityType);
        ClockWidgetService.App.get().getClockItem(objectID, entityType, new DateNonConvertable(), new AbstractAsyncCallback<ClockItem>() {
            @Override
            public void failure(Throwable caught) {
                item = new ClockItem();
            }

            @Override
            public void success(ClockItem result) {
                if (entityType != CRM_CASE) {
                    //reset timer label
                    timerDate.setHours(0);
                    timerDate.setMinutes(0);
                    timerDate.setSeconds(0);
                    hoursLabel.setText(timerFormat.format(timerDate));

                }
                //hide reset/logtime/comments buttons
                resetBtn.setVisible(false);
                logTimeBtn.setVisible(false);
                comments.setText("");
                if (tasks != null && objectID != null) {
                    tasks.setEnabled(false);
                    projects.setEnabled(false);
                }

                if (result != null) {
                    item = result;
                    //set timer time from server
                    timerDate.setSeconds(result.getElapsedTime() != null ? result.getElapsedTime() : 0);
                    diff = new Date().getTime() - timerDate.getTime();
                    hoursLabel.setText(timerFormat.format(timerDate));

                    if (!"".equalsIgnoreCase(projects.getSuggestBox().getText().trim()) && (projects.getSelectedItemID()!=null && projects.getSelectedItemID()>0 || entityType != CRM_CASE) ) {
                        pageIsLoaded = true;

                        if (result.getProjectID() != null && !projects.isSelected()) {
                            projects.setSelected(new SelectItem(result.getProjectID(), result.getProjectName()));
                        }
                        GWT.log(objectID + " " + result.getTaskName());
                        if (objectID != null) {
                            tasks.setSelected(objectID, entityType.equals(PM_TASK) ? result.getTaskName() : result.getIssueName());
                            startStopBtn.setVisible(true);
                        }
                        /*if (result.getEstimateTime() != null) {
                            estHours.setText(Utils.formatMinutes(result.getEstimateTime()));
                        }
                        if (result.getActualTime() != null) {
                            actHours.setText(Utils.formatMinutes(result.getActualTime()));
                        }*/
                        /*percent.setText(result.getPercent() != null ? result.getPercent().toString() : "");*/
                    } else {
                        /*if (result.getBusObjectId() != null) {
                            cases.setSelected(new SelectItem(result.getBusObjectId(), result.getCaseName()));
                        } else {
                            cases.setSelected(objectID, result.getCaseName());
                        }
                        startStopBtn.setVisible(true);*/
                        if (objectID != null) {
                            cases.setSelected(objectID, result.getCaseName());
                            startStopBtn.setVisible(true);
                        }
                    }
                    if (result.getElapsedTime() != null && result.getElapsedTime() > 0) {
                        comments.setText(result.getComment());
                    }
                    GWT.log("timerIsStarted " + timerIsStarted);
                    if (result.isStarted() && haveTimer) {
                        timerIsStarted = true;
                        timer.scheduleRepeating(1000);
                        if (startStopBtn.getText().equalsIgnoreCase(wfmStrings.pause())) {
                            startStopBtn.addStyleName("btn--darkgrey");
                            startStopBtn.removeStyleName("btn--success");
                        }
                    } else if (haveTimer && result.isHaveStoppedTime() && !result.isStarted()) {
                        if (timerIsStarted) {
                            timer.cancel();
                        }
                        startStopBtn.setText(wfmStrings.start());
                        startStopBtn.removeStyleName("btn--darkgrey");
                        startStopBtn.addStyleName("btn--success");
                        comments.setText(result.getComment());

                        if (result.getElapsedTime() != null && result.getElapsedTime() > 0 && result.isHaveStoppedTime()) {
                            resetBtn.setVisible(true);
                            logTimeBtn.setVisible(true);
                        } else {
                            if (tasks != null && projects != null) {
                                tasks.setEnabled(true);
                                projects.setEnabled(true);
                            }
                        }
                    } else {
                        if (tasks != null && projects != null) {
                            tasks.setEnabled(true);
                            projects.setEnabled(true);
                        }
                    }
                }
            }
        });
    }

    private void newTimer() {
        timer = new com.google.gwt.user.client.Timer() {
            @Override
            public void run() {
                timerDate = new Date(new Date().getTime() - diff);
                hoursLabel.setText(timerFormat.format(timerDate));
            }
        };
    }

    public void startTimer(boolean overrideAnotherTimerInstance) {
        if (item.isApprovedForToday() && "false".equals(Utils.userSettings.get(ISAUTOMATICAPPROVAL))) {
            Info.show(wfmStrings.cannotStartApproved(), Info.Type.INFO);
            return;
        } else if (item.isSentToApproveForToday() && "false".equals(Utils.userSettings.get(ISAUTOMATICAPPROVAL))) {
            Info.show(wfmStrings.cannotStartSentForApproval(), Info.Type.INFO);
            return;
        }

        if (tasks != null && projects != null) {
            tasks.setEnabled(false);
            projects.setEnabled(false);
        }

        if (timerIsStarted) {

            timer.cancel();
            resetBtn.setVisible(true);
            logTimeBtn.setVisible(true);

            startStopBtn.setText(wfmStrings.start());
            startStopBtn.removeStyleName("btn--darkgrey");
            startStopBtn.addStyleName("btn--success");

            item.setReset(false);
            item.setTodaysTime(parseSecond(hoursLabel.getText()));

            ClockWidgetService.App.get().stopTimer(item, new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable throwable) {
                }

                @Override
                public void success(Void aVoid) {
                    timerIsStarted = false;
                    if (entityType == PM_TASK) {
                        if ("true".equals(Utils.userSettings.get(SAVE_TIMER_INTO_TIMESHEET_AUTOMATICALLY))) {
                            applyTime();
                        }
                    }
                }
            });

        } else {
            if (!validate()) {
                return;
            }
            diff = new Date().getTime() - timerDate.getTime();

            if (item == null) {
                item = new ClockItem();
            }

            item.setBusObjectId(objectId);
            if (objectId == null && entityType.equals(PM_TASK)) {
                item.setTaskName(taskName.getText());
                item.setProjectID(projectID);
                item.setStartDate(new Date());
                item.setEndDate(DateUtil.addDays(new Date(), 7));
                LoadingPanel.loading(true);
            }
            if (!"".equalsIgnoreCase(projects.getSuggestBox().getText().trim()) && projects.getSelectedItemID()!=null && projects.getSelectedItemID()>0 ) {
                item.setRelation(PM_TASK);
                item.setTodaysTime(parseSecond(hoursLabel.getText()));
            } else {
                item.setRelation(CRM_CASE);
            }
            item.setComment(comments.getText());
//            item.setEstimateTime(estHours != null ? Utils.parseMinutes(estHours.getText()) : null);
            item.setOverrideAnotherTimerInstance(overrideAnotherTimerInstance);
            if (overrideAnotherTimerInstance) {
                item.setTodaysTime(parseSecond(hoursLabel.getText()));
            }
            ClockWidgetService.App.get().startTimer(item, new AbstractAsyncCallback<Integer>() {
                @Override
                public void failure(Throwable caught) {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    timerIsStarted = false;
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(final Integer result) {

                    //Start clock
                    timer.scheduleRepeating(1000);

                    Scheduler.get().scheduleDeferred(() -> {
                        LoadingPanel.loading(false);
                        timerIsStarted = true;

                        startStopBtn.setText(wfmStrings.pause());
                        startStopBtn.removeStyleName("btn--success");
                        startStopBtn.addStyleName("btn--darkgrey");

                        logTimeBtn.setVisible(false);
                        resetBtn.setVisible(false);

                        if (entityType == PM_TASK) {
                            if (objectId == null) {
                                objectId = result;
                                setAllData(objectId);
                            }
                        }
                        Info.show(wfmStrings.timerSuccStarted(), Info.Type.INFO);
                    });
                }
            });
        }
    }

    private void reset() {
        item.setReset(true);
        ClockWidgetService.App.get().stopTimer(item, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void success(Void result) {
                timer.cancel();

                startStopBtn.setText(wfmStrings.start());
                startStopBtn.removeStyleName("btn--darkgrey");
                startStopBtn.addStyleName("btn--success");

                resetHoursLabel();
                clearLookUps(projects);
                clearLookUps(tasks);

                comments.setText("");
                item.setComment("");

                if (tasks != null && projects != null) {
                    tasks.setEnabled(true);
                    projects.setEnabled(true);
                }

                Info.show(wfmStrings.lastWorkingTimeCleared(), Info.Type.INFO);

            }
        });
    }

    private void resetHoursLabel() {
        timerDate.setHours(0);
        timerDate.setMinutes(0);
        timerDate.setSeconds(0);
        hoursLabel.setText(timerFormat.format(timerDate));
        resetBtn.setVisible(false);
    }

    public void onInitialize() {
        projects = new CRMLookUp(LookUpConstants.PROJECT);
        projects.setDefaultText(Property.get(Constants.PROJECT, wfmStrings.selectProject(), wfmStrings.project()));
        projects.setFullSearch(true);
        projects.getSuggestBox().addSelectionHandler(selectionEvent -> clearLookUps(tasks));
        projects.setBeforeSearch(() -> {
            projects.getFilterParametrs().setEmployeeId(Utils.getUserID());
            projects.getFilterParametrs().setIDsOnly(true);
        });
        projects.getSuggestBox().addSelectionHandler(valueChangeEvent -> {
            showHideLookups();
        });
        projects.getSuggestBox().addBlurHandler(blurEvent -> {
            showHideLookups();
        });

        tasks = new CRMLookUp(LookUpConstants.TASK);
        tasks.getFilterParametrs().setAssignedItems(true);
        tasks.setType(LookUpConstants.PM_TASK_ID);
        tasks.getFilterParametrs().setExcludedType(_COMPLETED + " " + CANCELLED + " " + ON_HOLD + " " + _CLOSED);
        tasks.setDefaultText(wfmStrings.pleaseSelectTask());
        if (Utils.hasPermission(PermissionConstants.PM_TASKS_ADD) && entityType.equals(PM_TASK)) {
            tasks.setValueNotEmptyMeansSelected(true);
        }
        tasks.getSuggestBox().addSelectionHandler(selectionEvent -> {
            if (tasks.isSelected()) {
                objectId = tasks.getSelectedItem().getId();
                setAllData(tasks.getSelectedItem().getId());
                startStopBtn.setVisible(true);
            } else {
                startStopBtn.setVisible(false);
            }
        });

        tasks.getSuggestBox().addValueChangeHandler(valueChangeEvent -> {
            if (entityType.equals(PM_TASK)) {
                objectId = null;
            }
            startStopBtn.setVisible(tasks.isSelected());
        });

        tasks.setBeforeSearch(() -> {
            tasks.setProjectID(projects.getSelectedItemID());
//                tasks.setType(taskRadioButton.getValue() ? LookUpConstants.PM_TASK_ID : LookUpConstants.PM_ISSUE_ID);
        });
//-------------------------
        cases = new CRMLookUp(LookUpConstants.CRM_CASE);
        cases.setFullSearch(true);
//            cases.setWidth(MAX_DEFAULT_WIDTH);
        cases.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (cases.getSelectedItemID() != null) {
                setAllData(cases.getSelectedItemID());
                objectId = cases.getSelectedItemID();
                startStopBtn.setVisible(true);
            } else {
                startStopBtn.setVisible(false);
            }
        });


        /*if (!entityType.equals(CRM_CASE)) {
            estHours = new TextBox();
            estHours.setWidth(MIN_DEFAULT_WIDTH);
            estHours.setEnabled(false);
            actHours = new TextBox();
            actHours.setEnabled(false);
            actHours.setWidth(MIN_DEFAULT_WIDTH);
            percent = new TextBox();
            percent.setWidth(MIN_DEFAULT_WIDTH);
            Validation.addNumericKeyboardListener(percent);
        }
        comments = new TextArea2(1000);
        comments.setHeight("75px");
        comments.setWidth("150px");
        comments.setWidth(MIN_DEFAULT_WIDTH);*/


        logTimeBtn.setText(wfmStrings.logTime()/*entityType != CRM_CASE ? wfmStrings.logToTimesheet() : "Log time"*/);
        logTimeBtn.ensureDebugId("logToTimesheet");


        startStopBtn.setText(haveTimer ? wfmStrings.pause() : wfmStrings.start());
        startStopBtn.ensureDebugId("start");

        resetBtn.setVisible(false);
        resetBtn.ensureDebugId("reset");


        /*startBtn.setVisible(true);
        resetBtn.setVisible(true);*/
        if (haveTimer) {
//            startStopBtn.setVisible(false);
            resetBtn.setVisible(false);
            startStopBtn.setEnabled(true);
        }
        timerDate.setHours(0);
        timerDate.setMinutes(0);
        timerDate.setSeconds(0);


        projectName.clear();
        taskName.clear();
        projectName.add(projects);
        taskName.add(tasks);

        caseName.clear();
        caseName.add(cases);

        showHideLookups();

        /*if (entityType != CRM_CASE) {
            form.addField(wfmStrings.estimatedTime(), estHours);
            form.addField(wfmStrings.totalTime(), actHours);
            form.addField(wfmStrings.completed() + "(%)", percent);
        }*/
        setAllData(this.objectId);

    }

    public String getTimeSpentHM(int timeSpentInMinutes) {
        String timeSpentHM;
        if (timeSpentInMinutes == 0) {
            return timeSpentHM = "00:00";
        }
        timeSpentHM = "";
        if (timeSpentInMinutes / 60 < 10) {
            timeSpentHM = "0";
        }
        timeSpentHM = timeSpentHM + timeSpentInMinutes / 60;
        timeSpentHM = timeSpentHM + ":";
        if (timeSpentInMinutes % 60 < 10) {
            timeSpentHM = timeSpentHM + "0";
        }
        timeSpentHM = timeSpentHM + timeSpentInMinutes % 60;
        return timeSpentHM;
    }

    public void applyTime() {
        if (validate()) {
            if (entityType != CRM_CASE) {
                if (item.isApprovedForToday() && "false".equals(Utils.userSettings.get(ISAUTOMATICAPPROVAL))) {
                    Info.show(wfmStrings.cannotLogApproved(), Info.Type.INFO);
                    return;
                } else if (item.isSentToApproveForToday() && "false".equals(Utils.userSettings.get(ISAUTOMATICAPPROVAL))) {
                    Info.show(wfmStrings.cannotLogSentForApproval(), Info.Type.INFO);
                    return;
                }
                if (item.getTaskMap() != null && item.getTaskMap().get(tasks.getSelectedItem().getId()) != null && !validateAgainstTimesheetSettimgs(item.getTaskMap().get(tasks.getSelectedItem().getId()))) {
                    return;
                }
            }
            save();
        }
    }

    protected boolean validateAgainstTimesheetSettimgs(AttendanceStats attendanceStats) {
        Date clientToday = new Date();
        DateTimeFormat format = DateTimeFormat.getFormat(Utils.getShortDateFormat());
        Integer lastMinutes = (int) Math.ceil((double) parseSecond(hoursLabel.getText()) / 60);

        if ("true".equals(Utils.userSettings.get(VALIDATE_TASK_START))) {
            if ((clientToday.getYear() == attendanceStats.getTaskStart().getYear() && clientToday.getMonth() == attendanceStats.getTaskStart().getMonth()
                    && clientToday.getDate() < attendanceStats.getTaskStart().getDate()) ||
                    (clientToday.getYear() == attendanceStats.getTaskStart().getYear() && clientToday.getMonth() < attendanceStats.getTaskStart().getMonth()) ||
                    clientToday.getYear() < attendanceStats.getTaskStart().getYear()) {
                Info.show(wfmStrings.taskStartDateValidationMessage() + format.format(attendanceStats.getTaskStart()), Info.Type.WARNING);
                return false;
            }
        }
        if ("true".equals(Utils.userSettings.get(VALIDATE_TASK_END))) {
            if ((clientToday.getYear() == attendanceStats.getTaskEnd().getYear() && clientToday.getMonth() == attendanceStats.getTaskEnd().getMonth() && clientToday.getDate() > attendanceStats.getTaskEnd().getDate()) ||
                    (clientToday.getYear() == attendanceStats.getTaskEnd().getYear() && clientToday.getMonth() > attendanceStats.getTaskEnd().getMonth()) ||
                    clientToday.getYear() > attendanceStats.getTaskEnd().getYear()) {
                Info.show(wfmStrings.taskEndDateValidationMessage() + format.format(attendanceStats.getTaskEnd()), Info.Type.WARNING);
                return false;
            }
        }
        if ("true".equals(Utils.userSettings.get(VALIDATE_HOLIDAY)) && attendanceStats.isHoliday()) {
            Info.show(wfmStrings.holidayValidationMessage(), Info.Type.WARNING);
            return false;
        }
        //check for the weekend, weekend is when the timeslot equals to ZERO
        if ("true".equals(Utils.userSettings.get(VALIDATE_DAY_OFF)) && attendanceStats.isDayOff()) {
            Info.show(wfmStrings.dayOffValidationMessage(), Info.Type.WARNING);
            return false;
        }
        if ("true".equals(Utils.userSettings.get(VALIDATE_lEAVE_REQUEST)) && (attendanceStats.getLeaveMinutes() > 0)) {
            if (attendanceStats.getTimeslotMinutes() - attendanceStats.getLeaveMinutes() == 0) {
                Info.show(wfmStrings.dailyLeaveRequestValidationMessage(), Info.Type.WARNING);
                return false;
            }
            int maxMinutesAllowed = 0;
            if ("true".equals(Utils.userSettings.get(VALIDATE_MAXIMUM_HOURS)) && "false".equals(Utils.userSettings.get(VALIDATE_TIMESLOT))) {
                if (Utils.userSettings.get(MAXIMUM_HOURS) != null && !Utils.userSettings.get(MAXIMUM_HOURS).equals("")) {
                    maxMinutesAllowed = Integer.valueOf(Utils.userSettings.get(MAXIMUM_HOURS)) * 60;
                }
            } else {
                maxMinutesAllowed = attendanceStats.getTimeslotMinutes();
            }

            if (maxMinutesAllowed - attendanceStats.getLeaveMinutes() - attendanceStats.getTimesheetMinutes() < lastMinutes) {
                Info.show(wfmStrings.hourlyLeaveRequestValidationMessage() + Utils.formatMinutes(maxMinutesAllowed - attendanceStats.getLeaveMinutes()) + " " + wfmStrings.hours(), Info.Type.WARNING);
                return false;
            }
        }
        if ("true".equals(Utils.userSettings.get(VALIDATE_MAXIMUM_HOURS))) {
            if ("true".equals(Utils.userSettings.get(VALIDATE_TIMESLOT))) {
                if (attendanceStats.getTimeslotMinutes() - attendanceStats.getTimesheetMinutes() < lastMinutes) {
                    Info.show(wfmStrings.timeslotValidationMessage() + Utils.formatMinutes(attendanceStats.getTimeslotMinutes()) + " " + wfmStrings.hours(), Info.Type.WARNING);
                    return false;
                }
            } else {
                int maxHoursAllowed = 0;
                if (Utils.userSettings.get(MAXIMUM_HOURS) != null && !Utils.userSettings.get(MAXIMUM_HOURS).equals("")) {
                    maxHoursAllowed = Integer.valueOf(Utils.userSettings.get(MAXIMUM_HOURS));
                }
                if ((maxHoursAllowed * 60) - attendanceStats.getTimesheetMinutes() < lastMinutes) {
                    Info.show(wfmStrings.timeslotValidationMessage() + maxHoursAllowed + " " + wfmStrings.hours(), Info.Type.WARNING);
                    return false;
                }
            }
        }

        if ("true".equals(Utils.userSettings.get(TIMESHEET_COMMENT_REQUIRED))) {
            if (comments.getText() == null || "".equals(comments.getText())) {
                Info.show(wfmStrings.pleaseFillInCommentBox(), Info.Type.WARNING);
                return false;
            }
        }
        return true;
    }

    public boolean validate() {
        int errors = 0;
        boolean isTimeFormatError = false;
        if (!"".equalsIgnoreCase(projects.getSuggestBox().getText().trim()) && (projects.getSelectedItemID()!=null && projects.getSelectedItemID()>0 || entityType != CRM_CASE) ) {
            if (entityType == PM_ISSUE_TIMER) {
                if (!Validation.validateListBoxRequired(tasks, null, wfmStrings.pleaseSelectTask())) {
                    errors++;
                }
            }
            if ("".equals(tasks.getText()) || LookUp.wfmStrings.searchTypeMessage().equals(tasks.getText())) {
                if (!Validation.validateListBoxRequired(tasks, null, wfmStrings.pleaseSelectTask())) {
                    errors++;
                }
            }
            if (!Validation.validateListBoxRequired(projects, null, wfmStrings.pleaseSelectProject())) {
                errors++;
            }
        } else {
            if (cases != null && !Validation.validateListBoxRequired(cases, null, wfmStrings.pleaseSelectProject())) {
                errors++;
            }
        }
        if (errors > 0) {
            if (isTimeFormatError && errors == 1) {
                Info.show(wfmStrings.enterTimeSupportedFormats(), Info.Type.WARNING);
            } else {
                Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            }

            return false;
        }
        return true;
    }

    private void save() {
        logTimeBtn.setVisible(false);
        if (!"".equalsIgnoreCase(projects.getSuggestBox().getText().trim()) && (projects.getSelectedItemID()!=null && projects.getSelectedItemID()>0 || entityType != CRM_CASE) ) {
            item.setBusObjectId(tasks.getSelectedItem().getId());
        } else {
            item.setBusObjectId(cases.getSelectedItemID());
        }
        if (!"".equalsIgnoreCase(projects.getSuggestBox().getText().trim()) && projects.getSelectedItemID()!=null && projects.getSelectedItemID()>0 ) {
            item.setRelation(PM_TASK);
            item.setTodaysTime(parseSecond(hoursLabel.getText()));
        } else {
            item.setRelation(CRM_CASE);
        }

        item.setComment(comments.getText());
        timer.cancel();
        timerIsStarted = false;
        item.setReset(true);
        ClockWidgetService.App.get().applyTime(item, new AbstractAsyncCallback<Integer[]>() {
            @Override
            public void failure(Throwable caught) {
                Info.show(wfmStrings.notApplied(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer[] response) {
                if (!"".equalsIgnoreCase(projects.getSuggestBox().getText().trim()) && (projects.getSelectedItemID()!=null && projects.getSelectedItemID()>0 || entityType != CRM_CASE) ) {
                    if (item.getTaskMap()!=null && item.getTaskMap().size() > 0 && item.getTaskMap().get(item.getBusObjectId()) != null) {
                        item.getTaskMap().get(item.getBusObjectId()).setTimesheetMinutes(response[1]);
                    }
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.hours()), Info.Type.INFO);
                }
                resetHoursLabel();
                if (tasks != null && projects != null) {
                    tasks.setEnabled(true);
                    projects.setEnabled(true);
                }

                loadLoggedEntries();
            }
        });
    }
}
