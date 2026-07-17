package com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseorder;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingData;
import com.google.gwt.user.client.Command;

import java.math.BigDecimal;
import java.util.HashMap;

public class GrnExpenseAllocationPanel extends AbstractExpenseAllocation {
    private HashMap<Integer, BigDecimal> allocatedExpenses = new HashMap<>();
    private DynamicTable itemsTable;
    private ShippingData shippingData;
    private Command cmdAllocate;

    public GrnExpenseAllocationPanel(BigDecimal totalReceivedExpenses, DynamicTable itemsTable, ShippingData shippingData, boolean readOnly, Command cmdAllocate) {
        super(totalReceivedExpenses);
        this.itemsTable = itemsTable;
        this.shippingData = shippingData;
        this.cmdAllocate = cmdAllocate;
        allocationTypeListBox.setEnabled(!readOnly);
    }

    @Override
    protected SelectItem[] getAllocationTypes() {
        return new SelectItem[]{
                new SelectItem(ALLOCATE_RECEIVE_ITEM_BY_QTY, accountingStrings.allocateReceiveItemByQty()),
                new SelectItem(ALLOCATE_RECEIVE_ITEM_BY_AMOUNT, accountingStrings.allocateReceiveItemByAmount()),
                new SelectItem(ALLOCATE_MANUALLY, accountingStrings.allocateManually()),
                new SelectItem(DONOT_ALLOCATE, accountingStrings.doNotAllocate())
        };
    }

    protected void setAllocationType() {
        allocationTypeListBox.setSelected(new SelectItem(DONOT_ALLOCATE, accountingStrings.doNotAllocate()));
    }

    @Override
    protected void calculate(Integer allocationType) {
        BigDecimal remainingAllocation = getTotalExpenses();
        AllocationType.getById(allocationType).calculateAllocatedAmount(shippingData, remainingAllocation, itemsTable);

        if (cmdAllocate != null) {
            cmdAllocate.execute();
        }
    }
}
