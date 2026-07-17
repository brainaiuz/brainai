package com.edatasite.workforce.gwt.task.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.IdTime;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiCellTree;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.SelectionContainer;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskServiceAsync;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.MultiSelectionModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * User: Dilshod Madrahimov
 * Date: 5/3/14
 * Time: 3:39 PM
 */
public class TaskEstimatePopup extends KpiModal implements Constants {

    private final static ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final TaskServiceAsync taskService = TaskService.App.get();

    private KpiCheckBox startResourceCalculationForNewAssigneesFromTodayBox;
    private final KpiCellTree dynamicSelectorNew;
    private Integer taskID = null;

    public TaskEstimatePopup(final Integer taskID) {
        super();
        this.taskID = taskID;
        setDismissible(false);
        setTitle(projectStrings.taskEstimatedTime());
//        setSize(627, 370);
        setWidth(627);
        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.setSpacing(3);

        dynamicSelectorNew = new KpiCellTree();
        dynamicSelectorNew.drawSelectedSide(new SelectionContainer() {
            @Override
            public void selectedDataGrid(final KpiDataGrid<KpiTreeInfo> selectedDataGrid, ColumnSortEvent.ListHandler<KpiTreeInfo> sortHandler, final MultiSelectionModel<KpiTreeInfo> selectionModel) {
                selectedDataGrid.addRowCountChangeHandler(event -> {

                    // setManagers();
                });
                //employee name
                Column<KpiTreeInfo, String> employee = new Column<KpiTreeInfo, String>(new TextCell()) {
                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        return object.getName();
                    }
                };
                employee.setSortable(true);
                sortHandler.setComparator(employee, (o1, o2) -> o1.getName().compareTo(o2.getName()));
                selectedDataGrid.addColumn(employee, wfmStrings.employee());
                selectedDataGrid.setColumnWidth(employee, 40, com.google.gwt.dom.client.Style.Unit.PCT);
                //estimate date
                Column<KpiTreeInfo, String> time = null;
                if (!Utils.hasGenericAccess(GenericSettingsEnum.ENABLED_MANNUAL_RU_ESTIMATE))  {
                    final TextInputCell textInputCell = new TextInputCell();
                    time = new Column<KpiTreeInfo, String>(textInputCell) {
                        @Override
                        public String getValue(final KpiTreeInfo object) {
                            return Utils.formatMinutes(object.getTime());
                        }
                    };
                    textInputCell.setWidth("50px");
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
                selectedDataGrid.addColumn(action, wfmStrings.action());
                selectedDataGrid.setColumnWidth(action, 20, com.google.gwt.dom.client.Style.Unit.PCT);
            }

            @Override
            public void additionalActions(HTMLPanel actionsPanel) {
            }
        });
        reloadTaskEstimatedTimes();

        //save button
        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> saveTaskEstimatedTimesAndAssignees(taskID));
        saveButton.ensureDebugId("add_assignees_to_selected_tasks_save_button");
        //close button
        WfmButton2 closeButton = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_RESET, clickEvent -> close());
        closeButton.ensureDebugId("add_assignees_to_selected_tasks_close_button");
        mainPanel.add(dynamicSelectorNew);
        if ("true".equals(Utils.userSettings.get(RESOURCE_UTILIZATION_ENABLED)) && !Utils.hasGenericAccess(GenericSettingsEnum.ENABLED_MANNUAL_RU_ESTIMATE)) {
            startResourceCalculationForNewAssigneesFromTodayBox = new KpiCheckBox(projectStrings.startResourceCalculationForNewAssigneesFromToday());
            startResourceCalculationForNewAssigneesFromTodayBox.setValue(true);
            startResourceCalculationForNewAssigneesFromTodayBox.ensureDebugId("add_assignees_to_selected_tasks_start_resource_calculation");
            mainPanel.add(startResourceCalculationForNewAssigneesFromTodayBox);
        }
        add(mainPanel);
        addButton(closeButton);
        addButton(saveButton);
    }

    private void reloadTaskEstimatedTimes() {
        LoadingPanel.loading(true);
        taskService.getTaskMembersWithTreeInfo(taskID, new AbstractAsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>>() {
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

    /**
     * Update task estimated time and assignee
     *
     * @param assignees -  IdTime object {id=project employee id, time=time estimated time}
     */
    private void saveTaskEstimatedTimesAndAssignees(Integer taskID) {

        ArrayList<IdTime> assignees = new ArrayList<>();
        IdTime iTime;
        if (dynamicSelectorNew.getSelectedData() != null && dynamicSelectorNew.getSelectedData().size() > 0) {
            for (KpiTreeInfo save : dynamicSelectorNew.getSelectedData()) {
                iTime = new IdTime(save.getId(), save.getTime());
                iTime.setChangeEstimateTime(true);
                if (startResourceCalculationForNewAssigneesFromTodayBox != null) {
                    iTime.setStartResourceCalculationForNewAssigneesFromToday(startResourceCalculationForNewAssigneesFromTodayBox.getValue());
                }
                assignees.add(iTime);
            }
        }
        LoadingPanel.loading(true);
        /*taskService.updateTaskEstimatedTimesAndAssignees(taskID, assignees, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.error("", projectStrings.errorOcuredUpdate());
            }

            @Override
            public void success(Void result) {
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_ADD, null, TaskEstimatePopup.this);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CALENDAR_EVENT_ADD, null, TaskEstimatePopup.this);
                close();
                LoadingPanel.loading(false);
                Info.show("", projectStrings.taskAssigneesUpdatedSuccessfully());
            }
        });*/

        taskService.saveTaskAssignees(taskID, assignees.toArray(new IdTime[assignees.size()]), new AbstractAsyncCallback<Integer[]>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer[] result) {
                close();
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_MEMBERS_EDIT, result, TaskEstimatePopup.this);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.taskAssignees()), Info.Type.INFO);

            }
        });
    }
}
