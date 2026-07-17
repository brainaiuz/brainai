package com.edatasite.workforce.rest.v2.release10.accounting;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsInvoicingSettings;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsAccountType;
import com.edatasite.workforce.core.domain.accounting.EdsBrand;
import com.edatasite.workforce.core.domain.accounting.EdsProductCategory;
import com.edatasite.workforce.core.domain.accounting.EdsWarehouse;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductCategoryItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductKitItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductLocationItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.AssemblyItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.product.ProductPicture;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.accounting.server.app.ProductServiceLocal;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.InvoicingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.BrandManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemStockManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.WarehouseManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.WarehouseTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.AssemblyItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.CogsAccountTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.DiscountTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.IncomeAccountTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.InventoryStockItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.ProductBrandListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.ProductBrandTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.ProductCategoryListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.ProductCategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.ProductDetailsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.ProductItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.ProductKitItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.ProductListItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.ProductParentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.ProductPicturesTo;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.ProductRequestListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.ProductTypeTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.ProductsSearchByCustomFieldsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.TaxTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.UnitMeasurementTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.ZapierProductVariantTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ApiResult;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.EntityCategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseResultListData;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.CustomFieldDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.INVENTORY_ITEM;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.RECEIVABLE;

/**
 * Created by Dilsh0d on 11/2/2017.
 */
@Tag(name = "Product", description = "Product API")
@RestController()
@RequestMapping(value = "/2", headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE/*, MediaType.APPLICATION_XML_VALUE*/},
        consumes = {MediaType.ALL_VALUE})
