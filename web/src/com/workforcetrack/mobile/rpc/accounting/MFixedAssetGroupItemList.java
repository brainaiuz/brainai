package com.workforcetrack.mobile.rpc.accounting;

import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetGroupItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 24.12.11
 * Time: 17:45
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MFixedAssetGroupItemList {

    private List<MFixedAssetGroupItem> fixedAssetGroupItem;
    private Integer totalCount;

    public MFixedAssetGroupItemList() {
    }

    public MFixedAssetGroupItemList(List<FixedAssetGroupItem> itemList, Integer totalCount) {
        this.totalCount = totalCount;
        if (itemList != null && itemList.size() > 0) {
            fixedAssetGroupItem = new ArrayList<>();
            for (FixedAssetGroupItem item : itemList) {
                fixedAssetGroupItem.add(new MFixedAssetGroupItem(item));
            }
        }
    }

    public List<MFixedAssetGroupItem> getFixedAssetGroupItem() {
        return fixedAssetGroupItem;
    }

    public void setFixedAssetGroupItem(List<MFixedAssetGroupItem> fixedAssetGroupItem) {
        this.fixedAssetGroupItem = fixedAssetGroupItem;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
