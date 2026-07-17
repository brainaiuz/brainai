package com.workforcetrack.mobile.rpc.accounting;

import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetGroupItem;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 24.12.11
 * Time: 17:42
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MFixedAssetGroupItem {

    private Integer categoryID;
    private String categoryName;
    private Long itemCount;

    public MFixedAssetGroupItem() {
    }

    public MFixedAssetGroupItem(FixedAssetGroupItem item) {
        this.categoryID = item.getCategoryID();
        this.categoryName = item.getCategoryName();
        this.itemCount = item.getItemCount();
    }

    public FixedAssetGroupItem convert(FixedAssetGroupItem item) {
        if (item == null) {
            item = new FixedAssetGroupItem();
        }
        item.setCategoryID(getCategoryID());
        item.setCategoryName(getCategoryName());
        item.setItemCount(getItemCount());

        return item;
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
