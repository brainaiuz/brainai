package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.ReportingStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.AsyncWidget;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.TableRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.ReportingStepControlView;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.ColumnGroupWidget;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.SelectColumnItem;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.SelectColumnsReorder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.*;

/**
 * Created by Virus on 8/27/14.
 */
public class ReportingSelectColumns extends AsyncWidget {
    interface ReportingSelectColumnsUiBinder extends UiBinder<HTMLPanel, ReportingSelectColumns> {
    }

    private ReportingStepControlView view;
    private static ReportingSelectColumnsUiBinder ourUiBinder = GWT.create(ReportingSelectColumnsUiBinder.class);
    public static final ReportingStrings reportingStrings = ReportingStrings.App.get();
    public static final WfmStrings wfmStrings = WfmStrings.App.get();


    private SelectColumnsReorder columnsReorder = new SelectColumnsReorder();
    private HTMLPanel mainBody;
    private HashMap<String, SelectColumnItem> columnItems = new HashMap<>();

    public ReportingSelectColumns() {
        super(null, "customReport_tab_3");
    }

    public void setView(ReportingStepControlView view) {
        this.view = view;
    }

    protected Widget onInitialize() {
        mainBody = ourUiBinder.createAndBindUi(this);
        add(mainBody);
        super.addStyleName("collapsible--custom");
        columnsReorder.setView(view);
        loading();
        return null;
    }

    private void loading() {
        view.selectColumnsLoading(new AsyncCallback<ArrayList<TableRpc>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(ArrayList<TableRpc> result) {
                LinkedHashMap<String, String> columnsByGroupMap = view.getReport().getColumnsByGroupMap();

                if (columnsByGroupMap != null && !columnsByGroupMap.isEmpty()) {
                    columnsReorder.setCustomGroups(columnsByGroupMap);
                }

                LinkedList<ColumnRpc> cellList = new LinkedList<>(view.getReport().getSelectedColumns());
                for (ColumnRpc cell : cellList) {
                    if (!Utils.isNullOrEmpty(cell.getTitle())) {
                        columnsReorder.addColumn(cell, !view.getReport().getGroupColumns().contains(cell), false);
                    }
                }
                for (TableRpc rpc : result) {

                    final ColumnGroupWidget body = new ColumnGroupWidget(rpc);
                    body.setActive(true);
                    mainBody.add(body);

                    cellList = new LinkedList<>(rpc.getColumns());
                    cellList.sort(Comparator.comparing(o -> o.getTitle().toLowerCase()));
                    for (ColumnRpc cell : cellList) {
                        final SelectColumnItem item = body.addField(cell);
                        columnItems.put(item.getColumnRpc().getName(), item);
                        item.setCommand((timeToSelectAll) -> {
                            boolean draggable = !view.getReport().getGroupColumns().contains(item.getColumnRpc());
                            if (item.getColumnRpc().isChecked()) {
                                columnsReorder.addColumn(item.getColumnRpc(), draggable, true);
                                item.setEnabled(draggable);
                            } else {
                                columnsReorder.removeColumn(item.getColumnRpc(), true);
                            }
                            if (timeToSelectAll) {
                                body.timeToSelectAll();
                            }
                        });
                    }
                }
                reloadChanges();
                view.includeGroupingChanges();
            }
        });
    }

    public void selectAll(boolean value) {
        for (int i = 0; i < mainBody.getWidgetCount(); i++) {
            ColumnGroupWidget body = (ColumnGroupWidget) mainBody.getWidget(i);
            body.selectAll(value);
        }
    }

    private void reloadChanges() {
        view.setIncludeGroupingCommand(() -> {
            columnsReorder.clearGroup(view.getReport().getGroupColumns());
            view.getReport().setSelectedColumns(columnsReorder.getColumns(view.getReport().getGroupColumns(), view.getReport().getColumnMap()));
            for (ColumnRpc rpc : view.getReport().getSelectedColumns()) {
                SelectColumnItem item = columnItems.get(rpc.getName());
                if (item != null) { // item ba'zida null keladi. Balki nega null kelishini topish kerakdir !?
                    boolean b = view.getReport().getGroupColumns().contains(item.getColumnRpc());
                    if (b) {
                        item.makeGrouoColumn(b);
                        columnsReorder.makeGrouping(item.getColumnRpc());
                    } else {
                        item.setEnabled(true);
                        columnsReorder.makeDraggable(item.getColumnRpc());
                    }
                }
            }
            view.getReport().setModified(true);
        });
    }

    public SelectColumnsReorder getColumnsReorder() {
        return columnsReorder;
    }
}