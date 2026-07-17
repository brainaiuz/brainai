package com.edatasite.workforce.gwt.accounting.client.rpc.discount;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Dec 4, 2010
 * Time: 5:19:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class DiscountMultiRangeItem implements IsSerializable {

    private Integer id;
    private Integer type;
    private Integer fromQty;
    private Integer toQty;
    private BigDecimal fromAmount;
    private BigDecimal toAmount;
    private BigDecimal percentage;
    private BigDecimal fixedAmount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(BigDecimal fromAmount) {
        this.fromAmount = fromAmount;
    }

    public BigDecimal getToAmount() {
        return toAmount;
    }

    public void setToAmount(BigDecimal toAmount) {
        this.toAmount = toAmount;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public BigDecimal getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(BigDecimal fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public Integer getFromQty() {
        return fromQty;
    }

    public void setFromQty(Integer fromQty) {
        this.fromQty = fromQty;
    }

    public Integer getToQty() {
        return toQty;
    }

    public void setToQty(Integer toQty) {
        this.toQty = toQty;
    }
}
