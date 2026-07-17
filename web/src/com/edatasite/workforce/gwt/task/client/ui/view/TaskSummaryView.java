package com.edatasite.workforce.gwt.task.client.ui.view;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.form.Localize;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.DateTimePicker;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.NoteWidget2;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.StartEndTime;
import com.edatasite.workforce.gwt.core.client.ui.TaskStatusHistoryGrid;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.ExportPdfButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.view.Reminder;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskServiceAsync;
import com.edatasite.workforce.gwt.task.client.ui.ListBoxWithPM;
import com.edatasite.workforce.gwt.task.client.ui.TaskChangingStatusNoteModal;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SimpleHtmlSanitizer;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: Dilshod
 * Date: 11-Feb-2010
 * Time: 17:59:07
 */
public class TaskSummaryView extends CustomForm2 implements Constants, HasLinksInterface, FormHasCustomFieldInterface {

    private final static TaskServiceAsync taskService = TaskService.App.get();
    private final static ProjectStrings projectStrings = ProjectStrings.App.get();

    private final static NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");
    private final static String LOG_TIME_BUTTON = "LOG_TIME_BUTTON";
    private final static String TIMER_BUTTON = "TIMER_BUTTON";
    private final static ProvidesKey<PositionsSelectItem> KEY_PROVIDER = item -> item == null ? null : item.getId();

    private TaskSingleItem item;
    private TaskListItem taskListItem;
    private HasLinks linkingUtil;
    private FooterInformer link;
    AtomicBoolean firstClick = new AtomicBoolean(true);
    private NoteWidget2 noteWidget;
    private FormHasCustomField customFieldUtil;
    private ListBoxWithPM status;
    private WfmMessageBox changeStatusMessageBox;


    private final Integer taskID;
    private TextArea2 description;
    private HTML number, taskName, /*startDate, dueDate,*/
            type, completed,
            pManager, bManager, client, actualStartDate, actualEndDate, estimatedTime, timeSpent, actualTimeSpent, estimatedCost,
            actualCost, billable, createdBy, createdDate, updatedBy, updatedDate, waitingHours, rejectedHours;
    private DataListBox priority;
    private DateTimePicker dateTime;
    private final DateTimeFormat timeFormat = DateUtils.getTimeFormatInternal();

    private FlowPanel projectName, predecessorTask, successorTasks, dueDateReminder, parentWorkstream;

    private KpiDataGrid<PositionsSelectItem> dataGrid;
    private ListDataProvider<PositionsSelectItem> dataProvider;
    private ColumnSortEvent.ListHandler<PositionsSelectItem> listHandler;
    private final String test_code_ID_name = "summary_task_view_";
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private TaskStatusHistoryGrid taskStatusHistoryGrid;


