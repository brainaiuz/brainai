package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsApprovableLogHistory;

import java.util.List;

public interface ApprovableLogHistoryManager extends Manager<EdsApprovableLogHistory> {
    List<EdsApprovableLogHistory> listType(String entityType);
}
