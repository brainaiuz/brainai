package com.edatasite.workforce.core.solr.repository;

import com.edatasite.workforce.core.solr.document.CourseScheduleSolrDoc;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author: Sardorbek Juraboev on 05.09.2023 12:39.
 */
public interface CourseScheduleSolrDocRepository extends SolrCrudRepository<CourseScheduleSolrDoc, String> {
}
