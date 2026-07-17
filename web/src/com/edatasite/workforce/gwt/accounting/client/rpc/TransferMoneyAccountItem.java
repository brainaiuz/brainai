package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Feb 26, 2010
 * Time: 7:20:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class TransferMoneyAccountItem extends SelectItem {
    private boolean isBankAccount;

    public TransferMoneyAccountItem() {
    }

    public TransferMoneyAccountItem(Integer id, String name, boolean bankAccount) {
        super(id, name);
        isBankAccount = bankAccount;
    }

    public boolean isBankAccount() {
        return isBankAccount;
    }

    public void setBankAccount(boolean bankAccount) {
        isBankAccount = bankAccount;
    }
}
