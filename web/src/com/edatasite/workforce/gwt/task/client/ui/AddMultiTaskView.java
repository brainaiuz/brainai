package com.edatasite.workforce.gwt.task.client.ui;


import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.project.WbsItem;
import com.edatasite.workforce.gwt.core.client.rpc.task.MultiTaskList;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxRow;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldBuilder;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.AddListener;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.ColumnStatements;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.upload.MultiUploadForm;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskServiceAsync;
import com.edatasite.workforce.gwt.task.client.rpc.WorkstreamSingleItem;
import com.edatasite.workforce.gwt.workstream.client.ui.WorkstreamChooser;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: iskan
 * Date: Jan 12, 2008
 * Time: 7:51:15 PM
 */
public class AddMultiTaskView extends FooteredView implements CommandConstants, Constants, Colapse, FittedContent {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();

    private WfmForm.Field priorityField;
    private TextBox name;
    private DataListBox priority;
    private CRMLookUp project;
    private WorkstreamChooser parentWorkstream;
    private DatePicker startDate;
    private DatePicker dueDate;
    private MaterialLink saveButton;
    private MaterialLink saveAndCloseButton;
    private boolean saveAndClose;
    //    private MaterialLink addNewProject;
    private PositionsSelectItem[] items;
    private TextArea2 description;
    private final Integer defaultDescriptionCharacterLimit = Constants.DEFAULT_DESCRIPTION_CHARACTER_LIMIT;
    private Integer workStreamID;
    private static int COLUMNS_COUNT = 7;
    private DynamicTable dynamicTable;
    private VerticalPanel verticalPanel;
    private SelectItem[] prioList;
    private HorizontalPanel dynaPanel;
    private final TaskServiceAsync taskService = TaskService.App.get();
    private TaskAssigneesWidget taskAssigneesWidget;
    private ArrayList<String> fieldCodeName;
    private ArrayList<CompanyCustomFieldItem> listViewCustomFields;
    private ArrayList<CustomFieldBuilder> customFieldBuilder;

    private KpiCheckBox copyToDueDate;
    private KpiCheckBox copyToAllDesc;
    private KpiCheckBox copyToAllAssing;

    private Integer projectID;
    private boolean isProjectBillable;

    public AddMultiTaskView(String projectID) {
        super("addmultitask");
        setDescription(property.getSingular(projectStrings.addTask(), wfmStrings.task()));
        if (projectID != null) {
            this.projectID = Integer.valueOf(projectID);
        }
    }

    public AddMultiTaskView(String projectID, String workStreamID) {
        super("addmultitask");
        setDescription(property.getSingular(projectStrings.addTask(), wfmStrings.task()));
        if (projectID != null) {
            this.projectID = Integer.valueOf(projectID);
        }
        if (workStreamID != null) {
            this.workStreamID = Integer.valueOf(workStreamID);
        }
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        customFieldBuilder = new ArrayList<>();

        verticalPanel = new VerticalPanel();
        verticalPanel.setSpacing(5);
        dynaPanel = new HorizontalPanel();
        /*HorizontalPanel imagePan = new HorizontalPanel();
        AbstractImagePrototype addMultiTaskImageBundle = AbstractImagePrototype.create(pmAddMultiTaskImageBundle.addTaskImage());
        imagePan.add(addMultiTaskImageBundle.createImage());
        verticalPanel.add(imagePan);
        verticalPanel.setCellHorizontalAlignment(imagePan, HasAlignment.ALIGN_CENTER);*/
        drawDescriptionPanel();

        verticalPanel.add(dynaPanel);

        MaterialPanel mainPanel = new MaterialPanel("section-box box-bg--1");
        mainPanel.add(verticalPanel);
        add(mainPanel);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PROJECT_ADD, this, (sender, args) -> {
            if (args != null) {
                projectID = (Integer) args;
            }

            initProjects();
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PROJECT_MEMBER_ADD, this, (sender, args) -> {
            if (project.getSelectedItem() != null && project.getSelectedItemID().equals(args)) {
                reloadAssignees();
            }
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_WORKSTREAM_ADD, this, (sender, args) -> {
            if (args instanceof Integer) {
                workStreamID = (Integer) args;
                if (workStreamID != null && projectID != null) {
                    taskService.getFirstLevelWorkstreams(projectID, workStreamID, new AbstractAsyncCallback<WbsItem>() {
                        public void success(WbsItem result) {
                            if (result != null) {
                                parentWorkstream.setText(result.getName());
                                parentWorkstream.setWorkstream(result);
                            }
                        }
                    });
                }
            }
        });
        initProjects();
        LoadingPanel.loading(true);
        CommonService.App.get().getCompanyAddViewFieldsPosition(null, ViewAddFiledsCodeName.MultiTaskAdd, new AbstractAsyncCallback<ListPanelToolRpc>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(ListPanelToolRpc result) {
                LoadingPanel.loading(false);
                fieldCodeName = result.getColumnCodeName();
                listViewCustomFields = result.getListViewCustomFields();
                calculateColumnCount();
                initialization();
            }
        });

        return null;
    }

