package com.edatasite.workforce.core.domain.workflow;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsTelegramChat;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatListItem;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowTelegramAlert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "workflow_telegram_alerts")
public class EdsWorkflowTelegramAlert extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow")
    private EdsWorkflowRule workflow;

    private Integer telegramBotId;

    @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "workflowalert_telegramchat",
            joinColumns = {@JoinColumn(name = "workflowalert_id")},
            inverseJoinColumns = {@JoinColumn(name = "telegramchat_id")})
    private Set<EdsTelegramChat> telegramChats = new HashSet<>();

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "isworkflowactionTimeBased", columnDefinition = "boolean default false")
    private boolean workflowActionTimeBased = false;
    private String workflowActionStartTime;
    private Integer workflowActionStartTimeUnit;
    private String workflowActionStartTimeGranularity;
    private String receiverAttributes;

    public EdsWorkflowTelegramAlert() {
    }

    public EdsWorkflowTelegramAlert(Integer objectID, EdsWorkflowRule workflow, Integer telegramBotId, Set<EdsTelegramChat> telegramChats, Boolean deleted, String message, boolean workflowActionTimeBased, String workflowActionStartTime, Integer workflowActionStartTimeUnit, String workflowActionStartTimeGranularity, String receiverAttributes) {
        this.objectID = objectID;
        this.workflow = workflow;
        this.telegramBotId = telegramBotId;
        this.telegramChats = telegramChats;
        this.deleted = deleted;
        this.message = message;
        this.workflowActionTimeBased = workflowActionTimeBased;
        this.workflowActionStartTime = workflowActionStartTime;
        this.workflowActionStartTimeUnit = workflowActionStartTimeUnit;
        this.workflowActionStartTimeGranularity = workflowActionStartTimeGranularity;
        this.receiverAttributes = receiverAttributes;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public WorkflowTelegramAlert toRPC() {

        WorkflowTelegramAlert workflowTelegramAlert = new WorkflowTelegramAlert();
        workflowTelegramAlert.setObjectId(getObjectID());
        workflowTelegramAlert.setMessage(getMessage());
        if (getTelegramChats() != null && getTelegramChats().size() > 0) {
            ArrayList<TelegramChatListItem> telegramChatListItems = new ArrayList<>();
            for (EdsTelegramChat telegramChat : getTelegramChats()) {
                telegramChatListItems.add(telegramChat.getRPC());
            }
            workflowTelegramAlert.setTelegramChatListItems(telegramChatListItems);
        }
        if (getWorkflow() != null) {
            workflowTelegramAlert.setWorkflowRule(getWorkflow().getRPC(null));
            workflowTelegramAlert.setWorkflowId(getWorkflow().getObjectID());
        }
        workflowTelegramAlert.setWorkflowActionTimeBased(isWorkflowActionTimeBased());
        workflowTelegramAlert.setWorkflowActionStartTime(getWorkflowActionStartTime());
        workflowTelegramAlert.setWorkflowActionStartTimeUnit(getWorkflowActionStartTimeUnit());
        workflowTelegramAlert.setWorkflowActionStartTimeGranularity(getWorkflowActionStartTimeGranularity());
        return workflowTelegramAlert;
    }

    public EdsWorkflowRule getWorkflow() {
        return workflow;
    }

    public void setWorkflow(EdsWorkflowRule workflow) {
        this.workflow = workflow;
    }

    public Integer getTelegramBotId() {
        return telegramBotId;
    }

    public void setTelegramBotId(Integer telegramBotId) {
        this.telegramBotId = telegramBotId;
    }

    public Set<EdsTelegramChat> getTelegramChats() {
        return telegramChats;
    }

    public void setTelegramChats(Set<EdsTelegramChat> telegramChats) {
        this.telegramChats = telegramChats;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isWorkflowActionTimeBased() {
        return workflowActionTimeBased;
    }

    public void setWorkflowActionTimeBased(boolean workflowActionTimeBased) {
        this.workflowActionTimeBased = workflowActionTimeBased;
    }

    public String getWorkflowActionStartTime() {
        return workflowActionStartTime;
    }

    public void setWorkflowActionStartTime(String workflowActionStartTime) {
        this.workflowActionStartTime = workflowActionStartTime;
    }

    public Integer getWorkflowActionStartTimeUnit() {
        return workflowActionStartTimeUnit;
    }

    public void setWorkflowActionStartTimeUnit(Integer workflowActionStartTimeUnit) {
        this.workflowActionStartTimeUnit = workflowActionStartTimeUnit;
    }

    public String getWorkflowActionStartTimeGranularity() {
        return workflowActionStartTimeGranularity;
    }

    public void setWorkflowActionStartTimeGranularity(String workflowActionStartTimeGranularity) {
        this.workflowActionStartTimeGranularity = workflowActionStartTimeGranularity;
    }

    public String getReceiverAttributes() {
        return receiverAttributes;
    }

    public void setReceiverAttributes(String receiverAttributes) {
        this.receiverAttributes = receiverAttributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EdsWorkflowTelegramAlert)) return false;
        if (!super.equals(o)) return false;

        EdsWorkflowTelegramAlert that = (EdsWorkflowTelegramAlert) o;

        if (isWorkflowActionTimeBased() != that.isWorkflowActionTimeBased()) return false;
        if (getObjectID() != null ? !getObjectID().equals(that.getObjectID()) : that.getObjectID() != null)
            return false;
        if (getWorkflow() != null ? !getWorkflow().equals(that.getWorkflow()) : that.getWorkflow() != null)
            return false;
        if (getTelegramBotId() != null ? !getTelegramBotId().equals(that.getTelegramBotId()) : that.getTelegramBotId() != null)
            return false;
        if (getTelegramChats() != null ? !getTelegramChats().equals(that.getTelegramChats()) : that.getTelegramChats() != null)
            return false;
        if (getDeleted() != null ? !getDeleted().equals(that.getDeleted()) : that.getDeleted() != null) return false;
        if (getMessage() != null ? !getMessage().equals(that.getMessage()) : that.getMessage() != null) return false;
        if (getWorkflowActionStartTime() != null ? !getWorkflowActionStartTime().equals(that.getWorkflowActionStartTime()) : that.getWorkflowActionStartTime() != null)
            return false;
        if (getWorkflowActionStartTimeUnit() != null ? !getWorkflowActionStartTimeUnit().equals(that.getWorkflowActionStartTimeUnit()) : that.getWorkflowActionStartTimeUnit() != null)
            return false;
        if (getWorkflowActionStartTimeGranularity() != null ? !getWorkflowActionStartTimeGranularity().equals(that.getWorkflowActionStartTimeGranularity()) : that.getWorkflowActionStartTimeGranularity() != null)
            return false;
        if (getReceiverAttributes() != null ? !getReceiverAttributes().equals(that.getReceiverAttributes()) : that.getReceiverAttributes() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getObjectID() != null ? getObjectID().hashCode() : 0);
        result = 31 * result + (getWorkflow() != null ? getWorkflow().hashCode() : 0);
        result = 31 * result + (getTelegramBotId() != null ? getTelegramBotId().hashCode() : 0);
        result = 31 * result + (getTelegramChats() != null ? getTelegramChats().hashCode() : 0);
        result = 31 * result + (getDeleted() != null ? getDeleted().hashCode() : 0);
        result = 31 * result + (getMessage() != null ? getMessage().hashCode() : 0);
        result = 31 * result + (isWorkflowActionTimeBased() ? 1 : 0);
        result = 31 * result + (getWorkflowActionStartTime() != null ? getWorkflowActionStartTime().hashCode() : 0);
        result = 31 * result + (getWorkflowActionStartTimeUnit() != null ? getWorkflowActionStartTimeUnit().hashCode() : 0);
        result = 31 * result + (getWorkflowActionStartTimeGranularity() != null ? getWorkflowActionStartTimeGranularity().hashCode() : 0);
        result = 31 * result + (getReceiverAttributes() != null ? getReceiverAttributes().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EdsWorkflowTelegramAlert{" +
                "objectID=" + objectID +
                ", workflow=" + workflow +
                ", telegramBotId=" + telegramBotId +
                ", telegramChats=" + telegramChats +
                ", deleted=" + deleted +
                ", message='" + message + '\'' +
                ", workflowActionTimeBased=" + workflowActionTimeBased +
                ", workflowActionStartTime='" + workflowActionStartTime + '\'' +
                ", workflowActionStartTimeUnit=" + workflowActionStartTimeUnit +
                ", workflowActionStartTimeGranularity='" + workflowActionStartTimeGranularity + '\'' +
                ", receiverAttributes='" + receiverAttributes + '\'' +
                '}';
    }
}
