package com.edatasite.workforce.gwt.task.client.ui;

import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ReferenceParentEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.form.Localize;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.project.WbsItem;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiCellTree;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.SelectionContainer;
import com.edatasite.workforce.gwt.core.client.ui.cell.IconCell;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.HMWidget;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ReferenceLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.VerticalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.view.Reminder;
import com.edatasite.workforce.gwt.core.client.ui.view.ReminderView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.ui.view.WorkflowDateSelecter;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.task.client.rpc.EditTask;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskServiceAsync;
import com.edatasite.workforce.gwt.task.client.rpc.WorkstreamSingleItem;
import com.edatasite.workforce.gwt.wfmTimer.client.ui.StopWatch;
import com.edatasite.workforce.gwt.workstream.client.ui.WorkstreamChooser;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.MultiSelectionModel;
import gwt.material.design.client.ui.MaterialLink;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: iskan
 * Date: Jan 12, 2008
 * Time: 7:51:15 PM To
 */
public class AddTaskView extends CustomForm2 implements HasLinksInterface, CommandConstants, Constants, Colapse {

    private String forWhat = null;
    private final static TaskServiceAsync taskService = TaskService.App.get();

    private static final ProjectStrings projectStrings = ProjectStrings.App.get();

    private TextBox name;
    private ReferenceLookUp nameSuggestBox;

    private Numbering number;
    private VerticalPanelDiv pnlClients;

    private DataListBox priority;
    private CRMLookUp project;
    private DataListBox status;
    private WorkstreamChooser parentWorkstream;
    private TasksChooser predecessorTasks;
    private TasksChooser successorTasks;
    private boolean saveAndClose = false;
    private boolean saveAndNew = false;
    private boolean dontKeepDelays = false;
    private Widget richText;
    private TextArea2 area;
    private KpiCellTree dynamicSelectorNew;
    private DataListBox dwSingleAssignee;
    private WfmButton2 addNewProject;
    private String projectId;
    private KpiCheckBox billable;
    private GeneralFileUpload fileUpload;
    private WbsItem workStream;
    private HMWidget timeSpent;
    private TextBox txtTaskAmount;

    private Integer projectID = null;
    private Integer taskID = null;
    private Integer basicTaskID = null;
    private Integer statusID = null;
    private Integer workStreamID = null;

    private Reminder reminder;
    private DateTimePicker taskTime;
    private WorkflowDateSelecter workflowDate;
    private KpiCheckBox workflowTimeBased;
    private WorkflowDateSelecter workflowActionTimeBasedDate;
    private ReminderView reminderView;
    private KpiCheckBox enableEmailReminder;
    private VerticalPanel recurringPanel;

    private String copiedTaskName;
    private String copiedCaseID;
    private String copiedTaskDescription;
    private KpiRadioButton allEmployees;
    private KpiRadioButton allCompanyEmployees;
    private NumberData numberData;
    private boolean crmTask;
    private boolean detailed = true;
    private StopWatch stopWatch;
    private FooterInformer link;
    AtomicBoolean firstClick = new AtomicBoolean(true);
    ArrayList<RelationItem> relationItems = new ArrayList<>();

    private boolean isAmongAssignees = false;
    private boolean isSelectedMyself = false;
    private HasLinks linkingUtil;
    private FormHasCustomField customFieldUtil;
    private NoteWidget2 noteWidget;
    private final ArrayList<CompanyCustomFieldItem> customFields = null;
    private LinkedHashMap<String, FormProperty> formPropertyMap;

    public AddTaskView(String projectID) {
        super("addtask");
        setDescription(property.getSingular(projectStrings.addTask(), wfmStrings.task()));
        if (projectID != null) {
            this.projectID = Integer.valueOf(projectID);
        }
    }

    public AddTaskView(Integer basicTaskID) {
        super("addtask");
        setDescription(property.getSingular(projectStrings.addTask(), wfmStrings.task()));
        if (basicTaskID != null) {
            this.basicTaskID = basicTaskID;
        }
    }

    public AddTaskView(String forWhat, String copiedTaskName, String copiedCaseID, String copiedTaskDescription, boolean detailed, ArrayList<RelationItem> relationItems) {
        super("addtask");
        setDescription(property.getSingular(projectStrings.addCrmTask(), wfmStrings.task()));
        this.forWhat = forWhat;
        this.crmTask = forWhat != null && (forWhat.equals(CrmConstants.CRM_TASK) || forWhat.equals(CrmConstants.CRM_TASK_SHORTEN));
        this.copiedTaskName = copiedTaskName;
        this.copiedCaseID = copiedCaseID;
        this.copiedTaskDescription = copiedTaskDescription;
        this.detailed = detailed;
        this.relationItems = relationItems;
    }

