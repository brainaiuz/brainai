package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import com.edatasite.workforce.gwt.hrms.client.rpc.PerformanceNoteItem;

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

/**
 * User: Ilhombek
 * Date: 10/24/12
 * Time: 6:07 PM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "performanceNote")
public class EdsPerformanceNote extends EdsTraceable implements ObjectHistory {

    public static final String PERFORMANCE_NOTE_PRIORITIES = "_PERFORMANCE_NOTE_PRIORITIES";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "creationTime")
    private Date creationTime;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;

    @Column(name = "name")
    private String name;

    @Column(name = "endDate")
    private Date date_end;

    @Column(name = "startDate")
    private Date date_start;

    @Column(name = "description", length = 10000)
    private String description;

    @Column(name = "isIncident")
    private Boolean isIncident;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relatedTo_id")
    private EdsUser relatedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportedBy_id")
    private EdsUser reportedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolver_id")
    private EdsUser resolver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private EdsReference status;

    @Column(name = "visibility")
    private Boolean visibility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priorityID")
    private EdsReference priority;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public void setCreator(EdsUser value) {
    }

    public Boolean getDeleted() {
        return deleted != null ? deleted : Boolean.FALSE;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public void setUpdater(EdsUser user) {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!ServerUtils.equalsString(this.name, name)) {
            addChange(CustomFormConstants.NAME);
        }
        this.name = name;
    }

    public Date getDate_end() {
        return date_end;
    }

    public void setDate_end(Date date_end) {
        this.date_end = date_end;
    }

    public Date getDate_start() {
        return date_start;
    }

    public void setDate_start(Date date_start) {
        this.date_start = date_start;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIncident() {
        return isIncident != null ? isIncident : Boolean.FALSE;
    }

    public void setIncident(Boolean incident) {
        isIncident = incident;
    }

    public EdsUser getRelatedTo() {
        return relatedTo;
    }

    public void setRelatedTo(EdsUser relatedTo) {
        if (!ServerUtils.equalsEdsObject(this.relatedTo, relatedTo)) {
            addChange(CustomFormConstants.RELATED_EMPLOYEES);
        }
        this.relatedTo = relatedTo;
    }

    public EdsUser getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(EdsUser reportedBy) {
        if (!ServerUtils.equalsEdsObject(this.reportedBy, reportedBy)) {
            addChange(CustomFormConstants.REPORTED_BY);
        }
        this.reportedBy = reportedBy;
    }

    public EdsUser getResolver() {
        return resolver;
    }

    public void setResolver(EdsUser resolver) {
        if (!ServerUtils.equalsEdsObject(this.resolver, resolver)) {
            addChange(CustomFormConstants.RESOLVER);
        }
        this.resolver = resolver;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        if (!ServerUtils.equalsReference(this.status, status)) {
            addChange(CustomFormConstants.STATUS);
        }
        this.status = status;
    }

    public Boolean getVisibility() {
        return visibility != null ? visibility : Boolean.FALSE;
    }

    public void setVisibility(Boolean visibility) {
        if (!ServerUtils.equalsBoolean(this.visibility, visibility)) {
            addChange(CustomFormConstants.VISIBILITY);
        }
        this.visibility = visibility;
    }

    public EdsReference getPriority() {
        return priority;
    }

    public void setPriority(EdsReference priority) {
        if (!ServerUtils.equalsReference(this.priority, priority)) {
            addChange(CustomFormConstants.PRIORITY);
        }
        this.priority = priority;
    }

    public PerformanceNoteItem getRPC() {
        PerformanceNoteItem item = new PerformanceNoteItem();
        //performance note(incident) ID
        item.setObjectID(getObjectID());
        //performance note(incident) name
        item.setName(getName());
        //performance note(incident) description
        item.setDescription(getDescription());
        if (getDate_start() != null) {
            item.setStartDate(new DateNonConvertable(getDate_start()));
        }
        if (getDate_end() != null) {
            item.setEndDate(new DateNonConvertable(getDate_end()));
        }
        //performance note(incident) status
        if (getStatus() != null) {
            item.setStatusID(getStatus().getObjectID());
            item.setStatusName(getStatus().getName());
            item.setStatusCode(getStatus().getCode());
        }
        if (getPriority() != null) {
            item.setPriorityID(getPriority().getObjectID());
            item.setPriorityName(getPriority().getName());
            item.setPriorityCode(getPriority().getCode());
        }
        //performance note(incident) related to employee ID
        if (getRelatedTo() != null) {
            item.setRelatedToID(getRelatedTo().getObjectID());
            item.setRelatedToName(getRelatedTo().getName());
        }
        //performance note(incident) resolver ID
        if (getResolver() != null) {
            item.setResolverID(getResolver().getObjectID());
            item.setResolverName(getResolver().getName());
        }
        //performance note(incident) reported by ID
        if (getReportedBy() != null) {
            item.setReportedByID(getReportedBy().getObjectID());
            item.setReportedByName(getReportedBy().getName());
        }
        //performance note OR incident
        item.setIncident(getIncident());
        //performance note(incident) visibility (Public or Private)
        item.setPublic(getVisibility());


        return item;
    }

    @Override
    public Object getRealValue(String fieldID) {
        if (CustomFormConstants.DESCRIPTION.equals(fieldID)) {
            return getDescription();
        } else if (CustomFormConstants.RELATED_EMPLOYEES.equals(fieldID)) {
            return getRelatedTo();
        } else if (CustomFormConstants.NAME.equals(fieldID)) {
            return getName();
        } else if (CustomFormConstants.REPORTED_BY.equals(fieldID)) {
            return getReportedBy();
        } else if (CustomFormConstants.RESOLVER.equals(fieldID)) {
            return getResolver();
        } else if (CustomFormConstants.VISIBILITY.equals(fieldID)) {
            return getVisibility() != null && getVisibility() ? "Public" : "Private";
        } else if (CustomFormConstants.STATUS.equals(fieldID)) {
            return getStatus();
        } else if (CustomFormConstants.PRIORITY.equals(fieldID)) {
            return getPriority();
        }
        return super.getRealValue(fieldID);
    }
}
