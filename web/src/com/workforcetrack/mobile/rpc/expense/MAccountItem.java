package com.workforcetrack.mobile.rpc.expense;

import com.edatasite.workforce.gwt.accounting.client.rpc.AccountListItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/22/11
 * Time: 5:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class MAccountItem extends MSelectItem {

    private String code;
    private String currence;
    private String accountTypeCategory;

    public MAccountItem() {

    }

    public MAccountItem(AccountItem accountItem) {
        if (accountItem != null) {
            this.setObjectID(accountItem.getId());
            this.setDescription(accountItem.getDescription());
            this.setName(accountItem.getName());

            this.code = accountItem.getCode();
            this.accountTypeCategory = accountItem.getAccountTypeCategory();
        }

    }

    public MAccountItem(AccountListItem listItem) {
        if (listItem != null) {
            this.setObjectID(listItem.getObjectID());
            this.setDescription(listItem.getDescription());
            this.setName(listItem.getName());
            this.code = listItem.getCode() != null ? listItem.getCode() : "";
            this.currence = listItem.getCurrency();
        }
    }

    public AccountItem convertToAccountItem(AccountItem accountItem) {
        if (accountItem == null)
            accountItem = new AccountItem();
        accountItem.setId(this.getObjectID());
        accountItem.setDescription(this.getDescription());
        accountItem.setName(this.getName());

        accountItem.setCode(this.code);
        accountItem.setAccountTypeCategory(this.accountTypeCategory);

        return accountItem;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAccountTypeCategory() {
        return accountTypeCategory;
    }

    public void setAccountTypeCategory(String accountTypeCategory) {
        this.accountTypeCategory = accountTypeCategory;
    }
}
