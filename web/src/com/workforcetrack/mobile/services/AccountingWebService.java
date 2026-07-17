package com.workforcetrack.mobile.services;

import com.workforcetrack.mobile.rpc.accounting.MAccountList;
import com.workforcetrack.mobile.rpc.accounting.MAccountsByCategory;
import com.workforcetrack.mobile.rpc.accounting.MFixedAssetGroupItemList;
import com.workforcetrack.mobile.rpc.accounting.MFixedAssetItem;
import com.workforcetrack.mobile.rpc.accounting.MFixedAssetItemList;
import com.workforcetrack.mobile.rpc.accounting.MProductCategoryList;
import com.workforcetrack.mobile.rpc.accounting.MProductCategoryListItem;
import com.workforcetrack.mobile.rpc.client.MFilterParametrs;
import com.workforcetrack.mobile.rpc.opportunity.MNumberData;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/13/11
 * Time: 4:57 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AccountingWebService {

    MAccountList getAccountsForExpense();

    MAccountList getAccountList(MFilterParametrs filterParametrs);

    MAccountList getAccountsForExpense(MFilterParametrs filterParametrs);

    MAccountsByCategory getAccountsForInvoice();

    //The type may be one of these: Constants.RECEIVABLE, Constants.ASSETS
    MAccountList getAccountsForInvoice(MFilterParametrs filterParametrs, String type);

    Boolean createDefaultAccountingParameters();

    MProductCategoryList getCategoryList(MFilterParametrs mFilterParametrs);

    Integer saveProductCategory(MProductCategoryListItem mProductCategoryListItem);

    MProductCategoryListItem getProductCategory(Integer objectID);

    Boolean deleteProductCategory(Integer objectID);

    // FIXED ASSET
    MFixedAssetGroupItemList getFixedAssetGroups(MFilterParametrs fp);

    MFixedAssetGroupItemList getFixedAssetGroups();

    MFixedAssetItemList getFixedAssets(MFilterParametrs fp, Integer categoryID);

    Integer saveFixedAsset(MFixedAssetItem mFixedAssetItem);

    Boolean deleteFixedAsset(Integer objectID);

    MNumberData generateFixedAssetNumber();

    Boolean sendFixedAssetCountResults(ArrayList<String> existingFixedAssetIDs);

}
