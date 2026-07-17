package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: developer
 * Date: 5/1/12
 * Time: 4:12 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "agendadiscussion")
public class EdsMeetingAgendaDiscussion extends EdsObject {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer objectID;

	@Type(type = "text")
	private String discussionPoints;
	@Type(type = "text")
	private String actionPoints;

	private Date startDate;
	private Date dueDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userid")
	private EdsUser assignedTo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "agendatopicid")
	private EdsMeetingAgendaTopic agendaTopic;

	public Integer getObjectID() {
		return objectID;
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public EdsUser getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(EdsUser assignedTo) {
		this.assignedTo = assignedTo;
	}

	public EdsMeetingAgendaTopic getAgendaTopic() {
		return agendaTopic;
	}

	public void setAgendaTopic(EdsMeetingAgendaTopic agendaTopic) {
		this.agendaTopic = agendaTopic;
	}
}
