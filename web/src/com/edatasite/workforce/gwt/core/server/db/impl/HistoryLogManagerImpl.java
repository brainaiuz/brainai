package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsHistoryLog;
import com.edatasite.workforce.gwt.core.server.db.HistoryLogManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@Repository("historyLogManager")
public class HistoryLogManagerImpl extends BaseManager<EdsHistoryLog> implements HistoryLogManager {

    public HistoryLogManagerImpl() {
        super(EdsHistoryLog.class);
    }

    @Override
    public List<EdsHistoryLog> getEntityHistoryLog(Integer entityId, String entityType) {
        return (List<EdsHistoryLog>)findByNamedParams(
                "SELECT lh FROM EdsHistoryLog lh " +
                        "WHERE entityID = :entityId " +
                        "AND entityType = :entityType " +
                        "ORDER BY updatedDate DESC",
                Map.of("entityId", entityId, "entityType", entityType)
        );
    }
}
