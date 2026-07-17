/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/17 8:29:44                                                                                            *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 21-Jul-2009
 * Time: 17:22:16
 * To change this template use File | Settings | File Templates.
 */
public interface CaseManager extends Manager<EdsCase> {

    List<Integer> getCaseIDsByLeadIDs(List<Integer> caseIDs);

    List<Integer> getCaseIDsByIDs(List<Integer> caseIDs);

    EdsCase getByID(Integer id);

    List<Integer> getCasesIdsByIds(String caseIds);

    List<Integer> getCompanyCaseIdList(Integer companyID, int startat, int limit);

    List<EdsCase> getCompanyCaseList(Integer companyID, int startat, int limit);

    List<EdsCase> getCasesByIDs(List<Integer> caseIds);

    List<EdsCase> getCasesByIDs(String caseIds);

    List<Integer> getCompanyDeletedCaseForSolr(SolrReindexRpc solrReindex);

    List<EdsCase> getCompanyCaseForSolr(SolrReindexRpc solrReindex, int startat, int limit);

    List<EdsCase> getCompanyCaseListForSolr(SolrReindexRpc solrReindex, int startat, int limit);

    List<EdsCase> getCompanyCaseList(SolrReindexRpc solrReindex);

    void setCaseDeletedTrue(ArrayList<Integer> iDs);

    Map<Integer, Integer> getCasesWithTrackerIDs();

    EdsCase getByTrackerID(Integer trackerID);

    List<Integer> getTrackerIDsByCaseIDs(ArrayList<Integer> caseIDs);

    List<Integer> getTrackerIDsWithAttachments(List<Integer> trackerIDs);

    List<EdsCase> getCasesByReporter(String reporterType, Integer reporterID);

    Integer getTrackerID(Integer caseID);

    void update(EdsCase crmCase, boolean addToSolr);

    EdsCase getCaseByTrackerID(Integer trackerID);

    EdsCase getSiblingCaseByPrevItem(Integer previousCaseID, Integer statusID);

    Long getCaseCountByStatus(Long prev, EdsReference status);

    List<EdsCase> getCasesByStatus(Long prev, EdsReference status, int start, int limit);

    Long getMinKanbanOrder(Integer statusId);

    Integer getContactRequestCount(Integer contactId, Integer caseId);

    Object[] getLastCaseNumberAndCreationDate(Integer contactId);
}