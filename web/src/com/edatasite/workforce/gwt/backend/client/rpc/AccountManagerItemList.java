package com.edatasite.workforce.gwt.backend.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Dec 7, 2009
 * Time: 6:45:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccountManagerItemList implements IsSerializable {
    private int totalCount;
    private String loggedUserEmail;
    private AccountManagementListItem[] listItems;

    public AccountManagerItemList() {
    }

    public AccountManagerItemList(int total, AccountManagementListItem[] listItems, String loggedUserEmail) {
        this.totalCount = total;
        this.listItems = listItems;
        this.loggedUserEmail = loggedUserEmail;
    }

    public String getLoggedUserEmail() {
        return loggedUserEmail;
    }

    public void setLoggedUserEmail(String loggedUserEmail) {
        this.loggedUserEmail = loggedUserEmail;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public AccountManagementListItem[] getListItems() {
        return listItems;
    }

    public void setListItems(AccountManagementListItem[] listItems) {
        this.listItems = listItems;
    }

    public ListData getListData() {
        return new ListData(listItems, totalCount);
    }
}
