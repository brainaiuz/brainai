package com.edatasite.workforce.gwt.core.server.db.trainingcenter;

import com.edatasite.workforce.core.domain.trainingcenter.EdsTCScheduledTask;
import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCScheduleItem;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/6/12
 * Time: 1:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TCScheduledTaskManager extends Manager<EdsTCScheduledTask> {
    EdsTCScheduledTask getLastPendingScheduledTask();

    EdsTCScheduledTask getLastPDFGeneratedScheduledTask();

    EdsTCScheduledTask getLastZippedScheduledTask();

    List<Integer> getLocationsByScheduledTask(Integer scheduledTaskID);

    List<TCScheduleItem> getInvoiceSummaryReportData(Integer scheduledTaskID, Integer locationID);
}
