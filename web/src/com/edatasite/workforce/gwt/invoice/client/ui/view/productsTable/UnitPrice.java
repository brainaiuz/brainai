package com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.ui.wfmtooltip.WfmToolTipListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import gwt.material.design.jquery.client.api.JQuery;

import java.math.BigDecimal;
import java.util.HashMap;

public class UnitPrice extends TextBox implements CustomCellInterface {
//    static final String ON_CLIENT_PRICELEVEL_CHANGE = "onClientPriceLevelChange";

    private BigDecimal value;
    private BigDecimal valueInBaseCurrency;
    private BigDecimal exchangeRateValue;
    private BigDecimal netAmount;
    private BigDecimal totalAmount;
    private HashMap<String, BigDecimal> multiPricesMap;
    private boolean ignoreMultiPrice = true;
    private Integer currencyId;

    UnitPrice(BigDecimal exchangeRateValue, Integer currencyId) {
        super();
        addKeyboardListener();
        this.exchangeRateValue = exchangeRateValue;
        this.currencyId = currencyId;
    }

    public void addKeyboardListener() {
        setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        addKeyUpHandler(keyUpEvent -> {
            TextBox textBox = (TextBox) keyUpEvent.getSource();
            if (!textBox.getText().equals("-")) {
                recalculate(textBox);
                JQuery.$(UnitPrice.this).trigger(ProductsTable.CALCULATE, null);
                if (keyUpEvent.isControlKeyDown()) {
                    // Ctrl+C (Copy)
                    if (keyUpEvent.getNativeKeyCode() == 'V' || keyUpEvent.getNativeKeyCode() == 'v') {
                        textBox.setText(this.value.toString());
                    }
                }
            }
        });

        addFocusHandler(focusEvent -> {
            if (getValue() != null && !getValue().isEmpty()) {
                selectAll();
            }
        });

        addValueChangeHandler(changeEvent -> {
            TextBox textBox = (TextBox) changeEvent.getSource();
            if (!textBox.getText().equals("-")) {
                recalculate(textBox);
                JQuery.$(UnitPrice.this).trigger(ProductsTable.CALCULATE, null);
            }
        });

    }

    public void recalculate(TextBox textBox) {
        try {
            valueInBaseCurrency = (value = AccountingUtils.get().parseToBigDecimalWithValidate(textBox.getText())).divide(exchangeRateValue, 8, BigDecimal.ROUND_HALF_UP);
        } catch (NumberFormatException e) {
            setText(AccountingUtils.get().formatUnitPrice(BigDecimal.ZERO));
            valueInBaseCurrency = BigDecimal.ZERO;
            value = BigDecimal.ZERO;
        }
    }

    public void setValuableText(String text, BigDecimal value) {
        setText(text);
        setValue(value);
    }

    void setCostPriceTooltip(String costPrice) {
        WfmToolTipListener toolTipListener = new WfmToolTipListener(costPrice, 30000, "easyTooltip2");
            addMouseUpHandler(toolTipListener);
            addMouseDownHandler(toolTipListener);
        addFocusHandler(toolTipListener);
    }

    public BigDecimal getDoubleValue() {
        return value;
    }

    private void setValue(BigDecimal value) {
        this.value = value;
    }

    BigDecimal getValueInBaseCurrency() {
        return valueInBaseCurrency != null ? valueInBaseCurrency : BigDecimal.ZERO;
    }

    public void setValueInBaseCurrency(BigDecimal valueInBaseCurrency) {
        this.valueInBaseCurrency = valueInBaseCurrency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRateValue != null ? exchangeRateValue : BigDecimal.ZERO;
    }

    public void setExchangeRate(BigDecimal exchangeRateValue) {
        this.exchangeRateValue = exchangeRateValue;
    }

    BigDecimal getBigDecimalValue() {
        return AccountingUtils.get().parseToBigDecimal(ProductsTableUtils.getQuantityAsString(getText()));
    }

    @Override
    public String getDisplayValue() {
        return AccountingUtils.get().formatUnitPrice(getBigDecimalValue());
    }

    public void setItemValue(Object value) {
        setText(AccountingUtils.get().formatUnitPrice(AccountingUtils.parsePriceToBigDecimal((String) value)));
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    @Override
    public void setItemFocus(boolean focused) {
        setFocus(focused);
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    boolean isIgnoreMultiPrice() {
        return ignoreMultiPrice;
    }

    public void setIgnoreMultiPrice(boolean ignoreMultiPrice) {
        this.ignoreMultiPrice = ignoreMultiPrice;
    }

    HashMap<String, BigDecimal> getMultiPricesMap() {
        return multiPricesMap;
    }

    void setMultiPricesMap(HashMap<String, BigDecimal> multiPricesMap) {
        this.multiPricesMap = multiPricesMap;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }
}
