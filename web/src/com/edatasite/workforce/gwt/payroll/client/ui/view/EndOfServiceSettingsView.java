package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.EndOfServiceRules;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.AddListener;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.CategoryLookUp;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.EndOfServiceData;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Anchor;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.ListItem;
import gwt.material.design.client.ui.html.Span;
import gwt.material.design.client.ui.html.UnorderedList;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 02.05.14
 * Time: 15:46
 * To change this template use File | Settings | File Templates.
 */
public class EndOfServiceSettingsView extends CustomForm implements CustomFormConstants, Constants, Colapse {

    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();
    private static final String NUMBER = "NUMBER";
    private static final String RULE = "RULE";
    private static final String DAYS = "DAYS";
    private static final String MONTHS = "MONTHS";
    private static final Integer UNLIMITED = 0;
    private static final Integer LIMITED = 1;

    private UnorderedList mainPanel;
    private EditableTable settingsTableER;
    private EditableTable settingsTableCT;
    private EditableTable limitedSettingsTableER;
    private EditableTable limitedSettingsTableCT;
    private EndOfServiceData result;
    private DataListBox payFrom;
    private DynamicTable allowancesTable;
    private KpiSwitcher includeLeaveAllowances;
    private KpiSwitcher includeBenefitPayments;
    private KpiSwitcher allAllowancesLastPayment;
    private KpiSwitcher useMonthPayment;
    private KpiSwitcher fromLastPayment;
    private FormGroup allLastPaymentsField;
    private FormGroup allowanceTableField;
    private FormGroup fromLastPaymentField;

