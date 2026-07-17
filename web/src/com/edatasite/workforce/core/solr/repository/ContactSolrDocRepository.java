package com.edatasite.workforce.core.solr.repository;

import com.edatasite.workforce.core.solr.document.ContactSolrDoc;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author: Dilsh0d Tadjiev on 06.08.2020 22:10.
 */
public interface ContactSolrDocRepository extends SolrCrudRepository<ContactSolrDoc, String> {
}
