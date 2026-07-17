package com.edatasite.workforce.core.solr.repository;

import com.edatasite.workforce.core.solr.document.EmployeeSolrDoc;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:26.
 */
public interface EmployeeSolrDocRepository extends SolrCrudRepository<EmployeeSolrDoc, String> {
}
