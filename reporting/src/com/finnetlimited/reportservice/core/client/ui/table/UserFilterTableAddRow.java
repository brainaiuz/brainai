package com.finnetlimited.reportservice.core.client.ui.table;

import com.edatasite.workforce.gwt.core.client.ArrayUtils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.DurationType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.OperationType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.SqlColumnType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.RelatedFilterOptions;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.DRSDateBox;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.FilterRow;
import com.finnetlimited.reportservice.core.client.enumtype.BooleanType;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.ui.listbox.DRSListBox;
import com.finnetlimited.reportservice.core.client.ui.lookup.FilterLookUp;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

/**
 * User: ${Dilsh0d}
 * Date: 02-Apr-2010
 * Time: 16:31:57
 */
public class UserFilterTableAddRow extends VerticalPanel {

    private int row = 0;
    private LinkedList<ColumnRpc> columns;
    private ArrayList<SelectListRpc> settList;
    private ArrayList<SelectListRpc> selectList;
    private HashMap<Integer, ColumnRpc> map = new HashMap<>();
    private ReportRpc report;
    private Command operationCommand;

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    public UserFilterTableAddRow(ReportRpc report) {

        this.report = report;
        this.columns = new LinkedList<>(report.getColumnMap().values());
        init();
        addRow();
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

    /* add row method */

    public void addRow() {

        updateFilterControls();
        AddRow row = new AddRow();
        row.setSerialNum(generateNumber());
        add(row);
        if (getWidgetCount() > 1) {
            AddRow downRow = (AddRow) getWidget(getWidgetCount() - 2);
            downRow.setVisibleWidget(true);
        }
    }

    /* add row method */

    public void removeRemove(Widget widget) {

        setReportRpc(getReportRpc(report));
        if (getWidgetCount() - 1 == getWidgetIndex(widget)) {
            AddRow downRow = (AddRow) getWidget(getWidgetCount() - 2);
            downRow.setVisibleWidget(false);
        }
        remove(widget);
        updateFilterControls();
    }

    public void setBooleanListItem(int index) {
        for (int i = 0; i < getWidgetCount(); i++) {
            AddRow row = (AddRow) getWidget(i);
            row.setBooleanListItem(index);
        }
    }

    /* return selected data */

    public ReportRpc getReportRpc(ReportRpc report) {
        ArrayList<Integer> sett = new ArrayList<>();
        LinkedList<ColumnRpc> fieldd = new LinkedList<>();
        ArrayList<String> operators = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> boolType = new ArrayList<>();
        ArrayList<Integer> promptList = new ArrayList<>();

        for (int i = 0; i < getWidgetCount(); i++) {
            AddRow row = (AddRow) getWidget(i);
            String value = row.getValue();
            if (value != null && !"".equals(value.trim()) && !row.equals(wfmStrings.searchTypeMessage())) {
                sett.add(row.getSett());
                fieldd.add(row.getField());
                operators.add(row.getOperator());
                values.add(row.getValue());
                boolType.add(row.getBoolType());
                promptList.add(row.isPrompByInput() ? 1 : 0);
            }
        }
        report.setSett(sett);
        report.setFieldd(fieldd);
        report.setOperators(operators);
        report.setValues(values);
        report.setBoolType(boolType);
        report.setPromtList(promptList);
        return report;
    }

    private void setReportRpc(ReportRpc report) {
        this.report = report;
    }

    public void refreshTableRow(LinkedList<ColumnRpc> columns) {
        HashMap<Integer, ColumnRpc> secondMap = new HashMap<>();
        selectList = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            secondMap.put(i, columns.get(i));
            SelectListRpc select = new SelectListRpc();
            select.setId(i);
            select.setName(columns.get(i).getTitle());
            select.setDescription(columns.get(i).getName());
            selectList.add(select);
        }

        int k = 0;
        for (int i = getWidgetCount() - 1; i >= 0; i--) {
            AddRow row = (AddRow) getWidget(i);
            if (row.getValue() != null && !"".equals(row.getValue().trim())) {
                String selectedName = row.getField().getTitle();
                row.getFieldList().clear();
                row.getFieldList().setItemsNoNone(selectList);
                if (selectedName != null && !row.getFieldList().setSelectedName(selectedName)) {
                    if (i != 0) {
                        remove(row);
                    }
                }
            } else {
                if (i != 0) {
                    remove(row);
                } else {
                    row.getFieldList().clear();
                    row.getFieldList().setItemsNoNone(selectList);
                }
            }
        }
        map = secondMap;
        this.columns = columns;
    }

