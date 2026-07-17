package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.GoalAssigneeItem;
import com.edatasite.workforce.gwt.core.client.rpc.GoalItem;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.ui.cell.EditDataItem;
import com.edatasite.workforce.gwt.hrms.client.ui.cell.EditPopupCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import java.util.Collections;
import java.util.List;

/**
 * User: romeo
 * Date: 6/8/12
 * Time: 10:30 PM
 */
public class GoalAssigneeViewTab extends Composite {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private KpiDataGrid<GoalAssigneeItem> dataGrid;
    private ListDataProvider<GoalAssigneeItem> dataProvider;
    private final String emptyMessage;
    private GoalAssigneeItem[] goalAssigneeItem;
    private GoalItem goalItem;

    public static final ProvidesKey<GoalAssigneeItem> KEY_PROVIDER = item -> item == null ? null : item.getId();

    public GoalAssigneeViewTab(String emptyMessage) {
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
        //weight
        Column<GoalAssigneeItem, String> weight = new Column<GoalAssigneeItem, String>(new TextCell()) {
            @Override
            public String getValue(GoalAssigneeItem object) {
                return object.getWeight().toString();
            }
        };
        dataGrid.addColumn(weight, hrmsStrings.goalWeight());
        dataGrid.setColumnWidth(weight, 10, Style.Unit.PCT);
        //available weight
        Column<GoalAssigneeItem, String> availableWeight = new Column<GoalAssigneeItem, String>(new TextCell()) {
            @Override
            public String getValue(GoalAssigneeItem object) {
                return object.getAvaWeight().toString();
            }
        };
        dataGrid.addColumn(availableWeight, hrmsStrings.availableWeight());
        dataGrid.setColumnWidth(availableWeight, 10, Style.Unit.PCT);
        //target
        Column<GoalAssigneeItem, String> target = new Column<GoalAssigneeItem, String>(new TextCell()) {
            @Override
            public String getValue(final GoalAssigneeItem object) {
                return object.getTarget() > 0 ? String.valueOf(object.getTarget()) : "";
            }
        };
        dataGrid.addColumn(target, wfmStrings.target());
        dataGrid.setColumnWidth(target, 10, Style.Unit.PCT);
        //actual
        final EditPopupCell actualCell = new EditPopupCell();
        Column<GoalAssigneeItem, EditDataItem> actual = new Column<GoalAssigneeItem, EditDataItem>(actualCell) {
            @Override
            public EditDataItem getValue(final GoalAssigneeItem object) {
                EditDataItem editDataItem = new EditDataItem();
                editDataItem.setValue(object.getActual());
                editDataItem.setObjectId(object.getObjectId());
                editDataItem.setDescription(object.getDescription());
                editDataItem.setEditable(Utils.getUserID().equals(object.getId()) || Utils.hasPermission(PermissionConstants.HRMS_EDIT_PERSONAL_GOAL));
                return editDataItem;
            }
        };
        dataGrid.addColumn(actual, wfmStrings.actual());
        dataGrid.setColumnWidth(actual, 15, Style.Unit.PCT);
        //actual updater
        actual.setFieldUpdater((index, object, value) -> {
            if (Utils.getUserID().equals(object.getId()) || Utils.hasPermission(PermissionConstants.HRMS_EDIT_PERSONAL_GOAL)) {
                object.setActual(value.getValue());
                object.setDescription(value.getDescription());
                if (goalItem.getValidityPeriodItem() != null) {
                    object.setValidityPeriodId(goalItem.getValidityPeriodItem().getId());
                }
                GoalAssigneeItem[] goalAssigneeItems = new GoalAssigneeItem[1];
                goalAssigneeItems[0] = object;
                HrmsService.App.get().saveGoalAssigneeItems(goalAssigneeItems, new AbstractAsyncCallback<Void>() {
                    @Override
                    public void success(Void result) {
                        dataProvider.refresh();
                    }
                });
            }
        });
        //comment
        Column<GoalAssigneeItem, String> comment = new Column<GoalAssigneeItem, String>(new TextCell()) {
            @Override
            public String getValue(final GoalAssigneeItem object) {
                return object.getDescription();
            }
        };
        dataGrid.addColumn(comment, wfmStrings.comment());
        dataGrid.setColumnWidth(comment, 25, Style.Unit.PCT);
        if (Utils.showScoreCalculation()) {
            //score
            Column<GoalAssigneeItem, String> score = new Column<GoalAssigneeItem, String>(new TextCell()) {
                @Override
                public String getValue(final GoalAssigneeItem object) {
                    Double score = object.getScore(goalItem.getScore().getDescription());
                    return score != null ? Utils.formatDouble(score) : "";
                }
            };
            dataGrid.addColumn(score, wfmStrings.score());
            dataGrid.setColumnWidth(score, 15, Style.Unit.PCT);
            //final score
            Column<GoalAssigneeItem, String> finalScore = new Column<GoalAssigneeItem, String>(new TextCell()) {
                @Override
                public String getValue(final GoalAssigneeItem object) {
                    Double finalScore = object.getFinalScore(goalItem.getScore().getDescription());
                    return finalScore != null ? Utils.formatDouble(finalScore) : "";
                }
            };
            dataGrid.addColumn(finalScore, hrmsStrings.finalScore());
            dataGrid.setColumnWidth(finalScore, 15, Style.Unit.PCT);
        }
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
        this.goalItem = goalItem;

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