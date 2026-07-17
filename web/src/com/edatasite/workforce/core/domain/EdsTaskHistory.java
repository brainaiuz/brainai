package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 9/14/12
 * Time: 5:37 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "taskhistory")
public class EdsTaskHistory extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "taskid")
    private Integer taskId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectid")
    private EdsProject project;

    @Column(name = "name")
    @Type(type = "text")
    private String name;

    @org.apache.solr.client.solrj.beans.Field
    @Column(name = "description", length = 10000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusid")
    private EdsReference status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priorityid")
    private EdsReference priority;

    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "dueDate")
    private Date dueDate;

    @Column(name = "estimatedtime")
    private Integer estimatedTime;

    @Column(name = "number")
    private String number;

    @Column(name = "percent")
    private Float percent;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public EdsReference getPriority() {
        return priority;
    }

    public void setPriority(EdsReference priority) {
        this.priority = priority;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Float getPercent() {
        return percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public void copyTaskToTaskHistory(EdsTask oldTask) {
        setTaskId(oldTask.getObjectID());
        setName(oldTask.getName());
        setDescription(oldTask.getDescription());
        setCreator(oldTask.getCreator());
        setStartDate(oldTask.getStartDate());
        setDueDate(oldTask.getDueDate());
        setEstimatedTime(oldTask.getEstimatedTime());
        setNumber(oldTask.getNumber());
        setPercent(oldTask.getPercent());
        setPriority(oldTask.getPriority());
        setProject(oldTask.getProject());
        setStatus(oldTask.getStatus());
    }


}
