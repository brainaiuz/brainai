package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.PensionSchemeData;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 7/8/15
 * Time: 11:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class PensionSchemaSummaryView extends PensionSchemaAddEditView {

    private HTML schemeName;
    private HTML schemeProvider;
    //Employee Contribution
    private HTML deductionType;
    private HTML deductionValue;
    private HTML nonLocalDeductionValue;
    private HTML empMaxTaxableAmount;
    private HTML deductFrom;
    private DynamicTable allowancesTable;
    //Employer Contribution
    private HTML employerDeductionType;
    private HTML employerDeductionValue;
    private HTML employerNonLocalDeductionValue;
    private HTML compMaxTaxableAmount;
    private FormGroup allowancesTableField;

    public PensionSchemaSummaryView() {
        super("PENSION_SCHEME", wfmStrings.pensionScheme());
    }

    @Override
    protected Widget onInitialize() {
        return super.onInitialize();
    }

    @Override
    protected void init() {
        schemeName = new HTML();
        schemeName.addStyleName(Constants.DEFAULT_WIDTH);
        schemeProvider = new HTML();
        schemeProvider.addStyleName(Constants.DEFAULT_WIDTH);
        deductionType = new HTML();
        deductionType.addStyleName(Constants.DEFAULT_WIDTH);
        deductionValue = new HTML();
        deductionValue.addStyleName(Constants.DEFAULT_WIDTH);
        nonLocalDeductionValue = new HTML();
        nonLocalDeductionValue.addStyleName(Constants.DEFAULT_WIDTH);
        deductFrom = new HTML();
        deductFrom.addStyleName(Constants.DEFAULT_WIDTH);
        employerDeductionType = new HTML();
        employerDeductionType.addStyleName(Constants.DEFAULT_WIDTH);
        employerDeductionValue = new HTML();
        employerDeductionValue.addStyleName(Constants.DEFAULT_WIDTH);
        employerNonLocalDeductionValue = new HTML();
        employerNonLocalDeductionValue.addStyleName(Constants.DEFAULT_WIDTH);
        empMaxTaxableAmount = new HTML();
        empMaxTaxableAmount.addStyleName(Constants.DEFAULT_WIDTH);
        compMaxTaxableAmount = new HTML();
        compMaxTaxableAmount.addStyleName(Constants.DEFAULT_WIDTH);
        allowancesTable = new DynamicTable(getAllowanceTableColumns(), false);
        allowancesTable.setBorderWidth(0);

        addTitleField(PENSION_SCHEMA.PENSION_SCHEMA_DETAILS, payrollStrings.pensionSchemeDetails());
        addTitleField(PENSION_SCHEMA.EMPLOYEE_CONTRIBUTION, payrollStrings.employeeContribution());
        addTitleField(PENSION_SCHEMA.EMPLOYER_CONTRIBUTION, wfmStrings.employerContribution());
        addTitleField(PENSION_SCHEMA.PENSION_SCHEMA_CONTRIBUTION, payrollStrings.contribution());

        allowancesTableField = new FormGroup(allowancesTable);
        FormGroup schemeNameField = new FormGroup(payrollStrings.schemeName(), schemeName);
        FormGroup schemeProviderField = new FormGroup(payrollStrings.schemeProvider(), schemeProvider);
        FormGroup deductionTypeField = new FormGroup(payrollStrings.deductionType(), deductionType);
        FormGroup deductionValueField = new FormGroup(payrollStrings.forLocalEmployees(), deductionValue);
        FormGroup nonLocalDeductionValueField = new FormGroup(payrollStrings.forNonLocalEmployees(), nonLocalDeductionValue);
        FormGroup deductFromField = new FormGroup(payrollStrings.deductionFrom(), deductFrom);
        FormGroup employerDeductionTypeField = new FormGroup(payrollStrings.deductionType(), employerDeductionType);
        FormGroup employerDeductionValueField = new FormGroup(payrollStrings.forLocalEmployees(), employerDeductionValue);
        FormGroup employerNonLocalDeductionValueField = new FormGroup(payrollStrings.forNonLocalEmployees(), employerNonLocalDeductionValue);
        FormGroup empMaxTaxableAmountField = new FormGroup(payrollStrings.maxTaxableAmount(), empMaxTaxableAmount);
        FormGroup compMaxTaxableAmountField = new FormGroup(payrollStrings.maxTaxableAmount(), compMaxTaxableAmount);
        addField(PENSION_SCHEMA.DEDUCT_SETTINGS, allowancesTableField);
        addField(PENSION_SCHEMA.PENSION_SCHEMA_NAME, schemeNameField);
        addField(PENSION_SCHEMA.PENSION_PROVIDER_NAME, schemeProviderField);
        addField(PENSION_SCHEMA.EMPLOYEE_DEDUCT_TYPE, deductionTypeField);
        addField(PENSION_SCHEMA.EMPLOYEE_RATE_LOCAL, deductionValueField);
        addField(PENSION_SCHEMA.EMPLOYEE_RATE_NON_LOCAL, nonLocalDeductionValueField);
        addField(PENSION_SCHEMA.DEDUCT_FROM, deductFromField);
        addField(PENSION_SCHEMA.EMPLOYER_DEDUCT_TYPE, employerDeductionTypeField);
        addField(PENSION_SCHEMA.EMPLOYER_RATE_LOCAL, employerDeductionValueField);
        addField(PENSION_SCHEMA.EMPLOYER_RATE_NON_LOCAL, employerNonLocalDeductionValueField);
        addField(PENSION_SCHEMA.EMP_MAX_TAXABLE_AMOUNT, empMaxTaxableAmountField);
        addField(PENSION_SCHEMA.COMP_MAX_TAXABLE_AMOUNT, compMaxTaxableAmountField);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ADD_PENSION_SCHEME, PensionSchemaSummaryView.this, (sender, args) -> show());

        show();
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
    protected void addButtons() {
        addEditButton().addClickHandler(event -> goTo("pensionscheme|add/add"));
    }

    @Override
    protected String getFormID() {
        return super.getFormID();
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return super.getWikiCode();
    }

    @Override
    public String getIconStyle() {
        return "payroll ukni-bands-list";
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        PayrollService.App.get().getPensionSchemeById(null, new AbstractAsyncCallback<PensionSchemeData>() {
            @Override
            public void failure(Throwable throwable) {
                throwable.printStackTrace();
                LoadingPanel.loading(false);
            }

            @Override
            public void success(PensionSchemeData result) {
                LoadingPanel.loading(false);
                if (result != null) {
                    setValues(result);
                } else {
                    goTo("pensionscheme|add/add");
                }
            }
        });
    }

    @Override
    protected void setValues(PensionSchemeData ps) {
        schemeName.setText(ps.getSchemeName());
        schemeProvider.setText(ps.getProviderName());
        deductionType.setText(ps.getDeductionType() == 0 ? FIXED_RATE_CONTRIBUTION : PERCENTAGE_RATE_CONTRIBUTION);
        deductionValue.setText(ps.getDeductionValue() != null ? PayrollClientUtils.format(ps.getDeductionValue()) : PayrollClientUtils.format(BigDecimal.ZERO));
        nonLocalDeductionValue.setText(ps.getNonLocalDeductionValue() != null ? PayrollClientUtils.format(ps.getNonLocalDeductionValue()) : PayrollClientUtils.format(BigDecimal.ZERO));
        deductFrom.setText(ps.getDeductFrom() == 0 ? wfmStrings.basicSalary() : wfmStrings.basicAllowancePay());
        employerDeductionType.setText(ps.getEmployerDeductionType() == 0 ? FIXED_RATE_CONTRIBUTION : PERCENTAGE_RATE_CONTRIBUTION);
        employerDeductionValue.setText(ps.getEmployerDeductionValue() != null ? PayrollClientUtils.format(ps.getEmployerDeductionValue()) : PayrollClientUtils.format(BigDecimal.ZERO));
        employerNonLocalDeductionValue.setText(ps.getEmployerNonLocalDeductionValue() != null ? PayrollClientUtils.format(ps.getEmployerNonLocalDeductionValue()) : PayrollClientUtils.format(BigDecimal.ZERO));
        empMaxTaxableAmount.setText(PayrollClientUtils.format(ps.getEmpMaxTaxableAmount()));
        compMaxTaxableAmount.setText(PayrollClientUtils.format(ps.getCompMaxTaxableAmount()));
        if (ps.getAllowances() != null && ps.getAllowances().size() > 0) {
            allowancesTable.clear();
            for (PaymentDeductionSelectItem item : ps.getAllowances()) {
                allowancesTable.addRow(getWidgets(item));
            }
            allowancesTableField.setVisible(true);
        } else {
            allowancesTableField.setVisible(false);
        }

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
