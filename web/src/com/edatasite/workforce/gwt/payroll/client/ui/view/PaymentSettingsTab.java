package com.edatasite.workforce.gwt.payroll.client.ui.view;


import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm.Field;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.customtabbar.CustomTabWidget;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollSettings;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: xushnud
 * Date: 24-Apr-2010
 * Time: 16:41:15
 * To change this template use File | Settings | File Templates.
 */
public class PaymentSettingsTab extends CustomTabWidget implements Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();
    PayrollSettings settings = new PayrollSettings();

    private final Integer employeeID;
    private VerticalPanel vPanel;
    protected WfmForm payrollDetails;
    protected RadioButton hRate;
    protected RadioButton fRate;
    protected RadioButton fHRate;
    protected RadioButton dailyRate;

    protected TextBox normalRate;
    protected TextBox overTimeRate;
    protected TextBox salary;

    protected Field salaryLabelField;
    protected Field normalRateLabelField;
    protected Field overTimeRateLabelField;
    protected Field payPeriodLabelField;
    protected Field payMethodLabelField;

    protected Label employeePaidtabLabel;
    protected Label salaryLabel;
    protected Label payPeriodLabel;
    protected Label payMethodLabel;
    protected Label normalRateLabel;
    protected Label overTimeRateLabel;
    protected Label grossNetLabel;


    protected DataListBox payPeriod;
    protected DataListBox payMethod;
    protected DataListBox salaryGrossNet;
    protected SimpleLink calculateGrossPay;
    protected Field salaryField;
    protected Field normalRateField;
    protected Field overTimeRateField;
    protected Field payPeriodField;
    protected Field payMethodField;
    protected boolean isEmpPayrollSettingsExists = false;
    protected final FlexTable table = new FlexTable();
    private final boolean edit;