    /**
     * Calculate Dynamic Table Column Count
     */
    private void calculateColumnCount() {
        if (fieldCodeName != null) {
            int columnCount = 0;
            for (String field : ViewAddFiledsCodeName.MultiTaskAdd.getFields()) {
                if (fieldCodeName.contains(field)) {
                    columnCount++;
                }
            }
            if (listViewCustomFields != null && listViewCustomFields.size() != 0) {
                for (CompanyCustomFieldItem fieldItem : listViewCustomFields) {
                    if (fieldCodeName.contains(fieldItem.getColumnCode())) {
                        columnCount++;
                    }
                }
            } else {
                String[] fieldCodeArrays = ViewAddFiledsCodeName.MultiTaskAdd.getFields();
                ArrayList<String> removeFieldName = new ArrayList<>();
                for (String fieldCode : fieldCodeName) {
                    boolean p = true;
                    for (String fieldCode1 : fieldCodeArrays) {
                        if (fieldCode.equals(fieldCode1)) {
                            p = false;
                            break;
                        }
                    }
                    if (p) {
                        removeFieldName.add(fieldCode);
                    }
                }
                fieldCodeName.removeAll(removeFieldName);
            }
            COLUMNS_COUNT = columnCount;
        } else {
            COLUMNS_COUNT = 8;
        }
    }

    private void initialization() {
        initCopyToAllWidgets();
        taskService.getPriorities(new AbstractAsyncCallback<SelectItem[]>() {

            public void success(final SelectItem[] object) {
                DeferredCommand.addCommand(() -> {
                    prioList = object;
                    initDynamicTable();
                    //drawInnerPanel(); //tehere no need to this button, in rows already exist add row
                    add(createFooter());
                });
            }
        });
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return AddMultiTaskView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return AddMultiTaskView.this.getFooterRightSideWidgets();
            }
        });
    }

    public List<Widget> getFooterLeftSideWidgets() {
        return null;
    }

    private List<Widget> getFooterRightSideWidgets() {
        ArrayList<Widget> rightWidgets = new ArrayList<>();

        saveAndCloseButton = new MaterialLink(wfmStrings.save());
        MaterialSplitButton splitButton = new MaterialSplitButton(saveAndCloseButton);
        saveAndCloseButton.addClickHandler(event -> {
            if (validate()) {
                LoadingPanel.loading(true);
                saveAndClose = true;
                saveMultiTask();
            }
        });

        saveButton = new MaterialLink(wfmStrings.saveAndNew());
        saveButton.addClickHandler(event -> {
            if (validate()) {
                LoadingPanel.loading(true);
                saveMultiTask();
            }
        });
        splitButton.addItem(saveButton);

        Div saveWrapper = new Div();
        saveWrapper.add(splitButton);

        rightWidgets.add(saveWrapper);

        return rightWidgets;
    }

    /**
     * Create Copy All Widgets
     */
    private void initCopyToAllWidgets() {
        copyToAllDesc = new KpiCheckBox(wfmStrings.copyToAll());
        copyToAllDesc.ensureDebugId("Add_multi_task_copyToAll");
        copyToAllAssing = new KpiCheckBox(wfmStrings.copyToAll());
        copyToAllAssing.ensureDebugId("Add_multi_task_copyToAll");
        copyToDueDate = new KpiCheckBox(wfmStrings.copyToAll());
        copyToDueDate.ensureDebugId("Add_multi_task_copyToAll");
    }

    private void initDynamicTable() {
        dynamicTable = new DynamicTable(getColumnArray());
        insertCopyToAllWidgets();
        Widget[] widgets = getWidgetArray();
        dynamicTable.addRow(widgets);
        dynamicTable.addListener(new AddListener() {
            public void plusClicked(int rowId) {
                Widget[] widgets = getWidgetArray();
                dynamicTable.insertRow(rowId + 1, widgets);
                copyToAllDataNewRow(rowId + 1);
                if (customFieldBuilder.size() != 0) {
                    CustomFieldBuilder customBuilder = customFieldBuilder.get(customFieldBuilder.size() - 1);
                    customFieldBuilder.remove(customBuilder);
                    customFieldBuilder.add(rowId + 1, customBuilder);
                }
            }

            public void minusClicked(int rowId, Integer objectId) {
                if (customFieldBuilder.size() > 1 && customFieldBuilder.get(rowId) != null) {
                    customFieldBuilder.remove(customFieldBuilder.get(rowId));
                }
            }
        });
        dynaPanel.add(dynamicTable);
    }

    private void insertCopyToAllWidgets() {
        int index = 1;
        // Add Task Name Fields
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[0])) {
            int position = fieldCodeName != null ? fieldCodeName.indexOf(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[0]) + 1 : index++;
            dynamicTable.setHTML(1, position, "&nbsp;");
        }
        // Add Task Description Fields
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[1])) {
            int position = fieldCodeName != null ? fieldCodeName.indexOf(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[1]) + 1 : index++;
            dynamicTable.setWidget(1, position, copyToAllDesc);
        }
        // Add Task Assignees Fields
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[2])) {
            int position = fieldCodeName != null ? fieldCodeName.indexOf(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[2]) + 1 : index++;
            dynamicTable.setWidget(1, position, copyToAllAssing);
        }
        // Add Task StartDate Fields
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[3])) {
            int position = fieldCodeName != null ? fieldCodeName.indexOf(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[3]) + 1 : index++;
            dynamicTable.setHTML(1, position, "&nbsp;");
        }
        // Add Task DueDadet Fields
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[4])) {
            int position = fieldCodeName != null ? fieldCodeName.indexOf(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[4]) + 1 : index++;
            dynamicTable.setWidget(1, position, copyToDueDate);
        }
        // Add Task Priority Fields
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[5])) {
            int position = fieldCodeName != null ? fieldCodeName.indexOf(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[5]) + 1 : index++;
            dynamicTable.setHTML(1, position, "&nbsp;");
        }
        // Add Task Billable Fields
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[6])) {
            int position = fieldCodeName != null ? fieldCodeName.indexOf(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[6]) + 1 : index++;
            dynamicTable.setHTML(1, position, "&nbsp;");
        }
        // Add Task Attachment Fields
