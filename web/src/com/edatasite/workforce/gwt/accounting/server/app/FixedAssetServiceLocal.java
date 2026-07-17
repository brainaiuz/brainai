package com.edatasite.workforce.gwt.accounting.server.app;

import com.edatasite.workforce.core.domain.accounting.EdsFixedAsset;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetItem;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DeprecationItemMQ;

/**
 * Created by Normurod on 4/11/2017.
 */
public interface FixedAssetServiceLocal {

    ListResult<FixedAssetItem> getFixedAssets(ListingFilterParameter filterParameter);

    FixedAssetItem getFixedAssetData(Integer objectID);

    Integer saveFixedAssetData(FixedAssetItem item) throws NumberExistingException;

    void createOrUpdateFixedAssetTransaction(EdsFixedAsset fixedAsset);

    void disposeFixedAssetItem(FixedAssetItem fixedAssetItem);

    NumberData generateFixedAssetNumber();

    boolean validateFixedAssetNumber(String number, Integer objectID);

    void deleteFixedAsset(Integer id);

    void updateDeprecations(DeprecationItemMQ item);

    String getFixedAssetImageUrl(Integer imageID);

    void sendFixedAssetCountResults(String[] existingFixedAssetCodes);

    SelectItem[] getFixedAssetsForLookUp(ListingFilterParameter filterParameter);

    void updatePurchaseInvoiceRelatedFixedAsset(Integer objectID, boolean delete);

    void deleteSalesInvoiceRelatedFixedAsset(Integer invoiceID);

    void voidSalesInvoiceRelatedFixedAsset(Integer invoiceID, DateNonConvertable voidDate);
}
