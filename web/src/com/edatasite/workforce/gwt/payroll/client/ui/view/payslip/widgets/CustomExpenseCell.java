package com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.widgets;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.rpc.ExpenseData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.TotalTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LinkableCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PaymentAccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.PayslipItemAmountWidget;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
import com.edatasite.workforce.gwt.payroll.client.ui.PayrollContants;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Murad Satimov
 * Date: 3/27/18 12:26 AM
 */
public class CustomExpenseCell extends BaseAdditionlCell {
    private final PaymentDeductionObject expenses;
    private BigDecimal payTotal;
    private BigDecimal dedTotal;
    private HTML deductionTotal;

    public CustomExpenseCell(SinglePayrunItem item, Integer currencyId, boolean editable) {
        super(item);
        this.currencyId = currencyId;
        this.editable = editable;
        expenses = item != null ? item.getEmployeeExpenses() : null;
        totalValue = expenses != null ? expenses.getPaymentAmount() : BigDecimal.ZERO;
        calculateExpenseTotals();
    }

    private void calculateExpenseTotals() {
        totalValue = BigDecimal.ZERO;
        payTotal = BigDecimal.ZERO;
        dedTotal = BigDecimal.ZERO;

        if (expenses == null || expenses.getExpenses() == null) {
            return;
        }
        for (ExpenseData exp : expenses.getExpenses()) {
            if (exp.getPaymentType() == null || exp.getPaymentType().equals(0)) {
                payTotal = payTotal.add(BigDecimal.valueOf(exp.getAmount()));
            } else {
                dedTotal = dedTotal.add(BigDecimal.valueOf(exp.getAmount()));
            }
            totalValue = totalValue.add(BigDecimal.valueOf(exp.getAmount()));
        }
    }

    @Override
    protected void drawPopup() {
        categoriesDialogBox = new KpiModal();
        categoriesDialogBox.setWidth(600);
        categoriesTable = new EditableTable(getColumns(), editable);
        categoriesTable.setRemoveAllRows(true);
        categoriesTable.setRemoveRowListener(() -> {
            categoriesTable.getGrid().getModel().removeRow(categoriesTable.getGrid().getCurrentRow());
            applyTotal(categoriesTable, paymentTotal);
        });
        categoriesDialogBox.setTitle("Expense Reports");
        paymentTotal = new HTML(PayrollClientUtils.format(BigDecimal.ZERO));
        deductionTotal = new HTML(PayrollClientUtils.format(BigDecimal.ZERO));

        if (expenses != null && expenses.getExpenses() != null) {
            for (ExpenseData exp : expenses.getExpenses()) {
                addExpenseItem(exp);
            }
            recalculate();
        }

        categoriesDialogBox.add(categoriesTable);
        TotalTable totalTable = new TotalTable();
        totalTable.addItem(new Label(wfmStrings.paymentTotal()), paymentTotal);
        totalTable.addItem(new Label(wfmStrings.deductionsTotal()), deductionTotal);
        categoriesDialogBox.add(totalTable);
        if (!this.editable) {
            WfmButton2 close = new WfmButton2(wfmStrings.close());
            close.addClickHandler(clickEvent -> categoriesDialogBox.close());
            categoriesDialogBox.addButton(close);
        } else {
            WfmButton2 apply = new WfmButton2(wfmStrings.apply());

            apply.addClickHandler(clickEvent -> {
                if (isValid()) {
                    recalculate();
                    saveAndApplyData();
                    categoriesDialogBox.close();
                }
            });
            categoriesDialogBox.addButton(apply);
            WfmButton2 close = new WfmButton2(wfmStrings.close());
            close.addClickHandler(clickEvent -> categoriesDialogBox.close());
            categoriesDialogBox.addButton(close);
        }
    }

    private void saveAndApplyData() {
        if (saveHandler == null || !editable) {
            return;
        }
        item.setExpense(totalValue);
        item.setEmployeeExpenses(getEmployeeExpenses());

        CustomPaymentCell allowanceCell = (CustomPaymentCell) table.getColumnById(grid.getCurrentRow(), PayrollContants.ALLOWANCE);
        CustomPaymentCell deductioncell = (CustomPaymentCell) table.getColumnById(grid.getCurrentRow(), PayrollContants.DEDUCTION);
        CustomExpenseCell expenseCell = (CustomExpenseCell) table.getColumnById(grid.getCurrentRow(), PayrollContants.EXPENSE);
        PayslipItemAmountWidget pensionCell = (PayslipItemAmountWidget) table.getColumnById(grid.getCurrentRow(), PayrollContants.PENSION);
        BigDecimal total = item.getBasicSalary();

        total = total.add(allowanceCell.getTotalValue());
        total = total.add(expenseCell.getPayTotal());
        total = total.subtract(expenseCell.getDedTotal());
        total = total.subtract(deductioncell.getTotalValue());
        if (pensionCell != null && pensionCell.getAmount() != null) {
            total = total.subtract(pensionCell.getAmount());
        }
        item.setTotal(total);
        saveHandler.accept(grid.getCurrentRow(), item);
        LinkableCell cell = (LinkableCell) table.getColumnCellWidgetById(grid.getCurrentRow(), PayrollContants.EXPENSE);
        cell.displayActive(false);
    }