    public TaskSummaryView(Integer id) {
        super("summary");
        setDescription(property.getSingular(wfmStrings.summaryView(), wfmStrings.task()));
        this.taskID = id;
    }

    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Task, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
                TaskSummaryView.super.onInitialize();
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                formPropertyMap = result.getFormPropertyMap();
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                }
                TaskSummaryView.super.onInitialize();
            }

        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_STATUS_CHANGES, TaskSummaryView.this, (sender, args) -> refreshAssigeesStatus());

        return null;
    }

    @Override
    public void registerFields() {
        projectName = new FlowPanel();

        number = initHTML();
        number.ensureDebugId(test_code_ID_name + "number");
        number.addStyleName("task-details__number");

        taskName = initHTML();
        taskName.ensureDebugId(test_code_ID_name + "taskName");

        description = new TextArea2(DEFAULT_DESCRIPTION_CHARACTER_LIMIT);
        description.hideCharacterLimitPanel();
        description.ensureDebugId(test_code_ID_name + "description");
        description.setWidth("100%");
        description.setHeight("200px");

        dateTime = new DateTimePicker(false, true);
        dateTime.startDate.setEnabled(false);
        dateTime.dueDate.setEnabled(false);
        dateTime.startTime.setEnabled(false);
        dateTime.endTime.setEnabled(false);
        dateTime.allDay.setEnabled(false);

        priority = new DataListBox();
        priority.ensureDebugId(test_code_ID_name + "priority");
        priority.setEnabled(false);

        type = initHTML();
        type.ensureDebugId(test_code_ID_name + "type");

        status = new ListBoxWithPM();
        status.ensureDebugId(test_code_ID_name + "status");
        status.getListBox().setEnabled(false);
        status.getListBox().addValueChangeHandler(valueChangeEvent -> changeStatus());

        completed = initHTML();
        completed.ensureDebugId(test_code_ID_name + "completed");


        billable = initHTML();
        billable.ensureDebugId(test_code_ID_name + "billable");

        dataProvider = new ListDataProvider<>();
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataGrid.setWidth("100%");
        dataGrid.setHeight("120px");
        dataGrid.addStyleName("cellBasedWidget-mod cellBasedWidget-mod--fixed-height");
        dataGrid.getElement().getStyle().setOverflow(Style.Overflow.AUTO);
        dataGrid.ensureDebugId(test_code_ID_name + "assignees");
        listHandler = new ColumnSortEvent.ListHandler<>(dataProvider.getList());
        dataGrid.addColumnSortHandler(listHandler);
        dataProvider.addDataDisplay(dataGrid);

        parentWorkstream = new FlowPanel();
        parentWorkstream.ensureDebugId(test_code_ID_name.concat("workstream"));

        dueDateReminder = new FlowPanel();
        predecessorTask = new FlowPanel();
        successorTasks = new FlowPanel();

        pManager = initHTML();
        pManager.ensureDebugId(test_code_ID_name + "pManager");

        bManager = initHTML();
        bManager.ensureDebugId(test_code_ID_name + "bManager");

        client = initHTML();
        client.ensureDebugId(test_code_ID_name + "client");

        estimatedTime = initHTML();
        estimatedTime.ensureDebugId(test_code_ID_name + "estimatedTime");

        timeSpent = initHTML();
        timeSpent.ensureDebugId(test_code_ID_name + "timeSpent");

        estimatedCost = initHTML();
        estimatedCost.ensureDebugId(test_code_ID_name + "estimatedCost");

        actualCost = initHTML();
        actualCost.ensureDebugId(test_code_ID_name + "actualCost");

        actualStartDate = initHTML();
        actualStartDate.ensureDebugId(test_code_ID_name + "actualStartDate");

        actualEndDate = initHTML();
        actualEndDate.ensureDebugId(test_code_ID_name + "actualEndDate");

        actualTimeSpent = initHTML();
        actualTimeSpent.ensureDebugId(test_code_ID_name + "actualTimeSpent");

        waitingHours = initHTML();
        waitingHours.ensureDebugId(test_code_ID_name + "waitingHours");

        rejectedHours = initHTML();
        rejectedHours.ensureDebugId(test_code_ID_name + "rejectedHours");

        createdBy = initHTML();
        createdBy.ensureDebugId(test_code_ID_name + "createdBy");

        createdDate = initHTML();
        createdDate.ensureDebugId(test_code_ID_name + "createdDate");

        updatedBy = initHTML();
        updatedBy.ensureDebugId(test_code_ID_name + "updatedBy");

        updatedDate = initHTML();
        updatedDate.ensureDebugId(test_code_ID_name + "updatedDate");

        getCustomFieldUtil().drawCustomFields(this, taskID, true);
        noteWidget = new NoteWidget2(taskID, RelationItem.TYPE_TASK);
        noteWidget.addStyleName("file--TaskSummaryView");


        taskStatusHistoryGrid = new TaskStatusHistoryGrid(taskID);
        drawForm();
    }

    private void changeStatus() {
        LoadingPanel.loading(true);
        if (status != null) {
            if (status.getListBox().getValue().isSelected()) {
                new TaskChangingStatusNoteModal(taskID, status.getListBox().getValue().getId(), true);
                WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_LOAD_STATUS_HISTORY, TaskSummaryView.this, (sender, args) -> {
                    taskStatusHistoryGrid.refresher();
                });
            } else {
                taskService.updateTaskStatus(taskID, status.getListBox().getSelectedId(), null, new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSuccess(Void aVoid) {
                        LoadingPanel.loading(false);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_EDIT, aVoid, TaskSummaryView.this);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DASHBOARD_TASK_REFRESH, aVoid, TaskSummaryView.this);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_STATUS_CHANGES, aVoid, TaskSummaryView.this);
                    }
                });
            }
        }
    }

    private void drawForm() {
        //Task Details
        addTitleField(CustomFormConstants.TASK.TASK_DETAILS, getTitle(property.getSingular(wfmStrings.taskDetails(), wfmStrings.task())));
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.PROJECT) != null) {
            addField(CustomFormConstants.TASK.PROJECT, projectName, getTitle(formPropertyMap.get(CustomFormConstants.TASK.PROJECT).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.PROJECT).getTitle() : wfmStrings.project()));
        } else {
            addField(CustomFormConstants.TASK.PROJECT, projectName, getTitle(Property.get(Constants.PROJECT, wfmStrings.project())));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null) {
            addField(CustomFormConstants.NUMBER, number, getTitle(formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : wfmStrings.number()));
        } else {
            addField(CustomFormConstants.NUMBER, number, getTitle(wfmStrings.number()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null) {
            addField(CustomFormConstants.NAME, taskName, getTitle(formPropertyMap.get(CustomFormConstants.NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.NAME).getTitle() : wfmStrings.name()));
        } else {
            addField(NAME, taskName, getTitle(wfmStrings.name()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null) {
            addField(CustomFormConstants.DESCRIPTION, description, getTitle(formPropertyMap.get(CustomFormConstants.DESCRIPTION).isChanged() ? formPropertyMap.get(CustomFormConstants.DESCRIPTION).getTitle() : wfmStrings.description()));
        } else {
            addField(DESCRIPTION, description, getTitle(wfmStrings.description()), false);
        }
        //addField(START_DATE, startDate, getTitle(wfmStrings.startDate()));
        //addField(DUE_DATE, dueDate, getTitle(wfmStrings.dueDateField()));
        // Task StartDate
        dateTime.startDate.ensureDebugId(test_code_ID_name.concat("startDate"));
        dateTime.startTime.ensureDebugId(test_code_ID_name.concat("startTime"));
        //dateTime.startDate.addValueChangeHandler(handler);
        //dateTime.startTime.getListBox().addChangeHandler(handler);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.START_DATE) != null) {
            addField(CustomFormConstants.START_DATE, dateTime.startDate, getTitle(formPropertyMap.get(CustomFormConstants.START_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.START_DATE).getTitle() : wfmStrings.startDate()));
        } else {
            addField(START_DATE, new InputGroup(dateTime.startDate, dateTime.startTime), getTitle(wfmStrings.startDate(), true));
        }
        //Task Due Date
        dateTime.endTime.ensureDebugId(test_code_ID_name.concat("endTime"));
        dateTime.dueDate.ensureDebugId(test_code_ID_name.concat("dueDate"));
        dateTime.allDay.ensureDebugId(test_code_ID_name.concat("allDay"));
        //dateTime.dueDate.addValueChangeHandler(handler);
        //dateTime.endTime.getListBox().addChangeHandler(handler);
        //dateTime.allDay.addValueChangeHandler(handler);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DUE_DATE) != null) {
            addField(CustomFormConstants.DUE_DATE, dateTime.dueDate, getTitle(formPropertyMap.get(CustomFormConstants.DUE_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.DUE_DATE).getTitle() : wfmStrings.dueDate()));
        } else {
            addField(DUE_DATE, new AdvancedInputGroup(new InputGroup(dateTime.dueDate, dateTime.endTime), dateTime.allDay), getTitle(wfmStrings.dueDate(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PRIORITY) != null) {
            addField(CustomFormConstants.PRIORITY, priority, getTitle(formPropertyMap.get(CustomFormConstants.PRIORITY).isChanged() ? formPropertyMap.get(CustomFormConstants.PRIORITY).getTitle() : wfmStrings.priority()));
        } else {
            addField(PRIORITY, priority, getTitle(wfmStrings.priority()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TYPE) != null) {
            addField(CustomFormConstants.TYPE, type, getTitle(formPropertyMap.get(CustomFormConstants.TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.TYPE).getTitle() : wfmStrings.type()));
        } else {
            addField(TYPE, type, getTitle(wfmStrings.type()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null) {
            addField(CustomFormConstants.STATUS, status, getTitle(formPropertyMap.get(CustomFormConstants.STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.STATUS).getTitle() : wfmStrings.status()));
        } else {
            addField(STATUS, status, getTitle(wfmStrings.status()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.PERCENT) != null) {
            addField(CustomFormConstants.TASK.PERCENT, completed, getTitle(formPropertyMap.get(CustomFormConstants.TASK.PERCENT).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.PERCENT).getTitle() : wfmStrings.percent()));
        } else {
            addField(CustomFormConstants.TASK.PERCENT, completed, getTitle(wfmStrings.percent()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.BILLIBLE) != null) {
            addField(CustomFormConstants.TASK.BILLIBLE, billable, getTitle(formPropertyMap.get(CustomFormConstants.TASK.BILLIBLE).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.BILLIBLE).getTitle() : wfmStrings.billable()));
        } else {
            addField(CustomFormConstants.TASK.BILLIBLE, billable, getTitle(wfmStrings.billable()));
        }

        addField(CustomFormConstants.ASSIGNEE, dataGrid, null);
        //Dependencies
        addTitleField(CustomFormConstants.TASK.DEPENDENCIES, getTitle(property.getSingular(wfmStrings.taskDependencies(), wfmStrings.task())));
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.PARENT_WORKSTREAM) != null) {
            addField(CustomFormConstants.TASK.PARENT_WORKSTREAM, parentWorkstream, getTitle(formPropertyMap.get(CustomFormConstants.TASK.PARENT_WORKSTREAM).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.PARENT_WORKSTREAM).getTitle() : wfmStrings.workStream()));
        } else {
            addField(CustomFormConstants.TASK.PARENT_WORKSTREAM, parentWorkstream, getTitle(wfmStrings.workStream()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.DUE_DATE_REMINDER) != null) {
            addField(CustomFormConstants.TASK.DUE_DATE_REMINDER, dueDateReminder, getTitle(formPropertyMap.get(CustomFormConstants.TASK.DUE_DATE_REMINDER).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.DUE_DATE_REMINDER).getTitle() : wfmStrings.duedatereminder()));
        } else {
            addField(CustomFormConstants.TASK.DUE_DATE_REMINDER, dueDateReminder, getTitle(wfmStrings.duedatereminder()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.PREDECESSOR_TASK) != null) {
            addField(CustomFormConstants.TASK.PREDECESSOR_TASK, predecessorTask, getTitle(formPropertyMap.get(CustomFormConstants.TASK.PREDECESSOR_TASK).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.PREDECESSOR_TASK).getTitle() : wfmStrings.predeccessor()));
        } else {
            addField(CustomFormConstants.TASK.PREDECESSOR_TASK, predecessorTask, getTitle(wfmStrings.predeccessor()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.SUCCESSOR_TASK) != null) {
            addField(CustomFormConstants.TASK.SUCCESSOR_TASK, successorTasks, getTitle(formPropertyMap.get(CustomFormConstants.TASK.SUCCESSOR_TASK).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.SUCCESSOR_TASK).getTitle() : wfmStrings.successor()));
        } else {
            addField(CustomFormConstants.TASK.SUCCESSOR_TASK, successorTasks, getTitle(wfmStrings.successor()));
        }

        //More details
        if (!Utils.hasRole(CLIENT)) {
            addTitleField(CustomFormConstants.MORE_DETAILS, getTitle(wfmStrings.moreDetails()));
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.SUCCESSOR_TASK) != null) {
                addField(CustomFormConstants.TASK.SUCCESSOR_TASK, successorTasks, getTitle(formPropertyMap.get(CustomFormConstants.TASK.SUCCESSOR_TASK).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.SUCCESSOR_TASK).getTitle() : wfmStrings.successor()));
            } else {
                addField(CustomFormConstants.TASK.SUCCESSOR_TASK, successorTasks, getTitle(wfmStrings.successor()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.PM) != null) {
                addField(CustomFormConstants.TASK.PM, pManager, getTitle(formPropertyMap.get(CustomFormConstants.TASK.PM).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.PM).getTitle() : wfmStrings.projectManager()));
            } else {
                addField(CustomFormConstants.TASK.PM, pManager, getTitle(wfmStrings.projectManager()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.BACKUP_MANAGERS) != null) {
                addField(CustomFormConstants.TASK.BACKUP_MANAGERS, bManager, getTitle(formPropertyMap.get(CustomFormConstants.TASK.BACKUP_MANAGERS).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.BACKUP_MANAGERS).getTitle() : wfmStrings.backupManagers()));
            } else {
                addField(CustomFormConstants.TASK.BACKUP_MANAGERS, bManager, getTitle(wfmStrings.backupManagers()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.CLIENT) != null) {
                addField(CustomFormConstants.TASK.CLIENT, client, getTitle(formPropertyMap.get(CustomFormConstants.TASK.CLIENT).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.CLIENT).getTitle() : wfmStrings.customer()));
            } else {
                addField(CustomFormConstants.TASK.CLIENT, client, getTitle(Property.get(Constants.CLIENT_LIST, wfmStrings.customer())));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.ESTIMATED_TIME) != null) {
                addField(CustomFormConstants.TASK.ESTIMATED_TIME, estimatedTime, getTitle(formPropertyMap.get(CustomFormConstants.TASK.ESTIMATED_TIME).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.ESTIMATED_TIME).getTitle() : wfmStrings.estimatedTime()));
            } else {
                addField(CustomFormConstants.TASK.ESTIMATED_TIME, estimatedTime, getTitle(wfmStrings.estimatedTime()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.TIME_SPENT) != null) {
                addField(CustomFormConstants.TASK.TIME_SPENT, timeSpent, getTitle(formPropertyMap.get(CustomFormConstants.TASK.TIME_SPENT).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.TIME_SPENT).getTitle() : wfmStrings.timeSpentOnly()));
            } else {
                addField(CustomFormConstants.TASK.TIME_SPENT, timeSpent, getTitle(wfmStrings.timeSpentOnly()));
            }

            if (Utils.hasRole(Constants.DR) || Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.PM)) {
                if (Utils.hasPermission(PermissionConstants.PM_TASKS_VIEW_PROJECT_COST)) {
                    if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.ESTIMATED_COST) != null) {
                        addField(CustomFormConstants.TASK.ESTIMATED_COST, estimatedCost, getTitle(formPropertyMap.get(CustomFormConstants.TASK.ESTIMATED_COST).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.ESTIMATED_COST).getTitle() : wfmStrings.costEstimated()));
                    } else {
                        addField(CustomFormConstants.TASK.ESTIMATED_COST, estimatedCost, getTitle(wfmStrings.costEstimated()));
                    }

                    if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.ACTUAL_COST) != null) {
                        addField(CustomFormConstants.TASK.ACTUAL_COST, actualCost, getTitle(formPropertyMap.get(CustomFormConstants.TASK.ACTUAL_COST).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.ACTUAL_COST).getTitle() : wfmStrings.actualCost()));
                    } else {
                        addField(CustomFormConstants.TASK.ACTUAL_COST, actualCost, getTitle(wfmStrings.actualCost()));
                    }
                }
            }
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.ACTUAL_START_DATE) != null) {
                addField(CustomFormConstants.TASK.ACTUAL_START_DATE, actualStartDate, getTitle(formPropertyMap.get(CustomFormConstants.TASK.ACTUAL_START_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.ACTUAL_START_DATE).getTitle() : wfmStrings.actualStartDate()));
            } else {
                addField(CustomFormConstants.TASK.ACTUAL_START_DATE, actualStartDate, getTitle(wfmStrings.actualStartDate()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.ACTUAL_END_DATE) != null) {
                addField(CustomFormConstants.TASK.ACTUAL_END_DATE, actualEndDate, getTitle(formPropertyMap.get(CustomFormConstants.TASK.ACTUAL_END_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.ACTUAL_END_DATE).getTitle() : wfmStrings.actualEndDate()));
            } else {
                addField(CustomFormConstants.TASK.ACTUAL_END_DATE, actualEndDate, getTitle(wfmStrings.actualEndDate()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.ACTUAL_TIME_SPENT) != null) {
                addField(CustomFormConstants.TASK.ACTUAL_TIME_SPENT, actualTimeSpent, getTitle(formPropertyMap.get(CustomFormConstants.TASK.ACTUAL_TIME_SPENT).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.ACTUAL_TIME_SPENT).getTitle() : wfmStrings.actualTimeSpent()));
            } else {
                addField(CustomFormConstants.TASK.ACTUAL_TIME_SPENT, actualTimeSpent, getTitle(wfmStrings.actualTimeSpent()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.WAITING_HOURS) != null) {
                addField(CustomFormConstants.TASK.WAITING_HOURS, waitingHours, getTitle(formPropertyMap.get(CustomFormConstants.TASK.WAITING_HOURS).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.WAITING_HOURS).getTitle() : wfmStrings.waitingHours()));
            } else {
                addField(CustomFormConstants.TASK.WAITING_HOURS, waitingHours, getTitle(wfmStrings.waitingHours()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.REJECTED_HOURS) != null) {
                addField(CustomFormConstants.TASK.REJECTED_HOURS, rejectedHours, getTitle(formPropertyMap.get(CustomFormConstants.TASK.REJECTED_HOURS).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.REJECTED_HOURS).getTitle() : projectStrings.rejectedHours()));
            } else {
                addField(CustomFormConstants.TASK.REJECTED_HOURS, rejectedHours, getTitle(projectStrings.rejectedHours()));
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.CREATED_BY) != null) {
            addField(CustomFormConstants.TASK.CREATED_BY, createdBy, getTitle(formPropertyMap.get(CustomFormConstants.TASK.CREATED_BY).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.CREATED_BY).getTitle() : wfmStrings.createdBy()));
        } else {
            addField(CustomFormConstants.TASK.CREATED_BY, createdBy, getTitle(wfmStrings.createdBy()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.CREATED_DATE) != null) {
            addField(CustomFormConstants.TASK.CREATED_DATE, createdDate, getTitle(formPropertyMap.get(CustomFormConstants.TASK.CREATED_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.CREATED_DATE).getTitle() : wfmStrings.createdDate()));
        } else {
            addField(CustomFormConstants.TASK.CREATED_DATE, createdDate, getTitle(wfmStrings.createdDate()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.UPDATED_BY) != null) {
            addField(CustomFormConstants.TASK.UPDATED_BY, updatedBy, getTitle(formPropertyMap.get(CustomFormConstants.TASK.UPDATED_BY).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.UPDATED_BY).getTitle() : wfmStrings.modifiedBy()));
        } else {
            addField(CustomFormConstants.TASK.UPDATED_BY, updatedBy, getTitle(wfmStrings.modifiedBy()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.UPDATED_DATE) != null) {
            addField(CustomFormConstants.TASK.UPDATED_DATE, updatedDate, getTitle(formPropertyMap.get(CustomFormConstants.TASK.UPDATED_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.UPDATED_DATE).getTitle() : wfmStrings.modifiedDate()));
        } else {
            addField(CustomFormConstants.TASK.UPDATED_DATE, updatedDate, getTitle(wfmStrings.modifiedDate()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.TASK_NOTE) != null) {
            addField(CustomFormConstants.TASK.TASK_NOTE, noteWidget, getTitle(formPropertyMap.get(CustomFormConstants.TASK.TASK_NOTE).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.TASK_NOTE).getTitle() : wfmStrings.notes()));
        } else {
            addField(CustomFormConstants.TASK.TASK_NOTE, noteWidget, wfmStrings.notes(), true);
        }
        addField(CustomFormConstants.TASK.TASK_STATUS_HISTORY, taskStatusHistoryGrid, null, true);

        addTitleField(ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        show();
    }

    private void refreshAssigeesStatus() {
        LoadingPanel.loading(true);
        taskService.getTask(taskID, Utils.isCRM(), new AbstractAsyncCallback<TaskSingleItem>() {
            @Override
            public void failure(Throwable caught) {
                caught.getMessage();
                LoadingPanel.loading(false);
            }

            @Override
            public void success(TaskSingleItem item) {
                Scheduler.get().scheduleDeferred(() -> {
                    if (item.getIssueEmployees() != null && item.getIssueEmployees().length > 0) {
                        initDataProviderApply(item.getIssueEmployees());
                        dataGrid.refresh();
                    }
                });
                LoadingPanel.loading(false);
            }
        });
    }

    @Override
    protected void initPredefinedValues() {
    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            Localize.getInstance().localizeByFieldID(getFormID(), fieldID);
        }
        return null;
    }

    @Override
    protected void addButtons() {
        customizeButton.setVisible(false);

        link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);
        footer.addToLeftSide(link);

        link.addClickHandler(event -> {
            if (firstClick.get()) {
                getLinkingUtil().getAddLinkSideNavBox();
                getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(item.getRelations(), false);

                firstClick.set(false);
            } else {
                getLinkingUtil().getAddLinkSideNavBox().show();
            }
        });

        if (Utils.hasPermission(!Utils.isCRM() ? PermissionConstants.PM_TASKS_REMOVE : PermissionConstants.CRM_TASKS_REMOVE)) {
            MaterialDropDown options = addMoreSplitButton(wfmStrings.options());
            if (Utils.hasRole(Constants.ADMIN)) {
                MaterialLink customize = new MaterialLink(wfmStrings.customize());
                customize.addClickHandler(click -> {
                    String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
                    SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + (url != null ? URL.encodeQueryString(url) : ""));
                });
                options.add(customize);
            }

            if (Utils.hasPermission(!Utils.isCRM() ? PermissionConstants.PM_TASKS_REMOVE : PermissionConstants.CRM_TASKS_REMOVE)) {
                MaterialLink delete = new MaterialLink(wfmStrings.delete());
                delete.addClickHandler(event -> {
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    message.setTitle(property.getSingular(projectStrings.deleteTask(), wfmStrings.task()));
                    message.setMessage(wfmStrings.sureYouWantToDelete());
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            taskService.deleteTask(taskID, Utils.isFromCRM() ? PermissionConstants.CRM_CONTEXT : PermissionConstants.PM_CONTEXT, new AbstractAsyncCallback<String>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void success(String result) {
                                    LoadingPanel.loading(false);
                                    if (PermissionConstants.ALLOW.equals(result)) {
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_DELETE, result, TaskSummaryView.this);
                                    } else {
                                        Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                                    }
                                    Info.show(Property.get(Constants.TASK, wfmStrings.messSuccessfulyyDeleted(), wfmStrings.task()), Info.Type.INFO);
                                    closeTab();
                                }
                            });
                        }
                    });
                    message.open();
                });
                options.add(delete);
            }
        }

