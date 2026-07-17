package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsTaskStatusHistory;

import java.util.List;

public interface TaskStatusHistoryManager extends Manager<EdsTaskStatusHistory> {
    List<EdsTaskStatusHistory> getTaskStatusHistories(Integer taskId);
}
