package com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 12/22/11
 * Time: 5:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class FixedAssetGroupItem implements IsSerializable{
    private Integer categoryID;
    private String categoryName;
    private Long itemCount;

    public FixedAssetGroupItem() {
    }

    public FixedAssetGroupItem(Integer categoryID, String categoryName, Long itemCount) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.itemCount = itemCount;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getItemCount() {
        return itemCount;
    }

    public void setItemCount(Long itemCount) {
        this.itemCount = itemCount;
    }
}
