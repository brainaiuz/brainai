package com.edatasite.workforce.gwt.payroll.client.ui.view.additionalpayment;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.CategoryLookUp;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.ui.PayrollContants;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AdditionalPaymentItemModal extends KpiSideNavBox {
    interface PayrunPaymentModalUiBinder extends UiBinder<HTMLPanel, AdditionalPaymentItemModal> {
    }

    private static PayrunPaymentModalUiBinder ourUiBinder = GWT.create(PayrunPaymentModalUiBinder.class);
    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);

    private static final Integer calculationScale = Optional.ofNullable(Utils.getAccountingCalculationScale()).orElse(2);
    private PaymentDeductionObject item;

    @UiField
    HTMLPanel container;
    @UiField
    Div deductionDiv;
    @UiField
    Label deductionLabel;
    @UiField
    Div deductionPanel;
    @UiField
    Label taxLabel;
    @UiField
    Div taxPanel;
    @UiField
    Div employerContributionDiv;
    @UiField
    Label employerContributionLabel;
    @UiField
    Div employerContributionPanel;
    @UiField
    Div paymentPanel;

    private BigDecimal amount;
    private PaymentDeductionSelectItem category;
    private boolean isEdit;
    private TextBox paymentAmountBox;
    private EditableTable taxTable, employerContributionTable, deductionTable;
    private HTML taxEmptyHTML, employerContributionEmptyHTML, deductionEmptyHTML;
    private Map<String, ColumnConfigs> columnsMap;
    private List<PaymentDeductionObject> taxCategories;
    private List<PaymentDeductionObject> employerContributionCategories;
    private List<PaymentDeductionObject> deductionCategories;

    public AdditionalPaymentItemModal(PaymentDeductionObject item, Map<String, ColumnConfigs> columnsMap, BigDecimal amount, PaymentDeductionSelectItem category, boolean isEdit, List<PaymentDeductionObject> taxCategories, List<PaymentDeductionObject> employerContributionCategories, List<PaymentDeductionObject> deductionCategories) {
        super(500);
        this.item = item;
        this.amount = amount;
        this.category = category;
        this.isEdit = isEdit;
        this.taxCategories = taxCategories;
        this.employerContributionCategories = employerContributionCategories;
        this.deductionCategories = deductionCategories;
        this.columnsMap = columnsMap;
        ourUiBinder.createAndBindUi(this);
        initInternal();
        show();
    }

    private void initInternal() {
        addHeader(new HTML(item.getEmployee().getName()));

        paymentAmountBox = new TextBox();
        paymentAmountBox.setText(PayrollClientUtils.format(amount));
        Validation.addNumericKeyboardListener(paymentAmountBox, calculationScale, false);
        Validation.checkToFocusTextBox(paymentAmountBox, PayrollClientUtils.format(BigDecimal.ZERO));
        paymentAmountBox.setEnabled(false);
        paymentPanel.add(new FormGroup(wfmStrings.paymentAmount(), paymentAmountBox));

        BigDecimal taxableAmount = amount;
        BigDecimal totalTax = BigDecimal.ZERO;
        if (category != null) {
            PaymentDeductionSelectItem categoryItem = (PaymentDeductionSelectItem) category;
            if (PayrollConstants.MATERIAL_AID_TYPE_FAMILY_AFFAIRS.equals(categoryItem.getSystemCode()) ||
                    PayrollConstants.MATERIAL_AID_TYPE_FUNERAL.equals(categoryItem.getSystemCode()) ||
                    PayrollConstants.MATERIAL_AID_TYPE_GIFT.equals(categoryItem.getSystemCode())) {
                BigDecimal balance = item.getLgotaBalanceMap().getOrDefault(categoryItem.getSystemCode(), BigDecimal.ZERO);
                taxableAmount = taxableAmount.subtract(balance);
                taxableAmount = taxableAmount.compareTo(BigDecimal.ZERO) > 0 ? taxableAmount : BigDecimal.ZERO;
                if (balance.compareTo(BigDecimal.ZERO) > 0) {
                    TextBox lgotaBalance = new TextBox();
                    lgotaBalance.setText(PayrollClientUtils.format(balance));
                    Validation.addNumericKeyboardListener(lgotaBalance, calculationScale, false);
                    Validation.checkToFocusTextBox(lgotaBalance, PayrollClientUtils.format(BigDecimal.ZERO));
                    lgotaBalance.setEnabled(false);
                    paymentPanel.add(new FormGroup("Льгота баланс", lgotaBalance));

                    TextBox taxableAmountField = new TextBox();
                    taxableAmountField.setText(PayrollClientUtils.format(taxableAmount));
                    Validation.addNumericKeyboardListener(taxableAmountField, calculationScale, false);
                    Validation.checkToFocusTextBox(taxableAmountField, PayrollClientUtils.format(BigDecimal.ZERO));
                    taxableAmountField.setEnabled(false);
                    paymentPanel.add(new FormGroup("Taxable Amount", taxableAmountField));
                }
            }
        }

        if (columnsMap.containsKey(PayrollContants.TAX)) {
            taxLabel.setText(wfmStrings.tax());
            taxLabel.setVisible(true);

            taxTable = new EditableTable(getPaymentDeductionColumns(), false);

            taxEmptyHTML = new HTML("There are no taxes pending for this employee");

            if (taxCategories != null && taxCategories.size() > 0) {
                for (PaymentDeductionObject taxCategory : taxCategories) {
                    taxTable.addRow(getPaymentDeductionWidgets(taxCategory, PayrollConstants.CATEGORY_TAX, taxableAmount));
                    if (taxCategory != null) {
                        BigDecimal amount = taxableAmount.multiply(taxCategory.getPercentage()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                        totalTax = totalTax.add(amount);
                    }
                }
            }
            onChangeEvent(taxTable, taxEmptyHTML);

            taxPanel.add(taxTable);
            taxPanel.add(taxEmptyHTML);
            taxPanel.setVisible(true);
        }
        if (columnsMap.containsKey(PayrollContants.EMPLOYER_CONTRIBUTION)) {
            employerContributionLabel.setText(wfmStrings.employerContribution());

            employerContributionTable = new EditableTable(getPaymentDeductionColumns(), false);

            employerContributionEmptyHTML = new HTML("There are no employer contribution for this employee");

            boolean visible = false;
            if (employerContributionCategories != null && employerContributionCategories.size() > 0) {
                for (PaymentDeductionObject pdo : employerContributionCategories) {
                    employerContributionTable.addRow(getPaymentDeductionWidgets(pdo, PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION, taxableAmount));
                    visible = true;
                }
            }
            onChangeEvent(employerContributionTable, employerContributionEmptyHTML);

            employerContributionPanel.add(employerContributionTable);
//            employerContributionPanel.add(taxEmptyHTML);
            employerContributionLabel.setVisible(visible);
            employerContributionPanel.setVisible(visible);
            employerContributionDiv.setVisible(visible);
        }
        if (columnsMap.containsKey(PayrollContants.DEDUCTION)) {
            deductionLabel.setText(wfmStrings.deduction());

            deductionTable = new EditableTable(getPaymentDeductionColumns(), false);

            deductionEmptyHTML = new HTML("There are no deduction for this employee");

            boolean visible = false;
            if (deductionCategories != null && deductionCategories.size() > 0) {
                for (PaymentDeductionObject pdo : deductionCategories) {
                    BigDecimal deductionAmount = taxableAmount;
                    if (Integer.valueOf(4).equals(pdo.getType()) && !category.isExcludeInCustomDeductions()) {
                        deductionAmount = taxableAmount.subtract(totalTax);
                    }
                    deductionTable.addRow(getPaymentDeductionWidgets(pdo, PayrollConstants.CATEGORY_DEDUCTION, deductionAmount));
                    visible = true;
                }
            }
            onChangeEvent(deductionTable, deductionEmptyHTML);

            deductionPanel.add(deductionTable);
            deductionLabel.setVisible(visible);
            deductionPanel.setVisible(visible);
            deductionDiv.setVisible(visible);
        }

        addBody(container);
    }

    private void onChangeEvent(EditableTable table, HTML emptyTextHTML) {
        if (table.getRowCount() > 0) {
            table.setVisible(true);
            emptyTextHTML.setVisible(false);
        } else {
            table.setVisible(false);
            emptyTextHTML.setVisible(true);
        }
    }

    private ColumnConfig[] getPaymentDeductionColumns() {
        return new ColumnConfig[]{
                new ColumnConfig(LookUpCell.class, "category", wfmStrings.category(), 150, true),
                new ColumnConfig(CustomCell.class, "type", wfmStrings.type(), 100),
                new ColumnConfig(CustomCell.class, "amount", wfmStrings.amount(), 100, true, Constants.RIGHT_ALIGN_CELL)
        };
    }

    private Widget[] getPaymentDeductionWidgets(PaymentDeductionObject paymentDeduction, final String from, BigDecimal taxableAmount) {
        CategoryLookUp categoryLookUp = new CategoryLookUp(from, () -> true);
        categoryLookUp.setEnabled(false);

        EditableTextBox type = new EditableTextBox();
        type.setEnabled(false);
        type.setText(PayrollClientUtils.numberFormat(paymentDeduction.getPercentage()) + wfmStrings.ofBasicAllowances());

        EditableTextBox amountWidget = new EditableTextBox();
        amountWidget.setText(PayrollClientUtils.format(BigDecimal.ZERO));
        amountWidget.setEnabled(false);

        if (paymentDeduction != null) {
            if (paymentDeduction.getCategoryItem() != null) {
                categoryLookUp.addCategoryItem(paymentDeduction.getCategoryItem());
                categoryLookUp.setEnabled(false);
            }

            BigDecimal amount = taxableAmount.multiply(paymentDeduction.getPercentage()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
            amountWidget.setText(PayrollClientUtils.format(amount));
        }

        return new Widget[]{categoryLookUp, type, amountWidget};
    }

}
