package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsItemCustomFields;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsProductCategory;
import com.edatasite.workforce.core.domain.accounting.EdsProductPicture;
import com.edatasite.workforce.core.domain.accounting.EdsShippingMethod;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.magento.EdsMagentoApiSettings;
import com.edatasite.workforce.core.domain.settings.EdsEmailTemplate;
import com.edatasite.workforce.gwt.accounting.server.app.ProductServiceLocal;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateService;
import com.edatasite.workforce.gwt.core.client.rpc.EntityToEmailTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.view.PdfTemplateItemList;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.EmailTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.MagentoApiSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemStockManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductPictureManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ShippingMethodManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.invoice.client.rpc.MessageItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.SaveResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.server.app.BaseInvoiceService;
import com.edatasite.workforce.gwt.profile.client.rpc.MagentoSettingsItem;
import com.google.code.magja.model.category.Category;
import com.google.code.magja.model.customer.Customer;
import com.google.code.magja.model.customer.CustomerAddress;
import com.google.code.magja.model.media.Media;
import com.google.code.magja.model.order.Order;
import com.google.code.magja.model.order.OrderAddress;
import com.google.code.magja.model.order.OrderItem;
import com.google.code.magja.model.product.Product;
import com.google.code.magja.model.product.ProductAttribute;
import com.google.code.magja.model.product.ProductAttributeSet;
import com.google.code.magja.model.product.ProductMedia;
import com.google.code.magja.model.product.ProductType;
import com.google.code.magja.model.product.Visibility;
import com.google.code.magja.service.RemoteServiceFactory;
import com.google.code.magja.service.ServiceException;
import com.google.code.magja.service.product.ProductRemoteService;
import com.google.code.magja.soap.MagentoSoapClient;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Shohruh on 06 Dec 2016.
 */
@Transactional
@Service("magentoService")
public class MagentoServiceImpl implements MagentoService,Constants {
    private static final Logger log = LoggerFactory.getLogger(MagentoServiceImpl.class);

    @Autowired
    private ItemManager itemManager;
    @Autowired
    private ItemStockManager itemStockManager;
    @Autowired
    private ProductCategoryManager productCategoryManager;
    @Autowired
    private ProductPictureManager productPictureManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private CountryManager countryManager;
    @Autowired
    private MagentoApiSettingsManager magentoApiSettingsManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private ShippingMethodManager shippingMethodManager;
    @Autowired
    private EmailTemplateManager emailTemplateManager;
    @Autowired
    private ProductServiceLocal productServiceLocal;
    @Autowired
    private QuoteService quoteService;
    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private EmailTemplateService emailTemplateService;
    @Autowired
    private BaseInvoiceService baseInvoiceService;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;

    private MagentoSoapClient soapClient;

    private Map<String, HashMap<String, String>> mapper = new HashMap<>();

    public RemoteServiceFactory getServiceFactory() {
        return new RemoteServiceFactory(getSoapClient());
    }

