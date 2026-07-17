package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.server.db.impl.ListingObjectItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 09-Jul-2009
 * Time: 15:48:11
 * To change this template use File | Settings | File Templates.
 */
public interface OpportunityManager extends Manager<EdsOpportunity> {

    List<Object[]> getOpportunityByStage();

    List<Object[]> getOpportunityByLeadSource();

    void deleteItems(Integer objectID);

    Integer getLastIntNumber();

    boolean isOpportunityNumberExists(String number, Integer objectId);

    List<Integer> getCompanyOpportunityListForSolr(SolrReindexRpc solrReindex);

    List<EdsOpportunity> getCompanyOpportunityListForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit);

    List<Integer> getOpportunityIdsByIDs(String ids);

    List<EdsOpportunity> getOpportunityByIds(String Ids);

    List<EdsOpportunity> getOpportunities();

    List<EdsOpportunity> getOpportunitiesByCampaign(Integer campaignID);

    EdsOpportunity getOpportunityByContactId(Integer contactID);

    List<Integer> getOpportunityIdsWithLimit(int startat, int limit);

    List<Integer> deleteOpportinities(List<Integer> objectIDs, EdsUser user);

    SelectItem[] getOpportunityCountByStage(ListingFilterParameter fp);

    List<EdsOpportunity> getByImportFileID(Integer entityID, int start, int limit);

    void changeOpportunity(Integer campaignId, ArrayList<Integer> leadIDs, Integer objectID);

    List<EdsOpportunity> getOpportunityList(ListingFilterParameter filterParametrs);

    ListingObjectItem getOpportunityExpenseClaimList(Integer opportunityId, ListingFilterParameter filterParameter);

    EdsOpportunity getOpportunityByContactPhone(String callNumber);

    EdsOpportunity getSiblingOpportunityByPrevItem(Integer prevopportunity, Integer objectID);

    Long getOpportunityCountByStage(Long position, Integer stageId);

    List<EdsOpportunity> getOpportunitiesByStageId(Long position, Integer stageId, int start, int limit);

    Long getMinKanbanOrder(Integer stageId);

    List<EdsOpportunity> getOpportunityByCrmAccountID(Integer accountId);

    List<EdsOpportunity> getOpportunityByCrmContactID(Integer contactId);

    void update(EdsOpportunity obj, boolean withoutUpdateDate);

    EdsOpportunity getByNumber(String number);

    List<EdsOpportunity> getOpportuniesByCategoryId(Integer categoryId);

    List<EdsOpportunity> getOpportuniesByProductId(Integer productId);

    EdsOpportunity getOpportunyPreviusStage(Integer historicalParentId);
}
