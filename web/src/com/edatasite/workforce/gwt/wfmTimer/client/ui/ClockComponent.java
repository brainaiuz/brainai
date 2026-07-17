package com.edatasite.workforce.gwt.wfmTimer.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AttendanceStats;
import com.edatasite.workforce.gwt.core.client.rpc.ClockItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxRow;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.wfmTimer.client.rpc.ClockService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Span;

import java.util.Date;

/**
 * User: Ilxom Lutfullaev
 * Date: Aug 11, 2010
 * Time: 2:09:04 PM
 */

public class ClockComponent extends View implements Constants, Colapse {

    private static final DateTimeFormat timerFormat = DateTimeFormat.getFormat("HH:mm:ss");
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private DateTimeFormat dateTimeFormat;
    private WfmForm form;
    private Span hoursSpan;
    private CRMLookUp projects;
    private CRMLookUp cases;
    private DataListBox tasks;
    private WfmButton2 apply;
    private WfmButton2 start;
    private WfmButton2 reset;
    private TextBox startDate;
    private TextBox endDate;
    private TextBox estHours;
    private TextBox actHours;
    private TextBox percent;
    private TextArea2 comments;

    private Integer objectId;
    private Integer entityType = PM_TASK;
    private Date timerDate = new Date();
    private Integer projectID;
    private ClockItem item;
    private Timer timer;
    private boolean timerIsStarted = false;
    private boolean pageIsLoaded = false;
    private boolean anotherTimerIsRunning = false;
    private long diff = 0;

    public ClockComponent(Integer busObjectId, Integer type) {
        super("summary", wfmStrings.timer());
        this.objectId = busObjectId;
        this.entityType = type;
    }

    public ClockComponent(Integer busObjectId, Integer type, String param1) {
        this(busObjectId, type);
        if (type != CRM_CASE) {
            this.projectID = Integer.valueOf(param1);
        }
        this.objectId = busObjectId;
    }

    @Override
    public String getIconStyle() {
        return "crm campaign-list";
    }

    @Override
    protected Widget onInitialize() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TIMESHEET_SUBMIT_FOR_APPROVAL, ClockComponent.this, (sender, args) -> {
            if (tasks.getSelectedItem() != null) {
                setAllData(tasks.getSelectedItem().getId());
            }
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TIMESHEET_APPROVAL, ClockComponent.this, (sender, args) -> {
            if (tasks.getSelectedItem() != null) {
                setAllData(tasks.getSelectedItem().getId());
            }
        });
        hoursSpan = new Span();
        hoursSpan.setStyleName("wfmTimerHours");
        if (entityType != CRM_CASE) {
            projects = new CRMLookUp(LookUpConstants.PROJECT);
            projects.setFullSearch(true);
            projects.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> ClockService.App.get().getProjectTasks(projects.getSelectedItemID(), entityType, new DateNonConvertable(), new AbstractAsyncCallback<ClockItem>() {
                @Override
                public void failure(Throwable throwable) {
                }

                @Override
                public void success(ClockItem result) {
                    tasks.clear();
                    if (result.getTasks() != null) {
                        tasks.setItems(result.getTasks());
                    }
                }
            }));

