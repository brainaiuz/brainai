package com.edatasite.workforce.core.domain.crm.contact;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsHistory;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.CrmHistory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 08.12.2010
 * Time: 16:21:36
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "contactHistory")
public class EdsContactHistory extends EdsHistory implements CrmHistory {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contactId", updatable = false, insertable = false)
    private EdsCrmContact contact;
    @Column(name = "contactId")
    private Integer contactId;

    @Column(name = "creationTime")
    private Date creationTime;

    @Column(name = "message", length = 2000)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updaterId", updatable = false, insertable = false)
    private EdsUser updater;
    @Column(name = "updaterId")
    private Integer updaterId;

    @Override
    public Integer getEntityID() {
        return getContactId();
    }

    public EdsCrmContact getContact() {
        return contact;
    }

    public void setContact(EdsCrmContact contact) {
        this.contact = contact;
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    @Override
    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public EdsUser getUpdater() {
        return updater;
    }

    public void setUpdater(EdsUser updater) {
        this.updater = updater;
    }

    public Integer getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Integer updaterId) {
        this.updaterId = updaterId;
    }
}
