package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 05.10.11
 * Time: 9:56
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "taskreminder")
public class EdsTaskReminder extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taskID")
    private EdsTask task;

    @Column(name = "type")
    private Integer reminderType;

    @Column(name = "minutes")
    private Integer minutes;

    public Integer getObjectID() {
        return objectID;
    }

    public EdsTask getTask() {
        return task;
    }

    public void setTask(EdsTask task) {
        this.task = task;
    }

    public Integer getReminderType() {
        return reminderType;
    }

    public void setReminderType(Integer reminderType) {
        this.reminderType = reminderType;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }
}
