package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 10/3/12
 * Time: 3:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class CurrencyAdjustmentItem implements IsSerializable {
    private Integer objectID;

    private String type;
    private String name;
    private BigDecimal foreignBalance;
    private BigDecimal balance;
    private BigDecimal adjustmentBalance;
    private BigDecimal exchangeGainLoss;

    public CurrencyAdjustmentItem() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getForeignBalance() {
        return foreignBalance != null ? foreignBalance : BigDecimal.ZERO;
    }

    public void setForeignBalance(BigDecimal foreignBalance) {
        this.foreignBalance = foreignBalance;
    }

    public BigDecimal getBalance() {
        return balance != null ? balance : BigDecimal.ZERO;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getAdjustmentBalance() {
        return adjustmentBalance != null ? adjustmentBalance : BigDecimal.ZERO;
    }

    public void setAdjustmentBalance(BigDecimal adjustmentBalance) {
        this.adjustmentBalance = adjustmentBalance;
    }

    public BigDecimal getExchangeGainLoss() {
        return exchangeGainLoss != null ? exchangeGainLoss : BigDecimal.ZERO;
    }

    public void setExchangeGainLoss(BigDecimal exchangeGainLoss) {
        this.exchangeGainLoss = exchangeGainLoss;
    }
}
