package com.edatasite.workforce.core.solr.repository;

import com.edatasite.workforce.core.solr.document.ChartOfAccountSolrDoc;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:24.
 */
public interface ChartOfAccountSolrDocRepository extends SolrCrudRepository<ChartOfAccountSolrDoc, String> {
}
