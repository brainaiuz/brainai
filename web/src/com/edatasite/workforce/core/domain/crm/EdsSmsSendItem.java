package com.edatasite.workforce.core.domain.crm;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsSmsSettings;
import com.edatasite.workforce.gwt.core.client.rpc.sms.SmsSendItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Virus
 * Date: 7/18/11
 * Time: 7:48 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "smsSendItem")
public class EdsSmsSendItem extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private EdsSmsSettings provider;

    @Column(name = "entity_id")
    private Integer entityID;

    @Column(name = "userID")
    private Integer userID;

    @Column(name = "toNumber", length = 20)
    private String toNumber;

    @Column(name = "messageText", length = 1000)
    private String messageText;

    @Column(name = "sentDate")
    private Date sentDate;

    @Column(name = "isDelete", columnDefinition = "  boolean DEFAULT false ")
    private Boolean isDelete = false;

    public SmsSendItem getRPC(SmsSendItem item){
        item = item == null ? new SmsSendItem() : item;
        item.setDate(getSentDate());
        item.setToNumber(getToNumber());
        item.setMessageText(getMessageText());
        return item;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public EdsSmsSettings getProvider() {
        return provider;
    }

    public void setProvider(EdsSmsSettings provider) {
        this.provider = provider;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getToNumber() {
        return toNumber;
    }

    public void setToNumber(String toNumber) {
        this.toNumber = toNumber;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }
}
