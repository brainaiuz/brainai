package com.edatasite.workforce.gwt.core.server.db.rbac.history;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsTimeSheet;
import com.edatasite.workforce.core.domain.rbac.EdsTaskRbac;
import com.edatasite.workforce.core.domain.rbac.history.EdsTaskRbacHistory;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.Date;
import java.util.List;

/**
 * User: Abdulaziz
 * Date: Feb 11, 2010
 * Time: 1:25:59 PM
 */
public interface TaskRbacHistoryManager extends Manager<EdsTaskRbacHistory> {
    EdsTaskRbacHistory createHistory(EdsTaskRbac taskIndex);

    List<EdsTaskRbacHistory> getDueTasks(EdsEmployee employee, Date startOfWeek, Date endOfWeek, ListingFilterParameter fp);

    List<EdsTimeSheet> getEmployeeTaskTimesheets(EdsTask task);
}
