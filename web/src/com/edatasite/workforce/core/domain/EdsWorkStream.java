package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.server.domain.HasAttachments;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.gwt.task.client.rpc.WorkStreamSelectItem;
import com.edatasite.workforce.gwt.task.client.rpc.WorkstreamAssigneeItem;
import com.edatasite.workforce.gwt.task.client.rpc.WorkstreamSingleItem;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: Anvar Akramov
 * Date: 09.11.2008
 * Time: 17:15:22
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "workstream")
public class EdsWorkStream extends EdsObject implements HasAttachments, ObjectHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    @Type(type = "text")
    private String description;

    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "percent")
    private Float percent;

    @Column(name = "estimatedtime")
    private Integer estimatedTime = 0;

    private Integer actualTime = 0;

    private Double wageAmmount = 0.0;

    private Double clientChargeAmmount = 0.0;

    private Double actualWageAmount = 0.0; //Actual Cost
    private Double actualClientChargeAmount = 0.0;

    private Double plannedWageAmount = 0.0; //Estimated Cost
    private Double plannedClientChargeAmount = 0.0;

    @Column(name = "endDate")
    private Date endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;

    @Column(name = "creationTime")
    private Date creationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updaterid")
    private EdsUser updater;

    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentwsid")
    @Where(clause = "deleted = 'false'")
    @OrderBy("startDate")
    private Set<EdsTask> tasks = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentWSid")
    private EdsWorkStream parentWS;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectid")
    private EdsProject project;

    @OneToMany(fetch = FetchType.LAZY)
    @Where(clause = "deleted = 'false'")
    @JoinColumn(name = "parentWSid")
    Set<EdsWorkStream> subWorkStreams = new HashSet<>();

    @Column(name = "number")
    private String number;

    @Column(name = "saveNumberFormula")
    private String savedNumberFormula;

    @Column(name = "intnumber")
    private Integer intNumber;

    @Column(name = "deleted")
    private Boolean deleted = false;

    private Integer taskGanttOrder;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public EdsUser getUpdater() {
        return updater;
    }

    public void setUpdater(EdsUser updater) {
        this.updater = updater;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public EdsWorkStream getParentWS() {
        return parentWS;
    }

    public void setParentWS(EdsWorkStream parentWS) {
        this.parentWS = parentWS;
    }

    public Set<EdsTask> getTasks() {
        return tasks;
    }

    public void setTasks(Set<EdsTask> tasks) {
        this.tasks = tasks;
    }

    public Set<EdsWorkStream> getSubWorkStreams() {
        return subWorkStreams;
    }

    public void setSubWorkStreams(Set<EdsWorkStream> subWorkStreams) {
        this.subWorkStreams = subWorkStreams;
    }

    public Integer getActualTime() {
        return actualTime;
    }

    public void setActualTime(Integer actualTime) {
        this.actualTime = actualTime;
    }

    public Double getWageAmmount() {
        return wageAmmount;
    }

    public void setWageAmmount(Double wageAmmount) {
        this.wageAmmount = wageAmmount;
    }

    public Double getClientChargeAmmount() {
        return clientChargeAmmount;
    }

    public void setClientChargeAmmount(Double clientChargeAmmount) {
        this.clientChargeAmmount = clientChargeAmmount;
    }

    public Double getActualWageAmount() {
        return actualWageAmount;
    }

    public void setActualWageAmount(Double actualWageAmount) {
        this.actualWageAmount = actualWageAmount;
    }

    public Double getActualClientChargeAmount() {
        return actualClientChargeAmount;
    }

    public void setActualClientChargeAmount(Double actualClientChargeAmount) {
        this.actualClientChargeAmount = actualClientChargeAmount;
    }

    public Double getPlannedWageAmount() {
        return plannedWageAmount;
    }

    public void setPlannedWageAmount(Double plannedWageAmount) {
        this.plannedWageAmount = plannedWageAmount;
    }

    public Double getPlannedClientChargeAmount() {
        return plannedClientChargeAmount;
    }

    public void setPlannedClientChargeAmount(Double plannedClientChargeAmount) {
        this.plannedClientChargeAmount = plannedClientChargeAmount;
    }

    public EdsUpload[] getAttachments() {
        return new EdsUpload[0];
    }

    public WorkStreamSelectItem createWorkStreamSelectItem() {
        WorkStreamSelectItem result = new WorkStreamSelectItem();
        result.setId(getObjectID());
        result.setName(getName());
        result.setProjectId(getProject().getObjectID());
        return result;
    }

    public WorkstreamSingleItem createWorkstreamSingleItem() {
        WorkstreamSingleItem result = new WorkstreamSingleItem();
        result.setObjectID(getObjectID());
        result.setName(getName());
        result.setDescription(getDescription());
        if (getParentWS() != null) {
            result.setParentWS(getParentWS().createWorkstreamSingleItem());
        }
        if (getSubWorkStreams() != null && getSubWorkStreams().size() > 0) {
            WorkstreamSingleItem[] subWSs = new WorkstreamSingleItem[getSubWorkStreams().size()];
            int i = 0;
            for (EdsWorkStream ws : getSubWorkStreams()) {
                subWSs[i] = ws.createWorkstreamSingleItem();
                i++;
            }
            result.setSubWorkstreams(subWSs);
        }

        if (getTasks() != null && getTasks().size() > 0) {
            TaskListItem[] tasks = new TaskListItem[getTasks().size()];
            int n = 0;
            for (EdsTask task : getTasks()) {
                tasks[n] = task.createTaskListItem();
                n++;
            }
            result.setTasks(tasks);
        }

        return result;
    }


    public void updateEstimatedTime(int diff) {
        this.estimatedTime += diff;
        if (getParentWS() != null) {
            getParentWS().updateEstimatedTime(diff);
        }
    }

    public void updateActualTime(int diff) {
        this.actualTime += diff;
        if (getParentWS() != null) {
            getParentWS().updateActualTime(diff);
        }
    }

    public void updateWageAmmount(Double diff) {
        if (wageAmmount == null) {
            wageAmmount = 0.0;
        }
        this.wageAmmount += diff;
        if (getParentWS() != null) {
            getParentWS().updateWageAmmount(diff);
        }
    }

    public void updateClientChargeAmmount(Double diff) {
        if (clientChargeAmmount == null) {
            clientChargeAmmount = 0.0;
        }
        this.clientChargeAmmount += diff;
        if (getParentWS() != null) {
            getParentWS().updateClientChargeAmmount(diff);
        }
    }

    public void updateActualWageAmmount(Double diff) {
        if (actualWageAmount == null) {
            actualWageAmount = 0.0;
        }
        this.actualWageAmount += diff;
        if (getParentWS() != null) {
            getParentWS().updateActualWageAmmount(diff);
        }
    }

    public void updateActualClientChargeAmmount(Double diff) {
        if (actualClientChargeAmount == null) {
            actualClientChargeAmount = 0.0;
        }
        this.actualClientChargeAmount += diff;
        if (getParentWS() != null) {
            getParentWS().updateActualClientChargeAmmount(diff);
        }
    }

    public void updatePlannedWageAmmount(Double diff) {
        if (plannedWageAmount == null) {
            plannedWageAmount = 0.0;
        }
        this.plannedWageAmount += diff;
        if (getParentWS() != null) {
            getParentWS().updatePlannedWageAmmount(diff);
        }
    }

    public void updatePlannedClientChargeAmmount(Double diff) {
        if (plannedClientChargeAmount == null) {
            plannedClientChargeAmount = 0.0;
        }
        this.plannedClientChargeAmount += diff;
        if (getParentWS() != null) {
            getParentWS().updatePlannedClientChargeAmmount(diff);
        }
    }

    public void updateStartDate(Date changedStartDate) {
        this.startDate = changedStartDate;

        if (getParentWS() != null) {
            getParentWS().updateStartDate(changedStartDate);
        }
    }

    public void updateEndDate(Date changedEndDate) {
        this.endDate = changedEndDate;

        if (getParentWS() != null) {
            getParentWS().updateEndDate(changedEndDate);
        }
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Float getPercent() {
        return percent != null ? percent : 0;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }

    private transient List<WorkstreamAssigneeItem> assigneeEmployee;

    public List<WorkstreamAssigneeItem> getAssigneeEmployee() {
        return assigneeEmployee;
    }

    public void setAssigneeEmployee(List<WorkstreamAssigneeItem> assigneeEmployee) {
        this.assigneeEmployee = assigneeEmployee;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSavedNumberFormula() {
        return savedNumberFormula;
    }

    public void setSavedNumberFormula(String savedNumberFormula) {
        this.savedNumberFormula = savedNumberFormula;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public Integer getTaskGanttOrder() {
        return taskGanttOrder;
    }

    public void setTaskGanttOrder(Integer taskGanttOrder) {
        this.taskGanttOrder = taskGanttOrder;
    }
}
