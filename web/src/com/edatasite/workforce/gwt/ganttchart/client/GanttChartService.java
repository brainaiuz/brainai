package com.edatasite.workforce.gwt.ganttchart.client;

import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.ganttchart.client.enums.LoadItemType;
import com.edatasite.workforce.gwt.ganttchart.client.rpc.GanttItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskSelectItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 3/12/13
 * Time: 11:40 AM
 * To change this template use File | Settings | File Templates.
 */

public interface GanttChartService extends RemoteService {

    ListResult<TaskSingleItem> getGanttChartTasks(Integer projectID, Integer employeeID, Date from, Date to, String sortBy, boolean showActual, LoadItemType loadType, Integer start);
	ArrayList<SelectItem> getProjectEmployees(Integer projectID);
	String deleteTask(Boolean isWorkstream, Integer objectID);
	void saveTaskDates(Integer taskID, Date startDate, Date endDate);
	void saveGanttChartSettings(Integer projectID, String columns);
	GanttItem getProjectDetailsForGanttChart(Integer projectID);
	void saveCellValues(Integer taskID, String columnName, String value);
	void updateTaskDates(Integer taskID, String startDate, String endDate);
	GanttItem getGanttChart(GanttItem ganttItem);
	void saveTaskDependency(Integer taskID, Integer dependencyID, String action);
	void saveTaskDependency(Integer taskID, TaskSelectItem[] dependencies, String action);

	ChartData getGanttChartData(Integer projectId, HashSet<String> columns);

    ChartData getGanttChartDataLeave(Integer userId, HashSet<String> columns);

    class App {
        public static GanttChartServiceAsync get() {
			ServiceDefTarget target = GWT.create(GanttChartService.class);
			target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/ganttchart");
			return (GanttChartServiceAsync) target;
		}
	}
}
