package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * Created by Virus on 7/23/2016.
 */
public class CompanyAddress implements IsSerializable, Serializable {

    private SelectItem[] billAddresses;
    private SelectItem[] mailAddresses;

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
