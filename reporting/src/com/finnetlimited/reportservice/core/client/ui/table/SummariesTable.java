package com.finnetlimited.reportservice.core.client.ui.table;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.finnetlimited.reportservice.core.client.enumtype.IdType;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


/**
 * User: ${Dilsh0d}
 * Date: 16-Mar-2010
 * Time: 20:57:42
 */
public class SummariesTable extends HTMLPanel {

    private static final String _id = IdType.SUMMARIES_TABLE.getName();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private static int num = 0;

    private String id;
    private FlexTable table;
    private ArrayList<ColumnRpc> columns;
    private FlexTable.FlexCellFormatter formatter;
    private HashMap<String, ArrayList<KpiCheckBox>> mapBoxex = new HashMap<>();
    private HashMap<String, ColumnRpc> mapColumn = new HashMap<>();


    private KpiCheckBox selectAll;
    private KpiCheckBox unSelectAll;

    private Command command;

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public SummariesTable() {
        super("");
        setStyleName("sdbr-1");
        DOM.setElementAttribute(getElement(), "id", (_id + num));
        id = _id + num;
        num++;
    }

    public void addTadbleData(ArrayList<ColumnRpc> columnNames) {
        init();
        columns = columnNames;
        if (columns != null && columns.size() > 0) {
            int step = 4;
            for (int i = 0; i < columnNames.size(); i++) {
                if (i % 2 == 0) {
                    table.getRowFormatter().setStyleName(step, "odd");
                }

                ColumnRpc column = columns.get(i);
                table.setHTML(step, 0, column.getTitle());

                ArrayList<KpiCheckBox> checkList = new ArrayList<>();

                KpiCheckBox sum = new KpiCheckBox();
                sum.setValue(column.isSum());
                sum.setName(column.getName());
                sum.addClickHandler(clickEvent -> {
                    if (command != null) {
                        command.execute();
                    }
                });
                table.setWidget(step, 1, sum);
                table.getCellFormatter().setStyleName(step, 1, "c");

                KpiCheckBox avg = new KpiCheckBox();
                avg.setValue(column.isAvg());
                avg.setName(column.getName());
                table.setWidget(step, 2, avg);
                table.getCellFormatter().setStyleName(step, 2, "c");

                KpiCheckBox largest = new KpiCheckBox();
                largest.setValue(column.isLargest());
                largest.setName(column.getName());
                table.setWidget(step, 3, largest);
                table.getCellFormatter().setStyleName(step, 3, "c");

                KpiCheckBox smallest = new KpiCheckBox();
                smallest.setValue(column.isSmallest());
                smallest.setName(column.getName());
                table.setWidget(step, 4, smallest);
                table.getCellFormatter().setStyleName(step, 4, "c");

                KpiCheckBox count = new KpiCheckBox();
                count.setValue(column.isCount());
                count.setName(column.getName());
                table.setWidget(step, 5, count);
                table.getCellFormatter().setStyleName(step, 5, "c");

                checkList.add(sum);
                checkList.add(avg);
                checkList.add(largest);
                checkList.add(smallest);
                checkList.add(count);
                mapBoxex.put(column.getName(), checkList);
                mapColumn.put(column.getName(), column);

                if ("number".equals(column.getType()) && "percent".equals(column.getColumnFormat())) {
                    sum.setEnabled(false);
                    count.setEnabled(false);
                } else if ("string".equals(column.getType()) || "date".equals(column.getType())) {
                    sum.setEnabled(false);
                    avg.setEnabled(false);
                    largest.setEnabled(false);
                    smallest.setEnabled(false);
                } else if ("number".equals(column.getType())) {
                    count.setEnabled(false);
                }
                if ("date".equals(column.getType())) {
                    count.setEnabled(false);
                }
//                if (i == 0) count.setEnabled(false);
                step++;
            }
        }
    }

    public void removeAllColumn() {
        mapColumn.clear();
        mapBoxex.clear();
        table.removeAllRows();
    }

