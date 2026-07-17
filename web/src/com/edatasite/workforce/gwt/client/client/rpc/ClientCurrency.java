package com.edatasite.workforce.gwt.client.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Alisher
 * Date: Sep 29, 2009
 * Time: 11:20:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientCurrency implements IsSerializable {

    private Integer userCurrencyId;
    private SelectItem[] items;

    public Integer getUserCurrencyId() {
        return userCurrencyId;
    }

    public void setUserCurrencyId(Integer userCurrencyId) {
        this.userCurrencyId = userCurrencyId;
    }

    public SelectItem[] getItems() {
        return items;
    }

    public void setItems(SelectItem[] items) {
        this.items = items;
    }
}
