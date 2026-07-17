package com.edatasite.workforce.core.solr.repository;

import com.edatasite.workforce.core.solr.document.PurchaseInvoiceSolrDoc;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:30.
 */
public interface PurchaseInvoiceSolrDocRepository extends SolrCrudRepository<PurchaseInvoiceSolrDoc, String> {
}
