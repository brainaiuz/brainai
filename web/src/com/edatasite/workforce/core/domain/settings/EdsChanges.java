package com.edatasite.workforce.core.domain.settings;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsSuperUser;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.enums.HistoryType;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Hurshid on 8/1/2017.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "changes")
public class EdsChanges extends EdsSuperUser {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private Integer objectID;

    @Column (name = "field")
    private String field;

    @Column (name = "fromStringValue")
    @Type(type = "text")
    private String fromStringValue;

    @Column (name = "fromNumberValue", precision = 25, scale = 5)
    private BigDecimal fromNumberValue;

    @Column (name = "fromDateValue")
    private Date fromDateValue;

    @Column (name = "toStringValue")
    @Type(type = "text")
    private String toStringValue;

    @Column (name = "toNumberValue", precision = 25, scale = 5)
    private BigDecimal toNumberValue;

    @Column (name = "toDateValue")
    private Date toDateValue;

    @Column (name = "entityID")
    private Integer entityID;

    @Column (name = "entityName")
    private String entityName;

    @Column (name = "historyType")
    @Enumerated (EnumType.STRING)
    private HistoryType historyType;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (updatable = false)
    private EdsUser updater;

    @Temporal (TemporalType.TIMESTAMP)
    @Column (nullable = false, columnDefinition = "timestamp DEFAULT current_timestamp")
    private Date modificationDate;

    @Column (name = "isContact", columnDefinition = "boolean DEFAULT false")
    private Boolean isContact = false;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
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

    public EdsUser getUpdater() {
        return updater;
    }

    public void setUpdater(EdsUser updater) {
        this.updater = updater;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Boolean isContact() {
        if (isContact == null) {
            return false;
        }
        return isContact;
    }

    public void setIsContact(Boolean isContact) {
        this.isContact = isContact;
    }
}
