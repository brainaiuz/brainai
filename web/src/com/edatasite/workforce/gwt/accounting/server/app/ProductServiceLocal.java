package com.edatasite.workforce.gwt.accounting.server.app;

import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.AdjustmentItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.StockItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.StockTransferItem;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.product.ProductPicture;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.ZapierProductVariantTO;
import org.apache.solr.client.solrj.SolrQuery;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 7/25/12
 * Time: 8:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProductServiceLocal {

    NumberData generateProductNumber();

    NumberData generateProductNumber(Integer intNumber);

    boolean deleteProduct(Integer productId);

    List<NewProduct> getInterCompanyTransactionProducts(NewInvoiceItem[] invoiceItems);

    HashMap<Integer,Integer> convertInterCompanyProducts(List<NewProduct> products);

    String getImageUrl(EdsUpload upload);

    NewProduct getProductBaseData(Integer productId);

    ListResult<ProductItem> getProductsListFromSolr(ListingFilterParameter filterParametrs);

    ListResult<ProductItem> getProductsListFromSolrGeneric(ListingFilterParameter filterParametrs, List<String> searchInCustomFields);

    String getProductListSolrQuery(ListingFilterParameter filterParametrs, List<String> searchInCustomFields);

    NewProduct getProductEditData(Integer productId, Boolean isFromExisting);

    SelectItem[] getProductsAsSelectItem(ListingFilterParameter fp);

    ProductSelectItem[] getCompanyProductsByType(ListingFilterParameter filterParameters);

    SelectItem[] getProductColumns();

    NewProduct getProduct(Integer productId);

    ProductSelectItem saveProduct(NewProduct product);
    ProductSelectItem saveProduct(NewProduct product, boolean runWebhook);
    ZapierProductVariantTO convertToZapierProductVariant(NewProduct productVariant);

    AdjustmentItem getStockAdjustmentData(Integer objectID);

    TestRPC saveStockAdjustment(AdjustmentItem adjustmentItem);

    Integer deleteStockAdjustment(Integer objectID);

    Integer saveStockTransfer(StockTransferItem stockTransferItem);

    Integer deleteStockTransfer(Integer objectID);

    StockTransferItem getStockTransfer(Integer objectID);

    Integer[] saveVariationProducts(NewProduct[] products, Integer parentProductId, boolean runWebhook);

    Integer[] saveVariationProducts(NewProduct[] products, Integer parentProductId);

    ListResult<StockTransferItem> getStockTransferList(ListingFilterParameter filterParameter);

    ProductPicture[] getProductPictures(Integer productID, Integer fileSizeType);

    BigDecimal getAverageCost(Integer productID);

    SolrQuery getProductsServicesSolrQuery(ListingFilterParameter filterParameter, String solrQuery);

    BankTransferNumberData generateStockAdjustmentNumberFormat();

    void initProductWarehouseLocations(NewProduct product, EdsItem item, LinkedHashMap<Integer, List<StockItem>> stockItemsMap);

    void updateActive(List<Integer> ids, boolean active);
}
