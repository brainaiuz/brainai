package com.edatasite.workforce.gwt.expenses.client.ui.subtotal;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.expenses.client.ui.view.report.ExpenseAddEditView;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.jquery.client.api.JQuery;

import java.math.BigDecimal;

//import com.edatasite.workforce.gwt.expenses.client.ui.view.report.MultipleAddView;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 18.11.2008
 * Time: 18:43:47
 * To change this template use File | Settings | File Templates.
 */
public class Subtotal implements KeyUpHandler, SelectionHandler {

    private TextBox[] widgets; //TextBox array. Values for calculating are taken from here.
    private ExpenseAddEditView.ExtendedTaxLookUp taxLookUp; //Tax LookUp
    private ExpenseAddEditView.ExtendedTaxLookUp doubleTaxLookUp; //Double Tax customization
    private String taxAmount;
    private Label total; //Result will set to this Label.
    private Label baseTotal;
    private BigDecimal exchangeRateValue = BigDecimal.ONE;
    private PreparedListener preparedListener; //Listeners prepared() function called after all calculations completed.
    private Integer taxCalculationType = AccountingConstants.TAX_CALCULATION_EXCLUSIVE;

    public Subtotal(TextBox[] widgets, ExpenseAddEditView.ExtendedTaxLookUp taxLookUp, ExpenseAddEditView.ExtendedTaxLookUp doubleTaxLookUp, String taxAmount, Label total, Label baseTotal) {
        this.widgets = widgets;
        this.taxLookUp = taxLookUp;
        this.doubleTaxLookUp = doubleTaxLookUp;
        this.taxAmount = taxAmount;
        this.total = total;
        this.baseTotal = baseTotal;
        addListeners();
    }

    /**
     * Function get values from widget array,
     * transfers them to double primitive type and calculates result.
     * In the end it calls setSubtotal(double subtotal) function.
     */
    public void calculate(boolean calculateTotal) {

        BigDecimal units = getTextBoxValue(widgets[0]), cost = getTextBoxValue(widgets[1]);
        ExpenseAddEditView.ExtendedTextBox actualCost = (ExpenseAddEditView.ExtendedTextBox) widgets[1];
        cost = cost.setScale(AccountingUtils.customUnitPriceScale, BigDecimal.ROUND_HALF_UP);
        BigDecimal net = units.multiply(cost).setScale(AccountingUtils.systemCalculationScale, BigDecimal.ROUND_HALF_UP);
        BigDecimal baseTotal = BigDecimal.ZERO;
        BigDecimal total = net;
        BigDecimal taxInExpenseCurrency = BigDecimal.ZERO;
        BigDecimal doubleInExpenseCurrency = BigDecimal.ZERO;

        if (taxLookUp != null && taxLookUp.getSelectedData() != null && taxLookUp.getSelectedData() instanceof TaxItem) {
            BigDecimal taxPercent = taxLookUp.getSelectedItem() == null ? BigDecimal.ZERO : taxLookUp.getSelectedData().getEffectiveTaxPercent().setScale(AccountingUtils.systemCalculationScale, BigDecimal.ROUND_HALF_UP);

            if (AccountingConstants.TAX_CALCULATION_INCLUSIVE.equals(taxCalculationType)) {
                taxInExpenseCurrency = (net.multiply(taxPercent)).divide(AccountingConstants.HUNDRED.add(taxPercent), AccountingUtils.systemCalculationScale, BigDecimal.ROUND_HALF_UP);

            } else if (AccountingConstants.TAX_CALCULATION_EXCLUSIVE.equals(taxCalculationType)) {
                taxInExpenseCurrency = (net.multiply(taxPercent)).divide(AccountingConstants.HUNDRED, AccountingUtils.systemCalculationScale, BigDecimal.ROUND_HALF_UP);
            }
        }
        if (doubleTaxLookUp != null && doubleTaxLookUp.getSelectedData() != null && doubleTaxLookUp.getSelectedData() instanceof TaxItem) {
            BigDecimal taxPercent = doubleTaxLookUp.getSelectedItem() == null ? BigDecimal.ZERO : doubleTaxLookUp.getSelectedData().getEffectiveTaxPercent().setScale(AccountingUtils.systemCalculationScale, BigDecimal.ROUND_HALF_UP);

            if (AccountingConstants.TAX_CALCULATION_INCLUSIVE.equals(taxCalculationType)) {
                doubleInExpenseCurrency = (net.multiply(taxPercent)).divide(AccountingConstants.HUNDRED.add(taxPercent), AccountingUtils.systemCalculationScale, BigDecimal.ROUND_HALF_UP);

            } else if (AccountingConstants.TAX_CALCULATION_EXCLUSIVE.equals(taxCalculationType)) {
                doubleInExpenseCurrency = (net.multiply(taxPercent)).divide(AccountingConstants.HUNDRED, AccountingUtils.systemCalculationScale, BigDecimal.ROUND_HALF_UP);
            }
        }

        if (exchangeRateValue.compareTo(BigDecimal.ZERO) != 0) {
            if (taxLookUp != null) {
                taxLookUp.setTaxAmountInTc(taxInExpenseCurrency);
            }
            if (doubleTaxLookUp != null) {
                doubleTaxLookUp.setTaxAmountInBase(doubleInExpenseCurrency.divide(exchangeRateValue, AccountingUtils.systemCalculationScale, BigDecimal.ROUND_HALF_UP));
            }
            total.setScale(AccountingUtils.customUnitPriceScale, BigDecimal.ROUND_HALF_UP);
            baseTotal = baseTotal.add(total.divide(exchangeRateValue, AccountingUtils.systemCalculationScale, BigDecimal.ROUND_HALF_UP));
        } else {
            if (taxLookUp != null) {
                taxLookUp.setTaxAmountInBase(BigDecimal.ZERO);
                taxLookUp.setTaxAmountInTc(BigDecimal.ZERO);
            }
            if (doubleTaxLookUp != null) {
                doubleTaxLookUp.setTaxAmountInBase(BigDecimal.ZERO);
                doubleTaxLookUp.setTaxAmountInTc(BigDecimal.ZERO);
            }
        }
        if (this.baseTotal != null) {
            this.baseTotal.setText(AccountingUtils.get().formatPrice(baseTotal));
            JQuery.$(this.baseTotal).trigger("valueChange", null);
        }
        this.total.setText(AccountingUtils.get().formatPrice(total));
        JQuery.$(this.total).trigger("valueChange", null);

        // it works when the cost widget is not in focus
        if (!isElementFocused(actualCost.getElement())) {
            actualCost.setText(AccountingUtils.get().formatPrice(cost));
            JQuery.$(actualCost).trigger("valueChange", null);
        }

        if (preparedListener != null && calculateTotal) {
            preparedListener.prepared();
        }
    }


