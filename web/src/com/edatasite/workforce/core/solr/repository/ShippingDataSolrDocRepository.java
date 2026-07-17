package com.edatasite.workforce.core.solr.repository;

import com.edatasite.workforce.core.solr.document.ShippingDataSolrDoc;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:32.
 */
public interface ShippingDataSolrDocRepository extends SolrCrudRepository<ShippingDataSolrDoc, String> {
}
