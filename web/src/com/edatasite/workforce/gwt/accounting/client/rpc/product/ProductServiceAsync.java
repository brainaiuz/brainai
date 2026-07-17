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
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.invoice.client.rpc.QuantityItem;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.Date;

public interface ProductServiceAsync {
    Request getProductsList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<ProductItem>> async);

    Request getProductsListFromSolr(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<ProductItem>> async);

    Request getStockProductsList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<ProductItem>> async);

    void deleteSelectedProductServices(ArrayList<Integer> ids, AsyncCallback<ArrayList<Integer>> async);

    void getProduct(Integer productId, AsyncCallback<NewProduct> async);

    void getProductEditData(Integer productId, Boolean isFromExisting, AsyncCallback<NewProduct> async);

    void getVendorsAsSelectItem(AsyncCallback<SelectItem[]> async);

    void getUnitMeasurementsAsSelectItem(AsyncCallback<SelectItem[]> async);

    void saveProduct(NewProduct product, AsyncCallback<ProductSelectItem> async);

    void saveProduct(NewProduct product, boolean runWebhook, AsyncCallback<ProductSelectItem> async);

    void saveProducts(NewProduct[] products, AsyncCallback<Integer[]> async);

    void getProductForVariation(Integer productID, AsyncCallback<VariationItem> async);

    void saveVariationProducts(NewProduct[] products, Integer parentProductId, AsyncCallback<Integer[]> async);

    void deleteProduct(Integer productId, AsyncCallback<Boolean> async);

    void deleteProductChilds(Integer productId, AsyncCallback<Boolean> async);

    void getProductBaseData(Integer productId, AsyncCallback<NewProduct> async);

    void getProductBaseData(Integer productId, boolean fromOpportunnity, AsyncCallback<NewProduct> async);

    void getSerialsQty(Integer productId, String serialNumber, Date expirationDate, AsyncCallback<Integer> async);

    void getProductKitProducts(Integer productId, boolean checkForPurchasedFromSupplier, AsyncCallback<NewProduct[]> async);

    void getProductsForReport(ListingFilterParameter filterParametrs, AsyncCallback<NewProduct[]> async);

    void getStockValuations(ListingFilterParameter filterParametrs, DateNonConvertable fromDate, DateNonConvertable toDate, AsyncCallback<InventoryStockData> async);

    void getInventoryStock(ListingFilterParameter filterParametrs, AsyncCallback<ProductItem> callback);

    void enableComission(AsyncCallback<Boolean> async);

    void getProductByID(Integer objectID, AsyncCallback<ProductItem> async);

    void generateProductNumber(AsyncCallback<NumberData> callback);

    void generateProductNumber(Integer intNumber, AsyncCallback<NumberData> callback);

    void validateProduct(ListingFilterParameter filterParametrs, AsyncCallback<boolean[]> async);

    void saveStockAdjustment(AdjustmentItem adjustmentItem, AsyncCallback<TestRPC> callback);

    void saveStockTransfer(StockTransferItem stockTransferItem, AsyncCallback<Integer> callback);

    void updateInventoryItemsAfterExportSaasu(Integer objectId, Date lastUpdateDate, String saasuLastUpdatedUid, Integer saasuGUID, AsyncCallback<Void> async);

    //void updateItemByQB(NewProduct product, String externalGUID, Integer synchItemType, AsyncCallback<Boolean> async);

    void getInterCompanyProducts(ListingFilterParameter filterParametrs, AsyncCallback<SelectItem[]> callback);

    void getProductServiceListCustomSettings(AsyncCallback<ProductCustomSettings> callback);

    void updateProductCategory(Integer productID, Integer unitMeasurementId, AsyncCallback<Boolean> callback);

    void updateProductMeasurement(Integer productID, Integer categoryID, AsyncCallback<Boolean> callback);

    void inActiveProduct(Integer objectId, AsyncCallback<Boolean> callback);

    void getStockAdjustments(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<StockAdjustmentListItem>> callback);

    void getStockAdjustmentData(Integer objectID, AsyncCallback<AdjustmentItem> callback);

    void deleteStockAdjustment(Integer objectID, AsyncCallback<Integer> callback);

    void deleteStockTransfer(Integer objectID, AsyncCallback<Integer> callback);

    void getProductCommentList(Integer productId, AsyncCallback<ProductCommentList> async);

    void saveProductComment(ProductCommentItem productCommentItem, AsyncCallback<Integer> async);

    void deleteProductComment(Integer objectId, AsyncCallback<Void> async);

    void updateProductComment(ProductCommentItem productCommentItem, AsyncCallback<Void> async);

    void getProductWithLocale(Integer objectID, AsyncCallback<NewProduct> async);

    void saveProductLocalization(NewProduct product, AsyncCallback<Void> async);

    void getSubLocaleProduct(Integer parentID, Integer localeID, AsyncCallback<NewProduct> async);

    void getStockTransferList(ListingFilterParameter fp, AsyncCallback<ListResult<StockTransferItem>> asyncCallback);

    void getStockTransfer(Integer objectID, AsyncCallback<StockTransferItem> asyncCallback);

    void getStockTransferSummaryData(Integer objectID, AsyncCallback<StockTransferItem> asyncCallback);

    void reorderPointEmail(Integer companyId, AsyncCallback<Void> callback);

    void getProductAsSelectItem(Integer id, AsyncCallback<ProductSelectItem> async);

    void getProductsAsSelectItem(ListingFilterParameter fp, AsyncCallback<SelectItem[]> async);

    void getCompanyProductsByType(ListingFilterParameter fp, AsyncCallback<ProductSelectItem[]> async);

    void deleteProductPicture(Integer productPictureId, AsyncCallback<Boolean> async);

    void getProductPictures(Integer productID, Integer fileSizeType, AsyncCallback<ProductPicture[]> async);

    void getProductPictureByID(Integer productPictureID, AsyncCallback<ProductPicture> async);

    void setDefaultProductPicture(Integer productPictureId, Integer productID, AsyncCallback<Boolean> async);

    void getProductColumns(AsyncCallback<SelectItem[]> asyncCallback);


    void saveProductItemCellValue(ProductItem rowValue, String columnCodeName, AsyncCallback<Boolean> asyncCallback);

    void hasServicesIncluded(QuantityItem[] quantityItems, AsyncCallback<Boolean> asyncCallback);

    void inActiveProducts(ArrayList<Integer> objectId, AsyncCallback<Boolean> booleanAbstractAsyncCallback);

    void getProductCategoryCF(Integer id, AsyncCallback<ArrayList<CompanyCustomFieldItem>> asyncCallBack);

    void getProductLogHistoryList(ListingFilterParameter listingFilterParameter, AsyncCallback<ListResult<LogHistoryItem>> listResultAsyncCallback);

    void regenerateProductNumber(SelectItem[] supplierIds, Integer categoryId, NumberData productCurrentNumber, AsyncCallback<NumberData> callback);

    void getRentalProductEditData(Integer productId, Boolean isFromExisting, AsyncCallback<NewProduct> async);

    void updateStockAdjustmentStatus(Integer objectID, String statusCode, String rejectionReason, AsyncCallback<Void> async);

    void processAiImage(Integer pictureId, String prompt, String actionType, String aspectRatio, AsyncCallback<AiImageResult> async);

    void saveAiGeneratedImage(Integer pictureId, AsyncCallback<ProductPicture> async);

    void getSavedAiPrompts(AsyncCallback<AiSavedPrompt[]> async);

    void saveAiPrompt(String promptText, AsyncCallback<Void> async);

    void deleteAiPrompt(Integer promptId, AsyncCallback<Void> async);

    void getAiSuggestedPrompts(AsyncCallback<String[]> async);

    void isAiAvailable(AsyncCallback<Boolean> async);
}