//        if (!(Utils.hasRole(CLIENT) || Utils.hasUserMaxRoleID(MEM))) {
        if (!Utils.hasUserMaxRoleID(MEM)) {
            ExportPdfButton pdf = new ExportPdfButton(new ExportPdfButton.PdfRequestInterface() {
                @Override
                public String getUrl() {
                    return "/taskViewPDFHandler";
                }

                @Override
                public boolean isLandscapeOptionEnabled() {
                    return true;
                }

                @Override
                public HashMap<String, String> getParameters() {
                    RequestObject requestObject = new RequestObject(taskID);
                    return requestObject.getRequestParams();
                }
            });
            addRightButton(pdf);
        }

        if (Utils.hasPermission(!Utils.isCRM() ? PermissionConstants.PM_TASKS_EDIT : PermissionConstants.CRM_TASKS_EDIT)) {
            addButton(wfmStrings.edit(), WfmButton2.BTN_PRIMARY, event -> goTo("task|edit/" + taskID));
        }


    }

    private void showButtons() {
        if (Utils.hasGenericAccess(GenericSettingsEnum.SHOW_TIMER) && Utils.hasPermission(!Utils.isCRM() ? PermissionConstants.PM_TASKS_TIMER : PermissionConstants.CRM_TASKS_TIMER)) {
            if (item.isShowTimer()) {
                if (!item.getAtLeastOneTimerIsRunning() ||
                        "true".equals(Utils.userSettings.get(ENABLE_MULTIPLE_TIMER_INTSTANCES)) ||
                        ("false".equals(Utils.userSettings.get(ENABLE_MULTIPLE_TIMER_INTSTANCES)) && item.isTimerIsStarted())) {
                    WfmButton2 button = new WfmButton2(wfmStrings.timer(), WfmButton2.BTN_DEFAULT);
                    button.ensureDebugId(test_code_ID_name + "Timer_Button");
                    button.setTitle(wfmStrings.timer());
                    button.addClickHandler(clickEvent -> {
                        MainLayout.get().setTimerData(item.getObjectID(), Constants.PM_TASK, item.getProjectID());
                    });
                    addButton(TIMER_BUTTON, button);
                }
            }
        }
        if (item.isShowLogTime()) {
            WfmButton2 button = new WfmButton2(wfmStrings.logTime(), WfmButton2.BTN_SUCCESS);
            button.setTitle(wfmStrings.save());
            button.ensureDebugId(test_code_ID_name + "LogAtime");
            button.addClickHandler(clickEvent -> new TaskLogToTimeSheetPopup(taskID));
            addButton(LOG_TIME_BUTTON, button);
        }
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        taskService.getTask(taskID, Utils.isCRM(), new AbstractAsyncCallback<TaskSingleItem>() {
            @Override
            public void failure(Throwable caught) {
                caught.getMessage();
                LoadingPanel.loading(false);
            }

            @Override
            public void success(final TaskSingleItem o) {
                Scheduler.get().scheduleDeferred(() -> {
                    item = o;
                    LoadingPanel.loading(false);
                    if (item != null && item.getRelations() != null) {
                        link.setBadgeCount(item.getRelations().size());
                    }
                    drawTaskSummaryView();
                    showButtons();
                    initTaskAssigneesTableColumns();

                    taskListItem = new TaskListItem();
                    taskListItem.setObjectID(item.getObjectID());
                    taskListItem.setTaskStatusId(item.getStatusID());
                    taskListItem.setPriorityId(item.getPriorityID());
                    taskListItem.setStartDate(item.getStartDate());
                    taskListItem.setEndDate(item.getEndDate());
                    taskListItem.setDueDate(item.getDueDate());
                    taskListItem.setAllDay(item.isAllDay());

                    status.getListBox().setItems(item.getTaskStatuses());
                    status.getListBox().setAllowFirstItem(true);
                    addPredefinedValues(STATUS, item.getTaskStatuses());
                    if (item.getStatusID() != null) {
                        status.getListBox().setSelected(item.getStatusID());
                    }
                });
            }
        });
    }

    private void drawTaskSummaryView() {

        boolean hasAccess = !Utils.isLockCompletedProjecItems() || !PS_CLOSED.equals(item.getProjectStatusCode());
        //boolean taskEditPermission = Utils.hasRoles(DR, ADMIN, PM, HR, TL);
        boolean taskEditPermission = Utils.hasPermission(Utils.isCRM() ? PermissionConstants.CRM_TASKS_EDIT : PermissionConstants.PM_TASKS_EDIT);
        if (hasAccess && taskEditPermission) {
            priority.setEnabled(true);
            status.getListBox().setEnabled(true);
            dateTime.setEnabled(true);
            dateTime.sinkEvents(Event.FOCUSEVENTS);
        }

        if (item.getObjectID() != null) {
            Utils.registrRelation(item);
        }

        if (item.getNumberData() != null) {
            number.setHTML(item.getNumberData().getNumberString());
        } else {
            number.setHTML(wfmStrings.notAvailable());
        }
        taskName.setHTML(SimpleHtmlSanitizer.sanitizeHtml(item.getName()).asString());

        Widget projectLink = null;
        if (Utils.isFromCRM()) {
            projectLink = new HTML(item.getProjectName());
        } else if (Utils.hasPermission(PM_PROJECT_LIST) && !item.isSupplier()) {
            projectLink = new SimpleLink(item.getProjectName(), "project|summary/" + item.getProjectID() + "/" + null, item.getProjectName(), item.getNumberData().getLastNumberString());
//            projectLink = new SimpleLink(item.getProjectName(), "project|summary/" + item.getProjectID(), item.getProjectName(), item.getNumberData().getLastNumberString());
        } else {
            projectLink = new HTML(item.getProjectName());
        }
        projectName.add(projectLink);

        pManager.setHTML(item.getProjectManager() != null ? item.getProjectManager() : wfmStrings.notAvailable());
        FlexTable backupManagerLinks = new FlexTable();
        int i = 0;
        for (final SelectItem backupManager : item.getBackupManagers()) {
            HTML backupManagerHTML = new HTML();
            backupManagerHTML.setHTML(backupManager.getName());
            backupManagerLinks.setWidget(i, 0, backupManagerHTML);
            i++;
        }
        bManager.setHTML(item.getBackupManagers().size() > 0 ? backupManagerLinks.toString() : wfmStrings.notAvailable());

        description.setReadOnly(true);
        if (!Utils.isNullOrEmpty(item.getDescription())) {
            description.setText(item.getDescription().replace("\r\n", "\\r\\n"));
        }

        client.setHTML(item.getClientName());
        type.setHTML(item.getTypeName());

        priority.setItems(item.getPriority());
        priority.setSelected(item.getPriorityID());
        priority.addValueChangeHandler(valueChangeEvent -> onChangeField(TaskListItem.PRIORITY_NAME));

        if (item.getPercent() == null || Double.valueOf(item.getPercent()) == 0.0) {
            completed.setHTML("0.00%");
        } else if (Double.valueOf(item.getPercent()) > 100) {
            completed.setHTML("<p style='color: red'>" + item.getPercent() + "%</p>");
        } else {
            completed.setHTML(formatToDouble(String.valueOf(item.getPercent())) + "%");
        }

        billable.setHTML(item.getBillable() ? wfmStrings.yes() : wfmStrings.no());

        PositionsSelectItem[] issueEmployees = item.getIssueEmployees();
        if (issueEmployees != null && issueEmployees.length > 0) {
            initDataProviderApply(issueEmployees);
        }

        getDependencies();

        moreDetails();

        dataProvider.refresh();
        if (item.getProjectID() != null) {
            GeneralFileUpload fileUpload = new GeneralFileUpload(F_TASK, item.getProjectID(), item.getObjectID());
            addField(ATTACHMENTS, fileUpload, wfmStrings.attachments(), true);
        }

        createdBy.setHTML(item.getTaskCreator());
        createdDate.setHTML(DateUtils.dateFormatWithHour(item.getTaskCreationTime()));
        updatedBy.setHTML(item.getLastModifiedBy());
        updatedDate.setHTML(DateUtils.dateFormatWithHour(item.getLastModified()));

        //task custom fields
        if (taskID != null) {
            getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFieldItems(), true);
        }
    }

    private void onChangeField(String field) {
        switch (field) {
            case TaskListItem.PRIORITY_NAME:
                taskListItem.setPriorityId(priority.getSelectedId());
                break;
            case TaskListItem.BOTH_DATE:
                if (!validate()) {
                    return;
                }
                try {
                    String value;
                    if (dateTime.getAllDayCheckBox().getValue()) {
                        value = DateUtils.format(dateTime.getStartDate());
                    } else {
                        value = DateUtils.formatInternal(dateTime.getStartDate());
                    }
                    if ((value.contains("AM") || value.contains("PM") || value.contains(":"))
                            && dateTime.getDueDate() != null && dateTime.getDueDate().getTime() >= DateUtils.parseLongFormat(value).getTime()) {
                        taskListItem.setStartDate(DateUtils.parseLongFormat(value));
                        taskListItem.setAllDay(false);
                    } else if (!value.contains("AM") && !value.contains("PM") && !value.contains(":")
                            && dateTime.getDueDate() != null && dateTime.getDueDate().getTime() >= DateUtils.parse(value).getTime()) {
                        taskListItem.setStartDate(DateUtils.parse(value));
                        taskListItem.setAllDay(true);
                    } else {
                        Info.show(projectStrings.endDateCanNotBeBeforeStartDate(), Info.Type.WARNING);
                        return;
                    }
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }

                try {
                    String value;
                    if (dateTime.getAllDayCheckBox().getValue()) {
                        value = DateUtils.format(dateTime.getDueDate());
                    } else {
                        value = DateUtils.formatInternal(dateTime.getDueDate());
                    }
                    if ((value.contains("AM") || value.contains("PM") || value.contains(":"))
                            && dateTime.getStartDate() != null && dateTime.getStartDate().getTime() <= DateUtils.parseLongFormat(value).getTime() + 5 * 60 * 60 * 1000) {//add 5 hours hack
                        taskListItem.setDueDate(DateUtils.parseLongFormat(value));
                        taskListItem.setAllDay(false);
                    } else if (!value.contains("AM") && !value.contains("PM") && !value.contains(":")
                            && dateTime.getStartDate() != null && dateTime.getStartDate().getTime() <= DateUtils.parse(value).getTime()) {
                        taskListItem.setDueDate(DateUtils.parse(value));
                        taskListItem.setAllDay(true);
                    } else {
                        Info.show(projectStrings.endDateCanNotBeBeforeStartDate(), Info.Type.WARNING);
                        return;
                    }
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
                break;
            default:
                return;
        }

        LoadingPanel.loading(true);
        taskService.saveTaskEditCellValue(taskListItem, field, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Boolean result) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_EDIT, result, TaskSummaryView.this);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DASHBOARD_TASK_REFRESH, result, TaskSummaryView.this);
            }
        });

    }

    private boolean validate() {
        int errors = 0;
        if (dateTime.getStartDate() != null && dateTime.getDueDate() != null) {
            errors += markAsError(START_DATE, dateTime.startDate, !Validation.validateDateOrder(dateTime.getStartDate(), dateTime.getDueDate(), null, dateTime.isAllDay()));
        } else {
            if (dateTime.getStartDate() == null) {
                errors += markAsError(START_DATE, dateTime.startDate, true);
            } else if (dateTime.getDueDate() == null) {
                errors += markAsError(DUE_DATE, dateTime.dueDate, true);
            }
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void moreDetails() {
        //String start;
        //String due;
        String actualStart = DateUtils.format(item.getActualStartDate());
        String actualEnd = DateUtils.format(item.getActualEndDate());
        /*if (item.isAllDay()) {
            start = DateUtils.format(item.getStartDate());
            Date dueDate = item.getDueDate();
            due = DateUtils.format(dueDate);
        } else {
            start = DateUtils.formatInternal(item.getStartDate());
            due = DateUtils.formatInternal(item.getDueDate());
        }*/
        if (Utils.hasPermission(PermissionConstants.PM_TASKS_VIEW_PROJECT_COST)) {
            estimatedCost.setHTML(item.getEstimatedCost());
            actualCost.setHTML(item.getActualCost());
        }

        //startDate.setHTML(start);
        //dueDate.setHTML(due);

        if (item.isAllDay() != null && item.isAllDay()) {
            dateTime.startTime.setVisible(false);
            dateTime.endTime.setVisible(false);
            dateTime.allDay.setValue(true, true);
        } else {
            dateTime.startTime.setVisible(true);
            dateTime.endTime.setVisible(true);
            dateTime.allDay.setValue(false, true);
        }
        dateTime.setStartDate(item.getStartDate());
        dateTime.getStartDatePicker().setMonth(item.getStartDate());
        dateTime.setStartTime(new StartEndTime(timeFormat.format(item.getStartDate())).time);
        if (item.isAllDay() != null && item.isAllDay()) {
            Date dueDate = item.getDueDate();
            dateTime.setDueDate(dueDate);
        } else {
            dateTime.setDueDate(item.getDueDate());
        }
        dateTime.getDueDatePicker().setMonth(item.getDueDate());
        dateTime.setEndTime(new StartEndTime(timeFormat.format(item.getDueDate())).time);

        dateTime.startDate.addValueChangeHandler(valueChangeEvent -> onChangeField(TaskListItem.BOTH_DATE));
        dateTime.dueDate.addValueChangeHandler(valueChangeEvent -> onChangeField(TaskListItem.BOTH_DATE));

        dateTime.startTime.addValueChangeHandler(valueChangeEvent -> onChangeField(TaskListItem.BOTH_DATE));
        dateTime.startTime.getListBox().addChangeHandler(valueChangeEvent -> onChangeField(TaskListItem.BOTH_DATE));
        dateTime.endTime.addValueChangeHandler(valueChangeEvent -> onChangeField(TaskListItem.BOTH_DATE));
        dateTime.endTime.getListBox().addChangeHandler(valueChangeEvent -> onChangeField(TaskListItem.BOTH_DATE));

        if (!Utils.hasRole(Constants.CLIENT)) {
            estimatedTime.setHTML(Utils.formatMinutes(item.getEstimatedTime()));
        }
        if (!Utils.hasRole(Constants.CLIENT)) {
            timeSpent.setHTML(Utils.formatMinutes(item.getTimeSpent()));
        }

        actualStartDate.setHTML(actualStart);
        actualEndDate.setHTML(actualEnd);
        if (!Utils.hasRole(Constants.CLIENT)) {
            actualTimeSpent.setHTML(Utils.formatMinutes(item.getActualTime()));
            waitingHours.setHTML(item.getWaitingHours());
            rejectedHours.setHTML(item.getRejectedHours());
        }
    }

    private void getDependencies() {
        SimpleLink workstreamViewLink = new SimpleLink((item.getWorkstreamName() != null ? item.getWorkstreamName() : wfmStrings.notAvailable()), "workstream|summary/" + (item.getWorkstreamID() != null ? item.getWorkstreamID().toString() : wfmStrings.notAvailable()), item.getWorkstreamName(), item.getNumberData().getFirstNumberString());
        int rows = item.getPredecessorTasks().length / 3 + 1;
        Grid predTasks = new Grid(rows, 3);
        for (int i = 0; i < item.getPredecessorTasks().length; i++) {
            int r = i / 3;
            int c = i % 3;
            if (item.getPredecessorTasks()[i] != null) {
                if (i != item.getPredecessorTasks().length - 1) {
                    HorizontalPanel hp = new HorizontalPanel();
                    hp.add(new SimpleLink(item.getPredecessorTasks()[i].getName(), "task/" + item.getPredecessorTasks()[i].getId().toString()));
                    hp.add(new HTML(", "));
                    predTasks.setWidget(r, c, hp);
                } else {
                    predTasks.setWidget(r, c, new SimpleLink(item.getPredecessorTasks()[i].getName(), "task/" + item.getPredecessorTasks()[i].getId().toString()));
                }
            }
        }

        rows = item.getSuccessorTasks().length / 3 + 1;
        Grid successTasks = new Grid(rows, 3);
        for (int i = 0; i < item.getSuccessorTasks().length; i++) {
            int r = i / 3;
            int c = i % 3;
            if (item.getSuccessorTasks()[i] != null) {
                if (i != item.getSuccessorTasks().length - 1) {
                    HorizontalPanel hp = new HorizontalPanel();
                    hp.add(new SimpleLink(item.getSuccessorTasks()[i].getName(), "task/" + item.getSuccessorTasks()[i].getId().toString()));
                    hp.add(new HTML(", "));
                    successTasks.setWidget(r, c, hp);
                } else {
                    successTasks.setWidget(r, c, new SimpleLink(item.getSuccessorTasks()[i].getName(), "task/" + item.getSuccessorTasks()[i].getId().toString()));
                }
            }
        }

        if (item.getWorkstreamName() != null) {
            parentWorkstream.add(workstreamViewLink);
        } else {
            parentWorkstream.add(new HTML(wfmStrings.notAvailable()));
        }
        if (item.getPredecessorTasks() != null && item.getPredecessorTasks().length > 0) {
            predecessorTask.add(predTasks);
        } else {
            predecessorTask.add(new HTML(wfmStrings.notAvailable()));
        }
        if (item.getSuccessorTasks() != null && item.getSuccessorTasks().length > 0) {
            successorTasks.add(successTasks);
        } else {
            successorTasks.add(new HTML(wfmStrings.notAvailable()));
        }
        if (item.getReminder() != null && !item.getReminder().isEmpty()) {
            for (CalendarEventReminder eventReminder : item.getReminder()) {
                HTML value = new HTML(String.valueOf(Reminder.getReminderTimesMap().get(eventReminder.getReminderTimes())));
                value.setWidth("100px");
                dueDateReminder.add(new AdvancedInputGroup(new HTML(Reminder.getReminderTypesMap().get(eventReminder.getValue())), value));
            }
        } else {
            dueDateReminder.add(new HTML(wfmStrings.notAvailable()));
        }
    }

    private void initTaskAssigneesTableColumns() {
        final boolean canSee = item.getPermission() == EDIT;
        //employee name
        Column<PositionsSelectItem, String> employee = new Column<PositionsSelectItem, String>(new TextCell()) {
            @Override
            public String getValue(PositionsSelectItem object) {
                return (object.getEmployeeNumber() != null ? object.getEmployeeNumber() + " - " : "") + (object.getName() != null ? object.getName() : "");
            }
        };
        employee.setSortable(true);
        dataGrid.addColumn(employee, wfmStrings.employee());
        dataGrid.setColumnWidth(employee, 30, com.google.gwt.dom.client.Style.Unit.PCT);
        listHandler.setComparator(employee, (o1, o2) -> o2.getName().compareToIgnoreCase(o1.getName()));
        // department Name
        Column<PositionsSelectItem, String> department = new Column<PositionsSelectItem, String>(new TextCell()) {
            @Override
            public String getValue(PositionsSelectItem object) {
                return object.getDepartmentName();
            }
        };
        dataGrid.addColumn(department, Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department(), wfmStrings.department()));
        dataGrid.setColumnWidth(department, 30, com.google.gwt.dom.client.Style.Unit.PCT);

        //employee task status
        Column<PositionsSelectItem, String> employeeTaskStatus = new Column<PositionsSelectItem, String>(new TextCell()) {
            @Override
            public String getValue(PositionsSelectItem object) {
                return object.getStatusName();
            }
        };
        dataGrid.addColumn(employeeTaskStatus, property.getSingular(wfmStrings.status(), wfmStrings.task()));
        dataGrid.setColumnWidth(employeeTaskStatus, 15, com.google.gwt.dom.client.Style.Unit.PCT);


        if (!Utils.hasRole(CLIENT)) {
            //estimate time
            Column<PositionsSelectItem, String> estimatedTime = new Column<PositionsSelectItem, String>(new TextCell()) {
                @Override
                public String getValue(PositionsSelectItem object) {
                    if (canSee || Utils.getUserID().equals(object.getEmployeeId())) {
                        return Utils.formatMinutes(object.getTime());
                    } else {
                        return "";
                    }
                }
            };
            estimatedTime.setSortable(true);
            dataGrid.addColumn(estimatedTime, wfmStrings.estimatedTime());
            dataGrid.setColumnWidth(estimatedTime, 12, com.google.gwt.dom.client.Style.Unit.PCT);
            listHandler.setComparator(estimatedTime, (o1, o2) -> Utils.formatMinutes(o1.getTime()).compareToIgnoreCase(Utils.formatMinutes(o2.getTime())));

            Column<PositionsSelectItem, String> timeSpent = new Column<PositionsSelectItem, String>(new TextCell()) {
                @Override
                public String getValue(PositionsSelectItem object) {
                    if (canSee || Utils.getUserID().equals(object.getEmployeeId())) {
                        return Utils.formatMinutes(object.getTimeSpent());
                    } else {
                        return "";
                    }
                }
            };
            dataGrid.addColumn(timeSpent, wfmStrings.timeSpentOnly());
            dataGrid.setColumnWidth(timeSpent, 12, com.google.gwt.dom.client.Style.Unit.PCT);

            //actual time
//        if (item.isShowInTimesheet()) {
            Column<PositionsSelectItem, String> actualTime = new Column<PositionsSelectItem, String>(new TextCell()) {
                @Override
                public String getValue(PositionsSelectItem object) {
                    if (canSee || Utils.getUserID().equals(object.getEmployeeId())) {
                        return Utils.formatMinutes(object.getActualTime());
                    } else {
                        return "";
                    }
                }
            };
            dataGrid.addColumn(actualTime, wfmStrings.actualTime());
            dataGrid.setColumnWidth(actualTime, 12, com.google.gwt.dom.client.Style.Unit.PCT);

            //percentage
            Column<PositionsSelectItem, String> percent = new Column<PositionsSelectItem, String>(new TextCell()) {
                @Override
                public String getValue(final PositionsSelectItem object) {
                    String result = "";
                    if (canSee || Utils.getUserID().equals(object.getEmployeeId())) {
                        result = object.getPercent() != null ? object.getPercent().toString() : "0.00";
                        if (object.getPercent() == null || object.getPercent() == 0) {
                            result = "0.00%";
                        } else {
                            result = formatToDouble(result) + "%";
                        }
                    }
                    return result;
                }
            };
            dataGrid.addColumn(percent, wfmStrings.percentCompleted());
            dataGrid.setColumnWidth(percent, 12, com.google.gwt.dom.client.Style.Unit.PCT);
        }
    }

    private void initDataProviderApply(PositionsSelectItem[] issueEmployees) {
        List<PositionsSelectItem> employeeItems = dataProvider.getList();
        employeeItems.clear();
        dataGrid.setPageSize(issueEmployees.length);
        Collections.addAll(employeeItems, issueEmployees);
    }

    private String formatToDouble(String text) {
        return numberFormat.format(parseToDouble(text));
    }

    private double parseToDouble(String text) {
        return Double.parseDouble(text.replace(",", ""));
    }

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(TaskSummaryView.this) {
                @Override
                protected boolean isActionEditing() {
                    return false;
                }

                @Override
                public Integer getRelationID() {
                    return taskID;
                }

                @Override
                public String getRelationType() {
                    return RelationItem.TYPE_TASK;
                }

                @Override
                public String getRelationName() {
                    return item != null ? item.getName() : null;
                }

                @Override
                public boolean hasNoSummaryTab() {
                    return true;
                }
            };
        }
        return linkingUtil;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.TASK_MAX_FORM;
    }

    @Override
    public String getIconStyle() {
        return "tasks task-list";
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

    @Override
    public String getPropertyCode() {
        return Constants.TASK;
    }
}
