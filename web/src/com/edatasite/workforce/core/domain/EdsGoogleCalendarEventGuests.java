package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.crm.EdsEvent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Jan 29, 2011
 * Time: 12:04:14 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "googlecalendareventguests")
public class EdsGoogleCalendarEventGuests extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId")
    private EdsEvent event;

    private String email;

    private String status;

    private Boolean sendMail = false;

    private Boolean isNew = true;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public EdsEvent getEvent() {
        return event;
    }

    public void setEvent(EdsEvent event) {
        this.event = event;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean isSendMail() {
        return sendMail != null ? sendMail : true;
    }

    public void setSendMail(Boolean sendMail) {
        this.sendMail = sendMail;
    }

    public Boolean getNew() {
        return isNew != null ? isNew : false;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }
}
