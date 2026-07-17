package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Aug 13, 2009
 * Time: 6:17:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductList implements IsSerializable {
    private ProductItem[] items;
    private int totalCount;

    public ProductItem[] getItems() {
        return items;
    }

    public void setItems(ProductItem[] items) {
        this.items = items;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