    public EndOfServiceSettingsView() {
        super("endOfServiceSettings", payrollStrings.endOfServiceSettings());
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        mainPanel = new UnorderedList("collapsible--panels collapsible--arrows-left collapsible collapsible--gwt required");

        settingsTableER = new EditableTable(getColumns(), false);
        settingsTableCT = new EditableTable(getColumns(), false);
        limitedSettingsTableER = new EditableTable(getColumns(), false);
        limitedSettingsTableCT = new EditableTable(getColumns(), false);

        includeLeaveAllowances = new KpiSwitcher();
        includeLeaveAllowances.setOffLabel(payrollStrings.includeLeaveAllowances());

        includeBenefitPayments = new KpiSwitcher();
        includeBenefitPayments.setOffLabel(payrollStrings.includeBenefitPayments());

        allAllowancesLastPayment = new KpiSwitcher();
        allAllowancesLastPayment.setOffLabel(payrollStrings.allAllowancesLastPayment());
        allAllowancesLastPayment.addValueChangeHandler(e -> onChangeAllAllowancesCheckbox(!allAllowancesLastPayment.getValue()));

        useMonthPayment = new KpiSwitcher();
        useMonthPayment.setOffLabel(payrollStrings.usemonthforpayment());

        fromLastPayment = new KpiSwitcher();
        fromLastPayment.setOffLabel(payrollStrings.fromLastPayment());

        payFrom = createPayFromDropdown();
        payFrom.addValueChangeHandler((e) -> {
            allLastPaymentsField.setVisible(payFrom.getSelectedId() == 1);
            onChangeAllAllowancesCheckbox(payFrom.getSelectedId() == 1);
        });

        allowancesTable = new DynamicTable(getAllowanceTableColumns(), true);
        allowancesTable.addRow(getWidgetsForAllowancesTable(null));
        allowancesTable.addListener(new AddListener() {
            public void plusClicked(int rowId) {
                allowancesTable.insertRow(rowId + 1, getWidgetsForAllowancesTable(null));
            }

            @Override
            public void minusClicked(int rowId, Integer objectId) {

            }
        });
        allowancesTable.setBorderWidth(0);

        allowanceTableField = new FormGroup(wfmStrings.allowance(), allowancesTable);
        allLastPaymentsField = new FormGroup(allAllowancesLastPayment);
        fromLastPaymentField = new FormGroup(fromLastPayment);

        GRow defaultGrid = getGrid(payrollStrings.unlimitedContract());
        GColumn column1 = new GColumn(GColumnEnum.COL_6);
        GColumn column2 = new GColumn(GColumnEnum.COL_6);

        defaultGrid.add(column1);
        defaultGrid.add(column2);

        column1.add(new FormGroup(payrollStrings.employmentEndingByEmployeeResignation(), settingsTableER));
        column1.add(new FormGroup(useMonthPayment));
        column1.add(new FormGroup(includeLeaveAllowances));
        column1.add(new FormGroup(includeBenefitPayments));

        column2.add(new FormGroup(payrollStrings.employmentEndingByContractTermination(), settingsTableCT));
        column2.add(new FormGroup(wfmStrings.payFrom(), payFrom));
        column2.add(allLastPaymentsField);
        column2.add(allowanceTableField);
        column2.add(fromLastPaymentField);

        if (Utils.isUAECompany()) {
            GRow customGrid = getGrid(payrollStrings.limitedContract());
            GColumn column3 = new GColumn(GColumnEnum.COL_6);
            GColumn column4 = new GColumn(GColumnEnum.COL_6);

            customGrid.add(column3);
            customGrid.add(column4);

            column3.add(new FormGroup(payrollStrings.employmentEndingByEmployeeResignation(), limitedSettingsTableER));
            column4.add(new FormGroup(payrollStrings.employmentEndingByContractTermination(), limitedSettingsTableCT));
        }

        addField(END_OF_SERVICE_SETTINGS.MAIN_CONTENT, mainPanel);

        show();

        return this;
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), BTN_PRIMARY, clickEvent -> saveSettings());
    }

    @Override
    protected void getDataToFillFields() {
        getAndSetData();
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PAYROLL_END_OF_SERVICE_SETTINGS_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }


    private GRow getGrid(String title) {
        ListItem li = new ListItem("slideDown-box", "group", "active");

        Div header = new Div("collapsible-header", "active");
        Anchor a = new Anchor();
//        a.getElement().getStyle().setCursor(Style.Cursor.POINTER);

        Span titleSpan = new Span();
        titleSpan.setText(title);

        header.addClickHandler(clickEvent -> {
            if (header.getStyleName().contains("active")) {
                header.removeStyleName("active");
                li.removeStyleName("active");
            } else {
                header.addStyleName("active");
                li.addStyleName("active");
            }
        });

        Div body = new Div("collapsible-body");
        a.add(titleSpan);
        header.add(a);
        li.add(header);
        li.add(body);
        mainPanel.add(li);

        GRow grid = new GRow();
        body.add(grid);

        return grid;
    }

    private void onChangeAllAllowancesCheckbox(boolean visible) {
        allowanceTableField.setVisible(visible);
        fromLastPaymentField.setVisible(visible);
    }

    private DynamicTableColumn[] getAllowanceTableColumns() {
        DynamicTableColumn[] columns = new DynamicTableColumn[1];
        columns[0] = new DynamicTableColumn("", "allowance", 450);
        return columns;
    }


    private Widget[] getWidgetsForAllowancesTable(PaymentDeductionSelectItem item) {
        Widget[] widgets = new Widget[1];
        CategoryLookUp allowanceLookUp = new CategoryLookUp(PayrollConstants.CATEGORY_PAYMENT);
        if (item != null) {
            allowanceLookUp.addCategoryItem(item);
        }
        widgets[0] = allowanceLookUp;
        return widgets;
    }

    private DataListBox createPayFromDropdown() {
        SelectItem[] items = new SelectItem[2];
        items[0] = new SelectItem(0, wfmStrings.basicSalary());
        items[1] = new SelectItem(1, wfmStrings.basicAllowancePay());
        final DataListBox dropdown = new DataListBox();
        dropdown.setItems(items);
        dropdown.setSelected(0);
        return dropdown;
    }

    private void getAndSetData() {
        PayrollService.App.get().getEndOfServiceSettings(Utils.getCompanyrCountryCode(), new AsyncCallback<EndOfServiceData>() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(EndOfServiceData data) {
                if (data != null) {
                    result = data;
                    setData();
                } else {
                    for (int i = 1; i <= 4; i++) {
                        settingsTableER.addRow(getWidgets(i, true));
                    }
                    for (int i = 1; i <= 2; i++) {
                        settingsTableCT.addRow(getWidgets(i, false));
                    }
                }
            }
        });
    }

    private void setData() {
        int i = 0;
        for (EndOfServiceRules rule : result.getRules()) {
            if (Constants.EMPLOYEE_RESIGNATION.equals(rule.getReasonCode())) {
                if (Utils.isUAECompany() && rule.getRuleType().equals(LIMITED)) {
                    limitedSettingsTableER.addRow(generateWidgets(rule, ++i));
                } else {
                    settingsTableER.addRow(generateWidgets(rule, ++i));
                }
            } else {
                if (Utils.isUAECompany() && rule.getRuleType().equals(LIMITED)) {
                    limitedSettingsTableCT.addRow(generateWidgets(rule, ++i));
                } else {
                    settingsTableCT.addRow(generateWidgets(rule, ++i));
                }
            }
        }
        includeLeaveAllowances.setValue(result.isIncludeLeaveAllowances());
        useMonthPayment.setValue(result.isUseMonthPayment());
        includeBenefitPayments.setValue(result.isIncludeBenefitPayments());
        allAllowancesLastPayment.setValue(result.isAllAllowanceFromLastPayment());
        fromLastPayment.setValue(result.isFromLastPayment());

        payFrom.setSelected(result.getPayType());

        if (result.getAllowances() != null && result.getAllowances().size() > 0) {
            allAllowancesLastPayment.setVisible(true);
            allowancesTable.clear();
            for (PaymentDeductionSelectItem item : result.getAllowances()) {
                allowancesTable.addRow(getWidgetsForAllowancesTable(item));
            }
        }
        onChangeAllAllowancesCheckbox(!allAllowancesLastPayment.getValue());
    }

    private Object[] generateWidgets(EndOfServiceRules rule, Integer rowNo) {
        final LinkedHashMap<String, Object> widgetsMap = new LinkedHashMap<>();
        EditableTextBox numberBox = new EditableTextBox();
        numberBox.setText(rowNo.toString());
        numberBox.setEnabled(false);
        widgetsMap.put(NUMBER, numberBox);
        EditableTextBox ruleBox = new EditableTextBox();
        ruleBox.setEnabled(false);
        ruleBox.setText(rule.getRule());
        ruleBox.setAdditionalField(rule.getRuleCode());

        widgetsMap.put(RULE, ruleBox);

        EditableTextBox dayBox = new EditableTextBox();
        if (Utils.isSaudiCompany()) {
            dayBox.setText(rule.getPaymentAward());
            dayBox.setEnabled(false);
        } else {
            dayBox.setText(rule.getDays().toString());
        }

        widgetsMap.put(DAYS, dayBox);

        EditableTextBox monthBox = new EditableTextBox();
        if (Utils.isSaudiCompany()) {
            monthBox.setText(rule.getPaymentAward());
            monthBox.setEnabled(false);
        } else {
            if (rule.getMonths() != null) {
                monthBox.setText(rule.getMonths().toString());
            } else {
                monthBox.setText("0");
            }
        }

        widgetsMap.put(MONTHS, monthBox);


        return widgetsMap.values().toArray(new Object[]{});
    }

    private void saveSettings() {
        EndOfServiceData data = getData();
        PayrollService.App.get().saveEndOfServiceSettings(data, new AsyncCallback<EndOfServiceData>() {
            @Override
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(EndOfServiceData saveData) {
                result = saveData;
                Info.show("End Of Service Settings saved succesfully", Info.Type.INFO);
            }
        });
    }

    private EndOfServiceData getData() {
        EndOfServiceData data = result != null ? result : new EndOfServiceData();
        data.setCountryCode(Utils.getCompanyrCountryCode());
        EndOfServiceRules[] rules = new EndOfServiceRules[11];

        if (!Utils.isSaudiCompany()) {
            int index = -1;
            for (int rowID = 0; rowID < settingsTableER.getRowCount(); rowID++) {
                rules[++index] = new EndOfServiceRules();
                EditableTextBox rule = (EditableTextBox) settingsTableER.getColumnById(rowID, RULE);
                EditableTextBox days = (EditableTextBox) settingsTableER.getColumnById(rowID, DAYS);
                EditableTextBox months = (EditableTextBox) settingsTableER.getColumnById(rowID, MONTHS);
                rules[index].setDays(Integer.valueOf(days.getText()));
                rules[index].setMonths(Integer.valueOf(months.getText()));
                rules[index].setRuleCode(rule.getAdditionalField());
                rules[index].setRule(rule.getText());
                rules[index].setRuleType(UNLIMITED);
                rules[index].setReasonCode(Constants.EMPLOYEE_RESIGNATION);
            }

            for (int rowID = 0; rowID < settingsTableCT.getRowCount(); rowID++) {
                rules[++index] = new EndOfServiceRules();
                EditableTextBox rule = (EditableTextBox) settingsTableCT.getColumnById(rowID, RULE);
                EditableTextBox days = (EditableTextBox) settingsTableCT.getColumnById(rowID, DAYS);
                EditableTextBox months = (EditableTextBox) settingsTableCT.getColumnById(rowID, MONTHS);
                rules[index].setDays(Integer.valueOf(days.getText()));
                rules[index].setMonths(Integer.valueOf(months.getText()));
                rules[index].setRuleCode(rule.getAdditionalField());
                rules[index].setRule(rule.getText());
                rules[index].setRuleType(UNLIMITED);
                rules[index].setReasonCode(Constants.CONTRACT_TERMINATION);
            }
            if (Utils.isUAECompany()) {
                for (int rowID = 0; rowID < limitedSettingsTableER.getRowCount(); rowID++) {
                    rules[++index] = new EndOfServiceRules();
                    EditableTextBox rule = (EditableTextBox) limitedSettingsTableER.getColumnById(rowID, RULE);
                    EditableTextBox days = (EditableTextBox) limitedSettingsTableER.getColumnById(rowID, DAYS);
                    EditableTextBox months = (EditableTextBox) limitedSettingsTableER.getColumnById(rowID, MONTHS);
                    rules[index].setDays(Integer.valueOf(days.getText()));
                    rules[index].setMonths(Integer.valueOf(months.getText()));
                    rules[index].setRuleCode(rule.getAdditionalField());
                    rules[index].setRule(rule.getText());
                    rules[index].setRuleType(LIMITED);
                    rules[index].setReasonCode(Constants.EMPLOYEE_RESIGNATION);
                }

                for (int rowID = 0; rowID < limitedSettingsTableCT.getRowCount(); rowID++) {
                    rules[++index] = new EndOfServiceRules();
                    EditableTextBox rule = (EditableTextBox) limitedSettingsTableCT.getColumnById(rowID, RULE);
                    EditableTextBox days = (EditableTextBox) limitedSettingsTableCT.getColumnById(rowID, DAYS);
                    EditableTextBox months = (EditableTextBox) limitedSettingsTableCT.getColumnById(rowID, MONTHS);
                    rules[index].setDays(Integer.valueOf(days.getText()));
                    rules[index].setMonths(Integer.valueOf(months.getText()));
                    rules[index].setRuleCode(rule.getAdditionalField());
                    rules[index].setRule(rule.getText());
                    rules[index].setRuleType(LIMITED);
                    rules[index].setReasonCode(Constants.CONTRACT_TERMINATION);
                }
            }

            data.setRules(rules);
        }

        data.setIncludeLeaveAllowances(includeLeaveAllowances.getValue());
        data.setIncludeBenefitPayments(includeBenefitPayments.getValue());
        data.setUseMonthPayment(useMonthPayment.getValue());
        data.setAllAllowanceFromLastPayment(allAllowancesLastPayment.getValue());
        data.setFromLastPayment(fromLastPayment.getValue());

        data.setPayType(payFrom.getSelectedId());
        if (payFrom.getSelectedId() == 1) {
            data.setAllowances(new ArrayList<>());
            for (int i = 0; i < allowancesTable.getRowNumber(); i++) {
                DynamicTableItem item = allowancesTable.getItem(i);
                CategoryLookUp allowanceLookUp = (CategoryLookUp) item.getColumnById("allowance");
                if (allowanceLookUp.getSelectedData() != null) {
                    data.getAllowances().add(allowanceLookUp.getSelectedData());
                }
            }
        }

        return data;
    }

    private Object[] getWidgets(Integer rowNo, boolean fromER) {
        final LinkedHashMap<String, Object> widgetsMap = new LinkedHashMap<>();
        EditableTextBox numberBox = new EditableTextBox();
        numberBox.setText(rowNo.toString());
        numberBox.setEnabled(false);
        widgetsMap.put(NUMBER, numberBox);
        EditableTextBox ruleBox = new EditableTextBox();
        ruleBox.setEnabled(false);
        if (rowNo.equals(1)) {
            if (fromER) {
                ruleBox.setText("Less than 1 year");
                ruleBox.setAdditionalField("0<x<1");
            } else {
                    ruleBox.setText("Less than 5 years");
                    ruleBox.setAdditionalField("x<=5");
            }
        }
        if (rowNo.equals(2)) {
            if (fromER) {
                ruleBox.setText("1 to 3 years");
                ruleBox.setAdditionalField("1<=x<3");
            } else {
                    ruleBox.setText(wfmStrings.moreThan() + " 5 years");
                    ruleBox.setAdditionalField("x>5");
            }
        }

        if (rowNo.equals(3)) {
            ruleBox.setText("3 to 5 years");
            ruleBox.setAdditionalField("3<=x<5");
        }

        if (rowNo.equals(4)) {
            ruleBox.setText(wfmStrings.moreThan() + " 5 years");
            ruleBox.setAdditionalField("x>5");
        }

        widgetsMap.put(RULE, ruleBox);

        EditableTextBox dayBox = new EditableTextBox();

        if (rowNo.equals(1)) {
            if (fromER) {
                dayBox.setText("0");
            } else {
                dayBox.setText("21");
            }
        }
        if (rowNo.equals(2)) {
            if (fromER) {
                dayBox.setText("7");
            } else {
                dayBox.setText("30");
            }
        }

        if (rowNo.equals(3)) {
            dayBox.setText("14");
        }

        if (rowNo.equals(4)) {
            dayBox.setText("21");
        }
        widgetsMap.put(DAYS, dayBox);

        EditableTextBox montBox = new EditableTextBox();
        montBox.setText("0");
        widgetsMap.put(MONTHS, montBox);


        return widgetsMap.values().toArray(new Object[]{});
    }

    private ColumnConfig[] getColumns() {
        int i = -1;
        ColumnConfig[] columns = new ColumnConfig[3];
        columns[++i] = new ColumnConfig(CustomCell.class, wfmStrings.no(), wfmStrings.no(), 80, false, "center-align-Cell");
        columns[i].setTitle(wfmStrings.no());
        columns[++i] = new ColumnConfig(CustomCell.class, RULE, " -" + wfmStrings.taskDuration(), 150, false, "center-align-Cell");
        columns[i].setTitle(" - " + wfmStrings.taskDuration());
        columns[++i] = new ColumnConfig(CustomCell.class, DAYS, Utils.isSaudiCompany() ? "Payment Award" : "No. of Days", 100, false, "center-align-Cell");
        columns[i].setTitle(Utils.isSaudiCompany() ? "Payment Award" : "No. of Days");

        columns[++i] = new ColumnConfig(CustomCell.class, MONTHS, Utils.isSaudiCompany() ? "Payment Award" : "No. of Month", 100, false, "center-align-Cell");
        columns[i].setTitle(Utils.isSaudiCompany() ? "Payment Award" : "No. of Month");

        return columns;

    }

    @Override
    public String getIconStyle() {
        return "payroll payments-list";
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
