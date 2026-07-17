package com.finnetlimited.reportservice.core.client.ui.table;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.DateRangeType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.SortType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.SqlColumnType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;
import com.finnetlimited.reportservice.core.client.ui.listbox.DRSListBox;
import com.finnetlimited.reportservice.core.client.ui.panel.DRSVerticalPanel;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * User: ${Dilsh0d}
 * Date: 03-Apr-2010
 * Time: 20:17:41
 */
public class GroupingTable extends FlexTable {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private LinkedList<ColumnRpc> list;
    private ArrayList<SelectListRpc> columnList;
    private ArrayList<SelectListRpc> sortOrderList;
    private ArrayList<SelectListRpc> dateRangeList;
    private HashMap<Integer, ColumnRpc> map = new HashMap<>();
    private Command command;

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public GroupingTable() {
        setStyleName("order-table");
        getElement().getStyle().setWidth(60, Style.Unit.PCT);
    }

    private void init() {
        columnList = new ArrayList<>();
        int k = 0;
        sortOrderList = new ArrayList<>();
        for (SortType type : SortType.values()) {
            SelectListRpc select = new SelectListRpc();
            select.setId(k++);
            select.setName(type.name());
            sortOrderList.add(select);
        }
        k = 0;
        dateRangeList = new ArrayList<>();
        for (DateRangeType rangeType : DateRangeType.values()) {
            SelectListRpc select = new SelectListRpc();
            select.setId(k++);
            select.setName(rangeType.name());
            dateRangeList.add(select);
        }
    }

    public void drawSaveReportGroupingTable(ReportRpc report) {
        drawGroupingTable(new LinkedList<>(report.getColumnMap().values()));
        for (int i = 1; i <= report.getGroupColumns().size(); i++) {
            if (i <= 3) {
                GroupingRow row = (GroupingRow) getWidget(i, 0);
                row.getColumnBox().setSelectedName(report.getGroupColumns().get(i - 1).getTitle());
                row.getColumnBox().setEnabled(true);
                if (SortType.Ascending.name().equals(report.getSortTypes().get(i - 1))) {
                    row.getSortTableBox().setSelectedIndex(0);
                } else {
                    row.getSortTableBox().setSelectedIndex(1);
                }
                row.getSortTableBox().setEnabled(true);

                if (!"".equals(report.getRangeType().get(i - 1).trim())) {
                    row.getPanel3().setVisible(true);
                    for (int k = 0; k < row.getDateRangeBox().getItemCount(); k++) {
                        if (row.getDateRangeBox().getItemText(k).equals(report.getRangeType().get(i - 1).trim())) {
                            row.getDateRangeBox().setSelectedIndex(k);
                            row.getDateRangeBox().setEnabled(true);
                        }
                    }
                }
            }
        }
    }

    /* draw table */
    public void drawGroupingTable(LinkedList<ColumnRpc> list) {
        init();
        this.list = list;
        for (int s = 0; s < list.size(); s++) {
            map.put(s, list.get(s));
            SelectListRpc select = new SelectListRpc();
            select.setId(s);
            select.setName(list.get(s).getTitle());
            columnList.add(select);
        }

        int row = 0;
        addSectionTitleBlue(row++, wfmStrings.standardGrouping());
        addRow(wfmStrings.summarizeInformationBy(), row++);
        addRow(wfmStrings.andThenBy(), row++);
        addRow(wfmStrings.andFinallyBy(), row);
    }

    /* add Row */
    private void addRow(String title, int row) {
        GroupingRow groupingRow = new GroupingRow(title, row);
        groupingRow.setStyleName("kpi-label-row");
        setWidget(row, 0, groupingRow);
        if (row > 0) {
            getWidget(row, 0).getElement().getStyle().setMarginLeft(20 * row, Style.Unit.PX);
        }
    }

    /* Table Section title */
    private void addSectionTitleBlue(int row, String title) {
        setHTML(row, 0, "<b style='padding:0 0 0 10px'>" + title + "</b>");
        /*getFlexCellFormatter().setColSpan(row, 0, 4);*/
        getFlexCellFormatter().setStyleName(row, 0, "bheader");
        getRowFormatter().setStyleName(row, "theader");
    }

