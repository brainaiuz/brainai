package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Unni
 * Date: Dec 11, 2008
 * Time: 11:42:16 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "bugReport")
public class EdsBugReport extends EdsObject {
    public static final String _BUG_PRIORITY = "_BUG_PRIORITY";
    public static final String BP_CRITICAL = "BP_CRITICAL";
    public static final String BP_HIGH = "BP_HIGH";
    public static final String BP_MEDIUM = "BP_MEDIUM";
    public static final String BP_LOW = "BP_LOW";

    public static final String _BUG_STATUS = "_BUG_STATUS";
    public static final String BS_NEW = "BS_NEW";
    public static final String BS_RESOLVED = "BS_RESOLVED";
    public static final String BS_UNDER_INVESTIGATION = "BS_UNDER_INVESTIGATION";
    public static final String BS_IN_PROGRESS = "BS_IN_PROGRESS";
    public static final String IGNORED = "IGNORED";
    public static final String DONE = "DONE";

    public static final String BYUSER = "ByUser";
    public static final String AUTOMATIC = "Automatic";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "assign_id")
    private Integer assign;

    @Column(name = "assignName")
    private String assignName;

    /**
     * The attachments in this bug. A List so we can keep order.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bug", fetch = FetchType.LAZY)
    @OrderBy(value = "objectID")
    private List<EdsBugAttachment> bugAttachments = new ArrayList<>();

    /**
     * The bugHistory in this bug. A List so we can keep order.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy(value = "updateTime ASC")
    private List<EdsBugReport> bugHistory = new ArrayList<>();

    /**
     * The comments in this bug. A List so we can keep order.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bug", fetch = FetchType.LAZY)
    @OrderBy(value = "creationDate")
    private List<EdsBugComment> comments = new ArrayList<>();

    @Column(name = "creator_id")
    private Integer creator;

    @Column(name = "creatorName")
    private String creatorName;

    @Column(name = "creationTime")
    private Date creationTime;

    @Column(name = "createdFrom")
    private String createdFrom;

    @Column(name = "company_id")
    private Integer company;

    @Column(name = "companyName")
    private String companyName;

    @Column(name = "comment")
    @Type(type = "text")
    private String comment;

    @Column(name = "description")
    @Type(type = "text")
    private String description;

    @Column(name = "email")
    private String email;

    @Column(name = "label")
    private String label;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private EdsBugReport parent;

    @JoinColumn(name = "priority")
    private String priority;

    @Column(name = "status")
    private String status;

    @Column(name = "type")
    private String type;

    @Column(name = "updateTime")
    private Date updateTime;

    @Column(name = "updater_id")
    private Integer updater;

    @Column(name = "updaterName")
    private String updaterName;

    @Column(name = "userAgent")
    private String userAgent;

    @Column(name = "subject")
    private String subject;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getAssign() {
        return assign;
    }

    public void setAssign(Integer assign) {
        this.assign = assign;
    }

    public String getAssignName() {
        return assignName;
    }

    public void setAssignName(String assignName) {
        this.assignName = assignName;
    }

    public List<EdsBugAttachment> getBugAttachments() {
        return bugAttachments;
    }

    public void addBugAttachments(EdsBugAttachment bugAttachment) {
        getBugAttachments().add(bugAttachment);
    }


    public void setBugAttachments(List<EdsBugAttachment> bugAttachments) {
        this.bugAttachments = bugAttachments;
    }

    public List<EdsBugReport> getBugHistory() {
        return bugHistory;
    }

    public void setBugHistory(List<EdsBugReport> bugHistory) {
        this.bugHistory = bugHistory;
    }

    public List<EdsBugComment> getComments() {
        return comments;
    }

    public void setComments(List<EdsBugComment> comments) {
        this.comments = comments;
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getCreatedFrom() {
        return createdFrom;
    }

    public void setCreatedFrom(String createdFrom) {
        this.createdFrom = createdFrom;
    }

    public Integer getCompany() {
        return company;
    }

    public void setCompany(Integer company) {
        this.company = company;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public EdsBugReport getParent() {
        return parent;
    }

    public void setParent(EdsBugReport parent) {
        this.parent = parent;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUpdater() {
        return updater;
    }

    public void setUpdater(Integer updater) {
        this.updater = updater;
    }

    public String getUpdaterName() {
        return updaterName;
    }

    public void setUpdaterName(String updaterName) {
        this.updaterName = updaterName;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }
}