    public void drawSaveReportCriteriya(ReportRpc report) {

//        for (int i = 1; i < report.getValues().size(); i++) {
//            addRow();
//        }

        if (report.getValues() != null && report.getValues().size() > 0) {
            for (int i = 0; i < report.getFieldd().size(); i++) {
                if (i >= getWidgetCount()) {
                    addRow();
                }
                AddRow row = (AddRow) getWidget(i);
                //   row.getSettList().setSelectedIndex(report.getSett().get(i) - 1);

                row.getFieldList().setSelectedName(report.getFieldd().get(i).getTitle());
                if (report.getPromtList() != null && i < report.getPromtList().size()) {
                    row.getPromtInput().setValue(report.getPromtList().get(i).equals(1));
                }

                if (report.getFieldd().get(i).getFilterWidgetType() != null && !"".equals(report.getFieldd().get(i).getFilterWidgetType())) {
                    drawSavedFilterWidget(report, i, row);
                } else {
                    drawSavedFilter(report, i, row);
                }
            }
        }
    }

    private void drawSavedFilterWidget(ReportRpc report, int index, AddRow row) {
        row.getTo().setVisible(false);
        row.getDateBox().setVisible(false);
        row.getTextBox().setVisible(false);
        if (report.getFieldd().get(index).getFilterWidgetType().equals("checkbox")) {
            row.getLookUp().setVisible(false);
            row.getListBox().setVisible(false);
            row.getCheckBoxListPanel().setVisible(true);
            row.getCheckBoxList().setVisible(true);
            ArrayList<String> selectedValues = ArrayUtils.asList(report.getValues().get(index).split("<->"));
            row.fillCheckBoxListByColumn(report.getFieldd().get(index), selectedValues);
        } else if (report.getFieldd().get(index).getFilterWidgetType().equals("dropdown")) {
            row.getLookUp().setVisible(false);
            row.getListBox().setVisible(true);
            row.getCheckBoxListPanel().setVisible(false);
            row.fillDropDownByColumn(report.getFieldd().get(index), report.getValues().get(index));
        } else if (report.getFieldd().get(index).getFilterWidgetType().equals("lookup")) {
            row.getListBox().setVisible(false);
            row.getCheckBoxListPanel().setVisible(false);
            OperationType type = OperationType.valueOf(report.getOperators().get(index));
            for (int k = 0; k < row.getOperation().getItemCount(); k++) {
                if (type.getName().equals(row.getOperation().getItemText(k))) {
                    row.getOperation().setSelectedIndex(k);
                }
            }
            row.getLookUp().setVisible(true);
            row.getLookUp().setValue(report.getValues().get(index));
            row.getLookUp().setColumn(report.getFieldd().get(index));
            row.getLookUp().getTextBox().setText(report.getValues().get(index));
            row.getLookUp().getSuggestBox().setText(report.getValues().get(index));

//            row.getPromtInput().setValue(report.getPromtList().get(index).equals(1));
//            if (index < report.getBoolType().size() - 1) {
//                BooleanType bool = BooleanType.valueOf(report.getBoolType().get(index));
//                for (int k = 0; k < row.getBooleanType().getItemCount(); k++) {
//                    if (bool.name().equals(row.getBooleanType().getItemText(k))) {
//                        row.getBooleanType().setSelectedIndex(k);
//                    }
//                }
//            }
        }
    }

