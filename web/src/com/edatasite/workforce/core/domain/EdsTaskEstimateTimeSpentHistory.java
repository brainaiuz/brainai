package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Mar 14, 2011
 * Time: 2:35:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "TaskEstimatedTimeSpentHistory")
public class EdsTaskEstimateTimeSpentHistory extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taskid")
    private EdsTask task;

    @Column(name = "old_estimatedtime")
    private Integer oldEstimatedTime = 0;

    @Column(name = "estimatedtime")
    private Integer estimatedTime = 0;

    @Column(name = "old_timespent")
    private Integer oldTimespent = 0;

    @Column(name = "timespent")
    private Integer timespent = 0;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getOldEstimatedTime() {
        return oldEstimatedTime;
    }

    public void setOldEstimatedTime(Integer oldEstimatedTime) {
        this.oldEstimatedTime = oldEstimatedTime;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Integer getOldTimespent() {
        return oldTimespent;
    }

    public void setOldTimespent(Integer oldTimespent) {
        this.oldTimespent = oldTimespent;
    }

    public Integer getTimespent() {
        return timespent;
    }

    public void setTimespent(Integer timespent) {
        this.timespent = timespent;
    }

    public EdsTask getTask() {
        return task;
    }

    public void setTask(EdsTask task) {
        this.task = task;
    }
}
