package com.edatasite.workforce.core.solr.repository;

import com.edatasite.workforce.core.solr.document.CertificateSolrDoc;
import org.springframework.data.solr.repository.SolrCrudRepository;

public interface CertificateSolrDocRepository extends SolrCrudRepository<CertificateSolrDoc, String> {
}
