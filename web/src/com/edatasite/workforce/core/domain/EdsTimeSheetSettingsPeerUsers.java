package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * User: Ilhombek
 * Date: 20.07.2010
 * Time: 15:37:27
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "TimeSheetSettingsPeerUsers")
public class EdsTimeSheetSettingsPeerUsers extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeSheetSettingsId")
    private EdsTimeSheetSettings timeSheetSettings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private EdsUser user;

    private boolean deleted = false; 

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsTimeSheetSettings getTimeSheetSettings() {
        return timeSheetSettings;
    }

    public void setTimeSheetSettings(EdsTimeSheetSettings timeSheetSettings) {
        this.timeSheetSettings = timeSheetSettings;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
