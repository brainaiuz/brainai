package com.edatasite.workforce.core.domain.goal;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.DepartmentGoalEmployeeHistoryItem;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "dp_employee_metric_history")
public class EdsDepartmentGoalEmployeeMetricHistory extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "actual")
    private Double actual;

    @Column(name = "date")
    private Date date;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "creationDate")
    private Date creationDate;

    @Column(name = "comment")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee")
    private EdsUser assignee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goalAssigneeId")
    private EdsGoalAssignees goalAssignees;


    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Double getActual() {
        return actual;
    }

    public void setActual(Double actual) {
        this.actual = actual;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public EdsUser getAssignee() {
        return assignee;
    }

    public void setAssignee(EdsUser assignee) {
        this.assignee = assignee;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public EdsGoalAssignees getGoalAssignees() {
        return goalAssignees;
    }

    public void setGoalAssignees(EdsGoalAssignees goalAssignees) {
        this.goalAssignees = goalAssignees;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public DepartmentGoalEmployeeHistoryItem toRpc() {
        DepartmentGoalEmployeeHistoryItem item = new DepartmentGoalEmployeeHistoryItem();
        item.setId(objectID);
        item.setDate(date);
        item.setActual(actual);
        item.setComment(comment);
        if (assignee != null) {
            item.setEmployeeId(assignee.getObjectID());
            item.setEmployee(assignee.getFullName());
        }
        item.setCreationDate(creationDate);
        if (goalAssignees != null) {
            item.setGoalAssigneeId(goalAssignees.getObjectID());
        }
        return item;
    }
}

