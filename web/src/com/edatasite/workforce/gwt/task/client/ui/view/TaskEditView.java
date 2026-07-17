package com.edatasite.workforce.gwt.task.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.form.Localize;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.project.WbsItem;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiCellTree;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.SelectionContainer;
import com.edatasite.workforce.gwt.core.client.ui.cell.IconCell;
import com.edatasite.workforce.gwt.core.client.ui.cell.SelectItemCell;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.*;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.VerticalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.rbacpermission.TaskPermissionEnum;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.view.Reminder;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.DateUtils;
import com.edatasite.workforce.gwt.profile.client.ui.view.WorkflowDateSelecter;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.task.client.rpc.EditTask;
import com.edatasite.workforce.gwt.task.client.rpc.TaskSelectItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.task.client.ui.ListBoxWithPM;
import com.edatasite.workforce.gwt.task.client.ui.TasksChooser;
import com.edatasite.workforce.gwt.workstream.client.rpc.WbsService;
import com.edatasite.workforce.gwt.workstream.client.ui.WorkstreamChooser;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.MultiSelectionModel;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskEditView extends CustomForm2 implements Constants, HasLinksInterface, FormHasCustomFieldInterface, Colapse {
    private final Integer taskID;
    private Integer projectID = null;
    private EditTask task;
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();

    private KpiCellTree dynamicSelectorNew;
    private KpiRadioButton allEmployees;
    private KpiRadioButton allCompanyEmployees;
    private boolean error = false;
    private SelectItem[] taskStatusItems = null;
    private List<SelectItem> statusNames;
    private GeneralFileUpload fileUpload;
    private final Numbering number = new Numbering();
    private final TextBox name = new TextBox();
    private TextArea2 area;

    private final WorkstreamChooser parentWorkstream = new WorkstreamChooser();
    private final TasksChooser predTasks = new TasksChooser(true, TasksChooser.PREDECESSOR);
    private final TasksChooser succTasks = new TasksChooser(true, TasksChooser.SUCCESSOR);
    private Widget richText;
    private CRMLookUp projects;
    private VerticalPanelDiv pnlClients;
    private final HTML project = initHTML();
    private final DataListBox priority = new DataListBox();
    private final DataListBox type = new DataListBox();
    private final ListBoxWithPM status = new ListBoxWithPM();
    private final TextBox percent = new TextBox();
    private WfmButton2 updateButton;
    private final KpiCheckBox recalculateResourceHours = new KpiCheckBox(wfmStrings.recalculateHoursOnResourceUtilizationTool());
    private final KpiCheckBox billable = new KpiCheckBox("");
    private Reminder reminders;

    private final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");
    private DateTimePicker dateTime;
    final DateTimeFormat timeFormat = com.edatasite.workforce.gwt.core.client.DateUtils.getTimeFormatInternal();
    private final boolean isChangeTasksProject = Utils.hasGenericAccess(GenericSettingsEnum.IS_CHANGE_TASKS_PROJECT);
    private boolean dontKeepDelays = true;
    private final String forWhat;
    private WorkflowDateSelecter workflowDate;
    private KpiCheckBox workflowTimeBased;
    private WorkflowDateSelecter workflowActionTimeBasedDate;
    private TextArea nameSuggestBox;

    private FormHasCustomField customFieldUtil = null;
    private HasLinks linkingUtil;
    private FooterInformer link;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    AtomicBoolean firstClick = new AtomicBoolean(true);
    private TaskStatusHistoryGrid taskStatusHistoryGrid;

    public TaskEditView(Integer taskID) {
        this(taskID, null);
    }

    public TaskEditView(Integer taskID, String forWhat) {
        super("edit");
        setDescription(wfmStrings.edit());
        this.taskID = taskID;
        this.forWhat = forWhat;
    }

    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Task, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                formPropertyMap = result.getFormPropertyMap();
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                }
                getFirstRequest();
            }

        });
        return null;
    }

    int editableAssignee = READ;

    private void getFirstRequest() {
        super.onInitialize();
        TaskService.App.get().getEditTaskStatusDrop(taskID, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void success(SelectItem[] result) {
                taskStatusItems = result;
                statusNames = new ArrayList<>();
                Collections.addAll(statusNames, taskStatusItems);
                TaskService.App.get().getTaskEditablePermission(taskID, new AbstractAsyncCallback<Integer>() {
                    @Override
                    public void success(Integer result) {
                        editableAssignee = result;
                        if (result == EDIT) {
                            initAssigneeEditableForm();
                        } else {
                            initAssigneeViewForm();
                        }
                    }
                });
            }
        });
    }

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(TaskEditView.this) {
                @Override
                protected boolean isActionEditing() {
                    return true;
                }

                @Override
                protected Integer getRelationID() {
                    return taskID;
                }

                @Override
                protected String getRelationType() {
                    return RelationItem.TYPE_TASK;
                }

                @Override
                protected String getRelationName() {
                    return task != null ? task.getName() : null;
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

    private void editTaskData() {
        LoadingPanel.loading(true);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_WORKSTREAM_ADD, TaskEditView.this, (sender, args) -> {
            if (parentWorkstream != null) {
                parentWorkstream.reInit();
                WbsService.App.get().getFirstLevelWorkstreams(task.getProjectId(), new AbstractAsyncCallback<WbsItem[]>() {
                    @Override
                    public void success(WbsItem[] result) {
                        if (result != null && result.length > 0) {
                            WbsItem workStream = result[0];
                            parentWorkstream.setWorkstream(workStream);
                            parentWorkstream.setText(workStream.getName());
                        }
                    }
                });
            }
        });

        TaskService.App.get().getTaskForEdit(taskID, new AbstractAsyncCallback<EditTask>() {
            @Override
            public void success(EditTask object) {
                LoadingPanel.loading(false);
                if (object != null) {
                    task = object;
                    projectID = object.getProjectId();
                    projects.setSelected(new SelectItem(task.getProjectId(), task.getProjectName()));
                    reloadProjectClients();
                    project.setText(task.getProjectName());
                    link.setBadgeCount(task.getRelations().size());
                    if (task.getProjectId() != null) {
                        fileUpload = new GeneralFileUpload(F_TASK, task.getProjectId(), task.getObjectID());
                        addField(ATTACHMENTS, fileUpload, wfmStrings.attachments(), true);
                    }
                    number.setNumberData(task.getNumberData());

                    if (Utils.isEnableBonnardCustomization()) {
                        nameSuggestBox.setText(task.getName());
                    } else {
                        name.setText(task.getName());
                    }
                    area.setText(task.getDescription());

                    percent.setText((task.getPercent() != null && !String.valueOf(task.getPercent().intValue()).equals("0.0")) ? formatToDouble(task.getPercent().toString()) : "0");
                    if (task.isAllDay() != null && task.isAllDay()) {
                        dateTime.startTime.setVisible(false);
                        dateTime.endTime.setVisible(false);
                        dateTime.allDay.setValue(true, true);
                    } else {
                        dateTime.startTime.setVisible(true);
                        dateTime.endTime.setVisible(true);
                        dateTime.allDay.setValue(false, true);
                    }
                    dateTime.getStartDatePicker().setMonth(task.getStartDate());
                    dateTime.setStartDate(task.getStartDate());
                    dateTime.setStartTime(new StartEndTime(timeFormat.format(task.getStartDate())).time);
                    if (task.isAllDay() != null && task.isAllDay()) {
                        workflowDate.allDay.setValue(task.isAllDay());
                        Date dueDate = task.getDueDate();
                        dateTime.setDueDate(dueDate);
                    } else {
                        dateTime.setDueDate(task.getDueDate());
                    }
                    dateTime.getDueDatePicker().setMonth(task.getDueDate());
                    dateTime.setEndTime(new StartEndTime(timeFormat.format(task.getDueDate())).time);
                    if (task.getWorkflowID() != null) {
                        workflowDate.setStartDate(task.getWorkflowStartDate());
                        workflowDate.setDueDate(task.getWorkflowDueDate());
                        workflowDate.setDueDateGranularity(task.getWorkflowDueDateGranularity());
                        if (task.isWorkflowActionTimeBased()) {
                            workflowTimeBased.setValue(true, true);
                            workflowActionTimeBasedDate.setStartDate(task.getWorkflowActionStartTime());
                            workflowActionTimeBasedDate.setDueDate(task.getWorkflowActionStartTimeUnit());
                            workflowActionTimeBasedDate.setDueDateGranularity(task.getWorkflowActionStartTimeGranularity());
                        }
                    }
                    if (task.getBillable() != null) {
                        billable.setValue(task.getBillable());
                    }
                    reminders.setReminderDatas(task.getReminders());
                    parentWorkstream.setProjectId(task.getProjectId());
                    parentWorkstream.setProjectName(task.getProjectName());

                    if (task.getParentWSItem() != null && task.getParentWSItem().getName() != null) {
                        parentWorkstream.setText(task.getParentWSItem().getName());
                        parentWorkstream.setWorkstream((WbsItem) task.getParentWSItem());
                    }

                    refreshTaskDependencies(task);

                    HashMap<Integer, String[]> selectedTasks = new HashMap<>();
                    predTasks.addStyleName("TaskEditView-predTasks");
                    predTasks.setSelectedTasksMap(selectedTasks);
                    succTasks.setSelectedTasksMap(selectedTasks);
                    predTasks.setSucc_predChooser(succTasks);
                    succTasks.setSucc_predChooser(predTasks);
                    predTasks.setProjectId(task.getProjectId());
                    predTasks.setTaskId(task.getObjectID());
                    predTasks.setProjectName(task.getProjectName());
                    succTasks.setProjectId(task.getProjectId());
                    succTasks.setTaskId(task.getObjectID());
                    succTasks.setProjectName(task.getProjectName());

                    if (!(task.getPermission() == EDIT || Utils.hasPermission(PermissionConstants.PM_TASKS_EDIT) || Utils.hasPermission(PermissionConstants.CRM_TASKS_EDIT))) {
                        number.setEnabled(false);
                        name.setEnabled(false);
                        area.setEnabled(false);
                        priority.setEnabled(false);
                        type.setEnabled(false);
                        billable.setEnabled(false);
                        dateTime.startDate.setEnabled(false);
                        dateTime.dueDate.setEnabled(false);
                        dateTime.startTime.setEnabled(false);
                        dateTime.endTime.setEnabled(false);
                        dateTime.allDay.setEnabled(false);
                    }

                    if (task.getPermissions().contains(TaskPermissionEnum.ASSIGNEE_STATUS_EDIT.getCode())) {
                        status.setUpdateForAll(true);
                        status.setUpdateForAssignment(false);
                    } else {
                        status.setUpdateForAll(false);
                        status.setUpdateForAssignment(true);
                    }
                    status.initInternal();

                    predTasks.clearTable();
                    succTasks.clearTable();
                    if (task.getPredecessorTaskItems() != null && task.getPredecessorTaskItems().length > 0) {
                        for (int i = 0; i < task.getPredecessorTaskItems().length; i++) {
                            if (task.getPredecessorTaskItems()[i] != null) {
                                predTasks.addTableItem(task.getPredecessorTaskItems()[i]);
                            }
                        }
                        predTasks.refreshAddedTasks();
                    }
                    if (task.getSuccessorTaskItems() != null && task.getSuccessorTaskItems().length > 0) {
                        for (int i = 0; i < task.getSuccessorTaskItems().length; i++) {
                            if (task.getSuccessorTaskItems()[i] != null) {
                                succTasks.addTableItem(task.getSuccessorTaskItems()[i]);
                            }
                        }
                        succTasks.refreshAddedTasks();
                    }

                    LoadingPanel.loading(true);
                    TaskService.App.get().getTaskMembersWithTreeInfo(taskID, new AbstractAsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>>() {
                        @Override
                        public void success(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> result) {
                            if (dynamicSelectorNew.getSelectAll() != null && dynamicSelectorNew.getSelectAll().getValue()) {
                                dynamicSelectorNew.getSelectAll().setValue(false);
                            }
                            dynamicSelectorNew.setItems(result);
                            LoadingPanel.loading(false);
                        }
                    });

                    TaskService.App.get().getPriorities(new AbstractAsyncCallback<SelectItem[]>() {
                        @Override
                        public void failure(Throwable throwable) {
                        }

                        @Override
                        public void success(final SelectItem[] priorityItems) {
                            Scheduler.get().scheduleDeferred(() -> {
                                priority.setItems(priorityItems);
                                addPredefinedValues(PRIORITY, priorityItems);
                                if (task != null && task.getPriorityId() != null) {
                                    priority.setSelected(task.getPriorityId());
                                }
                            });
                        }
                    });
                    TaskService.App.get().getTaskTypes(new AbstractAsyncCallback<SelectItem[]>() {
                        @Override
                        public void failure(Throwable throwable) {
                        }

                        @Override
                        public void success(final SelectItem[] typeItems) {
                            Scheduler.get().scheduleDeferred(() -> {
                                type.setItems(typeItems);
                                addPredefinedValues(TYPE, typeItems);
                                if (task != null && task.getTypeId() != null) {
                                    type.setSelected(task.getTypeId());
                                }
                            });
                        }
                    });

                    TaskService.App.get().getEditTaskStatusDrop(taskID, new AbstractAsyncCallback<SelectItem[]>() {
                        @Override
                        public void success(final SelectItem[] result) {
                            Scheduler.get().scheduleDeferred(() -> {
                                status.getListBox().setItems(result);
                                addPredefinedValues(STATUS, result);
                                if (task != null && task.getStatusId() != null) {
                                    status.getListBox().setSelected(task.getStatusId());
                                }
                            });
                        }
                    });
                    getCustomFieldUtil().fillCustomFieldsWithData(object.getCustomFieldItems());
                } else {
                    clear();
                    final HelpTextPanel helpTextPanel = new HelpTextPanel(projectStrings.youDonTHaveEnoughPermissionToEditThisTask(), 450);
                    helpTextPanel.setCellSpacing(20);
                    add(helpTextPanel);
                }
            }
        });
    }


    private void initAssigneeViewForm() {
        dynamicSelectorNew.drawSelectedSide(new SelectionContainer() {
            @Override
            public void selectedDataGrid(KpiDataGrid<KpiTreeInfo> selectedDataGrid, ColumnSortEvent.ListHandler<KpiTreeInfo> sortHandler, MultiSelectionModel<KpiTreeInfo> selectionModel) {
                //Employee
                Column<KpiTreeInfo, String> employee = new Column<KpiTreeInfo, String>(new TextCell()) {
                    @Override
                    public String getValue(KpiTreeInfo object) {
                        return object.getName();
                    }
                };
                selectedDataGrid.addColumn(employee, wfmStrings.employee());
                selectedDataGrid.setColumnWidth(employee, 20, com.google.gwt.dom.client.Style.Unit.PCT);

                Column<KpiTreeInfo, String> position = new Column<KpiTreeInfo, String>(new TextCell()) {
                    @Override
                    public String getValue(KpiTreeInfo object) {
                        return object.getPositionName() != null ? object.getPositionName() : wfmStrings.notAvailable();
                    }
                };
                selectedDataGrid.addColumn(position, wfmStrings.position());
                selectedDataGrid.setColumnWidth(position, 20, com.google.gwt.dom.client.Style.Unit.PCT);
            }

            @Override
            public void additionalActions(HTMLPanel actionsPanel) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }

    private void initAssigneeEditableForm() {
        dynamicSelectorNew.drawSelectedSide(new SelectionContainer() {
            @Override
            public void selectedDataGrid(final KpiDataGrid<KpiTreeInfo> selectedDataGrid, ColumnSortEvent.ListHandler<KpiTreeInfo> sortHandler, final MultiSelectionModel<KpiTreeInfo> selectionModel) {

                //employee name
                Column<KpiTreeInfo, String> employee = new Column<KpiTreeInfo, String>(new TextCell()) {
                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        return object.getName();
                    }
                };
                employee.setSortable(true);
                sortHandler.setComparator(employee, Comparator.comparing(KpiTreeInfo::getName));
                selectedDataGrid.addColumn(employee, wfmStrings.employee());
                selectedDataGrid.setColumnWidth(employee, 30, com.google.gwt.dom.client.Style.Unit.PCT);


                SelectItemCell statusInputCell = new SelectItemCell(statusNames);
                statusInputCell.setStyleName("debug_input-status");
                Column<KpiTreeInfo, SelectItem> statusColumn = new Column<KpiTreeInfo, SelectItem>(statusInputCell) {
                    @Override
                    public SelectItem getValue(KpiTreeInfo object) {
                        return new SelectItem(object.getStatusId());
                    }
                };
                selectedDataGrid.addColumn(statusColumn, wfmStrings.status());
                statusColumn.setFieldUpdater((index, object, value) -> object.setStatusId(value.getId()));
                selectedDataGrid.setColumnWidth(statusColumn, 30, com.google.gwt.dom.client.Style.Unit.PCT);


                //estimate date
                Column<KpiTreeInfo, String> time = null;
                if (!Utils.hasGenericAccess(GenericSettingsEnum.ENABLED_MANNUAL_RU_ESTIMATE)) {
                    final TextInputCell timeInputCell = new TextInputCell("form-control debug_time-input--cell");
                    time = new Column<KpiTreeInfo, String>(timeInputCell) {
                        @Override
                        public String getValue(final KpiTreeInfo object) {
                            return Utils.formatMinutes(object.getTime());
                        }
                    };
                    time.setFieldUpdater((index, object, value) -> {
                        if (!"".equals(value) && !"00:00".equals(value)) {
                            object.setTime(Utils.parseMinutes(value));
                        }
                    });
                } else {
                    time = new Column<KpiTreeInfo, String>(new TextCell()) {
                        @Override
                        public String getValue(final KpiTreeInfo object) {
                            return Utils.formatMinutes(object.getTime());
                        }
                    };
                }

                selectedDataGrid.addColumn(time, wfmStrings.estimatedTime());
                selectedDataGrid.setColumnWidth(time, 20, com.google.gwt.dom.client.Style.Unit.PCT);


                //Completed
                final TextInputCell completedInputCell = new TextInputCell("form-control debug_completed-input--cell");
                if ("true".equals(Utils.userSettings.get(ISAUTOMATIC))) {
                    Column<KpiTreeInfo, String> percent = new Column<KpiTreeInfo, String>(new TextCell()) {
                        @Override
                        public String getValue(final KpiTreeInfo object) {
                            return String.valueOf(object.getPercent() != null ? object.getPercent() : 0);
                        }
                    };
                    selectedDataGrid.addColumn(percent, wfmStrings.percentCompleted());
                    selectedDataGrid.setColumnWidth(percent, 20, com.google.gwt.dom.client.Style.Unit.PCT);
                } else {
                    Column<KpiTreeInfo, String> percent = new Column<KpiTreeInfo, String>(completedInputCell) {
                        @Override
                        public String getValue(final KpiTreeInfo object) {
                            return String.valueOf(object.getPercent() != null ? object.getPercent() : 0);
                        }
                    };
                    percent.setFieldUpdater((index, object, value) -> {
                        if (!value.equals("") && (value.matches(Constants.REGEX_REAL_NUMBERS_WITH_COMMA) || value.matches(Constants.REGEX_REAL_NUMBERS_WITH_DOT))) {
                            error = false;
                            value = value.replace(",", ".");
                            object.setPercent(Float.valueOf(value));
                        } else {
                            error = true;
                        }
                    });
                    selectedDataGrid.addColumn(percent, wfmStrings.percentCompleted());
                    selectedDataGrid.setColumnWidth(percent, 20, com.google.gwt.dom.client.Style.Unit.PCT);
                }


                //remove action
                final Column<KpiTreeInfo, String> action = new Column<KpiTreeInfo, String>(new IconCell("ficon--trash pointer")) {
                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        return null;
                    }
                };
                action.setFieldUpdater((index, object, value) -> {
                    List<KpiTreeInfo> contacts = selectedDataGrid.getList();
                    contacts.remove(object);
                    object.setSelected(false);
                    selectionModel.setSelected(object, false);
                });
                selectedDataGrid.addColumn(action, "");
                action.setCellStyleNames("center");
                selectedDataGrid.setColumnWidth(action, 10, com.google.gwt.dom.client.Style.Unit.PCT);
            }

            @Override
            public void additionalActions(HTMLPanel actionsPanel) {
                if (Utils.hasUserMaxRoleID(MEM) || Utils.hasRole(CLIENT) || Utils.hasEitherRole(ESS_USER_CODE)) {
                    allEmployees.setVisible(false);
                    allCompanyEmployees.setVisible(false);
                } else {
                    allEmployees.setValue(Utils.isEmployee());
                    actionsPanel.add(allEmployees);

                    if (!Utils.isEmployeeAssignmentEnable()) {
                        actionsPanel.add(allCompanyEmployees);
                    }

                    allEmployees.addValueChangeHandler(booleanValueChangeEvent -> {
                        if (booleanValueChangeEvent.getValue()) {
                            reloadAssigneesTree();
                        }
                    });

                    allCompanyEmployees.addValueChangeHandler(event -> {
                        if (event.getValue()) {
                            reloadAllCompanyEmployees();
                        }
                    });
                }
            }
        });
    }

    private void reloadAssigneesTree() {
        LoadingPanel.loading(true);
        LinkedHashMap<Integer, Integer> usersList = null;
        if (dynamicSelectorNew.getSelectedEntityIDs() != null) {
            usersList = dynamicSelectorNew.getSelectedEmployeeIDsWithStatus();
        }
        TaskService.App.get().getAssigneesWithTreeInfoLinkedHashMapWithParams(usersList, projectID, taskID, LayoutRPC.TASK_MIN_FORM.equals(getFormID()) || Utils.isEnableBonnardCustomization(), new AbstractAsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>>() {
            @Override
            public void success(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> result) {
                if (dynamicSelectorNew.getSelectAll() != null && dynamicSelectorNew.getSelectAll().getValue()) {
                    dynamicSelectorNew.getSelectAll().setValue(false);
                }
                dynamicSelectorNew.setItems(result);
                LoadingPanel.loading(false);
            }
        });
    }

    private void reloadAllCompanyEmployees() {
        LoadingPanel.loading(true);
        LinkedHashMap<Integer, Integer> usersList = null;
        if (dynamicSelectorNew.getSelectedEntityIDs() != null) {
            usersList = dynamicSelectorNew.getSelectedEmployeeIDsWithStatus();
        }
        TaskService.App.get().getAssigneesWithTreeInfoLinkedHashMap(usersList, new AbstractAsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>>() {
            @Override
            public void success(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> result) {
                if (dynamicSelectorNew.getSelectAll() != null && dynamicSelectorNew.getSelectAll().getValue()) {
                    dynamicSelectorNew.getSelectAll().setValue(false);
                }
                dynamicSelectorNew.setItems(result);
                LoadingPanel.loading(false);
            }
        });
    }

    private IdTime[] getSelectedAssignees() {
        ArrayList<IdTime> items = new ArrayList<>();
        IdTime iTime;
        Integer estimatedTime;
        if (dynamicSelectorNew.getSelectedData() != null && dynamicSelectorNew.getSelectedData().size() > 0) {
            for (KpiTreeInfo treeInfo : dynamicSelectorNew.getSelectedData()) {
                estimatedTime = treeInfo.getTime() != null ? treeInfo.getTime() : 0;
                iTime = new IdTime(treeInfo.getId(), estimatedTime, treeInfo.getPercent() != null ? treeInfo.getPercent() : 0f, treeInfo.getStatusId());
                items.add(iTime);
            }
        }
        return items.toArray(new IdTime[items.size()]);
    }

    private Widget createRichText() {
        area = new TextArea2(Constants.DEFAULT_DESCRIPTION_CHARACTER_LIMIT);
        area.getTextArea().addStyleName("textArea-white-space");
        return area;
    }

    @Override
    protected String getWikiCode() {
        return Utils.isCRM() ? PermissionConstants.CRM_TASKS_EDIT : PermissionConstants.PM_TASKS_EDIT;
    }

    @Override
    protected String getFormID() {
        return forWhat != null && forWhat.equals(Constants.WORKFLOW) ? LayoutRPC.WORKFLOW_TASK_MIN_FORM : LayoutRPC.TASK_MAX_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }

    @Override
    protected void getDataToFillFields() {
        editTaskData();
    }

    @Override
    protected void initPredefinedValues() {
        //To change body of implemented methods use File | Settings | File Templates.
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
        link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);
        footer.addToLeftSide(link);

        link.addClickHandler(event -> {
            if (firstClick.get()) {
                getLinkingUtil().getAddLinkSideNavBox();
                getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(task.getRelations(), false);

                firstClick.set(false);
            } else {
                getLinkingUtil().getAddLinkSideNavBox().show();
            }
        });

        updateButton = addButton(wfmStrings.update(), WfmButton2.BTN_PRIMARY, event -> {
            if (successorChangeMethodDisplayNeeded()) {
                chooseSuccessorsSaveMethod();
            } else if (isMessageNeeded()) {
                isItOkToChangeProject();
            } else {
                if (!validate() || error) {
                    return;
                }
                save();
            }
        });
        updateButton.ensureDebugId("Edit_task_updateButton");

    }

    public void registerFields() {
        allEmployees = new KpiRadioButton("employee", Property.get(Constants.PROJECT, wfmStrings.projectEmployees(), wfmStrings.project()));
        allCompanyEmployees = new KpiRadioButton("employee", wfmStrings.all());

        dynamicSelectorNew = new KpiCellTree();
        dynamicSelectorNew.removeStyleNameFromDataGrid("cellBasedWidget-mod--static-body");
        dynamicSelectorNew.addStyleNameToDataGrid("cellBasedWidget-mod--fixed-height cellBasedWidget-mod--cell-not-overflow");
        dateTime = new DateTimePicker(false, true);
        parentWorkstream.getWorkstreamNameBox().addClickHandler(event -> parentWorkstream.publicShowShell());
        parentWorkstream.getWorkstreamNameBox().addClickHandler(handler);
        number.setEnabled(Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_TASK_NUMBERING));
        number.ensureDebugId("Edit_task_number");
        number.getTxtNumber().addValueChangeHandler(handler);
        number.getLastTxt().addValueChangeHandler(handler);
        number.getTxtPrefix().addValueChangeHandler(handler);

        pnlClients = new VerticalPanelDiv();
        pnlClients.setSpacing(5);

        projects = new CRMLookUp(LookUpConstants.PROJECT);
        projects.setFullSearch(true);
        projects.ensureDebugId("Edit_Task_project");
        projects.addValueChangeHandler(handler);
        projects.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            projectID = projects.getSelectedItemID();
            allEmployees.setValue(true, true);
            reloadProjectClients();
        });

        workflowDate = new WorkflowDateSelecter(true, false);
        workflowActionTimeBasedDate = new WorkflowDateSelecter(true, true);
        workflowActionTimeBasedDate.setVisible(false);
        workflowTimeBased = new KpiCheckBox(wfmStrings.executionTime());
        workflowTimeBased.addValueChangeHandler(booleanValueChangeEvent -> workflowActionTimeBasedDate.setVisible(booleanValueChangeEvent.getValue()));

        status.ensureDebugId("Edit_task_status");
        status.getListBox().setAllowFirstItem(true);
        if (Utils.isDoubleMessageEnable()) {
            status.getListBox().addValueChangeHandler(valueChangeEvent -> {
                WfmMessageBox changeStatusMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                changeStatusMessageBox.setMessage(wfmMessages.doYouWantToChangeStatusTo(status.getListBox().getSelectedItem().getName()));
                changeStatusMessageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        task.setStatusId(status.getListBox().getSelectedItem().getId());
                    }

                    @Override
                    public void onCancel() {
                        status.getListBox().setSelected(task.getStatusId());
                    }
                });

                changeStatusMessageBox.setTitle(wfmStrings.warning());
                changeStatusMessageBox.open();
            });
        }

        dateTime.sinkEvents(Event.FOCUSEVENTS);
        priority.setAllowFirstItem(true);
        type.setAllowFirstItem(true);

