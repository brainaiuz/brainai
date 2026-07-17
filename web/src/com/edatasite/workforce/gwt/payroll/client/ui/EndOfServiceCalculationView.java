package com.edatasite.workforce.gwt.payroll.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.PayslipItemFilter;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.ExpenseData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.EndOfServiceRules;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.Frequency;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.TotalTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeByPermissionLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PaymentAccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollEmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.CategoryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.PayslipItemAmountWidget;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.EndOfServiceData;
import com.edatasite.workforce.gwt.payroll.client.rpc.EoSCalculationData;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.ADDITIONAL_PAYMENT_ITEM_LIST;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.EXPENSES;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.PAYMENT_POLICY;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 12.05.14
 * Time: 17:30
 * To change this template use File | Settings | File Templates.
 */
public class EndOfServiceCalculationView extends CustomForm implements Colapse {

    private static final String TOTAL_WORKED_DAYS = "TOTAL_WORKED_DAYS";
    private static final String EOS_REASON = "EOS_REASON";
    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();
    private static final Integer calculationScale = Optional.ofNullable(Utils.getAccountingCalculationScale()).orElse(2);
    private PayrollEmployeeLookUp employeeLookUp;
    private DatePicker date;
    private TextBox paymentNumber;
    private BankTransferNumberData numberData;
    private DataListBox reasonList;
    private ColumnConfig[] columns;
    private EndOfServiceData eosSettings;
    private EoSCalculationData calculationData;
    private final Boolean fromSaudi;
    private CurrencyWidget currencyWidget;
    protected EditableTable calculationTable;
    //single payrun
    private EditableTable paymentsTable;
    private EditableTable deductionsTable;
    private EditableTable expensesTable;
    private TotalTable totalsTable;
    private TextArea2 paymentPolicy;
    private EmployeeByPermissionLookUp approver;
    private BigDecimal calculatedSalary, comPenTotal, total, addPay, expTotal, penTotal, allowance;
    private SinglePayrunItem payslipData;
    private HTML allowanceTotalHtml, deductTotalHtml, expenseTotalHtml, pensionTotalHtml, totalLabel, totalhtml, baseTotalLabel, baseTotalHtml;

    public EndOfServiceCalculationView(boolean fromSaudi) {
        super("endOfServiceCalculation", wfmStrings.endOfServiceGratuity());
        this.fromSaudi = fromSaudi;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        init();
        getDataToFillFields();
        return null;
    }

    private PaymentDeductionObject expenses;

    public void init() {
        employeeLookUp = new PayrollEmployeeLookUp(true, true);
        date = new DatePicker();
        date.setDate(new Date());

        employeeLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            final PayslipItemFilter filter = new PayslipItemFilter();

            filter.setProcessDate(new DateNonConvertable(date.getDate()));
            filter.setFromChangeHandler(true);
            filter.setCalculateBasicSalaryFromProject(Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PAYRUN_EMPLOYEE_SALARY_CURRENCY));
            filter.setEmployeeID(employeeLookUp.getSelectedItemID());

