package com.edatasite.workforce.core.domain.settings;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.shared.db.EdsObject;
import javax.persistence.*;

/**
 * User: Aziz
 * Date: 08.02.12
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "wfp_notification")

public class EdsWfpNotification extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uuid", referencedColumnName = "uuid", nullable = false)
    private EdsUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "templateId")
    private EdsEmailTemplate emailTemplate;

    private String recordUUID;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public EdsEmailTemplate getEmailTemplate() {
        return emailTemplate;
    }

    public void setEmailTemplate(EdsEmailTemplate emailTemplate) {
        this.emailTemplate = emailTemplate;
    }

    public String getRecordUUID() {
        return recordUUID;
    }

    public void setRecordUUID(String recordUUID) {
        this.recordUUID = recordUUID;
    }
}
