package com.edatasite.workforce.core.domain.projectcost;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsTask;

import javax.persistence.*;
import java.util.Date;

/**
 * User: Dilsh0d
 * Date: 15-May-2010
 * Time: 12:42:44
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "projectCostItem")
public class EdsProjectCostItem extends EdsObject {

    public static final String COST_TYPE = "COST_TYPES";
    public static final String STANDART = "STANDART";
    public static final String MARKUP = "MARKUP";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectid")
    private EdsProject project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taskid")
    private EdsTask task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resourceTypeId")
    private EdsReference resourceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "costTypeId")
    private EdsReference costType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "otherCostItemsId")
    private EdsOtherCostItems otherCostItems;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resourceid")
    private EdsResource resource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resourcepoolid")
    private EdsResourcePool resourcePool;

    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "endDate")
    private Date endDate;

    @Column(name = "planned")
    private Boolean planned = true;

    @Column(name = "percent")
    private Boolean percent = false;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public EdsTask getTask() {
        return task;
    }

    public void setTask(EdsTask task) {
        this.task = task;
    }

    public EdsReference getResourceType() {
        return resourceType;
    }

    public void setResourceType(EdsReference resourceType) {
        this.resourceType = resourceType;
    }

    public EdsReference getCostType() {
        return costType;
    }

    public void setCostType(EdsReference costType) {
        this.costType = costType;
    }

    public EdsOtherCostItems getOtherCostItems() {
        return otherCostItems;
    }

    public void setOtherCostItems(EdsOtherCostItems otherCostItems) {
        this.otherCostItems = otherCostItems;
    }

    public EdsResource getResource() {
        return resource;
    }

    public void setResource(EdsResource resource) {
        this.resource = resource;
    }

    public EdsResourcePool getResourcePool() {
        return resourcePool;
    }

    public void setResourcePool(EdsResourcePool resourcePool) {
        this.resourcePool = resourcePool;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean isPlanned() {
        return planned;
    }

    public void setPlanned(Boolean planned) {
        this.planned = planned;
    }

    public Boolean isPercent() {
        return percent;
    }

    public void setPercent(Boolean percent) {
        this.percent = percent;
    }
}
