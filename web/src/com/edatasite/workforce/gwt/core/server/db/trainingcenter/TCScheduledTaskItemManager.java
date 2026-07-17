package com.edatasite.workforce.gwt.core.server.db.trainingcenter;

import com.edatasite.workforce.core.domain.trainingcenter.EdsTCScheduledTaskItem;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/6/12
 * Time: 6:13 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TCScheduledTaskItemManager extends Manager<EdsTCScheduledTaskItem> {
    List<EdsTCScheduledTaskItem> getLastPendingScheduledTaskItems(Integer scheduledTaskID);
}
