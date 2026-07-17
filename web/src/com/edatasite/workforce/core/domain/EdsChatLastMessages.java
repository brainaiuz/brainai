package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: HaveANiceDay
 * Date: 03.11.11
 * Time: 14:58
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "chatlastmessages")
public class EdsChatLastMessages extends EdsObject {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer objectID;

    @Column(name="roomname")
    private String roomName;

    @Column(name="accountid")
    private Integer accountID;

    @Column(name="lastmessagedate")
    private String lastMessageDate;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public String getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(String lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }
}
