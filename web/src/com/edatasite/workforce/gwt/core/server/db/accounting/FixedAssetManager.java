package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsFixedAsset;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetGroupItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/6/11
 * Time: 1:55 PM
 * To change this template use File | Settings | File Templates.
 */
public interface FixedAssetManager extends Manager<EdsFixedAsset> {
    List<EdsFixedAsset> getFixedAssets();

    ListResult<EdsFixedAsset> getFixedAssets(ListingFilterParameter filterParameter);

    List<EdsFixedAsset> getFixedAssetsForLookUp(ListingFilterParameter filterParameter);

    boolean isFixedAssetNumberExists(String code, Integer objectID);

    Integer getFixedAssetLastIntNumber();

    List<FixedAssetGroupItem> getFixedAssetGroups(ListingFilterParameter filterParameter);

    List<EdsFixedAsset> getDepreciationEnabledFixedAssets(LinkedList<Integer> ids);

    LinkedList<Integer> getDepreciationEnabledFixedAssetsIDs();

    String getImage(Integer image);

    EdsFixedAsset getFixedAssetByPurchaseOrder(Integer purchaseOrderID);

    EdsFixedAsset getFixedAssetByPurchaseInvoice(Integer purchaseInvoiceID);

    EdsFixedAsset getFixedAssetBySalesInvoice(Integer salesInvoiceID);

    void deleteFixedAssetDailyDepreciatioinRate(EdsFixedAsset fixedAsset);
}
