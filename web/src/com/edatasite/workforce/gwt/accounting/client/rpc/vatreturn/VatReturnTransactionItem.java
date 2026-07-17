package com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn;

import com.edatasite.workforce.gwt.accounting.client.rpc.enums.VatReturnTransactionType;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

public class VatReturnTransactionItem implements IsSerializable {

    private VatReturnTransactionType type;
    private Integer objectId;
    private String number;
    private DateNonConvertable date;
    private TaxAmountItem amountItem;
    private Integer transactionId;
    private String crmAccountName;
    private String crmAccountTrn;

    public VatReturnTransactionType getType() {
        return type;
    }

    public void setType(VatReturnTransactionType type) {
        this.type = type;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public TaxAmountItem getAmountItem() {
        return amountItem;
    }

    public void setAmountItem(TaxAmountItem amountItem) {
        this.amountItem = amountItem;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public String getCrmAccountName() {
        return crmAccountName;
    }

    public void setCrmAccountName(String crmAccountName) {
        this.crmAccountName = crmAccountName;
    }

    public String getCrmAccountTrn() {
        return crmAccountTrn;
    }

    public void setCrmAccountTrn(String crmAccountTrn) {
        this.crmAccountTrn = crmAccountTrn;
    }
}
