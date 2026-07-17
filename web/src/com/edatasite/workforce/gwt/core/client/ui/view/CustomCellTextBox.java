package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.TextBox;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/27/12
 * Time: 5:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomCellTextBox extends TextBox implements CustomCellInterface {

    private static NumberFormat qtyNumberFormat = NumberFormat.getFormat(",##0.00");

    private BigDecimal baseCurrencyValue;
    private boolean numeric;

    public BigDecimal getBaseCurrencyValue() {
        return baseCurrencyValue;
    }

    public void setBaseCurrencyValue(BigDecimal baseCurrencyValue) {
        this.baseCurrencyValue = baseCurrencyValue;
    }

    public CustomCellTextBox() {
        super();
    }

    public CustomCellTextBox(boolean numeric) {
        super();
        this.numeric = numeric;
    }

    @Override
    public String getDisplayValue() {
        return numeric ? formatQty(parsePriceToBigDecimal(getText())) : getText();
    }

    @Override
    public void setItemValue(Object value) {
        setText(String.valueOf(value));
    }

    @Override
    public void setItemFocus(boolean focused) {
        setFocus(focused);
    }

    public static BigDecimal parsePriceToBigDecimal(String text) {
        if (text != null && text.length() > 0) {
            NumberFormat priceFormat = NumberFormat.getFormat(",##0.00000");
            return new BigDecimal(Utils.universalParse(priceFormat, text));
        }
        return BigDecimal.ZERO;
    }

    public String formatQty(BigDecimal bigDecimal) {
        return qtyNumberFormat.format(bigDecimal.setScale(Utils.getAccountingCustomQtyScale() != null ? Utils.getAccountingCustomQtyScale() : 2, BigDecimal.ROUND_HALF_UP).doubleValue());
    }
}
