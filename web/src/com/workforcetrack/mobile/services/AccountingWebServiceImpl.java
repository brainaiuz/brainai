package com.workforcetrack.mobile.services;


import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountList;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountsByCategory;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductCategoryItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetGroupItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetItem;
import com.edatasite.workforce.gwt.accounting.server.app.FixedAssetServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.accounting.FixedAssetManager;
import com.google.common.collect.Lists;
import com.workforcetrack.mobile.rpc.accounting.MAccountList;
import com.workforcetrack.mobile.rpc.accounting.MAccountsByCategory;
import com.workforcetrack.mobile.rpc.accounting.MFixedAssetGroupItemList;
import com.workforcetrack.mobile.rpc.accounting.MFixedAssetItem;
import com.workforcetrack.mobile.rpc.accounting.MFixedAssetItemList;
import com.workforcetrack.mobile.rpc.accounting.MProductCategoryList;
import com.workforcetrack.mobile.rpc.accounting.MProductCategoryListItem;
import com.workforcetrack.mobile.rpc.client.MFilterParametrs;
import com.workforcetrack.mobile.rpc.opportunity.MNumberData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 02.07.11
 * Time: 13:19
 */
@Service("accountingWebService")
public class AccountingWebServiceImpl implements AccountingWebService {

    @Autowired
    private AccountingService accountingService;
    @Autowired
    private FixedAssetManager fixedAssetManager;
    @Autowired
    private FixedAssetServiceLocal fixedAssetService;

    public void setAccountingService(AccountingService accountingService) {
        this.accountingService = accountingService;
    }

    @Override
    public MAccountList getAccountsForExpense() {
        return getAccountsForExpense(null);
    }

    @Override
    public MAccountList getAccountsForExpense(MFilterParametrs filterParametrs) {
        ListingFilterParameter fp = null;
        if (filterParametrs != null) {
            fp = filterParametrs.convertToFilterParametrs();
        }

        AccountItem[] accountItems = accountingService.getAccountsForExpense(fp);

        return new MAccountList(accountItems);
    }

    @Override
    public MAccountList getAccountList(MFilterParametrs filterParametrs) {
        ListingFilterParameter fp = null;
        if (filterParametrs != null) {
            fp = filterParametrs.convertToFilterParametrs();
        }

        AccountList accountList = accountingService.getAccountListByAccountType(fp);

        return new MAccountList(accountList.getResults());
    }

    @Override
    public MAccountsByCategory getAccountsForInvoice() {

        AccountsByCategory accountsByCategory = accountingService.getAccountsForInvoice();

        return new MAccountsByCategory(accountsByCategory);
    }

    @Override
    public MAccountList getAccountsForInvoice(MFilterParametrs filterParametrs, String type) {
        if (type == null || "".equals(type)) {
            return null;
        }
        ListingFilterParameter fp = filterParametrs.convertToFilterParametrs();
        AccountItem[] accountItems = accountingService.getAccountsForInvoice(fp, Lists.newArrayList(type));
        return new MAccountList(accountItems);
    }

