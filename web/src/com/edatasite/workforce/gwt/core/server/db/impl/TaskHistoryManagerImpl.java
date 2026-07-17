package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsTaskHistory;
import com.edatasite.workforce.gwt.core.server.db.TaskHistoryManager;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 9/14/12
 * Time: 6:00 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("taskHistoryManager")
public class TaskHistoryManagerImpl extends BaseManager<EdsTaskHistory> implements TaskHistoryManager {

    public TaskHistoryManagerImpl() {
        super(EdsTaskHistory.class);
    }

    public EdsTaskHistory getTaskHistoryByTaskId(Integer objectId) {
        return (EdsTaskHistory) findSingle("select th from EdsTaskHistory th where th.taskId=?", objectId);
    }
}
