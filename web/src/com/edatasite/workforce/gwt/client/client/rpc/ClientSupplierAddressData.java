package com.edatasite.workforce.gwt.client.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/24/11
 * Time: 7:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClientSupplierAddressData implements IsSerializable{
    private Integer clientSupplierID;
    private Integer currencyID;
    private Integer crmAccountID;
    private Integer primaryBillAddressID;
    private Integer primaryMailAddressID;
    private SelectItem[] billAddresses;
    private SelectItem[] mailAddresses;

    public ClientSupplierAddressData() {
    }

    public Integer getClientSupplierID() {
        return clientSupplierID;
    }

    public void setClientSupplierID(Integer clientSupplierID) {
        this.clientSupplierID = clientSupplierID;
    }

    public Integer getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(Integer currencyID) {
        this.currencyID = currencyID;
    }

    public Integer getCrmAccountID() {
        return crmAccountID;
    }

    public void setCrmAccountID(Integer crmAccountID) {
        this.crmAccountID = crmAccountID;
    }

    public Integer getPrimaryBillAddressID() {
        return primaryBillAddressID;
    }

    public void setPrimaryBillAddressID(Integer primaryBillAddressID) {
        this.primaryBillAddressID = primaryBillAddressID;
    }

    public Integer getPrimaryMailAddressID() {
        return primaryMailAddressID;
    }

    public void setPrimaryMailAddressID(Integer primaryMailAddressID) {
        this.primaryMailAddressID = primaryMailAddressID;
    }

    public SelectItem[] getBillAddresses() {
        return billAddresses;
    }

    public void setBillAddresses(SelectItem[] billAddresses) {
        this.billAddresses = billAddresses;
    }

    public SelectItem[] getMailAddresses() {
        return mailAddresses;
    }

    public void setMailAddresses(SelectItem[] mailAddresses) {
        this.mailAddresses = mailAddresses;
    }
}
