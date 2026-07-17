package com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseorder;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 2/17/12
 * Time: 9:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class AllocationTextBox extends TextBox {
    private BigDecimal receivedExpenseAmount;
    private static NumberFormat numberFormat = NumberFormat.getFormat(",##0" + Utils.generateDecimalByScale(AccountingUtils.systemCalculationScale));

    public AllocationTextBox(BigDecimal receivedExpenseAmount) {
        this.receivedExpenseAmount = receivedExpenseAmount != null ? receivedExpenseAmount : BigDecimal.ZERO;
        applyParameters();
        allocateAmount(this.receivedExpenseAmount);
    }

    private void applyParameters() {
        setWidth("90px");
        setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(this, 2);
        setEnabled(false);
        setText(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
    }

    public void allocateAmount(BigDecimal amountToAllocate) {
        setText(numberFormat.format(amountToAllocate.setScale(AccountingUtils.systemCalculationScale, BigDecimal.ROUND_HALF_UP).doubleValue()));
    }

    public BigDecimal getAllocatedAmount() {
        if (getText() != null && !"".equals(getText().trim())) {
            return AccountingUtils.get().parseToBigDecimal(getText()).setScale(AccountingUtils.systemCalculationScale, BigDecimal.ROUND_HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getReceivedExpenseAmount() {
        return receivedExpenseAmount;
    }
}
