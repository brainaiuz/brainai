package com.edatasite.workforce.gwt.core.client.ui.view;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.math.BigDecimal;

public class PaymentCalculationDetail implements IsSerializable, Serializable {

    private String name;
    private String calculation;
    private String formula;
    private BigDecimal amount;

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getCalculation() {
        return this.calculation;
    }

    public void setCalculation(final String calculation) {
        this.calculation = calculation;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public String getFormula() {
        return this.formula;
    }

    public void setFormula(final String formula) {
        this.formula = formula;
    }
}