    private void drawSavedFilter(ReportRpc report, int index, AddRow row) {

        if (report.getSett().size() > 0) {
            for (int i = 0; i < row.getSettListbox().getItemCount(); i++) {
                if (Integer.parseInt(row.getSettListbox().getValue(i)) == report.getSett().get(index)) {
                    row.getSettListbox().setSelectedIndex(i);
                    break;
                }
            }
        }

        row.fillOperators(report.getFieldd().get(index));
        row.getCheckBoxList().setVisible(false);
        row.getListBox().setVisible(false);
        if (report.getBoolType().size() > 0) {
            for (int i = 0; i < row.getBooleanType().getItemCount(); i++) {
                if (row.getBooleanType().getValue(i).equals(report.getBoolTypeAt(index))) {
                    row.getBooleanType().setSelectedIndex(i);
                    break;
                }
            }
        }
        if (SqlColumnType.DATE.getName().equals(report.getFieldd().get(index).getType())) {
            DurationType duration = DurationType.valueOf(report.getOperators().get(index));
            for (int k = 0; k < row.getOperation().getItemCount(); k++) {
                if (duration.getName().equals(row.getOperation().getItemText(k))) {
                    row.getOperation().setSelectedIndex(k);
                }
            }
            if (duration.equals(DurationType.After) || duration.equals(DurationType.Before) || duration.equals(DurationType.Equals)) {
                row.getDateBox().setVisible(true);
                row.getTo().setVisible(false);
                row.getDateBox().setText(FilterRow.setDate(report.getValues().get(index)));
            } else if (duration.equals(DurationType.Between)) {
                String[] tokens = report.getValues().get(index).split("_");
                row.getDateBox().setVisible(true);
                row.getTo().setVisible(true);
                row.getDateBox().setText(FilterRow.setDate(tokens[0]));
                row.getTo().setText(FilterRow.setDate(tokens[1]));
            } else if (duration.equals(DurationType.AgeInDays)) {

            } else {
                row.getDateBox().setVisible(false);
                row.getTo().setVisible(false);
            }
            row.getTextBox().setVisible(false);
        } else {
            row.getTextBox().setVisible(true);
            row.getLookUp().setVisible(false);
            row.getTo().setVisible(false);
            row.getDateBox().setVisible(false);
            OperationType type = OperationType.valueOf(report.getOperators().get(index));
            for (int k = 0; k < row.getOperation().getItemCount(); k++) {
                if (type.getName().equals(row.getOperation().getItemText(k))) {
                    row.getOperation().setSelectedIndex(k);
                }
            }
            row.getTextBox().setValue(report.getValues().get(index));
        }
    }

    private void updateFilterControls() {

//        updateFilterControls(0);
    }

    private void updateFilterControls(Integer serialNum) {
//        report.getRelatedFilters().clear();
//        for (Integer i = 0; i < getWidgetCount(); i++) {
//            AddRow row = (AddRow) getWidget(i);
//            if (serialNum != row.getSerialNum()) {
//                if (row.getValue() != null && !"".equals(row.getValue().trim()) && !row.getValue().equals(wfmStrings.searchTypeMessage())) {
//                    fillOptions(row);
//                }
//            }
//            row.getLookUp().setReport(report);
//        }
    }

    private void updateFilterControls(AddRow current, String controlName) {
//        report.getRelatedFilters().clear();
//        for (Integer i = 0; i < getWidgetCount(); i++) {
//            AddRow row = (AddRow) getWidget(i);
//            if (row.getSett() == current.getSett()) {
//                if (controlName.equals("booleantype")) {
//                    if (row.getValue() != null && !"".equals(row.getValue().trim()) && !row.getValue().equals(wfmStrings.searchTypeMessage())) {
//                        fillOptions(row);
//                    }
//                } else if (controlName.equals("operation")) {
//                    if (current.getSerialNum() != row.getSerialNum()) {
//                        if (row.getValue() != null && !"".equals(row.getValue().trim()) && !row.getValue().equals(wfmStrings.searchTypeMessage())) {
//                            fillOptions(row);
//                        }
//                    }
//                } else if (controlName.equals("sett")) {
//                    if (current.getSerialNum() != row.getSerialNum()) {
//                        if (row.getValue() != null && !"".equals(row.getValue().trim()) && !row.getValue().equals(wfmStrings.searchTypeMessage())) {
//                            fillOptions(row);
//                        }
//                    }
//
//                    if (current.getSerialNum() == row.getSerialNum()) {
//                        row.fillDropDownByColumn(row.getField(), row.getValue());
//                    }
//                }
//            }
//            row.getLookUp().clearOracleItems();
//            row.getLookUp().clearFilter();
//            row.getLookUp().letters.clear();
//            row.getLookUp().setReport(report);
//            row.getLookUp().setSett(current.getSett());
//
//        }
    }

