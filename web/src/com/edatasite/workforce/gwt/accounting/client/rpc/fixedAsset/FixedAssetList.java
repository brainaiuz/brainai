package com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/5/11
 * Time: 12:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class FixedAssetList implements IsSerializable{
    private FixedAssetItem[] items;
    private int totalCount;

    public FixedAssetList() {
    }

    public FixedAssetList(FixedAssetItem[] items, int totalCount) {
        this.items = items;
        this.totalCount = totalCount;
    }

    public FixedAssetItem[] getItems() {
        return items;
    }

    public void setItems(FixedAssetItem[] items) {
        this.items = items;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
