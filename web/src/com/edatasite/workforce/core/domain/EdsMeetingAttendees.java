package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: developer
 * Date: 4/30/12
 * Time: 12:21 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "meetingattendees")
public class EdsMeetingAttendees extends EdsObject {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer objectID;

	private boolean isAttendees;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attendeesEmployeeID")
	private EdsUser attendeesEmployee;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "meetingminutesid")
	private EdsMeetingMinutes meetingMinutes;

	public Integer getObjectID() {
		return objectID;
	}

	public boolean isAttendees() {
		return isAttendees;
	}

	public void setAttendees(boolean attendees) {
		this.isAttendees = attendees;
	}

	public EdsUser getAttendeesEmployee() {
		return attendeesEmployee;
	}

	public void setAttendeesEmployee(EdsUser attendeesEmployee) {
		this.attendeesEmployee = attendeesEmployee;
	}

	public EdsMeetingMinutes getMeetingMinutes() {
		return meetingMinutes;
	}

	public void setMeetingMinutes(EdsMeetingMinutes meetingMinutes) {
		this.meetingMinutes = meetingMinutes;
	}
}
