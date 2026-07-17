package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;


/**
 * Created by IntelliJ IDEA.
 * User: Bunyod
 * Date: 9/13/12
 * Time: 1:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class AgingDetailItem implements IsSerializable {

    private String transactionType;
    private BigDecimal transactionAmount;

    public AgingDetailItem() {

    }


    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
}
