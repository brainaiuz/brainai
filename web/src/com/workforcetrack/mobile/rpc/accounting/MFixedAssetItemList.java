package com.workforcetrack.mobile.rpc.accounting;

import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 24.12.11
 * Time: 17:37
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MFixedAssetItemList {

    private List<MFixedAssetItem> fixedAssetItem;
    private Integer totalCount;

    public MFixedAssetItemList() {
    }

    public MFixedAssetItemList(ListResult<FixedAssetItem> itemList) {
        if (itemList != null && itemList.getList() != null && itemList.getList().size() > 0) {
            fixedAssetItem = new ArrayList<>();
            totalCount = itemList.getTotal();
            for (FixedAssetItem item : itemList.getList()) {
                fixedAssetItem.add(new MFixedAssetItem(item));
            }
        }
    }

    //@XmlElement(name = "fixedAssetItem")
    public List<MFixedAssetItem> getFixedAssetItem() {
        return fixedAssetItem;
    }

    public void setFixedAssetItem(List<MFixedAssetItem> fixedAssetItem) {
        this.fixedAssetItem = fixedAssetItem;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