    /**
     * @param itemID
     * @param magentoWebSites
     * @return Map of websites of magento
     * This logic need for parsing native PHP API response data. Response data not match any formats(json, xml,...).
     * Because this code should convert response data to json format.
     */
    private HashMap<Integer, String> getMagentoWebSites(String webSiteApiUrl) {
        HashMap<Integer, String> webSitesMap = new HashMap<>();
        try {
            URL magentoURL = new URL(webSiteApiUrl);
            HttpURLConnection connection = (HttpURLConnection) magentoURL.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer str = new StringBuffer();
            while ((inputLine = in.readLine()) != null)
                str.append(inputLine);
            in.close();
            String response = str.toString();
            response = response.replace("Array", "").replace("<pre>", "");
            response = response.replace("\\[", "").replace("]", "");
            response = "{\"websites\": [" + response;
            response = response.replaceAll(" {2}", " ");
            response = response.replace("=> stdClass Object", "");
            response = response.replace("\\(", "{").replace("\\)", "}");
            response = response.replaceFirst("\\{ {2}0 {5}\\{", "{");
            response = response.replaceAll("} {2}[1-9.] {5}\\{", "}, {");
            response = response.replace("website_id =>", "\"website_id\": ").replace("name =>", ",\"name\": ");
            response = response + "}";
            response = response.replace("}}}", "}]}");
            response = response.trim();

            JSONObject obj = new JSONObject(response);
            JSONArray arr = obj.getJSONArray("websites");
            for (int i = 0; i < arr.length(); i++) {
                String post_id = arr.getJSONObject(i).getString("website_id").trim();
                String post_name = arr.getJSONObject(i).getString("name").trim();
                webSitesMap.put(Integer.valueOf(post_id), post_name);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return webSitesMap;
    }

    private MagentoSoapClient getSoapClient() {
        if (soapClient == null) {
            EdsMagentoApiSettings settings = magentoApiSettingsManager.getSettings();
            if (settings != null) {
                soapClient = new MagentoSoapClient(settings.getSoapConfig());
            }
        }
        return soapClient;
    }

    public MagentoSettingsItem getMagentoSettings() {
        MagentoSettingsItem magentoSettingsItem;
        EdsMagentoApiSettings magentoApiSettings = magentoApiSettingsManager.getSettings();
        if (magentoApiSettings != null) {
            magentoSettingsItem = new MagentoSettingsItem();
            magentoSettingsItem.setApiUrl(magentoApiSettings.getApiUrl());
            magentoSettingsItem.setApiUser(magentoApiSettings.getApiUser());
            magentoSettingsItem.setApiKey(magentoApiSettings.getApiKey());
            EdsUser user = magentoApiSettings.getKpiUser();
            if (user != null) {
                magentoSettingsItem.setUser(user.getAsSelectItem());
            }
            return magentoSettingsItem;
        }
        return null;
    }

    public void saveMagentoSettings(MagentoSettingsItem magentoSettings) {
        EdsMagentoApiSettings magentoApiSettings = magentoApiSettingsManager.getSettings();
        if (magentoApiSettings == null) {
            magentoApiSettings = new EdsMagentoApiSettings();
        }
        magentoApiSettings.setApiUrl(magentoSettings.getApiUrl());
        magentoApiSettings.setApiUser(magentoSettings.getApiUser());
        magentoApiSettings.setApiKey(magentoSettings.getApiKey());
        if (magentoSettings.getUser() != null) {
            magentoApiSettings.setKpiUser(userManager.get(magentoSettings.getUser().getId()));
        } else {
            magentoApiSettings.setKpiUser(null);
        }
        magentoApiSettingsManager.createOrUpdate(magentoApiSettings);
    }

    @Override
    public void synchronizeWithMagentoCatalog() {
        //sync categories
        syncCategories();
        //sync products
        List<String> configuredProducts = syncProducts();
        //associate created child products
        associateProductsToParent(configuredProducts);
        //sync product media
        syncProductPictures();
        //sync stock
        syncInventories();
    }

    @Override
    public void resetMagentoSynchronization() {
        resetProductPictures();

        resetProducts();

        resetCategories();
    }

    /**
     * @param itemID
     * @param magentoWebSites
     * @return Configured product sku which should be associated
     */
    @Transactional
    public String syncProduct(Integer itemID, String syncType, HashMap<Integer, String> magentoWebSites) {
        String cunfiguredProduct = null;
        EdsItem item = itemManager.get(itemID);
        Set<ProductRemoteService.Dependency> dependencies = new HashSet<>();

        Product product = new Product();
        product.setAttributeSet(getAttributeSet(item.getCategory()));
        product.setId(item.getMagentoEntityID());
        product.setSku(item.getProductNumber());
        product.setName(item.getName());
        product.setDescription(item.getDescription() == null || item.getDescription().isEmpty() ? "description" : item.getDescription());
        product.setShortDescription(product.getDescription());
        product.setPrice(item.getSellingPrice().doubleValue());
        product.setWeight(Double.valueOf(item.getWeightPerUnit() == null || item.getWeightPerUnit().isEmpty() ? "0" : item.getWeightPerUnit()));
        product.set("status", item.isActive());
        if (item.getCategory() != null) {
            List<Integer> categories = new ArrayList<>();
            categories.add(item.getCategory().getMagentoEntityID());
            product.set("category_ids", categories);
        }

        if (item.getParent() != null) {
            product.setType(ProductType.SIMPLE);
            product.setVisibility(Visibility.NOT_VISIBLE_INDIVIDUALLY);
            if (item.getCategory() != null) {
                EdsItemCustomFields itemCustomFields = item.getItemCustomFields();
                ArrayList<CompanyCustomFieldItem> customFieldsItems = commonServiceLocal.getCompanyCustomFieldsByRelationship(ViewName.ProductCategory, item.getCategory().getObjectID(), null);
                CustomFieldsUtils.setRPCCustomFieldItems(itemCustomFields, customFieldsItems);
                for (CompanyCustomFieldItem fieldItem : customFieldsItems) {
                    String attributeCode = fieldItem.getAliasName().toLowerCase() + "_" + item.getCategory().getObjectID();
                    String label = fieldItem.getFieldStringValue();
                    try {
                        ProductAttribute productAttribute = getServiceFactory().getProductAttributeRemoteService().getByCode(attributeCode);
                        for (Map.Entry<Integer, String> entry : productAttribute.getOptions().entrySet()) {
                            if (entry.getValue().equals(label)) {
                                product.set(attributeCode, entry.getKey());
                                break;
                            }
                        }
                        cunfiguredProduct = item.getParent().getProductNumber();
                    } catch (ServiceException e) {
                        log.error("Cannot get attribute with code: " + attributeCode + "\n" + e.getMessage());
                    }
                }
            }
        } else {
            if (CONFIGURED.equals(syncType)) {
                product.setType(ProductType.CONFIGURABLE);
                product.setVisibility(Visibility.CATALOG_SEARCH);
            } else {
                product.setType(ProductType.SIMPLE);
                product.setVisibility(Visibility.CATALOG_SEARCH);
            }
        }
        if (magentoWebSites != null && !magentoWebSites.isEmpty()) {
            EdsItemCustomFields itemCustomFields = item.getCustomFields();
            ArrayList<CompanyCustomFieldItem> customFieldItems = commonServiceLocal.getCompanyCustomFields(ViewName.ProductServiceView);
            CustomFieldsUtils.setRPCCustomFieldItems(itemCustomFields, customFieldItems);
            if (customFieldItems != null && !customFieldItems.isEmpty()) {
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ("Website".equals(fieldItem.getFieldName())) {
                        if (fieldItem.getFieldStringValue() != null && !fieldItem.getFieldStringValue().isEmpty()) {
                            ArrayList<Integer> sitesList = new ArrayList<>();
                            String[] sites = fieldItem.getFieldStringValue().split(",");
                            for (Integer webSiteId : magentoWebSites.keySet()) {
                                for (String site : sites) {
                                    if (magentoWebSites.get(webSiteId).equals(site)) {
                                        sitesList.add(webSiteId);
                                    }
                                }
                            }
                            product.setWebsites(sitesList.toArray(new Integer[]{}));
                        } else {
                            product.setWebsites(new Integer[]{});
                        }
                    }
                }
            } else {
                product.setWebsites(new Integer[]{});
            }
        }
        try {
            if (item.getMagentoEntityID() != null) {
                getServiceFactory().getProductRemoteService().update(product, null, "", dependencies);
            } else {
                getServiceFactory().getProductRemoteService().add(product);
                item.setMagentoEntityID(product.getId());
            }
            item.setMagentoSyncDate(new Date());
            itemManager.update(item);
            itemManager.flushAndClear();
        } catch (ServiceException | NoSuchAlgorithmException e) {
            log.error("Cannot sync product with sku: " + product.getSku() + "\n" + (e instanceof ServiceException ? ((ServiceException) e).getCause().getMessage() : e.getMessage()));
            try {
                Product existingProduct = getServiceFactory().getProductRemoteService().getBySku(item.getProductNumber());
                item.setMagentoEntityID(existingProduct.getId());
                item.setMagentoSyncDate(new Date());
                itemManager.update(item);
                itemManager.flushAndClear();
            } catch (Exception e1) {
                log.error("Cannot resync product with sku: " + product.getSku() + "\n" + e.getMessage());
            }
        }
        return cunfiguredProduct;
    }

    public List<String> syncProducts() {
        log.info("Magento Product Sync started");
        String syncType = SIMPLE;//temporary solution for abco

        long startTime = System.currentTimeMillis();
        List<String> configuredProducts = new ArrayList<>();
        List<Integer> items = itemManager.getItemsForSync(syncType);
        HashMap<Integer, String> magentoWebSites = new HashMap<>();
        if (items != null && !items.isEmpty()) {
            EdsUser user = userManager.getUser();
            EdsCompany company = user.getCompany();
            if (user != null && company != null && company.getObjectID() == 60618) { //Essoman Company
                magentoWebSites = getMagentoWebSites("http://essoman.com/tests/api_tester.php");
            }
        }
        mapper.clear();
        int count = 0;
        for (Integer itemId : items) {
            EdsItem item = itemManager.get(itemId);
            EdsItem parent = item.getParent();
            itemManager.flushAndClear();
            if (item.getDeleted() && item.getMagentoEntityID() != null) {
                deleteProduct(item);
                continue;
            }
            if (parent != null && (parent.getMagentoSyncDate() == null || (parent.getLastUpdateTime() != null ? parent.getMagentoSyncDate().before(parent.getLastUpdateTime()) : parent.getMagentoEntityID() == null))) {
                syncProduct(parent.getObjectID(), syncType, magentoWebSites);
            }
            String sku = syncProduct(itemId, syncType, magentoWebSites);
            if (sku != null) {
                configuredProducts.add(sku);
            }
        }
        String interval = System.currentTimeMillis() - startTime + "ms";
        log.info("Magento Product Sync ended. Elapsed time = " + interval);
        return configuredProducts;
    }

    @Override
    @Transactional
    public void resetProducts() {
        log.info("Magento Product Reset started");
        long startTime = System.currentTimeMillis();
        try {
            getServiceFactory().getProductRemoteService().deleteAll();
            itemManager.updateItemsAfterReset();
        } catch (ServiceException e) {
            log.error("Error while removing all products, error: " + e.getMessage());
        }

        String interval = System.currentTimeMillis() - startTime + "ms";
        log.info("Magento Product Reset ended. Elapsed time = " + interval);
    }

    public void deleteProduct(EdsItem item) {
        if (item.getMagentoEntityID() != null) {
            try {
                getServiceFactory().getProductRemoteService().delete(item.getMagentoEntityID());
                Date removedDate = new Date();
                item.setMagentoSyncDate(removedDate);
                item.setLastUpdateTime(removedDate);
                item.setUpdater(userManager.getUser());
                itemManager.merge(item);

                if (item.getParent() != null) {
                    EdsItem parent = item.getParent();
                    if (!parent.getHasVariations()) {
                        deleteProduct(parent);
                        parent.setMagentoEntityID(null);
                        parent.setMagentoSyncDate(null);
                        itemManager.update(parent);

                        List<EdsProductPicture> productPictures = productPictureManager.getProductPictures(parent);
                        for (EdsProductPicture picture : productPictures) {
                            picture.setLastSyncTime(new Date());
                            picture.setMagentoFile(null);
                            productPictureManager.update(picture);
                        }
                    }
                }
                itemManager.flushAndClear();
                productPictureManager.flushAndClear();
            } catch (ServiceException e) {
                log.error("Cannot delete product with id: " + item.getMagentoEntityID() + "\n" + e.getMessage());
            }
        }
    }

    public void associateProductsToParent(List<String> configuredProducts) {
        for (String sku : configuredProducts) {
            List<EdsItem> childs = itemManager.getChildProducts(sku);
            try {
                Product product = getServiceFactory().getProductRemoteService().getBySku(sku);
                List<String> skus = new ArrayList<>();
                for (EdsItem child : childs) {
                    skus.add(child.getProductNumber());
                }
                product.set("associated_skus", skus);
                getServiceFactory().getProductRemoteService().update(product, null);
            } catch (ServiceException | NoSuchAlgorithmException e) {
                log.error("Cannot associate product. Sku: " + sku + "\n" + e.getMessage());
            }
        }
    }

    public void syncProductPicture(EdsProductPicture picture) {
        try {
            EdsItem item = picture.getProduct();
            Product product = new Product(item.getProductNumber() + " ");

            Media image = new Media();
            image.setName(picture.getName());
            image.setData(image2Byte(productServiceLocal.getImageUrl(picture)));
            image.setMime(picture.getContentType());

            ProductMedia productMedia = new ProductMedia();
            productMedia.setLabel(picture.getName());
            productMedia.setProduct(product);
            productMedia.setImage(image);
            productMedia.setFile(picture.getMagentoFile());
            productMedia.setExclude(false);

            Set<ProductMedia.Type> types = new HashSet<>();
            types.add(ProductMedia.Type.IMAGE);
            types.add(ProductMedia.Type.SMALL_IMAGE);
            types.add(ProductMedia.Type.THUMBNAIL);
            productMedia.setTypes(types);

            if (productMedia.getFile() != null) {//if picture has updated, we have to recreate it (in order to default picture logic works correctly)
                getServiceFactory().getProductMediaRemoteService().reCreate(productMedia);
            } else {
                getServiceFactory().getProductMediaRemoteService().create(productMedia);
            }
            picture.setMagentoFile(productMedia.getFile());
            picture.setLastSyncTime(new Date());
            productPictureManager.update(picture);
        } catch (IOException | ServiceException e) {
            log.error("Cannot sync image: " + picture.getName() + "\n" + e.getMessage());
        }
    }

    private byte[] image2Byte(String urlString) throws IOException {
        URL url = new URL(urlString);
        try (InputStream is = url.openStream()) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[1024];

            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            return buffer.toByteArray();
        }
    }

