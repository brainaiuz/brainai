package com.edatasite.workforce.core.domain.goal;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.gwt.core.client.rpc.GoalAssigneeItem;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: Oct 29, 2009
 * Time: 4:25:18 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "goalassignees")
public class EdsGoalAssignees extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "weight")
    private Double weight = 0.d;

    @Column(name = "target")
    private Double target = 0.d;

    @Column(name = "actual")
    private Double actual = 0.d;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee")
    private EdsEmployee assignee;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "goalAssigneeId")
    private List<EdsDepartmentGoalEmployeeMetricHistory> dpEmployeeMetricHistory = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    private EdsGoal goal;


    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "description", length = 500)
    private String description;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getTarget() {
        return target;
    }

    public void setTarget(Double target) {
        this.target = target;
    }

    public Double getActual() {
        return actual;
    }

    public void setActual(Double actual) {
        this.actual = actual;
    }

    public EdsEmployee getAssignee() {
        return assignee;
    }

    public void setAssignee(EdsEmployee assignee) {
        this.assignee = assignee;
    }

    public EdsGoal getGoal() {
        return goal;
    }

    public void setGoal(EdsGoal goal) {
        this.goal = goal;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<EdsDepartmentGoalEmployeeMetricHistory> getDpEmployeeMetricHistory() {
        return dpEmployeeMetricHistory;
    }

    public void setDpEmployeeMetricHistory(List<EdsDepartmentGoalEmployeeMetricHistory> dpEmployeeMetricHistory) {
        this.dpEmployeeMetricHistory = dpEmployeeMetricHistory;
    }

    public GoalAssigneeItem toAssigneItem() {
        GoalAssigneeItem assignItem = new GoalAssigneeItem();
        assignItem.setObjectId(getObjectID());
        assignItem.setWeight(getWeight());
        assignItem.setId(getAssignee().getObjectID());
        assignItem.setName(assignee != null ? assignee.getName() : "");
        assignItem.setActual(getActual());
        assignItem.setTarget(getTarget());
        assignItem.setAssignee(true);
        assignItem.setDepartmentName(getAssignee().getEmployeeTeam().getTeam().getName());
        assignItem.setDepartmentId(getAssignee().getEmployeeTeam().getTeam().getObjectID());
        return assignItem;
    }
}
