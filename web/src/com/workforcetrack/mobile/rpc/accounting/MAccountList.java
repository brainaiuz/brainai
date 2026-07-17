package com.workforcetrack.mobile.rpc.accounting;

import com.edatasite.workforce.gwt.accounting.client.rpc.AccountListItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.workforcetrack.mobile.rpc.expense.MAccountItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/13/11
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "accountList")
public class MAccountList {

    private List<MAccountItem> accountListItem;

    public MAccountList() {

    }

    public MAccountList(AccountItem[] accountItems) {
        if (accountItems != null) {
            this.accountListItem = new ArrayList<>();
            for (AccountItem accountItem : accountItems) {
                this.accountListItem.add(new MAccountItem(accountItem));
            }
        }
    }

    public MAccountList(AccountListItem[] accountItems) {
        if (accountItems != null) {
            this.accountListItem = new ArrayList<>();
            for (AccountListItem accountItem : accountItems) {
                this.accountListItem.add(new MAccountItem(accountItem));
            }
        }
    }

    public List<MAccountItem> getAccountListItem() {
        return accountListItem;
    }

    public void setAccountListItem(List<MAccountItem> accountListItem) {
        this.accountListItem = accountListItem;
    }
}
