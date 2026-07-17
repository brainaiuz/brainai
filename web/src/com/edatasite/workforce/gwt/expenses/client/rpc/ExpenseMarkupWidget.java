package com.edatasite.workforce.gwt.expenses.client.rpc;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.rpc.BillableExpenseItem;
import com.google.gwt.user.client.ui.HTML;

import java.math.BigDecimal;

public class ExpenseMarkupWidget extends HTML {
    private BillableExpenseItem billableExpenseItem;
    private BigDecimal markupAmount;
    private BigDecimal effectiveTaxPercent;

    public ExpenseMarkupWidget(String html) {
        super(html);
    }

    public BillableExpenseItem getBillableExpenseItem() {
        return billableExpenseItem;
    }

    public void setBillableExpenseItem(BillableExpenseItem billableExpenseItem) {
        this.billableExpenseItem = billableExpenseItem;
    }

    public void setMarkupAmount(BigDecimal markupAmount, BigDecimal exchangeRate) {
        billableExpenseItem.setMarkupAmount(markupAmount);
        billableExpenseItem.setMarkupAmountInBase(markupAmount.divide(exchangeRate, AccountingUtils.systemCalculationScale, BigDecimal.ROUND_HALF_UP));
    }

    public BigDecimal getEffectiveTaxPercent() {
        return effectiveTaxPercent;
    }

    public void setEffectiveTaxPercent(BigDecimal effectiveTaxPercent) {
        this.effectiveTaxPercent = effectiveTaxPercent;
    }
}
