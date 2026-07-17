package com.finnetlimited.reportservice.core.client.ui.table;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandlerAdapter;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.WfmContentPanel;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.TableRpc;
import com.finnetlimited.reportservice.core.client.enumtype.IdType;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * User: ${Dilsh0d}
 * Date: 15-Mar-2010
 * Time: 21:06:27
 */
public class SelectedColumnTable extends HTMLPanel {
    private static final WfmStrings wfmStrings= WfmStrings.App.get();

    private static final String _id = IdType.SELECTED_TABLE.getName();
    private static int num = 0;

    private String id;
    private ArrayList<TableRpc> tablesList;

    private KpiCheckBox selectAll;
    private KpiCheckBox unSelect;
    private FlexTable table;
    private FlexTable.FlexCellFormatter formatter;
    private HashMap<String, ArrayList<KpiCheckBox>> mapCheckBox = new HashMap<>();
    private HashMap<KpiCheckBox, ColumnRpc> mapColumn = new HashMap<>();
    private LinkedList<ColumnRpc> selectedGroupColumns;
    private Command command;

    //Select order columns panel
    private FlowPanel orderPanel;
    private WfmContentPanel showColumns;
    private AbsolutePanel boundaryPanel;
    private PickupDragController showColumnDragController;
    private VerticalPanel showVerticalPanel;
    private VerticalPanel showUnDraggableVerticalPanel;
    private HashMap<String, Label> showColumnMap = new HashMap<>();
    final HashMap<String, Integer> widgetsPos = new HashMap<>();
    private HashMap<ColumnRpc, Integer> columnsOrder = new HashMap<>();
    private Label top;
    private Label up;
    private Label down;
    private Label bottom;
    private ArrayList<String> selectedColumns;
    private ArrayList<ColumnRpc> allColumns;
    private ReportRpc report;
    private ArrayList<ColumnRpc> groupedColumns;


    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public SelectedColumnTable() {
        super("");
        setStyleName("mainBar");
        DOM.setElementAttribute(getElement(), "id", (_id + num));
        id = _id + num;
        num++;
//        init();
    }

    public void addReportRpc(ReportRpc reportRpc) {
        this.report = reportRpc;
    }