//        if (!Utils.hasRole(CLIENT)) {
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[7])) {
            int position = fieldCodeName != null ? fieldCodeName.indexOf(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[7]) + 1 : index++;
            dynamicTable.setHTML(1, position, "&nbsp;");
        }
//        }
        // Add Task Build CustomField
        if (fieldCodeName != null) {
            for (CompanyCustomFieldItem fieldItem : listViewCustomFields) {
                if (fieldCodeName.contains(fieldItem.getColumnCode())) {
                    dynamicTable.setHTML(1, fieldCodeName.indexOf(fieldItem.getColumnCode()) + 1, "&nbsp;");
                }
            }
        }
    }

    private Widget[] getWidgetArray() {
        Widget[] widgets = new Widget[COLUMNS_COUNT];
        name = new TextBox();
        name.ensureDebugId("Add_multi_task_taskName");
        name.setWidth("125px");

        description = new TextArea2(defaultDescriptionCharacterLimit);
        description.ensureDebugId("Add_multi_task_description");
        description.hideCharacterLimitPanel();
        description.setWidth("230px");
        description.addStyleName("description-default-color");

        taskAssigneesWidget = new TaskAssigneesWidget();
        taskAssigneesWidget.getResultGrid().getElement().getStyle().setMarginLeft(1, Style.Unit.PX);
        taskAssigneesWidget.ensureDebugId("Add_multi_task_assigneesEstimatedTime");
        if (items != null) {
            PositionsSelectItem[] objs = clonePostionsSelectItem();
            taskAssigneesWidget.setItems(objs);
        }
        startDate = new DatePicker(new Date());
        //default start date
        Date resetValue = new Date();
        DateUtil.resetTime(resetValue);
        startDate.setDate(resetValue);

        startDate.ensureDebugId("Add_multi_task_startDate");
        startDate.setWidth("70px");

        dueDate = new DatePicker();
        dueDate.ensureDebugId("Add_multi_task_dueDate");
        dueDate.setWidth("70px");

        priority = new DataListBox();
        priority.ensureDebugId("Add_multi_task_priority");
        priority.addStyleName("no-margin");
        priority.setItems(prioList);
        if (prioList != null) {
            for (SelectItem anObject : prioList) {
                if (anObject.getName().trim().equals(wfmStrings.medium())) {
                    priority.setSelected(anObject.getId());
                }
            }
        }
        HorizontalPanel prioHp = new HorizontalPanel();
        prioHp.add(priority);
        //Billable
        KpiCheckBox billable = new KpiCheckBox();
        billable.ensureDebugId("Add_multi_task_billable");
        if (isProjectBillable && project.getSelectedItem() != null) {
            billable.setValue(true);
        } else billable.setValue(!isProjectBillable && project.getSelectedItem() == null);
        MaterialPanel billablePanel = new MaterialPanel();
        billablePanel.setStyleName("text-center");
        billablePanel.add(billable);

        MultiUploadForm multiUploadForm = new MultiUploadForm(false);
        multiUploadForm.ensureDebugId("Add_multi_task_attach");
        multiUploadForm.setUploadedText(projectStrings.successfullyAttached());

        int index = 0;
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[0])) {
            int position = fieldCodeName != null ? fieldCodeName.indexOf(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[0]) : index++;
            widgets[position] = name;
        }
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[1])) {
            int position = fieldCodeName != null ? fieldCodeName.indexOf(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[1]) : index++;
            widgets[position] = description;
        }
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[2])) {
            int position = fieldCodeName != null ? fieldCodeName.indexOf(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[2]) : index++;
            widgets[position] = taskAssigneesWidget;
        }
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[3])) {
            int position = fieldCodeName != null ? fieldCodeName.indexOf(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[3]) : index++;
            widgets[position] = startDate;
        }
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[4])) {
            int position = fieldCodeName != null ? fieldCodeName.indexOf(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[4]) : index++;
            widgets[position] = dueDate;
        }
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[5])) {
            int position = fieldCodeName != null ? fieldCodeName.indexOf(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[5]) : index++;
            widgets[position] = prioHp;
        }
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[6])) {
            int position = fieldCodeName != null ? fieldCodeName.indexOf(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[6]) : index++;
            widgets[position] = billablePanel;
        }
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[7])) {
            int position = fieldCodeName != null ? fieldCodeName.indexOf(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[7]) : index++;
            widgets[position] = multiUploadForm;
        }
        if (fieldCodeName != null) {
            CustomFieldBuilder fieldBuilder = new CustomFieldBuilder(listViewCustomFields);
            fieldBuilder.generateFieldsWidgets(fieldCodeName);
            for (CompanyCustomFieldItem fieldItem : listViewCustomFields) {
                if (fieldCodeName.contains(fieldItem.getColumnCode())) {
                    widgets[fieldCodeName.indexOf(fieldItem.getColumnCode())] = fieldBuilder.getCustomWidget(fieldItem.getColumnCode());
                }
            }
            customFieldBuilder.add(customFieldBuilder.size(), fieldBuilder);
        }
        return widgets;
    }

    private void drawDescriptionPanel() {
        GBox groupBox = new GBox();
        groupBox.setStyleUnited(true);
        groupBox.setStyleWidthFree(true);
        GBoxRow groupBoxRow = new GBoxRow();
        groupBox.add(groupBoxRow);

        project = new CRMLookUp(LookUpConstants.PROJECT);
        project.setFullSearch(true);
        project.ensureDebugId("Add_multi_task_project");
//        project.addStyleName(DEFAULT_WIDTH);
        project.setWidth(MAX_DEFAULT_WIDTH);

        /*addNewProject = new MaterialLink();
        addNewProject.ensureDebugId("Add_multi_task_addNewProject_link");
        addNewProject.setStyleName("hasicon--left");
        addNewProject.addClickHandler(event -> {
            SinksContainerFactory.entryPoint.onHistoryChanged("project|add/add/pm");
        });
        Icon plusIcon = new Icon();
        plusIcon.setStyleName("ficon--plus");
        addNewProject.add(plusIcon);
        addNewProject.setText(projectStrings.addProject());*/

        parentWorkstream = new WorkstreamChooser();
        parentWorkstream.ensureDebugId("Add_multi_task_parent_workstream");
        if (workStreamID != null) {
            taskService.getWorkstream(workStreamID, new AbstractAsyncCallback<WorkstreamSingleItem>() {
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                public void success(final WorkstreamSingleItem result) {
                    parentWorkstream.setText(result.getName());
                    parentWorkstream.getWorkstreamNameBox().setEnabled(false);
//                    project.setEnabled(false);
                }
            });
        } else if (projectID != null) {
//            project.setEnabled(false);
        }
        parentWorkstream.getWorkstreamNameBox().addClickHandler(event -> {
            if (project.isSelected()) {
                parentWorkstream.publicShowShell();
            } else {
                WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, wfmStrings.pleaseSelectProjectFirst());
                messageBox.open();
            }
        });

        project.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            projectChange();
            checkProjectBillable();
            parentWorkstream.reInit();
        });

        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_ADD)) {
            /*MaterialPanel projectCheckBoxPanel = new MaterialPanel("group-box__item-outside-component");
            projectCheckBoxPanel.add(addNewProject);*/

            MaterialPanel projectPanel = new MaterialPanel();
            projectPanel.add(project);
//            projectPanel.add(projectCheckBoxPanel);

            groupBoxRow.add(new FormGroup(Property.get(Constants.PROJECT, wfmStrings.projectField()), projectPanel));
        } else {
            groupBoxRow.add(new FormGroup(Property.get(Constants.PROJECT, wfmStrings.projectField()), project));
        }

        /*MaterialLink addNewWorkStream = new MaterialLink();
        addNewWorkStream.ensureDebugId("Add_multi_task_parent_workstream_link");
        addNewWorkStream.setStyleName("hasicon--left");

        Icon addWorkstreamIcon = new Icon();
        addWorkstreamIcon.setStyleName("ficon--plus");
        addNewWorkStream.add(addWorkstreamIcon);
        addNewWorkStream.setText(projectStrings.addNewWorkstream());
        addNewWorkStream.addClickHandler(event -> {
            String addWorkStreamAction = "workstream|add/add/" + ((projectID != null && projectID > 0) ? projectID.toString() : "");
            SinksContainerFactory.entryPoint.onHistoryChanged(addWorkStreamAction);
        });*/
        if (Utils.hasRole(PM) || Utils.hasRole(DR) || Utils.hasRole(ADMIN)) {
            /*MaterialPanel workstreamCheckBoxPanel = new MaterialPanel("group-box__item-outside-component");
            workstreamCheckBoxPanel.add(addNewWorkStream);*/

            MaterialPanel workstreamPanel = new MaterialPanel();
            workstreamPanel.add(parentWorkstream);
//            workstreamPanel.add(workstreamCheckBoxPanel);

            groupBoxRow.add(new FormGroup(wfmStrings.workStream(), workstreamPanel));
        } else {
            groupBoxRow.add(new FormGroup(wfmStrings.workStream(), parentWorkstream));
        }
        verticalPanel.add(groupBox);
    }

    private void initProjects() {
        CommonService.App.get().getProjects(false, Utils.hasGenericAccess(GenericSettingsEnum.PROJECT_NAME_SHOW_WITH_PROJECT_NUMBER), new AbstractAsyncCallback<ProjectItem[]>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            public void success(final ProjectItem[] object) {
                DeferredCommand.addCommand(() -> {
                    LoadingPanel.loading(false);
                    if (projectID != null) {
                        for (ProjectItem item : object) {
                            if (item.getId().equals(projectID)) {
                                project.setSelected(new SelectItem(item.getId(), item.getName()));
                                break;
                            }
                        }
                    }
                    projectChange();
                    checkProjectBillable();
                });
            }
        });
    }

    private void projectChange() {
        if (taskAssigneesWidget != null) {
            taskAssigneesWidget.clear();
        }
        if (project.getSelectedItem() != null) {
            parentWorkstream.addStyleName(DEFAULT_WIDTH);
            parentWorkstream.setProjectId(project.getSelectedItemID());
            parentWorkstream.setProjectName(project.getSelectedItem().getName());

            projectID = project.getSelectedItem().getId();

            if (project.getSelectedItem().getId() != 0) {
                reloadAssignees();
            }
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
                    isProjectBillable = isBillable != null ? isBillable : false;
                    if (isBillable != null && !isBillable) {
                        for (int i = 0; i < dynamicTable.getRowNumber(); i++) {
                            final DynamicTableItem tableItem = dynamicTable.getItem(i);
                            MaterialPanel billablePanel = (MaterialPanel) tableItem.getColumnById(wfmStrings.billable());
                            KpiCheckBox billableCheckBox = (KpiCheckBox) billablePanel.getWidget(0);
                            if (billableCheckBox != null) {
                                billableCheckBox.setValue(false);
                            }
                        }
                    } else if (isBillable != null && isBillable && dynamicTable != null) {
                        for (int i = 0; i < dynamicTable.getRowNumber(); i++) {
                            final DynamicTableItem tableItem = dynamicTable.getItem(i);
                            MaterialPanel billablePanel = (MaterialPanel) tableItem.getColumnById(wfmStrings.billable());
                            KpiCheckBox billableCheckBox = (KpiCheckBox) billablePanel.getWidget(0);
                            if (billableCheckBox != null && !billableCheckBox.isEnabled()) {
                                billableCheckBox.setValue(true);
                            }
                        }
                    }
                }
            });
        }
    }

    private void reloadAssignees() {
        LoadingPanel.loading(true);
        taskService.getAssigneesWithPositions1(project.getSelectedItemID(), new AbstractAsyncCallback<PositionsSelectItem[]>() {

            public void success(PositionsSelectItem[] result) {
                items = result;
                PositionsSelectItem[] objs = clonePostionsSelectItem();
                reInitAssignees(objs);
                LoadingPanel.loading(false);
            }

            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }
        });
    }

    private PositionsSelectItem[] clonePostionsSelectItem() {
        return clonePostionsSelectItem(items);
    }

    private PositionsSelectItem[] clonePostionsSelectItem(PositionsSelectItem[] list) {
        PositionsSelectItem[] objs = new PositionsSelectItem[list.length];
        for (int i = 0; i < list.length; i++) {
            PositionsSelectItem it = new PositionsSelectItem();
            it.setId(list[i].getId());
            it.setTime(list[i].getTime());
            it.setName(list[i].getName());
            it.setDepartmentId(list[i].getDepartmentId());
            it.setDepartmentName(list[i].getDepartmentName());
            it.setEmployeeId(list[i].getEmployeeId());
            it.setSelected(list[i].isSelected());
            objs[i] = it;
        }
        return objs;
    }

    private void drawInnerPanel() {
        WfmButton2 addNewLine = new WfmButton2(wfmStrings.addNewLines());
        addNewLine.ensureDebugId("Add_multi_task_addNewLines_buttom");
        addNewLine.addClickHandler(sender -> {
            for (int i = 0; i < 2; i++) {
                Widget[] widgets = getWidgetArray();
                dynamicTable.addRow(widgets);
                copyToAllDataNewRow(dynamicTable.getRowNumber() - 1);
            }
        });
        addNewLine.addStyleName("margin-top");
        verticalPanel.add(addNewLine);
        verticalPanel.setCellHorizontalAlignment(addNewLine, HorizontalPanel.ALIGN_LEFT);
    }

    /**
     * Copy All Widgets
     *
     * @param rowNumber
     */
    private void copyToAllDataNewRow(int rowNumber) {
        DynamicTableItem copyTableItem = dynamicTable.getItem(0);
        DynamicTableItem tableItem = dynamicTable.getItem(rowNumber);
        if (copyToAllDesc.getValue()) {
            TextArea2 descriptionCopy = (TextArea2) copyTableItem.getColumnById(wfmStrings.description());
            TextArea2 description = (TextArea2) tableItem.getColumnById(wfmStrings.description());
            if (descriptionCopy.getText() != null) {
                description.setText(descriptionCopy.getText());
            }
        }
        if (copyToAllAssing.getValue()) {
            TaskAssigneesWidget copyTaskAssigneesWidget = (TaskAssigneesWidget) copyTableItem.getColumnById(wfmStrings.assigneesOrEstimatedTime());
            TaskAssigneesWidget taskAssigneesWidget = (TaskAssigneesWidget) tableItem.getColumnById(wfmStrings.assigneesOrEstimatedTime());
            if (copyTaskAssigneesWidget.getSelectedItems().length != 0) {
                taskAssigneesWidget.setItems(clonePostionsSelectItem(copyTaskAssigneesWidget.getAllItems()));
                taskAssigneesWidget.initResult(clonePostionsSelectItem(copyTaskAssigneesWidget.getSelectedItems()));
            }
        }
        if (copyToDueDate.getValue()) {
            DatePicker startDateCopy = (DatePicker) copyTableItem.getColumnById(wfmStrings.startDate());
            DatePicker startDate = (DatePicker) tableItem.getColumnById(wfmStrings.startDate());
            if (startDateCopy.getDate() != null) {
                startDate.setDate(startDateCopy.getDate());
            }
            DatePicker dueDateCopy = (DatePicker) copyTableItem.getColumnById(wfmStrings.dueDate());
            DatePicker dueDate = (DatePicker) tableItem.getColumnById(wfmStrings.dueDate());
            if (dueDateCopy.getDate() != null) {
                dueDate.setDate(dueDateCopy.getDate());
            }
        }
    }

    private DynamicTableColumn[] getColumnArray() {
        DynamicTableColumn[] columns = new DynamicTableColumn[COLUMNS_COUNT];
        int index = 0;
        // Add Task Name Fields
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[0])) {
            int position = fieldCodeName != null ? fieldCodeName.indexOf(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[0]) : index++;
            columns[position] = new DynamicTableColumn(wfmStrings.name(), wfmStrings.taskName(), new ColumnStatements(".", projectStrings.enterTaskName()), 128);
        }
        // Add Task Description Fields
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[1])) {
            int position = fieldCodeName != null ? fieldCodeName.indexOf(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[1]) : index++;
            columns[position] = new DynamicTableColumn(wfmStrings.description(), wfmStrings.description(), new ColumnStatements(projectStrings.shortDescriptionTask(), projectStrings.enterTaskDescription()), 238);
        }
        // Add Task Assignees Fields
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[2])) {
            int position = fieldCodeName != null ? fieldCodeName.indexOf(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[2]) : index++;
            columns[position] = new DynamicTableColumn(wfmStrings.assignees(), wfmStrings.assigneesOrEstimatedTime(), new ColumnStatements("", projectStrings.selectAssignees()), 215, true);
        }
        // Add Task StartDate Fields
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[3])) {
            int position = fieldCodeName != null ? fieldCodeName.indexOf(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[3]) : index++;
            columns[position] = new DynamicTableColumn(wfmStrings.startDate(), wfmStrings.startDate(), new ColumnStatements("", ""), 76, true);
        }
        // Add Task DueDadet Fields
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[4])) {
            int position = fieldCodeName != null ? fieldCodeName.indexOf(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[4]) : index++;
            columns[position] = new DynamicTableColumn(wfmStrings.dueDate(), wfmStrings.dueDate(), new ColumnStatements("", ""), 76);
        }
        // Add Task Priority Fields
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[5])) {
            int position = fieldCodeName != null ? fieldCodeName.indexOf(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[5]) : index++;
            columns[position] = new DynamicTableColumn(wfmStrings.priority(), wfmStrings.priority(), new ColumnStatements(".", projectStrings.plsChoosePriority()), 94, true);
        }
        // Add Task Billable Fields
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[6])) {
            int position = fieldCodeName != null ? fieldCodeName.indexOf(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[6]) : index++;
            columns[position] = new DynamicTableColumn(wfmStrings.billable(), wfmStrings.billable(), new ColumnStatements("", ""), 50);
        }
        // Add Task Attachment Fields
