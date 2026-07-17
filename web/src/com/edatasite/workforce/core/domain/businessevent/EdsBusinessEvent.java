package com.edatasite.workforce.core.domain.businessevent;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsSuperUser;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * User: Abdulaziz
 * Date: Dec 14, 2009
 * Time: 12:32:50 PM
 * <p/>
 * Business events fires in cases when system automatically takes some action
 * for example sending emails to task assignees, sending email to the company created employees
 * sending activation links, company creation notification, sending sms notification,
 * project manager assignee notification, events that caused process
 */
@Entity
//@Inheritance(strategy = InheritanceType.JOINED)
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "businessevent")
public class EdsBusinessEvent extends EdsSuperUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private Integer companyId;

    private Date time;
    private String eventType;
    private String processorName;
    private Integer sourceID;
    private Integer createdByID;
    private Integer additionalSourceID;
    private boolean processed = false;
    @Column(name = "processFailed", columnDefinition = "boolean default false")
    private boolean processFailed = false;
    private Integer entityID;
    private String entityType;
    private int attempts = 0;
    private String status;
    private boolean solrIndexed = false;
    private boolean myUpdatesItemAdd = false;
    private boolean myUpdatesItemEdit = false;
    private boolean myUpdatesItemDelete = false;
    private boolean sendMail1 = false;
    private boolean sendMail2 = false;
    private boolean chatRegistr = false;
    private boolean isRbacIndexed = false;
    @Column(name = "isUpdateOrgChartCash", columnDefinition = "boolean default false")
    private boolean isUpdateOrgChartCash = false;

    @Column(name = "sendEmailNotification", columnDefinition = "boolean default true")
    private boolean sendEmailNotification = true;
    @Type(type = "text")
    @Column(name = "customstringfield")
    private String customStringField;

    @Type(type = "text")
    @Column(name = "entityIDs")
    private String entityIDs;

    @Type(type = "text")
    @Column(name = "relationIDs")
    private String relationIDs;

    private Integer relationID;
    private String relationType;
    @Column(name = "sorder", columnDefinition = "integer default 0")
    private int sorder;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Integer getSourceID() {
        return sourceID;
    }

    public void setSourceID(Integer sourceID) {
        this.sourceID = sourceID;
    }

    public Integer getCreatedByID() {
        return createdByID;
    }

    public void setCreatedByID(Integer createdByID) {
        this.createdByID = createdByID;
    }

    public Integer getAdditionalSourceID() {
        return additionalSourceID;
    }

    public void setAdditionalSourceID(Integer additionalSourceID) {
        this.additionalSourceID = additionalSourceID;
    }

    public String getProcessorName() {
        return processorName;
    }

    public void setProcessorName(String processorName) {
        this.processorName = processorName;
    }

    @Deprecated
    public boolean isProcessed() {
        return processed;
    }

    @Deprecated
    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public boolean isProcessFailed() {
        return processFailed;
    }

    public void setProcessFailed(boolean processFailed) {
        this.processFailed = processFailed;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSolrIndexed() {
        return solrIndexed;
    }

    public void setSolrIndexed(boolean solrIndexed) {
        this.solrIndexed = solrIndexed;
    }

    public boolean isMyUpdatesItemAdd() {
        return myUpdatesItemAdd;
    }

    public void setMyUpdatesItemAdd(boolean myUpdatesItemAdd) {
        this.myUpdatesItemAdd = myUpdatesItemAdd;
    }

    public boolean isMyUpdatesItemEdit() {
        return myUpdatesItemEdit;
    }

    public void setMyUpdatesItemEdit(boolean myUpdatesItemEdit) {
        this.myUpdatesItemEdit = myUpdatesItemEdit;
    }

    public boolean isMyUpdatesItemDelete() {
        return myUpdatesItemDelete;
    }

    public void setMyUpdatesItemDelete(boolean myUpdatesItemDelete) {
        this.myUpdatesItemDelete = myUpdatesItemDelete;
    }

    public boolean isSendMail1() {
        return sendMail1;
    }

    public void setSendMail1(boolean sendMail1) {
        this.sendMail1 = sendMail1;
    }

    public boolean isSendMail2() {
        return sendMail2;
    }

    public void setSendMail2(boolean sendMail2) {
        this.sendMail2 = sendMail2;
    }

    public boolean isChatRegistr() {
        return chatRegistr;
    }

    public void setChatRegistr(boolean chatRegistr) {
        this.chatRegistr = chatRegistr;
    }

    public boolean isRbacIndexed() {
        return isRbacIndexed;
    }

    public void setRbacIndexed(boolean rbacIndexed) {
        isRbacIndexed = rbacIndexed;
    }

    public String getCustomStringField() {
        return customStringField;
    }

    public void setCustomStringField(String customStringField) {
        this.customStringField = customStringField;
    }

    public boolean isSendEmailNotification() {
        return sendEmailNotification;
    }

    public void setSendEmailNotification(boolean sendEmailNotification) {
        this.sendEmailNotification = sendEmailNotification;
    }

    public Integer getRelationID() {
        return relationID;
    }

    public void setRelationID(Integer relationID) {
        this.relationID = relationID;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getEntityIDs() {
        return entityIDs;
    }

    public void setEntityIDs(String entityIDs) {
        this.entityIDs = entityIDs;
    }

    public String getRelationIDs() {
        return relationIDs;
    }

    public void setRelationIDs(String relationIDs) {
        this.relationIDs = relationIDs;
    }

    public boolean isUpdateOrgChartCash() {
        return isUpdateOrgChartCash;
    }

    public void setUpdateOrgChartCash(boolean updateOrgChartCash) {
        isUpdateOrgChartCash = updateOrgChartCash;
    }

}
