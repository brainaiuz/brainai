package com.edatasite.workforce.gwt.core.server.db.impl.analyzer;

import com.edatasite.workforce.core.domain.analyzer.EdsSolrDbConsistency;
import com.edatasite.workforce.gwt.core.server.db.analyzer.SolrDbConsistencyManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: Abdulaziz
 * Date: Oct 16, 2010
 * Time: 6:13:10 PM
 */
@Repository("solrDbConsistencyManager")
public class SolrDbConsistencyManagerImpl extends BaseManager<EdsSolrDbConsistency> implements SolrDbConsistencyManager {
    public SolrDbConsistencyManagerImpl() {
        super(EdsSolrDbConsistency.class);
    }

    @Override
    public void updateOldInconsistency(Integer companyID, String entityType) {
        update("UPDATE EdsSolrDbConsistency SET fixed = true WHERE companyid =? AND entityType =? AND fixed = false", companyID, entityType);
    }

    @Override
    public List<EdsSolrDbConsistency> getCompanyInconsistiens(Integer companyid) {
        return (List<EdsSolrDbConsistency>) findLimited("SELECT ed FROM EdsSolrDbConsistency ed WHERE ed.fixed = false AND ed.companyid = " + companyid, 100);
    }

    @Override
    public List<Long> getCompanyInconsistiensCount(Integer companyid, String entryType, String status) {
        return (List<Long>) find("SELECT count(id) FROM EdsSolrDbConsistency ed WHERE ed.fixed = false AND ed.companyid =? AND ed.entityType=? AND ed.status=?", companyid, entryType, status);
    }

    public List<EdsSolrDbConsistency> getCompanyInconsistiens(Integer companyid, String entityType, String status, Integer start, int limit) {
        return (List<EdsSolrDbConsistency>) findLimited("SELECT ed FROM EdsSolrDbConsistency ed WHERE ed.companyid = ? AND ed.entityType = ? AND ed.status = ? AND ed.objectID > ? AND ed.fixed = false order by ed.objectID asc", limit, companyid, entityType, status, start);
    }

    public void removeFixedInconsistences(Integer companyID) {
        updateNative("DELETE FROM " + getPublic() + ".solrdbconsistency WHERE fixed = true AND companyid = " + companyID);
    }

    public void removeFixedInconsistences() {
        updateNative("DELETE FROM " + getPublic() + ".solrdbconsistency WHERE fixed = true");
    }

    public List<Integer> getComapanyIDsWithInconsistenct() {
        return (List<Integer>) findNative("SELECT DISTINCT companyid FROM " + getPublic() + ".solrdbconsistency WHERE fixed = false");
    }

    @Override
    @Transactional
    public void removeInconsistences(Integer companyID, String entityType) {
        updateNative("DELETE FROM " + getPublic() + ".solrdbconsistency WHERE companyid =" + companyID + " AND entityType ='" + entityType + "'");
        flush();
    }
}
