package com.edatasite.workforce.core.domain.myupdates;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsSuperUser;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * User: Abdulaziz
 * Date: Jan 6, 2010
 * Time: 3:44:10 PM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "myupdate")
public class EdsMyUpdate extends EdsSuperUser {

    public static final String ADD = "ADD";
    public static final String EDIT = "EDIT";
    public static final String DELETE = "DELETE";
    public static final String DRAFT = "DRAFT";
    public static final String ALERT = "ALERT";
    public static final String NOTE = "NOTE";
    public static final String MESSAGE = "MESSAGE";
    public static final String STATUS_CHANGE = "STATUS_CHANGE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private Date date;
    @Index(name = "myupdate_index_eventtype")
    private String eventType;// type of event that cause this update it may be ADD, UPDATE, DELETE ......
    @Index(name = "myupdate_index_privateupdate")
    private Boolean privateUpdate = false; //
    @Index(name = "myupdate_index_userid")
    private Integer receiver;// it may be userid or specific groupid
    private Integer receiverType;// it may be user or specific group   EdsTrusteeType

    private Integer inducerID;// it may be userid or specific groupid
    private Integer inducerType;// it may be user or specific group    EdsTrusteeType

    private Integer affectedID;//it may be task, project, employee, team or other
    private Integer affectedType;//it may be entity, user, group

    private String typeCode;

    private String formId;

//    @ManyToOne(cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
//    @JoinColumn(name = "causeid")
//    private EdsMyUpdateCause cause;                                                                                                                         ˜

    private Integer companyID;
    private String itemName;
    private String statusCode;//it may be lead, opportunity, task, project or other entities status code

    private Integer relationID;
    private String relationType;

    @Column(precision = 25, scale = 5)
    private BigDecimal amount;//base currency for history

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getReceiver() {
        return receiver;
    }

    public void setReceiver(Integer receiver) {
        this.receiver = receiver;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(Integer receiverType) {
        this.receiverType = receiverType;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    //    public EdsMyUpdateCause getCause() {
//        return cause;
//    }
//
//    public void setCause(EdsMyUpdateCause cause) {
//        this.cause = cause;
//    }

    public Integer getInducerID() {
        return inducerID;
    }

    public void setInducerID(Integer inducerID) {
        this.inducerID = inducerID;
    }

    public Integer getInducerType() {
        return inducerType;
    }

    public void setInducerType(Integer inducerType) {
        this.inducerType = inducerType;
    }

    public Integer getAffectedID() {
        return affectedID;
    }

    public void setAffectedID(Integer affectedID) {
        this.affectedID = affectedID;
    }

    public Integer getAffectedType() {
        return affectedType;
    }

    public void setAffectedType(Integer affectedType) {
        this.affectedType = affectedType;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public boolean isPrivateUpdate() {
        return privateUpdate;
    }

    public void setPrivateUpdate(boolean privateUpdate) {
        this.privateUpdate = privateUpdate;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
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

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }
}
