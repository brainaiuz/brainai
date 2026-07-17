package com.edatasite.workforce.gwt.core.client.rpc.payroll;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 2011-07-19
 * Time: 8:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class CategoryRate implements IsSerializable {

    /**
     * We need two variables as a Double, because we will check
     * in to null value and we need their null value. Primitive
     * types can't be null, thus we used Double.
     */
    private BigDecimal fixedAmount;
    private BigDecimal from;
    private BigDecimal percentage;
    private BigDecimal to;

    public BigDecimal getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(BigDecimal fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public BigDecimal getFrom() {
        return from;
    }

    public void setFrom(BigDecimal from) {
        this.from = from;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public BigDecimal getTo() {
        return to;
    }

    public void setTo(BigDecimal to) {
        this.to = to;
    }
}
