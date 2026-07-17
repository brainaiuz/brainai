package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Aug 25, 2009
 * Time: 2:56:08 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "SickRequestComment")
public class EdsSickRequestComment extends EdsSuperUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer objectID;

	@Column(name = "creationDate")
	private Date creationDate;

	@Column(name = "text", length = 1000)
	private String text;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@JoinColumn(name = "sickRequestId")
	private EdsSickRequest sickRequest;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private EdsUser user;


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public EdsSickRequest getSickRequest() {
        return sickRequest;
    }

    public void setSickRequest(EdsSickRequest sickRequest) {
        this.sickRequest = sickRequest;
    }

    public EdsUser getUser() {
        return user;
    }

	public void setUser(EdsUser user) {
		this.user = user;
	}
}
