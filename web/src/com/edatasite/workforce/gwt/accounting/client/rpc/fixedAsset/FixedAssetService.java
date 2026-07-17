package com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset;

import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Created by Normurod on 4/11/2017.
 */
public interface FixedAssetService extends RemoteService {

    ListResult<FixedAssetItem> getFixedAssets(ListingFilterParameter filterParameter);

    FixedAssetItem getFixedAssetData(Integer objectID);

    Integer saveFixedAssetData(FixedAssetItem item) throws NumberExistingException;

    Integer updateFixedAssetData(FixedAssetItem item) throws NumberExistingException;

    void sendFixedAssetCountResults(String[] existingFixedAssetCodes);

    FixedAssetGroups getFixedAssetCategories(ListingFilterParameter filterParameter);

    void deleteFixedAsset(Integer id);

    void disposeFixedAssetItem(FixedAssetItem fixedAssetItem);

    NumberData generateFixedAssetNumber();

    boolean validateFixedAssetNumber(String number, Integer objectID);

    String getFixedAssetImageUrl(Integer imageID);

    SelectItem[] getFixedAssetsForLookUp(ListingFilterParameter filterParameter);

    boolean saveFixedAssetCellValue(FixedAssetItem rowValue, String columnCodeName);

    boolean sendToUpdateDeprecationMQ(DateNonConvertable depreciationEndDate);

    class App {
        public static FixedAssetServiceAsync get() {
            ServiceDefTarget target = GWT.create(FixedAssetService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/fixedAsset");
            return (FixedAssetServiceAsync) target;
        }
    }
}
