package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 9/14/12
 * Time: 5:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class VatReturnDetailItem implements IsSerializable {
    private Integer objectID;
    private Integer boxID;
    private String type;
    private String transferType;
    private boolean creditNote;
    private String name;
    private BigDecimal amount;
    private String number;

    public VatReturnDetailItem() {
    }

    public VatReturnDetailItem(Integer boxID, BigDecimal amount) {
        this.boxID = boxID;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public boolean isCreditNote() {
        return creditNote;
    }

    public void setCreditNote(boolean creditNote) {
        this.creditNote = creditNote;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getBoxID() {
        return boxID;
    }

    public void setBoxID(Integer boxID) {
        this.boxID = boxID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}