package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountTypeEnum;

import java.math.BigDecimal;

/**
 * Created by Dilsh0d Madrahimov on 03.02.2015.
 */
public class DiscountTO extends SelectItemTO {

    BigDecimal percentage;
    BigDecimal fixedAmount;
    DiscountTypeEnum type;

    public DiscountTO() {
    }

    public DiscountTO(Integer id) {
        super(id);
    }

    public DiscountTO(Integer id, String name, String code) {
        super(id, name, code, "");
    }

    public DiscountTO(DiscountItem discountItem) {
        this.id = discountItem.getId();
        this.name = discountItem.getName();
        this.code = discountItem.getCode();
        this.percentage = discountItem.getPercentage();
        this.fixedAmount = discountItem.getFixedAmount();
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

    public DiscountTypeEnum getType() {
        return type;
    }

    public void setType(DiscountTypeEnum type) {
        this.type = type;
    }
}