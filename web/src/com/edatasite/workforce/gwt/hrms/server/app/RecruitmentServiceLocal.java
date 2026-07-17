package com.edatasite.workforce.gwt.hrms.server.app;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.recruitment.EdsPlacement;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.VacancyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.hrms.client.rpc.PlacementItem;
import org.apache.solr.client.solrj.SolrQuery;

import java.util.List;

/**
 * User: Ilhombek
 * Date: 7/27/12
 * Time: 1:29 PM
 */
public interface RecruitmentServiceLocal {
    SolrQuery getSolrQueryForVacancy(ListingFilterParameter fp);

    ListResult<ContactListItem> listCandidates(ListingFilterParameter filterParameter);

    void indexCompanyVacancy(SolrReindexRpc solrReindexRpc);

    void indexCompanyEmployeeStep(SolrReindexRpc solrReindexRpc);

    ListResult<VacancyItem> getVacancyList(ListingFilterParameter filterParameter);

    SelectItem[] getVacancyJobFamily();

    void insertCandidateStatusHistory(EdsCrmContact contact, EdsReference newStatus, String note);

    List<EdsPlacement> getPlacements(ListingFilterParameter filterParameter);

    void savePlacement(PlacementItem placementItem, DateNonConvertable hireDate);

    NumberData generatePlacementNumber();

}
