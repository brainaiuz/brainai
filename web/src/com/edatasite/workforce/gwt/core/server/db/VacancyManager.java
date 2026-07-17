package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.recruitment.EdsVacancy;
import com.edatasite.workforce.gwt.contact.client.rpc.CommonItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;

import java.util.List;

/**
 * User: Hayot
 * Date: 09-Jul-2012
 * Time: 16:42:58
 */
public interface VacancyManager extends Manager<EdsVacancy> {
    List<EdsVacancy> list(ListingFilterParameter fp);

    Integer getVacancyLastIntNumber();

    List<Object[]> getList();

    void deleteCandidateVacancies(Integer candidateID);

    List<Object[]> getVacancyMatchedCandidates(Integer vacancyId);

    List<CommonItem> getCandidatePerVacancyChartData();

    List<EdsVacancy> getUndeletedVacancyIn(String vacancyIds);

    List<Integer> getUndeletedVacancyIdList(String s);

    List<Integer> getVacancyIdListWithLimit(Integer companyID, int startat, int limit);

    List<Integer> getCompanyDeleteVacancyForSolr(SolrReindexRpc solrReindexRpc);

    List<EdsVacancy> getVacancyListForSolr(SolrReindexRpc solrReindexRpc, Integer start, int limit);

    List<Integer> getVacancyIdsForSolr(List<Integer> idsFromSolrDocument);

    EdsVacancy getByIntegrationId(String integrationId);

    EdsVacancy getVacancyByNumber(String vacancyNumber);
}