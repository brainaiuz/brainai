package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 13.11.2008
 * Time: 12:49:45
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "positiontask")
public class EdsPositionTask extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "positionId")
    private EdsPosition position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taskId")
    private EdsTask task;

    /**
     * Planned time in minnutes for
     * employee in current workstream
     */
    private Integer estimatedTime;


    public EdsTask getTask() {
        return task;
    }

    public void setTask(EdsTask task) {
        this.task = task;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public EdsPosition getPosition() {
        return position;
    }

    public void setPosition(EdsPosition position) {
        this.position = position;
    }

    public Integer getEstimatedTime() {
        return estimatedTime != null ? estimatedTime : 0;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }
}
