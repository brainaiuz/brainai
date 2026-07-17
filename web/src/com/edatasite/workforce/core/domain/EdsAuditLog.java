package com.edatasite.workforce.core.domain;

import com.edatasite.workforce.core.domain.enums.HistoryType;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryItem;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Hurshid on 3/18/2019
 */
@Document(collection = "audit_log")
public class EdsAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer objectID;

    private String companyId;

    @Indexed
    private String field;

    private String fromStringValue;

    private BigDecimal fromNumberValue;

    private Date fromDateValue;

    private String toStringValue;

    private BigDecimal toNumberValue;

    private Date toDateValue;

    private Integer entityID;

    private String entityName;

    private HistoryType historyType;

    private Integer updater;

    private String updaterName;

    private Date modificationDate;

    private Boolean isContact = false;


    public HistoryItem toRpc() {
        HistoryItem item = new HistoryItem();
        item.setEntityName(getEntityName());
        item.setField(getField());
        item.setFromStringValue(getFromStringValue());
        item.setFromNumberValue(getFromNumberValue());
        item.setFromDateValue(getFromDateValue());
        item.setToStringValue(getToStringValue());
        item.setToNumberValue(getToNumberValue());
        item.setToDateValue(getToDateValue());
        item.setUpdatedDate(getModificationDate());
        item.setUserName(getUpdaterName());
        item.setEntityType(getHistoryType() != null ? getHistoryType().getName() : "");
        return item;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
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

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public HistoryType getHistoryType() {
        return historyType;
    }

    public void setHistoryType(HistoryType historyType) {
        this.historyType = historyType;
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

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Boolean getContact() {
        return isContact;
    }

    public void setContact(Boolean contact) {
        isContact = contact;
    }
}