    private void init() {

        selectAll = new KpiCheckBox("<b>" + wfmStrings.selectAll() + "</b>", true);
        selectAll.addClickHandler(clickEvent -> {
            unSelectAll.setValue(false);
            KpiCheckBox anchor = (KpiCheckBox) clickEvent.getSource();
            anchor.setValue(true);
            select(true);
        });

        unSelectAll = new KpiCheckBox("<b>" + wfmStrings.deselectAll() + "</b>", true);
        //unSelectAll.getElement().setAttribute("");
        unSelectAll.addClickHandler(clickEvent -> {
            selectAll.setValue(false);
            KpiCheckBox anchor = (KpiCheckBox) clickEvent.getSource();
            anchor.setValue(true);
            select(false);
        });

        table = new FlexTable();
//        table.getElement().setAttribute("style", "width:75%");

        table.getElement();
        table.setStyleName("colums");
        formatter = table.getFlexCellFormatter();

        table.setHTML(0, 0, "<b>" + wfmStrings.standartSummaryFields() + "</b>");
        formatter.setColSpan(0, 0, 4);
        formatter.setStyleName(0, 0, "bheader");
        formatter.getElement(0, 0).setAttribute("style", "height:auto;");


        HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.add(selectAll);
        hPanel.add(unSelectAll);
        hPanel.getElement().setAttribute("style", "float:right;");
        table.setWidget(0, 1, hPanel);
        formatter.setColSpan(0, 1, 2);

        formatter.setStyleName(0, 1, "bheader");

//        table.setWidget(0, 3, new HTMLPanel(""));
//        formatter.setStyleName(0, 3, "bheader");

        table.getRowFormatter().setStyleName(0, "theader");


        table.getRowFormatter().setStyleName(2, "tsub-header odd");
        table.setHTML(1, 0, "<br/>");
        formatter.setColSpan(1, 0, 6);
        table.setHTML(2, 0, "<b>Columns</b>");

        table.setHTML(2, 1, "<b>"+wfmStrings.sum()+"</b>");
        table.getCellFormatter().setStyleName(2, 1, "c");
        table.setHTML(2, 2, "<b>" + wfmStrings.average() + "</b>");
        table.getCellFormatter().setStyleName(2, 2, "c");
        table.setHTML(2, 3, "<b>" + wfmStrings.largestValue() + "</b>");
        table.getCellFormatter().setStyleName(2, 3, "c");
        table.setHTML(2, 4, "<b>" + wfmStrings.smallestValue() + "</b>");
        table.getCellFormatter().setStyleName(2, 4, "c");
        table.setHTML(2, 5, "<b>" + wfmStrings.count() + "</b>");
        table.getCellFormatter().setStyleName(2, 5, "c");

        KpiCheckBox readCount = new KpiCheckBox();
        readCount.setValue(true);
        readCount.setEnabled(false);
        table.setHTML(3, 0, wfmStrings.recorderCount());
        table.setWidget(3, 1, readCount);
        table.getCellFormatter().setStyleName(3, 1, "c");

        add(table, id);
    }

    public void select(boolean p) {
        for (String key : mapBoxex.keySet()) {
            for (KpiCheckBox box : mapBoxex.get(key)) {
                if (box.isEnabled()) {
                    box.setValue(p);
                }
            }
        }
    }

    public LinkedList<ColumnRpc> getCheckedColumns() {
        LinkedList<ColumnRpc> list = new LinkedList<>();
        for (String name : mapBoxex.keySet()) {
            ArrayList<KpiCheckBox> summaries = mapBoxex.get(name);
            ColumnRpc column = mapColumn.get(name);
            boolean isChecked = false;
            if (summaries.get(0).getValue()) {
                column.setSum(true);
                isChecked = true;
            } else {
                column.setSum(false);
            }
            if (summaries.get(1).getValue()) {
                column.setAvg(true);
                isChecked = true;
            } else {
                column.setAvg(false);
            }
            if (summaries.get(2).getValue()) {
                isChecked = true;
                column.setLargest(true);
            } else {
                column.setLargest(false);
            }
            if (summaries.get(3).getValue()) {
                isChecked = true;
                column.setSmallest(true);
            } else {
                column.setSmallest(false);
            }
            if (summaries.get(4).getValue()) {
                isChecked = true;
                column.setCount(true);
            } else {
                column.setCount(false);
            }
            if (isChecked) {
                list.add(column);
            }
        }
        return list;
    }
}
