package com.edatasite.workforce.rest.v3.release10.accounting.service;

import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.accounting.EdsProductPicture;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductCategoryItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductLocationItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountService;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelPPItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.server.app.ProductServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrProductServiceRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaTemplate;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductPictureManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.ConvertUtils;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.InventoryStockInformationDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.ProductDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.ProductLocationDto;
import com.edatasite.workforce.rest.v3.release10.accounting.enums.ProductTypeEnum;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApiProductService implements Constants {
    private static final Logger log = LoggerFactory.getLogger(ApiProductService.class);

    private final ItemManager itemManager;
    private final AccountingManager accountingManager;
    private final ProductService productService;
    private final DiscountService discountService;
    private final CrmAccountManager crmAccountManager;
    private final CommonService commonService;
    private final ProductServiceLocal productServiceLocal;
    private final PriceLevelService priceLevelService;
    private final ProductPictureManager productPictureManager;
    private final UploadManager uploadManager;
    private final AccountingService accountingService;

    @Autowired
    public ApiProductService(ItemManager itemManager,
                             AccountingManager accountingManager,
                             ProductService productService,
                             DiscountService discountService,
                             CrmAccountManager crmAccountManager,
                             CommonService commonService,
                             ProductServiceLocal productServiceLocal,
                             PriceLevelService priceLevelService,
                             ProductPictureManager productPictureManager,
                             UploadManager uploadManager,
                             AccountingService accountingService) {
        this.accountingManager = accountingManager;
        this.itemManager = itemManager;
        this.productService = productService;
        this.discountService = discountService;
        this.crmAccountManager = crmAccountManager;
        this.commonService = commonService;
        this.productServiceLocal = productServiceLocal;
        this.priceLevelService = priceLevelService;
        this.productPictureManager = productPictureManager;
        this.uploadManager = uploadManager;
        this.accountingService = accountingService;
    }

    @Transactional
    public void save(final ProductDto productDto, boolean isNew) throws RestException {
        EdsItem edsItem = isNew ? null : validateForExistence(productDto);
        NewProduct product = productService.getProductEditData(edsItem != null ? edsItem.getObjectID() : null, false);

        Integer intNumber = null;
        String dateLongFormat = String.valueOf(System.currentTimeMillis());
        if (isNew && StringUtils.isNotBlank(productDto.getNumber()) && accountingManager.isProductNumberExists(productDto.getNumber(), productDto.getId())) {
            String number = productDto.getNumber() + "-" + dateLongFormat;
            productDto.setNumber(number);
            intNumber = accountingManager.getProductLastIntNumber() + 1;
            //   throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Product with the given number already exists", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isNotBlank(productDto.getUpcNumber()) && accountingManager.isProductUpcNumberExists(productDto.getUpcNumber(), productDto.getId())) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Product with the given upc number already exists", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        product.setObjectKey(productDto.getObjectKey());
        product.setItemName(productDto.getName());
        Integer finalIntNumber = intNumber;
        Optional.ofNullable(productDto.getNumber()).ifPresent(number -> product.setNumberData(new NumberData(number, finalIntNumber)));
        product.setType(ProductTypeEnum.valueOf(productDto.getType()).getId());
        product.setDescription(productDto.getDescription());
        product.setActive(productDto.isActive());

        product.setSellingPrice(productDto.getSellingPrice());
        product.setUnitPrice(productDto.getPurchasePrice());
        product.setSoldToCustomer(productDto.isSoldToCustomer());

        if (!productDto.getSalesAccount().idIsNull()) {
            product.setAccountId(productDto.getSalesAccount().getId());
        } else if (!productDto.getSalesAccount().codeIsBlank()) {
            Optional.ofNullable(accountingManager.getAccountByCode(productDto.getSalesAccount().getCode())).ifPresent(account -> product.setAccountId(account.getObjectID()));
        }

        if (productDto.getPurchaseAccount() != null) {
            if (!productDto.getPurchaseAccount().idIsNull()) {
                product.setCogsAccountID(productDto.getPurchaseAccount().getId());
            } else if (!productDto.getPurchaseAccount().codeIsBlank()) {
                Optional.ofNullable(accountingManager.getAccountByCode(productDto.getPurchaseAccount().getCode())).ifPresent(account -> product.setCogsAccountID(account.getObjectID()));
            }
        }
        product.setPurchasedFromSupplier(productDto.isPurchasedFromSupplier());
        IdCode categoryDto = productDto.getCategory();
        if (categoryDto != null) {
            if (categoryDto.getId() != null) {
                ProductCategoryItem productCategory = accountingService.getProductCategory(categoryDto.getId());
                product.setCategoryID(productCategory.getId());
                product.setCategoryName(productCategory.getName());
            } else {
                SelectItem category = Arrays.stream(product.getProductCategories())
                        .filter(x -> x.getId().equals(categoryDto.getId()) || x.getName().equals(categoryDto.getCode()))
                        .findAny()
                        .orElse(new SelectItem());
                product.setCategoryID(category.getId());
                product.setCategoryName(category.getName());
            }
        }

        if (productDto.getDiscounts() != null && !productDto.getDiscounts().isEmpty()) {
            List<DiscountItem> discountItems = new ArrayList<>();
            productDto.getDiscounts().forEach(discount -> {
                if (!discount.idIsNull()) {
                    DiscountItem discountItem = discountService.getDiscountData(discount.getId());
                    discountItems.add(discountItem);
                } else if (!discount.codeIsBlank()) {
                    ListingFilterParameter filterParameter = new ListingFilterParameter();
                    filterParameter.setSearchKey(discount.getCode());
                    List<DiscountItem> discountList = discountService.getDiscountList(filterParameter).getList();
                    if (!discountList.isEmpty()) {
                        discountItems.add(discountList.get(0));
                    }
                }
            });
            product.setDiscountItems(discountItems.toArray(new DiscountItem[]{}));
        }

        if (productDto.getBrand() != null) {
            SelectItem brand = Arrays.stream(product.getBrands()).filter(x -> x.getId().equals(productDto.getBrand().getId()) || x.getName().equals(productDto.getBrand().getCode())).findAny().orElse(new SelectItem());
            product.setBrandID(brand.getId());
            product.setBrandName(brand.getName());
        }
        product.setVatId(productDto.getTaxId());
        product.setInternalSKUNumber(productDto.getSkuNumber());
        if (productDto.getUnitMeasurement() != null) {
            SelectItem unitMeasurement = Arrays.stream(product.getUnitMeasurements()).filter(x -> x.getId().equals(productDto.getUnitMeasurement().getId()) || x.getName().equals(productDto.getUnitMeasurement().getCode())).findAny().orElse(new SelectItem());
            product.setUnitMeasurementID(unitMeasurement.getId());
        }
        product.setPartNumber(productDto.getPartNumber());
        product.setUpcNumber(productDto.getUpcNumber());
        product.setManufacturer(productDto.getManufacturer());
        if (productDto.getSuppliers() != null && !productDto.getSuppliers().isEmpty()) {
            List<SelectItem> suppliers = new ArrayList<>();
            productDto.getSuppliers().forEach(supplier -> {
                if (!supplier.idIsNull()) {
                    EdsCrmAccount crmAccount = crmAccountManager.get(supplier.getId());
                    if (crmAccount != null) {
                        suppliers.add(crmAccount.getAsSelectItem());
                    }
                } else if (!supplier.codeIsBlank()) {
                    EdsCrmAccount crmAccount = crmAccountManager.getCrmAccountByName(supplier.getCode());
                    if (crmAccount != null) {
                        suppliers.add(crmAccount.getAsSelectItem());
                    }
                }
            });
            product.setSuppliers(suppliers.toArray(new SelectItem[]{}));
        }

        if (ProductTypeEnum.INVENTORY_ITEM.name().equals(productDto.getType())) {
            if (productDto.getPurchasePrice() == null) {
                throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "purchasePrice cannot be null when type equals to INVENTORY_ITEM", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
            }

            InventoryStockInformationDto inventoryStockInformationDto = Optional.ofNullable(productDto.getInventoryStockInformation()).orElseThrow(() -> new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "inventoryStockInformation cannot be null when type equals to INVENTORY_ITEM", ApiConstants.INVALID, HttpStatus.BAD_REQUEST));
            if (!inventoryStockInformationDto.getAssetAccount().idIsNull()) {
                Optional.ofNullable(accountingManager.get(inventoryStockInformationDto.getAssetAccount().getId())).ifPresent(account -> {
                    product.setAssetAccountID(account.getObjectID());
                    product.setAssetAccount(account.createAccountItem());
                });
            } else if (!inventoryStockInformationDto.getAssetAccount().codeIsBlank()) {
                Optional.ofNullable(accountingManager.getAccountByCode(inventoryStockInformationDto.getAssetAccount().getCode())).ifPresent(account -> {
                    product.setAssetAccountID(account.getObjectID());
                    product.setAssetAccount(account.createAccountItem());
                });
            }
            product.setAsOf(new DateNonConvertable(inventoryStockInformationDto.getAsOf()));
            product.setInventoryTrackingEnabled(inventoryStockInformationDto.isTrackSerialnumber());
            product.setBatchTrackingEnabled(inventoryStockInformationDto.isBatchSerialnumber());
            product.setTrackBatchesEnabled(inventoryStockInformationDto.isTrackBatches());

            List<ProductLocationItem> productLocationItems = new ArrayList<>();
            BigDecimal totalQty = BigDecimal.ZERO;
            for (ProductLocationDto productLocationDto : inventoryStockInformationDto.getProductLocations()) {
                ProductLocationItem productLocationItem = new ProductLocationItem();
                productLocationItem.setMinReorderQty(productLocationDto.getReorderPoint());
                productLocationItem.setQty(productLocationDto.getQtyOnHand());
                if (productLocationDto.getWarehouse() != null) {
                    productLocationItem.setWarehouseID(productLocationDto.getWarehouse().getId());
                    productLocationItem.setWarehouseName(productLocationDto.getWarehouse().getName());
                }
                productLocationItems.add(productLocationItem);
                totalQty = totalQty.add(productLocationDto.getQtyOnHand());
            }
            product.setTotalQtyOnHand(totalQty);
            product.setQuantity(totalQty);
            product.setTotalValue(totalQty.multiply(productDto.getPurchasePrice()));
            product.setProductLocations(productLocationItems.toArray(new ProductLocationItem[]{}));
        }

        /*if (!isNew) {
            edsItem = itemManager.get(productDto.getId());
        }*/
        product.setProductCustomFieldItems(CustomFieldsUtils.convertCustomFields(productDto.getCustomFields(), commonService.getCompanyCustomFields(ViewName.ProductServiceView), !isNew ? edsItem.getCustomFields() : null));

        productService.saveProduct(product);

        productDto.setId(product.getObjectId());
        productDto.setNumber(product.getNumberData().getNumberString());
        productDto.setCreatedAt(product.getCreatedDate());
        productDto.setUpdatedAt(product.getLastUpdateTime());

    }

    @Transactional
    public ProductDto savePatch(final ProductDto productDto) throws RestException {
        EdsItem edsItem = validateForExistence(productDto);
        NewProduct product = productService.getProductEditData(edsItem.getObjectID(), false);

        Integer intNumber = null;
        if (StringUtils.isNotBlank(productDto.getNumber()) && accountingManager.isProductNumberExists(productDto.getNumber(), edsItem.getObjectID())) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Product with the given number already exists", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isNotBlank(productDto.getUpcNumber()) && accountingManager.isProductUpcNumberExists(productDto.getUpcNumber(), edsItem.getObjectID())) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Product with the given upc number already exists", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        Optional.ofNullable(productDto.getName()).ifPresent(product::setItemName);
        Integer finalIntNumber = intNumber;
        Optional.ofNullable(productDto.getNumber()).ifPresent(number -> product.setNumberData(new NumberData(number, finalIntNumber)));
        Optional.ofNullable(productDto.getType()).ifPresent(type -> product.setType(ProductTypeEnum.valueOf(type).getId()));
        Optional.ofNullable(productDto.getDescription()).ifPresent(product::setDescription);
        Optional.ofNullable(productDto.getSellingPrice()).ifPresent(product::setSellingPrice);
        Optional.ofNullable(productDto.getPurchasePrice()).ifPresent(product::setUnitPrice);

        Optional.ofNullable(productDto.getSalesAccount()).ifPresent(salesAccount -> {
            if (!salesAccount.idIsNull()) {
                product.setAccountId(salesAccount.getId());
            } else if (!salesAccount.codeIsBlank()) {
                Optional.ofNullable(accountingManager.getAccountByCode(salesAccount.getCode())).ifPresent(account -> product.setAccountId(account.getObjectID()));
            }
        });

        Optional.ofNullable(productDto.getPurchaseAccount()).ifPresent(purchaseAccount -> {
            if (!purchaseAccount.idIsNull()) {
                product.setCogsAccountID(purchaseAccount.getId());
            } else if (!purchaseAccount.codeIsBlank()) {
                Optional.ofNullable(accountingManager.getAccountByCode(purchaseAccount.getCode())).ifPresent(account -> product.setCogsAccountID(account.getObjectID()));
            }
        });
        Optional.ofNullable(productDto.getCategory()).ifPresent(category -> {
            SelectItem selectedCategory = Arrays.stream(product.getProductCategories()).filter(x -> x.getId().equals(category.getId()) || x.getName().equals(category.getCode())).findAny().orElse(new SelectItem());
            product.setCategoryID(selectedCategory.getId());
            product.setCategoryName(selectedCategory.getName());
        });

        if (productDto.getDiscounts() != null && !productDto.getDiscounts().isEmpty()) {
            List<DiscountItem> discountItems = new ArrayList<>();
            productDto.getDiscounts().forEach(discount -> {
                if (!discount.idIsNull()) {
                    DiscountItem discountItem = discountService.getDiscountData(discount.getId());
                    discountItems.add(discountItem);
                } else if (!discount.codeIsBlank()) {
                    ListingFilterParameter filterParameter = new ListingFilterParameter();
                    filterParameter.setSearchKey(discount.getCode());
                    List<DiscountItem> discountList = discountService.getDiscountList(filterParameter).getList();
                    if (!discountList.isEmpty()) {
                        discountItems.add(discountList.get(0));
                    }
                }
            });
            product.setDiscountItems(discountItems.toArray(new DiscountItem[]{}));
        }

        Optional.ofNullable(productDto.getBrand()).ifPresent(brand -> {
            SelectItem selectedBrand = Arrays.stream(product.getBrands()).filter(x -> x.getId().equals(brand.getId()) || x.getName().equals(brand.getCode())).findAny().orElse(new SelectItem());
            product.setBrandID(selectedBrand.getId());
            product.setBrandName(selectedBrand.getName());
        });

        Optional.ofNullable(productDto.getTaxId()).ifPresent(product::setVatId);
        Optional.ofNullable(productDto.getSkuNumber()).ifPresent(product::setInternalSKUNumber);
        Optional.ofNullable(productDto.getUnitMeasurement()).ifPresent(unitMeasurement -> {
            SelectItem selectedUnitMeasurement = Arrays.stream(product.getUnitMeasurements()).filter(x -> x.getId().equals(unitMeasurement.getId()) || x.getName().equals(unitMeasurement.getCode())).findAny().orElse(new SelectItem());
            product.setUnitMeasurementID(selectedUnitMeasurement.getId());
        });
        Optional.ofNullable(productDto.getPartNumber()).ifPresent(product::setPartNumber);
        Optional.ofNullable(productDto.getUpcNumber()).ifPresent(product::setUpcNumber);
        Optional.ofNullable(productDto.getManufacturer()).ifPresent(product::setManufacturer);
        if (productDto.getSuppliers() != null && !productDto.getSuppliers().isEmpty()) {
            List<SelectItem> suppliers = new ArrayList<>();
            productDto.getSuppliers().forEach(supplier -> {
                if (!supplier.idIsNull()) {
                    EdsCrmAccount crmAccount = crmAccountManager.get(supplier.getId());
                    if (crmAccount != null) {
                        suppliers.add(crmAccount.getAsSelectItem());
                    }
                } else if (!supplier.codeIsBlank()) {
                    EdsCrmAccount crmAccount = crmAccountManager.getCrmAccountByName(supplier.getCode());
                    if (crmAccount != null) {
                        suppliers.add(crmAccount.getAsSelectItem());
                    }
                }
            });
            product.setSuppliers(suppliers.toArray(new SelectItem[]{}));
        }

        if (ProductTypeEnum.INVENTORY_ITEM.getId().equals(product.getType())) {

            Optional.ofNullable(productDto.getInventoryStockInformation()).ifPresent(inventoryStockInformationDto -> {
                Optional.ofNullable(inventoryStockInformationDto.getAssetAccount()).ifPresent(assetAccount -> {
                    if (!assetAccount.idIsNull()) {
                        Optional.ofNullable(accountingManager.get(assetAccount.getId())).ifPresent(account -> {
                            product.setAssetAccountID(account.getObjectID());
                            product.setAssetAccount(account.createAccountItem());
                        });
                    } else if (!assetAccount.codeIsBlank()) {
                        Optional.ofNullable(accountingManager.getAccountByCode(assetAccount.getCode())).ifPresent(account -> {
                            product.setAssetAccountID(account.getObjectID());
                            product.setAssetAccount(account.createAccountItem());
                        });
                    }
                });
                Optional.ofNullable(inventoryStockInformationDto.getAsOf()).ifPresent(asOf -> product.setAsOf(new DateNonConvertable(asOf)));
                Optional.ofNullable(inventoryStockInformationDto.getProductLocations()).ifPresent(productLocationDtos -> {
                    List<ProductLocationItem> productLocationItems = new ArrayList<>();
                    BigDecimal totalQty = BigDecimal.ZERO;
                    for (ProductLocationDto productLocationDto : productLocationDtos) {
                        ProductLocationItem productLocationItem = new ProductLocationItem();
                        productLocationItem.setObjectID(productLocationDto.getObjectId());
                        productLocationItem.setMinReorderQty(productLocationDto.getReorderPoint());
                        productLocationItem.setQty(productLocationDto.getQtyOnHand());
                        if (productLocationDto.getWarehouse() != null) {
                            productLocationItem.setWarehouseID(productLocationDto.getWarehouse().getId());
                            productLocationItem.setWarehouseName(productLocationDto.getWarehouse().getName());
                        }
                        productLocationItems.add(productLocationItem);
                        totalQty = totalQty.add(productLocationDto.getQtyOnHand());
                    }
                    product.setTotalQtyOnHand(totalQty);
                    product.setQuantity(totalQty);
                    product.setTotalValue(totalQty.multiply(productDto.getPurchasePrice()));
                    product.setProductLocations(productLocationItems.toArray(new ProductLocationItem[]{}));
                });

            });

        }
        product.setProductCustomFieldItems(CustomFieldsUtils.convertCustomFields(productDto.getCustomFields(), commonService.getCompanyCustomFields(ViewName.ProductServiceView), edsItem.getCustomFields()));

        productService.saveProduct(product);

        productDto.setId(product.getObjectId());
        productDto.setNumber(product.getNumberData().getNumberString());
        productDto.setCreatedAt(product.getCreatedDate());
        productDto.setUpdatedAt(product.getLastUpdateTime());
        return productDto;
    }

    @Transactional(readOnly = true)
    public ProductDto getById(final Integer id) throws RestException {
        Optional.ofNullable(itemManager.get(id)).orElseThrow(() -> new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Product with the given Id not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST));
        NewProduct newProduct = productService.getProductEditData(id, false);
        HashMap<PriceLevelItem, PriceLevelPPItem> priceLevels = priceLevelService.getPriceLevelPPItems(id);
        return ConvertUtils.toDto(newProduct, priceLevels);
    }

    public void deleteById(final Integer id) throws RestException {
        boolean deleted = productService.deleteProduct(id);
        if (!deleted) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Product cannot be deleted because it is used in invoices or quotes", ApiConstants.INVALID, HttpStatus.CONFLICT);
        }
    }

    public ListResultTO<ProductDto> getProductList(ListingFilterParameter filterParameter) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_PRODUCTS_SERVICES_CORE);
        QueryResponse resp = null;
        try {
            resp = server.query(productServiceLocal.getProductsServicesSolrQuery(filterParameter, productServiceLocal.getProductListSolrQuery(filterParameter, Collections.emptyList())), SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }

        ListResultTO<ProductDto> listResultTO = new ListResultTO<>();
        if (resp != null) {
            List<Integer> ids = resp.getResults().stream().map(doc -> SolrUtils.asInteger(doc, SolrProductServiceRepresenter.FIELD_PRODUCT_ID)).collect(Collectors.toList());
            listResultTO.setTotalNumber(ids.size());
            ArrayList<ProductDto> products = new ArrayList<>();
            ids.forEach(id -> {
                NewProduct newProduct = productService.getProductEditData(id, false);
                HashMap<PriceLevelItem, PriceLevelPPItem> priceLevel = priceLevelService.getPriceLevelPPItems(id);
                ProductDto product = ConvertUtils.toDto(newProduct, priceLevel);
                product.setImages(productServiceLocal.getProductPictures(id, 0));
                products.add(product);
                listResultTO.setItems(products);
            });
        }

        return listResultTO;
    }

    EdsItem validateForExistence(ProductDto dto) throws RestException {
        EdsItem edsItem = null;
        if (dto.isExistingObject()) {
            if (StringUtils.isNotBlank(dto.getObjectKey())) {
                edsItem = itemManager.getItemByObjectKey(dto.getObjectKey());
            }
            if (edsItem == null && dto.getId() != null) {
                edsItem = itemManager.getItem(dto.getId());
            }
            if (edsItem == null && StringUtils.isNotBlank(dto.getNumber())) {
                edsItem = itemManager.getItemByNumber(dto.getNumber());
            }

            if (edsItem == null) {
                throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Product with the given Id or Number not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
            }
            if (dto.getType() != null && !edsItem.getType().equals(ProductTypeEnum.valueOf(dto.getType()).getId())) {
                if (!productService.deleteProduct(edsItem.getObjectID())) {
                    throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Please note that you can not update the product type which has been used in invoices and quotes", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
                }
            }
        }
        return edsItem;
    }

    @Transactional
    public List<String> uploadProductPictureToPublicBucket(List<Integer> productIds, String tempLink) throws RestException {
        final int BUFFER_SIZE = 8192;

        if (productIds == null || productIds.isEmpty()) {
            throw new RestException("Product IDs cannot be null or empty.", "Product IDs connot be null or empty.", 400, HttpStatus.BAD_REQUEST);
        }
        if (productIds.size() > 100) {
            throw new RestException("Product IDs cannot be more than 100.", "Product IDs connot be more than 100.", 400, HttpStatus.BAD_REQUEST);
        }

        var edsItems = productIds.stream()
                .map(itemManager::get)
                .toList();
        List<String> uploadedLinks = new ArrayList<>();

        for (EdsItem edsItem : edsItems) {
            var productPictures = productPictureManager.getProductPictures(edsItem, 0);
            if (productPictures == null || productPictures.isEmpty()) {
                log.warn("No product pictures found for product item: {}", edsItem.getObjectID());
                continue;
            }
            for (EdsProductPicture picture : productPictures) {
                String fileUrl = tempLink != null ? tempLink : uploadManager.getFileURL(picture);

                try (InputStream inputStream = new URL(fileUrl).openStream();
                     ByteArrayOutputStream bufferStream = new ByteArrayOutputStream()) {

                    byte[] buffer = new byte[BUFFER_SIZE];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        bufferStream.write(buffer, 0, bytesRead);
                    }

                    byte[] fileBytes = bufferStream.toByteArray();
                    if (fileBytes.length == 0) {
                        continue;
                    }

                    String link = uploadManager.putFileToPublicFolder(picture, new ByteArrayInputStream(fileBytes));
                    uploadedLinks.add(link);
                } catch (MalformedURLException e) {
                    log.error("Malformed URL: {}", fileUrl, e);
                } catch (IOException e) {
                    log.error("I/O Error while processing file at URL: {}", fileUrl, e);
                } catch (NoSuchAlgorithmException e) {
                    log.error("Security error while uploading file for picture ID: {}", picture.getObjectID(), e);
                }
            }
        }

        return uploadedLinks;
    }

    public List<ProductDto> getSimpleProductList(ListingFilterParameter fp) {
        ListResult<ProductItem> products = productService.getProductsListFromSolr(fp);
        List<Integer> productIds = products.getList().stream().map(ProductItem::getObjectId).toList();
        ArrayList<CompanyCustomFieldItem> customFieldItems  = commonService.getCompanyCustomFields(ViewName.ProductServiceView);
        return itemManager.getProductsByIds(productIds).stream().map(it -> ConvertUtils.toDto(it, customFieldItems)).toList();

    }

    public ListResultTO<ProductDto> getSimpleProductResultList(ListingFilterParameter fp) {
        var list = productService.getProductsListFromSolr(fp);
        if (list == null && list.getList() == null) return new ListResultTO<>();
        var result = list.getList().stream()
                .map(ConvertUtils::toDto)
                .collect(Collectors.toCollection(ArrayList::new));

        ListResultTO<ProductDto> resultTO = new ListResultTO<>();
        resultTO.setTotalNumber(result.size());
        resultTO.setItems(result);

        return resultTO;
    }

    @Transactional
    public void updateActive(List<Integer> ids, boolean active) {
        productServiceLocal.updateActive(ids, active);
    }
}