    private Integer generateNumber() {
        return new Random().nextInt();
    }

    private void updateReportFilter(Integer sett) {
        RelatedFilterOptions options = report.getRelatedFilters().get(sett);
        if (options == null) {
            options = new RelatedFilterOptions();
        }

        report.setBoolType(options.getBoolTypes());
        report.setFieldd(options.getFields());
        report.setValues(options.getValues());
        report.setOperators(options.getOperators());
        report.setSett(options.getSets());
    }

    private AddRow findRow(Integer serialNum) {
        for (Integer i = 0; i < getWidgetCount(); i++) {
            AddRow row = (AddRow) getWidget(i);
            if (row.getSerialNum() == serialNum) {
                return row;
            }
        }

        return null;
    }

    private void fillOptions(AddRow row) {
        RelatedFilterOptions options = report.getRelatedFilters().get(row.getSett());
        if (options == null) {
            options = new RelatedFilterOptions();
        }
        options.getBoolTypes().add(row.getBoolType(true));
        options.getFields().add(row.getField());
        options.getOperators().add(row.getOperator());
        options.getSets().add(1);
        options.getValues().add(row.getValue());
        report.getRelatedFilters().put(row.getSett(), options);
    }

    /* Add row class */

    public class AddRow extends HorizontalPanel {

        private Integer serialNum;

        private ListBox sett;
        private DRSListBox field;
        private ListBox operation;
        private DRSDateBox dateBox;
        private DRSDateBox to;
        private ListBox booleanType;
        private Anchor remove;
        private Anchor addrow;
        private FilterLookUp lookUp;
        private TextBox text;
        private ListBox listBox;
        private FlowPanel checkBoxListPanel;
        private FlexTable checkBoxList;
        private KpiCheckBox isPromtInput;

//        private DRSHorizontalPanel operationPanel;

        public AddRow() {

            initial();
            setWidth("100%");
            getElement().getStyle().setMarginTop(7, Style.Unit.PX);
            getElement().getStyle().setMarginBottom(5, Style.Unit.PX);
        }

