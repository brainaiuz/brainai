package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: developer
 * Date: 4/30/12
 * Time: 1:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class MeetingAttendeesItem implements IsSerializable {

	private Integer objectID;
	private boolean isAttendees;
	private Integer meetingMinutesId;
	private SelectItem absentEmployee;

	public MeetingAttendeesItem(){

	}

	public Integer getObjectID() {
		return objectID;
	}

	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}

	public boolean isAttendees() {
		return isAttendees;
	}

	public void setAttendees(boolean attendees) {
		isAttendees = attendees;
	}

	public Integer getMeetingMinutesId() {
		return meetingMinutesId;
	}

	public void setMeetingMinutesId(Integer meetingMinutesId) {
		this.meetingMinutesId = meetingMinutesId;
	}

	public SelectItem getAbsentEmployee() {
		return absentEmployee;
	}

	public void setAbsentEmployee(SelectItem absentEmployee) {
		this.absentEmployee = absentEmployee;
	}
}
