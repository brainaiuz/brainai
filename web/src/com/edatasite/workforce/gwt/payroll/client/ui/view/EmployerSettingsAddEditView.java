package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.KeyValueStruct;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.EmployerSettings;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.columnGrid.RowColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.AddListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.VerticalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.CategoryLookUp;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 7/2/15
 * Time: 11:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmployerSettingsAddEditView extends CustomForm implements CustomFormConstants, Constants, Colapse {

    protected static final PayrollStrings payrollStrings = PayrollStrings.App.get();
    protected static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final Integer DEFAULT_MONTH_DAY = 30;

    private TextBox officeName, officeNumber, officeReference, companyCode, wpsNo, companyName, address1, address2, crNo, gosiNo, licenseNo, phoneNo, monthDays, defaultStartDate;
    private DatePicker gosiExpiryDate, crExpiryDate, licenseExpiryDate;
    private DataListBox bankListBox, country, expenseBankAccount;
    private TextArea2 paymentPolicy;
    private KpiSwitcher enableTwoLevelApproval, enableDoubleConfirmation, byDefaultEmailNotification, enableNonPaidLeaveRequests, enablePaidLeaveRequests, defaultAddPayTypeSwitcher;
    private KpiSwitcher disablePayrollTransactions, paidLeaveDaysImpact, nonPaidLeaveDaysImpact, payrollMultiCurrency, showYearToDate;
    private CustomFlexTable settingsTable, leaveDailyType, timesheetHoursCalcutationType, leaveMoneyType, defaultAddPayType;
    private CategoryLookUp leaveMoneyTypeCategory;
    private FormGroup leaveDailyRC, timesheetHoursRC, leaveMoneyRC, leaveMoneyCatRC;
    private RadioButton byCalendar, byEmployerSettings, byTimeslotSettings, dailyOvertime, weeklyOvertime, monthlyOvertime;

    public EmployerSettingsAddEditView() {
        super("addEmployerSettings", payrollStrings.employerSettings());
    }

    /*public EmployerSettingsAddEditView(String name, String desc) {
        super(name, desc);
    }*/

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        init();
        return null;
    }

    private void init() {
        officeName = new TextBox();
        officeName.addStyleName(DEFAULT_WIDTH);
        officeNumber = new TextBox();
        officeNumber.addStyleName(DEFAULT_WIDTH);
        officeReference = new TextBox();
        officeReference.addStyleName(DEFAULT_WIDTH);
        companyCode = new TextBox();
        companyCode.addStyleName(DEFAULT_WIDTH);
        wpsNo = new TextBox();
        wpsNo.addStyleName(DEFAULT_WIDTH);
        companyName = new TextBox();
        companyName.addStyleName(DEFAULT_WIDTH);
        address1 = new TextBox();
        address1.addStyleName(DEFAULT_WIDTH);
        address2 = new TextBox();
        address2.addStyleName(DEFAULT_WIDTH);
        crNo = new TextBox();
        crNo.addStyleName(DEFAULT_WIDTH);
        gosiNo = new TextBox();
        gosiNo.addStyleName(DEFAULT_WIDTH);
        licenseNo = new TextBox();
        licenseNo.addStyleName(DEFAULT_WIDTH);
        phoneNo = new TextBox();
        phoneNo.addStyleName(DEFAULT_WIDTH);
        gosiExpiryDate = new DatePicker();
        gosiExpiryDate.addStyleName(DEFAULT_WIDTH);
        crExpiryDate = new DatePicker();
        crExpiryDate.addStyleName(DEFAULT_WIDTH);
        licenseExpiryDate = new DatePicker();
        licenseExpiryDate.addStyleName(DEFAULT_WIDTH);
        country = new DataListBox();
        country.addStyleName(DEFAULT_WIDTH);
        bankListBox = new DataListBox();
        bankListBox.addStyleName(DEFAULT_WIDTH);
        expenseBankAccount = new DataListBox();
        expenseBankAccount.addStyleName(DEFAULT_WIDTH);

        enableNonPaidLeaveRequests = new KpiSwitcher();
        enableNonPaidLeaveRequests.setOffLabel(payrollStrings.enableLeaveRequests());
        enableNonPaidLeaveRequests.addValueChangeHandler(valueChangeEvent -> settingsTable.setVisible(valueChangeEvent.getValue()));

        enablePaidLeaveRequests = new KpiSwitcher();
        enablePaidLeaveRequests.setOffLabel(payrollStrings.enablePaidLeaveRequests());
        enablePaidLeaveRequests.setValue(true);
        enablePaidLeaveRequests.addValueChangeHandler(valueChangeEvent -> {
            timesheetHoursRC.setVisible(valueChangeEvent.getValue());
            leaveDailyRC.setVisible(valueChangeEvent.getValue());
            leaveMoneyRC.setVisible(valueChangeEvent.getValue());
            leaveMoneyCatRC.setVisible(valueChangeEvent.getValue());
        });

        defaultAddPayTypeSwitcher = new KpiSwitcher();
        defaultAddPayTypeSwitcher.setOffLabel(payrollStrings.defaultAdditionalPayment());
        defaultAddPayTypeSwitcher.setValue(false);
        defaultAddPayTypeSwitcher.addValueChangeHandler(event ->{
           defaultAddPayType.setVisible(event.getValue());
        });

        disablePayrollTransactions = new KpiSwitcher();

        paidLeaveDaysImpact = new KpiSwitcher();
        paidLeaveDaysImpact.setValue(true);

        nonPaidLeaveDaysImpact = new KpiSwitcher();
        nonPaidLeaveDaysImpact.setValue(true);

        enableTwoLevelApproval = new KpiSwitcher();
        enableTwoLevelApproval.setOffLabel(payrollStrings.enableTwoLevelApprovalForPayrun());
        enableDoubleConfirmation = new KpiSwitcher();
        byDefaultEmailNotification = new KpiSwitcher();
        payrollMultiCurrency = new KpiSwitcher();
        showYearToDate = new KpiSwitcher();
        monthDays = new TextBox();
        monthDays.addStyleName(DEFAULT_WIDTH);
        monthDays.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        monthDays.setText(String.valueOf(DEFAULT_MONTH_DAY));

        defaultStartDate = new TextBox();
        defaultStartDate.addStyleName(DEFAULT_WIDTH);
        defaultStartDate.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        defaultStartDate.setText(String.valueOf(DEFAULT_START_DATE_VALUE));

        settingsTable = new CustomFlexTable(payrollStrings.deductionType(), true);
        timesheetHoursCalcutationType = new CustomFlexTable(true, wfmStrings.paymentType());
        leaveDailyType = new CustomFlexTable(wfmStrings.paymentType());
        leaveMoneyType = new CustomFlexTable(wfmStrings.paymentType());
        timesheetHoursCalcutationType.setVisible(true);
        leaveDailyType.setVisible(true);
        leaveMoneyType.setVisible(true);

        leaveMoneyTypeCategory = new CategoryLookUp(PayrollConstants.CATEGORY_PAYMENT);
        leaveMoneyTypeCategory.addStyleName(DEFAULT_WIDTH);

        defaultAddPayType = new CustomFlexTable(payrollStrings.defaultAdditionalPayment(), false, true);

        paymentPolicy = new TextArea2(1000);
        paymentPolicy.setPlaceHolder(payrollStrings.hereYouCanAddYourPaymentPolicy());

        byCalendar = new KpiRadioButton("dailyRateCalculationType", payrollStrings.byCalendar());
        byEmployerSettings = new KpiRadioButton("dailyRateCalculationType", payrollStrings.byEmployerSettings());
        byTimeslotSettings = new KpiRadioButton("dailyRateCalculationType", payrollStrings.byTimeslotSettings());

        byCalendar.setValue(true);
        monthDays.setVisible(!(byCalendar.getValue() || byTimeslotSettings.getValue()));

        HorizontalPanel dailyRateCalculationTypePanel = new HorizontalPanel();
        dailyRateCalculationTypePanel.setSpacing(10);
        dailyRateCalculationTypePanel.add(byCalendar);
        dailyRateCalculationTypePanel.add(byEmployerSettings);
        dailyRateCalculationTypePanel.add(byTimeslotSettings);
        byCalendar.addValueChangeHandler(valueChangeEvent -> {
            monthDays.setVisible(!(byCalendar.getValue() || byTimeslotSettings.getValue()));
        });

        byEmployerSettings.addValueChangeHandler(valueChangeEvent -> {
            monthDays.setVisible(!(byCalendar.getValue() || byTimeslotSettings.getValue()));
        });

        byTimeslotSettings.addValueChangeHandler(valueChangeEvent -> {
            monthDays.setVisible(!(byCalendar.getValue() || byTimeslotSettings.getValue()));
        });


        dailyOvertime = new KpiRadioButton("overtimeCalculationType", wfmStrings.daily());
        weeklyOvertime = new KpiRadioButton("overtimeCalculationType", wfmStrings.weekly());
        monthlyOvertime = new KpiRadioButton("overtimeCalculationType", wfmStrings.monthly());

        dailyOvertime.setValue(true);

        HorizontalPanel overtimeCalculationTypePanel = new HorizontalPanel();
        overtimeCalculationTypePanel.setSpacing(10);
        overtimeCalculationTypePanel.add(dailyOvertime);
        //for some time disable weekly option
//        overtimeCalculationTypePanel.add(weeklyOvertime);
        overtimeCalculationTypePanel.add(monthlyOvertime);

        timesheetHoursRC = new FormGroup(payrollStrings.timesheerHoursCalcuationSettings(), timesheetHoursCalcutationType);
        leaveDailyRC = new FormGroup(payrollStrings.leaveEncashmentDailyTypeSettings(), leaveDailyType);
        leaveMoneyRC = new FormGroup(payrollStrings.leaveEncashmentMoneyTypeSettings(), leaveMoneyType);
        leaveMoneyCatRC = new FormGroup(payrollStrings.leaveMoneyTypeCategory(), leaveMoneyTypeCategory);

        addTitleField(EMPLOYER_SETTINGS.EMPLOYER_PAYROLL_SETTINGS, payrollStrings.employerSettings());
        addTitleField(EMPLOYER_SETTINGS.PAYRUN_SETTINGS, payrollStrings.payrunSettings());
        addField(EMPLOYER_SETTINGS.OFFICE_NAME, officeName, payrollStrings.officeName());
        addField(EMPLOYER_SETTINGS.OFFICE_NUMBER, officeNumber, wfmStrings.officeNumber());
        addField(EMPLOYER_SETTINGS.OFFICE_REFERENCE, officeReference, accountingStrings.referenceNumber());
        addField(EMPLOYER_SETTINGS.COMPANY_CODE, companyCode, payrollStrings.companyCode());
        addField(WPS_NUMBER, wpsNo, (!"".equals(Utils.getPersonalID()) ? Utils.getPersonalID() : wfmStrings.wpsNumber()));
        addField(EMPLOYER_SETTINGS.COMPANY_COUNTRY, country, wfmStrings.country());
        addField(EMPLOYER_SETTINGS.ADDRESS1, address1, payrollStrings.address1());
        addField(EMPLOYER_SETTINGS.ADDRESS2, address2, wfmStrings.address2());
        addField(EMPLOYER_SETTINGS.CR_NUMBER, crNo, payrollStrings.crNo());
        addField(EMPLOYER_SETTINGS.BANK_ACCOUNT, bankListBox, Property.get(Constants.BANKACCOUNT, wfmStrings.bankAccountDetail(), wfmStrings.bankAccount()));
        addField(EMPLOYER_SETTINGS.GOSI_NUMBER, gosiNo, payrollStrings.gosiNo());
        addField(EMPLOYER_SETTINGS.LICENSE_NUMBER, licenseNo, wfmStrings.licenseNo());
        addField(EMPLOYER_SETTINGS.PHONE_NUMBER, phoneNo, payrollStrings.phoneNo());
        addField(EMPLOYER_SETTINGS.GOSI_DATE, gosiExpiryDate, payrollStrings.gosiExpiryDate());
        addField(EMPLOYER_SETTINGS.CR_DATE, crExpiryDate, payrollStrings.crExpiryDate());
        addField(EMPLOYER_SETTINGS.LICENSE_DATE, licenseExpiryDate, payrollStrings.licenseExpiryDate());
        addField(EMPLOYER_SETTINGS.EXPENSE_ACCOUNT, expenseBankAccount, wfmStrings.pleaseSelectAccount());
        addField(EMPLOYER_SETTINGS.SHOW_YEAR_TO_DATE, showYearToDate, payrollStrings.showYearToDate());
        addField(EMPLOYER_SETTINGS.NUMBER_OF_WORK_DAYS, monthDays, payrollStrings.numberOfWorkDaysInMonth());
        addField(EMPLOYER_SETTINGS.DEFAULT_START_DATE, defaultStartDate, payrollStrings.defaultStartDate());
        addField(EMPLOYER_SETTINGS.LEAVE_DEDUCTIONS, enableNonPaidLeaveRequests, payrollStrings.enableLeaveRequests());
        addField(EMPLOYER_SETTINGS.LEAVE_PAYMENTS, enablePaidLeaveRequests, payrollStrings.enablePaidLeaveRequests());
        addField(EMPLOYER_SETTINGS.DISABLE_PAYROLL_TRANSACTIONS, disablePayrollTransactions, payrollStrings.dontRecordPayrollTransactions());
        addField(EMPLOYER_SETTINGS.LEAVE_SETTINGS, settingsTable);
        addField(EMPLOYER_SETTINGS.TIMESHEET_HOURS_CALCULATION_SETTINGS, timesheetHoursRC);
        addField(EMPLOYER_SETTINGS.LEAVE_PAYMENTS_DAILY_SETTINGS, leaveDailyRC);
        addField(EMPLOYER_SETTINGS.LEAVE_PAYMENTS_MONEY_SETTINGS, leaveMoneyRC);
        addField(EMPLOYER_SETTINGS.LEAVE_PAYMENTS_MONEY_TYPE, leaveMoneyCatRC);
        addField(EMPLOYER_SETTINGS.PAYMENT_POLICY, paymentPolicy);
        addField(EMPLOYER_SETTINGS.DOUBLE_CONFIRMATION, enableDoubleConfirmation, payrollStrings.enableDoubleConfirmation());
        addField(EMPLOYER_SETTINGS.DAILY_RATE_CALCULATION, dailyRateCalculationTypePanel, payrollStrings.dailyRateCalculation());
        addField(EMPLOYER_SETTINGS.OVERTIME_CALCULATION, overtimeCalculationTypePanel, payrollStrings.overtimeCalculation());
        addField(EMPLOYER_SETTINGS.BY_DEFAULT_EMAIL_NOTIFICATION, byDefaultEmailNotification, payrollStrings.byDefaultEmailNotification());
        addField(EMPLOYER_SETTINGS.PAYROLL_MULTI_CURRENCY, payrollMultiCurrency, payrollStrings.enableMultiCurrencyForPayroll());
        addField(EMPLOYER_SETTINGS.LEAVE_DAYS_IMPACT, paidLeaveDaysImpact, payrollStrings.paidLeaveDaysImpact());
        addField(EMPLOYER_SETTINGS.NON_PAID_LEAVE_DAYS_IMPACT, nonPaidLeaveDaysImpact, payrollStrings.nonPaidLeaveDaysImpact());
        addField(EMPLOYER_SETTINGS.DEFAULT_ADDITIONAL_TYPE, defaultAddPayTypeSwitcher, payrollStrings.defaultAdditionalPayment());
        addField(EMPLOYER_SETTINGS.DEFAULT_ADDITIONAL_TYPE_SETTINGS, defaultAddPayType);

        setExpenseBankAccounts(null);
        setCountry(null);
        setBankAccounts(null);

        show();
    }

    private void setCountry(final Integer countryId) {
        if (country.getItems() != null) {
            if (countryId != null)
                country.setSelected(countryId);
        } else {
            EmployeeService.App.get().getCountries(new AbstractAsyncCallback<SelectItem[]>() {
                public void failure(Throwable caught) {

                }

                public void success(SelectItem[] result) {
                    country.setItems(result);
                    if (countryId != null)
                        country.setSelected(countryId);
                }
            });
        }
    }

    private void setBankAccounts(final Integer bankAccountID) {
        if (bankListBox.getItems() != null) {
            if (bankAccountID != null) {
                bankListBox.setSelected(bankAccountID);
            }
        } else {
            AccountingService.App.get().getBankAccountItems(new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable throwable) {
                }

                @Override
                public void success(SelectItem[] result) {
                    bankListBox.setItems(result);
                    if (bankAccountID != null) {
                        bankListBox.setSelected(bankAccountID);
                    }
                }
            });
        }
    }

    private void setExpenseBankAccounts(final Integer expenseBankAccounID) {
        if (expenseBankAccount.getItems() != null) {
            if (expenseBankAccounID != null) {
                expenseBankAccount.setSelected(expenseBankAccounID);
            }
        } else {
            AllInOneService.App.get().getAccountsForPayment(new ListingFilterParameter(), new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable throwable) {
                }

                @Override
                public void success(SelectItem[] result) {
                    expenseBankAccount.setItems(result);
                    if (expenseBankAccounID != null) {
                        expenseBankAccount.setSelected(expenseBankAccounID);
                    }
                }
            });
        }
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.update(), BTN_PRIMARY, clickEvent -> save());
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
                gosiExpiryDate.setDate(DateUtils.fullDateFormat.parse(setting.getValue()));
            } else if (CR_EXPIRY_DATE.equals(setting.getKey()) && !"".equals(setting.getValue())) {
                crExpiryDate.setDate(DateUtils.fullDateFormat.parse(setting.getValue()));
            } else if (LICENSE_EXPIRY_DATE.equals(setting.getKey()) && !"".equals(setting.getValue())) {
                licenseExpiryDate.setDate(DateUtils.dateAndTimeFormatFull.parse(setting.getValue()));
            } else if (COUNTRY_ID.equals(setting.getKey())) {
                if (setting.getValue() != null && !"".equals(setting.getValue())) {
                    setCountry(Integer.parseInt(setting.getValue()));
                }
            } else if (BANK_ACCOUNT_ID.equals(setting.getKey())) {
                if (setting.getValue() != null && !"".equals(setting.getValue())) {
                    setBankAccounts(Integer.parseInt(setting.getValue()));
                }
            } else if (PAYMENT_POLICY.equals(setting.getKey())) {
                if (setting.getValue() != null && !"".equals(setting.getValue())) {
                    paymentPolicy.setText(setting.getValue());
                }
            } else if (EXPENSE_PAID_ACCOUNT.equals(setting.getKey())) {
                if (setting.getValue() != null && !"".equals(setting.getValue())) {
                    setExpenseBankAccounts(Integer.valueOf(setting.getValue()));
                }
            } else if (ENABLED_DOUBLE_APPROVER_PAYRUN.equals(setting.getKey())) {
                enableTwoLevelApproval.setValue(setting.getValue() != null && "true".equals(setting.getValue()));
            } else if (ENABLED_LEAVE_DEDUCTIONS.equals(setting.getKey())) {
                enableNonPaidLeaveRequests.setValue(setting.getValue() != null && "true".equals(setting.getValue()));
                settingsTable.setVisible(enableNonPaidLeaveRequests.getValue());
            } else if (ENABLED_LEAVE_PAYMENTS.equals(setting.getKey())) {
                enablePaidLeaveRequests.setValue(setting.getValue() != null && "true".equals(setting.getValue()));
                timesheetHoursRC.setVisible(enablePaidLeaveRequests.getValue());
                leaveDailyRC.setVisible(enablePaidLeaveRequests.getValue());
                leaveMoneyRC.setVisible(enablePaidLeaveRequests.getValue());
                leaveMoneyCatRC.setVisible(enablePaidLeaveRequests.getValue());
            } else if (NUMBER_OF_WORK_DAYS.equals(setting.getKey())) {
                if (setting.getValue() != null && !"".equals(setting.getValue())) {
                    monthDays.setText(setting.getValue());
                }
            } else if (SHOW_YEAR_TO_DATE.equals(setting.getKey())) {
                if (setting.getValue() != null && !"".equals(setting.getValue())) {
                    showYearToDate.setValue("true".equals(setting.getValue()));
                }
            } else if (DEDUCT_TYPE.equals(setting.getKey())) {
                if (setting.getValue() != null && !"".equals(setting.getValue())) {
                    settingsTable.setType(Integer.parseInt(setting.getValue()));
                }
            } else if (DOUBLE_CONFIRMATION.equals(setting.getKey())) {
                enableDoubleConfirmation.setValue(setting.getValue() != null && "true".equals(setting.getValue()));
            } else if (DAILY_RATE_BY_EMPLOYER_SETTINGS.equals(setting.getKey())) {
                byEmployerSettings.setValue(setting.getValue() != null && ("true".equals(setting.getValue()) || "BY_STATIC_DAY".equals(setting.getValue())));
                byTimeslotSettings.setValue(setting.getValue() != null && "BY_TIMESLOT".equals(setting.getValue()));
                byCalendar.setValue(!byEmployerSettings.getValue() && !byTimeslotSettings.getValue());
            } else if (DISABLE_PAYROLL_TRANSACTIONS.equals(setting.getKey())) {
                disablePayrollTransactions.setValue(setting.getValue() != null && "true".equals(setting.getValue()));
            } else if (BY_DEFAULT_EMAIL_NOTIFICATION.equals(setting.getKey())) {
                byDefaultEmailNotification.setValue(setting.getValue() != null && "true".equals(setting.getValue()));
            } else if (TIMESHEET_HOURS_CALCUTATION_TYPE.equals(setting.getKey())) {
                if (setting.getValue() != null && !setting.getValue().isEmpty()) {
                    timesheetHoursCalcutationType.setType(Integer.parseInt(setting.getValue()));
                }
            } else if (LEAVE_DAILY_PAYMENT_TYPE.equals(setting.getKey())) {
                if (setting.getValue() != null && !setting.getValue().isEmpty()) {
                    leaveDailyType.setType(Integer.parseInt(setting.getValue()));
                }
            } else if (LEAVE_MONEY_PAYMENT_TYPE.equals(setting.getKey())) {
                if (setting.getValue() != null && !setting.getValue().isEmpty()) {
                    leaveMoneyType.setType(Integer.parseInt(setting.getValue()));
                }
            } else if (LEAVE_DAYS_IMPACT.equals(setting.getKey())) {
                paidLeaveDaysImpact.setValue(setting.getValue() == null || "true".equals(setting.getValue()));
            } else if (NON_PAID_LEAVE_DAYS_IMPACT.equals(setting.getKey())) {
                nonPaidLeaveDaysImpact.setValue(setting.getValue() == null || "true".equals(setting.getValue()));
            } else if (MULTI_CURRENCY_FOR_PAYROLL.equals(setting.getKey())) {
                payrollMultiCurrency.setValue(setting.getValue() == null || "true".equals(setting.getValue()));
            } else if (OVERTIME_RATE_BY_EMPLOYER_SETTINGS.equals(setting.getKey())) {
                monthlyOvertime.setValue(setting.getValue() != null && "MONTHLY".equals(setting.getValue()));
//                weeklyOvertime.setValue(setting.getValue() != null && "WEEKLY".equals(setting.getValue()));
                dailyOvertime.setValue(!monthlyOvertime.getValue() && !weeklyOvertime.getValue());
            } else if (DEFAULT_START_DATE.equals(setting.getKey())) {
                if (setting.getValue() != null && !"".equals(setting.getValue())) {
                    defaultStartDate.setText(setting.getValue());
                }
            } else if (EMPLOYER_SETTINGS.DEFAULT_ADDITIONAL_TYPE.equals(setting.getKey())) {
                boolean isVisible = setting.getValue() != null && "true".equals(setting.getValue());
                defaultAddPayTypeSwitcher.setValue(isVisible);
                defaultAddPayType.setVisible(isVisible);
            } else if (EMPLOYER_SETTINGS.DEFAULT_ADDITIONAL_TYPE_SETTINGS.equals(setting.getKey())) {
                defaultAddPayType.setType(Integer.parseInt(setting.getValue()));
            }
        }
        if (settings.getLeaveMoneyTypeCategory() != null) {
            leaveMoneyTypeCategory.addCategoryItem(settings.getLeaveMoneyTypeCategory());
        }
        settingsTable.setAllowances(settings.getAllowances(DEDUCT_ALLOWANCES));
        timesheetHoursCalcutationType.setAllowances(settings.getAllowances(TIMESHEET_HOURS_ALLOWANCES));
        leaveDailyType.setAllowances(settings.getAllowances(LEAVE_DAILY_ALLOWANCES));
        leaveMoneyType.setAllowances(settings.getAllowances(LEAVE_MONEY_ALLOWANCES));
        defaultAddPayType.setAllowances(settings.getAllowances(ADDITIONAL_PAYMENT_ALLOWANCES));
    }

    private void save() {
        if (!validate()) {
            return;
        }
        LoadingPanel.loading(true);
        PayrollService.App.get().saveCompanyPayrollSettings(getData(), new AbstractAsyncCallback<Integer>() {

            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Integer objectID) {
                LoadingPanel.loading(false);
                Utils.setPayrollTransactionDisabled(disablePayrollTransactions.getValue());
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), payrollStrings.employerSettings()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYER_SETTINGS_UPDATE, null, EmployerSettingsAddEditView.this);
                closeTab();
            }
        });
    }

    private EmployerSettings getData() {
        EmployerSettings data = new EmployerSettings();
        String gosiExpire = "", crExpire = "", licenseExpire = "";
        try {
            gosiExpire = DateUtils.fullDateFormat.format(gosiExpiryDate.getDate());
        } catch (Exception e) {
        }
        try {
            crExpire = DateUtils.fullDateFormat.format(crExpiryDate.getDate());
        } catch (Exception e) {
        }
        try {
            licenseExpire = DateUtils.fullDateFormat.format(licenseExpiryDate.getDate());
        } catch (Exception e) {
        }
        String employeeDailyRateType = byEmployerSettings.getValue() ? "BY_STATIC_DAY" : (byTimeslotSettings.getValue() ? "BY_TIMESLOT" : "BY_CALENDAR");
        String overtimeRateType = monthlyOvertime.getValue() ? "MONTHLY" : (weeklyOvertime.getValue() ? "WEEKLY" : "DAILY");
        data.setSettings(new KeyValueStruct[]{
                new KeyValueStruct(OFFICE_NUMBER, officeNumber.getText()),
                new KeyValueStruct(PAYE_REF_NUMBER, officeReference.getText()),
                new KeyValueStruct(OFFICE_NAME, officeName.getText()),
                new KeyValueStruct(COMPANY_CODE, companyCode.getText()),
                new KeyValueStruct(WPS_NO, wpsNo.getText()),
                new KeyValueStruct(COMPANY_NAME, companyName.getText()),
                new KeyValueStruct(ADDRESS1, address1.getText()),
                new KeyValueStruct(ADDRESS2, address2.getText()),
                new KeyValueStruct(CR_NO, crNo.getText()),
                new KeyValueStruct(GOSI_NO, gosiNo.getText()),
                new KeyValueStruct(LICENSE_NO, licenseNo.getText()),
                new KeyValueStruct(PHONE_NO, phoneNo.getText()),
                new KeyValueStruct(GOSI_EXPIRY_DATE, gosiExpire),
                new KeyValueStruct(CR_EXPIRY_DATE, crExpire),
                new KeyValueStruct(LICENSE_EXPIRY_DATE, licenseExpire),
                new KeyValueStruct(COUNTRY_ID, country.getSelectedId() != null ? country.getSelectedId().toString() : null),
                new KeyValueStruct(BANK_ACCOUNT_ID, bankListBox.getSelectedId() != null ? bankListBox.getSelectedId().toString() : null),
                new KeyValueStruct(PAYMENT_POLICY, paymentPolicy.getText()),
                new KeyValueStruct(EXPENSE_PAID_ACCOUNT, expenseBankAccount.getSelectedId() != null ? expenseBankAccount.getSelectedId().toString() : null),
                new KeyValueStruct(ENABLED_LEAVE_DEDUCTIONS, enableNonPaidLeaveRequests.getValue().toString()),
                new KeyValueStruct(SHOW_YEAR_TO_DATE, showYearToDate.getValue() ? "true" : "false"),
                new KeyValueStruct(NUMBER_OF_WORK_DAYS, monthDays.getText()),
                new KeyValueStruct(DEFAULT_START_DATE, defaultStartDate.getText()),
                new KeyValueStruct(ENABLED_DOUBLE_APPROVER_PAYRUN, enableTwoLevelApproval.getValue().toString()),
                new KeyValueStruct(DEDUCT_TYPE, settingsTable.getType().toString()),
                new KeyValueStruct(TIMESHEET_HOURS_CALCUTATION_TYPE, timesheetHoursCalcutationType.getType().toString()),
                new KeyValueStruct(LEAVE_DAILY_PAYMENT_TYPE, leaveDailyType.getType().toString()),
                new KeyValueStruct(LEAVE_MONEY_PAYMENT_TYPE, leaveMoneyType.getType().toString()),
                new KeyValueStruct(DOUBLE_CONFIRMATION, enableDoubleConfirmation.getValue().toString()),
                new KeyValueStruct(DAILY_RATE_BY_EMPLOYER_SETTINGS, employeeDailyRateType),
                new KeyValueStruct(OVERTIME_RATE_BY_EMPLOYER_SETTINGS, overtimeRateType),
                new KeyValueStruct(DISABLE_PAYROLL_TRANSACTIONS, disablePayrollTransactions.getValue().toString()),
                new KeyValueStruct(BY_DEFAULT_EMAIL_NOTIFICATION, byDefaultEmailNotification.getValue().toString()),
                new KeyValueStruct(LEAVE_DAYS_IMPACT, paidLeaveDaysImpact.getValue().toString()),
                new KeyValueStruct(NON_PAID_LEAVE_DAYS_IMPACT, nonPaidLeaveDaysImpact.getValue().toString()),
                new KeyValueStruct(MULTI_CURRENCY_FOR_PAYROLL, payrollMultiCurrency.getValue().toString()),
                new KeyValueStruct(ENABLED_LEAVE_PAYMENTS, enablePaidLeaveRequests.getValue().toString()),
                new KeyValueStruct(EMPLOYER_SETTINGS.DEFAULT_ADDITIONAL_TYPE, defaultAddPayTypeSwitcher.getValue().toString()),
                new KeyValueStruct(EMPLOYER_SETTINGS.DEFAULT_ADDITIONAL_TYPE_SETTINGS, defaultAddPayType.getType().toString())
        });
        data.setLeaveMoneyTypeCategory(leaveMoneyTypeCategory.getSelectedData());
        List<PaymentDeductionSelectItem> deductAllowances = data.getAllowances(DEDUCT_ALLOWANCES);
        List<PaymentDeductionSelectItem> timesheetHoursAllowances = data.getAllowances(TIMESHEET_HOURS_ALLOWANCES);
        List<PaymentDeductionSelectItem> leaveDailyAllowances = data.getAllowances(LEAVE_DAILY_ALLOWANCES);
        List<PaymentDeductionSelectItem> leaveMoneyAllowances = data.getAllowances(LEAVE_MONEY_ALLOWANCES);
        List<PaymentDeductionSelectItem> defaultAddPayTypeAllowances = data.getAllowances(ADDITIONAL_PAYMENT_ALLOWANCES);
        if (settingsTable.getType() == 1) {
            for (int i = 0; i < settingsTable.getAllowancesTable().getRowNumber(); i++) {
                if (settingsTable.getPaymentDeductionSelectItem(i) != null)
                    deductAllowances.add(settingsTable.getPaymentDeductionSelectItem(i));
            }
        }
        if (timesheetHoursCalcutationType.getType() == 1) {
            for (int i = 0; i < timesheetHoursCalcutationType.getAllowancesTable().getRowNumber(); i++) {
                if (timesheetHoursCalcutationType.getPaymentDeductionSelectItem(i) != null)
                    timesheetHoursAllowances.add(timesheetHoursCalcutationType.getPaymentDeductionSelectItem(i));
            }
        }
        if (leaveDailyType.getType() == 1) {
            for (int i = 0; i < leaveDailyType.getAllowancesTable().getRowNumber(); i++) {
                if (leaveDailyType.getPaymentDeductionSelectItem(i) != null)
                    leaveDailyAllowances.add(leaveDailyType.getPaymentDeductionSelectItem(i));
            }
        }
        if (leaveMoneyType.getType() == 1) {
            for (int i = 0; i < leaveMoneyType.getAllowancesTable().getRowNumber(); i++) {
                if (leaveMoneyType.getPaymentDeductionSelectItem(i) != null)
                    leaveMoneyAllowances.add(leaveMoneyType.getPaymentDeductionSelectItem(i));
            }
        }
        if (defaultAddPayType.getType() == 1) {
            for (int i = 0; i < defaultAddPayType.getAllowancesTable().getRowNumber(); i++) {
                if (defaultAddPayType.getPaymentDeductionSelectItem(i) != null)
                    defaultAddPayTypeAllowances.add(defaultAddPayType.getPaymentDeductionSelectItem(i));
            }
        }
        return data;
    }

    private boolean validate() {
        return true;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PAYROLL_COMPANY_SETTINGS_FORM;
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

    class CustomFlexTable extends VerticalPanelDiv {
        private DataListBox deductionType;
        private DynamicTable allowancesTable;
        private RowColumn allowanceRC;
        boolean withNetPay;
        boolean allAllowance;
        boolean withFixed;

        CustomFlexTable(boolean allAllowance, String title) {
            super();
            this.allAllowance = allAllowance;
            createWidgets(title);
        }

        CustomFlexTable(String title, boolean... withNetPay) {
            super();
            if (withNetPay.length == 2){
                this.withNetPay = withNetPay[0];
                this.withFixed = withNetPay[1];
            }else{
                this.withNetPay = withNetPay.length  > 0 && withNetPay[0];
            }
            createWidgets(title);
        }

        private void createWidgets(String title) {
            deductionType = createDeductTypeDropdown();
            allowancesTable = new DynamicTable(getAllowanceTableColumns(), true);
            allowancesTable.addRow(getWidgets(null));
            allowancesTable.addListener(new AddListener() {
                public void plusClicked(int rowId) {
                    allowancesTable.insertRow(rowId + 1, getWidgets(null));
                }

                @Override
                public void minusClicked(int rowId, Integer objectId) {

                }
            });
            allowancesTable.setBorderWidth(0);

            RowColumn typeRC = new RowColumn();
            typeRC.setLabel(title);
            typeRC.setComponent(deductionType);

            allowanceRC = new RowColumn();
            allowanceRC.setComponent(allowancesTable);

            add(typeRC);
            setVisible(false);
        }

        public void setType(Integer type) {
            deductionType.setSelected(type);
        }

        public Integer getType() {
            return deductionType.getSelectedId();
        }

        public DynamicTable getAllowancesTable() {
            return allowancesTable;
        }

        public void setAllowances(List<PaymentDeductionSelectItem> allowances) {
            if (getType() == 1) {
                add(allowanceRC);
            }
            if (allowances.size() > 0) {
                allowancesTable.clear();
                for (PaymentDeductionSelectItem allowance : allowances) {
                    allowancesTable.addRow(getWidgets(allowance));
                }
            }
        }

        public PaymentDeductionSelectItem getPaymentDeductionSelectItem(Integer rowId) {
            DynamicTableItem row = allowancesTable.getItem(rowId);
            CategoryLookUp categoryLookUp = (CategoryLookUp) row.getColumnById("allowance");
            return categoryLookUp.getSelectedData();
        }

        private DataListBox createDeductTypeDropdown() {
            SelectItem[] items = new SelectItem[withNetPay || allAllowance || withFixed ? 3 : 2];
            items[0] = new SelectItem(0, wfmStrings.basicSalary());
            items[1] = new SelectItem(1, wfmStrings.basicAllowancePay());
            if (allAllowance) items[2] = new SelectItem(2, payrollStrings.allAllowances());
            if (withNetPay) items[2] = new SelectItem(2, payrollStrings.deductFromNetPay());
            if (withFixed) items[2] = new SelectItem(2,wfmStrings.fixedAmount());
            final DataListBox dropdown = new DataListBox();
            dropdown.setWithoutNullLabel(true);
            dropdown.addStyleName(DEFAULT_WIDTH);
            dropdown.setItems(items);
            dropdown.setSelected(0);
            dropdown.addValueChangeHandler(changeEvent -> {
                if (dropdown.getSelectedId() == 1) {
                    add(allowanceRC);
                } else {
                    allowanceRC.removeFromParent();
                }
            });
            return dropdown;
        }

        private Widget[] getWidgets(PaymentDeductionSelectItem item) {
            Widget[] widgets = new Widget[1];
            CategoryLookUp allowanceLookUp = new CategoryLookUp(PayrollConstants.CATEGORY_PAYMENT);
            allowanceLookUp.addStyleName(DEFAULT_WIDTH);
            if (item != null) {
                allowanceLookUp.addCategoryItem(item);
            }
            widgets[0] = allowanceLookUp;
            return widgets;
        }

        private DynamicTableColumn[] getAllowanceTableColumns() {
            DynamicTableColumn[] columns = new DynamicTableColumn[1];
            columns[0] = new DynamicTableColumn(wfmStrings.allowance(), "allowance", 200);
            return columns;
        }
    }
}