    @Override
    public Boolean createDefaultAccountingParameters() {
        try {
            accountingService.createDefaultAccountingParametersForMobile();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public MProductCategoryList getCategoryList(MFilterParametrs mFilterParametrs) {

        ListingFilterParameter fp = new ListingFilterParameter();
        ListLoadConfig loadConfig = new ListLoadConfig();
        MFilterParametrs.convert(fp, mFilterParametrs, false);
        MFilterParametrs.convertToListLoadConfig(loadConfig, mFilterParametrs, false);
        return new MProductCategoryList(accountingService.getProductCategoriesList(fp));
    }

    @Override
    public Integer saveProductCategory(MProductCategoryListItem mListItem) {
        ProductCategoryItem productCategoryItem;
        Integer result = -1;
        try {
            if (mListItem.getObjectID() != null && !mListItem.getObjectID().equals(0)) {
                productCategoryItem = accountingService.getProductCategory(mListItem.getObjectID());
            } else {
                productCategoryItem = new ProductCategoryItem();
                productCategoryItem.setStoreFrontIDs(new Integer[0]);
                productCategoryItem.setWebsiteIDs(new Integer[0]);
            }

            result = accountingService.saveProductCategory(mListItem.convertToProductCategoryItem(productCategoryItem));
            return result > 0 ? result : -1;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public MProductCategoryListItem getProductCategory(Integer objectID) {
        if (objectID == null) {
            return null;
        }
        ProductCategoryItem productCategoryItem = accountingService.getProductCategory(objectID);

        return new MProductCategoryListItem(productCategoryItem);
    }

    @Override
    public Boolean deleteProductCategory(Integer objectID) {
        try {
            return accountingService.deleteProductCategory(objectID);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public MFixedAssetGroupItemList getFixedAssetGroups(MFilterParametrs fp) {
        ListingFilterParameter lfp = fp.convertToListingFilterParameter(null);
        List<FixedAssetGroupItem> groupItems = fixedAssetManager.getFixedAssetGroups(lfp);
        Integer totalCount = groupItems.size();
        groupItems = ListUtils.getSublist(groupItems, fp.getStart(), fp.getLimit());
        return new MFixedAssetGroupItemList(groupItems, totalCount);
    }

    @Override
    public MFixedAssetGroupItemList getFixedAssetGroups() {
        ListingFilterParameter lfp = new ListingFilterParameter();
        List<FixedAssetGroupItem> groupItems = fixedAssetManager.getFixedAssetGroups(lfp);
        return new MFixedAssetGroupItemList(groupItems, groupItems.size());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public MFixedAssetItemList getFixedAssets(MFilterParametrs fp, Integer categoryID) {
        if (fp != null && categoryID != null) {
            ListingFilterParameter lfp = fp.convertToListingFilterParameter(null);
            lfp.setCategoryID(categoryID);
            ListResult<FixedAssetItem> assetItems = fixedAssetService.getFixedAssets(lfp);
            MFixedAssetItemList resultList = new MFixedAssetItemList();
            if (assetItems != null && assetItems.getList() != null) {
                resultList.setTotalCount(assetItems.getTotal());
                List<MFixedAssetItem> fixedAssetItems = new ArrayList<>();
                EdsUser user = fixedAssetManager.getUser();
                Integer companyID = user.getCompany().getObjectID();
                String dateFormat = ServerUtils.getShortDateFormat(user);
                for (FixedAssetItem item : assetItems.getList()) {
                    item = fixedAssetService.getFixedAssetData(item.getObjectID());
                    fixedAssetItems.add(new MFixedAssetItem(item, companyID, dateFormat));
                }
                resultList.setFixedAssetItem(fixedAssetItems);
            }

            return resultList;
        }
        return null;
    }

    @Override
    public Integer saveFixedAsset(MFixedAssetItem mFixedAssetItem) {
        if (mFixedAssetItem == null) {
            return -1;
        }
        Integer result = -1;
        FixedAssetItem assetItem = null;
        try {
            if (mFixedAssetItem.getObjectID() != null && mFixedAssetItem.getObjectID() > 0) {
                assetItem = fixedAssetService.getFixedAssetData(mFixedAssetItem.getObjectID());
            }
            result = fixedAssetService.saveFixedAssetData(mFixedAssetItem.convert(assetItem));
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            result = -2;
        } catch (Exception e) {
            e.printStackTrace();
            result = -1;
        }
        return result;
    }

    @Override
    public Boolean deleteFixedAsset(Integer objectID) {
        if (objectID == null || objectID.equals(0)) {
            return null;
        }

        try {
            fixedAssetService.deleteFixedAsset(objectID);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public MNumberData generateFixedAssetNumber() {
        return new MNumberData(fixedAssetService.generateFixedAssetNumber());
    }

    @Override
    public Boolean sendFixedAssetCountResults(ArrayList<String> existingFixedAssetIDs) {
        if (existingFixedAssetIDs == null || existingFixedAssetIDs.size() == 0) {
            return Boolean.FALSE;
        }

        try {
            fixedAssetService.sendFixedAssetCountResults(existingFixedAssetIDs.toArray(new String[]{}));
            return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }
}