    public void syncProductPictures() {
        log.info("Magento Product Media Sync started");
        long startTime = System.currentTimeMillis();
        List<EdsProductPicture> pictures = productPictureManager.getProductPicturesForSync(FILE_SIZE_ORIGINAL);
        int count = 0;
        for (EdsProductPicture picture : pictures) {
            if (picture.getDeleted()) {
                deleteProductPicture(picture);
            } else {
                syncProductPicture(picture);
            }
        }
        productPictureManager.flushAndClear();
        String interval = System.currentTimeMillis() - startTime + "ms";
        log.info("Magento Product Media Sync ended. Elapsed time = " + interval);
    }

    @Override
    @Transactional
    public void resetProductPictures() {
        log.info("Magento Product Media Reset started");
        long startTime = System.currentTimeMillis();
        List<EdsProductPicture> pictures = productPictureManager.getProductPicturesForReset(FILE_SIZE_ORIGINAL);
        int count = 0;
        for (EdsProductPicture picture : pictures) {
            deleteProductPicture(picture);
        }
        productPictureManager.updateProductPicturesAfterReset();
        String interval = System.currentTimeMillis() - startTime + "ms";
        log.info("Magento Product Media Reset ended. Elapsed time = " + interval);
    }

    public void deleteProductPicture(EdsProductPicture picture) {
        if (picture.getMagentoFile() != null) {
            EdsItem item = picture.getProduct();
            Product product = new Product(item.getProductNumber() + " ");
            ProductMedia productMedia = new ProductMedia();
            productMedia.setProduct(product);
            productMedia.setFile(picture.getMagentoFile());
            try {
                getServiceFactory().getProductMediaRemoteService().delete(productMedia);
                picture.setLastSyncTime(new Date());
                productPictureManager.update(picture);
            } catch (ServiceException e) {
                log.error("Cannot delete image: " + picture.getName() + "\n" + e.getMessage());
            }
        }
    }