            tasks = new DataListBox();
            tasks.addStyleName(DEFAULT_WIDTH);
            tasks.addValueChangeHandler(changeEvent -> {
                if (tasks.getSelectedItem() != null) {
                    anotherTimerIsRunning = false;
                    setAllData(tasks.getSelectedItem().getId());
                }
            });
        } else {
            cases = new CRMLookUp(LookUpConstants.CRM_CASE);
            cases.setFullSearch(true);
            cases.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
                if (cases.getSelectedItemID() != null) {
                    anotherTimerIsRunning = false;
                    setAllData(cases.getSelectedItemID());
                }
            });
        }
        startDate = new TextBox();
        startDate.setText(DateUtils.formatInternalShort(new Date()));
        startDate.setEnabled(false);
        startDate.addStyleName(DEFAULT_WIDTH);
        endDate = new TextBox();
        endDate.setEnabled(false);
        endDate.addStyleName(DEFAULT_WIDTH);
        if (!entityType.equals(CRM_CASE)) {
            estHours = new TextBox();
            estHours.addStyleName(DEFAULT_WIDTH);
            estHours.setEnabled(false);
            actHours = new TextBox();
            actHours.setEnabled(false);
            actHours.addStyleName(DEFAULT_WIDTH);
            percent = new TextBox();
            percent.addStyleName(DEFAULT_WIDTH);
            Validation.addNumericKeyboardListener(percent);
        }
        comments = new TextArea2(1000, wfmStrings.comments());
        comments.setHeight("50px");


        apply = new WfmButton2(entityType != CRM_CASE ? wfmStrings.save() : wfmStrings.logTime(), WfmButton2.BTN_DEFAULT, clickEvent -> applyTime());
        apply.ensureDebugId("logToTimesheet");

        start = new WfmButton2(wfmStrings.start(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            if (anotherTimerIsRunning) {
                WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, wfmStrings.areYouSureToResetTimerToCurrent(), new CloseHandler() {
                    @Override
                    public void onCancel() {
                        //it is ok do nothing
                    }

                    @Override
                    public void onSubmit() {
                        startTimer(true);
                    }
                });
                wfmMessageBox.setTitle(wfmStrings.confirmation());
                wfmMessageBox.open();
            } else {
                startTimer(false);
            }
        });
        start.ensureDebugId("start");

        reset = new WfmButton2(wfmStrings.reset(), WfmButton2.BTN_DEFAULT, clickEvent -> {
            if (anotherTimerIsRunning) {
                WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, wfmStrings.areYouSureToResetTimer(), new CloseHandler() {
                    @Override
                    public void onCancel() {
                        //it is ok do nothing
                    }

                    @Override
                    public void onSubmit() {
                        reset();
                    }
                });
                wfmMessageBox.setTitle(wfmStrings.confirmation());
                wfmMessageBox.open();
            } else {
                reset();
            }
        });
        reset.ensureDebugId("reset");

        MaterialPanel buttonsPanel = new MaterialPanel("wfmTimerHours-wrapper");
        buttonsPanel.add(start);
        buttonsPanel.add(hoursSpan);
        buttonsPanel.add(reset);

        timerDate.setHours(0);
        timerDate.setMinutes(0);
        timerDate.setSeconds(0);

        timer = new Timer() {
            @Override
            public void run() {
//                timerDate.setSeconds(timerDate.getSeconds() + 1);
                timerDate = new Date(new Date().getTime() - diff);
                hoursSpan.setText(timerFormat.format(timerDate));
            }
        };

        form = new WfmForm();
        form.addField(null, buttonsPanel);
        buttonsPanel.getElement().getStyle().clearDisplay();

        MaterialPanel sectionBoxPanel = new MaterialPanel("section-box__content");

        GBox groupBox = new GBox();
        groupBox.setStyleUnited(true);

        GBoxRow groupBoxRow = new GBoxRow();
        groupBox.add(groupBoxRow);

        if (entityType != CRM_CASE) {
            GBoxItem projectField = new GBoxItem(wfmStrings.projectName(), projects);
            groupBoxRow.add(projectField);
            GBoxItem taskField = new GBoxItem(wfmStrings.taskName(), tasks);
            groupBoxRow.add(taskField);
        } else {
            groupBoxRow.add(new GBoxItem(Property.get(Constants.CASE_LIST, wfmStrings.crmCase()), cases));
        }

        if (entityType != CRM_CASE) {
            groupBoxRow.add(new GBoxItem(wfmStrings.estimatedTime(), estHours));
            groupBoxRow.add(new GBoxItem(wfmStrings.totalTime(), actHours));
            groupBoxRow.add(new GBoxItem(wfmStrings.completed() + "(%)", percent));
        }
        GBoxItem commentItem = new GBoxItem(comments);
        commentItem.setStyleNoBorder(true);
        GBox commnetGroupBox = new GBox(new GBoxRow(commentItem));
        commnetGroupBox.setStyleUnited(true);

        GBoxItem applyItem = new GBoxItem(apply);
        applyItem.removeBoxItemLabel();
        applyItem.addStyleName("group-box--align-right");
        GBox buttonGroupBox = new GBox(new GBoxRow(applyItem));
        buttonGroupBox.addStyleName("group-box--align-right");

        setAllData(this.objectId);

        add(form);

        MaterialPanel sectionBox = new MaterialPanel("section-box box-bg--1");
        sectionBox.add(groupBox);
        sectionBox.add(commnetGroupBox);
        sectionBox.add(buttonGroupBox);
        add(sectionBox);
        return null;
    }

    private void reset() {
        item.setReset(true);
        ClockService.App.get().stopTimer(item, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void success(Void result) {
                anotherTimerIsRunning = false;
                timer.cancel();
                start.setText(wfmStrings.start());
                reset.setEnabled(false);
                resetHoursLabel();
                Info.show(wfmStrings.lastWorkingTimeCleared(), Info.Type.INFO);
                if (entityType == PM_TASK) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_EDIT, null, ClockComponent.this);
                }
            }
        });
    }

    private void applyTime() {
        if (entityType != CRM_CASE) {
            if (item.isApprovedForToday() && "false".equals(Utils.userSettings.get(ISAUTOMATICAPPROVAL))) {
                Info.show(wfmStrings.cannotLogApproved(), Info.Type.INFO);
                return;
            } else if (item.isSentToApproveForToday() && "false".equals(Utils.userSettings.get(ISAUTOMATICWAITINGFORAPPROVAL))) {
                Info.show(wfmStrings.cannotLogSentForApproval(), Info.Type.INFO);
                return;
            }
            if (!validateAgainstTimesheetSettimgs(item.getTaskMap().get(tasks.getSelectedItem().getId()))) {
                return;
            }
        }
        if (anotherTimerIsRunning) {
            WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, wfmStrings.areYouSureToResetTimerToCurrent(), new CloseHandler() {
                @Override
                public void onCancel() {
                    //it is ok do nothing
                }

                @Override
                public void onSubmit() {
                    save();
                }
            });
            wfmMessageBox.setTitle(wfmStrings.confirmation());
            wfmMessageBox.open();
        } else {
            save();
        }
    }

    public static Integer parseSecond(String minutes) throws NumberFormatException, StringIndexOutOfBoundsException {
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

    private void save() {
        apply.setEnabled(false);
        item.setBusObjectId(entityType != CRM_CASE ? tasks.getSelectedItem().getId() : cases.getSelectedItemID());
        item.setRelation(entityType);
        if (entityType != CRM_CASE) {
            item.setTodaysTime(parseSecond(hoursSpan.getText()));
            if (percent.getText() != null && !"".equals(percent.getText())) {
                item.setPercent(Float.valueOf(percent.getText()));
            }
        }
        item.setComment(comments.getText());
        timer.cancel();
        timerIsStarted = false;
        item.setReset(true);
        ClockService.App.get().applyTime(item, new AbstractAsyncCallback<Integer[]>() {
            @Override
            public void failure(Throwable caught) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer[] response) {
                anotherTimerIsRunning = false;
                if (entityType != CRM_CASE) {
                    start.setEnabled(true);
                    startDate.setText("");
                    endDate.setText("");
                    actHours.setText(Utils.formatMinutes(response[0]));
                    item.getTaskMap().get(item.getBusObjectId()).setTimesheetMinutes(response[1]);

                    if (entityType == PM_TASK) {
                        //WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_EDIT, null, ClockComponent.this);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TIMESHEET_TASK_STATUS_CHANGED, null, null);
                    }
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.hours()), Info.Type.INFO);
                }
                resetHoursLabel();

                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TIMER_STARTED, null, ClockComponent.this);
            }
        });
    }

    private boolean validateAgainstTimesheetSettimgs(AttendanceStats attendanceStats) {
        Date clientToday = new Date();
        DateTimeFormat format = DateTimeFormat.getFormat(Utils.getShortDateFormat());
        Integer lastMinutes = (int) Math.ceil((double) parseSecond(hoursSpan.getText()) / 60);
        if ("true".equals(Utils.userSettings.get(VALIDATE_TASK_START))) {
            if ((clientToday.getYear() == attendanceStats.getTaskStart().getYear() && clientToday.getMonth() == attendanceStats.getTaskStart().getMonth() && clientToday.getDate() < attendanceStats.getTaskStart().getDate()) ||
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
            Info.get().show(wfmStrings.dayOffValidationMessage(), Info.Type.WARNING);
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
                Info.show(wfmStrings.hourlyLeaveRequestValidationMessage() + Utils.formatMinutes(maxMinutesAllowed - attendanceStats.getLeaveMinutes()) + wfmStrings.hours(), Info.Type.WARNING);
                return false;
            }
        }
        if ("true".equals(Utils.userSettings.get(VALIDATE_MAXIMUM_HOURS))) {
            if ("true".equals(Utils.userSettings.get(VALIDATE_TIMESLOT))) {
                if (attendanceStats.getTimeslotMinutes() - attendanceStats.getTimesheetMinutes() < lastMinutes) {
                    Info.show(wfmStrings.timeslotValidationMessage() + Utils.formatMinutes(attendanceStats.getTimeslotMinutes()) + wfmStrings.hours(), Info.Type.WARNING);
                    return false;
                }
            } else {
                int maxHoursAllowed = 0;
                if (Utils.userSettings.get(MAXIMUM_HOURS) != null && !Utils.userSettings.get(MAXIMUM_HOURS).equals("")) {
                    maxHoursAllowed = Integer.valueOf(Utils.userSettings.get(MAXIMUM_HOURS));
                }
                if ((maxHoursAllowed * 60) - attendanceStats.getTimesheetMinutes() < lastMinutes) {
                    Info.show(wfmStrings.timeslotValidationMessage() + maxHoursAllowed + wfmStrings.hours(), Info.Type.WARNING);
                    return false;
                }
            }
        }
        return true;
    }

    private void resetHoursLabel() {
        timerDate.setHours(0);
        timerDate.setMinutes(0);
        timerDate.setSeconds(0);
        hoursSpan.setText(timerFormat.format(timerDate));
    }

    private void setAllData(final Integer objectID) {
        ClockService.App.get().getClockItem(objectID, entityType, new DateNonConvertable(), new AbstractAsyncCallback<ClockItem>() {
            @Override
            public void failure(Throwable caught) {
                item = new ClockItem();
            }

            @Override
            public void success(ClockItem result) {
                if (entityType != CRM_CASE) {
                    estHours.setText("");
                    actHours.setText("");
                    percent.setText("");
                    reset.setEnabled(false);
                }
                apply.setEnabled(false);
                comments.setText("");
                if (result != null) {
                    item = result;
                    if (objectID.equals(objectId)) {
                        if (result.getElapsedTime() != null && result.getElapsedTime() > 0) {
                            timerDate.setSeconds(result.getElapsedTime());
                            diff = new Date().getTime() - timerDate.getTime();
                        }
                    } else {
                        if (entityType != CRM_CASE) {
                            objectId = tasks.getSelectedItem().getId();
                        } else {
                            objectId = cases.getSelectedItemID();
                        }
                        if (result.isStarted()) {
                            anotherTimerIsRunning = true;
                        }
                    }
                    if (entityType != CRM_CASE) {
                        percent.setEnabled(!Utils.userSettings.get(ISAUTOMATIC).equals("true"));
                    }
                    hoursSpan.setText(timerFormat.format(timerDate));
                    if (entityType != CRM_CASE) {
                        if (!pageIsLoaded) {
                            pageIsLoaded = true;
//                            projects.setItems(result.getProjects());
                            if (result.getProjectID() != null) {
                                projects.setSelected(new SelectItem(result.getProjectID(), result.getProjectName()));
                            } else if (projectID != null) {
                                projects.setSelected(projectID);
                            }
                            tasks.setItems(result.getTasks());
                            if (objectId != null) {
                                tasks.setSelected(new SelectItem(objectId, result.getProjectName()));
                            }
                        }
                        if (result.getEstimateTime() != null) {
                            estHours.setText(Utils.formatMinutes(result.getEstimateTime()));
                        }
                        if (result.getActualTime() != null) {
                            actHours.setText(Utils.formatMinutes(result.getActualTime()));
                        }
                        percent.setText(result.getPercent() != null ? result.getPercent().toString() : "");
                    } else {
//                        cases.setItems(result.getCases());
                        if (result.getBusObjectId() != null) {
                            cases.setSelected(new SelectItem(result.getBusObjectId(), result.getCaseName()));
                        } else if (objectID != null) {
                            cases.setSelected(new SelectItem(objectID, result.getCaseName()));
                        }
                    }
                    comments.setText(result.getComment());
                    if (result.isStarted() && !anotherTimerIsRunning) {
                        timerIsStarted = true;
                        start.setText(wfmStrings.stop());
                        timer.scheduleRepeating(1000);
                    } else if (!result.isReset()) {
                        if (entityType != CRM_CASE) {
                            reset.setEnabled(true);
                        }
                        apply.setEnabled(true);
                    }
                    if (result.getCompanyDateTimeFormat() != null && !"".equals(result.getCompanyDateTimeFormat())) {
                        dateTimeFormat = DateTimeFormat.getFormat(result.getCompanyDateTimeFormat());
                        if (result.getEndDate() != null && !result.isStarted()) {
                            endDate.setText(dateTimeFormat.format(result.getEndDate()));
                        }
                        if (result.getStartDate() != null) {
                            startDate.setText(dateTimeFormat.format(result.getStartDate()));
                        }
                    } else {
                        if (result.getStartDate() != null) {
                            startDate.setText(DateUtils.formatInternalShort(result.getStartDate()));
                        }
                        if (result.getEndDate() != null && !result.isStarted()) {
                            endDate.setText(DateUtils.formatInternalShort(result.getEndDate()));
                        }
                    }
                }
                changeContainerName();
            }
        });
    }

    private void changeContainerName() {
        if (tasks != null && tasks.getSelectedItem() != null && tasks.getSelectedItem().getName() != null && !"".equals(tasks.getSelectedItem().getName())) {
            String taskName = tasks.getSelectedItem().getName();
            if (taskName.length() > 25) {
                taskName = taskName.substring(0, 25) + "...";
            }
            container.setDescription(wfmStrings.timer() + "-" + taskName);
        }
    }

    public void startTimer(boolean overrideAnotherTimerInstance) {
        if (item.isApprovedForToday() && "false".equals(Utils.userSettings.get(ISAUTOMATICAPPROVAL))) {
            Info.show(wfmStrings.cannotStartApproved(), Info.Type.INFO);
            return;
        } else if (item.isSentToApproveForToday() && "false".equals(Utils.userSettings.get(ISAUTOMATICAPPROVAL))) {
            Info.show(wfmStrings.cannotStartSentForApproval(), Info.Type.INFO);
            return;
        }
        if (timerIsStarted) {
            timer.cancel();
            reset.setEnabled(true);
            apply.setEnabled(true);
            start.setText(wfmStrings.start());
            item.setReset(false);
            item.setTodaysTime(parseSecond(hoursSpan.getText()));
            ClockService.App.get().stopTimer(item, new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable throwable) {
                }

                @Override
                public void success(Void aVoid) {
                    anotherTimerIsRunning = false;
                    timerIsStarted = false;
                    if (entityType == PM_TASK) {
                        if ("true".equals(Utils.userSettings.get(SAVE_TIMER_INTO_TIMESHEET_AUTOMATICALLY))) {
                            applyTime();
                        }
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_EDIT, null, ClockComponent.this);
                    }

//                    MaterialToast.fireToast("Event stop fired");
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TIMER_STARTED, null, ClockComponent.this);
                }
            });
        } else {
            if (!validate()) {
                return;
            }
            diff = new Date().getTime() - timerDate.getTime();
            timer.scheduleRepeating(1000);
            if (item == null) {
                item = new ClockItem();
            }
            item.setBusObjectId(objectId);
            item.setRelation(entityType);
            item.setComment(comments.getText());
            item.setEstimateTime(estHours != null ? Utils.parseMinutes(estHours.getText()) : null);
            item.setOverrideAnotherTimerInstance(overrideAnotherTimerInstance);
            if (overrideAnotherTimerInstance) {
                item.setTodaysTime(parseSecond(hoursSpan.getText()));
            }
            ClockService.App.get().startTimer(item, new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable caught) {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    timerIsStarted = false;
                }

                @Override
                public void success(Void result) {
                    anotherTimerIsRunning = false;
                    timerIsStarted = true;
                    start.setText(wfmStrings.stop());
                    apply.setEnabled(false);
                    reset.setEnabled(false);
                    if (entityType == PM_TASK) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_EDIT, null, ClockComponent.this);
                    }
                    Info.show(wfmStrings.timerSuccStarted(), Info.Type.INFO);

                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TIMER_STARTED, null, ClockComponent.this);
                }
            });
        }

    }

    private boolean validate() {
        int errors = 0;
        boolean isTimeFormatError = false;

        if (tasks != null && !Validation.validateListBoxRequired(tasks, new HTML(), "")) {
            errors++;
        }
        if (projects != null && !Validation.validateLookUpRequired(projects)) {
            errors++;
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


    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public void setEntityType(Integer entityType) {
        this.entityType = entityType;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}