package com.edatasite.workforce.gwt.core.server.office365.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;

import javax.persistence.*;

/**
 * Created by umidbekkarimov on 11/23/15.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "office365settings")
public class EdsOffice365Settings extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private EdsUser user;

    @Column(name = "syncCalendar", columnDefinition = "boolean DEFAULT false")
    private boolean syncCalendar = false;

    @Column(name = "taskCalendarId")
    private String taskCalendarId;

    @Column(name = "eventCalendarId")
    private String eventCalendarId;

    @Override
    public Integer getObjectID() {
        return this.objectID;
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

    public boolean isSyncCalendar() {
        return syncCalendar;
    }

    public void setSyncCalendar(boolean syncCalendar) {
        this.syncCalendar = syncCalendar;
    }

    public String getTaskCalendarId() {
        return taskCalendarId;
    }

    public void setTaskCalendarId(String taskCalendarId) {
        this.taskCalendarId = taskCalendarId;
    }

    public String getEventCalendarId() {
        return eventCalendarId;
    }

    public void setEventCalendarId(String eventCalendarId) {
        this.eventCalendarId = eventCalendarId;
    }
}
