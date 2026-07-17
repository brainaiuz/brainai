package com.edatasite.workforce.core.solr.repository;

import com.edatasite.workforce.core.solr.document.LeaveRequestSolrDoc;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:29.
 */
public interface LeaveRequestSolrDocRepository extends SolrCrudRepository<LeaveRequestSolrDoc, String> {
}
