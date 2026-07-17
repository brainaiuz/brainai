package com.edatasite.workforce.core.domain.crm;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

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
 * User: Aziz
 * Date: 25.01.2010
 * Time: 13:25:10
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "maillistmessage")
public class EdsMailListMessage extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maillistid")
    private EdsMailList mailList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mailmessageid")
    private EdsMailMessage mailMessage;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public EdsMailList getMailList() {
        return mailList;
    }

    public void setMailList(EdsMailList mailList) {
        this.mailList = mailList;
    }

    public EdsMailMessage getMailMessage() {
        return mailMessage;
    }

    public void setMailMessage(EdsMailMessage mailMessage) {
        this.mailMessage = mailMessage;
    }
}
