package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsTaskHistory;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 9/14/12
 * Time: 5:56 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TaskHistoryManager extends Manager<EdsTaskHistory> {
    EdsTaskHistory getTaskHistoryByTaskId(Integer objectId);

}
