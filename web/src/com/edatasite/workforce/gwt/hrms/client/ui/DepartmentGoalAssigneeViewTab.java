package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.GoalAssigneeItem;
import com.edatasite.workforce.gwt.core.client.rpc.GoalItem;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import java.util.Collections;
import java.util.List;

public class DepartmentGoalAssigneeViewTab extends Composite {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private KpiDataGrid<GoalAssigneeItem> dataGrid;
    private ListDataProvider<GoalAssigneeItem> dataProvider;
    private final String emptyMessage;
    private GoalAssigneeItem[] goalAssigneeItem;

    public static final ProvidesKey<GoalAssigneeItem> KEY_PROVIDER = item -> item == null ? null : item.getId();

    public DepartmentGoalAssigneeViewTab(String emptyMessage) {
        this.emptyMessage = emptyMessage;
        initData();
        initTableColumns();
    }

    private void initTableColumns() {

        //employee
        final Column<GoalAssigneeItem, String> employee = new Column<GoalAssigneeItem, String>(new TextCell()) {
            @Override
            public String getValue(GoalAssigneeItem object) {
                return object.getName();
            }
        };
        dataGrid.addColumn(employee, wfmStrings.employee());
        dataGrid.setColumnWidth(employee, 25, Style.Unit.PCT);


        //target
        Column<GoalAssigneeItem, String> target = new Column<GoalAssigneeItem, String>(new TextCell()) {
            @Override
            public String getValue(final GoalAssigneeItem object) {
                return object.getTarget() > 0 ? String.valueOf(object.getTarget()) : "0";
            }
        };
        dataGrid.addColumn(target, wfmStrings.target());
        dataGrid.setColumnWidth(target, 20, Style.Unit.PCT);


        //actual
        Column<GoalAssigneeItem, String> actual = new Column<GoalAssigneeItem, String>(new TextCell()) {
            @Override
            public String getValue(final GoalAssigneeItem object) {
                return object.getActual() > 0 ? String.valueOf(object.getActual()) : "0";

            }
        };
        dataGrid.addColumn(actual, wfmStrings.actual());
        dataGrid.setColumnWidth(actual, 20, Style.Unit.PCT);
    }

    public void draw() {
        if (goalAssigneeItem == null || goalAssigneeItem.length == 0) {
            dataProvider.getList().clear();
            dataGrid.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(emptyMessage, null, null));
        } else {
            drawInitialize(goalAssigneeItem);
        }
        dataProvider.refresh();
    }

    public void setItem(GoalItem goalItem) {
        this.goalAssigneeItem = goalItem.getGoalAssigneeItem();
    }

    private void addDataDisplay(HasData<GoalAssigneeItem> display) {
        dataProvider.addDataDisplay(display);
    }

    private void drawInitialize(GoalAssigneeItem[] goalAssigneeItems) {
        List<GoalAssigneeItem> dataProviderList = dataProvider.getList();
        dataProviderList.clear();
        Collections.addAll(dataProviderList, goalAssigneeItems);
    }

    private void initData() {
        dataProvider = new ListDataProvider<>();
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataGrid.addStyleName("cellBasedWidget-mod cellBasedWidget-mod--static-body");
        dataGrid.getElement().getStyle().setOverflow(Style.Overflow.AUTO);
        dataGrid.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(wfmStrings.noDataAvailable(), "", ""));
        dataGrid.setSize("100%", "100%");
        addDataDisplay(dataGrid);
        initWidget(dataGrid);
    }

}