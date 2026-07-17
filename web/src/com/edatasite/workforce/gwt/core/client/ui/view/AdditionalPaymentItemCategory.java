package com.edatasite.workforce.gwt.core.client.ui.view;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.math.BigDecimal;

public class AdditionalPaymentItemCategory implements IsSerializable, Serializable {

    private Integer id;
    private Integer type;
    private BigDecimal percentage;
    private BigDecimal amount;
    private PaymentDeductionSelectItem categoryItem;
    private String reference;

    public Integer getId() {
        return this.id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(final Integer type) {
        this.type = type;
    }

    public BigDecimal getPercentage() {
        return this.percentage;
    }

    public void setPercentage(final BigDecimal percentage) {
        this.percentage = percentage;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentDeductionSelectItem getCategoryItem() {
        return this.categoryItem;
    }

    public void setCategoryItem(final PaymentDeductionSelectItem categoryItem) {
        this.categoryItem = categoryItem;
    }

    public String getReference() {
        return this.reference;
    }

    public void setReference(final String reference) {
        this.reference = reference;
    }
}
