package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 26.11.2008
 * Time: 14:41:31
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "history")
public abstract class EdsHistory extends EdsSuperUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "date")
    private Date eventDate;

    @ManyToOne
    @JoinColumn(name = "eventId")
    private EdsReference event;

    @Column(name = "description", length = 10000)
    private String eventDescription;

    @Column(name = "visibility")
    private Boolean visibility; //boshqa sqllarga o'tkazilsa nullni olmasligi mumkin(Mysqlda boolean uchun null yo'q.)


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public EdsReference getEvent() {
        return event;
    }

    public void setEvent(EdsReference event) {
        this.event = event;
    }

    public Boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

}