    public PaymentDeductionObject getEmployeeExpenses() {
        List<ExpenseData> expenseDataList = new ArrayList<>();

        for (int i = 0; categoriesTable != null && i < categoriesTable.getRowCount(); i++) {
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) categoriesTable.getColumnById(i, "amount");
            DataListBox type = (DataListBox) categoriesTable.getColumnById(i, "type");
            PaymentAccountsLookUp paidFrom = (PaymentAccountsLookUp) categoriesTable.getColumnById(i, "paidfrom");
            ExpenseData data = (ExpenseData) amountWidget.getObject();

            data.setPaymentType(type.getSelectedId(true));
            data.setAccountID(paidFrom.getSelectedItemID());
            expenseDataList.add(data);
        }
        if (expenses != null && categoriesTable != null) {
            expenses.setExpenses(expenseDataList.toArray(new ExpenseData[]{}));
        }
        return expenses;
    }


    private boolean isValid() {
        int errors = 0;
        if (categoriesTable != null) {
            for (int rowID = 0; categoriesTable != null && rowID < categoriesTable.getRowCount(); rowID++) {
                PaymentAccountsLookUp paidAccount = (PaymentAccountsLookUp) categoriesTable.getColumnById(rowID, "paidfrom");

                if (paidAccount.getSelectedItem() == null) {
                    errors++;
                }
            }
        } else if (expenses != null) {
            for (ExpenseData exp : expenses.getExpenses()) {
                if (exp.getAccountID() == null) {
                    errors++;
                }
            }
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void addExpenseItem(ExpenseData exp) {
        EditableTextBox title = new EditableTextBox();
        title.setEnabled(false);
        title.setText(exp.getTitle());

        PayslipItemAmountWidget amountWidget = new PayslipItemAmountWidget();
        amountWidget.setAmount(BigDecimal.valueOf(exp.getAmount()));
        amountWidget.getAmountTextBox().setEnabled(false);
        amountWidget.setBaseAmount(exp.isInBaseCurrency() ? BigDecimal.valueOf(exp.getAmount()) : null);
        amountWidget.setObject(exp);

        PaymentAccountsLookUp paidFromAccount = new PaymentAccountsLookUp();
        if (exp.getAccountID() != null) {
            paidFromAccount.setSelected(new SelectItem(exp.getAccountID(), exp.getAccount()));
        }
        paidFromAccount.setEnabled(editable);
        paidFromAccount.setCurrencyID(this.currencyId);
        DataListBox expensePaymentType = new DataListBox();
        expensePaymentType.setWithoutNullLabel(true);
        expensePaymentType.setItems(new SelectItem[]{
                new SelectItem(0, wfmStrings.payment()),
                new SelectItem(1, wfmStrings.deduction())
        });
        expensePaymentType.setSelected(0);
        expensePaymentType.addValueChangeHandler(changeEvent -> recalculate());
        expensePaymentType.setEnabled(editable);

        categoriesTable.addRow(new Widget[]{title, amountWidget, paidFromAccount, expensePaymentType});
    }

    private void recalculate() {
        totalValue = BigDecimal.ZERO;
        payTotal = BigDecimal.ZERO;
        dedTotal = BigDecimal.ZERO;

        for (int i = 0; i < categoriesTable.getRowCount(); i++) {
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) categoriesTable.getColumnById(i, "amount");
            DataListBox paymentType = (DataListBox) categoriesTable.getColumnById(i, "type");

            if (paymentType.getSelectedId(true).equals(0)) {
                payTotal = payTotal.add(amountWidget.getAmount());
            } else {
                dedTotal = dedTotal.add(amountWidget.getAmount());
            }
            totalValue = totalValue.add(amountWidget.getAmount());
            LinkableCell cell = (LinkableCell) table.getColumnCellWidgetById(grid.getCurrentRow(), PayrollContants.EXPENSE);
            if (cell != null) {
                cell.displayActive(false);
            }
        }
        paymentTotal.setHTML(PayrollClientUtils.format(payTotal));
        deductionTotal.setHTML(PayrollClientUtils.format(dedTotal));
    }

    @Override
    protected ColumnConfig[] getColumns() {
        return new ColumnConfig[]{
                new ColumnConfig(CustomCell.class, "category", Property.get(Constants.EXPENSES_CLAIM, wfmStrings.expense()), 120, true, Constants.LEFT_ALIGN_CELL),
                new ColumnConfig(CustomCell.class, "amount", wfmStrings.amount(), 100, true, Constants.RIGHT_ALIGN_CELL),
                new ColumnConfig(LookUpCell.class, "paidfrom", wfmStrings.paidFrom(), 150, true),
                new ColumnConfig(CustomCell.class, "type", wfmStrings.type(), 100, true, Constants.LEFT_ALIGN_CELL)
        };
    }

    public BigDecimal getPayTotal() {
        return payTotal;
    }

    public BigDecimal getDedTotal() {
        return dedTotal;
    }
}
