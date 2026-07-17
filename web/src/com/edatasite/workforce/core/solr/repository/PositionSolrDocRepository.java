package com.edatasite.workforce.core.solr.repository;

import com.edatasite.workforce.core.solr.document.PositionSolrDoc;
import org.springframework.data.solr.repository.SolrCrudRepository;

public interface PositionSolrDocRepository extends SolrCrudRepository<PositionSolrDoc,String> {


}
