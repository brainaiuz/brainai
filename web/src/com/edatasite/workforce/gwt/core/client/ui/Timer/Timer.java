package com.edatasite.workforce.gwt.core.client.ui.Timer;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.ClockItem;
import com.edatasite.workforce.gwt.core.client.rpc.ClockWidgetService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 25.03.14
 * Time: 12:17
 * To change this template use File | Settings | File Templates.
 */
public class Timer extends TimerParent {
    private WfmForm form;
    private WfmForm.Field projectField;
    private WfmForm.Field caseField;
    private WfmForm.Field taskField;
    private CRMLookUp projects;
    private CRMLookUp cases;
    private CRMLookUp tasks;
    private Button apply;
    private Button start;
    private Button reset;
    private TextBox estHours;
    private TextBox actHours;
    private TextBox percent;
    private TextArea2 comments;

    private Integer objectId;
    private Integer entityType = PM_TASK;
    private Date timerDate = new Date();
    private Integer projectID;
    private ClockItem item;
    private com.google.gwt.user.client.Timer timer;
    private boolean timerIsStarted = false;
    private boolean pageIsLoaded = false;
    private final boolean anotherTimerIsRunning = true;
    private boolean timerIsRunning = true;
    private boolean haveTimer = false;
    private long diff = 0;
    private final int left;
    private final int top;
    private final com.google.gwt.gen2.table.override.client.FlexTable timerHeader = new com.google.gwt.gen2.table.override.client.FlexTable();
    private RadioButton taskRadioButton;
    private RadioButton issueRadioButton;
    private RadioButton caseRadioButton;

    public Timer(int absoluteLeft, int absoluteTop) {
        super(true);
        this.left = absoluteLeft;
        this.top = absoluteTop == 0 ? 30 : absoluteTop;
        setAnimationEnabled(true);
        sinkEvents(Event.ONCONTEXTMENU);
        this.addHandler(contextMenuEvent -> {
            contextMenuEvent.preventDefault();
            contextMenuEvent.stopPropagation();
        }, ContextMenuEvent.getType());
        this.getElement().getStyle().setPadding(0, Style.Unit.PX);
        newTimer();
        draftMenus(null);
    }

    private void draftMenus(final Integer newType) {
        ClockWidgetService.App.get().getHistoryClockItem(new AbstractAsyncCallback<ClockItem>() {
            @Override
            public void success(ClockItem result) {
                if (result != null) {
                    entityType = (newType != null ? newType : (result.getRelation() != null ? result.getRelation() : PM_TASK));
                    objectId = entityType.equals(result.getRelation()) ? result.getBusObjectId() : null;
                    haveTimer = true;
                } else {
                    entityType = newType != null ? newType : PM_TASK;
                    haveTimer = false;
                }
                com.google.gwt.gen2.table.override.client.FlexTable headerPanle = headerPanel();
                if (entityType != CRM_CASE) {
                    headerPanle.getElement().getStyle().setWidth(330, Style.Unit.PX);
                } else {
                    headerPanle.getElement().getStyle().setWidth(310, Style.Unit.PX);
                }
                onInitialize();
                headerPanle.setWidget(1, 0, form);
                setWidget(headerPanle);
            }
        });
    }

