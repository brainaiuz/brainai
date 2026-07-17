package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.KeyValueStruct;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.EmployerSettings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 7/4/15
 * Time: 12:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class EmployerSettingsSummaryView /*extends EmployerSettingsAddEditView*/ extends CustomForm implements CustomFormConstants, Constants, Colapse {

    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private HTML officeName, officeNumber, officeReference, companyCode, wpsNo, companyName, address1, address2, country, expenseAccount;
    private HTML crNo, gosiNo, licenseNo, phoneNo, gosiExpiryDate, crExpiryDate, licenseExpiryDate, bankName, monthDays, deductionType,addPaymentType, timesheHoursDaily, leaveDaily, leaveMoney, dailyRateCalculationType, overtimeRateCalculationType, defaultStartDate;

    private KpiSwitcher showYearToDate;
    private KpiSwitcher enableTwoLevelApproval;
    private KpiSwitcher enableDoubleConfirmation;
    private KpiSwitcher disablePayrollTransactions;
    private KpiSwitcher byDefaultEmailNotification;
    private KpiSwitcher enableNonPaidLeaveRequests;
    private KpiSwitcher enablePaidLeaveRequests;
    private KpiSwitcher leaveDaysImpact;
    private KpiSwitcher nonPaidLeaveDaysImpact;
    private KpiSwitcher enabledMultiCurrency;
    private KpiSwitcher defaultAddPayTypeSwitcher;
    private FormGroup defaulAdditionalPaymentSetting;
    private FormGroup deductSettings;
    private FormGroup timsheetHoursSettings;
    private FormGroup leaveDailySettings;
    private FormGroup leaveMoneySettings;
    private TextArea2 paymentPolicy;
    private DynamicTable allowancesTable;

    public EmployerSettingsSummaryView() {
        super("employersettings", payrollStrings.employerSettings());
    }


    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        init();
        return null;
    }

    private void init() {
        officeName = new HTML();
        officeNumber = new HTML();
        officeReference = new HTML();
        companyCode = new HTML();
        wpsNo = new HTML();
        companyName = new HTML();
        address1 = new HTML();
        address2 = new HTML();
        country = new HTML();
        expenseAccount = new HTML();
        crNo = new HTML();
        gosiNo = new HTML();
        licenseNo = new HTML();
        phoneNo = new HTML();
        gosiExpiryDate = new HTML();
        crExpiryDate = new HTML();
        licenseExpiryDate = new HTML();
        bankName = new HTML();
        monthDays = new HTML();
        defaultStartDate = new HTML();
        deductionType = new HTML();
        addPaymentType = new HTML();
        timesheHoursDaily = new HTML();
        leaveDaily = new HTML();
        leaveMoney = new HTML();
        dailyRateCalculationType = new HTML();
        dailyRateCalculationType.setText(payrollStrings.byCalendar());
        overtimeRateCalculationType = new HTML();
        overtimeRateCalculationType.setText(wfmStrings.daily());
        enableNonPaidLeaveRequests = new KpiSwitcher();
        enableNonPaidLeaveRequests.setOffLabel(payrollStrings.enableLeaveRequests());
        enableNonPaidLeaveRequests.setEnabled(false);
        enablePaidLeaveRequests = new KpiSwitcher();
        enablePaidLeaveRequests.setOffLabel(payrollStrings.enablePaidLeaveRequests());
        enablePaidLeaveRequests.setEnabled(false);
        enablePaidLeaveRequests.setValue(true);
        enableTwoLevelApproval = new KpiSwitcher();
        enableTwoLevelApproval.setOffLabel(payrollStrings.enableTwoLevelApprovalForPayrun());
        enableTwoLevelApproval.setEnabled(false);
        showYearToDate = new KpiSwitcher();
        showYearToDate.setEnabled(false);
        enableDoubleConfirmation = new KpiSwitcher();
        enableDoubleConfirmation.setEnabled(false);
        byDefaultEmailNotification = new KpiSwitcher();
        byDefaultEmailNotification.setEnabled(false);
        defaultAddPayTypeSwitcher = new KpiSwitcher();
        defaultAddPayTypeSwitcher.setEnabled(false);
        enabledMultiCurrency = new KpiSwitcher();
        enabledMultiCurrency.setEnabled(false);
        disablePayrollTransactions = new KpiSwitcher();
        disablePayrollTransactions.setEnabled(false);
        leaveDaysImpact = new KpiSwitcher();
        leaveDaysImpact.setEnabled(false);
        leaveDaysImpact.setValue(true);
        nonPaidLeaveDaysImpact = new KpiSwitcher();
        nonPaidLeaveDaysImpact.setEnabled(false);
        nonPaidLeaveDaysImpact.setValue(true);
        allowancesTable = new DynamicTable(getAllowanceTableColumns());
        allowancesTable.setBorderWidth(0);
        allowancesTable.setVisible(false);

        deductSettings = new FormGroup(payrollStrings.deductionType(), deductionType);
        deductSettings.setVisible(false);

        defaulAdditionalPaymentSetting = new FormGroup(payrollStrings.defaultAdditionalPayment(),addPaymentType);
        defaulAdditionalPaymentSetting.setVisible(false);
        timsheetHoursSettings = new FormGroup(payrollStrings.timesheerHoursCalcuationSettings(), timesheHoursDaily);
        leaveDailySettings = new FormGroup(payrollStrings.leaveEncashmentDailyTypeSettings(), leaveDaily);
        leaveMoneySettings = new FormGroup(payrollStrings.leaveEncashmentMoneyTypeSettings(), leaveMoney);

        paymentPolicy = new TextArea2(1000);
        paymentPolicy.setEnabled(false);

        addTitleField(EMPLOYER_SETTINGS.EMPLOYER_PAYROLL_SETTINGS, payrollStrings.employerSettings());
        addTitleField(EMPLOYER_SETTINGS.PAYRUN_SETTINGS, payrollStrings.payrunSettings());
        addField(EMPLOYER_SETTINGS.OFFICE_NUMBER, officeNumber, wfmStrings.officeNumber());
        addField(EMPLOYER_SETTINGS.OFFICE_NAME, officeName, payrollStrings.officeName());
        addField(EMPLOYER_SETTINGS.OFFICE_REFERENCE, officeReference, accountingStrings.referenceNumber());
        addField(EMPLOYER_SETTINGS.COMPANY_CODE, companyCode, payrollStrings.companyCode());
        addField(WPS_NUMBER, wpsNo, (!"".equals(Utils.getPersonalID()) ? Utils.getPersonalID() : wfmStrings.wpsNumber()));
        addField(EMPLOYER_SETTINGS.COMPANY_COUNTRY, country, wfmStrings.country());
        addField(EMPLOYER_SETTINGS.ADDRESS1, address1, payrollStrings.address1());
        addField(EMPLOYER_SETTINGS.ADDRESS2, address2, wfmStrings.address2());
        addField(EMPLOYER_SETTINGS.CR_NUMBER, crNo, payrollStrings.crNo());
        addField(EMPLOYER_SETTINGS.BANK_ACCOUNT, bankName, Property.get(Constants.BANKACCOUNT, wfmStrings.bankAccountDetail(), wfmStrings.bankAccount()));
        addField(EMPLOYER_SETTINGS.GOSI_NUMBER, gosiNo, payrollStrings.gosiNo());
        addField(EMPLOYER_SETTINGS.LICENSE_NUMBER, licenseNo, wfmStrings.licenseNo());
        addField(EMPLOYER_SETTINGS.PHONE_NUMBER, phoneNo, payrollStrings.phoneNo());
        addField(EMPLOYER_SETTINGS.GOSI_DATE, gosiExpiryDate, payrollStrings.gosiExpiryDate());
        addField(EMPLOYER_SETTINGS.CR_DATE, crExpiryDate, payrollStrings.crExpiryDate());
        addField(EMPLOYER_SETTINGS.LICENSE_DATE, licenseExpiryDate, payrollStrings.licenseExpiryDate());
        addField(EMPLOYER_SETTINGS.EXPENSE_ACCOUNT, expenseAccount, wfmStrings.pleaseSelectAccount());
        addField(EMPLOYER_SETTINGS.LEAVE_DEDUCTIONS, enableNonPaidLeaveRequests, payrollStrings.enableLeaveRequests());
        addField(EMPLOYER_SETTINGS.LEAVE_SETTINGS, deductSettings);
        addField(EMPLOYER_SETTINGS.DEFAULT_ADDITIONAL_TYPE_SETTINGS, defaulAdditionalPaymentSetting);
        addField(EMPLOYER_SETTINGS.DEFAULT_ADDITIONAL_TYPE, defaultAddPayTypeSwitcher, payrollStrings.defaultAdditionalPayment());
        addField(EMPLOYER_SETTINGS.LEAVE_PAYMENTS, enablePaidLeaveRequests, payrollStrings.enablePaidLeaveRequests());
        addField(EMPLOYER_SETTINGS.TIMESHEET_HOURS_CALCULATION_SETTINGS, timsheetHoursSettings);
        addField(EMPLOYER_SETTINGS.LEAVE_PAYMENTS_DAILY_SETTINGS, leaveDailySettings);
        addField(EMPLOYER_SETTINGS.LEAVE_PAYMENTS_MONEY_SETTINGS, leaveMoneySettings);
        addField(EMPLOYER_SETTINGS.PAYMENT_POLICY, paymentPolicy);
        addField(EMPLOYER_SETTINGS.SHOW_YEAR_TO_DATE, showYearToDate, payrollStrings.showYearToDate());
        addField(EMPLOYER_SETTINGS.NUMBER_OF_WORK_DAYS, monthDays, payrollStrings.numberOfWorkDaysInMonth());
        addField(EMPLOYER_SETTINGS.DEFAULT_START_DATE, defaultStartDate, payrollStrings.defaultStartDate());
        addField(EMPLOYER_SETTINGS.DOUBLE_CONFIRMATION, enableDoubleConfirmation, payrollStrings.enableDoubleConfirmation());
        addField(EMPLOYER_SETTINGS.DAILY_RATE_CALCULATION, dailyRateCalculationType, payrollStrings.dailyRateCalculation());
        addField(EMPLOYER_SETTINGS.OVERTIME_CALCULATION, overtimeRateCalculationType, payrollStrings.overtimeCalculation());
        addField(EMPLOYER_SETTINGS.DISABLE_PAYROLL_TRANSACTIONS, disablePayrollTransactions, payrollStrings.dontRecordPayrollTransactions());
        addField(EMPLOYER_SETTINGS.BY_DEFAULT_EMAIL_NOTIFICATION, byDefaultEmailNotification, payrollStrings.byDefaultEmailNotification());
        addField(EMPLOYER_SETTINGS.PAYROLL_MULTI_CURRENCY, enabledMultiCurrency, payrollStrings.enableMultiCurrencyForPayroll());
        addField(EMPLOYER_SETTINGS.LEAVE_DAYS_IMPACT, leaveDaysImpact, payrollStrings.paidLeaveDaysImpact());
        addField(EMPLOYER_SETTINGS.NON_PAID_LEAVE_DAYS_IMPACT, nonPaidLeaveDaysImpact, payrollStrings.nonPaidLeaveDaysImpact());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYER_SETTINGS_UPDATE, EmployerSettingsSummaryView.this, (sender, args) -> show());

        show();
    }


    @Override
    protected void addButtons() {
        addEditButton().addClickHandler(event -> goTo("employersettings|add/add"));
    }

    private DynamicTableColumn[] getAllowanceTableColumns() {
        DynamicTableColumn[] columns = new DynamicTableColumn[1];
        columns[0] = new DynamicTableColumn(wfmStrings.allowance(), "allowance", 200);
        return columns;
    }

    private Widget[] getWidgets(PaymentDeductionSelectItem item) {
        Widget[] widgets = new Widget[1];
        Label allowance = new Label();
        if (item != null) {
            allowance.setText(item.getName());
        }
        widgets[0] = allowance;
        return widgets;
    }

    @Override
    protected void getDataToFillFields() {
        PayrollService.App.get().getCompanyPayrollSettings(new AbstractAsyncCallback<EmployerSettings>() {
            @Override
            public void failure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void success(EmployerSettings settings) {
                if (settings != null) {
                    setValues(settings);
                } else {
                    closeTab();
                    goTo("employersettings/add|add");
                }
            }
        });
    }

    private void setValues(EmployerSettings settings) {
        for (final KeyValueStruct setting : settings.getSettings()) {
            if (OFFICE_NUMBER.equals(setting.getKey())) {
                officeNumber.setText(setting.getValue());
            } else if (PAYE_REF_NUMBER.equals(setting.getKey())) {
                officeReference.setText(setting.getValue());
            } else if (OFFICE_NAME.equals(setting.getKey())) {
                officeName.setText(setting.getValue());
            } else if (COMPANY_CODE.equals(setting.getKey())) {
                companyCode.setText(setting.getValue());
            } else if (WPS_NO.equals(setting.getKey())) {
                wpsNo.setText(setting.getValue());
            } else if (COMPANY_NAME.equals(setting.getKey())) {
                companyName.setText(setting.getValue());
            } else if (ADDRESS1.equals(setting.getKey())) {
                address1.setText(setting.getValue());
            } else if (ADDRESS2.equals(setting.getKey())) {
                address2.setText(setting.getValue());
            } else if (CR_NO.equals(setting.getKey())) {
                crNo.setText(setting.getValue());
            } else if (GOSI_NO.equals(setting.getKey())) {
                gosiNo.setText(setting.getValue());
            } else if (LICENSE_NO.equals(setting.getKey())) {
                licenseNo.setText(setting.getValue());
            } else if (PHONE_NO.equals(setting.getKey())) {
                phoneNo.setText(setting.getValue());
            } else if (GOSI_EXPIRY_DATE.equals(setting.getKey()) && !"".equals(setting.getValue())) {
                try {
                    gosiExpiryDate.setText(DateUtils.format(DateUtils.fullDateFormat.parse(setting.getValue())));
                } catch (Exception e) {
                    gosiExpiryDate.setText(setting.getValue());
                }
            } else if (CR_EXPIRY_DATE.equals(setting.getKey()) && !"".equals(setting.getValue())) {
                try {
                    crExpiryDate.setText(DateUtils.format(DateUtils.fullDateFormat.parse(setting.getValue())));
                } catch (IllegalArgumentException e) {
                    crExpiryDate.setText(setting.getValue());
                }
            } else if (LICENSE_EXPIRY_DATE.equals(setting.getKey()) && !"".equals(setting.getValue())) {
                try {
                    licenseExpiryDate.setText(DateUtils.format(DateUtils.fullDateFormat.parse(setting.getValue())));
                } catch (Exception e) {
                    licenseExpiryDate.setText(setting.getValue());
                }
            } else if (COUNTRY_NAME.equals(setting.getKey())) {
                if (setting.getValue() != null && !"".equals(setting.getValue())) {
                    country.setText(setting.getValue());
                }
            } else if (BANK_ACCOUNT_NAME.equals(setting.getKey())) {
                if (setting.getValue() != null && !"".equals(setting.getValue())) {
                    bankName.setText(setting.getValue());
                }
            } else if (PAYMENT_POLICY.equals(setting.getKey())) {
                if (setting.getValue() != null && !"".equals(setting.getValue())) {
                    paymentPolicy.setText(setting.getValue());
                }
            } else if (EXPENSE_PAID_ACCOUNT_NAME.equals(setting.getKey())) {
                if (setting.getValue() != null && !"".equals(setting.getValue())) {
                    expenseAccount.setText(setting.getValue());
                }
            } else if (ENABLED_DOUBLE_APPROVER_PAYRUN.equals(setting.getKey())) {
                enableTwoLevelApproval.setValue(setting.getValue() != null && "true".equals(setting.getValue()));
            } else if (SHOW_YEAR_TO_DATE.equals(setting.getKey())) {
                showYearToDate.setValue(setting.getValue() != null && "true".equals(setting.getValue()));
            } else if (ENABLED_LEAVE_DEDUCTIONS.equals(setting.getKey())) {
                enableNonPaidLeaveRequests.setValue(setting.getValue() != null && "true".equals(setting.getValue()));
                deductSettings.setVisible(enableNonPaidLeaveRequests.getValue());
            } else if (EMPLOYER_SETTINGS.DEFAULT_ADDITIONAL_TYPE.equals(setting.getKey())){
                boolean isVisible = setting.getValue() != null && "true".equals(setting.getValue());
                defaultAddPayTypeSwitcher.setValue(isVisible);
                defaulAdditionalPaymentSetting.setVisible(isVisible);
            }else if (ENABLED_LEAVE_PAYMENTS.equals(setting.getKey())) {
                enablePaidLeaveRequests.setValue(setting.getValue() != null && "true".equals(setting.getValue()));
                timsheetHoursSettings.setVisible(enablePaidLeaveRequests.getValue());
                leaveDailySettings.setVisible(enablePaidLeaveRequests.getValue());
                leaveMoneySettings.setVisible(enablePaidLeaveRequests.getValue());
            } else if (NUMBER_OF_WORK_DAYS.equals(setting.getKey())) {
                if (setting.getValue() != null && !"".equals(setting.getValue())) {
                    monthDays.setText(setting.getValue());
                }
            } else if (DEDUCT_TYPE.equals(setting.getKey())) {
                if (setting.getValue() != null && !"".equals(setting.getValue())) {
                    if (Integer.parseInt(setting.getValue()) == 0) {
                        deductionType.setText(wfmStrings.basicSalary());
                    } else if (Integer.parseInt(setting.getValue()) == 1) {
                        deductionType.setText(wfmStrings.basicAllowancePay());
                    } else {
                        deductionType.setText(payrollStrings.deductFromNetPay());
                    }
                }
            } else if(EMPLOYER_SETTINGS.DEFAULT_ADDITIONAL_TYPE_SETTINGS.equals(setting.getKey())){
                if (setting.getValue() != null && !"".equals(setting.getValue())) {
                    if (Integer.parseInt(setting.getValue()) == 0) {
                        addPaymentType.setText(wfmStrings.basicSalary());
                    } else if (Integer.parseInt(setting.getValue()) == 1) {
                        addPaymentType.setText(wfmStrings.basicAllowancePay());
                    } else {
                        addPaymentType.setText(wfmStrings.fixedAmount());
                    }
                }
            }else if (TIMESHEET_HOURS_CALCUTATION_TYPE.equals(setting.getKey())) {
                if (setting.getValue() != null && !"".equals(setting.getValue())) {
                    if (Integer.parseInt(setting.getValue()) == 0) {
                        timesheHoursDaily.setText(wfmStrings.basicSalary());
                    } else if (Integer.parseInt(setting.getValue()) == 1) {
                        timesheHoursDaily.setText(wfmStrings.basicAllowancePay());
                    } else {
                        timesheHoursDaily.setText(payrollStrings.deductFromNetPay());
                    }
                }
            } else if (LEAVE_DAILY_PAYMENT_TYPE.equals(setting.getKey())) {
                if (setting.getValue() != null && !"".equals(setting.getValue())) {
                    if (Integer.parseInt(setting.getValue()) == 0) {
                        leaveDaily.setText(wfmStrings.basicSalary());
                    } else if (Integer.parseInt(setting.getValue()) == 1) {
                        leaveDaily.setText(wfmStrings.basicAllowancePay());
                    } else {
                        leaveDaily.setText(payrollStrings.deductFromNetPay());
                    }
                }
            } else if (LEAVE_MONEY_PAYMENT_TYPE.equals(setting.getKey())) {
                if (setting.getValue() != null && !"".equals(setting.getValue())) {
                    if (Integer.parseInt(setting.getValue()) == 0) {
                        leaveMoney.setText(wfmStrings.basicSalary());
                    } else if (Integer.parseInt(setting.getValue()) == 1) {
                        leaveMoney.setText(wfmStrings.basicAllowancePay());
                    } else {
                        leaveMoney.setText(payrollStrings.deductFromNetPay());
                    }
                }
            } else if (DOUBLE_CONFIRMATION.equals(setting.getKey())) {
                enableDoubleConfirmation.setValue(setting.getValue() != null && "true".equals(setting.getValue()));
            } else if (DAILY_RATE_BY_EMPLOYER_SETTINGS.equals(setting.getKey())) {
                if (setting.getValue() != null && ("true".equals(setting.getValue()) || "BY_STATIC_DAY".equals(setting.getValue()))) {
                    dailyRateCalculationType.setText(payrollStrings.byEmployerSettings());
                } else if (setting.getValue() != null && "BY_TIMESLOT".equals(setting.getValue())) {
                    dailyRateCalculationType.setText(payrollStrings.byTimeslotSettings());
                } else {
                    dailyRateCalculationType.setText(payrollStrings.byCalendar());
                }
            } else if (DISABLE_PAYROLL_TRANSACTIONS.equals(setting.getKey())) {
                disablePayrollTransactions.setValue(setting.getValue() != null && "true".equals(setting.getValue()));
            } else if (BY_DEFAULT_EMAIL_NOTIFICATION.equals(setting.getKey())) {
                byDefaultEmailNotification.setValue(setting.getValue() != null && "true".equals(setting.getValue()));
            } else if (EMPLOYER_SETTINGS.DEFAULT_ADDITIONAL_TYPE.equals(setting.getKey())) {
                defaultAddPayTypeSwitcher.setValue(setting.getValue() != null && "true".equals(setting.getValue()));
            } else if (LEAVE_DAYS_IMPACT.equals(setting.getKey())) {
                leaveDaysImpact.setValue(setting.getValue() == null || "true".equals(setting.getValue()));
            } else if (NON_PAID_LEAVE_DAYS_IMPACT.equals(setting.getKey())) {
                nonPaidLeaveDaysImpact.setValue(setting.getValue() == null || "true".equals(setting.getValue()));
            } else if (MULTI_CURRENCY_FOR_PAYROLL.equals(setting.getKey())) {
                enabledMultiCurrency.setValue(setting.getValue() != null && "true".equals(setting.getValue()));
            } else if (OVERTIME_RATE_BY_EMPLOYER_SETTINGS.equals(setting.getKey())) {
                if (setting.getValue() != null && "MONTHLY".equals(setting.getValue())) {
                    overtimeRateCalculationType.setText(wfmStrings.monthly());
                } else if (setting.getValue() != null && "WEEKLY".equals(setting.getValue())) {
                    overtimeRateCalculationType.setText(wfmStrings.weekly());
                } else {
                    overtimeRateCalculationType.setText(wfmStrings.daily());
                }
            } else if (DEFAULT_START_DATE.equals(setting.getKey())) {
                if (setting.getValue() != null && !"".equals(setting.getValue())) {
                    defaultStartDate.setText(setting.getValue());
                }
            }
        }
        if (monthDays.getText() == null || "".equals(monthDays.getText())) {
            monthDays.setText(String.valueOf(DEFAULT_NUMBER_OF_WORK_DAYS));
        }

        if (defaultStartDate.getText() == null || "".equals(defaultStartDate.getText())) {
            defaultStartDate.setText(String.valueOf(DEFAULT_START_DATE_VALUE));
        }
        if (settings.getAllowances(DEDUCT_ALLOWANCES) != null && settings.getAllowances(DEDUCT_ALLOWANCES).size() > 0) {
            allowancesTable.clear();
            allowancesTable.setVisible(true);
            for (PaymentDeductionSelectItem allowance : settings.getAllowances(DEDUCT_ALLOWANCES)) {
                allowancesTable.addRow(getWidgets(allowance));
            }
        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PAYROLL_COMPANY_SETTINGS_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return "payroll settings";
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
