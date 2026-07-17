package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsHistoryLog;

import java.util.List;


public interface HistoryLogManager extends Manager<EdsHistoryLog> {

    List<EdsHistoryLog> getEntityHistoryLog(Integer entityId, String entityType);

}
