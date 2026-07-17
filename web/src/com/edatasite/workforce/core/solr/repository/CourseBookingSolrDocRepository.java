package com.edatasite.workforce.core.solr.repository;

import com.edatasite.workforce.core.solr.document.CourseBookingSolrDoc;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:25.
 */
public interface CourseBookingSolrDocRepository extends SolrCrudRepository<CourseBookingSolrDoc, String> {
}