            PayrollService.App.get().getEmployeeEosData(filter, Utils.getCompanyrCountryCode(), new AsyncCallback<EoSCalculationData>() {
                @Override
                public void onFailure(Throwable caught) {
                    caught.printStackTrace();
                }

                @Override
                public void onSuccess(EoSCalculationData result) {
                    calculationData = result;
                    if (result.isEnabledMultiCurrency()) {
                        FormGroup exchangeRateBox = new FormGroup(getTitle(wfmStrings.exchangeRate()), currencyWidget);
                        addField(EXCHANGE_RATE, exchangeRateBox);
                        if (result.getCurrency() != null) {
                            currencyWidget.setCurrency(result.getCurrency().getId());
                        }
                    }
                    setValues(result);
                }
            });
        });
        numberData = new BankTransferNumberData();
        paymentNumber = new TextBox();
        reasonList = new DataListBox();
        reasonList.setWithoutNullLabel(true);
        reasonList.addListItem(new SelectItem(0, wfmStrings.employeeResignation(), Constants.EMPLOYEE_RESIGNATION));
        reasonList.addListItem(new SelectItem(1, wfmStrings.contractTermination(), Constants.CONTRACT_TERMINATION));
        reasonList.setSelected(0);
        reasonList.setChangeEvent(() -> {
            if (calculationData != null) {
                setValues(calculationData);
            }
        });
        currencyWidget = new CurrencyWidget();
        currencyWidget.setEnabled(false);
        currencyWidget.setDatePicker(date);
        currencyWidget.addListener(() -> {
//                updateTotal();
        });
        calculationTable = new EditableTable(getColumns(), false);
        calculationTable.addRow(getWidgets(null));

        addTitleField(PAYROLL.EOS_GRATUITY, payrollStrings.endOfServiceGratuityCalculation());
        addField(PAYROLL_STARTER.EMPLOYEE, employeeLookUp, getTitle(wfmStrings.employee()));
        addField(CREATED_DATE, date, getTitle(wfmStrings.date()));
        addField(PAYROLL_STARTER.PAYMENT_NUMBER, paymentNumber, getTitle(wfmStrings.number()));
        addField(REASON, reasonList, getTitle(wfmStrings.reason()));
        addField(PAYROLL_STARTER.CALCULATION_TABLE, calculationTable);

        initPayrunWidgets();
    }

    public Widget[] getWidgets(EoSCalculationData data) {
        final List<Widget> widgetsMap = new ArrayList<>();
        EditableTextBox hireDateBox = new EditableTextBox();
        hireDateBox.setEnabled(false);
        if (data != null && data.getHireDate() != null) {
            hireDateBox.setText(DateUtils.getFormat().format(data.getHireDate().getDate()));
        }
        widgetsMap.add(hireDateBox);
        EditableTextBox resignationDateBox = new EditableTextBox();
        resignationDateBox.setEnabled(false);
        if (data != null && data.getResignationDate() != null && data.getResignationDate().getDate() != null) {
            resignationDateBox.setText(DateUtils.getFormat().format(data.getResignationDate().getDate()));
        }
        widgetsMap.add(resignationDateBox);

        if (fromSaudi) {
            TextArea2 eosReason = new TextArea2();
            if (data != null && data.getEosReasonString() != null) {
                eosReason.setText(data.getEosReasonString());
            }
            widgetsMap.add(eosReason);
        }

        EditableTextBox totalWorkedDays = new EditableTextBox();
        totalWorkedDays.setEnabled(false);
        if (data != null) {
            totalWorkedDays.setText(data.getTotalWorkedDays().toString());
        } else {
            totalWorkedDays.setText("0");
        }

        widgetsMap.add(totalWorkedDays);

        EditableTextBox totalAmount = new EditableTextBox();
        totalAmount.setEnabled(false);
        if (data != null) {
            totalAmount.setText(data.getEosAmount() != null ? PayrollClientUtils.format(data.getEosAmount()) : PayrollClientUtils.format(BigDecimal.ZERO));
        } else {
            totalAmount.setText(PayrollClientUtils.format(BigDecimal.ZERO));
        }
        widgetsMap.add(totalAmount);
        return widgetsMap.toArray(new Widget[]{});
    }

    public ColumnConfig[] getColumns() {
        columns = new ColumnConfig[fromSaudi ? 5 : 4];
        columns[0] = new ColumnConfig(CustomCell.class, "Hire Date", 100, false);
        columns[0].setTitle(wfmStrings.hireDate());
        columns[1] = new ColumnConfig(CustomCell.class, "Resignation Date", 120, false);
        columns[1].setTitle(wfmStrings.resignationDate());
        if (fromSaudi) {
            columns[2] = new ColumnConfig(CustomCell.class, "End Of Service Reason", 150, false);
            columns[2].setTitle("End Of Service Reason");
            columns[3] = new ColumnConfig(CustomCell.class, wfmStrings.totalWorkedDays(), 150, false);
            columns[3].setTitle(wfmStrings.totalWorkedDays());
            columns[4] = new ColumnConfig(CustomCell.class, "EoS Gratuity amount", 150, false);
            columns[4].setTitle(payrollStrings.eoSGratuityAmount());
        } else {
            columns[2] = new ColumnConfig(CustomCell.class, wfmStrings.totalWorkedDays(), 150, false);
            columns[2].setTitle(wfmStrings.totalWorkedDays());
            columns[3] = new ColumnConfig(CustomCell.class, "EoS Gratuity amount", 150, false);
            columns[3].setTitle(payrollStrings.eoSGratuityAmount());
        }

        return columns;
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), WfmButton2.BTN_PRIMARY, wfmStrings.save(), wfmStrings.save(), event -> {
            save();
        });
    }


    private void save() {
        if (!this.isValidForm()) {
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            return;
        }
        final EoSCalculationData data = this.getTransferObject();

        LoadingPanel.loading(true);
        PayrollService.App.get().saveEosCalculationData(data, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Boolean result) {
                LoadingPanel.loading(false);
                if (result == null || result) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EOS_CALCULATION_SAVE, null, EndOfServiceCalculationView.this);
                    closeTab();
                } else {
                    Info.show("Error saving End of Service Gratuity Payment", Info.Type.WARNING);
                }
            }
        });
    }

    private boolean isValidForm() {
        int errors = 0;

        if (!Validation.validateLookUpRequired(approver)) {
            errors++;
        }
        if (!Validation.validateLookUpRequired(employeeLookUp)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(paymentNumber)) {
            errors++;
        }
        if (!Validation.validateDate(date)) {
            errors++;
        }

        if (errors > 0) {
            return false;
        }

        if (Utils.isEndOfServiceLocked() && DateUtils.getTransactionLockDate().after(date.getDate())) {
            Info.show(wfmMessages.dateShouldBeAfterClosedBeforeDate(wfmStrings.endOfServiceGratuity(), Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        }

        return true;
    }


    private EoSCalculationData getTransferObject() {
        final EoSCalculationData data = calculationData != null ? calculationData : new EoSCalculationData();

        data.setEmployee(employeeLookUp.getSelectedItem());
        data.setNumberData(numberData);
        data.setPaymentNumber(paymentNumber.getText());
        data.setCreator(new SelectItem(Utils.getUserID(), ""));
        data.setReasonCode(reasonList.getSelectedItem().getDescription());
        data.setDate(new DateNonConvertable(date.getDate()));
        if (data.isEnabledMultiCurrency()) {
            data.setCurrency(currencyWidget.getCurrency());
            data.setExchangeRate(currencyWidget.getExchangeRate());
        }
        final SinglePayrunItem payrunItem = getData();

        if (payrunItem != null) {
            data.setPayrunItem(payrunItem);
        }
        return data;
    }

    @Override
    protected void getDataToFillFields() {

        PayrollService.App.get().getEndOfServiceSettings(Utils.getCompanyrCountryCode(), new AsyncCallback<EndOfServiceData>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(EndOfServiceData result) {
                eosSettings = result;
                numberData = result.getNumberData();
                paymentNumber.setText(result.getNumberData().getNumberString());
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PAYROLL_EOS_CALCULATION_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
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

    public void setValues(EoSCalculationData result) {
        paymentNumber.setText(result.getPaymentNumber());
        numberData = result.getNumberData();
        BigDecimal total = BigDecimal.ZERO;

        if (result.getHireDate() != null && result.getHireDate().getDate() == null) {
            Info.show("Please Add Hire Date", Info.Type.WARNING);
            calculationTable.removeAllRows();
            calculationTable.addRow(getWidgets(null));
            return;
        }
        if (result.getResignationDate() != null && result.getResignationDate().getDate() == null) {
            Info.show("Please Add Resignation Date", Info.Type.WARNING);
            calculationTable.removeAllRows();
            calculationTable.addRow(getWidgets(null));
            return;
        }
        if (result.getTotalWorkedDays() == null) {
            return;
        }
        double year = Double.valueOf(result.getTotalWorkedDays()) / 365;
        if (fromSaudi) {
            BigDecimal amount = result.getBasicSalary();
            amount = amount.add(result.getLastPaymentsTotal());
            if (reasonList.getSelectedId() == 1) {
                if (year <= 5) {
                    total = amount.multiply(BigDecimal.valueOf(year / 2.0));
                } else {
                    total = amount.multiply(BigDecimal.valueOf(year - 2.5));
                }
            } else {
                if (year > 2 && year <= 5) {
                    total = amount.multiply(BigDecimal.valueOf(year / 6.0));
                } else if (year > 5 && year <= 10) {
                    total = amount.multiply(BigDecimal.valueOf(2.0 * (year - 2.5) / 3.0));
                } else if (year > 10) {
                    total = amount.multiply(BigDecimal.valueOf(year - 2.5));
                }
            }
        } else {
            double d = result.getNumberOfWorkDay() != null ? 1 / result.getNumberOfWorkDay() : 12.00 / 365;
            Integer days = 0;
            Integer months = null;
            boolean moreThan5Years = false;
            for (EndOfServiceRules rule : eosSettings.getRules()) {
                if (rule.getMonths() != null && rule.isUseMonthPayment()) {
                    months = rule.getMonths();
                }
                if (reasonList.getSelectedId() == 0) {  // Employee Resignation
                    if (rule.getRuleCode().equals("0<x<1") && rule.getReasonCode().equals(Constants.EMPLOYEE_RESIGNATION) && rule.getRuleType().equals(result.getEmployeeContractType())) {
                        if (year > 0 && year < 1) {
                            days = rule.getDays();
                        }
                    } else if (rule.getRuleCode().equals("1<=x<3")) {
                        if (year >= 1 && year < 3) {
                            days = rule.getDays();
                        }
                    } else if (rule.getRuleCode().equals("3<=x<5")) {
                        if (year >= 3 && year < 5) {
                            days = rule.getDays();
                        }
                    } else if (rule.getRuleCode().equals("x>5") && rule.getReasonCode().equals(Constants.EMPLOYEE_RESIGNATION) && rule.getRuleType().equals(result.getEmployeeContractType())) {
                        if (year > 5) {
                            moreThan5Years = true;
                            days = rule.getDays();
                        }
                    } else if (rule.getRuleCode().equals("1<=x<5") && rule.getReasonCode().equals(Constants.EMPLOYEE_RESIGNATION) && rule.getRuleType().equals(result.getEmployeeContractType())) {
                        if (year >= 1 && year < 5) {
                            days = rule.getDays();
                        }
                    }
                } else {
                    if (rule.getRuleCode().equals("x<=5")) {
                        if (year <= 5) {
                            days = rule.getDays();
                        }
                    } else if (rule.getRuleCode().equals("x>5") && rule.getReasonCode().equals(Constants.CONTRACT_TERMINATION) && rule.getRuleType().equals(result.getEmployeeContractType())) {
                        if (year > 5) {
                            moreThan5Years = true;
                            days = rule.getDays();
                        }
                    } else if (rule.getRuleCode().equals("0<x<1") && rule.getReasonCode().equals(Constants.CONTRACT_TERMINATION) && rule.getRuleType().equals(result.getEmployeeContractType())) {
                        if (year > 0 && year < 1) {
                            days = rule.getDays();
                        }
                    } else if (rule.getRuleCode().equals("x>1") && rule.getRuleType().equals(result.getEmployeeContractType())) {
                        if (year > 1) {
                            days = rule.getDays();
                        }
                    }
                }
            }
            BigDecimal calculatePeriod;
            if (months != null) {
                calculatePeriod = new BigDecimal(months);
            } else {
                calculatePeriod = BigDecimal.valueOf(d).multiply(new BigDecimal(days));
            }
            if (moreThan5Years) {
                BigDecimal years5Total = result.getBasicSalary().multiply(BigDecimal.valueOf(d * 21 * 5));
                BigDecimal after5YearsTotal = result.getBasicSalary().multiply(calculatePeriod).multiply(BigDecimal.valueOf(year - 5));
                total = years5Total.add(after5YearsTotal);
            } else {
                total = result.getBasicSalary().multiply(calculatePeriod).multiply(BigDecimal.valueOf(year));
            }
        }
        total = total.add(result.getLeaveAllowanceTotal());
        total = total.add(result.getBenefitPaymentTotal());
        result.setEosAmount(total);
        calculationTable.removeAllRows();
        calculationTable.addRow(getWidgets(result));
        setPayrunData(result.getPayrunItem());
    }

    private void initPayrunWidgets() {
        paymentPolicy = new TextArea2(1000);
        paymentPolicy.setPlaceHolder(payrollStrings.hereYouCanAddYourPaymentPolicy());

        totalLabel = new HTML(wfmStrings.total());

        allowanceTotalHtml = new HTML(PayrollClientUtils.format(BigDecimal.ZERO));
        deductTotalHtml = new HTML(PayrollClientUtils.format(BigDecimal.ZERO));
        expenseTotalHtml = new HTML(PayrollClientUtils.format(BigDecimal.ZERO));
        pensionTotalHtml = new HTML(PayrollClientUtils.format(BigDecimal.ZERO));
        totalhtml = new HTML(PayrollClientUtils.format(BigDecimal.ZERO));

        totalsTable = new TotalTable();
        totalsTable.addItem(wfmStrings.totalAllowances(), allowanceTotalHtml);
        totalsTable.addItem(wfmStrings.totalDeductions(), deductTotalHtml);
        totalsTable.addItem(wfmStrings.totalExpenses(), expenseTotalHtml);
        totalsTable.addItem(wfmStrings.govPension(), pensionTotalHtml);
        totalsTable.addItem(totalLabel, totalhtml);

        paymentsTable = new EditableTable(getColumns(false, wfmStrings.payments()), false, false);
        setDefaultRows(true);
        deductionsTable = new EditableTable(getColumns(false, wfmStrings.deductions()), false, false);
        setDefaultRows(false);
        expensesTable = new EditableTable(getColumns(true, wfmStrings.expense()), false, false);
        setDefaultRows(null);

        approver = new EmployeeByPermissionLookUp();
        approver.setPermissionCode(PermissionConstants.PAYROLL_CAN_APPROVE_PAYSLIP);

        addTitleField(ADDITIONAL_INFORMATION, Property.get(ADDITIONAL_PAYMENT_ITEM_LIST, payrollStrings.singlePayment()));
        addField(PAYMENT_POLICY, paymentPolicy);
        addField(APPROVER, approver, wfmStrings.approver());
        addField(PAYMENT_TABLE, paymentsTable);
        addField(DEDUCTION_TABLE, deductionsTable);
        addField(EXPENSES, expensesTable);
        addField(TOTAL_TABLE_PANEL, totalsTable);
    }

    private void setPayrunData(SinglePayrunItem payrunItem) {
        if (payrunItem == null) {
            return;
        }
        paymentsTable.setValidRows(0);
        deductionsTable.setValidRows(0);
        expensesTable.setValidRows(0);

        payslipData = payrunItem;
        paymentPolicy.setText(payrunItem.getPaymentPolicy());
        currencyWidget.setVisible(payrunItem.isEnabledMultiCurrency());
        currencyWidget.setOnloadListener(() -> {
            if (payrunItem.isEnabledMultiCurrency() && payrunItem.getCurrency() != null) {
                currencyWidget.setCurrency(payrunItem.getCurrency().getId(), payrunItem.getExchangeRate());
                totalLabel.setText(wfmMessages.total(currencyWidget.getCurrencyName()));
                baseTotalLabel = new HTML(wfmMessages.total(currencyWidget.getBaseCurrencyName()));
                baseTotalHtml = new HTML();
                totalsTable.addWidgetsInARow(baseTotalLabel, baseTotalHtml);
            } else {
                totalLabel.setText(wfmMessages.total(currencyWidget.getBaseCurrencyName()));
            }
        });
        if (payslipData.isFromEndOfService()) {
            employeeLookUp.setEnabled(false);
        }
        paymentsTable.removeAllRows();

        if (payrunItem.getPaymentCategories().isEmpty()) {
            setDefaultRows(true);
        } else {
            for (PaymentDeductionObject payment : payrunItem.getPaymentCategories()) {
                if (payment.isSalaryObject()) {
                    addItem(payment, true, paymentsTable);
                    paymentsTable.incValidRow();
                }
            }
            for (PaymentDeductionObject payment : payrunItem.getPaymentCategories()) {
                if (!payment.isSalaryObject()) {
                    addItem(payment, true, paymentsTable);
                    paymentsTable.incValidRow();
                }
            }
        }

        deductionsTable.removeAllRows();
        if (payrunItem.getDeductionCategories().isEmpty()) {
            setDefaultRows(false);
        } else {
            for (PaymentDeductionObject deduction : payrunItem.getDeductionCategories()) {
                addItem(deduction, false, deductionsTable);
                deductionsTable.incValidRow();
            }
        }

        expenses = payrunItem.getEmployeeExpenses();
        expensesTable.removeAllRows();
        if (payrunItem.getEmployeeExpenses() != null && payrunItem.getEmployeeExpenses().getExpenses() != null && payrunItem.getEmployeeExpenses().getExpenses().length > 0) {
            for (ExpenseData exp : payrunItem.getEmployeeExpenses().getExpenses()) {
                addExpenseItem(exp);
            }
        } else {
            addExpenseItem(null);
        }
        calculate(false);
    }

    private ColumnConfig[] getColumns(boolean fromExpenses, String title) {
        ColumnConfig[] columns;
        if (fromExpenses) {
            columns = new ColumnConfig[4];
            columns[0] = new ColumnConfig(CustomCell.class, "category", title, 150, true, "left-align-Cell");
            columns[1] = new ColumnConfig(CustomCell.class, "amount", wfmStrings.amount(), 100, false, "center-align-Cell");
            columns[2] = new ColumnConfig(LookUpCell.class, "paidfrom", wfmStrings.paidFrom(), 150, true);
            columns[3] = new ColumnConfig(CustomCell.class, "type", wfmStrings.type(), 100, false, "center-align-Cell");
        } else {
            columns = new ColumnConfig[4];
            columns[0] = new ColumnConfig(LookUpCell.class, "category", title, 200, true, "left-align-Cell");
            columns[1] = new ColumnConfig(CustomCell.class, "type", wfmStrings.type(), 100, false, "center-align-Cell");
            columns[2] = new ColumnConfig(CustomCell.class, "remarks", wfmStrings.remarks(), 100, false, "center-align-Cell");
            columns[3] = new ColumnConfig(CustomCell.class, "amount", wfmStrings.amount(), 100, true, "right-align-Cell");
        }
        return columns;
    }

    private void setDefaultRows(Boolean isPayment) {
        int length = 0;
        while (length < 5) {
            if (isPayment == null) {
                addExpenseItem(null);
            } else if (isPayment) {
                addItem(null, true, paymentsTable);
            } else {
                addItem(null, false, deductionsTable);
            }
            length++;
        }
    }

    private void addExpenseItem(ExpenseData exp, boolean... notEditable) {
        if (notEditable.length > 0 && notEditable[0]) {
            return;
        }
        final EditableTextBox title = new EditableTextBox();

        title.setEnabled(false);
        if (exp != null && exp.getTitle() != null) {
            title.setText(exp.getTitle());
        }
        final PayslipItemAmountWidget amountWidget = new PayslipItemAmountWidget();

        amountWidget.getAmountTextBox().setEnabled(false);
        if (exp != null && exp.getAmount() != null) {
            amountWidget.setAmount(BigDecimal.valueOf(exp.getAmount()));
            amountWidget.setBaseAmount(exp.isInBaseCurrency() ? BigDecimal.valueOf(exp.getAmount()) : null);
            amountWidget.setObject(exp);
        }
        final PaymentAccountsLookUp paidFromAccount = new PaymentAccountsLookUp();

        paidFromAccount.setEnabled(false);
        if (exp != null && exp.getAccountID() != null) {
            paidFromAccount.setSelected(new SelectItem(exp.getAccountID(), exp.getAccount()));
        }
        final DataListBox expensePaymentType = new DataListBox();

        expensePaymentType.setWithoutNullLabel(true);
        expensePaymentType.setItems(new SelectItem[]{
                new SelectItem(0, wfmStrings.payment()),
                new SelectItem(1, wfmStrings.deduction())
        });
        expensePaymentType.setSelected(0);
        expensePaymentType.setEnabled(false);
        expensesTable.addRow(new Widget[]{title, amountWidget, paidFromAccount, expensePaymentType});
        expensesTable.incValidRow();
    }

    private void calculate(boolean withoutLeaveDecution) {
        Integer days;

        BigDecimal basicSalary, dailyRate, leaveAmount;
        BigDecimal expPayTotal = BigDecimal.ZERO, expDedTotal = BigDecimal.ZERO;
        PayslipItemAmountWidget amountWidget = null;
        boolean isLeaveCalculated = false;
        calculatedSalary = BigDecimal.ZERO;
        comPenTotal = BigDecimal.ZERO;
        total = BigDecimal.ZERO;
        addPay = BigDecimal.ZERO;
        BigDecimal dedTotal = BigDecimal.ZERO;
        expTotal = BigDecimal.ZERO;
        penTotal = BigDecimal.ZERO;
        for (int i = 0; i < paymentsTable.getRowCount(); i++) {
            PayslipItemAmountWidget salaryWidget = (PayslipItemAmountWidget) paymentsTable.getColumnById(i, "amount");
            if (salaryWidget.isSalaryObject()) {
                amountWidget = salaryWidget;
                break;
            }
        }
        if (employeeLookUp.getSelectedItem() != null) {
            days = payslipData.getNumberOfWorkDay() != null ? payslipData.getNumberOfWorkDay().intValue() : 0;

            basicSalary = amountWidget != null ? amountWidget.getAmount() : BigDecimal.ZERO;
            dailyRate = (payslipData.getSalary().divide(BigDecimal.valueOf(Constants.DEFAULT_NUMBER_OF_WORK_DAYS), calculationScale, RoundingMode.HALF_UP));
            calculatedSalary = dailyRate.multiply(BigDecimal.valueOf(days));

            for (int k = 0; k < paymentsTable.getRowCount(); k++) {
                PayslipItemAmountWidget amount = (PayslipItemAmountWidget) paymentsTable.getColumnById(k, "amount");
                if (amount.getPercentage() != null && amountWidget != null) {
                    CustomCell amountWidgetCell = (CustomCell) paymentsTable.getColumnCellWidgetById(k, "amount");
                    amountWidgetCell.InActive();
                }
                if (!amount.isSalaryObject()) {
                    addPay = addPay.add(amount.getAmount());
                }
            }

            for (int i = 0; i < deductionsTable.getRowCount(); i++) {
                CategoryLookUp categoryLookUp = (CategoryLookUp) deductionsTable.getColumnById(i, "category");
                PayslipItemAmountWidget amount = (PayslipItemAmountWidget) deductionsTable.getColumnById(i, "amount");
                CustomCell amountWidgetCell = (CustomCell) deductionsTable.getColumnCellWidgetById(i, "amount");
                if (categoryLookUp.getSelectedData() != null && Constants.LEAVE_DEDUCTIONS.equals(categoryLookUp.getSelectedData().getCode()) && !withoutLeaveDecution) {
                    amountWidgetCell.InActive();
                }
                if (amount.getPercentage() != null) {
                    amountWidgetCell.InActive();
                }
                if (withoutLeaveDecution) {
                    if (categoryLookUp.getSelectedData() != null && !Constants.LEAVE_DEDUCTIONS.equals(categoryLookUp.getSelectedData().getCode())) {
                        dedTotal = dedTotal.add(amount.getAmount());
                    }
                } else {
                    dedTotal = dedTotal.add(amount.getAmount());
                }

            }

            for (int i = 0; i < expensesTable.getRowCount(); i++) {
                PayslipItemAmountWidget amount = (PayslipItemAmountWidget) expensesTable.getColumnById(i, "amount");
                DataListBox paymentType = (DataListBox) expensesTable.getColumnById(i, "type");
                if (paymentType.getSelectedId(true).equals(0)) {
                    expPayTotal = expPayTotal.add(amount.getAmount());
                } else {
                    expDedTotal = expDedTotal.add(amount.getAmount());
                }
                expTotal = expTotal.add(amount.getAmount());
            }
            allowance = addPay;
            addPay = addPay.add(!isLeaveCalculated && !payslipData.isFromEndOfService()
                    ? basicSalary
                    : BigDecimal.ZERO);
            total = addPay;
            total = total.add(expPayTotal);
            total = total.subtract(expDedTotal);
            total = total.subtract(dedTotal);
            if (payslipData.isCalculatePension() && ((payslipData.getPensionRate() != null && payslipData.getPensionRate().compareTo(BigDecimal.ZERO) != 0) || (payslipData.getNonLocalPensionRate() != null && payslipData.getNonLocalPensionRate().compareTo(BigDecimal.ZERO) != 0)) && !payslipData.isFromEndOfService()) {
                if (payslipData.getPensionType() != null && payslipData.getPensionType() == 0) {
                    if (payslipData.isLocalEmployee() && payslipData.getPensionRate() != null) {
                        penTotal = payslipData.getPensionRate();
                    } else if (!payslipData.isLocalEmployee() && payslipData.getNonLocalPensionRate() != null) {
                        penTotal = payslipData.getNonLocalPensionRate();
                    }
                } else if (payslipData.getPensionType() != null) {
                    BigDecimal empTaxableAmount = BigDecimal.ZERO;
                    BigDecimal compTaxableAmount = BigDecimal.ZERO;
                    if (payslipData.getEmpMaxTaxableAmount().compareTo(BigDecimal.ZERO) > 0 && calculatedSalary.compareTo(empTaxableAmount) >= 0) {
                        empTaxableAmount = payslipData.getEmpMaxTaxableAmount();
                    } else {
                        empTaxableAmount = calculatedSalary;
                    }
                    if (payslipData.getEmpMaxTaxableAmount().compareTo(BigDecimal.ZERO) > 0 && calculatedSalary.compareTo(compTaxableAmount) >= 0) {
                        compTaxableAmount = payslipData.getCompMaxTaxableAmount();
                    } else {
                        compTaxableAmount = calculatedSalary;
                    }
                    if (payslipData.isLocalEmployee()) {
                        if (payslipData.getPensionRate() != null && BigDecimal.ZERO.compareTo(payslipData.getPensionRate()) < 0) {
                            if (payslipData.getPensionAllowances() != null && payslipData.getPensionAllowances().size() > 0) {
                                penTotal = getPensionAmount(calculatedSalary, payslipData.getPensionRate(), payslipData.getPensionAllowances(), payslipData.getEmpMaxTaxableAmount());
                            } else {
                                penTotal = empTaxableAmount.multiply(payslipData.getPensionRate()).divide(BigDecimal.valueOf(100), calculationScale, RoundingMode.HALF_UP);
                            }

                        }
                        if (payslipData.getCompanyPensionRate() != null && BigDecimal.ZERO.compareTo(payslipData.getCompanyPensionRate()) < 0) {
                            if (payslipData.getPensionAllowances() != null && payslipData.getPensionAllowances().size() > 0) {
                                comPenTotal = getPensionAmount(calculatedSalary, payslipData.getCompanyPensionRate(), payslipData.getPensionAllowances(), payslipData.getCompMaxTaxableAmount());
                            } else {
                                comPenTotal = compTaxableAmount.multiply(payslipData.getCompanyPensionRate()).divide(BigDecimal.valueOf(100), calculationScale, RoundingMode.HALF_UP);
                            }
                        }
                    } else if (!payslipData.isLocalEmployee()) {
                        if (payslipData.getNonLocalPensionRate() != null && BigDecimal.ZERO.compareTo(payslipData.getNonLocalPensionRate()) < 0) {
                            if (payslipData.getPensionAllowances() != null && payslipData.getPensionAllowances().size() > 0) {
                                penTotal = getPensionAmount(calculatedSalary, payslipData.getNonLocalPensionRate(), payslipData.getPensionAllowances(), payslipData.getEmpMaxTaxableAmount());
                            } else {
                                penTotal = empTaxableAmount.multiply(payslipData.getNonLocalPensionRate()).divide(BigDecimal.valueOf(100), calculationScale, RoundingMode.HALF_UP);
                            }
                        }
                        if (payslipData.getCompanyNonLocalPensionRate() != null && BigDecimal.ZERO.compareTo(payslipData.getCompanyNonLocalPensionRate()) < 0) {
                            if (payslipData.getPensionAllowances() != null && payslipData.getPensionAllowances().size() > 0) {
                                comPenTotal = getPensionAmount(calculatedSalary, payslipData.getCompanyNonLocalPensionRate(), payslipData.getPensionAllowances(), payslipData.getCompMaxTaxableAmount());
                            } else {
                                comPenTotal = compTaxableAmount.multiply(payslipData.getCompanyNonLocalPensionRate()).divide(BigDecimal.valueOf(100), calculationScale, RoundingMode.HALF_UP);
                            }
                        }
                    }
                }
                total = total.subtract(penTotal);
            }
        }

        allowanceTotalHtml.setText(PayrollClientUtils.format(addPay));
        deductTotalHtml.setText(PayrollClientUtils.format(dedTotal));
        expenseTotalHtml.setText(PayrollClientUtils.format(expTotal));
        pensionTotalHtml.setText(PayrollClientUtils.format(penTotal));
        totalhtml.setText(PayrollClientUtils.format(total));
        if (payslipData.isEnabledMultiCurrency()) {
            if (currencyWidget.getExchangeRate() != null) {
                baseTotalHtml = new HTML(PayrollClientUtils.format(total.divide(currencyWidget.getExchangeRate(), calculationScale, RoundingMode.HALF_UP)));
            } else {
                baseTotalHtml = new HTML(PayrollClientUtils.format(total));
            }
        }
    }

    private void addItem(PaymentDeductionObject paymentDeduction, boolean isPayment, EditableTable categoriesTable) {
        final CategoryLookUp categoryLookUp = new CategoryLookUp(isPayment
                ? PayrollConstants.CATEGORY_PAYMENT
                : PayrollConstants.CATEGORY_DEDUCTION, () -> true);

        if (paymentDeduction != null && paymentDeduction.getCategoryItem() != null) {
            categoryLookUp.addCategoryItem(paymentDeduction.getCategoryItem());
            categoryLookUp.setEnabled(false);
        }
        categoryLookUp.setEnabled(false);
        EditableTextBox type = new EditableTextBox();
        EditableTextBox remarks = new EditableTextBox();
        type.setEnabled(false);
        if (paymentDeduction != null && paymentDeduction.getType() != null) {
            if (paymentDeduction.getType() == 0 || paymentDeduction.isLoan()) {
                type.setText(wfmStrings.fixed());
            } else if (paymentDeduction.getType() == 1) {
                type.setText(PayrollClientUtils.numberFormat(paymentDeduction.getPercentage()) + " % of Basic Salary");
            } else {
                type.setText(PayrollClientUtils.numberFormat(paymentDeduction.getPercentage()) + " % of Basic + Allowances");
            }
        } else {
            type.setText(wfmStrings.fixed());
        }
        final PayslipItemAmountWidget amountWidget = new PayslipItemAmountWidget();

        amountWidget.setEditable(false);
        amountWidget.setWidth("100px");
        amountWidget.setAmount(BigDecimal.ZERO);
        if (paymentDeduction != null) {
            if (paymentDeduction.getRemarks() != null) {
                remarks.setText(paymentDeduction.getRemarks());
            }
            if (paymentDeduction.getLeavePaymentItem() != null) {
                amountWidget.setLeavePaymentItem(paymentDeduction.getLeavePaymentItem());
            }
        }
        if (paymentDeduction != null) {
            amountWidget.setSickRequestIds(paymentDeduction.getSickRequestids());
            amountWidget.setSalaryObject(paymentDeduction.isSalaryObject());
            amountWidget.setCashAdvance(paymentDeduction.isCashAdvance());
            amountWidget.setTaxable(paymentDeduction.getCategoryItem().getTaxable());
            if (paymentDeduction.getType() == null || paymentDeduction.getType() == 0 || paymentDeduction.isLoan()) {
                amountWidget.setAmount(paymentDeduction.getPaymentAmount());
            } else if (paymentDeduction.getPercentage() != null) {
                amountWidget.setPercentage(paymentDeduction.getPercentage());
                amountWidget.setAmount(paymentDeduction.getPaymentAmount());
            }
            if (Constants.LEAVE_DEDUCTIONS.equals(paymentDeduction.getCategoryItem().getCode())) {
                amountWidget.setNumberOfWorkDays(paymentDeduction.getNumberOfWorkDays());
                amountWidget.setLeaveDaysCount(paymentDeduction.getLeaveDaysCount());
                amountWidget.setLeaveDeductType(paymentDeduction.getLeaveType());
                amountWidget.setLinkedCategories(paymentDeduction.getLinkedCategories());
            } else if (Constants.LEAVE_ENCHASHMENT.equals(paymentDeduction.getCategoryItem().getCode())) {
                amountWidget.setLeaveDaysCount(paymentDeduction.getLeaveDaysCount());
            }
            if (!isPayment && paymentDeduction.isLoan()) {
                amountWidget.setLoan(true);
                amountWidget.setRemainingAmount(paymentDeduction.getRemainingAmount());
            } else if (paymentDeduction.isSalaryObject()) {
                amountWidget.getAmountTextBox().addKeyUpHandler(keyUpEvent -> {
                    for (int i = 0; i < paymentsTable.getGrid().getRowCount(); i++) {
                        PayslipItemAmountWidget amount = (PayslipItemAmountWidget) paymentsTable.getColumnById(i, "amount");
                        if (amount.getPercentage() != null) {
                            CustomCell amountWidgetCell = (CustomCell) paymentsTable.getColumnCellWidgetById(i, "amount");
                            amount.setAmount(amountWidget.getAmount().multiply(amount.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, RoundingMode.HALF_UP));
                            amountWidgetCell.InActive();
                        }
                    }
                    for (int i = 0; i < deductionsTable.getGrid().getRowCount(); i++) {
                        PayslipItemAmountWidget amount = (PayslipItemAmountWidget) deductionsTable.getColumnById(i, "amount");
                        if (amount.getPercentage() != null) {
                            CustomCell amountWidgetCell = (CustomCell) deductionsTable.getColumnCellWidgetById(i, "amount");

                            if (amount.isFromAllAllowances()) {
                                BigDecimal allowanceTotal = BigDecimal.ZERO;
                                allowanceTotal = getAllowanceTotal(null);
                                amount.setAmount(allowanceTotal.multiply(amount.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, RoundingMode.HALF_UP));
                            } else if (amount.getLinkedCategories() != null && amount.getLinkedCategories().size() > 0) {
                                BigDecimal allowanceTotal = BigDecimal.ZERO;
                                allowanceTotal = getAllowanceTotal(amount.getLinkedCategories()).add(payslipData.getBasicSalary());
                                amount.setAmount(allowanceTotal.multiply(amount.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, RoundingMode.HALF_UP));
                            } else {
                                amount.setAmount(payslipData.getSalary().multiply(amount.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, RoundingMode.HALF_UP));
                            }

                            amount.setAmount(amountWidget.getAmount().multiply(amount.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, RoundingMode.HALF_UP));
                            amountWidgetCell.InActive();
                        }
                    }
                    calculate(false);
                });
            } else {
                amountWidget.getAmountTextBox().addKeyUpHandler(event -> calculate(false));
            }
            amountWidget.setItemID(paymentDeduction.getId());
        } else {
            amountWidget.getAmountTextBox().addKeyUpHandler(event -> calculate(false));
        }
        categoriesTable.addRow(new Widget[]{categoryLookUp, type, remarks, amountWidget});
    }

    private BigDecimal getAllowanceTotal(List<PaymentDeductionObject> linkedCategories) {
        BigDecimal result = BigDecimal.ZERO;
        if (linkedCategories != null) {
            for (int i = 0; i < paymentsTable.getRowCount(); i++) {
                CategoryLookUp categoryLookUp = (CategoryLookUp) paymentsTable.getColumnById(i, "category");
                PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) paymentsTable.getColumnById(i, "amount");
                if (categoryLookUp.getSelectedData() != null) {
                    for (PaymentDeductionObject item : linkedCategories) {
                        if (item.getCategoryItem().getId().equals(categoryLookUp.getSelectedData().getId())) {
                            result = result.add(amountWidget.getAmount());
                            break;
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < paymentsTable.getRowCount(); i++) {
                CategoryLookUp categoryLookUp = (CategoryLookUp) paymentsTable.getColumnById(i, "category");
                PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) paymentsTable.getColumnById(i, "amount");
                if (categoryLookUp.getSelectedData() != null) {
                    result = result.add(amountWidget.getAmount());
                }
            }
        }
        return result;
    }

    public BigDecimal getPensionAmount(BigDecimal calculatedSalary, BigDecimal pensionRate, List<PaymentDeductionSelectItem> pensionAllowances, BigDecimal maxTaxableAmount) {
        BigDecimal allowanceTotal = BigDecimal.ZERO;
        for (int i = 0; i < paymentsTable.getRowCount(); i++) {
            CategoryLookUp categoryLookUp = (CategoryLookUp) paymentsTable.getColumnById(i, "category");
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) paymentsTable.getColumnById(i, "amount");
            if (categoryLookUp.getSelectedData() != null) {
                for (PaymentDeductionSelectItem item : pensionAllowances) {
                    if (item.getId().equals(categoryLookUp.getSelectedData().getId())) {
                        allowanceTotal = allowanceTotal.add(amountWidget.getAmount());
                        break;
                    }
                }
            }
        }
        allowanceTotal = allowanceTotal.add(calculatedSalary);
        if (maxTaxableAmount.compareTo(BigDecimal.ZERO) > 0 && allowanceTotal.compareTo(maxTaxableAmount) >= 0) {
            allowanceTotal = maxTaxableAmount;
        }
        return allowanceTotal.multiply(pensionRate).divide(BigDecimal.valueOf(100), calculationScale, RoundingMode.HALF_UP);
    }

    private SinglePayrunItem getData() {
        if (payslipData == null) {
            return null;
        }
        final SinglePayrunItem data = new SinglePayrunItem();

        data.setStatus(Constants.PAYRUN_STATUS_DRAFT);
        data.setTotal(total);
        data.setMonthID(payslipData.getMonthID());
        data.setFrequency(Frequency.ANNUAL.getId());
        data.setYear(payslipData.getYear());
        data.setMonth(payslipData.getMonth());
        data.setApprover(approver.getSelectedItem());
        data.setCreator(new SelectItem(Utils.getUserID(), ""));
        data.setDeduction(PayrollClientUtils.parseToBigDecimal(deductTotalHtml.getText()));
        data.setAllowance(allowance);
        data.setExpense(expTotal);
        data.setActualMonthPay(!payslipData.isFromEndOfService() ? calculatedSalary : BigDecimal.ZERO);
        data.setEmployeeID(employeeLookUp.getSelectedItemID());
        data.setFromDate(payslipData.getFromDate());
        data.setToDate(payslipData.getToDate());
        data.setProcessDate(payslipData.getProcessDate());
        data.setDaysWorked(payslipData.getDaysWorked());
        data.setPaymentPolicy(paymentPolicy.getText());
        if (paymentsTable.getValidRows() > 0) {
            data.setPaymentCategories(getCategories(paymentsTable, true));
        }
        if (deductionsTable.getValidRows() > 0) {
            data.setDeductionCategories(getCategories(deductionsTable, false));
        }
        if (expensesTable.getValidRows() > 0) {
            data.setEmployeeExpenses(getEmployeeExpenses());
        }
        data.setPensionRate(payslipData.getPensionRate());
        data.setPensionType(payslipData.getPensionType());
        data.setPensionValueType(payslipData.getPensionValueType());
        data.setPensionAmount(penTotal);
        data.setCompanyPensionAmount(comPenTotal);
        data.setCompanyPensionRate(payslipData.getCompanyPensionRate());
        data.setCompanyNonLocalPensionRate(payslipData.getCompanyNonLocalPensionRate());
        data.setCompanyPensionType(payslipData.getCompanyPensionType());
        data.setEmpMaxTaxableAmount(payslipData.getEmpMaxTaxableAmount());
        data.setCompMaxTaxableAmount(payslipData.getCompMaxTaxableAmount());
        data.setCreationDate(new DateNonConvertable(new Date()));

        if (payslipData.isEnabledMultiCurrency()) {
            data.setTotalInBase(total.divide(currencyWidget.getExchangeRate(), calculationScale, RoundingMode.HALF_UP));
            data.setCurrency(currencyWidget.getCurrency());
            data.setExchangeRate(currencyWidget.getExchangeRate());
        }
        if (!payslipData.isFromEndOfService()) {
            for (int i = 0; i < paymentsTable.getRowCount(); i++) {
                PayslipItemAmountWidget salaryWidget = (PayslipItemAmountWidget) paymentsTable.getColumnById(i, "amount");
                if (salaryWidget.isSalaryObject()) {
                    data.setBasicSalary(salaryWidget.getAmount());
                    break;
                }
            }
        }
        return data;
    }

    public ArrayList<PaymentDeductionObject> getCategories(EditableTable categoriesTable, boolean isPayment) {
        final ArrayList<PaymentDeductionObject> result = new ArrayList<>();

        if (categoriesTable == null) {
            return result;
        }
        for (int i = 0; i < categoriesTable.getRowCount(); i++) {
            final CategoryLookUp categoryLookUp = (CategoryLookUp) categoriesTable.getColumnById(i, "category");

            if (categoryLookUp.getSelectedData() != null) {
                final PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) categoriesTable.getColumnById(i, "amount");
                final EditableTextBox remarks = (EditableTextBox) categoriesTable.getColumnById(i, "remarks");
                final PaymentDeductionObject object = new PaymentDeductionObject();

                object.setCategoryItem(categoryLookUp.getSelectedData());
                object.setPaymentAmount(amountWidget.getAmount());
                object.setId(amountWidget.getItemID());
                object.setSalaryObject(amountWidget.isSalaryObject());
                object.setLeaveDaysCount(amountWidget.getLeaveDaysCount());
                object.setRemarks(remarks != null ? remarks.getText() : "");
                object.setSickRequestids(amountWidget.getSickRequestIds());
                object.setLeavePaymentItem(amountWidget.getLeavePaymentItem());
                result.add(object);
            }
        }
        return result;
    }

    public PaymentDeductionObject getEmployeeExpenses() {
        final List<ExpenseData> expenseDataList = new ArrayList<>();
        ExpenseData data;

        for (int i = 0; i < expensesTable.getRowCount(); i++) {
            if (expensesTable.isItemValid(i)) {
                PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) expensesTable.getColumnById(i, "amount");
                DataListBox type = (DataListBox) expensesTable.getColumnById(i, "type");
                PaymentAccountsLookUp paidFrom = (PaymentAccountsLookUp) expensesTable.getColumnById(i, "paidfrom");
                data = (ExpenseData) amountWidget.getObject();
                data.setPaymentType(type.getSelectedId(true));
                data.setAccountID(paidFrom.getSelectedItemID());
                expenseDataList.add(data);
            }
        }
        if (expenses != null) {
            expenses.setExpenses(expenseDataList.toArray(new ExpenseData[]{}));
        }
        return expenses;
    }

}
