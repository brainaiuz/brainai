package com.edatasite.workforce.gwt.ganttchart.client;

import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.ganttchart.client.enums.LoadItemType;
import com.edatasite.workforce.gwt.ganttchart.client.rpc.GanttItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskSelectItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 3/12/13
 * Time: 11:41 AM
 * To change this template use File | Settings | File Templates.
 */

public interface GanttChartServiceAsync {

	void getGanttChartTasks(Integer projectID, Integer employeeID, Date from, Date to, String sortBy, boolean showActual, LoadItemType loadType, Integer start, AsyncCallback<ListResult<TaskSingleItem>> callback);

	void getProjectEmployees(Integer projectID, AsyncCallback<ArrayList<SelectItem>> callback);

	void deleteTask(Boolean isWorkstream, Integer objectID, AsyncCallback<String> callback);

	void saveTaskDates(Integer taskID, Date startDate, Date endDate, AsyncCallback<Void> callback);

	void saveGanttChartSettings(Integer projectID, String columns, AsyncCallback<Void> callback);

	void getProjectDetailsForGanttChart(Integer projectID, AsyncCallback<GanttItem> callback);

	void saveCellValues(Integer taskID, String columnName, String value, AsyncCallback<Void> callback);

    void updateTaskDates(Integer taskID, String startDate, String endDate, AsyncCallback<Void> callback);

	void getGanttChart(GanttItem ganttItem, AsyncCallback<GanttItem> callback);

	void saveTaskDependency(Integer taskID, Integer dependencyID, String action, AsyncCallback<Void> callback);

	void saveTaskDependency(Integer taskID, TaskSelectItem[] dependencies, String action, AsyncCallback<Void> callback);

	void getGanttChartData(Integer projectId, HashSet<String> columns, AsyncCallback<ChartData> callback);

    void getGanttChartDataLeave(Integer userId, HashSet<String> columns, AsyncCallback<ChartData> callback);
}
