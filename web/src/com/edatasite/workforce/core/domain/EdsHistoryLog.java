package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.accounting.client.rpc.LogHistoryItem;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "historyLog")
public class EdsHistoryLog extends EdsObject {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer objectID;

    @Column(name = "entity_id")
    private Integer entityID;

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "creatorID")
    private Integer creatorID;

    @Column(name = "creator")
    private String creator;

    @Column(name = "field")
    private String field;

    @Column(name = "fromStringValue", length = 10000)
    private String fromStringValue;

    @Column(name = "fromNumberValue", precision = 19, scale = 2)
    private BigDecimal fromNumberValue;

    @Column(name = "fromDateValue")
    private Date fromDateValue;

    @Column(name = "toStringValue", length = 10000)
    private String toStringValue;

    @Column(name = "toNumberValue", precision = 19, scale = 2)
    private BigDecimal toNumberValue;

    @Column(name = "toDateValue")
    private Date toDateValue;

    @Column(name = "updatedDate")
    private Date updatedDate;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public Integer getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(Integer creatorID) {
        this.creatorID = creatorID;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFromStringValue() {
        return fromStringValue;
    }

    public void setFromStringValue(String fromStringValue) {
        this.fromStringValue = fromStringValue;
    }

    public BigDecimal getFromNumberValue() {
        return fromNumberValue;
    }

    public void setFromNumberValue(BigDecimal fromNumberValue) {
        this.fromNumberValue = fromNumberValue;
    }

    public Date getFromDateValue() {
        return fromDateValue;
    }

    public void setFromDateValue(Date fromDateValue) {
        this.fromDateValue = fromDateValue;
    }

    public String getToStringValue() {
        return toStringValue;
    }

    public void setToStringValue(String toStringValue) {
        this.toStringValue = toStringValue;
    }

    public BigDecimal getToNumberValue() {
        return toNumberValue;
    }

    public void setToNumberValue(BigDecimal toNumberValue) {
        this.toNumberValue = toNumberValue;
    }

    public Date getToDateValue() {
        return toDateValue;
    }

    public void setToDateValue(Date toDateValue) {
        this.toDateValue = toDateValue;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public LogHistoryItem toRpc() {
        LogHistoryItem item = new LogHistoryItem();
        item.setEntityID(this.entityID);
        item.setEntityType(this.entityType);
        item.setUserID(this.creatorID);
        item.setCreatorID(this.creatorID);
        item.setCreator(this.creator);
        item.setUserName(this.creator);
        item.setField(this.field);
        item.setFromStringValue(this.fromStringValue);
        item.setFromNumberValue(this.fromNumberValue);
        item.setFromDateValue(this.fromDateValue);
        item.setToStringValue(this.toStringValue);
        item.setToNumberValue(this.toNumberValue);
        item.setToDateValue(this.toDateValue);
        item.setUpdatedDate(this.updatedDate);
        return item;
    }

    public EdsHistoryLog convertToDb(LogHistoryItem historyItem) {
        if (historyItem == null) {
            return null;
        }
        EdsHistoryLog log = new EdsHistoryLog();
        log.setEntityID(historyItem.getEntityID());
        log.setEntityType(historyItem.getEntityType());
        log.setCreatorID(historyItem.getCreatorID());
        log.setCreator(historyItem.getCreator());
        log.setField(historyItem.getField());
        log.setFromStringValue(historyItem.getFromStringValue());
        log.setFromNumberValue(historyItem.getFromNumberValue());
        log.setFromDateValue(historyItem.getFromDateValue());
        log.setToStringValue(historyItem.getToStringValue());
        log.setToNumberValue(historyItem.getToNumberValue());
        log.setToDateValue(historyItem.getToDateValue());
        log.setUpdatedDate(historyItem.getUpdatedDate());
        return log;
    }
}
