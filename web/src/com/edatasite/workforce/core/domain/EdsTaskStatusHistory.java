package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;

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

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "taskStatusHistory")
public class EdsTaskStatusHistory extends EdsTraceable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "modifiedDate")
    private Date modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modifierId")
    private EdsEmployee modifier;

    @Column(name = "comment", length = 3000)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusId")
    private EdsReference status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taskId")
    private EdsTask task;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public EdsEmployee getModifier() {
        return modifier;
    }

    public void setModifier(EdsEmployee modifier) {
        this.modifier = modifier;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public EdsTask getTask() {
        return task;
    }

    public void setTask(EdsTask task) {
        this.task = task;
    }
}
