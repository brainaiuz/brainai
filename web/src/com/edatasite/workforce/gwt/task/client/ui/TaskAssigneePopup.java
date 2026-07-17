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
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.MultiSelectionModel;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * User: Ilxom Lutfullaev
 * Date: 8/21/13
 * Time: 4:44 PM
 */

public class TaskAssigneePopup extends KpiModal implements Constants {

    private final static ProjectStrings projectStrings = ProjectStrings.App.get();

    private KpiCheckBox startResourceCalculationForNewAssigneesFromTodayBox;
    private final KpiCellTree dynamicSelectorNew;

    public TaskAssigneePopup(final ArrayList<Integer> taskIDs) {
        super();
        setCloseButton(true);
        setTitle(projectStrings.addAssigneesToSelectedTasks());
        setSize(800, 370);
        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.setSpacing(3);

        Div row = new Div();
        row.setStyleName("row");

        Div coll = new Div();
        coll.setStyleName("col-12");

        dynamicSelectorNew = new KpiCellTree();
        dynamicSelectorNew.drawSelectedSide(new SelectionContainer() {
            @Override
            public void selectedDataGrid(final KpiDataGrid<KpiTreeInfo> selectedDataGrid, ColumnSortEvent.ListHandler<KpiTreeInfo> sortHandler, final MultiSelectionModel<KpiTreeInfo> selectionModel) {
                selectedDataGrid.addRowCountChangeHandler(event -> {
                    for (KpiTreeInfo object : ((KpiDataGrid<KpiTreeInfo>) event.getSource()).getList()) {
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
                sortHandler.setComparator(employee, (o1, o2) -> o1.getName().compareTo(o2.getName()));
                selectedDataGrid.addColumn(employee, wfmStrings.employee());
                selectedDataGrid.setColumnWidth(employee, 40, com.google.gwt.dom.client.Style.Unit.PCT);
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
        reloadAssigneesTree();

        coll.add(dynamicSelectorNew);
        row.add(coll);
        mainPanel.add(row);

        if ("true".equals(Utils.userSettings.get(RESOURCE_UTILIZATION_ENABLED)) && !Utils.hasGenericAccess(GenericSettingsEnum.ENABLED_MANNUAL_RU_ESTIMATE)) {
            startResourceCalculationForNewAssigneesFromTodayBox = new KpiCheckBox(projectStrings.startResourceCalculationForNewAssigneesFromToday());
            startResourceCalculationForNewAssigneesFromTodayBox.setValue(true);
            startResourceCalculationForNewAssigneesFromTodayBox.ensureDebugId("add_assignees_to_selected_tasks_start_resource_calculation");
            mainPanel.add(startResourceCalculationForNewAssigneesFromTodayBox);
        }

        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> saveTaskNewAssignees(taskIDs));
        addButton(saveButton);
        add(mainPanel);
    }

    private void reloadAssigneesTree() {
        LoadingPanel.loading(true);
        TaskService.App.get().getAssigneesWithTreeInfoLinkedHashMap(null, new AbstractAsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>>() {
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

    private void saveTaskNewAssignees(ArrayList<Integer> taskIDs) {
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
        TaskService.App.get().addAssigneesToTask(taskIDs, assignees, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void success(Void result) {
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_ADD, null, TaskAssigneePopup.this);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CALENDAR_EVENT_ADD, null, TaskAssigneePopup.this);
                close();
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.taskAssignees()));
            }
        });
    }
}