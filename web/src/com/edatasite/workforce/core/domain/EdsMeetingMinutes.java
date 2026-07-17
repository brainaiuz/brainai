package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.customfields.EdsMeetingMinutesCustomFields;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: developer
 * Date: 4/20/12
 * Time: 5:34 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "meetingminutes")
public class EdsMeetingMinutes extends EdsObject {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer objectID;

	@Column(name = "title")
	private String title;

	@Column(name = "startDate")
	private Date startDate;

	@Column(name = "dueDate")
	private Date dueDate;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userid")
	private EdsUser calledBy;

	@Column(name = "location")
	private String location;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type")
	private EdsReference type;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectid")
    private EdsProject project;

    @Column(name = "converted_to_project")
    private Boolean convertedToProject = false;

	@Column(name="nonCompanyAttendees")
	@Type(type = "text")
	private String nonCompanyAttendees;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meetingminutescustomfieldsid", unique = true)
    private EdsMeetingMinutesCustomFields edsMeetingMinutesCustomFields;

    private Integer emailTemplateID;


	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public EdsUser getCalledBy() {
		return calledBy;
	}

	public void setCalledBy(EdsUser calledBy) {
		this.calledBy = calledBy;
	}

	public EdsReference getType() {
		return type;
	}

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public void setType(EdsReference type) {
		this.type = type;
	}

	public EdsUser getPrepairedBy() {
		return prepairedBy;
	}

	public void setPrepairedBy(EdsUser prepairedBy) {
		this.prepairedBy = prepairedBy;
	}

    public Date getNextMeetingDate() {
        return nextMeetingDate;
    }

    public void setNextMeetingDate(Date nextMeetingDate) {
        this.nextMeetingDate = nextMeetingDate;
    }

    @OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prepairedby")
	private EdsUser prepairedBy;

    @Column (name = "nextmeetingdate")
    private Date nextMeetingDate;

	@Column(name = "purpose", length = 10000)
	private String purpose;

	@Column(name = "meetingnumber")
	private String meetingNumber;

	@Column(name = "intNumber")
	private Integer intNumber;

	private Date lastUpdateTime;

	public String getMeetingNumber() {
		return meetingNumber;
	}

	public void setMeetingNumber(String meetingNumber) {
		this.meetingNumber = meetingNumber;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}


	public Integer getObjectID() {
		return objectID;
	}

	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public Integer getIntNumber() {
		return intNumber;
	}

	public void setIntNumber(Integer intNumber) {
		this.intNumber = intNumber;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

    public String getNonCompanyAttendees() {
        return nonCompanyAttendees;
    }

    public void setNonCompanyAttendees(String nonCompanyAttendees) {
        this.nonCompanyAttendees = nonCompanyAttendees;
    }

    public EdsMeetingMinutesCustomFields getEdsMeetingMinutesCustomFields() {
        return edsMeetingMinutesCustomFields;
    }

    public void setEdsMeetingMinutesCustomFields(EdsMeetingMinutesCustomFields edsMeetingMinutesCustomFields) {
        this.edsMeetingMinutesCustomFields = edsMeetingMinutesCustomFields;
    }

    public Integer getEmailTemplateID() {
        return emailTemplateID;
    }

    public void setEmailTemplateID(Integer emailTemplateID) {
        this.emailTemplateID = emailTemplateID;
    }

    public Boolean getConvertedToProject() {
        return convertedToProject;
    }

    public void setConvertedToProject(Boolean convertedToProject) {
        this.convertedToProject = convertedToProject;
    }
}
