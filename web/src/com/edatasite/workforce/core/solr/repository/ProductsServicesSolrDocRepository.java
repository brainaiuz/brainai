package com.edatasite.workforce.core.solr.repository;

import com.edatasite.workforce.core.solr.document.ProductsServicesSolrDoc;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:30.
 */
public interface ProductsServicesSolrDocRepository extends SolrCrudRepository<ProductsServicesSolrDoc, String> {
}
