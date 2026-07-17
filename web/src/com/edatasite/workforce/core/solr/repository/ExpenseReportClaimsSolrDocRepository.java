package com.edatasite.workforce.core.solr.repository;

import com.edatasite.workforce.core.solr.document.ExpenseReportClaimsSolrDoc;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:27.
 */
public interface ExpenseReportClaimsSolrDocRepository extends SolrCrudRepository<ExpenseReportClaimsSolrDoc, String> {
}