    // Native JavaScript method to check if an element is focused
    private native boolean isElementFocused(Element elem) /*-{
        return elem === $wnd.document.activeElement;
    }-*/;

    private BigDecimal getTextBoxValue(TextBox txtBox) {
        if ("n/a".equals(txtBox.getText())) {
            return BigDecimal.ONE;
        }
        return (txtBox.getText().indexOf(':') == -1 ? AccountingUtils.get().parseToBigDecimal(txtBox.getText()) : AccountingUtils.get().parseToBigDecimal(getHourValue(txtBox.getText())));
    }

    private String getHourValue(String text) {
        String[] time = text.split(":");
        return (Double.parseDouble(time[0]) + Double.parseDouble(time[1]) / 60) + "";
    }

    /**
     * KeyboardListener implementation.
     * After user inputs value - we try to calculate it.
     *
     * @param event
     */
    public void onKeyUp(KeyUpEvent event) {
        calculate(true);
    }

    /**
     * Define your PreparedListener implementation
     * to call after all calculations comleted.
     *
     * @param preparedListener
     */
    public void addPreparedListener(PreparedListener preparedListener) {
        this.preparedListener = preparedListener;
    }

    /**
     * Add this class to widgets as KeyboardListener.
     * We handle KeyUp events for calculating subtotal in real time.
     */
    private void addListeners() {

        for (TextBox widget : widgets) {
            widget.addKeyUpHandler(keyUpEvent -> {
                BigDecimal cost = getTextBoxValue(widgets[1]);
                if (exchangeRateValue.compareTo(BigDecimal.ZERO) != 0) {
                    ((ExpenseAddEditView.ExtendedTextBox) widgets[1]).setCostAmountInBase(cost.divide(exchangeRateValue, AccountingUtils.systemCalculationScale, BigDecimal.ROUND_HALF_UP));
                }
                calculate(true);
            });
        }

        if (taxLookUp != null) {
            taxLookUp.getSuggestBox().addSelectionHandler(this);
            taxLookUp.getSuggestBox().addKeyUpHandler(this);
        }
        if (doubleTaxLookUp != null) {
            doubleTaxLookUp.getSuggestBox().addSelectionHandler(this);
            doubleTaxLookUp.getSuggestBox().addKeyUpHandler(this);
        }
    }

    @Override
    public void onSelection(SelectionEvent selectionEvent) {
        calculate(true);
    }

    public void setExchangeRateValue(BigDecimal exchangeRateValue) {
        this.exchangeRateValue = exchangeRateValue;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public void setTaxCalculationType(Integer taxCalculationType, Boolean calculate) {
        this.taxCalculationType = taxCalculationType != null ? taxCalculationType : AccountingConstants.TAX_CALCULATION_EXCLUSIVE;
        if (calculate) {
            calculate(true);
        }
    }

    public void setUnitCost(BigDecimal unitCost) {
        if (unitCost != null) {
            if (unitCost.compareTo(BigDecimal.ZERO) != 0) {
                this.widgets[1].setText(AccountingUtils.get().formatUnitPrice(unitCost));
            } else {
                this.widgets[1].setText(AccountingUtils.getUnitPriceZero());
            }
        }
    }
}
