package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.HTML;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExtendedHTML extends HTML implements CustomCellInterface {
    private static final NumberFormat defaultNumberFormat = Utils.getCalculationNumberFormat();
    private BigDecimal value;

    public ExtendedHTML() {
        super();
    }

    public ExtendedHTML(String html) {
        super(html);
    }

    @Override
    public String getDisplayValue() {
        return getHTML();
    }

    @Override
    public void setItemValue(Object value) {
    }

    @Override
    public void setItemFocus(boolean focused) {

    }

    public void setValue(BigDecimal value) {
        // high precision value (getValue) use it for calculations
        this.value = value;
        // formatted value from finance settings (getText) use it for display
        setHTML(format(value));
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getValueString() {
        if (value != null) {
            return value.toString();
        }
        String text = getText();
        return (text == null || text.isEmpty()) ? "0.00" : text;
    }

    public String format(BigDecimal bigDecimal) {
        return defaultNumberFormat.format(bigDecimal.setScale(getPriceScale(), RoundingMode.HALF_UP).doubleValue());
    }

    public static int getPriceScale() {
        if (Utils.getAccountingCalculationScale() != null) {
            return Utils.getAccountingCalculationScale();
        }
        return 2;
    }

}
