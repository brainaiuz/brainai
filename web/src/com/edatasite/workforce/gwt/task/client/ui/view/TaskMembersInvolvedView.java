package com.edatasite.workforce.gwt.task.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.ExistingAndNewTaskMembers;
import com.edatasite.workforce.gwt.core.client.rpc.IdTime;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.cell.SelectItemCell;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskServiceAsync;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TaskMembersInvolvedView extends View implements Constants {

    private final Integer taskID;
    private static final TaskServiceAsync taskService = TaskService.App.get();
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();

    private WfmForm form;
    private SelectItem[] taskStatusItems = null;
    private List<SelectItem> statusNames;
    private DataListBox dateBox;
    public static final String EMPLOYEE = "employee";
    public static final String POSITION = "position";
    private VerticalPanel verticalPanel;
    private boolean bag = true;
    private boolean estimateTimeChange;
    private KpiCheckBox startResourceCalculationForNewAssigneesFromTodayBox;

    private Map<Integer, Boolean> estimateTimeChangeMap;
    private Map<Integer, Integer> oldEstimateTime;

    private KpiDataGrid<PositionsSelectItem> dataGrid;
    private ListDataProvider<PositionsSelectItem> dataProvider;
    private final Map<Integer, PositionsSelectItem> positionEmployeesMap = new HashMap<>();
    private ColumnSortEvent.ListHandler<PositionsSelectItem> sortHandler;

    public static final ProvidesKey<PositionsSelectItem> KEY_PROVIDER = item -> item == null ? null : item.getPositionId();

    public TaskMembersInvolvedView(Integer taskID) {
        super("members", wfmStrings.membersInvolved());
        this.taskID = taskID;
    }

    @Override
    public String getIconStyle() {
        return "member members-list";
    }

    private void supplyProvider(PositionsSelectItem[] reportResults) {
        List<PositionsSelectItem> tableses = dataProvider.getList();
        tableses.clear();
        dataGrid.setPageSize(200);
        dataGrid.setPixelSize(650, 200);
        Collections.addAll(tableses, reportResults);
    }

    private void addDataDisplay(HasData<PositionsSelectItem> display) {
        dataProvider.addDataDisplay(display);
    }

    private void getFirstRequest() {
        dataProvider = new ListDataProvider<>();
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        sortHandler = new ColumnSortEvent.ListHandler<>(dataProvider.getList());
        dataGrid.addColumnSortHandler(sortHandler);

        estimateTimeChangeMap = new LinkedHashMap<>();
        oldEstimateTime = new LinkedHashMap<>();

        LoadingPanel.loading(true);
        taskService.getEditTaskStatusDrop(taskID, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void success(SelectItem[] result) {
                taskStatusItems = result;

                statusNames = new ArrayList<>();
                Collections.addAll(statusNames, taskStatusItems);
                taskService.getAssigneesWithPositions(taskID, null, new AbstractAsyncCallback<ExistingAndNewTaskMembers>() {
                    @Override
                    public void success(final ExistingAndNewTaskMembers res) {
                        LoadingPanel.loading(false);
                        if (res.getPermission() == EDIT) {
                            supplyProvider(res.getExistingMembers());
                            initEditForm(res);
                            taskService.getTaskPositionsAsPSI(taskID, new AbstractAsyncCallback<PositionsSelectItem[]>() {
                                public void success(PositionsSelectItem[] items) {
                                    initEditTableColumns();
                                    addDataDisplay(dataGrid);

                                }
                            });
                        } else {
                            supplyProvider(res.getExistingMembers());
                            initTableColumns();
                            addDataDisplay(dataGrid);
                        }
                    }
                });
            }
        });
    }

    public void initEditForm(ExistingAndNewTaskMembers items) {

        clear();
        form = new WfmForm();
        form.setStyleName("padding10");
        hideEmployeeListBox();
        VerticalPanel vertu = new VerticalPanel();
        verticalPanel = new VerticalPanel();
        verticalPanel.getElement().getStyle().setProperty("border", "1px solid #78a7c2");
        dateBox.setWidth("120px");
        final PositionsSelectItem[] newMembers = items.getNewMembers();
        if (newMembers != null && newMembers.length != 0) {

            for (PositionsSelectItem newMember : newMembers) {
                positionEmployeesMap.put(newMember.getId(), newMember);
            }
            dateBox.setItems(newMembers);
        }

        if (positionEmployeesMap.size() > 0) {
            dateBox.addValueChangeHandler(event -> {
                SelectItem item = dateBox.getSelectedItem();
                addContact(positionEmployeesMap.get(item.getId()));
                if (dateBox.getSelectedIndex() > 0) {
                    dateBox.removeBySelectItemId(positionEmployeesMap.get(item.getId()));
                }

                dateBox.setSelectedIndex(0);
            });
            if (Utils.hasPermission(PermissionConstants.PM_ASSIGN_TASK_TO_MEMBER)) {
                vertu.add(dateBox);
            }
        }
        verticalPanel.add(dataGrid);
        vertu.add(verticalPanel);
        form.addField(wfmStrings.assignees(), vertu, true);

        if ("true".equals(Utils.userSettings.get(RESOURCE_UTILIZATION_ENABLED)) && !Utils.hasGenericAccess(GenericSettingsEnum.ENABLED_MANNUAL_RU_ESTIMATE)) {
            startResourceCalculationForNewAssigneesFromTodayBox = new KpiCheckBox(projectStrings.startResourceCalculationForNewAssigneesFromToday());
            startResourceCalculationForNewAssigneesFromTodayBox.setValue(true);
            form.addField(null, startResourceCalculationForNewAssigneesFromTodayBox, false);
        }

        WfmButton2 updateButton = new WfmButton2(wfmStrings.update());

        updateButton.addClickHandler(sender -> {
            if (bag) {

                if (estimateTimeChangeMap.size() > 0) {
                    for (Boolean chET : estimateTimeChangeMap.values()) {
                        if (chET) {
                            estimateTimeChange = true;
                            break;
                        }
                    }
                }

                if (estimateTimeChange) {
                    if ("true".equals(Utils.userSettings.get(RESOURCE_UTILIZATION_ENABLED))) {
                        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.changingEstimatedHoursWillOverwritePreviouslyPlannedEstimatesOnResourceUtilizationView());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                updateChangesTaskAssignees();
                            }
                        });
                        messageBox.open();
                    } else {
                        updateChangesTaskAssignees();
                    }
                } else {
                    updateChangesTaskAssignees();
                }
            }
        }

        );

        form.addButton(updateButton);
        add(form);
    }

    private void hideEmployeeListBox() {
        dateBox = new DataListBox();
        dateBox.setVisible(true);
    }

    /**
     * Add a new contact.
     *
     * @param contact the contact to add.
     */
    private void addContact(PositionsSelectItem contact) {
        List<PositionsSelectItem> contacts = dataProvider.getList();
        contacts.remove(contact);
        contacts.add(contact);
    }

    //Edit
    private void initEditTableColumns() {
        //Employee
        Column<PositionsSelectItem, String> employee = new Column<PositionsSelectItem, String>(new TextCell()) {
            @Override
            public String getValue(final PositionsSelectItem object) {
                return object.getName();
            }
        };
        employee.setSortable(true);
        sortHandler.setComparator(employee, (o1, o2) -> o1.getName().compareTo(o2.getName()));
        dataGrid.addColumn(employee, wfmStrings.employee());
        dataGrid.setColumnWidth(employee, 18.5, com.google.gwt.dom.client.Style.Unit.PCT);
        //Position
        Column<PositionsSelectItem, String> position = new Column<PositionsSelectItem, String>(new TextCell()) {
            @Override
            public String getValue(final PositionsSelectItem object) {
                return object.getPositionName();
            }
        };
        dataGrid.addColumn(position, wfmStrings.position());
        dataGrid.setColumnWidth(position, 10, com.google.gwt.dom.client.Style.Unit.PCT);
        //Task Status
        SelectItemCell selectionCell = new SelectItemCell(statusNames);
        selectionCell.setWidth("100px");
        Column<PositionsSelectItem, SelectItem> statusColumn = new Column<PositionsSelectItem, SelectItem>(selectionCell) {
            @Override
            public SelectItem getValue(PositionsSelectItem object) {
                return new SelectItem(object.getStatusId());
            }
        };
        dataGrid.addColumn(statusColumn, wfmStrings.status());
        statusColumn.setFieldUpdater((index, object, value) -> object.setStatusId(value.getId()));
        dataGrid.setColumnWidth(statusColumn, 20, com.google.gwt.dom.client.Style.Unit.PCT);
        Column<PositionsSelectItem, String> time = null;
        if (!Utils.hasGenericAccess(GenericSettingsEnum.ENABLED_MANNUAL_RU_ESTIMATE)) {
            TextInputCell textInputCellT = new TextInputCell();
            time = new Column<PositionsSelectItem, String>(textInputCellT) {
                @Override
                public String getValue(final PositionsSelectItem object) {
                    return Utils.formatMinutes(object.getTime());
                }
            };
            textInputCellT.setWidth("50px");
        } else {
            time = new Column<PositionsSelectItem, String>(new TextCell()) {
                @Override
                public String getValue(final PositionsSelectItem object) {
                    return Utils.formatMinutes(object.getTime());
                }
            };
        }


        time.setFieldUpdater((index, object, value) -> {
            if (!"".equals(value)) {
                value = validateEstimateTime(value);
            }
            if (!"".equals(value)) {
                if (estimateTimeChangeMap != null) {
                    estimateTimeChangeMap.put(object.getId(), !Utils.formatMinutes(object.getTime()).equals(value));
                }
                object.setTime(Utils.parseMinutes(value));
            }
        });
        dataGrid.addColumn(time, wfmStrings.estimatedTime());
        dataGrid.setColumnWidth(time, 15, com.google.gwt.dom.client.Style.Unit.PCT);
        //Actual Time
        Column<PositionsSelectItem, String> actualTime = new Column<PositionsSelectItem, String>(new TextCell()) {
            @Override
            public String getValue(final PositionsSelectItem object) {
                return Utils.formatMinutes(object.getActualTime() != null ? object.getActualTime() : 0);
            }
        };
        dataGrid.addColumn(actualTime, wfmStrings.actualTime());
        dataGrid.setColumnWidth(actualTime, 12.5, com.google.gwt.dom.client.Style.Unit.PCT);
        //Percent
        if ("true".equals(Utils.userSettings.get(ISAUTOMATIC))) {
            Column<PositionsSelectItem, String> percent = new Column<PositionsSelectItem, String>(new TextCell()) {
                @Override
                public String getValue(final PositionsSelectItem object) {
                    return String.valueOf(object.getPercent() != null ? object.getPercent() : 0);
                }
            };
            dataGrid.addColumn(percent, wfmStrings.percentCompleted());
            dataGrid.setColumnWidth(percent, 12.5, com.google.gwt.dom.client.Style.Unit.PCT);
        } else {
            TextInputCell textInputCell = new TextInputCell();
            Column<PositionsSelectItem, String> percent = new Column<PositionsSelectItem, String>(textInputCell) {
                @Override
                public String getValue(final PositionsSelectItem object) {
                    return String.valueOf(object.getPercent() != null ? object.getPercent() : 0);
                }
            };
            textInputCell.setWidth("50px");
            percent.setFieldUpdater((index, object, value) -> {
                if (!value.equals("") && (value.matches(Constants.REGEX_REAL_NUMBERS_WITH_COMMA) || value.matches(Constants.REGEX_REAL_NUMBERS_WITH_DOT))) {
                    bag = true;
                    value = value.replace(",", ".");
                    object.setPercent(Float.valueOf(value));
                } else {
                    bag = false;
                }
            });
            dataGrid.addColumn(percent, wfmStrings.percentCompleted());
            dataGrid.setColumnWidth(percent, 13, com.google.gwt.dom.client.Style.Unit.PCT);
        }
        //Action
        if ((Utils.hasRole(Utils.PM) || Utils.hasRole(Utils.DR) || Utils.hasRole(Utils.ADMIN))) {
            final Column<PositionsSelectItem, String> action = new Column<PositionsSelectItem, String>(new SimpleLinkCell()) {
                @Override
                public String getValue(final PositionsSelectItem object) {
                    return wfmStrings.delete();
                }
            };
            action.setFieldUpdater((index, object, value) -> {
                List<PositionsSelectItem> contacts = dataProvider.getList();
                contacts.remove(object);
                positionEmployeesMap.put(object.getId(), object);
                dateBox.addListItem(positionEmployeesMap.get(object.getId()));
            });
            dataGrid.addColumn(action, wfmStrings.action());
            dataGrid.setColumnWidth(action, 11, com.google.gwt.dom.client.Style.Unit.PCT);
        }
    }

    private String validateEstimateTime(String value) {
        if (!"".equals(value)) {
            try {
                float estimateTimeT = Utils.parseMinutes(value);
                if (estimateTimeT == 0) {
//                    bag = false;
                    return "00:00";
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
//                bag = false;
                return "00:00";
            }
        }
        return "00:00";
    }

    //View
    private void initTableColumns() {
        clear();
        //Position
        Column<PositionsSelectItem, String> position = new Column<PositionsSelectItem, String>(new TextCell()) {
            @Override
            public String getValue(PositionsSelectItem object) {
                return object.getPositionName() != null ? object.getPositionName() : "N/A";
            }
        };
        dataGrid.addColumn(position, wfmStrings.position());
        dataGrid.setColumnWidth(position, 20, com.google.gwt.dom.client.Style.Unit.PCT);
        //Employee
        Column<PositionsSelectItem, String> employee = new Column<PositionsSelectItem, String>(new TextCell()) {
            @Override
            public String getValue(PositionsSelectItem object) {
                return object.getName();
            }
        };
        dataGrid.addColumn(employee, wfmStrings.employee());
        dataGrid.setColumnWidth(employee, 20, com.google.gwt.dom.client.Style.Unit.PCT);

        form = new WfmForm();
        verticalPanel = new VerticalPanel();
        verticalPanel.getElement().getStyle().setProperty("border", "1px solid #add2e4");
        dataGrid.setSize("320px", "200px");
        verticalPanel.add(dataGrid);
        form.addField(wfmStrings.taskAssignees(), verticalPanel);
        add(form);
    }

    @Override
    protected Widget onInitialize() {
        getFirstRequest();
        return null;
    }

    private void updateChangesTaskAssignees() {
        LoadingPanel.loading(true);
        IdTime[] projectEmployee = new IdTime[dataProvider.getList().size()];
        int i = 0;
        for (PositionsSelectItem selectItem : dataProvider.getList()) {
            IdTime idTime = new IdTime(selectItem.getId(), selectItem.getTime() != null ? selectItem.getTime() : 0,
                    selectItem.getActualTime() != null ? selectItem.getActualTime() : 0,
                    selectItem.getPercent() != null && selectItem.getPercent() > 100f ? Float.valueOf(100) : selectItem.getPercent(),
                    selectItem.getStatusId());
            if (estimateTimeChangeMap != null && estimateTimeChangeMap.size() > 0) {
                Boolean isChangeEstimateTime = estimateTimeChangeMap.get(selectItem.getId());
                idTime.setChangeEstimateTime((isChangeEstimateTime != null) ? isChangeEstimateTime : Boolean.valueOf(false));
            }
            if (startResourceCalculationForNewAssigneesFromTodayBox != null) {
                idTime.setStartResourceCalculationForNewAssigneesFromToday(startResourceCalculationForNewAssigneesFromTodayBox.getValue());
            }
            if ((Integer.valueOf(COMPLETED).equals(selectItem.getStatusId()) || selectItem.getPercent() != null && selectItem.getPercent() > 100f) && !Utils.hasGenericAccess(GenericSettingsEnum.PROJECT_PERCENT_OVER_HUNDRED)) {
                idTime.setPercent(100f);
            }
            projectEmployee[i++] = idTime;
        }

        taskService.saveTaskAssignees(taskID, projectEmployee, new AbstractAsyncCallback<Integer[]>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer[] result) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_MEMBERS_EDIT, result, TaskMembersInvolvedView.this);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.taskAssignees()), Info.Type.INFO);
                closeTab();
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
}
