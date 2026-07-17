package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsStepEmployee;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 8/24/12
 * Time: 4:32 PM
 * To change this template use File | Settings | File Templates.
 */
public interface StepEmployeeManager extends Manager<EdsStepEmployee> {

    List<EdsStepEmployee> getEmployeeStepsByEmployeeId(Integer userId);

    EdsStepEmployee getEmployeeStepByEmployeeIdAndStepId(Integer userId, Integer stepId);

    List<EdsStepEmployee> getStepList(ListingFilterParameter fp);

    List<EdsStepEmployee> getListForApprovalWidget(ListingFilterParameter fp);

    Integer getTotalCount(ListingFilterParameter fp);

    void updateStepStatuses(List<Integer> statusIDs);

    void removeByIDs(ArrayList<Integer> stepIDs);

    List<EdsStepEmployee> archiveOthers(Integer employeeID, Integer objectID, Integer stepID, String type);

    List<Integer> getCompanyDeleteEmployeeStepForSolr(SolrReindexRpc solrReindexRpc);

    List<EdsStepEmployee> getEmployeeStepListForSolr(SolrReindexRpc solrReindexRpc, Integer start, int limit);

    List<EdsStepEmployee> getUndeletedStepIn(String stepIDs);

    List<Integer> getEmployeeStepIdListWithLimit(Integer companyID, int start, int limit);

    List<Integer> getUndeletedEmployeeStepIdList(String stepIDs);

    List<EdsStepEmployee> getStepsByFormID(String formID, Integer entityID, String entityType);

    String getStepIDsBySolrIDs(List<Integer> idsFromSolrDocument);

    HashMap<Integer, String> getApproversStatus(String existingProjectIDs);
}
