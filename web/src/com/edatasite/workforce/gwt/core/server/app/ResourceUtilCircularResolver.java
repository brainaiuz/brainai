package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.resourceUtil.ExportToExcelItem;
import com.edatasite.workforce.gwt.core.client.rpc.resourceUtil.ProjectTaskItem;
import com.edatasite.workforce.gwt.core.client.rpc.resourceUtil.ResourceUtilItem;
import com.edatasite.workforce.gwt.core.client.rpc.resourceUtil.TaskItem;

import java.util.Date;

/**
 * User: Ilhombek
 * Date: 6/29/12
 * Time: 5:42 PM
 */
public interface ResourceUtilCircularResolver {

	void saveResourceUtilDailyEstimatedTime(Integer employeeID, Integer taskID, boolean isChangeTaskStartTime, boolean isChangeTaskEndTime, DateNonConvertable nonConvertable, Date dailyDate, Integer lastDailyEstimatedTime);

	ResourceUtilItem getResourceUtilization(ListingFilterParameter fp);

	ProjectTaskItem[] getEmployeeProjectsResourceUtil(Integer start, String startDateString, String endDateString, ListingFilterParameter filterParameter);

	TaskItem[] getEmployeeProjectTasksResourceUtil(String startDateString, String endDateString, Integer start, ListingFilterParameter filterParameter);

    ExportToExcelItem getResourceUtilization(ListingFilterParameter fp, String startDateString, String endDateString, int daysInMonth);
}