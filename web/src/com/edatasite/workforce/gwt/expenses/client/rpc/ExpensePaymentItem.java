package com.edatasite.workforce.gwt.expenses.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Apr 24, 2009
 * Time: 5:31:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExpensePaymentItem implements IsSerializable {
    private Integer objectId;
    private String status;
    private BigDecimal amount;
    private Date paidDate;
    private String user;
    private String reference;
    private String paidTo;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getPaidTo() {
        return paidTo;
    }

    public void setPaidTo(String paidTo) {
        this.paidTo = paidTo;
    }
}
