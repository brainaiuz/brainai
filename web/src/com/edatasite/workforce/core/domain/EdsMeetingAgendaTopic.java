package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
* Created with IntelliJ IDEA.
* User: developer
* Date: 4/24/12
* Time: 4:42 PM
* To change this template use File | Settings | File Templates.
*/
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "agendatopic")
public class EdsMeetingAgendaTopic extends EdsObject {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer objectID;

	private Integer number;

	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "meetingminutesid")
	private EdsMeetingMinutes meetingMinutes;

	public Integer getObjectID() {
		return objectID;
	}

	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EdsMeetingMinutes getMeetingMinutes() {
		return meetingMinutes;
	}

	public void setMeetingMinutes(EdsMeetingMinutes meetingMinutes) {
		this.meetingMinutes = meetingMinutes;
	}



}