    /* return ReportRpc Serializable object */
    public ReportRpc getReport(ReportRpc report) {
        LinkedList<ColumnRpc> columns = new LinkedList<>();
        ArrayList<String> sortType = new ArrayList<>();
        ArrayList<String> rangeType = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            GroupingRow row = (GroupingRow) getWidget(i, 0);
            if (row.getSelectColumnRpc() != null) {
                columns.add(row.getSelectColumnRpc());
                sortType.add(row.getSelectSortType());
                rangeType.add(row.getRangeType());
                if (report.getCustomFilter() != null) {
                    report.getCustomFilter().put("#!date!#", row.getRangeType());
                }
            }
        }
        LinkedList<ColumnRpc> afterRemovedColumns = new LinkedList<>();
        for (ColumnRpc column : columns) {
            for (int j = 0; j < report.getSelectedColumns().size(); j++) {
                if (!column.getName().equals(report.getSelectedColumns().get(j).getName())) {
                    afterRemovedColumns.add(report.getSelectedColumns().get(j));
//                    report.getSelectedColumns().remove(j); //todo: serious performance issue
                }
            }
        }
        report.getSelectedColumns().clear();
        report.getSelectedColumns().addAll(afterRemovedColumns);
        if (!columns.isEmpty()) {
            report.getSelectedColumns().addAll(0, columns);
        }

        report.setGroupColumns(columns);
        report.setSortTypes(sortType);
        report.setRangeType(rangeType);
//        report.setRunFromFirstStep(false);
        return report;
    }

    public void setStepDownRows(int row, boolean enabled) {
        int end = enabled ? row + 1 : 3;
        for (int i = row + 1; i <= end; i++) {
            GroupingRow rowWidgets = (GroupingRow) getWidget(i, 0);
            if (!enabled) {
                rowWidgets.getColumnBox().setSelectedIndex(0);
            }
            rowWidgets.setEnabledRow(enabled);
        }
    }

    /**
     * Grouping Table Row  began
     */
    protected class GroupingRow extends HorizontalPanel {
        private int row;
        private String title;
        private DRSListBox columnBox;
        private DRSListBox sortTableBox;
        private DRSListBox dateRangeBox;
        private DRSVerticalPanel panel3;

        public GroupingRow(String title, int row) {
            this.row = row;
            this.title = title;
//            for (int i = 0; i < row; i++) {
//                add(new HTML("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));
//            }
            init();
        }

        private void init() {
            columnBox = new DRSListBox();
            columnBox.setWidth("200px");
            columnBox.setItems(columnList);
            columnBox.addChangeHandler(changeEvent -> {
                if (command != null) {
                    command.execute();
                }
                changeColumnBox();
                if (columnBox.getSelectId() == null && row < 3) {
                    setStepDownRows(row, false);
                } else if (row < 3) {
                    setStepDownRows(row, true);
                }
            });

            sortTableBox = new DRSListBox();
            sortTableBox.setItemsNoNone(sortOrderList);
            sortTableBox.setSelectedIndex(0);

            dateRangeBox = new DRSListBox();
            dateRangeBox.setItemsNoNone(dateRangeList);
            dateRangeBox.setSelectedIndex(0);

            DRSVerticalPanel panel1 = new DRSVerticalPanel(title, columnBox);
            DRSVerticalPanel panel2 = new DRSVerticalPanel(wfmStrings.sortOrder(), sortTableBox);
            panel2.addStyleName("sort-tbl");
            panel3 = new DRSVerticalPanel(wfmStrings.dateRange(), dateRangeBox);
            panel3.setVisible(false);

            add(panel1);
            add(panel2);
            add(panel3);

            if (row > 1) {
                setEnabledRow(false);
            }
        }

        public void setEnabledRow(boolean p) {
            columnBox.setEnabled(p);
            sortTableBox.setEnabled(p);
            dateRangeBox.setEnabled(p);
        }

        public DRSListBox getColumnBox() {
            return columnBox;
        }

        private void changeColumnBox() {
            ColumnRpc column = map.get(columnBox.getSelectId());
            if (columnBox.getSelectId() != null && SqlColumnType.DATE.getName().equals(column.getType())) {
                panel3.setVisible(true);
            } else {
                panel3.setVisible(false);
            }
        }

        public ColumnRpc getSelectColumnRpc() {
            if (columnBox.getSelectId() != null) {
                return map.get(columnBox.getSelectId());
            }
            return null;
        }

        public String getSelectSortType() {
            return sortTableBox.getSelecedName();
        }

        public String getRangeType() {
            if (panel3.isVisible() && dateRangeBox.getSelectId() != null) {
                return dateRangeBox.getSelecedName();
            }
            return "";
        }

        public DRSListBox getSortTableBox() {
            return sortTableBox;
        }

        public DRSListBox getDateRangeBox() {
            return dateRangeBox;
        }

        public DRSVerticalPanel getPanel3() {
            return panel3;
        }
    }
}
