/*
package com.finnetlimited.reportservice.core.client.ui.table;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.DurationType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.OperationType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.SqlColumnType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.stage.widget.unit.DRSDateBox;
import com.finnetlimited.reportservice.core.client.ui.lookup.FilterLookUp;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

*/
/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: Apr 1, 2011
 * Time: 6:56:52 PM
 * To change this template use File | Settings | File Templates.
 *//*

public class UserDefinedFilterTableAddRow extends HorizontalPanel {
    protected static final WfmStrings strings = WfmStrings.App.get();
    private int row = 0;
    private ArrayList<ColumnRpc> columns;
    private ArrayList<SelectListRpc> settList;
    private ArrayList<SelectListRpc> selectList;
    private HashMap<Integer, ColumnRpc> map = new HashMap<Integer, ColumnRpc>();
    private ReportRpc report;
    private Boolean showButton = false;
    private final Button filterButton = new Button("Apply");

    public UserDefinedFilterTableAddRow() {
        // showButton = false;
    }

    public UserDefinedFilterTableAddRow(Boolean showButton) {
        this.showButton = showButton;
    }

    public void draw(ReportRpc report) {
        this.report = report;
        columns = report.getSelectedColumns();
        init();
        clear();
        setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        setVerticalAlignment(HasAlignment.ALIGN_TOP);
        setSpacing(20);
        // setSize("100%", "30px");

        boolean confirmShowButton = false;
        if (report.getPromtList() != null) {
            for (int i = 0; i < report.getPromtList().size(); i++) {
                if (confirmShowButton == false) {
                    confirmShowButton = true;
                }
                if (report.getPromtList().get(i).equals(1)) {
                    AddRow row = new AddRow(report.getFieldd().get(i), report.getValues().get(i), report.getOperators().get(i));
                    add(row);
                }
            }
        }
        if (showButton && confirmShowButton) {
            */
/*   filterButton = new Button();
            filterButton.setText("Apply");*//*

            add(filterButton);
        }
    }

    private void init() {
        selectList = new ArrayList<SelectListRpc>();
        for (int i = 0; i < columns.size(); i++) {
            map.put(i, columns.get(i));
            SelectListRpc select = new SelectListRpc();
            select.setId(i);
            select.setName(columns.get(i).getTitle());
            select.setDescription(columns.get(i).getName());
            selectList.add(select);
        }

        settList = new ArrayList<SelectListRpc>();
        for (int i = 1; i <= 10; i++) {
            SelectListRpc selectList = new SelectListRpc();
            selectList.setId(i);
            selectList.setName(i + "");
            settList.add(selectList);
        }
    }

    public ReportRpc getReportingRpc(ReportRpc report) {
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
                            report.getOperators().set(i, row.getOperatorValue());
                        }
                        j++;
                    }
                }
            }
        }


        return report;
    }

    public void clearWidgets() {
        int widgetcounts = getWidgetCount();
        for (int i = widgetcounts - 1; i > -1; i--) {
            getWidget(i).removeFromParent();
        }
    }


    public class AddRow extends VerticalPanel {

        private FlowPanel pnlTitle;
        private Label label;
        private DRSDateBox dateBox;
        private ListBox operation;
        private FilterLookUp lookUp;
        private DRSDateBox to;
        private FlexTable checkBoxList;
        private FlowPanel checkBoxListPanel;
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
            //setSize("100%", "30px");

            */
/* Fields *//*

            label = new Label();
            label.setText(column.getTitle() + " ");
            label.addStyleName("bheader");
            label.addStyleName("fheader");
            label.getElement().getStyle().setPaddingLeft(10, Style.Unit.PX);
            label.getElement().getStyle().setPaddingTop(5, Style.Unit.PX);
            label.getElement().getStyle().setPaddingTop(3, Style.Unit.PX);

            pnlTitle = new FlowPanel();
            pnlTitle.getElement().setAttribute("style", "");
            pnlTitle.add(label);
            pnlTitle.setStyleName("theader");

            if (column != null) {
                add(pnlTitle*/
/*label*//*
);

                if (column.getFilterWidgetType() != null && !"".equals(column.getFilterWidgetType())) {
                    if (column.getFilterWidgetType().equals("checkbox")) {
                        add(getCheckAll());
                        checkBoxListPanel = new FlowPanel();
                        checkBoxListPanel.getElement().setAttribute("style", "width:400px; height:100px; overflow:scroll");
                        checkBoxList = new FlexTable();
                        checkBoxList.setStyleName("checkboxlist-filter-list");
                        checkBoxListPanel.add(checkBoxList);
                        add(checkBoxListPanel);
                        ArrayList<String> values = new ArrayList<String>(Arrays.asList(value.split("<->")));
                        fillCheckBoxListByColumn(column, values);
                    } else {
                        listBox = new ListBox();
                        listBox.setWidth("140px");
                        add(listBox);
                        fillDropDownByColumn(column, value);
                    }
                } else {
                    operation = new ListBox();
                    operation.setWidth("162px");
                    operation.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
                    operation.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
                    add(operation);

                    if (column.getType().equals(SqlColumnType.DATE.getName())) {
                        fillDurationOperators();
                        DurationType duration = DurationType.valueOf(operatorValue);
                        for (int k = 0; k < operation.getItemCount(); k++) {
                            if (duration.getName().equals(operation.getItemText(k))) {
                                operation.setSelectedIndex(k);
                            }
                        }

                        operation.addValueChangeHandler(new ChangeHandler() {

                            @Override
                            public void onChange(ChangeEvent event) {
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
                            }
                        });

                        final FlowPanel ctrlContainer = new FlowPanel();
                        ctrlContainer.getElement().setAttribute("style", "padding-top:5px;padding-left:10px;");

                        final FlowPanel dateBoxDiv = new FlowPanel();
                        dateBoxDiv.getElement().setAttribute("style", "float:left;margin-right:10px;");
                        dateBox = new DRSDateBox();
                        dateBoxDiv.add(dateBox);

                        final FlowPanel toDateDiv = new FlowPanel();
                        toDateDiv.getElement().getStyle().setPaddingLeft(10, Style.Unit.PX);
                        to = new DRSDateBox();
                        toDateDiv.add(to);

                        ctrlContainer.add(dateBoxDiv);
                        ctrlContainer.add(toDateDiv);

                        add(ctrlContainer);
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
                            ctrlContainer.getElement().getStyle().setWidth(250, Style.Unit.PX);
                        } else {
                            dateBox.setVisible(false);
                            to.setVisible(false);
                        }
                    } else {
                        fillOperationOperators();
                        OperationType duration = OperationType.valueOf(operatorValue);
                        for (int k = 0; k < operation.getItemCount(); k++) {
                            if (duration.getName().equals(operation.getItemText(k))) {
                                operation.setSelectedIndex(k);
                            }
                        }

                        final FlowPanel flowPanel = new FlowPanel();
                        flowPanel.getElement().setAttribute("style", "padding-left:10px;");
                        lookUp = new FilterLookUp(report);
                        flowPanel.add(lookUp);
                        add(flowPanel);
                        lookUp.getElement().setAttribute("style", "margin-top:5px;");
                        lookUp.setValue(value);
                        lookUp.setColumn(column);
                        lookUp.getTextBox().setText(value);
                        lookUp.getSuggestBox().setText(value);
                    }
                }
            }
        }

        private CheckBox getCheckAll() {
            final CheckBox checkAll = new CheckBox("<b>All</b>", true);
            checkAll.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    if (checkBoxList != null) {
                        for (int row = 0; row < checkBoxList.getRowCount(); row++) {
                            for (int cell = 0; cell < checkBoxList.getCellCount(row); cell++) {
                                ((CheckBox) checkBoxList.getWidget(row, cell)).setValue(checkAll.getValue());
                            }
                        }
                    }
                }
            });

            return checkAll;
        }

        public String getValue() {
            if (column != null) {
                if (column.getFilterWidgetType() != null && !"".equals(column.getFilterWidgetType())) {
                    if (column.getFilterWidgetType().equals("checkbox")) {
                        StringBuilder tempBuffer = new StringBuilder();
                        for (int i = 0; i < checkBoxList.getRowCount(); i++) {
                            for (int j = 0; j < checkBoxList.getCellCount(i); j++) {
                                CheckBox checkBox = (CheckBox) checkBoxList.getWidget(i, j);
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
                    } else if (operation.getValue(operation.getSelectedIndex()).equals(DurationType.Before.name())) {
                        return dateBox.getText();
                    } else if (!operation.getValue(operation.getSelectedIndex()).equals(DurationType.Equals.name()) && !operation.getValue(operation.getSelectedIndex()).equals(DurationType.After.name())) {
                        return DurationType.valueOf(operation.getValue(operation.getSelectedIndex())).getStartDate() + "_" + DurationType.valueOf(operation.getValue(operation.getSelectedIndex())).getEndDate();
                    } else {
                        return dateBox.getText();
                    }
                } else if (!strings.searchTypeMessage().equals(lookUp.getText().toLowerCase())) {
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

        public void fillDurationOperators() {
            operation.clear();
            for (DurationType type : DurationType.values()) {
                operation.addItem(type.getName(), type.name());
            }
        }


        public void fillOperationOperators() {
            operation.clear();
            for (OperationType type : OperationType.getOperations(column.getType())) {
                operation.addItem(type.getName(), type.name());
            }
        }

        public void fillOperationIsEquals() {
            operation.clear();
            operation.addItem(OperationType.IsEqualTo.getName(), OperationType.IsEqualTo.name());
        }

        public void fillCheckBoxListByColumn(ColumnRpc column, final ArrayList<String> values) {
            CoreService.App.get().getFilterSelectItems("", report, column, new AsyncCallback<ArrayList<SelectItem>>() {

                @Override
                public void onFailure(Throwable throwable) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void onSuccess(ArrayList<SelectItem> selectItems) {
                    Integer row = 0;
                    Integer columnIndex = 0;
                    for (SelectItem selectItem : selectItems) {
                        final CheckBox checkbox = new CheckBox();
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
            CoreService.App.get().getFilterSelectItems("", report, column, new AsyncCallback<ArrayList<SelectItem>>() {

                @Override
                public void onFailure(Throwable throwable) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void onSuccess(ArrayList<SelectItem> selectItems) {
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

    public Button getFilterButton() {
        return filterButton;
    }
}
*/
