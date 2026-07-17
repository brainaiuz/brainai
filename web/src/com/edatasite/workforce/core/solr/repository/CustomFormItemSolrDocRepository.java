package com.edatasite.workforce.core.solr.repository;

import com.edatasite.workforce.core.solr.document.CustomFormItemSolrDoc;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:26.
 */
public interface CustomFormItemSolrDocRepository extends SolrCrudRepository<CustomFormItemSolrDoc, String> {
}