    public void syncCategories() {
        log.info("Magento Category Sync started");
        long startTime = System.currentTimeMillis();
        List<EdsProductCategory> productCategories = productCategoryManager.getProductCategoriesForSync();
        for (EdsProductCategory productCategory : productCategories) {
            if (productCategory.getDeleted()) {
                deleteCategory(productCategory.getMagentoEntityID());
                continue;
            }
            syncCategory(productCategory);
            //sync attributes if any
            createProductAttributes(productCategory);
        }
        productCategoryManager.flushAndClear();
        String interval = System.currentTimeMillis() - startTime + "ms";
        log.info("Magento Category Sync ended. Elapsed time = " + interval);
    }

    @Override
    @Transactional
    public void resetCategories() {
        log.info("Magento Category Reset started");
        long startTime = System.currentTimeMillis();
        List<EdsProductCategory> productCategories = productCategoryManager.getProductCategoriesForReset();
        for (EdsProductCategory productCategory : productCategories) {
            deleteCategory(productCategory.getMagentoEntityID());
            deleteProductAttributes(productCategory);
            deleteAttributeSet(productCategory);
        }
        productCategoryManager.updateProductCategoriesAfterReset();
        String interval = System.currentTimeMillis() - startTime + "ms";
        log.info("Magento Category Reset ended. Elapsed time = " + interval);
    }

