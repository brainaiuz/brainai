package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 2/9/13
 * Time: 12:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class StockAdjustmentListItem implements IsSerializable {
    public static final String ACTION = "action";
    public static final String NUMBER = "number";
    public static final String DATE = "date";
    public static final String ADJUSTMENT_ACCOUNT = "account";
    public static final String MEMO = "memo";
    public static final String STATUS = "status";

    private Integer objectID;
    private String number;
    private Date date;
    private SelectItem adjustmentAccount;
    private SelectItem creator;
    private SelectItem updator;
    private String accountName;
    private String memo;
    private String statusCode;

    public StockAdjustmentListItem() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public SelectItem getAdjustmentAccount() {
        return adjustmentAccount;
    }

    public void setAdjustmentAccount(SelectItem adjustmentAccount) {
        this.adjustmentAccount = adjustmentAccount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(final String statusCode) {
        this.statusCode = statusCode;
    }

    public SelectItem getCreator() {
        return creator;
    }

    public void setCreator(SelectItem creator) {
        this.creator = creator;
    }

    public SelectItem getUpdator() {
        return updator;
    }

    public void setUpdator(SelectItem updator) {
        this.updator = updator;
    }
}
