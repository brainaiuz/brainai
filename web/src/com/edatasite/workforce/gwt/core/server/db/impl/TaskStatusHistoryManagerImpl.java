package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsTaskStatusHistory;
import com.edatasite.workforce.gwt.core.server.db.TaskStatusHistoryManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("taskStatusHistoryManager")
public class TaskStatusHistoryManagerImpl extends BaseManager<EdsTaskStatusHistory> implements TaskStatusHistoryManager {


    public TaskStatusHistoryManagerImpl() {
        super(EdsTaskStatusHistory.class);
    }

    @Override
    public List<EdsTaskStatusHistory> getTaskStatusHistories(Integer taskId) {
        return find("select tsh from EdsTaskStatusHistory tsh where tsh.task.objectID = " + taskId + " order by id desc");
    }
}