    public void syncCategory(EdsProductCategory productCategory) {
        EdsProductCategory parentCategory = productCategory.getParent();
        if (parentCategory != null && parentCategory.getMagentoEntityID() == null) {
            syncCategory(parentCategory);
        }
        Integer parentId = parentCategory != null && parentCategory.getMagentoEntityID() != null ?
                           parentCategory.getMagentoEntityID() : getSoapClient().getConfig().getDefaultRootCategoryId();
        Category category = getServiceFactory().getCategoryRemoteService().getMinimalCategory(parentId, productCategory.getName());
        category.setId(productCategory.getMagentoEntityID());
        category.setIncludeInMenu(true);
        if (productCategory.getMagentoEntityID() == null) {
            category.setPosition(productCategory.getOrder());
        }
        try {
            List<Category> results = getServiceFactory().getCategoryRemoteService().create(parentId, category);
            if (results.get(0) != null) {
                productCategory.setMagentoEntityID(results.get(0).getId());
                productCategory.setMagentoSyncDate(new Date());
                productCategoryManager.update(productCategory);
            }
        } catch (ServiceException e) {
            log.error("Cannot sync category: " + productCategory.getName() + "\n" + e.getMessage());
        }
    }

    public void createProductAttributes(EdsProductCategory productCategory) {
        List<CompanyCustomFieldItem> list = commonServiceLocal.getCompanyCustomFieldsByRelationship(ViewName.ProductCategory, productCategory.getObjectID(), null);
        for (CompanyCustomFieldItem item : list) {
//            Create attribute
            ProductAttribute attribute = new ProductAttribute();
            attribute.setCode(item.getAliasName().toLowerCase() + "_" + productCategory.getObjectID());
            attribute.setScope("global");
            attribute.setType("varchar");
            attribute.setInput("select");
            attribute.setAttributeClass("");
            attribute.setSource("");
            attribute.setVisible(true);
            attribute.setRequired(false);
            attribute.setUserDefined(true);
            attribute.setDefaultValue("");
            attribute.setSearchable(true);
            attribute.setFilterable(true);
            attribute.setComparable(true);
            attribute.setVisibleOnFront(true);
            attribute.setVisibleInAdvancedSearch(true);
            attribute.setUnique(false);
            attribute.setConfigurable(true);

            Map<String, Object> frontendLabels = new HashMap<>();
            frontendLabels.put("store_id", 0);
            frontendLabels.put("label", item.getFieldName());
            ArrayList<Map<String, Object>> params = new ArrayList<>();
            params.add(frontendLabels);
            attribute.set("frontend_label", params);

//            create options
            if (item.getPredefinedValues() != null) {
                String[] strings = item.getPredefinedValues();
                attribute.setOptions(new HashMap<>());
                for (int i = 0; i < strings.length; i++) {
                    attribute.getOptions().put(i, strings[i]);
                }
            }

            Integer setId = getAttributeSet(productCategory).getId();
            try {
                getServiceFactory().getProductAttributeRemoteService().saveAttribute(attribute, setId);
            } catch (ServiceException e) {
                log.error("Cannot create attribute: " + attribute.getCode() + "\n" + e.getMessage());
            }
        }
    }

    public void deleteProductAttributes(EdsProductCategory productCategory) {
        List<CompanyCustomFieldItem> list = commonServiceLocal.getCompanyCustomFieldsByRelationship(ViewName.ProductCategory, productCategory.getObjectID(), null);
        for (CompanyCustomFieldItem item : list) {
            if (item.getAliasName() == null) {
                continue;
            }
            String attributeName = item.getAliasName().toLowerCase() + "_" + productCategory.getObjectID();
            try {
                getSoapClient().call("product_attribute.remove", attributeName);
            } catch (org.apache.axis2.AxisFault e) {
                log.error("Cannot delete attribute: " + attributeName + " Exception: " + e.getMessage());
            }
        }
    }