    public void addDataList(ArrayList<TableRpc> tableList, boolean isSelectAllCheckBox) {
        isSelectAllCheckBox = true;
        init();
        if (report != null) {
            for (ColumnRpc crpc : report.getSelectedColumns()) {
                Label lbl = new Label(crpc.getTitle());
                if (!report.getGroupColumns().contains(crpc) && !selectedColumns.contains(lbl)) {
                    selectedColumns.add(crpc.getTitle());
                }
            }
        }
        tablesList = tableList;
        if (tablesList != null && tablesList.size() != 0) {
            int step = 2;
            for (final TableRpc tableName : tablesList) {
                allColumns.addAll(tableName.getColumns());
                table.setHTML(step, 0, "<b style='text-transform: uppercase;font-weight: bold;'>" + tableName.getTableName() + "</b>");
                formatter.setStyleName(step, 0, "gheader");
                //   formatter.setColSpan(stage, 0, 3);
                // Select table column
                final KpiCheckBox selectAllColumn = new KpiCheckBox(wfmStrings.selectAll(), true);
                selectAllColumn.setName(tableName.getTableName());
                // un Select table column
                final KpiCheckBox unSelectAllColumn = new KpiCheckBox(wfmStrings.deselectAll(), true);
                unSelectAllColumn.setName(tableName.getTableName());

                selectAllColumn.addClickHandler(clickEvent -> {
                    KpiCheckBox anchor = (KpiCheckBox) clickEvent.getSource();
                    ArrayList<KpiCheckBox> checks = mapCheckBox.get(anchor.getName());
                    if (checks != null && checks.size() != 0) {
                        unSelectAllColumn.setValue(false);
                        anchor.setValue(true);
                        for (KpiCheckBox check : checks) {
                            check.setValue(true);
                            if (!selectedColumns.contains(check.getText())) {
                                selectedColumns.add(check.getText());
                            }
                        }
                        refreshDataList(selectedColumns, true);
                    }
                });
                table.setWidget(step, 1, selectAllColumn);
                formatter.setStyleName(step, 1, "gheader");

                unSelectAllColumn.addClickHandler(clickEvent -> {
                    KpiCheckBox anchor = (KpiCheckBox) clickEvent.getSource();
                    ArrayList<KpiCheckBox> checks = mapCheckBox.get(anchor.getName());
                    if (checks != null && checks.size() != 0) {
                        selectAllColumn.setValue(false);
                        anchor.setValue(true);
                        for (KpiCheckBox check : checks) {
                            if (check.isEnabled()) {
                                check.setValue(false);
                                if (selectedColumns.contains(check.getText())) {
                                    selectedColumns.remove(check.getText());
                                }
                            }
                            if (!check.isEnabled()) {
                                if (selectedColumns.contains(check.getText())) {
                                    selectedColumns.remove(check.getText());
                                }
                            }
                        }
                    }
                    refreshDataList(selectedColumns, true);
                });

                table.setWidget(step, 2, unSelectAllColumn);
                formatter.setStyleName(step, 2, "gheader");

                table.getRowFormatter().setStyleName(step, "theader");


                step++;
                int col = 0;
                ArrayList<KpiCheckBox> checkList = new ArrayList<>();
                LinkedList<ColumnRpc> columns = tableName.getColumns();
                for (ColumnRpc column : columns) {
                    if (col == 3) {
                        col = 0;
                        step++;
                    }

                    final KpiCheckBox checkBox = new KpiCheckBox(column.getTitle(), true);
                    final Label tempLabel = new Label(column.getTitle());
                    checkBox.setName(tableName.getTableName());
                    if (report != null) {
                        if (report.getSelectedColumns().contains(column.getTitle())) {
                            checkBox.setEnabled(true);
                        }
                    } else {
                        checkBox.setEnabled(true);
                    }
                    checkBox.addClickHandler(clickEvent -> {
                        if (command != null) {
                            command.execute();
                        }
                        if (checkBox.getValue()) {
                            if (!selectedColumns.contains(tempLabel)) {
                                selectedColumns.add(checkBox.getText());
                            }
                        } else {
                            selectedColumns.remove(checkBox.getText());
                        }
                        refreshDataList(selectedColumns, true);
                    });
                    if (isSelectAllCheckBox) {
                        if (report != null) {
                            if (report.getSelectedColumns().contains(column)) {
                                checkBox.setEnabled(true);
                                checkBox.setValue(true);
                            }
                        } else {
                            checkBox.setEnabled(true);
                            checkBox.setValue(true);
                        }
                        if (!selectedColumns.contains(checkBox.getText()) && checkBox.isEnabled() && checkBox.getValue()) {
                            selectedColumns.add(checkBox.getText());
                        }

                    } else {
                        if (report != null) {
                            if (report.getSelectedColumns().contains(column)) {
                                checkBox.setValue(report.getSelectedColumns().get(report.getSelectedColumns().indexOf(column)).isChecked());
                            }
                        }
                    }
                    table.setWidget(step, col++, checkBox);

                    checkList.add(checkBox);
                    mapColumn.put(checkBox, column);
                }
                step++;
                mapCheckBox.put(tableName.getTableName(), checkList);
            }
            if (report != null) {
                setSelectedGrouping(report.getGroupColumns());
            }
        }
        refreshDataList(selectedColumns, false);
    }

    private void init() {
        table = null;
        table = new FlexTable();
        SelectOrderPanel();
        table.setStyleName("colums");
//        table.setWidth("77%");
        table.setHeight("100%");
        formatter = table.getFlexCellFormatter();
        allColumns = new ArrayList<>();
        selectedColumns = new ArrayList<>();

        top = new Label(wfmStrings.top());
        up = new Label("Up");
        down = new Label("down");
        bottom = new Label("bottom");

        selectAll = new KpiCheckBox("<b>" + wfmStrings.selectAll() + "</b>", true);
        selectAll.addClickHandler(clickEvent -> {
            if (command != null) {
                command.execute();
            }
            for (int i = 0; i < table.getRowCount(); i++) {
                if (table.getCellCount(i) == 3 && table.getWidget(i, 1) != null && table.getWidget(i, 1) instanceof KpiCheckBox) {
                    ((KpiCheckBox) table.getWidget(i, 1)).setValue(true);
                    ((KpiCheckBox) table.getWidget(i, 2)).setValue(false);
                }
            }
            for (String key : mapCheckBox.keySet()) {
                ArrayList<KpiCheckBox> checks = mapCheckBox.get(key);
                if (checks != null && checks.size() != 0) {
                    for (KpiCheckBox check : checks) {
                        check.setValue(true);
                        if (!selectedColumns.contains(check.getText())) {
                            selectedColumns.add(check.getText());
                        }
                    }
                }
            }
            refreshDataList(selectedColumns, true);
        }

        );

        unSelect = new KpiCheckBox("<b>" + wfmStrings.deselectAll() + "</b>", true);
        unSelect.getElement().getStyle().setColor("#ffffff");
        unSelect.addClickHandler(clickEvent -> {
            if (command != null) {
                command.execute();
            }
            for (int i = 0; i < table.getRowCount(); i++) {
                if (table.getCellCount(i) == 3 && table.getWidget(i, 2) != null && table.getWidget(i, 2) instanceof KpiCheckBox) {
                    ((KpiCheckBox) table.getWidget(i, 1)).setValue(false);
                    ((KpiCheckBox) table.getWidget(i, 2)).setValue(true);
                }
            }
            for (String key : mapCheckBox.keySet()) {
                ArrayList<KpiCheckBox> checks = mapCheckBox.get(key);
                if (checks != null && checks.size() != 0) {
                    for (KpiCheckBox check : checks) {
                        if (check.isEnabled()) {
                            check.setValue(false);
                            selectedColumns.remove(check.getText());
                        } else {
//                                check.setValue(true);
                            if (selectedColumns.contains(check.getText())) {
                                selectedColumns.remove(check.getText());
                            }
                        }
                    }
                }
            }
            refreshDataList(selectedColumns, true);
        }

        );

        table.setHTML(0, 0, "<b style='color: #ffffff;text-transform: uppercase;font-weight: bold;'>" + wfmStrings.columns() + "</b>");
        //formatter.setColSpan(0, 0, 3);
        formatter.setStyleName(0, 0, "bheader");
        table.setWidget(0, 1, selectAll);
        formatter.setStyleName(0, 1, "bheader");
        table.setWidget(0, 2, unSelect);
        formatter.setStyleName(0, 2, "bheader");
        table.getRowFormatter().setStyleName(0, "theader");

        table.setHTML(1, 0, "&nbsp;");
        formatter.setColSpan(1, 0, 5);
        add(orderPanel);
        HTMLPanel leftBarInner = new HTMLPanel("");
        leftBarInner.setStyleName("overhide leftBarInner");
        leftBarInner.add(table);
        add(leftBarInner, id);
//        add(table, id);
    }