    protected Widget onInitialize() {

        hoursLabel = new Label();
        hoursLabel.setStyleName("wfmTimerHoursHeader");
        if (entityType != CRM_CASE) {
            projects = new CRMLookUp(LookUpConstants.PROJECT);
            projects.setFullSearch(true);
            projects.setWidth(MIN_DEFAULT_WIDTH);
            projects.getSuggestBox().addSelectionHandler(selectionEvent -> clearLookUps(tasks));
            projects.setBeforeSearch(() -> {
                projects.getFilterParametrs().setEmployeeId(Utils.getUserID());
                projects.getFilterParametrs().setIDsOnly(true);
            });

            tasks = new CRMLookUp(LookUpConstants.TASK);
            if (Utils.hasPermission(PermissionConstants.PM_TASKS_ADD) && entityType.equals(PM_TASK)) {
                tasks.setValueNotEmptyMeansSelected(true);
            }
            tasks.setWidth(MIN_DEFAULT_WIDTH);
            tasks.getSuggestBox().addSelectionHandler(selectionEvent -> {
                if (tasks.isSelected()) {
                    setAllData(tasks.getSelectedItem().getId());
                    objectId = tasks.getSelectedItem().getId();
                }
            });

            tasks.getSuggestBox().addValueChangeHandler(valueChangeEvent -> {
                if (entityType.equals(PM_TASK)) {
                    objectId = null;
                }
            });

            tasks.setBeforeSearch(() -> {
                tasks.setProjectID(projects.getSelectedItemID());
                tasks.setType(taskRadioButton.getValue() ? LookUpConstants.PM_TASK_ID : LookUpConstants.PM_ISSUE_ID);
            });

        } else {
            cases = new CRMLookUp(LookUpConstants.CRM_CASE);
            cases.setFullSearch(true);
            cases.setWidth(MIN_DEFAULT_WIDTH);
            cases.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
                if (cases.getSelectedItemID() != null) {
                    setAllData(cases.getSelectedItemID());
                    objectId = cases.getSelectedItemID();
                }
            });
        }
        if (!entityType.equals(CRM_CASE)) {
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
        comments = new TextArea2(1000, wfmStrings.comments());
        comments.setHeight("75px");
        comments.setWidth("150px");
        comments.setWidth(MIN_DEFAULT_WIDTH);


        apply = new Button(entityType != CRM_CASE ? wfmStrings.save() : "Log time");
        apply.ensureDebugId("logToTimesheet");
        apply.setStyleName("wfmTimer-buttons");
        apply.addClickHandler(event -> applyTime());

        start = new Button(haveTimer ? wfmStrings.stop() : wfmStrings.start());
        start.ensureDebugId("start");
        start.setStyleName("wfmTimer-startButton");
        start.addClickHandler(event -> startTimer(false));

        reset = new Button(wfmStrings.reset());
        reset.setEnabled(false);
        reset.ensureDebugId("reset");
        reset.setStyleName("wfmTimer-buttons");
        reset.addClickHandler(event -> reset());

        FlexTable buttonsPanel = new FlexTable();
        buttonsPanel.setWidget(0, 1, start);
        buttonsPanel.setWidget(0, 2, reset);

        timerDate.setHours(0);
        timerDate.setMinutes(0);
        timerDate.setSeconds(0);

        form = new WfmForm();
        form.addField(null, hoursLabel);
        form.addField(null, buttonsPanel);
        if (entityType != CRM_CASE) {
            projectField = form.addField(wfmStrings.projectName(), projects, true);
            taskField = form.addField(entityType.equals(PM_TASK) ? wfmStrings.taskName() : wfmStrings.issueName(), tasks, true);
        } else {
            caseField = form.addField(Property.get(Constants.CASE_LIST, wfmStrings.crmCase()), cases, true);
        }

        if (entityType != CRM_CASE) {
            form.addField(wfmStrings.estimatedTime(), estHours);
            form.addField(wfmStrings.totalTime(), actHours);
            form.addField(wfmStrings.completed() + "(%)", percent);
        }
        form.addField(null, comments);
        form.addButton(apply);

        setAllData(this.objectId);
        return form;
    }

    private void clearLookUps(LookUp lookUp) {
        if (lookUp != null) {
            lookUp.clearAndClearItems();
            lookUp.refreshOracle(true);
        }
        objectId = null;
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

    private com.google.gwt.gen2.table.override.client.FlexTable headerPanel() {
        taskRadioButton = new KpiRadioButton("timerType");
        issueRadioButton = new KpiRadioButton("timerType");
        caseRadioButton = new KpiRadioButton("timerType");

        switch (entityType) {
            case PM_TASK:
                taskRadioButton.setValue(true);
                break;
            case PM_ISSUE_TIMER:
                issueRadioButton.setValue(true);
                break;
            case CRM_CASE:
                caseRadioButton.setValue(true);
                break;
        }
        taskRadioButton.addClickHandler(clickEvent -> {
            form.getFields().clear();
            draftMenus(PM_TASK);
            if (Utils.hasPermission(PermissionConstants.PM_TASKS_ADD)) {
                tasks.setValueNotEmptyMeansSelected(true);
            }
            clearLookUps(tasks);
        });

        issueRadioButton.addClickHandler(clickEvent -> {
            form.getFields().clear();
            draftMenus(PM_ISSUE_TIMER);
            tasks.setValueNotEmptyMeansSelected(false);
            clearLookUps(tasks);
        });

        caseRadioButton.addClickHandler(clickEvent -> {
            form.getFields().clear();
            draftMenus(CRM_CASE);
        });

        com.google.gwt.gen2.table.override.client.FlexTable taskPanel = new com.google.gwt.gen2.table.override.client.FlexTable();
        taskPanel.getElement().setAttribute("cellpadding", "5");
        taskPanel.setWidget(0, 0, taskRadioButton);
        taskPanel.setWidget(0, 1, getNameWidget(wfmStrings.task()));

        com.google.gwt.gen2.table.override.client.FlexTable issuePanel = new com.google.gwt.gen2.table.override.client.FlexTable();
        issuePanel.getElement().setAttribute("cellpadding", "5");
        issuePanel.setWidget(0, 0, issueRadioButton);
        issuePanel.setWidget(0, 1, getNameWidget(Property.get(Constants.ISSUE, wfmStrings.issue())));

        com.google.gwt.gen2.table.override.client.FlexTable casePanel = new com.google.gwt.gen2.table.override.client.FlexTable();
        casePanel.getElement().setAttribute("cellpadding", "5");
        casePanel.setWidget(0, 0, caseRadioButton);
        casePanel.setWidget(0, 1, getNameWidget(Property.getPluralWithObjectCode(Constants.CASE_LIST, wfmStrings.cases())));


        com.google.gwt.gen2.table.override.client.FlexTable headir = new com.google.gwt.gen2.table.override.client.FlexTable();
        headir.setWidth("100%");
        headir.getFlexCellFormatter().setWidth(0, 0, "33%");
        headir.getFlexCellFormatter().setWidth(0, 1, "33%");
        headir.getFlexCellFormatter().setWidth(0, 2, "34%");
        headir.setWidget(0, 0, taskPanel);
        headir.setWidget(0, 1, issuePanel);
        headir.setWidget(0, 2, casePanel);
        timerHeader.setWidth("100%");
        timerHeader.setWidget(0, 0, headir);
        this.setPopupPositionAndShow((i, i1) -> setPopupPosition(left, top));
        this.setStyleName("timerPopup");
        return timerHeader;
    }

    private HTML getNameWidget(final String name) {
        if (name != null && !"".equals(name)) {
            return new HTML("<span style='color:#4f4f4f; margin-left:-5px;'>" + name + "</span>");
        }
        return new HTML();
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
                start.setText(wfmStrings.start());
                reset.setEnabled(false);
                resetHoursLabel();
                clearLookUps(projects);
                clearLookUps(tasks);
                Info.show(wfmStrings.lastWorkingTimeCleared(), Info.Type.INFO);
                if (entityType == PM_TASK) {
                }
            }
        });
    }

    private void resetHoursLabel() {
        timerDate.setHours(0);
        timerDate.setMinutes(0);
        timerDate.setSeconds(0);
        hoursLabel.setText(timerFormat.format(timerDate));
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
            timer.scheduleRepeating(1000);
            if (item == null) {
                item = new ClockItem();
            }
            item.setBusObjectId(objectId);
            if (objectId == null && entityType.equals(PM_TASK)) {
                item.setTaskName(tasks.getText());
                item.setProjectID(projects.getSelectedItemID());
                item.setStartDate(new Date());
                item.setEndDate(DateUtil.addDays(new Date(), 7));
                LoadingPanel.loading(true);
            }
            item.setRelation(entityType);
            item.setComment(comments.getText());
            item.setEstimateTime(estHours != null ? Utils.parseMinutes(estHours.getText()) : null);
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
                    Scheduler.get().scheduleDeferred(() -> {
                        LoadingPanel.loading(false);
                        timerIsStarted = true;
                        start.setText(wfmStrings.stop());
                        apply.setEnabled(false);
                        reset.setEnabled(false);
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

    private boolean validate() {
        int errors = 0;
        boolean isTimeFormatError = false;
        if (entityType != CRM_CASE) {
            if (entityType == PM_ISSUE_TIMER) {
                if (taskField != null && !Validation.validateListBoxRequired(tasks, taskField, wfmStrings.pleaseSelectTask())) {
                    errors++;
                }
            }
            if ("".equals(tasks.getText()) || LookUp.wfmStrings.searchTypeMessage().equals(tasks.getText())) {
                if (taskField != null && !Validation.validateListBoxRequired(tasks, taskField, wfmStrings.pleaseSelectTask())) {
                    errors++;
                }
            }
            if (projectField != null && !Validation.validateListBoxRequired(projects, projectField, wfmStrings.pleaseSelectProject())) {
                errors++;
            }
        } else {
            if (cases != null && !Validation.validateListBoxRequired(cases, caseField, wfmStrings.pleaseSelectProject())) {
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

    private void setAllData(final Integer objectID) {
        ClockWidgetService.App.get().getClockItem(objectID, entityType, new DateNonConvertable(), new AbstractAsyncCallback<ClockItem>() {
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
                    if (result.getElapsedTime() != null && result.getElapsedTime() > 0 && timerIsRunning) {
                        timerIsRunning = false;
                        timerDate.setSeconds(result.getElapsedTime());
                        diff = new Date().getTime() - timerDate.getTime();
                    } else if (result.getElapsedTime() != null && result.getElapsedTime() > 0 && !result.isStarted()) {
                        timerIsRunning = false;
                        timerDate.setSeconds(result.getElapsedTime());
                        diff = new Date().getTime() - timerDate.getTime();
                    }
                    if (entityType != CRM_CASE) {
                        percent.setEnabled(!Utils.userSettings.get(ISAUTOMATIC).equals("true"));
                    }
                    hoursLabel.setText(timerFormat.format(timerDate));
                    if (entityType != CRM_CASE) {
                        pageIsLoaded = true;
                        if (result.getProjectID() != null && !projects.isSelected()) {
                            projects.setSelected(new SelectItem(result.getProjectID(), result.getProjectName()));
                        }
                        if (objectId != null) {
                            tasks.setSelected(objectId, entityType.equals(PM_TASK) ? result.getTaskName() : result.getIssueName());
                        }
                        if (result.getEstimateTime() != null) {
                            estHours.setText(Utils.formatMinutes(result.getEstimateTime()));
                        }
                        if (result.getActualTime() != null) {
                            actHours.setText(Utils.formatMinutes(result.getActualTime()));
                        }
                        percent.setText(result.getPercent() != null ? result.getPercent().toString() : "");
                    } else {
                        if (result.getBusObjectId() != null) {
                            cases.setSelected(new SelectItem(result.getBusObjectId(), result.getCaseName()));
                        }
                    }
                    comments.setText(result.getComment());
                    if (result.isStarted() && haveTimer) {
                        timerIsStarted = true;
                        timer.scheduleRepeating(1000);
                    }
                    if (haveTimer && result.isHaveStoppedTime() && !result.isStarted()) {
                        start.setText(wfmStrings.start());
                        if (result.getElapsedTime() != null && result.getElapsedTime() > 0 || result.isHaveStoppedTime()) {
                            reset.setEnabled(true);
                            apply.setEnabled(true);
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
        }
    }

    private void applyTime() {
        if (validate()) {
            if (entityType != CRM_CASE) {
                if (item.isApprovedForToday() && "false".equals(Utils.userSettings.get(ISAUTOMATICAPPROVAL))) {
                    Info.show(wfmStrings.cannotLogApproved(), Info.Type.INFO);
                    return;
                } else if (item.isSentToApproveForToday() && "false".equals(Utils.userSettings.get(ISAUTOMATICAPPROVAL))) {
                    Info.show(wfmStrings.cannotLogSentForApproval(), Info.Type.INFO);
                    return;
                }
                if (!validateAgainstTimesheetSettimgs(item.getTaskMap().get(tasks.getSelectedItem().getId()))) {
                    return;
                }
            }
            save();
        }
    }

    private void save() {
        apply.setEnabled(false);
        item.setBusObjectId(entityType != CRM_CASE ? tasks.getSelectedItem().getId() : cases.getSelectedItemID());
        item.setRelation(entityType);
        if (entityType != CRM_CASE) {
            item.setTodaysTime(parseSecond(hoursLabel.getText()));
            if (percent.getText() != null && !"".equals(percent.getText())) {
                item.setPercent(Float.valueOf(percent.getText()));
            }
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
                if (entityType != CRM_CASE) {
                    start.setEnabled(true);
                    actHours.setText(Utils.formatMinutes(response[0]));
                    if (item.getTaskMap().size() > 0 && item.getTaskMap().get(item.getBusObjectId()) != null) {
                        item.getTaskMap().get(item.getBusObjectId()).setTimesheetMinutes(response[1]);
                    }
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.hours()), Info.Type.INFO);
                }
                resetHoursLabel();
            }
        });
    }

}
