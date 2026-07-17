package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: May 3, 2010
 * Time: 5:51:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductCategoryList implements IsSerializable {
    private ProductCategoryItem[] items;
    private int totalCount;
    private int storeFrontID;

    public ProductCategoryItem[] getItems() {
        return items;
    }

    public void setItems(ProductCategoryItem[] items) {
        this.items = items;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public ListData getListData() {
        return new ListData(items, totalCount);
    }

    public int getStoreFrontID() {
        return storeFrontID;
    }

    public void setStoreFrontID(int storeFrontID) {
        this.storeFrontID = storeFrontID;
    }
}
