package com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 12/22/11
 * Time: 5:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class FixedAssetGroups implements IsSerializable{
    private FixedAssetGroupItem[] items;
    private int totalCount;

    public FixedAssetGroups() {
    }

    public FixedAssetGroups(FixedAssetGroupItem[] items, int totalCount) {
        this.items = items;
        this.totalCount = totalCount;
    }

    public FixedAssetGroupItem[] getItems() {
        return items;
    }

    public void setItems(FixedAssetGroupItem[] items) {
        this.items = items;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