//        percent.setWidth("250px");
        percent.setEnabled(true);

        percent.addValueChangeHandler(handler);
        percent.setEnabled(!Utils.userSettings.get(ISAUTOMATIC).equals("true"));
        Validation.addNumericKeyboardListener(percent);
        percent.addKeyboardListener(new KeyboardListenerAdapter() {
            @Override
            public void onKeyUp(Widget sender, char keyCode, int modifiers) {
                Validation.numberValidation(percent);
            }
        });

        addTitleField(CustomFormConstants.TASK.TASK_DETAILS, getTitle(property.getSingular(wfmStrings.taskDetails(), wfmStrings.task())));

        // Task's Project
        if (isChangeTasksProject && Utils.hasRole(ADMIN)) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.PROJECT) != null) {
                addField(CustomFormConstants.TASK.PROJECT, project, getTitle(formPropertyMap.get(CustomFormConstants.TASK.PROJECT).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.PROJECT).getTitle() : wfmStrings.project()));
            } else {
                addField(CustomFormConstants.TASK.PROJECT, projects, getTitle(Property.get(Constants.PROJECT, wfmStrings.project())));
            }

        } else {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.PROJECT) != null) {
                addField(CustomFormConstants.TASK.PROJECT, project, getTitle(formPropertyMap.get(CustomFormConstants.TASK.PROJECT).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.PROJECT).getTitle() : wfmStrings.project()));
            } else {
                addField(CustomFormConstants.TASK.PROJECT, project, getTitle(Property.get(Constants.PROJECT, wfmStrings.project())));
            }

        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.CLIENT) != null) {
            addField(CustomFormConstants.TASK.CLIENT, pnlClients, getTitle(formPropertyMap.get(CustomFormConstants.TASK.CLIENT).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.CLIENT).getTitle() : wfmStrings.customer(), formPropertyMap.get(CustomFormConstants.TASK.CLIENT).isRequired()));
        } else {
            addField(CustomFormConstants.TASK.CLIENT, pnlClients, getTitle(Property.get(Constants.CLIENT_LIST, wfmStrings.customer())));
        }
        // Task Number
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null) {
            addField(CustomFormConstants.NUMBER, number, getTitle(formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : wfmStrings.number()));
        } else {
            addField(CustomFormConstants.NUMBER, number, getTitle(wfmStrings.number()));
        }
        // Task Name Field
        name.addValueChangeHandler(handler);

        nameSuggestBox = new TextArea();
        nameSuggestBox.setSize("500px", "100px");

        if (Utils.isEnableBonnardCustomization()) {
            addField(NAME, nameSuggestBox, getTitle(wfmStrings.name(), true));
        } else {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null) {
                addField(CustomFormConstants.NAME, name, getTitle(formPropertyMap.get(CustomFormConstants.NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.NAME).getTitle() : wfmStrings.name(), formPropertyMap.get(CustomFormConstants.NAME).isRequired()));
                name.setEnabled(!formPropertyMap.get(CustomFormConstants.NAME).isDisabled());
            } else {
                addField(NAME, name, getTitle(wfmStrings.name(), true));
            }
        }
        // Task Description Field
        richText = createRichText();
        richText.ensureDebugId("Edit_task_description");
        richText.setWidth("100%");
        richText.setHeight("200px");
        ((TextArea2) richText).getTextArea().addChangeHandler(handler);

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null) {
            addField(CustomFormConstants.DESCRIPTION, richText, getTitle(formPropertyMap.get(CustomFormConstants.DESCRIPTION).isChanged() ? formPropertyMap.get(CustomFormConstants.DESCRIPTION).getTitle() : wfmStrings.description()));
        } else {
            addField(DESCRIPTION, richText, null, false);
        }
        // Pred Task Field
        predTasks.ensureDebugId("Edit_Task_predeccessorTasks");
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.PREDECESSOR_TASK) != null) {
            addField(CustomFormConstants.TASK.PREDECESSOR_TASK, predTasks, getTitle(formPropertyMap.get(CustomFormConstants.TASK.PREDECESSOR_TASK).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.PREDECESSOR_TASK).getTitle() : wfmStrings.predeccessor()));
        } else {
            addField(CustomFormConstants.TASK.PREDECESSOR_TASK, predTasks, getTitle(wfmStrings.predeccessor()));
        }
        // Succ Task Field
        succTasks.ensureDebugId("Edit_Task_successorTasks");
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.SUCCESSOR_TASK) != null) {
            addField(CustomFormConstants.TASK.SUCCESSOR_TASK, succTasks, getTitle(formPropertyMap.get(CustomFormConstants.TASK.SUCCESSOR_TASK).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.SUCCESSOR_TASK).getTitle() : wfmStrings.successor()));
        } else {
            addField(CustomFormConstants.TASK.SUCCESSOR_TASK, succTasks, getTitle(wfmStrings.successor()));
        }
        //Recalculate hours on Resource Utilization tool
        if ("true".equals(Utils.userSettings.get(RESOURCE_UTILIZATION_ENABLED)) && (!Utils.hasGenericAccess(GenericSettingsEnum.ENABLED_MANNUAL_RU_ESTIMATE))) {
            recalculateResourceHours.ensureDebugId("Edit_Task_recalculate_hours_on_Resource_Utilization_tool");
            recalculateResourceHours.addValueChangeHandler(handler);
            addField(CustomFormConstants.RECALCULATE_HOURS_ON_RESOURCE_UTIL, recalculateResourceHours, null);

            if (Utils.hasGenericAccess(GenericSettingsEnum.RECALCULATE_RESOURCE_HOURS)) {
                recalculateResourceHours.setValue(false);
            }
        }
        checkUserForAssignPermission();
        // Task Billable Field
        billable.ensureDebugId("Edit_task_billable");
        billable.addValueChangeHandler(handler);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.BILLIBLE) != null) {
            addField(CustomFormConstants.TASK.BILLIBLE, billable, getTitle(formPropertyMap.get(CustomFormConstants.TASK.BILLIBLE).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.BILLIBLE).getTitle() : wfmStrings.billable()));
        } else {
            addField(CustomFormConstants.TASK.BILLIBLE, billable, getTitle(wfmStrings.billable()));
        }
        // Task Priority
        priority.ensureDebugId("Edit_tak_percent");
        priority.addValueChangeHandler(handler);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PRIORITY) != null) {
            addField(CustomFormConstants.PRIORITY, priority, getTitle(formPropertyMap.get(CustomFormConstants.PRIORITY).isChanged() ? formPropertyMap.get(CustomFormConstants.PRIORITY).getTitle() : wfmStrings.priority()));
        } else {
            addField(PRIORITY, priority, getTitle(wfmStrings.priority(), false));
        }

        // Task Type
        type.ensureDebugId("Edit_task_type");
        type.addValueChangeHandler(handler);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TYPE) != null) {
            addField(CustomFormConstants.TYPE, type, getTitle(formPropertyMap.get(CustomFormConstants.TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.TYPE).getTitle() : wfmStrings.type()));
        } else {
            addField(TYPE, type, getTitle(wfmStrings.type(), false));
        }
        // Task Status
        status.getListBox().addValueChangeHandler(handler);
        status.addValueChangeEvent(handler);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null) {
            addField(CustomFormConstants.STATUS, status, getTitle(formPropertyMap.get(CustomFormConstants.STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.STATUS).getTitle() : wfmStrings.status()));
        } else {
            addField(STATUS, status, getTitle(wfmStrings.status(), true));
        }
        // Task StartDate
        dateTime.startDate.ensureDebugId("Edit_Task_startDate");
        dateTime.startTime.ensureDebugId("Edit_Task_startTime");
        dateTime.startDate.addValueChangeHandler(handler);
        dateTime.startTime.getListBox().addChangeHandler(handler);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.START_DATE) != null) {
            addField(CustomFormConstants.START_DATE, new InputGroup(dateTime.startDate, dateTime.startTime), getTitle(formPropertyMap.get(CustomFormConstants.START_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.START_DATE).getTitle() : wfmStrings.startDate()));
        } else {
            addField(START_DATE, new InputGroup(dateTime.startDate, dateTime.startTime), getTitle(wfmStrings.startDate(), true));
        }
        //Task Due Date
        dateTime.endTime.ensureDebugId("Edit_Task_endTime");
        dateTime.dueDate.ensureDebugId("Edit_Task_dueDate");
        dateTime.allDay.ensureDebugId("Edit_task_allDay");
        dateTime.dueDate.addValueChangeHandler(handler);
        dateTime.endTime.getListBox().addChangeHandler(handler);
        dateTime.allDay.addValueChangeHandler(handler);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DUE_DATE) != null) {
            addField(CustomFormConstants.DUE_DATE, new AdvancedInputGroup(new InputGroup(dateTime.dueDate, dateTime.endTime)), getTitle(formPropertyMap.get(CustomFormConstants.DUE_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.DUE_DATE).getTitle() : wfmStrings.dueDate()));
        } else {
            addField(DUE_DATE, new AdvancedInputGroup(new InputGroup(dateTime.dueDate, dateTime.endTime), dateTime.allDay), getTitle(wfmStrings.dueDate(), true));
        }

        NoteWidget2 noteWidget = new NoteWidget2(taskID, RelationItem.TYPE_TASK);
        noteWidget.addStyleName("file--TaskEditVIew");
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.TASK_NOTE) != null) {
            addField(CustomFormConstants.TASK.TASK_NOTE, noteWidget, getTitle(formPropertyMap.get(CustomFormConstants.TASK.TASK_NOTE).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.TASK_NOTE).getTitle() : wfmStrings.notes()));
        } else {
            addField(CustomFormConstants.TASK.TASK_NOTE, noteWidget, wfmStrings.notes(), true);
        }
        // WorkStream
        WfmButton2 clearIcon = new WfmButton2(null, WfmButton2.BTN_WHITE);
        clearIcon.addStyleName("btn--icon");
        clearIcon.add(new SvgIcon(SvgEnum.x));
        clearIcon.addClickHandler(sender -> parentWorkstream.clearSelection());
        parentWorkstream.ensureDebugId("Edit_task_parentWorkstream");
        parentWorkstream.getWorkstreamNameBox().addChangeHandler(handler);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.PARENT_WORKSTREAM) != null) {
            addField(CustomFormConstants.TASK.PARENT_WORKSTREAM, new AdvancedInputGroup(null, parentWorkstream, clearIcon, true, false), getTitle(formPropertyMap.get(CustomFormConstants.TASK.PARENT_WORKSTREAM).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.PARENT_WORKSTREAM).getTitle() : wfmStrings.workStream()));
        } else {
            addField(CustomFormConstants.TASK.PARENT_WORKSTREAM, new AdvancedInputGroup(null, parentWorkstream, clearIcon, true, false), getTitle(wfmStrings.workStream()));
        }

        succTasks.setSelectionChange(() -> {
            predTasks.resultsShell = null;
            setNonSavedFieldsCount(getNonSavedFieldsCount() + 1);
        });

        predTasks.setSelectionChange(() -> {
            succTasks.resultsShell = null;
            setNonSavedFieldsCount(getNonSavedFieldsCount() + 1);
        });

        succTasks.setDeletePredOrSuccTask(() -> {
            predTasks.refreshResults();
            setNonSavedFieldsCount(getNonSavedFieldsCount() + 1);
        });

        predTasks.setDeletePredOrSuccTask(() -> {
            succTasks.refreshResults();
            setNonSavedFieldsCount(getNonSavedFieldsCount() + 1);
        });

        reminders = new Reminder(false);
        reminders.ensureDebugId("Edit_task_dueDateReminder");
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.DUE_DATE_REMINDER) != null) {
            addField(CustomFormConstants.TASK.DUE_DATE_REMINDER, reminders, getTitle(formPropertyMap.get(CustomFormConstants.TASK.DUE_DATE_REMINDER).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.DUE_DATE_REMINDER).getTitle() : wfmStrings.duedatereminder()));
        } else {
            addField(CustomFormConstants.TASK.DUE_DATE_REMINDER, reminders, getTitle(wfmStrings.duedatereminder()));
        }

        addTitleField(CustomFormConstants.TASK.TASK_ASSIGNEES, wfmStrings.assignees());
        addField(ASSIGNEE, dynamicSelectorNew, null);

        //workflow due date
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WORKFLOW_DATE) != null) {
            addField(CustomFormConstants.WORKFLOW_DATE, workflowDate, getTitle(formPropertyMap.get(CustomFormConstants.WORKFLOW_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.WORKFLOW_DATE).getTitle() : wfmStrings.date(), formPropertyMap.get(CustomFormConstants.WORKFLOW_DATE).isRequired()));
            workflowDate.setEnabled(!formPropertyMap.get(CustomFormConstants.WORKFLOW_DATE).isDisabled());
        } else {
            addField(WORKFLOW_DATE, workflowDate, getTitle(wfmStrings.date(), true));
        }
        //workflow due date
        addTitleField(WORKFLOW_TIME_BASED_HEADER, wfmStrings.timeBasedAction());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WORKFLOW_TIME_BASED) != null) {
            addField(CustomFormConstants.WORKFLOW_TIME_BASED, workflowTimeBased, getTitle(formPropertyMap.get(CustomFormConstants.WORKFLOW_TIME_BASED).isChanged() ? formPropertyMap.get(CustomFormConstants.WORKFLOW_TIME_BASED).getTitle() : wfmStrings.executionTime(), formPropertyMap.get(CustomFormConstants.WORKFLOW_TIME_BASED).isRequired()));
            workflowTimeBased.setEnabled(!formPropertyMap.get(CustomFormConstants.WORKFLOW_TIME_BASED).isDisabled());
        } else {
            addField(WORKFLOW_TIME_BASED, new InputGroup(workflowTimeBased, workflowActionTimeBasedDate), wfmStrings.executionTime());
        }
        taskStatusHistoryGrid = new TaskStatusHistoryGrid(taskID);
        addField(CustomFormConstants.TASK.TASK_STATUS_HISTORY, taskStatusHistoryGrid, null, true);

        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, taskID);
        show();
    }

    private void checkUserForAssignPermission() {
        TaskService.App.get().getAssignEmployeeToProject(projectID, PermissionConstants.PM_ASSIGN_TASK_TO_MEMBER, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void failure(Throwable throwable) {
                //isAssignEmployeeAccess = false;
            }

            @Override
            public void success(Boolean result) {
                allCompanyEmployees.setVisible(result != null ? result : false);
            }
        });
    }

    private Boolean isMessageNeeded() {
        Boolean wsNotChanged = true, predNotChanged = true, succNotChanged = true;

        Boolean messageNeeded = (predTasks.getTasks() != null && predTasks.getTasks().length > 0 && predTasks.getTasks()[0] != null) ||
                (succTasks.getTasks() != null && succTasks.getTasks().length > 0 && succTasks.getTasks()[0] != null) ||
                parentWorkstream.getWorkstream() != null ||
                task.getPredecessorTaskItems() != null || task.getSuccessorTaskItems() != null || task.getParentWSItem() != null;
        if (messageNeeded) {
            if (task.getParentWSItem() != null && parentWorkstream.getWorkstream() != null) {
                wsNotChanged = task.getParentWSItem().getId().equals(parentWorkstream.getWorkstream().getId());
            }
            if (predTasks.getTasks() != null && predTasks.getTasks().length > 0 && predTasks.getTasks()[0] != null && task.getPredecessorTaskItems() != null) {
                Set<TaskSelectItem> tasksBefore = new HashSet<>(Arrays.asList(task.getPredecessorTaskItems()));
                Set<TaskSelectItem> tasksAfter = new HashSet<>(Arrays.asList(predTasks.getTasks()));
                Boolean foundEachItem = true;
                for (TaskSelectItem taskBefore : tasksBefore) {
                    if (!tasksAfter.contains(taskBefore)) {
                        foundEachItem = false;
                        break;
                    }
                }
                predNotChanged = foundEachItem;
                if (predNotChanged) {
                    for (TaskSelectItem taskAfter : tasksAfter) {
                        if (taskAfter != null && !tasksBefore.contains(taskAfter)) {
                            foundEachItem = false;
                            break;
                        }
                    }
                    predNotChanged = foundEachItem;
                }
            } else if (predTasks.getTasks() != null && predTasks.getTasks().length > 0 && predTasks.getTasks()[0] != null && task.getPredecessorTaskItems() == null) {
                predNotChanged = false;
            }
            succNotChanged = successorNotChanged();
            if (wsNotChanged && predNotChanged && succNotChanged) {
                messageNeeded = false;
            }
        }
        return messageNeeded;
    }

    private void reloadProjectClients() {
        if (projects.getSelectedItemID() != null) {
            ProjectService.App.get().getProjectClients(projects.getSelectedItemID(), new AsyncCallback<SelectItem[]>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(SelectItem[] clients) {
                    pnlClients.clear();

                    if (clients != null) {
                        for (SelectItem client : clients) {
                            pnlClients.add(new HTML(client.getName()));
                        }
                    }
                }
            });
        }
    }

    private boolean successorNotChanged() {
        boolean succNotChanged = true;
        if (succTasks.getTasks() != null && succTasks.getTasks().length > 0 && succTasks.getTasks()[0] != null && task.getSuccessorTaskItems() != null) {
            Set<TaskSelectItem> tasksBefore = new HashSet<>(Arrays.asList(task.getSuccessorTaskItems()));
            Set<TaskSelectItem> tasksAfter = new HashSet<>(Arrays.asList(succTasks.getTasks()));
            Boolean foundEachItem = true;
            for (TaskSelectItem taskBefore : tasksBefore) {
                if (!tasksAfter.contains(taskBefore)) {
                    foundEachItem = false;
                    break;
                }
            }
            succNotChanged = foundEachItem;
            if (succNotChanged) {
                for (TaskSelectItem taskAfter : tasksAfter) {
                    if (taskAfter != null && !tasksBefore.contains(taskAfter)) {
                        foundEachItem = false;
                        break;
                    }
                }
                succNotChanged = foundEachItem;
            }
        } else if (succTasks.getTasks() != null && succTasks.getTasks().length > 0 && succTasks.getTasks()[0] != null && task.getSuccessorTaskItems() == null) {
            succNotChanged = false;
        } else if ((succTasks.getTasks() == null || succTasks.getTasks().length == 0 || succTasks.getTasks()[0] == null) && task.getSuccessorTaskItems() != null) {
            succNotChanged = false;
        }
        return succNotChanged;
    }

    private boolean successorChangeMethodDisplayNeeded() {
        if (succTasks.getTasks() != null && succTasks.getTasks().length > 0 && succTasks.getTasks()[0] != null) {
            return !DateUtils.areOnTheSameDay(task.getStartDate(), dateTime.getStartDate()) ||
                    !DateUtil.areOnTheSameDay(task.getDueDate(), dateTime.getDueDate());
        }
        return false;
    }

    private void chooseSuccessorsSaveMethod() {
        final KpiModal dialogBox = new KpiModal();
        dialogBox.setWidth(500);
//        dialogBox.setAnimationEnabled(true);
//        dialogBox.setGlassEnabled(true);
        dialogBox.setTitle(projectStrings.pleaseChooseAnOption());
        VerticalPanel vpMain = new VerticalPanel();
        VerticalPanel vpRB = new VerticalPanel();

        RadioButton keepAllDelays = new KpiRadioButton("option");
        keepAllDelays.setText(projectStrings.keepTheLagTime());
        keepAllDelays.addClickHandler(handler);
        final RadioButton dontKeepAllDelays = new KpiRadioButton("option");
        dontKeepAllDelays.setText(projectStrings.doNotKeepTheLagTime());
        dontKeepAllDelays.addClickHandler(handler);
        keepAllDelays.setValue(true);
        dontKeepAllDelays.setValue(false);
        vpRB.add(keepAllDelays);
        vpRB.add(dontKeepAllDelays);
        vpMain.add(new HTML(projectStrings.editingTaskMightShiftSuccessor()));
        vpMain.add(vpRB);
        vpMain.setSpacing(8);
        WfmButton2 ok = new WfmButton2(wfmStrings.ok(), WfmButton2.BTN_PRIMARY);
        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel());
        dialogBox.add(vpMain);
        dialogBox.addButton(cancel);
        dialogBox.addButton(ok);
        dialogBox.center();

        ok.addClickHandler(event -> {
            dontKeepDelays = dontKeepAllDelays.getValue();
            dialogBox.close();
            if (!validate() && !error) {
                return;
            }
            save();
        });

        cancel.addClickHandler(event -> dialogBox.close());
    }

    private void isItOkToChangeProject() {
        TaskService.App.get().projectStartedAlready(task.getProjectId(), new AbstractAsyncCallback<Boolean>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(Boolean startedAlready) {
                if (startedAlready) {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.confirmation());
                    messageBox.setMessage(wfmStrings.sureToEditTask());
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            if ("YES".equalsIgnoreCase(messageBox.getPressedButtonName())) {
                                if (!validate() && !error) {
                                    return;
                                }
                                save();
                            }
                        }
                    });
                    messageBox.open();
                } else {
                    if (!validate() && !error) {
                        return;
                    }
                    save();
                }
            }
        });
    }

    @Override
    public String getIconStyle() {
        return "edits task-edit";
    }

    private void save() {
        if (projectID != null) {
            task.setProjectId(projectID);
        }
        if (isChangeTasksProject && Utils.hasRole(ADMIN) && projects.getSelectedItem() != null) {
            task.setProjectId(projects.getSelectedItem().getId());
        }
        task.setNumber(number.getNumberData(true).getNumberString());
        task.setNumberData(number.getNumberData(true));

        if (Utils.isEnableBonnardCustomization()) {
            task.setName(nameSuggestBox.getText());
        } else {
            task.setName(name.getText());
        }
        task.setDescription(area.getText());
        if (priority.getSelectedItem() != null) {
            task.setPriorityId(priority.getSelectedItem().getId());
        }
        if (type.getSelectedItem() != null) {
            task.setTypeId(type.getSelectedItem().getId());
        }

        IdTime[] assignees = getSelectedAssignees();
        task.setAssigneeItems(assignees);
        if (Integer.valueOf(COMPLETED).equals(status.getListBox().getSelectedItem().getId()) && !Utils.hasGenericAccess(GenericSettingsEnum.PROJECT_PERCENT_OVER_HUNDRED)) {
            task.setPercent(100f);
        } else {
            if (percent.getText() != null && !percent.getText().equals("")) {
                String[] st = percent.getText().split("\\.");
                if (parseToDouble(st[0]) >= 100 && !Utils.hasGenericAccess(GenericSettingsEnum.PROJECT_PERCENT_OVER_HUNDRED)) {
                    task.setPercent(100f);
                } else {

                    task.setPercent((float) parseToDouble(percent.getText()));
                }
            } else {
                task.setPercent(0f);
            }
        }

        if ("true".equals(Utils.userSettings.get(RESOURCE_UTILIZATION_ENABLED)) && !Utils.hasGenericAccess(GenericSettingsEnum.ENABLED_MANNUAL_RU_ESTIMATE)) {
            //set recalculate hours on Resource Utilization tool
            task.setRecalculateResourceHours(recalculateResourceHours.getValue());
        }
        task.setBillable(billable.getValue());
        task.setStartDate(dateTime.getStartDate());
        task.setDueDate(dateTime.getDueDate());

        task.setStatusId(status.getListBox().getSelectedItem().getId());
        task.setUpdateTaskStatusForAll(status.getUpdateForAll());
        task.setUpdateAssignmentTaskStatus(!status.getUpdateForAll() && status.getUpdateForAssignment());
        if (editableAssignee == READ && Utils.hasPermission(PermissionConstants.PM_ASSIGN_TASK_TO_MEMBER)) {
            Info.show("You do not have the permission to change the assignment. Only a PM, Project Backup Manager, Company Director, or Company Administrator can make this change.", Info.Type.WARNING);
            task.setUpdateAssignmentTaskStatus(false);
        }

        task.setAllDay(dateTime.isAllDay());
        if (parentWorkstream.getWorkstream() != null) {
            task.setParentWSItem(new SelectItem(parentWorkstream.getWorkstream().getId()));
        } else {
            task.setParentWSItem(null);
        }
        if (predTasks.getTasks() != null && predTasks.getTasks().length > 0) {
            task.setPredecessorTaskItems(predTasks.getTasks());
        }
        if (succTasks.getTasks() != null && succTasks.getTasks().length > 0) {
            task.setSuccessorTaskItems(succTasks.getTasks());
        }
        task.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
        task.setReminders(reminders.getReminderDatas());
        if (firstClick.get()) {
            task.setRelations(task.getRelations());
        } else {
            task.setRelations(getLinkingUtil().getAddLinkSideNavBox().getSelectedRelations());
        }
        task.setDontKeepDelays(dontKeepDelays);
        if (forWhat != null && forWhat.equals(Constants.WORKFLOW)) {
            task.setAllDay(workflowDate.isAllDay());
            task.setWorkflowTask(true);
            task.setWorkflowID(task.getWorkflowID() != null ? task.getWorkflowID() : task.getWorkflowRelationID());
            task.setWorkflowStartDate(workflowDate.getWorkflowStartDate());
            task.setWorkflowDueDate(workflowDate.getWorkflowDueDateUnit());
            task.setWorkflowDueDateGranularity(workflowDate.getWorkflowDueDateGranularity());
            task.setWorkflowActionTimeBased(workflowTimeBased.getValue());
            if (task.isWorkflowActionTimeBased()) {
                task.setWorkflowActionStartTime(workflowActionTimeBasedDate.getWorkflowStartDate());
                task.setWorkflowActionStartTimeUnit(workflowActionTimeBasedDate.getWorkflowDueDateUnit());
                task.setWorkflowActionStartTimeGranularity(workflowActionTimeBasedDate.getWorkflowDueDateGranularity());
            }
        }

        task.setNonAssignedIncluded(allCompanyEmployees.getValue());

        LoadingPanel.loading(true);
        TaskService.App.get().updateTask(task, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                try {
                    throw caught;
                } catch (NumberExistingException ex) {
                    WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, ex.getDetailedMessage());
                    messageBox.setTitle(wfmStrings.error());
                    messageBox.open();
                } catch (Throwable ex) {
                    Info.show(wfmStrings.failed(), Info.Type.WARNING);
                }
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_EDIT, result, TaskEditView.this);
                Info.show(property.getSingular(wfmStrings.updated(), wfmStrings.task()), Info.Type.INFO);
                resetNonSavedFieldsCount();
                closeTab();
            }
        });
    }

    private boolean validate() {
        int errors = 0;
        clearErrorStyle();
        errors = super.customValidate();
        errors += markAsError(projects, isChangeTasksProject && Utils.hasRole(ADMIN) && projects.getSelectedItemID() == null);

        if (Utils.isEnableBonnardCustomization()) {
            errors += markAsError(name, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.NAME).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.NAME).getTitle() : wfmStrings.name(), name, formPropertyMap.get(CustomFormConstants.NAME).getMinChar()));
        } else {
            errors += markAsError(name, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.NAME).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.NAME).getTitle() : wfmStrings.name(), name, formPropertyMap.get(CustomFormConstants.NAME).getMinChar()
            ));
        }
        errors += markAsError(priority, priority.getSelectedId() == null);
        errors += markAsError(status, status.getListBox().getSelectedId() == null);
        if (dateTime.getStartDate() != null && dateTime.getDueDate() != null) {
            errors += markAsError(START_DATE, dateTime.startDate, !Validation.validateDateOrder(dateTime.getStartDate(), dateTime.getDueDate(), null, dateTime.isAllDay()));
        } else {
            if (dateTime.getStartDate() == null) {
                errors += markAsError(START_DATE, dateTime.startDate, true);
            } else if (dateTime.getDueDate() == null) {
                errors += markAsError(DUE_DATE, dateTime.dueDate, true);
            }
        }
        errors += getCustomFieldUtil().validateCustomFields();

        if (fieldMap != null) {
            if (fieldMap.get(CustomFormConstants.TASK.PREDECESSOR_TASK) != null) {
                Field predecessorTask = fieldMap.get(CustomFormConstants.TASK.PREDECESSOR_TASK);
                if (predecessorTask != null && predecessorTask.isRequired()) {
                    errors += markAsError(CustomFormConstants.TASK.PREDECESSOR_TASK, predTasks, !(predTasks.getTasks() != null && predTasks.getTasks().length > 0 && predTasks.getTasks()[0] != null));
                }
            }
            if (fieldMap.get(CustomFormConstants.TASK.SUCCESSOR_TASK) != null) {
                Field successorTask = fieldMap.get(CustomFormConstants.TASK.SUCCESSOR_TASK);
                if (successorTask != null && successorTask.isRequired()) {
                    errors += markAsError(CustomFormConstants.TASK.SUCCESSOR_TASK, succTasks, !(succTasks.getTasks() != null && succTasks.getTasks().length > 0 && succTasks.getTasks()[0] != null));
                }
            }
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;

    }

    private void refreshTaskDependencies(EditTask task) {
        Integer[] td = new Integer[(task.getPredecessorTaskItems() != null ? task.getPredecessorTaskItems().length : 0)
                + (task.getSuccessorTaskItems() != null ? task.getSuccessorTaskItems().length : 0) + 1];
        int inc = 0;
        if (task.getPredecessorTaskItems() != null) {
            for (int i = 0; i < task.getPredecessorTaskItems().length; i++) {
                if (task.getPredecessorTaskItems()[i] != null) {
                    td[inc] = task.getPredecessorTaskItems()[i].getId();
                    inc++;
                }
            }
        }
        if (task.getSuccessorTaskItems() != null) {
            for (int i = 0; i < task.getSuccessorTaskItems().length; i++) {
                if (task.getSuccessorTaskItems()[i] != null) {
                    td[inc] = task.getSuccessorTaskItems()[i].getId();
                    inc++;
                }
            }
        }
        td[td.length - 1] = task.getObjectID();
        predTasks.setTaskDependencies(td);
        succTasks.setTaskDependencies(td);
    }

    private String formatToDouble(String text) {
        return numberFormat.format(parseToDouble(text));
    }

    private double parseToDouble(String text) {
        return Double.parseDouble(text.replace(",", ""));
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