        private void initial() {
            sett = new ListBox();
            for (Integer i = 1; i <= 10; i++) {
                sett.addItem(i.toString(), i.toString());
            }

            /* Fields */
            field = new DRSListBox();
            field.setWidth("150px");
            field.setItemsNoNone(selectList);
            /* Operation */
            operation = new ListBox();
            operation.setWidth("163px");
            fillOperators();
            //  operationPanel = new DRSHorizontalPanel("Operation", operation);
            /*  *//* Value *//*
           /* Date */
            dateBox = new DRSDateBox();
            to = new DRSDateBox();
            lookUp = new FilterLookUp(report);

            text = new TextBox();
            text.setWidth("160px");

            listBox = new ListBox();
            listBox.setWidth("140px");

            checkBoxListPanel = new FlowPanel();
            checkBoxListPanel.getElement().setAttribute("style", "height:100px;overflow-x:scroll; width:620px;");
            checkBoxList = new FlexTable();
            checkBoxListPanel.add(checkBoxList);
            if (map.size() > 0) {
                if (map.get(0).getFilterWidgetType() != null && !"".equals(map.get(0).getFilterWidgetType())) {
                    text.setVisible(false);
                    to.setVisible(false);
                    dateBox.setVisible(false);
//                    operationPanel.setVisible(false);
                    if (map.get(0).getFilterWidgetType().equals("checkbox")) {
                        listBox.setVisible(false);
                        checkBoxListPanel.setVisible(true);
                        fillCheckBoxListByColumn(map.get(0), null);
                    } else if (map.get(0).getFilterWidgetType().equals("dropdown")) {
                        listBox.setVisible(true);
                        checkBoxListPanel.setVisible(false);
                        lookUp.setVisible(false);
                        fillDropDownByColumn(map.get(0), null);
                    } else if (map.get(0).getFilterWidgetType().equals("lookup")) {
                        lookUp.setVisible(true);
                        listBox.setVisible(false);
                        checkBoxListPanel.setVisible(false);
                        lookUp.setColumn(map.get(0));
                        lookUp.setSett(getSett());
                    }
                } else {
                    //        operationPanel.setVisible(true);
                    lookUp.setVisible(false);
                    listBox.setVisible(false);
                    checkBoxListPanel.setVisible(false);
                    to.setVisible(false);
                    if (SqlColumnType.DATE.getName().equals(map.get(0).getType())) {
                        text.setVisible(false);
                    } else {
                        dateBox.setVisible(false);
                    }

                }
            }


            booleanType = new ListBox();
            booleanType.setWidth("50px");
            for (BooleanType type : BooleanType.values()) {
                booleanType.addItem(type.name(), type.name());
            }
            booleanType.setVisible(false);

            isPromtInput = new KpiCheckBox("Is user defined");
            isPromtInput.setValue(true);
            isPromtInput.setWidth("50px");
            isPromtInput.setVisible(false);

            addrow = new Anchor();
            addrow.setHTML("<img src='/images/add-more.png' alt='add' />");
            addrow.addClickHandler(event -> addRow());

            remove = new Anchor();
            remove.setHTML("<img src='/images/remove-anchor.png' alt='remove' />");
            remove.setName(row + "");
            remove.addClickHandler(clickEvent -> removeRemove(remove.getParent().getParent()));


//            if (row != 0) {
//                remove.setVisible(true);
//            } else {
//                remove.setVisible(false);
//            }
            HorizontalPanel table = new HorizontalPanel();
            table.addStyleName("tbl-filter");
            table.getElement().setAttribute("style", "border-bottom:1px solid #ccc;");

            table.add(sett);
            table.add(field);
            table.add(operation);
            HorizontalPanel div = new HorizontalPanel();
            div.add(text);
            div.add(lookUp);
            div.add(listBox);
            div.add(checkBoxListPanel);
            div.add(dateBox);
            div.add(to);
            table.add(div);
            add(table);
            table.add(addrow);
            table.add(remove);
            table.add(booleanType);


            setCellHorizontalAlignment(remove, ALIGN_LEFT);
            setCellVerticalAlignment(remove, VerticalPanel.ALIGN_BOTTOM);
            row++;

            field.addChangeHandler(changeEvent -> {
                if (map.containsKey(field.getSelectedIndex())) {
                    // agarda filter widget type berilgan bulsa
                    if (map.get(field.getSelectedIndex()).getFilterWidgetType() != null && !"".equals(map.get(field.getSelectedIndex()).getFilterWidgetType())) {

                        to.setVisible(false);
                        text.setVisible(false);
                        dateBox.setVisible(false);
//                            operationPanel.setVisible(false);
                        if (map.get(field.getSelectedIndex()).getFilterWidgetType().equals("checkbox")) {
                            lookUp.setVisible(false);
                            listBox.setVisible(false);
                            checkBoxList.setVisible(true);
                            checkBoxListPanel.setVisible(true);
                            fillCheckBoxListByColumn(map.get(field.getSelectedIndex()), null);
                        } else if (map.get(field.getSelectedIndex()).getFilterWidgetType().equals("dropdown")) {
                            lookUp.setVisible(false);
                            checkBoxListPanel.setVisible(false);
                            listBox.setVisible(true);
                            fillDropDownByColumn(map.get(field.getSelectedIndex()), null);
                        } else {
                            lookUp.setVisible(true);
                            checkBoxListPanel.setVisible(false);
                            listBox.setVisible(false);
                            lookUp.setColumn(map.get(field.getSelectedIndex()));
                            lookUp.clear();
                            lookUp.clearOracleItems();
                            lookUp.letters.clear();
                        }
                    } else {
                        listBox.setVisible(false);
                        checkBoxListPanel.setVisible(false);
                        lookUp.setVisible(false);
//                            operationPanel.setVisible(true);
                        if (SqlColumnType.DATE.getName().equals(map.get(field.getSelectedIndex()).getType())) {
                            text.setVisible(false);
                            lookUp.setVisible(false);
                            dateBox.setVisible(true);
                            if (operation.getValue(operation.getSelectedIndex()).equals(DurationType.Between)) {
                                to.setVisible(true);
                            }
                        } else {
                            dateBox.setVisible(false);
                            to.setVisible(false);
                            text.setVisible(true);
                        }
                        fillOperators();
                    }
                }
                updateFilterControls();
            });

            operation.addChangeHandler(event -> {

                updateFilterControls(findRow(getSerialNum()), "operation");
                operationChangeHandler();
            });

            sett.addChangeHandler(event -> updateFilterControls(findRow(getSerialNum()), "sett"));

            listBox.addChangeHandler(event -> updateFilterControls());

            lookUp.getTextBox().addBlurHandler(event -> updateFilterControls());

            lookUp.getTextBox().addFocusHandler(event -> updateFilterControls(getSerialNum()));

            booleanType.addChangeHandler(event -> updateFilterControls(findRow(getSerialNum()), "booleantype"));

        }

