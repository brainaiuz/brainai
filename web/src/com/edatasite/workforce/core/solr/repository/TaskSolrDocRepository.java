package com.edatasite.workforce.core.solr.repository;

import com.edatasite.workforce.core.solr.document.TaskSolrDoc;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:32.
 */
public interface TaskSolrDocRepository extends SolrCrudRepository<TaskSolrDoc, String> {

    void deleteByTaskId(String taskId);
}