    public ProductAttributeSet getAttributeSet(EdsProductCategory productCategory) {
        if (productCategory != null) {
            ProductAttributeSet result = null;
            try {
                List<ProductAttributeSet> sets = getServiceFactory().getProductAttributeRemoteService().listAllProductAttributeSet();
                for (ProductAttributeSet set : sets) {
                    if (set.getName().equals("Set" + "_" + productCategory.getObjectID())) {
                        result = set;
                        return result;
                    }
                }
            } catch (ServiceException e) {
                log.error("Cannot get attribute set of category: " + productCategory.getName() + "\n" + e.getMessage());
            }
            result = new ProductAttributeSet();
            result.setName("Set" + "_" + productCategory.getObjectID());
            try {
                getServiceFactory().getProductAttributeRemoteService().saveAttributeSet(result);
                return result;
            } catch (ServiceException e) {
                log.error("Cannot save attribute set: " + result.getName() + "\n" + e.getMessage());
            }
        }
        return ProductAttributeSet.getDefaultProductAttributeSet();
    }

    public void deleteAttributeSet(EdsProductCategory productCategory) {
        if (productCategory == null) {
            return;
        }
        try {
            List<ProductAttributeSet> sets = getServiceFactory().getProductAttributeRemoteService().listAllProductAttributeSet();
            for (ProductAttributeSet set : sets) {
                if (set.getName().equals("Set" + "_" + productCategory.getObjectID())) {
                    soapClient.call("product_attribute_set.remove", set.getId().toString());
                    return;
                }
            }
        } catch (ServiceException e) {
            log.error("Cannot delete attribute set of category: " + productCategory.getName() + "\n" + e.getMessage());
        } catch (org.apache.axis2.AxisFault e) {
            log.error("Cannot delete attribute set of category: " + productCategory.getName() + "\n" + e.getMessage());
        }
    }

    public void deleteCategory(Integer categoryId) {
        if (categoryId != null) {
            try {
                getServiceFactory().getCategoryRemoteService().delete(categoryId);
            } catch (ServiceException e) {
                log.error("Cannot delete category: " + categoryId + "\n" + e.getMessage());
            }
        }
    }

    public void syncInventories() {
        log.info("Magento Stock Sync started");
        long startTime = System.currentTimeMillis();
        Map<Integer, BigDecimal> stockItems = itemStockManager.getItemStocksForSync();
        if (stockItems != null) {
            for (Map.Entry<Integer, BigDecimal> stockItem : stockItems.entrySet()) {
                EdsItem item = itemManager.get(stockItem.getKey());
                Product product = new Product(item.getMagentoEntityID());
                try {
                    product.setQty(stockItem.getValue().doubleValue());
                    product.setManageStock(true);
                    getServiceFactory().getProductRemoteService().updateInventory(product);
                    item.setStockChanged(false);
                    itemManager.update(item);
                } catch (ServiceException e) {
                    log.error("Cannot sync stock of product: " + item.getProductNumber() + "\n" + e.getMessage());
                }
            }
        }
        itemStockManager.flushAndClear();
        String interval = System.currentTimeMillis() - startTime + "ms";
        log.info("Magento Stock Sync ended. Elapsed time = " + interval);
    }

    public void syncInventory(EdsItem item, BigDecimal qtyDiff) {
        syncInventory(item, qtyDiff, false);
    }

    public void syncInventory(EdsItem item, BigDecimal qtyDiff, boolean remove) {
        Set<Product> productSet = new HashSet<>();
        if (item.getMagentoEntityID() != null) {
            try {
                Product product = new Product(item.getMagentoEntityID());
                productSet.add(product);

                getServiceFactory().getProductRemoteService().getInventoryInfo(productSet);
                Iterator<Product> iterator = productSet.iterator();
                product = iterator.next();
                if (remove) {
                    qtyDiff.multiply(new BigDecimal(-1));
                }
                product.setQty(qtyDiff.add(BigDecimal.valueOf(product.getQty())).doubleValue());

                getServiceFactory().getProductRemoteService().updateInventory(product);
            } catch (ServiceException e) {
                log.error("Cannot sync stock of product: " + item.getProductNumber() + "\n" + e.getMessage());
            }
        }
    }

    public EdsCrmAccount syncCustomer(Integer customerId) throws ServiceException{
        Customer customer = getServiceFactory().getCustomerRemoteService().getById(customerId);
        if (customer != null) {
            List<CustomerAddress> cAddresses = getServiceFactory().getCustomerAddressRemoteService().list(customerId);
            return saveCustomer(customer, cAddresses);
        }
        return null;
    }

    private EdsCrmAccount saveCustomer(Customer customer, List<CustomerAddress> cAddresses) {
        CrmAccountItem item = new CrmAccountItem();
        if (customer.getId() != 0) {
            EdsCrmAccount crmAccount = crmAccountManager.getAccountByMagentoId(customer.getId());
            if (crmAccount != null) {
                item.setObjectId(crmAccount.getObjectID());
            }
        }
        String number = crmAccountManager.generateAccountNumber(EdsCrmAccount.CUSTOMER);
        item.setNumber(number);
        item.setMagentoEntityId(customer.getId());
        item.setMagentoLastSyncDate(new Date());
        item.setName(customer.getFirstName() + " " + customer.getLastName() + "(" + number + ")");
        item.setEmail(customer.getEmail());
        item.setCurrencyId(financialSettingsManager.getFinancialSettings().getCurrency().getObjectID());

        SelectItem[] accountTypes = new SelectItem[1];
        SelectItem accountType = new SelectItem(referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.CUSTOMER).getObjectID(), "", "", true);
        accountTypes[0] = accountType;
        item.setAccountTypes(accountTypes);

