package com.edatasite.workforce.core.domain.issue;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.issue.client.rpc.IssueItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * User: Anvarbek
 * Date: 11.05.2009
 * Time: 20:59:13
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "issue")
@Inheritance(strategy = InheritanceType.JOINED)
public class EdsIssue extends EdsTask {

    public static final String PUBLIC = "PUBLIC";
    public static final String PRIVATE = "PRIVATE";

    public static final String ISSUE_PRIORITY = "_ISSUE_PRIORITY";

    public static final String RELATED_TO_ISSUE = "RELATED_TO_ISSUE";

    public static final String CRITICAL = "CRITICAL";
    public static final String IS_HIGH = "IS_HIGH";
    public static final String IS_MEDIUM = "IS_MEDIUM";
    public static final String IS_LOW = "IS_LOW";

    public static final String ISSUE_STATUS = "_ISSUE_STATUS";

    public static final String _OPEN = "_OPEN";
    public static final String _UNDER_INVESTIGATION = "_UNDER_INVESTIGATION";
    public static final String _IN_PROGRESS = "_IN_PROGRESS";
    public static final String _IN_REVIEW = "_IN_REVIEW";
    public static final String _RESOLVED = "_RESOLVED";
    public static final String _CLOSED = "_CLOSED";
    public static final String _NEW = "_NEW";


    @Column(name = "access")
    private String access;   //public or private

    @Column(name = "enableTimesheet")
    private Boolean enableTimesheet; //true or false

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issueStatusid")
    private EdsReference issueStatus;

    @Column(name = "intNumber")
    private Integer intNumber;

    @Column(name = "number")
    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priorityId")
    private EdsReference priority;

    @ManyToOne
    @JoinColumn(name = "reportedById")
    private EdsUser reportedBy;

    @ManyToOne
    @JoinColumn(name = "resolver")
    private EdsEmployee resolver;

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public Boolean getEnableTimesheet() {
        return enableTimesheet != null ? enableTimesheet : Boolean.FALSE;
    }

    public void setEnableTimesheet(Boolean enableTimesheet) {
        this.enableTimesheet = enableTimesheet;
    }

    public EdsReference getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(EdsReference issueStatus) {
        this.issueStatus = issueStatus;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public EdsReference getPriority() {
        return priority;
    }

    public void setPriority(EdsReference priority) {
        this.priority = priority;
    }

    public EdsUser getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(EdsUser reportedBy) {
        this.reportedBy = reportedBy;
    }

    public EdsEmployee getResolver() {
        return resolver;
    }

    public void setResolver(EdsEmployee resolver) {
        this.resolver = resolver;
    }

    public IssueItem getRPC() {
        IssueItem issueItem = new IssueItem();
        //issue ID
        issueItem.setObjectID(getObjectID());
        //issue project
        if (getProject() != null) {
            issueItem.setProjectID(getProject().getObjectID());
            issueItem.setProjectName(getProject().getName());
        }
        //issue name
        issueItem.setName(getName());
        //issue description
        issueItem.setDescription(getDescription());
        //issue visibility (Internal or Public or Private)
        issueItem.setPublic(Constants.INTERNAL_ISSUE.equals(getAccess()) ? null : Constants.PUBLIC_ISSUE.equals(getAccess()));
        //issue period Start date
        issueItem.setStartDate(new Date(getStartDate().getTime()));
        //issue period End date
        issueItem.setEndDate(new Date(getDueDate().getTime()));
        //issue priority
        if (getPriority() != null) {
            issueItem.setPriorityID(getPriority().getObjectID());
            issueItem.setPriorityName(getPriority().getName());
            issueItem.setPriorityCode(getPriority().getCode());
        }
        //issue status
        if (getIssueStatus() != null) {
            issueItem.setStatusID(getIssueStatus().getObjectID());
            issueItem.setStatusName(getIssueStatus().getName());
            issueItem.setStatusCode(getIssueStatus().getCode());
        }
        //issue reported by
        if (getReportedBy() != null) {
            issueItem.setReportedByID(getReportedBy().getObjectID());
            issueItem.setReportedByName(getReportedBy().getName());
        }
        //issue resolver
        if (getResolver() != null) {
            issueItem.setResolverID(getResolver().getObjectID());
            issueItem.setResolverName(getResolver().getName());
        }
        //issue timeSheet enabled
        issueItem.setTimeSheetEnabled(getEnableTimesheet());
        //issue billable
        if (getEnableTimesheet()) {
            issueItem.setBillable(getBillable());
        }
        //issue created by
        issueItem.setCreatedBy(getCreator() != null ? getCreator().getName() : "N/A");
        issueItem.setCreatedID(getCreator() != null ? getCreator().getObjectID() : null);
        //issue created date
        issueItem.setCreatedFrom(getCreationTime() != null ? new Date(getCreationTime().getTime()) : null);
        //issue last updated by
        issueItem.setLastUpdatedBy(getUpdater() != null ? getUpdater().getName() : "N/A");
        //issue last updated date
        issueItem.setLastUpdatedDate(getLastUpdateTime() != null ? new Date(getLastUpdateTime().getTime()) : null);

        return issueItem;
    }
}
