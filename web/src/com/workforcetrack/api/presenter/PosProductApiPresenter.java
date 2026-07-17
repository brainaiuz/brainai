package com.workforcetrack.api.presenter;


import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sancho
 * Date: 16.03.13
 * Time: 13:35
 * To change this template use File | Settings | File Templates.
 */
public class PosProductApiPresenter extends BaseApiPresenter {

    public static final String PRODUCT_NUMBER = "productNumber";
    public static final String TYPE = "type";
    public static final String COST_PRICE = "costPrice";
    public static final String SELLING_PRICE = "unitPrice";
    public static final String ITEMS_IN_STOCK = "itemsInStock";
    public static final String SALE_ORDER_QUANTITY = "saleOrderQuantity";
    public static final String TAX_RATE = "taxRate";
    public static final String TYPE_NAME = "typeName";
    public static final String ACCOUNT_ID = "accountID";
    public static final String ACCOUNT_NAME = "accountName";
    public static final String CATEGORY = "category";

    public static final String DEFAULT_IMAGE_URL = "defaultImageURL";

    public static Map<String, Object> convertToMapItem(ProductItem item) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (item != null) {
            map.put(OBJECT_ID, item.getObjectId());
            map.put(NAME, item.getName());
            map.put(DESCRIPTION, item.getDescription());
            map.put(PRODUCT_NUMBER, item.getProductNumber());
            map.put(TYPE, item.getType());
            map.put(TYPE_NAME, item.getTypeName());
            map.put(COST_PRICE, item.getCostPrice());
            map.put(SELLING_PRICE, item.getUnitpPrice());
            map.put(ITEMS_IN_STOCK, item.getItemsInStock());
            map.put(TAX_RATE, item.getTaxRate());
            map.put(SALE_ORDER_QUANTITY, item.getOnSaleOrderQty());
            map.put(CATEGORY, item.getCategory());
            map.put(ACCOUNT_ID, item.getAccountID());
            map.put(ACCOUNT_NAME, item.getAccount());
        }

        return map;
    }

    public static Map<String, Object> convertToMapListing(ListResult<ProductItem> items) {
        Map<String, Object> map = new LinkedHashMap<>();
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        if (items.getTotal() != null && items.getTotal() > 0) {
            for (ProductItem item : items.getList()) {
                list.add(convertToMapItem(item));
            }
        }
        map.put(TOTAL_COUNT, items.getTotal());
        map.put(ITEMS, list);
        return map;
    }


}
