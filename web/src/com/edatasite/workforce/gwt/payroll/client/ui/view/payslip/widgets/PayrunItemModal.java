package com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.widgets;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.ExpenseData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.TotalTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PaymentAccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.CategoryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.PayslipItemAmountWidget;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
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
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import org.gwt.advanced.client.ui.widget.EditableGrid;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.ERROR_FORM_STYLE;

public class PayrunItemModal extends KpiSideNavBox {
    interface PayrunPaymentModalUiBinder extends UiBinder<HTMLPanel, PayrunItemModal> {
    }

    private static final PayrunPaymentModalUiBinder ourUiBinder = GWT.create(PayrunPaymentModalUiBinder.class);
    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);

    private static final Integer calculationScale = Optional.ofNullable(Utils.getAccountingCalculationScale()).orElse(2);
    private SinglePayrunItem item;

    @UiField
    HTMLPanel container;
    @UiField
    FormGroup basicSalary;
    @UiField
    Label paymentLabel;
    @UiField
    Div paymentPanel;
    @UiField
    MaterialLink paymentAdd;
    @UiField
    FormGroup pensionPanel;
    @UiField
    Label deductionLabel;
    @UiField
    Div deductionPanel;
    @UiField
    MaterialLink deductionAdd;
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
    MaterialLink taxAdd;
    @UiField
    Label expenseLabel;
    @UiField
    Div expensePanel;
    @UiField
    TotalTable totalTable;

    private TextBox basicSalaryBox;
    private EditableTable paymentTable, deductionTable, taxTable, expenseTable, employerContributionTable;
    private HTML paymentEmptyHTML, deductionEmptyHTML, taxEmptyHTML, expenseEmptyHTML, employerContributionEmptyHTML;
    private final Map<String, ColumnConfig> columnsMap;
    private HTML total;
    private EditableGrid grid;
    private WfmButton2 saveButton;
    private final boolean editable;
    private HTML pensionTotal;

    protected BiConsumer<Integer, SinglePayrunItem> saveHandler;

    public PayrunItemModal(SinglePayrunItem item, Map<String, ColumnConfig> columnsMap) {
        super(500);
        this.item = item;
        this.columnsMap = columnsMap;
        this.editable = item.isEditable();
        ourUiBinder.createAndBindUi(this);
        initInternal();
        show();
    }

    private void initInternal() {
        addHeader(new HTML(item.getEmployee()));

        if (columnsMap.containsKey(PayrollContants.BASIC_SALARY)) {
            basicSalaryBox = new TextBox();
            basicSalaryBox.setText(PayrollClientUtils.format(item.getBasicSalary()));
            Validation.addNumericKeyboardListener(basicSalaryBox, calculationScale, false);
            Validation.checkToFocusTextBox(basicSalaryBox, PayrollClientUtils.format(BigDecimal.ZERO));
            basicSalaryBox.addKeyUpHandler(e -> updatePaymentDeductionTables());

            basicSalary.setLabel(wfmStrings.basicSalary());
            basicSalary.setContent(basicSalaryBox);
            basicSalaryBox.setEnabled(editable);
        }
        if (columnsMap.containsKey(PayrollContants.ALLOWANCE)) {
            paymentLabel.setText(payrollStrings.allowanceDetails());
            paymentLabel.setVisible(true);

            paymentTable = new EditableTable(getPaymentDeductionColumns(), editable, editable, false);
            paymentTable.setListener(new EditableTableListener() {
                                         @Override
                                         public void addRow() {
                                             paymentTable.addRow(getPaymentDeductionWidgets(null, PayrollConstants.CATEGORY_PAYMENT));
                                             onChangeEvent(paymentTable, paymentEmptyHTML);
                                         }

                                         @Override
                                         public void removeRow() {
                                             onChangeEvent(paymentTable, paymentEmptyHTML);
                                             updatePaymentDeductionTables();
                                         }
                                     }
            );

            paymentEmptyHTML = new HTML("There are no allowances for this employee");

            for (PaymentDeductionObject pdo : item.getPaymentCategories()) {
                if (!pdo.isSalaryObject()) {
                    paymentTable.addRow(getPaymentDeductionWidgets(pdo, PayrollConstants.CATEGORY_PAYMENT));
                }
            }
            if (paymentTable.getRowCount() == 0) {
                paymentTable.addRow(getPaymentDeductionWidgets(null, PayrollConstants.CATEGORY_PAYMENT));
            }
            onChangeEvent(paymentTable, paymentEmptyHTML);

            paymentPanel.add(paymentTable);
            paymentPanel.add(paymentEmptyHTML);
            paymentPanel.setVisible(true);
        }
        if (columnsMap.containsKey(PayrollContants.PENSION)) {
            pensionTotal = new HTML();
            pensionTotal.setText(PayrollClientUtils.format(item.getPensionAmount() != null ? item.getPensionAmount() : BigDecimal.ZERO));
            pensionPanel.setLabel(payrollStrings.pension());
            pensionPanel.setContent(pensionTotal);
            pensionPanel.setVisible(true);

        }
        /*if (columnsMap.containsKey(PayrollContants.EMPLOYER_CONTRIBUTION)) {
            employerContributionPanel.setLabel(payrollStrings.employerContribution());
            employerContributionPanel.setContent(new HTML(PayrollClientUtils.format(item.getEmployerContribution() != null ? item.getEmployerContribution() : BigDecimal.ZERO)));
            employerContributionPanel.setVisible(true);
        }*/
        if (columnsMap.containsKey(PayrollContants.DEDUCTION)) {
            deductionLabel.setText(wfmStrings.deductionDetails());
            deductionLabel.setVisible(true);

            deductionTable = new EditableTable(getPaymentDeductionColumns(), editable, editable, false);
            deductionTable.setListener(new EditableTableListener() {
                                           @Override
                                           public void addRow() {
                                               deductionTable.addRow(getPaymentDeductionWidgets(null, PayrollConstants.CATEGORY_DEDUCTION));
                                               onChangeEvent(deductionTable, deductionEmptyHTML);
                                           }

                                           @Override
                                           public void removeRow() {
                                               onChangeEvent(deductionTable, deductionEmptyHTML);
                                               updatePaymentDeductionTables();
                                           }
                                       }
            );

            deductionEmptyHTML = new HTML("There are no deductions pending for this employee");

            for (PaymentDeductionObject pdo : item.getDeductionCategories()) {
                if (!pdo.isSalaryObject()) {
                    deductionTable.addRow(getPaymentDeductionWidgets(pdo, PayrollConstants.CATEGORY_DEDUCTION));
                }
            }
            if (deductionTable.getRowCount() == 0) {
                deductionTable.addRow(getPaymentDeductionWidgets(null, PayrollConstants.CATEGORY_DEDUCTION));
            }
            onChangeEvent(deductionTable, deductionEmptyHTML);

            deductionPanel.add(deductionTable);
            deductionPanel.add(deductionEmptyHTML);
            deductionPanel.setVisible(true);
        }
        if (columnsMap.containsKey(PayrollContants.TAX)) {
            taxLabel.setText(wfmStrings.tax());
            taxLabel.setVisible(true);

            taxTable = new EditableTable(getPaymentDeductionColumns(), editable, editable, false);
            taxTable.setListener(new EditableTableListener() {
                                     @Override
                                     public void addRow() {
                                         taxTable.addRow(getPaymentDeductionWidgets(null, PayrollConstants.CATEGORY_TAX));
                                         onChangeEvent(taxTable, taxEmptyHTML);
                                     }

                                     @Override
                                     public void removeRow() {
                                         onChangeEvent(taxTable, taxEmptyHTML);
                                         updatePaymentDeductionTables();
                                     }
                                 }
            );

            taxEmptyHTML = new HTML("There are no taxes pending for this employee");

            for (PaymentDeductionObject pdo : item.getTaxCategories()) {
                if (!pdo.isSalaryObject()) {
                    taxTable.addRow(getPaymentDeductionWidgets(pdo, PayrollConstants.CATEGORY_TAX));
                }
            }
            if (taxTable.getRowCount() == 0) {
                taxTable.addRow(getPaymentDeductionWidgets(null, PayrollConstants.CATEGORY_TAX));
            }
            onChangeEvent(taxTable, taxEmptyHTML);

            taxPanel.add(taxTable);
            taxPanel.add(taxEmptyHTML);
            taxPanel.setVisible(true);
        }
        if (columnsMap.containsKey(PayrollContants.EMPLOYER_CONTRIBUTION)) {
            employerContributionLabel.setText(wfmStrings.employerContribution());

            employerContributionTable = new EditableTable(getPaymentDeductionColumns(), editable);
            employerContributionTable.setRemoveRowListener(() -> {
                employerContributionTable.getGrid().getModel().removeRow(employerContributionTable.getGrid().getCurrentRow());
                onChangeEvent(employerContributionTable, employerContributionEmptyHTML);
                calculate();
            });

            employerContributionEmptyHTML = new HTML("There are no employer contribution for this employee");

            boolean visible = false;
            for (PaymentDeductionObject pdo : item.getEmployerContributionCategories()) {
                if (!pdo.isSalaryObject()) {
                    employerContributionTable.addRow(getPaymentDeductionWidgets(pdo, PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION));
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
        if (Utils.isEnableAccountingModule() && columnsMap.containsKey(PayrollContants.EXPENSE)) {
            expenseLabel.setText(wfmStrings.expense());
            expenseLabel.setVisible(true);

            expenseTable = new EditableTable(getExpenseColumns());
            expenseTable.setRemoveRowListener(() -> {
                expenseTable.getGrid().getModel().removeRow(expenseTable.getGrid().getCurrentRow());
                onChangeEvent(expenseTable, expenseEmptyHTML);
                updatePaymentDeductionTables();
            });

            expenseEmptyHTML = new HTML("There are no expenses for this employee");

            if (item.getEmployeeExpenses() != null && item.getEmployeeExpenses().getExpenses() != null) {
                for (ExpenseData expense : item.getEmployeeExpenses().getExpenses()) {
                    expenseTable.addRow(getExpenseWidgets(expense));
                }
            }
            onChangeEvent(expenseTable, expenseEmptyHTML);

            expensePanel.add(expenseTable);
            expensePanel.add(expenseEmptyHTML);
            expensePanel.setVisible(true);
        }

        total = new HTML();
        total.setText(PayrollClientUtils.format(Optional.ofNullable(item.getTotal()).orElse(BigDecimal.ZERO)));
        totalTable.addItem(wfmStrings.total(), total);

        addBody(container);

        saveButton = new WfmButton2(wfmStrings.saveChanges(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        if (editable) {
            addFooter(saveButton);
        }
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

    private Widget[] getPaymentDeductionWidgets(PaymentDeductionObject paymentDeduction, final String from) {
        CategoryLookUp categoryLookUp = new CategoryLookUp(from, () -> true);
        categoryLookUp.setEnabled(editable);
        categoryLookUp.getSuggestBox().addSelectionHandler(e -> categoryLookUp.getSuggestBox().removeStyleName(ERROR_FORM_STYLE));

        EditableTextBox type = new EditableTextBox();
        type.setEnabled(false);
        type.setText(wfmStrings.fixed());

        PayslipItemAmountWidget amountWidget = new PayslipItemAmountWidget();
        amountWidget.setAmount(BigDecimal.ZERO);
        amountWidget.setEditable(editable);

        if (paymentDeduction != null) {
            if (paymentDeduction.getCategoryItem() != null) {
                categoryLookUp.addCategoryItem(paymentDeduction.getCategoryItem());
                categoryLookUp.setEnabled(false);
            }
            amountWidget.setSickRequestIds(paymentDeduction.getSickRequestids());
            amountWidget.setSalaryObject(paymentDeduction.isSalaryObject());
            amountWidget.setCashAdvance(paymentDeduction.isCashAdvance());
            amountWidget.setTaxable(paymentDeduction.getCategoryItem().getTaxable());

            if (paymentDeduction.getType() != null) {
                if (paymentDeduction.getType() == 0 || paymentDeduction.isLoan()) {
                    type.setText(wfmStrings.fixed());
                } else if (paymentDeduction.getType() == 1) {
                    type.setText(PayrollClientUtils.numberFormat(paymentDeduction.getPercentage()) + wfmStrings.basicOfPersentage());
                    amountWidget.setEditable(false);
                } else {
                    type.setText(PayrollClientUtils.numberFormat(paymentDeduction.getPercentage()) + wfmStrings.ofBasicAllowances());
                    amountWidget.setEditable(false);
                }
            }

            if (paymentDeduction.getPercentage() != null) {
                amountWidget.setPercentage(paymentDeduction.getPercentage());
            }
            if (paymentDeduction.getPaymentAmount() != null) {
                amountWidget.setAmount(paymentDeduction.getPaymentAmount());
            }

            if (Constants.LEAVE_DEDUCTIONS.equals(paymentDeduction.getCategoryItem().getCode())) {
                amountWidget.setNumberOfWorkDays(paymentDeduction.getNumberOfWorkDays());
                amountWidget.setLeaveDaysCount(paymentDeduction.getLeaveDaysCount());
                amountWidget.setLeaveDeductType(paymentDeduction.getLeaveType());
            } else if (Constants.LEAVE_ENCHASHMENT.equals(paymentDeduction.getCategoryItem().getCode())) {
                amountWidget.setLeaveDaysCount(paymentDeduction.getLeaveDaysCount());
            }

            if (paymentDeduction.getLeavePaymentItem() != null) {
                amountWidget.setLeavePaymentItem(paymentDeduction.getLeavePaymentItem());
            }

            amountWidget.setFromAllAllowances(paymentDeduction.isFromAllAllowances());
            amountWidget.setLinkedCategories(paymentDeduction.getLinkedCategories());

            if (PayrollConstants.CATEGORY_DEDUCTION.equals(from) && paymentDeduction.isLoan()) {
                amountWidget.setLoan(true);
                amountWidget.setRemainingAmount(paymentDeduction.getRemainingAmount());
                amountWidget.getAmountTextBox().addKeyUpHandler(keyUpEvent -> {
                    if (amountWidget.getRemainingAmount() != null && amountWidget.getAmount().compareTo(amountWidget.getRemainingAmount()) > 0) {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setWidth("400px");
                        messageBox.setTitle(wfmStrings.confirmation());
                        messageBox.setMessage("Amount of the loan cannot be more than the remaining amount. Do you want to deduct the remaining amount? ");
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onCancel() {
                                amountWidget.setAmount(null);
                                messageBox.close();
                            }

                            @Override
                            public void onSubmit() {
                                amountWidget.setAmount(amountWidget.getRemainingAmount());
                            }
                        });
                        messageBox.open();
                    }
                    calculate();
                });
            }
            amountWidget.setItemID(paymentDeduction.getId());
        }

        amountWidget.getAmountTextBox().addKeyUpHandler(e -> {
            amountWidget.getAmountTextBox().removeStyleName(ERROR_FORM_STYLE);
            updatePaymentDeductionTables();
        });

        return new Widget[]{categoryLookUp, type, amountWidget};
    }

    private void updatePaymentDeductionTables() {
        BigDecimal salary = PayrollClientUtils.parseToBigDecimal(basicSalaryBox.getText());

        for (int i = 0; i < paymentTable.getGrid().getRowCount(); i++) {
            PayslipItemAmountWidget amount = (PayslipItemAmountWidget) paymentTable.getColumnById(i, "amount");
            if (amount.getPercentage() != null) {
                CustomCell amountWidgetCell = (CustomCell) paymentTable.getColumnCellWidgetById(i, "amount");
                amount.setAmount(salary.multiply(amount.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, RoundingMode.HALF_UP));
                amountWidgetCell.InActive();
            }
        }

        BigDecimal nonTaxableDeductions = BigDecimal.ZERO;
        for (int i = 0; i < deductionTable.getGrid().getRowCount(); i++) {
            PayslipItemAmountWidget amount = (PayslipItemAmountWidget) deductionTable.getColumnById(i, "amount");
            if (amount.getPercentage() != null) {
                CustomCell amountWidgetCell = (CustomCell) deductionTable.getColumnCellWidgetById(i, "amount");
                GWT.log("deduction: " + amount.getItemID() + " percentage: " + amount.getPercentage());

                if (amount.isFromAllAllowances()) {
                    BigDecimal allowanceTotal = BigDecimal.ZERO;
                    allowanceTotal = getTableTotal(paymentTable, null);
                    amount.setAmount(salary.add(allowanceTotal).multiply(amount.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, RoundingMode.HALF_UP));
                } else if (amount.getLinkedCategories() != null && amount.getLinkedCategories().size() > 0) {
                    BigDecimal allowanceTotal = BigDecimal.ZERO;
                    allowanceTotal = getTableTotal(paymentTable, amount.getLinkedCategories());
                    amount.setAmount(salary.add(allowanceTotal).multiply(amount.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, RoundingMode.HALF_UP));
                } else {
                    amount.setAmount(salary.multiply(amount.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, RoundingMode.HALF_UP));
                }
                if (!amount.isTaxable()) {
                    nonTaxableDeductions = nonTaxableDeductions.add(amount.getAmount());
                }

                amountWidgetCell.InActive();
            }
        }

        for (int i = 0; i < taxTable.getGrid().getRowCount(); i++) {
            PayslipItemAmountWidget amount = (PayslipItemAmountWidget) taxTable.getColumnById(i, "amount");
            if (amount.getPercentage() != null) {
                CustomCell amountWidgetCell = (CustomCell) taxTable.getColumnCellWidgetById(i, "amount");

                if (amount.isFromAllAllowances()) {
                    BigDecimal allowanceTotal = BigDecimal.ZERO;
                    allowanceTotal = getTableTotal(paymentTable, null);
                    amount.setAmount(salary.add(allowanceTotal).subtract(nonTaxableDeductions).multiply(amount.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, RoundingMode.HALF_UP));
                } else if (amount.getLinkedCategories() != null && amount.getLinkedCategories().size() > 0) {
                    BigDecimal allowanceTotal = BigDecimal.ZERO;
                    allowanceTotal = getTableTotal(paymentTable, amount.getLinkedCategories());
                    amount.setAmount(salary.add(allowanceTotal).subtract(nonTaxableDeductions).multiply(amount.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, RoundingMode.HALF_UP));
                } else {
                    amount.setAmount(salary.subtract(nonTaxableDeductions).multiply(amount.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, RoundingMode.HALF_UP));
                }

                amountWidgetCell.InActive();
            }
        }

        for (int i = 0; i < employerContributionTable.getGrid().getRowCount(); i++) {
            PayslipItemAmountWidget amount = (PayslipItemAmountWidget) employerContributionTable.getColumnById(i, "amount");
            if (amount.getPercentage() != null) {
                CustomCell amountWidgetCell = (CustomCell) employerContributionTable.getColumnCellWidgetById(i, "amount");

                if (amount.isFromAllAllowances()) {
                    BigDecimal allowanceTotal = BigDecimal.ZERO;
                    allowanceTotal = getTableTotal(paymentTable, null);
                    amount.setAmount(allowanceTotal.multiply(amount.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, RoundingMode.HALF_UP));
                } else if (amount.getLinkedCategories() != null && amount.getLinkedCategories().size() > 0) {
                    BigDecimal allowanceTotal = BigDecimal.ZERO;
                    allowanceTotal = getTableTotal(paymentTable, amount.getLinkedCategories()).add(salary);
                    amount.setAmount(allowanceTotal.multiply(amount.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, RoundingMode.HALF_UP));
                } else {
                    amount.setAmount(salary.multiply(amount.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, RoundingMode.HALF_UP));
                }

                amountWidgetCell.InActive();
            }
        }

        calculate();
    }

    private ColumnConfig[] getExpenseColumns() {
        return new ColumnConfig[]{
                new ColumnConfig(CustomCell.class, "category", Property.get(Constants.EXPENSES_CLAIM, wfmStrings.expense()), 190, true, Constants.LEFT_ALIGN_CELL),
                new ColumnConfig(CustomCell.class, "amount", wfmStrings.amount(), 140, true, Constants.RIGHT_ALIGN_CELL),
                new ColumnConfig(LookUpCell.class, "paidfrom", wfmStrings.paidFrom(), 190, true)
        };
    }

    private Widget[] getExpenseWidgets(ExpenseData expense) {
        ExtendedHTMLCell title = new ExtendedHTMLCell();
        title.setText(expense.getTitle());

        PayslipItemAmountWidget amount = new PayslipItemAmountWidget();
        amount.setItemID(expense.getObjectID());
        amount.setAmount(BigDecimal.valueOf(expense.getAmount()));

        PaymentAccountsLookUp paidFromAccount = new PaymentAccountsLookUp();
        if (expense.getAccountID() != null) {
            paidFromAccount.setSelected(new SelectItem(expense.getAccountID(), expense.getAccount()));
        }
        paidFromAccount.setEnabled(editable);

        return new Widget[]{title, amount, paidFromAccount};
    }

    private BigDecimal getTableTotal(EditableTable table, List<PaymentDeductionObject> linkedCategories) {
        BigDecimal result = BigDecimal.ZERO;

        if (linkedCategories != null) {
            for (int i = 0; table != null && i < table.getRowCount(); i++) {
                CategoryLookUp categoryLookUp = (CategoryLookUp) table.getColumnById(i, "category");
                PayslipItemAmountWidget amountTextBox = (PayslipItemAmountWidget) table.getColumnById(i, "amount");

                if (categoryLookUp.getSelectedData() != null) {
                    for (PaymentDeductionObject item : linkedCategories) {
                        if (item.getCategoryItem().getId().equals(categoryLookUp.getSelectedData().getId())) {
                            result = result.add(amountTextBox.getAmount());
                            break;
                        }
                    }
                }
            }
        } else {
            for (int i = 0; table != null && i < table.getRowCount(); i++) {
                PayslipItemAmountWidget amountTextBox = (PayslipItemAmountWidget) table.getColumnById(i, "amount");
                result = result.add(amountTextBox.getAmount());
            }
        }

        return result;
    }

    private void calculate() {
        BigDecimal result = PayrollClientUtils.parseToBigDecimal(basicSalaryBox.getText());
        BigDecimal pensionAmount = result;

        for (int i = 0; i < paymentTable.getRowCount(); i++) {
            PayslipItemAmountWidget amountTextBox = (PayslipItemAmountWidget) paymentTable.getColumnById(i, "amount");
            result = result.add(amountTextBox.getAmount());
            CategoryLookUp categoryLookUp = (CategoryLookUp) paymentTable.getColumnById(i, "category");
            if (item.getPensionAllowances() != null && !item.getPensionAllowances().isEmpty() && amountTextBox != null) {
                for (PaymentDeductionSelectItem selectItem : item.getPensionAllowances()) {
                    if (categoryLookUp.getSelectedData().getName().equals(selectItem.getName())) {
                        pensionAmount = pensionAmount.add(amountTextBox.getAmount());
                    }
                }
            }
        }
        if (item.getPensionRate() != null && item.getPensionRate().compareTo(BigDecimal.ZERO) != 0) {
            pensionAmount = pensionAmount.multiply(item.getPensionRate()).divide(BigDecimal.valueOf(100), calculationScale, RoundingMode.HALF_UP);
        }
        if (pensionTotal != null) {
            pensionTotal.setHTML(PayrollClientUtils.format(Optional.ofNullable(pensionAmount).orElse(BigDecimal.ZERO)));
            result = result.subtract(pensionAmount);
        }

        for (int i = 0; i < deductionTable.getRowCount(); i++) {
            PayslipItemAmountWidget amountTextBox = (PayslipItemAmountWidget) deductionTable.getColumnById(i, "amount");
            result = result.subtract(amountTextBox.getAmount());
        }

        for (int i = 0; i < taxTable.getRowCount(); i++) {
            PayslipItemAmountWidget amountTextBox = (PayslipItemAmountWidget) taxTable.getColumnById(i, "amount");
            result = result.subtract(amountTextBox.getAmount());
        }

        for (int i = 0; expenseTable != null && i < expenseTable.getRowCount(); i++) {
            PayslipItemAmountWidget amountTextBox = (PayslipItemAmountWidget) expenseTable.getColumnById(i, "amount");
            result = result.add(amountTextBox.getAmount());
        }

        total.setHTML(PayrollClientUtils.format(result));
    }

    private void save() {
        if (!valid()) {
            return;
        }
        item = getData();
        saveHandler.accept(grid.getCurrentRow(), item);
        remove();
    }

    private SinglePayrunItem getData() {
        item.setBasicSalary(PayrollClientUtils.parseToBigDecimal(basicSalaryBox.getText()));
        if (pensionTotal != null) {
            item.setPensionAmount(PayrollClientUtils.parseToBigDecimal(pensionTotal.getText()));
        }
        item.setPaymentCategories(getCategories(paymentTable));
        item.setAllowance(getTableTotal(paymentTable, null));
        item.setDeductionCategories(getCategories(deductionTable));
        item.setDeduction(getTableTotal(deductionTable, null));
        item.setTaxCategories(getCategories(taxTable));
        item.setTax(getTableTotal(taxTable, null));
        item.setEmployerContributionCategories(getCategories(employerContributionTable));
        item.setEmployerContribution(getTableTotal(employerContributionTable, null));
        if (item.getEmployeeExpenses() != null) {
            item.getEmployeeExpenses().setExpenses(getEmployeeExpenses());
            item.getEmployeeExpenses().setPaymentAmount(getTableTotal(expenseTable, null));
        }
        item.setTotal(PayrollClientUtils.parseToBigDecimal(total.getText().replace(" ", "")));

        return item;
    }

    public ArrayList<PaymentDeductionObject> getCategories(EditableTable table) {
        final ArrayList<PaymentDeductionObject> result = new ArrayList<>();

        for (int i = 0; i < table.getRowCount(); i++) {
            CategoryLookUp categoryLookUp = (CategoryLookUp) table.getColumnById(i, "category");
            PayslipItemAmountWidget amountTextBox = (PayslipItemAmountWidget) table.getColumnById(i, "amount");
            if (categoryLookUp.getSelectedData() == null) {
                continue;
            }

            PaymentDeductionObject object = new PaymentDeductionObject();
            object.setId(amountTextBox.getItemID());
            object.setCategoryItem(categoryLookUp.getSelectedData());
            object.setPaymentAmount(amountTextBox.getAmount());
            object.setSickRequestids(categoryLookUp.getSickRequestIds());
            object.setLeavePaymentItem(categoryLookUp.getLeavePaymentItem());
            result.add(object);
        }
        return result;
    }

    public ExpenseData[] getEmployeeExpenses() {
        List<ExpenseData> expenseDataList = new ArrayList<>();

        for (int i = 0; expenseTable != null && i < expenseTable.getRowCount(); i++) {
            ExtendedHTMLCell title = (ExtendedHTMLCell) expenseTable.getColumnById(i, "category");
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) expenseTable.getColumnById(i, "amount");
            PaymentAccountsLookUp paidFrom = (PaymentAccountsLookUp) expenseTable.getColumnById(i, "paidfrom");
            ExpenseData data = new ExpenseData();
            data.setObjectID(amountWidget.getItemID());
            data.setTitle(title.getText());
            data.setAmount(amountWidget.getAmount().doubleValue());
            data.setAccountID(paidFrom.getSelectedItemID());
            data.setAccount(paidFrom.getSelectedItem().getName());
            expenseDataList.add(data);
        }
        return expenseDataList.toArray(new ExpenseData[]{});
    }

    private boolean valid() {
        int errors = 0;
        for (int i = 0; i < paymentTable.getRowCount(); i++) {
            CategoryLookUp category = (CategoryLookUp) paymentTable.getColumnById(i, "category");
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) paymentTable.getColumnById(i, "amount");

            if (category.getSelectedData() != null) {
                if (amountWidget.getAmountTextBox() == null || amountWidget.getAmountTextBox().getText().isEmpty() || BigDecimal.ZERO.compareTo(amountWidget.getAmount()) >= 0) {
                    paymentTable.setColumnValid("amount");
                    amountWidget.getAmountTextBox().addStyleName(ERROR_FORM_STYLE);
                    errors++;
                }
            }
            if (amountWidget.getAmountTextBox() != null && !amountWidget.getAmountTextBox().getText().isEmpty() && BigDecimal.ZERO.compareTo(amountWidget.getAmount()) < 0) {
                if (category.getSelectedItem() == null) {
                    paymentTable.notValid(i, "category");
                    errors++;
                }
            }

        }

        for (int i = 0; i < deductionTable.getRowCount(); i++) {
            CategoryLookUp category = (CategoryLookUp) deductionTable.getColumnById(i, "category");
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) deductionTable.getColumnById(i, "amount");

            if (category.getSelectedData() != null) {
                if (amountWidget.getAmountTextBox() == null || amountWidget.getAmountTextBox().getText().isEmpty() || BigDecimal.ZERO.compareTo(amountWidget.getAmount()) >= 0) {
                    deductionTable.setColumnValid("amount");
                    amountWidget.getAmountTextBox().addStyleName(ERROR_FORM_STYLE);
                    errors++;
                }
            }
            if (amountWidget.getAmountTextBox() != null && !amountWidget.getAmountTextBox().getText().isEmpty() && BigDecimal.ZERO.compareTo(amountWidget.getAmount()) < 0) {
                if (category.getSelectedItem() == null) {
                    deductionTable.notValid(i, "category");
                    errors++;
                }
            }
        }

        for (int i = 0; i < taxTable.getRowCount(); i++) {
            CategoryLookUp category = (CategoryLookUp) taxTable.getColumnById(i, "category");
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) taxTable.getColumnById(i, "amount");

            if (category.getSelectedData() != null) {
                if (amountWidget.getAmountTextBox() == null || amountWidget.getAmountTextBox().getText().isEmpty() || BigDecimal.ZERO.compareTo(amountWidget.getAmount()) >= 0) {
                    taxTable.setColumnValid("amount");
                    amountWidget.getAmountTextBox().addStyleName(ERROR_FORM_STYLE);
                    errors++;
                }
            }
            if (amountWidget.getAmountTextBox() != null && !amountWidget.getAmountTextBox().getText().isEmpty() && BigDecimal.ZERO.compareTo(amountWidget.getAmount()) < 0) {
                if (category.getSelectedItem() == null) {
                    taxTable.notValid(i, "category");
                    errors++;
                }
            }
        }

        for (int rowID = 0; expenseTable != null && rowID < expenseTable.getRowCount(); rowID++) {
            PaymentAccountsLookUp paidAccount = (PaymentAccountsLookUp) expenseTable.getColumnById(rowID, "paidfrom");

            if (!Validation.validateLookUpRequired(paidAccount)) {
                errors++;
            }
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        } else if (PayrollClientUtils.parseToBigDecimal(total.getText()).compareTo(BigDecimal.ZERO) < 0) {
            Info.show(wfmStrings.payslipCantBeNegative(), Info.Type.WARNING);
            return false;
        }

        return true;
    }

    public void setSaveHandler(BiConsumer<Integer, SinglePayrunItem> saveHandler) {
        this.saveHandler = saveHandler;
    }

    public void setGrid(EditableGrid grid) {
        this.grid = grid;
    }
}
