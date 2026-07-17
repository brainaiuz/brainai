package com.edatasite.workforce.gwt.crm.client.ui.view.widgets;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;

/**
 * Created by Abror Abdukadirov on 16.06.2016.
 * Email abror.abduqadirov@gmail.com
 */
public class OpportunityDiscountWidget extends HorizontalPanel implements CustomCellInterface, AccountingConstants {

    private static final AccountingUtils utils = AccountingUtils.get();
    public static final String DEFAULT_DISCOUNT_TYPE_UNIT = "%";

    private TextBox txtDiscount;
    private Label lblCurrencyUnit;
    private BigDecimal value;

    public OpportunityDiscountWidget() {
        super();
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
            BigDecimal discountAmount = ZERO;
            try {
                discountAmount = utils.parseToBigDecimal(txtDiscount.getText(), utils.discountNumberFormat);
            } catch (NumberFormatException ex) {
                txtDiscount.setText(utils.getDiscountZero());
            } finally {
                String discUnit = (DEFAULT_DISCOUNT_TYPE_UNIT.equals(((Label) getWidget(1)).getText())) ? DEFAULT_DISCOUNT_TYPE_UNIT : ((Label) getWidget(1)).getText();
                if (discUnit.equals(DEFAULT_DISCOUNT_TYPE_UNIT) && discountAmount.compareTo(HUNDRED) > 0) {
                    txtDiscount.setText(txtDiscount.getText().substring(0, utils.getDiscountScale()));
                    discountAmount = utils.parseToBigDecimal(txtDiscount.getText(), utils.discountNumberFormat);
                }
                value = discountAmount;
            }
        });

        lblCurrencyUnit = new Label();
        lblCurrencyUnit.setText(DEFAULT_DISCOUNT_TYPE_UNIT);

        add(txtDiscount);
        add(lblCurrencyUnit);
    }

    public String getDiscountUnit() {
        return getWidgetCount() > 1 ? ((Label) getWidget(1)).getText() : DEFAULT_DISCOUNT_TYPE_UNIT;
    }

    public void setDiscountUnit(String discountUnit) {
        Widget wtDiscountUnit = null;
        if (getWidgetCount() > 1) {
            wtDiscountUnit = getWidget(1);
            ((Label) wtDiscountUnit).setText(discountUnit);
            remove(1);
        }

        if (wtDiscountUnit == null) {
            wtDiscountUnit = new Label(discountUnit);
        }

        add(wtDiscountUnit);
    }

    public BigDecimal getValue() {
        return value != null ? value : ZERO;
    }

    public void setValueText(String text, BigDecimal value) {
        this.value = value;
        ((TextBox) getWidget(0)).setText(text);
    }

    public void addChangeHandler(ChangeHandler changeHandler) {
        txtDiscount.addChangeHandler(changeHandler);
    }

    @Override
    public String getDisplayValue() {
        return txtDiscount.getText() + " " + lblCurrencyUnit.getText();
    }

    @Override
    public void setItemValue(Object value) {

    }

    public TextBox getTxtDiscount() {
        return txtDiscount;
    }

    @Override
    public void setItemFocus(boolean focused) {
        txtDiscount.setFocus(focused);
    }
}
