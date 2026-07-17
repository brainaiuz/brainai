package com.edatasite.workforce.core.solr.repository;

import com.edatasite.workforce.core.solr.document.EmployeeStepSolrDoc;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:27.
 */
public interface EmployeeStepSolrDocRepository extends SolrCrudRepository<EmployeeStepSolrDoc, String> {
}
