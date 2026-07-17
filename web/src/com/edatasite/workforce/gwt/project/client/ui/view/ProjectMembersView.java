package com.edatasite.workforce.gwt.project.client.ui.view;

import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.enums.EmployeeAssignmentEnum;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiCellTree;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.SelectionContainer;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.EditProject;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectServiceAsync;
import com.edatasite.workforce.gwt.project.client.ui.view.projectposition.ProjectPositionWidget;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.MultiSelectionModel;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.*;

public class ProjectMembersView extends View implements Constants {

    private static final ProjectStrings projectStrings = ProjectStrings.App.get();

    private static final DisCloseIconBundle DIS_CLOSE_ICON_BUNDLE = GWT.create(DisCloseIconBundle.class);

    public interface DisCloseIconBundle extends ClientBundle {
        @Source("com/edatasite/workforce/gwt/core/client/bundles/icons/bottom-arrow.png")
        ImageResource openImage();

        @Source("com/edatasite/workforce/gwt/core/client/bundles/icons/right-arrow.png")
        ImageResource closedImage();
    }

    private Command fireSaveMembers;

    private MaterialPanel verticalPanel;
    private WfmForm form;
    private DataListBox manager;
    private Set<SelectItem> systemManagers = new HashSet<>();
    private MultiTableNewUI backupManagerTable;
    private SelectItem[] backupManagerItems;
    private EditProject project;
    private Label managerLabel;
    private Label backupManagerLabel;
    private KpiCellTree membersSelector;
    private ProjectPositionWidget projectPositionWidget;

    private WfmForm.Field managerField;
    private WfmForm.Field backupManagerField;
    private final Integer projectID;
    private WfmButton2 saveCloseButton;
    private Integer defaultProjectID;
    private KpiCheckBox copyNewEmployeesToProjectTasks;
    private WfmForm.Field copyNewEmployeesToProjectTasksField;
    private final boolean isParentNullProject;
    private boolean hasEmployeeAssignRole = false;
    private boolean canEdit = true;

    private DataListBox employeeAssignment;
    private FlowPanel pnlEmployeeAssignmentContainer;

    public ProjectMembersView(Integer projectID, boolean isParentNullProject, boolean canEdit) {
        super("members", wfmStrings.members());
        this.projectID = projectID;
        this.isParentNullProject = isParentNullProject;
        this.canEdit = canEdit;
    }

    @Override
    public String getIconStyle() {
        return "bgMark task-members-involved";
    }

