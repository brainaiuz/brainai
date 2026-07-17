package com.edatasite.workforce.gwt.accounting.client.rpc.product;

import com.edatasite.workforce.gwt.accounting.client.rpc.LogHistoryItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductCommentItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductCommentList;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductCustomSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.StockAdjustmentListItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.VariationItem;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.product.AiImageResult;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.product.AiSavedPrompt;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.product.ProductPicture;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.invoice.client.rpc.QuantityItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Feb 2, 2011
 * Time: 7:39:21 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProductService extends RemoteService {

    ListResult<ProductItem> getProductsList(ListingFilterParameter filterParametrs);

    ListResult<ProductItem> getProductsListFromSolr(ListingFilterParameter filterParametrs);

    ListResult<ProductItem> getStockProductsList(ListingFilterParameter filterParametrs);

    InventoryStockData getStockValuations(ListingFilterParameter filterParametrs, DateNonConvertable fromDate, DateNonConvertable toDate);

    ArrayList<Integer> deleteSelectedProductServices(ArrayList<Integer> ids);

    ProductItem getProductByID(Integer objectID);

    NewProduct[] getProductsForReport(ListingFilterParameter filterParametrs);

    ProductItem getInventoryStock(ListingFilterParameter filterParametrs);

    NewProduct getProduct(Integer productId);

    NewProduct getProductEditData(Integer productId, Boolean isFromExisting);

    NewProduct getRentalProductEditData(Integer productId, Boolean isFromExisting);

    NewProduct getProductBaseData(Integer productId);

    Integer getSerialsQty(Integer productId, String serialNumber, Date expirationDate);

    NewProduct[] getProductKitProducts(Integer productId, boolean checkForPurchasedFromSupplier);

    SelectItem[] getVendorsAsSelectItem();

    SelectItem[] getUnitMeasurementsAsSelectItem();

    ProductSelectItem saveProduct(NewProduct product);

    ProductSelectItem saveProduct(NewProduct product, boolean runWebhook);

    Integer[] saveProducts(NewProduct[] products);

    VariationItem getProductForVariation(Integer productID);

    Integer[] saveVariationProducts(NewProduct[] products, Integer parentProductId);

    TestRPC saveStockAdjustment(AdjustmentItem adjustmentItem);

    boolean enableComission();

    boolean deleteProduct(Integer productId);

    boolean deleteProductChilds(Integer productId);

    ProductCommentList getProductCommentList(Integer productId);

    NumberData generateProductNumber();

    NumberData generateProductNumber(Integer intNumber);

    boolean[] validateProduct(ListingFilterParameter filterParametrs);

    void updateInventoryItemsAfterExportSaasu(Integer objectId, Date lastUpdateDate, String saasuLastUpdatedUid, Integer saasuGUID);

    //Boolean updateItemByQB(NewProduct product, String externalGUID, Integer synchItemType);

    SelectItem[] getInterCompanyProducts(ListingFilterParameter filterParametrs);

    ProductCustomSettings getProductServiceListCustomSettings();

    Boolean updateProductCategory(Integer productID, Integer categoryID);

    Boolean updateProductMeasurement(Integer productID, Integer unitMeasurementId);

    boolean inActiveProduct(Integer objectId);

    Boolean inActiveProducts(ArrayList<Integer> ids);

    ListResult<StockAdjustmentListItem> getStockAdjustments(ListingFilterParameter filterParametrs);

    AdjustmentItem getStockAdjustmentData(Integer objectID);

    Integer deleteStockAdjustment(Integer objectID);

    Integer deleteStockTransfer(Integer objectID);

    Integer saveProductComment(ProductCommentItem productCommentItem);

    void deleteProductComment(Integer objectId);

    void updateProductComment(ProductCommentItem productCommentItem);

    NewProduct getProductWithLocale(Integer objectID);

    void saveProductLocalization(NewProduct product);

    NewProduct getSubLocaleProduct(Integer parentID, Integer localeID);

    Integer saveStockTransfer(StockTransferItem stockTransferItem);

    ListResult<StockTransferItem> getStockTransferList(ListingFilterParameter fp);

    StockTransferItem getStockTransfer(Integer objectID);

    StockTransferItem getStockTransferSummaryData(Integer objectID);

    void reorderPointEmail(Integer companyId);

    ProductSelectItem getProductAsSelectItem(Integer id);

    SelectItem[] getProductsAsSelectItem(ListingFilterParameter fp);

    ProductSelectItem[] getCompanyProductsByType(ListingFilterParameter fp);

    Boolean deleteProductPicture(Integer productPictureId);

    ProductPicture[] getProductPictures(Integer productID, Integer fileSizeType);

    ProductPicture getProductPictureByID(Integer productPictureID);

    Boolean setDefaultProductPicture(Integer productPictureId, Integer productID);

    SelectItem[] getProductColumns();

    boolean saveProductItemCellValue(ProductItem rowValue, String columnCodeName);

    boolean hasServicesIncluded(QuantityItem[] quantityItems);

    ArrayList<CompanyCustomFieldItem> getProductCategoryCF(Integer id);

    NewProduct getProductBaseData(Integer productId, boolean fromOpportunnity);

    ListResult<LogHistoryItem> getProductLogHistoryList(ListingFilterParameter listingFilterParameter);

    NumberData regenerateProductNumber(SelectItem[] supplierIds, Integer categoryId, NumberData productCurrentNumber);

    void updateStockAdjustmentStatus(Integer objectID, String statusCode, String rejectionReason);

    AiImageResult processAiImage(Integer pictureId, String prompt, String actionType, String aspectRatio);

    ProductPicture saveAiGeneratedImage(Integer pictureId);

    AiSavedPrompt[] getSavedAiPrompts();

    void saveAiPrompt(String promptText);

    void deleteAiPrompt(Integer promptId);

    String[] getAiSuggestedPrompts();

    boolean isAiAvailable();


    class App {
        public static ProductServiceAsync get() {
            ServiceDefTarget target = GWT.create(ProductService.class);
            target.setServiceEntryPoint(Utils.getHostNameURL() + "rpc/product");
            return (ProductServiceAsync) target;
        }
    }
}
