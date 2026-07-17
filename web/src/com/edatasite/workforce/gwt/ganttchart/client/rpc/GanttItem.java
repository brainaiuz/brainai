package com.edatasite.workforce.gwt.ganttchart.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 25.04.14.
 */
public class GanttItem implements IsSerializable {

	private Integer projectID, employeeID;
	private String name, description;
	private Date startDate, endDate;
	private String manager, columnNames;
	private String locale, sortBy;
	private boolean showActual;
	private SelectItem[] priorities;
	private ArrayList<SelectItem> employees;
	private ArrayList<GCWorkstreamItem> subWorkstreams;
	private ArrayList<TaskSingleItem> tasks;
	private ArrayList<Integer> dayOffs;
	private Integer weekStartDay;

	public GanttItem() {

	}

	public Integer getProjectID() {
		return projectID;
	}

	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String columnNames) {
		this.columnNames = columnNames;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public SelectItem[] getPriorities() {
		return priorities;
	}

	public void setPriorities(SelectItem[] priorities) {
		this.priorities = priorities;
	}

	public ArrayList<GCWorkstreamItem> getSubWorkstreams() {
		if (subWorkstreams == null) {
			subWorkstreams = new ArrayList<>();
		}
		return subWorkstreams;
	}

	public void setSubWorkstreams(ArrayList<GCWorkstreamItem> subWorkstreams) {
		this.subWorkstreams = subWorkstreams;
	}

	public ArrayList<TaskSingleItem> getTasks() {
		if (tasks == null) {
			tasks = new ArrayList<>();
		}
		return tasks;
	}

	public void setTasks(ArrayList<TaskSingleItem> tasks) {
		this.tasks = tasks;
	}

	public Integer getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(Integer employeeID) {
		this.employeeID = employeeID;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public boolean isShowActual() {
		return showActual;
	}

	public void setShowActual(boolean showActual) {
		this.showActual = showActual;
	}

	public ArrayList<Integer> getDayOffs() {
		return dayOffs;
	}

	public void setDayOffs(ArrayList<Integer> dayOffs) {
		this.dayOffs = dayOffs;
	}

	public Integer getWeekStartDay() {
		return weekStartDay != null ? weekStartDay : 1;
	}

	public void setWeekStartDay(Integer weekStartDay) {
		this.weekStartDay = weekStartDay;
	}

	public ArrayList<SelectItem> getEmployees() {
		return employees;
	}

	public void setEmployees(ArrayList<SelectItem> employees) {
		this.employees = employees;
	}
}