    @Override
    protected Widget onInitialize() {
        getFirstRequest();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ASSIGN_EMPLOYEE_TO_PROJECT, ProjectMembersView.this, (sender, args) -> setManagers());
        return null;
    }

    private void enabledButtons(boolean b) {
        if (saveCloseButton != null) {
            saveCloseButton.setEnabled(b);
        }
    }

    private WidgetsMap getBackupManagersMap(Integer backupManagerID) {
        WidgetsMap widgetsMap = new WidgetsMap();
        DataListBox backupManagersBox = new DataListBox();
        backupManagersBox.setEnabled(false);
        backupManagersBox.addStyleName(DEFAULT_WIDTH);
        widgetsMap.addWidgets(backupManagersBox);
        if (backupManagerItems != null) {
            backupManagersBox.setItems(backupManagerItems);
            backupManagersBox.setEnabled(true);
        }
        if (backupManagerID != null) {
            backupManagersBox.setSelected(backupManagerID);
        }
        widgetsMap.addWidgetToMap(MultiTable.LIST_BOX, backupManagersBox);
        return widgetsMap;
    }

    private void fillBackupManagersList() {
        manager.setSelected(project.getManagerId());
        if (project.getBackupManagerIDs() != null && project.getBackupManagerIDs().size() > 0) {
            backupManagerTable.removeAllRows();
            for (Integer backupManagerID : project.getBackupManagerIDs()) {
                backupManagerTable.addWidgets(getBackupManagersMap(backupManagerID));
            }
        }
//        setManagers();
    }

    private void initManagers() {
        ProjectService.App.get().getProjectForEdit(projectID, null, null, new AbstractAsyncCallback<EditProject>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(final EditProject object) {
                project = object;
                systemManagers = project.getManagers();
                if (Utils.hasGenericAccess(GenericSettingsEnum.IS_COPY_NEW_EMPLOYEES_TO_PROJECT_TASKS)) {
                    copyNewEmployeesToProjectTasksField.setVisible(true);
                }
                if (project.getEmployeeAssignment() != null) {
                    employeeAssignment.setSelected(project.getEmployeeAssignment().getId());

                    pnlEmployeeAssignmentContainer.clear();
                    if (EmployeeAssignmentEnum.BY_POSITION.equals(project.getEmployeeAssignment())) {
                        projectPositionWidget.setValues(project.getProjectPositions());
                        pnlEmployeeAssignmentContainer.add(projectPositionWidget);
                        setManagers();
                    } else {
                        pnlEmployeeAssignmentContainer.add(membersSelector);
                        initMembers();
                    }
                } else {
                    pnlEmployeeAssignmentContainer.add(membersSelector);
                    initMembers();
                }
                fillBackupManagersList();
                setManagers();
                LoadingPanel.loading(false);
            }
        });
    }

    private void initMembers() {
        EmployeeService.App.get().getProjectEmployeesForAddEdit(projectID, hasEmployeeAssignRole, new AbstractAsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>>() {
            @Override
            public void failure(Throwable caught) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> members) {
                membersSelector.setItems(members);
                Scheduler.get().scheduleDeferred(() -> {
                    fillBackupManagersList();
                    setManagers();
                });
            }
        });
    }

    public void initEditForm() {
        // Employee Assignment
        employeeAssignment = new DataListBox();
        employeeAssignment.setVisibleItemCount(1);
        employeeAssignment.addStyleName(DEFAULT_WIDTH);
        employeeAssignment.setWithoutNullLabel(true);
        SelectItem[] eaTypes = new SelectItem[]{new SelectItem(EmployeeAssignmentEnum.BY_POSITION.getId(), EmployeeAssignmentEnum.BY_POSITION.getTitle()),
                new SelectItem(EmployeeAssignmentEnum.BY_EMPLOYEE.getId(), EmployeeAssignmentEnum.BY_EMPLOYEE.getTitle())};
        employeeAssignment.setItems(eaTypes);
        employeeAssignment.setVisible(Utils.isEmployeeAssignmentEnable());
        employeeAssignment.setEnabled(false);

        pnlEmployeeAssignmentContainer = new FlowPanel();
        pnlEmployeeAssignmentContainer.addStyleName("single-item");
        projectPositionWidget = new ProjectPositionWidget(projectID, false);

        membersSelector = new KpiCellTree();

        membersSelector.drawSelectedSide(new SelectionContainer() {
            @Override
            public void selectedDataGrid(final KpiDataGrid<KpiTreeInfo> selectedDataGrid, ColumnSortEvent.ListHandler<KpiTreeInfo> sortHandler, final MultiSelectionModel<KpiTreeInfo> selectionModel) {
                selectedDataGrid.addRowCountChangeHandler(event -> setManagers());
                //Employee Name Blow
                Column<KpiTreeInfo, String> employee = new Column<KpiTreeInfo, String>(new TextCell()) {
                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        return object.getName();
                    }
                };
                employee.setSortable(true);
                sortHandler.setComparator(employee, (o1, o2) -> o1.getName().compareTo(o2.getName()));
                selectedDataGrid.addColumn(employee, wfmStrings.employee());
                selectedDataGrid.setColumnWidth(employee, 32, com.google.gwt.dom.client.Style.Unit.PCT);

                if (Utils.hasPermission(PermissionConstants.PM_EMPLOYEE_RATE_HISTORY)) {
                    //Wage Rate
                    Column<KpiTreeInfo, String> wageRate = null;
                    if (Utils.hasGenericAccess(GenericSettingsEnum.IS_DISABLED_WAGE_RATE)) {
                        wageRate = new Column<KpiTreeInfo, String>(new TextCell()) {
                            @Override
                            public String getValue(final KpiTreeInfo object) {
                                return object.getWageRate() != null ? object.getWageRate().toString() : "0.00";
                            }
                        };
                    } else {
                        final TextInputCell wageRateCell = new TextInputCell();
                        wageRate = new Column<KpiTreeInfo, String>(wageRateCell) {
                            @Override
                            public String getValue(final KpiTreeInfo object) {
                                return object.getWageRate() != null ? object.getWageRate().toString() : "0.00";
                            }
                        };
                        wageRate.setFieldUpdater((index, object, value) -> object.setWageRate(Double.valueOf(value)));
                    }
                    selectedDataGrid.addColumn(wageRate, wfmStrings.wageRate());
                    selectedDataGrid.setColumnWidth(wageRate, 17, com.google.gwt.dom.client.Style.Unit.PCT);
                    //client Wage Rate
                    final TextInputCell clientRateCell = new TextInputCell();
                    Column<KpiTreeInfo, String> clientRate = new Column<KpiTreeInfo, String>(clientRateCell) {
                        @Override
                        public String getValue(final KpiTreeInfo object) {
                            return object.getClientChargeRate() != null ? object.getClientChargeRate().toString() : "0.00";
                        }
                    };
                    clientRate.setFieldUpdater((index, object, value) -> object.setClientChargeRate(Double.valueOf(value)));
                    selectedDataGrid.addColumn(clientRate, wfmStrings.clientChargeRate());
                    selectedDataGrid.setColumnWidth(clientRate, 17, com.google.gwt.dom.client.Style.Unit.PCT);

                    //Workload Percentage
                    final TextInputCell employeeWorkloadPercentage = new TextInputCell();
                    Column<KpiTreeInfo, String> workloadPercentage = new Column<KpiTreeInfo, String>(employeeWorkloadPercentage) {
                        @Override
                        public String getValue(KpiTreeInfo object) {
                            return object.getWorkloadPercentage() != null ? object.getWorkloadPercentage().toString() : "0";
                        }
                    };
                    workloadPercentage.setFieldUpdater((index, object, value) -> {

                        try {
                            Float percentageFloatValue = Float.valueOf(value);
                            if (percentageFloatValue < 0) {
                                Info.show(wfmStrings.percentInvolvementValueCanOnlyBeBetween0And100(), Info.Type.WARNING);
                                percentageFloatValue = 0f;
                                employeeWorkloadPercentage.clearViewData(KpiTreeInfo.KEY_PROVIDER.getKey(object));
                            } else if (percentageFloatValue > 100) {
                                Info.show(wfmStrings.percentInvolvementValueCanOnlyBeBetween0And100(), Info.Type.WARNING);
                                percentageFloatValue = !Utils.hasGenericAccess(GenericSettingsEnum.PROJECT_PERCENT_OVER_HUNDRED) ? 100f : percentageFloatValue;
                                employeeWorkloadPercentage.clearViewData(KpiTreeInfo.KEY_PROVIDER.getKey(object));
                            }
                            object.setWorkloadPercentage(percentageFloatValue);
                        } catch (NumberFormatException ex) {
                            object.setWorkloadPercentage(0f);
                            employeeWorkloadPercentage.clearViewData(KpiTreeInfo.KEY_PROVIDER.getKey(object));
                        }
                    });
                    selectedDataGrid.addColumn(workloadPercentage, wfmStrings.workloadPercent());
                    selectedDataGrid.setColumnWidth(workloadPercentage, 18, Style.Unit.PCT);
                }
                //Remove Action
                final Column<KpiTreeInfo, String> action = new Column<KpiTreeInfo, String>(new SimpleLinkCell()) {
                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        return wfmStrings.delete();
                    }
                };
                action.setFieldUpdater((index, object, value) -> {
                    List<KpiTreeInfo> contacts = selectedDataGrid.getList();
                    contacts.remove(object);
                    object.setSelected(false);
                    selectionModel.setSelected(object, false);
                });
                if ((!Utils.getDefaultProjectID().equals(projectID) && hasEmployeeAssignRole)) {
                    selectedDataGrid.addColumn(action, wfmStrings.action());
                    selectedDataGrid.setColumnWidth(action, 16, com.google.gwt.dom.client.Style.Unit.PCT);
                }
            }

            @Override
            public void additionalActions(HTMLPanel actionsPanel) {
                if (Utils.hasPermission(PermissionConstants.PM_EMPLOYEE_ADD)) {
                    SimpleLink addNewEmployee = new SimpleLink(wfmStrings.addEmployee());
                    addNewEmployee.setStyleName("right");
                    addNewEmployee.addClickHandler(widget -> goTo("employee|add/add"));
                    actionsPanel.add(addNewEmployee);
                }
            }
        });

        //buttons
        //save button
        //save and close button
        saveCloseButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveCloseButton.ensureDebugId("saveCloseButton");
        saveCloseButton.addClickHandler(widget -> save(true));
        enabledButtons(canEdit);

        verticalPanel = new MaterialPanel("section-box box-bg--1 box-radius wfmForm-container");
        MaterialPanel buttonPanel = new MaterialPanel("btns-group right");
        buttonPanel.add(saveCloseButton);
        verticalPanel.add(buttonPanel);

        form = new WfmForm(new String[]{"15%", "80%"});
        verticalPanel.add(form);

        form.addField(wfmStrings.members(), pnlEmployeeAssignmentContainer/*membersSelector*/);

        if (!Utils.isEmployeeAssignmentEnable()) {
            pnlEmployeeAssignmentContainer.add(membersSelector);
        }
