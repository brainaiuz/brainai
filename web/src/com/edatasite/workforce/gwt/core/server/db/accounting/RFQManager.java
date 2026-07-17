package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsRFQ;
import com.edatasite.workforce.core.domain.accounting.EdsRFQItem;
import com.edatasite.workforce.core.domain.accounting.EdsShippingData;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/27/12
 * Time: 6:36 PM
 * To change this template use File | Settings | File Templates.
 */
public interface RFQManager extends Manager<EdsRFQ>{
    NumberData generateNumberData();

    List<EdsRFQ> getRFQList(ListingFilterParameter filterParameters);

    Integer getRFQCount(ListingFilterParameter filterParameters);

    void deleteRFQItems(Integer rfqID);

    List<EdsRFQItem> getRFQItemsBySupplier(Integer supplierID, Integer rfqID);

    void mergeOldCrmAccountToNewOne(List<Integer> oldAccountIDs, Integer newAccountID);

    boolean isSupplierBidApplied(Integer rfqID);

    boolean isNotConvertedBidsExists(Integer rfqID);

    List<EdsRFQItem> getRFQItemsForAccountant(Integer objectID);

    boolean isRFQNumberExist(String number, Integer objectID);

    Integer getByOpportunity(Integer opportunityId);

    List<Integer> getRFQIdsByIds(String IDs);

    List<Integer> getRFQIdsWithLimit(Integer startat, Integer limit);

    List<EdsRFQ> getRFQListForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit);
}
