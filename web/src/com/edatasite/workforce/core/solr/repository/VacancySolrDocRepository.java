package com.edatasite.workforce.core.solr.repository;

import com.edatasite.workforce.core.solr.document.VacancySolrDoc;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:33.
 */
public interface VacancySolrDocRepository extends SolrCrudRepository<VacancySolrDoc, String> {
}