//    private

    public PaymentSettingsTab(String tabName, Integer employeeID, boolean edit) {
        super(tabName);
        this.employeeID = employeeID;
        this.edit = edit;
    }

    public void initData() {
        vPanel = new VerticalPanel();
    }

    public void viewShow() {
//        WfmForm table = new WfmForm();
        payrollDetails = new WfmForm("103,200,120".split(","), "100%");
//        payrollDetails.setSize("480","220");
        hRate = new KpiRadioButton("paytype", payrollStrings.hourlyRate());
        fRate = new KpiRadioButton("paytype", wfmStrings.fixedRate());
        fHRate = new KpiRadioButton("paytype", wfmStrings.fixedHourlyRate());
        dailyRate = new KpiRadioButton("paytype", payrollStrings.dailyRate());

        salary = new TextBox();
        salary.addStyleName(DEFAULT_WIDTH);
        salary.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        salary.setText("0.00");
        Validation.addNumericKeyboardListener(salary);

        salaryGrossNet = new DataListBox();
        salaryGrossNet.setWidth("100");
        salaryGrossNet.setAllowFirstItem(true);
        salaryGrossNet.setItems(PayrollConstants.SALARY_AMOUNT_LIST);

        calculateGrossPay = new SimpleLink(payrollStrings.calculateGrossPay());
        calculateGrossPay.setVisible(false);

        normalRate = new TextBox();
        normalRate.addStyleName(DEFAULT_WIDTH);
        normalRate.setText("0.00");
        normalRate.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(normalRate);

        overTimeRate = new TextBox();
        overTimeRate.addStyleName(DEFAULT_WIDTH);
        overTimeRate.setText("0.00");
        overTimeRate.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(overTimeRate);
        payPeriod = new DataListBox();
        payPeriod.addStyleName(DEFAULT_WIDTH);
        payPeriod.setItems(PayrollClientUtils.getPayFrequencies(Utils.isArabicCompany()));

        payMethod = new DataListBox();
        payMethod.addStyleName(DEFAULT_WIDTH);
        payMethod.setItems(PayrollConstants.PAY_METHOD_LIST);

        if ((employeeID == null && !edit) || (employeeID != null && edit)) {

            FlexTable employeePaidTab = new FlexTable();
            employeePaidTab.setWidget(0, 0, hRate);
            employeePaidTab.setWidget(1, 0, fRate);
            employeePaidTab.setWidget(2, 0, fHRate);
            employeePaidTab.setWidget(3, 0, dailyRate);
            employeePaidTab.getColumnFormatter().setWidth(0, "150px");


            table.setWidget(0, 0, salaryGrossNet);
            table.setWidget(1, 0, calculateGrossPay);


            salaryGrossNet.addValueChangeHandler(sender -> {
                calculateGrossPay.setVisible(salaryGrossNet.getSelectedItem() != null && "Net".equals(salaryGrossNet.getSelectedItem().getName()));
            });

            fRate.setValue(true);
            hRate.addClickHandler(clickEvent -> hRateClickHandler());
            fRate.addClickHandler(clickEvent -> fRateClickHandler(table));
            fHRate.addClickHandler(clickEvent -> fHRateClickHandler(table));
            dailyRate.addClickHandler(clickEvent -> dailyRateClickHandler(/*table*/));

            payrollDetails.addField(wfmStrings.employeeIsPaid(), employeePaidTab);
            //payrollDetails.getFlex().getCellFormatter().setWidth(0, 0, "150px");

            salaryField = payrollDetails.addField(wfmStrings.basicSalary(), new Widget[]{salary, table}, true, 2);
            drawPaymentSettngsComponents();

        }
        if (employeeID != null && !edit) {
            payrollDetails = new WfmForm("103,120".split(","), "100%");
//            payrollDetails.setSize("350","220");
            payrollDetails.setLabelSize("150px");
            employeePaidtabLabel = new Label();
            employeePaidtabLabel.addStyleName(DEFAULT_WIDTH);
            salaryLabel = new Label();
            salaryLabel.addStyleName(DEFAULT_WIDTH);
            payPeriodLabel = new Label();
            payPeriodLabel.addStyleName(DEFAULT_WIDTH);
            payMethodLabel = new Label();
            payMethodLabel.addStyleName(DEFAULT_WIDTH);

            overTimeRateLabel = new Label();
            overTimeRateLabel.addStyleName(DEFAULT_WIDTH);
            normalRateLabel = new Label();
            normalRateLabel.addStyleName(DEFAULT_WIDTH);
            grossNetLabel = new Label();
            grossNetLabel.addStyleName(DEFAULT_WIDTH);

            payrollDetails.addField(wfmStrings.employeeIsPaid(), employeePaidtabLabel);
            salaryLabelField = payrollDetails.addField(wfmStrings.basicSalary(), salaryLabel);
            normalRateLabelField = payrollDetails.addField(wfmStrings.normalRate(), normalRateLabel);
            overTimeRateLabelField = payrollDetails.addField(wfmStrings.overtimeRate(), overTimeRateLabel);
            payPeriodLabelField = payrollDetails.addField(wfmStrings.payPeriod(), payPeriodLabel);
            payMethodLabelField = payrollDetails.addField(wfmStrings.paymentMethod(), payMethodLabel);

//            payrollDetails.setSize("220","220");


        }

        vPanel.add(payrollDetails);
        add(vPanel);
    }

    protected void hRateClickHandler() {

        if (salaryField != null) {
            payrollDetails.removeField(salaryField);
            salaryField = null;
        }
        if (normalRateField != null) {
            payrollDetails.removeField(normalRateField);
            normalRateField = null;
        }
        normalRateField = payrollDetails.addField(wfmStrings.normalRate(), normalRate, true);
        if (overTimeRateField != null) {
            payrollDetails.removeField(overTimeRateField);
            overTimeRateField = null;
        }
        overTimeRateField = payrollDetails.addField(wfmStrings.overtimeRate(), overTimeRate, true);
        removePaymentSettingsComponents();
        drawPaymentSettngsComponents();
    }

    protected void fHRateClickHandler(FlexTable table) {
        if (salaryField != null) {
            payrollDetails.removeField(salaryField);
            salaryField = null;
        }
        salaryField = payrollDetails.addField(wfmStrings.basicSalary(), new Widget[]{salary, table}, true);
        if (normalRateField != null) {
            payrollDetails.removeField(normalRateField);
            normalRateField = null;
        }
        normalRateField = payrollDetails.addField(wfmStrings.normalRate(), normalRate, true);
        if (overTimeRateField != null) {
            payrollDetails.removeField(overTimeRateField);
            overTimeRateField = null;
        }
        overTimeRateField = payrollDetails.addField(wfmStrings.overtimeRate(), overTimeRate, true);
        removePaymentSettingsComponents();
        drawPaymentSettngsComponents();
    }

    protected void fRateClickHandler(FlexTable table) {
        if (salaryField != null) {
            payrollDetails.removeField(salaryField);
            salaryField = null;
        }
        salaryField = payrollDetails.addField(wfmStrings.basicSalary(), new Widget[]{salary, table}, true);
        if (normalRateField != null) {
            payrollDetails.removeField(normalRateField);
            normalRateField = null;
        }
        if (overTimeRateField != null) {
            payrollDetails.removeField(overTimeRateField);
            overTimeRateField = null;
        }

        removePaymentSettingsComponents();
        drawPaymentSettngsComponents();
    }

    protected void dailyRateClickHandler() {
        hRateClickHandler();
    }

    private void drawPaymentSettngsComponents() {
        payPeriodField = payrollDetails.addField(wfmStrings.payPeriod(), payPeriod, true);
        payMethodField = payrollDetails.addField(wfmStrings.paymentMethod(), payMethod);
    }

    private void removePaymentSettingsComponents() {
        if (payPeriodField != null) {
            payrollDetails.removeField(payPeriodField);
            payPeriodField = null;
        }
        if (payMethodField != null) {
            payrollDetails.removeField(payMethodField);
            payMethodField = null;
        }
    }

}
