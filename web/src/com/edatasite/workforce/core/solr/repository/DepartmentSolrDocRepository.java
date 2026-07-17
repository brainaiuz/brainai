package com.edatasite.workforce.core.solr.repository;

import com.edatasite.workforce.core.solr.document.DepartmentSolrDoc;
import org.springframework.data.solr.repository.SolrCrudRepository;

public interface DepartmentSolrDocRepository extends SolrCrudRepository<DepartmentSolrDoc,String> {
}
