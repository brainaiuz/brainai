package com.edatasite.workforce.gwt.dashboard.server.app;

import com.edatasite.workforce.gwt.core.client.rpc.dashboard.InOutItem;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Xushnud
 * Date: 22.03.2010
 * Time: 18:23:27
 * To change this template use File | Settings | File Templates.
 */
public interface DashboardServiceLocal {

	InOutItem[] getInOutReport(Integer clientId, Integer projectId, Integer departmentId, Integer employeeId, Integer viewAsId,
							   String groupByName, Date t1, Date t2, boolean showDate, boolean showCheckIn, boolean showCheckOut,
							   boolean showActualIn, boolean showLeaveReq, boolean showLauchHour,
							   boolean showTimesheetHour, boolean showBudgetHour, boolean showMissingHours, boolean showFinImpact);

}
