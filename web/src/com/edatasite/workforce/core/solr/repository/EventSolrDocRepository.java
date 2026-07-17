package com.edatasite.workforce.core.solr.repository;

import com.edatasite.workforce.core.solr.document.EventSolrDoc;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:27.
 */
public interface EventSolrDocRepository extends SolrCrudRepository<EventSolrDoc, String> {
}
