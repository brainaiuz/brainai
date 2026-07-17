package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.GoalAssigneeItem;
import com.edatasite.workforce.gwt.core.client.rpc.GoalItem;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.customtabbar.CustomTabWidget;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * User: JavaZone
 * Date: Nov 23, 2011
 * Time: 7:08:45 PM
 */
public class GoalWeightEditTab extends CustomTabWidget {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private KpiDataGrid<GoalItem> dataGrid;
    private ListDataProvider<GoalItem> dataProvider;

    private final HashMap<String, Double> map = new HashMap<>();


    public static final ProvidesKey<GoalItem> KEY_PROVIDER = item -> item == null ? null : item.getObjectId();

    public GoalWeightEditTab(String tabName) {
        super(tabName);
    }

    public void addDataDisplay(HasData<GoalItem> display) {
        dataProvider.addDataDisplay(display);
    }

    @Override
    public void initData() {
        dataProvider = new ListDataProvider<>();
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataGrid.setWidth("100%");
        dataGrid.setHeight("100%");
        dataGrid.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(wfmStrings.thereAreNoGoalWeihtsYet(), "", null));
        dataGrid.getEmptyTableWidget().getElement().getStyle().setMarginTop(-50, com.google.gwt.dom.client.Style.Unit.PX);
        addDataDisplay(dataGrid);
        add(dataGrid);
        initTableColumns();
    }

    public List<GoalItem> getWeightIterator() {
        return dataProvider.getList();
    }

    public void getResult(GoalItem[] goalTableses) {
        List<GoalItem> tableses = dataProvider.getList();
        tableses.clear();
        Collections.addAll(tableses, goalTableses);
    }

    public HashMap<String, Double> getMap() {
        return map;
    }

    public void removeAll() {
        dataProvider.refresh();
    }

    @Override
    public void viewShow() {
    }

    private void initTableColumns() {
        //name
        Column<GoalItem, String> name = new Column<GoalItem, String>(new TextCell()) {
            @Override
            public String getValue(GoalItem object) {
                return object.getTitle() != null ? object.getTitle() : "";
            }
        };
        dataGrid.addColumn(name, hrmsStrings.goalTitle());
        dataGrid.setColumnWidth(name, 20, com.google.gwt.dom.client.Style.Unit.PCT);
        //description
        Column<GoalItem, String> description = new Column<GoalItem, String>(new TextCell()) {
            @Override
            public String getValue(GoalItem object) {
                return object.getDescription() != null ? object.getDescription() : "";
            }
        };
        dataGrid.addColumn(description, wfmStrings.description());
        dataGrid.setColumnWidth(description, 20, com.google.gwt.dom.client.Style.Unit.PCT);
        //weight
        Column<GoalItem, String> weight = new Column<GoalItem, String>(new TextInputCell()) {
            @Override
            public String getValue(GoalItem object) {
                StringBuilder s = new StringBuilder();
                if (object.getGoalAssigneeItem() != null) {
                    object.getGoalAssigneeItem();
                    for (GoalAssigneeItem goalAssignee : object.getGoalAssigneeItem()) {
                        if (goalAssignee != null) {
                            s.append(goalAssignee.getObjectId().toString()).append(",");
                        }
                    }
                }
                if (s.length() > 0) {
                    if (!map.containsKey(s.toString())) {
                        map.put(s.toString(), Double.parseDouble("" + object.getWeight()));
                    }
                }
                return "" + object.getWeight();
            }
        };
        weight.setFieldUpdater((index, object, value) -> {
            StringBuilder s = new StringBuilder();
            if (object.getGoalAssigneeItem() != null) {
                object.getGoalAssigneeItem();
                for (GoalAssigneeItem goalAssignee : object.getGoalAssigneeItem()) {
                    if (goalAssignee != null) {
                        s.append(goalAssignee.getObjectId().toString()).append(",");
                    }
                }
            }
            if (s.length() > 0) {
                map.put(s.toString(), Double.parseDouble(value));
            }
        });
        dataGrid.addColumn(weight, hrmsStrings.goalWeight());
        dataGrid.setColumnWidth(weight, 15, com.google.gwt.dom.client.Style.Unit.PCT);
        //id
        Column<GoalItem, String> id = new Column<GoalItem, String>(new TextCell()) {
            @Override
            public String getValue(GoalItem object) {
                StringBuilder s = new StringBuilder();
                if (object.getGoalAssigneeItem() != null) {
                    object.getGoalAssigneeItem();
                    for (GoalAssigneeItem goalAssignee : object.getGoalAssigneeItem()) {
                        if (goalAssignee != null) {
                            s.append(goalAssignee.getObjectId().toString()).append(",");
                        }
                    }
                }
                if (s.length() > 0) {
                    return s.substring(0, s.length() - 1);
                }
                return s.toString();
            }
        };
        dataGrid.addColumn(id, "id");
        dataGrid.setColumnWidth(id, 20, com.google.gwt.dom.client.Style.Unit.PCT);
    }
}