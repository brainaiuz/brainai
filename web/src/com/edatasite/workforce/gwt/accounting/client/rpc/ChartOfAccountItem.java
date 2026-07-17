package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod
 * Date: 7/16/12
 * Time: 11:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChartOfAccountItem implements IsSerializable {

    private ArrayList<EdsAccount> accountList;
    private int totalCount;

    public ChartOfAccountItem() {

    }

    public ArrayList<EdsAccount> getAccountList() {
        return accountList;
    }

    public void setAccountList(ArrayList<EdsAccount> accountList) {
        this.accountList = accountList;
    }

    public Integer  getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