        public void operationChangeHandler() {
            if (map.containsKey(field.getSelectedIndex()) && SqlColumnType.DATE.getName().equals(map.get(field.getSelectedIndex()).getType())) {
                if (operation.getValue(operation.getSelectedIndex()).equals(DurationType.Between.name())) {
                    dateBox.setVisible(true);
                    to.setVisible(true);
                } else if (!operation.getValue(operation.getSelectedIndex()).equals(DurationType.Equals.name()) && !operation.getValue(operation.getSelectedIndex()).equals(DurationType.After.name()) &&
                        !operation.getValue(operation.getSelectedIndex()).equals(DurationType.Before.name())) {
                    dateBox.setVisible(false);
                    to.setVisible(false);
                } else {
                    dateBox.setVisible(true);
                    to.setVisible(false);
                }
            }

            getReportRpc(report);
        }

        public void setBooleanListItem(int index) {
            booleanType.setSelectedIndex(index);
        }

        public void setVisibleWidget(boolean p) {
            booleanType.setVisible(p);
        }

        public ColumnRpc getField() {
            return map.get(field.getSelectedIndex());
        }

        public String getOperator() {
            return operation.getValue(operation.getSelectedIndex());
        }

        public String getValue() {

            if (getField() != null) {
                if (getField().getFilterWidgetType() != null && !"".equals(getField().getFilterWidgetType())) {
                    if (getField().getFilterWidgetType().equals("checkbox")) {
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
                    } else if (getField().getFilterWidgetType().equals("dropdown")) {
                        if (listBox.getSelectedIndex() >= 0) {
                            return listBox.getValue(listBox.getSelectedIndex());
                        } else {
                            return listBox.getValue(0);
                        }

                    } else if (getField().getFilterWidgetType().equals("lookup")) {
                        if (!wfmStrings.searchTypeMessage().equals(lookUp.getText().toLowerCase())) {
                            return lookUp.getText();
                        }
                    }

                } else {
                    if (SqlColumnType.DATE.getName().equals(getField().getType())) {
                        if (operation.getValue(operation.getSelectedIndex()).equals(DurationType.Between.name())) {
                            if (!to.getText().contains("23:59:59")) {
                                return FilterRow.getDate(dateBox.getSelectedDate()) + "_" + FilterRow.getDate(to.getSelectedDate()) + " 23:59:59";
                            }
                            return FilterRow.getDate(dateBox.getSelectedDate()) + "_" + FilterRow.getDate(to.getSelectedDate());
                        } else if (operation.getValue(operation.getSelectedIndex()).equals(DurationType.Before.name())) {
                            return FilterRow.getDate(dateBox.getSelectedDate());
                        } else if (!operation.getValue(operation.getSelectedIndex()).equals(DurationType.Equals.name()) && !operation.getValue(operation.getSelectedIndex()).equals(DurationType.After.name()) &&
                                !operation.getValue(operation.getSelectedIndex()).equals(DurationType.Before.name())) {
                            String endDate = DurationType.valueOf(operation.getValue(operation.getSelectedIndex())).getEndDate();
                            return DurationType.valueOf(operation.getValue(operation.getSelectedIndex())).getStartDate() + (endDate == null ? "" : ("_" + endDate));
                        } else {
                            return FilterRow.getDate(dateBox.getSelectedDate());
                        }
                    } else if (!"".equals(text.getValue())) {
                        return text.getValue();
                    }
                }
            }
            return null;
        }

