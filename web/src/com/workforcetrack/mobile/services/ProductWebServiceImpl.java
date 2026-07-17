package com.workforcetrack.mobile.services;

import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductLocationItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ModuleManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.workforcetrack.mobile.rpc.accounting.MProductList;
import com.workforcetrack.mobile.rpc.accounting.MProductListItem;
import com.workforcetrack.mobile.rpc.accounting.MTaxList;
import com.workforcetrack.mobile.rpc.client.MFilterParametrs;
import com.workforcetrack.mobile.rpc.client.MSelectItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/23/11
 * Time: 3:18 PM
 */
@Service("productWebService")
public class ProductWebServiceImpl implements ProductWebService {

    @Autowired
    private ProductService productService;
    @Autowired
    private AccountingService accountingService;
    @Autowired
    GenericSettingsManager genericSettingsManager;
    @Autowired
    ModuleManager moduleManager;
    @Autowired
    FinancialSettingsManager financialSettingsManager;

    @Override
    public MProductListItem getBaseData(Integer objectID) {
        NewProduct newProduct = productService.getProductBaseData(objectID);

        return new MProductListItem(newProduct);
    }

    @Override
    public MProductListItem getBaseData() {
        return getBaseData(null);
    }

    @Override
    public MProductList getList(MFilterParametrs mFilterParametrs) {
        if (mFilterParametrs == null)
            return null;

        ListingFilterParameter filterParametrs = mFilterParametrs.convertToListingFilterParameter(null);
        ListResult<ProductItem> productList = productService.getProductsList(filterParametrs);
        return new MProductList(productList);
    }

    @Override
    public MProductListItem get(Integer objectID) {
        if (objectID == null) {
            return null;
        }

        ProductItem productListItem = productService.getProductByID(objectID);
        MProductListItem mProductListItem = new MProductListItem(productListItem);
        mProductListItem.getTaxRateByTaxIDs();

        NewProduct newProduct = productService.getProduct(objectID);
        if (newProduct.getProductCategories() != null && newProduct.getProductCategories().length > 0) {
            mProductListItem.productCategories = new ArrayList<>();
            for (SelectItem item : newProduct.getProductCategories()) {
                mProductListItem.productCategories.add(new MSelectItem(item.getId(), item.getName()));
            }
        }
        if (newProduct.getProductLocations() != null && newProduct.getProductLocations().length > 0) {
            mProductListItem.productLocations = new ArrayList<>();
            for (ProductLocationItem item : newProduct.getProductLocations()) {
                mProductListItem.productLocations.add(new MSelectItem(item.getWarehouseID(), item.getWarehouseName()));
            }
        }
        mProductListItem.setUnitPrice(newProduct.getUnitPrice());
        mProductListItem.setTaxList(new MTaxList(newProduct.getTaxList()));
        mProductListItem.setAccount(productListItem.getAccount());
        return mProductListItem;
    }

    private List<MSelectItem> getWarehouselist() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setLookUp(true);
        fp.setLimit(20);
        SelectItem[] wareHouseList = accountingService.getWarehousesForLookUp(fp);
        List<MSelectItem> list = new ArrayList<>();
        for (SelectItem selectItem : wareHouseList){
            list.add(new MSelectItem(selectItem.getId(), selectItem.getName()));
        }
        return list;
    }

    private ProductLocationItem setWarehouselist(Integer warehouseID) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setWarehouseID(warehouseID);
        ListResult<ProductLocationItem> list = accountingService.getWarehouseProductsList(fp);
        return list.getList().size() > 0 ? list.getList().get(0): null;
    }

    @Override
    public MProductListItem edit(Integer objectID) {
        if (objectID == null)
            return null;

        NewProduct productListItem = productService.getProductBaseData(objectID);
        MProductListItem mProductListItem = new MProductListItem(productListItem);
        mProductListItem.getTaxRateByTaxIDs();
        return mProductListItem;
    }

    @Override
    public MProductListItem get() {
        NewProduct productListItem = productService.getProduct(null);
        MProductListItem mProductListItem = new MProductListItem(productListItem);
        List<MSelectItem> list = getWarehouselist();
        mProductListItem.setProductLocations(list);
        mProductListItem.getTaxRateByTaxIDs();
        mProductListItem.setAccount(productListItem.getAccountItem() != null ? productListItem.getAccountItem().getName() : null);
        return mProductListItem;
    }

    @Override
    public Integer save(MProductListItem mItem) {
        NewProduct newProduct;
        try {
            if (mItem.getObjectID() != null && !mItem.getObjectID().equals(0)) {
                newProduct = productService.getProduct(mItem.getObjectID());
            } else {
                newProduct = new NewProduct();
                newProduct.setNumberData(productService.generateProductNumber());
                if (mItem.getType().equals(AccountingConstants.INVENTORY_ITEM)) {
                    newProduct.setEnableCompanyIT(true);
                    newProduct.setEnableIT(true);
                    EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
                    boolean isMultiWarehouseEnabled = financialSettings.getEnableMultiWarehouse();
                    if(isMultiWarehouseEnabled) {
                        List<MSelectItem> list = getWarehouselist();
                        for (MSelectItem selectItem : list) {
                            if (mItem.getWarehouseName().equals(selectItem.getName())) {
                                ProductLocationItem productLocationItem = new ProductLocationItem();
                                productLocationItem = setWarehouselist(selectItem.getObjectID());
                                productLocationItem.setObjectID(null);
                                productLocationItem.setQty(BigDecimal.valueOf(mItem.getTotalQtyOnHand().doubleValue()));
                                newProduct.setProductLocations(new ProductLocationItem[]{productLocationItem});
                                break;
                            }
                        }
                    }
                }
            }
            MProductListItem.convert(newProduct, mItem, false);
            return productService.saveProduct(newProduct).getId();
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }

    }

    @Override
    public Boolean delete(Integer objectID) {
        if (objectID == null)
            return false;

        try {
            productService.deleteProduct(objectID);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
