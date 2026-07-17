package com.edatasite.workforce.core.domain.crm;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;

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
 * Created by Azazello on 7/10/2017.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "messageUnsubscribers")
public class EdsMessageUnsubscribers extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_id")
    private EdsCrmContact entity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "messageid")
    private EdsMailMessage mailmessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maillist_id")
    private EdsMailList mailList;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public EdsCrmContact getEntity() {
        return entity;
    }

    public void setEntity(EdsCrmContact entity) {
        this.entity = entity;
    }

    public EdsMailMessage getMailmessage() {
        return mailmessage;
    }

    public void setMailmessage(EdsMailMessage mailmessage) {
        this.mailmessage = mailmessage;
    }

    public EdsMailList getMailList() {
        return mailList;
    }

    public void setMailList(EdsMailList mailList) {
        this.mailList = mailList;
    }
}
