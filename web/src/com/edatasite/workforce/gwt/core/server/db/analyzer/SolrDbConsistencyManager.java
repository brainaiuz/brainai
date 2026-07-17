package com.edatasite.workforce.gwt.core.server.db.analyzer;

import com.edatasite.workforce.core.domain.analyzer.EdsSolrDbConsistency;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * User: Abdulaziz
 * Date: Oct 16, 2010
 * Time: 6:12:21 PM
 */
public interface SolrDbConsistencyManager extends Manager<EdsSolrDbConsistency> {
    void updateOldInconsistency(Integer companyID, String entityType);

    List<EdsSolrDbConsistency> getCompanyInconsistiens(Integer companyid);

    List<Long> getCompanyInconsistiensCount(Integer companyid, String entryType, String status);

    List<EdsSolrDbConsistency> getCompanyInconsistiens(Integer companyid, String entity, String status, Integer start, int limit);

    void removeFixedInconsistences(Integer companyID);

    void removeFixedInconsistences();

    List<Integer> getComapanyIDsWithInconsistenct();

    void removeInconsistences(Integer companyID, String entityType);
}
