package com.edatasite.workforce.core.solr.repository;

import com.edatasite.workforce.core.solr.document.AdditionalPaymentSolrDoc;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:23.
 */
public interface AdditionalPaymentSolrDocRepository extends SolrCrudRepository<AdditionalPaymentSolrDoc, String> {
}