        syncCustomerAddresses(item, cAddresses);
        Integer crmAccountId = crmServiceLocal.saveAccount(item, EdsCrmAccount.CUSTOMER, null, false, false, false, false);
        return crmAccountManager.get(crmAccountId);
    }

    public void syncCustomerAddresses(CrmAccountItem crmAccountItem, List<CustomerAddress> cAddresses) {
        List<Address> billingAddresses = new ArrayList<>();
        List<Address> mailingAddresses = new ArrayList<>();
        ArrayList<ContactListItem> contacts = new ArrayList<>();

        for (CustomerAddress cAddress : cAddresses) {
            Address address = new Address();
            address.setAddress(cAddress.getStreet());
            address.setCity(cAddress.getCity());

            EdsCountry country = countryManager.getCountryByCode(cAddress.getCountryCode());
            if (country != null) {
                address.setCountryId(country.getObjectID());
                address.setCountry(country.getName());
            }
            address.setZipCode(cAddress.getPostCode());
            address.setPrimary(cAddress.getDefaultBilling() || cAddress.getDefaultShipping());
            if (cAddress.getDefaultShipping()) {
                mailingAddresses.add(address);
            }
            billingAddresses.add(address);

            ContactListItem contactListItem = new ContactListItem();
            contactListItem.getWorkPhone().add(cAddress.getTelephone());
            contactListItem.setPrimaryPhone(cAddress.getTelephone());
            contactListItem.getWorkEmail().add(crmAccountItem.getEmail());
            contactListItem.setPrimaryEmail(crmAccountItem.getEmail());
            contactListItem.setFirstName(cAddress.getFirstName());
            contactListItem.setLastName(cAddress.getLastName());
            contactListItem.setPrimaryContact(cAddress.getDefaultBilling() || cAddress.getDefaultShipping());
            contactListItem.setPrimaryAddress(address);
            contactListItem.setCrmAccount(crmAccountItem);
            contacts.add(contactListItem);
        }
        crmAccountItem.setBillAddresses(billingAddresses.toArray(new Address[0]));
        crmAccountItem.setMailAddresses(mailingAddresses.toArray(new Address[0]));
        crmAccountItem.setContacts(contacts);
    }

    @Transactional
    public void syncOrder(Integer orderId) throws ServiceException{
        Order order = getServiceFactory().getOrderRemoteService().getById(orderId);
        EdsCurrency baseCurrency = financialSettingsManager.getFinancialSettings().getCurrency();
        EdsCrmAccount crmAccount = null;
        NewInvoice data = new NewInvoice();
        if (order.getCustomerIsGuest()) {
            OrderAddress billingAddress = order.getBillingAddress();
            OrderAddress shippingAddress = order.getShippingAddress();
            Customer guest = new Customer();
            guest.setId(0);
            guest.setFirstName(billingAddress.getFirstName());
            guest.setLastName(billingAddress.getLastName());
            guest.setEmail(order.getCustomerEmail());

            List<CustomerAddress> customerAddresses = new ArrayList<>();
            customerAddresses.add(getCustomerAddressFromOrder(billingAddress, true, false));
            customerAddresses.add(getCustomerAddressFromOrder(shippingAddress, false, true));

            crmAccount = saveCustomer(guest, customerAddresses);
            data.setClientID(crmAccount.getObjectID());
        } else {
            crmAccount = crmAccountManager.getAccountByMagentoId(order.getCustomer().getId());
            if (crmAccount == null) {
                crmAccount = syncCustomer(order.getCustomer().getId());
            }
            data.setClientID(crmAccount.getObjectID());
        }
        if (crmAccount != null) {
            data.setBillAddressID(crmAccount.getBillingAddress() != null ? crmAccount.getBillingAddress().getObjectID() : null);
            data.setMailAddressID(crmAccount.getMailingAddress() != null ? crmAccount.getMailingAddress().getObjectID() : null);
        }
        data.setInvoiceNumber(orderId.toString());
        Calendar calendar = Calendar.getInstance();
        data.setInvoiceDate(new DateNonConvertable(calendar.getTime()));
        calendar.add(Calendar.DATE, 30);
        data.setDueDate(new DateNonConvertable(calendar.getTime()));

        data.setStatusCode(SALE_ORDER);
        data.setSalesOrder(true);

        data.setBaseCurrency(baseCurrency.createCurrencyItem());
        data.setExchageRate(new BigDecimal("1.000"));
        data.setCurrencyID(baseCurrency.getObjectID());

        if (order.getShippingMethod() != null && order.getShippingDescription() != null) {
            EdsShippingMethod shippingMethod = shippingMethodManager.getShippingMethodByName(order.getShippingDescription());
            if (shippingMethod == null) {
                shippingMethod = new EdsShippingMethod();
                shippingMethod.setName(order.getShippingDescription());
                shippingMethod.setDescription(order.getShippingMethod());
                shippingMethod.setPrice(BigDecimal.valueOf(order.getShippingAmount()));
                shippingMethod.setCurrency(baseCurrency);
                shippingMethodManager.create(shippingMethod);
                shippingMethodManager.flushAndClear();
            }
            data.setShippingMethodID(shippingMethod.getObjectID());
            data.setShippingPrice(BigDecimal.valueOf(order.getShippingAmount()));
        }

        data.setSubtotal(BigDecimal.valueOf(order.getSubtotal()));
        data.setTotal(BigDecimal.valueOf(order.getGrandTotal()));
        data.setTotalDiscount(BigDecimal.valueOf(order.getDiscountAmount()));
        List<NewInvoiceItem> dataItems = new ArrayList<>();
        for (OrderItem orderItem : order.getItems()) {
            NewInvoiceItem dataItem = new NewInvoiceItem();
            if (orderItem.getParentItemId() == null) {
                EdsItem edsItem = itemManager.getItemByNumber(orderItem.getSku());
                dataItem.setItemID(edsItem.getObjectID());
                dataItem.setDescription(edsItem.getDescription());
                dataItem.setQuantity(BigDecimal.valueOf(orderItem.getQtyOrdered()));
                dataItem.setUnitPrice(BigDecimal.valueOf(orderItem.getPrice()));
                dataItem.setDiscountAmount(new BigDecimal((String) orderItem.get("discount_amount")));
                dataItem.setDiscountPercent(new BigDecimal((String) orderItem.get("discount_percent")));
                dataItem.setTotalAmount(BigDecimal.valueOf(orderItem.getRowTotal()));

                dataItems.add(dataItem);
            }
        }
        data.setItems(dataItems.toArray(new NewInvoiceItem[0]));

        SaveResult result = quoteService.saveSaleQuote(data);
        /*if(result.getId()!= null){
            EdsSaleQuote saleQuote = (EdsSaleQuote) quoteManager.get(result.getId());
            baseEventPostProcessor.registerEvent(SalesOrderEventListenerImpl.TYPE, SalesOrderEventListenerImpl.EVENT_PICKLIST_SALE_ORDER, saleQuote, userManager.getUser());
        }*/
    }

    private CustomerAddress getCustomerAddressFromOrder(OrderAddress orderAddress, boolean billingDefault, boolean shippingDefault) {
        CustomerAddress customerAddress = new CustomerAddress();
        customerAddress.setStreet(orderAddress.getStreet());
        customerAddress.setCity(orderAddress.getCity());
        customerAddress.setCompany(orderAddress.getCompany());
        customerAddress.setCountryCode(orderAddress.getCountryCode());
        customerAddress.setFax(orderAddress.getFax());
        customerAddress.setPostCode(orderAddress.getPostCode());
        customerAddress.setTelephone(orderAddress.getTelephone());
        customerAddress.setDefaultBilling(billingDefault);
        customerAddress.setDefaultShipping(shippingDefault);
        return customerAddress;
    }

    public void emailOrderToCustomer(Integer orderID, EdsCrmAccount crmAccount) {
        /*get company default order email template content*/
        EdsEmailTemplate defaultEmailTemplate = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(SALES_ORDER_CATEGORY);
        if (defaultEmailTemplate == null) {
            defaultEmailTemplate = emailTemplateManager.getDefaultEmailTemplateByCategory(SALES_ORDER_CATEGORY);
        }
        EntityToEmailTemplate item = new EntityToEmailTemplate();
        item.setEntityId(orderID);
        item.setEntityType(SALES_ORDER_CATEGORY);
        item.setMailReceiverId(crmAccount.getPrimaryContact().getObjectID());
        item.setEmailTemplateId(defaultEmailTemplate.getObjectID());
        EmailTemplateItem emailTemplateItem = emailTemplateService.generateEmailTemplateData(item, null);

        /*get company default pdf template*/
        PdfTemplateItemList pdfTemplateItemList = baseInvoiceService.getCompanyPdfTemplatesByType(PdfReferenceCodeNameEnum.SALES_ORDER.name());
        Integer defaultPdfTemplateID = pdfTemplateItemList.getDefaultTemplateID();
        if (defaultPdfTemplateID == null && pdfTemplateItemList.getItems().length > 0) {//if default template doesn't exist, then get the fuckin' first one of the list
            defaultPdfTemplateID = pdfTemplateItemList.getItems()[0].getId();
        }

        MessageItem messageItem = new MessageItem();
        messageItem.setSubject(emailTemplateItem.getSubject());
        messageItem.setClient(true);
        messageItem.setInvoiceID(orderID);
        messageItem.setMailContent(emailTemplateItem.getMessageHTML());
        messageItem.setContactId(crmAccount.getPrimaryContact().getObjectID());
        messageItem.setEmailTemplateID(defaultEmailTemplate.getObjectID());
        messageItem.setPdfTemplateID(defaultPdfTemplateID);
        messageItem.setType(SALES_ORDER_CATEGORY);
        messageItem.setToEmails(crmAccount.getPrimaryContact().getPrimaryEmail());
        messageItem.setFromEmail(userManager.getUser().getEmail());
        messageItem.setReplyTo(userManager.getUser().getEmail());
        messageItem.setFileResources(new ArrayList<>());

        quoteService.sendToClientOrSupplier(messageItem);
    }
}
