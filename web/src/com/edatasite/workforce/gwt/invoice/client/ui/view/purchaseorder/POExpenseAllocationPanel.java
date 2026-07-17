package com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseorder;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseListItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 1/17/12
 * Time: 5:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class POExpenseAllocationPanel extends AbstractExpenseAllocation {

    public static Integer calculationScale = AccountingUtils.getPriceScale();

    private HashMap<Integer, BigDecimal> allocatedExpenses = new HashMap<>();
    private DynamicTable productsTable;
    private NewInvoice invoiceData;

    public POExpenseAllocationPanel(BigDecimal totalReceivedExpenses, DynamicTable productsTable, NewInvoice invoiceData, boolean readOnly) {
        super(totalReceivedExpenses);
        this.productsTable = productsTable;
        this.invoiceData = invoiceData;
        allocationTypeListBox.setEnabled(!readOnly);

        if (invoiceData != null && invoiceData.getExpenseAllocationType() != null) {
            allocationTypeListBox.setSelected(invoiceData.getExpenseAllocationType());
        } else {
            allocationTypeListBox.setSelected(ALLOCATE_BY_AMOUNT);
        }
    }

    @Override
    protected void calculate(Integer allocationType) {
        BigDecimal remainingAllocation = getRemainingBalance();
        AllocationType.getById(allocationType).calculateAllocatedAmount(invoiceData, remainingAllocation, productsTable);

        if (DONOT_ALLOCATE.equals(allocationType)) {
            allocatedExpenses.clear();
        } else {
            applyCheckedExpenses();
        }
        setAllocatedExpenses(allocatedExpenses);
    }

    protected SelectItem[] getAllocationTypes() {
        return new SelectItem[]{
                new SelectItem(ALLOCATE_BY_QTY, accountingStrings.allocateByQty()),
                new SelectItem(ALLOCATE_RECEIVE_ITEM_BY_QTY, accountingStrings.allocateReceiveItemByQty()),
                new SelectItem(ALLOCATE_BY_AMOUNT, accountingStrings.allocateByAmount()),
                new SelectItem(ALLOCATE_RECEIVE_ITEM_BY_AMOUNT, accountingStrings.allocateReceiveItemByAmount()),
                new SelectItem(ALLOCATE_MANUALLY, accountingStrings.allocateManually()),
                new SelectItem(DONOT_ALLOCATE, accountingStrings.doNotAllocate())
        };
    }

    protected void setAllocationType() {
        allocationTypeListBox.setSelected(new SelectItem(ALLOCATE_BY_AMOUNT, accountingStrings.allocateByAmount()));
//        calculate(allocationTypeListBox.getSelectedId());
    }

    private void applyCheckedExpenses() {
        allocatedExpenses.clear();
        for (ExpenseListItem ei : expenseItems) {
            allocatedExpenses.put(ei.getId(), ei.getBaseSubtotal());
        }
        setAllocatedExpenses(allocatedExpenses);
    }

    private void setAllocatedExpenses(HashMap<Integer, BigDecimal> allocatedExpenses) {
        invoiceData.setAllocatedExpenses(allocatedExpenses);
    }
}
