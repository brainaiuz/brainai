package com.edatasite.workforce.gwt.ganttchart.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 4/22/13
 * Time: 3:05 PM
 * To change this template use File | Settings | File Templates.
 */

public class GCWorkstreamItem implements IsSerializable {

	private Integer objectID;
	private String name;
	private String description;
	private Date startDate;
	private Date endDate;
	private GCWorkstreamItem parentWS;
	private Integer parentWSID;
	private String parentWSName;
	private Integer taskGanttOrder;
	private ArrayList<GCWorkstreamItem> subWorkstreams;
	private ArrayList<TaskSingleItem> tasks;

	public GCWorkstreamItem() {}

	public GCWorkstreamItem(Integer objectID, String name) {
		this.objectID = objectID;
		this.name = name;
	}

	public Integer getObjectID() {
		return objectID;
	}

	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
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

	public GCWorkstreamItem getParentWS() {
		return parentWS;
	}

	public void setParentWS(GCWorkstreamItem parentWS) {
		this.parentWS = parentWS;
	}

	public Integer getParentWSID() {
		return parentWSID;
	}

	public void setParentWSID(Integer parentWSID) {
		this.parentWSID = parentWSID;
	}

	public String getParentWSName() {
		return parentWSName;
	}

	public void setParentWSName(String parentWSName) {
		this.parentWSName = parentWSName;
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

	public Integer getTaskGanttOrder() {
		return taskGanttOrder;
	}

	public void setTaskGanttOrder(Integer taskGanttOrder) {
		this.taskGanttOrder = taskGanttOrder;
	}
}