//        if (!Utils.hasRole(CLIENT)) {
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[7])) {
            int position = fieldCodeName != null ? fieldCodeName.indexOf(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[7]) : index++;
            columns[position] = new DynamicTableColumn(wfmStrings.attachment(), wfmStrings.attachment(), new ColumnStatements("", ""), 72);
        }
//        }
        // Add Task Build CustomField
        if (fieldCodeName != null) {
            for (CompanyCustomFieldItem fieldItem : listViewCustomFields) {
                if (fieldCodeName.contains(fieldItem.getColumnCode())) {
                    columns[fieldCodeName.indexOf(fieldItem.getColumnCode())] = new DynamicTableColumn(fieldItem.getFieldName(), fieldItem.getColumnCode(), new ColumnStatements("", ""), 82);
                }
            }
        }
        return columns;
    }

    private boolean validate() {
        int errors = 0;
        int wrong = 0;

        if (!Validation.validateLookUpRequired(project)) {
            Info.show(wfmStrings.pleaseChooseProject(), Info.Type.WARNING);
            return false;
        }
        dynamicTable.resetValidation();
        for (int rowId = 0; rowId < dynamicTable.getRowNumber(); rowId++) {
            DynamicTableItem tableItem = dynamicTable.getItem(rowId);
            if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[0])) {
                TextBox taskName = (TextBox) tableItem.getColumnById(wfmStrings.taskName());
                if (taskName.getText() == null || "".equals(taskName.getText())) {
                    dynamicTable.notValid(rowId, wfmStrings.taskName());
                    errors++;
                }
            }

            if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[2])) {
                final TaskAssigneesWidget taskAssigneesWidget = (TaskAssigneesWidget) tableItem.getColumnById(wfmStrings.assigneesOrEstimatedTime());
                taskAssigneesWidget.getResultGrid().removeStyleName(Constants.ERROR_FORM_STYLE);
                if (taskAssigneesWidget.getSelectedItems().length == 0) {
                    errors++;
                    taskAssigneesWidget.getResultGrid().addStyleName(Constants.ERROR_FORM_STYLE);
                }
            }

            final DatePicker startDate = (DatePicker) tableItem.getColumnById(wfmStrings.startDate());
            if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[3])) {
                if (startDate.getDate() == null) {
                    dynamicTable.notValid(rowId, wfmStrings.startDate());
                    errors++;
                }
            }

            if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[4])) {
                DatePicker dueDate = (DatePicker) tableItem.getColumnById(wfmStrings.dueDate());
                if (dueDate.getDate() == null) {
                    dynamicTable.notValid(rowId, wfmStrings.dueDate());
                    errors++;
                } else if (startDate.getDate() != null && dueDate.getDate().before(startDate.getDate())) {
                    dynamicTable.notValid(rowId, wfmStrings.dueDate());
                    wrong++;
                }
            }

            if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[5])) {
                final HorizontalPanel prioHp = (HorizontalPanel) tableItem.getColumnById(wfmStrings.priority());
                final DataListBox priority = (DataListBox) prioHp.getWidget(0);
                if (!Validation.validateListBoxRequired(priority, priorityField, projectStrings.plsChoosePriority())) {
                    errors++;
                    dynamicTable.notValid(rowId, wfmStrings.priority());
                }
            }
        }

        if (wrong > 0) {
            Info.show("Start date should be greater than end date", Info.Type.WARNING);
            return false;
        } else if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        } else {
            return true;
        }
    }

    public void reInitAssignees(PositionsSelectItem[] result) {
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[2])) {
            final int position = fieldCodeName != null ? fieldCodeName.indexOf(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[2]) + 1 : 3;
            for (int i = 1; i < dynamicTable.getRowCount() - 1; i++) {

                final TaskAssigneesWidget taskAssignees = (TaskAssigneesWidget) dynamicTable.getWidget(i, position);
                taskAssignees.clear();
                taskAssignees.setItems(result);
            }
        }
    }

    public void saveMultiTask() {
        saveButton.setEnabled(false);
        saveAndCloseButton.setEnabled(false);
        final MultiTaskList multiTaskList = new MultiTaskList();
        multiTaskList.setProjectID(project.getSelectedItemID());
        if (parentWorkstream.getWorkstream() != null) {
            multiTaskList.setWorkstreamID(parentWorkstream.getWorkstream().getId());
        } else if (workStreamID != null) {
            multiTaskList.setWorkstreamID(workStreamID);
        }
        final TaskSingleItem[] taskSingleItems = new TaskSingleItem[dynamicTable.getRowNumber()];
        for (int i = 0; i < dynamicTable.getRowNumber(); i++) {
            final DynamicTableItem tableItem = dynamicTable.getItem(i);
            taskSingleItems[i] = getTask(tableItem);
            if (customFieldBuilder.size() > i && customFieldBuilder.get(i) != null) {
                taskSingleItems[i].setCustomFieldItems(customFieldBuilder.get(i).getWidgetValues());
            }
        }
        multiTaskList.setTaskSingleItems(taskSingleItems);

        LoadingPanel.loading(true);

        taskService.saveMultipleTask(multiTaskList, new AbstractAsyncCallback<Integer[]>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                saveButton.setEnabled(true);
                saveAndCloseButton.setEnabled(true);
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, wfmStrings.errorOccurredSavingChanges());
                messageBox.setTitle(wfmStrings.error());
                messageBox.open();
            }

            public void success(final Integer[] result) {
                LoadingPanel.loading(false);
                saveButton.setEnabled(true);
                saveAndCloseButton.setEnabled(true);
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.OK, projectStrings.multipleTaskAddSuccess(), new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        onShellOk();
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_ADD, result, AddMultiTaskView.this);
                    }
                });
                messageBox.setTitle(wfmStrings.information());
                messageBox.open();
            }
        });
        //refreshOnDemand(new String[]{TASK_LIST, TIMESHEET});
    }

    private void onShellOk() {
        if (saveAndClose) {
            closeTab();
        } else {
            reinit();
        }
    }

    public void reinit() {
        customFieldBuilder.clear();
        saveAndClose = false;
        dynaPanel.clear();
        initDynamicTable();
    }

    public TaskSingleItem getTask(DynamicTableItem tableItem) {
        final TaskSingleItem taskSingleItem = new TaskSingleItem();
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[0])) {
            TextBox taskName = (TextBox) tableItem.getColumnById(wfmStrings.taskName());
            taskSingleItem.setName(taskName.getText());
        }
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[1])) {
            TextArea2 description = (TextArea2) tableItem.getColumnById(wfmStrings.description());
            taskSingleItem.setDescription(description.getText());
        }
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[2])) {
            TaskAssigneesWidget taskAssigneesWidget = (TaskAssigneesWidget) tableItem.getColumnById(wfmStrings.assigneesOrEstimatedTime());
            int membersCount = taskAssigneesWidget.getSelectItemsList().size();
            IdTime[] idTimes = new IdTime[membersCount];
            for (int i = 0; i < membersCount; i++) {
                PositionsSelectItem item = taskAssigneesWidget.getSelectedItems()[i];
                IdTime idTime = new IdTime();
                idTime.setId(item.getId());
                idTime.setTime(item.getTime());
                idTimes[i] = idTime;
            }
            taskSingleItem.setProjectEmployees(idTimes);
        }
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[3])) {
            DatePicker startDate = (DatePicker) tableItem.getColumnById(wfmStrings.startDate());
            taskSingleItem.setStartDate(DateTimePicker.getDateTime(DateUtil.resetTime(startDate.getDate()), Utils.getDefaultCurrentUserTimeSlotStartTIME()));
        }
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[4])) {
            DatePicker dueDate = (DatePicker) tableItem.getColumnById(wfmStrings.dueDate());
            taskSingleItem.setDueDate(DateTimePicker.getDateTime(DateUtil.getDayLastTime(dueDate.getDate()), Utils.getDefaultCurrentUserTimeSlotEndTIME()));
        }
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[5])) {
            HorizontalPanel prioHp = (HorizontalPanel) tableItem.getColumnById(wfmStrings.priority());
            DataListBox priority = (DataListBox) prioHp.getWidget(0);
            taskSingleItem.setPriorityID(priority.getSelectedItem().getId());
        } else {
            if (prioList != null) {
                taskSingleItem.setPriorityID(prioList[0].getId());
            }
        }
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[6])) {
            MaterialPanel billablePanel = (MaterialPanel) tableItem.getColumnById(wfmStrings.billable());
            KpiCheckBox billableCheckBox = (KpiCheckBox) billablePanel.getWidget(0);
            taskSingleItem.setBillable(billableCheckBox.getValue());
        }
        if (fieldCodeName == null || fieldCodeName.contains(ViewAddFiledsCodeName.MultiTaskAdd.getFields()[7])) {
            MultiUploadForm multiUploadForm = (MultiUploadForm) tableItem.getColumnById(wfmStrings.attachment());
            taskSingleItem.setAttachments(multiUploadForm.getUploadForm().getAttachedFiles());
        }

        taskSingleItem.setAllDay(true);
        return taskSingleItem;
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
