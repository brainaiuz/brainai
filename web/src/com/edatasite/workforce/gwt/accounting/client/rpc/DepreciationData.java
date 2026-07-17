package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/11/11
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class DepreciationData extends ListResult<DepreciationItem> implements IsSerializable {
    private Integer assetID;
    private String assetName;
    private DepreciationItem[] items;
    private int totalCount;

    public DepreciationData() {
    }

    public DepreciationData(DepreciationItem[] items, int totalCount) {
        this.items = items;
        this.totalCount = totalCount;
    }

    public Integer getAssetID() {
        return assetID;
    }

    public void setAssetID(Integer assetID) {
        this.assetID = assetID;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public DepreciationItem[] getItems() {
        return items;
    }

    public void setItems(DepreciationItem[] items) {
        this.items = items;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
