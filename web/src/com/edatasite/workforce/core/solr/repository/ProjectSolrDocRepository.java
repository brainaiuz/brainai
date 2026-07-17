package com.edatasite.workforce.core.solr.repository;

import com.edatasite.workforce.core.solr.document.ProjectSolrDoc;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:30.
 */
public interface ProjectSolrDocRepository extends SolrCrudRepository<ProjectSolrDoc, String> {
}
