package com.edatasite.workforce.gwt.core.client.rpc.solr;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: HaveANiceDay
 * Date: 15.10.11
 * Time: 11:37
 * To change this template use File | Settings | File Templates.
 */

public final class SolrProductServiceRepresenter implements IsSerializable {
    public static final String PRODUCT_SOLR_DOC = "ProductSolrInputDocument";
    public static final String WAREHOUSE_SOLR_DOC = "WarehouseSolrInputDocument";

    public static final String SPLIT = "@";
    public static final String FIELD_COMPOSITE_ID = "oid";
    public static final String FIELD_COMPANY_ID = "companyId";

    public static final String FIELD_DOC_TYPE = "docType";

    public static final String FIELD_PRODUCT_ID = "productId";
    public static final String FIELD_PRODUCT_PARENT_ID = "productParentId";
    public static final String FIELD_PRODUCT_NUMBER = "productNumber";
    public static final String FIELD_PRODUCT_NAME = "productName";
    public static final String FIELD_PRODUCT_TYPE_ID = "productTypeId";
    public static final String FIELD_PRODUCT_TYPE_NAME = "productTypeName";
    public static final String FIELD_PRODUCT_TYPE_ID_NAME = "productTypeIdName";
    public static final String FIELD_PRODUCT_RENTAL_ITEM_ID = "productRentalItemId";

    public static final String FIELD_PRODUCT_DISCOUNT_TYPE_ID = "productDiscountTypeId";
    public static final String FIELD_PRODUCT_DISCOUNT_TYPE_NAME = "productDiscountTypeName";
    public static final String FIELD_PRODUCT_DISCOUNT_AMOUNT = "productDiscountAmount";


    public static final String FIELD_PRODUCT_ACTIVE = "productActive";
    public static final String FIELD_RENT_STATUS = "rentStatus";
    public static final String FIELD_PRODUCT_STOREFRONT_ENABLE = "productStorefrontEnable";
    public static final String FIELD_ACCOUNT_ID = "accountId";
    public static final String FIELD_ACCOUNT_NAME = "accountName";
    public static final String FIELD_ACCOUNT_ID_NAME = "accountIdName";
    public static final String FIELD_COGS_ACCOUNT_ID = "cogsAccountId";
    public static final String FIELD_COGS_ACCOUNT_ID_NAME = "cogsAccountIdName";
    public static final String FIELD_COGS_ACCOUNT_NAME = "cogsAccountName";
    public static final String FIELD_ASSET_ACCOUNT_ID = "assetAccountId";
    public static final String FIELD_ASSET_ACCOUNT_ID_NAME = "assetAccountIdName";
    public static final String FIELD_ASSET_ACCOUNT_NAME = "assetAccountName";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_COMPOSITE = "COMPOSITE";
    public static final String FIELD_UNITPRICE = "unitprice";
    public static final String FIELD_COSTPRICE = "costprice";
    public static final String FIELD_TAXRATE = "taxrate";
    public static final String FIELD_TAXRATE_ID = "taxrateId";
    public static final String FIELD_TAX_EFFECTIVE_RATE = "taxEffectiveRate";
    public static final String FIELD_VENDOR = "vendor";
    public static final String FIELD_CATEGORY = "category";
    public static final String FIELD_CATEGORY_ID = "categoryId";
    public static final String FIELD_PARENT_CATEGORY = "parentCategory";
    public static final String FIELD_PARENT_CATEGORY_ID = "parentCategoryId";
    public static final String FIELD_INVENTORY_TRACKING_ENABLED = "inventoryTrackingEnabled";
    public static final String FIELD_TRACKING_BATCHES_ENABLED = "trackBatchesEnabled";
    public static final String FIELD_PART_NUMBER = "partNumber";
    public static final String FIELD_BARCODE = "barcode";
    public static final String FIELD_QUANTITY_ON_HAND = "quantityOnHand";
    public static final String FIELD_MANUFACTURER = "manufacturer";
    public static final String FIELD_SKU_NUMBER = "skuNumber";
    public static final String FIELD_UPC_NUMBER = "upsNumber";
    public static final String FIELD_SUBSIDIARY_PRODUCT_UNIQ_NUM = "subsidiaryProductUniqNum";
    public static final String FIELD_AVERAGE_COST = "averageCost";
    public static final String FIELD_UNIT_MEASUREMENT_ID = "unitMeasureMentId";
    public static final String FIELD_UNIT_MEASUREMENT_NAME = "unitMeasureMentName";
    public static final String FIELD_UNIT_MEASUREMENT_ID_NAME = "unitMeasurementIdName";
    public static final String FIELD_CREATED_DATE = "createdDate";
    public static final String FIELD_UPDATED_DATE = "updatedDate";

    public static final String FIELD_BRAND_ID = "brandId";
    public static final String FIELD_BRAND_NAME = "brandName";
    public static final String FIELD_BRAND_ID_NAME = "brandIdName";

    public static final String FIELD_MULTI_SUPPLIER_ID = "multiSupplierId";
    public static final String FIELD_MULTI_SUPPLIER_NAME = "multiSupplierName";
    public static final String FIELD_MULTI_SUPPLIER_ID_NAME = "multiSupplierIdName";
    public static final String FIELD_MULTI_SUPPLIER_NUMBER = "multiSupplierNumber";

    public static final String FIELD_MULTI_LOCATION_ID = "multiLocationId";
    public static final String FIELD_MULTI_LOCATION_NAME = "multiLocationName";
    public static final String FIELD_MULTI_LOCATION_ID_NAME = "multiLocationIdName";

    public static final String FIELD_WAREHOUSE_ID = "warehouseId";
    public static final String FIELD_WAREHOUSE_NAME = "warehouseName";
    public static final String FIELD_WAREHOUSE_STOCK = "warehouseStock";

    public static final String FIELD_CREATOR_ID = "creatorId";
    public static final String FIELD_CREATOR_NAME = "creatorName";
    public static final String FIELD_CREATOR_ID_NAME = "creatorIdName";
    public static final String FIELD_CUSTOM_DESCRIPTION = "newProductCustomDescription";

    public static final String FIELD_UPDATER_ID = "updaterId";
    public static final String FIELD_UPDATER_NAME = "updaterName";
    public static final String FIELD_UPDATER_ID_NAME = "updaterIdName";

    public static final String SORTABLE_PRODUCT_NUMBER = "sortableProductNumber";
    public static final String SORTABLE_PRODUCT_NAME = "sortableProductName";
    public static final String SORTABLE_DESCRIPTION = "sortableDescription";
    public static final String SORTABLE_PRODUCT_TYPE_NAME = "sortableProductTypeName";
    public static final String SORTABLE_PRODUCT_DISCOUNT_TYPE_NAME = "sortableproductDiscountTypeName";
    public static final String SORTABLE_ACCOUNT_NAME = "sortableAccountName";
    public static final String SORTABLE_TAXRATE = "sortableTaxrate";
    public static final String SORTABLE_VENDOR = "sortableVendor";
    public static final String SORTABLE_CATEGORY = "sortableCategory";
    public static final String SORTABLE_CREATOR_NAME = "sortableCreatorName";
    public static final String SORTABLE_STATUS = "SORTABLE_STATUS";

}
