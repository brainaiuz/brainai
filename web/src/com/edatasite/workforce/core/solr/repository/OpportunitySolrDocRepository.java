package com.edatasite.workforce.core.solr.repository;

import com.edatasite.workforce.core.solr.document.OpportunitySolrDoc;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:29.
 */
public interface OpportunitySolrDocRepository extends SolrCrudRepository<OpportunitySolrDoc, String> {
}