    public void removeAllColumns() {
        mapCheckBox.clear();
        mapColumn.clear();
        table.removeAllRows();
        remove(orderPanel);
    }

    public void setSelectedGrouping(LinkedList<ColumnRpc> columns) {
        if (columns != null && columns.size() != 0) {
            LinkedList<ColumnRpc> temp = new LinkedList<>();
            selectedGroupColumns = columns;
            temp.addAll(columns);
            for (KpiCheckBox check : mapColumn.keySet()) {
                check.setEnabled(true);
                for (ColumnRpc column : columns) {
                    if (column.getName().equals(mapColumn.get(check).getName())) {
                        check.setValue(true);
                        check.setEnabled(false);
                    }
                }
            }
            if (report != null) {
                report.setGroupColumns(temp);
            }

        } else {
            selectedGroupColumns = null;
        }
        refreshDataList(selectedColumns, false);
    }

    public ArrayList<ColumnRpc> getSelectedGrouping() {
        ArrayList<ColumnRpc> temp = new ArrayList<>();
        temp.addAll(selectedGroupColumns);
        return temp;
    }

    public ArrayList<ColumnRpc> getCheckedColumns() {

        ArrayList<ColumnRpc> columns = new ArrayList<>();
        if (selectedGroupColumns != null && selectedGroupColumns.size() > 0) {
            columns.addAll(selectedGroupColumns);
        }
        for (String name : mapCheckBox.keySet()) {
            ArrayList<KpiCheckBox> checksList = mapCheckBox.get(name);
            for (KpiCheckBox box : checksList) {
                if (box.getValue() && columns.indexOf(mapColumn.get(box)) < 0) {
                    columns.add(mapColumn.get(box));
                }
            }
        }


        return columns;
    }

    public LinkedList<ColumnRpc> getAllColumns() {
        LinkedList<ColumnRpc> columns = new LinkedList<>();
        for (String name : mapCheckBox.keySet()) {
            ArrayList<KpiCheckBox> checksList = mapCheckBox.get(name);
            for (KpiCheckBox box : checksList) {
                columns.add(mapColumn.get(box));
            }
        }
        return columns;
    }

    public void setColumnsMap(ReportRpc report) {
        for (String name : mapCheckBox.keySet()) {
            ArrayList<KpiCheckBox> checksList = mapCheckBox.get(name);
            for (KpiCheckBox box : checksList) {
                report.getColumnMap().put(mapColumn.get(box).getName(), mapColumn.get(box));
            }
        }
    }

    public LinkedList<ColumnRpc> getOrderColumns() {
        LinkedList<ColumnRpc> columnsOrder = new LinkedList<>();
        if (selectedGroupColumns != null) {
            if (selectedGroupColumns.size() != 0) {
                columnsOrder.addAll(selectedGroupColumns);
                for (int i = 0; i < showVerticalPanel.getWidgetCount(); i++) {
                    for (ColumnRpc temp : allColumns) {
                        Label tempLabel = (Label) showVerticalPanel.getWidget(i);
                        if (temp.getTitle().equals(tempLabel.getText()) && !columnsOrder.contains(temp)) {
                            columnsOrder.add(temp);
                        }
                    }
                }

            }
        } else {
            if (showVerticalPanel.getWidgetCount() != 0) {
                for (int i = 0; i < showVerticalPanel.getWidgetCount(); i++) {
                    for (ColumnRpc temp : allColumns) {
                        Label tempLabel = (Label) showVerticalPanel.getWidget(i);
                        if (temp.getTitle().equals(tempLabel.getText()) && !columnsOrder.contains(temp)) {
                            columnsOrder.add(temp);
                        }
                    }
                }
            }

        }
        return columnsOrder;
    }

