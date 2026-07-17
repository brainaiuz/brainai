package com.edatasite.workforce.gwt.project.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/17/12
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewProjectBudgetRowItem implements IsSerializable {
    private SelectItem account;
    private LinkedHashMap<String, NewProjectBudgetCellItem> cellDataMap;

    public NewProjectBudgetRowItem() {
    }

    public SelectItem getAccount() {
        return account;
    }

    public void setAccount(SelectItem account) {
        this.account = account;
    }

    public LinkedHashMap<String, NewProjectBudgetCellItem> getCellDataMap() {
        if (cellDataMap == null) {
            cellDataMap = new LinkedHashMap<>();
        }
        return cellDataMap;
    }
}