public class ApiProductControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiProductControllerV2.class);
    @Autowired
    protected InvoicingSettingsManager invoicingSettingsManager;
    @Autowired
    private ProductServiceLocal productServiceLocal;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private ProductCategoryManager productCategoryManager;
    @Autowired
    private AccountingServiceLocal accountingServiceLocal;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private WarehouseManager warehouseManager;
    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    private CommonServiceLocal commonService;
    @Autowired
    private CompanyCustomFieldsManager companyCFManager;
    @Autowired
    private BrandManager brandManager;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private ItemStockManager itemStockManager;

    @Operation(summary = "Get Product List for Zapier", description = "Retrieves list of products based on provided search_text")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of product details."),
            @ApiResponse(responseCode = "400", description = "Start point and limit is required"),
            @ApiResponse(responseCode = "422", description = "Start point and limit can not be zero at the same time"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/products_zapier", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getZapierProductList(@RequestBody ProductRequestListTO requestListData) throws RestException {
        ApiResult result = (ApiResult) getProductList(requestListData);
        return ((ResponseResultListData) (result.getData())).getList();
    }

    @Operation(summary = "Get Product Variants List for Zapier", description = "Retrieves list of product variants based on provided search_text")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of product details."),
            @ApiResponse(responseCode = "400", description = "Start point and limit is required"),
            @ApiResponse(responseCode = "422", description = "Start point and limit can not be zero at the same time"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/product_variants_zapier", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getZapierProductVariantList(@RequestParam(value = "warehouse_id", required = false) Integer warehouseId) throws RestException {
        ArrayList<ZapierProductVariantTO> result = new ArrayList<>();
        try {
            List<EdsItem> productVariantsList = itemManager.getVariantsForZapier(warehouseId);

            for (EdsItem item : productVariantsList) {
                NewProduct productVariant = productServiceLocal.getProduct(item.getObjectID());
                productVariant.setItemsInStock(itemStockManager.getAvailableStock(productVariant.getObjectId(), warehouseId, null));
                ZapierProductVariantTO t = productServiceLocal.convertToZapierProductVariant(productVariant);
                if (t != null) {
                    result.add(t);
                }
            }
            return result;
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get Product List", description = "Retrieves list of products based on provided search_text")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of product details."),
            @ApiResponse(responseCode = "400", description = "Start point and limit is required"),
            @ApiResponse(responseCode = "422", description = "Start point and limit can not be zero at the same time"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/products", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getProductList(@RequestBody ProductRequestListTO requestListData) throws RestException {
        if (requestListData.getStart() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Start point is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestListData.getLimit() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Limit is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestListData.getStart().equals(requestListData.getLimit()) && requestListData.getLimit() == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Start point and limit can not be zero at the same time", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setStart(requestListData.getStart());
        filterParameter.setLimit(requestListData.getLimit());
        filterParameter.setSearchKey(requestListData.getSearch_text());
        filterParameter.setWarehouseID(requestListData.getWarehouse_id());
        filterParameter.setFromMobile(true);
        filterParameter.setLookUp(true);
        filterParameter.setShowProductBatches(requestListData.isIncludeBatches());
        filterParameter.setFeatured(true);
        filterParameter.setCategoryID(requestListData.getEntity_id());
        filterParameter.setShowChild(true);
        filterParameter.setBrandID(requestListData.getBrand_id());
        //Added for Javlon's Apteka
        filterParameter.setAvoidZero(requestListData.isAvoid_zero());

        //Also Retrieve custom field values
        List<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.ProductServiceView);
        if (CollectionUtils.isNotEmpty(customFieldsItems)) {
            List<String> columnCodeNames = customFieldsItems.stream().map(CompanyCustomFieldItem::getColumnCode).toList();
            ListPanelToolRpc panelTools = new ListPanelToolRpc();
            panelTools.setColumnCodeName(new ArrayList<>(columnCodeNames));
            filterParameter.setListPanelTool(panelTools);
        }


        try {
//            ProductSelectItem[] lookUpIds = productServiceLocal.getCompanyProductsByType(filterParameter);
//            if (lookUpIds != null && lookUpIds.length > 0) {
//                LinkedList<Integer> objectIds = new LinkedList<>();
//                for (ProductSelectItem productSelectItem : lookUpIds) {
//                    objectIds.add(productSelectItem.getId());
//                }
//                filterParameter.setObjectIDs(objectIds);
//            }
//            filterParameter.setSearchKey(null);
            ListResult<ProductItem> productListResult = productServiceLocal.getProductsListFromSolr(filterParameter);
            ArrayList<ProductListItemTO> productList = new ArrayList<>();

            EdsInvoicingSettings invoicingSettings = invoicingSettingsManager.getInvoiceSettings(invoicingSettingsManager.getUser().getCompany());
            for (ProductItem item : productListResult.getList()) {
                ProductListItemTO product = convertDTO(customFieldsItems, item);
                if (filterParameter.getWarehouseID() != null) {
                    product.setQuantity(itemStockManager.getAvailableStock(product.getId(), filterParameter.getWarehouseID(), null));
                }
                if (invoicingSettings != null && invoicingSettings.getTaxCalculationType() != null) {
                    product.setTaxCalculationType(invoicingSettings.getTaxCalculationType());
                }
                if (requestListData.isAvoid_zero() && (item.getItemsInStock() == null || item.getItemsInStock().doubleValue() <= 0)) {
                    productListResult.setTotal(productListResult.getTotal() - 1);
                } else {
                    productList.add(product);
                }

            }
            return successResponse(new ResponseResultListData<>(productList, productListResult.getTotal()));
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get Product List (By Custom Fields)", description = "Retrieves list of products based on provided search_text")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of product details."),
            @ApiResponse(responseCode = "400", description = "Start point and limit is required"),
            @ApiResponse(responseCode = "422", description = "Start point and limit can not be zero at the same time"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/products-cf", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getProductListByCustomFields(@RequestBody ProductsSearchByCustomFieldsTO requestListData) throws RestException {
        if (requestListData.getStart() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Start point is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestListData.getLimit() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Limit is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestListData.getStart().equals(requestListData.getLimit()) && requestListData.getLimit() == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Start point and limit can not be zero at the same time", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setStart(requestListData.getStart());
        filterParameter.setLimit(requestListData.getLimit());
        filterParameter.setSearchKey(requestListData.getSearch_text());
        filterParameter.setWarehouseID(requestListData.getWarehouse_id());
        filterParameter.setFromMobile(true);
        filterParameter.setLookUp(true);
        //Added for Javlon's Apteka
        filterParameter.setAvoidZero(requestListData.isAvoid_zero());

        //Also Retrieve custom field values
        List<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.ProductServiceView);
        if (CollectionUtils.isNotEmpty(customFieldsItems)) {
            List<String> columnCodeNames = customFieldsItems.stream().map(CompanyCustomFieldItem::getColumnCode).toList();
            ListPanelToolRpc panelTools = new ListPanelToolRpc();
            panelTools.setColumnCodeName(new ArrayList<>(columnCodeNames));
            filterParameter.setListPanelTool(panelTools);
        }


        try {
            ListResult<ProductItem> productListResult = productServiceLocal.getProductsListFromSolrGeneric(filterParameter, requestListData.getSearch_custom_fields());
            ArrayList<ProductListItemTO> productList = new ArrayList<>();
            for (ProductItem item : productListResult.getList()) {
                ProductListItemTO product = convertDTO(customFieldsItems, item);

                if (requestListData.isAvoid_zero() && (item.getItemsInStock() == null || item.getItemsInStock().doubleValue() <= 0)) {
                    productListResult.setTotal(productListResult.getTotal() - 1);
                } else {
                    productList.add(product);
                }

            }
            return successResponse(new ResponseResultListData<>(productList, productListResult.getTotal()));
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ProductListItemTO convertDTO(List<CompanyCustomFieldItem> customFieldsItems, ProductItem item) {
        ProductListItemTO product = new ProductListItemTO();
        product.setId(item.getObjectId());
        product.setDescription(item.getDescription());
        product.setVendor(item.getVendor());
        product.setQuantity(item.getItemsInStock());
        product.setImage_url(item.getDefaultPictureUrl());
        product.setName(item.getName());
        product.setNumber(item.getProductNumber());
        if (item.getMultiPrices() != null && !item.getMultiPrices().isEmpty()) {
            product.setMultiPrices(item.getMultiPrices());
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        if (item.getCreatedDate() != null) {
            product.setCreated_date(dateFormat.format(item.getCreatedDate()));
        }
        if (item.getUpdatedDate() != null) {
            try {

                product.setUpdated_date(dateFormat.format(item.getUpdatedDate()));

            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        if (item.getSkuNumber() != null && !"".equals(item.getSkuNumber())) {
            product.setSku_number(item.getSkuNumber());
        }
        if (item.getBarCodeString() != null && !"".equals(item.getBarCodeString())) {
            product.setBarcode(item.getBarCodeString());
        }
        product.setProduct_type(new ProductTypeTO(item.getType(), item.getTypeName()));
        if (item.getCategoryId() != null) {
            product.setCategory(new ProductCategoryTO(item.getCategoryId(), item.getCategory()));
        }
        if (item.getParentCategory() != null) {
            product.setCategory_parent(new ProductCategoryTO(item.getParentCategory().getId(), item.getParentCategory().getName()));
        }
        product.setTrackBatchesEnabled(item.getTrackBatchesEnabled());

        if (product.isTrackBatchesEnabled() && item.getBatchItems() != null) {
            product.setBatchItems(item.getBatchItems());
        }

        if (StringUtils.isNotBlank(item.getBrand())) {
            EdsBrand edsBrand = brandManager.getBrandByName(item.getBrand());
            ProductBrandTO productBrand = new ProductBrandTO();
            productBrand.setBrand_id(edsBrand.getObjectID());
            productBrand.setBrand_name(edsBrand.getName());
            productBrand.setBrand_description(edsBrand.getDescription());
            if (edsBrand.getImageID() != null) {
                productBrand.setImage_url(uploadManager.getUploadFileUrl(edsBrand.getImageID()));
            }
            product.setBrand(productBrand);
        }

        if (INVENTORY_ITEM.equals(item.getType()) || AccountingConstants.ASSEMBLY_ITEM.equals(item.getType())
                || AccountingConstants.PRODUCT_KIT.equals(item.getType())) {
            product.setUnit_price(item.getUnitpPrice());
            product.setCost_price(item.getCostPrice());

            if (INVENTORY_ITEM.equals(item.getType())) {
                ArrayList<InventoryStockItemTO> inventoryStockItems = new ArrayList<>();
                for (ProductLocationItem productLocationItem : item.getProductLocations()) {
                    InventoryStockItemTO inventoryStockItem = new InventoryStockItemTO();
                    inventoryStockItem.setWarehouse(new WarehouseTO(productLocationItem.getWarehouseID(), productLocationItem.getWarehouseName()));
                    inventoryStockItem.setQuantity_on_hand(productLocationItem.getQty());
                    inventoryStockItem.setReorder_point(productLocationItem.getMinReorderPoint());
                    inventoryStockItems.add(inventoryStockItem);
                }
                product.setInventory_stock_item_list(inventoryStockItems);
            }
        } else if (AccountingConstants.NON_INVENTORY_ITEM.equals(item.getType()) || AccountingConstants.SERVICE.equals(item.getType())
                || AccountingConstants.OTHER_CHARGE.equals(item.getType())) {
            product.setRate(item.getUnitpPrice());
        }
        ProductPicture[] productPictures = productServiceLocal.getProductPictures(item.getObjectId(), 0);
        List<ProductPicturesTo> newProductPictures = new ArrayList<>();
        for (ProductPicture picture : productPictures) {
            ProductPicturesTo picturesTo = new ProductPicturesTo();
            picturesTo.setName(picture.getName());
            picturesTo.setDefaultPicture(picture.isDefaultPicture());
            picturesTo.setPictureType(picture.getPictureType());
            picturesTo.setUrl(picture.getUrl());

            newProductPictures.add(picturesTo);
            product.setProduct_pictures(newProductPictures);
        }
        //Tax info added for Apteka (Javlon)
        product.setTax_id(item.getTaxAmountId());
        product.setTax_name(item.getTaxRate());
        product.setTax_effective_rate(item.getTaxAmount());
        //Discounts for koleso.uz (Musabek)
        product.setDiscounts(Arrays.asList(item.getDiscounts()));

        product.setAccount_id(item.getAccountID());
        product.setAccount_name(item.getAccount());
        //Product custom fields
        if (CollectionUtils.isNotEmpty(customFieldsItems)) {

            HashMap<Integer, Map<String, String>> cfLookupVals = new HashMap();
            customFieldsItems.forEach(cf -> {
                if (Constants.TYPE_ENTITY_LOOKUP.equals(cf.getUiType())) {

                    Map<String, String> vals = Arrays.stream(companyCFManager.getCustomFieldDataByQuery(SecurityContext.getCompanyID(), cf.getQuery()))
                            .toList()
                            .stream()
                            .collect(
                                    Collectors.toMap(x -> x.getId().toString(), SelectItem::getName)
                            );
                    cfLookupVals.put(cf.getObjectId(), vals);
                }
            });
            List<CompanyCustomFieldItem> productCustomFieldItems = new ArrayList<>();
            customFieldsItems.forEach(cf -> {
                if (item.getCustomFieldsValue(cf.getColumnCode()) != null) {
                    if (cf.getColumnCode().contains("string")) {
                        if (Constants.TYPE_ENTITY_LOOKUP.equals(cf.getUiType())) {
                            cf.setFieldStringValue(cfLookupVals.get(cf.getObjectId()).get(item.getCustomFieldsValue(cf.getColumnCode()).toString()));
                        } else {
                            cf.setFieldStringValue(item.getCustomFieldsValue(cf.getColumnCode()).toString());
                        }
                    } else if (cf.getColumnCode().contains("double")) {
                        cf.setFieldStringValue(item.getCustomFieldsValue(cf.getColumnCode()).toString());
                    } else if (cf.getColumnCode().contains("date")) {
                        cf.setFieldDateNonConvertedValue(new DateNonConvertable((Date) (item.getCustomFieldsValue(cf.getColumnCode()))));
                    }
                    productCustomFieldItems.add(cf);
                }
            });
            product.setCustom_fields(getCustomFields(productCustomFieldItems));
        }
        if (item.getCategoryCustomFieldItems() != null && !item.getCategoryCustomFieldItems().isEmpty()) {
            List<CustomFieldDto> cfs = new ArrayList<>();
            item.getCategoryCustomFieldItems().forEach(m -> {
                CustomFieldDto cf = CustomFieldsUtils.getCustomFieldDto(m);
                if (cf.getValue() != null) {
                    cfs.add(cf);
                }
            });
            product.setCategoryCustomFields(cfs);
        }
        return product;
    }

    @Operation(summary = "Create Product", description = "Create Product")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response will have true if created or false if error occured."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/product/create", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object createProduct(@RequestBody ProductDetailsTO productTO) throws RestException {

        if (productTO.getProduct_type() == null || productTO.getProduct_type().getType_id() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Product type required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isNotBlank(productTO.getUpc_number())) {
            if (accountingManager.isProductNumberExists(productTO.getUpc_number(), null)) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Product with given upc_number already exist", CONFLICT, HttpStatus.BAD_REQUEST);
            }
        }
        if (StringUtils.isNotBlank(productTO.getNumber())) {
            if (accountingManager.isProductNumberExists(productTO.getNumber(), null)) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Product with given number already exist", CONFLICT, HttpStatus.BAD_REQUEST);
            }
        }

        NewProduct newProduct = convertProduct(productTO);

        //CREATE PRODUCT
        ProductSelectItem productSelectItem = productServiceLocal.saveProduct(newProduct);

        if (productSelectItem != null && productSelectItem.getId() != null && productSelectItem.getId() > 0) {

            ProductListItemTO productListItemTO = new ProductListItemTO();
            productListItemTO.setId(productSelectItem.getId());
            return new ApiResult(productListItemTO);

        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, GENERAL_ERROR_MESSAGE, SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Create Product For Zapier", description = "Create Product")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response will have true if created or false if error occured."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/product/create_zapier", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object createProductForZapier(@RequestBody String json) throws RestException {

        log.info("!!!!!!!!!!!!!!!!!CREATE PRODUCT: {}", json);
        ProductListItemTO productTO = null;
        try {
            productTO = new ObjectMapper().readValue(json, ProductListItemTO.class);
        } catch (Exception e) {
            log.error("", e);
        }
        NewProduct newProduct = convertProduct(productTO);


        //CREATE PRODUCT
        ProductSelectItem parentProduct = productServiceLocal.saveProduct(newProduct, false);

        if (parentProduct != null && parentProduct.getId() != null && parentProduct.getId() > 0) {

            ProductListItemTO productListItemTO = new ProductListItemTO();
            productListItemTO.setId(parentProduct.getId());
            //PARSE PRODUCT VARIANTS FROM SHOPIFIER
            ArrayList<NewProduct> variants = getVariants(productTO.getVariants(), parentProduct.getId(), newProduct);
            if (variants != null) {
                /*EdsItem pItem = itemManager.get(parentProduct.getId());
                if(pItem!=null) {
                    pItem.setHasVariations(true);
                    itemManager.update(pItem);
                }
                for(NewProduct variant : variants) {
                    ProductSelectItem createdVariant = productServiceLocal.saveProduct(variant, false);
                    log.info("Product Variant {} were created id={}", variant.getItemName(), createdVariant.getId());
                }*/
                if (variants.size() > 1) {
                    productServiceLocal.saveVariationProducts(variants.toArray(new NewProduct[]{}), parentProduct.getId(), false);
                } else if (variants.size() == 1
                        && StringUtils.isNotBlank(variants.get(0).getItemName()) && variants.get(0).getItemName().startsWith("Default")) {
                    //Dont create variant if there is only one default variant
                    newProduct.setObjectId(parentProduct.getId());
                    newProduct.setInternalSKUNumber(variants.get(0).getInternalSKUNumber());
                    newProduct.setBarCodeText(variants.get(0).getBarCodeText());

                    try {
                        newProduct.setUnitPrice(variants.get(0).getUnitPrice());
                    } catch (Exception e) {
                        log.warn("Got Error: ", e);
                        newProduct.setUnitPrice(new BigDecimal("0"));
                    }

                    try {
                        newProduct.setQuantity(variants.get(0).getQuantity());
                    } catch (Exception e) {
                        log.warn("Got Error: ", e);
                        newProduct.setQuantity(new BigDecimal("0"));
                    }
                    productServiceLocal.saveProduct(newProduct, false);
                }
            }
            return new ApiResult(productListItemTO);

        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, GENERAL_ERROR_MESSAGE, SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    ArrayList<NewProduct> getVariants(String variantsFromZapier, Integer parentProductId, NewProduct parentProduct) {
        ArrayList<NewProduct> result = new ArrayList<>();

        if (StringUtils.isNotBlank(variantsFromZapier)) {
            String[] variants = variantsFromZapier.split("\n\n");
            ArrayList<HashMap<String, String>> vars = new ArrayList<>();
            for (String variant : variants) {
                if (StringUtils.isNotBlank(variant)) {
                    HashMap<String, String> variantMap = new HashMap<>();
                    String[] variantFields = variant.split("\n");
//                  log.info(variantFields);
                    for (String fieldValue : variantFields) {
                        String[] keyval = fieldValue.split(":");
                        variantMap.put(keyval[0].trim(), keyval[1].trim());
//                  log.info(keyval[0] + " - " + keyval[1]);
                    }
                    vars.add(variantMap);
                }
            }

            for (HashMap<String, String> variant : vars) {
                NewProduct v = SerializationUtils.clone(parentProduct);
                v.setParentId(parentProductId);
                v.setObjectId(null);
                try {
                    v.setSellingPrice(new BigDecimal(variant.get("price")));
                } catch (Exception e) {
                    log.error("Error", e);
                    v.setSellingPrice(new BigDecimal("0"));
                }
                if (StringUtils.isNotBlank(variant.get("cost"))) {
                    try {
                        v.setUnitPrice(new BigDecimal(variant.get("cost")));
                    } catch (Exception e) {
                        log.error("Error", e);

                    }
                }
                try {
                    v.setZapiervariantid(Long.valueOf(variant.get("id")));
                } catch (Exception e) {
                    log.error("Error", e);
                }

                v.setItemName(parentProduct.getItemName() + " - " + variant.get("title"));
                v.setInternalSKUNumber(variant.get("sku"));
                v.setBarCodeText(variant.get("barcode"));
                try {
                    v.setQuantity(new BigDecimal(variant.get("inventory_quantity")));
                } catch (Exception e) {
                    log.error("Error", e);
                    v.setQuantity(new BigDecimal("0"));
                }
                v.setItemsInStock(v.getQuantity());
                v.setHasVariations(false);

                EdsWarehouse defaultWarehouse = warehouseManager.getDefaultWarehouse();
                if (defaultWarehouse != null) {
                    v.setTotalValue(Optional.ofNullable(v.getQuantity()).orElse(BigDecimal.ZERO).multiply(Optional.ofNullable(v.getUnitPrice()).orElse(BigDecimal.ZERO)));

                    v.setWarehouse(defaultWarehouse.getAsSelectItem());
                    List<ProductLocationItem> locationItems = new ArrayList<>();
                    ProductLocationItem locationItem = new ProductLocationItem();
                    locationItem.setQty(v.getQuantity());
                    locationItem.setMinReorderPoint(BigDecimal.ONE);
                    locationItem.setMinReorderQty(BigDecimal.ONE);
                    locationItem.setWarehouseID(defaultWarehouse.getObjectID());

                    locationItems.add(locationItem);

                    v.setProductLocations(locationItems.toArray(new ProductLocationItem[]{}));
                }

                result.add(v);
            }
        }
        return result;
    }


    @Operation(summary = "Create Product Variant For Zapier", description = "Create Product Variants")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response will have true if created or false if error occured."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/product/variant/create_zapier", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object createProductVariantForZapier(@RequestBody ZapierProductVariantTO productVariantTO) throws RestException {

        NewProduct newProduct = convertProduct(productVariantTO);

        //CREATE PRODUCT
        ProductSelectItem productSelectItem = productServiceLocal.saveProduct(newProduct, false);

        if (productSelectItem != null && productSelectItem.getId() != null && productSelectItem.getId() > 0) {

            ProductListItemTO productListItemTO = new ProductListItemTO();
            productListItemTO.setId(productSelectItem.getId());
            return new ApiResult(productListItemTO);

        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, GENERAL_ERROR_MESSAGE, SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update Product", description = "Update Product")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response will have true if created or false if error occured."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/product/update", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object updateProduct(@RequestBody ProductDetailsTO productTO) throws RestException {

        if (productTO.getId() == null || productTO.getId() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Product id invalid required", INVALID, HttpStatus.BAD_REQUEST);
        }
        if (productTO.getProduct_type() == null || productTO.getProduct_type().getType_id() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Product type required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isNotBlank(productTO.getUpc_number())) {
            if (accountingManager.isProductNumberExists(productTO.getUpc_number(), productTO.getId())) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Product with given upc_number already exist", CONFLICT, HttpStatus.BAD_REQUEST);
            }
        }
        if (StringUtils.isNotBlank(productTO.getNumber())) {
            if (accountingManager.isProductNumberExists(productTO.getNumber(), productTO.getId())) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Product with given number already exist", CONFLICT, HttpStatus.BAD_REQUEST);
            }
        }

        NewProduct newProduct = convertProduct(productTO);
        newProduct.setObjectId(productTO.getId());

        //UPDATE PRODUCT
        ProductSelectItem productSelectItem = productServiceLocal.saveProduct(newProduct);

        if (productSelectItem != null && productSelectItem.getId() != null && productSelectItem.getId() > 0) {

            ProductListItemTO productListItemTO = new ProductListItemTO();
            productListItemTO.setId(productSelectItem.getId());
            return new ApiResult(productListItemTO);

        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, GENERAL_ERROR_MESSAGE, SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private NewProduct convertProduct(ProductDetailsTO productTO) {
        NewProduct newProduct = new NewProduct();
        EdsCompany company = itemManager.getUser().getCompany();

        newProduct.setType(productTO.getProduct_type().getType_id());
        if (StringUtils.isNotBlank(productTO.getNumber())) {
            newProduct.setNumberData(new NumberData(productTO.getNumber(), null));
        }
        newProduct.setItemName(productTO.getName());
        newProduct.setDescription(productTO.getDescription());

        if (productTO.getCategory() != null && productTO.getCategory().getCategory_id() != null) {
            newProduct.setCategoryID(productTO.getCategory().getCategory_id());
        }

        if (productTO.getTax() != null && productTO.getTax().getTax_id() != null) {
            newProduct.setVatId(productTO.getTax().getTax_id());
        }
        if (productTO.getDouble_tax() != null && productTO.getDouble_tax().getTax_id() != null) {
            newProduct.setDoubleVatId(productTO.getDouble_tax().getTax_id());
        }

        newProduct.setInternalSKUNumber(productTO.getSku_number());
        newProduct.setManufacturer(productTO.getManufacturer());
        newProduct.setPartNumber(productTO.getPart_number());
        newProduct.setBarCodeText(productTO.getBarcode());
        newProduct.setActive(productTO.getActive() != null ? productTO.getActive() : Boolean.FALSE);
        if (productTO.getCustomer() != null && productTO.getCustomer().getCustomer_id() != null) {
            newProduct.setCustomer(new SelectItem(productTO.getCustomer().getCustomer_id()));
        }
        /*if (item.getObjectID() != null) {
            for (EdsItem child : itemManager.getChildProducts(item.getObjectID())) {
                activateProduct(child.getObjectID(), item.isActive());
            }
        }*/

        newProduct.setUpcNumber(productTO.getUpc_number());
        if (productTO.getUnit_measurement() != null && productTO.getUnit_measurement().getMeasurement_id() != null) {
            newProduct.setUnitMeasurementID(productTO.getUnit_measurement().getMeasurement_id());
        }

        newProduct.setWeightPerUnit(productTO.getWeight_per_unit());
        if (productTO.getCost_price() != null) {
            newProduct.setUnitPrice(productTO.getCost_price());
        } else {
            newProduct.setUnitPrice(BigDecimal.ZERO);
        }
        newProduct.setSellingPrice(productTO.getUnit_price());
        newProduct.setComission(productTO.getComission());
        newProduct.setEnableIT(productTO.getEnable_it() != null ? productTO.getEnable_it() : Boolean.FALSE);
//        newProduct.setShowOnOpportunity(productTO.getShow_on_opportunity() != null ? productTO.getShow_on_opportunity() : Boolean.FALSE);
        if (productTO.getBrand() != null) {
            newProduct.setBrandID(productTO.getBrand().getBrand_id());
        }

        //set Income account
        if (productTO.getIncome_account() != null) {
            newProduct.setAccountId(productTO.getIncome_account().getIncome_account_id());
        }

        //set Cogs account
        if (productTO.getCogs_account() != null) {
            newProduct.setCogsAccountID(productTO.getCogs_account().getCogs_account_id());
        }

        //Inventory Item Total information
        //set asset account
        if (productTO.getAsset_account() != null) {
            newProduct.setAssetAccountID(productTO.getAsset_account().getAsset_account_id());
        }

//        newProduct.setGlobalReorderPoint(productTO.getGlobalReorderPoint());
        if (productTO.getQuantity() != null) {
            newProduct.setQuantity(productTO.getQuantity());
        } else {
            newProduct.setQuantity(BigDecimal.ZERO);
        }
        newProduct.setTotalValue(newProduct.getUnitPrice().multiply(newProduct.getQuantity()));

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        if (StringUtils.isNotBlank(productTO.getAs_of_date())) {
            try {
                newProduct.setAsOf(new DateNonConvertable(longDateTimezoneFormat.parse(productTO.getAs_of_date())));
            } catch (ParseException e) {
                log.error("", e);
            }
        }
        newProduct.setPurchasedFromSupplier(productTO.getPurchased_from_supplier() != null ? productTO.getPurchased_from_supplier() : Boolean.FALSE);

        if (productTO.getSuppliers() != null && productTO.getSuppliers().size() > 0) {
            newProduct.setSuppliers(productTO.getSuppliers().stream().filter(s -> s.getSupplier_id() != null && s.getSupplier_id() > 0)
                    .map(supplierTO -> new SelectItem(supplierTO.getSupplier_id())).toArray(SelectItem[]::new));
        }

        //product category custom fields
//        EdsItemCustomFields itemCustomFields = createProductCustomFields(product.getCategoryCustomFieldItems());
//        item.setItemCustomFields(itemCustomFields);

        //product custom fields
//        EdsItemCustomFields customFields = createProductCustomFields(product.getProductCustomFieldItems());
//        newProduct.setProductCustomFieldItems(customFields);

        if (productTO.getProduct_parent() != null) {
            newProduct.setParentId(productTO.getProduct_parent().getParent_id());
        }

        if (StringUtils.isNotBlank(productTO.getSubsidiary_product_unique_id())) {
            newProduct.setSubsidiaryProductUniqueID(productTO.getSubsidiary_product_unique_id());
        } else {
            newProduct.setItemNameID(productTO.getSubsidiary_product_id());
        }
        if (productTO.getAssembly_items() != null) {

            ArrayList<AssemblyItem> supplierItems = productTO.getAssembly_items().stream()
                    .map(assemblyItemTO -> {
                        AssemblyItem assemblyItem = new AssemblyItem();
                        assemblyItem.setAssemblyItemId(assemblyItemTO.getAssembly_item_id());
                        if (assemblyItemTO.getProduct() != null) {
                            assemblyItem.setProduct(new SelectItem(assemblyItemTO.getProduct().getId(),
                                    assemblyItemTO.getProduct().getName()));
                        }
                        assemblyItem.setDescription(assemblyItemTO.getDescription());
                        assemblyItem.setQuantity(assemblyItemTO.getQuantity());
                        assemblyItem.setCostPrice(assemblyItemTO.getCost_price());
                        assemblyItem.setTotal(assemblyItemTO.getTotal());
                        assemblyItem.setProductType(assemblyItemTO.getProduct_type());
                        return assemblyItem;
                    }).collect(Collectors.toCollection(ArrayList::new));

            newProduct.setAssemblyItems(supplierItems);
        }
        if (productTO.getCurrency() != null) {
            newProduct.setCurrencyId(productTO.getCurrency().getId());
            newProduct.setExchangeRate(productTO.getCurrency().getExchange_rate());
        }
        /*if (productTO.getBarcode()) {
            item.setBarcodeFile((EdsUpload) uploadManager.get(product.getBarcodeID()));
            item.setBarcodeChecksum(product.getBarcodeChecksum());
        }*/
        if (productTO.getDefault_warehouse() != null && productTO.getDefault_warehouse().getWarehouse_id() != null) {
            newProduct.setDefaultItemWarehouse(new SelectItem(productTO.getDefault_warehouse().getWarehouse_id()));
        }
        return newProduct;
    }

    private NewProduct convertProduct(ProductListItemTO productTO) {
        NewProduct newProduct = new NewProduct();
        //EdsCompany company = itemManager.getUser().getCompany();
        EdsItem item = itemManager.getItemByName(productTO.getName());
        if (item != null) {
            newProduct = productServiceLocal.getProduct(item.getObjectID());
        }
        if (newProduct.getType() == null) {
            newProduct.setType(INVENTORY_ITEM);
        }
        newProduct.setItemName(productTO.getName());
        newProduct.setDescription(productTO.getDescription());

        if (productTO.getCategory() != null && productTO.getCategory().getCategory_id() != null) {
            newProduct.setCategoryID(productTO.getCategory().getCategory_id());
        }

        newProduct.setInternalSKUNumber(productTO.getSku_number());
        newProduct.setPartNumber(productTO.getPart_number());
        newProduct.setBarCodeText(productTO.getBarcode());
        newProduct.setActive(Boolean.TRUE);
        newProduct.setAsOf(new DateNonConvertable());
        newProduct.setEnableIT(true);
        if (productTO.getQuantity() != null) {
            newProduct.setQuantity(productTO.getQuantity());
        } else {
            newProduct.setQuantity(BigDecimal.ZERO);
        }

        EdsAccount assetAccount = accountingManager.getAccountByName("Stock");
        if (assetAccount != null) {
            newProduct.setAssetAccountID(assetAccount.getObjectID());
        }

        EdsAccount costOfSales = accountingManager.getAccountTypeWithMinCode(EdsAccountType.COST_OF_SALES);
        if (costOfSales != null) {
            newProduct.setCogsAccountID(costOfSales.getObjectID());
        }
        AccountItem accountItem = invoiceServiceLocal.getDefaultAccountItem(null, RECEIVABLE);
        if (accountItem != null) {
            newProduct.setAccountId(accountItem.getId());
            newProduct.setAccountItem(accountItem);
        }

        if (StringUtils.isNotBlank(productTO.getVendor())) {
            try {
                EdsCrmAccount edsCrmAccount = crmAccountManager.getCrmAccountByName(productTO.getVendor());
                if (edsCrmAccount != null) {
                    newProduct.setSuppliers(new SelectItem[]{edsCrmAccount.getAsSelectItem()});
                } else {
                    CrmAccountItem account = new CrmAccountItem();
                    account.setName(productTO.getVendor());
                    ArrayList<SelectItem> accountTypes = new ArrayList<>();
                    EdsReference customerType = referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.SUPPLIER);
                    if (customerType != null) {
                        accountTypes.add(customerType.getAsSelectItem());
                    }
                    account.setAccountTypes(accountTypes.toArray(new SelectItem[0]));
                    //Save Company with Contact
                    Integer savedAccountID = crmServiceLocal.saveAccount(account, CrmAccountItem.CUSTOMER, null,
                            false, false, true, true, false);
                    if (savedAccountID != null && savedAccountID > 0) {
                        newProduct.setSuppliers(new SelectItem[]{new SelectItem(savedAccountID, account.getName())});
                    }
                }
            } catch (Exception e) {
                log.error("", e);
            }
//            newProduct.setVendorItem();
        }
        /*if (item.getObjectID() != null) {
            for (EdsItem child : itemManager.getChildProducts(item.getObjectID())) {
                activateProduct(child.getObjectID(), item.isActive());
            }
        }*/

        newProduct.setUpcNumber(productTO.getNumber());
        newProduct.setSellingPrice(productTO.getUnit_price());
        if (productTO.getCost_price() != null) {
            newProduct.setUnitPrice(productTO.getCost_price());
        } else {
            newProduct.setUnitPrice(BigDecimal.ZERO);
        }
        newProduct.setTotalValue(newProduct.getQuantity().multiply(newProduct.getUnitPrice()));

        if (StringUtils.isNotBlank(productTO.getCategories())) {
            newProduct.setCategoryName(productTO.getCategories());
            EdsProductCategory category = productCategoryManager.getCategoryByName(productTO.getCategories());
            if (category != null) {
                newProduct.setCategoryID(category.getObjectID());
            } else {
                ProductCategoryItem productCategoryItem = new ProductCategoryItem();
                productCategoryItem.setName(productTO.getCategories());
                Integer catId = accountingServiceLocal.saveProductCategory(productCategoryItem);
                newProduct.setCategoryID(catId);
            }
        }

        EdsWarehouse defaultWarehouse = warehouseManager.getDefaultWarehouse();
        if (defaultWarehouse != null) {
            newProduct.setWarehouse(defaultWarehouse.getAsSelectItem());
            List<ProductLocationItem> locationItems = new ArrayList<>();
            ProductLocationItem locationItem = new ProductLocationItem();
            locationItem.setQty(productTO.getQuantity());
            locationItem.setMinReorderPoint(BigDecimal.ONE);
            locationItem.setMinReorderQty(BigDecimal.ONE);
            locationItems.add(locationItem);

            newProduct.setProductLocations(locationItems.toArray(new ProductLocationItem[]{}));
        }

        /*SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);


        if (productTO.getSuppliers() != null && productTO.getSuppliers().size() > 0) {
            List<SelectItem> supplierItems = productTO.getSuppliers().stream().filter(s -> s.getSupplier_id()!=null && s.getSupplier_id()>0)
                    .map(supplierTO -> new SelectItem(supplierTO.getSupplier_id())).collect(Collectors.toList());
            newProduct.setSuppliers( supplierItems.toArray(new SelectItem[0]));
        }*/

        //product category custom fields
//        EdsItemCustomFields itemCustomFields = createProductCustomFields(product.getCategoryCustomFieldItems());
//        item.setItemCustomFields(itemCustomFields);

        //product custom fields
//        EdsItemCustomFields customFields = createProductCustomFields(product.getProductCustomFieldItems());
//        newProduct.setProductCustomFieldItems(customFields);

        return newProduct;
    }

    private NewProduct convertProduct(ZapierProductVariantTO productTO) {
        NewProduct newProduct = new NewProduct();
        //EdsCompany company = itemManager.getUser().getCompany();
        EdsItem item = itemManager.getItemByName(productTO.getName());
        if (item != null) {
            newProduct = productServiceLocal.getProduct(item.getObjectID());
        }
        if (newProduct.getType() == null) {
            newProduct.setType(INVENTORY_ITEM);
        }
        newProduct.setItemName(productTO.getName());
        newProduct.setInternalSKUNumber(productTO.getSku_number());
        newProduct.setActive(Boolean.TRUE);
        newProduct.setSellingPrice(productTO.getUnit_price());
        newProduct.setUnitPrice(new BigDecimal("0"));
        //TODO unit price 0 bulgani uchun total ham nol buladi, sababi TotalValue=UnitPrice*QTY
        newProduct.setTotalValue(BigDecimal.ZERO);
        newProduct.setParentId(productTO.getParent_id());

        //product category custom fields
//        EdsItemCustomFields itemCustomFields = createProductCustomFields(product.getCategoryCustomFieldItems());
//        item.setItemCustomFields(itemCustomFields);
        //product custom fields
//        EdsItemCustomFields customFields = createProductCustomFields(product.getProductCustomFieldItems());
//        newProduct.setProductCustomFieldItems(customFields);
        return newProduct;
    }

    @Operation(summary = "Get Product Details", description = "Retrieves list of product details based on provided product_id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of product details."),
            @ApiResponse(responseCode = "400", description = "product_id is required"),
            @ApiResponse(responseCode = "404", description = "Product with provided product_id is not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    public Object getProduct(@PathVariable(value = "id") Integer id) throws RestException {
        if (id == null || id == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsItem edsItem = itemManager.get(id);
        if (edsItem == null || edsItem.getDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Product with id " + id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        NewProduct item;
        try {
            item = productServiceLocal.getProduct(id);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (item.getObjectId() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Product with id " + id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        ProductItemTO product = getProduct(item);

        return successResponse(product);
    }

    @Operation(summary = "Get Product Details", description = "Retrieves list of product details based on provided product_number")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of product details."),
            @ApiResponse(responseCode = "400", description = "product_number is required"),
            @ApiResponse(responseCode = "404", description = "Product with provided product_number is not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public Object getProductByNumber(@RequestParam(value = "number") String number) throws RestException {
        if (number == null || number.isEmpty()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Number is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsItem edsItem = itemManager.getItemByNumber(number);
        if (edsItem == null || edsItem.getDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Product with number " + number + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        NewProduct item;
        try {
            item = productServiceLocal.getProduct(edsItem.getObjectID());
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (item.getObjectId() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Product with number " + number + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        ProductItemTO product = getProduct(item);

        return successResponse(product);
    }

    private ProductItemTO getProduct(NewProduct item) {
        ProductItemTO product = new ProductItemTO();
        product.setId(item.getObjectId());
        product.setName(item.getItemName());
        product.setDescription(item.getDescription());
        if (item.getNumberData() != null) {
            product.setNumber(item.getNumberData().getNumberString());
        }
        if (item.getType() != null) {
            product.setProduct_type(new ProductTypeTO(item.getType(), item.getTypeName()));
        }
        if (INVENTORY_ITEM.equals(item.getType()) || AccountingConstants.ASSEMBLY_ITEM.equals(item.getType())
                || AccountingConstants.PRODUCT_KIT.equals(item.getType())) {
            product.setUnit_price(item.getUnitPrice());
            product.setCost_price(item.getSellingPrice());
        } else if (AccountingConstants.NON_INVENTORY_ITEM.equals(item.getType()) || AccountingConstants.SERVICE.equals(item.getType())
                || AccountingConstants.OTHER_CHARGE.equals(item.getType())) {
            product.setRate(item.getUnitPrice());
        }
        if (item.getCogsAccount() != null) {
            product.setCogs_account(new CogsAccountTO(item.getCogsAccount().getId(), item.getCogsAccount().getName()));
        }
        if (item.getAccountItem() != null) {
            product.setIncome_account(new IncomeAccountTO(item.getAccountItem().getId(), item.getAccountItem().getName()));
        }
        if (item.getCategoryID() != null) {
            EdsProductCategory category = productCategoryManager.get(item.getCategoryID());
            if (category != null) {
                product.setCategory(new ProductCategoryTO(category.getObjectID(), category.getName()));
                if (category.getParent() != null) {
                    product.setCategory_parent(new ProductCategoryTO(category.getParent().getObjectID(), category.getParent().getName()));
                }
            }
        }
        if (item.getBrandID() != null) {
            product.setBrand(new ProductBrandTO(item.getBrandID(), item.getBrandName()));
        }
        if (item.getTaxItem() != null) {
            product.setTax(new TaxTO(item.getTaxItem().getId(), item.getTaxItem().getName()));
        }
        if (item.getDiscountItems() != null) {
            ArrayList<DiscountTO> discountList = new ArrayList<>();
            for (DiscountItem discountItem : item.getDiscountItems()) {
                discountList.add(new DiscountTO(discountItem.getId(), discountItem.getName()));
            }
            product.setDiscount_list(discountList);
        }

        product.setActive(item.isActive());
        if (item.getTaxItem() != null) {
            product.setTax(new TaxTO(item.getTaxItem().getId(), item.getTaxItem().getName()));
        }
        product.setOrder(item.getOrder());
        product.setCommission(item.getComission());
        product.setManufacturer(item.getManufacturer());
        product.setUpc_number(item.getUpcNumber());
        product.setSku_number(item.getInternalSKUNumber());
        product.setBarcode(item.getBarCodeText());
        if (item.getParentId() != null) {
            product.setProduct_parent(new ProductParentTO(item.getParentId(), item.getParentName()));
        }
        if (item.getUnitMeasurement() != null) {
            product.setUnit_measurement(new UnitMeasurementTO(item.getUnitMeasurement().getId(), item.getUnitMeasurement().getName()));
        }
        product.setWeight_per_unit(item.getWeightPerUnit());
        if (INVENTORY_ITEM.equals(item.getType())) {
            ArrayList<InventoryStockItemTO> inventoryStockItems = new ArrayList<>();
            for (ProductLocationItem productLocationItem : item.getProductLocations()) {
                InventoryStockItemTO inventoryStockItem = new InventoryStockItemTO();
                inventoryStockItem.setWarehouse(new WarehouseTO(productLocationItem.getWarehouseID(), productLocationItem.getWarehouseName()));
                inventoryStockItem.setQuantity_on_hand(productLocationItem.getQty());
                inventoryStockItem.setReorder_point(productLocationItem.getMinReorderPoint());
                inventoryStockItems.add(inventoryStockItem);
            }
            product.setInventory_stock_item_list(inventoryStockItems);
        } else if (AccountingConstants.ASSEMBLY_ITEM.equals(item.getType())) {
            if (item.getAssemblyItems() != null && !item.getAssemblyItems().isEmpty()) {
                List<AssemblyItemTO> assemblyItems = item.getAssemblyItems().stream()
                        .map(i -> {
                            AssemblyItemTO assemblyItemTO = new AssemblyItemTO();
                            assemblyItemTO.setAssembly_item_id(i.getAssemblyItemId());
                            if (i.getProduct() != null) {
                                assemblyItemTO.setProduct(new IdNameTO(i.getProduct().getId(),
                                        i.getProduct().getName()));
                            }
                            assemblyItemTO.setDescription(i.getDescription());
                            assemblyItemTO.setQuantity(i.getQuantity());
                            assemblyItemTO.setCost_price(i.getCostPrice());
                            assemblyItemTO.setTotal(i.getTotal());
                            assemblyItemTO.setProduct_type(i.getProductType());
                            return assemblyItemTO;
                        }).collect(Collectors.toList());
                product.setAssembly_items((ArrayList) assemblyItems);
            }
        } else if (AccountingConstants.PRODUCT_KIT.equals(item.getType())) {
            if (item.getProductKitItems() != null && item.getProductKitItems().length > 0) {
                ArrayList<ProductKitItemTO> productKitItems = new ArrayList<>();

                for (ProductKitItem productKit : item.getProductKitItems()) {
                    ProductKitItemTO productKitItemTO = new ProductKitItemTO();
                    productKitItemTO.setProduct_kit_item_id(productKit.getProductKitID());
                    if (productKit.getProductItem() != null) {
                        productKitItemTO.setProduct(new IdNameTO(productKit.getProductItem().getId(),
                                productKit.getProductItem().getName()));
                    }
                    productKitItemTO.setDescription(productKit.getDescription());
                    productKitItemTO.setQuantity(productKit.getQuantity());
                    if (productKit.getCost() != null && !productKit.getCost().isEmpty()) {
                        productKitItemTO.setCost_price(new BigDecimal(productKit.getCost()));
                    }
                    if (productKit.getPrice() != null && !productKit.getPrice().isEmpty()) {
                        productKitItemTO.setSell_price(new BigDecimal(productKit.getPrice()));
                    }
                    if (productKit.getSubtotal() != null && !productKit.getSubtotal().isEmpty()) {
                        productKitItemTO.setSub_total(new BigDecimal(productKit.getSubtotal()));
                    }
                    productKitItems.add(productKitItemTO);
                }

                product.setProduct_kit_items(productKitItems);
            }
        }
        if (item.getCategoryCustomFieldItems() != null && !item.getCategoryCustomFieldItems().isEmpty()) {
            List<CustomFieldDto> cfs = new ArrayList<>();
            item.getCategoryCustomFieldItems().forEach(m -> {
                CustomFieldDto cf = CustomFieldsUtils.getCustomFieldDto(m);
                if (cf.getValue() != null) {
                    cfs.add(cf);
                }
            });
            product.setCategoryCustomFields(cfs);
        }

        return product;
    }

    @Operation(summary = "Delete Product", description = "Deletes a product based on provided product_id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with related error code and message."),
            @ApiResponse(responseCode = "400", description = "product_id is required"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/product/{id}", method = RequestMethod.DELETE)
    public Object deleteProduct(@PathVariable(value = "id") Integer id) throws RestException {
        if (id == null || id == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Product id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        boolean isDeleted;
        try {
            isDeleted = productServiceLocal.deleteProduct(id);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (isDeleted) {
            return successResponse(new ResponseData());
        } else {
            throw new RestException("You can not delete the product which have been already used in the system", "You can not delete the product which have been already used in the system", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }
    }

    @Operation(summary = "Get Product Categories", description = "Retrieves list of product categories")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of product categories"),
            @ApiResponse(responseCode = "401", description = "Session expired")})
    @RequestMapping(value = "/product/categories", method = RequestMethod.GET)
    public Object getProductCategories() throws RestException {

        EdsUser user = userManager.getUser();
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setObjectId(user.getObjectID());

        ListResult<ProductCategoryItem> productCategoryItemListResult;
        try {
            productCategoryItemListResult = accountingServiceLocal.getProductCategoriesList(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ArrayList<ProductCategoryTO> productCategories = new ArrayList<>();
        if (productCategoryItemListResult != null) {
            if (productCategoryItemListResult.getList() != null && productCategoryItemListResult.getList().size() > 0) {
                productCategoryItemListResult.getList().forEach(productCategoryItem -> {
                    ProductCategoryTO productCategory = new ProductCategoryTO();
                    productCategory.setCategory_id(productCategoryItem.getId());
                    productCategory.setCategory_name(productCategoryItem.getName());
                    productCategory.setParent_id(productCategoryItem.getParentCategoryID());
                    productCategories.add(productCategory);
                });
            }
        }
        return successResponse(new ProductCategoryListResultTO(productCategories));
    }

    @Operation(summary = "Get Product Categories", description = "Retrieves list of product categories")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of product categories"),
            @ApiResponse(responseCode = "401", description = "Session expired")})
    @RequestMapping(value = "/product/categories/version2", method = RequestMethod.GET)
    public Object getProductCategoriesForMobile(@RequestParam(required = false) String searchKey) throws RestException {

        EdsUser user = userManager.getUser();
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setObjectId(user.getObjectID());
        if (searchKey != null) {
            filterParameter.setSearchKey(searchKey);
        }
        ListResult<ProductCategoryItem> productCategoryItemListResult;
        try {
            productCategoryItemListResult = accountingServiceLocal.getProductCategoriesList(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        EntityCategoryTO productCategories = new EntityCategoryTO();
        if (productCategoryItemListResult != null) {
            if (productCategoryItemListResult.getList() != null && productCategoryItemListResult.getList().size() > 0) {
                List<CategoryTO> categories = new ArrayList<>();
                productCategoryItemListResult.getList().forEach(productCategoryItem -> {
                    CategoryTO category = new CategoryTO();
                    category.setId(productCategoryItem.getId());
                    category.setTitle(productCategoryItem.getName());
                    categories.add(category);
                });
                productCategories.setList(categories);
                productCategories.setTotal_count(categories.size());
            }
        }
        return successResponse(productCategories);
    }


    @Operation(summary = "Get Product Brands", description = "Retrieves list of product brands")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of product brands"),
            @ApiResponse(responseCode = "401", description = "Session expired")})
    @RequestMapping(value = "/product/brands", method = RequestMethod.GET)
    public Object getProductBrands() throws RestException {

        List<EdsBrand> brandList;
        try {
            brandList = brandManager.getBrandList(new ListingFilterParameter());
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ArrayList<ProductBrandTO> productBrands = new ArrayList<>();

        if (brandList != null && brandList.size() > 0) {
            brandList.forEach(edsBrand -> {
                ProductBrandTO productBrandTO = new ProductBrandTO();
                productBrandTO.setBrand_id(edsBrand.getObjectID());
                productBrandTO.setBrand_name(edsBrand.getName());
                productBrandTO.setBrand_description(edsBrand.getDescription());
                productBrandTO.setImage_url(uploadManager.getUploadFileUrl(edsBrand.getImageID()));
                if (edsBrand.getParentBrand() != null) {
                    productBrandTO.setParent_id(edsBrand.getParentBrand().getObjectID());
                }
                productBrands.add(productBrandTO);
            });
        }

        return successResponse(new ProductBrandListResultTO(productBrands));
    }

    @Operation(summary = "Get Product Brands", description = "Retrieves list of product brands")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of product brands"),
            @ApiResponse(responseCode = "401", description = "Session expired")})
    @RequestMapping(value = "/product/brands/version2", method = RequestMethod.GET)
    public Object getProductBrandsForMobile(@RequestParam(required = false) String searchKey) throws RestException {

        List<EdsBrand> brandList;
        ListingFilterParameter fp = new ListingFilterParameter();
        if (searchKey != null) {
            fp.setSearchKey(searchKey);
        }
        try {
            brandList = brandManager.getBrandList(fp);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        EntityCategoryTO entityCategories = new EntityCategoryTO();
        ArrayList<CategoryTO> productBrands = new ArrayList<>();

        if (brandList != null && brandList.size() > 0) {
            brandList.forEach(edsBrand -> {
                CategoryTO category = new CategoryTO();
                category.setId(edsBrand.getObjectID());
                category.setTitle(edsBrand.getName());
                productBrands.add(category);
            });
            entityCategories.setTotal_count(brandList.size());
            entityCategories.setList(productBrands);
        }

        return successResponse(entityCategories);
    }


}
