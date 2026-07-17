package com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.*;

import java.math.BigDecimal;

public class Discount extends HorizontalPanel implements CustomCellInterface {

    private TextBox txtDiscount;
    private Label lblCurrencyUnit;
    private BigDecimal value;
    private BigDecimal valueInBaseCurrency;
    private BigDecimal exchangeRateValue;
    private Command onDiscountChange;
    private AccountingUtils utils = AccountingUtils.get();
    public Discount(BigDecimal exchangeRateValue) {
        super();
        this.exchangeRateValue = exchangeRateValue;
        initialize();
    }

    private void initialize() {
        clear();
        txtDiscount = new TextBox();
        txtDiscount.setText(utils.getDiscountZero());
        txtDiscount.setWidth("47px");
        txtDiscount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(txtDiscount, utils.getDiscountScale());
        Validation.checkToFocusTextBox(txtDiscount, utils.getDiscountZero());
        txtDiscount.addKeyUpHandler(event -> {
            BigDecimal discountAmount = BigDecimal.ZERO;
            try {
                discountAmount = utils.parseToBigDecimal(txtDiscount.getText(), utils.discountNumberFormat);
            } catch (NumberFormatException ex) {
                txtDiscount.setText(utils.getDiscountZero());
            } finally {
                String discUnit = ((Label) getWidget(1)).getText();
                if (discUnit.equals(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT) && discountAmount.compareTo(AccountingConstants.HUNDRED) > 0) {
                    txtDiscount.setText(txtDiscount.getText().substring(0, utils.getDiscountScale()));
                    discountAmount = utils.parseToBigDecimal(txtDiscount.getText(), utils.discountNumberFormat);
                }
                value = discountAmount;
                valueInBaseCurrency = (value = discountAmount).divide(exchangeRateValue, 2, BigDecimal.ROUND_HALF_UP);
            }
            if (onDiscountChange != null) {
                onDiscountChange.execute();
            }
        });

        txtDiscount.addFocusHandler(focusEvent -> {
            if (txtDiscount.getValue() != null && !txtDiscount.getValue().isEmpty()) {
                txtDiscount.selectAll();
            }
        });

        lblCurrencyUnit = new Label();
        lblCurrencyUnit.setText(ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT);

        //add widgets to horizontal panel
        add(txtDiscount);
        add(lblCurrencyUnit);
    }

    public void setOnDiscountChange(Command onDiscountChange) {
        this.onDiscountChange = onDiscountChange;
    }

    String getDiscountUnit() {
        return getWidgetCount() > 1 ? ((Label) getWidget(1)).getText() : ProductsTable.DEFAULT_DISCOUNT_TYPE_UNIT;
    }

    void setDiscountUnit(String discountUnit) {
        Widget wtDiscountUnit = null;
        if (getWidgetCount() > 1) {
            wtDiscountUnit = getWidget(1);
            ((Label) wtDiscountUnit).setText(discountUnit);
            remove(1);
        }

        //if discount unit is null create new discount unit
        if (wtDiscountUnit == null) {
            wtDiscountUnit = new Label(discountUnit);
        }

        add(wtDiscountUnit);
    }

    public BigDecimal getValue() {
        return value != null ? value : BigDecimal.ZERO;
    }

    public void setValueText(String text, BigDecimal value) {
        this.value = value;
        ((TextBox) getWidget(0)).setText(text);
    }

    BigDecimal getValueInBaseCurrency() {
        return valueInBaseCurrency != null ? valueInBaseCurrency : BigDecimal.ZERO;
    }

    void setValueInBaseCurrency(BigDecimal value) {
        this.valueInBaseCurrency = value;
    }

    public void setEnabled(boolean enabled) {
        if (txtDiscount != null) {
            txtDiscount.setEnabled(enabled);
        }
    }

    @Override
    public String getDisplayValue() {
        return txtDiscount.getText() + " " + lblCurrencyUnit.getText();
    }

    @Override
    public void setItemValue(Object value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setItemFocus(boolean focused) {
        txtDiscount.setFocus(focused);
    }

    public TextBox getTxtDiscount() {
        return this.txtDiscount;
    }

    public void setTxtDiscount(final TextBox txtDiscount) {
        this.txtDiscount = txtDiscount;
    }
}
