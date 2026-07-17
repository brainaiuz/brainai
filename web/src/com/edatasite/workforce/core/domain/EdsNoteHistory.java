package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 26.08.2009
 * Time: 16:55:04
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "note")
public class EdsNoteHistory extends EdsHistory {
    public static final int PROJECT = 1;
    public static final int TASK = 2;
    public static final int CLIENT = 3;
    public static final int EMPLOYEE = 4;
    public static final int DEPARTMENT = 5;
    public static final int SUPPLIER = 6;
    public static final int PM_ISSUE = 7;
    public static final int MEETING_MINUTES = 8;
    public static final int PERSONAL_GOAL = 9;
    public static final int DEPARTMENT_GOAL = 10;
    public static final int PROJECT_GOAL = 11;
    public static final int BUSINESS_GOAL = 12;
    public static final int COMPANY_GOAL = 13;
    public static final int VACANCY = 14;
    public static final int PLACEMENT = 15;
    public static final int CRM_CONTACT = 16;
    public static final int CANDIDATE = CRM_CONTACT;
    public static final int CRM_LEAD = CRM_CONTACT;
    public static final int CRM_ACCOUNT = 17;
    public static final int CRM_CASE = 18;
    public static final int CRM_OPPORTUNITY = 19;
    public static final int CRM_CAMPAIGN = 20;
    public static final int PM_CONTRACT = 21;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commentatorId")
    private EdsUser employee;

    @Column(name = "comment")
    @Type(type = "text")
    private String comment;

    @Column(name = "subject")
    private String subject;

    @Column(name = "related_to")
    private int relatedTo = 0;

    @Column(name = "related_id")
    private Integer relatedId;

    @Column(name = "entity_id")
    private Integer entityID;

    @Column(name = "lastUpdated")
    private Date lastUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attachmentId")
    private EdsUpload attachment;


    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id")
    private Set<EdsNoteComment> noteComments = new HashSet<>();

    @Column(name = "x")
    private int x = 100;

    /**
     * The y position of the note.
     */
    @Column(name = "y")
    private int y = 100;

    /**
     * The width of the note.
     * <p/>
     * <p>
     * NOTE: The application does not currently provide the ability to resize
     * notes.
     * </p>
     */
    @Column(name = "width")
    private int width = 300;

    /**
     * The height of the note
     * <p/>
     * <p>
     * NOTE: The application does not currently provide the ability to resize
     * notes.
     * </p>
     */
    @Column(name = "height")
    private int height = 250;

    public HistoryListItem getHistoryItem() {
        return getRPC();
    }

    public EdsUser getEmployee() {
        return employee;
    }

    public void setEmployee(EdsUser employee) {
        this.employee = employee;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRelatedTo() {
        return relatedTo;
    }

    public void setRelatedTo(int relatedTo) {
        this.relatedTo = relatedTo;
    }

    public Integer getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Integer relatedId) {
        this.relatedId = relatedId;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Set<EdsNoteComment> getNoteComments() {
        return noteComments;
    }

    public void setNoteComments(Set<EdsNoteComment> noteComments) {
        this.noteComments = noteComments;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public EdsUpload getAttachment() {
        return attachment;
    }

    public void setAttachment(EdsUpload attachment) {
        this.attachment = attachment;
    }

    public static ArrayList<HistoryListItem> asHistoryListItems(List<EdsNoteHistory> notes) {
        ArrayList<HistoryListItem> historyListItems = new ArrayList<>();
        if (notes != null && notes.size() > 0) {
            for (EdsNoteHistory note : notes) {
                historyListItems.add(note.getRPC());
            }
        }
        return historyListItems;
    }

    public HistoryListItem getRPC() {
        HistoryListItem item = new HistoryListItem();
        item.setSubject(getSubject());
        item.setObjectID(getObjectID());
        item.setEventDate(new Date(getEventDate().getTime()));
        item.setEventDescription(getEventDescription());
        item.setEmployee("N/A");
        if (getEmployee() != null) {
            item.setEmployeeID(getEmployee().getObjectID());
            item.setEmployee(getEmployee().getName());
        }
        if (isSuperUser()) {
            item.setEmployee(Constants.defaultSupportName);
        }
        item.setComment(getComment());
        item.setVisibility(isVisibility());
        item.setRelatedId(getRelatedId());
        item.setEntityID(getEntityID());
        item.setRelatedToId(getRelatedTo());
        if (getAttachment() != null) {
            item.setAttachmentID(getAttachment().getObjectID());
        }
        return item;
    }

    public static int getRelatedToByEntityType(String entityType) {
        if (entityType != null && !"".equals(entityType)) {
            if (RelationItem.TYPE_CONTACT.equals(entityType) || RelationItem.TYPE_LEAD.equals(entityType) || RelationItem.TYPE_CANDIDATE.equals(entityType)) {
                return CRM_CONTACT;
            }
            if (RelationItem.TYPE_CRM_ACCOUNT.equals(entityType)) {
                return CRM_ACCOUNT;
            }
            if (RelationItem.TYPE_CASE.equals(entityType)) {
                return CRM_CASE;
            }
            if (RelationItem.TYPE_OPPORTUNITY.equals(entityType)) {
                return CRM_OPPORTUNITY;
            }
            if (CrmConstants.CAMPAIGN.equals(entityType)) {
                return CRM_CAMPAIGN;
            }
            if (RelationItem.TYPE_TASK.equals(entityType)) {
                return TASK;
            }
            if (RelationItem.TYPE_PROJECT.equals(entityType)) {
                return PROJECT;
            }
            if (RelationItem.TYPE_CONTRACT.equals(entityType)) {
                return PM_CONTRACT;
            }
            if (RelationItem.PM_ISSUE.equals(entityType)) {
                return PM_ISSUE;
            }
            if (RelationItem.TYPE_EMPLOYEE.equals(entityType)) {
                return EMPLOYEE;
            }
        }
        return 0;
    }
}