//        }

        copyNewEmployeesToProjectTasks = new KpiCheckBox("&nbsp;" + wfmStrings.assignNewMembersToProjectTasks(), true);
        copyNewEmployeesToProjectTasksField = form.addField(null, copyNewEmployeesToProjectTasks);
        copyNewEmployeesToProjectTasksField.setVisible(false);


        manager = new DataListBox();
        manager.addStyleName(DEFAULT_WIDTH);
        managerField = form.addField(wfmStrings.manager(), manager, true);
        backupManagerTable = new MultiTableNewUI(10, new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getBackupManagersMap(null);
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        });
        backupManagerTable.addStyleName(DEFAULT_WIDTH);
        backupManagerField = form.addField(wfmStrings.backupManagers(), backupManagerTable);

        //project employee changes history
        projectEmployeeChangesHistory();

        manager.addValueChangeHandler(sender -> setManagers());
        initManagers();

        if (Utils.getDefaultProjectID().equals(projectID) || !hasEmployeeAssignRole)
            membersSelector.showTreePanel(false);

        add(verticalPanel);
    }

    //////////////////////////
    private void projectEmployeeChangesHistory() {

        final KpiDataGrid<KpiTreeInfo> dataGrid = new KpiDataGrid<>(KpiTreeInfo.KEY_PROVIDER);
        dataGrid.addStyleName("cellBasedWidget-mod");
        dataGrid.setWidth("100%");
        dataGrid.setHeight("200px");
        dataGrid.getElement().getStyle().setBorderWidth(1d, Style.Unit.PX);
        dataGrid.getElement().getStyle().setBorderColor("Grey");
        dataGrid.getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);

        //columns
        //department name (current department)
        Column<KpiTreeInfo, String> department = new Column<KpiTreeInfo, String>(new TextCell()) {
            @Override
            public String getValue(KpiTreeInfo object) {
                return dataGrid.refactor(object.getDepartmentName());
            }
        };
        dataGrid.addColumn(department, Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()));
        dataGrid.setColumnWidth(department, 20, Style.Unit.PCT);
        //employee name
        Column<KpiTreeInfo, String> employee = new Column<KpiTreeInfo, String>(new TextCell()) {
            @Override
            public String getValue(KpiTreeInfo object) {
                return dataGrid.refactor(object.getName());
            }
        };
        dataGrid.addColumn(employee, wfmStrings.employee());
        dataGrid.setColumnWidth(employee, 20, Style.Unit.PCT);
        //employee wage rate
        Column<KpiTreeInfo, String> employeeWageRate = new Column<KpiTreeInfo, String>(new TextCell()) {
            @Override
            public String getValue(KpiTreeInfo object) {
                return dataGrid.refactor(object.getWageRate() != null ? object.getWageRate().toString() : "");
            }
        };
        dataGrid.addColumn(employeeWageRate, wfmStrings.wageRate());
        dataGrid.setColumnWidth(employeeWageRate, 15, Style.Unit.PCT);
        //employee client charge rate
        Column<KpiTreeInfo, String> employeeClientChargeRate = new Column<KpiTreeInfo, String>(new TextCell()) {
            @Override
            public String getValue(KpiTreeInfo object) {
                return dataGrid.refactor(object.getClientChargeRate() != null ? object.getClientChargeRate().toString() : "");
            }
        };
        dataGrid.addColumn(employeeClientChargeRate, wfmStrings.clientChargeRate());
        dataGrid.setColumnWidth(employeeClientChargeRate, 15, Style.Unit.PCT);

        //employee workload percentage
        final TextInputCell employeeWorkloadPercentage = new TextInputCell();
        employeeWorkloadPercentage.setEnabled(false);
        employeeWorkloadPercentage.setWidth("80px");
        Column<KpiTreeInfo, String> employeeWorkloadPercentageC = new Column<KpiTreeInfo, String>(employeeWorkloadPercentage) {
            @Override
            public String getValue(KpiTreeInfo object) {
                return (object.getWorkloadPercentage() != null ? object.getWorkloadPercentage().toString() : "0.0") + "%";
            }
        };
        dataGrid.addColumn(employeeWorkloadPercentageC, wfmStrings.workloadPercent());
        dataGrid.setColumnWidth(employeeWorkloadPercentageC, 15, Style.Unit.PCT);

        //last update date
        Column<KpiTreeInfo, String> lastUpdateDate = new Column<KpiTreeInfo, String>(new TextCell()) {
            @Override
            public String getValue(KpiTreeInfo object) {
                return dataGrid.refactor(object.getLastUpdateDate() != null ? DateUtils.format(object.getLastUpdateDate()) : "");
            }
        };
        dataGrid.addColumn(lastUpdateDate, wfmStrings.modifiedDate());
        dataGrid.setColumnWidth(lastUpdateDate, 15, Style.Unit.PCT);

        //
        renderProjectEmployeesHistory(dataGrid);
        fireSaveMembers = () -> renderProjectEmployeesHistory(dataGrid);

        DisclosurePanel disclosurePanel = new DisclosurePanel(DIS_CLOSE_ICON_BUNDLE.openImage(), DIS_CLOSE_ICON_BUNDLE.closedImage(), wfmStrings.projectEmployeesHistory());
        disclosurePanel.setWidth("100%");
        disclosurePanel.setOpen(true);
        disclosurePanel.setContent(dataGrid);

        verticalPanel.add(disclosurePanel);
        //get project
    }

    private void renderProjectEmployeesHistory(final KpiDataGrid<KpiTreeInfo> dataGrid) {
        ProjectService.App.get().getProjectEmployeesHistory(projectID, new AbstractAsyncCallback<KpiTreeInfo[]>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(KpiTreeInfo[] members) {
                dataGrid.supplyProvider(members);
                dataGrid.refresh();
            }
        });
    }

    public void initViewForm() {
        form = new WfmForm();
        final KpiDataGrid<KpiTreeInfo> members = new KpiDataGrid<>(KpiTreeInfo.KEY_PROVIDER);
        members.setWidth("300px");
        members.setHeight("200px");
        members.getElement().getStyle().setBorderWidth(1d, Style.Unit.PX);
        members.getElement().getStyle().setBorderColor("Grey");
        members.getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
        Column<KpiTreeInfo, String> department = new Column<KpiTreeInfo, String>(new TextCell()) {
            @Override
            public String getValue(final KpiTreeInfo object) {
                return members.refactor(object.getDepartmentName());
            }
        };

        members.addColumn(department, Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()));
        members.setColumnWidth(department, 40, com.google.gwt.dom.client.Style.Unit.PCT);

        Column<KpiTreeInfo, String> employee = new Column<KpiTreeInfo, String>(new TextCell()) {
            @Override
            public String getValue(final KpiTreeInfo object) {
                return members.refactor(object.getName());
            }
        };

        members.addColumn(employee, wfmStrings.employee());
        members.setColumnWidth(employee, 40, com.google.gwt.dom.client.Style.Unit.PCT);

        form.addField(wfmStrings.members(), members);
        managerLabel = new Label();
        managerField = form.addField(wfmStrings.manager(), managerLabel);
        backupManagerLabel = new Label();
        backupManagerField = form.addField(wfmStrings.backupManagers(), backupManagerLabel);

        ProjectService.App.get().getProjectEmployeesForView(projectID, new AbstractAsyncCallback<KpiTreeInfo[]>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(KpiTreeInfo[] result) {
                LoadingPanel.loading(false);
                members.supplyProvider(result);
                members.refresh();
            }
        });

        ProjectService.App.get().getProjectForEdit(projectID, null, null,
                new AbstractAsyncCallback<EditProject>() {
                    @Override
                    public void failure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(EditProject object) {
                        LoadingPanel.loading(false);
                        project = object;
                        managerLabel.setText(project.getManagerName());
                        backupManagerLabel.setText(project.getBackupManagerName());
                    }
                });
        add(form);
    }

    public void getFirstRequest() {
        clear();
        LoadingPanel.loading(true);
        if (Utils.hasPermission(PermissionConstants.PM_ADD_ASSIGNEES_TO_PROJECT)) {
            hasEmployeeAssignRole = true;
        }
        ProjectServiceAsync projectService = ProjectService.App.get();
        if (Utils.getDefaultProjectID() == 0) {
            projectService.getDefaultProjectID(new AbstractAsyncCallback<Integer>() {
                @Override
                public void success(Integer projectID) {
                    Utils.setDefaultProjectID(projectID);
                }
            });
        }
        EmployeeService.App.get().getProjectEmployeeEditablePermmission(projectID, new AbstractAsyncCallback<Integer>() {
            @Override
            public void success(Integer result) {
                if (result == EDIT || Utils.hasPermission(PermissionConstants.PM_PROJECT_EDIT)) {
                    initEditForm();
                } else {
                    initViewForm();
                }

            }
        });
    }

    public SelectItem[] getItemsFromList(java.util.List list) {//<ProjectMember>
        SelectItem[] items = new SelectItem[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ProjectMember pm = (ProjectMember) list.get(i);
            items[i] = new SelectItem(pm.getId(), pm.getName());
        }
        return items;
    }

    private void onShellOk(boolean closeTabT) {
        if (closeTabT) {
            closeTab();
        }
    }

    private void save(final boolean closeTabT) {
        enabledButtons(false);
        if (!validate()) {
            enabledButtons(true);
            return;
        }
        if (manager.getSelectedItem() != null) {
            project.setManagerId(manager.getSelectedItem().getId());
        }

        project.setCopyNewEmployeesToProjectTasks(copyNewEmployeesToProjectTasks.getValue());

        if (Utils.isEmployeeAssignmentEnable() && employeeAssignment.getSelectedId() != null && EmployeeAssignmentEnum.BY_POSITION.equals(EmployeeAssignmentEnum.buildWithId(employeeAssignment.getSelectedId()))) {
            project.setProjectPositions(projectPositionWidget.getProjectPositions());
            project.setProjectMemberFromTreeInfo(projectPositionWidget.getProjectMembers());
        } else {
            project.setProjectMemberFromTreeInfo(membersSelector.getSelectedData());
        }
//        project.setProjectMemberFromTreeInfo(membersSelector.getSelectedData());
        ArrayList<Integer> backupManagerIDs = new ArrayList<>();
        for (Map<String, Widget> row : backupManagerTable.getWidgets()) {
            if (row != null) {
                DataListBox db = (DataListBox) row.get(MultiTable.LIST_BOX);
                if (db != null && db.getSelectedItem() != null && db.getSelectedItem().getId() != null) {
                    if (!backupManagerIDs.contains(db.getSelectedItem().getId())) {
                        backupManagerIDs.add(db.getSelectedItem().getId());
                    }
                }
            }
        }
        project.setBackupManagerIDs(backupManagerIDs);
        LoadingPanel.loading(true);
        ProjectService.App.get().updateProject(project, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable caught) {
                enabledButtons(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);

            }

            @Override
            public void success(Void result) {
                enabledButtons(true);
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.changes()), Info.Type.INFO);
                onShellOk(closeTabT);
                if (isParentNullProject) {// is project parent null that refresh project
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PROJECT_MEMBER_ADD, projectID, ProjectMembersView.this);
                } else { // this is sub project refresh sub project
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SUB_PROJECT_MEMBER_ADD, projectID, ProjectMembersView.this);
                }
                if (fireSaveMembers != null) {
                    fireSaveMembers.execute();
                }
            }
        });
    }

    private void setManagers() {
        int selectedLeader = manager.getSelectedItem() != null ? manager.getSelectedItem().getId() : 0;
        manager.clear();
        manager.setSelectedNullLabel();
        manager.setEnabled(false);

        SelectItem[] selection = membersSelector.getSelectedItems();
        Set<SelectItem> managerList = new HashSet<>();
        if (Utils.isEmployeeAssignmentEnable() && EmployeeAssignmentEnum.BY_POSITION.equals(EmployeeAssignmentEnum.buildWithId(employeeAssignment.getSelectedId() != null ? employeeAssignment.getSelectedId() : 0))
                &&  projectPositionWidget != null) {
            if (projectPositionWidget.getSelectedMemebers().length > 0) {
                managerList.addAll(Arrays.asList(projectPositionWidget.getSelectedMemebers()));
            }
        }
        if (Utils.isEmployeeAssignmentEnable() && systemManagers.size() > 0) {
            managerList.addAll(systemManagers);
            selection = managerList.toArray(new SelectItem[]{});
        }

        manager.setItems(selection);
        backupManagerItems = new SelectItem[selection.length];
        if (selectedLeader != 0) {
            manager.setSelected(selectedLeader);
            if (manager.getSelectedItem() != null) {
                backupManagerItems = new SelectItem[selection.length > 0 ? selection.length - 1 : 0];
            }
        }
        manager.setEnabled(manager.getItemCount() > 0);
        int i = 0;
        for (SelectItem item : selection) {
            if (selectedLeader != item.getId()) {
                backupManagerItems[i] = item;
                i++;
            }
        }

        int[] selectedBackupLeaderIDs = new int[backupManagerTable.getWidgets().size()];
        int count = 0;
        for (Map<String, Widget> row : backupManagerTable.getWidgets()) {
            if (row != null) {
                DataListBox db = (DataListBox) row.get(MultiTable.LIST_BOX);
                if (db != null && db.getSelectedItem() != null && db.getSelectedItem().getId() != null) {
                    selectedBackupLeaderIDs[count] = db.getSelectedItem().getId();
                }
                count++;
            }
        }
        backupManagerTable.removeAllRows();
        for (int selectedBackupLeaderID : selectedBackupLeaderIDs) {
            backupManagerTable.addWidgets(getBackupManagersMap(selectedBackupLeaderID));
        }
    }

    private boolean validate() {
        int errors = 0;
        form.cleanupErrors();
        if (!Validation.validateListBoxRequired(manager, managerField, wfmStrings.pleaseSpecifyManager())) {
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
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