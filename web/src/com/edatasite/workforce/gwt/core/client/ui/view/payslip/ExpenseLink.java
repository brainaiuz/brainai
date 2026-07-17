package com.edatasite.workforce.gwt.core.client.ui.view.payslip;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.ExpenseData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PaymentAccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.*;

/**
 * Created by User on 12/15/2016.
 */
public class ExpenseLink extends Anchor implements IsSerializable {
    private TextBox amountTxtBox;
    private ExpenseData[] expenses;
    private KpiModal expenseDialogBox;
    private FlexTable expensesTable;
    private EditableTable paymentsTable;
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public ExpenseLink(ExpenseData[] expenses, TextBox amountTxtBox, EditableTable paymentsTable) {
        super(wfmStrings.details());
        this.expenses = expenses;
        this.amountTxtBox = amountTxtBox;
        drawExpensePanel();
        addClickHandler(clickEvent -> expenseDialogBox.open());
    }

    private void drawExpensePanel() {
        expenseDialogBox = new KpiModal();
        expensesTable = new FlexTable();
        expensesTable.getColumnFormatter().setWidth(0, "30px");
        expensesTable.getColumnFormatter().setWidth(1, "200px");
        expensesTable.getColumnFormatter().setWidth(2, "50px");
        expensesTable.getColumnFormatter().setWidth(3, "120px");
        expensesTable.setWidget(0, 1, new HTML("<b>" + Property.get(Constants.EXPENSES_CLAIM, wfmStrings.expense()) + ":</b>"));
        expensesTable.setWidget(0, 2, new HTML("<b><center>" + wfmStrings.amount() + ":</center></b>"));
        expensesTable.setWidget(0, 3, new HTML("<b><center>" + wfmStrings.paidFrom() + ":</center></b>"));
        if (expenses != null && expenses.length > 0) {
            int row = 1;
            for (ExpenseData exp : expenses) {
                ExtendedCheckBox checkBox = new ExtendedCheckBox(exp);
                PaymentAccountsLookUp accountsLookUp = new PaymentAccountsLookUp();
                accountsLookUp.getSuggestBox().setWidth("120px");
                if (exp.getAccountID() != null) {
                    accountsLookUp.setSelected(new SelectItem(exp.getAccountID(), exp.getAccount()));
                }
                expensesTable.setWidget(row, 0, checkBox);
                expensesTable.setWidget(row, 1, new Label(exp.getTitle()));
                expensesTable.setWidget(row, 2, new Label(Utils.formatDouble(exp.getAmount())));
                expensesTable.setWidget(row, 3, accountsLookUp);
                row++;
            }
        }
        ScrollPanel scrollPanel = new ScrollPanel();
        scrollPanel.setSize("300px", "150px");
        scrollPanel.add(expensesTable);
        expenseDialogBox.setSize(300, 200);
        expenseDialogBox.add(scrollPanel);

        Button apply = new Button(wfmStrings.apply());
        apply.addClickHandler(clickEvent -> {
            if (isEnabledExpenseAccountsSelected()) {
                double amount = 0d;
                expenses = new ExpenseData[expensesTable.getRowCount() - 1];
                int j = 0;
                for (int i = 1; i < expensesTable.getRowCount(); i++) {
                    ExtendedCheckBox checkBox = (ExtendedCheckBox) expensesTable.getWidget(i, 0);
                    PaymentAccountsLookUp lookUp = (PaymentAccountsLookUp) expensesTable.getWidget(i, 3);
                    if (lookUp.getSelectedItem() != null) {
                        checkBox.getExpense().setAccountID(lookUp.getSelectedItem().getId());
                        checkBox.getExpense().setAccount(lookUp.getSelectedItem().getName());
                    } else {
                        checkBox.getExpense().setAccountID(null);
                        checkBox.getExpense().setAccount(null);
                    }
                    if (checkBox.getValue()) {
                        amount += checkBox.getExpense().getAmount();
                    }
                    expenses[j] = checkBox.getExpense();
                    j++;
                }

                amountTxtBox.setText(Utils.formatDouble(amount));
                int currentRow = paymentsTable.getGrid().getCurrentRow();
                if (currentRow > 0) {
                    CustomCell amountCell = (CustomCell) paymentsTable.getColumnCellWidgetById(currentRow, "amount");
                    amountCell.InActive();
                }

                expenseDialogBox.close();
            } else {
                Info.show(wfmStrings.pleaseSelectPaidAccount(), Info.Type.WARNING);
            }
        });
        expenseDialogBox.addButton(apply);
    }

    public boolean isEnabledExpenseAccountsSelected() {
        for (int i = 1; i < expensesTable.getRowCount(); i++) {
            ExtendedCheckBox checkBox = (ExtendedCheckBox) expensesTable.getWidget(i, 0);
            PaymentAccountsLookUp lookUp = (PaymentAccountsLookUp) expensesTable.getWidget(i, 3);
            if (checkBox.getValue() && lookUp.getSelectedItem() == null) {
                return false;
            }
        }
        return true;
    }

    public void setExpenses(ExpenseData[] expenses) {
        this.expenses = expenses;
    }

    public class ExtendedCheckBox extends KpiCheckBox {
        private ExpenseData expense;

        public ExtendedCheckBox(ExpenseData exp) {
            this.expense = exp;
            setValue(expense.isApplied());
            addClickHandler(clickEvent -> expense.setApplied(getValue()));
        }

        public ExpenseData getExpense() {
            return expense;
        }
    }

    public ExpenseData[] getExpenses() {
        return expenses;
    }
}
