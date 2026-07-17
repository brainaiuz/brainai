package com.edatasite.workforce.gwt.employee.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.GoalAssigneeItem;
import com.edatasite.workforce.gwt.core.client.rpc.GoalItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.Errors;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.AddListener;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.ColumnStatements;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.hrms.client.rpc.GroupGoalITem;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;

public class MultiGoalPanel extends Div implements Constants, Errors {

    public static final String TITLE = "TITLE";
    private static final String TARGET = "TARGET";
    private static final String ACTUAL = "ACTUAL";
    private static final String WEIGHT = "WEIGHT";
    public static final String DESCRIPTION = "DESCRIPTION";


    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private DynamicTable goalsTable;

    protected SelectItem[] validityPeriods;

    MultiGoalPanel() {
        initEmployeesTable();
    }

    void createStandartRows() {
        for (int i = 1; i <= 3; i++) {
            Widget[] widgets = getWidgetArray(null);
            goalsTable.addRow(widgets);
        }
    }

    void fillTable(ArrayList<GoalItem> goalItems) {
        for (GoalItem goalItem : goalItems) {
            Widget[] widgets = getWidgetArray(goalItem);
            goalsTable.addRow(widgets);
        }
    }

    public void save(GroupGoalITem groupGoalITem) {
        if (validateGoals()) {
            groupGoalITem.setGoalItems(getPersonalGoals());
            LoadingPanel.loading(true);
            HrmsService.App.get().createGroupGoals(groupGoalITem, new AbstractAsyncCallback<Void>() {
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                public void success(Void o) {
                    LoadingPanel.loading(false);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_GROUP_GOAL_ADD, o, MultiGoalPanel.this);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_GROUP_GOAL_CLOSE, o, MultiGoalPanel.this);
                }
            });
        }
    }

    private ArrayList<GoalItem> getPersonalGoals() {
        ArrayList<GoalItem> goals = new ArrayList<>();

        for (int i = 0; i < goalsTable.getRowNumber(); i++) {
            if (validateRow(i).isRowValid()) {
                DynamicTableItem row = goalsTable.getItem(i);
                GoalItem item = new GoalItem();

                TitleBox titleBox = (TitleBox) row.getColumnById(TITLE);
                item.setObjectId(titleBox.getGoalId());
                item.setTitle(titleBox.getValue());

                GoalAssigneeItem assigneeItem = new GoalAssigneeItem();
                TextBox actual = (TextBox) row.getColumnById(ACTUAL);
                try {
                    assigneeItem.setActual(Double.valueOf(actual.getText()));
                } catch (NumberFormatException ex) {
                    assigneeItem.setActual(0d);
                }

                TextBox target = (TextBox) row.getColumnById(TARGET);
                try {
                    assigneeItem.setTarget(Double.valueOf(target.getText()));
                } catch (NumberFormatException ex) {
                    assigneeItem.setTarget(0d);
                }
                assigneeItem.setWeight(0d);

                TextBox weight = (TextBox) row.getColumnById(WEIGHT);
                try {
                    assigneeItem.setWeight(Double.valueOf(weight.getText()));
                } catch (NumberFormatException ex) {
                    assigneeItem.setWeight(0d);
                }

                item.setGoalAssigneeItem(new GoalAssigneeItem[]{assigneeItem});

                TextArea description = (TextArea) row.getColumnById(DESCRIPTION);
                item.setDescription(description.getText());

                goals.add(item);
            }
        }
        return goals;
    }

    private boolean validateGoals() {
        goalsTable.resetValidation();
        int errors = 0;

        for (int rowId = 0; rowId < goalsTable.getRowNumber(); rowId++) {
            ValidityResponse validityResponse = validateRow(rowId);
            if (!validityResponse.isRowValid()) {
                errors++;
            }
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }

        return true;
    }

    private ValidityResponse validateRow(Integer rowId) {
        boolean rowValid = true;
        DynamicTableItem tableItem = goalsTable.getItem(rowId);
        TitleBox title = (TitleBox) tableItem.getColumnById(TITLE);
        TextArea description = (TextArea) tableItem.getColumnById(DESCRIPTION);

        if (Utils.isNullOrEmpty(title.getText())) {
            goalsTable.notValid(rowId, TITLE);
            rowValid = false;
        }
        if (!Validation.validateTextAreaRequired(description)) {
            goalsTable.notValid(rowId, DESCRIPTION);
            rowValid = false;
        }

        return new ValidityResponse(rowValid);
    }

    private void initEmployeesTable() {
        goalsTable = new DynamicTable(getColumnArray());
        goalsTable.addListener(new AddListener() {
            @Override
            public void plusClicked(int rowId) {
                Widget[] widgets = getWidgetArray(null);
                goalsTable.insertRow(rowId + 1, widgets);
            }

            @Override
            public void minusClicked(int rowId, Integer objectId) {

            }
        });
        add(goalsTable);
    }

    private DynamicTableColumn[] getColumnArray() {

        DynamicTableColumn[] columns = new DynamicTableColumn[5];
        int index = 0;
        columns[index++] = new DynamicTableColumn("<b class=customTitle>" + wfmStrings.title() + "<font color='red'>*</font>:</b>", TITLE, new ColumnStatements(".", ""), 100);
        columns[index++] = new DynamicTableColumn("<b class=customTitle>" + wfmStrings.target() + ":</b>", TARGET, new ColumnStatements(".", ""), 100);
        columns[index++] = new DynamicTableColumn("<b class=customTitle>" + wfmStrings.actual() + ":</b>", ACTUAL, new ColumnStatements(".", ""), 100);
        columns[index++] = new DynamicTableColumn("<b class=customTitle>" + wfmStrings.weight() + ":</b>", WEIGHT, new ColumnStatements(".", ""), 100);
        columns[index] = new DynamicTableColumn("<b class=customTitle>" + wfmStrings.description() + "<font color='red'>*</font>:</b>", DESCRIPTION, new ColumnStatements(".", ""), 140);
        return columns;
    }

    private Widget[] getWidgetArray(GoalItem goalItem) {

        TitleBox titleBox = new TitleBox();
        TextBox target = new TextBox();
        TextBox actual = new TextBox();
        TextBox weight = new TextBox();
        TextArea description = new TextArea();
        description.addStyleName(DEFAULT_WIDTH);

        Widget[] widgets = new Widget[5];
        int index = 0;
        widgets[index++] = titleBox;
        widgets[index++] = target;
        widgets[index++] = actual;
        widgets[index++] = weight;
        widgets[index] = description;

        if (goalItem != null) {
            titleBox.setGoalId(goalItem.getObjectId());
            titleBox.setText(goalItem.getTitle());
            if (goalItem.getGoalAssigneeItem() != null && goalItem.getGoalAssigneeItem().length > 0) {
                target.setText(String.valueOf(goalItem.getGoalAssigneeItem()[0].getTarget()));
                actual.setText(String.valueOf(goalItem.getGoalAssigneeItem()[0].getActual()));
                weight.setText(String.valueOf(goalItem.getGoalAssigneeItem()[0].getWeight()));
            }
            description.setText(goalItem.getDescription());
        }

        return widgets;

    }

    private class ValidityResponse {
        private boolean rowValid;

        ValidityResponse(boolean rowValid) {
            this.rowValid = rowValid;
        }

        boolean isRowValid() {
            return rowValid;
        }

    }

    private class TitleBox extends TextBox {
        private Integer goalId;

        Integer getGoalId() {
            return goalId;
        }

        void setGoalId(Integer goalId) {
            this.goalId = goalId;
        }
    }
}