    public AddTaskView(String projectID, String workStreamID) {
        super("addtask");
        setDescription(property.getSingular(projectStrings.addTask(), wfmStrings.task()));
        if (!projectID.equals("")) {
            this.projectID = Integer.valueOf(projectID);
        }
        if (!workStreamID.equals("")) {
            this.workStreamID = Integer.valueOf(workStreamID);
        }
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(AddTaskView.this) {
                @Override
                protected boolean isActionEditing() {
                    return true;
                }

                @Override
                protected Integer getRelationID() {
                    return null;
                }

                @Override
                protected String getRelationType() {
                    return RelationItem.TYPE_TASK;
                }

                @Override
                protected String getRelationName() {
                    return null;
                }
            };
        }
        return linkingUtil;
    }

    private void initInternal() {
        super.onInitialize();
    }

    protected void registerFields() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_WORKSTREAM_ADD, AddTaskView.this, (sender, args) -> {
            if (parentWorkstream != null) {
                parentWorkstream.reInit();
                AllInOneService.App.get().getFirstLevelWorkstreams(Integer.parseInt(projectId), new AbstractAsyncCallback<WbsItem[]>() {
                    @Override
                    public void success(WbsItem[] result) {
                        if (result != null && result.length > 0) {
                            workStream = result[0];
                            parentWorkstream.setText(workStream.getName());
                            parentWorkstream.setWorkstream(workStream);
                        }
                    }
                });
            }
        });
        workflowDate = new WorkflowDateSelecter(true, false);
        workflowActionTimeBasedDate = new WorkflowDateSelecter(true, true);
        workflowActionTimeBasedDate.setVisible(false);
        workflowTimeBased = new KpiCheckBox(wfmStrings.executionTime());
        workflowTimeBased.addValueChangeHandler(booleanValueChangeEvent -> workflowActionTimeBasedDate.setVisible(booleanValueChangeEvent.getValue()));
        taskTime = new DateTimePicker(false, true);
        taskTime.startDate.ensureDebugId("Task_star_date");
        taskTime.startTime.ensureDebugId("Task_star_time");
        taskTime.dueDate.ensureDebugId("Task_due_date");
        taskTime.endTime.ensureDebugId("Task_end_time");

        //project list
        project = new CRMLookUp(LookUpConstants.PROJECT);
        project.setFullSearch(true);
        project.ensureDebugId("Task_project");

        //project add link
        addNewProject = new WfmButton2("", WfmButton2.BTN_WHITE);
        addNewProject.addStyleName("ficon--plus");
        addNewProject.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("project|add/add/pm"));
        new KpiToolTip(addNewProject, projectStrings.addProject());
        //task name
        name = new TextBox();
        name.ensureDebugId("Task_name");
        name.addStyleName(DEFAULT_WIDTH);

        TextArea txtArea = new TextArea();
        nameSuggestBox = new ReferenceLookUp(ReferenceParentEnum._TASK_FROM_TEMPLATE, txtArea);
        nameSuggestBox.setOpenIconVisibility(false);
        nameSuggestBox.setMaxSearchKey(10);

        //from CASE copied to task name
        if (copiedTaskName != null) {
            name.setText(copiedTaskName);
        }
        number = new Numbering();
        number.ensureDebugId("Task_number");
        number.setEnabled(Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_TASK_NUMBERING));
        //priority list
        priority = new DataListBox();
        priority.ensureDebugId("Task_priority");
        priority.addStyleName(DEFAULT_WIDTH);
        priority.setVisibleItemCount(1);
        taskService.getPriorities(new AbstractAsyncCallback<SelectItem[]>() {
            public void success(final SelectItem[] object) {
                Scheduler.get().scheduleDeferred(() -> {
                    priority.setItems(object);
                    addPredefinedValues(PRIORITY, object);
                    if (object != null) {
                        for (SelectItem anObject : object) {
//                            if (anObject.isSelected()) {
//                                priority.setSelected(anObject.getId());
//                            }
                        }
                    }
                });
            }
        });
        //workStream object
        parentWorkstream = new WorkstreamChooser();
        parentWorkstream.ensureDebugId("Task_parent_workstream");
        if (workStreamID != null) {
            taskService.getWorkstream(workStreamID, new AbstractAsyncCallback<WorkstreamSingleItem>() {
                @Override
                public void failure(Throwable caught) {
                }

                @Override
                public void success(final WorkstreamSingleItem result) {
                    parentWorkstream.setText(result.getName());
                    parentWorkstream.getWorkstreamNameBox().setEnabled(false);
                    if (saveAndClose) {
                        project.setEnabled(false);
                    }
                }
            });
        } else if (projectID != null) {
            if (saveAndClose) {
                project.setEnabled(false);
            }
        }

        parentWorkstream.getWorkstreamNameBox().addClickHandler(event -> {
            if (project.isSelected()) {
                parentWorkstream.publicShowShell();
            } else {
                Info.show(wfmStrings.pleaseSelectProjectFirst(), Info.Type.WARNING);
            }
        });
        //single assignee
        dwSingleAssignee = new DataListBox();
        dwSingleAssignee.addStyleName(DEFAULT_WIDTH);
        //assign container
        dynamicSelectorNew = new KpiCellTree();
        //project employees(selected project) radio button
        allEmployees = new KpiRadioButton("employee", Property.get(Constants.PROJECT, wfmStrings.projectEmployees(), wfmStrings.project()));
        //all employees(company) radio button
        allCompanyEmployees = new KpiRadioButton("employee", wfmStrings.all());
        dynamicSelectorNew.drawSelectedSide(new SelectionContainer() {
            @Override
            public void selectedDataGrid(final KpiDataGrid<KpiTreeInfo> selectedDataGrid, ColumnSortEvent.ListHandler<KpiTreeInfo> sortHandler, final MultiSelectionModel<KpiTreeInfo> selectionModel) {
                selectedDataGrid.addRowCountChangeHandler(event -> {
                    isSelectedMyself = false;
                    if (selectedDataGrid.getList().size() == 0) {
                        isAmongAssignees = false;
                        isSelectedMyself = true;
                        status.setSelected(NOT_STARTED);
                    }
                    for (KpiTreeInfo object : ((KpiDataGrid<KpiTreeInfo>) event.getSource()).getList()) {
                        if (object.getName().contains("Myself")) {
                            status.setEnabled(true);
                            isAmongAssignees = true;
                            break;
                        } else if (!isSelectedMyself) {
                            isAmongAssignees = false;
                            isSelectedMyself = true;
                            if (statusID == null) {
                                status.setSelected(NOT_STARTED);
                            } else {
                                status.setSelected(statusID);
                            }
                        }
                    }
                });
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
                selectedDataGrid.setColumnWidth(employee, 45, com.google.gwt.dom.client.Style.Unit.PCT);
                //estimate date
                Column<KpiTreeInfo, String> time = null;
                if (!Utils.hasGenericAccess(GenericSettingsEnum.ENABLED_MANNUAL_RU_ESTIMATE)) {
                    final TextInputCell textInputCell = new TextInputCell();
                    time = new Column<KpiTreeInfo, String>(textInputCell) {
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
                selectedDataGrid.setColumnWidth(action, 10, com.google.gwt.dom.client.Style.Unit.PCT);
            }

            @Override
            public void additionalActions(HTMLPanel actionsPanel) {
                if (Utils.hasUserMaxRoleID(MEM) || Utils.hasRole(CLIENT)) {
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

        dynamicSelectorNew.ensureDebugId("Task_assignees");
        //predecessor tasks
        predecessorTasks = new TasksChooser(true, TasksChooser.PREDECESSOR);
        predecessorTasks.ensureDebugId("Task_predecessor_tasks");
        //successor tasks
        successorTasks = new TasksChooser(true, TasksChooser.SUCCESSOR);
        successorTasks.ensureDebugId("Task_successor_tasks");
//        successorTasks.addStyleName(DEFAULT_WIDTH);

        predecessorTasks.setSelectionChange(() -> {
            if (successorTasks.resultsShell != null) {
                successorTasks.refreshResults();
            }
        });

        successorTasks.setSelectionChange(() -> {
            if (predecessorTasks.resultsShell != null) {
                predecessorTasks.refreshResults();
            }
        });
        successorTasks.setDeletePredOrSuccTask(() -> {
            if (predecessorTasks.resultsShell != null) {
                predecessorTasks.refreshResults();
            }
        });
        predecessorTasks.setDeletePredOrSuccTask(() -> {
            if (successorTasks.resultsShell != null) {
                successorTasks.refreshResults();
            }
        });
        HashMap<Integer, String[]> selectedTasks = new HashMap<>();
        predecessorTasks.setSelectedTasksMap(selectedTasks);
        successorTasks.setSelectedTasksMap(selectedTasks);

        predecessorTasks.setSucc_predChooser(successorTasks);
        successorTasks.setSucc_predChooser(predecessorTasks);

        predecessorTasks.setEnabled(false);
        successorTasks.setEnabled(false);

        //time tracker
        taskTime.setStartDate(new Date());
        taskTime.setDueDate(new Date());
        //project change listener
        project.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            projectChange();
            checkProjectBillable();
            allEmployees.setValue(true, true);
        });

        timeSpent = new HMWidget();
        txtTaskAmount = new TextBox();
        Validation.addNumericKeyboardListener(txtTaskAmount, 2);

        pnlClients = new VerticalPanelDiv();
        pnlClients.setSpacing(5);

        status = new DataListBox();
        status.ensureDebugId("Task_status");
        status.addStyleName(DEFAULT_WIDTH);
        status.setEnabled(true);
        CommonService.App.get().getAddTaskStatusDrop(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void success(SelectItem[] result) {
                status.setItems(result);
                addPredefinedValues(STATUS, result);
                if (result != null) {
//                    for (SelectItem aResult : result) {
//                        if (aResult.isSelected()) {
//                            status.setSelected(aResult.getId());
//                        }
//                    }
                }
            }
        });

        //description
        richText = createRichText();
        richText.setWidth("100%");
        richText.setHeight("200px");
        richText.ensureDebugId("Task_description");
        //billable
        billable = new KpiCheckBox("");
        billable.ensureDebugId("Task_billable");
        billable.setValue(true);
        //attachments
        fileUpload = new GeneralFileUpload(F_TASK, null, null);
        fileUpload.ensureDebugId("Task_task_attachments");

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PROJECT_ADD, AddTaskView.this, (sender, args) -> {
            if (args instanceof Integer && projectID == null) {
                projectID = (Integer) args;
            }
            initProjects();
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PROJECT_MEMBER_ADD, AddTaskView.this, (sender, args) -> {
            if (project.getSelectedItem() != null && project.getSelectedItem().getId().equals(args)) {
                reloadAssigneesTree();
            }
        });
        initProjects();
        checkUserForAssignPermission();
        if (basicTaskID != null) {
            getBasicTask();
        } else {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null) {
                addField(CustomFormConstants.STATUS, status, getTitle(formPropertyMap.get(CustomFormConstants.STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.STATUS).getTitle() : wfmStrings.status(), formPropertyMap.get(CustomFormConstants.STATUS).isRequired()), false,
                        formPropertyMap.get(CustomFormConstants.STATUS).isInformation());
                if (formPropertyMap.get(CustomFormConstants.STATUS).isInformation()) {
                    new KpiToolTip(status, formPropertyMap.get(CustomFormConstants.STATUS).getInformationText());
                }

                status.setEnabled(!formPropertyMap.get(CustomFormConstants.STATUS).isDisabled());
            } else {
                addField(STATUS, status, getTitle(wfmStrings.status(), true));
            }
        }
        recurringPanel = new VerticalPanel();
        enableEmailReminder = new KpiCheckBox();
        enableEmailReminder.ensureDebugId("Task_recurring_checkbox");
        recurringPanel.add(enableEmailReminder);
        enableEmailReminder.addValueChangeHandler(booleanValueChangeEvent -> {
            if (enableEmailReminder.getValue()) {
                reminderView = new ReminderView(SchedulerConstant.RECURRING_TASK_FORM);
                reminderView.setStyleName("reccurence-view");
                reminderView.getElement().getStyle().setLeft(-87, com.google.gwt.dom.client.Style.Unit.PX);
                reminderView.getElement().getStyle().setTop(10, com.google.gwt.dom.client.Style.Unit.PX);

                reminderView.drawForm(null);
                reminderView.setStartDate(taskTime.getStartDate());
                reminderView.setStart(taskTime.getStartDate());
                reminderView.hideNeverRadioButton();
                recurringPanel.add(reminderView);
                recurringPanel.addStyleName("recurringPanel");
            } else {
                recurringPanel.remove(reminderView);
                reminderView = null;
            }
        });
        noteWidget = new NoteWidget2(taskID, RelationItem.TYPE_TASK);
        noteWidget.addStyleName("file--AddTaskView");

        addFields();

        if (projectID == null) {
            setDefaultValuesByFormProperty();
        }
    }

    private void getBasicTask() {
        LoadingPanel.loading(true);
        taskService.getTaskForEdit(basicTaskID, new AbstractAsyncCallback<EditTask>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(EditTask result) {
                project.setSelected(new SelectItem(result.getProjectId(), result.getProjectName()));
                generateTaskNumber(new Date());
                if (Utils.hasGenericAccess(GenericSettingsEnum.SINGLE_ASSIGNEE_TO_TASK)) {
                    loadProjectEmployee(project.getSelectedItemID(), result.getFirstAssigneeId());
                } else {
                    reloadAssigneesTree();
                }

                if (Utils.isEnableBonnardCustomization()) {
                    reloadProjectClients();
                }

                name.setText(result.getName());

                if (Utils.isEnableBonnardCustomization() && nameSuggestBox != null) {
                    nameSuggestBox.addItem(new SelectItem(0, result.getName()));
                }
                area.setText(result.getDescription());
                priority.setSelected(result.getPriorityId());
                taskTime.setStartDate(new Date());
                taskTime.setDueDate(new Date());
                taskTime.setAllDay(result.isAllDay());
                if (result.getWorkflowID() != null) {
                    workflowDate.setStartDate(result.getWorkflowStartDate());
                    workflowDate.setDueDate(result.getWorkflowDueDate());
                    workflowDate.setDueDateGranularity(result.getWorkflowDueDateGranularity());
                    if (result.isWorkflowActionTimeBased()) {
                        workflowTimeBased.setValue(true, true);
                        workflowActionTimeBasedDate.setStartDate(result.getWorkflowActionStartTime());
                        workflowActionTimeBasedDate.setDueDate(result.getWorkflowActionStartTimeUnit());
                        workflowActionTimeBasedDate.setDueDateGranularity(result.getWorkflowActionStartTimeGranularity());
                    }
                }
                status.setSelected(result.getStatusId());
                statusID = result.getStatusId();
                if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null) {
                    addField(CustomFormConstants.STATUS, status, getTitle(formPropertyMap.get(CustomFormConstants.STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.STATUS).getTitle() : wfmStrings.status(), formPropertyMap.get(CustomFormConstants.STATUS).isRequired()));
                    status.setEnabled(!formPropertyMap.get(CustomFormConstants.STATUS).isDisabled());
                } else {
                    addField(STATUS, status, getTitle(wfmStrings.status(), true));
                }
                billable.setValue(result.getBillable());
                if (result.getParentWSItem() != null) {
                    parentWorkstream.setSelectedWorkstreamId(result.getParentWSItem().getId());
                }
                if (result.getParentWSItem() != null && result.getParentWSItem().getName() != null) {
                    parentWorkstream.setText(result.getParentWSItem().getName());
                    parentWorkstream.setWorkstream((WbsItem) result.getParentWSItem());
                }

                refreshTaskDependencies(result);

                predecessorTasks.setEnabled(true);
                successorTasks.setEnabled(true);

                HashMap<Integer, String[]> selectedTasks = new HashMap<>();
                predecessorTasks.setSelectedTasksMap(selectedTasks);
                predecessorTasks.setSucc_predChooser(successorTasks);
                predecessorTasks.setProjectId(result.getProjectId());
                predecessorTasks.setProjectName(result.getProjectName());
                predecessorTasks.setTaskId(result.getObjectID());
                predecessorTasks.clearTable();

                successorTasks.setSelectedTasksMap(selectedTasks);
                successorTasks.setSucc_predChooser(predecessorTasks);
                successorTasks.setProjectId(result.getProjectId());
                successorTasks.setTaskId(result.getObjectID());
                successorTasks.setProjectName(result.getProjectName());
                successorTasks.clearTable();
                if (result.getPredecessorTaskItems() != null && result.getPredecessorTaskItems().length > 0) {
                    for (int i = 0; i < result.getPredecessorTaskItems().length; i++) {
                        if (result.getPredecessorTaskItems()[i] != null) {
                            predecessorTasks.addTableItem(result.getPredecessorTaskItems()[i]);
                        }
                    }
                    predecessorTasks.refreshAddedTasks();
                }
                if (result.getSuccessorTaskItems() != null && result.getSuccessorTaskItems().length > 0) {
                    for (int i = 0; i < result.getSuccessorTaskItems().length; i++) {
                        if (result.getSuccessorTaskItems()[i] != null) {
                            successorTasks.addTableItem(result.getSuccessorTaskItems()[i]);
                        }
                    }
                    successorTasks.refreshAddedTasks();
                }
                reminder.setReminderDatas(result.getReminders());
                LoadingPanel.loading(false);
            }
        });

    }

    private void setDefaultValuesByFormProperty() {
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null && formPropertyMap.get(CustomFormConstants.NUMBER).getDefaultValue() != null) {
            number.getTxtNumber().setText(formPropertyMap.get(CustomFormConstants.NUMBER).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null && formPropertyMap.get(CustomFormConstants.NAME).getDefaultValue() != null) {
            name.setText(formPropertyMap.get(CustomFormConstants.NAME).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION).getDefaultValue() != null) {
            area.setText(formPropertyMap.get(CustomFormConstants.DESCRIPTION).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.START_DATE) != null && formPropertyMap.get(CustomFormConstants.START_DATE).getDefaultValue() != null) {
//            taskTime.startDate.setDate(new Date(formPropertyMap.get(CustomFormConstants.START_DATE).getDefaultValue()));
            if (!"".equals(formPropertyMap.get(CustomFormConstants.START_DATE).getDefaultValue()) && ("TODAY".equals(formPropertyMap.get(CustomFormConstants.START_DATE).getDefaultValue()) || "TOMORROW".equals(formPropertyMap.get(CustomFormConstants.START_DATE).getDefaultValue())
                    || "YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.START_DATE).getDefaultValue()))) {
                Date currentDate = new Date();
                if ("TOMORROW".equals(formPropertyMap.get(CustomFormConstants.START_DATE).getDefaultValue())) {
                    currentDate = DateUtil.addDays(currentDate, 1);
                } else if ("YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.START_DATE).getDefaultValue())) {
                    currentDate = DateUtil.minusDays(currentDate, 1);
                }
                taskTime.startDate.setDate(currentDate);
            } else {
                try {
                    taskTime.startDate.setDate(DateUtils.parse(formPropertyMap.get(CustomFormConstants.START_DATE).getDefaultValue()));
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DUE_DATE) != null && formPropertyMap.get(CustomFormConstants.DUE_DATE).getDefaultValue() != null
                && !"".equals(formPropertyMap.get(CustomFormConstants.DUE_DATE).getDefaultValue())) {
            if ("TODAY".equals(formPropertyMap.get(CustomFormConstants.DUE_DATE).getDefaultValue()) || "TOMORROW".equals(formPropertyMap.get(CustomFormConstants.DUE_DATE).getDefaultValue())
                    || "YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.DUE_DATE).getDefaultValue())) {
                Date currentDate = new Date();
                if ("TOMORROW".equals(formPropertyMap.get(CustomFormConstants.DUE_DATE).getDefaultValue())) {
                    currentDate = DateUtil.addDays(currentDate, 1);
                } else if ("YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.DUE_DATE).getDefaultValue())) {
                    currentDate = DateUtil.minusDays(currentDate, 1);
                }
                taskTime.dueDate.setDate(currentDate);
            } else {
                try {
                    taskTime.dueDate.setDate(DateUtils.parse(formPropertyMap.get(CustomFormConstants.DUE_DATE).getDefaultValue()));
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }

        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PRIORITY) != null && formPropertyMap.get(CustomFormConstants.PRIORITY).getDefaultValue() != null) {
            priority.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.PRIORITY).getSelectedId(), formPropertyMap.get(CustomFormConstants.PRIORITY).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null && formPropertyMap.get(CustomFormConstants.STATUS).getDefaultValue() != null) {
            status.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.STATUS).getSelectedId(), formPropertyMap.get(CustomFormConstants.STATUS).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.PROJECT) != null && formPropertyMap.get(CustomFormConstants.TASK.PROJECT).getDefaultValue() != null) {
            project.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.TASK.PROJECT).getSelectedId(), formPropertyMap.get(CustomFormConstants.TASK.PROJECT).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.TASK_AMOUNT) != null && formPropertyMap.get(CustomFormConstants.TASK.TASK_AMOUNT).getDefaultValue() != null) {
            txtTaskAmount.setText(formPropertyMap.get(CustomFormConstants.TASK.TASK_AMOUNT).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.TASK_NOTE) != null && noteWidget != null && formPropertyMap.get(CustomFormConstants.TASK.TASK_NOTE).getDefaultValue() != null) {
            noteWidget.getTextBox().setData(formPropertyMap.get(CustomFormConstants.TASK.TASK_NOTE).getDefaultValue());
        }
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
        predecessorTasks.setTaskDependencies(td);
        successorTasks.setTaskDependencies(td);
    }

    private void addFields() {
        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_ADD) && !crmTask && (forWhat == null || !forWhat.equals(WORKFLOW))) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.PROJECT) != null) {
                addField(CustomFormConstants.TASK.PROJECT, project, getTitle(formPropertyMap.get(CustomFormConstants.TASK.PROJECT).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.PROJECT).getTitle() : wfmStrings.project(), formPropertyMap.get(CustomFormConstants.TASK.PROJECT).isRequired()), false,
                        formPropertyMap.get(CustomFormConstants.TASK.PROJECT).isInformation());
                if (formPropertyMap.get(CustomFormConstants.TASK.PROJECT).isInformation()) {
                    new KpiToolTip(project, formPropertyMap.get(CustomFormConstants.TASK.PROJECT).getInformationText());
                }

                project.setEnabled(!formPropertyMap.get(CustomFormConstants.TASK.PROJECT).isDisabled());
            } else {
                addField(CustomFormConstants.TASK.PROJECT, new AdvancedInputGroup(null, project, addNewProject, true, false), getTitle(Property.get(Constants.PROJECT, wfmStrings.project()), true));
            }


        } else {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.PROJECT) != null) {
                addField(CustomFormConstants.TASK.PROJECT, project, getTitle(formPropertyMap.get(CustomFormConstants.TASK.PROJECT).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.PROJECT).getTitle() : wfmStrings.project(), formPropertyMap.get(CustomFormConstants.TASK.PROJECT).isRequired()), false,
                        formPropertyMap.get(CustomFormConstants.TASK.PROJECT).isInformation());
                if (formPropertyMap.get(CustomFormConstants.TASK.PROJECT).isInformation()) {
                    new KpiToolTip(project, formPropertyMap.get(CustomFormConstants.TASK.PROJECT).getInformationText());
                }

                project.setEnabled(!formPropertyMap.get(CustomFormConstants.TASK.PROJECT).isDisabled());
            } else {
                addField(CustomFormConstants.TASK.PROJECT, project, getTitle(Property.get(Constants.PROJECT, wfmStrings.project()), true));
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.CLIENT) != null) {
            addField(CustomFormConstants.TASK.CLIENT, pnlClients, getTitle(formPropertyMap.get(CustomFormConstants.TASK.CLIENT).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.CLIENT).getTitle() : wfmStrings.customer(), formPropertyMap.get(CustomFormConstants.TASK.CLIENT).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.TASK.CLIENT).isInformation());
            if (formPropertyMap.get(CustomFormConstants.TASK.CLIENT).isInformation()) {
                new KpiToolTip(pnlClients, formPropertyMap.get(CustomFormConstants.TASK.CLIENT).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TASK.CLIENT, pnlClients, getTitle(Property.get(Constants.CLIENT_LIST, wfmStrings.customer())));
        }

        //task number
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null) {
            addField(CustomFormConstants.NUMBER, number, getTitle(formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : wfmStrings.number(), formPropertyMap.get(CustomFormConstants.NUMBER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.NUMBER).isInformation());
            if (formPropertyMap.get(CustomFormConstants.NUMBER).isInformation()) {
                new KpiToolTip(number, formPropertyMap.get(CustomFormConstants.NUMBER).getInformationText());
            }

            number.setEnabled(!formPropertyMap.get(CustomFormConstants.NUMBER).isDisabled());
        } else {
            addField(CustomFormConstants.NUMBER, number, getTitle(wfmStrings.number(), true));
        }
        //task name
        if (Utils.isEnableBonnardCustomization()) {
            addField(NAME, nameSuggestBox, getTitle(wfmStrings.name(), true));
        } else {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null) {
                addField(CustomFormConstants.NAME, name, getTitle(formPropertyMap.get(CustomFormConstants.NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.NAME).getTitle() : wfmStrings.name(), formPropertyMap.get(CustomFormConstants.NAME).isRequired()), false,
                        formPropertyMap.get(CustomFormConstants.NAME).isInformation());
                if (formPropertyMap.get(CustomFormConstants.NAME).isInformation()) {
                    new KpiToolTip(name, formPropertyMap.get(CustomFormConstants.NAME).getInformationText());
                }

                name.setEnabled(!formPropertyMap.get(CustomFormConstants.NAME).isDisabled());
            } else {
                addField(NAME, name, getTitle(wfmStrings.name(), true));
            }
        }
        //task description
        addTitleField(CustomFormConstants.TASK.TASK_DETAILS, property.getSingular(wfmStrings.taskDetails(), wfmStrings.task()));
        ((TextArea2) richText).setText("");
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null) {
            addField(CustomFormConstants.DESCRIPTION, richText, getTitle(formPropertyMap.get(CustomFormConstants.DESCRIPTION).isChanged() ? formPropertyMap.get(CustomFormConstants.DESCRIPTION).getTitle() : wfmStrings.description(), formPropertyMap.get(CustomFormConstants.DESCRIPTION).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.DESCRIPTION).isInformation());
            if (formPropertyMap.get(CustomFormConstants.DESCRIPTION).isInformation()) {
                new KpiToolTip(richText, formPropertyMap.get(CustomFormConstants.DESCRIPTION).getInformationText());
            }
        } else {
            addField(DESCRIPTION, richText, null);
        }
        //start date
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.START_DATE) != null) {
            addField(CustomFormConstants.START_DATE, Utils.hasGenericAccess(GenericSettingsEnum.SHOW_START_TIME_END_TIME) ? new InputGroup(taskTime.startDate, taskTime.startTime) : taskTime.startDate, getTitle(formPropertyMap.get(CustomFormConstants.START_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.START_DATE).getTitle() : wfmStrings.startDate(), formPropertyMap.get(CustomFormConstants.START_DATE).isRequired()), false, formPropertyMap.get(CustomFormConstants.START_DATE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.START_DATE).isInformation()) {
                new KpiToolTip(taskTime.startDate, formPropertyMap.get(CustomFormConstants.START_DATE).getInformationText());
            }

            taskTime.startDate.setEnabled(!formPropertyMap.get(CustomFormConstants.START_DATE).isDisabled());
            taskTime.startTime.setEnabled(!formPropertyMap.get(CustomFormConstants.START_DATE).isDisabled());
        } else {
            addField(START_DATE, new InputGroup(taskTime.startDate, taskTime.startTime), getTitle(wfmStrings.startDate(), true));
        }
        //due date
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DUE_DATE) != null) {
            addField(CustomFormConstants.DUE_DATE, Utils.hasGenericAccess(GenericSettingsEnum.SHOW_START_TIME_END_TIME) ? new AdvancedInputGroup(new InputGroup(taskTime.dueDate, taskTime.endTime), taskTime.allDay) : taskTime.dueDate, getTitle(formPropertyMap.get(CustomFormConstants.DUE_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.DUE_DATE).getTitle() : wfmStrings.dueDate(), formPropertyMap.get(CustomFormConstants.DUE_DATE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.DUE_DATE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.DUE_DATE).isInformation()) {
                new KpiToolTip(taskTime.dueDate, formPropertyMap.get(CustomFormConstants.DUE_DATE).getInformationText());
            }
            taskTime.dueDate.setEnabled(!formPropertyMap.get(CustomFormConstants.DUE_DATE).isDisabled());
            taskTime.endTime.setEnabled(!formPropertyMap.get(CustomFormConstants.DUE_DATE).isDisabled());
        } else {
            addField(DUE_DATE, new AdvancedInputGroup(new InputGroup(taskTime.dueDate, taskTime.endTime), taskTime.allDay), getTitle(wfmStrings.dueDate(), true));
        }
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
        //assignees
        addTitleField(CustomFormConstants.TASK.TASK_ASSIGNEES, wfmStrings.assignees());

        if (Utils.hasGenericAccess(GenericSettingsEnum.SINGLE_ASSIGNEE_TO_TASK)) {
            if (Utils.hasRole(Constants.TIMESHEET_EDITOR)) {
                addField(ASSIGNEE, dwSingleAssignee, null);
            }
        } else {
            addField(ASSIGNEE, dynamicSelectorNew, null);
        }
        //billable

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.BILLIBLE) != null) {
            addField(CustomFormConstants.TASK.BILLIBLE, billable, getTitle(formPropertyMap.get(CustomFormConstants.TASK.BILLIBLE).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.BILLIBLE).getTitle() : wfmStrings.billable(), formPropertyMap.get(CustomFormConstants.TASK.BILLIBLE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.TASK.BILLIBLE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.TASK.BILLIBLE).isInformation()) {
                new KpiToolTip(billable, formPropertyMap.get(CustomFormConstants.TASK.BILLIBLE).getInformationText());
            }

            billable.setEnabled(!formPropertyMap.get(CustomFormConstants.TASK.BILLIBLE).isDisabled());
        } else {
            addField(CustomFormConstants.TASK.BILLIBLE, billable, getTitle(wfmStrings.billable()));
        }
        //priority
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PRIORITY) != null) {
            addField(CustomFormConstants.PRIORITY, priority, getTitle(formPropertyMap.get(CustomFormConstants.PRIORITY).isChanged() ? formPropertyMap.get(CustomFormConstants.PRIORITY).getTitle() : wfmStrings.priority(), formPropertyMap.get(CustomFormConstants.PRIORITY).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.PRIORITY).isInformation());
            if (formPropertyMap.get(CustomFormConstants.PRIORITY).isInformation()) {
                new KpiToolTip(priority, formPropertyMap.get(CustomFormConstants.PRIORITY).getInformationText());
            }

            priority.setEnabled(!formPropertyMap.get(CustomFormConstants.PRIORITY).isDisabled());
        } else {
            addField(PRIORITY, priority, getTitle(wfmStrings.priority(), false));
        }
        //stop watch
        addField(CustomFormConstants.TASK.STOPWATCH, stopWatch, " ");
        //time spent
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.TIME_SPENT) != null) {
            addField(CustomFormConstants.TASK.TIME_SPENT, timeSpent, getTitle(formPropertyMap.get(CustomFormConstants.TASK.TIME_SPENT).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.TIME_SPENT).getTitle() : wfmStrings.timeSpent(), formPropertyMap.get(CustomFormConstants.TASK.TIME_SPENT).isRequired()));
        } else {
            addField(CustomFormConstants.TASK.TIME_SPENT, timeSpent, getTitle(wfmStrings.timeSpent(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.TASK_AMOUNT) != null) {
            addField(CustomFormConstants.TASK.TASK_AMOUNT, txtTaskAmount, getTitle(formPropertyMap.get(CustomFormConstants.TASK.TASK_AMOUNT).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.TASK_AMOUNT).getTitle() : property.getSingular(wfmStrings.taskAmount(), wfmStrings.task()), formPropertyMap.get(CustomFormConstants.TASK.TASK_AMOUNT).isRequired()));
            txtTaskAmount.setEnabled(!formPropertyMap.get(CustomFormConstants.TASK.TASK_AMOUNT).isDisabled());
        } else {
            addField(CustomFormConstants.TASK.TASK_AMOUNT, txtTaskAmount, getTitle(property.getSingular(wfmStrings.taskAmount(), wfmStrings.task()), false));
        }
        //attachments
        addField(ATTACHMENTS, fileUpload, getTitle(wfmStrings.attachments(), false), true);

        //parent task
        WfmButton2 clearIcon = new WfmButton2("", WfmButton2.BTN_WHITE);
        clearIcon.addStyleName("btn--icon");
        clearIcon.add(new SvgIcon(SvgEnum.x));
        clearIcon.addClickHandler(sender -> {
            parentWorkstream.clearSelection();
            parentWorkstream.getWorkstreamNameBox().setEnabled(true);
        });
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.PARENT_WORKSTREAM) != null) {
            addField(CustomFormConstants.TASK.PARENT_WORKSTREAM, parentWorkstream, getTitle(formPropertyMap.get(CustomFormConstants.TASK.PARENT_WORKSTREAM).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.PARENT_WORKSTREAM).getTitle() : wfmStrings.workStream(), formPropertyMap.get(CustomFormConstants.TASK.PARENT_WORKSTREAM).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.TASK.PARENT_WORKSTREAM).isInformation());
            if (formPropertyMap.get(CustomFormConstants.TASK.PARENT_WORKSTREAM).isInformation()) {
                new KpiToolTip(parentWorkstream, formPropertyMap.get(CustomFormConstants.TASK.PARENT_WORKSTREAM).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TASK.PARENT_WORKSTREAM, new AdvancedInputGroup(null, parentWorkstream, clearIcon, true, false), getTitle(wfmStrings.workStream(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.PREDECESSOR_TASK) != null) {
            addField(CustomFormConstants.TASK.PREDECESSOR_TASK, predecessorTasks, getTitle(formPropertyMap.get(CustomFormConstants.TASK.PREDECESSOR_TASK).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.PREDECESSOR_TASK).getTitle() : wfmStrings.predeccessor(), formPropertyMap.get(CustomFormConstants.TASK.PREDECESSOR_TASK).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.TASK.PREDECESSOR_TASK).isInformation());
            if (formPropertyMap.get(CustomFormConstants.TASK.PREDECESSOR_TASK).isInformation()) {
                new KpiToolTip(predecessorTasks, formPropertyMap.get(CustomFormConstants.TASK.PREDECESSOR_TASK).getInformationText());
            }

            predecessorTasks.setEnabled(!formPropertyMap.get(CustomFormConstants.TASK.PREDECESSOR_TASK).isDisabled());
        } else {
            addField(CustomFormConstants.TASK.PREDECESSOR_TASK, predecessorTasks, getTitle(wfmStrings.predeccessor()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.SUCCESSOR_TASK) != null) {
            addField(CustomFormConstants.TASK.SUCCESSOR_TASK, successorTasks, getTitle(formPropertyMap.get(CustomFormConstants.TASK.SUCCESSOR_TASK).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.SUCCESSOR_TASK).getTitle() : wfmStrings.successor(), formPropertyMap.get(CustomFormConstants.TASK.SUCCESSOR_TASK).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.TASK.SUCCESSOR_TASK).isInformation());
            if (formPropertyMap.get(CustomFormConstants.TASK.SUCCESSOR_TASK).isInformation()) {
                new KpiToolTip(successorTasks, formPropertyMap.get(CustomFormConstants.TASK.SUCCESSOR_TASK).getInformationText());
            }

            successorTasks.setEnabled(!formPropertyMap.get(CustomFormConstants.TASK.SUCCESSOR_TASK).isDisabled());
        } else {
            addField(CustomFormConstants.TASK.SUCCESSOR_TASK, successorTasks, getTitle(wfmStrings.successor()));
        }

        /*task reminders*/
        reminder = new Reminder(false, "115px");
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.DUE_DATE_REMINDER) != null) {
            addField(CustomFormConstants.TASK.DUE_DATE_REMINDER, reminder, getTitle(formPropertyMap.get(CustomFormConstants.TASK.DUE_DATE_REMINDER).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.DUE_DATE_REMINDER).getTitle() : wfmStrings.duedatereminder(), formPropertyMap.get(CustomFormConstants.TASK.DUE_DATE_REMINDER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.TASK.DUE_DATE_REMINDER).isInformation());
            if (formPropertyMap.get(CustomFormConstants.TASK.DUE_DATE_REMINDER).isInformation()) {
                new KpiToolTip(reminder, formPropertyMap.get(CustomFormConstants.TASK.DUE_DATE_REMINDER).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TASK.DUE_DATE_REMINDER, reminder, getTitle(wfmStrings.duedatereminder(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.RECURRENING) != null) {
            addField(CustomFormConstants.TASK.RECURRENING, recurringPanel, getTitle(formPropertyMap.get(CustomFormConstants.TASK.RECURRENING).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.RECURRENING).getTitle() : wfmStrings.recurring(), formPropertyMap.get(CustomFormConstants.TASK.RECURRENING).isRequired()));
        } else {
            addField(CustomFormConstants.TASK.RECURRENING, recurringPanel, getTitle(wfmStrings.recurring()));
        }


//        if (Utils.hasPermission(PermissionConstants.PM_TASK_LINKS)) {
//            VerticalPanel linkAndLinkPanel = new VerticalPanel();
//            linkAndLinkPanel.ensureDebugId("Task_add_link");
//            linkAndLinkPanel.add(getLinkingUtil().getAddLink());
//            linkAndLinkPanel.add(getLinkingUtil().getLinksPanel());
//            addField(LINKS, linkAndLinkPanel, wfmStrings.links(), true);
//        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.TASK_NOTE) != null) {
            addField(CustomFormConstants.TASK.TASK_NOTE, noteWidget, getTitle(formPropertyMap.get(CustomFormConstants.TASK.TASK_NOTE).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.TASK_NOTE).getTitle() : wfmStrings.notes(), formPropertyMap.get(CustomFormConstants.TASK.TASK_NOTE).isRequired()), true,
                    formPropertyMap.get(CustomFormConstants.TASK.TASK_NOTE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.TASK.TASK_NOTE).isInformation()) {
                new KpiToolTip(noteWidget, formPropertyMap.get(CustomFormConstants.TASK.TASK_NOTE).getInformationText());
            }
            if (noteWidget.getTextBox() != null) {
                noteWidget.getTextBox().getMaterialRichEditor().setEnabled(!formPropertyMap.get(CustomFormConstants.TASK.TASK_NOTE).isDisabled());
            }
        } else {
            addField(CustomFormConstants.TASK.TASK_NOTE, noteWidget, wfmStrings.notes(), true);
        }

        addTitleField(CustomFormConstants.TASK.ADVANCED_OPTIONS, wfmStrings.advancedOptions());
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        if (saveAndNew && customFields != null) {
            getCustomFieldUtil().setCompanyCustomFieldItems(customFields);
        }
        getCustomFieldUtil().drawCustomFields(this, null);

        setDefaultValues();
    }

    private void checkUserForAssignPermission() {
        Integer projectId = project.getSelectedItemID();
        TaskService.App.get().getAssignEmployeeToProject(projectId, PermissionConstants.PM_ASSIGN_TASK_TO_MEMBER, new AbstractAsyncCallback<Boolean>() {
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

    private void initProjects() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setProjectId(projectID);
        fp.setCategory(TASK);
        CommonService.App.get().getProjects(fp, crmTask, Utils.hasGenericAccess(GenericSettingsEnum.PROJECT_NAME_SHOW_WITH_PROJECT_NUMBER), new AbstractAsyncCallback<ProjectItem[]>() {
            @Override
            public void success(final ProjectItem[] object) {
                if (object != null && object.length > 0) {
                    billable.setValue(object[0].isSelected());
                    project.setSelected(new SelectItem(object[0].getId(), object[0].getName()));
                }
                projectChange();
            }
        });
    }

    private void reInit() {
        registerFields();
        initForm();
//        addFields();
        isAmongAssignees = false;
        saveAndClose = false;
    }

    private void save() {
        if (!validate()) {
            return;
        }
        LoadingPanel.loading(true);
        saveTask();
    }

    private void chooseSuccessorsSaveMethod() {
        final DialogBox dialogBox = new DialogBox();
        dialogBox.setAnimationEnabled(true);
        dialogBox.setGlassEnabled(true);
        dialogBox.setText(projectStrings.pleaseChooseAnOption());
        VerticalPanel vpMain = new VerticalPanel();
        VerticalPanel vpRB = new VerticalPanel();

        RadioButton keepAllDelays = new KpiRadioButton("option");
        keepAllDelays.setText(projectStrings.keepTheLagTime());
        final RadioButton dontKeepAllDelays = new KpiRadioButton("option");
        dontKeepAllDelays.setText(projectStrings.doNotKeepTheLagTime());
        dontKeepAllDelays.addStyleName("dontKeepAllDelays");
        keepAllDelays.setValue(true);
        dontKeepAllDelays.setValue(false);
        vpRB.add(keepAllDelays);
        vpRB.add(dontKeepAllDelays);
        HorizontalPanel hpButtons = new HorizontalPanel();
        WfmButton2 ok = new WfmButton2(wfmStrings.ok());
        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel());
        hpButtons.add(ok);
        hpButtons.add(cancel);
        vpMain.add(new HTML(projectStrings.addindTaskMightShiftSuccessor()));
        vpMain.add(vpRB);
        vpMain.add(hpButtons);
        vpMain.setCellHorizontalAlignment(hpButtons, HasHorizontalAlignment.ALIGN_CENTER);
        vpMain.setSpacing(8);
        vpMain.addStyleName("DialogBox-table file--AddTaskView"); // Add Task with predecessor and Successor https://prnt.sc/rpdqoo
        dialogBox.setWidget(vpMain);
        dialogBox.show();

        ok.addClickHandler(event -> {
            dontKeepDelays = dontKeepAllDelays.getValue();
            dialogBox.hide();
            save();
        });
        cancel.addClickHandler(event -> dialogBox.hide());
    }

    private void saveTask() {
        enableButton(false);
        TaskSingleItem newTask = new TaskSingleItem();
        newTask.setBaseTaskID(basicTaskID);
        newTask.setProjectID(project.getSelectedItemID());
        if (stopWatch != null && stopWatch.isTimerStarted()) {
            ClockItem item = new ClockItem();
            item.setRelation(PM_TASK);
            item.setStartDate(stopWatch.getStartedTime());
            newTask.setTimer(item);
        }
        if (Utils.isEnableBonnardCustomization()) {
            newTask.setName(nameSuggestBox.getText());
        } else {
            newTask.setName(name.getText());
        }
        newTask.setDescription(area.getText());
        IdTime[] assignees = getSelectedAssignees();
        newTask.setPriorityID(priority.getSelectedId());
        newTask.setStartDate(taskTime.getStartDate());
        newTask.setDueDate(taskTime.getDueDate());
        newTask.setAllDay(taskTime.isAllDay());
        newTask.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
        newTask.setNotes(noteWidget.getNewNotesToSave());

        newTask.setStatusID(status.getSelectedId());

        if (timeSpent != null && timeSpent.getValueAsMinutes() > 0) {
            newTask.setActualTime(timeSpent.getValueAsMinutes());
        }

        if (txtTaskAmount != null && Utils.parseToBigDecimal(txtTaskAmount.getText()).compareTo(BigDecimal.ZERO) > 0) {
            newTask.setTaskAmount(Utils.parseToBigDecimal(txtTaskAmount.getText()));

            if (newTask.getActualTime() == null || newTask.getActualTime() == 0) {
                newTask.setActualTime(60); //one hour
            }
        }

        newTask.setBillable(billable.getValue());

        if (parentWorkstream.getWorkstream() != null) {
            newTask.setWorkstreamID(parentWorkstream.getWorkstream().getId());
        } else if (workStreamID != null) {
            newTask.setWorkstreamID(workStreamID);
        }
        newTask.setPredecessorTasks(predecessorTasks.getTasks());
        newTask.setSuccessorTasks(successorTasks.getTasks());
        newTask.setLastModified(new Date());
        newTask.setAttachments(fileUpload.getAttachedFiles());
        newTask.setReminder(reminder.getReminderDatas());

        if (enableEmailReminder.getValue()) {
            if (reminderView != null) {
                RecurrenceJobItem recurrenceJobItem = reminderView.getData();
                if (recurrenceJobItem != null) {
                    recurrenceJobItem.setStartDate(taskTime.getStartDate());
                    if (recurrenceJobItem.getType().equals(SchedulerConstant.RECURRENCE_TYPE_MONTHLY)) {
                        recurrenceJobItem.setMonthlyOrYearlyDay(taskTime.getStartDate().getDate());
                        recurrenceJobItem.setMonthlyOrYearlyPatternOption(SchedulerConstant.MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
                    }
                    newTask.setRecurrenceJobItem(recurrenceJobItem);
                }
            }
        }

        if (Utils.hasGenericAccess(GenericSettingsEnum.SINGLE_ASSIGNEE_TO_TASK)) {
            if (dwSingleAssignee.getSelectedId() != null) {
                ProjectMember member = projectMembersMap.get(dwSingleAssignee.getSelectedId());
                newTask.setProjectEmployees(new IdTime[]{new IdTime(member.getProjectEmployeeId(), null)});
            }
        } else {
            newTask.setProjectEmployees(assignees);
        }
        newTask.setInstancesCount(1);

        if (numberData != null) {
            numberData = number.getNumberData(true);
            newTask.setNumberData(numberData);
        }
        if (firstClick.get()) {
            newTask.setRelations(relationItems);
        } else {
            newTask.setRelations(getLinkingUtil().getAddLinkSideNavBox().getSelectedRelations());
        }
        LoadingPanel.loading(true);
        newTask.setDontKeepDelays(dontKeepDelays);

        if (Constants.WORKFLOW.equals(forWhat)) {
            newTask.setWorkflowTask(true);
            newTask.setWorkflowID(newTask.getWorkflowID() != null ? newTask.getWorkflowID() : newTask.getWorkflowRelationID());
            newTask.setWorkflowStartDate(workflowDate.getWorkflowStartDate());
            newTask.setWorkflowDueDate(workflowDate.getWorkflowDueDateUnit());
            newTask.setWorkflowDueDateGranularity(workflowDate.getWorkflowDueDateGranularity());
            newTask.setWorkflowActionTimeBased(workflowTimeBased.getValue());
            if (newTask.isWorkflowActionTimeBased()) {
                newTask.setWorkflowActionStartTime(workflowActionTimeBasedDate.getWorkflowStartDate());
                newTask.setWorkflowActionStartTimeUnit(workflowActionTimeBasedDate.getWorkflowDueDateUnit());
                newTask.setWorkflowActionStartTimeGranularity(workflowActionTimeBasedDate.getWorkflowDueDateGranularity());
            }
        }
        if (allCompanyEmployees.getValue()) {
            saveTaskMethodWithNewProjectEmployees(newTask);
        } else {
            saveTaskMethod(newTask);
        }
//        refreshOnDemand(new String[]{TASK_LIST, Constants.TIMESHEET});
    }

    @Override
    protected int validateNonStandartFields() {
        int error = 0;
        for (String fieldCode : getRequiredCodes()) {
            if (fieldCode != null) {
                if (CustomFormConstants.TASK.PARENT_WORKSTREAM.equals(fieldCode)) {
                    error += markAsError(parentWorkstream, parentWorkstream.getWorkstream() == null);
                }
            }
        }
        return error;
    }

    private void saveTaskMethodWithNewProjectEmployees(TaskSingleItem newTask) {
        taskService.saveTaskWithNewProjectEmployees(newTask, new AbstractAsyncCallback<Integer[]>() {
            @Override
            public void failure(Throwable caught) {
                failureSaveTask(caught);
            }

            @Override
            public void success(Integer[] result) {
                successfullySaveTask(result);
            }
        });
    }

    private void saveTaskMethod(TaskSingleItem newTask) {
        taskService.saveTask(newTask, new AbstractAsyncCallback<Integer[]>() {
            @Override
            public void failure(Throwable caught) {
                failureSaveTask(caught);
            }

            @Override
            public void success(Integer[] result) {
                successfullySaveTask(result);
            }
        });
    }

    private void failureSaveTask(Throwable caught) {
        LoadingPanel.loading(false);
        try {
            throw caught;
        } catch (NumberExistingException ex) {
            WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, ex.getDetailedMessage());
            messageBox.setTitle(wfmStrings.error());
            messageBox.open();
        } catch (Throwable ex) {
            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
        }
        enableButton(true);
    }

    private void successfullySaveTask(Integer[] result) {
        projectID = result[0];
        taskID = result[1];
//        refreshOnDemand("addissue", TASK_ADD);
        LoadingPanel.loading(false);
        onShellOk();
        Info.show(property.getSingular(wfmStrings.messSuccessfullyAdded(), wfmStrings.task()), Info.Type.INFO);
        enableButton(true);
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_ADD, result, this);
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CALENDAR_EVENT_ADD, taskID, this);

        if (timeSpent != null && timeSpent.getValueAsMinutes() > 0 ||
                txtTaskAmount != null && Utils.parseToBigDecimal(txtTaskAmount.getText()).compareTo(BigDecimal.ZERO) > 0) {
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LOAD_LOGGED_TIMES, null, AddTaskView.this);
        }

    }

    private boolean validate() {
        int errors = 0;
        boolean isTimeFormatError = false;
        boolean isEstimatedTimeError = false;
        clearErrorStyle();

        errors = super.customValidate();
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null && formPropertyMap.get(CustomFormConstants.NUMBER).isRequired()) {
            errors += markAsError(CustomFormConstants.NUMBER, number, !number.validate());
        }

        if (Utils.isEnableBonnardCustomization()) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null && formPropertyMap.get(CustomFormConstants.NAME).isRequired()) {
                errors += markAsError(name, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.NAME).isChanged() ?
                        formPropertyMap.get(CustomFormConstants.NAME).getTitle() : wfmStrings.name(), name, formPropertyMap.get(CustomFormConstants.NAME).getMinChar()));
            }
        } else {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null && formPropertyMap.get(CustomFormConstants.NAME).isRequired()) {
                errors += markAsError(name, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.NAME).isChanged() ?
                        formPropertyMap.get(CustomFormConstants.NAME).getTitle() : wfmStrings.name(), name, formPropertyMap.get(CustomFormConstants.NAME).getMinChar()));
            } else {
                errors += markAsError(name, name.getText() == null || "".equals(name.getText().trim()));
            }
        }


        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION).isRequired()) {
            errors += markAsError(area, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.DESCRIPTION).isChanged() ? formPropertyMap.get(CustomFormConstants.DESCRIPTION).getTitle() : wfmStrings.description(), area.getTextArea(), formPropertyMap.get(CustomFormConstants.DESCRIPTION).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.START_DATE) != null && formPropertyMap.get(CustomFormConstants.START_DATE).isRequired()) {
            errors += markAsError(START_DATE, taskTime.startDate, !Validation.validateDateOrder(taskTime.getStartDate(), taskTime.getDueDate(), null, taskTime.isAllDay()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DUE_DATE) != null && formPropertyMap.get(CustomFormConstants.DUE_DATE).isRequired()) {
            errors += markAsError(DUE_DATE, taskTime.dueDate, !Validation.validateDateOrder(taskTime.getStartDate(), taskTime.getDueDate(), null, taskTime.isAllDay()));
        }

//        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WORKFLOW_DATE) != null && formPropertyMap.get(CustomFormConstants.WORKFLOW_DATE).isRequired()) {
//            errors += markAsError(workflowDate, !workflowDate.isAttached());
//        }

//        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WORKFLOW_TIME_BASED) != null && formPropertyMap.get(CustomFormConstants.WORKFLOW_TIME_BASED).isRequired()) {
//            errors += markAsError(workflowTimeBased, !workflowTimeBased.isAttached());
//        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PRIORITY) != null && formPropertyMap.get(CustomFormConstants.PRIORITY).isRequired()) {
            errors += markAsError(CustomFormConstants.PRIORITY, priority, priority.getSelectedId() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null && formPropertyMap.get(CustomFormConstants.STATUS).isRequired()) {
            errors += markAsError(CustomFormConstants.STATUS, status, !Validation.validateListBoxRequired(status));
        }

//        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.TIME_SPENT) != null && formPropertyMap.get(CustomFormConstants.TASK.TIME_SPENT).isRequired()) {
//            errors += markAsError(CustomFormConstants.TASK.TIME_SPENT, timeSpent, timeSpent.isAttached());
//        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.TASK_AMOUNT) != null && formPropertyMap.get(CustomFormConstants.TASK.TASK_AMOUNT).isRequired()) {
            errors += markAsError(txtTaskAmount, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.TASK.TASK_AMOUNT).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.TASK.TASK_AMOUNT).getTitle() : wfmStrings.taskAmount(), txtTaskAmount, formPropertyMap.get(CustomFormConstants.TASK.TASK_AMOUNT).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.PARENT_WORKSTREAM) != null && formPropertyMap.get(CustomFormConstants.TASK.PARENT_WORKSTREAM).isRequired()) {
            errors += markAsError(parentWorkstream, parentWorkstream.getWorkstream() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.PREDECESSOR_TASK) != null && formPropertyMap.get(CustomFormConstants.TASK.PREDECESSOR_TASK).isRequired()) {
            errors += markAsError(CustomFormConstants.TASK.PREDECESSOR_TASK, predecessorTasks, !(predecessorTasks.getTasks() != null && predecessorTasks.getTasks().length > 0 && predecessorTasks.getTasks()[0] != null));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LEAD_SOURCE) != null && formPropertyMap.get(CustomFormConstants.LEAD_SOURCE).isRequired()) {
            errors += markAsError(CustomFormConstants.LEAD_SOURCE, pnlClients, pnlClients.getClass() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.SUCCESSOR_TASK) != null && formPropertyMap.get(CustomFormConstants.TASK.SUCCESSOR_TASK).isRequired()) {
            errors += markAsError(CustomFormConstants.TASK.SUCCESSOR_TASK, successorTasks, !(successorTasks.getTasks() != null && successorTasks.getTasks().length > 0 && successorTasks.getTasks()[0] != null));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.DUE_DATE_REMINDER) != null && formPropertyMap.get(CustomFormConstants.TASK.DUE_DATE_REMINDER).isRequired()) {
            errors += markAsError(reminder, !reminder.validateDueReminder());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.RECURRENING) != null && formPropertyMap.get(CustomFormConstants.TASK.RECURRENING).isRequired()) {
            errors += markAsError(CustomFormConstants.TASK.RECURRENING, enableEmailReminder, !Validation.validateCheckBoxRequired(enableEmailReminder, null));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.TASK_NOTE) != null && formPropertyMap.get(CustomFormConstants.TASK.TASK_NOTE).isRequired()) {
//            if (noteWidget != null && !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.TASK.TASK_NOTE).isChanged() ? formPropertyMap.get(CustomFormConstants.TASK.TASK_NOTE).getTitle() : wfmStrings.notes(), noteWidget.getTextBox().getTextArea(), formPropertyMap.get(CustomFormConstants.TASK.TASK_NOTE).getMinChar())) {
//                errors++;
//            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TASK.PROJECT) != null && formPropertyMap.get(CustomFormConstants.TASK.PROJECT).isRequired()) {
            errors += markAsError(CustomFormConstants.TASK.PROJECT, project, project.getSelectedItemID() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null && formPropertyMap.get(CustomFormConstants.STATUS).isRequired()) {
            errors += markAsError(STATUS, status, isAmongAssignees && status.getSelectedId() == null);
        }
//        errors += markAsError(START_DATE, taskTime.startDate, !Validation.validateDateOrder(taskTime.getStartDate(), taskTime.getDueDate(), null, taskTime.isAllDay()));
        if (taskTime.getStartDate() != null && taskTime.getDueDate() != null) {
            errors += markAsError(START_DATE, taskTime.startDate, !Validation.validateDateOrder(taskTime.getStartDate(), taskTime.getDueDate(), null, taskTime.isAllDay()));
        } else {
            if (taskTime.getStartDate() == null) {
                errors += markAsError(START_DATE, taskTime.startDate, true);
            } else if (taskTime.getDueDate() == null) {
                errors += markAsError(DUE_DATE, taskTime.dueDate, true);
            }
        }
        if (Utils.hasGenericAccess(GenericSettingsEnum.SINGLE_ASSIGNEE_TO_TASK)) {
            errors += markAsError(CustomFormConstants.ASSIGNEE, dwSingleAssignee, dwSingleAssignee.getSelectedId() == null);
        } else {
            if (dynamicSelectorNew.getSelectedData() == null || dynamicSelectorNew.getSelectedData().isEmpty()) {
                errors += markAsError(CustomFormConstants.ASSIGNEE, dynamicSelectorNew, true);
            } else {
                if (Utils.hasGenericAccess(GenericSettingsEnum.ESTIMATED_TIME_REQUIRED_ENABLED)) {

                    boolean hasMissingTime = dynamicSelectorNew.getSelectedData()
                            .stream()
                            .anyMatch(info -> info.getTime() == null);

                    errors += markAsError(CustomFormConstants.ASSIGNEE, dynamicSelectorNew, hasMissingTime);

                    isEstimatedTimeError = hasMissingTime;
                }
            }
        }
        errors += getCustomFieldUtil().validateCustomFields();
        if (fieldMap != null) {
            if (fieldMap.get(CustomFormConstants.TASK.PREDECESSOR_TASK) != null) {
                Field predecessorTask = fieldMap.get(CustomFormConstants.TASK.PREDECESSOR_TASK);
                if (predecessorTask != null && predecessorTask.isRequired()) {
                    errors += markAsError(CustomFormConstants.TASK.PREDECESSOR_TASK, predecessorTasks, !(predecessorTasks.getTasks() != null && predecessorTasks.getTasks().length > 0 && predecessorTasks.getTasks()[0] != null));
                }
            }
            if (fieldMap.get(CustomFormConstants.TASK.SUCCESSOR_TASK) != null) {
                Field successorTask = fieldMap.get(CustomFormConstants.TASK.SUCCESSOR_TASK);
                if (successorTask != null && successorTask.isRequired()) {
                    errors += markAsError(CustomFormConstants.TASK.SUCCESSOR_TASK, successorTasks, !(successorTasks.getTasks() != null && successorTasks.getTasks().length > 0 && successorTasks.getTasks()[0] != null));
                }
            }
        }
        if (errors > 0) {
            if (isTimeFormatError && errors == 1) {
                Info.show(wfmStrings.enterTimeSupportedFormats(), Info.Type.WARNING);
            } else if(isEstimatedTimeError) {
                Info.show("Please enter estimated time", Info.Type.WARNING);
            } else {
                Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            }

            return false;
        }
        return true;
    }

    private void isItOkToChangeProject() {
        taskService.projectStartedAlready(project.getSelectedItemID(), new AbstractAsyncCallback<Boolean>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(Boolean startedAlready) {
                if (startedAlready) {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.confirmation());
                    messageBox.setMessage(wfmStrings.sureToAddTask());
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            if ("YES".equalsIgnoreCase(messageBox.getPressedButtonName())) {
                                save();
                            } else {
                                //it's ok do nothing
                            }
                        }
                    });
                    messageBox.open();
                } else {
                    save();
                }
            }
        });
    }

    private void onShellOk() {
        if (saveAndClose) {
            closeTab();
            SinksContainerFactory.entryPoint.onHistoryChanged("task|summary/" + taskID);
        } else if (saveAndNew) {
            closeTab();
            SinksContainerFactory.entryPoint.onHistoryChanged("task|add/add");
//            onInitialize();
        } else {
            reInit();
        }
    }

    private Widget createRichText() {
        area = new TextArea2(Constants.DEFAULT_DESCRIPTION_CHARACTER_LIMIT);
        //from CASE copied to task description
        if (CONVERT_TO_TASK_FROM_CASE.equals(copiedTaskDescription) && copiedCaseID != null) {
            Integer caseID = Integer.valueOf(copiedCaseID);
            AllInOneService.App.get().getCaseDescription(caseID, true, new AbstractAsyncCallback<String>() {
                @Override
                public void success(String result) {
                    area.setText(result);
                }
            });
        }
        return area;
    }

    @Override
    protected Widget onInitialize() {
        if (!saveAndNew || customFields == null) {
            CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Task, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
                @Override
                public void failure(Throwable throwable) {
                    super.failure(throwable);
                    initInternal();
                }

                @Override
                public void success(CompanyCfAndPropertyItems result) {
                    super.success(result);
                    formPropertyMap = result.getFormPropertyMap();
                    if (result != null) {
                        getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                    }
                    initInternal();
                }

            });
        }
        return null;
    }

    @Override
    protected void initPredefinedValues() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected String getWikiCode() {
        return crmTask ? PermissionConstants.CRM_TASKS_ADD : PermissionConstants.PM_TASKS_ADD;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    public HistoryProcessor getProcessor() {
        return null;
    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            Localize.getInstance().localizeByFieldID(getFormID(), fieldID);
        }
        return null;
    }

    @Override
    protected void getDataToFillFields() {
    }

    @Override
    protected String getFormID() {
        return detailed ? LayoutRPC.TASK_MAX_FORM : forWhat != null && forWhat.equals(Constants.WORKFLOW) ? LayoutRPC.WORKFLOW_TASK_MIN_FORM : LayoutRPC.TASK_MIN_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected void addButtons() {
        link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);
        footer.addToLeftSide(link);

        link.addClickHandler(event -> {
            if (firstClick.get()) {
                getLinkingUtil().getAddLinkSideNavBox();
                getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(relationItems, true);

                firstClick.set(false);
            } else {
                getLinkingUtil().getAddLinkSideNavBox().show();
            }
        });

        MaterialLink save = new MaterialLink(wfmStrings.save());
        MaterialSplitButton splitButton = new MaterialSplitButton(save);

        save.addClickHandler(event -> {
            saveAndClose = true;
            saveAndNew = false;
            if (successorTasks.getTasks() != null && successorTasks.getTasks().length > 0 && successorTasks.getTasks()[0] != null) {
                chooseSuccessorsSaveMethod();
            } else {
                if ((predecessorTasks.getTasks() != null && predecessorTasks.getTasks().length > 0 && predecessorTasks.getTasks()[0] != null) ||
                        (successorTasks.getTasks() != null && successorTasks.getTasks().length > 0 && successorTasks.getTasks()[0] != null) ||
                        parentWorkstream.getWorkstream() != null) {
                    isItOkToChangeProject();
                } else {
                    save();
                }
            }
        });

        MaterialLink saveAdd = new MaterialLink(wfmStrings.saveAndNew());
        saveAdd.addClickHandler(event -> {
            saveAndClose = false;
            saveAndNew = true;
            if (successorTasks.getTasks() != null && successorTasks.getTasks().length > 0 && successorTasks.getTasks()[0] != null) {
                chooseSuccessorsSaveMethod();
            } else {
                if ((predecessorTasks.getTasks() != null && predecessorTasks.getTasks().length > 0 && predecessorTasks.getTasks()[0] != null) ||
                        (successorTasks.getTasks() != null && successorTasks.getTasks().length > 0 && successorTasks.getTasks()[0] != null) ||
                        parentWorkstream.getWorkstream() != null) {
                    isItOkToChangeProject();
                } else {
                    save();
                }
            }
            allEmployees.setValue(true, true);
        });

        splitButton.addItem(saveAdd);
        addButton(splitButton);

//        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
//        cancel.addClickHandler(clickEvent -> closeTab());
//        addButton(cancel);

        if (!detailed && this.crmTask) {//faqat case uchun qilingan
            addButton(wfmStrings.showDetails(), null, "showDetails", clickEvent -> {
                if (getLinkingUtil().getAddLinkSideNavBox() != null && getLinkingUtil().getAddLinkSideNavBox().getSelectedRelations() != null && getLinkingUtil().getAddLinkSideNavBox().getSelectedRelations().size() > 0) {
                    RelationItem[] predefinedTags = null;
                    predefinedTags = getLinkingUtil().getAddLinkSideNavBox().getSelectedRelations().toArray(new RelationItem[]{});
                    if (!detailed && predefinedTags[0] != null && predefinedTags[0].getToType() != null && predefinedTags[0].getToID() != null && RelationItem.TYPE_CASE.equals(predefinedTags[0].getToType())) {
                        closeTab();
//                            SinksContainerFactory.entryPoint.onHistoryChanged("task|add/add" + "/" + CrmConstants.CRM_TASK + "/" + predefinedTags[0].getToID() + "/" + predefinedTags[0].getToType() + "/" + predefinedTags[0].getToName() + "/" + CONVERT_TO_TASK_FROM_CASE);
                        SinksContainerFactory.entryPoint.onHistoryChanged("task|add/add" + "/" + CrmConstants.CRM_TASK + "/" + predefinedTags[0].getToID() + "/" + predefinedTags[0].getToType() + "/" + predefinedTags[0].getToName() + "/" + predefinedTags[0].getToName() + "/" + CONVERT_TO_TASK_FROM_CASE);
                    }
                }
            });
        }
    }

    private void reloadAssigneesTree() {
        if (project.getSelectedItem() != null) {
            projectId = project.getSelectedItemID().toString();
            LoadingPanel.loading(true);
            LinkedHashMap<Integer, Integer> usersList = null;
            if (dynamicSelectorNew.getSelectedEntityIDs() != null) {
                usersList = dynamicSelectorNew.getSelectedEmployeeIDsWithStatus();
            }
            taskService.getAssigneesWithTreeInfoLinkedHashMapWithParams(usersList, project.getSelectedItem().getId(), basicTaskID, LayoutRPC.TASK_MIN_FORM.equals(getFormID()) || Utils.isEnableBonnardCustomization(), new AbstractAsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>>() {
                @Override
                public void success(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> result) {
                    if (dynamicSelectorNew.getSelectAll() != null && dynamicSelectorNew.getSelectAll().getValue()) {
                        dynamicSelectorNew.getSelectAll().setValue(false);
                    }
                    dynamicSelectorNew.setItems(result);
                    LoadingPanel.loading(false);
                }
            });
        } else {
            dynamicSelectorNew.clear();
        }
    }

    private void reloadProjectClients() {
        if (project.getSelectedItemID() != null)
            ProjectService.App.get().getProjectClients(project.getSelectedItemID(), new AsyncCallback<SelectItem[]>() {
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
        if (dynamicSelectorNew.getSelectedData() != null && dynamicSelectorNew.getSelectedData().size() > 0) {
            for (KpiTreeInfo save : dynamicSelectorNew.getSelectedData()) {
                iTime = new IdTime(save.getId(), save.getTime());
                items.add(iTime);
            }
        }
        return items.toArray(new IdTime[items.size()]);
    }

    private void projectChange() {
        parentWorkstream.reInit();
        if (project.getSelectedItem() != null) {
            parentWorkstream.addStyleName(DEFAULT_WIDTH);
            parentWorkstream.setProjectId(project.getSelectedItem().getId());
            parentWorkstream.setProjectName(project.getSelectedItem().getName());
            if (workStreamID != null) {
                taskService.getFirstLevelWorkstreams(projectID, workStreamID, new AbstractAsyncCallback<WbsItem>() {
                    @Override
                    public void success(WbsItem result) {
                        workStream = result;
                        if (workStream != null) {
                            parentWorkstream.setText(workStream.getName());
                            parentWorkstream.setWorkstream(workStream);
                            parentWorkstream.getWorkstreamNameBox().setEnabled(false);
                        }
                    }
                });
            }
            predecessorTasks.setEnabled(true);
            successorTasks.setEnabled(true);
            predecessorTasks.setProjectId(project.getSelectedItem().getId());
            predecessorTasks.setProjectName(project.getSelectedItem().getName());
            successorTasks.setProjectId(project.getSelectedItem().getId());
            successorTasks.setProjectName(project.getSelectedItem().getName());
            predecessorTasks.clearTable();
            successorTasks.clearTable();
            if (project.getSelectedItem().getId() != 0) {

                if (Utils.hasGenericAccess(GenericSettingsEnum.SINGLE_ASSIGNEE_TO_TASK)) {
                    loadProjectEmployee(project.getSelectedItemID(), Utils.getUserID());
                } else {
                    reloadAssigneesTree();
                }
                reloadProjectClients();
            }
        }
        if (project.getSelectedItem() == null) {
            predecessorTasks.setEnabled(false);
            successorTasks.setEnabled(false);
        }

        if (project.isSelected()) {
            generateTaskNumber(taskTime.getStartDate());
        }
    }

    private void checkProjectBillable() {
        if (project.getSelectedItem() != null) {
            CommonService.App.get().checkProjectBillable(project.getSelectedItem().getId(), new AsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(Boolean isBillable) {
                    if (isBillable != null && !isBillable) {
                        billable.setValue(false);
                    } else if (isBillable != null && isBillable) {
                        billable.setValue(true);
                    }
                }
            });
        }
    }

    private void generateTaskNumber(Date startDate) {
        taskService.generateTaskNumber(project.getSelectedItemID(), startDate, null, new AbstractAsyncCallback<NumberData>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void success(NumberData result) {
                numberData = result;
                number.setNumberData(numberData);
            }
        });
    }

    HashMap<Integer, ProjectMember> projectMembersMap = new HashMap<>();

    private void loadProjectEmployee(Integer projectID, Integer userID) {
        if (projectID == null) {
            return;
        }

        ProjectService.App.get().getProjectEmployees(projectID, new AsyncCallback<ProjectMember[]>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ProjectMember[] projectMembers) {
                projectMembersMap.clear();
                ArrayList<SelectItem> items = new ArrayList<>();
                for (ProjectMember member : projectMembers) {
                    items.add(new SelectItem(member.getId(), member.getName()));
                    projectMembersMap.put(member.getId(), member);
                }
                dwSingleAssignee.clear();
                dwSingleAssignee.setItems(items.toArray(new SelectItem[]{}));
                if (userID != null) {
                    dwSingleAssignee.setSelected(userID);
                }
            }
        });
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