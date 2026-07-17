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
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 25.01.2010
 * Time: 13:25:10
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "messagetrack",
        indexes = {
                @Index(columnList = "messageid", name = "messagetrack_messageid_idx")
        })
public class EdsMailMessageTrack extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_id")
    private EdsCrmContact entity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "messageid")
    private EdsMailMessage message;

    private Integer openedCount = 0;

    private String IPAddress = "";

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

    public EdsMailMessage getMessage() {
        return message;
    }

    public void setMessage(EdsMailMessage message) {
        this.message = message;
    }

    public Integer getOpenedCount() {
        return openedCount;
    }

    public void setOpenedCount(Integer openedCount) {
        this.openedCount = openedCount;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }
}