        public String getBoolType() {
            return getBoolType(false);
        }

        public String getBoolType(boolean isfilter) {
            if (booleanType.isVisible() || isfilter) {
                return booleanType.getValue(booleanType.getSelectedIndex());
            }
            return null;
        }

        public Integer getSett() {
            return sett.getSelectedIndex() + 1;
        }

        public ListBox getSettListbox() {
            return sett;
        }

        public DRSListBox getFieldList() {
            return field;
        }

        public ListBox getOperation() {
            return operation;
        }

        public ListBox getListBox() {
            return listBox;
        }

        public FlexTable getCheckBoxList() {
            return checkBoxList;
        }

        public FlowPanel getCheckBoxListPanel() {
            return checkBoxListPanel;
        }

        public FilterLookUp getLookUp() {
            return lookUp;
        }

        public TextBox getTextBox() {
            return text;
        }

        public DRSDateBox getDateBox() {
            return dateBox;
        }

        public DRSDateBox getTo() {
            return to;
        }

        public ListBox getBooleanType() {
            return booleanType;
        }

        public KpiCheckBox getPromtInput() {
            return isPromtInput;
        }

        public void setPromtInput(KpiCheckBox promtInput) {
            isPromtInput = promtInput;
        }

        public boolean isPrompByInput() {
            return isPromtInput.getValue();
        }

        public void fillOperators() {
            fillOperators(getField());
        }

        public void fillOperators(ColumnRpc column) {
            operation.clear();
            if (!column.getType().equals(SqlColumnType.DATE.getName())) {
                if (!column.isTreeSelect()) {
                    for (OperationType type : OperationType.getOperations(column.getType())) {
                        operation.addItem(type.getName(), type.name());
                    }
                } else {
                    operation.addItem(OperationType.Contains.getName(), OperationType.Contains.name());
                    operation.addItem(OperationType.DoesNoTContain.getName(), OperationType.DoesNoTContain.name());
                }
            } else {
                for (DurationType type : DurationType.values()) {
                    operation.addItem(type.getName(), type.name());
                }
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
                        if (columnIndex > 10) {
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
            updateReportFilter(getSett());
//            listBox.clear();
//            listBox.addItem("Please wait...");
            CoreService.App.get().getFilterSelectItems("", report, column, new AsyncCallback<LinkedList<SelectItem>>() {

                @Override
                public void onFailure(Throwable throwable) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void onSuccess(LinkedList<SelectItem> selectItems) {
                    listBox.clear();
                    for (int i = 0; i < selectItems.size(); i++) {
                        listBox.addItem(selectItems.get(i).getName());
                        if (value != null && value.equals(selectItems.get(i).getName())) {
                            listBox.setSelectedIndex(i);
                        }
                    }

                    if (listBox.getSelectedIndex() < 0) {
                        listBox.setSelectedIndex(0);
                    }
                }
            });
        }

        public void setSerialNum(Integer serialNum) {
            this.serialNum = serialNum;
        }

        public Integer getSerialNum() {
            return this.serialNum;
        }
    }
}
