package com.edatasite.workforce.core.domain.emailfetching;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Azazello on 3/30/15.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "tracker")
public class EdsTracker extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "message_id")
    private String messageID;

    @Column(name = "tracker_id")
    private Integer trackerID;

    @Column(name = "emailSetting_id")
    private Integer emailSettingID;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public Integer getTrackerID() {
        return trackerID;
    }

    public void setTrackerID(Integer trackerID) {
        this.trackerID = trackerID;
    }

    public Integer getEmailSettingID() {
        return emailSettingID;
    }

    public void setEmailSettingID(Integer emailSettingID) {
        this.emailSettingID = emailSettingID;
    }
}
