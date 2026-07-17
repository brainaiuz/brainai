package com.finnetlimited.reportservice.core.client.ui.table;

import com.edatasite.workforce.gwt.core.client.ArrayUtils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.DurationType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.SqlColumnType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.DRSDateBox;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.ui.lookup.FilterLookUp;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: Jun 23, 2011
 * Time: 4:34:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class DashboardUserDefinedFilterTableAddRow extends HorizontalPanel {

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    private int row = 0;
    private LinkedList<ColumnRpc> columns;
    private ArrayList<SelectListRpc> settList;
    private ArrayList<SelectListRpc> selectList;
    private HashMap<Integer, ColumnRpc> map = new HashMap<>();
    private ReportRpc report;
    private Boolean showButton = false;
    private Button filterButton;

    public DashboardUserDefinedFilterTableAddRow() {
        // showButton = false;
    }

    public DashboardUserDefinedFilterTableAddRow(Boolean showButton) {
        this.showButton = showButton;
    }

    public void draw(ReportRpc report) {
        this.report = report;
        columns = report.getSelectedColumns();
        init();
        clear();
        setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
        setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
        setSpacing(20);
        setSize("60%", "50px");

        Boolean rowAdded = false;

        if (report.getPromtList() != null) {
            for (int i = 0; i < report.getPromtList().size(); i++) {
                if (report.getPromtList().get(i).equals(1)) {
                    AddRow row = new AddRow(report.getFieldd().get(i), report.getValues().get(i), report.getOperators().get(i));
                    add(row);
                    rowAdded = true;
                }
            }
        }
        if (showButton && rowAdded) {
            filterButton = new Button();
            filterButton.setText("Apply");
            add(filterButton);
        }
    }

    private void init() {
        selectList = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            map.put(i, columns.get(i));
            SelectListRpc select = new SelectListRpc();
            select.setId(i);
            select.setName(columns.get(i).getTitle());
            select.setDescription(columns.get(i).getName());
            selectList.add(select);
        }

        settList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            SelectListRpc selectList = new SelectListRpc();
            selectList.setId(i);
            selectList.setName(i + "");
            settList.add(selectList);
        }
    }

    public ReportRpc getReportRpc(ReportRpc report) {
        int j = 0;

        if (report.getPromtList() != null) {
            for (int i = 0; i < report.getPromtList().size(); i++) {
                if (report.getPromtList().get(i).equals(1)) {
                    if (j < getWidgetCount()) {
                        AddRow row = (AddRow) getWidget(j);
                        if (row.getValue() != null && !"".equals(row.getValue().trim())) {
                            report.getValues().set(i, row.getValue());
                        }
                        if (row.getOperatorValue() != null && !"".equals(row.getOperatorValue())) {
                            report.setOperatorAtIndex(i, row.getOperatorValue());
                        }
                        j++;
                    }
                }
            }
        }


        return report;
    }

    public Button getFilterButton() {
        return filterButton;
    }

    public class AddRow extends HorizontalPanel {

        private FlowPanel pnlTitle;
        private Label label;
        private DRSDateBox dateBox;
        private ListBox operation;
        private FilterLookUp lookUp;
        private DRSDateBox to;
        private FlexTable checkBoxList;
        private ListBox listBox;

        private String value;
        private ColumnRpc column;
        private String operatorValue;

        public AddRow(ColumnRpc column, String value, String operatorValue) {
            this.column = column;
            this.value = value;
            this.operatorValue = operatorValue;
            initial();
        }

        private void initial() {
            setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
            setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
            setSpacing(10);
            setSize("100%", "50px");

            /* Fields */
            label = new Label();
            label.setText(column.getTitle() + " ");
            label.addStyleName("bheader");
            label.addStyleName("fheader");

            pnlTitle = new FlowPanel();
            pnlTitle.add(label);
            pnlTitle.setStyleName("theader");

            if (column != null) {
                add(pnlTitle/*label*/);

                if (column.getFilterWidgetType() != null && !"".equals(column.getFilterWidgetType())) {
                    if (column.getFilterWidgetType().equals("checkbox")) {
                        checkBoxList = new FlexTable();
                        add(checkBoxList);
                        fillCheckBoxListByColumn(column, ArrayUtils.asList(value.split("<->")));
                    } else {
                        listBox = new ListBox();
                        add(listBox);
                        fillDropDownByColumn(column, value);
                    }
                } else {
                    if (column.getType().equals(SqlColumnType.DATE.getName())) {
                        operation = new ListBox();
                        operation.setWidth("130px");
                        add(operation);
                        fillOperators();
                        DurationType duration = DurationType.valueOf(operatorValue);
                        for (int k = 0; k < operation.getItemCount(); k++) {
                            if (duration.getName().equals(operation.getItemText(k))) {
                                operation.setSelectedIndex(k);
                            }
                        }

                        operation.addChangeHandler(event -> {
                            if (column != null && SqlColumnType.DATE.getName().equals(column.getType())) {
                                if (operation.getValue(operation.getSelectedIndex()).equals(DurationType.Between.name())) {
                                    dateBox.setVisible(true);
                                    to.setVisible(true);
                                } else if (operation.getValue(operation.getSelectedIndex()).equals(DurationType.After.name()) || operation.getValue(operation.getSelectedIndex()).equals(DurationType.Before.name()) || operation.getValue(operation.getSelectedIndex()).equals(DurationType.Equals.name())) {
                                    dateBox.setVisible(true);
                                    to.setVisible(false);
                                } else {
                                    dateBox.setVisible(false);
                                    to.setVisible(false);
                                }
                            }
                        });

                        dateBox = new DRSDateBox();
                        to = new DRSDateBox();
                        add(dateBox);
                        setCellVerticalAlignment(dateBox, VerticalPanel.ALIGN_BOTTOM);
                        add(to);
                        setCellVerticalAlignment(to, VerticalPanel.ALIGN_BOTTOM);
                        if (duration.equals(DurationType.After) || duration.equals(DurationType.Before) || duration.equals(DurationType.Equals)) {
                            dateBox.setVisible(true);
                            to.setVisible(false);
                            dateBox.setText(value);
                        } else if (duration.equals(DurationType.Between)) {
                            dateBox.setVisible(true);
                            to.setVisible(true);
                            String[] tokens = value.split("_");
                            dateBox.setText(tokens[0]);
                            to.setText(tokens[1]);
                        } else {
                            dateBox.setVisible(false);
                            to.setVisible(false);
                        }
                    } else {
                        lookUp = new FilterLookUp(report);
                        add(lookUp);
                        lookUp.setValue(value);
                        lookUp.setColumn(column);
                        lookUp.getTextBox().setText(value);
                        lookUp.getSuggestBox().setText(value);
                    }
                }
            }
        }

        public String getValue() {
            if (column != null) {
                if (column.getFilterWidgetType() != null && !"".equals(column.getFilterWidgetType())) {
                    if (column.getFilterWidgetType().equals("checkbox")) {
                        StringBuilder tempBuffer = new StringBuilder();
                        for (int i = 0; i < checkBoxList.getRowCount(); i++) {
                            for (int j = 0; j < checkBoxList.getCellCount(i); j++) {
                                KpiCheckBox checkBox = (KpiCheckBox) checkBoxList.getWidget(i, j);
                                if (checkBox.getValue()) {
                                    if (tempBuffer.length() > 0) {
                                        tempBuffer.append("<->");
                                    }
                                    tempBuffer.append(checkBox.getText());
                                }
                            }
                        }
                        return tempBuffer.toString();
                    } else if (column.getFilterWidgetType().equals("dropdown")) {
                        if (listBox.getItemCount() > 0) {
                            return listBox.getValue(listBox.getSelectedIndex());
                        }
                        return "";
                    }
                }
                if (SqlColumnType.DATE.getName().equals(column.getType())) {
                    if (operation.getValue(operation.getSelectedIndex()).equals(DurationType.Between.name())) {
                        return dateBox.getText() + "_" + to.getText();
                    } else if (!operation.getValue(operation.getSelectedIndex()).equals(DurationType.Equals.name()) && !operation.getValue(operation.getSelectedIndex()).equals(DurationType.After.name()) &&
                            !operation.getValue(operation.getSelectedIndex()).equals(DurationType.Before.name())) {
                        return DurationType.valueOf(operation.getValue(operation.getSelectedIndex())).getStartDate() + "_" + DurationType.valueOf(operation.getValue(operation.getSelectedIndex())).getEndDate();
                    } else {
                        return dateBox.getText();
                    }
                } else if (!wfmStrings.searchTypeMessage().equals(lookUp.getText().toLowerCase())) {
                    return lookUp.getText();
                }
            }
            return null;
        }

        public String getOperatorValue() {
            if (column != null && SqlColumnType.DATE.getName().equals(column.getType())) {
                return operation.getValue(operation.getSelectedIndex());
            } else {
                return operatorValue;
            }
        }

        public void fillOperators() {
            operation.clear();
            for (DurationType type : DurationType.values()) {
                operation.addItem(type.getName(), type.name());
            }
        }

        public void fillCheckBoxListByColumn(ColumnRpc column, final ArrayList<String> values) {
            CoreService.App.get().getFilterSelectItems("", report, column, new AsyncCallback<LinkedList<SelectItem>>() {

                @Override
                public void onFailure(Throwable throwable) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void onSuccess(LinkedList<SelectItem> selectItems) {
                    Integer row = 0;
                    Integer columnIndex = 0;
                    for (SelectItem selectItem : selectItems) {
                        final KpiCheckBox checkbox = new KpiCheckBox();
                        checkbox.setText(selectItem.getName());
                        if (values != null && values.size() > 0) {
                            if (values.contains(selectItem.getName())) {
                                checkbox.setValue(true);
                            }
                        }
                        if (columnIndex > 4) {
                            row++;
                            columnIndex = 0;
                        }
                        checkBoxList.setWidget(row, columnIndex, checkbox);
                        columnIndex++;
                    }

                }
            });
        }

        public void fillDropDownByColumn(ColumnRpc column, final String value) {
            CoreService.App.get().getFilterSelectItems("", report, column, new AsyncCallback<LinkedList<SelectItem>>() {

                @Override
                public void onFailure(Throwable throwable) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void onSuccess(LinkedList<SelectItem> selectItems) {
                    for (int i = 0; i < selectItems.size(); i++) {
                        listBox.addItem(selectItems.get(i).getName());
                        if (value != null && value.equals(selectItems.get(i).getName())) {
                            listBox.setSelectedIndex(i);
                        }
                    }
                }
            });
        }
    }
}