    private void SelectOrderPanel() {
        showVerticalPanel = new VerticalPanel();
        showUnDraggableVerticalPanel = new VerticalPanel();
        boundaryPanel = new AbsolutePanel();
        showColumnDragController = new PickupDragController(boundaryPanel, false);
        orderPanel = new FlowPanel();
        showColumns = new WfmContentPanel();
//        showColumns.addStyleName("draggable-columns");
        orderPanel.setStyleName("order-panel");
//        orderPanel.setHeight("241px");
//        showColumns.setSize("158px", "239px");
        showColumns.setWidth("200px");

        showColumns.setCaptionLeftHTML("<div class='col-order'>" + wfmStrings.columnOrder() + "</div>");
        boundaryPanel.getElement().getStyle().setOverflow(Style.Overflow.VISIBLE);
        boundaryPanel.setStyleName("boundary-panel");
        boundaryPanel.setSize("100%", "87%");
        showVerticalPanel.setWidth("100%");
        showVerticalPanel.setSpacing(0);
        showUnDraggableVerticalPanel.setWidth("100%");
        showUnDraggableVerticalPanel.setSpacing(0);
        boundaryPanel.add(showUnDraggableVerticalPanel);
        boundaryPanel.add(showVerticalPanel);
        showColumns.add(boundaryPanel);
        orderPanel.add(showColumns);
        showColumns.getElement().getStyle().clearBorderStyle();
        showColumnDragController.setBehaviorMultipleSelection(false);

        // initialize our column drop controller
        VerticalPanelDropController columnDropController = new VerticalPanelDropController(showVerticalPanel);

        showColumnDragController.registerDropController(columnDropController);
        showColumnDragController.addDragHandler(new DragHandlerAdapter() {
            @Override
            public void onDragEnd(DragEndEvent event) {
                selectedColumns.clear();
                for (int i = 0; i < showVerticalPanel.getWidgetCount(); i++) {
                    Label lbl = (Label) showVerticalPanel.getWidget(i);
                    selectedColumns.add(lbl.getText());
                }
            }

            @Override
            public void onDragStart(DragStartEvent event) {

            }
        });
    }

    private void refreshDataList(ArrayList<String> list, boolean clicked) {
        showVerticalPanel.clear();
        showUnDraggableVerticalPanel.clear();
        if (report != null && !clicked) {
            if (report.getSelectedColumns() != null && report.getSelectedColumns().size() != 0) {
                if (selectedGroupColumns != null && selectedGroupColumns.size() != 0) {
                    for (ColumnRpc selectedColumns : selectedGroupColumns) {
                        Label l = new Label(selectedColumns.getTitle());
                        l.setStyleName("undraggable-label dotted-label");
                        showUnDraggableVerticalPanel.add(l);
                    }
                }
                for (ColumnRpc selectedColumns : report.getSelectedColumns()) {
                    if (!report.getGroupColumns().contains(selectedColumns)) {
                        Label l = new Label(selectedColumns.getTitle());
                        showVerticalPanel.add(l);
                        l.setStyleName("dotted-label");
                        showColumnDragController.makeDraggable(l);
                    }
                }
            }
        } else {
            Label label;
            Integer i = 0;
            for (String string : list) {
                label = new Label();
                label.setText(string);
                label.setStyleName("dotted-label");
                if (selectedGroupColumns != null) {
                    ArrayList<String> tempList = new ArrayList<>();
                    for (ColumnRpc temp : selectedGroupColumns) {
                        tempList.add(temp.getTitle());
                    }
                    if (!tempList.contains(label.getText())) {
                        showVerticalPanel.add(label);
                        showColumnDragController.makeDraggable(label);
                    }
                } else {
                    showVerticalPanel.add(label);
                    showColumnDragController.makeDraggable(label);
                }
            }
            if (selectedGroupColumns != null) {
                if (selectedGroupColumns.size() != 0) {
                    for (ColumnRpc column : selectedGroupColumns) {
                        Label unDraggable = new Label(column.getTitle());
                        unDraggable.setStyleName("undraggable-label dotted-label");
                        showUnDraggableVerticalPanel.add(unDraggable);
                    }
                }
            }
        }
    }

}
