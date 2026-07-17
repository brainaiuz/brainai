package com.edatasite.workforce.core.solr.repository;

import com.edatasite.workforce.core.solr.document.SaleInvoiceSolrDoc;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:31.
 */
public interface SaleInvoiceSolrDocRepository extends SolrCrudRepository<SaleInvoiceSolrDoc, String> {

}
