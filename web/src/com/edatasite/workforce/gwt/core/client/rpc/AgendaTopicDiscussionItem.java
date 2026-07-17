package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 * User: developer
 * Date: 4/25/12
 * Time: 10:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class AgendaTopicDiscussionItem implements IsSerializable {
	public static String DISCUSSION_POINTS = "discussionPoints";
	public static String ACTION_POINTS = "actionPoints";
	public static String ASSIGNED_TO = "assignedTo";
	public static String START_DATE = "startDate";
	public static String DUE_DATE = "actionPoints";

	private Integer objectID;
	private String discussionPoints;
	private String actionPoints;
	private SelectItem assignedTo;
	private SelectItem[] assignedToItems;

	private Date startDate;
	private Date dueDate;
	private Integer meetingMinutesId;



	public AgendaTopicDiscussionItem(){

	}

	public Integer getObjectID() {
		return objectID;
	}

	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}

	public String getDiscussionPoints() {
		return discussionPoints;
	}

	public void setDiscussionPoints(String discussionPoints) {
		this.discussionPoints = discussionPoints;
	}

	public String getActionPoints() {
		return actionPoints;
	}

	public void setActionPoints(String actionPoints) {
		this.actionPoints = actionPoints;
	}

	public SelectItem getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(SelectItem assignedTo) {
		this.assignedTo = assignedTo;
	}

	public SelectItem[] getAssignedToItems() {
		return assignedToItems;
	}

	public void setAssignedToItems(SelectItem[] assignedToItems) {
		this.assignedToItems = assignedToItems;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Integer getMeetingMinutesId() {
		return meetingMinutesId;
	}

	public void setMeetingMinutesId(Integer meetingMinutesId) {
		this.meetingMinutesId = meetingMinutesId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


}
