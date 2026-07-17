package com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset;

import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by Normurod on 4/11/2017.
 */
public interface FixedAssetServiceAsync {

    void getFixedAssetData(Integer objectID, AsyncCallback<FixedAssetItem> callback);

    void saveFixedAssetData(FixedAssetItem item, AsyncCallback<Integer> callback) throws NumberExistingException;

    void updateFixedAssetData(FixedAssetItem item, AsyncCallback<Integer> callback) throws NumberExistingException;

    void sendFixedAssetCountResults(String[] existingFixedAssetCodes, AsyncCallback<Void> callback);

    void getFixedAssetCategories(ListingFilterParameter filterParameter, AsyncCallback<FixedAssetGroups> callback);

    Request getFixedAssets(ListingFilterParameter filterParameter, AsyncCallback<ListResult<FixedAssetItem>> callback);

    void deleteFixedAsset(Integer id, AsyncCallback<Void> callback);

    void disposeFixedAssetItem(FixedAssetItem fixedAssetItem, AsyncCallback<Void> callback);

    void generateFixedAssetNumber(AsyncCallback<NumberData> async);

    void validateFixedAssetNumber(String number, Integer objectID, AsyncCallback<Boolean> async);

    void getFixedAssetImageUrl(Integer imageID, AsyncCallback<String> asyncCallback);

    void getFixedAssetsForLookUp(ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> callback);

    void saveFixedAssetCellValue(FixedAssetItem rowValue, String columnCodeName, AsyncCallback<Boolean> asyncCallback);

    void sendToUpdateDeprecationMQ(DateNonConvertable depreciationEndDate, AsyncCallback<Boolean> callback);

}
