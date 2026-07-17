package com.edatasite.workforce.gwt.accounting.client.rpc.discount;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Dec 7, 2010
 * Time: 12:03:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class DiscountAppliesItem extends SelectItem {

   SelectItem[] items;

    public SelectItem[] getItems() {
        return items;
    }

    public void setItems(SelectItem[] items) {
        this.items = items;
    }
}
