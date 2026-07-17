package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsShippingData;
import com.edatasite.workforce.gwt.core.client.enums.ShippingDataType;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingDataItem;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: Murad Satimov
 * Date: 1/9/18 8:26 PM
 */
public interface ShippingDataManager extends Manager<EdsShippingData> {

    Integer getListingCount(ListingFilterParameter fp);

    List<EdsShippingData> getList(ListingFilterParameter fp);

    Integer getGrnGdnRelatedInvoiceNumber(Integer shippingDataId);

    List<EdsShippingData> getGrnGdnsByInvoiceId(Integer shippingDataId);

    List<EdsShippingData> getNotConvertedGDNs(Integer salesOrderId);

    Integer getLastIntNumberData(ShippingDataType shippingDataType);

    Long countShippingDataByQuoteIdAndType(Integer quoteId, ShippingDataType shippingType);

    List<EdsShippingData> getByQuoteId(Integer quoteId);

    boolean hasGDNInSalesOrder(Integer salesORderId);

    boolean hasGRNInPurchaseOrder(Integer salesORderId);

    List<EdsShippingData> getShippingDataQuoteIds(List<Integer> quoteIds);

    List<Integer> getAllInvoicesByShippingDataIds(List<Integer> shippingDataId, String status);

    List<EdsShippingData> getShippingDataList(ListingFilterParameter fp);

    Integer getShippingDataCount(ListingFilterParameter fp);

    List<Integer> getShippingDataIdsByIds(String IDs);

    List<EdsShippingData> getShippingDataListForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit);

    List<Integer> getShippingDataIdsWithLimit(Integer startat, Integer limit);

    BigDecimal getTotalAllocatedAmount(Integer purchaseOrderId);

    BigDecimal getUnallocatedAmount(Integer shippingDataId, Integer purchaseOrderId);

    Boolean getShippingDataByNumber(String number);

    Map<Integer, ShippingDataItem> getQuoteItemIdsUsedInGrnOrGdn(Integer quoteId);

    Date getLatestGrnDate(Integer quoteId);

    List<Integer> getCompanyDeletedShippingDatasForSolr(SolrReindexRpc solrReindex);
}
