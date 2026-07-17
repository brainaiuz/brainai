package com.edatasite.workforce.core.solr.repository;

import com.edatasite.workforce.core.solr.document.NewsSolrDoc;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:29.
 */
public interface NewsSolrDocRepository extends SolrCrudRepository<NewsSolrDoc, String> {
}
