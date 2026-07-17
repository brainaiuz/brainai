package com.edatasite.workforce.core.solr.repository;

import com.edatasite.workforce.core.solr.document.GroupPayrunSolrDoc;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:28.
 */
public interface GroupPayrunSolrDocRepository extends SolrCrudRepository<GroupPayrunSolrDoc, String> {
}
