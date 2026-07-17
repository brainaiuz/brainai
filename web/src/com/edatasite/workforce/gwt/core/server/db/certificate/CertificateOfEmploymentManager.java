package com.edatasite.workforce.gwt.core.server.db.certificate;

import com.edatasite.workforce.core.domain.certificate.EdsCertificateOfEmployment;
import com.edatasite.workforce.core.domain.certificate.EdsCertificateOfEmploymentType;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by Khasan on 11.09.14.
 */
public interface CertificateOfEmploymentManager extends Manager<EdsCertificateOfEmployment> {

    NumberData getCertificateNumber();

    List<EdsCertificateOfEmploymentType> getCertificateTypes();

    List<EdsCertificateOfEmploymentType> getCertificateTypesWithPermission();

    EdsCertificateOfEmploymentType getCertificateType(Integer certificateTypeID);

    List<EdsCertificateOfEmployment> getCertificateList(ListingFilterParameter fp);

    boolean isCertificateForm(String formId);

    List<EdsCertificateOfEmployment> getCertificatesForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit);

    List<Integer> getCertificateIdsByIds(String IDs);

    List<Integer> getCertificateIdsWithLimit(Integer start, Integer limit);

    List<EdsCertificateOfEmployment> getCertificatesByIds(String ids);

    List<Integer> getCompanyDeletedCertificatesForSolr(SolrReindexRpc solrReindex);
}
