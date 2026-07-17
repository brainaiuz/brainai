package com.edatasite.workforce.gwt.core.server.db.impl.trainingcenter;

import com.edatasite.workforce.core.domain.trainingcenter.EdsTCScheduledTask;
import com.edatasite.workforce.core.domain.trainingcenter.EdsTCScheduledTaskItem;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.TCScheduledTaskItemManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/6/12
 * Time: 6:14 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("tcScheduledTaskItemManager")
public class TCScheduledTaskItemManagerImpl extends BaseManager<EdsTCScheduledTaskItem> implements TCScheduledTaskItemManager {
    public TCScheduledTaskItemManagerImpl() {
        super(EdsTCScheduledTaskItem.class);
    }

    @Override
    public List<EdsTCScheduledTaskItem> getLastPendingScheduledTaskItems(Integer scheduledTaskID) {
        return (List<EdsTCScheduledTaskItem>) findLimited("select sti from EdsTCScheduledTaskItem sti " +
                " where sti.scheduledTask.objectID = ? and sti.status = ? ORDER BY sti.objectID", 20, scheduledTaskID, EdsTCScheduledTask.STATUS_PENDING);
    }
}
